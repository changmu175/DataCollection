/*  1:   */ package org.apache.poi.xslf.usermodel;
/*  2:   */ 
/*  3:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle;
/*  4:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle;
/*  5:   */ 
/*  6:   */ public class XSLFTableStyle
/*  7:   */ {
/*  8:   */   private CTTableStyle _tblStyle;
/*  9:   */   
/* 10:   */   public static enum TablePartStyle
/* 11:   */   {
/* 12:32 */     wholeTbl,  band1H,  band2H,  band1V,  band2V,  firstCol,  lastCol,  firstRow,  lastRow,  seCell,  swCell,  neCell,  nwCell;
/* 13:   */     
/* 14:   */     private TablePartStyle() {}
/* 15:   */   }
/* 16:   */   
/* 17:   */   XSLFTableStyle(CTTableStyle style)
/* 18:   */   {
/* 19:36 */     this._tblStyle = style;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public CTTableStyle getXmlObject()
/* 23:   */   {
/* 24:40 */     return this._tblStyle;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public String getStyleName()
/* 28:   */   {
/* 29:44 */     return this._tblStyle.getStyleName();
/* 30:   */   }
/* 31:   */   
/* 32:   */   public String getStyleId()
/* 33:   */   {
/* 34:48 */     return this._tblStyle.getStyleId();
/* 35:   */   }
/* 36:   */   
/* 37:   */   protected CTTablePartStyle getTablePartStyle(TablePartStyle tps)
/* 38:   */   {
/* 39:55 */     switch (1.$SwitchMap$org$apache$poi$xslf$usermodel$XSLFTableStyle$TablePartStyle[tps.ordinal()])
/* 40:   */     {
/* 41:   */     case 1: 
/* 42:   */     default: 
/* 43:58 */       return this._tblStyle.getWholeTbl();
/* 44:   */     case 2: 
/* 45:60 */       return this._tblStyle.getBand1H();
/* 46:   */     case 3: 
/* 47:62 */       return this._tblStyle.getBand2H();
/* 48:   */     case 4: 
/* 49:64 */       return this._tblStyle.getBand1V();
/* 50:   */     case 5: 
/* 51:66 */       return this._tblStyle.getBand2V();
/* 52:   */     case 6: 
/* 53:68 */       return this._tblStyle.getFirstCol();
/* 54:   */     case 7: 
/* 55:70 */       return this._tblStyle.getLastCol();
/* 56:   */     case 8: 
/* 57:72 */       return this._tblStyle.getFirstRow();
/* 58:   */     case 9: 
/* 59:74 */       return this._tblStyle.getLastRow();
/* 60:   */     case 10: 
/* 61:76 */       return this._tblStyle.getSeCell();
/* 62:   */     case 11: 
/* 63:78 */       return this._tblStyle.getSwCell();
/* 64:   */     case 12: 
/* 65:80 */       return this._tblStyle.getNeCell();
/* 66:   */     }
/* 67:82 */     return this._tblStyle.getNwCell();
/* 68:   */   }
/* 69:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFTableStyle
 * JD-Core Version:    0.7.0.1
 */