/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.ExternSheetReferenceToken;
/*  4:   */ import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
/*  5:   */ import org.apache.poi.ss.formula.WorkbookDependentFormula;
/*  6:   */ import org.apache.poi.ss.util.CellReference;
/*  7:   */ import org.apache.poi.util.LittleEndianInput;
/*  8:   */ import org.apache.poi.util.LittleEndianOutput;
/*  9:   */ 
/* 10:   */ public final class Ref3DPtg
/* 11:   */   extends RefPtgBase
/* 12:   */   implements WorkbookDependentFormula, ExternSheetReferenceToken
/* 13:   */ {
/* 14:   */   public static final byte sid = 58;
/* 15:   */   private static final int SIZE = 7;
/* 16:   */   private int field_1_index_extern_sheet;
/* 17:   */   
/* 18:   */   public Ref3DPtg(LittleEndianInput in)
/* 19:   */   {
/* 20:44 */     this.field_1_index_extern_sheet = in.readShort();
/* 21:45 */     readCoordinates(in);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public Ref3DPtg(String cellref, int externIdx)
/* 25:   */   {
/* 26:49 */     this(new CellReference(cellref), externIdx);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public Ref3DPtg(CellReference c, int externIdx)
/* 30:   */   {
/* 31:53 */     super(c);
/* 32:54 */     setExternSheetIndex(externIdx);
/* 33:   */   }
/* 34:   */   
/* 35:   */   public String toString()
/* 36:   */   {
/* 37:58 */     StringBuffer sb = new StringBuffer();
/* 38:59 */     sb.append(getClass().getName());
/* 39:60 */     sb.append(" [");
/* 40:61 */     sb.append("sheetIx=").append(getExternSheetIndex());
/* 41:62 */     sb.append(" ! ");
/* 42:63 */     sb.append(formatReferenceAsString());
/* 43:64 */     sb.append("]");
/* 44:65 */     return sb.toString();
/* 45:   */   }
/* 46:   */   
/* 47:   */   public void write(LittleEndianOutput out)
/* 48:   */   {
/* 49:69 */     out.writeByte(58 + getPtgClass());
/* 50:70 */     out.writeShort(getExternSheetIndex());
/* 51:71 */     writeCoordinates(out);
/* 52:   */   }
/* 53:   */   
/* 54:   */   public int getSize()
/* 55:   */   {
/* 56:75 */     return 7;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public int getExternSheetIndex()
/* 60:   */   {
/* 61:79 */     return this.field_1_index_extern_sheet;
/* 62:   */   }
/* 63:   */   
/* 64:   */   public void setExternSheetIndex(int index)
/* 65:   */   {
/* 66:83 */     this.field_1_index_extern_sheet = index;
/* 67:   */   }
/* 68:   */   
/* 69:   */   public String format2DRefAsString()
/* 70:   */   {
/* 71:86 */     return formatReferenceAsString();
/* 72:   */   }
/* 73:   */   
/* 74:   */   public String toFormulaString(FormulaRenderingWorkbook book)
/* 75:   */   {
/* 76:93 */     return ExternSheetNameResolver.prependSheetName(book, this.field_1_index_extern_sheet, formatReferenceAsString());
/* 77:   */   }
/* 78:   */   
/* 79:   */   public String toFormulaString()
/* 80:   */   {
/* 81:96 */     throw new RuntimeException("3D references need a workbook to determine formula text");
/* 82:   */   }
/* 83:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.Ref3DPtg
 * JD-Core Version:    0.7.0.1
 */