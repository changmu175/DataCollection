/*  1:   */ package org.apache.poi.ss.formula;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  4:   */ 
/*  5:   */ final class PlainValueCellCacheEntry
/*  6:   */   extends CellCacheEntry
/*  7:   */ {
/*  8:   */   public PlainValueCellCacheEntry(ValueEval value)
/*  9:   */   {
/* 10:29 */     updateValue(value);
/* 11:   */   }
/* 12:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.PlainValueCellCacheEntry
 * JD-Core Version:    0.7.0.1
 */