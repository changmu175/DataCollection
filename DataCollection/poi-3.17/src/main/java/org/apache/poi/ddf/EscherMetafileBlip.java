/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import java.awt.Dimension;
/*   4:    */ import java.awt.Rectangle;
/*   5:    */ import java.io.ByteArrayInputStream;
/*   6:    */ import java.io.ByteArrayOutputStream;
/*   7:    */ import java.io.IOException;
/*   8:    */ import java.util.zip.DeflaterOutputStream;
/*   9:    */ import java.util.zip.InflaterInputStream;
/*  10:    */ import org.apache.poi.util.LittleEndian;
/*  11:    */ import org.apache.poi.util.POILogFactory;
/*  12:    */ import org.apache.poi.util.POILogger;
/*  13:    */ 
/*  14:    */ public final class EscherMetafileBlip
/*  15:    */   extends EscherBlipRecord
/*  16:    */ {
/*  17: 34 */   private static final POILogger log = POILogFactory.getLogger(EscherMetafileBlip.class);
/*  18:    */   public static final short RECORD_ID_EMF = -4070;
/*  19:    */   public static final short RECORD_ID_WMF = -4069;
/*  20:    */   public static final short RECORD_ID_PICT = -4068;
/*  21:    */   private static final int HEADER_SIZE = 8;
/*  22: 42 */   private final byte[] field_1_UID = new byte[16];
/*  23: 46 */   private final byte[] field_2_UID = new byte[16];
/*  24:    */   private int field_2_cb;
/*  25:    */   private int field_3_rcBounds_x1;
/*  26:    */   private int field_3_rcBounds_y1;
/*  27:    */   private int field_3_rcBounds_x2;
/*  28:    */   private int field_3_rcBounds_y2;
/*  29:    */   private int field_4_ptSize_w;
/*  30:    */   private int field_4_ptSize_h;
/*  31:    */   private int field_5_cbSave;
/*  32:    */   private byte field_6_fCompression;
/*  33:    */   private byte field_7_fFilter;
/*  34:    */   private byte[] raw_pictureData;
/*  35:    */   private byte[] remainingData;
/*  36:    */   
/*  37:    */   public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory)
/*  38:    */   {
/*  39: 63 */     int bytesAfterHeader = readHeader(data, offset);
/*  40: 64 */     int pos = offset + 8;
/*  41: 65 */     System.arraycopy(data, pos, this.field_1_UID, 0, 16);pos += 16;
/*  42: 67 */     if ((getOptions() ^ getSignature()) == 16)
/*  43:    */     {
/*  44: 68 */       System.arraycopy(data, pos, this.field_2_UID, 0, 16);pos += 16;
/*  45:    */     }
/*  46: 71 */     this.field_2_cb = LittleEndian.getInt(data, pos);pos += 4;
/*  47: 72 */     this.field_3_rcBounds_x1 = LittleEndian.getInt(data, pos);pos += 4;
/*  48: 73 */     this.field_3_rcBounds_y1 = LittleEndian.getInt(data, pos);pos += 4;
/*  49: 74 */     this.field_3_rcBounds_x2 = LittleEndian.getInt(data, pos);pos += 4;
/*  50: 75 */     this.field_3_rcBounds_y2 = LittleEndian.getInt(data, pos);pos += 4;
/*  51: 76 */     this.field_4_ptSize_w = LittleEndian.getInt(data, pos);pos += 4;
/*  52: 77 */     this.field_4_ptSize_h = LittleEndian.getInt(data, pos);pos += 4;
/*  53: 78 */     this.field_5_cbSave = LittleEndian.getInt(data, pos);pos += 4;
/*  54: 79 */     this.field_6_fCompression = data[pos];pos++;
/*  55: 80 */     this.field_7_fFilter = data[pos];pos++;
/*  56:    */     
/*  57: 82 */     this.raw_pictureData = new byte[this.field_5_cbSave];
/*  58: 83 */     System.arraycopy(data, pos, this.raw_pictureData, 0, this.field_5_cbSave);
/*  59: 84 */     pos += this.field_5_cbSave;
/*  60: 88 */     if (this.field_6_fCompression == 0) {
/*  61: 89 */       super.setPictureData(inflatePictureData(this.raw_pictureData));
/*  62:    */     } else {
/*  63: 91 */       super.setPictureData(this.raw_pictureData);
/*  64:    */     }
/*  65: 94 */     int remaining = bytesAfterHeader - pos + offset + 8;
/*  66: 95 */     if (remaining > 0)
/*  67:    */     {
/*  68: 96 */       this.remainingData = new byte[remaining];
/*  69: 97 */       System.arraycopy(data, pos, this.remainingData, 0, remaining);
/*  70:    */     }
/*  71: 99 */     return bytesAfterHeader + 8;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  75:    */   {
/*  76:104 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  77:    */     
/*  78:106 */     int pos = offset;
/*  79:107 */     LittleEndian.putShort(data, pos, getOptions());pos += 2;
/*  80:108 */     LittleEndian.putShort(data, pos, getRecordId());pos += 2;
/*  81:109 */     LittleEndian.putInt(data, pos, getRecordSize() - 8);pos += 4;
/*  82:    */     
/*  83:111 */     System.arraycopy(this.field_1_UID, 0, data, pos, this.field_1_UID.length);pos += this.field_1_UID.length;
/*  84:112 */     if ((getOptions() ^ getSignature()) == 16)
/*  85:    */     {
/*  86:113 */       System.arraycopy(this.field_2_UID, 0, data, pos, this.field_2_UID.length);pos += this.field_2_UID.length;
/*  87:    */     }
/*  88:115 */     LittleEndian.putInt(data, pos, this.field_2_cb);pos += 4;
/*  89:116 */     LittleEndian.putInt(data, pos, this.field_3_rcBounds_x1);pos += 4;
/*  90:117 */     LittleEndian.putInt(data, pos, this.field_3_rcBounds_y1);pos += 4;
/*  91:118 */     LittleEndian.putInt(data, pos, this.field_3_rcBounds_x2);pos += 4;
/*  92:119 */     LittleEndian.putInt(data, pos, this.field_3_rcBounds_y2);pos += 4;
/*  93:120 */     LittleEndian.putInt(data, pos, this.field_4_ptSize_w);pos += 4;
/*  94:121 */     LittleEndian.putInt(data, pos, this.field_4_ptSize_h);pos += 4;
/*  95:122 */     LittleEndian.putInt(data, pos, this.field_5_cbSave);pos += 4;
/*  96:123 */     data[pos] = this.field_6_fCompression;pos++;
/*  97:124 */     data[pos] = this.field_7_fFilter;pos++;
/*  98:    */     
/*  99:126 */     System.arraycopy(this.raw_pictureData, 0, data, pos, this.raw_pictureData.length);pos += this.raw_pictureData.length;
/* 100:127 */     if (this.remainingData != null)
/* 101:    */     {
/* 102:128 */       System.arraycopy(this.remainingData, 0, data, pos, this.remainingData.length);pos += this.remainingData.length;
/* 103:    */     }
/* 104:131 */     listener.afterRecordSerialize(offset + getRecordSize(), getRecordId(), getRecordSize(), this);
/* 105:132 */     return getRecordSize();
/* 106:    */   }
/* 107:    */   
/* 108:    */   private static byte[] inflatePictureData(byte[] data)
/* 109:    */   {
/* 110:    */     try
/* 111:    */     {
/* 112:143 */       InflaterInputStream in = new InflaterInputStream(new ByteArrayInputStream(data));
/* 113:    */       
/* 114:145 */       ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 115:146 */       byte[] buf = new byte[4096];
/* 116:    */       int readBytes;
/* 117:148 */       while ((readBytes = in.read(buf)) > 0) {
/* 118:149 */         out.write(buf, 0, readBytes);
/* 119:    */       }
/* 120:151 */       return out.toByteArray();
/* 121:    */     }
/* 122:    */     catch (IOException e)
/* 123:    */     {
/* 124:153 */       log.log(5, new Object[] { "Possibly corrupt compression or non-compressed data", e });
/* 125:    */     }
/* 126:154 */     return data;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public int getRecordSize()
/* 130:    */   {
/* 131:160 */     int size = 58 + this.raw_pictureData.length;
/* 132:161 */     if (this.remainingData != null) {
/* 133:162 */       size += this.remainingData.length;
/* 134:    */     }
/* 135:164 */     if ((getOptions() ^ getSignature()) == 16) {
/* 136:165 */       size += this.field_2_UID.length;
/* 137:    */     }
/* 138:167 */     return size;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public byte[] getUID()
/* 142:    */   {
/* 143:177 */     return this.field_1_UID;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void setUID(byte[] uid)
/* 147:    */   {
/* 148:187 */     if ((uid == null) || (uid.length != 16)) {
/* 149:188 */       throw new IllegalArgumentException("uid must be byte[16]");
/* 150:    */     }
/* 151:190 */     System.arraycopy(uid, 0, this.field_1_UID, 0, this.field_1_UID.length);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public byte[] getPrimaryUID()
/* 155:    */   {
/* 156:200 */     return this.field_2_UID;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public void setPrimaryUID(byte[] primaryUID)
/* 160:    */   {
/* 161:210 */     if ((primaryUID == null) || (primaryUID.length != 16)) {
/* 162:211 */       throw new IllegalArgumentException("primaryUID must be byte[16]");
/* 163:    */     }
/* 164:213 */     System.arraycopy(primaryUID, 0, this.field_2_UID, 0, this.field_2_UID.length);
/* 165:    */   }
/* 166:    */   
/* 167:    */   public int getUncompressedSize()
/* 168:    */   {
/* 169:222 */     return this.field_2_cb;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public void setUncompressedSize(int uncompressedSize)
/* 173:    */   {
/* 174:231 */     this.field_2_cb = uncompressedSize;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public Rectangle getBounds()
/* 178:    */   {
/* 179:240 */     return new Rectangle(this.field_3_rcBounds_x1, this.field_3_rcBounds_y1, this.field_3_rcBounds_x2 - this.field_3_rcBounds_x1, this.field_3_rcBounds_y2 - this.field_3_rcBounds_y1);
/* 180:    */   }
/* 181:    */   
/* 182:    */   public void setBounds(Rectangle bounds)
/* 183:    */   {
/* 184:252 */     this.field_3_rcBounds_x1 = bounds.x;
/* 185:253 */     this.field_3_rcBounds_y1 = bounds.y;
/* 186:254 */     this.field_3_rcBounds_x2 = (bounds.x + bounds.width);
/* 187:255 */     this.field_3_rcBounds_y2 = (bounds.y + bounds.height);
/* 188:    */   }
/* 189:    */   
/* 190:    */   public Dimension getSizeEMU()
/* 191:    */   {
/* 192:264 */     return new Dimension(this.field_4_ptSize_w, this.field_4_ptSize_h);
/* 193:    */   }
/* 194:    */   
/* 195:    */   public void setSizeEMU(Dimension sizeEMU)
/* 196:    */   {
/* 197:273 */     this.field_4_ptSize_w = sizeEMU.width;
/* 198:274 */     this.field_4_ptSize_h = sizeEMU.height;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public int getCompressedSize()
/* 202:    */   {
/* 203:283 */     return this.field_5_cbSave;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public void setCompressedSize(int compressedSize)
/* 207:    */   {
/* 208:292 */     this.field_5_cbSave = compressedSize;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public boolean isCompressed()
/* 212:    */   {
/* 213:301 */     return this.field_6_fCompression == 0;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public void setCompressed(boolean compressed)
/* 217:    */   {
/* 218:310 */     this.field_6_fCompression = (compressed ? 0 : -2);
/* 219:    */   }
/* 220:    */   
/* 221:    */   public byte getFilter()
/* 222:    */   {
/* 223:319 */     return this.field_7_fFilter;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public void setFilter(byte filter)
/* 227:    */   {
/* 228:328 */     this.field_7_fFilter = filter;
/* 229:    */   }
/* 230:    */   
/* 231:    */   public byte[] getRemainingData()
/* 232:    */   {
/* 233:339 */     return this.remainingData;
/* 234:    */   }
/* 235:    */   
/* 236:    */   public short getSignature()
/* 237:    */   {
/* 238:348 */     switch (getRecordId())
/* 239:    */     {
/* 240:    */     case -4070: 
/* 241:349 */       return 15680;
/* 242:    */     case -4069: 
/* 243:350 */       return 8544;
/* 244:    */     case -4068: 
/* 245:351 */       return 21536;
/* 246:    */     }
/* 247:353 */     if (log.check(5)) {
/* 248:354 */       log.log(5, new Object[] { "Unknown metafile: " + getRecordId() });
/* 249:    */     }
/* 250:356 */     return 0;
/* 251:    */   }
/* 252:    */   
/* 253:    */   public void setPictureData(byte[] pictureData)
/* 254:    */   {
/* 255:361 */     super.setPictureData(pictureData);
/* 256:362 */     setUncompressedSize(pictureData.length);
/* 257:    */     try
/* 258:    */     {
/* 259:369 */       ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 260:370 */       DeflaterOutputStream dos = new DeflaterOutputStream(bos);
/* 261:371 */       dos.write(pictureData);
/* 262:372 */       dos.close();
/* 263:373 */       this.raw_pictureData = bos.toByteArray();
/* 264:    */     }
/* 265:    */     catch (IOException e)
/* 266:    */     {
/* 267:375 */       throw new RuntimeException("Can't compress metafile picture data", e);
/* 268:    */     }
/* 269:378 */     setCompressedSize(this.raw_pictureData.length);
/* 270:379 */     setCompressed(true);
/* 271:    */   }
/* 272:    */   
/* 273:    */   protected Object[][] getAttributeMap()
/* 274:    */   {
/* 275:384 */     return new Object[][] { { "UID", this.field_1_UID, "UID2", this.field_2_UID }, { "Uncompressed Size", Integer.valueOf(this.field_2_cb) }, { "Bounds", getBounds().toString() }, { "Size in EMU", getSizeEMU().toString() }, { "Compressed Size", Integer.valueOf(this.field_5_cbSave) }, { "Compression", Byte.valueOf(this.field_6_fCompression) }, { "Filter", Byte.valueOf(this.field_7_fFilter) }, { "Extra Data", "" }, { "Remaining Data", this.remainingData } };
/* 276:    */   }
/* 277:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherMetafileBlip
 * JD-Core Version:    0.7.0.1
 */