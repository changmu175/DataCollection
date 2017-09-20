/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndian;
/*   4:    */ 
/*   5:    */ public class EscherClientAnchorRecord
/*   6:    */   extends EscherRecord
/*   7:    */ {
/*   8:    */   public static final short RECORD_ID = -4080;
/*   9:    */   public static final String RECORD_DESCRIPTION = "MsofbtClientAnchor";
/*  10:    */   private short field_1_flag;
/*  11:    */   private short field_2_col1;
/*  12:    */   private short field_3_dx1;
/*  13:    */   private short field_4_row1;
/*  14:    */   private short field_5_dy1;
/*  15:    */   private short field_6_col2;
/*  16:    */   private short field_7_dx2;
/*  17:    */   private short field_8_row2;
/*  18:    */   private short field_9_dy2;
/*  19: 53 */   private byte[] remainingData = new byte[0];
/*  20: 54 */   private boolean shortRecord = false;
/*  21:    */   
/*  22:    */   public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory)
/*  23:    */   {
/*  24: 58 */     int bytesRemaining = readHeader(data, offset);
/*  25: 59 */     int pos = offset + 8;
/*  26: 60 */     int size = 0;
/*  27: 68 */     if (bytesRemaining != 4)
/*  28:    */     {
/*  29: 70 */       this.field_1_flag = LittleEndian.getShort(data, pos + size);size += 2;
/*  30: 71 */       this.field_2_col1 = LittleEndian.getShort(data, pos + size);size += 2;
/*  31: 72 */       this.field_3_dx1 = LittleEndian.getShort(data, pos + size);size += 2;
/*  32: 73 */       this.field_4_row1 = LittleEndian.getShort(data, pos + size);size += 2;
/*  33: 74 */       if (bytesRemaining >= 18)
/*  34:    */       {
/*  35: 75 */         this.field_5_dy1 = LittleEndian.getShort(data, pos + size);size += 2;
/*  36: 76 */         this.field_6_col2 = LittleEndian.getShort(data, pos + size);size += 2;
/*  37: 77 */         this.field_7_dx2 = LittleEndian.getShort(data, pos + size);size += 2;
/*  38: 78 */         this.field_8_row2 = LittleEndian.getShort(data, pos + size);size += 2;
/*  39: 79 */         this.field_9_dy2 = LittleEndian.getShort(data, pos + size);size += 2;
/*  40: 80 */         this.shortRecord = false;
/*  41:    */       }
/*  42:    */       else
/*  43:    */       {
/*  44: 82 */         this.shortRecord = true;
/*  45:    */       }
/*  46:    */     }
/*  47: 85 */     bytesRemaining -= size;
/*  48: 86 */     this.remainingData = new byte[bytesRemaining];
/*  49: 87 */     System.arraycopy(data, pos + size, this.remainingData, 0, bytesRemaining);
/*  50: 88 */     return 8 + size + bytesRemaining;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  54:    */   {
/*  55: 94 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  56: 96 */     if (this.remainingData == null) {
/*  57: 97 */       this.remainingData = new byte[0];
/*  58:    */     }
/*  59: 99 */     LittleEndian.putShort(data, offset, getOptions());
/*  60:100 */     LittleEndian.putShort(data, offset + 2, getRecordId());
/*  61:101 */     int remainingBytes = this.remainingData.length + (this.shortRecord ? 8 : 18);
/*  62:102 */     LittleEndian.putInt(data, offset + 4, remainingBytes);
/*  63:103 */     LittleEndian.putShort(data, offset + 8, this.field_1_flag);
/*  64:104 */     LittleEndian.putShort(data, offset + 10, this.field_2_col1);
/*  65:105 */     LittleEndian.putShort(data, offset + 12, this.field_3_dx1);
/*  66:106 */     LittleEndian.putShort(data, offset + 14, this.field_4_row1);
/*  67:107 */     if (!this.shortRecord)
/*  68:    */     {
/*  69:108 */       LittleEndian.putShort(data, offset + 16, this.field_5_dy1);
/*  70:109 */       LittleEndian.putShort(data, offset + 18, this.field_6_col2);
/*  71:110 */       LittleEndian.putShort(data, offset + 20, this.field_7_dx2);
/*  72:111 */       LittleEndian.putShort(data, offset + 22, this.field_8_row2);
/*  73:112 */       LittleEndian.putShort(data, offset + 24, this.field_9_dy2);
/*  74:    */     }
/*  75:114 */     System.arraycopy(this.remainingData, 0, data, offset + (this.shortRecord ? 16 : 26), this.remainingData.length);
/*  76:115 */     int pos = offset + 8 + (this.shortRecord ? 8 : 18) + this.remainingData.length;
/*  77:    */     
/*  78:117 */     listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
/*  79:118 */     return pos - offset;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public int getRecordSize()
/*  83:    */   {
/*  84:124 */     return 8 + (this.shortRecord ? 8 : 18) + (this.remainingData == null ? 0 : this.remainingData.length);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public short getRecordId()
/*  88:    */   {
/*  89:129 */     return -4080;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public String getRecordName()
/*  93:    */   {
/*  94:134 */     return "ClientAnchor";
/*  95:    */   }
/*  96:    */   
/*  97:    */   public short getFlag()
/*  98:    */   {
/*  99:144 */     return this.field_1_flag;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setFlag(short field_1_flag)
/* 103:    */   {
/* 104:154 */     this.field_1_flag = field_1_flag;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public short getCol1()
/* 108:    */   {
/* 109:164 */     return this.field_2_col1;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void setCol1(short field_2_col1)
/* 113:    */   {
/* 114:174 */     this.field_2_col1 = field_2_col1;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public short getDx1()
/* 118:    */   {
/* 119:184 */     return this.field_3_dx1;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setDx1(short field_3_dx1)
/* 123:    */   {
/* 124:194 */     this.field_3_dx1 = field_3_dx1;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public short getRow1()
/* 128:    */   {
/* 129:204 */     return this.field_4_row1;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void setRow1(short field_4_row1)
/* 133:    */   {
/* 134:214 */     this.field_4_row1 = field_4_row1;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public short getDy1()
/* 138:    */   {
/* 139:224 */     return this.field_5_dy1;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void setDy1(short field_5_dy1)
/* 143:    */   {
/* 144:234 */     this.shortRecord = false;
/* 145:235 */     this.field_5_dy1 = field_5_dy1;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public short getCol2()
/* 149:    */   {
/* 150:245 */     return this.field_6_col2;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void setCol2(short field_6_col2)
/* 154:    */   {
/* 155:255 */     this.shortRecord = false;
/* 156:256 */     this.field_6_col2 = field_6_col2;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public short getDx2()
/* 160:    */   {
/* 161:266 */     return this.field_7_dx2;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public void setDx2(short field_7_dx2)
/* 165:    */   {
/* 166:276 */     this.shortRecord = false;
/* 167:277 */     this.field_7_dx2 = field_7_dx2;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public short getRow2()
/* 171:    */   {
/* 172:287 */     return this.field_8_row2;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public void setRow2(short field_8_row2)
/* 176:    */   {
/* 177:297 */     this.shortRecord = false;
/* 178:298 */     this.field_8_row2 = field_8_row2;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public short getDy2()
/* 182:    */   {
/* 183:308 */     return this.field_9_dy2;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public void setDy2(short field_9_dy2)
/* 187:    */   {
/* 188:318 */     this.shortRecord = false;
/* 189:319 */     this.field_9_dy2 = field_9_dy2;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public byte[] getRemainingData()
/* 193:    */   {
/* 194:329 */     return this.remainingData;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public void setRemainingData(byte[] remainingData)
/* 198:    */   {
/* 199:338 */     if (remainingData == null) {
/* 200:339 */       this.remainingData = new byte[0];
/* 201:    */     } else {
/* 202:341 */       this.remainingData = ((byte[])remainingData.clone());
/* 203:    */     }
/* 204:    */   }
/* 205:    */   
/* 206:    */   protected Object[][] getAttributeMap()
/* 207:    */   {
/* 208:347 */     return new Object[][] { { "Flag", Short.valueOf(this.field_1_flag) }, { "Col1", Short.valueOf(this.field_2_col1) }, { "DX1", Short.valueOf(this.field_3_dx1) }, { "Row1", Short.valueOf(this.field_4_row1) }, { "DY1", Short.valueOf(this.field_5_dy1) }, { "Col2", Short.valueOf(this.field_6_col2) }, { "DX2", Short.valueOf(this.field_7_dx2) }, { "Row2", Short.valueOf(this.field_8_row2) }, { "DY2", Short.valueOf(this.field_9_dy2) }, { "Extra Data", this.remainingData } };
/* 209:    */   }
/* 210:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherClientAnchorRecord
 * JD-Core Version:    0.7.0.1
 */