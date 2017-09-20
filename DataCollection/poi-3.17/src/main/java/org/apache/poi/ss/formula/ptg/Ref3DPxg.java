/*   1:    */ package org.apache.poi.ss.formula.ptg;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.NameIdentifier;
/*   4:    */ import org.apache.poi.ss.formula.SheetIdentifier;
/*   5:    */ import org.apache.poi.ss.formula.SheetNameFormatter;
/*   6:    */ import org.apache.poi.ss.formula.SheetRangeIdentifier;
/*   7:    */ import org.apache.poi.ss.util.CellReference;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class Ref3DPxg
/*  11:    */   extends RefPtgBase
/*  12:    */   implements Pxg3D
/*  13:    */ {
/*  14: 35 */   private int externalWorkbookNumber = -1;
/*  15:    */   private String firstSheetName;
/*  16:    */   private String lastSheetName;
/*  17:    */   
/*  18:    */   public Ref3DPxg(int externalWorkbookNumber, SheetIdentifier sheetName, String cellref)
/*  19:    */   {
/*  20: 40 */     this(externalWorkbookNumber, sheetName, new CellReference(cellref));
/*  21:    */   }
/*  22:    */   
/*  23:    */   public Ref3DPxg(int externalWorkbookNumber, SheetIdentifier sheetName, CellReference c)
/*  24:    */   {
/*  25: 43 */     super(c);
/*  26: 44 */     this.externalWorkbookNumber = externalWorkbookNumber;
/*  27:    */     
/*  28: 46 */     this.firstSheetName = sheetName.getSheetIdentifier().getName();
/*  29: 47 */     if ((sheetName instanceof SheetRangeIdentifier)) {
/*  30: 48 */       this.lastSheetName = ((SheetRangeIdentifier)sheetName).getLastSheetIdentifier().getName();
/*  31:    */     } else {
/*  32: 50 */       this.lastSheetName = null;
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   public Ref3DPxg(SheetIdentifier sheetName, String cellref)
/*  37:    */   {
/*  38: 55 */     this(sheetName, new CellReference(cellref));
/*  39:    */   }
/*  40:    */   
/*  41:    */   public Ref3DPxg(SheetIdentifier sheetName, CellReference c)
/*  42:    */   {
/*  43: 58 */     this(-1, sheetName, c);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public String toString()
/*  47:    */   {
/*  48: 62 */     StringBuffer sb = new StringBuffer();
/*  49: 63 */     sb.append(getClass().getName());
/*  50: 64 */     sb.append(" [");
/*  51: 65 */     if (this.externalWorkbookNumber >= 0)
/*  52:    */     {
/*  53: 66 */       sb.append(" [");
/*  54: 67 */       sb.append("workbook=").append(getExternalWorkbookNumber());
/*  55: 68 */       sb.append("] ");
/*  56:    */     }
/*  57: 70 */     sb.append("sheet=").append(this.firstSheetName);
/*  58: 71 */     if (this.lastSheetName != null)
/*  59:    */     {
/*  60: 72 */       sb.append(" : ");
/*  61: 73 */       sb.append("sheet=").append(this.lastSheetName);
/*  62:    */     }
/*  63: 75 */     sb.append(" ! ");
/*  64: 76 */     sb.append(formatReferenceAsString());
/*  65: 77 */     sb.append("]");
/*  66: 78 */     return sb.toString();
/*  67:    */   }
/*  68:    */   
/*  69:    */   public int getExternalWorkbookNumber()
/*  70:    */   {
/*  71: 82 */     return this.externalWorkbookNumber;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public String getSheetName()
/*  75:    */   {
/*  76: 85 */     return this.firstSheetName;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String getLastSheetName()
/*  80:    */   {
/*  81: 88 */     return this.lastSheetName;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void setSheetName(String sheetName)
/*  85:    */   {
/*  86: 92 */     this.firstSheetName = sheetName;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setLastSheetName(String sheetName)
/*  90:    */   {
/*  91: 95 */     this.lastSheetName = sheetName;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public String format2DRefAsString()
/*  95:    */   {
/*  96: 99 */     return formatReferenceAsString();
/*  97:    */   }
/*  98:    */   
/*  99:    */   public String toFormulaString()
/* 100:    */   {
/* 101:103 */     StringBuffer sb = new StringBuffer();
/* 102:104 */     if (this.externalWorkbookNumber >= 0)
/* 103:    */     {
/* 104:105 */       sb.append('[');
/* 105:106 */       sb.append(this.externalWorkbookNumber);
/* 106:107 */       sb.append(']');
/* 107:    */     }
/* 108:109 */     if (this.firstSheetName != null) {
/* 109:110 */       SheetNameFormatter.appendFormat(sb, this.firstSheetName);
/* 110:    */     }
/* 111:112 */     if (this.lastSheetName != null)
/* 112:    */     {
/* 113:113 */       sb.append(':');
/* 114:114 */       SheetNameFormatter.appendFormat(sb, this.lastSheetName);
/* 115:    */     }
/* 116:116 */     sb.append('!');
/* 117:117 */     sb.append(formatReferenceAsString());
/* 118:118 */     return sb.toString();
/* 119:    */   }
/* 120:    */   
/* 121:    */   public int getSize()
/* 122:    */   {
/* 123:122 */     return 1;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void write(LittleEndianOutput out)
/* 127:    */   {
/* 128:125 */     throw new IllegalStateException("XSSF-only Ptg, should not be serialised");
/* 129:    */   }
/* 130:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.Ref3DPxg
 * JD-Core Version:    0.7.0.1
 */