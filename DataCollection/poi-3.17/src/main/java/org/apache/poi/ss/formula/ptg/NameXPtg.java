/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
/*  4:   */ import org.apache.poi.ss.formula.WorkbookDependentFormula;
/*  5:   */ import org.apache.poi.util.LittleEndianInput;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class NameXPtg
/*  9:   */   extends OperandPtg
/* 10:   */   implements WorkbookDependentFormula
/* 11:   */ {
/* 12:   */   public static final short sid = 57;
/* 13:   */   private static final int SIZE = 7;
/* 14:   */   private final int _sheetRefIndex;
/* 15:   */   private final int _nameNumber;
/* 16:   */   private final int _reserved;
/* 17:   */   
/* 18:   */   private NameXPtg(int sheetRefIndex, int nameNumber, int reserved)
/* 19:   */   {
/* 20:45 */     this._sheetRefIndex = sheetRefIndex;
/* 21:46 */     this._nameNumber = nameNumber;
/* 22:47 */     this._reserved = reserved;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public NameXPtg(int sheetRefIndex, int nameIndex)
/* 26:   */   {
/* 27:55 */     this(sheetRefIndex, nameIndex + 1, 0);
/* 28:   */   }
/* 29:   */   
/* 30:   */   public NameXPtg(LittleEndianInput in)
/* 31:   */   {
/* 32:59 */     this(in.readUShort(), in.readUShort(), in.readUShort());
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void write(LittleEndianOutput out)
/* 36:   */   {
/* 37:63 */     out.writeByte(57 + getPtgClass());
/* 38:64 */     out.writeShort(this._sheetRefIndex);
/* 39:65 */     out.writeShort(this._nameNumber);
/* 40:66 */     out.writeShort(this._reserved);
/* 41:   */   }
/* 42:   */   
/* 43:   */   public int getSize()
/* 44:   */   {
/* 45:70 */     return 7;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public String toFormulaString(FormulaRenderingWorkbook book)
/* 49:   */   {
/* 50:75 */     return book.resolveNameXText(this);
/* 51:   */   }
/* 52:   */   
/* 53:   */   public String toFormulaString()
/* 54:   */   {
/* 55:78 */     throw new RuntimeException("3D references need a workbook to determine formula text");
/* 56:   */   }
/* 57:   */   
/* 58:   */   public String toString()
/* 59:   */   {
/* 60:82 */     String retValue = "NameXPtg:[sheetRefIndex:" + this._sheetRefIndex + " , nameNumber:" + this._nameNumber + "]";
/* 61:   */     
/* 62:84 */     return retValue;
/* 63:   */   }
/* 64:   */   
/* 65:   */   public byte getDefaultOperandClass()
/* 66:   */   {
/* 67:88 */     return 32;
/* 68:   */   }
/* 69:   */   
/* 70:   */   public int getSheetRefIndex()
/* 71:   */   {
/* 72:92 */     return this._sheetRefIndex;
/* 73:   */   }
/* 74:   */   
/* 75:   */   public int getNameIndex()
/* 76:   */   {
/* 77:95 */     return this._nameNumber - 1;
/* 78:   */   }
/* 79:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.NameXPtg
 * JD-Core Version:    0.7.0.1
 */