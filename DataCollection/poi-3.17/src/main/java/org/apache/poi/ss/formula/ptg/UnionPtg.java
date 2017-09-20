/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class UnionPtg
/*  6:   */   extends OperationPtg
/*  7:   */ {
/*  8:   */   public static final byte sid = 16;
/*  9:29 */   public static final OperationPtg instance = new UnionPtg();
/* 10:   */   
/* 11:   */   public final boolean isBaseToken()
/* 12:   */   {
/* 13:36 */     return true;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public int getSize()
/* 17:   */   {
/* 18:41 */     return 1;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void write(LittleEndianOutput out)
/* 22:   */   {
/* 23:45 */     out.writeByte(16 + getPtgClass());
/* 24:   */   }
/* 25:   */   
/* 26:   */   public String toFormulaString()
/* 27:   */   {
/* 28:50 */     return ",";
/* 29:   */   }
/* 30:   */   
/* 31:   */   public String toFormulaString(String[] operands)
/* 32:   */   {
/* 33:57 */     StringBuffer buffer = new StringBuffer();
/* 34:   */     
/* 35:59 */     buffer.append(operands[0]);
/* 36:60 */     buffer.append(",");
/* 37:61 */     buffer.append(operands[1]);
/* 38:62 */     return buffer.toString();
/* 39:   */   }
/* 40:   */   
/* 41:   */   public int getNumberOfOperands()
/* 42:   */   {
/* 43:67 */     return 2;
/* 44:   */   }
/* 45:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.UnionPtg
 * JD-Core Version:    0.7.0.1
 */