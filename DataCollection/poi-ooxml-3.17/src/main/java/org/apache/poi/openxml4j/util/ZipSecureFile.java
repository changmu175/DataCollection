/*   1:    */ package org.apache.poi.openxml4j.util;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FilterInputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.PushbackInputStream;
/*   8:    */ import java.lang.reflect.Field;
/*   9:    */ import java.security.AccessController;
/*  10:    */ import java.security.PrivilegedAction;
/*  11:    */ import java.util.zip.InflaterInputStream;
/*  12:    */ import java.util.zip.ZipEntry;
/*  13:    */ import java.util.zip.ZipException;
/*  14:    */ import java.util.zip.ZipFile;
/*  15:    */ import java.util.zip.ZipInputStream;
/*  16:    */ import org.apache.poi.util.POILogFactory;
/*  17:    */ import org.apache.poi.util.POILogger;
/*  18:    */ import org.apache.poi.util.SuppressForbidden;
/*  19:    */ 
/*  20:    */ public class ZipSecureFile
/*  21:    */   extends ZipFile
/*  22:    */ {
/*  23: 48 */   private static final POILogger LOG = POILogFactory.getLogger(ZipSecureFile.class);
/*  24: 50 */   private static double MIN_INFLATE_RATIO = 0.01D;
/*  25: 51 */   private static long MAX_ENTRY_SIZE = 4294967295L;
/*  26:    */   private static final long GRACE_ENTRY_SIZE = 102400L;
/*  27: 57 */   private static long MAX_TEXT_SIZE = 10485760L;
/*  28:    */   
/*  29:    */   public static void setMinInflateRatio(double ratio)
/*  30:    */   {
/*  31: 68 */     MIN_INFLATE_RATIO = ratio;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static double getMinInflateRatio()
/*  35:    */   {
/*  36: 79 */     return MIN_INFLATE_RATIO;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static void setMaxEntrySize(long maxEntrySize)
/*  40:    */   {
/*  41: 92 */     if ((maxEntrySize < 0L) || (maxEntrySize > 4294967295L)) {
/*  42: 93 */       throw new IllegalArgumentException("Max entry size is bounded [0-4GB], but had " + maxEntrySize);
/*  43:    */     }
/*  44: 95 */     MAX_ENTRY_SIZE = maxEntrySize;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static long getMaxEntrySize()
/*  48:    */   {
/*  49:106 */     return MAX_ENTRY_SIZE;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static void setMaxTextSize(long maxTextSize)
/*  53:    */   {
/*  54:120 */     if ((maxTextSize < 0L) || (maxTextSize > 4294967295L)) {
/*  55:121 */       throw new IllegalArgumentException("Max text size is bounded [0-4GB], but had " + maxTextSize);
/*  56:    */     }
/*  57:123 */     MAX_TEXT_SIZE = maxTextSize;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public static long getMaxTextSize()
/*  61:    */   {
/*  62:134 */     return MAX_TEXT_SIZE;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public ZipSecureFile(File file, int mode)
/*  66:    */     throws ZipException, IOException
/*  67:    */   {
/*  68:138 */     super(file, mode);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public ZipSecureFile(File file)
/*  72:    */     throws ZipException, IOException
/*  73:    */   {
/*  74:142 */     super(file);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public ZipSecureFile(String name)
/*  78:    */     throws ZipException, IOException
/*  79:    */   {
/*  80:146 */     super(name);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public InputStream getInputStream(ZipEntry entry)
/*  84:    */     throws IOException
/*  85:    */   {
/*  86:166 */     InputStream zipIS = super.getInputStream(entry);
/*  87:167 */     return addThreshold(zipIS);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public static ThresholdInputStream addThreshold(InputStream zipIS)
/*  91:    */     throws IOException
/*  92:    */   {
/*  93:    */     ThresholdInputStream newInner;
/*  94:    */     ThresholdInputStream newInner;
/*  95:172 */     if ((zipIS instanceof InflaterInputStream)) {
/*  96:173 */       newInner = (ThresholdInputStream)AccessController.doPrivileged(new PrivilegedAction()
/*  97:    */       {
/*  98:    */         @SuppressForbidden("TODO: Fix this to not use reflection (it will break in Java 9)! Better would be to wrap *before* instead of trying to insert wrapper afterwards.")
/*  99:    */         public ZipSecureFile.ThresholdInputStream run()
/* 100:    */         {
/* 101:    */           try
/* 102:    */           {
/* 103:179 */             Field f = FilterInputStream.class.getDeclaredField("in");
/* 104:180 */             f.setAccessible(true);
/* 105:181 */             InputStream oldInner = (InputStream)f.get(this.val$zipIS);
/* 106:182 */             ThresholdInputStream newInner2 = new ThresholdInputStream(oldInner, null);
/* 107:183 */             f.set(this.val$zipIS, newInner2);
/* 108:184 */             return newInner2;
/* 109:    */           }
/* 110:    */           catch (Exception ex)
/* 111:    */           {
/* 112:186 */             ZipSecureFile.LOG.log(5, new Object[] { "SecurityManager doesn't allow manipulation via reflection for zipbomb detection - continue with original input stream", ex });
/* 113:    */           }
/* 114:188 */           return null;
/* 115:    */         }
/* 116:    */       });
/* 117:    */     } else {
/* 118:193 */       newInner = null;
/* 119:    */     }
/* 120:196 */     return new ThresholdInputStream(zipIS, newInner);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public static class ThresholdInputStream
/* 124:    */     extends PushbackInputStream
/* 125:    */   {
/* 126:200 */     long counter = 0L;
/* 127:201 */     long markPos = 0L;
/* 128:    */     ThresholdInputStream cis;
/* 129:    */     
/* 130:    */     public ThresholdInputStream(InputStream is, ThresholdInputStream cis)
/* 131:    */     {
/* 132:205 */       super();
/* 133:206 */       this.cis = cis;
/* 134:    */     }
/* 135:    */     
/* 136:    */     public int read()
/* 137:    */       throws IOException
/* 138:    */     {
/* 139:211 */       int b = this.in.read();
/* 140:212 */       if (b > -1) {
/* 141:213 */         advance(1);
/* 142:    */       }
/* 143:215 */       return b;
/* 144:    */     }
/* 145:    */     
/* 146:    */     public int read(byte[] b, int off, int len)
/* 147:    */       throws IOException
/* 148:    */     {
/* 149:220 */       int cnt = this.in.read(b, off, len);
/* 150:221 */       if (cnt > -1) {
/* 151:222 */         advance(cnt);
/* 152:    */       }
/* 153:224 */       return cnt;
/* 154:    */     }
/* 155:    */     
/* 156:    */     public long skip(long n)
/* 157:    */       throws IOException
/* 158:    */     {
/* 159:229 */       long s = this.in.skip(n);
/* 160:230 */       this.counter += s;
/* 161:231 */       return s;
/* 162:    */     }
/* 163:    */     
/* 164:    */     public synchronized void reset()
/* 165:    */       throws IOException
/* 166:    */     {
/* 167:236 */       this.counter = this.markPos;
/* 168:237 */       super.reset();
/* 169:    */     }
/* 170:    */     
/* 171:    */     public void advance(int advance)
/* 172:    */       throws IOException
/* 173:    */     {
/* 174:241 */       this.counter += advance;
/* 175:244 */       if (this.counter > ZipSecureFile.MAX_ENTRY_SIZE) {
/* 176:245 */         throw new IOException("Zip bomb detected! The file would exceed the max size of the expanded data in the zip-file. This may indicates that the file is used to inflate memory usage and thus could pose a security risk. You can adjust this limit via ZipSecureFile.setMaxEntrySize() if you need to work with files which are very large. Counter: " + this.counter + ", cis.counter: " + (this.cis == null ? 0L : this.cis.counter) + "Limits: MAX_ENTRY_SIZE: " + ZipSecureFile.MAX_ENTRY_SIZE);
/* 177:    */       }
/* 178:253 */       if (this.cis == null) {
/* 179:254 */         return;
/* 180:    */       }
/* 181:258 */       if (this.counter <= 102400L) {
/* 182:259 */         return;
/* 183:    */       }
/* 184:262 */       double ratio = this.cis.counter / this.counter;
/* 185:263 */       if (ratio >= ZipSecureFile.MIN_INFLATE_RATIO) {
/* 186:264 */         return;
/* 187:    */       }
/* 188:268 */       throw new IOException("Zip bomb detected! The file would exceed the max. ratio of compressed file size to the size of the expanded data.\nThis may indicate that the file is used to inflate memory usage and thus could pose a security risk.\nYou can adjust this limit via ZipSecureFile.setMinInflateRatio() if you need to work with files which exceed this limit.\nCounter: " + this.counter + ", cis.counter: " + this.cis.counter + ", ratio: " + ratio + "\n" + "Limits: MIN_INFLATE_RATIO: " + ZipSecureFile.MIN_INFLATE_RATIO);
/* 189:    */     }
/* 190:    */     
/* 191:    */     public ZipEntry getNextEntry()
/* 192:    */       throws IOException
/* 193:    */     {
/* 194:276 */       if (!(this.in instanceof ZipInputStream)) {
/* 195:277 */         throw new UnsupportedOperationException("underlying stream is not a ZipInputStream");
/* 196:    */       }
/* 197:279 */       this.counter = 0L;
/* 198:280 */       return ((ZipInputStream)this.in).getNextEntry();
/* 199:    */     }
/* 200:    */     
/* 201:    */     public void closeEntry()
/* 202:    */       throws IOException
/* 203:    */     {
/* 204:284 */       if (!(this.in instanceof ZipInputStream)) {
/* 205:285 */         throw new UnsupportedOperationException("underlying stream is not a ZipInputStream");
/* 206:    */       }
/* 207:287 */       this.counter = 0L;
/* 208:288 */       ((ZipInputStream)this.in).closeEntry();
/* 209:    */     }
/* 210:    */     
/* 211:    */     public void unread(int b)
/* 212:    */       throws IOException
/* 213:    */     {
/* 214:293 */       if (!(this.in instanceof PushbackInputStream)) {
/* 215:294 */         throw new UnsupportedOperationException("underlying stream is not a PushbackInputStream");
/* 216:    */       }
/* 217:296 */       if (--this.counter < 0L) {
/* 218:297 */         this.counter = 0L;
/* 219:    */       }
/* 220:299 */       ((PushbackInputStream)this.in).unread(b);
/* 221:    */     }
/* 222:    */     
/* 223:    */     public void unread(byte[] b, int off, int len)
/* 224:    */       throws IOException
/* 225:    */     {
/* 226:304 */       if (!(this.in instanceof PushbackInputStream)) {
/* 227:305 */         throw new UnsupportedOperationException("underlying stream is not a PushbackInputStream");
/* 228:    */       }
/* 229:307 */       this.counter -= len;
/* 230:308 */       if (--this.counter < 0L) {
/* 231:309 */         this.counter = 0L;
/* 232:    */       }
/* 233:311 */       ((PushbackInputStream)this.in).unread(b, off, len);
/* 234:    */     }
/* 235:    */     
/* 236:    */     @SuppressForbidden("just delegating")
/* 237:    */     public int available()
/* 238:    */       throws IOException
/* 239:    */     {
/* 240:317 */       return this.in.available();
/* 241:    */     }
/* 242:    */     
/* 243:    */     public boolean markSupported()
/* 244:    */     {
/* 245:322 */       return this.in.markSupported();
/* 246:    */     }
/* 247:    */     
/* 248:    */     public synchronized void mark(int readlimit)
/* 249:    */     {
/* 250:327 */       this.markPos = this.counter;
/* 251:328 */       this.in.mark(readlimit);
/* 252:    */     }
/* 253:    */   }
/* 254:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.openxml4j.util.ZipSecureFile

 * JD-Core Version:    0.7.0.1

 */