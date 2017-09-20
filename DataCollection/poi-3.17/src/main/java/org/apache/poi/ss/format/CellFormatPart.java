/*   1:    */ package org.apache.poi.ss.format;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.util.Locale;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.TreeMap;
/*   7:    */ import java.util.logging.Logger;
/*   8:    */ import java.util.regex.Matcher;
/*   9:    */ import java.util.regex.Pattern;
/*  10:    */ import javax.swing.JLabel;
/*  11:    */ import org.apache.poi.hssf.util.HSSFColor;
/*  12:    */ import org.apache.poi.util.LocaleUtil;
/*  13:    */ 
/*  14:    */ public class CellFormatPart
/*  15:    */ {
/*  16:    */   private final Color color;
/*  17:    */   private CellFormatCondition condition;
/*  18:    */   private final CellFormatter format;
/*  19:    */   private final CellFormatType type;
/*  20: 59 */   private static final Map<String, Color> NAMED_COLORS = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*  21:    */   public static final Pattern COLOR_PAT;
/*  22:    */   public static final Pattern CONDITION_PAT;
/*  23:    */   public static final Pattern SPECIFICATION_PAT;
/*  24:    */   public static final Pattern CURRENCY_PAT;
/*  25:    */   public static final Pattern FORMAT_PAT;
/*  26:    */   
/*  27:    */   static
/*  28:    */   {
/*  29: 62 */     Map<Integer, HSSFColor> colors = HSSFColor.getIndexHash();
/*  30: 63 */     for (HSSFColor color : colors.values())
/*  31:    */     {
/*  32: 64 */       Class<? extends HSSFColor> type = color.getClass();
/*  33: 65 */       String name = type.getSimpleName();
/*  34: 66 */       if (name.equals(name.toUpperCase(Locale.ROOT)))
/*  35:    */       {
/*  36: 67 */         short[] rgb = color.getTriplet();
/*  37: 68 */         Color c = new Color(rgb[0], rgb[1], rgb[2]);
/*  38: 69 */         NAMED_COLORS.put(name, c);
/*  39: 70 */         if (name.indexOf('_') > 0) {
/*  40: 71 */           NAMED_COLORS.put(name.replace('_', ' '), c);
/*  41:    */         }
/*  42: 72 */         if (name.indexOf("_PERCENT") > 0) {
/*  43: 73 */           NAMED_COLORS.put(name.replace("_PERCENT", "%").replace('_', ' '), c);
/*  44:    */         }
/*  45:    */       }
/*  46:    */     }
/*  47:110 */     String condition = "([<>=]=?|!=|<>)    # The operator\n  \\s*([0-9]+(?:\\.[0-9]*)?)\\s*  # The constant to test against\n";
/*  48:    */     
/*  49:    */ 
/*  50:    */ 
/*  51:114 */     String currency = "(\\[\\$.{0,3}-[0-9a-f]{3}\\])";
/*  52:    */     
/*  53:116 */     String color = "\\[(black|blue|cyan|green|magenta|red|white|yellow|color [0-9]+)\\]";
/*  54:    */     
/*  55:    */ 
/*  56:    */ 
/*  57:    */ 
/*  58:    */ 
/*  59:    */ 
/*  60:123 */     String part = "\\\\.                 # Quoted single character\n|\"([^\\\\\"]|\\\\.)*\"         # Quoted string of characters (handles escaped quotes like \\\") \n|" + currency + "                   # Currency symbol in a given locale\n" + "|_.                             # Space as wide as a given character\n" + "|\\*.                           # Repeating fill character\n" + "|@                              # Text: cell text\n" + "|([0?\\#](?:[0?\\#,]*))         # Number: digit + other digits and commas\n" + "|e[-+]                          # Number: Scientific: Exponent\n" + "|m{1,5}                         # Date: month or minute spec\n" + "|d{1,4}                         # Date: day/date spec\n" + "|y{2,4}                         # Date: year spec\n" + "|h{1,2}                         # Date: hour spec\n" + "|s{1,2}                         # Date: second spec\n" + "|am?/pm?                        # Date: am/pm spec\n" + "|\\[h{1,2}\\]                   # Elapsed time: hour spec\n" + "|\\[m{1,2}\\]                   # Elapsed time: minute spec\n" + "|\\[s{1,2}\\]                   # Elapsed time: second spec\n" + "|[^;]                           # A character\n" + "";
/*  61:    */     
/*  62:    */ 
/*  63:    */ 
/*  64:    */ 
/*  65:    */ 
/*  66:    */ 
/*  67:    */ 
/*  68:    */ 
/*  69:    */ 
/*  70:    */ 
/*  71:    */ 
/*  72:    */ 
/*  73:    */ 
/*  74:    */ 
/*  75:    */ 
/*  76:    */ 
/*  77:    */ 
/*  78:    */ 
/*  79:142 */     String format = "(?:" + color + ")?                 # Text color\n" + "(?:\\[" + condition + "\\])?               # Condition\n" + "(?:\\[\\$-[0-9a-fA-F]+\\])?                # Optional locale id, ignored currently\n" + "((?:" + part + ")+)                        # Format spec\n";
/*  80:    */     
/*  81:    */ 
/*  82:    */ 
/*  83:    */ 
/*  84:    */ 
/*  85:    */ 
/*  86:149 */     int flags = 6;
/*  87:150 */     COLOR_PAT = Pattern.compile(color, flags);
/*  88:151 */     CONDITION_PAT = Pattern.compile(condition, flags);
/*  89:152 */     SPECIFICATION_PAT = Pattern.compile(part, flags);
/*  90:153 */     CURRENCY_PAT = Pattern.compile(currency, flags);
/*  91:154 */     FORMAT_PAT = Pattern.compile(format, flags);
/*  92:    */   }
/*  93:    */   
/*  94:160 */   public static final int COLOR_GROUP = findGroup(FORMAT_PAT, "[Blue]@", "Blue");
/*  95:161 */   public static final int CONDITION_OPERATOR_GROUP = findGroup(FORMAT_PAT, "[>=1]@", ">=");
/*  96:162 */   public static final int CONDITION_VALUE_GROUP = findGroup(FORMAT_PAT, "[>=1]@", "1");
/*  97:163 */   public static final int SPECIFICATION_GROUP = findGroup(FORMAT_PAT, "[Blue][>1]\\a ?", "\\a ?");
/*  98:    */   
/*  99:    */   public CellFormatPart(String desc)
/* 100:    */   {
/* 101:177 */     this(LocaleUtil.getUserLocale(), desc);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public CellFormatPart(Locale locale, String desc)
/* 105:    */   {
/* 106:187 */     Matcher m = FORMAT_PAT.matcher(desc);
/* 107:188 */     if (!m.matches()) {
/* 108:189 */       throw new IllegalArgumentException("Unrecognized format: " + CellFormatter.quote(desc));
/* 109:    */     }
/* 110:192 */     this.color = getColor(m);
/* 111:193 */     this.condition = getCondition(m);
/* 112:194 */     this.type = getCellFormatType(m);
/* 113:195 */     this.format = getFormatter(locale, m);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public boolean applies(Object valueObject)
/* 117:    */   {
/* 118:209 */     if ((this.condition == null) || (!(valueObject instanceof Number)))
/* 119:    */     {
/* 120:210 */       if (valueObject == null) {
/* 121:211 */         throw new NullPointerException("valueObject");
/* 122:    */       }
/* 123:212 */       return true;
/* 124:    */     }
/* 125:214 */     Number num = (Number)valueObject;
/* 126:215 */     return this.condition.pass(num.doubleValue());
/* 127:    */   }
/* 128:    */   
/* 129:    */   private static int findGroup(Pattern pat, String str, String marker)
/* 130:    */   {
/* 131:232 */     Matcher m = pat.matcher(str);
/* 132:233 */     if (!m.find()) {
/* 133:234 */       throw new IllegalArgumentException("Pattern \"" + pat.pattern() + "\" doesn't match \"" + str + "\"");
/* 134:    */     }
/* 135:237 */     for (int i = 1; i <= m.groupCount(); i++)
/* 136:    */     {
/* 137:238 */       String grp = m.group(i);
/* 138:239 */       if ((grp != null) && (grp.equals(marker))) {
/* 139:240 */         return i;
/* 140:    */       }
/* 141:    */     }
/* 142:242 */     throw new IllegalArgumentException("\"" + marker + "\" not found in \"" + pat.pattern() + "\"");
/* 143:    */   }
/* 144:    */   
/* 145:    */   private static Color getColor(Matcher m)
/* 146:    */   {
/* 147:255 */     String cdesc = m.group(COLOR_GROUP);
/* 148:256 */     if ((cdesc == null) || (cdesc.length() == 0)) {
/* 149:257 */       return null;
/* 150:    */     }
/* 151:258 */     Color c = (Color)NAMED_COLORS.get(cdesc);
/* 152:259 */     if (c == null) {
/* 153:260 */       CellFormatter.logger.warning("Unknown color: " + CellFormatter.quote(cdesc));
/* 154:    */     }
/* 155:261 */     return c;
/* 156:    */   }
/* 157:    */   
/* 158:    */   private CellFormatCondition getCondition(Matcher m)
/* 159:    */   {
/* 160:273 */     String mdesc = m.group(CONDITION_OPERATOR_GROUP);
/* 161:274 */     if ((mdesc == null) || (mdesc.length() == 0)) {
/* 162:275 */       return null;
/* 163:    */     }
/* 164:276 */     return CellFormatCondition.getInstance(m.group(CONDITION_OPERATOR_GROUP), m.group(CONDITION_VALUE_GROUP));
/* 165:    */   }
/* 166:    */   
/* 167:    */   private CellFormatType getCellFormatType(Matcher matcher)
/* 168:    */   {
/* 169:289 */     String fdesc = matcher.group(SPECIFICATION_GROUP);
/* 170:290 */     return formatType(fdesc);
/* 171:    */   }
/* 172:    */   
/* 173:    */   private CellFormatter getFormatter(Locale locale, Matcher matcher)
/* 174:    */   {
/* 175:302 */     String fdesc = matcher.group(SPECIFICATION_GROUP);
/* 176:    */     
/* 177:    */ 
/* 178:305 */     Matcher currencyM = CURRENCY_PAT.matcher(fdesc);
/* 179:306 */     if (currencyM.find())
/* 180:    */     {
/* 181:307 */       String currencyPart = currencyM.group(1);
/* 182:    */       String currencyRepl;
/* 183:    */       String currencyRepl;
/* 184:309 */       if (currencyPart.startsWith("[$-")) {
/* 185:311 */         currencyRepl = "$";
/* 186:    */       } else {
/* 187:313 */         currencyRepl = currencyPart.substring(2, currencyPart.lastIndexOf('-'));
/* 188:    */       }
/* 189:315 */       fdesc = fdesc.replace(currencyPart, currencyRepl);
/* 190:    */     }
/* 191:319 */     return this.type.formatter(locale, fdesc);
/* 192:    */   }
/* 193:    */   
/* 194:    */   private CellFormatType formatType(String fdesc)
/* 195:    */   {
/* 196:330 */     fdesc = fdesc.trim();
/* 197:331 */     if ((fdesc.equals("")) || (fdesc.equalsIgnoreCase("General"))) {
/* 198:332 */       return CellFormatType.GENERAL;
/* 199:    */     }
/* 200:334 */     Matcher m = SPECIFICATION_PAT.matcher(fdesc);
/* 201:335 */     boolean couldBeDate = false;
/* 202:336 */     boolean seenZero = false;
/* 203:337 */     while (m.find())
/* 204:    */     {
/* 205:338 */       String repl = m.group(0);
/* 206:340 */       if (repl.length() > 0)
/* 207:    */       {
/* 208:341 */         char c1 = repl.charAt(0);
/* 209:342 */         char c2 = '\000';
/* 210:343 */         if (repl.length() > 1) {
/* 211:344 */           c2 = Character.toLowerCase(repl.charAt(1));
/* 212:    */         }
/* 213:346 */         switch (c1)
/* 214:    */         {
/* 215:    */         case '@': 
/* 216:348 */           return CellFormatType.TEXT;
/* 217:    */         case 'D': 
/* 218:    */         case 'Y': 
/* 219:    */         case 'd': 
/* 220:    */         case 'y': 
/* 221:353 */           return CellFormatType.DATE;
/* 222:    */         case 'H': 
/* 223:    */         case 'M': 
/* 224:    */         case 'S': 
/* 225:    */         case 'h': 
/* 226:    */         case 'm': 
/* 227:    */         case 's': 
/* 228:361 */           couldBeDate = true;
/* 229:362 */           break;
/* 230:    */         case '0': 
/* 231:365 */           seenZero = true;
/* 232:366 */           break;
/* 233:    */         case '[': 
/* 234:368 */           if ((c2 == 'h') || (c2 == 'm') || (c2 == 's')) {
/* 235:369 */             return CellFormatType.ELAPSED;
/* 236:    */           }
/* 237:371 */           if (c2 == '$') {
/* 238:373 */             return CellFormatType.NUMBER;
/* 239:    */           }
/* 240:376 */           throw new IllegalArgumentException("Unsupported [] format block '" + repl + "' in '" + fdesc + "' with c2: " + c2);
/* 241:    */         case '#': 
/* 242:    */         case '?': 
/* 243:380 */           return CellFormatType.NUMBER;
/* 244:    */         }
/* 245:    */       }
/* 246:    */     }
/* 247:386 */     if (couldBeDate) {
/* 248:387 */       return CellFormatType.DATE;
/* 249:    */     }
/* 250:388 */     if (seenZero) {
/* 251:389 */       return CellFormatType.NUMBER;
/* 252:    */     }
/* 253:390 */     return CellFormatType.TEXT;
/* 254:    */   }
/* 255:    */   
/* 256:    */   static String quoteSpecial(String repl, CellFormatType type)
/* 257:    */   {
/* 258:406 */     StringBuilder sb = new StringBuilder();
/* 259:407 */     for (int i = 0; i < repl.length(); i++)
/* 260:    */     {
/* 261:408 */       char ch = repl.charAt(i);
/* 262:409 */       if ((ch == '\'') && (type.isSpecial('\'')))
/* 263:    */       {
/* 264:410 */         sb.append('\000');
/* 265:    */       }
/* 266:    */       else
/* 267:    */       {
/* 268:414 */         boolean special = type.isSpecial(ch);
/* 269:415 */         if (special) {
/* 270:416 */           sb.append("'");
/* 271:    */         }
/* 272:417 */         sb.append(ch);
/* 273:418 */         if (special) {
/* 274:419 */           sb.append("'");
/* 275:    */         }
/* 276:    */       }
/* 277:    */     }
/* 278:421 */     return sb.toString();
/* 279:    */   }
/* 280:    */   
/* 281:    */   public CellFormatResult apply(Object value)
/* 282:    */   {
/* 283:434 */     boolean applies = applies(value);
/* 284:    */     Color textColor;
/* 285:    */     String text;
/* 286:    */     Color textColor;
/* 287:437 */     if (applies)
/* 288:    */     {
/* 289:438 */       String text = this.format.format(value);
/* 290:439 */       textColor = this.color;
/* 291:    */     }
/* 292:    */     else
/* 293:    */     {
/* 294:441 */       text = this.format.simpleFormat(value);
/* 295:442 */       textColor = null;
/* 296:    */     }
/* 297:444 */     return new CellFormatResult(applies, text, textColor);
/* 298:    */   }
/* 299:    */   
/* 300:    */   public CellFormatResult apply(JLabel label, Object value)
/* 301:    */   {
/* 302:457 */     CellFormatResult result = apply(value);
/* 303:458 */     label.setText(result.text);
/* 304:459 */     if (result.textColor != null) {
/* 305:460 */       label.setForeground(result.textColor);
/* 306:    */     }
/* 307:462 */     return result;
/* 308:    */   }
/* 309:    */   
/* 310:    */   CellFormatType getCellFormatType()
/* 311:    */   {
/* 312:472 */     return this.type;
/* 313:    */   }
/* 314:    */   
/* 315:    */   boolean hasCondition()
/* 316:    */   {
/* 317:481 */     return this.condition != null;
/* 318:    */   }
/* 319:    */   
/* 320:    */   public static StringBuffer parseFormat(String fdesc, CellFormatType type, PartHandler partHandler)
/* 321:    */   {
/* 322:502 */     Matcher m = SPECIFICATION_PAT.matcher(fdesc);
/* 323:503 */     StringBuffer fmt = new StringBuffer();
/* 324:504 */     while (m.find())
/* 325:    */     {
/* 326:505 */       String part = group(m, 0);
/* 327:506 */       if (part.length() > 0)
/* 328:    */       {
/* 329:507 */         String repl = partHandler.handlePart(m, part, type, fmt);
/* 330:508 */         if (repl == null) {
/* 331:509 */           switch (part.charAt(0))
/* 332:    */           {
/* 333:    */           case '"': 
/* 334:511 */             repl = quoteSpecial(part.substring(1, part.length() - 1), type);
/* 335:    */             
/* 336:513 */             break;
/* 337:    */           case '\\': 
/* 338:515 */             repl = quoteSpecial(part.substring(1), type);
/* 339:516 */             break;
/* 340:    */           case '_': 
/* 341:518 */             repl = " ";
/* 342:519 */             break;
/* 343:    */           case '*': 
/* 344:521 */             repl = expandChar(part);
/* 345:522 */             break;
/* 346:    */           default: 
/* 347:524 */             repl = part;
/* 348:    */           }
/* 349:    */         }
/* 350:528 */         m.appendReplacement(fmt, Matcher.quoteReplacement(repl));
/* 351:    */       }
/* 352:    */     }
/* 353:531 */     m.appendTail(fmt);
/* 354:533 */     if (type.isSpecial('\''))
/* 355:    */     {
/* 356:535 */       int pos = 0;
/* 357:536 */       while ((pos = fmt.indexOf("''", pos)) >= 0) {
/* 358:537 */         fmt.delete(pos, pos + 2);
/* 359:    */       }
/* 360:541 */       pos = 0;
/* 361:542 */       while ((pos = fmt.indexOf("", pos)) >= 0) {
/* 362:543 */         fmt.replace(pos, pos + 1, "''");
/* 363:    */       }
/* 364:    */     }
/* 365:547 */     return fmt;
/* 366:    */   }
/* 367:    */   
/* 368:    */   static String expandChar(String part)
/* 369:    */   {
/* 370:562 */     char ch = part.charAt(1);
/* 371:563 */     String repl = "" + ch + ch + ch;
/* 372:564 */     return repl;
/* 373:    */   }
/* 374:    */   
/* 375:    */   public static String group(Matcher m, int g)
/* 376:    */   {
/* 377:577 */     String str = m.group(g);
/* 378:578 */     return str == null ? "" : str;
/* 379:    */   }
/* 380:    */   
/* 381:    */   public String toString()
/* 382:    */   {
/* 383:582 */     return this.format.format;
/* 384:    */   }
/* 385:    */   
/* 386:    */   static abstract interface PartHandler
/* 387:    */   {
/* 388:    */     public abstract String handlePart(Matcher paramMatcher, String paramString, CellFormatType paramCellFormatType, StringBuffer paramStringBuffer);
/* 389:    */   }
/* 390:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.format.CellFormatPart
 * JD-Core Version:    0.7.0.1
 */