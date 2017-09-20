/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.TwoDEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.BoolEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  6:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  7:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  8:   */ 
/*  9:   */ public final class Hlookup
/* 10:   */   extends Var3or4ArgFunction
/* 11:   */ {
/* 12:43 */   private static final ValueEval DEFAULT_ARG3 = BoolEval.TRUE;
/* 13:   */   
/* 14:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/* 15:   */   {
/* 16:47 */     return evaluate(srcRowIndex, srcColumnIndex, arg0, arg1, arg2, DEFAULT_ARG3);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2, ValueEval arg3)
/* 20:   */   {
/* 21:   */     try
/* 22:   */     {
/* 23:55 */       ValueEval lookupValue = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
/* 24:56 */       TwoDEval tableArray = LookupUtils.resolveTableArrayArg(arg1);
/* 25:57 */       boolean isRangeLookup = LookupUtils.resolveRangeLookupArg(arg3, srcRowIndex, srcColumnIndex);
/* 26:58 */       int colIndex = LookupUtils.lookupIndexOfValue(lookupValue, LookupUtils.createRowVector(tableArray, 0), isRangeLookup);
/* 27:59 */       int rowIndex = LookupUtils.resolveRowOrColIndexArg(arg2, srcRowIndex, srcColumnIndex);
/* 28:60 */       LookupUtils.ValueVector resultCol = createResultColumnVector(tableArray, rowIndex);
/* 29:61 */       return resultCol.getItem(colIndex);
/* 30:   */     }
/* 31:   */     catch (EvaluationException e)
/* 32:   */     {
/* 33:63 */       return e.getErrorEval();
/* 34:   */     }
/* 35:   */   }
/* 36:   */   
/* 37:   */   private LookupUtils.ValueVector createResultColumnVector(TwoDEval tableArray, int rowIndex)
/* 38:   */     throws EvaluationException
/* 39:   */   {
/* 40:75 */     if (rowIndex >= tableArray.getHeight()) {
/* 41:76 */       throw EvaluationException.invalidRef();
/* 42:   */     }
/* 43:78 */     return LookupUtils.createRowVector(tableArray, rowIndex);
/* 44:   */   }
/* 45:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Hlookup
 * JD-Core Version:    0.7.0.1
 */