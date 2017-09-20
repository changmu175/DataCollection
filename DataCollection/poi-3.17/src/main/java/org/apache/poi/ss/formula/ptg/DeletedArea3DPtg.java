/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
/*  4:   */ import org.apache.poi.ss.formula.WorkbookDependentFormula;
/*  5:   */ import org.apache.poi.ss.usermodel.FormulaError;
/*  6:   */ import org.apache.poi.util.LittleEndianInput;
/*  7:   */ import org.apache.poi.util.LittleEndianOutput;
/*  8:   */ 
/*  9:   */ public final class DeletedArea3DPtg
/* 10:   */   extends OperandPtg
/* 11:   */   implements WorkbookDependentFormula
/* 12:   */ {
/* 13:   */   public static final byte sid = 61;
/* 14:   */   private final int field_1_index_extern_sheet;
/* 15:   */   private final int unused1;
/* 16:   */   private final int unused2;
/* 17:   */   
/* 18:   */   public DeletedArea3DPtg(int externSheetIndex)
/* 19:   */   {
/* 20:40 */     this.field_1_index_extern_sheet = externSheetIndex;
/* 21:41 */     this.unused1 = 0;
/* 22:42 */     this.unused2 = 0;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public DeletedArea3DPtg(LittleEndianInput in)
/* 26:   */   {
/* 27:46 */     this.field_1_index_extern_sheet = in.readUShort();
/* 28:47 */     this.unused1 = in.readInt();
/* 29:48 */     this.unused2 = in.readInt();
/* 30:   */   }
/* 31:   */   
/* 32:   */   public String toFormulaString(FormulaRenderingWorkbook book)
/* 33:   */   {
/* 34:51 */     return ExternSheetNameResolver.prependSheetName(book, this.field_1_index_extern_sheet, FormulaError.REF.getString());
/* 35:   */   }
/* 36:   */   
/* 37:   */   public String toFormulaString()
/* 38:   */   {
/* 39:54 */     throw new RuntimeException("3D references need a workbook to determine formula text");
/* 40:   */   }
/* 41:   */   
/* 42:   */   public byte getDefaultOperandClass()
/* 43:   */   {
/* 44:57 */     return 0;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public int getSize()
/* 48:   */   {
/* 49:60 */     return 11;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public void write(LittleEndianOutput out)
/* 53:   */   {
/* 54:63 */     out.writeByte(61 + getPtgClass());
/* 55:64 */     out.writeShort(this.field_1_index_extern_sheet);
/* 56:65 */     out.writeInt(this.unused1);
/* 57:66 */     out.writeInt(this.unused2);
/* 58:   */   }
/* 59:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.DeletedArea3DPtg
 * JD-Core Version:    0.7.0.1
 */