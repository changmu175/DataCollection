/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.List;
/*   7:    */ import org.apache.poi.ss.formula.LazyRefEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  10:    */ import org.apache.poi.ss.formula.eval.NotImplementedException;
/*  11:    */ import org.apache.poi.ss.formula.eval.NotImplementedFunctionException;
/*  12:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  13:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  14:    */ 
/*  15:    */ public class Subtotal
/*  16:    */   implements Function
/*  17:    */ {
/*  18:    */   private static Function findFunction(int functionCode)
/*  19:    */     throws EvaluationException
/*  20:    */   {
/*  21: 70 */     switch (functionCode)
/*  22:    */     {
/*  23:    */     case 1: 
/*  24: 71 */       return AggregateFunction.subtotalInstance(AggregateFunction.AVERAGE);
/*  25:    */     case 2: 
/*  26: 72 */       return Count.subtotalInstance();
/*  27:    */     case 3: 
/*  28: 73 */       return Counta.subtotalInstance();
/*  29:    */     case 4: 
/*  30: 74 */       return AggregateFunction.subtotalInstance(AggregateFunction.MAX);
/*  31:    */     case 5: 
/*  32: 75 */       return AggregateFunction.subtotalInstance(AggregateFunction.MIN);
/*  33:    */     case 6: 
/*  34: 76 */       return AggregateFunction.subtotalInstance(AggregateFunction.PRODUCT);
/*  35:    */     case 7: 
/*  36: 77 */       return AggregateFunction.subtotalInstance(AggregateFunction.STDEV);
/*  37:    */     case 8: 
/*  38: 78 */       throw new NotImplementedFunctionException("STDEVP");
/*  39:    */     case 9: 
/*  40: 79 */       return AggregateFunction.subtotalInstance(AggregateFunction.SUM);
/*  41:    */     case 10: 
/*  42: 80 */       throw new NotImplementedFunctionException("VAR");
/*  43:    */     case 11: 
/*  44: 81 */       throw new NotImplementedFunctionException("VARP");
/*  45:    */     }
/*  46: 83 */     if ((functionCode > 100) && (functionCode < 112)) {
/*  47: 84 */       throw new NotImplementedException("SUBTOTAL - with 'exclude hidden values' option");
/*  48:    */     }
/*  49: 86 */     throw EvaluationException.invalidValue();
/*  50:    */   }
/*  51:    */   
/*  52:    */   public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex)
/*  53:    */   {
/*  54: 90 */     int nInnerArgs = args.length - 1;
/*  55: 91 */     if (nInnerArgs < 1) {
/*  56: 92 */       return ErrorEval.VALUE_INVALID;
/*  57:    */     }
/*  58:    */     Function innerFunc;
/*  59:    */     try
/*  60:    */     {
/*  61: 97 */       ValueEval ve = OperandResolver.getSingleValue(args[0], srcRowIndex, srcColumnIndex);
/*  62: 98 */       int functionCode = OperandResolver.coerceValueToInt(ve);
/*  63: 99 */       innerFunc = findFunction(functionCode);
/*  64:    */     }
/*  65:    */     catch (EvaluationException e)
/*  66:    */     {
/*  67:101 */       return e.getErrorEval();
/*  68:    */     }
/*  69:105 */     List<ValueEval> list = new ArrayList(Arrays.asList(args).subList(1, args.length));
/*  70:    */     
/*  71:107 */     Iterator<ValueEval> it = list.iterator();
/*  72:112 */     while (it.hasNext())
/*  73:    */     {
/*  74:113 */       ValueEval eval = (ValueEval)it.next();
/*  75:114 */       if ((eval instanceof LazyRefEval))
/*  76:    */       {
/*  77:115 */         LazyRefEval lazyRefEval = (LazyRefEval)eval;
/*  78:116 */         if (lazyRefEval.isSubTotal()) {
/*  79:117 */           it.remove();
/*  80:    */         }
/*  81:    */       }
/*  82:    */     }
/*  83:122 */     return innerFunc.evaluate((ValueEval[])list.toArray(new ValueEval[list.size()]), srcRowIndex, srcColumnIndex);
/*  84:    */   }
/*  85:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Subtotal
 * JD-Core Version:    0.7.0.1
 */