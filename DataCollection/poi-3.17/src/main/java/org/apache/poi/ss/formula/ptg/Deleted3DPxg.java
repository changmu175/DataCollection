/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.SheetNameFormatter;
/*  4:   */ import org.apache.poi.ss.usermodel.FormulaError;
/*  5:   */ import org.apache.poi.util.LittleEndianOutput;
/*  6:   */ 
/*  7:   */ public final class Deleted3DPxg
/*  8:   */   extends OperandPtg
/*  9:   */   implements Pxg
/* 10:   */ {
/* 11:29 */   private int externalWorkbookNumber = -1;
/* 12:   */   private String sheetName;
/* 13:   */   
/* 14:   */   public Deleted3DPxg(int externalWorkbookNumber, String sheetName)
/* 15:   */   {
/* 16:33 */     this.externalWorkbookNumber = externalWorkbookNumber;
/* 17:34 */     this.sheetName = sheetName;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public Deleted3DPxg(String sheetName)
/* 21:   */   {
/* 22:37 */     this(-1, sheetName);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String toString()
/* 26:   */   {
/* 27:41 */     StringBuffer sb = new StringBuffer();
/* 28:42 */     sb.append(getClass().getName());
/* 29:43 */     sb.append(" [");
/* 30:44 */     if (this.externalWorkbookNumber >= 0)
/* 31:   */     {
/* 32:45 */       sb.append(" [");
/* 33:46 */       sb.append("workbook=").append(getExternalWorkbookNumber());
/* 34:47 */       sb.append("] ");
/* 35:   */     }
/* 36:49 */     sb.append("sheet=").append(getSheetName());
/* 37:50 */     sb.append(" ! ");
/* 38:51 */     sb.append(FormulaError.REF.getString());
/* 39:52 */     sb.append("]");
/* 40:53 */     return sb.toString();
/* 41:   */   }
/* 42:   */   
/* 43:   */   public int getExternalWorkbookNumber()
/* 44:   */   {
/* 45:57 */     return this.externalWorkbookNumber;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public String getSheetName()
/* 49:   */   {
/* 50:60 */     return this.sheetName;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public void setSheetName(String sheetName)
/* 54:   */   {
/* 55:64 */     this.sheetName = sheetName;
/* 56:   */   }
/* 57:   */   
/* 58:   */   public String toFormulaString()
/* 59:   */   {
/* 60:68 */     StringBuffer sb = new StringBuffer();
/* 61:69 */     if (this.externalWorkbookNumber >= 0)
/* 62:   */     {
/* 63:70 */       sb.append('[');
/* 64:71 */       sb.append(this.externalWorkbookNumber);
/* 65:72 */       sb.append(']');
/* 66:   */     }
/* 67:74 */     if (this.sheetName != null) {
/* 68:75 */       SheetNameFormatter.appendFormat(sb, this.sheetName);
/* 69:   */     }
/* 70:77 */     sb.append('!');
/* 71:78 */     sb.append(FormulaError.REF.getString());
/* 72:79 */     return sb.toString();
/* 73:   */   }
/* 74:   */   
/* 75:   */   public byte getDefaultOperandClass()
/* 76:   */   {
/* 77:83 */     return 32;
/* 78:   */   }
/* 79:   */   
/* 80:   */   public int getSize()
/* 81:   */   {
/* 82:87 */     return 1;
/* 83:   */   }
/* 84:   */   
/* 85:   */   public void write(LittleEndianOutput out)
/* 86:   */   {
/* 87:90 */     throw new IllegalStateException("XSSF-only Ptg, should not be serialised");
/* 88:   */   }
/* 89:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.Deleted3DPxg
 * JD-Core Version:    0.7.0.1
 */