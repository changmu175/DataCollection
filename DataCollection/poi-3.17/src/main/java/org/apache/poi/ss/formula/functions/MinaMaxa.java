/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ public abstract class MinaMaxa
/*  4:   */   extends MultiOperandNumericFunction
/*  5:   */ {
/*  6:   */   protected MinaMaxa()
/*  7:   */   {
/*  8:27 */     super(true, true);
/*  9:   */   }
/* 10:   */   
/* 11:30 */   public static final Function MAXA = new MinaMaxa()
/* 12:   */   {
/* 13:   */     protected double evaluate(double[] values)
/* 14:   */     {
/* 15:32 */       return values.length > 0 ? MathX.max(values) : 0.0D;
/* 16:   */     }
/* 17:   */   };
/* 18:35 */   public static final Function MINA = new MinaMaxa()
/* 19:   */   {
/* 20:   */     protected double evaluate(double[] values)
/* 21:   */     {
/* 22:37 */       return values.length > 0 ? MathX.min(values) : 0.0D;
/* 23:   */     }
/* 24:   */   };
/* 25:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.MinaMaxa
 * JD-Core Version:    0.7.0.1
 */