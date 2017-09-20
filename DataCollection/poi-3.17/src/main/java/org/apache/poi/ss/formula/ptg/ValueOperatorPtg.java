/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public abstract class ValueOperatorPtg
/*  6:   */   extends OperationPtg
/*  7:   */ {
/*  8:   */   public final boolean isBaseToken()
/*  9:   */   {
/* 10:35 */     return true;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public final byte getDefaultOperandClass()
/* 14:   */   {
/* 15:39 */     return 32;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void write(LittleEndianOutput out)
/* 19:   */   {
/* 20:43 */     out.writeByte(getSid());
/* 21:   */   }
/* 22:   */   
/* 23:   */   protected abstract byte getSid();
/* 24:   */   
/* 25:   */   public final int getSize()
/* 26:   */   {
/* 27:49 */     return 1;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public final String toFormulaString()
/* 31:   */   {
/* 32:54 */     throw new RuntimeException("toFormulaString(String[] operands) should be used for subclasses of OperationPtgs");
/* 33:   */   }
/* 34:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.ValueOperatorPtg
 * JD-Core Version:    0.7.0.1
 */