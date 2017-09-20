/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   4:    */ import org.apache.poi.ss.formula.ptg.NamePtg;
/*   5:    */ import org.apache.poi.ss.formula.ptg.NameXPtg;
/*   6:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   7:    */ import org.apache.poi.ss.formula.udf.UDFFinder;
/*   8:    */ import org.apache.poi.util.Internal;
/*   9:    */ 
/*  10:    */ @Internal
/*  11:    */ public abstract interface EvaluationWorkbook
/*  12:    */ {
/*  13:    */   public abstract String getSheetName(int paramInt);
/*  14:    */   
/*  15:    */   public abstract int getSheetIndex(EvaluationSheet paramEvaluationSheet);
/*  16:    */   
/*  17:    */   public abstract int getSheetIndex(String paramString);
/*  18:    */   
/*  19:    */   public abstract EvaluationSheet getSheet(int paramInt);
/*  20:    */   
/*  21:    */   public abstract ExternalSheet getExternalSheet(int paramInt);
/*  22:    */   
/*  23:    */   public abstract ExternalSheet getExternalSheet(String paramString1, String paramString2, int paramInt);
/*  24:    */   
/*  25:    */   public abstract int convertFromExternSheetIndex(int paramInt);
/*  26:    */   
/*  27:    */   public abstract ExternalName getExternalName(int paramInt1, int paramInt2);
/*  28:    */   
/*  29:    */   public abstract ExternalName getExternalName(String paramString1, String paramString2, int paramInt);
/*  30:    */   
/*  31:    */   public abstract EvaluationName getName(NamePtg paramNamePtg);
/*  32:    */   
/*  33:    */   public abstract EvaluationName getName(String paramString, int paramInt);
/*  34:    */   
/*  35:    */   public abstract String resolveNameXText(NameXPtg paramNameXPtg);
/*  36:    */   
/*  37:    */   public abstract Ptg[] getFormulaTokens(EvaluationCell paramEvaluationCell);
/*  38:    */   
/*  39:    */   public abstract UDFFinder getUDFFinder();
/*  40:    */   
/*  41:    */   public abstract SpreadsheetVersion getSpreadsheetVersion();
/*  42:    */   
/*  43:    */   public abstract void clearAllCachedResultValues();
/*  44:    */   
/*  45:    */   public static class ExternalSheet
/*  46:    */   {
/*  47:    */     private final String _workbookName;
/*  48:    */     private final String _sheetName;
/*  49:    */     
/*  50:    */     public ExternalSheet(String workbookName, String sheetName)
/*  51:    */     {
/*  52: 95 */       this._workbookName = workbookName;
/*  53: 96 */       this._sheetName = sheetName;
/*  54:    */     }
/*  55:    */     
/*  56:    */     public String getWorkbookName()
/*  57:    */     {
/*  58: 99 */       return this._workbookName;
/*  59:    */     }
/*  60:    */     
/*  61:    */     public String getSheetName()
/*  62:    */     {
/*  63:102 */       return this._sheetName;
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public static class ExternalSheetRange
/*  68:    */     extends ExternalSheet
/*  69:    */   {
/*  70:    */     private final String _lastSheetName;
/*  71:    */     
/*  72:    */     public ExternalSheetRange(String workbookName, String firstSheetName, String lastSheetName)
/*  73:    */     {
/*  74:108 */       super(firstSheetName);
/*  75:109 */       this._lastSheetName = lastSheetName;
/*  76:    */     }
/*  77:    */     
/*  78:    */     public String getFirstSheetName()
/*  79:    */     {
/*  80:113 */       return getSheetName();
/*  81:    */     }
/*  82:    */     
/*  83:    */     public String getLastSheetName()
/*  84:    */     {
/*  85:116 */       return this._lastSheetName;
/*  86:    */     }
/*  87:    */   }
/*  88:    */   
/*  89:    */   public static class ExternalName
/*  90:    */   {
/*  91:    */     private final String _nameName;
/*  92:    */     private final int _nameNumber;
/*  93:    */     private final int _ix;
/*  94:    */     
/*  95:    */     public ExternalName(String nameName, int nameNumber, int ix)
/*  96:    */     {
/*  97:125 */       this._nameName = nameName;
/*  98:126 */       this._nameNumber = nameNumber;
/*  99:127 */       this._ix = ix;
/* 100:    */     }
/* 101:    */     
/* 102:    */     public String getName()
/* 103:    */     {
/* 104:130 */       return this._nameName;
/* 105:    */     }
/* 106:    */     
/* 107:    */     public int getNumber()
/* 108:    */     {
/* 109:133 */       return this._nameNumber;
/* 110:    */     }
/* 111:    */     
/* 112:    */     public int getIx()
/* 113:    */     {
/* 114:136 */       return this._ix;
/* 115:    */     }
/* 116:    */   }
/* 117:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.formula.EvaluationWorkbook

 * JD-Core Version:    0.7.0.1

 */