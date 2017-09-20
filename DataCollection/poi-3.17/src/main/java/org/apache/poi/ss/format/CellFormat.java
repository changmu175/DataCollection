/*   1:    */ package org.apache.poi.ss.format;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Date;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Locale;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.WeakHashMap;
/*   9:    */ import java.util.logging.Level;
/*  10:    */ import java.util.logging.Logger;
/*  11:    */ import java.util.regex.Matcher;
/*  12:    */ import java.util.regex.Pattern;
/*  13:    */ import javax.swing.JLabel;
/*  14:    */ import org.apache.poi.ss.usermodel.Cell;
/*  15:    */ import org.apache.poi.ss.usermodel.CellType;
/*  16:    */ import org.apache.poi.ss.usermodel.DateUtil;
/*  17:    */ import org.apache.poi.util.LocaleUtil;
/*  18:    */ import org.apache.poi.util.Removal;
/*  19:    */ 
/*  20:    */ public class CellFormat
/*  21:    */ {
/*  22:    */   private final Locale locale;
/*  23:    */   private final String format;
/*  24:    */   private final CellFormatPart posNumFmt;
/*  25:    */   private final CellFormatPart zeroNumFmt;
/*  26:    */   private final CellFormatPart negNumFmt;
/*  27:    */   private final CellFormatPart textFmt;
/*  28:    */   private final int formatPartCount;
/*  29:104 */   private static final Pattern ONE_PART = Pattern.compile(CellFormatPart.FORMAT_PAT.pattern() + "(;|$)", 6);
/*  30:    */   private static final String INVALID_VALUE_FOR_FORMAT = "###############################################################################################################################################################################################################################################################";
/*  31:120 */   private static String QUOTE = "\"";
/*  32:    */   @Deprecated
/*  33:    */   @Removal(version="3.18")
/*  34:129 */   public static final CellFormat GENERAL_FORMAT = createGeneralFormat(LocaleUtil.getUserLocale());
/*  35:    */   
/*  36:    */   private static CellFormat createGeneralFormat(final Locale locale)
/*  37:    */   {
/*  38:132 */     new CellFormat(locale, "General", locale)
/*  39:    */     {
/*  40:    */       public CellFormatResult apply(Object value)
/*  41:    */       {
/*  42:135 */         String text = new CellGeneralFormatter(locale).format(value);
/*  43:136 */         return new CellFormatResult(true, text, null);
/*  44:    */       }
/*  45:    */     };
/*  46:    */   }
/*  47:    */   
/*  48:142 */   private static final Map<Locale, Map<String, CellFormat>> formatCache = new WeakHashMap();
/*  49:    */   
/*  50:    */   public static CellFormat getInstance(String format)
/*  51:    */   {
/*  52:154 */     return getInstance(LocaleUtil.getUserLocale(), format);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static synchronized CellFormat getInstance(Locale locale, String format)
/*  56:    */   {
/*  57:167 */     Map<String, CellFormat> formatMap = (Map)formatCache.get(locale);
/*  58:168 */     if (formatMap == null)
/*  59:    */     {
/*  60:169 */       formatMap = new WeakHashMap();
/*  61:170 */       formatCache.put(locale, formatMap);
/*  62:    */     }
/*  63:172 */     CellFormat fmt = (CellFormat)formatMap.get(format);
/*  64:173 */     if (fmt == null)
/*  65:    */     {
/*  66:174 */       if ((format.equals("General")) || (format.equals("@"))) {
/*  67:175 */         fmt = createGeneralFormat(locale);
/*  68:    */       } else {
/*  69:177 */         fmt = new CellFormat(locale, format);
/*  70:    */       }
/*  71:178 */       formatMap.put(format, fmt);
/*  72:    */     }
/*  73:180 */     return fmt;
/*  74:    */   }
/*  75:    */   
/*  76:    */   private CellFormat(Locale locale, String format)
/*  77:    */   {
/*  78:189 */     this.locale = locale;
/*  79:190 */     this.format = format;
/*  80:191 */     CellFormatPart defaultTextFormat = new CellFormatPart(locale, "@");
/*  81:192 */     Matcher m = ONE_PART.matcher(format);
/*  82:193 */     List<CellFormatPart> parts = new ArrayList();
/*  83:195 */     while (m.find()) {
/*  84:    */       try
/*  85:    */       {
/*  86:197 */         String valueDesc = m.group();
/*  87:200 */         if (valueDesc.endsWith(";")) {
/*  88:201 */           valueDesc = valueDesc.substring(0, valueDesc.length() - 1);
/*  89:    */         }
/*  90:203 */         parts.add(new CellFormatPart(locale, valueDesc));
/*  91:    */       }
/*  92:    */       catch (RuntimeException e)
/*  93:    */       {
/*  94:205 */         CellFormatter.logger.log(Level.WARNING, "Invalid format: " + CellFormatter.quote(m.group()), e);
/*  95:    */         
/*  96:207 */         parts.add(null);
/*  97:    */       }
/*  98:    */     }
/*  99:211 */     this.formatPartCount = parts.size();
/* 100:213 */     switch (this.formatPartCount)
/* 101:    */     {
/* 102:    */     case 1: 
/* 103:215 */       this.posNumFmt = ((CellFormatPart)parts.get(0));
/* 104:216 */       this.negNumFmt = null;
/* 105:217 */       this.zeroNumFmt = null;
/* 106:218 */       this.textFmt = defaultTextFormat;
/* 107:219 */       break;
/* 108:    */     case 2: 
/* 109:221 */       this.posNumFmt = ((CellFormatPart)parts.get(0));
/* 110:222 */       this.negNumFmt = ((CellFormatPart)parts.get(1));
/* 111:223 */       this.zeroNumFmt = null;
/* 112:224 */       this.textFmt = defaultTextFormat;
/* 113:225 */       break;
/* 114:    */     case 3: 
/* 115:227 */       this.posNumFmt = ((CellFormatPart)parts.get(0));
/* 116:228 */       this.negNumFmt = ((CellFormatPart)parts.get(1));
/* 117:229 */       this.zeroNumFmt = ((CellFormatPart)parts.get(2));
/* 118:230 */       this.textFmt = defaultTextFormat;
/* 119:231 */       break;
/* 120:    */     case 4: 
/* 121:    */     default: 
/* 122:234 */       this.posNumFmt = ((CellFormatPart)parts.get(0));
/* 123:235 */       this.negNumFmt = ((CellFormatPart)parts.get(1));
/* 124:236 */       this.zeroNumFmt = ((CellFormatPart)parts.get(2));
/* 125:237 */       this.textFmt = ((CellFormatPart)parts.get(3));
/* 126:    */     }
/* 127:    */   }
/* 128:    */   
/* 129:    */   public CellFormatResult apply(Object value)
/* 130:    */   {
/* 131:252 */     if ((value instanceof Number))
/* 132:    */     {
/* 133:253 */       Number num = (Number)value;
/* 134:254 */       double val = num.doubleValue();
/* 135:255 */       if ((val < 0.0D) && (((this.formatPartCount == 2) && (!this.posNumFmt.hasCondition()) && (!this.negNumFmt.hasCondition())) || ((this.formatPartCount == 3) && (!this.negNumFmt.hasCondition())) || ((this.formatPartCount == 4) && (!this.negNumFmt.hasCondition())))) {
/* 136:263 */         return this.negNumFmt.apply(Double.valueOf(-val));
/* 137:    */       }
/* 138:265 */       return getApplicableFormatPart(Double.valueOf(val)).apply(Double.valueOf(val));
/* 139:    */     }
/* 140:267 */     if ((value instanceof Date))
/* 141:    */     {
/* 142:270 */       Double numericValue = Double.valueOf(DateUtil.getExcelDate((Date)value));
/* 143:271 */       if (DateUtil.isValidExcelDate(numericValue.doubleValue())) {
/* 144:272 */         return getApplicableFormatPart(numericValue).apply(value);
/* 145:    */       }
/* 146:274 */       throw new IllegalArgumentException("value " + numericValue + " of date " + value + " is not a valid Excel date");
/* 147:    */     }
/* 148:277 */     return this.textFmt.apply(value);
/* 149:    */   }
/* 150:    */   
/* 151:    */   private CellFormatResult apply(Date date, double numericValue)
/* 152:    */   {
/* 153:290 */     return getApplicableFormatPart(Double.valueOf(numericValue)).apply(date);
/* 154:    */   }
/* 155:    */   
/* 156:    */   public CellFormatResult apply(Cell c)
/* 157:    */   {
/* 158:303 */     switch (2.$SwitchMap$org$apache$poi$ss$usermodel$CellType[ultimateTypeEnum(c).ordinal()])
/* 159:    */     {
/* 160:    */     case 1: 
/* 161:305 */       return apply("");
/* 162:    */     case 2: 
/* 163:307 */       return apply(Boolean.valueOf(c.getBooleanCellValue()));
/* 164:    */     case 3: 
/* 165:309 */       Double value = Double.valueOf(c.getNumericCellValue());
/* 166:310 */       if (getApplicableFormatPart(value).getCellFormatType() == CellFormatType.DATE)
/* 167:    */       {
/* 168:311 */         if (DateUtil.isValidExcelDate(value.doubleValue())) {
/* 169:312 */           return apply(c.getDateCellValue(), value.doubleValue());
/* 170:    */         }
/* 171:314 */         return apply("###############################################################################################################################################################################################################################################################");
/* 172:    */       }
/* 173:317 */       return apply(value);
/* 174:    */     case 4: 
/* 175:320 */       return apply(c.getStringCellValue());
/* 176:    */     }
/* 177:322 */     return apply("?");
/* 178:    */   }
/* 179:    */   
/* 180:    */   public CellFormatResult apply(JLabel label, Object value)
/* 181:    */   {
/* 182:336 */     CellFormatResult result = apply(value);
/* 183:337 */     label.setText(result.text);
/* 184:338 */     if (result.textColor != null) {
/* 185:339 */       label.setForeground(result.textColor);
/* 186:    */     }
/* 187:341 */     return result;
/* 188:    */   }
/* 189:    */   
/* 190:    */   private CellFormatResult apply(JLabel label, Date date, double numericValue)
/* 191:    */   {
/* 192:355 */     CellFormatResult result = apply(date, numericValue);
/* 193:356 */     label.setText(result.text);
/* 194:357 */     if (result.textColor != null) {
/* 195:358 */       label.setForeground(result.textColor);
/* 196:    */     }
/* 197:360 */     return result;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public CellFormatResult apply(JLabel label, Cell c)
/* 201:    */   {
/* 202:373 */     switch (2.$SwitchMap$org$apache$poi$ss$usermodel$CellType[ultimateTypeEnum(c).ordinal()])
/* 203:    */     {
/* 204:    */     case 1: 
/* 205:375 */       return apply(label, "");
/* 206:    */     case 2: 
/* 207:377 */       return apply(label, Boolean.valueOf(c.getBooleanCellValue()));
/* 208:    */     case 3: 
/* 209:379 */       Double value = Double.valueOf(c.getNumericCellValue());
/* 210:380 */       if (getApplicableFormatPart(value).getCellFormatType() == CellFormatType.DATE)
/* 211:    */       {
/* 212:381 */         if (DateUtil.isValidExcelDate(value.doubleValue())) {
/* 213:382 */           return apply(label, c.getDateCellValue(), value.doubleValue());
/* 214:    */         }
/* 215:384 */         return apply(label, "###############################################################################################################################################################################################################################################################");
/* 216:    */       }
/* 217:387 */       return apply(label, value);
/* 218:    */     case 4: 
/* 219:390 */       return apply(label, c.getStringCellValue());
/* 220:    */     }
/* 221:392 */     return apply(label, "?");
/* 222:    */   }
/* 223:    */   
/* 224:    */   private CellFormatPart getApplicableFormatPart(Object value)
/* 225:    */   {
/* 226:406 */     if ((value instanceof Number))
/* 227:    */     {
/* 228:408 */       double val = ((Number)value).doubleValue();
/* 229:410 */       if (this.formatPartCount == 1)
/* 230:    */       {
/* 231:411 */         if ((!this.posNumFmt.hasCondition()) || ((this.posNumFmt.hasCondition()) && (this.posNumFmt.applies(Double.valueOf(val))))) {
/* 232:413 */           return this.posNumFmt;
/* 233:    */         }
/* 234:415 */         return new CellFormatPart(this.locale, "General");
/* 235:    */       }
/* 236:417 */       if (this.formatPartCount == 2)
/* 237:    */       {
/* 238:418 */         if (((!this.posNumFmt.hasCondition()) && (val >= 0.0D)) || ((this.posNumFmt.hasCondition()) && (this.posNumFmt.applies(Double.valueOf(val))))) {
/* 239:420 */           return this.posNumFmt;
/* 240:    */         }
/* 241:421 */         if ((!this.negNumFmt.hasCondition()) || ((this.negNumFmt.hasCondition()) && (this.negNumFmt.applies(Double.valueOf(val))))) {
/* 242:423 */           return this.negNumFmt;
/* 243:    */         }
/* 244:426 */         return new CellFormatPart(QUOTE + "###############################################################################################################################################################################################################################################################" + QUOTE);
/* 245:    */       }
/* 246:429 */       if (((!this.posNumFmt.hasCondition()) && (val > 0.0D)) || ((this.posNumFmt.hasCondition()) && (this.posNumFmt.applies(Double.valueOf(val))))) {
/* 247:431 */         return this.posNumFmt;
/* 248:    */       }
/* 249:432 */       if (((!this.negNumFmt.hasCondition()) && (val < 0.0D)) || ((this.negNumFmt.hasCondition()) && (this.negNumFmt.applies(Double.valueOf(val))))) {
/* 250:434 */         return this.negNumFmt;
/* 251:    */       }
/* 252:437 */       return this.zeroNumFmt;
/* 253:    */     }
/* 254:441 */     throw new IllegalArgumentException("value must be a Number");
/* 255:    */   }
/* 256:    */   
/* 257:    */   /**
/* 258:    */    * @deprecated
/* 259:    */    */
/* 260:    */   public static int ultimateType(Cell cell)
/* 261:    */   {
/* 262:461 */     return ultimateTypeEnum(cell).getCode();
/* 263:    */   }
/* 264:    */   
/* 265:    */   /**
/* 266:    */    * @deprecated
/* 267:    */    */
/* 268:    */   public static CellType ultimateTypeEnum(Cell cell)
/* 269:    */   {
/* 270:478 */     CellType type = cell.getCellTypeEnum();
/* 271:479 */     if (type == CellType.FORMULA) {
/* 272:480 */       return cell.getCachedFormulaResultTypeEnum();
/* 273:    */     }
/* 274:482 */     return type;
/* 275:    */   }
/* 276:    */   
/* 277:    */   public boolean equals(Object obj)
/* 278:    */   {
/* 279:495 */     if (this == obj) {
/* 280:496 */       return true;
/* 281:    */     }
/* 282:497 */     if ((obj instanceof CellFormat))
/* 283:    */     {
/* 284:498 */       CellFormat that = (CellFormat)obj;
/* 285:499 */       return this.format.equals(that.format);
/* 286:    */     }
/* 287:501 */     return false;
/* 288:    */   }
/* 289:    */   
/* 290:    */   public int hashCode()
/* 291:    */   {
/* 292:511 */     return this.format.hashCode();
/* 293:    */   }
/* 294:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.format.CellFormat
 * JD-Core Version:    0.7.0.1
 */