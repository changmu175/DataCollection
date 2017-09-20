/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class RangePtg
/*  6:   */   extends OperationPtg
/*  7:   */ {
/*  8:   */   public static final int SIZE = 1;
/*  9:   */   public static final byte sid = 17;
/* 10:30 */   public static final OperationPtg instance = new RangePtg();
/* 11:   */   
/* 12:   */   public final boolean isBaseToken()
/* 13:   */   {
/* 14:37 */     return true;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public int getSize()
/* 18:   */   {
/* 19:42 */     return 1;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void write(LittleEndianOutput out)
/* 23:   */   {
/* 24:46 */     out.writeByte(17 + getPtgClass());
/* 25:   */   }
/* 26:   */   
/* 27:   */   public String toFormulaString()
/* 28:   */   {
/* 29:51 */     return ":";
/* 30:   */   }
/* 31:   */   
/* 32:   */   public String toFormulaString(String[] operands)
/* 33:   */   {
/* 34:58 */     StringBuffer buffer = new StringBuffer();
/* 35:   */     
/* 36:60 */     buffer.append(operands[0]);
/* 37:61 */     buffer.append(":");
/* 38:62 */     buffer.append(operands[1]);
/* 39:63 */     return buffer.toString();
/* 40:   */   }
/* 41:   */   
/* 42:   */   public int getNumberOfOperands()
/* 43:   */   {
/* 44:68 */     return 2;
/* 45:   */   }
/* 46:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.RangePtg
 * JD-Core Version:    0.7.0.1
 */