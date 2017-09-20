/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.cont.ContinuableRecord;
/*   4:    */ import org.apache.poi.hssf.record.cont.ContinuableRecordOutput;
/*   5:    */ import org.apache.poi.hssf.usermodel.HSSFRichTextString;
/*   6:    */ import org.apache.poi.ss.formula.ptg.OperandPtg;
/*   7:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   8:    */ import org.apache.poi.util.BitField;
/*   9:    */ import org.apache.poi.util.BitFieldFactory;
/*  10:    */ import org.apache.poi.util.HexDump;
/*  11:    */ import org.apache.poi.util.RecordFormatException;
/*  12:    */ 
/*  13:    */ public final class TextObjectRecord
/*  14:    */   extends ContinuableRecord
/*  15:    */ {
/*  16:    */   public static final short sid = 438;
/*  17:    */   private static final int FORMAT_RUN_ENCODED_SIZE = 8;
/*  18: 41 */   private static final BitField HorizontalTextAlignment = BitFieldFactory.getInstance(14);
/*  19: 42 */   private static final BitField VerticalTextAlignment = BitFieldFactory.getInstance(112);
/*  20: 43 */   private static final BitField textLocked = BitFieldFactory.getInstance(512);
/*  21:    */   public static final short HORIZONTAL_TEXT_ALIGNMENT_LEFT_ALIGNED = 1;
/*  22:    */   public static final short HORIZONTAL_TEXT_ALIGNMENT_CENTERED = 2;
/*  23:    */   public static final short HORIZONTAL_TEXT_ALIGNMENT_RIGHT_ALIGNED = 3;
/*  24:    */   public static final short HORIZONTAL_TEXT_ALIGNMENT_JUSTIFIED = 4;
/*  25:    */   public static final short VERTICAL_TEXT_ALIGNMENT_TOP = 1;
/*  26:    */   public static final short VERTICAL_TEXT_ALIGNMENT_CENTER = 2;
/*  27:    */   public static final short VERTICAL_TEXT_ALIGNMENT_BOTTOM = 3;
/*  28:    */   public static final short VERTICAL_TEXT_ALIGNMENT_JUSTIFY = 4;
/*  29:    */   public static final short TEXT_ORIENTATION_NONE = 0;
/*  30:    */   public static final short TEXT_ORIENTATION_TOP_TO_BOTTOM = 1;
/*  31:    */   public static final short TEXT_ORIENTATION_ROT_RIGHT = 2;
/*  32:    */   public static final short TEXT_ORIENTATION_ROT_LEFT = 3;
/*  33:    */   private int field_1_options;
/*  34:    */   private int field_2_textOrientation;
/*  35:    */   private int field_3_reserved4;
/*  36:    */   private int field_4_reserved5;
/*  37:    */   private int field_5_reserved6;
/*  38:    */   private int field_8_reserved7;
/*  39:    */   private HSSFRichTextString _text;
/*  40:    */   private int _unknownPreFormulaInt;
/*  41:    */   private OperandPtg _linkRefPtg;
/*  42:    */   private Byte _unknownPostFormulaByte;
/*  43:    */   
/*  44:    */   public TextObjectRecord() {}
/*  45:    */   
/*  46:    */   public TextObjectRecord(RecordInputStream in)
/*  47:    */   {
/*  48: 88 */     this.field_1_options = in.readUShort();
/*  49: 89 */     this.field_2_textOrientation = in.readUShort();
/*  50: 90 */     this.field_3_reserved4 = in.readUShort();
/*  51: 91 */     this.field_4_reserved5 = in.readUShort();
/*  52: 92 */     this.field_5_reserved6 = in.readUShort();
/*  53: 93 */     int field_6_textLength = in.readUShort();
/*  54: 94 */     int field_7_formattingDataLength = in.readUShort();
/*  55: 95 */     this.field_8_reserved7 = in.readInt();
/*  56: 97 */     if (in.remaining() > 0)
/*  57:    */     {
/*  58:100 */       if (in.remaining() < 11) {
/*  59:101 */         throw new RecordFormatException("Not enough remaining data for a link formula");
/*  60:    */       }
/*  61:103 */       int formulaSize = in.readUShort();
/*  62:104 */       this._unknownPreFormulaInt = in.readInt();
/*  63:105 */       Ptg[] ptgs = Ptg.readTokens(formulaSize, in);
/*  64:106 */       if (ptgs.length != 1) {
/*  65:107 */         throw new RecordFormatException("Read " + ptgs.length + " tokens but expected exactly 1");
/*  66:    */       }
/*  67:110 */       this._linkRefPtg = ((OperandPtg)ptgs[0]);
/*  68:111 */       if (in.remaining() > 0) {
/*  69:112 */         this._unknownPostFormulaByte = Byte.valueOf(in.readByte());
/*  70:    */       } else {
/*  71:114 */         this._unknownPostFormulaByte = null;
/*  72:    */       }
/*  73:    */     }
/*  74:    */     else
/*  75:    */     {
/*  76:117 */       this._linkRefPtg = null;
/*  77:    */     }
/*  78:119 */     if (in.remaining() > 0) {
/*  79:120 */       throw new RecordFormatException("Unused " + in.remaining() + " bytes at end of record");
/*  80:    */     }
/*  81:    */     String text;
/*  82:    */     String text;
/*  83:124 */     if (field_6_textLength > 0) {
/*  84:125 */       text = readRawString(in, field_6_textLength);
/*  85:    */     } else {
/*  86:127 */       text = "";
/*  87:    */     }
/*  88:129 */     this._text = new HSSFRichTextString(text);
/*  89:131 */     if (field_7_formattingDataLength > 0) {
/*  90:132 */       processFontRuns(in, this._text, field_7_formattingDataLength);
/*  91:    */     }
/*  92:    */   }
/*  93:    */   
/*  94:    */   private static String readRawString(RecordInputStream in, int textLength)
/*  95:    */   {
/*  96:137 */     byte compressByte = in.readByte();
/*  97:138 */     boolean isCompressed = (compressByte & 0x1) == 0;
/*  98:139 */     if (isCompressed) {
/*  99:140 */       return in.readCompressedUnicode(textLength);
/* 100:    */     }
/* 101:142 */     return in.readUnicodeLEString(textLength);
/* 102:    */   }
/* 103:    */   
/* 104:    */   private static void processFontRuns(RecordInputStream in, HSSFRichTextString str, int formattingRunDataLength)
/* 105:    */   {
/* 106:147 */     if (formattingRunDataLength % 8 != 0) {
/* 107:148 */       throw new RecordFormatException("Bad format run data length " + formattingRunDataLength + ")");
/* 108:    */     }
/* 109:151 */     int nRuns = formattingRunDataLength / 8;
/* 110:152 */     for (int i = 0; i < nRuns; i++)
/* 111:    */     {
/* 112:153 */       short index = in.readShort();
/* 113:154 */       short iFont = in.readShort();
/* 114:155 */       in.readInt();
/* 115:156 */       str.applyFont(index, str.length(), iFont);
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   public short getSid()
/* 120:    */   {
/* 121:161 */     return 438;
/* 122:    */   }
/* 123:    */   
/* 124:    */   private void serializeTXORecord(ContinuableRecordOutput out)
/* 125:    */   {
/* 126:166 */     out.writeShort(this.field_1_options);
/* 127:167 */     out.writeShort(this.field_2_textOrientation);
/* 128:168 */     out.writeShort(this.field_3_reserved4);
/* 129:169 */     out.writeShort(this.field_4_reserved5);
/* 130:170 */     out.writeShort(this.field_5_reserved6);
/* 131:171 */     out.writeShort(this._text.length());
/* 132:172 */     out.writeShort(getFormattingDataLength());
/* 133:173 */     out.writeInt(this.field_8_reserved7);
/* 134:175 */     if (this._linkRefPtg != null)
/* 135:    */     {
/* 136:176 */       int formulaSize = this._linkRefPtg.getSize();
/* 137:177 */       out.writeShort(formulaSize);
/* 138:178 */       out.writeInt(this._unknownPreFormulaInt);
/* 139:179 */       this._linkRefPtg.write(out);
/* 140:180 */       if (this._unknownPostFormulaByte != null) {
/* 141:181 */         out.writeByte(this._unknownPostFormulaByte.byteValue());
/* 142:    */       }
/* 143:    */     }
/* 144:    */   }
/* 145:    */   
/* 146:    */   private void serializeTrailingRecords(ContinuableRecordOutput out)
/* 147:    */   {
/* 148:187 */     out.writeContinue();
/* 149:188 */     out.writeStringData(this._text.getString());
/* 150:189 */     out.writeContinue();
/* 151:190 */     writeFormatData(out, this._text);
/* 152:    */   }
/* 153:    */   
/* 154:    */   protected void serialize(ContinuableRecordOutput out)
/* 155:    */   {
/* 156:195 */     serializeTXORecord(out);
/* 157:196 */     if (this._text.getString().length() > 0) {
/* 158:197 */       serializeTrailingRecords(out);
/* 159:    */     }
/* 160:    */   }
/* 161:    */   
/* 162:    */   private int getFormattingDataLength()
/* 163:    */   {
/* 164:202 */     if (this._text.length() < 1) {
/* 165:204 */       return 0;
/* 166:    */     }
/* 167:206 */     return (this._text.numFormattingRuns() + 1) * 8;
/* 168:    */   }
/* 169:    */   
/* 170:    */   private static void writeFormatData(ContinuableRecordOutput out, HSSFRichTextString str)
/* 171:    */   {
/* 172:210 */     int nRuns = str.numFormattingRuns();
/* 173:211 */     for (int i = 0; i < nRuns; i++)
/* 174:    */     {
/* 175:212 */       out.writeShort(str.getIndexOfFormattingRun(i));
/* 176:213 */       int fontIndex = str.getFontOfFormattingRun(i);
/* 177:214 */       out.writeShort(fontIndex == 0 ? 0 : fontIndex);
/* 178:215 */       out.writeInt(0);
/* 179:    */     }
/* 180:217 */     out.writeShort(str.length());
/* 181:218 */     out.writeShort(0);
/* 182:219 */     out.writeInt(0);
/* 183:    */   }
/* 184:    */   
/* 185:    */   public void setHorizontalTextAlignment(int value)
/* 186:    */   {
/* 187:226 */     this.field_1_options = HorizontalTextAlignment.setValue(this.field_1_options, value);
/* 188:    */   }
/* 189:    */   
/* 190:    */   public int getHorizontalTextAlignment()
/* 191:    */   {
/* 192:233 */     return HorizontalTextAlignment.getValue(this.field_1_options);
/* 193:    */   }
/* 194:    */   
/* 195:    */   public void setVerticalTextAlignment(int value)
/* 196:    */   {
/* 197:240 */     this.field_1_options = VerticalTextAlignment.setValue(this.field_1_options, value);
/* 198:    */   }
/* 199:    */   
/* 200:    */   public int getVerticalTextAlignment()
/* 201:    */   {
/* 202:247 */     return VerticalTextAlignment.getValue(this.field_1_options);
/* 203:    */   }
/* 204:    */   
/* 205:    */   public void setTextLocked(boolean value)
/* 206:    */   {
/* 207:254 */     this.field_1_options = textLocked.setBoolean(this.field_1_options, value);
/* 208:    */   }
/* 209:    */   
/* 210:    */   public boolean isTextLocked()
/* 211:    */   {
/* 212:261 */     return textLocked.isSet(this.field_1_options);
/* 213:    */   }
/* 214:    */   
/* 215:    */   public int getTextOrientation()
/* 216:    */   {
/* 217:271 */     return this.field_2_textOrientation;
/* 218:    */   }
/* 219:    */   
/* 220:    */   public void setTextOrientation(int textOrientation)
/* 221:    */   {
/* 222:282 */     this.field_2_textOrientation = textOrientation;
/* 223:    */   }
/* 224:    */   
/* 225:    */   public HSSFRichTextString getStr()
/* 226:    */   {
/* 227:286 */     return this._text;
/* 228:    */   }
/* 229:    */   
/* 230:    */   public void setStr(HSSFRichTextString str)
/* 231:    */   {
/* 232:290 */     this._text = str;
/* 233:    */   }
/* 234:    */   
/* 235:    */   public Ptg getLinkRefPtg()
/* 236:    */   {
/* 237:294 */     return this._linkRefPtg;
/* 238:    */   }
/* 239:    */   
/* 240:    */   public String toString()
/* 241:    */   {
/* 242:298 */     StringBuffer sb = new StringBuffer();
/* 243:    */     
/* 244:300 */     sb.append("[TXO]\n");
/* 245:301 */     sb.append("    .options        = ").append(HexDump.shortToHex(this.field_1_options)).append("\n");
/* 246:302 */     sb.append("         .isHorizontal = ").append(getHorizontalTextAlignment()).append('\n');
/* 247:303 */     sb.append("         .isVertical   = ").append(getVerticalTextAlignment()).append('\n');
/* 248:304 */     sb.append("         .textLocked   = ").append(isTextLocked()).append('\n');
/* 249:305 */     sb.append("    .textOrientation= ").append(HexDump.shortToHex(getTextOrientation())).append("\n");
/* 250:306 */     sb.append("    .reserved4      = ").append(HexDump.shortToHex(this.field_3_reserved4)).append("\n");
/* 251:307 */     sb.append("    .reserved5      = ").append(HexDump.shortToHex(this.field_4_reserved5)).append("\n");
/* 252:308 */     sb.append("    .reserved6      = ").append(HexDump.shortToHex(this.field_5_reserved6)).append("\n");
/* 253:309 */     sb.append("    .textLength     = ").append(HexDump.shortToHex(this._text.length())).append("\n");
/* 254:310 */     sb.append("    .reserved7      = ").append(HexDump.intToHex(this.field_8_reserved7)).append("\n");
/* 255:    */     
/* 256:312 */     sb.append("    .string = ").append(this._text).append('\n');
/* 257:314 */     for (int i = 0; i < this._text.numFormattingRuns(); i++) {
/* 258:315 */       sb.append("    .textrun = ").append(this._text.getFontOfFormattingRun(i)).append('\n');
/* 259:    */     }
/* 260:318 */     sb.append("[/TXO]\n");
/* 261:319 */     return sb.toString();
/* 262:    */   }
/* 263:    */   
/* 264:    */   public Object clone()
/* 265:    */   {
/* 266:324 */     TextObjectRecord rec = new TextObjectRecord();
/* 267:325 */     rec._text = this._text;
/* 268:    */     
/* 269:327 */     rec.field_1_options = this.field_1_options;
/* 270:328 */     rec.field_2_textOrientation = this.field_2_textOrientation;
/* 271:329 */     rec.field_3_reserved4 = this.field_3_reserved4;
/* 272:330 */     rec.field_4_reserved5 = this.field_4_reserved5;
/* 273:331 */     rec.field_5_reserved6 = this.field_5_reserved6;
/* 274:332 */     rec.field_8_reserved7 = this.field_8_reserved7;
/* 275:    */     
/* 276:334 */     rec._text = this._text;
/* 277:336 */     if (this._linkRefPtg != null)
/* 278:    */     {
/* 279:337 */       rec._unknownPreFormulaInt = this._unknownPreFormulaInt;
/* 280:338 */       rec._linkRefPtg = this._linkRefPtg.copy();
/* 281:339 */       rec._unknownPostFormulaByte = this._unknownPostFormulaByte;
/* 282:    */     }
/* 283:341 */     return rec;
/* 284:    */   }
/* 285:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.TextObjectRecord
 * JD-Core Version:    0.7.0.1
 */