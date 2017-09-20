/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
/*   5:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
/*   6:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
/*   7:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
/*   8:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties;
/*   9:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
/*  10:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextStrikeType;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextUnderlineType;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape;
/*  15:    */ 
/*  16:    */ public class XSSFTextRun
/*  17:    */ {
/*  18:    */   private final CTRegularTextRun _r;
/*  19:    */   private final XSSFTextParagraph _p;
/*  20:    */   
/*  21:    */   XSSFTextRun(CTRegularTextRun r, XSSFTextParagraph p)
/*  22:    */   {
/*  23: 39 */     this._r = r;
/*  24: 40 */     this._p = p;
/*  25:    */   }
/*  26:    */   
/*  27:    */   XSSFTextParagraph getParentParagraph()
/*  28:    */   {
/*  29: 44 */     return this._p;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String getText()
/*  33:    */   {
/*  34: 48 */     return this._r.getT();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setText(String text)
/*  38:    */   {
/*  39: 52 */     this._r.setT(text);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public CTRegularTextRun getXmlObject()
/*  43:    */   {
/*  44: 56 */     return this._r;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setFontColor(Color color)
/*  48:    */   {
/*  49: 60 */     CTTextCharacterProperties rPr = getRPr();
/*  50: 61 */     CTSolidColorFillProperties fill = rPr.isSetSolidFill() ? rPr.getSolidFill() : rPr.addNewSolidFill();
/*  51: 62 */     CTSRgbColor clr = fill.isSetSrgbClr() ? fill.getSrgbClr() : fill.addNewSrgbClr();
/*  52: 63 */     clr.setVal(new byte[] { (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() });
/*  53: 65 */     if (fill.isSetHslClr()) {
/*  54: 65 */       fill.unsetHslClr();
/*  55:    */     }
/*  56: 66 */     if (fill.isSetPrstClr()) {
/*  57: 66 */       fill.unsetPrstClr();
/*  58:    */     }
/*  59: 67 */     if (fill.isSetSchemeClr()) {
/*  60: 67 */       fill.unsetSchemeClr();
/*  61:    */     }
/*  62: 68 */     if (fill.isSetScrgbClr()) {
/*  63: 68 */       fill.unsetScrgbClr();
/*  64:    */     }
/*  65: 69 */     if (fill.isSetSysClr()) {
/*  66: 69 */       fill.unsetSysClr();
/*  67:    */     }
/*  68:    */   }
/*  69:    */   
/*  70:    */   public Color getFontColor()
/*  71:    */   {
/*  72: 75 */     CTTextCharacterProperties rPr = getRPr();
/*  73: 76 */     if (rPr.isSetSolidFill())
/*  74:    */     {
/*  75: 77 */       CTSolidColorFillProperties fill = rPr.getSolidFill();
/*  76: 79 */       if (fill.isSetSrgbClr())
/*  77:    */       {
/*  78: 80 */         CTSRgbColor clr = fill.getSrgbClr();
/*  79: 81 */         byte[] rgb = clr.getVal();
/*  80: 82 */         return new Color(0xFF & rgb[0], 0xFF & rgb[1], 0xFF & rgb[2]);
/*  81:    */       }
/*  82:    */     }
/*  83: 86 */     return new Color(0, 0, 0);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setFontSize(double fontSize)
/*  87:    */   {
/*  88: 95 */     CTTextCharacterProperties rPr = getRPr();
/*  89: 96 */     if (fontSize == -1.0D)
/*  90:    */     {
/*  91: 97 */       if (rPr.isSetSz()) {
/*  92: 97 */         rPr.unsetSz();
/*  93:    */       }
/*  94:    */     }
/*  95:    */     else
/*  96:    */     {
/*  97: 99 */       if (fontSize < 1.0D) {
/*  98:100 */         throw new IllegalArgumentException("Minimum font size is 1pt but was " + fontSize);
/*  99:    */       }
/* 100:103 */       rPr.setSz((int)(100.0D * fontSize));
/* 101:    */     }
/* 102:    */   }
/* 103:    */   
/* 104:    */   public double getFontSize()
/* 105:    */   {
/* 106:111 */     double scale = 1.0D;
/* 107:112 */     double size = 11.0D;
/* 108:113 */     CTTextNormalAutofit afit = getParentParagraph().getParentShape().getTxBody().getBodyPr().getNormAutofit();
/* 109:114 */     if (afit != null) {
/* 110:114 */       scale = afit.getFontScale() / 100000.0D;
/* 111:    */     }
/* 112:116 */     CTTextCharacterProperties rPr = getRPr();
/* 113:117 */     if (rPr.isSetSz()) {
/* 114:118 */       size = rPr.getSz() * 0.01D;
/* 115:    */     }
/* 116:121 */     return size * scale;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public double getCharacterSpacing()
/* 120:    */   {
/* 121:130 */     CTTextCharacterProperties rPr = getRPr();
/* 122:131 */     if (rPr.isSetSpc()) {
/* 123:132 */       return rPr.getSpc() * 0.01D;
/* 124:    */     }
/* 125:134 */     return 0.0D;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void setCharacterSpacing(double spc)
/* 129:    */   {
/* 130:147 */     CTTextCharacterProperties rPr = getRPr();
/* 131:148 */     if (spc == 0.0D)
/* 132:    */     {
/* 133:149 */       if (rPr.isSetSpc()) {
/* 134:149 */         rPr.unsetSpc();
/* 135:    */       }
/* 136:    */     }
/* 137:    */     else {
/* 138:151 */       rPr.setSpc((int)(100.0D * spc));
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void setFont(String typeface)
/* 143:    */   {
/* 144:162 */     setFontFamily(typeface, (byte)-1, (byte)-1, false);
/* 145:    */   }
/* 146:    */   
/* 147:    */   public void setFontFamily(String typeface, byte charset, byte pictAndFamily, boolean isSymbol)
/* 148:    */   {
/* 149:166 */     CTTextCharacterProperties rPr = getRPr();
/* 150:168 */     if (typeface == null)
/* 151:    */     {
/* 152:169 */       if (rPr.isSetLatin()) {
/* 153:169 */         rPr.unsetLatin();
/* 154:    */       }
/* 155:170 */       if (rPr.isSetCs()) {
/* 156:170 */         rPr.unsetCs();
/* 157:    */       }
/* 158:171 */       if (rPr.isSetSym()) {
/* 159:171 */         rPr.unsetSym();
/* 160:    */       }
/* 161:    */     }
/* 162:173 */     else if (isSymbol)
/* 163:    */     {
/* 164:174 */       CTTextFont font = rPr.isSetSym() ? rPr.getSym() : rPr.addNewSym();
/* 165:175 */       font.setTypeface(typeface);
/* 166:    */     }
/* 167:    */     else
/* 168:    */     {
/* 169:177 */       CTTextFont latin = rPr.isSetLatin() ? rPr.getLatin() : rPr.addNewLatin();
/* 170:178 */       latin.setTypeface(typeface);
/* 171:179 */       if (charset != -1) {
/* 172:179 */         latin.setCharset(charset);
/* 173:    */       }
/* 174:180 */       if (pictAndFamily != -1) {
/* 175:180 */         latin.setPitchFamily(pictAndFamily);
/* 176:    */       }
/* 177:    */     }
/* 178:    */   }
/* 179:    */   
/* 180:    */   public String getFontFamily()
/* 181:    */   {
/* 182:189 */     CTTextCharacterProperties rPr = getRPr();
/* 183:190 */     CTTextFont font = rPr.getLatin();
/* 184:191 */     if (font != null) {
/* 185:192 */       return font.getTypeface();
/* 186:    */     }
/* 187:194 */     return "Calibri";
/* 188:    */   }
/* 189:    */   
/* 190:    */   public byte getPitchAndFamily()
/* 191:    */   {
/* 192:198 */     CTTextCharacterProperties rPr = getRPr();
/* 193:199 */     CTTextFont font = rPr.getLatin();
/* 194:200 */     if (font != null) {
/* 195:201 */       return font.getPitchFamily();
/* 196:    */     }
/* 197:203 */     return 0;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public void setStrikethrough(boolean strike)
/* 201:    */   {
/* 202:212 */     getRPr().setStrike(strike ? STTextStrikeType.SNG_STRIKE : STTextStrikeType.NO_STRIKE);
/* 203:    */   }
/* 204:    */   
/* 205:    */   public boolean isStrikethrough()
/* 206:    */   {
/* 207:219 */     CTTextCharacterProperties rPr = getRPr();
/* 208:220 */     if (rPr.isSetStrike()) {
/* 209:221 */       return rPr.getStrike() != STTextStrikeType.NO_STRIKE;
/* 210:    */     }
/* 211:223 */     return false;
/* 212:    */   }
/* 213:    */   
/* 214:    */   public boolean isSuperscript()
/* 215:    */   {
/* 216:230 */     CTTextCharacterProperties rPr = getRPr();
/* 217:231 */     if (rPr.isSetBaseline()) {
/* 218:232 */       return rPr.getBaseline() > 0;
/* 219:    */     }
/* 220:234 */     return false;
/* 221:    */   }
/* 222:    */   
/* 223:    */   public void setBaselineOffset(double baselineOffset)
/* 224:    */   {
/* 225:247 */     getRPr().setBaseline((int)baselineOffset * 1000);
/* 226:    */   }
/* 227:    */   
/* 228:    */   public void setSuperscript(boolean flag)
/* 229:    */   {
/* 230:257 */     setBaselineOffset(flag ? 30.0D : 0.0D);
/* 231:    */   }
/* 232:    */   
/* 233:    */   public void setSubscript(boolean flag)
/* 234:    */   {
/* 235:267 */     setBaselineOffset(flag ? -25.0D : 0.0D);
/* 236:    */   }
/* 237:    */   
/* 238:    */   public boolean isSubscript()
/* 239:    */   {
/* 240:274 */     CTTextCharacterProperties rPr = getRPr();
/* 241:275 */     if (rPr.isSetBaseline()) {
/* 242:276 */       return rPr.getBaseline() < 0;
/* 243:    */     }
/* 244:278 */     return false;
/* 245:    */   }
/* 246:    */   
/* 247:    */   public TextCap getTextCap()
/* 248:    */   {
/* 249:285 */     CTTextCharacterProperties rPr = getRPr();
/* 250:286 */     if (rPr.isSetCap()) {
/* 251:287 */       return TextCap.values()[(rPr.getCap().intValue() - 1)];
/* 252:    */     }
/* 253:289 */     return TextCap.NONE;
/* 254:    */   }
/* 255:    */   
/* 256:    */   public void setBold(boolean bold)
/* 257:    */   {
/* 258:298 */     getRPr().setB(bold);
/* 259:    */   }
/* 260:    */   
/* 261:    */   public boolean isBold()
/* 262:    */   {
/* 263:305 */     CTTextCharacterProperties rPr = getRPr();
/* 264:306 */     if (rPr.isSetB()) {
/* 265:307 */       return rPr.getB();
/* 266:    */     }
/* 267:309 */     return false;
/* 268:    */   }
/* 269:    */   
/* 270:    */   public void setItalic(boolean italic)
/* 271:    */   {
/* 272:316 */     getRPr().setI(italic);
/* 273:    */   }
/* 274:    */   
/* 275:    */   public boolean isItalic()
/* 276:    */   {
/* 277:323 */     CTTextCharacterProperties rPr = getRPr();
/* 278:324 */     if (rPr.isSetI()) {
/* 279:325 */       return rPr.getI();
/* 280:    */     }
/* 281:327 */     return false;
/* 282:    */   }
/* 283:    */   
/* 284:    */   public void setUnderline(boolean underline)
/* 285:    */   {
/* 286:334 */     getRPr().setU(underline ? STTextUnderlineType.SNG : STTextUnderlineType.NONE);
/* 287:    */   }
/* 288:    */   
/* 289:    */   public boolean isUnderline()
/* 290:    */   {
/* 291:341 */     CTTextCharacterProperties rPr = getRPr();
/* 292:342 */     if (rPr.isSetU()) {
/* 293:343 */       return rPr.getU() != STTextUnderlineType.NONE;
/* 294:    */     }
/* 295:345 */     return false;
/* 296:    */   }
/* 297:    */   
/* 298:    */   protected CTTextCharacterProperties getRPr()
/* 299:    */   {
/* 300:349 */     return this._r.isSetRPr() ? this._r.getRPr() : this._r.addNewRPr();
/* 301:    */   }
/* 302:    */   
/* 303:    */   public String toString()
/* 304:    */   {
/* 305:354 */     return "[" + getClass() + "]" + getText();
/* 306:    */   }
/* 307:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFTextRun
 * JD-Core Version:    0.7.0.1
 */