/*   1:    */ package org.apache.poi.ss.formula.ptg;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.SheetNameFormatter;
/*   4:    */ import org.apache.poi.util.LittleEndianOutput;
/*   5:    */ 
/*   6:    */ public final class NameXPxg
/*   7:    */   extends OperandPtg
/*   8:    */   implements Pxg
/*   9:    */ {
/*  10: 31 */   private int externalWorkbookNumber = -1;
/*  11:    */   private String sheetName;
/*  12:    */   private String nameName;
/*  13:    */   
/*  14:    */   public NameXPxg(int externalWorkbookNumber, String sheetName, String nameName)
/*  15:    */   {
/*  16: 36 */     this.externalWorkbookNumber = externalWorkbookNumber;
/*  17: 37 */     this.sheetName = sheetName;
/*  18: 38 */     this.nameName = nameName;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public NameXPxg(String sheetName, String nameName)
/*  22:    */   {
/*  23: 41 */     this(-1, sheetName, nameName);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public NameXPxg(String nameName)
/*  27:    */   {
/*  28: 44 */     this(-1, null, nameName);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public String toString()
/*  32:    */   {
/*  33: 48 */     StringBuffer sb = new StringBuffer();
/*  34: 49 */     sb.append(getClass().getName());
/*  35: 50 */     sb.append(" [");
/*  36: 51 */     if (this.externalWorkbookNumber >= 0)
/*  37:    */     {
/*  38: 52 */       sb.append(" [");
/*  39: 53 */       sb.append("workbook=").append(getExternalWorkbookNumber());
/*  40: 54 */       sb.append("] ");
/*  41:    */     }
/*  42: 56 */     sb.append("sheet=").append(getSheetName());
/*  43: 57 */     sb.append(" ! ");
/*  44: 58 */     sb.append("name=");
/*  45: 59 */     sb.append(this.nameName);
/*  46: 60 */     sb.append("]");
/*  47: 61 */     return sb.toString();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public int getExternalWorkbookNumber()
/*  51:    */   {
/*  52: 65 */     return this.externalWorkbookNumber;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public String getSheetName()
/*  56:    */   {
/*  57: 68 */     return this.sheetName;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public String getNameName()
/*  61:    */   {
/*  62: 71 */     return this.nameName;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setSheetName(String sheetName)
/*  66:    */   {
/*  67: 75 */     this.sheetName = sheetName;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public String toFormulaString()
/*  71:    */   {
/*  72: 79 */     StringBuffer sb = new StringBuffer();
/*  73: 80 */     boolean needsExclamation = false;
/*  74: 81 */     if (this.externalWorkbookNumber >= 0)
/*  75:    */     {
/*  76: 82 */       sb.append('[');
/*  77: 83 */       sb.append(this.externalWorkbookNumber);
/*  78: 84 */       sb.append(']');
/*  79: 85 */       needsExclamation = true;
/*  80:    */     }
/*  81: 87 */     if (this.sheetName != null)
/*  82:    */     {
/*  83: 88 */       SheetNameFormatter.appendFormat(sb, this.sheetName);
/*  84: 89 */       needsExclamation = true;
/*  85:    */     }
/*  86: 91 */     if (needsExclamation) {
/*  87: 92 */       sb.append('!');
/*  88:    */     }
/*  89: 94 */     sb.append(this.nameName);
/*  90: 95 */     return sb.toString();
/*  91:    */   }
/*  92:    */   
/*  93:    */   public byte getDefaultOperandClass()
/*  94:    */   {
/*  95: 99 */     return 32;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public int getSize()
/*  99:    */   {
/* 100:103 */     return 1;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void write(LittleEndianOutput out)
/* 104:    */   {
/* 105:106 */     throw new IllegalStateException("XSSF-only Ptg, should not be serialised");
/* 106:    */   }
/* 107:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.NameXPxg
 * JD-Core Version:    0.7.0.1
 */