/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
/*  4:   */ import org.apache.poi.ss.formula.WorkbookDependentFormula;
/*  5:   */ import org.apache.poi.util.LittleEndianInput;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class NamePtg
/*  9:   */   extends OperandPtg
/* 10:   */   implements WorkbookDependentFormula
/* 11:   */ {
/* 12:   */   public static final short sid = 35;
/* 13:   */   private static final int SIZE = 5;
/* 14:   */   private int field_1_label_index;
/* 15:   */   private short field_2_zero;
/* 16:   */   
/* 17:   */   public NamePtg(int nameIndex)
/* 18:   */   {
/* 19:42 */     this.field_1_label_index = (1 + nameIndex);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public NamePtg(LittleEndianInput in)
/* 23:   */   {
/* 24:47 */     this.field_1_label_index = in.readUShort();
/* 25:48 */     this.field_2_zero = in.readShort();
/* 26:   */   }
/* 27:   */   
/* 28:   */   public int getIndex()
/* 29:   */   {
/* 30:55 */     return this.field_1_label_index - 1;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void write(LittleEndianOutput out)
/* 34:   */   {
/* 35:60 */     out.writeByte(35 + getPtgClass());
/* 36:61 */     out.writeShort(this.field_1_label_index);
/* 37:62 */     out.writeShort(this.field_2_zero);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public int getSize()
/* 41:   */   {
/* 42:67 */     return 5;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public String toFormulaString(FormulaRenderingWorkbook book)
/* 46:   */   {
/* 47:72 */     return book.getNameText(this);
/* 48:   */   }
/* 49:   */   
/* 50:   */   public String toFormulaString()
/* 51:   */   {
/* 52:77 */     throw new RuntimeException("3D references need a workbook to determine formula text");
/* 53:   */   }
/* 54:   */   
/* 55:   */   public byte getDefaultOperandClass()
/* 56:   */   {
/* 57:82 */     return 0;
/* 58:   */   }
/* 59:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.NamePtg
 * JD-Core Version:    0.7.0.1
 */