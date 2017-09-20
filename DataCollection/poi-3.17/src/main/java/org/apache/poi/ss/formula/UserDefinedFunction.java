/*  1:   */ package org.apache.poi.ss.formula;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.FunctionNameEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.NotImplementedFunctionException;
/*  5:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  6:   */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/*  7:   */ 
/*  8:   */ final class UserDefinedFunction
/*  9:   */   implements FreeRefFunction
/* 10:   */ {
/* 11:34 */   public static final FreeRefFunction instance = new UserDefinedFunction();
/* 12:   */   
/* 13:   */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 14:   */   {
/* 15:41 */     int nIncomingArgs = args.length;
/* 16:42 */     if (nIncomingArgs < 1) {
/* 17:43 */       throw new RuntimeException("function name argument missing");
/* 18:   */     }
/* 19:46 */     ValueEval nameArg = args[0];
/* 20:   */     String functionName;
/* 21:48 */     if ((nameArg instanceof FunctionNameEval)) {
/* 22:49 */       functionName = ((FunctionNameEval)nameArg).getFunctionName();
/* 23:   */     } else {
/* 24:51 */       throw new RuntimeException("First argument should be a NameEval, but got (" + nameArg.getClass().getName() + ")");
/* 25:   */     }
/* 26:   */     String functionName;
/* 27:54 */     FreeRefFunction targetFunc = ec.findUserDefinedFunction(functionName);
/* 28:55 */     if (targetFunc == null) {
/* 29:56 */       throw new NotImplementedFunctionException(functionName);
/* 30:   */     }
/* 31:58 */     int nOutGoingArgs = nIncomingArgs - 1;
/* 32:59 */     ValueEval[] outGoingArgs = new ValueEval[nOutGoingArgs];
/* 33:60 */     System.arraycopy(args, 1, outGoingArgs, 0, nOutGoingArgs);
/* 34:61 */     return targetFunc.evaluate(outGoingArgs, ec);
/* 35:   */   }
/* 36:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.UserDefinedFunction
 * JD-Core Version:    0.7.0.1
 */