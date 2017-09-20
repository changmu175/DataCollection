/*  1:   */ package org.apache.poi.ss.formula;
/*  2:   */ 
/*  3:   */ public class SheetIdentifier
/*  4:   */ {
/*  5:   */   public String _bookName;
/*  6:   */   public NameIdentifier _sheetIdentifier;
/*  7:   */   
/*  8:   */   public SheetIdentifier(String bookName, NameIdentifier sheetIdentifier)
/*  9:   */   {
/* 10:25 */     this._bookName = bookName;
/* 11:26 */     this._sheetIdentifier = sheetIdentifier;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public String getBookName()
/* 15:   */   {
/* 16:29 */     return this._bookName;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public NameIdentifier getSheetIdentifier()
/* 20:   */   {
/* 21:32 */     return this._sheetIdentifier;
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected void asFormulaString(StringBuffer sb)
/* 25:   */   {
/* 26:35 */     if (this._bookName != null) {
/* 27:36 */       sb.append(" [").append(this._sheetIdentifier.getName()).append("]");
/* 28:   */     }
/* 29:38 */     if (this._sheetIdentifier.isQuoted()) {
/* 30:39 */       sb.append("'").append(this._sheetIdentifier.getName()).append("'");
/* 31:   */     } else {
/* 32:41 */       sb.append(this._sheetIdentifier.getName());
/* 33:   */     }
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String asFormulaString()
/* 37:   */   {
/* 38:45 */     StringBuffer sb = new StringBuffer(32);
/* 39:46 */     asFormulaString(sb);
/* 40:47 */     return sb.toString();
/* 41:   */   }
/* 42:   */   
/* 43:   */   public String toString()
/* 44:   */   {
/* 45:50 */     StringBuffer sb = new StringBuffer(64);
/* 46:51 */     sb.append(getClass().getName());
/* 47:52 */     sb.append(" [");
/* 48:53 */     asFormulaString(sb);
/* 49:54 */     sb.append("]");
/* 50:55 */     return sb.toString();
/* 51:   */   }
/* 52:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.SheetIdentifier
 * JD-Core Version:    0.7.0.1
 */