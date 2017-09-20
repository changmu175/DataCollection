/*   1:    */ package org.apache.poi.ss.formula.eval.forked;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Set;
/*   6:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   7:    */ import org.apache.poi.ss.formula.EvaluationCell;
/*   8:    */ import org.apache.poi.ss.formula.EvaluationName;
/*   9:    */ import org.apache.poi.ss.formula.EvaluationSheet;
/*  10:    */ import org.apache.poi.ss.formula.EvaluationWorkbook;
/*  11:    */ import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalName;
/*  12:    */ import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalSheet;
/*  13:    */ import org.apache.poi.ss.formula.ptg.NamePtg;
/*  14:    */ import org.apache.poi.ss.formula.ptg.NameXPtg;
/*  15:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  16:    */ import org.apache.poi.ss.formula.udf.UDFFinder;
/*  17:    */ import org.apache.poi.ss.usermodel.Workbook;
/*  18:    */ import org.apache.poi.util.Internal;
/*  19:    */ 
/*  20:    */ @Internal
/*  21:    */ final class ForkedEvaluationWorkbook
/*  22:    */   implements EvaluationWorkbook
/*  23:    */ {
/*  24:    */   private final EvaluationWorkbook _masterBook;
/*  25:    */   private final Map<String, ForkedEvaluationSheet> _sharedSheetsByName;
/*  26:    */   
/*  27:    */   public ForkedEvaluationWorkbook(EvaluationWorkbook master)
/*  28:    */   {
/*  29: 49 */     this._masterBook = master;
/*  30: 50 */     this._sharedSheetsByName = new HashMap();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public ForkedEvaluationCell getOrCreateUpdatableCell(String sheetName, int rowIndex, int columnIndex)
/*  34:    */   {
/*  35: 55 */     ForkedEvaluationSheet sheet = getSharedSheet(sheetName);
/*  36: 56 */     return sheet.getOrCreateUpdatableCell(rowIndex, columnIndex);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public EvaluationCell getEvaluationCell(String sheetName, int rowIndex, int columnIndex)
/*  40:    */   {
/*  41: 60 */     ForkedEvaluationSheet sheet = getSharedSheet(sheetName);
/*  42: 61 */     return sheet.getCell(rowIndex, columnIndex);
/*  43:    */   }
/*  44:    */   
/*  45:    */   private ForkedEvaluationSheet getSharedSheet(String sheetName)
/*  46:    */   {
/*  47: 65 */     ForkedEvaluationSheet result = (ForkedEvaluationSheet)this._sharedSheetsByName.get(sheetName);
/*  48: 66 */     if (result == null)
/*  49:    */     {
/*  50: 67 */       result = new ForkedEvaluationSheet(this._masterBook.getSheet(this._masterBook.getSheetIndex(sheetName)));
/*  51:    */       
/*  52: 69 */       this._sharedSheetsByName.put(sheetName, result);
/*  53:    */     }
/*  54: 71 */     return result;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void copyUpdatedCells(Workbook workbook)
/*  58:    */   {
/*  59: 75 */     String[] sheetNames = new String[this._sharedSheetsByName.size()];
/*  60: 76 */     this._sharedSheetsByName.keySet().toArray(sheetNames);
/*  61: 77 */     for (String sheetName : sheetNames)
/*  62:    */     {
/*  63: 78 */       ForkedEvaluationSheet sheet = (ForkedEvaluationSheet)this._sharedSheetsByName.get(sheetName);
/*  64: 79 */       sheet.copyUpdatedCells(workbook.getSheet(sheetName));
/*  65:    */     }
/*  66:    */   }
/*  67:    */   
/*  68:    */   public int convertFromExternSheetIndex(int externSheetIndex)
/*  69:    */   {
/*  70: 85 */     return this._masterBook.convertFromExternSheetIndex(externSheetIndex);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public EvaluationWorkbook.ExternalSheet getExternalSheet(int externSheetIndex)
/*  74:    */   {
/*  75: 90 */     return this._masterBook.getExternalSheet(externSheetIndex);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public EvaluationWorkbook.ExternalSheet getExternalSheet(String firstSheetName, String lastSheetName, int externalWorkbookNumber)
/*  79:    */   {
/*  80: 94 */     return this._masterBook.getExternalSheet(firstSheetName, lastSheetName, externalWorkbookNumber);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public Ptg[] getFormulaTokens(EvaluationCell cell)
/*  84:    */   {
/*  85: 99 */     if ((cell instanceof ForkedEvaluationCell)) {
/*  86:101 */       throw new RuntimeException("Updated formulas not supported yet");
/*  87:    */     }
/*  88:103 */     return this._masterBook.getFormulaTokens(cell);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public EvaluationName getName(NamePtg namePtg)
/*  92:    */   {
/*  93:108 */     return this._masterBook.getName(namePtg);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public EvaluationName getName(String name, int sheetIndex)
/*  97:    */   {
/*  98:113 */     return this._masterBook.getName(name, sheetIndex);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public EvaluationSheet getSheet(int sheetIndex)
/* 102:    */   {
/* 103:118 */     return getSharedSheet(getSheetName(sheetIndex));
/* 104:    */   }
/* 105:    */   
/* 106:    */   public EvaluationWorkbook.ExternalName getExternalName(int externSheetIndex, int externNameIndex)
/* 107:    */   {
/* 108:123 */     return this._masterBook.getExternalName(externSheetIndex, externNameIndex);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public EvaluationWorkbook.ExternalName getExternalName(String nameName, String sheetName, int externalWorkbookNumber)
/* 112:    */   {
/* 113:127 */     return this._masterBook.getExternalName(nameName, sheetName, externalWorkbookNumber);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public int getSheetIndex(EvaluationSheet sheet)
/* 117:    */   {
/* 118:132 */     if ((sheet instanceof ForkedEvaluationSheet))
/* 119:    */     {
/* 120:133 */       ForkedEvaluationSheet mes = (ForkedEvaluationSheet)sheet;
/* 121:134 */       return mes.getSheetIndex(this._masterBook);
/* 122:    */     }
/* 123:136 */     return this._masterBook.getSheetIndex(sheet);
/* 124:    */   }
/* 125:    */   
/* 126:    */   public int getSheetIndex(String sheetName)
/* 127:    */   {
/* 128:141 */     return this._masterBook.getSheetIndex(sheetName);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public String getSheetName(int sheetIndex)
/* 132:    */   {
/* 133:146 */     return this._masterBook.getSheetName(sheetIndex);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public String resolveNameXText(NameXPtg ptg)
/* 137:    */   {
/* 138:151 */     return this._masterBook.resolveNameXText(ptg);
/* 139:    */   }
/* 140:    */   
/* 141:    */   public UDFFinder getUDFFinder()
/* 142:    */   {
/* 143:156 */     return this._masterBook.getUDFFinder();
/* 144:    */   }
/* 145:    */   
/* 146:    */   public SpreadsheetVersion getSpreadsheetVersion()
/* 147:    */   {
/* 148:160 */     return this._masterBook.getSpreadsheetVersion();
/* 149:    */   }
/* 150:    */   
/* 151:    */   public void clearAllCachedResultValues()
/* 152:    */   {
/* 153:171 */     this._masterBook.clearAllCachedResultValues();
/* 154:    */   }
/* 155:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.forked.ForkedEvaluationWorkbook
 * JD-Core Version:    0.7.0.1
 */