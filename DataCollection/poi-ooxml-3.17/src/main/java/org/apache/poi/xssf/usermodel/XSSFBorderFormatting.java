/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.BorderFormatting;
/*   4:    */ import org.apache.poi.ss.usermodel.BorderStyle;
/*   5:    */ import org.apache.poi.ss.usermodel.Color;
/*   6:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder;
/*   7:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr;
/*   8:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
/*   9:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor.Factory;
/*  10:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderStyle.Enum;
/*  11:    */ 
/*  12:    */ public class XSSFBorderFormatting
/*  13:    */   implements BorderFormatting
/*  14:    */ {
/*  15:    */   IndexedColorMap _colorMap;
/*  16:    */   CTBorder _border;
/*  17:    */   
/*  18:    */   XSSFBorderFormatting(CTBorder border, IndexedColorMap colorMap)
/*  19:    */   {
/*  20: 36 */     this._border = border;
/*  21: 37 */     this._colorMap = colorMap;
/*  22:    */   }
/*  23:    */   
/*  24:    */   /**
/*  25:    */    * @deprecated
/*  26:    */    */
/*  27:    */   public short getBorderBottom()
/*  28:    */   {
/*  29: 46 */     return getBorderBottomEnum().getCode();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public BorderStyle getBorderBottomEnum()
/*  33:    */   {
/*  34: 53 */     return getBorderStyle(this._border.getBottom());
/*  35:    */   }
/*  36:    */   
/*  37:    */   /**
/*  38:    */    * @deprecated
/*  39:    */    */
/*  40:    */   public short getBorderDiagonal()
/*  41:    */   {
/*  42: 62 */     return getBorderDiagonalEnum().getCode();
/*  43:    */   }
/*  44:    */   
/*  45:    */   public BorderStyle getBorderDiagonalEnum()
/*  46:    */   {
/*  47: 69 */     return getBorderStyle(this._border.getDiagonal());
/*  48:    */   }
/*  49:    */   
/*  50:    */   /**
/*  51:    */    * @deprecated
/*  52:    */    */
/*  53:    */   public short getBorderLeft()
/*  54:    */   {
/*  55: 78 */     return getBorderLeftEnum().getCode();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public BorderStyle getBorderLeftEnum()
/*  59:    */   {
/*  60: 85 */     return getBorderStyle(this._border.getLeft());
/*  61:    */   }
/*  62:    */   
/*  63:    */   /**
/*  64:    */    * @deprecated
/*  65:    */    */
/*  66:    */   public short getBorderRight()
/*  67:    */   {
/*  68: 94 */     return getBorderRightEnum().getCode();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public BorderStyle getBorderRightEnum()
/*  72:    */   {
/*  73:101 */     return getBorderStyle(this._border.getRight());
/*  74:    */   }
/*  75:    */   
/*  76:    */   /**
/*  77:    */    * @deprecated
/*  78:    */    */
/*  79:    */   public short getBorderTop()
/*  80:    */   {
/*  81:110 */     return getBorderTopEnum().getCode();
/*  82:    */   }
/*  83:    */   
/*  84:    */   public BorderStyle getBorderTopEnum()
/*  85:    */   {
/*  86:117 */     return getBorderStyle(this._border.getTop());
/*  87:    */   }
/*  88:    */   
/*  89:    */   public XSSFColor getBottomBorderColorColor()
/*  90:    */   {
/*  91:122 */     return getColor(this._border.getBottom());
/*  92:    */   }
/*  93:    */   
/*  94:    */   public short getBottomBorderColor()
/*  95:    */   {
/*  96:126 */     return getIndexedColor(getBottomBorderColorColor());
/*  97:    */   }
/*  98:    */   
/*  99:    */   public XSSFColor getDiagonalBorderColorColor()
/* 100:    */   {
/* 101:131 */     return getColor(this._border.getDiagonal());
/* 102:    */   }
/* 103:    */   
/* 104:    */   public short getDiagonalBorderColor()
/* 105:    */   {
/* 106:135 */     return getIndexedColor(getDiagonalBorderColorColor());
/* 107:    */   }
/* 108:    */   
/* 109:    */   public XSSFColor getLeftBorderColorColor()
/* 110:    */   {
/* 111:140 */     return getColor(this._border.getLeft());
/* 112:    */   }
/* 113:    */   
/* 114:    */   public short getLeftBorderColor()
/* 115:    */   {
/* 116:144 */     return getIndexedColor(getLeftBorderColorColor());
/* 117:    */   }
/* 118:    */   
/* 119:    */   public XSSFColor getRightBorderColorColor()
/* 120:    */   {
/* 121:149 */     return getColor(this._border.getRight());
/* 122:    */   }
/* 123:    */   
/* 124:    */   public short getRightBorderColor()
/* 125:    */   {
/* 126:153 */     return getIndexedColor(getRightBorderColorColor());
/* 127:    */   }
/* 128:    */   
/* 129:    */   public XSSFColor getTopBorderColorColor()
/* 130:    */   {
/* 131:158 */     return getColor(this._border.getTop());
/* 132:    */   }
/* 133:    */   
/* 134:    */   public short getTopBorderColor()
/* 135:    */   {
/* 136:162 */     return getIndexedColor(getRightBorderColorColor());
/* 137:    */   }
/* 138:    */   
/* 139:    */   /**
/* 140:    */    * @deprecated
/* 141:    */    */
/* 142:    */   public void setBorderBottom(short border)
/* 143:    */   {
/* 144:170 */     setBorderBottom(BorderStyle.valueOf(border));
/* 145:    */   }
/* 146:    */   
/* 147:    */   public void setBorderBottom(BorderStyle border)
/* 148:    */   {
/* 149:174 */     CTBorderPr pr = this._border.isSetBottom() ? this._border.getBottom() : this._border.addNewBottom();
/* 150:175 */     if (border == BorderStyle.NONE) {
/* 151:175 */       this._border.unsetBottom();
/* 152:    */     } else {
/* 153:176 */       pr.setStyle(STBorderStyle.Enum.forInt(border.getCode() + 1));
/* 154:    */     }
/* 155:    */   }
/* 156:    */   
/* 157:    */   /**
/* 158:    */    * @deprecated
/* 159:    */    */
/* 160:    */   public void setBorderDiagonal(short border)
/* 161:    */   {
/* 162:184 */     setBorderDiagonal(BorderStyle.valueOf(border));
/* 163:    */   }
/* 164:    */   
/* 165:    */   public void setBorderDiagonal(BorderStyle border)
/* 166:    */   {
/* 167:188 */     CTBorderPr pr = this._border.isSetDiagonal() ? this._border.getDiagonal() : this._border.addNewDiagonal();
/* 168:189 */     if (border == BorderStyle.NONE) {
/* 169:189 */       this._border.unsetDiagonal();
/* 170:    */     } else {
/* 171:190 */       pr.setStyle(STBorderStyle.Enum.forInt(border.getCode() + 1));
/* 172:    */     }
/* 173:    */   }
/* 174:    */   
/* 175:    */   /**
/* 176:    */    * @deprecated
/* 177:    */    */
/* 178:    */   public void setBorderLeft(short border)
/* 179:    */   {
/* 180:198 */     setBorderLeft(BorderStyle.valueOf(border));
/* 181:    */   }
/* 182:    */   
/* 183:    */   public void setBorderLeft(BorderStyle border)
/* 184:    */   {
/* 185:202 */     CTBorderPr pr = this._border.isSetLeft() ? this._border.getLeft() : this._border.addNewLeft();
/* 186:203 */     if (border == BorderStyle.NONE) {
/* 187:203 */       this._border.unsetLeft();
/* 188:    */     } else {
/* 189:204 */       pr.setStyle(STBorderStyle.Enum.forInt(border.getCode() + 1));
/* 190:    */     }
/* 191:    */   }
/* 192:    */   
/* 193:    */   /**
/* 194:    */    * @deprecated
/* 195:    */    */
/* 196:    */   public void setBorderRight(short border)
/* 197:    */   {
/* 198:212 */     setBorderRight(BorderStyle.valueOf(border));
/* 199:    */   }
/* 200:    */   
/* 201:    */   public void setBorderRight(BorderStyle border)
/* 202:    */   {
/* 203:216 */     CTBorderPr pr = this._border.isSetRight() ? this._border.getRight() : this._border.addNewRight();
/* 204:217 */     if (border == BorderStyle.NONE) {
/* 205:217 */       this._border.unsetRight();
/* 206:    */     } else {
/* 207:218 */       pr.setStyle(STBorderStyle.Enum.forInt(border.getCode() + 1));
/* 208:    */     }
/* 209:    */   }
/* 210:    */   
/* 211:    */   /**
/* 212:    */    * @deprecated
/* 213:    */    */
/* 214:    */   public void setBorderTop(short border)
/* 215:    */   {
/* 216:226 */     setBorderTop(BorderStyle.valueOf(border));
/* 217:    */   }
/* 218:    */   
/* 219:    */   public void setBorderTop(BorderStyle border)
/* 220:    */   {
/* 221:230 */     CTBorderPr pr = this._border.isSetTop() ? this._border.getTop() : this._border.addNewTop();
/* 222:231 */     if (border == BorderStyle.NONE) {
/* 223:231 */       this._border.unsetTop();
/* 224:    */     } else {
/* 225:232 */       pr.setStyle(STBorderStyle.Enum.forInt(border.getCode() + 1));
/* 226:    */     }
/* 227:    */   }
/* 228:    */   
/* 229:    */   public void setBottomBorderColor(Color color)
/* 230:    */   {
/* 231:237 */     XSSFColor xcolor = XSSFColor.toXSSFColor(color);
/* 232:238 */     if (xcolor == null) {
/* 233:238 */       setBottomBorderColor((CTColor)null);
/* 234:    */     } else {
/* 235:239 */       setBottomBorderColor(xcolor.getCTColor());
/* 236:    */     }
/* 237:    */   }
/* 238:    */   
/* 239:    */   public void setBottomBorderColor(short color)
/* 240:    */   {
/* 241:243 */     CTColor ctColor = CTColor.Factory.newInstance();
/* 242:244 */     ctColor.setIndexed(color);
/* 243:245 */     setBottomBorderColor(ctColor);
/* 244:    */   }
/* 245:    */   
/* 246:    */   public void setBottomBorderColor(CTColor color)
/* 247:    */   {
/* 248:248 */     CTBorderPr pr = this._border.isSetBottom() ? this._border.getBottom() : this._border.addNewBottom();
/* 249:249 */     if (color == null) {
/* 250:250 */       pr.unsetColor();
/* 251:    */     } else {
/* 252:252 */       pr.setColor(color);
/* 253:    */     }
/* 254:    */   }
/* 255:    */   
/* 256:    */   public void setDiagonalBorderColor(Color color)
/* 257:    */   {
/* 258:258 */     XSSFColor xcolor = XSSFColor.toXSSFColor(color);
/* 259:259 */     if (xcolor == null) {
/* 260:259 */       setDiagonalBorderColor((CTColor)null);
/* 261:    */     } else {
/* 262:260 */       setDiagonalBorderColor(xcolor.getCTColor());
/* 263:    */     }
/* 264:    */   }
/* 265:    */   
/* 266:    */   public void setDiagonalBorderColor(short color)
/* 267:    */   {
/* 268:264 */     CTColor ctColor = CTColor.Factory.newInstance();
/* 269:265 */     ctColor.setIndexed(color);
/* 270:266 */     setDiagonalBorderColor(ctColor);
/* 271:    */   }
/* 272:    */   
/* 273:    */   public void setDiagonalBorderColor(CTColor color)
/* 274:    */   {
/* 275:269 */     CTBorderPr pr = this._border.isSetDiagonal() ? this._border.getDiagonal() : this._border.addNewDiagonal();
/* 276:270 */     if (color == null) {
/* 277:271 */       pr.unsetColor();
/* 278:    */     } else {
/* 279:273 */       pr.setColor(color);
/* 280:    */     }
/* 281:    */   }
/* 282:    */   
/* 283:    */   public void setLeftBorderColor(Color color)
/* 284:    */   {
/* 285:279 */     XSSFColor xcolor = XSSFColor.toXSSFColor(color);
/* 286:280 */     if (xcolor == null) {
/* 287:280 */       setLeftBorderColor((CTColor)null);
/* 288:    */     } else {
/* 289:281 */       setLeftBorderColor(xcolor.getCTColor());
/* 290:    */     }
/* 291:    */   }
/* 292:    */   
/* 293:    */   public void setLeftBorderColor(short color)
/* 294:    */   {
/* 295:285 */     CTColor ctColor = CTColor.Factory.newInstance();
/* 296:286 */     ctColor.setIndexed(color);
/* 297:287 */     setLeftBorderColor(ctColor);
/* 298:    */   }
/* 299:    */   
/* 300:    */   public void setLeftBorderColor(CTColor color)
/* 301:    */   {
/* 302:290 */     CTBorderPr pr = this._border.isSetLeft() ? this._border.getLeft() : this._border.addNewLeft();
/* 303:291 */     if (color == null) {
/* 304:292 */       pr.unsetColor();
/* 305:    */     } else {
/* 306:294 */       pr.setColor(color);
/* 307:    */     }
/* 308:    */   }
/* 309:    */   
/* 310:    */   public void setRightBorderColor(Color color)
/* 311:    */   {
/* 312:300 */     XSSFColor xcolor = XSSFColor.toXSSFColor(color);
/* 313:301 */     if (xcolor == null) {
/* 314:301 */       setRightBorderColor((CTColor)null);
/* 315:    */     } else {
/* 316:302 */       setRightBorderColor(xcolor.getCTColor());
/* 317:    */     }
/* 318:    */   }
/* 319:    */   
/* 320:    */   public void setRightBorderColor(short color)
/* 321:    */   {
/* 322:306 */     CTColor ctColor = CTColor.Factory.newInstance();
/* 323:307 */     ctColor.setIndexed(color);
/* 324:308 */     setRightBorderColor(ctColor);
/* 325:    */   }
/* 326:    */   
/* 327:    */   public void setRightBorderColor(CTColor color)
/* 328:    */   {
/* 329:311 */     CTBorderPr pr = this._border.isSetRight() ? this._border.getRight() : this._border.addNewRight();
/* 330:312 */     if (color == null) {
/* 331:313 */       pr.unsetColor();
/* 332:    */     } else {
/* 333:315 */       pr.setColor(color);
/* 334:    */     }
/* 335:    */   }
/* 336:    */   
/* 337:    */   public void setTopBorderColor(Color color)
/* 338:    */   {
/* 339:321 */     XSSFColor xcolor = XSSFColor.toXSSFColor(color);
/* 340:322 */     if (xcolor == null) {
/* 341:322 */       setTopBorderColor((CTColor)null);
/* 342:    */     } else {
/* 343:323 */       setTopBorderColor(xcolor.getCTColor());
/* 344:    */     }
/* 345:    */   }
/* 346:    */   
/* 347:    */   public void setTopBorderColor(short color)
/* 348:    */   {
/* 349:327 */     CTColor ctColor = CTColor.Factory.newInstance();
/* 350:328 */     ctColor.setIndexed(color);
/* 351:329 */     setTopBorderColor(ctColor);
/* 352:    */   }
/* 353:    */   
/* 354:    */   public void setTopBorderColor(CTColor color)
/* 355:    */   {
/* 356:332 */     CTBorderPr pr = this._border.isSetTop() ? this._border.getTop() : this._border.addNewTop();
/* 357:333 */     if (color == null) {
/* 358:334 */       pr.unsetColor();
/* 359:    */     } else {
/* 360:336 */       pr.setColor(color);
/* 361:    */     }
/* 362:    */   }
/* 363:    */   
/* 364:    */   public BorderStyle getBorderVerticalEnum()
/* 365:    */   {
/* 366:341 */     return getBorderStyle(this._border.getVertical());
/* 367:    */   }
/* 368:    */   
/* 369:    */   public BorderStyle getBorderHorizontalEnum()
/* 370:    */   {
/* 371:345 */     return getBorderStyle(this._border.getHorizontal());
/* 372:    */   }
/* 373:    */   
/* 374:    */   public short getVerticalBorderColor()
/* 375:    */   {
/* 376:349 */     return getIndexedColor(getVerticalBorderColorColor());
/* 377:    */   }
/* 378:    */   
/* 379:    */   public XSSFColor getVerticalBorderColorColor()
/* 380:    */   {
/* 381:353 */     return getColor(this._border.getVertical());
/* 382:    */   }
/* 383:    */   
/* 384:    */   public short getHorizontalBorderColor()
/* 385:    */   {
/* 386:357 */     return getIndexedColor(getHorizontalBorderColorColor());
/* 387:    */   }
/* 388:    */   
/* 389:    */   public XSSFColor getHorizontalBorderColorColor()
/* 390:    */   {
/* 391:361 */     return getColor(this._border.getHorizontal());
/* 392:    */   }
/* 393:    */   
/* 394:    */   public void setBorderHorizontal(BorderStyle border)
/* 395:    */   {
/* 396:365 */     CTBorderPr pr = this._border.isSetHorizontal() ? this._border.getHorizontal() : this._border.addNewHorizontal();
/* 397:366 */     if (border == BorderStyle.NONE) {
/* 398:366 */       this._border.unsetHorizontal();
/* 399:    */     } else {
/* 400:367 */       pr.setStyle(STBorderStyle.Enum.forInt(border.getCode() + 1));
/* 401:    */     }
/* 402:    */   }
/* 403:    */   
/* 404:    */   public void setBorderVertical(BorderStyle border)
/* 405:    */   {
/* 406:371 */     CTBorderPr pr = this._border.isSetVertical() ? this._border.getVertical() : this._border.addNewVertical();
/* 407:372 */     if (border == BorderStyle.NONE) {
/* 408:372 */       this._border.unsetVertical();
/* 409:    */     } else {
/* 410:373 */       pr.setStyle(STBorderStyle.Enum.forInt(border.getCode() + 1));
/* 411:    */     }
/* 412:    */   }
/* 413:    */   
/* 414:    */   public void setHorizontalBorderColor(short color)
/* 415:    */   {
/* 416:377 */     CTColor ctColor = CTColor.Factory.newInstance();
/* 417:378 */     ctColor.setIndexed(color);
/* 418:379 */     setHorizontalBorderColor(ctColor);
/* 419:    */   }
/* 420:    */   
/* 421:    */   public void setHorizontalBorderColor(Color color)
/* 422:    */   {
/* 423:383 */     XSSFColor xcolor = XSSFColor.toXSSFColor(color);
/* 424:384 */     if (xcolor == null) {
/* 425:384 */       setBottomBorderColor((CTColor)null);
/* 426:    */     } else {
/* 427:385 */       setHorizontalBorderColor(xcolor.getCTColor());
/* 428:    */     }
/* 429:    */   }
/* 430:    */   
/* 431:    */   public void setHorizontalBorderColor(CTColor color)
/* 432:    */   {
/* 433:389 */     CTBorderPr pr = this._border.isSetHorizontal() ? this._border.getHorizontal() : this._border.addNewHorizontal();
/* 434:390 */     if (color == null) {
/* 435:391 */       pr.unsetColor();
/* 436:    */     } else {
/* 437:393 */       pr.setColor(color);
/* 438:    */     }
/* 439:    */   }
/* 440:    */   
/* 441:    */   public void setVerticalBorderColor(short color)
/* 442:    */   {
/* 443:398 */     CTColor ctColor = CTColor.Factory.newInstance();
/* 444:399 */     ctColor.setIndexed(color);
/* 445:400 */     setVerticalBorderColor(ctColor);
/* 446:    */   }
/* 447:    */   
/* 448:    */   public void setVerticalBorderColor(Color color)
/* 449:    */   {
/* 450:404 */     XSSFColor xcolor = XSSFColor.toXSSFColor(color);
/* 451:405 */     if (xcolor == null) {
/* 452:405 */       setBottomBorderColor((CTColor)null);
/* 453:    */     } else {
/* 454:406 */       setVerticalBorderColor(xcolor.getCTColor());
/* 455:    */     }
/* 456:    */   }
/* 457:    */   
/* 458:    */   public void setVerticalBorderColor(CTColor color)
/* 459:    */   {
/* 460:410 */     CTBorderPr pr = this._border.isSetVertical() ? this._border.getVertical() : this._border.addNewVertical();
/* 461:411 */     if (color == null) {
/* 462:412 */       pr.unsetColor();
/* 463:    */     } else {
/* 464:414 */       pr.setColor(color);
/* 465:    */     }
/* 466:    */   }
/* 467:    */   
/* 468:    */   private BorderStyle getBorderStyle(CTBorderPr borderPr)
/* 469:    */   {
/* 470:423 */     if (borderPr == null) {
/* 471:423 */       return BorderStyle.NONE;
/* 472:    */     }
/* 473:424 */     STBorderStyle.Enum ptrn = borderPr.getStyle();
/* 474:425 */     return ptrn == null ? BorderStyle.NONE : BorderStyle.valueOf((short)(ptrn.intValue() - 1));
/* 475:    */   }
/* 476:    */   
/* 477:    */   private short getIndexedColor(XSSFColor color)
/* 478:    */   {
/* 479:429 */     return color == null ? 0 : color.getIndexed();
/* 480:    */   }
/* 481:    */   
/* 482:    */   private XSSFColor getColor(CTBorderPr pr)
/* 483:    */   {
/* 484:433 */     return pr == null ? null : new XSSFColor(pr.getColor(), this._colorMap);
/* 485:    */   }
/* 486:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFBorderFormatting
 * JD-Core Version:    0.7.0.1
 */