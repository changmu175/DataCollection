/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import org.apache.poi.sl.draw.DrawPaint;
/*   5:    */ import org.apache.poi.sl.usermodel.ColorStyle;
/*   6:    */ import org.apache.poi.sl.usermodel.PresetColor;
/*   7:    */ import org.apache.poi.util.Internal;
/*   8:    */ import org.apache.poi.util.POILogFactory;
/*   9:    */ import org.apache.poi.util.POILogger;
/*  10:    */ import org.apache.xmlbeans.XmlObject;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTColor;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STPresetColorVal.Enum;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STSchemeColorVal.Enum;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STSystemColorVal.Enum;
/*  24:    */ import org.w3c.dom.NamedNodeMap;
/*  25:    */ import org.w3c.dom.Node;
/*  26:    */ 
/*  27:    */ @Internal
/*  28:    */ public class XSLFColor
/*  29:    */ {
/*  30: 49 */   private static final POILogger LOGGER = POILogFactory.getLogger(XSLFColor.class);
/*  31:    */   private XmlObject _xmlObject;
/*  32:    */   private Color _color;
/*  33:    */   private CTSchemeColor _phClr;
/*  34:    */   
/*  35:    */   public XSLFColor(XmlObject obj, XSLFTheme theme, CTSchemeColor phClr)
/*  36:    */   {
/*  37: 56 */     this._xmlObject = obj;
/*  38: 57 */     this._phClr = phClr;
/*  39: 58 */     this._color = toColor(obj, theme);
/*  40:    */   }
/*  41:    */   
/*  42:    */   @Internal
/*  43:    */   public XmlObject getXmlObject()
/*  44:    */   {
/*  45: 63 */     return this._xmlObject;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public Color getColor()
/*  49:    */   {
/*  50: 72 */     return DrawPaint.applyColorTransform(getColorStyle());
/*  51:    */   }
/*  52:    */   
/*  53:    */   public ColorStyle getColorStyle()
/*  54:    */   {
/*  55: 76 */     new ColorStyle()
/*  56:    */     {
/*  57:    */       public Color getColor()
/*  58:    */       {
/*  59: 79 */         return XSLFColor.this._color;
/*  60:    */       }
/*  61:    */       
/*  62:    */       public int getAlpha()
/*  63:    */       {
/*  64: 84 */         return XSLFColor.this.getRawValue("alpha");
/*  65:    */       }
/*  66:    */       
/*  67:    */       public int getHueOff()
/*  68:    */       {
/*  69: 89 */         return XSLFColor.this.getRawValue("hueOff");
/*  70:    */       }
/*  71:    */       
/*  72:    */       public int getHueMod()
/*  73:    */       {
/*  74: 94 */         return XSLFColor.this.getRawValue("hueMod");
/*  75:    */       }
/*  76:    */       
/*  77:    */       public int getSatOff()
/*  78:    */       {
/*  79: 99 */         return XSLFColor.this.getRawValue("satOff");
/*  80:    */       }
/*  81:    */       
/*  82:    */       public int getSatMod()
/*  83:    */       {
/*  84:104 */         return XSLFColor.this.getRawValue("satMod");
/*  85:    */       }
/*  86:    */       
/*  87:    */       public int getLumOff()
/*  88:    */       {
/*  89:109 */         return XSLFColor.this.getRawValue("lumOff");
/*  90:    */       }
/*  91:    */       
/*  92:    */       public int getLumMod()
/*  93:    */       {
/*  94:114 */         return XSLFColor.this.getRawValue("lumMod");
/*  95:    */       }
/*  96:    */       
/*  97:    */       public int getShade()
/*  98:    */       {
/*  99:119 */         return XSLFColor.this.getRawValue("shade");
/* 100:    */       }
/* 101:    */       
/* 102:    */       public int getTint()
/* 103:    */       {
/* 104:124 */         return XSLFColor.this.getRawValue("tint");
/* 105:    */       }
/* 106:    */     };
/* 107:    */   }
/* 108:    */   
/* 109:    */   Color toColor(XmlObject obj, XSLFTheme theme)
/* 110:    */   {
/* 111:130 */     Color color = null;
/* 112:131 */     for (XmlObject ch : obj.selectPath("*")) {
/* 113:132 */       if ((ch instanceof CTHslColor))
/* 114:    */       {
/* 115:133 */         CTHslColor hsl = (CTHslColor)ch;
/* 116:134 */         int h = hsl.getHue2();
/* 117:135 */         int s = hsl.getSat2();
/* 118:136 */         int l = hsl.getLum2();
/* 119:137 */         color = DrawPaint.HSL2RGB(h / 60000.0D, s / 1000.0D, l / 1000.0D, 1.0D);
/* 120:    */       }
/* 121:138 */       else if ((ch instanceof CTPresetColor))
/* 122:    */       {
/* 123:139 */         CTPresetColor prst = (CTPresetColor)ch;
/* 124:140 */         String colorName = prst.getVal().toString();
/* 125:141 */         PresetColor pc = PresetColor.valueOfOoxmlId(colorName);
/* 126:142 */         if (pc != null) {
/* 127:143 */           color = pc.color;
/* 128:    */         }
/* 129:    */       }
/* 130:145 */       else if ((ch instanceof CTSchemeColor))
/* 131:    */       {
/* 132:146 */         CTSchemeColor schemeColor = (CTSchemeColor)ch;
/* 133:147 */         String colorRef = schemeColor.getVal().toString();
/* 134:148 */         if (this._phClr != null) {
/* 135:150 */           colorRef = this._phClr.getVal().toString();
/* 136:    */         }
/* 137:153 */         CTColor ctColor = theme.getCTColor(colorRef);
/* 138:154 */         if (ctColor != null) {
/* 139:155 */           color = toColor(ctColor, null);
/* 140:    */         }
/* 141:    */       }
/* 142:157 */       else if ((ch instanceof CTScRgbColor))
/* 143:    */       {
/* 144:159 */         CTScRgbColor scrgb = (CTScRgbColor)ch;
/* 145:160 */         color = new Color(DrawPaint.lin2srgb(scrgb.getR()), DrawPaint.lin2srgb(scrgb.getG()), DrawPaint.lin2srgb(scrgb.getB()));
/* 146:    */       }
/* 147:161 */       else if ((ch instanceof CTSRgbColor))
/* 148:    */       {
/* 149:163 */         CTSRgbColor srgb = (CTSRgbColor)ch;
/* 150:164 */         byte[] val = srgb.getVal();
/* 151:165 */         color = new Color(0xFF & val[0], 0xFF & val[1], 0xFF & val[2]);
/* 152:    */       }
/* 153:166 */       else if ((ch instanceof CTSystemColor))
/* 154:    */       {
/* 155:167 */         CTSystemColor sys = (CTSystemColor)ch;
/* 156:168 */         if (sys.isSetLastClr())
/* 157:    */         {
/* 158:169 */           byte[] val = sys.getLastClr();
/* 159:170 */           color = new Color(0xFF & val[0], 0xFF & val[1], 0xFF & val[2]);
/* 160:    */         }
/* 161:    */         else
/* 162:    */         {
/* 163:172 */           String colorName = sys.getVal().toString();
/* 164:173 */           PresetColor pc = PresetColor.valueOfOoxmlId(colorName);
/* 165:174 */           if (pc != null) {
/* 166:175 */             color = pc.color;
/* 167:    */           }
/* 168:177 */           if (color == null) {
/* 169:178 */             color = Color.black;
/* 170:    */           }
/* 171:    */         }
/* 172:    */       }
/* 173:181 */       else if (!(ch instanceof CTFontReference))
/* 174:    */       {
/* 175:185 */         throw new IllegalArgumentException("Unexpected color choice: " + ch.getClass());
/* 176:    */       }
/* 177:    */     }
/* 178:188 */     return color;
/* 179:    */   }
/* 180:    */   
/* 181:    */   @Internal
/* 182:    */   protected void setColor(Color color)
/* 183:    */   {
/* 184:198 */     if (!(this._xmlObject instanceof CTSolidColorFillProperties))
/* 185:    */     {
/* 186:199 */       LOGGER.log(7, new Object[] { "XSLFColor.setColor currently only supports CTSolidColorFillProperties" });
/* 187:200 */       return;
/* 188:    */     }
/* 189:202 */     CTSolidColorFillProperties fill = (CTSolidColorFillProperties)this._xmlObject;
/* 190:203 */     if (fill.isSetSrgbClr()) {
/* 191:204 */       fill.unsetSrgbClr();
/* 192:    */     }
/* 193:207 */     if (fill.isSetScrgbClr()) {
/* 194:208 */       fill.unsetScrgbClr();
/* 195:    */     }
/* 196:211 */     if (fill.isSetHslClr()) {
/* 197:212 */       fill.unsetHslClr();
/* 198:    */     }
/* 199:215 */     if (fill.isSetPrstClr()) {
/* 200:216 */       fill.unsetPrstClr();
/* 201:    */     }
/* 202:219 */     if (fill.isSetSchemeClr()) {
/* 203:220 */       fill.unsetSchemeClr();
/* 204:    */     }
/* 205:223 */     if (fill.isSetSysClr()) {
/* 206:224 */       fill.unsetSysClr();
/* 207:    */     }
/* 208:227 */     float[] rgbaf = color.getRGBComponents(null);
/* 209:228 */     boolean addAlpha = (rgbaf.length == 4) && (rgbaf[3] < 1.0F);
/* 210:    */     CTPositiveFixedPercentage alphaPct;
/* 211:    */     CTPositiveFixedPercentage alphaPct;
/* 212:232 */     if ((isInt(rgbaf[0])) && (isInt(rgbaf[1])) && (isInt(rgbaf[2])))
/* 213:    */     {
/* 214:234 */       CTSRgbColor rgb = fill.addNewSrgbClr();
/* 215:    */       
/* 216:236 */       byte[] rgbBytes = { (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() };
/* 217:237 */       rgb.setVal(rgbBytes);
/* 218:238 */       alphaPct = addAlpha ? rgb.addNewAlpha() : null;
/* 219:    */     }
/* 220:    */     else
/* 221:    */     {
/* 222:240 */       CTScRgbColor rgb = fill.addNewScrgbClr();
/* 223:241 */       rgb.setR(DrawPaint.srgb2lin(rgbaf[0]));
/* 224:242 */       rgb.setG(DrawPaint.srgb2lin(rgbaf[1]));
/* 225:243 */       rgb.setB(DrawPaint.srgb2lin(rgbaf[2]));
/* 226:244 */       alphaPct = addAlpha ? rgb.addNewAlpha() : null;
/* 227:    */     }
/* 228:248 */     if (alphaPct != null) {
/* 229:249 */       alphaPct.setVal((int)(100000.0F * rgbaf[3]));
/* 230:    */     }
/* 231:    */   }
/* 232:    */   
/* 233:    */   private static boolean isInt(float f)
/* 234:    */   {
/* 235:257 */     return Math.abs(f * 255.0F - Math.rint(f * 255.0F)) < 9.999999747378752E-006D;
/* 236:    */   }
/* 237:    */   
/* 238:    */   private int getRawValue(String elem)
/* 239:    */   {
/* 240:261 */     String query = "declare namespace a='http://schemas.openxmlformats.org/drawingml/2006/main' $this//a:" + elem;
/* 241:266 */     if (this._phClr != null)
/* 242:    */     {
/* 243:267 */       XmlObject[] obj = this._phClr.selectPath(query);
/* 244:268 */       if (obj.length == 1)
/* 245:    */       {
/* 246:269 */         Node attr = obj[0].getDomNode().getAttributes().getNamedItem("val");
/* 247:270 */         if (attr != null) {
/* 248:271 */           return Integer.parseInt(attr.getNodeValue());
/* 249:    */         }
/* 250:    */       }
/* 251:    */     }
/* 252:276 */     XmlObject[] obj = this._xmlObject.selectPath(query);
/* 253:277 */     if (obj.length == 1)
/* 254:    */     {
/* 255:278 */       Node attr = obj[0].getDomNode().getAttributes().getNamedItem("val");
/* 256:279 */       if (attr != null) {
/* 257:280 */         return Integer.parseInt(attr.getNodeValue());
/* 258:    */       }
/* 259:    */     }
/* 260:284 */     return -1;
/* 261:    */   }
/* 262:    */   
/* 263:    */   private int getPercentageValue(String elem)
/* 264:    */   {
/* 265:297 */     int val = getRawValue(elem);
/* 266:298 */     return val == -1 ? val : val / 1000;
/* 267:    */   }
/* 268:    */   
/* 269:    */   private int getAngleValue(String elem)
/* 270:    */   {
/* 271:302 */     int val = getRawValue(elem);
/* 272:303 */     return val == -1 ? val : val / 60000;
/* 273:    */   }
/* 274:    */   
/* 275:    */   int getAlpha()
/* 276:    */   {
/* 277:313 */     return getPercentageValue("alpha");
/* 278:    */   }
/* 279:    */   
/* 280:    */   int getAlphaMod()
/* 281:    */   {
/* 282:323 */     return getPercentageValue("alphaMod");
/* 283:    */   }
/* 284:    */   
/* 285:    */   int getAlphaOff()
/* 286:    */   {
/* 287:335 */     return getPercentageValue("alphaOff");
/* 288:    */   }
/* 289:    */   
/* 290:    */   int getHue()
/* 291:    */   {
/* 292:340 */     return getAngleValue("hue");
/* 293:    */   }
/* 294:    */   
/* 295:    */   int getHueMod()
/* 296:    */   {
/* 297:344 */     return getPercentageValue("hueMod");
/* 298:    */   }
/* 299:    */   
/* 300:    */   int getHueOff()
/* 301:    */   {
/* 302:348 */     return getPercentageValue("hueOff");
/* 303:    */   }
/* 304:    */   
/* 305:    */   int getLum()
/* 306:    */   {
/* 307:359 */     return getPercentageValue("lum");
/* 308:    */   }
/* 309:    */   
/* 310:    */   int getLumMod()
/* 311:    */   {
/* 312:369 */     return getPercentageValue("lumMod");
/* 313:    */   }
/* 314:    */   
/* 315:    */   int getLumOff()
/* 316:    */   {
/* 317:379 */     return getPercentageValue("lumOff");
/* 318:    */   }
/* 319:    */   
/* 320:    */   int getSat()
/* 321:    */   {
/* 322:390 */     return getPercentageValue("sat");
/* 323:    */   }
/* 324:    */   
/* 325:    */   int getSatMod()
/* 326:    */   {
/* 327:400 */     return getPercentageValue("satMod");
/* 328:    */   }
/* 329:    */   
/* 330:    */   int getSatOff()
/* 331:    */   {
/* 332:410 */     return getPercentageValue("satOff");
/* 333:    */   }
/* 334:    */   
/* 335:    */   int getRed()
/* 336:    */   {
/* 337:422 */     return getPercentageValue("red");
/* 338:    */   }
/* 339:    */   
/* 340:    */   int getRedMod()
/* 341:    */   {
/* 342:426 */     return getPercentageValue("redMod");
/* 343:    */   }
/* 344:    */   
/* 345:    */   int getRedOff()
/* 346:    */   {
/* 347:430 */     return getPercentageValue("redOff");
/* 348:    */   }
/* 349:    */   
/* 350:    */   int getGreen()
/* 351:    */   {
/* 352:442 */     return getPercentageValue("green");
/* 353:    */   }
/* 354:    */   
/* 355:    */   int getGreenMod()
/* 356:    */   {
/* 357:446 */     return getPercentageValue("greenMod");
/* 358:    */   }
/* 359:    */   
/* 360:    */   int getGreenOff()
/* 361:    */   {
/* 362:450 */     return getPercentageValue("greenOff");
/* 363:    */   }
/* 364:    */   
/* 365:    */   int getBlue()
/* 366:    */   {
/* 367:462 */     return getPercentageValue("blue");
/* 368:    */   }
/* 369:    */   
/* 370:    */   int getBlueMod()
/* 371:    */   {
/* 372:466 */     return getPercentageValue("blueMod");
/* 373:    */   }
/* 374:    */   
/* 375:    */   int getBlueOff()
/* 376:    */   {
/* 377:470 */     return getPercentageValue("blueOff");
/* 378:    */   }
/* 379:    */   
/* 380:    */   public int getShade()
/* 381:    */   {
/* 382:482 */     return getPercentageValue("shade");
/* 383:    */   }
/* 384:    */   
/* 385:    */   public int getTint()
/* 386:    */   {
/* 387:494 */     return getPercentageValue("tint");
/* 388:    */   }
/* 389:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFColor
 * JD-Core Version:    0.7.0.1
 */