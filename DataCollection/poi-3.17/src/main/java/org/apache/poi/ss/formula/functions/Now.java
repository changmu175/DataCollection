/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import java.util.Date;
/*  4:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  6:   */ import org.apache.poi.ss.usermodel.DateUtil;
/*  7:   */ 
/*  8:   */ public final class Now
/*  9:   */   extends Fixed0ArgFunction
/* 10:   */ {
/* 11:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex)
/* 12:   */   {
/* 13:34 */     Date now = new Date(System.currentTimeMillis());
/* 14:35 */     return new NumberEval(DateUtil.getExcelDate(now));
/* 15:   */   }
/* 16:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Now
 * JD-Core Version:    0.7.0.1
 */