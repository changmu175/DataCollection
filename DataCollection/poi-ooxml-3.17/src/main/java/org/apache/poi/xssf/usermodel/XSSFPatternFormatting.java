/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.Color;
/*   4:    */ import org.apache.poi.ss.usermodel.PatternFormatting;
/*   5:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
/*   6:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor.Factory;
/*   7:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill;
/*   8:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill;
/*   9:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPatternType.Enum;
/*  10:    */ 
/*  11:    */ public class XSSFPatternFormatting
/*  12:    */   implements PatternFormatting
/*  13:    */ {
/*  14:    */   IndexedColorMap _colorMap;
/*  15:    */   CTFill _fill;
/*  16:    */   
/*  17:    */   XSSFPatternFormatting(CTFill fill, IndexedColorMap colorMap)
/*  18:    */   {
/*  19: 36 */     this._fill = fill;
/*  20: 37 */     this._colorMap = colorMap;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public XSSFColor getFillBackgroundColorColor()
/*  24:    */   {
/*  25: 41 */     if (!this._fill.isSetPatternFill()) {
/*  26: 41 */       return null;
/*  27:    */     }
/*  28: 42 */     return new XSSFColor(this._fill.getPatternFill().getBgColor(), this._colorMap);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public XSSFColor getFillForegroundColorColor()
/*  32:    */   {
/*  33: 45 */     if ((!this._fill.isSetPatternFill()) || (!this._fill.getPatternFill().isSetFgColor())) {
/*  34: 46 */       return null;
/*  35:    */     }
/*  36: 47 */     return new XSSFColor(this._fill.getPatternFill().getFgColor(), this._colorMap);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public short getFillPattern()
/*  40:    */   {
/*  41: 51 */     if ((!this._fill.isSetPatternFill()) || (!this._fill.getPatternFill().isSetPatternType())) {
/*  42: 51 */       return 0;
/*  43:    */     }
/*  44: 53 */     return (short)(this._fill.getPatternFill().getPatternType().intValue() - 1);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public short getFillBackgroundColor()
/*  48:    */   {
/*  49: 57 */     XSSFColor color = getFillBackgroundColorColor();
/*  50: 58 */     if (color == null) {
/*  51: 58 */       return 0;
/*  52:    */     }
/*  53: 59 */     return color.getIndexed();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public short getFillForegroundColor()
/*  57:    */   {
/*  58: 62 */     XSSFColor color = getFillForegroundColorColor();
/*  59: 63 */     if (color == null) {
/*  60: 63 */       return 0;
/*  61:    */     }
/*  62: 64 */     return color.getIndexed();
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setFillBackgroundColor(Color bg)
/*  66:    */   {
/*  67: 68 */     XSSFColor xcolor = XSSFColor.toXSSFColor(bg);
/*  68: 69 */     if (xcolor == null) {
/*  69: 69 */       setFillBackgroundColor((CTColor)null);
/*  70:    */     } else {
/*  71: 70 */       setFillBackgroundColor(xcolor.getCTColor());
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setFillBackgroundColor(short bg)
/*  76:    */   {
/*  77: 73 */     CTColor bgColor = CTColor.Factory.newInstance();
/*  78: 74 */     bgColor.setIndexed(bg);
/*  79: 75 */     setFillBackgroundColor(bgColor);
/*  80:    */   }
/*  81:    */   
/*  82:    */   private void setFillBackgroundColor(CTColor color)
/*  83:    */   {
/*  84: 78 */     CTPatternFill ptrn = this._fill.isSetPatternFill() ? this._fill.getPatternFill() : this._fill.addNewPatternFill();
/*  85: 79 */     if (color == null) {
/*  86: 80 */       ptrn.unsetBgColor();
/*  87:    */     } else {
/*  88: 82 */       ptrn.setBgColor(color);
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setFillForegroundColor(Color fg)
/*  93:    */   {
/*  94: 87 */     XSSFColor xcolor = XSSFColor.toXSSFColor(fg);
/*  95: 88 */     if (xcolor == null) {
/*  96: 88 */       setFillForegroundColor((CTColor)null);
/*  97:    */     } else {
/*  98: 89 */       setFillForegroundColor(xcolor.getCTColor());
/*  99:    */     }
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setFillForegroundColor(short fg)
/* 103:    */   {
/* 104: 92 */     CTColor fgColor = CTColor.Factory.newInstance();
/* 105: 93 */     fgColor.setIndexed(fg);
/* 106: 94 */     setFillForegroundColor(fgColor);
/* 107:    */   }
/* 108:    */   
/* 109:    */   private void setFillForegroundColor(CTColor color)
/* 110:    */   {
/* 111: 97 */     CTPatternFill ptrn = this._fill.isSetPatternFill() ? this._fill.getPatternFill() : this._fill.addNewPatternFill();
/* 112: 98 */     if (color == null) {
/* 113: 99 */       ptrn.unsetFgColor();
/* 114:    */     } else {
/* 115:101 */       ptrn.setFgColor(color);
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setFillPattern(short fp)
/* 120:    */   {
/* 121:106 */     CTPatternFill ptrn = this._fill.isSetPatternFill() ? this._fill.getPatternFill() : this._fill.addNewPatternFill();
/* 122:107 */     if (fp == 0) {
/* 123:107 */       ptrn.unsetPatternType();
/* 124:    */     } else {
/* 125:108 */       ptrn.setPatternType(STPatternType.Enum.forInt(fp + 1));
/* 126:    */     }
/* 127:    */   }
/* 128:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFPatternFormatting
 * JD-Core Version:    0.7.0.1
 */