/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import java.awt.Dimension;
/*   4:    */ import java.awt.Rectangle;
/*   5:    */ import java.io.ByteArrayInputStream;
/*   6:    */ import java.io.ByteArrayOutputStream;
/*   7:    */ import java.io.IOException;
/*   8:    */ import java.util.zip.InflaterInputStream;
/*   9:    */ import org.apache.poi.util.LittleEndian;
/*  10:    */ import org.apache.poi.util.POILogFactory;
/*  11:    */ import org.apache.poi.util.POILogger;
/*  12:    */ 
/*  13:    */ public final class EscherPictBlip
/*  14:    */   extends EscherBlipRecord
/*  15:    */ {
/*  16: 32 */   private static final POILogger log = POILogFactory.getLogger(EscherPictBlip.class);
/*  17:    */   public static final short RECORD_ID_EMF = -4070;
/*  18:    */   public static final short RECORD_ID_WMF = -4069;
/*  19:    */   public static final short RECORD_ID_PICT = -4068;
/*  20:    */   private static final int HEADER_SIZE = 8;
/*  21: 40 */   private final byte[] field_1_UID = new byte[16];
/*  22:    */   private int field_2_cb;
/*  23:    */   private int field_3_rcBounds_x1;
/*  24:    */   private int field_3_rcBounds_y1;
/*  25:    */   private int field_3_rcBounds_x2;
/*  26:    */   private int field_3_rcBounds_y2;
/*  27:    */   private int field_4_ptSize_w;
/*  28:    */   private int field_4_ptSize_h;
/*  29:    */   private int field_5_cbSave;
/*  30:    */   private byte field_6_fCompression;
/*  31:    */   private byte field_7_fFilter;
/*  32:    */   private byte[] raw_pictureData;
/*  33:    */   
/*  34:    */   public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory)
/*  35:    */   {
/*  36: 56 */     int bytesAfterHeader = readHeader(data, offset);
/*  37: 57 */     int pos = offset + 8;
/*  38:    */     
/*  39: 59 */     System.arraycopy(data, pos, this.field_1_UID, 0, 16);pos += 16;
/*  40: 60 */     this.field_2_cb = LittleEndian.getInt(data, pos);pos += 4;
/*  41: 61 */     this.field_3_rcBounds_x1 = LittleEndian.getInt(data, pos);pos += 4;
/*  42: 62 */     this.field_3_rcBounds_y1 = LittleEndian.getInt(data, pos);pos += 4;
/*  43: 63 */     this.field_3_rcBounds_x2 = LittleEndian.getInt(data, pos);pos += 4;
/*  44: 64 */     this.field_3_rcBounds_y2 = LittleEndian.getInt(data, pos);pos += 4;
/*  45: 65 */     this.field_4_ptSize_w = LittleEndian.getInt(data, pos);pos += 4;
/*  46: 66 */     this.field_4_ptSize_h = LittleEndian.getInt(data, pos);pos += 4;
/*  47: 67 */     this.field_5_cbSave = LittleEndian.getInt(data, pos);pos += 4;
/*  48: 68 */     this.field_6_fCompression = data[pos];pos++;
/*  49: 69 */     this.field_7_fFilter = data[pos];pos++;
/*  50:    */     
/*  51: 71 */     this.raw_pictureData = new byte[this.field_5_cbSave];
/*  52: 72 */     System.arraycopy(data, pos, this.raw_pictureData, 0, this.field_5_cbSave);
/*  53: 76 */     if (this.field_6_fCompression == 0) {
/*  54: 78 */       super.setPictureData(inflatePictureData(this.raw_pictureData));
/*  55:    */     } else {
/*  56: 82 */       super.setPictureData(this.raw_pictureData);
/*  57:    */     }
/*  58: 85 */     return bytesAfterHeader + 8;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  62:    */   {
/*  63: 90 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  64:    */     
/*  65: 92 */     int pos = offset;
/*  66: 93 */     LittleEndian.putShort(data, pos, getOptions());pos += 2;
/*  67: 94 */     LittleEndian.putShort(data, pos, getRecordId());pos += 2;
/*  68: 95 */     LittleEndian.putInt(data, 0, getRecordSize() - 8);pos += 4;
/*  69:    */     
/*  70: 97 */     System.arraycopy(this.field_1_UID, 0, data, pos, 16);pos += 16;
/*  71: 98 */     LittleEndian.putInt(data, pos, this.field_2_cb);pos += 4;
/*  72: 99 */     LittleEndian.putInt(data, pos, this.field_3_rcBounds_x1);pos += 4;
/*  73:100 */     LittleEndian.putInt(data, pos, this.field_3_rcBounds_y1);pos += 4;
/*  74:101 */     LittleEndian.putInt(data, pos, this.field_3_rcBounds_x2);pos += 4;
/*  75:102 */     LittleEndian.putInt(data, pos, this.field_3_rcBounds_y2);pos += 4;
/*  76:103 */     LittleEndian.putInt(data, pos, this.field_4_ptSize_w);pos += 4;
/*  77:104 */     LittleEndian.putInt(data, pos, this.field_4_ptSize_h);pos += 4;
/*  78:105 */     LittleEndian.putInt(data, pos, this.field_5_cbSave);pos += 4;
/*  79:106 */     data[pos] = this.field_6_fCompression;pos++;
/*  80:107 */     data[pos] = this.field_7_fFilter;pos++;
/*  81:    */     
/*  82:109 */     System.arraycopy(this.raw_pictureData, 0, data, pos, this.raw_pictureData.length);
/*  83:    */     
/*  84:111 */     listener.afterRecordSerialize(offset + getRecordSize(), getRecordId(), getRecordSize(), this);
/*  85:112 */     return 25 + this.raw_pictureData.length;
/*  86:    */   }
/*  87:    */   
/*  88:    */   private static byte[] inflatePictureData(byte[] data)
/*  89:    */   {
/*  90:    */     try
/*  91:    */     {
/*  92:123 */       InflaterInputStream in = new InflaterInputStream(new ByteArrayInputStream(data));
/*  93:124 */       ByteArrayOutputStream out = new ByteArrayOutputStream();
/*  94:125 */       byte[] buf = new byte[4096];
/*  95:    */       int readBytes;
/*  96:127 */       while ((readBytes = in.read(buf)) > 0) {
/*  97:128 */         out.write(buf, 0, readBytes);
/*  98:    */       }
/*  99:130 */       return out.toByteArray();
/* 100:    */     }
/* 101:    */     catch (IOException e)
/* 102:    */     {
/* 103:132 */       log.log(3, new Object[] { "Possibly corrupt compression or non-compressed data", e });
/* 104:    */     }
/* 105:133 */     return data;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public int getRecordSize()
/* 109:    */   {
/* 110:139 */     return 58 + this.raw_pictureData.length;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public byte[] getUID()
/* 114:    */   {
/* 115:149 */     return this.field_1_UID;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setUID(byte[] uid)
/* 119:    */   {
/* 120:159 */     if ((uid == null) || (uid.length != 16)) {
/* 121:160 */       throw new IllegalArgumentException("uid must be byte[16]");
/* 122:    */     }
/* 123:162 */     System.arraycopy(uid, 0, this.field_1_UID, 0, this.field_1_UID.length);
/* 124:    */   }
/* 125:    */   
/* 126:    */   public int getUncompressedSize()
/* 127:    */   {
/* 128:171 */     return this.field_2_cb;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void setUncompressedSize(int uncompressedSize)
/* 132:    */   {
/* 133:180 */     this.field_2_cb = uncompressedSize;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public Rectangle getBounds()
/* 137:    */   {
/* 138:189 */     return new Rectangle(this.field_3_rcBounds_x1, this.field_3_rcBounds_y1, this.field_3_rcBounds_x2 - this.field_3_rcBounds_x1, this.field_3_rcBounds_y2 - this.field_3_rcBounds_y1);
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void setBounds(Rectangle bounds)
/* 142:    */   {
/* 143:201 */     this.field_3_rcBounds_x1 = bounds.x;
/* 144:202 */     this.field_3_rcBounds_y1 = bounds.y;
/* 145:203 */     this.field_3_rcBounds_x2 = (bounds.x + bounds.width);
/* 146:204 */     this.field_3_rcBounds_y2 = (bounds.y + bounds.height);
/* 147:    */   }
/* 148:    */   
/* 149:    */   public Dimension getSizeEMU()
/* 150:    */   {
/* 151:213 */     return new Dimension(this.field_4_ptSize_w, this.field_4_ptSize_h);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public void setSizeEMU(Dimension sizeEMU)
/* 155:    */   {
/* 156:222 */     this.field_4_ptSize_w = sizeEMU.width;
/* 157:223 */     this.field_4_ptSize_h = sizeEMU.height;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public int getCompressedSize()
/* 161:    */   {
/* 162:232 */     return this.field_5_cbSave;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public void setCompressedSize(int compressedSize)
/* 166:    */   {
/* 167:241 */     this.field_5_cbSave = compressedSize;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public boolean isCompressed()
/* 171:    */   {
/* 172:250 */     return this.field_6_fCompression == 0;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public void setCompressed(boolean compressed)
/* 176:    */   {
/* 177:259 */     this.field_6_fCompression = (compressed ? 0 : -2);
/* 178:    */   }
/* 179:    */   
/* 180:    */   public byte getFilter()
/* 181:    */   {
/* 182:268 */     return this.field_7_fFilter;
/* 183:    */   }
/* 184:    */   
/* 185:    */   public void setFilter(byte filter)
/* 186:    */   {
/* 187:277 */     this.field_7_fFilter = filter;
/* 188:    */   }
/* 189:    */   
/* 190:    */   protected Object[][] getAttributeMap()
/* 191:    */   {
/* 192:282 */     return new Object[][] { { "UID", this.field_1_UID }, { "Uncompressed Size", Integer.valueOf(this.field_2_cb) }, { "Bounds", getBounds().toString() }, { "Size in EMU", getSizeEMU().toString() }, { "Compressed Size", Integer.valueOf(this.field_5_cbSave) }, { "Compression", Byte.valueOf(this.field_6_fCompression) }, { "Filter", Byte.valueOf(this.field_7_fFilter) }, { "Extra Data", getPicturedata() } };
/* 193:    */   }
/* 194:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherPictBlip
 * JD-Core Version:    0.7.0.1
 */