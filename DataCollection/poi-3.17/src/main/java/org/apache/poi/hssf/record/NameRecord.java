/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.cont.ContinuableRecord;
/*   4:    */ import org.apache.poi.hssf.record.cont.ContinuableRecordOutput;
/*   5:    */ import org.apache.poi.ss.formula.Formula;
/*   6:    */ import org.apache.poi.ss.formula.ptg.Area3DPtg;
/*   7:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   8:    */ import org.apache.poi.ss.formula.ptg.Ref3DPtg;
/*   9:    */ import org.apache.poi.util.HexDump;
/*  10:    */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*  11:    */ import org.apache.poi.util.LittleEndianInput;
/*  12:    */ import org.apache.poi.util.StringUtil;
/*  13:    */ 
/*  14:    */ public final class NameRecord
/*  15:    */   extends ContinuableRecord
/*  16:    */ {
/*  17:    */   public static final short sid = 24;
/*  18:    */   public static final byte BUILTIN_CONSOLIDATE_AREA = 1;
/*  19:    */   public static final byte BUILTIN_AUTO_OPEN = 2;
/*  20:    */   public static final byte BUILTIN_AUTO_CLOSE = 3;
/*  21:    */   public static final byte BUILTIN_DATABASE = 4;
/*  22:    */   public static final byte BUILTIN_CRITERIA = 5;
/*  23:    */   public static final byte BUILTIN_PRINT_AREA = 6;
/*  24:    */   public static final byte BUILTIN_PRINT_TITLE = 7;
/*  25:    */   public static final byte BUILTIN_RECORDER = 8;
/*  26:    */   public static final byte BUILTIN_DATA_FORM = 9;
/*  27:    */   public static final byte BUILTIN_AUTO_ACTIVATE = 10;
/*  28:    */   public static final byte BUILTIN_AUTO_DEACTIVATE = 11;
/*  29:    */   public static final byte BUILTIN_SHEET_TITLE = 12;
/*  30:    */   public static final byte BUILTIN_FILTER_DB = 13;
/*  31:    */   private short field_1_option_flag;
/*  32:    */   private byte field_2_keyboard_shortcut;
/*  33:    */   private short field_5_externSheetIndex_plus1;
/*  34:    */   private int field_6_sheetNumber;
/*  35:    */   private boolean field_11_nameIsMultibyte;
/*  36:    */   private byte field_12_built_in_code;
/*  37:    */   private String field_12_name_text;
/*  38:    */   private Formula field_13_name_definition;
/*  39:    */   private String field_14_custom_menu_text;
/*  40:    */   private String field_15_description_text;
/*  41:    */   private String field_16_help_topic_text;
/*  42:    */   private String field_17_status_bar_text;
/*  43:    */   
/*  44:    */   private static final class Option
/*  45:    */   {
/*  46:    */     public static final int OPT_HIDDEN_NAME = 1;
/*  47:    */     public static final int OPT_FUNCTION_NAME = 2;
/*  48:    */     public static final int OPT_COMMAND_NAME = 4;
/*  49:    */     public static final int OPT_MACRO = 8;
/*  50:    */     public static final int OPT_COMPLEX = 16;
/*  51:    */     public static final int OPT_BUILTIN = 32;
/*  52:    */     public static final int OPT_BINDATA = 4096;
/*  53:    */     
/*  54:    */     public static final boolean isFormula(int optValue)
/*  55:    */     {
/*  56: 70 */       return (optValue & 0xF) == 0;
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   public NameRecord()
/*  61:    */   {
/*  62: 92 */     this.field_13_name_definition = Formula.create(Ptg.EMPTY_PTG_ARRAY);
/*  63:    */     
/*  64: 94 */     this.field_12_name_text = "";
/*  65: 95 */     this.field_14_custom_menu_text = "";
/*  66: 96 */     this.field_15_description_text = "";
/*  67: 97 */     this.field_16_help_topic_text = "";
/*  68: 98 */     this.field_17_status_bar_text = "";
/*  69:    */   }
/*  70:    */   
/*  71:    */   public NameRecord(byte builtin, int sheetNumber)
/*  72:    */   {
/*  73:108 */     this();
/*  74:109 */     this.field_12_built_in_code = builtin;
/*  75:110 */     setOptionFlag((short)(this.field_1_option_flag | 0x20));
/*  76:    */     
/*  77:112 */     this.field_6_sheetNumber = sheetNumber;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setOptionFlag(short flag)
/*  81:    */   {
/*  82:119 */     this.field_1_option_flag = flag;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setKeyboardShortcut(byte shortcut)
/*  86:    */   {
/*  87:127 */     this.field_2_keyboard_shortcut = shortcut;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public int getSheetNumber()
/*  91:    */   {
/*  92:136 */     return this.field_6_sheetNumber;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public byte getFnGroup()
/*  96:    */   {
/*  97:144 */     int masked = this.field_1_option_flag & 0xFC0;
/*  98:145 */     return (byte)(masked >> 4);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void setSheetNumber(int value)
/* 102:    */   {
/* 103:151 */     this.field_6_sheetNumber = value;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setNameText(String name)
/* 107:    */   {
/* 108:159 */     this.field_12_name_text = name;
/* 109:160 */     this.field_11_nameIsMultibyte = StringUtil.hasMultibyte(name);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void setCustomMenuText(String text)
/* 113:    */   {
/* 114:167 */     this.field_14_custom_menu_text = text;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void setDescriptionText(String text)
/* 118:    */   {
/* 119:174 */     this.field_15_description_text = text;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setHelpTopicText(String text)
/* 123:    */   {
/* 124:181 */     this.field_16_help_topic_text = text;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public void setStatusBarText(String text)
/* 128:    */   {
/* 129:188 */     this.field_17_status_bar_text = text;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public short getOptionFlag()
/* 133:    */   {
/* 134:195 */     return this.field_1_option_flag;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public byte getKeyboardShortcut()
/* 138:    */   {
/* 139:202 */     return this.field_2_keyboard_shortcut;
/* 140:    */   }
/* 141:    */   
/* 142:    */   private int getNameTextLength()
/* 143:    */   {
/* 144:210 */     if (isBuiltInName()) {
/* 145:211 */       return 1;
/* 146:    */     }
/* 147:213 */     return this.field_12_name_text.length();
/* 148:    */   }
/* 149:    */   
/* 150:    */   public boolean isHiddenName()
/* 151:    */   {
/* 152:221 */     return (this.field_1_option_flag & 0x1) != 0;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void setHidden(boolean b)
/* 156:    */   {
/* 157:224 */     if (b) {
/* 158:225 */       this.field_1_option_flag = ((short)(this.field_1_option_flag | 0x1));
/* 159:    */     } else {
/* 160:227 */       this.field_1_option_flag = ((short)(this.field_1_option_flag & 0xFFFFFFFE));
/* 161:    */     }
/* 162:    */   }
/* 163:    */   
/* 164:    */   public boolean isFunctionName()
/* 165:    */   {
/* 166:234 */     return (this.field_1_option_flag & 0x2) != 0;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void setFunction(boolean function)
/* 170:    */   {
/* 171:244 */     if (function) {
/* 172:245 */       this.field_1_option_flag = ((short)(this.field_1_option_flag | 0x2));
/* 173:    */     } else {
/* 174:247 */       this.field_1_option_flag = ((short)(this.field_1_option_flag & 0xFFFFFFFD));
/* 175:    */     }
/* 176:    */   }
/* 177:    */   
/* 178:    */   public boolean hasFormula()
/* 179:    */   {
/* 180:255 */     return (Option.isFormula(this.field_1_option_flag)) && (this.field_13_name_definition.getEncodedTokenSize() > 0);
/* 181:    */   }
/* 182:    */   
/* 183:    */   public boolean isCommandName()
/* 184:    */   {
/* 185:262 */     return (this.field_1_option_flag & 0x4) != 0;
/* 186:    */   }
/* 187:    */   
/* 188:    */   public boolean isMacro()
/* 189:    */   {
/* 190:268 */     return (this.field_1_option_flag & 0x8) != 0;
/* 191:    */   }
/* 192:    */   
/* 193:    */   public boolean isComplexFunction()
/* 194:    */   {
/* 195:274 */     return (this.field_1_option_flag & 0x10) != 0;
/* 196:    */   }
/* 197:    */   
/* 198:    */   public boolean isBuiltInName()
/* 199:    */   {
/* 200:284 */     return (this.field_1_option_flag & 0x20) != 0;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public String getNameText()
/* 204:    */   {
/* 205:293 */     return isBuiltInName() ? translateBuiltInName(getBuiltInName()) : this.field_12_name_text;
/* 206:    */   }
/* 207:    */   
/* 208:    */   public byte getBuiltInName()
/* 209:    */   {
/* 210:301 */     return this.field_12_built_in_code;
/* 211:    */   }
/* 212:    */   
/* 213:    */   public Ptg[] getNameDefinition()
/* 214:    */   {
/* 215:309 */     return this.field_13_name_definition.getTokens();
/* 216:    */   }
/* 217:    */   
/* 218:    */   public void setNameDefinition(Ptg[] ptgs)
/* 219:    */   {
/* 220:313 */     this.field_13_name_definition = Formula.create(ptgs);
/* 221:    */   }
/* 222:    */   
/* 223:    */   public String getCustomMenuText()
/* 224:    */   {
/* 225:320 */     return this.field_14_custom_menu_text;
/* 226:    */   }
/* 227:    */   
/* 228:    */   public String getDescriptionText()
/* 229:    */   {
/* 230:327 */     return this.field_15_description_text;
/* 231:    */   }
/* 232:    */   
/* 233:    */   public String getHelpTopicText()
/* 234:    */   {
/* 235:334 */     return this.field_16_help_topic_text;
/* 236:    */   }
/* 237:    */   
/* 238:    */   public String getStatusBarText()
/* 239:    */   {
/* 240:341 */     return this.field_17_status_bar_text;
/* 241:    */   }
/* 242:    */   
/* 243:    */   public void serialize(ContinuableRecordOutput out)
/* 244:    */   {
/* 245:352 */     int field_7_length_custom_menu = this.field_14_custom_menu_text.length();
/* 246:353 */     int field_8_length_description_text = this.field_15_description_text.length();
/* 247:354 */     int field_9_length_help_topic_text = this.field_16_help_topic_text.length();
/* 248:355 */     int field_10_length_status_bar_text = this.field_17_status_bar_text.length();
/* 249:    */     
/* 250:    */ 
/* 251:358 */     out.writeShort(getOptionFlag());
/* 252:359 */     out.writeByte(getKeyboardShortcut());
/* 253:360 */     out.writeByte(getNameTextLength());
/* 254:    */     
/* 255:362 */     out.writeShort(this.field_13_name_definition.getEncodedTokenSize());
/* 256:363 */     out.writeShort(this.field_5_externSheetIndex_plus1);
/* 257:364 */     out.writeShort(this.field_6_sheetNumber);
/* 258:365 */     out.writeByte(field_7_length_custom_menu);
/* 259:366 */     out.writeByte(field_8_length_description_text);
/* 260:367 */     out.writeByte(field_9_length_help_topic_text);
/* 261:368 */     out.writeByte(field_10_length_status_bar_text);
/* 262:369 */     out.writeByte(this.field_11_nameIsMultibyte ? 1 : 0);
/* 263:371 */     if (isBuiltInName())
/* 264:    */     {
/* 265:373 */       out.writeByte(this.field_12_built_in_code);
/* 266:    */     }
/* 267:    */     else
/* 268:    */     {
/* 269:375 */       String nameText = this.field_12_name_text;
/* 270:376 */       if (this.field_11_nameIsMultibyte) {
/* 271:377 */         StringUtil.putUnicodeLE(nameText, out);
/* 272:    */       } else {
/* 273:379 */         StringUtil.putCompressedUnicode(nameText, out);
/* 274:    */       }
/* 275:    */     }
/* 276:382 */     this.field_13_name_definition.serializeTokens(out);
/* 277:383 */     this.field_13_name_definition.serializeArrayConstantData(out);
/* 278:    */     
/* 279:385 */     StringUtil.putCompressedUnicode(getCustomMenuText(), out);
/* 280:386 */     StringUtil.putCompressedUnicode(getDescriptionText(), out);
/* 281:387 */     StringUtil.putCompressedUnicode(getHelpTopicText(), out);
/* 282:388 */     StringUtil.putCompressedUnicode(getStatusBarText(), out);
/* 283:    */   }
/* 284:    */   
/* 285:    */   private int getNameRawSize()
/* 286:    */   {
/* 287:391 */     if (isBuiltInName()) {
/* 288:392 */       return 1;
/* 289:    */     }
/* 290:394 */     int nChars = this.field_12_name_text.length();
/* 291:395 */     if (this.field_11_nameIsMultibyte) {
/* 292:396 */       return 2 * nChars;
/* 293:    */     }
/* 294:398 */     return nChars;
/* 295:    */   }
/* 296:    */   
/* 297:    */   protected int getDataSize()
/* 298:    */   {
/* 299:402 */     return 13 + getNameRawSize() + this.field_14_custom_menu_text.length() + this.field_15_description_text.length() + this.field_16_help_topic_text.length() + this.field_17_status_bar_text.length() + this.field_13_name_definition.getEncodedSize();
/* 300:    */   }
/* 301:    */   
/* 302:    */   public int getExternSheetNumber()
/* 303:    */   {
/* 304:415 */     Ptg[] tokens = this.field_13_name_definition.getTokens();
/* 305:416 */     if (tokens.length == 0) {
/* 306:417 */       return 0;
/* 307:    */     }
/* 308:420 */     Ptg ptg = tokens[0];
/* 309:421 */     if (ptg.getClass() == Area3DPtg.class) {
/* 310:422 */       return ((Area3DPtg)ptg).getExternSheetIndex();
/* 311:    */     }
/* 312:425 */     if (ptg.getClass() == Ref3DPtg.class) {
/* 313:426 */       return ((Ref3DPtg)ptg).getExternSheetIndex();
/* 314:    */     }
/* 315:428 */     return 0;
/* 316:    */   }
/* 317:    */   
/* 318:    */   public NameRecord(RecordInputStream ris)
/* 319:    */   {
/* 320:442 */     byte[] remainder = ris.readAllContinuedRemainder();
/* 321:443 */     LittleEndianInput in = new LittleEndianByteArrayInputStream(remainder);
/* 322:    */     
/* 323:445 */     this.field_1_option_flag = in.readShort();
/* 324:446 */     this.field_2_keyboard_shortcut = in.readByte();
/* 325:447 */     int field_3_length_name_text = in.readUByte();
/* 326:448 */     int field_4_length_name_definition = in.readShort();
/* 327:449 */     this.field_5_externSheetIndex_plus1 = in.readShort();
/* 328:450 */     this.field_6_sheetNumber = in.readUShort();
/* 329:451 */     int f7_customMenuLen = in.readUByte();
/* 330:452 */     int f8_descriptionTextLen = in.readUByte();
/* 331:453 */     int f9_helpTopicTextLen = in.readUByte();
/* 332:454 */     int f10_statusBarTextLen = in.readUByte();
/* 333:    */     
/* 334:    */ 
/* 335:457 */     this.field_11_nameIsMultibyte = (in.readByte() != 0);
/* 336:458 */     if (isBuiltInName()) {
/* 337:459 */       this.field_12_built_in_code = in.readByte();
/* 338:461 */     } else if (this.field_11_nameIsMultibyte) {
/* 339:462 */       this.field_12_name_text = StringUtil.readUnicodeLE(in, field_3_length_name_text);
/* 340:    */     } else {
/* 341:464 */       this.field_12_name_text = StringUtil.readCompressedUnicode(in, field_3_length_name_text);
/* 342:    */     }
/* 343:468 */     int nBytesAvailable = in.available() - (f7_customMenuLen + f8_descriptionTextLen + f9_helpTopicTextLen + f10_statusBarTextLen);
/* 344:    */     
/* 345:470 */     this.field_13_name_definition = Formula.read(field_4_length_name_definition, in, nBytesAvailable);
/* 346:    */     
/* 347:    */ 
/* 348:473 */     this.field_14_custom_menu_text = StringUtil.readCompressedUnicode(in, f7_customMenuLen);
/* 349:474 */     this.field_15_description_text = StringUtil.readCompressedUnicode(in, f8_descriptionTextLen);
/* 350:475 */     this.field_16_help_topic_text = StringUtil.readCompressedUnicode(in, f9_helpTopicTextLen);
/* 351:476 */     this.field_17_status_bar_text = StringUtil.readCompressedUnicode(in, f10_statusBarTextLen);
/* 352:    */   }
/* 353:    */   
/* 354:    */   public short getSid()
/* 355:    */   {
/* 356:484 */     return 24;
/* 357:    */   }
/* 358:    */   
/* 359:    */   public String toString()
/* 360:    */   {
/* 361:539 */     StringBuffer sb = new StringBuffer();
/* 362:    */     
/* 363:541 */     sb.append("[NAME]\n");
/* 364:542 */     sb.append("    .option flags           = ").append(HexDump.shortToHex(this.field_1_option_flag)).append("\n");
/* 365:543 */     sb.append("    .keyboard shortcut      = ").append(HexDump.byteToHex(this.field_2_keyboard_shortcut)).append("\n");
/* 366:544 */     sb.append("    .length of the name     = ").append(getNameTextLength()).append("\n");
/* 367:545 */     sb.append("    .extSheetIx(1-based, 0=Global)= ").append(this.field_5_externSheetIndex_plus1).append("\n");
/* 368:546 */     sb.append("    .sheetTabIx             = ").append(this.field_6_sheetNumber).append("\n");
/* 369:547 */     sb.append("    .Menu text length       = ").append(this.field_14_custom_menu_text.length()).append("\n");
/* 370:548 */     sb.append("    .Description text length= ").append(this.field_15_description_text.length()).append("\n");
/* 371:549 */     sb.append("    .Help topic text length = ").append(this.field_16_help_topic_text.length()).append("\n");
/* 372:550 */     sb.append("    .Status bar text length = ").append(this.field_17_status_bar_text.length()).append("\n");
/* 373:551 */     sb.append("    .NameIsMultibyte        = ").append(this.field_11_nameIsMultibyte).append("\n");
/* 374:552 */     sb.append("    .Name (Unicode text)    = ").append(getNameText()).append("\n");
/* 375:553 */     Ptg[] ptgs = this.field_13_name_definition.getTokens();
/* 376:554 */     sb.append("    .Formula (nTokens=").append(ptgs.length).append("):").append("\n");
/* 377:555 */     for (Ptg ptg : ptgs) {
/* 378:556 */       sb.append("       " + ptg).append(ptg.getRVAType()).append("\n");
/* 379:    */     }
/* 380:559 */     sb.append("    .Menu text       = ").append(this.field_14_custom_menu_text).append("\n");
/* 381:560 */     sb.append("    .Description text= ").append(this.field_15_description_text).append("\n");
/* 382:561 */     sb.append("    .Help topic text = ").append(this.field_16_help_topic_text).append("\n");
/* 383:562 */     sb.append("    .Status bar text = ").append(this.field_17_status_bar_text).append("\n");
/* 384:563 */     sb.append("[/NAME]\n");
/* 385:    */     
/* 386:565 */     return sb.toString();
/* 387:    */   }
/* 388:    */   
/* 389:    */   private static String translateBuiltInName(byte name)
/* 390:    */   {
/* 391:573 */     switch (name)
/* 392:    */     {
/* 393:    */     case 10: 
/* 394:575 */       return "Auto_Activate";
/* 395:    */     case 3: 
/* 396:576 */       return "Auto_Close";
/* 397:    */     case 11: 
/* 398:577 */       return "Auto_Deactivate";
/* 399:    */     case 2: 
/* 400:578 */       return "Auto_Open";
/* 401:    */     case 1: 
/* 402:579 */       return "Consolidate_Area";
/* 403:    */     case 5: 
/* 404:580 */       return "Criteria";
/* 405:    */     case 4: 
/* 406:581 */       return "Database";
/* 407:    */     case 9: 
/* 408:582 */       return "Data_Form";
/* 409:    */     case 6: 
/* 410:583 */       return "Print_Area";
/* 411:    */     case 7: 
/* 412:584 */       return "Print_Titles";
/* 413:    */     case 8: 
/* 414:585 */       return "Recorder";
/* 415:    */     case 12: 
/* 416:586 */       return "Sheet_Title";
/* 417:    */     case 13: 
/* 418:587 */       return "_FilterDatabase";
/* 419:    */     }
/* 420:591 */     return "Unknown";
/* 421:    */   }
/* 422:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.NameRecord
 * JD-Core Version:    0.7.0.1
 */