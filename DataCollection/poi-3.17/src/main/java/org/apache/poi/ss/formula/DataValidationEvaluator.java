/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*  10:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  11:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  12:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*  13:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  14:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  15:    */ import org.apache.poi.ss.usermodel.Cell;
/*  16:    */ import org.apache.poi.ss.usermodel.CellType;
/*  17:    */ import org.apache.poi.ss.usermodel.DataValidation;
/*  18:    */ import org.apache.poi.ss.usermodel.DataValidationConstraint;
/*  19:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  20:    */ import org.apache.poi.ss.usermodel.Workbook;
/*  21:    */ import org.apache.poi.ss.util.CellRangeAddressBase;
/*  22:    */ import org.apache.poi.ss.util.CellRangeAddressList;
/*  23:    */ import org.apache.poi.ss.util.CellReference;
/*  24:    */ import org.apache.poi.ss.util.SheetUtil;
/*  25:    */ 
/*  26:    */ public class DataValidationEvaluator
/*  27:    */ {
/*  28: 66 */   private final Map<String, List<? extends DataValidation>> validations = new HashMap();
/*  29:    */   private final Workbook workbook;
/*  30:    */   private final WorkbookEvaluator workbookEvaluator;
/*  31:    */   
/*  32:    */   public DataValidationEvaluator(Workbook wb, WorkbookEvaluatorProvider provider)
/*  33:    */   {
/*  34: 78 */     this.workbook = wb;
/*  35: 79 */     this.workbookEvaluator = provider._getWorkbookEvaluator();
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected WorkbookEvaluator getWorkbookEvaluator()
/*  39:    */   {
/*  40: 86 */     return this.workbookEvaluator;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void clearAllCachedValues()
/*  44:    */   {
/*  45: 94 */     this.validations.clear();
/*  46:    */   }
/*  47:    */   
/*  48:    */   private List<? extends DataValidation> getValidations(Sheet sheet)
/*  49:    */   {
/*  50:103 */     List<? extends DataValidation> dvs = (List)this.validations.get(sheet.getSheetName());
/*  51:104 */     if ((dvs == null) && (!this.validations.containsKey(sheet.getSheetName())))
/*  52:    */     {
/*  53:105 */       dvs = sheet.getDataValidations();
/*  54:106 */       this.validations.put(sheet.getSheetName(), dvs);
/*  55:    */     }
/*  56:108 */     return dvs;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public DataValidation getValidationForCell(CellReference cell)
/*  60:    */   {
/*  61:123 */     DataValidationContext vc = getValidationContextForCell(cell);
/*  62:124 */     return vc == null ? null : vc.getValidation();
/*  63:    */   }
/*  64:    */   
/*  65:    */   public DataValidationContext getValidationContextForCell(CellReference cell)
/*  66:    */   {
/*  67:139 */     Sheet sheet = this.workbook.getSheet(cell.getSheetName());
/*  68:140 */     if (sheet == null) {
/*  69:140 */       return null;
/*  70:    */     }
/*  71:141 */     List<? extends DataValidation> dataValidations = getValidations(sheet);
/*  72:142 */     if (dataValidations == null) {
/*  73:142 */       return null;
/*  74:    */     }
/*  75:143 */     for (DataValidation dv : dataValidations)
/*  76:    */     {
/*  77:144 */       CellRangeAddressList regions = dv.getRegions();
/*  78:145 */       if (regions == null) {
/*  79:145 */         return null;
/*  80:    */       }
/*  81:147 */       for (CellRangeAddressBase range : regions.getCellRangeAddresses()) {
/*  82:148 */         if (range.isInRange(cell)) {
/*  83:149 */           return new DataValidationContext(dv, this, range, cell);
/*  84:    */         }
/*  85:    */       }
/*  86:    */     }
/*  87:153 */     return null;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public List<ValueEval> getValidationValuesForCell(CellReference cell)
/*  91:    */   {
/*  92:173 */     DataValidationContext context = getValidationContextForCell(cell);
/*  93:175 */     if (context == null) {
/*  94:175 */       return null;
/*  95:    */     }
/*  96:177 */     return getValidationValuesForConstraint(context);
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected static List<ValueEval> getValidationValuesForConstraint(DataValidationContext context)
/* 100:    */   {
/* 101:185 */     DataValidationConstraint val = context.getValidation().getValidationConstraint();
/* 102:186 */     if (val.getValidationType() != 3) {
/* 103:186 */       return null;
/* 104:    */     }
/* 105:188 */     String formula = val.getFormula1();
/* 106:    */     
/* 107:190 */     List<ValueEval> values = new ArrayList();
/* 108:192 */     if ((val.getExplicitListValues() != null) && (val.getExplicitListValues().length > 0))
/* 109:    */     {
/* 110:194 */       for (String s : val.getExplicitListValues()) {
/* 111:195 */         if (s != null) {
/* 112:195 */           values.add(new StringEval(s));
/* 113:    */         }
/* 114:    */       }
/* 115:    */     }
/* 116:197 */     else if (formula != null)
/* 117:    */     {
/* 118:200 */       ValueEval eval = context.getEvaluator().getWorkbookEvaluator().evaluateList(formula, context.getTarget(), context.getRegion());
/* 119:203 */       if ((eval instanceof TwoDEval))
/* 120:    */       {
/* 121:204 */         TwoDEval twod = (TwoDEval)eval;
/* 122:205 */         for (int i = 0; i < twod.getHeight(); i++)
/* 123:    */         {
/* 124:206 */           ValueEval cellValue = twod.getValue(i, 0);
/* 125:207 */           values.add(cellValue);
/* 126:    */         }
/* 127:    */       }
/* 128:    */     }
/* 129:211 */     return Collections.unmodifiableList(values);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public boolean isValidCell(CellReference cellRef)
/* 133:    */   {
/* 134:228 */     DataValidationContext context = getValidationContextForCell(cellRef);
/* 135:230 */     if (context == null) {
/* 136:230 */       return true;
/* 137:    */     }
/* 138:232 */     Cell cell = SheetUtil.getCell(this.workbook.getSheet(cellRef.getSheetName()), cellRef.getRow(), cellRef.getCol());
/* 139:237 */     if ((cell == null) || (isType(cell, CellType.BLANK)) || ((isType(cell, CellType.STRING)) && ((cell.getStringCellValue() == null) || (cell.getStringCellValue().isEmpty())))) {
/* 140:243 */       return context.getValidation().getEmptyCellAllowed();
/* 141:    */     }
/* 142:248 */     return ValidationEnum.isValid(cell, context);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public static boolean isType(Cell cell, CellType type)
/* 146:    */   {
/* 147:258 */     CellType cellType = cell.getCellTypeEnum();
/* 148:259 */     return (cellType == type) || ((cellType == CellType.FORMULA) && (cell.getCachedFormulaResultTypeEnum() == type));
/* 149:    */   }
/* 150:    */   
/* 151:    */   public static enum ValidationEnum
/* 152:    */   {
/* 153:271 */     ANY,  INTEGER,  DECIMAL,  LIST,  DATE,  TIME,  TEXT_LENGTH,  FORMULA;
/* 154:    */     
/* 155:    */     private ValidationEnum() {}
/* 156:    */     
/* 157:    */     public boolean isValidValue(Cell cell, DataValidationContext context)
/* 158:    */     {
/* 159:370 */       return isValidNumericCell(cell, context);
/* 160:    */     }
/* 161:    */     
/* 162:    */     protected boolean isValidNumericCell(Cell cell, DataValidationContext context)
/* 163:    */     {
/* 164:381 */       if (!DataValidationEvaluator.isType(cell, CellType.NUMERIC)) {
/* 165:381 */         return false;
/* 166:    */       }
/* 167:383 */       Double value = Double.valueOf(cell.getNumericCellValue());
/* 168:384 */       return isValidNumericValue(value, context);
/* 169:    */     }
/* 170:    */     
/* 171:    */     protected boolean isValidNumericValue(Double value, DataValidationContext context)
/* 172:    */     {
/* 173:    */       try
/* 174:    */       {
/* 175:392 */         Double t1 = evalOrConstant(context.getFormula1(), context);
/* 176:394 */         if (t1 == null) {
/* 177:394 */           return true;
/* 178:    */         }
/* 179:395 */         Double t2 = null;
/* 180:396 */         if ((context.getOperator() == 0) || (context.getOperator() == 1))
/* 181:    */         {
/* 182:397 */           t2 = evalOrConstant(context.getFormula2(), context);
/* 183:399 */           if (t2 == null) {
/* 184:399 */             return true;
/* 185:    */           }
/* 186:    */         }
/* 187:401 */         return OperatorEnum.values()[context.getOperator()].isValid(value, t1, t2);
/* 188:    */       }
/* 189:    */       catch (NumberFormatException e) {}
/* 190:404 */       return false;
/* 191:    */     }
/* 192:    */     
/* 193:    */     private Double evalOrConstant(String formula, DataValidationContext context)
/* 194:    */       throws NumberFormatException
/* 195:    */     {
/* 196:418 */       if ((formula == null) || (formula.trim().isEmpty())) {
/* 197:418 */         return null;
/* 198:    */       }
/* 199:    */       try
/* 200:    */       {
/* 201:420 */         return Double.valueOf(formula);
/* 202:    */       }
/* 203:    */       catch (NumberFormatException e)
/* 204:    */       {
/* 205:425 */         ValueEval eval = context.getEvaluator().getWorkbookEvaluator().evaluate(formula, context.getTarget(), context.getRegion());
/* 206:426 */         if ((eval instanceof RefEval)) {
/* 207:427 */           eval = ((RefEval)eval).getInnerValueEval(((RefEval)eval).getFirstSheetIndex());
/* 208:    */         }
/* 209:429 */         if ((eval instanceof BlankEval)) {
/* 210:429 */           return null;
/* 211:    */         }
/* 212:430 */         if ((eval instanceof NumberEval)) {
/* 213:430 */           return Double.valueOf(((NumberEval)eval).getNumberValue());
/* 214:    */         }
/* 215:431 */         if ((eval instanceof StringEval))
/* 216:    */         {
/* 217:432 */           String value = ((StringEval)eval).getStringValue();
/* 218:433 */           if ((value == null) || (value.trim().isEmpty())) {
/* 219:433 */             return null;
/* 220:    */           }
/* 221:435 */           return Double.valueOf(value);
/* 222:    */         }
/* 223:437 */         throw new NumberFormatException("Formula '" + formula + "' evaluates to something other than a number");
/* 224:    */       }
/* 225:    */     }
/* 226:    */     
/* 227:    */     public static boolean isValid(Cell cell, DataValidationContext context)
/* 228:    */     {
/* 229:448 */       return values()[context.getValidation().getValidationConstraint().getValidationType()].isValidValue(cell, context);
/* 230:    */     }
/* 231:    */   }
/* 232:    */   
/* 233:    */   public static abstract enum OperatorEnum
/* 234:    */   {
/* 235:458 */     BETWEEN,  NOT_BETWEEN,  EQUAL,  NOT_EQUAL,  GREATER_THAN,  LESS_THAN,  GREATER_OR_EQUAL,  LESS_OR_EQUAL;
/* 236:    */     
/* 237:500 */     public static final OperatorEnum IGNORED = BETWEEN;
/* 238:    */     
/* 239:    */     private OperatorEnum() {}
/* 240:    */     
/* 241:    */     public abstract boolean isValid(Double paramDouble1, Double paramDouble2, Double paramDouble3);
/* 242:    */   }
/* 243:    */   
/* 244:    */   public static class DataValidationContext
/* 245:    */   {
/* 246:    */     private final DataValidation dv;
/* 247:    */     private final DataValidationEvaluator dve;
/* 248:    */     private final CellRangeAddressBase region;
/* 249:    */     private final CellReference target;
/* 250:    */     
/* 251:    */     public DataValidationContext(DataValidation dv, DataValidationEvaluator dve, CellRangeAddressBase region, CellReference target)
/* 252:    */     {
/* 253:532 */       this.dv = dv;
/* 254:533 */       this.dve = dve;
/* 255:534 */       this.region = region;
/* 256:535 */       this.target = target;
/* 257:    */     }
/* 258:    */     
/* 259:    */     public DataValidation getValidation()
/* 260:    */     {
/* 261:541 */       return this.dv;
/* 262:    */     }
/* 263:    */     
/* 264:    */     public DataValidationEvaluator getEvaluator()
/* 265:    */     {
/* 266:547 */       return this.dve;
/* 267:    */     }
/* 268:    */     
/* 269:    */     public CellRangeAddressBase getRegion()
/* 270:    */     {
/* 271:553 */       return this.region;
/* 272:    */     }
/* 273:    */     
/* 274:    */     public CellReference getTarget()
/* 275:    */     {
/* 276:559 */       return this.target;
/* 277:    */     }
/* 278:    */     
/* 279:    */     public int getOffsetColumns()
/* 280:    */     {
/* 281:563 */       return this.target.getCol() - this.region.getFirstColumn();
/* 282:    */     }
/* 283:    */     
/* 284:    */     public int getOffsetRows()
/* 285:    */     {
/* 286:567 */       return this.target.getRow() - this.region.getFirstRow();
/* 287:    */     }
/* 288:    */     
/* 289:    */     public int getSheetIndex()
/* 290:    */     {
/* 291:571 */       return this.dve.getWorkbookEvaluator().getSheetIndex(this.target.getSheetName());
/* 292:    */     }
/* 293:    */     
/* 294:    */     public String getFormula1()
/* 295:    */     {
/* 296:575 */       return this.dv.getValidationConstraint().getFormula1();
/* 297:    */     }
/* 298:    */     
/* 299:    */     public String getFormula2()
/* 300:    */     {
/* 301:579 */       return this.dv.getValidationConstraint().getFormula2();
/* 302:    */     }
/* 303:    */     
/* 304:    */     public int getOperator()
/* 305:    */     {
/* 306:583 */       return this.dv.getValidationConstraint().getOperator();
/* 307:    */     }
/* 308:    */   }
/* 309:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.formula.DataValidationEvaluator

 * JD-Core Version:    0.7.0.1

 */