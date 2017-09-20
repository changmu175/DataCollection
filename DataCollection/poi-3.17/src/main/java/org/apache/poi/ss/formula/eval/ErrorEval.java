/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ import org.apache.poi.ss.usermodel.FormulaError;
/*  6:   */ 
/*  7:   */ public final class ErrorEval
/*  8:   */   implements ValueEval
/*  9:   */ {
/* 10:29 */   private static final Map<FormulaError, ErrorEval> evals = new HashMap();
/* 11:32 */   public static final ErrorEval NULL_INTERSECTION = new ErrorEval(FormulaError.NULL);
/* 12:34 */   public static final ErrorEval DIV_ZERO = new ErrorEval(FormulaError.DIV0);
/* 13:36 */   public static final ErrorEval VALUE_INVALID = new ErrorEval(FormulaError.VALUE);
/* 14:38 */   public static final ErrorEval REF_INVALID = new ErrorEval(FormulaError.REF);
/* 15:40 */   public static final ErrorEval NAME_INVALID = new ErrorEval(FormulaError.NAME);
/* 16:42 */   public static final ErrorEval NUM_ERROR = new ErrorEval(FormulaError.NUM);
/* 17:44 */   public static final ErrorEval NA = new ErrorEval(FormulaError.NA);
/* 18:47 */   public static final ErrorEval FUNCTION_NOT_IMPLEMENTED = new ErrorEval(FormulaError.FUNCTION_NOT_IMPLEMENTED);
/* 19:50 */   public static final ErrorEval CIRCULAR_REF_ERROR = new ErrorEval(FormulaError.CIRCULAR_REF);
/* 20:   */   private FormulaError _error;
/* 21:   */   
/* 22:   */   public static ErrorEval valueOf(int errorCode)
/* 23:   */   {
/* 24:58 */     FormulaError error = FormulaError.forInt(errorCode);
/* 25:59 */     ErrorEval eval = (ErrorEval)evals.get(error);
/* 26:60 */     if (eval != null) {
/* 27:61 */       return eval;
/* 28:   */     }
/* 29:63 */     throw new RuntimeException("Unhandled error type for code " + errorCode);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public static String getText(int errorCode)
/* 33:   */   {
/* 34:73 */     if (FormulaError.isValidCode(errorCode)) {
/* 35:74 */       return FormulaError.forInt(errorCode).getString();
/* 36:   */     }
/* 37:77 */     return "~non~std~err(" + errorCode + ")~";
/* 38:   */   }
/* 39:   */   
/* 40:   */   private ErrorEval(FormulaError error)
/* 41:   */   {
/* 42:82 */     this._error = error;
/* 43:83 */     evals.put(error, this);
/* 44:   */   }
/* 45:   */   
/* 46:   */   public int getErrorCode()
/* 47:   */   {
/* 48:87 */     return this._error.getLongCode();
/* 49:   */   }
/* 50:   */   
/* 51:   */   public String getErrorString()
/* 52:   */   {
/* 53:90 */     return this._error.getString();
/* 54:   */   }
/* 55:   */   
/* 56:   */   public String toString()
/* 57:   */   {
/* 58:93 */     return getClass().getName() + " [" + this._error.getString() + "]";
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.ErrorEval
 * JD-Core Version:    0.7.0.1
 */