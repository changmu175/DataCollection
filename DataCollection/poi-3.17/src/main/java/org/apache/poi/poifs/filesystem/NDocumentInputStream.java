/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.nio.ByteBuffer;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import org.apache.poi.poifs.property.DocumentProperty;
/*   7:    */ import org.apache.poi.util.LittleEndian;
/*   8:    */ 
/*   9:    */ public final class NDocumentInputStream
/*  10:    */   extends DocumentInputStream
/*  11:    */ {
/*  12:    */   private int _current_offset;
/*  13:    */   private int _current_block_count;
/*  14:    */   private int _marked_offset;
/*  15:    */   private int _marked_offset_count;
/*  16:    */   private final int _document_size;
/*  17:    */   private boolean _closed;
/*  18:    */   private final NPOIFSDocument _document;
/*  19:    */   private Iterator<ByteBuffer> _data;
/*  20:    */   private ByteBuffer _buffer;
/*  21:    */   
/*  22:    */   public NDocumentInputStream(DocumentEntry document)
/*  23:    */     throws IOException
/*  24:    */   {
/*  25: 63 */     if (!(document instanceof DocumentNode)) {
/*  26: 64 */       throw new IOException("Cannot open internal document storage, " + document + " not a Document Node");
/*  27:    */     }
/*  28: 66 */     this._current_offset = 0;
/*  29: 67 */     this._current_block_count = 0;
/*  30: 68 */     this._marked_offset = 0;
/*  31: 69 */     this._marked_offset_count = 0;
/*  32: 70 */     this._document_size = document.getSize();
/*  33: 71 */     this._closed = false;
/*  34: 73 */     if (this._document_size < 0) {}
/*  35: 76 */     DocumentNode doc = (DocumentNode)document;
/*  36: 77 */     DocumentProperty property = (DocumentProperty)doc.getProperty();
/*  37: 78 */     this._document = new NPOIFSDocument(property, ((DirectoryNode)doc.getParent()).getNFileSystem());
/*  38:    */     
/*  39:    */ 
/*  40:    */ 
/*  41: 82 */     this._data = this._document.getBlockIterator();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public NDocumentInputStream(NPOIFSDocument document)
/*  45:    */   {
/*  46: 91 */     this._current_offset = 0;
/*  47: 92 */     this._current_block_count = 0;
/*  48: 93 */     this._marked_offset = 0;
/*  49: 94 */     this._marked_offset_count = 0;
/*  50: 95 */     this._document_size = document.getSize();
/*  51: 96 */     this._closed = false;
/*  52: 97 */     this._document = document;
/*  53: 98 */     this._data = this._document.getBlockIterator();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public int available()
/*  57:    */   {
/*  58:103 */     return remainingBytes();
/*  59:    */   }
/*  60:    */   
/*  61:    */   private int remainingBytes()
/*  62:    */   {
/*  63:112 */     if (this._closed) {
/*  64:113 */       throw new IllegalStateException("cannot perform requested operation on a closed stream");
/*  65:    */     }
/*  66:115 */     return this._document_size - this._current_offset;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void close()
/*  70:    */   {
/*  71:120 */     this._closed = true;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void mark(int ignoredReadlimit)
/*  75:    */   {
/*  76:125 */     this._marked_offset = this._current_offset;
/*  77:126 */     this._marked_offset_count = Math.max(0, this._current_block_count - 1);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public int read()
/*  81:    */     throws IOException
/*  82:    */   {
/*  83:131 */     dieIfClosed();
/*  84:132 */     if (atEOD()) {
/*  85:133 */       return -1;
/*  86:    */     }
/*  87:135 */     byte[] b = new byte[1];
/*  88:136 */     int result = read(b, 0, 1);
/*  89:137 */     if (result >= 0)
/*  90:    */     {
/*  91:138 */       if (b[0] < 0) {
/*  92:139 */         return b[0] + 256;
/*  93:    */       }
/*  94:141 */       return b[0];
/*  95:    */     }
/*  96:143 */     return result;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public int read(byte[] b, int off, int len)
/* 100:    */     throws IOException
/* 101:    */   {
/* 102:148 */     dieIfClosed();
/* 103:149 */     if (b == null) {
/* 104:150 */       throw new IllegalArgumentException("buffer must not be null");
/* 105:    */     }
/* 106:152 */     if ((off < 0) || (len < 0) || (b.length < off + len)) {
/* 107:153 */       throw new IndexOutOfBoundsException("can't read past buffer boundaries");
/* 108:    */     }
/* 109:155 */     if (len == 0) {
/* 110:156 */       return 0;
/* 111:    */     }
/* 112:158 */     if (atEOD()) {
/* 113:159 */       return -1;
/* 114:    */     }
/* 115:161 */     int limit = Math.min(remainingBytes(), len);
/* 116:162 */     readFully(b, off, limit);
/* 117:163 */     return limit;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void reset()
/* 121:    */   {
/* 122:174 */     if ((this._marked_offset == 0) && (this._marked_offset_count == 0))
/* 123:    */     {
/* 124:175 */       this._current_block_count = this._marked_offset_count;
/* 125:176 */       this._current_offset = this._marked_offset;
/* 126:177 */       this._data = this._document.getBlockIterator();
/* 127:178 */       this._buffer = null;
/* 128:179 */       return;
/* 129:    */     }
/* 130:183 */     this._data = this._document.getBlockIterator();
/* 131:184 */     this._current_offset = 0;
/* 132:185 */     for (int i = 0; i < this._marked_offset_count; i++)
/* 133:    */     {
/* 134:186 */       this._buffer = ((ByteBuffer)this._data.next());
/* 135:187 */       this._current_offset += this._buffer.remaining();
/* 136:    */     }
/* 137:190 */     this._current_block_count = this._marked_offset_count;
/* 138:193 */     if (this._current_offset != this._marked_offset)
/* 139:    */     {
/* 140:195 */       this._buffer = ((ByteBuffer)this._data.next());
/* 141:196 */       this._current_block_count += 1;
/* 142:    */       
/* 143:    */ 
/* 144:    */ 
/* 145:    */ 
/* 146:201 */       int skipBy = this._marked_offset - this._current_offset;
/* 147:202 */       this._buffer.position(this._buffer.position() + skipBy);
/* 148:    */     }
/* 149:206 */     this._current_offset = this._marked_offset;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public long skip(long n)
/* 153:    */     throws IOException
/* 154:    */   {
/* 155:211 */     dieIfClosed();
/* 156:212 */     if (n < 0L) {
/* 157:213 */       return 0L;
/* 158:    */     }
/* 159:215 */     long new_offset = this._current_offset + n;
/* 160:217 */     if (new_offset < this._current_offset) {
/* 161:219 */       new_offset = this._document_size;
/* 162:220 */     } else if (new_offset > this._document_size) {
/* 163:221 */       new_offset = this._document_size;
/* 164:    */     }
/* 165:224 */     long rval = new_offset - this._current_offset;
/* 166:    */     
/* 167:    */ 
/* 168:227 */     byte[] skip = new byte[(int)rval];
/* 169:228 */     readFully(skip);
/* 170:229 */     return rval;
/* 171:    */   }
/* 172:    */   
/* 173:    */   private void dieIfClosed()
/* 174:    */     throws IOException
/* 175:    */   {
/* 176:233 */     if (this._closed) {
/* 177:234 */       throw new IOException("cannot perform requested operation on a closed stream");
/* 178:    */     }
/* 179:    */   }
/* 180:    */   
/* 181:    */   private boolean atEOD()
/* 182:    */   {
/* 183:239 */     return this._current_offset == this._document_size;
/* 184:    */   }
/* 185:    */   
/* 186:    */   private void checkAvaliable(int requestedSize)
/* 187:    */   {
/* 188:243 */     if (this._closed) {
/* 189:244 */       throw new IllegalStateException("cannot perform requested operation on a closed stream");
/* 190:    */     }
/* 191:246 */     if (requestedSize > this._document_size - this._current_offset) {
/* 192:247 */       throw new RuntimeException("Buffer underrun - requested " + requestedSize + " bytes but " + (this._document_size - this._current_offset) + " was available");
/* 193:    */     }
/* 194:    */   }
/* 195:    */   
/* 196:    */   public void readFully(byte[] buf, int off, int len)
/* 197:    */   {
/* 198:254 */     if (len < 0) {
/* 199:255 */       throw new RuntimeException("Can't read negative number of bytes");
/* 200:    */     }
/* 201:258 */     checkAvaliable(len);
/* 202:    */     
/* 203:260 */     int read = 0;
/* 204:261 */     while (read < len)
/* 205:    */     {
/* 206:262 */       if ((this._buffer == null) || (this._buffer.remaining() == 0))
/* 207:    */       {
/* 208:263 */         this._current_block_count += 1;
/* 209:264 */         this._buffer = ((ByteBuffer)this._data.next());
/* 210:    */       }
/* 211:267 */       int limit = Math.min(len - read, this._buffer.remaining());
/* 212:268 */       this._buffer.get(buf, off + read, limit);
/* 213:269 */       this._current_offset += limit;
/* 214:270 */       read += limit;
/* 215:    */     }
/* 216:    */   }
/* 217:    */   
/* 218:    */   public byte readByte()
/* 219:    */   {
/* 220:276 */     return (byte)readUByte();
/* 221:    */   }
/* 222:    */   
/* 223:    */   public double readDouble()
/* 224:    */   {
/* 225:281 */     return Double.longBitsToDouble(readLong());
/* 226:    */   }
/* 227:    */   
/* 228:    */   public long readLong()
/* 229:    */   {
/* 230:286 */     checkAvaliable(8);
/* 231:287 */     byte[] data = new byte[8];
/* 232:288 */     readFully(data, 0, 8);
/* 233:289 */     return LittleEndian.getLong(data, 0);
/* 234:    */   }
/* 235:    */   
/* 236:    */   public short readShort()
/* 237:    */   {
/* 238:294 */     checkAvaliable(2);
/* 239:295 */     byte[] data = new byte[2];
/* 240:296 */     readFully(data, 0, 2);
/* 241:297 */     return LittleEndian.getShort(data);
/* 242:    */   }
/* 243:    */   
/* 244:    */   public int readInt()
/* 245:    */   {
/* 246:302 */     checkAvaliable(4);
/* 247:303 */     byte[] data = new byte[4];
/* 248:304 */     readFully(data, 0, 4);
/* 249:305 */     return LittleEndian.getInt(data);
/* 250:    */   }
/* 251:    */   
/* 252:    */   public int readUShort()
/* 253:    */   {
/* 254:310 */     checkAvaliable(2);
/* 255:311 */     byte[] data = new byte[2];
/* 256:312 */     readFully(data, 0, 2);
/* 257:313 */     return LittleEndian.getUShort(data);
/* 258:    */   }
/* 259:    */   
/* 260:    */   public int readUByte()
/* 261:    */   {
/* 262:318 */     checkAvaliable(1);
/* 263:319 */     byte[] data = new byte[1];
/* 264:320 */     readFully(data, 0, 1);
/* 265:321 */     if (data[0] >= 0) {
/* 266:322 */       return data[0];
/* 267:    */     }
/* 268:323 */     return data[0] + 256;
/* 269:    */   }
/* 270:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.NDocumentInputStream
 * JD-Core Version:    0.7.0.1
 */