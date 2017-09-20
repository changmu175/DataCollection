/*   1:    */ package org.apache.poi.poifs.storage;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.nio.ByteBuffer;
/*   7:    */ import java.util.Arrays;
/*   8:    */ import org.apache.poi.hssf.OldExcelFormatException;
/*   9:    */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*  10:    */ import org.apache.poi.poifs.common.POIFSConstants;
/*  11:    */ import org.apache.poi.poifs.filesystem.FileMagic;
/*  12:    */ import org.apache.poi.poifs.filesystem.NotOLE2FileException;
/*  13:    */ import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
/*  14:    */ import org.apache.poi.util.HexDump;
/*  15:    */ import org.apache.poi.util.IOUtils;
/*  16:    */ import org.apache.poi.util.IntegerField;
/*  17:    */ import org.apache.poi.util.LittleEndian;
/*  18:    */ import org.apache.poi.util.LongField;
/*  19:    */ import org.apache.poi.util.ShortField;
/*  20:    */ 
/*  21:    */ public final class HeaderBlock
/*  22:    */   implements HeaderBlockConstants
/*  23:    */ {
/*  24:    */   private static final byte _default_value = -1;
/*  25:    */   private final POIFSBigBlockSize bigBlockSize;
/*  26:    */   private int _bat_count;
/*  27:    */   private int _property_start;
/*  28:    */   private int _sbat_start;
/*  29:    */   private int _sbat_count;
/*  30:    */   private int _xbat_start;
/*  31:    */   private int _xbat_count;
/*  32:    */   private final byte[] _data;
/*  33:    */   
/*  34:    */   public HeaderBlock(InputStream stream)
/*  35:    */     throws IOException
/*  36:    */   {
/*  37:102 */     this(readFirst512(stream));
/*  38:105 */     if (this.bigBlockSize.getBigBlockSize() != 512)
/*  39:    */     {
/*  40:106 */       int rest = this.bigBlockSize.getBigBlockSize() - 512;
/*  41:107 */       byte[] tmp = new byte[rest];
/*  42:108 */       IOUtils.readFully(stream, tmp);
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public HeaderBlock(ByteBuffer buffer)
/*  47:    */     throws IOException
/*  48:    */   {
/*  49:113 */     this(IOUtils.toByteArray(buffer, 512));
/*  50:    */   }
/*  51:    */   
/*  52:    */   private HeaderBlock(byte[] data)
/*  53:    */     throws IOException
/*  54:    */   {
/*  55:117 */     this._data = ((byte[])data.clone());
/*  56:    */     
/*  57:    */ 
/*  58:120 */     FileMagic fm = FileMagic.valueOf(data);
/*  59:122 */     switch (1.$SwitchMap$org$apache$poi$poifs$filesystem$FileMagic[fm.ordinal()])
/*  60:    */     {
/*  61:    */     case 1: 
/*  62:    */       break;
/*  63:    */     case 2: 
/*  64:126 */       throw new OfficeXmlFileException("The supplied data appears to be in the Office 2007+ XML. You are calling the part of POI that deals with OLE2 Office Documents. You need to call a different part of POI to process this data (eg XSSF instead of HSSF)");
/*  65:    */     case 3: 
/*  66:130 */       throw new NotOLE2FileException("The supplied data appears to be a raw XML file. Formats such as Office 2003 XML are not supported");
/*  67:    */     case 4: 
/*  68:133 */       throw new NotOLE2FileException("The supplied data appears to be in the old MS Write format. Apache POI doesn't currently support this format");
/*  69:    */     case 5: 
/*  70:    */     case 6: 
/*  71:    */     case 7: 
/*  72:138 */       throw new OldExcelFormatException("The supplied data appears to be in " + fm + " format. " + "HSSF only supports the BIFF8 format, try OldExcelExtractor");
/*  73:    */     default: 
/*  74:142 */       String exp = HexDump.longToHex(-2226271756974174256L);
/*  75:143 */       String act = HexDump.longToHex(LittleEndian.getLong(data, 0));
/*  76:144 */       throw new NotOLE2FileException("Invalid header signature; read " + act + ", expected " + exp + " - Your file appears not to be a valid OLE2 document");
/*  77:    */     }
/*  78:150 */     if (this._data[30] == 12) {
/*  79:151 */       this.bigBlockSize = POIFSConstants.LARGER_BIG_BLOCK_SIZE_DETAILS;
/*  80:152 */     } else if (this._data[30] == 9) {
/*  81:153 */       this.bigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
/*  82:    */     } else {
/*  83:155 */       throw new IOException("Unsupported blocksize  (2^" + this._data[30] + "). Expected 2^9 or 2^12.");
/*  84:    */     }
/*  85:159 */     this._bat_count = new IntegerField(44, data).get();
/*  86:160 */     this._property_start = new IntegerField(48, this._data).get();
/*  87:161 */     this._sbat_start = new IntegerField(60, this._data).get();
/*  88:162 */     this._sbat_count = new IntegerField(64, this._data).get();
/*  89:163 */     this._xbat_start = new IntegerField(68, this._data).get();
/*  90:164 */     this._xbat_count = new IntegerField(72, this._data).get();
/*  91:    */   }
/*  92:    */   
/*  93:    */   public HeaderBlock(POIFSBigBlockSize bigBlockSize)
/*  94:    */   {
/*  95:172 */     this.bigBlockSize = bigBlockSize;
/*  96:    */     
/*  97:    */ 
/*  98:175 */     this._data = new byte[512];
/*  99:176 */     Arrays.fill(this._data, (byte)-1);
/* 100:    */     
/* 101:    */ 
/* 102:179 */     new LongField(0, -2226271756974174256L, this._data);
/* 103:180 */     new IntegerField(8, 0, this._data);
/* 104:181 */     new IntegerField(12, 0, this._data);
/* 105:182 */     new IntegerField(16, 0, this._data);
/* 106:183 */     new IntegerField(20, 0, this._data);
/* 107:184 */     new ShortField(24, (short)59, this._data);
/* 108:185 */     new ShortField(26, (short)3, this._data);
/* 109:186 */     new ShortField(28, (short)-2, this._data);
/* 110:    */     
/* 111:188 */     new ShortField(30, bigBlockSize.getHeaderValue(), this._data);
/* 112:189 */     new IntegerField(32, 6, this._data);
/* 113:190 */     new IntegerField(36, 0, this._data);
/* 114:191 */     new IntegerField(40, 0, this._data);
/* 115:192 */     new IntegerField(52, 0, this._data);
/* 116:193 */     new IntegerField(56, 4096, this._data);
/* 117:    */     
/* 118:    */ 
/* 119:196 */     this._bat_count = 0;
/* 120:197 */     this._sbat_count = 0;
/* 121:198 */     this._xbat_count = 0;
/* 122:199 */     this._property_start = -2;
/* 123:200 */     this._sbat_start = -2;
/* 124:201 */     this._xbat_start = -2;
/* 125:    */   }
/* 126:    */   
/* 127:    */   private static byte[] readFirst512(InputStream stream)
/* 128:    */     throws IOException
/* 129:    */   {
/* 130:207 */     byte[] data = new byte[512];
/* 131:208 */     int bsCount = IOUtils.readFully(stream, data);
/* 132:209 */     if (bsCount != 512) {
/* 133:210 */       throw alertShortRead(bsCount, 512);
/* 134:    */     }
/* 135:212 */     return data;
/* 136:    */   }
/* 137:    */   
/* 138:    */   private static IOException alertShortRead(int pRead, int expectedReadSize)
/* 139:    */   {
/* 140:    */     int read;
/* 141:    */     int read;
/* 142:217 */     if (pRead < 0) {
/* 143:219 */       read = 0;
/* 144:    */     } else {
/* 145:221 */       read = pRead;
/* 146:    */     }
/* 147:223 */     String type = " byte" + (read == 1 ? "" : "s");
/* 148:    */     
/* 149:225 */     return new IOException("Unable to read entire header; " + read + type + " read; expected " + expectedReadSize + " bytes");
/* 150:    */   }
/* 151:    */   
/* 152:    */   public int getPropertyStart()
/* 153:    */   {
/* 154:236 */     return this._property_start;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public void setPropertyStart(int startBlock)
/* 158:    */   {
/* 159:244 */     this._property_start = startBlock;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public int getSBATStart()
/* 163:    */   {
/* 164:251 */     return this._sbat_start;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public int getSBATCount()
/* 168:    */   {
/* 169:254 */     return this._sbat_count;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public void setSBATStart(int startBlock)
/* 173:    */   {
/* 174:264 */     this._sbat_start = startBlock;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public void setSBATBlockCount(int count)
/* 178:    */   {
/* 179:273 */     this._sbat_count = count;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public int getBATCount()
/* 183:    */   {
/* 184:280 */     return this._bat_count;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public void setBATCount(int count)
/* 188:    */   {
/* 189:287 */     this._bat_count = count;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public int[] getBATArray()
/* 193:    */   {
/* 194:299 */     int[] result = new int[Math.min(this._bat_count, 109)];
/* 195:300 */     int offset = 76;
/* 196:301 */     for (int j = 0; j < result.length; j++)
/* 197:    */     {
/* 198:302 */       result[j] = LittleEndian.getInt(this._data, offset);
/* 199:303 */       offset += 4;
/* 200:    */     }
/* 201:305 */     return result;
/* 202:    */   }
/* 203:    */   
/* 204:    */   public void setBATArray(int[] bat_array)
/* 205:    */   {
/* 206:312 */     int count = Math.min(bat_array.length, 109);
/* 207:313 */     int blank = 109 - count;
/* 208:    */     
/* 209:315 */     int offset = 76;
/* 210:316 */     for (int i = 0; i < count; i++)
/* 211:    */     {
/* 212:317 */       LittleEndian.putInt(this._data, offset, bat_array[i]);
/* 213:318 */       offset += 4;
/* 214:    */     }
/* 215:320 */     for (int i = 0; i < blank; i++)
/* 216:    */     {
/* 217:321 */       LittleEndian.putInt(this._data, offset, -1);
/* 218:322 */       offset += 4;
/* 219:    */     }
/* 220:    */   }
/* 221:    */   
/* 222:    */   public int getXBATCount()
/* 223:    */   {
/* 224:330 */     return this._xbat_count;
/* 225:    */   }
/* 226:    */   
/* 227:    */   public void setXBATCount(int count)
/* 228:    */   {
/* 229:336 */     this._xbat_count = count;
/* 230:    */   }
/* 231:    */   
/* 232:    */   public int getXBATIndex()
/* 233:    */   {
/* 234:343 */     return this._xbat_start;
/* 235:    */   }
/* 236:    */   
/* 237:    */   public void setXBATStart(int startBlock)
/* 238:    */   {
/* 239:349 */     this._xbat_start = startBlock;
/* 240:    */   }
/* 241:    */   
/* 242:    */   public POIFSBigBlockSize getBigBlockSize()
/* 243:    */   {
/* 244:356 */     return this.bigBlockSize;
/* 245:    */   }
/* 246:    */   
/* 247:    */   void writeData(OutputStream stream)
/* 248:    */     throws IOException
/* 249:    */   {
/* 250:370 */     new IntegerField(44, this._bat_count, this._data);
/* 251:371 */     new IntegerField(48, this._property_start, this._data);
/* 252:372 */     new IntegerField(60, this._sbat_start, this._data);
/* 253:373 */     new IntegerField(64, this._sbat_count, this._data);
/* 254:374 */     new IntegerField(68, this._xbat_start, this._data);
/* 255:375 */     new IntegerField(72, this._xbat_count, this._data);
/* 256:    */     
/* 257:    */ 
/* 258:378 */     stream.write(this._data, 0, 512);
/* 259:381 */     for (int i = 512; i < this.bigBlockSize.getBigBlockSize(); i++) {
/* 260:382 */       stream.write(0);
/* 261:    */     }
/* 262:    */   }
/* 263:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.HeaderBlock
 * JD-Core Version:    0.7.0.1
 */