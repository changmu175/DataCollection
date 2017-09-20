/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.BufferedInputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ import java.nio.ByteBuffer;
/*   8:    */ import java.util.Arrays;
/*   9:    */ import java.util.Collections;
/*  10:    */ import java.util.Iterator;
/*  11:    */ import java.util.List;
/*  12:    */ import org.apache.poi.poifs.dev.POIFSViewable;
/*  13:    */ import org.apache.poi.poifs.property.DocumentProperty;
/*  14:    */ import org.apache.poi.util.HexDump;
/*  15:    */ 
/*  16:    */ public final class NPOIFSDocument
/*  17:    */   implements POIFSViewable
/*  18:    */ {
/*  19:    */   private DocumentProperty _property;
/*  20:    */   private NPOIFSFileSystem _filesystem;
/*  21:    */   private NPOIFSStream _stream;
/*  22:    */   private int _block_size;
/*  23:    */   
/*  24:    */   public NPOIFSDocument(DocumentNode document)
/*  25:    */     throws IOException
/*  26:    */   {
/*  27: 50 */     this((DocumentProperty)document.getProperty(), ((DirectoryNode)document.getParent()).getNFileSystem());
/*  28:    */   }
/*  29:    */   
/*  30:    */   public NPOIFSDocument(DocumentProperty property, NPOIFSFileSystem filesystem)
/*  31:    */     throws IOException
/*  32:    */   {
/*  33: 60 */     this._property = property;
/*  34: 61 */     this._filesystem = filesystem;
/*  35: 63 */     if (property.getSize() < 4096)
/*  36:    */     {
/*  37: 64 */       this._stream = new NPOIFSStream(this._filesystem.getMiniStore(), property.getStartBlock());
/*  38: 65 */       this._block_size = this._filesystem.getMiniStore().getBlockStoreBlockSize();
/*  39:    */     }
/*  40:    */     else
/*  41:    */     {
/*  42: 67 */       this._stream = new NPOIFSStream(this._filesystem, property.getStartBlock());
/*  43: 68 */       this._block_size = this._filesystem.getBlockStoreBlockSize();
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   public NPOIFSDocument(String name, NPOIFSFileSystem filesystem, InputStream stream)
/*  48:    */     throws IOException
/*  49:    */   {
/*  50: 81 */     this._filesystem = filesystem;
/*  51:    */     
/*  52:    */ 
/*  53: 84 */     int length = store(stream);
/*  54:    */     
/*  55:    */ 
/*  56: 87 */     this._property = new DocumentProperty(name, length);
/*  57: 88 */     this._property.setStartBlock(this._stream.getStartBlock());
/*  58:    */   }
/*  59:    */   
/*  60:    */   public NPOIFSDocument(String name, int size, NPOIFSFileSystem filesystem, POIFSWriterListener writer)
/*  61:    */     throws IOException
/*  62:    */   {
/*  63: 94 */     this._filesystem = filesystem;
/*  64: 96 */     if (size < 4096)
/*  65:    */     {
/*  66: 97 */       this._stream = new NPOIFSStream(filesystem.getMiniStore());
/*  67: 98 */       this._block_size = this._filesystem.getMiniStore().getBlockStoreBlockSize();
/*  68:    */     }
/*  69:    */     else
/*  70:    */     {
/*  71:100 */       this._stream = new NPOIFSStream(filesystem);
/*  72:101 */       this._block_size = this._filesystem.getBlockStoreBlockSize();
/*  73:    */     }
/*  74:104 */     OutputStream innerOs = this._stream.getOutputStream();
/*  75:105 */     DocumentOutputStream os = new DocumentOutputStream(innerOs, size);
/*  76:106 */     POIFSDocumentPath path = new POIFSDocumentPath(name.split("\\\\"));
/*  77:107 */     String docName = path.getComponent(path.length() - 1);
/*  78:108 */     POIFSWriterEvent event = new POIFSWriterEvent(os, path, docName, size);
/*  79:109 */     writer.processPOIFSWriterEvent(event);
/*  80:110 */     innerOs.close();
/*  81:    */     
/*  82:    */ 
/*  83:113 */     this._property = new DocumentProperty(name, size);
/*  84:114 */     this._property.setStartBlock(this._stream.getStartBlock());
/*  85:    */   }
/*  86:    */   
/*  87:    */   private int store(InputStream stream)
/*  88:    */     throws IOException
/*  89:    */   {
/*  90:121 */     int bigBlockSize = 4096;
/*  91:122 */     BufferedInputStream bis = new BufferedInputStream(stream, 4097);
/*  92:123 */     bis.mark(4096);
/*  93:126 */     if (bis.skip(4096L) < 4096L)
/*  94:    */     {
/*  95:127 */       this._stream = new NPOIFSStream(this._filesystem.getMiniStore());
/*  96:128 */       this._block_size = this._filesystem.getMiniStore().getBlockStoreBlockSize();
/*  97:    */     }
/*  98:    */     else
/*  99:    */     {
/* 100:130 */       this._stream = new NPOIFSStream(this._filesystem);
/* 101:131 */       this._block_size = this._filesystem.getBlockStoreBlockSize();
/* 102:    */     }
/* 103:135 */     bis.reset();
/* 104:    */     
/* 105:    */ 
/* 106:138 */     OutputStream os = this._stream.getOutputStream();
/* 107:139 */     byte[] buf = new byte[1024];
/* 108:140 */     int length = 0;
/* 109:    */     int readBytes;
/* 110:142 */     for (; (readBytes = bis.read(buf)) != -1; length += readBytes) {
/* 111:143 */       os.write(buf, 0, readBytes);
/* 112:    */     }
/* 113:147 */     int usedInBlock = length % this._block_size;
/* 114:148 */     if ((usedInBlock != 0) && (usedInBlock != this._block_size))
/* 115:    */     {
/* 116:149 */       int toBlockEnd = this._block_size - usedInBlock;
/* 117:150 */       byte[] padding = new byte[toBlockEnd];
/* 118:151 */       Arrays.fill(padding, (byte)-1);
/* 119:152 */       os.write(padding);
/* 120:    */     }
/* 121:156 */     os.close();
/* 122:157 */     return length;
/* 123:    */   }
/* 124:    */   
/* 125:    */   void free()
/* 126:    */     throws IOException
/* 127:    */   {
/* 128:164 */     this._stream.free();
/* 129:165 */     this._property.setStartBlock(-2);
/* 130:    */   }
/* 131:    */   
/* 132:    */   NPOIFSFileSystem getFileSystem()
/* 133:    */   {
/* 134:170 */     return this._filesystem;
/* 135:    */   }
/* 136:    */   
/* 137:    */   int getDocumentBlockSize()
/* 138:    */   {
/* 139:174 */     return this._block_size;
/* 140:    */   }
/* 141:    */   
/* 142:    */   Iterator<ByteBuffer> getBlockIterator()
/* 143:    */   {
/* 144:178 */     if (getSize() > 0) {
/* 145:179 */       return this._stream.getBlockIterator();
/* 146:    */     }
/* 147:181 */     List<ByteBuffer> empty = Collections.emptyList();
/* 148:182 */     return empty.iterator();
/* 149:    */   }
/* 150:    */   
/* 151:    */   public int getSize()
/* 152:    */   {
/* 153:190 */     return this._property.getSize();
/* 154:    */   }
/* 155:    */   
/* 156:    */   public void replaceContents(InputStream stream)
/* 157:    */     throws IOException
/* 158:    */   {
/* 159:194 */     free();
/* 160:195 */     int size = store(stream);
/* 161:196 */     this._property.setStartBlock(this._stream.getStartBlock());
/* 162:197 */     this._property.updateSize(size);
/* 163:    */   }
/* 164:    */   
/* 165:    */   DocumentProperty getDocumentProperty()
/* 166:    */   {
/* 167:204 */     return this._property;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public Object[] getViewableArray()
/* 171:    */   {
/* 172:213 */     String result = "<NO DATA>";
/* 173:215 */     if (getSize() > 0)
/* 174:    */     {
/* 175:217 */       byte[] data = new byte[getSize()];
/* 176:218 */       int offset = 0;
/* 177:219 */       for (ByteBuffer buffer : this._stream)
/* 178:    */       {
/* 179:220 */         int length = Math.min(this._block_size, data.length - offset);
/* 180:221 */         buffer.get(data, offset, length);
/* 181:222 */         offset += length;
/* 182:    */       }
/* 183:225 */       result = HexDump.dump(data, 0L, 0);
/* 184:    */     }
/* 185:228 */     return new String[] { result };
/* 186:    */   }
/* 187:    */   
/* 188:    */   public Iterator<Object> getViewableIterator()
/* 189:    */   {
/* 190:238 */     return Collections.emptyList().iterator();
/* 191:    */   }
/* 192:    */   
/* 193:    */   public boolean preferArray()
/* 194:    */   {
/* 195:249 */     return true;
/* 196:    */   }
/* 197:    */   
/* 198:    */   public String getShortDescription()
/* 199:    */   {
/* 200:259 */     StringBuffer buffer = new StringBuffer();
/* 201:    */     
/* 202:261 */     buffer.append("Document: \"").append(this._property.getName()).append("\"");
/* 203:262 */     buffer.append(" size = ").append(getSize());
/* 204:263 */     return buffer.toString();
/* 205:    */   }
/* 206:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.NPOIFSDocument
 * JD-Core Version:    0.7.0.1
 */