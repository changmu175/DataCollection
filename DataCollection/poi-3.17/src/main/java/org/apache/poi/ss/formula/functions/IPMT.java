/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  5:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  6:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  7:   */ 
/*  8:   */ public class IPMT
/*  9:   */   extends NumericFunction
/* 10:   */ {
/* 11:   */   public double eval(ValueEval[] args, int srcCellRow, int srcCellCol)
/* 12:   */     throws EvaluationException
/* 13:   */   {
/* 14:32 */     if (args.length != 4) {
/* 15:33 */       throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 16:   */     }
/* 17:37 */     ValueEval v1 = OperandResolver.getSingleValue(args[0], srcCellRow, srcCellCol);
/* 18:38 */     ValueEval v2 = OperandResolver.getSingleValue(args[1], srcCellRow, srcCellCol);
/* 19:39 */     ValueEval v3 = OperandResolver.getSingleValue(args[2], srcCellRow, srcCellCol);
/* 20:40 */     ValueEval v4 = OperandResolver.getSingleValue(args[3], srcCellRow, srcCellCol);
/* 21:   */     
/* 22:42 */     double interestRate = OperandResolver.coerceValueToDouble(v1);
/* 23:43 */     int period = OperandResolver.coerceValueToInt(v2);
/* 24:44 */     int numberPayments = OperandResolver.coerceValueToInt(v3);
/* 25:45 */     double PV = OperandResolver.coerceValueToDouble(v4);
/* 26:   */     
/* 27:47 */     double result = Finance.ipmt(interestRate, period, numberPayments, PV);
/* 28:   */     
/* 29:49 */     checkValue(result);
/* 30:   */     
/* 31:51 */     return result;
/* 32:   */   }
/* 33:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.IPMT
 * JD-Core Version:    0.7.0.1
 */