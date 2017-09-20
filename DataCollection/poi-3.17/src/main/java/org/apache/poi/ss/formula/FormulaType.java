/*  1:   */ package org.apache.poi.ss.formula;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ 
/*  5:   */ @Internal
/*  6:   */ public enum FormulaType
/*  7:   */ {
/*  8:30 */   CELL(true),  SHARED(true),  ARRAY(false),  CONDFORMAT(true),  NAMEDRANGE(false),  DATAVALIDATION_LIST(false);
/*  9:   */   
/* 10:   */   private final boolean isSingleValue;
/* 11:   */   
/* 12:   */   private FormulaType(boolean singleValue)
/* 13:   */   {
/* 14:64 */     this.isSingleValue = singleValue;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public boolean isSingleValue()
/* 18:   */   {
/* 19:71 */     return this.isSingleValue;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public static FormulaType forInt(int code)
/* 23:   */   {
/* 24:82 */     if ((code >= 0) && (code < values().length)) {
/* 25:83 */       return values()[code];
/* 26:   */     }
/* 27:85 */     throw new IllegalArgumentException("Invalid FormulaType code: " + code);
/* 28:   */   }
/* 29:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.FormulaType
 * JD-Core Version:    0.7.0.1
 */