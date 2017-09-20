/*  1:   */ package org.apache.poi.ss.formula;
/*  2:   */ 
/*  3:   */ public class SheetRangeIdentifier
/*  4:   */   extends SheetIdentifier
/*  5:   */ {
/*  6:   */   public NameIdentifier _lastSheetIdentifier;
/*  7:   */   
/*  8:   */   public SheetRangeIdentifier(String bookName, NameIdentifier firstSheetIdentifier, NameIdentifier lastSheetIdentifier)
/*  9:   */   {
/* 10:24 */     super(bookName, firstSheetIdentifier);
/* 11:25 */     this._lastSheetIdentifier = lastSheetIdentifier;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public NameIdentifier getFirstSheetIdentifier()
/* 15:   */   {
/* 16:28 */     return super.getSheetIdentifier();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public NameIdentifier getLastSheetIdentifier()
/* 20:   */   {
/* 21:31 */     return this._lastSheetIdentifier;
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected void asFormulaString(StringBuffer sb)
/* 25:   */   {
/* 26:34 */     super.asFormulaString(sb);
/* 27:35 */     sb.append(':');
/* 28:36 */     if (this._lastSheetIdentifier.isQuoted()) {
/* 29:37 */       sb.append("'").append(this._lastSheetIdentifier.getName()).append("'");
/* 30:   */     } else {
/* 31:39 */       sb.append(this._lastSheetIdentifier.getName());
/* 32:   */     }
/* 33:   */   }
/* 34:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.SheetRangeIdentifier
 * JD-Core Version:    0.7.0.1
 */