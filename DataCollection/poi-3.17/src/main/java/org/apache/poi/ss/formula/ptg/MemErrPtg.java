/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianInput;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class MemErrPtg
/*  7:   */   extends OperandPtg
/*  8:   */ {
/*  9:   */   public static final short sid = 39;
/* 10:   */   private static final int SIZE = 7;
/* 11:   */   private int field_1_reserved;
/* 12:   */   private short field_2_subex_len;
/* 13:   */   
/* 14:   */   public MemErrPtg(LittleEndianInput in)
/* 15:   */   {
/* 16:36 */     this.field_1_reserved = in.readInt();
/* 17:37 */     this.field_2_subex_len = in.readShort();
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void write(LittleEndianOutput out)
/* 21:   */   {
/* 22:41 */     out.writeByte(39 + getPtgClass());
/* 23:42 */     out.writeInt(this.field_1_reserved);
/* 24:43 */     out.writeShort(this.field_2_subex_len);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public int getSize()
/* 28:   */   {
/* 29:47 */     return 7;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public String toFormulaString()
/* 33:   */   {
/* 34:51 */     return "ERR#";
/* 35:   */   }
/* 36:   */   
/* 37:   */   public byte getDefaultOperandClass()
/* 38:   */   {
/* 39:55 */     return 32;
/* 40:   */   }
/* 41:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.MemErrPtg
 * JD-Core Version:    0.7.0.1
 */