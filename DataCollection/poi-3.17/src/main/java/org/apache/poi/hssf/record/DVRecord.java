/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.common.UnicodeString;
/*   4:    */ import org.apache.poi.ss.formula.Formula;
/*   5:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   6:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*   7:    */ import org.apache.poi.ss.util.CellRangeAddressList;
/*   8:    */ import org.apache.poi.util.BitField;
/*   9:    */ import org.apache.poi.util.LittleEndianOutput;
/*  10:    */ import org.apache.poi.util.StringUtil;
/*  11:    */ 
/*  12:    */ public final class DVRecord
/*  13:    */   extends StandardRecord
/*  14:    */   implements Cloneable
/*  15:    */ {
/*  16:    */   public static final short sid = 446;
/*  17: 41 */   private static final UnicodeString NULL_TEXT_STRING = new UnicodeString("");
/*  18:    */   private int _option_flags;
/*  19:    */   private UnicodeString _promptTitle;
/*  20:    */   private UnicodeString _errorTitle;
/*  21:    */   private UnicodeString _promptText;
/*  22:    */   private UnicodeString _errorText;
/*  23: 54 */   private short _not_used_1 = 16352;
/*  24:    */   private Formula _formula1;
/*  25: 58 */   private short _not_used_2 = 0;
/*  26:    */   private Formula _formula2;
/*  27:    */   private CellRangeAddressList _regions;
/*  28: 69 */   private static final BitField opt_data_type = new BitField(15);
/*  29: 70 */   private static final BitField opt_error_style = new BitField(112);
/*  30: 71 */   private static final BitField opt_string_list_formula = new BitField(128);
/*  31: 72 */   private static final BitField opt_empty_cell_allowed = new BitField(256);
/*  32: 73 */   private static final BitField opt_suppress_dropdown_arrow = new BitField(512);
/*  33: 74 */   private static final BitField opt_show_prompt_on_cell_selected = new BitField(262144);
/*  34: 75 */   private static final BitField opt_show_error_on_invalid_value = new BitField(524288);
/*  35: 76 */   private static final BitField opt_condition_operator = new BitField(7340032);
/*  36:    */   
/*  37:    */   public DVRecord(int validationType, int operator, int errorStyle, boolean emptyCellAllowed, boolean suppressDropDownArrow, boolean isExplicitList, boolean showPromptBox, String promptTitle, String promptText, boolean showErrorBox, String errorTitle, String errorText, Ptg[] formula1, Ptg[] formula2, CellRangeAddressList regions)
/*  38:    */   {
/*  39: 86 */     if ((promptTitle != null) && (promptTitle.length() > 32)) {
/*  40: 87 */       throw new IllegalStateException("Prompt-title cannot be longer than 32 characters, but had: " + promptTitle);
/*  41:    */     }
/*  42: 89 */     if ((promptText != null) && (promptText.length() > 255)) {
/*  43: 90 */       throw new IllegalStateException("Prompt-text cannot be longer than 255 characters, but had: " + promptText);
/*  44:    */     }
/*  45: 93 */     if ((errorTitle != null) && (errorTitle.length() > 32)) {
/*  46: 94 */       throw new IllegalStateException("Error-title cannot be longer than 32 characters, but had: " + errorTitle);
/*  47:    */     }
/*  48: 96 */     if ((errorText != null) && (errorText.length() > 255)) {
/*  49: 97 */       throw new IllegalStateException("Error-text cannot be longer than 255 characters, but had: " + errorText);
/*  50:    */     }
/*  51:100 */     int flags = 0;
/*  52:101 */     flags = opt_data_type.setValue(flags, validationType);
/*  53:102 */     flags = opt_condition_operator.setValue(flags, operator);
/*  54:103 */     flags = opt_error_style.setValue(flags, errorStyle);
/*  55:104 */     flags = opt_empty_cell_allowed.setBoolean(flags, emptyCellAllowed);
/*  56:105 */     flags = opt_suppress_dropdown_arrow.setBoolean(flags, suppressDropDownArrow);
/*  57:106 */     flags = opt_string_list_formula.setBoolean(flags, isExplicitList);
/*  58:107 */     flags = opt_show_prompt_on_cell_selected.setBoolean(flags, showPromptBox);
/*  59:108 */     flags = opt_show_error_on_invalid_value.setBoolean(flags, showErrorBox);
/*  60:109 */     this._option_flags = flags;
/*  61:110 */     this._promptTitle = resolveTitleText(promptTitle);
/*  62:111 */     this._promptText = resolveTitleText(promptText);
/*  63:112 */     this._errorTitle = resolveTitleText(errorTitle);
/*  64:113 */     this._errorText = resolveTitleText(errorText);
/*  65:114 */     this._formula1 = Formula.create(formula1);
/*  66:115 */     this._formula2 = Formula.create(formula2);
/*  67:116 */     this._regions = regions;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public DVRecord(RecordInputStream in)
/*  71:    */   {
/*  72:121 */     this._option_flags = in.readInt();
/*  73:    */     
/*  74:123 */     this._promptTitle = readUnicodeString(in);
/*  75:124 */     this._errorTitle = readUnicodeString(in);
/*  76:125 */     this._promptText = readUnicodeString(in);
/*  77:126 */     this._errorText = readUnicodeString(in);
/*  78:    */     
/*  79:128 */     int field_size_first_formula = in.readUShort();
/*  80:129 */     this._not_used_1 = in.readShort();
/*  81:    */     
/*  82:    */ 
/*  83:    */ 
/*  84:    */ 
/*  85:134 */     this._formula1 = Formula.read(field_size_first_formula, in);
/*  86:    */     
/*  87:136 */     int field_size_sec_formula = in.readUShort();
/*  88:137 */     this._not_used_2 = in.readShort();
/*  89:    */     
/*  90:    */ 
/*  91:140 */     this._formula2 = Formula.read(field_size_sec_formula, in);
/*  92:    */     
/*  93:    */ 
/*  94:143 */     this._regions = new CellRangeAddressList(in);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public int getDataType()
/*  98:    */   {
/*  99:152 */     return opt_data_type.getValue(this._option_flags);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public int getErrorStyle()
/* 103:    */   {
/* 104:160 */     return opt_error_style.getValue(this._option_flags);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public boolean getListExplicitFormula()
/* 108:    */   {
/* 109:168 */     return opt_string_list_formula.isSet(this._option_flags);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public boolean getEmptyCellAllowed()
/* 113:    */   {
/* 114:175 */     return opt_empty_cell_allowed.isSet(this._option_flags);
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean getSuppressDropdownArrow()
/* 118:    */   {
/* 119:184 */     return opt_suppress_dropdown_arrow.isSet(this._option_flags);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public boolean getShowPromptOnCellSelected()
/* 123:    */   {
/* 124:191 */     return opt_show_prompt_on_cell_selected.isSet(this._option_flags);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public boolean getShowErrorOnInvalidValue()
/* 128:    */   {
/* 129:199 */     return opt_show_error_on_invalid_value.isSet(this._option_flags);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public int getConditionOperator()
/* 133:    */   {
/* 134:208 */     return opt_condition_operator.getValue(this._option_flags);
/* 135:    */   }
/* 136:    */   
/* 137:    */   public String getPromptTitle()
/* 138:    */   {
/* 139:213 */     return resolveTitleString(this._promptTitle);
/* 140:    */   }
/* 141:    */   
/* 142:    */   public String getErrorTitle()
/* 143:    */   {
/* 144:217 */     return resolveTitleString(this._errorTitle);
/* 145:    */   }
/* 146:    */   
/* 147:    */   public String getPromptText()
/* 148:    */   {
/* 149:221 */     return resolveTitleString(this._promptText);
/* 150:    */   }
/* 151:    */   
/* 152:    */   public String getErrorText()
/* 153:    */   {
/* 154:225 */     return resolveTitleString(this._errorText);
/* 155:    */   }
/* 156:    */   
/* 157:    */   public Ptg[] getFormula1()
/* 158:    */   {
/* 159:229 */     return Formula.getTokens(this._formula1);
/* 160:    */   }
/* 161:    */   
/* 162:    */   public Ptg[] getFormula2()
/* 163:    */   {
/* 164:233 */     return Formula.getTokens(this._formula2);
/* 165:    */   }
/* 166:    */   
/* 167:    */   public CellRangeAddressList getCellRangeAddress()
/* 168:    */   {
/* 169:237 */     return this._regions;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public String toString()
/* 173:    */   {
/* 174:242 */     StringBuffer sb = new StringBuffer();
/* 175:243 */     sb.append("[DV]\n");
/* 176:244 */     sb.append(" options=").append(Integer.toHexString(this._option_flags));
/* 177:245 */     sb.append(" title-prompt=").append(formatTextTitle(this._promptTitle));
/* 178:246 */     sb.append(" title-error=").append(formatTextTitle(this._errorTitle));
/* 179:247 */     sb.append(" text-prompt=").append(formatTextTitle(this._promptText));
/* 180:248 */     sb.append(" text-error=").append(formatTextTitle(this._errorText));
/* 181:249 */     sb.append("\n");
/* 182:250 */     appendFormula(sb, "Formula 1:", this._formula1);
/* 183:251 */     appendFormula(sb, "Formula 2:", this._formula2);
/* 184:252 */     sb.append("Regions: ");
/* 185:253 */     int nRegions = this._regions.countRanges();
/* 186:254 */     for (int i = 0; i < nRegions; i++)
/* 187:    */     {
/* 188:255 */       if (i > 0) {
/* 189:256 */         sb.append(", ");
/* 190:    */       }
/* 191:258 */       CellRangeAddress addr = this._regions.getCellRangeAddress(i);
/* 192:259 */       sb.append('(').append(addr.getFirstRow()).append(',').append(addr.getLastRow());
/* 193:260 */       sb.append(',').append(addr.getFirstColumn()).append(',').append(addr.getLastColumn()).append(')');
/* 194:    */     }
/* 195:262 */     sb.append("\n");
/* 196:263 */     sb.append("[/DV]");
/* 197:    */     
/* 198:265 */     return sb.toString();
/* 199:    */   }
/* 200:    */   
/* 201:    */   private static String formatTextTitle(UnicodeString us)
/* 202:    */   {
/* 203:269 */     String str = us.getString();
/* 204:270 */     if ((str.length() == 1) && (str.charAt(0) == 0)) {
/* 205:271 */       return "'\\0'";
/* 206:    */     }
/* 207:273 */     return str;
/* 208:    */   }
/* 209:    */   
/* 210:    */   private static void appendFormula(StringBuffer sb, String label, Formula f)
/* 211:    */   {
/* 212:277 */     sb.append(label);
/* 213:279 */     if (f == null)
/* 214:    */     {
/* 215:280 */       sb.append("<empty>\n");
/* 216:281 */       return;
/* 217:    */     }
/* 218:283 */     Ptg[] ptgs = f.getTokens();
/* 219:284 */     sb.append('\n');
/* 220:285 */     for (Ptg ptg : ptgs) {
/* 221:286 */       sb.append('\t').append(ptg).append('\n');
/* 222:    */     }
/* 223:    */   }
/* 224:    */   
/* 225:    */   public void serialize(LittleEndianOutput out)
/* 226:    */   {
/* 227:292 */     out.writeInt(this._option_flags);
/* 228:    */     
/* 229:294 */     serializeUnicodeString(this._promptTitle, out);
/* 230:295 */     serializeUnicodeString(this._errorTitle, out);
/* 231:296 */     serializeUnicodeString(this._promptText, out);
/* 232:297 */     serializeUnicodeString(this._errorText, out);
/* 233:298 */     out.writeShort(this._formula1.getEncodedTokenSize());
/* 234:299 */     out.writeShort(this._not_used_1);
/* 235:300 */     this._formula1.serializeTokens(out);
/* 236:    */     
/* 237:302 */     out.writeShort(this._formula2.getEncodedTokenSize());
/* 238:303 */     out.writeShort(this._not_used_2);
/* 239:304 */     this._formula2.serializeTokens(out);
/* 240:    */     
/* 241:306 */     this._regions.serialize(out);
/* 242:    */   }
/* 243:    */   
/* 244:    */   private static UnicodeString resolveTitleText(String str)
/* 245:    */   {
/* 246:316 */     if ((str == null) || (str.length() < 1)) {
/* 247:317 */       return NULL_TEXT_STRING;
/* 248:    */     }
/* 249:319 */     return new UnicodeString(str);
/* 250:    */   }
/* 251:    */   
/* 252:    */   private static String resolveTitleString(UnicodeString us)
/* 253:    */   {
/* 254:323 */     if ((us == null) || (us.equals(NULL_TEXT_STRING))) {
/* 255:324 */       return null;
/* 256:    */     }
/* 257:326 */     return us.getString();
/* 258:    */   }
/* 259:    */   
/* 260:    */   private static UnicodeString readUnicodeString(RecordInputStream in)
/* 261:    */   {
/* 262:330 */     return new UnicodeString(in);
/* 263:    */   }
/* 264:    */   
/* 265:    */   private static void serializeUnicodeString(UnicodeString us, LittleEndianOutput out)
/* 266:    */   {
/* 267:334 */     StringUtil.writeUnicodeString(out, us.getString());
/* 268:    */   }
/* 269:    */   
/* 270:    */   private static int getUnicodeStringSize(UnicodeString us)
/* 271:    */   {
/* 272:337 */     String str = us.getString();
/* 273:338 */     return 3 + str.length() * (StringUtil.hasMultibyte(str) ? 2 : 1);
/* 274:    */   }
/* 275:    */   
/* 276:    */   protected int getDataSize()
/* 277:    */   {
/* 278:342 */     int size = 12;
/* 279:343 */     size += getUnicodeStringSize(this._promptTitle);
/* 280:344 */     size += getUnicodeStringSize(this._errorTitle);
/* 281:345 */     size += getUnicodeStringSize(this._promptText);
/* 282:346 */     size += getUnicodeStringSize(this._errorText);
/* 283:347 */     size += this._formula1.getEncodedTokenSize();
/* 284:348 */     size += this._formula2.getEncodedTokenSize();
/* 285:349 */     size += this._regions.getSize();
/* 286:350 */     return size;
/* 287:    */   }
/* 288:    */   
/* 289:    */   public short getSid()
/* 290:    */   {
/* 291:354 */     return 446;
/* 292:    */   }
/* 293:    */   
/* 294:    */   public DVRecord clone()
/* 295:    */   {
/* 296:363 */     return (DVRecord)cloneViaReserialise();
/* 297:    */   }
/* 298:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.DVRecord
 * JD-Core Version:    0.7.0.1
 */