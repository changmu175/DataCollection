/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.CFRuleBase;
/*   4:    */ import org.apache.poi.hssf.util.HSSFColor;
/*   5:    */ import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
/*   6:    */ import org.apache.poi.ss.usermodel.BorderStyle;
/*   7:    */ import org.apache.poi.ss.usermodel.Color;
/*   8:    */ 
/*   9:    */ public final class HSSFBorderFormatting
/*  10:    */   implements org.apache.poi.ss.usermodel.BorderFormatting
/*  11:    */ {
/*  12:    */   private final HSSFWorkbook workbook;
/*  13:    */   private final CFRuleBase cfRuleRecord;
/*  14:    */   private final org.apache.poi.hssf.record.cf.BorderFormatting borderFormatting;
/*  15:    */   
/*  16:    */   protected HSSFBorderFormatting(CFRuleBase cfRuleRecord, HSSFWorkbook workbook)
/*  17:    */   {
/*  18: 37 */     this.workbook = workbook;
/*  19: 38 */     this.cfRuleRecord = cfRuleRecord;
/*  20: 39 */     this.borderFormatting = cfRuleRecord.getBorderFormatting();
/*  21:    */   }
/*  22:    */   
/*  23:    */   protected org.apache.poi.hssf.record.cf.BorderFormatting getBorderFormattingBlock()
/*  24:    */   {
/*  25: 43 */     return this.borderFormatting;
/*  26:    */   }
/*  27:    */   
/*  28:    */   /**
/*  29:    */    * @deprecated
/*  30:    */    */
/*  31:    */   public short getBorderBottom()
/*  32:    */   {
/*  33: 52 */     return (short)this.borderFormatting.getBorderBottom();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public BorderStyle getBorderBottomEnum()
/*  37:    */   {
/*  38: 59 */     return BorderStyle.valueOf((short)this.borderFormatting.getBorderBottom());
/*  39:    */   }
/*  40:    */   
/*  41:    */   /**
/*  42:    */    * @deprecated
/*  43:    */    */
/*  44:    */   public short getBorderDiagonal()
/*  45:    */   {
/*  46: 68 */     return (short)this.borderFormatting.getBorderDiagonal();
/*  47:    */   }
/*  48:    */   
/*  49:    */   public BorderStyle getBorderDiagonalEnum()
/*  50:    */   {
/*  51: 75 */     return BorderStyle.valueOf((short)this.borderFormatting.getBorderDiagonal());
/*  52:    */   }
/*  53:    */   
/*  54:    */   /**
/*  55:    */    * @deprecated
/*  56:    */    */
/*  57:    */   public short getBorderLeft()
/*  58:    */   {
/*  59: 84 */     return (short)this.borderFormatting.getBorderLeft();
/*  60:    */   }
/*  61:    */   
/*  62:    */   public BorderStyle getBorderLeftEnum()
/*  63:    */   {
/*  64: 91 */     return BorderStyle.valueOf((short)this.borderFormatting.getBorderLeft());
/*  65:    */   }
/*  66:    */   
/*  67:    */   /**
/*  68:    */    * @deprecated
/*  69:    */    */
/*  70:    */   public short getBorderRight()
/*  71:    */   {
/*  72:100 */     return (short)this.borderFormatting.getBorderRight();
/*  73:    */   }
/*  74:    */   
/*  75:    */   public BorderStyle getBorderRightEnum()
/*  76:    */   {
/*  77:107 */     return BorderStyle.valueOf((short)this.borderFormatting.getBorderRight());
/*  78:    */   }
/*  79:    */   
/*  80:    */   /**
/*  81:    */    * @deprecated
/*  82:    */    */
/*  83:    */   public short getBorderTop()
/*  84:    */   {
/*  85:116 */     return (short)this.borderFormatting.getBorderTop();
/*  86:    */   }
/*  87:    */   
/*  88:    */   public BorderStyle getBorderTopEnum()
/*  89:    */   {
/*  90:123 */     return BorderStyle.valueOf((short)this.borderFormatting.getBorderTop());
/*  91:    */   }
/*  92:    */   
/*  93:    */   public short getBottomBorderColor()
/*  94:    */   {
/*  95:128 */     return (short)this.borderFormatting.getBottomBorderColor();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public HSSFColor getBottomBorderColorColor()
/*  99:    */   {
/* 100:132 */     return this.workbook.getCustomPalette().getColor(this.borderFormatting.getBottomBorderColor());
/* 101:    */   }
/* 102:    */   
/* 103:    */   public short getDiagonalBorderColor()
/* 104:    */   {
/* 105:139 */     return (short)this.borderFormatting.getDiagonalBorderColor();
/* 106:    */   }
/* 107:    */   
/* 108:    */   public HSSFColor getDiagonalBorderColorColor()
/* 109:    */   {
/* 110:143 */     return this.workbook.getCustomPalette().getColor(this.borderFormatting.getDiagonalBorderColor());
/* 111:    */   }
/* 112:    */   
/* 113:    */   public short getLeftBorderColor()
/* 114:    */   {
/* 115:150 */     return (short)this.borderFormatting.getLeftBorderColor();
/* 116:    */   }
/* 117:    */   
/* 118:    */   public HSSFColor getLeftBorderColorColor()
/* 119:    */   {
/* 120:154 */     return this.workbook.getCustomPalette().getColor(this.borderFormatting.getLeftBorderColor());
/* 121:    */   }
/* 122:    */   
/* 123:    */   public short getRightBorderColor()
/* 124:    */   {
/* 125:161 */     return (short)this.borderFormatting.getRightBorderColor();
/* 126:    */   }
/* 127:    */   
/* 128:    */   public HSSFColor getRightBorderColorColor()
/* 129:    */   {
/* 130:165 */     return this.workbook.getCustomPalette().getColor(this.borderFormatting.getRightBorderColor());
/* 131:    */   }
/* 132:    */   
/* 133:    */   public short getTopBorderColor()
/* 134:    */   {
/* 135:172 */     return (short)this.borderFormatting.getTopBorderColor();
/* 136:    */   }
/* 137:    */   
/* 138:    */   public HSSFColor getTopBorderColorColor()
/* 139:    */   {
/* 140:176 */     return this.workbook.getCustomPalette().getColor(this.borderFormatting.getTopBorderColor());
/* 141:    */   }
/* 142:    */   
/* 143:    */   public boolean isBackwardDiagonalOn()
/* 144:    */   {
/* 145:182 */     return this.borderFormatting.isBackwardDiagonalOn();
/* 146:    */   }
/* 147:    */   
/* 148:    */   public boolean isForwardDiagonalOn()
/* 149:    */   {
/* 150:185 */     return this.borderFormatting.isForwardDiagonalOn();
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void setBackwardDiagonalOn(boolean on)
/* 154:    */   {
/* 155:189 */     this.borderFormatting.setBackwardDiagonalOn(on);
/* 156:190 */     if (on) {
/* 157:191 */       this.cfRuleRecord.setTopLeftBottomRightBorderModified(on);
/* 158:    */     }
/* 159:    */   }
/* 160:    */   
/* 161:    */   public void setForwardDiagonalOn(boolean on)
/* 162:    */   {
/* 163:195 */     this.borderFormatting.setForwardDiagonalOn(on);
/* 164:196 */     if (on) {
/* 165:197 */       this.cfRuleRecord.setBottomLeftTopRightBorderModified(on);
/* 166:    */     }
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void setBorderBottom(short border)
/* 170:    */   {
/* 171:203 */     this.borderFormatting.setBorderBottom(border);
/* 172:204 */     if (border != 0) {
/* 173:205 */       this.cfRuleRecord.setBottomBorderModified(true);
/* 174:    */     } else {
/* 175:207 */       this.cfRuleRecord.setBottomBorderModified(false);
/* 176:    */     }
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void setBorderBottom(BorderStyle border)
/* 180:    */   {
/* 181:212 */     setBorderBottom(border.getCode());
/* 182:    */   }
/* 183:    */   
/* 184:    */   public void setBorderDiagonal(short border)
/* 185:    */   {
/* 186:217 */     this.borderFormatting.setBorderDiagonal(border);
/* 187:218 */     if (border != 0)
/* 188:    */     {
/* 189:219 */       this.cfRuleRecord.setBottomLeftTopRightBorderModified(true);
/* 190:220 */       this.cfRuleRecord.setTopLeftBottomRightBorderModified(true);
/* 191:    */     }
/* 192:    */     else
/* 193:    */     {
/* 194:222 */       this.cfRuleRecord.setBottomLeftTopRightBorderModified(false);
/* 195:223 */       this.cfRuleRecord.setTopLeftBottomRightBorderModified(false);
/* 196:    */     }
/* 197:    */   }
/* 198:    */   
/* 199:    */   public void setBorderDiagonal(BorderStyle border)
/* 200:    */   {
/* 201:228 */     setBorderDiagonal(border.getCode());
/* 202:    */   }
/* 203:    */   
/* 204:    */   public void setBorderLeft(short border)
/* 205:    */   {
/* 206:233 */     this.borderFormatting.setBorderLeft(border);
/* 207:234 */     if (border != 0) {
/* 208:235 */       this.cfRuleRecord.setLeftBorderModified(true);
/* 209:    */     } else {
/* 210:237 */       this.cfRuleRecord.setLeftBorderModified(false);
/* 211:    */     }
/* 212:    */   }
/* 213:    */   
/* 214:    */   public void setBorderLeft(BorderStyle border)
/* 215:    */   {
/* 216:242 */     setBorderLeft(border.getCode());
/* 217:    */   }
/* 218:    */   
/* 219:    */   public void setBorderRight(short border)
/* 220:    */   {
/* 221:247 */     this.borderFormatting.setBorderRight(border);
/* 222:248 */     if (border != 0) {
/* 223:249 */       this.cfRuleRecord.setRightBorderModified(true);
/* 224:    */     } else {
/* 225:251 */       this.cfRuleRecord.setRightBorderModified(false);
/* 226:    */     }
/* 227:    */   }
/* 228:    */   
/* 229:    */   public void setBorderRight(BorderStyle border)
/* 230:    */   {
/* 231:256 */     setBorderRight(border.getCode());
/* 232:    */   }
/* 233:    */   
/* 234:    */   public void setBorderTop(short border)
/* 235:    */   {
/* 236:261 */     this.borderFormatting.setBorderTop(border);
/* 237:262 */     if (border != 0) {
/* 238:263 */       this.cfRuleRecord.setTopBorderModified(true);
/* 239:    */     } else {
/* 240:265 */       this.cfRuleRecord.setTopBorderModified(false);
/* 241:    */     }
/* 242:    */   }
/* 243:    */   
/* 244:    */   public void setBorderTop(BorderStyle border)
/* 245:    */   {
/* 246:270 */     setBorderTop(border.getCode());
/* 247:    */   }
/* 248:    */   
/* 249:    */   public void setBottomBorderColor(short color)
/* 250:    */   {
/* 251:275 */     this.borderFormatting.setBottomBorderColor(color);
/* 252:276 */     if (color != 0) {
/* 253:277 */       this.cfRuleRecord.setBottomBorderModified(true);
/* 254:    */     } else {
/* 255:279 */       this.cfRuleRecord.setBottomBorderModified(false);
/* 256:    */     }
/* 257:    */   }
/* 258:    */   
/* 259:    */   public void setBottomBorderColor(Color color)
/* 260:    */   {
/* 261:283 */     HSSFColor hcolor = HSSFColor.toHSSFColor(color);
/* 262:284 */     if (hcolor == null) {
/* 263:285 */       setBottomBorderColor((short)0);
/* 264:    */     } else {
/* 265:287 */       setBottomBorderColor(hcolor.getIndex());
/* 266:    */     }
/* 267:    */   }
/* 268:    */   
/* 269:    */   public void setDiagonalBorderColor(short color)
/* 270:    */   {
/* 271:293 */     this.borderFormatting.setDiagonalBorderColor(color);
/* 272:294 */     if (color != 0)
/* 273:    */     {
/* 274:295 */       this.cfRuleRecord.setBottomLeftTopRightBorderModified(true);
/* 275:296 */       this.cfRuleRecord.setTopLeftBottomRightBorderModified(true);
/* 276:    */     }
/* 277:    */     else
/* 278:    */     {
/* 279:298 */       this.cfRuleRecord.setBottomLeftTopRightBorderModified(false);
/* 280:299 */       this.cfRuleRecord.setTopLeftBottomRightBorderModified(false);
/* 281:    */     }
/* 282:    */   }
/* 283:    */   
/* 284:    */   public void setDiagonalBorderColor(Color color)
/* 285:    */   {
/* 286:304 */     HSSFColor hcolor = HSSFColor.toHSSFColor(color);
/* 287:305 */     if (hcolor == null) {
/* 288:306 */       setDiagonalBorderColor((short)0);
/* 289:    */     } else {
/* 290:308 */       setDiagonalBorderColor(hcolor.getIndex());
/* 291:    */     }
/* 292:    */   }
/* 293:    */   
/* 294:    */   public void setLeftBorderColor(short color)
/* 295:    */   {
/* 296:314 */     this.borderFormatting.setLeftBorderColor(color);
/* 297:315 */     if (color != 0) {
/* 298:316 */       this.cfRuleRecord.setLeftBorderModified(true);
/* 299:    */     } else {
/* 300:318 */       this.cfRuleRecord.setLeftBorderModified(false);
/* 301:    */     }
/* 302:    */   }
/* 303:    */   
/* 304:    */   public void setLeftBorderColor(Color color)
/* 305:    */   {
/* 306:323 */     HSSFColor hcolor = HSSFColor.toHSSFColor(color);
/* 307:324 */     if (hcolor == null) {
/* 308:325 */       setLeftBorderColor((short)0);
/* 309:    */     } else {
/* 310:327 */       setLeftBorderColor(hcolor.getIndex());
/* 311:    */     }
/* 312:    */   }
/* 313:    */   
/* 314:    */   public void setRightBorderColor(short color)
/* 315:    */   {
/* 316:333 */     this.borderFormatting.setRightBorderColor(color);
/* 317:334 */     if (color != 0) {
/* 318:335 */       this.cfRuleRecord.setRightBorderModified(true);
/* 319:    */     } else {
/* 320:337 */       this.cfRuleRecord.setRightBorderModified(false);
/* 321:    */     }
/* 322:    */   }
/* 323:    */   
/* 324:    */   public void setRightBorderColor(Color color)
/* 325:    */   {
/* 326:342 */     HSSFColor hcolor = HSSFColor.toHSSFColor(color);
/* 327:343 */     if (hcolor == null) {
/* 328:344 */       setRightBorderColor((short)0);
/* 329:    */     } else {
/* 330:346 */       setRightBorderColor(hcolor.getIndex());
/* 331:    */     }
/* 332:    */   }
/* 333:    */   
/* 334:    */   public void setTopBorderColor(short color)
/* 335:    */   {
/* 336:352 */     this.borderFormatting.setTopBorderColor(color);
/* 337:353 */     if (color != 0) {
/* 338:354 */       this.cfRuleRecord.setTopBorderModified(true);
/* 339:    */     } else {
/* 340:356 */       this.cfRuleRecord.setTopBorderModified(false);
/* 341:    */     }
/* 342:    */   }
/* 343:    */   
/* 344:    */   public void setTopBorderColor(Color color)
/* 345:    */   {
/* 346:361 */     HSSFColor hcolor = HSSFColor.toHSSFColor(color);
/* 347:362 */     if (hcolor == null) {
/* 348:363 */       setTopBorderColor((short)0);
/* 349:    */     } else {
/* 350:365 */       setTopBorderColor(hcolor.getIndex());
/* 351:    */     }
/* 352:    */   }
/* 353:    */   
/* 354:    */   public BorderStyle getBorderVerticalEnum()
/* 355:    */   {
/* 356:374 */     return BorderStyle.NONE;
/* 357:    */   }
/* 358:    */   
/* 359:    */   public BorderStyle getBorderHorizontalEnum()
/* 360:    */   {
/* 361:382 */     return BorderStyle.NONE;
/* 362:    */   }
/* 363:    */   
/* 364:    */   public short getVerticalBorderColor()
/* 365:    */   {
/* 366:390 */     return HSSFColorPredefined.AUTOMATIC.getIndex();
/* 367:    */   }
/* 368:    */   
/* 369:    */   public Color getVerticalBorderColorColor()
/* 370:    */   {
/* 371:398 */     return HSSFColorPredefined.AUTOMATIC.getColor();
/* 372:    */   }
/* 373:    */   
/* 374:    */   public short getHorizontalBorderColor()
/* 375:    */   {
/* 376:406 */     return HSSFColorPredefined.AUTOMATIC.getIndex();
/* 377:    */   }
/* 378:    */   
/* 379:    */   public Color getHorizontalBorderColorColor()
/* 380:    */   {
/* 381:414 */     return HSSFColorPredefined.AUTOMATIC.getColor();
/* 382:    */   }
/* 383:    */   
/* 384:    */   public void setBorderHorizontal(BorderStyle border) {}
/* 385:    */   
/* 386:    */   public void setBorderVertical(BorderStyle border) {}
/* 387:    */   
/* 388:    */   public void setHorizontalBorderColor(short color) {}
/* 389:    */   
/* 390:    */   public void setHorizontalBorderColor(Color color) {}
/* 391:    */   
/* 392:    */   public void setVerticalBorderColor(short color) {}
/* 393:    */   
/* 394:    */   public void setVerticalBorderColor(Color color) {}
/* 395:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFBorderFormatting

 * JD-Core Version:    0.7.0.1

 */