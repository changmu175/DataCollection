/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndian;
/*   4:    */ 
/*   5:    */ public final class EscherBSERecord
/*   6:    */   extends EscherRecord
/*   7:    */ {
/*   8:    */   public static final short RECORD_ID = -4089;
/*   9:    */   public static final String RECORD_DESCRIPTION = "MsofbtBSE";
/*  10:    */   public static final byte BT_ERROR = 0;
/*  11:    */   public static final byte BT_UNKNOWN = 1;
/*  12:    */   public static final byte BT_EMF = 2;
/*  13:    */   public static final byte BT_WMF = 3;
/*  14:    */   public static final byte BT_PICT = 4;
/*  15:    */   public static final byte BT_JPEG = 5;
/*  16:    */   public static final byte BT_PNG = 6;
/*  17:    */   public static final byte BT_DIB = 7;
/*  18:    */   private byte field_1_blipTypeWin32;
/*  19:    */   private byte field_2_blipTypeMacOS;
/*  20: 44 */   private final byte[] field_3_uid = new byte[16];
/*  21:    */   private short field_4_tag;
/*  22:    */   private int field_5_size;
/*  23:    */   private int field_6_ref;
/*  24:    */   private int field_7_offset;
/*  25:    */   private byte field_8_usage;
/*  26:    */   private byte field_9_name;
/*  27:    */   private byte field_10_unused2;
/*  28:    */   private byte field_11_unused3;
/*  29:    */   private EscherBlipRecord field_12_blipRecord;
/*  30: 55 */   private byte[] _remainingData = new byte[0];
/*  31:    */   
/*  32:    */   public EscherBSERecord()
/*  33:    */   {
/*  34: 58 */     setRecordId((short)-4089);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory)
/*  38:    */   {
/*  39: 63 */     int bytesRemaining = readHeader(data, offset);
/*  40: 64 */     int pos = offset + 8;
/*  41: 65 */     this.field_1_blipTypeWin32 = data[pos];
/*  42: 66 */     this.field_2_blipTypeMacOS = data[(pos + 1)];
/*  43: 67 */     System.arraycopy(data, pos + 2, this.field_3_uid, 0, 16);
/*  44: 68 */     this.field_4_tag = LittleEndian.getShort(data, pos + 18);
/*  45: 69 */     this.field_5_size = LittleEndian.getInt(data, pos + 20);
/*  46: 70 */     this.field_6_ref = LittleEndian.getInt(data, pos + 24);
/*  47: 71 */     this.field_7_offset = LittleEndian.getInt(data, pos + 28);
/*  48: 72 */     this.field_8_usage = data[(pos + 32)];
/*  49: 73 */     this.field_9_name = data[(pos + 33)];
/*  50: 74 */     this.field_10_unused2 = data[(pos + 34)];
/*  51: 75 */     this.field_11_unused3 = data[(pos + 35)];
/*  52: 76 */     bytesRemaining -= 36;
/*  53:    */     
/*  54: 78 */     int bytesRead = 0;
/*  55: 79 */     if (bytesRemaining > 0)
/*  56:    */     {
/*  57: 81 */       this.field_12_blipRecord = ((EscherBlipRecord)recordFactory.createRecord(data, pos + 36));
/*  58: 82 */       bytesRead = this.field_12_blipRecord.fillFields(data, pos + 36, recordFactory);
/*  59:    */     }
/*  60: 84 */     pos += 36 + bytesRead;
/*  61: 85 */     bytesRemaining -= bytesRead;
/*  62:    */     
/*  63: 87 */     this._remainingData = new byte[bytesRemaining];
/*  64: 88 */     System.arraycopy(data, pos, this._remainingData, 0, bytesRemaining);
/*  65: 89 */     return bytesRemaining + 8 + 36 + (this.field_12_blipRecord == null ? 0 : this.field_12_blipRecord.getRecordSize());
/*  66:    */   }
/*  67:    */   
/*  68:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  69:    */   {
/*  70: 95 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  71: 97 */     if (this._remainingData == null) {
/*  72: 98 */       this._remainingData = new byte[0];
/*  73:    */     }
/*  74:101 */     LittleEndian.putShort(data, offset, getOptions());
/*  75:102 */     LittleEndian.putShort(data, offset + 2, getRecordId());
/*  76:103 */     int blipSize = this.field_12_blipRecord == null ? 0 : this.field_12_blipRecord.getRecordSize();
/*  77:104 */     int remainingBytes = this._remainingData.length + 36 + blipSize;
/*  78:105 */     LittleEndian.putInt(data, offset + 4, remainingBytes);
/*  79:    */     
/*  80:107 */     data[(offset + 8)] = this.field_1_blipTypeWin32;
/*  81:108 */     data[(offset + 9)] = this.field_2_blipTypeMacOS;
/*  82:109 */     System.arraycopy(this.field_3_uid, 0, data, offset + 10, 16);
/*  83:110 */     LittleEndian.putShort(data, offset + 26, this.field_4_tag);
/*  84:111 */     LittleEndian.putInt(data, offset + 28, this.field_5_size);
/*  85:112 */     LittleEndian.putInt(data, offset + 32, this.field_6_ref);
/*  86:113 */     LittleEndian.putInt(data, offset + 36, this.field_7_offset);
/*  87:114 */     data[(offset + 40)] = this.field_8_usage;
/*  88:115 */     data[(offset + 41)] = this.field_9_name;
/*  89:116 */     data[(offset + 42)] = this.field_10_unused2;
/*  90:117 */     data[(offset + 43)] = this.field_11_unused3;
/*  91:118 */     int bytesWritten = 0;
/*  92:119 */     if (this.field_12_blipRecord != null) {
/*  93:120 */       bytesWritten = this.field_12_blipRecord.serialize(offset + 44, data, new NullEscherSerializationListener());
/*  94:    */     }
/*  95:122 */     System.arraycopy(this._remainingData, 0, data, offset + 44 + bytesWritten, this._remainingData.length);
/*  96:123 */     int pos = offset + 8 + 36 + this._remainingData.length + bytesWritten;
/*  97:    */     
/*  98:125 */     listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
/*  99:126 */     return pos - offset;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public int getRecordSize()
/* 103:    */   {
/* 104:131 */     int field_12_size = 0;
/* 105:132 */     if (this.field_12_blipRecord != null) {
/* 106:133 */       field_12_size = this.field_12_blipRecord.getRecordSize();
/* 107:    */     }
/* 108:135 */     int remaining_size = 0;
/* 109:136 */     if (this._remainingData != null) {
/* 110:137 */       remaining_size = this._remainingData.length;
/* 111:    */     }
/* 112:139 */     return 44 + field_12_size + remaining_size;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public String getRecordName()
/* 116:    */   {
/* 117:145 */     return "BSE";
/* 118:    */   }
/* 119:    */   
/* 120:    */   public byte getBlipTypeWin32()
/* 121:    */   {
/* 122:155 */     return this.field_1_blipTypeWin32;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void setBlipTypeWin32(byte blipTypeWin32)
/* 126:    */   {
/* 127:164 */     this.field_1_blipTypeWin32 = blipTypeWin32;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public byte getBlipTypeMacOS()
/* 131:    */   {
/* 132:174 */     return this.field_2_blipTypeMacOS;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void setBlipTypeMacOS(byte blipTypeMacOS)
/* 136:    */   {
/* 137:183 */     this.field_2_blipTypeMacOS = blipTypeMacOS;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public byte[] getUid()
/* 141:    */   {
/* 142:192 */     return this.field_3_uid;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void setUid(byte[] uid)
/* 146:    */   {
/* 147:201 */     if ((uid == null) || (uid.length != 16)) {
/* 148:202 */       throw new IllegalArgumentException("uid must be byte[16]");
/* 149:    */     }
/* 150:204 */     System.arraycopy(uid, 0, this.field_3_uid, 0, this.field_3_uid.length);
/* 151:    */   }
/* 152:    */   
/* 153:    */   public short getTag()
/* 154:    */   {
/* 155:213 */     return this.field_4_tag;
/* 156:    */   }
/* 157:    */   
/* 158:    */   public void setTag(short tag)
/* 159:    */   {
/* 160:222 */     this.field_4_tag = tag;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public int getSize()
/* 164:    */   {
/* 165:231 */     return this.field_5_size;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public void setSize(int size)
/* 169:    */   {
/* 170:240 */     this.field_5_size = size;
/* 171:    */   }
/* 172:    */   
/* 173:    */   public int getRef()
/* 174:    */   {
/* 175:249 */     return this.field_6_ref;
/* 176:    */   }
/* 177:    */   
/* 178:    */   public void setRef(int ref)
/* 179:    */   {
/* 180:258 */     this.field_6_ref = ref;
/* 181:    */   }
/* 182:    */   
/* 183:    */   public int getOffset()
/* 184:    */   {
/* 185:267 */     return this.field_7_offset;
/* 186:    */   }
/* 187:    */   
/* 188:    */   public void setOffset(int offset)
/* 189:    */   {
/* 190:276 */     this.field_7_offset = offset;
/* 191:    */   }
/* 192:    */   
/* 193:    */   public byte getUsage()
/* 194:    */   {
/* 195:285 */     return this.field_8_usage;
/* 196:    */   }
/* 197:    */   
/* 198:    */   public void setUsage(byte usage)
/* 199:    */   {
/* 200:294 */     this.field_8_usage = usage;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public byte getName()
/* 204:    */   {
/* 205:303 */     return this.field_9_name;
/* 206:    */   }
/* 207:    */   
/* 208:    */   public void setName(byte name)
/* 209:    */   {
/* 210:312 */     this.field_9_name = name;
/* 211:    */   }
/* 212:    */   
/* 213:    */   public byte getUnused2()
/* 214:    */   {
/* 215:316 */     return this.field_10_unused2;
/* 216:    */   }
/* 217:    */   
/* 218:    */   public void setUnused2(byte unused2)
/* 219:    */   {
/* 220:320 */     this.field_10_unused2 = unused2;
/* 221:    */   }
/* 222:    */   
/* 223:    */   public byte getUnused3()
/* 224:    */   {
/* 225:324 */     return this.field_11_unused3;
/* 226:    */   }
/* 227:    */   
/* 228:    */   public void setUnused3(byte unused3)
/* 229:    */   {
/* 230:328 */     this.field_11_unused3 = unused3;
/* 231:    */   }
/* 232:    */   
/* 233:    */   public EscherBlipRecord getBlipRecord()
/* 234:    */   {
/* 235:332 */     return this.field_12_blipRecord;
/* 236:    */   }
/* 237:    */   
/* 238:    */   public void setBlipRecord(EscherBlipRecord blipRecord)
/* 239:    */   {
/* 240:336 */     this.field_12_blipRecord = blipRecord;
/* 241:    */   }
/* 242:    */   
/* 243:    */   public byte[] getRemainingData()
/* 244:    */   {
/* 245:345 */     return this._remainingData;
/* 246:    */   }
/* 247:    */   
/* 248:    */   public void setRemainingData(byte[] remainingData)
/* 249:    */   {
/* 250:354 */     this._remainingData = (remainingData == null ? new byte[0] : (byte[])remainingData.clone());
/* 251:    */   }
/* 252:    */   
/* 253:    */   public static String getBlipType(byte b)
/* 254:    */   {
/* 255:365 */     switch (b)
/* 256:    */     {
/* 257:    */     case 0: 
/* 258:366 */       return " ERROR";
/* 259:    */     case 1: 
/* 260:367 */       return " UNKNOWN";
/* 261:    */     case 2: 
/* 262:368 */       return " EMF";
/* 263:    */     case 3: 
/* 264:369 */       return " WMF";
/* 265:    */     case 4: 
/* 266:370 */       return " PICT";
/* 267:    */     case 5: 
/* 268:371 */       return " JPEG";
/* 269:    */     case 6: 
/* 270:372 */       return " PNG";
/* 271:    */     case 7: 
/* 272:373 */       return " DIB";
/* 273:    */     }
/* 274:375 */     if (b < 32) {
/* 275:376 */       return " NotKnown";
/* 276:    */     }
/* 277:378 */     return " Client";
/* 278:    */   }
/* 279:    */   
/* 280:    */   protected Object[][] getAttributeMap()
/* 281:    */   {
/* 282:383 */     return new Object[][] { { "BlipTypeWin32", Byte.valueOf(this.field_1_blipTypeWin32) }, { "BlipTypeMacOS", Byte.valueOf(this.field_2_blipTypeMacOS) }, { "SUID", this.field_3_uid }, { "Tag", Short.valueOf(this.field_4_tag) }, { "Size", Integer.valueOf(this.field_5_size) }, { "Ref", Integer.valueOf(this.field_6_ref) }, { "Offset", Integer.valueOf(this.field_7_offset) }, { "Usage", Byte.valueOf(this.field_8_usage) }, { "Name", Byte.valueOf(this.field_9_name) }, { "Unused2", Byte.valueOf(this.field_10_unused2) }, { "Unused3", Byte.valueOf(this.field_11_unused3) }, { "Blip Record", this.field_12_blipRecord }, { "Extra Data", this._remainingData } };
/* 283:    */   }
/* 284:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherBSERecord
 * JD-Core Version:    0.7.0.1
 */