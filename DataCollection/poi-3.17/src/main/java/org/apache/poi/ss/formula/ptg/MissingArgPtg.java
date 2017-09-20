/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class MissingArgPtg
/*  6:   */   extends ScalarConstantPtg
/*  7:   */ {
/*  8:   */   private static final int SIZE = 1;
/*  9:   */   public static final byte sid = 22;
/* 10:34 */   public static final Ptg instance = new MissingArgPtg();
/* 11:   */   
/* 12:   */   public void write(LittleEndianOutput out)
/* 13:   */   {
/* 14:41 */     out.writeByte(22 + getPtgClass());
/* 15:   */   }
/* 16:   */   
/* 17:   */   public int getSize()
/* 18:   */   {
/* 19:45 */     return 1;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public String toFormulaString()
/* 23:   */   {
/* 24:49 */     return " ";
/* 25:   */   }
/* 26:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.MissingArgPtg
 * JD-Core Version:    0.7.0.1
 */