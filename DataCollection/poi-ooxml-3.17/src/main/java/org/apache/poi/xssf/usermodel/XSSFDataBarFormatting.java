/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.Color;
/*  4:   */ import org.apache.poi.ss.usermodel.DataBarFormatting;
/*  5:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataBar;
/*  6:   */ 
/*  7:   */ public class XSSFDataBarFormatting
/*  8:   */   implements DataBarFormatting
/*  9:   */ {
/* 10:   */   IndexedColorMap _colorMap;
/* 11:   */   CTDataBar _databar;
/* 12:   */   
/* 13:   */   XSSFDataBarFormatting(CTDataBar databar, IndexedColorMap colorMap)
/* 14:   */   {
/* 15:34 */     this._databar = databar;
/* 16:35 */     this._colorMap = colorMap;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public boolean isIconOnly()
/* 20:   */   {
/* 21:39 */     if (this._databar.isSetShowValue()) {
/* 22:40 */       return !this._databar.getShowValue();
/* 23:   */     }
/* 24:41 */     return false;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void setIconOnly(boolean only)
/* 28:   */   {
/* 29:44 */     this._databar.setShowValue(!only);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public boolean isLeftToRight()
/* 33:   */   {
/* 34:48 */     return true;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void setLeftToRight(boolean ltr) {}
/* 38:   */   
/* 39:   */   public int getWidthMin()
/* 40:   */   {
/* 41:55 */     return 0;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public void setWidthMin(int width) {}
/* 45:   */   
/* 46:   */   public int getWidthMax()
/* 47:   */   {
/* 48:62 */     return 100;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public void setWidthMax(int width) {}
/* 52:   */   
/* 53:   */   public XSSFColor getColor()
/* 54:   */   {
/* 55:69 */     return new XSSFColor(this._databar.getColor(), this._colorMap);
/* 56:   */   }
/* 57:   */   
/* 58:   */   public void setColor(Color color)
/* 59:   */   {
/* 60:72 */     this._databar.setColor(((XSSFColor)color).getCTColor());
/* 61:   */   }
/* 62:   */   
/* 63:   */   public XSSFConditionalFormattingThreshold getMinThreshold()
/* 64:   */   {
/* 65:76 */     return new XSSFConditionalFormattingThreshold(this._databar.getCfvoArray(0));
/* 66:   */   }
/* 67:   */   
/* 68:   */   public XSSFConditionalFormattingThreshold getMaxThreshold()
/* 69:   */   {
/* 70:79 */     return new XSSFConditionalFormattingThreshold(this._databar.getCfvoArray(1));
/* 71:   */   }
/* 72:   */   
/* 73:   */   public XSSFConditionalFormattingThreshold createThreshold()
/* 74:   */   {
/* 75:83 */     return new XSSFConditionalFormattingThreshold(this._databar.addNewCfvo());
/* 76:   */   }
/* 77:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFDataBarFormatting
 * JD-Core Version:    0.7.0.1
 */