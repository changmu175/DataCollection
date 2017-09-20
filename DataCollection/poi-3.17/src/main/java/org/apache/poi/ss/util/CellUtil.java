/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.HashSet;
/*   7:    */ import java.util.Locale;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Set;
/*  10:    */ import org.apache.poi.ss.usermodel.BorderStyle;
/*  11:    */ import org.apache.poi.ss.usermodel.Cell;
/*  12:    */ import org.apache.poi.ss.usermodel.CellStyle;
/*  13:    */ import org.apache.poi.ss.usermodel.CreationHelper;
/*  14:    */ import org.apache.poi.ss.usermodel.FillPatternType;
/*  15:    */ import org.apache.poi.ss.usermodel.Font;
/*  16:    */ import org.apache.poi.ss.usermodel.HorizontalAlignment;
/*  17:    */ import org.apache.poi.ss.usermodel.RichTextString;
/*  18:    */ import org.apache.poi.ss.usermodel.Row;
/*  19:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  20:    */ import org.apache.poi.ss.usermodel.VerticalAlignment;
/*  21:    */ import org.apache.poi.ss.usermodel.Workbook;
/*  22:    */ import org.apache.poi.util.POILogFactory;
/*  23:    */ import org.apache.poi.util.POILogger;
/*  24:    */ 
/*  25:    */ public final class CellUtil
/*  26:    */ {
/*  27: 53 */   private static final POILogger log = POILogFactory.getLogger(CellUtil.class);
/*  28:    */   public static final String ALIGNMENT = "alignment";
/*  29:    */   public static final String BORDER_BOTTOM = "borderBottom";
/*  30:    */   public static final String BORDER_LEFT = "borderLeft";
/*  31:    */   public static final String BORDER_RIGHT = "borderRight";
/*  32:    */   public static final String BORDER_TOP = "borderTop";
/*  33:    */   public static final String BOTTOM_BORDER_COLOR = "bottomBorderColor";
/*  34:    */   public static final String LEFT_BORDER_COLOR = "leftBorderColor";
/*  35:    */   public static final String RIGHT_BORDER_COLOR = "rightBorderColor";
/*  36:    */   public static final String TOP_BORDER_COLOR = "topBorderColor";
/*  37:    */   public static final String DATA_FORMAT = "dataFormat";
/*  38:    */   public static final String FILL_BACKGROUND_COLOR = "fillBackgroundColor";
/*  39:    */   public static final String FILL_FOREGROUND_COLOR = "fillForegroundColor";
/*  40:    */   public static final String FILL_PATTERN = "fillPattern";
/*  41:    */   public static final String FONT = "font";
/*  42:    */   public static final String HIDDEN = "hidden";
/*  43:    */   public static final String INDENTION = "indention";
/*  44:    */   public static final String LOCKED = "locked";
/*  45:    */   public static final String ROTATION = "rotation";
/*  46:    */   public static final String VERTICAL_ALIGNMENT = "verticalAlignment";
/*  47:    */   public static final String WRAP_TEXT = "wrapText";
/*  48: 77 */   private static final Set<String> shortValues = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] { "bottomBorderColor", "leftBorderColor", "rightBorderColor", "topBorderColor", "fillForegroundColor", "fillBackgroundColor", "indention", "dataFormat", "font", "rotation" })));
/*  49: 90 */   private static final Set<String> booleanValues = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] { "locked", "hidden", "wrapText" })));
/*  50: 96 */   private static final Set<String> borderTypeValues = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] { "borderBottom", "borderLeft", "borderRight", "borderTop" })));
/*  51:    */   
/*  52:    */   private static final class UnicodeMapping
/*  53:    */   {
/*  54:    */     public final String entityName;
/*  55:    */     public final String resolvedValue;
/*  56:    */     
/*  57:    */     public UnicodeMapping(String pEntityName, String pResolvedValue)
/*  58:    */     {
/*  59:114 */       this.entityName = ("&" + pEntityName + ";");
/*  60:115 */       this.resolvedValue = pResolvedValue;
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public static Row getRow(int rowIndex, Sheet sheet)
/*  65:    */   {
/*  66:131 */     Row row = sheet.getRow(rowIndex);
/*  67:132 */     if (row == null) {
/*  68:133 */       row = sheet.createRow(rowIndex);
/*  69:    */     }
/*  70:135 */     return row;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public static Cell getCell(Row row, int columnIndex)
/*  74:    */   {
/*  75:147 */     Cell cell = row.getCell(columnIndex);
/*  76:149 */     if (cell == null) {
/*  77:150 */       cell = row.createCell(columnIndex);
/*  78:    */     }
/*  79:152 */     return cell;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public static Cell createCell(Row row, int column, String value, CellStyle style)
/*  83:    */   {
/*  84:166 */     Cell cell = getCell(row, column);
/*  85:    */     
/*  86:168 */     cell.setCellValue(cell.getRow().getSheet().getWorkbook().getCreationHelper().createRichTextString(value));
/*  87:170 */     if (style != null) {
/*  88:171 */       cell.setCellStyle(style);
/*  89:    */     }
/*  90:173 */     return cell;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public static Cell createCell(Row row, int column, String value)
/*  94:    */   {
/*  95:186 */     return createCell(row, column, value, null);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static void setAlignment(Cell cell, HorizontalAlignment align)
/*  99:    */   {
/* 100:205 */     setCellStyleProperty(cell, "alignment", align);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public static void setVerticalAlignment(Cell cell, VerticalAlignment align)
/* 104:    */   {
/* 105:224 */     setCellStyleProperty(cell, "verticalAlignment", align);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public static void setFont(Cell cell, Font font)
/* 109:    */   {
/* 110:236 */     Workbook wb = cell.getSheet().getWorkbook();
/* 111:237 */     short fontIndex = font.getIndex();
/* 112:238 */     if (!wb.getFontAt(fontIndex).equals(font)) {
/* 113:239 */       throw new IllegalArgumentException("Font does not belong to this workbook");
/* 114:    */     }
/* 115:245 */     setCellStyleProperty(cell, "font", Short.valueOf(fontIndex));
/* 116:    */   }
/* 117:    */   
/* 118:    */   public static void setCellStyleProperties(Cell cell, Map<String, Object> properties)
/* 119:    */   {
/* 120:274 */     Workbook workbook = cell.getSheet().getWorkbook();
/* 121:275 */     CellStyle originalStyle = cell.getCellStyle();
/* 122:276 */     CellStyle newStyle = null;
/* 123:277 */     Map<String, Object> values = getFormatProperties(originalStyle);
/* 124:278 */     putAll(properties, values);
/* 125:    */     
/* 126:    */ 
/* 127:    */ 
/* 128:282 */     int numberCellStyles = workbook.getNumCellStyles();
/* 129:284 */     for (int i = 0; i < numberCellStyles; i++)
/* 130:    */     {
/* 131:285 */       CellStyle wbStyle = workbook.getCellStyleAt(i);
/* 132:286 */       Map<String, Object> wbStyleMap = getFormatProperties(wbStyle);
/* 133:289 */       if (wbStyleMap.equals(values))
/* 134:    */       {
/* 135:290 */         newStyle = wbStyle;
/* 136:291 */         break;
/* 137:    */       }
/* 138:    */     }
/* 139:296 */     if (newStyle == null)
/* 140:    */     {
/* 141:297 */       newStyle = workbook.createCellStyle();
/* 142:298 */       setFormatProperties(newStyle, workbook, values);
/* 143:    */     }
/* 144:301 */     cell.setCellStyle(newStyle);
/* 145:    */   }
/* 146:    */   
/* 147:    */   public static void setCellStyleProperty(Cell cell, String propertyName, Object propertyValue)
/* 148:    */   {
/* 149:322 */     Map<String, Object> property = Collections.singletonMap(propertyName, propertyValue);
/* 150:323 */     setCellStyleProperties(cell, property);
/* 151:    */   }
/* 152:    */   
/* 153:    */   private static Map<String, Object> getFormatProperties(CellStyle style)
/* 154:    */   {
/* 155:337 */     Map<String, Object> properties = new HashMap();
/* 156:338 */     put(properties, "alignment", style.getAlignmentEnum());
/* 157:339 */     put(properties, "verticalAlignment", style.getVerticalAlignmentEnum());
/* 158:340 */     put(properties, "borderBottom", style.getBorderBottomEnum());
/* 159:341 */     put(properties, "borderLeft", style.getBorderLeftEnum());
/* 160:342 */     put(properties, "borderRight", style.getBorderRightEnum());
/* 161:343 */     put(properties, "borderTop", style.getBorderTopEnum());
/* 162:344 */     put(properties, "bottomBorderColor", Short.valueOf(style.getBottomBorderColor()));
/* 163:345 */     put(properties, "dataFormat", Short.valueOf(style.getDataFormat()));
/* 164:346 */     put(properties, "fillPattern", style.getFillPatternEnum());
/* 165:347 */     put(properties, "fillForegroundColor", Short.valueOf(style.getFillForegroundColor()));
/* 166:348 */     put(properties, "fillBackgroundColor", Short.valueOf(style.getFillBackgroundColor()));
/* 167:349 */     put(properties, "font", Short.valueOf(style.getFontIndex()));
/* 168:350 */     put(properties, "hidden", Boolean.valueOf(style.getHidden()));
/* 169:351 */     put(properties, "indention", Short.valueOf(style.getIndention()));
/* 170:352 */     put(properties, "leftBorderColor", Short.valueOf(style.getLeftBorderColor()));
/* 171:353 */     put(properties, "locked", Boolean.valueOf(style.getLocked()));
/* 172:354 */     put(properties, "rightBorderColor", Short.valueOf(style.getRightBorderColor()));
/* 173:355 */     put(properties, "rotation", Short.valueOf(style.getRotation()));
/* 174:356 */     put(properties, "topBorderColor", Short.valueOf(style.getTopBorderColor()));
/* 175:357 */     put(properties, "wrapText", Boolean.valueOf(style.getWrapText()));
/* 176:358 */     return properties;
/* 177:    */   }
/* 178:    */   
/* 179:    */   private static void putAll(Map<String, Object> src, Map<String, Object> dest)
/* 180:    */   {
/* 181:370 */     for (String key : src.keySet()) {
/* 182:371 */       if (shortValues.contains(key)) {
/* 183:372 */         dest.put(key, Short.valueOf(getShort(src, key)));
/* 184:373 */       } else if (booleanValues.contains(key)) {
/* 185:374 */         dest.put(key, Boolean.valueOf(getBoolean(src, key)));
/* 186:375 */       } else if (borderTypeValues.contains(key)) {
/* 187:376 */         dest.put(key, getBorderStyle(src, key));
/* 188:377 */       } else if ("alignment".equals(key)) {
/* 189:378 */         dest.put(key, getHorizontalAlignment(src, key));
/* 190:379 */       } else if ("verticalAlignment".equals(key)) {
/* 191:380 */         dest.put(key, getVerticalAlignment(src, key));
/* 192:381 */       } else if ("fillPattern".equals(key)) {
/* 193:382 */         dest.put(key, getFillPattern(src, key));
/* 194:384 */       } else if (log.check(3)) {
/* 195:385 */         log.log(3, new Object[] { "Ignoring unrecognized CellUtil format properties key: " + key });
/* 196:    */       }
/* 197:    */     }
/* 198:    */   }
/* 199:    */   
/* 200:    */   private static void setFormatProperties(CellStyle style, Workbook workbook, Map<String, Object> properties)
/* 201:    */   {
/* 202:400 */     style.setAlignment(getHorizontalAlignment(properties, "alignment"));
/* 203:401 */     style.setVerticalAlignment(getVerticalAlignment(properties, "verticalAlignment"));
/* 204:402 */     style.setBorderBottom(getBorderStyle(properties, "borderBottom"));
/* 205:403 */     style.setBorderLeft(getBorderStyle(properties, "borderLeft"));
/* 206:404 */     style.setBorderRight(getBorderStyle(properties, "borderRight"));
/* 207:405 */     style.setBorderTop(getBorderStyle(properties, "borderTop"));
/* 208:406 */     style.setBottomBorderColor(getShort(properties, "bottomBorderColor"));
/* 209:407 */     style.setDataFormat(getShort(properties, "dataFormat"));
/* 210:408 */     style.setFillPattern(getFillPattern(properties, "fillPattern"));
/* 211:409 */     style.setFillForegroundColor(getShort(properties, "fillForegroundColor"));
/* 212:410 */     style.setFillBackgroundColor(getShort(properties, "fillBackgroundColor"));
/* 213:411 */     style.setFont(workbook.getFontAt(getShort(properties, "font")));
/* 214:412 */     style.setHidden(getBoolean(properties, "hidden"));
/* 215:413 */     style.setIndention(getShort(properties, "indention"));
/* 216:414 */     style.setLeftBorderColor(getShort(properties, "leftBorderColor"));
/* 217:415 */     style.setLocked(getBoolean(properties, "locked"));
/* 218:416 */     style.setRightBorderColor(getShort(properties, "rightBorderColor"));
/* 219:417 */     style.setRotation(getShort(properties, "rotation"));
/* 220:418 */     style.setTopBorderColor(getShort(properties, "topBorderColor"));
/* 221:419 */     style.setWrapText(getBoolean(properties, "wrapText"));
/* 222:    */   }
/* 223:    */   
/* 224:    */   private static short getShort(Map<String, Object> properties, String name)
/* 225:    */   {
/* 226:431 */     Object value = properties.get(name);
/* 227:432 */     if ((value instanceof Short)) {
/* 228:433 */       return ((Short)value).shortValue();
/* 229:    */     }
/* 230:435 */     return 0;
/* 231:    */   }
/* 232:    */   
/* 233:    */   private static BorderStyle getBorderStyle(Map<String, Object> properties, String name)
/* 234:    */   {
/* 235:446 */     Object value = properties.get(name);
/* 236:    */     BorderStyle border;
/* 237:448 */     if ((value instanceof BorderStyle))
/* 238:    */     {
/* 239:449 */       border = (BorderStyle)value;
/* 240:    */     }
/* 241:    */     else
/* 242:    */     {
/* 243:    */       BorderStyle border;
/* 244:452 */       if ((value instanceof Short))
/* 245:    */       {
/* 246:453 */         if (log.check(5)) {
/* 247:454 */           log.log(5, new Object[] { "Deprecation warning: CellUtil properties map uses Short values for " + name + ". Should use BorderStyle enums instead." });
/* 248:    */         }
/* 249:457 */         short code = ((Short)value).shortValue();
/* 250:458 */         border = BorderStyle.valueOf(code);
/* 251:    */       }
/* 252:    */       else
/* 253:    */       {
/* 254:    */         BorderStyle border;
/* 255:460 */         if (value == null) {
/* 256:461 */           border = BorderStyle.NONE;
/* 257:    */         } else {
/* 258:464 */           throw new RuntimeException("Unexpected border style class. Must be BorderStyle or Short (deprecated).");
/* 259:    */         }
/* 260:    */       }
/* 261:    */     }
/* 262:    */     BorderStyle border;
/* 263:466 */     return border;
/* 264:    */   }
/* 265:    */   
/* 266:    */   private static FillPatternType getFillPattern(Map<String, Object> properties, String name)
/* 267:    */   {
/* 268:478 */     Object value = properties.get(name);
/* 269:    */     FillPatternType pattern;
/* 270:480 */     if ((value instanceof FillPatternType))
/* 271:    */     {
/* 272:481 */       pattern = (FillPatternType)value;
/* 273:    */     }
/* 274:    */     else
/* 275:    */     {
/* 276:    */       FillPatternType pattern;
/* 277:484 */       if ((value instanceof Short))
/* 278:    */       {
/* 279:485 */         if (log.check(5)) {
/* 280:486 */           log.log(5, new Object[] { "Deprecation warning: CellUtil properties map uses Short values for " + name + ". Should use FillPatternType enums instead." });
/* 281:    */         }
/* 282:489 */         short code = ((Short)value).shortValue();
/* 283:490 */         pattern = FillPatternType.forInt(code);
/* 284:    */       }
/* 285:    */       else
/* 286:    */       {
/* 287:    */         FillPatternType pattern;
/* 288:492 */         if (value == null) {
/* 289:493 */           pattern = FillPatternType.NO_FILL;
/* 290:    */         } else {
/* 291:496 */           throw new RuntimeException("Unexpected fill pattern style class. Must be FillPatternType or Short (deprecated).");
/* 292:    */         }
/* 293:    */       }
/* 294:    */     }
/* 295:    */     FillPatternType pattern;
/* 296:498 */     return pattern;
/* 297:    */   }
/* 298:    */   
/* 299:    */   private static HorizontalAlignment getHorizontalAlignment(Map<String, Object> properties, String name)
/* 300:    */   {
/* 301:510 */     Object value = properties.get(name);
/* 302:    */     HorizontalAlignment align;
/* 303:512 */     if ((value instanceof HorizontalAlignment))
/* 304:    */     {
/* 305:513 */       align = (HorizontalAlignment)value;
/* 306:    */     }
/* 307:    */     else
/* 308:    */     {
/* 309:    */       HorizontalAlignment align;
/* 310:516 */       if ((value instanceof Short))
/* 311:    */       {
/* 312:517 */         if (log.check(5)) {
/* 313:518 */           log.log(5, new Object[] { "Deprecation warning: CellUtil properties map used a Short value for " + name + ". Should use HorizontalAlignment enums instead." });
/* 314:    */         }
/* 315:521 */         short code = ((Short)value).shortValue();
/* 316:522 */         align = HorizontalAlignment.forInt(code);
/* 317:    */       }
/* 318:    */       else
/* 319:    */       {
/* 320:    */         HorizontalAlignment align;
/* 321:524 */         if (value == null) {
/* 322:525 */           align = HorizontalAlignment.GENERAL;
/* 323:    */         } else {
/* 324:528 */           throw new RuntimeException("Unexpected horizontal alignment style class. Must be HorizontalAlignment or Short (deprecated).");
/* 325:    */         }
/* 326:    */       }
/* 327:    */     }
/* 328:    */     HorizontalAlignment align;
/* 329:530 */     return align;
/* 330:    */   }
/* 331:    */   
/* 332:    */   private static VerticalAlignment getVerticalAlignment(Map<String, Object> properties, String name)
/* 333:    */   {
/* 334:542 */     Object value = properties.get(name);
/* 335:    */     VerticalAlignment align;
/* 336:544 */     if ((value instanceof VerticalAlignment))
/* 337:    */     {
/* 338:545 */       align = (VerticalAlignment)value;
/* 339:    */     }
/* 340:    */     else
/* 341:    */     {
/* 342:    */       VerticalAlignment align;
/* 343:548 */       if ((value instanceof Short))
/* 344:    */       {
/* 345:549 */         if (log.check(5)) {
/* 346:550 */           log.log(5, new Object[] { "Deprecation warning: CellUtil properties map used a Short value for " + name + ". Should use VerticalAlignment enums instead." });
/* 347:    */         }
/* 348:553 */         short code = ((Short)value).shortValue();
/* 349:554 */         align = VerticalAlignment.forInt(code);
/* 350:    */       }
/* 351:    */       else
/* 352:    */       {
/* 353:    */         VerticalAlignment align;
/* 354:556 */         if (value == null) {
/* 355:557 */           align = VerticalAlignment.BOTTOM;
/* 356:    */         } else {
/* 357:560 */           throw new RuntimeException("Unexpected vertical alignment style class. Must be VerticalAlignment or Short (deprecated).");
/* 358:    */         }
/* 359:    */       }
/* 360:    */     }
/* 361:    */     VerticalAlignment align;
/* 362:562 */     return align;
/* 363:    */   }
/* 364:    */   
/* 365:    */   private static boolean getBoolean(Map<String, Object> properties, String name)
/* 366:    */   {
/* 367:574 */     Object value = properties.get(name);
/* 368:576 */     if ((value instanceof Boolean)) {
/* 369:577 */       return ((Boolean)value).booleanValue();
/* 370:    */     }
/* 371:579 */     return false;
/* 372:    */   }
/* 373:    */   
/* 374:    */   private static void put(Map<String, Object> properties, String name, Object value)
/* 375:    */   {
/* 376:590 */     properties.put(name, value);
/* 377:    */   }
/* 378:    */   
/* 379:    */   public static Cell translateUnicodeValues(Cell cell)
/* 380:    */   {
/* 381:602 */     String s = cell.getRichStringCellValue().getString();
/* 382:603 */     boolean foundUnicode = false;
/* 383:604 */     String lowerCaseStr = s.toLowerCase(Locale.ROOT);
/* 384:606 */     for (UnicodeMapping entry : unicodeMappings)
/* 385:    */     {
/* 386:607 */       String key = entry.entityName;
/* 387:608 */       if (lowerCaseStr.contains(key))
/* 388:    */       {
/* 389:609 */         s = s.replaceAll(key, entry.resolvedValue);
/* 390:610 */         foundUnicode = true;
/* 391:    */       }
/* 392:    */     }
/* 393:613 */     if (foundUnicode) {
/* 394:614 */       cell.setCellValue(cell.getRow().getSheet().getWorkbook().getCreationHelper().createRichTextString(s));
/* 395:    */     }
/* 396:617 */     return cell;
/* 397:    */   }
/* 398:    */   
/* 399:621 */   private static UnicodeMapping[] unicodeMappings = { um("alpha", "α"), um("beta", "β"), um("gamma", "γ"), um("delta", "δ"), um("epsilon", "ε"), um("zeta", "ζ"), um("eta", "η"), um("theta", "θ"), um("iota", "ι"), um("kappa", "κ"), um("lambda", "λ"), um("mu", "μ"), um("nu", "ν"), um("xi", "ξ"), um("omicron", "ο") };
/* 400:    */   
/* 401:    */   private static UnicodeMapping um(String entityName, String resolvedValue)
/* 402:    */   {
/* 403:641 */     return new UnicodeMapping(entityName, resolvedValue);
/* 404:    */   }
/* 405:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.CellUtil
 * JD-Core Version:    0.7.0.1
 */