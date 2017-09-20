/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.TwoDEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.BoolEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  6:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  7:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  8:   */ 
/*  9:   */ public final class Vlookup
/* 10:   */   extends Var3or4ArgFunction
/* 11:   */ {
/* 12:41 */   private static final ValueEval DEFAULT_ARG3 = BoolEval.TRUE;
/* 13:   */   
/* 14:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/* 15:   */   {
/* 16:45 */     return evaluate(srcRowIndex, srcColumnIndex, arg0, arg1, arg2, DEFAULT_ARG3);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval lookup_value, ValueEval table_array, ValueEval col_index, ValueEval range_lookup)
/* 20:   */   {
/* 21:   */     try
/* 22:   */     {
/* 23:53 */       ValueEval lookupValue = OperandResolver.getSingleValue(lookup_value, srcRowIndex, srcColumnIndex);
/* 24:54 */       TwoDEval tableArray = LookupUtils.resolveTableArrayArg(table_array);
/* 25:55 */       boolean isRangeLookup = LookupUtils.resolveRangeLookupArg(range_lookup, srcRowIndex, srcColumnIndex);
/* 26:56 */       int rowIndex = LookupUtils.lookupIndexOfValue(lookupValue, LookupUtils.createColumnVector(tableArray, 0), isRangeLookup);
/* 27:57 */       int colIndex = LookupUtils.resolveRowOrColIndexArg(col_index, srcRowIndex, srcColumnIndex);
/* 28:58 */       LookupUtils.ValueVector resultCol = createResultColumnVector(tableArray, colIndex);
/* 29:59 */       return resultCol.getItem(rowIndex);
/* 30:   */     }
/* 31:   */     catch (EvaluationException e)
/* 32:   */     {
/* 33:61 */       return e.getErrorEval();
/* 34:   */     }
/* 35:   */   }
/* 36:   */   
/* 37:   */   private LookupUtils.ValueVector createResultColumnVector(TwoDEval tableArray, int colIndex)
/* 38:   */     throws EvaluationException
/* 39:   */   {
/* 40:74 */     if (colIndex >= tableArray.getWidth()) {
/* 41:75 */       throw EvaluationException.invalidRef();
/* 42:   */     }
/* 43:77 */     return LookupUtils.createColumnVector(tableArray, colIndex);
/* 44:   */   }
/* 45:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Vlookup
 * JD-Core Version:    0.7.0.1
 */