/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import org.apache.poi.ss.usermodel.Cell;
/*   5:    */ import org.apache.poi.ss.usermodel.CellType;
/*   6:    */ import org.apache.poi.ss.usermodel.CellValue;
/*   7:    */ import org.apache.poi.ss.usermodel.CreationHelper;
/*   8:    */ import org.apache.poi.ss.usermodel.FormulaEvaluator;
/*   9:    */ import org.apache.poi.ss.usermodel.RichTextString;
/*  10:    */ import org.apache.poi.ss.usermodel.Row;
/*  11:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  12:    */ import org.apache.poi.ss.usermodel.Workbook;
/*  13:    */ 
/*  14:    */ public abstract class BaseFormulaEvaluator
/*  15:    */   implements FormulaEvaluator, WorkbookEvaluatorProvider
/*  16:    */ {
/*  17:    */   protected final WorkbookEvaluator _bookEvaluator;
/*  18:    */   
/*  19:    */   protected BaseFormulaEvaluator(WorkbookEvaluator bookEvaluator)
/*  20:    */   {
/*  21: 38 */     this._bookEvaluator = bookEvaluator;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public static void setupEnvironment(String[] workbookNames, BaseFormulaEvaluator[] evaluators)
/*  25:    */   {
/*  26: 49 */     WorkbookEvaluator[] wbEvals = new WorkbookEvaluator[evaluators.length];
/*  27: 50 */     for (int i = 0; i < wbEvals.length; i++) {
/*  28: 51 */       wbEvals[i] = evaluators[i]._bookEvaluator;
/*  29:    */     }
/*  30: 53 */     CollaboratingWorkbooksEnvironment.setup(workbookNames, wbEvals);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setupReferencedWorkbooks(Map<String, FormulaEvaluator> evaluators)
/*  34:    */   {
/*  35: 58 */     CollaboratingWorkbooksEnvironment.setupFormulaEvaluator(evaluators);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public WorkbookEvaluator _getWorkbookEvaluator()
/*  39:    */   {
/*  40: 63 */     return this._bookEvaluator;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void clearAllCachedResultValues()
/*  44:    */   {
/*  45: 76 */     this._bookEvaluator.clearAllCachedResultValues();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public CellValue evaluate(Cell cell)
/*  49:    */   {
/*  50: 91 */     if (cell == null) {
/*  51: 92 */       return null;
/*  52:    */     }
/*  53: 95 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cell.getCellTypeEnum().ordinal()])
/*  54:    */     {
/*  55:    */     case 1: 
/*  56: 97 */       return CellValue.valueOf(cell.getBooleanCellValue());
/*  57:    */     case 2: 
/*  58: 99 */       return CellValue.getError(cell.getErrorCellValue());
/*  59:    */     case 3: 
/*  60:101 */       return evaluateFormulaCellValue(cell);
/*  61:    */     case 4: 
/*  62:103 */       return new CellValue(cell.getNumericCellValue());
/*  63:    */     case 5: 
/*  64:105 */       return new CellValue(cell.getRichStringCellValue().getString());
/*  65:    */     case 6: 
/*  66:107 */       return null;
/*  67:    */     }
/*  68:109 */     throw new IllegalStateException("Bad cell type (" + cell.getCellTypeEnum() + ")");
/*  69:    */   }
/*  70:    */   
/*  71:    */   public Cell evaluateInCell(Cell cell)
/*  72:    */   {
/*  73:132 */     if (cell == null) {
/*  74:133 */       return null;
/*  75:    */     }
/*  76:135 */     Cell result = cell;
/*  77:136 */     if (cell.getCellTypeEnum() == CellType.FORMULA)
/*  78:    */     {
/*  79:137 */       CellValue cv = evaluateFormulaCellValue(cell);
/*  80:138 */       setCellValue(cell, cv);
/*  81:139 */       setCellType(cell, cv);
/*  82:    */     }
/*  83:141 */     return result;
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected abstract CellValue evaluateFormulaCellValue(Cell paramCell);
/*  87:    */   
/*  88:    */   /**
/*  89:    */    * @deprecated
/*  90:    */    */
/*  91:    */   public int evaluateFormulaCell(Cell cell)
/*  92:    */   {
/*  93:164 */     return evaluateFormulaCellEnum(cell).getCode();
/*  94:    */   }
/*  95:    */   
/*  96:    */   public CellType evaluateFormulaCellEnum(Cell cell)
/*  97:    */   {
/*  98:189 */     if ((cell == null) || (cell.getCellTypeEnum() != CellType.FORMULA)) {
/*  99:190 */       return CellType._NONE;
/* 100:    */     }
/* 101:192 */     CellValue cv = evaluateFormulaCellValue(cell);
/* 102:    */     
/* 103:194 */     setCellValue(cell, cv);
/* 104:195 */     return cv.getCellTypeEnum();
/* 105:    */   }
/* 106:    */   
/* 107:    */   protected static void setCellType(Cell cell, CellValue cv)
/* 108:    */   {
/* 109:199 */     CellType cellType = cv.getCellTypeEnum();
/* 110:200 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/* 111:    */     {
/* 112:    */     case 1: 
/* 113:    */     case 2: 
/* 114:    */     case 4: 
/* 115:    */     case 5: 
/* 116:205 */       cell.setCellType(cellType);
/* 117:206 */       return;
/* 118:    */     case 6: 
/* 119:209 */       throw new IllegalArgumentException("This should never happen. Blanks eventually get translated to zero.");
/* 120:    */     case 3: 
/* 121:212 */       throw new IllegalArgumentException("This should never happen. Formulas should have already been evaluated.");
/* 122:    */     }
/* 123:214 */     throw new IllegalStateException("Unexpected cell value type (" + cellType + ")");
/* 124:    */   }
/* 125:    */   
/* 126:    */   protected abstract RichTextString createRichTextString(String paramString);
/* 127:    */   
/* 128:    */   protected void setCellValue(Cell cell, CellValue cv)
/* 129:    */   {
/* 130:221 */     CellType cellType = cv.getCellTypeEnum();
/* 131:222 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/* 132:    */     {
/* 133:    */     case 1: 
/* 134:224 */       cell.setCellValue(cv.getBooleanValue());
/* 135:225 */       break;
/* 136:    */     case 2: 
/* 137:227 */       cell.setCellErrorValue(cv.getErrorValue());
/* 138:228 */       break;
/* 139:    */     case 4: 
/* 140:230 */       cell.setCellValue(cv.getNumberValue());
/* 141:231 */       break;
/* 142:    */     case 5: 
/* 143:233 */       cell.setCellValue(createRichTextString(cv.getStringValue()));
/* 144:234 */       break;
/* 145:    */     case 3: 
/* 146:    */     case 6: 
/* 147:    */     default: 
/* 148:240 */       throw new IllegalStateException("Unexpected cell value type (" + cellType + ")");
/* 149:    */     }
/* 150:    */   }
/* 151:    */   
/* 152:    */   public static void evaluateAllFormulaCells(Workbook wb)
/* 153:    */   {
/* 154:257 */     FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
/* 155:258 */     evaluateAllFormulaCells(wb, evaluator);
/* 156:    */   }
/* 157:    */   
/* 158:    */   protected static void evaluateAllFormulaCells(Workbook wb, FormulaEvaluator evaluator)
/* 159:    */   {
/* 160:261 */     for (int i = 0; i < wb.getNumberOfSheets(); i++)
/* 161:    */     {
/* 162:262 */       Sheet sheet = wb.getSheetAt(i);
/* 163:264 */       for (Row r : sheet) {
/* 164:265 */         for (Cell c : r) {
/* 165:266 */           if (c.getCellTypeEnum() == CellType.FORMULA) {
/* 166:267 */             evaluator.evaluateFormulaCellEnum(c);
/* 167:    */           }
/* 168:    */         }
/* 169:    */       }
/* 170:    */     }
/* 171:    */   }
/* 172:    */   
/* 173:    */   public void setIgnoreMissingWorkbooks(boolean ignore)
/* 174:    */   {
/* 175:277 */     this._bookEvaluator.setIgnoreMissingWorkbooks(ignore);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public void setDebugEvaluationOutputForNextEval(boolean value)
/* 179:    */   {
/* 180:283 */     this._bookEvaluator.setDebugEvaluationOutputForNextEval(value);
/* 181:    */   }
/* 182:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.BaseFormulaEvaluator
 * JD-Core Version:    0.7.0.1
 */