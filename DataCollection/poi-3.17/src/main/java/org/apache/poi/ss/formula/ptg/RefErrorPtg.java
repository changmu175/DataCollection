/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.FormulaError;
/*  4:   */ import org.apache.poi.util.LittleEndianInput;
/*  5:   */ import org.apache.poi.util.LittleEndianOutput;
/*  6:   */ 
/*  7:   */ public final class RefErrorPtg
/*  8:   */   extends OperandPtg
/*  9:   */ {
/* 10:   */   private static final int SIZE = 5;
/* 11:   */   public static final byte sid = 42;
/* 12:   */   private int field_1_reserved;
/* 13:   */   
/* 14:   */   public RefErrorPtg()
/* 15:   */   {
/* 16:35 */     this.field_1_reserved = 0;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public RefErrorPtg(LittleEndianInput in)
/* 20:   */   {
/* 21:38 */     this.field_1_reserved = in.readInt();
/* 22:   */   }
/* 23:   */   
/* 24:   */   public String toString()
/* 25:   */   {
/* 26:42 */     return getClass().getName();
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void write(LittleEndianOutput out)
/* 30:   */   {
/* 31:46 */     out.writeByte(42 + getPtgClass());
/* 32:47 */     out.writeInt(this.field_1_reserved);
/* 33:   */   }
/* 34:   */   
/* 35:   */   public int getSize()
/* 36:   */   {
/* 37:52 */     return 5;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public String toFormulaString()
/* 41:   */   {
/* 42:56 */     return FormulaError.REF.getString();
/* 43:   */   }
/* 44:   */   
/* 45:   */   public byte getDefaultOperandClass()
/* 46:   */   {
/* 47:60 */     return 0;
/* 48:   */   }
/* 49:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.RefErrorPtg
 * JD-Core Version:    0.7.0.1
 */