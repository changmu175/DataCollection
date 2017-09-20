/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import java.util.Calendar;
/*  4:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  6:   */ import org.apache.poi.ss.usermodel.DateUtil;
/*  7:   */ import org.apache.poi.util.LocaleUtil;
/*  8:   */ 
/*  9:   */ public final class Today
/* 10:   */   extends Fixed0ArgFunction
/* 11:   */ {
/* 12:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex)
/* 13:   */   {
/* 14:32 */     Calendar now = LocaleUtil.getLocaleCalendar();
/* 15:33 */     now.clear(10);
/* 16:34 */     now.set(11, 0);
/* 17:35 */     now.clear(12);
/* 18:36 */     now.clear(13);
/* 19:37 */     now.clear(14);
/* 20:38 */     return new NumberEval(DateUtil.getExcelDate(now.getTime()));
/* 21:   */   }
/* 22:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Today
 * JD-Core Version:    0.7.0.1
 */