/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
/*  4:   */ import org.apache.poi.ss.formula.WorkbookDependentFormula;
/*  5:   */ import org.apache.poi.ss.usermodel.FormulaError;
/*  6:   */ import org.apache.poi.util.LittleEndianInput;
/*  7:   */ import org.apache.poi.util.LittleEndianOutput;
/*  8:   */ 
/*  9:   */ public final class DeletedRef3DPtg
/* 10:   */   extends OperandPtg
/* 11:   */   implements WorkbookDependentFormula
/* 12:   */ {
/* 13:   */   public static final byte sid = 60;
/* 14:   */   private final int field_1_index_extern_sheet;
/* 15:   */   private final int unused1;
/* 16:   */   
/* 17:   */   public DeletedRef3DPtg(LittleEndianInput in)
/* 18:   */   {
/* 19:39 */     this.field_1_index_extern_sheet = in.readUShort();
/* 20:40 */     this.unused1 = in.readInt();
/* 21:   */   }
/* 22:   */   
/* 23:   */   public DeletedRef3DPtg(int externSheetIndex)
/* 24:   */   {
/* 25:44 */     this.field_1_index_extern_sheet = externSheetIndex;
/* 26:45 */     this.unused1 = 0;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String toFormulaString(FormulaRenderingWorkbook book)
/* 30:   */   {
/* 31:49 */     return ExternSheetNameResolver.prependSheetName(book, this.field_1_index_extern_sheet, FormulaError.REF.getString());
/* 32:   */   }
/* 33:   */   
/* 34:   */   public String toFormulaString()
/* 35:   */   {
/* 36:52 */     throw new RuntimeException("3D references need a workbook to determine formula text");
/* 37:   */   }
/* 38:   */   
/* 39:   */   public byte getDefaultOperandClass()
/* 40:   */   {
/* 41:55 */     return 0;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public int getSize()
/* 45:   */   {
/* 46:58 */     return 7;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public void write(LittleEndianOutput out)
/* 50:   */   {
/* 51:61 */     out.writeByte(60 + getPtgClass());
/* 52:62 */     out.writeShort(this.field_1_index_extern_sheet);
/* 53:63 */     out.writeInt(this.unused1);
/* 54:   */   }
/* 55:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.DeletedRef3DPtg
 * JD-Core Version:    0.7.0.1
 */