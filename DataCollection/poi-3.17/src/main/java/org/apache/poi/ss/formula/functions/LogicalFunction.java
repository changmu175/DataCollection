/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.AreaEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   8:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  10:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*  11:    */ import org.apache.poi.ss.formula.eval.RefListEval;
/*  12:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  13:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  14:    */ 
/*  15:    */ public abstract class LogicalFunction
/*  16:    */   extends Fixed1ArgFunction
/*  17:    */ {
/*  18:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/*  19:    */   {
/*  20:    */     ValueEval ve;
/*  21:    */     try
/*  22:    */     {
/*  23: 32 */       ve = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
/*  24:    */     }
/*  25:    */     catch (EvaluationException e)
/*  26:    */     {
/*  27: 39 */       ve = e.getErrorEval();
/*  28:    */     }
/*  29: 41 */     return BoolEval.valueOf(evaluate(ve));
/*  30:    */   }
/*  31:    */   
/*  32: 49 */   public static final Function ISLOGICAL = new LogicalFunction()
/*  33:    */   {
/*  34:    */     protected boolean evaluate(ValueEval arg)
/*  35:    */     {
/*  36: 51 */       return arg instanceof BoolEval;
/*  37:    */     }
/*  38:    */   };
/*  39: 54 */   public static final Function ISNONTEXT = new LogicalFunction()
/*  40:    */   {
/*  41:    */     protected boolean evaluate(ValueEval arg)
/*  42:    */     {
/*  43: 56 */       return !(arg instanceof StringEval);
/*  44:    */     }
/*  45:    */   };
/*  46: 59 */   public static final Function ISNUMBER = new LogicalFunction()
/*  47:    */   {
/*  48:    */     protected boolean evaluate(ValueEval arg)
/*  49:    */     {
/*  50: 61 */       return arg instanceof NumberEval;
/*  51:    */     }
/*  52:    */   };
/*  53: 64 */   public static final Function ISTEXT = new LogicalFunction()
/*  54:    */   {
/*  55:    */     protected boolean evaluate(ValueEval arg)
/*  56:    */     {
/*  57: 66 */       return arg instanceof StringEval;
/*  58:    */     }
/*  59:    */   };
/*  60: 70 */   public static final Function ISBLANK = new LogicalFunction()
/*  61:    */   {
/*  62:    */     protected boolean evaluate(ValueEval arg)
/*  63:    */     {
/*  64: 73 */       return arg instanceof BlankEval;
/*  65:    */     }
/*  66:    */   };
/*  67: 77 */   public static final Function ISERROR = new LogicalFunction()
/*  68:    */   {
/*  69:    */     protected boolean evaluate(ValueEval arg)
/*  70:    */     {
/*  71: 80 */       return arg instanceof ErrorEval;
/*  72:    */     }
/*  73:    */   };
/*  74: 95 */   public static final Function ISERR = new LogicalFunction()
/*  75:    */   {
/*  76:    */     protected boolean evaluate(ValueEval arg)
/*  77:    */     {
/*  78: 98 */       if ((arg instanceof ErrorEval)) {
/*  79: 99 */         return arg != ErrorEval.NA;
/*  80:    */       }
/*  81:101 */       return false;
/*  82:    */     }
/*  83:    */   };
/*  84:115 */   public static final Function ISNA = new LogicalFunction()
/*  85:    */   {
/*  86:    */     protected boolean evaluate(ValueEval arg)
/*  87:    */     {
/*  88:118 */       return arg == ErrorEval.NA;
/*  89:    */     }
/*  90:    */   };
/*  91:122 */   public static final Function ISREF = new Fixed1ArgFunction()
/*  92:    */   {
/*  93:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/*  94:    */     {
/*  95:125 */       if (((arg0 instanceof RefEval)) || ((arg0 instanceof AreaEval)) || ((arg0 instanceof RefListEval))) {
/*  96:126 */         return BoolEval.TRUE;
/*  97:    */       }
/*  98:128 */       return BoolEval.FALSE;
/*  99:    */     }
/* 100:    */   };
/* 101:    */   
/* 102:    */   protected abstract boolean evaluate(ValueEval paramValueEval);
/* 103:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.LogicalFunction
 * JD-Core Version:    0.7.0.1
 */