/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.Color;
/*  4:   */ import org.apache.poi.ss.usermodel.ColorScaleFormatting;
/*  5:   */ import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold;
/*  6:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo;
/*  7:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
/*  8:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColorScale;
/*  9:   */ 
/* 10:   */ public class XSSFColorScaleFormatting
/* 11:   */   implements ColorScaleFormatting
/* 12:   */ {
/* 13:   */   private CTColorScale _scale;
/* 14:   */   private IndexedColorMap _indexedColorMap;
/* 15:   */   
/* 16:   */   XSSFColorScaleFormatting(CTColorScale scale, IndexedColorMap colorMap)
/* 17:   */   {
/* 18:37 */     this._scale = scale;
/* 19:38 */     this._indexedColorMap = colorMap;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public int getNumControlPoints()
/* 23:   */   {
/* 24:42 */     return this._scale.sizeOfCfvoArray();
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void setNumControlPoints(int num)
/* 28:   */   {
/* 29:45 */     while (num < this._scale.sizeOfCfvoArray())
/* 30:   */     {
/* 31:46 */       this._scale.removeCfvo(this._scale.sizeOfCfvoArray() - 1);
/* 32:47 */       this._scale.removeColor(this._scale.sizeOfColorArray() - 1);
/* 33:   */     }
/* 34:49 */     while (num > this._scale.sizeOfCfvoArray())
/* 35:   */     {
/* 36:50 */       this._scale.addNewCfvo();
/* 37:51 */       this._scale.addNewColor();
/* 38:   */     }
/* 39:   */   }
/* 40:   */   
/* 41:   */   public XSSFColor[] getColors()
/* 42:   */   {
/* 43:56 */     CTColor[] ctcols = this._scale.getColorArray();
/* 44:57 */     XSSFColor[] c = new XSSFColor[ctcols.length];
/* 45:58 */     for (int i = 0; i < ctcols.length; i++) {
/* 46:59 */       c[i] = new XSSFColor(ctcols[i], this._indexedColorMap);
/* 47:   */     }
/* 48:61 */     return c;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public void setColors(Color[] colors)
/* 52:   */   {
/* 53:64 */     CTColor[] ctcols = new CTColor[colors.length];
/* 54:65 */     for (int i = 0; i < colors.length; i++) {
/* 55:66 */       ctcols[i] = ((XSSFColor)colors[i]).getCTColor();
/* 56:   */     }
/* 57:68 */     this._scale.setColorArray(ctcols);
/* 58:   */   }
/* 59:   */   
/* 60:   */   public XSSFConditionalFormattingThreshold[] getThresholds()
/* 61:   */   {
/* 62:72 */     CTCfvo[] cfvos = this._scale.getCfvoArray();
/* 63:73 */     XSSFConditionalFormattingThreshold[] t = new XSSFConditionalFormattingThreshold[cfvos.length];
/* 64:75 */     for (int i = 0; i < cfvos.length; i++) {
/* 65:76 */       t[i] = new XSSFConditionalFormattingThreshold(cfvos[i]);
/* 66:   */     }
/* 67:78 */     return t;
/* 68:   */   }
/* 69:   */   
/* 70:   */   public void setThresholds(ConditionalFormattingThreshold[] thresholds)
/* 71:   */   {
/* 72:81 */     CTCfvo[] cfvos = new CTCfvo[thresholds.length];
/* 73:82 */     for (int i = 0; i < thresholds.length; i++) {
/* 74:83 */       cfvos[i] = ((XSSFConditionalFormattingThreshold)thresholds[i]).getCTCfvo();
/* 75:   */     }
/* 76:85 */     this._scale.setCfvoArray(cfvos);
/* 77:   */   }
/* 78:   */   
/* 79:   */   public XSSFColor createColor()
/* 80:   */   {
/* 81:92 */     return new XSSFColor(this._scale.addNewColor(), this._indexedColorMap);
/* 82:   */   }
/* 83:   */   
/* 84:   */   public XSSFConditionalFormattingThreshold createThreshold()
/* 85:   */   {
/* 86:95 */     return new XSSFConditionalFormattingThreshold(this._scale.addNewCfvo());
/* 87:   */   }
/* 88:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFColorScaleFormatting
 * JD-Core Version:    0.7.0.1
 */