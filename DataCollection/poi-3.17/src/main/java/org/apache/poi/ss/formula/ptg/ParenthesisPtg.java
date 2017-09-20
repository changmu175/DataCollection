/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class ParenthesisPtg
/*  6:   */   extends ControlPtg
/*  7:   */ {
/*  8:   */   private static final int SIZE = 1;
/*  9:   */   public static final byte sid = 21;
/* 10:37 */   public static final ControlPtg instance = new ParenthesisPtg();
/* 11:   */   
/* 12:   */   public void write(LittleEndianOutput out)
/* 13:   */   {
/* 14:44 */     out.writeByte(21 + getPtgClass());
/* 15:   */   }
/* 16:   */   
/* 17:   */   public int getSize()
/* 18:   */   {
/* 19:48 */     return 1;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public String toFormulaString()
/* 23:   */   {
/* 24:52 */     return "()";
/* 25:   */   }
/* 26:   */   
/* 27:   */   public String toFormulaString(String[] operands)
/* 28:   */   {
/* 29:56 */     return "(" + operands[0] + ")";
/* 30:   */   }
/* 31:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.ParenthesisPtg
 * JD-Core Version:    0.7.0.1
 */