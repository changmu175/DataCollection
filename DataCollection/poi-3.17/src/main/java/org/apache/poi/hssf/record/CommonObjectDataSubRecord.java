/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.BitField;
/*   4:    */ import org.apache.poi.util.BitFieldFactory;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianInput;
/*   7:    */ import org.apache.poi.util.LittleEndianOutput;
/*   8:    */ import org.apache.poi.util.RecordFormatException;
/*   9:    */ 
/*  10:    */ public final class CommonObjectDataSubRecord
/*  11:    */   extends SubRecord
/*  12:    */   implements Cloneable
/*  13:    */ {
/*  14:    */   public static final short sid = 21;
/*  15: 33 */   private static final BitField locked = BitFieldFactory.getInstance(1);
/*  16: 34 */   private static final BitField printable = BitFieldFactory.getInstance(16);
/*  17: 35 */   private static final BitField autofill = BitFieldFactory.getInstance(8192);
/*  18: 36 */   private static final BitField autoline = BitFieldFactory.getInstance(16384);
/*  19:    */   public static final short OBJECT_TYPE_GROUP = 0;
/*  20:    */   public static final short OBJECT_TYPE_LINE = 1;
/*  21:    */   public static final short OBJECT_TYPE_RECTANGLE = 2;
/*  22:    */   public static final short OBJECT_TYPE_OVAL = 3;
/*  23:    */   public static final short OBJECT_TYPE_ARC = 4;
/*  24:    */   public static final short OBJECT_TYPE_CHART = 5;
/*  25:    */   public static final short OBJECT_TYPE_TEXT = 6;
/*  26:    */   public static final short OBJECT_TYPE_BUTTON = 7;
/*  27:    */   public static final short OBJECT_TYPE_PICTURE = 8;
/*  28:    */   public static final short OBJECT_TYPE_POLYGON = 9;
/*  29:    */   public static final short OBJECT_TYPE_RESERVED1 = 10;
/*  30:    */   public static final short OBJECT_TYPE_CHECKBOX = 11;
/*  31:    */   public static final short OBJECT_TYPE_OPTION_BUTTON = 12;
/*  32:    */   public static final short OBJECT_TYPE_EDIT_BOX = 13;
/*  33:    */   public static final short OBJECT_TYPE_LABEL = 14;
/*  34:    */   public static final short OBJECT_TYPE_DIALOG_BOX = 15;
/*  35:    */   public static final short OBJECT_TYPE_SPINNER = 16;
/*  36:    */   public static final short OBJECT_TYPE_SCROLL_BAR = 17;
/*  37:    */   public static final short OBJECT_TYPE_LIST_BOX = 18;
/*  38:    */   public static final short OBJECT_TYPE_GROUP_BOX = 19;
/*  39:    */   public static final short OBJECT_TYPE_COMBO_BOX = 20;
/*  40:    */   public static final short OBJECT_TYPE_RESERVED2 = 21;
/*  41:    */   public static final short OBJECT_TYPE_RESERVED3 = 22;
/*  42:    */   public static final short OBJECT_TYPE_RESERVED4 = 23;
/*  43:    */   public static final short OBJECT_TYPE_RESERVED5 = 24;
/*  44:    */   public static final short OBJECT_TYPE_COMMENT = 25;
/*  45:    */   public static final short OBJECT_TYPE_RESERVED6 = 26;
/*  46:    */   public static final short OBJECT_TYPE_RESERVED7 = 27;
/*  47:    */   public static final short OBJECT_TYPE_RESERVED8 = 28;
/*  48:    */   public static final short OBJECT_TYPE_RESERVED9 = 29;
/*  49:    */   public static final short OBJECT_TYPE_MICROSOFT_OFFICE_DRAWING = 30;
/*  50:    */   private short field_1_objectType;
/*  51:    */   private int field_2_objectId;
/*  52:    */   private short field_3_option;
/*  53:    */   private int field_4_reserved1;
/*  54:    */   private int field_5_reserved2;
/*  55:    */   private int field_6_reserved3;
/*  56:    */   
/*  57:    */   public CommonObjectDataSubRecord() {}
/*  58:    */   
/*  59:    */   public CommonObjectDataSubRecord(LittleEndianInput in, int size)
/*  60:    */   {
/*  61: 84 */     if (size != 18) {
/*  62: 85 */       throw new RecordFormatException("Expected size 18 but got (" + size + ")");
/*  63:    */     }
/*  64: 87 */     this.field_1_objectType = in.readShort();
/*  65: 88 */     this.field_2_objectId = in.readUShort();
/*  66: 89 */     this.field_3_option = in.readShort();
/*  67: 90 */     this.field_4_reserved1 = in.readInt();
/*  68: 91 */     this.field_5_reserved2 = in.readInt();
/*  69: 92 */     this.field_6_reserved3 = in.readInt();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public String toString()
/*  73:    */   {
/*  74: 98 */     StringBuffer buffer = new StringBuffer();
/*  75:    */     
/*  76:100 */     buffer.append("[ftCmo]\n");
/*  77:101 */     buffer.append("    .objectType           = ").append("0x").append(HexDump.toHex(getObjectType())).append(" (").append(getObjectType()).append(" )");
/*  78:    */     
/*  79:    */ 
/*  80:104 */     buffer.append(System.getProperty("line.separator"));
/*  81:105 */     buffer.append("    .objectId             = ").append("0x").append(HexDump.toHex(getObjectId())).append(" (").append(getObjectId()).append(" )");
/*  82:    */     
/*  83:    */ 
/*  84:108 */     buffer.append(System.getProperty("line.separator"));
/*  85:109 */     buffer.append("    .option               = ").append("0x").append(HexDump.toHex(getOption())).append(" (").append(getOption()).append(" )");
/*  86:    */     
/*  87:    */ 
/*  88:112 */     buffer.append(System.getProperty("line.separator"));
/*  89:113 */     buffer.append("         .locked                   = ").append(isLocked()).append('\n');
/*  90:114 */     buffer.append("         .printable                = ").append(isPrintable()).append('\n');
/*  91:115 */     buffer.append("         .autofill                 = ").append(isAutofill()).append('\n');
/*  92:116 */     buffer.append("         .autoline                 = ").append(isAutoline()).append('\n');
/*  93:117 */     buffer.append("    .reserved1            = ").append("0x").append(HexDump.toHex(getReserved1())).append(" (").append(getReserved1()).append(" )");
/*  94:    */     
/*  95:    */ 
/*  96:120 */     buffer.append(System.getProperty("line.separator"));
/*  97:121 */     buffer.append("    .reserved2            = ").append("0x").append(HexDump.toHex(getReserved2())).append(" (").append(getReserved2()).append(" )");
/*  98:    */     
/*  99:    */ 
/* 100:124 */     buffer.append(System.getProperty("line.separator"));
/* 101:125 */     buffer.append("    .reserved3            = ").append("0x").append(HexDump.toHex(getReserved3())).append(" (").append(getReserved3()).append(" )");
/* 102:    */     
/* 103:    */ 
/* 104:128 */     buffer.append(System.getProperty("line.separator"));
/* 105:    */     
/* 106:130 */     buffer.append("[/ftCmo]\n");
/* 107:131 */     return buffer.toString();
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void serialize(LittleEndianOutput out)
/* 111:    */   {
/* 112:137 */     out.writeShort(21);
/* 113:138 */     out.writeShort(getDataSize());
/* 114:    */     
/* 115:140 */     out.writeShort(this.field_1_objectType);
/* 116:141 */     out.writeShort(this.field_2_objectId);
/* 117:142 */     out.writeShort(this.field_3_option);
/* 118:143 */     out.writeInt(this.field_4_reserved1);
/* 119:144 */     out.writeInt(this.field_5_reserved2);
/* 120:145 */     out.writeInt(this.field_6_reserved3);
/* 121:    */   }
/* 122:    */   
/* 123:    */   protected int getDataSize()
/* 124:    */   {
/* 125:150 */     return 18;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public short getSid()
/* 129:    */   {
/* 130:158 */     return 21;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public CommonObjectDataSubRecord clone()
/* 134:    */   {
/* 135:163 */     CommonObjectDataSubRecord rec = new CommonObjectDataSubRecord();
/* 136:    */     
/* 137:165 */     rec.field_1_objectType = this.field_1_objectType;
/* 138:166 */     rec.field_2_objectId = this.field_2_objectId;
/* 139:167 */     rec.field_3_option = this.field_3_option;
/* 140:168 */     rec.field_4_reserved1 = this.field_4_reserved1;
/* 141:169 */     rec.field_5_reserved2 = this.field_5_reserved2;
/* 142:170 */     rec.field_6_reserved3 = this.field_6_reserved3;
/* 143:171 */     return rec;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public short getObjectType()
/* 147:    */   {
/* 148:213 */     return this.field_1_objectType;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public void setObjectType(short field_1_objectType)
/* 152:    */   {
/* 153:255 */     this.field_1_objectType = field_1_objectType;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public int getObjectId()
/* 157:    */   {
/* 158:265 */     return this.field_2_objectId;
/* 159:    */   }
/* 160:    */   
/* 161:    */   public void setObjectId(int field_2_objectId)
/* 162:    */   {
/* 163:275 */     this.field_2_objectId = field_2_objectId;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public short getOption()
/* 167:    */   {
/* 168:285 */     return this.field_3_option;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public void setOption(short field_3_option)
/* 172:    */   {
/* 173:295 */     this.field_3_option = field_3_option;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public int getReserved1()
/* 177:    */   {
/* 178:305 */     return this.field_4_reserved1;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public void setReserved1(int field_4_reserved1)
/* 182:    */   {
/* 183:315 */     this.field_4_reserved1 = field_4_reserved1;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public int getReserved2()
/* 187:    */   {
/* 188:325 */     return this.field_5_reserved2;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public void setReserved2(int field_5_reserved2)
/* 192:    */   {
/* 193:335 */     this.field_5_reserved2 = field_5_reserved2;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public int getReserved3()
/* 197:    */   {
/* 198:345 */     return this.field_6_reserved3;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public void setReserved3(int field_6_reserved3)
/* 202:    */   {
/* 203:355 */     this.field_6_reserved3 = field_6_reserved3;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public void setLocked(boolean value)
/* 207:    */   {
/* 208:366 */     this.field_3_option = locked.setShortBoolean(this.field_3_option, value);
/* 209:    */   }
/* 210:    */   
/* 211:    */   public boolean isLocked()
/* 212:    */   {
/* 213:375 */     return locked.isSet(this.field_3_option);
/* 214:    */   }
/* 215:    */   
/* 216:    */   public void setPrintable(boolean value)
/* 217:    */   {
/* 218:386 */     this.field_3_option = printable.setShortBoolean(this.field_3_option, value);
/* 219:    */   }
/* 220:    */   
/* 221:    */   public boolean isPrintable()
/* 222:    */   {
/* 223:395 */     return printable.isSet(this.field_3_option);
/* 224:    */   }
/* 225:    */   
/* 226:    */   public void setAutofill(boolean value)
/* 227:    */   {
/* 228:406 */     this.field_3_option = autofill.setShortBoolean(this.field_3_option, value);
/* 229:    */   }
/* 230:    */   
/* 231:    */   public boolean isAutofill()
/* 232:    */   {
/* 233:415 */     return autofill.isSet(this.field_3_option);
/* 234:    */   }
/* 235:    */   
/* 236:    */   public void setAutoline(boolean value)
/* 237:    */   {
/* 238:426 */     this.field_3_option = autoline.setShortBoolean(this.field_3_option, value);
/* 239:    */   }
/* 240:    */   
/* 241:    */   public boolean isAutoline()
/* 242:    */   {
/* 243:435 */     return autoline.isSet(this.field_3_option);
/* 244:    */   }
/* 245:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.CommonObjectDataSubRecord
 * JD-Core Version:    0.7.0.1
 */