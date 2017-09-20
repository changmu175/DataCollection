/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.FormulaError;
/*  4:   */ import org.apache.poi.util.LittleEndianInput;
/*  5:   */ import org.apache.poi.util.LittleEndianOutput;
/*  6:   */ 
/*  7:   */ public final class AreaErrPtg
/*  8:   */   extends OperandPtg
/*  9:   */ {
/* 10:   */   public static final byte sid = 43;
/* 11:   */   private final int unused1;
/* 12:   */   private final int unused2;
/* 13:   */   
/* 14:   */   public AreaErrPtg()
/* 15:   */   {
/* 16:35 */     this.unused1 = 0;
/* 17:36 */     this.unused2 = 0;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public AreaErrPtg(LittleEndianInput in)
/* 21:   */   {
/* 22:41 */     this.unused1 = in.readInt();
/* 23:42 */     this.unused2 = in.readInt();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void write(LittleEndianOutput out)
/* 27:   */   {
/* 28:46 */     out.writeByte(43 + getPtgClass());
/* 29:47 */     out.writeInt(this.unused1);
/* 30:48 */     out.writeInt(this.unused2);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public String toFormulaString()
/* 34:   */   {
/* 35:52 */     return FormulaError.REF.getString();
/* 36:   */   }
/* 37:   */   
/* 38:   */   public byte getDefaultOperandClass()
/* 39:   */   {
/* 40:56 */     return 0;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public int getSize()
/* 44:   */   {
/* 45:60 */     return 9;
/* 46:   */   }
/* 47:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.AreaErrPtg
 * JD-Core Version:    0.7.0.1
 */