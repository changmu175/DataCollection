/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianInput;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class MemFuncPtg
/*  7:   */   extends OperandPtg
/*  8:   */ {
/*  9:   */   public static final byte sid = 41;
/* 10:   */   private final int field_1_len_ref_subexpression;
/* 11:   */   
/* 12:   */   public MemFuncPtg(LittleEndianInput in)
/* 13:   */   {
/* 14:36 */     this(in.readUShort());
/* 15:   */   }
/* 16:   */   
/* 17:   */   public MemFuncPtg(int subExprLen)
/* 18:   */   {
/* 19:40 */     this.field_1_len_ref_subexpression = subExprLen;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public int getSize()
/* 23:   */   {
/* 24:44 */     return 3;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void write(LittleEndianOutput out)
/* 28:   */   {
/* 29:48 */     out.writeByte(41 + getPtgClass());
/* 30:49 */     out.writeShort(this.field_1_len_ref_subexpression);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public String toFormulaString()
/* 34:   */   {
/* 35:53 */     return "";
/* 36:   */   }
/* 37:   */   
/* 38:   */   public byte getDefaultOperandClass()
/* 39:   */   {
/* 40:57 */     return 0;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public int getNumberOfOperands()
/* 44:   */   {
/* 45:61 */     return this.field_1_len_ref_subexpression;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public int getLenRefSubexpression()
/* 49:   */   {
/* 50:65 */     return this.field_1_len_ref_subexpression;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public final String toString()
/* 54:   */   {
/* 55:69 */     StringBuffer sb = new StringBuffer(64);
/* 56:70 */     sb.append(getClass().getName()).append(" [len=");
/* 57:71 */     sb.append(this.field_1_len_ref_subexpression);
/* 58:72 */     sb.append("]");
/* 59:73 */     return sb.toString();
/* 60:   */   }
/* 61:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.MemFuncPtg
 * JD-Core Version:    0.7.0.1
 */