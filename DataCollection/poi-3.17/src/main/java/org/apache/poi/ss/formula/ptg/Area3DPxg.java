/*   1:    */ package org.apache.poi.ss.formula.ptg;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   4:    */ import org.apache.poi.ss.formula.NameIdentifier;
/*   5:    */ import org.apache.poi.ss.formula.SheetIdentifier;
/*   6:    */ import org.apache.poi.ss.formula.SheetNameFormatter;
/*   7:    */ import org.apache.poi.ss.formula.SheetRangeIdentifier;
/*   8:    */ import org.apache.poi.ss.util.AreaReference;
/*   9:    */ import org.apache.poi.util.LittleEndianOutput;
/*  10:    */ 
/*  11:    */ public final class Area3DPxg
/*  12:    */   extends AreaPtgBase
/*  13:    */   implements Pxg3D
/*  14:    */ {
/*  15: 36 */   private int externalWorkbookNumber = -1;
/*  16:    */   private String firstSheetName;
/*  17:    */   private String lastSheetName;
/*  18:    */   
/*  19:    */   public Area3DPxg(int externalWorkbookNumber, SheetIdentifier sheetName, String arearef)
/*  20:    */   {
/*  21: 41 */     this(externalWorkbookNumber, sheetName, new AreaReference(arearef, SpreadsheetVersion.EXCEL2007));
/*  22:    */   }
/*  23:    */   
/*  24:    */   public Area3DPxg(int externalWorkbookNumber, SheetIdentifier sheetName, AreaReference arearef)
/*  25:    */   {
/*  26: 44 */     super(arearef);
/*  27: 45 */     this.externalWorkbookNumber = externalWorkbookNumber;
/*  28: 46 */     this.firstSheetName = sheetName.getSheetIdentifier().getName();
/*  29: 47 */     if ((sheetName instanceof SheetRangeIdentifier)) {
/*  30: 48 */       this.lastSheetName = ((SheetRangeIdentifier)sheetName).getLastSheetIdentifier().getName();
/*  31:    */     } else {
/*  32: 50 */       this.lastSheetName = null;
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   public Area3DPxg(SheetIdentifier sheetName, String arearef)
/*  37:    */   {
/*  38: 55 */     this(sheetName, new AreaReference(arearef, SpreadsheetVersion.EXCEL2007));
/*  39:    */   }
/*  40:    */   
/*  41:    */   public Area3DPxg(SheetIdentifier sheetName, AreaReference arearef)
/*  42:    */   {
/*  43: 58 */     this(-1, sheetName, arearef);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public String toString()
/*  47:    */   {
/*  48: 63 */     StringBuffer sb = new StringBuffer();
/*  49: 64 */     sb.append(getClass().getName());
/*  50: 65 */     sb.append(" [");
/*  51: 66 */     if (this.externalWorkbookNumber >= 0)
/*  52:    */     {
/*  53: 67 */       sb.append(" [");
/*  54: 68 */       sb.append("workbook=").append(getExternalWorkbookNumber());
/*  55: 69 */       sb.append("] ");
/*  56:    */     }
/*  57: 71 */     sb.append("sheet=").append(getSheetName());
/*  58: 72 */     if (this.lastSheetName != null)
/*  59:    */     {
/*  60: 73 */       sb.append(" : ");
/*  61: 74 */       sb.append("sheet=").append(this.lastSheetName);
/*  62:    */     }
/*  63: 76 */     sb.append(" ! ");
/*  64: 77 */     sb.append(formatReferenceAsString());
/*  65: 78 */     sb.append("]");
/*  66: 79 */     return sb.toString();
/*  67:    */   }
/*  68:    */   
/*  69:    */   public int getExternalWorkbookNumber()
/*  70:    */   {
/*  71: 83 */     return this.externalWorkbookNumber;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public String getSheetName()
/*  75:    */   {
/*  76: 86 */     return this.firstSheetName;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String getLastSheetName()
/*  80:    */   {
/*  81: 89 */     return this.lastSheetName;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void setSheetName(String sheetName)
/*  85:    */   {
/*  86: 93 */     this.firstSheetName = sheetName;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setLastSheetName(String sheetName)
/*  90:    */   {
/*  91: 96 */     this.lastSheetName = sheetName;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public String format2DRefAsString()
/*  95:    */   {
/*  96:100 */     return formatReferenceAsString();
/*  97:    */   }
/*  98:    */   
/*  99:    */   public String toFormulaString()
/* 100:    */   {
/* 101:104 */     StringBuffer sb = new StringBuffer();
/* 102:105 */     if (this.externalWorkbookNumber >= 0)
/* 103:    */     {
/* 104:106 */       sb.append('[');
/* 105:107 */       sb.append(this.externalWorkbookNumber);
/* 106:108 */       sb.append(']');
/* 107:    */     }
/* 108:110 */     SheetNameFormatter.appendFormat(sb, this.firstSheetName);
/* 109:111 */     if (this.lastSheetName != null)
/* 110:    */     {
/* 111:112 */       sb.append(':');
/* 112:113 */       SheetNameFormatter.appendFormat(sb, this.lastSheetName);
/* 113:    */     }
/* 114:115 */     sb.append('!');
/* 115:116 */     sb.append(formatReferenceAsString());
/* 116:117 */     return sb.toString();
/* 117:    */   }
/* 118:    */   
/* 119:    */   public int getSize()
/* 120:    */   {
/* 121:121 */     return 1;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void write(LittleEndianOutput out)
/* 125:    */   {
/* 126:124 */     throw new IllegalStateException("XSSF-only Ptg, should not be serialised");
/* 127:    */   }
/* 128:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.Area3DPxg
 * JD-Core Version:    0.7.0.1
 */