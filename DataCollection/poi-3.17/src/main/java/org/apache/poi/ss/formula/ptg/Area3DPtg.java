/*   1:    */ package org.apache.poi.ss.formula.ptg;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   4:    */ import org.apache.poi.ss.formula.ExternSheetReferenceToken;
/*   5:    */ import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
/*   6:    */ import org.apache.poi.ss.formula.WorkbookDependentFormula;
/*   7:    */ import org.apache.poi.ss.util.AreaReference;
/*   8:    */ import org.apache.poi.util.LittleEndianInput;
/*   9:    */ import org.apache.poi.util.LittleEndianOutput;
/*  10:    */ 
/*  11:    */ public final class Area3DPtg
/*  12:    */   extends AreaPtgBase
/*  13:    */   implements WorkbookDependentFormula, ExternSheetReferenceToken
/*  14:    */ {
/*  15:    */   public static final byte sid = 59;
/*  16:    */   private static final int SIZE = 11;
/*  17:    */   private int field_1_index_extern_sheet;
/*  18:    */   
/*  19:    */   public Area3DPtg(String arearef, int externIdx)
/*  20:    */   {
/*  21: 45 */     super(new AreaReference(arearef, SpreadsheetVersion.EXCEL97));
/*  22: 46 */     setExternSheetIndex(externIdx);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public Area3DPtg(LittleEndianInput in)
/*  26:    */   {
/*  27: 50 */     this.field_1_index_extern_sheet = in.readShort();
/*  28: 51 */     readCoordinates(in);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public Area3DPtg(int firstRow, int lastRow, int firstColumn, int lastColumn, boolean firstRowRelative, boolean lastRowRelative, boolean firstColRelative, boolean lastColRelative, int externalSheetIndex)
/*  32:    */   {
/*  33: 57 */     super(firstRow, lastRow, firstColumn, lastColumn, firstRowRelative, lastRowRelative, firstColRelative, lastColRelative);
/*  34: 58 */     setExternSheetIndex(externalSheetIndex);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public Area3DPtg(AreaReference arearef, int externIdx)
/*  38:    */   {
/*  39: 62 */     super(arearef);
/*  40: 63 */     setExternSheetIndex(externIdx);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public String toString()
/*  44:    */   {
/*  45: 68 */     StringBuffer sb = new StringBuffer();
/*  46: 69 */     sb.append(getClass().getName());
/*  47: 70 */     sb.append(" [");
/*  48: 71 */     sb.append("sheetIx=").append(getExternSheetIndex());
/*  49: 72 */     sb.append(" ! ");
/*  50: 73 */     sb.append(formatReferenceAsString());
/*  51: 74 */     sb.append("]");
/*  52: 75 */     return sb.toString();
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void write(LittleEndianOutput out)
/*  56:    */   {
/*  57: 80 */     out.writeByte(59 + getPtgClass());
/*  58: 81 */     out.writeShort(this.field_1_index_extern_sheet);
/*  59: 82 */     writeCoordinates(out);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public int getSize()
/*  63:    */   {
/*  64: 87 */     return 11;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public int getExternSheetIndex()
/*  68:    */   {
/*  69: 91 */     return this.field_1_index_extern_sheet;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setExternSheetIndex(int index)
/*  73:    */   {
/*  74: 95 */     this.field_1_index_extern_sheet = index;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String format2DRefAsString()
/*  78:    */   {
/*  79: 98 */     return formatReferenceAsString();
/*  80:    */   }
/*  81:    */   
/*  82:    */   public String toFormulaString(FormulaRenderingWorkbook book)
/*  83:    */   {
/*  84:105 */     return ExternSheetNameResolver.prependSheetName(book, this.field_1_index_extern_sheet, formatReferenceAsString());
/*  85:    */   }
/*  86:    */   
/*  87:    */   public String toFormulaString()
/*  88:    */   {
/*  89:109 */     throw new RuntimeException("3D references need a workbook to determine formula text");
/*  90:    */   }
/*  91:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.Area3DPtg
 * JD-Core Version:    0.7.0.1
 */