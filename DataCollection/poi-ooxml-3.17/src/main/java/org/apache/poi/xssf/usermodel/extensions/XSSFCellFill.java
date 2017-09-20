/*   1:    */ package org.apache.poi.xssf.usermodel.extensions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.Internal;
/*   4:    */ import org.apache.poi.xssf.usermodel.IndexedColorMap;
/*   5:    */ import org.apache.poi.xssf.usermodel.XSSFColor;
/*   6:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
/*   7:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill;
/*   8:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill.Factory;
/*   9:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill;
/*  10:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPatternType.Enum;
/*  11:    */ 
/*  12:    */ public final class XSSFCellFill
/*  13:    */ {
/*  14:    */   private IndexedColorMap _indexedColorMap;
/*  15:    */   private CTFill _fill;
/*  16:    */   
/*  17:    */   public XSSFCellFill(CTFill fill, IndexedColorMap colorMap)
/*  18:    */   {
/*  19: 42 */     this._fill = fill;
/*  20: 43 */     this._indexedColorMap = colorMap;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public XSSFCellFill()
/*  24:    */   {
/*  25: 50 */     this._fill = CTFill.Factory.newInstance();
/*  26:    */   }
/*  27:    */   
/*  28:    */   public XSSFColor getFillBackgroundColor()
/*  29:    */   {
/*  30: 59 */     CTPatternFill ptrn = this._fill.getPatternFill();
/*  31: 60 */     if (ptrn == null) {
/*  32: 60 */       return null;
/*  33:    */     }
/*  34: 62 */     CTColor ctColor = ptrn.getBgColor();
/*  35: 63 */     return ctColor == null ? null : new XSSFColor(ctColor, this._indexedColorMap);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setFillBackgroundColor(int index)
/*  39:    */   {
/*  40: 72 */     CTPatternFill ptrn = ensureCTPatternFill();
/*  41: 73 */     CTColor ctColor = ptrn.isSetBgColor() ? ptrn.getBgColor() : ptrn.addNewBgColor();
/*  42: 74 */     ctColor.setIndexed(index);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setFillBackgroundColor(XSSFColor color)
/*  46:    */   {
/*  47: 83 */     CTPatternFill ptrn = ensureCTPatternFill();
/*  48: 84 */     ptrn.setBgColor(color.getCTColor());
/*  49:    */   }
/*  50:    */   
/*  51:    */   public XSSFColor getFillForegroundColor()
/*  52:    */   {
/*  53: 93 */     CTPatternFill ptrn = this._fill.getPatternFill();
/*  54: 94 */     if (ptrn == null) {
/*  55: 94 */       return null;
/*  56:    */     }
/*  57: 96 */     CTColor ctColor = ptrn.getFgColor();
/*  58: 97 */     return ctColor == null ? null : new XSSFColor(ctColor, this._indexedColorMap);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setFillForegroundColor(int index)
/*  62:    */   {
/*  63:106 */     CTPatternFill ptrn = ensureCTPatternFill();
/*  64:107 */     CTColor ctColor = ptrn.isSetFgColor() ? ptrn.getFgColor() : ptrn.addNewFgColor();
/*  65:108 */     ctColor.setIndexed(index);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setFillForegroundColor(XSSFColor color)
/*  69:    */   {
/*  70:117 */     CTPatternFill ptrn = ensureCTPatternFill();
/*  71:118 */     ptrn.setFgColor(color.getCTColor());
/*  72:    */   }
/*  73:    */   
/*  74:    */   public STPatternType.Enum getPatternType()
/*  75:    */   {
/*  76:127 */     CTPatternFill ptrn = this._fill.getPatternFill();
/*  77:128 */     return ptrn == null ? null : ptrn.getPatternType();
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setPatternType(STPatternType.Enum patternType)
/*  81:    */   {
/*  82:137 */     CTPatternFill ptrn = ensureCTPatternFill();
/*  83:138 */     ptrn.setPatternType(patternType);
/*  84:    */   }
/*  85:    */   
/*  86:    */   private CTPatternFill ensureCTPatternFill()
/*  87:    */   {
/*  88:142 */     CTPatternFill patternFill = this._fill.getPatternFill();
/*  89:143 */     if (patternFill == null) {
/*  90:144 */       patternFill = this._fill.addNewPatternFill();
/*  91:    */     }
/*  92:146 */     return patternFill;
/*  93:    */   }
/*  94:    */   
/*  95:    */   @Internal
/*  96:    */   public CTFill getCTFill()
/*  97:    */   {
/*  98:156 */     return this._fill;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public int hashCode()
/* 102:    */   {
/* 103:161 */     return this._fill.toString().hashCode();
/* 104:    */   }
/* 105:    */   
/* 106:    */   public boolean equals(Object o)
/* 107:    */   {
/* 108:165 */     if (!(o instanceof XSSFCellFill)) {
/* 109:165 */       return false;
/* 110:    */     }
/* 111:167 */     XSSFCellFill cf = (XSSFCellFill)o;
/* 112:168 */     return this._fill.toString().equals(cf.getCTFill().toString());
/* 113:    */   }
/* 114:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.extensions.XSSFCellFill
 * JD-Core Version:    0.7.0.1
 */