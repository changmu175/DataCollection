/*   1:    */ package org.apache.poi.poifs.nio;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileNotFoundException;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ import java.io.RandomAccessFile;
/*   8:    */ import java.lang.reflect.Method;
/*   9:    */ import java.nio.ByteBuffer;
/*  10:    */ import java.nio.channels.Channels;
/*  11:    */ import java.nio.channels.FileChannel;
/*  12:    */ import java.nio.channels.FileChannel.MapMode;
/*  13:    */ import java.nio.channels.WritableByteChannel;
/*  14:    */ import java.security.AccessController;
/*  15:    */ import java.security.PrivilegedAction;
/*  16:    */ import java.util.ArrayList;
/*  17:    */ import java.util.List;
/*  18:    */ import org.apache.poi.util.IOUtils;
/*  19:    */ import org.apache.poi.util.POILogFactory;
/*  20:    */ import org.apache.poi.util.POILogger;
/*  21:    */ import org.apache.poi.util.SuppressForbidden;
/*  22:    */ 
/*  23:    */ public class FileBackedDataSource
/*  24:    */   extends DataSource
/*  25:    */ {
/*  26: 44 */   private static final POILogger logger = POILogFactory.getLogger(FileBackedDataSource.class);
/*  27:    */   private FileChannel channel;
/*  28:    */   private boolean writable;
/*  29:    */   private RandomAccessFile srcFile;
/*  30: 57 */   private List<ByteBuffer> buffersToClean = new ArrayList();
/*  31:    */   
/*  32:    */   public FileBackedDataSource(File file)
/*  33:    */     throws FileNotFoundException
/*  34:    */   {
/*  35: 60 */     this(newSrcFile(file, "r"), true);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public FileBackedDataSource(File file, boolean readOnly)
/*  39:    */     throws FileNotFoundException
/*  40:    */   {
/*  41: 64 */     this(newSrcFile(file, readOnly ? "r" : "rw"), readOnly);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public FileBackedDataSource(RandomAccessFile srcFile, boolean readOnly)
/*  45:    */   {
/*  46: 68 */     this(srcFile.getChannel(), readOnly);
/*  47: 69 */     this.srcFile = srcFile;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public FileBackedDataSource(FileChannel channel, boolean readOnly)
/*  51:    */   {
/*  52: 73 */     this.channel = channel;
/*  53: 74 */     this.writable = (!readOnly);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public boolean isWriteable()
/*  57:    */   {
/*  58: 78 */     return this.writable;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public FileChannel getChannel()
/*  62:    */   {
/*  63: 82 */     return this.channel;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public ByteBuffer read(int length, long position)
/*  67:    */     throws IOException
/*  68:    */   {
/*  69: 87 */     if (position >= size()) {
/*  70: 88 */       throw new IndexOutOfBoundsException("Position " + position + " past the end of the file");
/*  71:    */     }
/*  72:    */     ByteBuffer dst;
/*  73: 98 */     if (this.writable)
/*  74:    */     {
/*  75: 99 */       ByteBuffer dst = this.channel.map(MapMode.READ_WRITE, position, length);
/*  76:    */       
/*  77:    */ 
/*  78:102 */       this.buffersToClean.add(dst);
/*  79:    */     }
/*  80:    */     else
/*  81:    */     {
/*  82:105 */       this.channel.position(position);
/*  83:106 */       dst = ByteBuffer.allocate(length);
/*  84:    */       
/*  85:    */ 
/*  86:109 */       int worked = IOUtils.readFully(this.channel, dst);
/*  87:110 */       if (worked == -1) {
/*  88:111 */         throw new IndexOutOfBoundsException("Position " + position + " past the end of the file");
/*  89:    */       }
/*  90:    */     }
/*  91:116 */     dst.position(0);
/*  92:    */     
/*  93:    */ 
/*  94:119 */     return dst;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void write(ByteBuffer src, long position)
/*  98:    */     throws IOException
/*  99:    */   {
/* 100:124 */     this.channel.write(src, position);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void copyTo(OutputStream stream)
/* 104:    */     throws IOException
/* 105:    */   {
/* 106:130 */     WritableByteChannel out = Channels.newChannel(stream);
/* 107:    */     
/* 108:132 */     this.channel.transferTo(0L, this.channel.size(), out);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public long size()
/* 112:    */     throws IOException
/* 113:    */   {
/* 114:137 */     return this.channel.size();
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void close()
/* 118:    */     throws IOException
/* 119:    */   {
/* 120:144 */     for (ByteBuffer buffer : this.buffersToClean) {
/* 121:145 */       unmap(buffer);
/* 122:    */     }
/* 123:147 */     this.buffersToClean.clear();
/* 124:149 */     if (this.srcFile != null) {
/* 125:151 */       this.srcFile.close();
/* 126:    */     } else {
/* 127:153 */       this.channel.close();
/* 128:    */     }
/* 129:    */   }
/* 130:    */   
/* 131:    */   private static RandomAccessFile newSrcFile(File file, String mode)
/* 132:    */     throws FileNotFoundException
/* 133:    */   {
/* 134:158 */     if (!file.exists()) {
/* 135:159 */       throw new FileNotFoundException(file.toString());
/* 136:    */     }
/* 137:161 */     return new RandomAccessFile(file, mode);
/* 138:    */   }
/* 139:    */   
/* 140:    */   private static void unmap(ByteBuffer buffer)
/* 141:    */   {
/* 142:169 */     if (buffer.getClass().getName().endsWith("HeapByteBuffer")) {
/* 143:170 */       return;
/* 144:    */     }
/* 145:173 */     AccessController.doPrivileged(new PrivilegedAction()
/* 146:    */     {
/* 147:    */       @SuppressForbidden("Java 9 Jigsaw whitelists access to sun.misc.Cleaner, so setAccessible works")
/* 148:    */       public Void run()
/* 149:    */       {
/* 150:    */         try
/* 151:    */         {
/* 152:178 */           Method getCleanerMethod = this.val$buffer.getClass().getMethod("cleaner", new Class[0]);
/* 153:179 */           getCleanerMethod.setAccessible(true);
/* 154:180 */           Object cleaner = getCleanerMethod.invoke(this.val$buffer, new Object[0]);
/* 155:181 */           if (cleaner != null) {
/* 156:182 */             cleaner.getClass().getMethod("clean", new Class[0]).invoke(cleaner, new Object[0]);
/* 157:    */           }
/* 158:    */         }
/* 159:    */         catch (Exception e)
/* 160:    */         {
/* 161:185 */           FileBackedDataSource.logger.log(5, new Object[] { "Unable to unmap memory mapped ByteBuffer.", e });
/* 162:    */         }
/* 163:187 */         return null;
/* 164:    */       }
/* 165:    */     });
/* 166:    */   }
/* 167:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.poifs.nio.FileBackedDataSource

 * JD-Core Version:    0.7.0.1

 */