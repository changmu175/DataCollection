/*   1:    */ package org.apache.poi.xssf.usermodel.helpers;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.ss.formula.FormulaParseException;
/*   7:    */ import org.apache.poi.ss.formula.FormulaParser;
/*   8:    */ import org.apache.poi.ss.formula.FormulaRenderer;
/*   9:    */ import org.apache.poi.ss.formula.FormulaShifter;
/*  10:    */ import org.apache.poi.ss.formula.FormulaType;
/*  11:    */ import org.apache.poi.ss.formula.ptg.AreaErrPtg;
/*  12:    */ import org.apache.poi.ss.formula.ptg.AreaPtg;
/*  13:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  14:    */ import org.apache.poi.ss.usermodel.Cell;
/*  15:    */ import org.apache.poi.ss.usermodel.Hyperlink;
/*  16:    */ import org.apache.poi.ss.usermodel.Name;
/*  17:    */ import org.apache.poi.ss.usermodel.Row;
/*  18:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  19:    */ import org.apache.poi.ss.usermodel.Workbook;
/*  20:    */ import org.apache.poi.ss.usermodel.helpers.RowShifter;
/*  21:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*  22:    */ import org.apache.poi.util.Internal;
/*  23:    */ import org.apache.poi.util.POILogFactory;
/*  24:    */ import org.apache.poi.util.POILogger;
/*  25:    */ import org.apache.poi.xssf.usermodel.XSSFCell;
/*  26:    */ import org.apache.poi.xssf.usermodel.XSSFEvaluationWorkbook;
/*  27:    */ import org.apache.poi.xssf.usermodel.XSSFHyperlink;
/*  28:    */ import org.apache.poi.xssf.usermodel.XSSFRow;
/*  29:    */ import org.apache.poi.xssf.usermodel.XSSFSheet;
/*  30:    */ import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/*  31:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCell;
/*  32:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellFormula;
/*  33:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule;
/*  34:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting;
/*  35:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
/*  36:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellFormulaType;
/*  37:    */ 
/*  38:    */ public final class XSSFRowShifter
/*  39:    */   extends RowShifter
/*  40:    */ {
/*  41: 61 */   private static final POILogger logger = POILogFactory.getLogger(XSSFRowShifter.class);
/*  42:    */   
/*  43:    */   public XSSFRowShifter(XSSFSheet sh)
/*  44:    */   {
/*  45: 64 */     super(sh);
/*  46:    */   }
/*  47:    */   
/*  48:    */   /**
/*  49:    */    * @deprecated
/*  50:    */    */
/*  51:    */   public List<CellRangeAddress> shiftMerged(int startRow, int endRow, int n)
/*  52:    */   {
/*  53: 77 */     return shiftMergedRegions(startRow, endRow, n);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void updateNamedRanges(FormulaShifter shifter)
/*  57:    */   {
/*  58: 84 */     Workbook wb = this.sheet.getWorkbook();
/*  59: 85 */     XSSFEvaluationWorkbook fpb = XSSFEvaluationWorkbook.create((XSSFWorkbook)wb);
/*  60: 86 */     for (Name name : wb.getAllNames())
/*  61:    */     {
/*  62: 87 */       String formula = name.getRefersToFormula();
/*  63: 88 */       int sheetIndex = name.getSheetIndex();
/*  64: 89 */       int rowIndex = -1;
/*  65:    */       
/*  66: 91 */       Ptg[] ptgs = FormulaParser.parse(formula, fpb, FormulaType.NAMEDRANGE, sheetIndex, -1);
/*  67: 92 */       if (shifter.adjustFormula(ptgs, sheetIndex))
/*  68:    */       {
/*  69: 93 */         String shiftedFmla = FormulaRenderer.toFormulaString(fpb, ptgs);
/*  70: 94 */         name.setRefersToFormula(shiftedFmla);
/*  71:    */       }
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void updateFormulas(FormulaShifter shifter)
/*  76:    */   {
/*  77:104 */     updateSheetFormulas(this.sheet, shifter);
/*  78:    */     
/*  79:    */ 
/*  80:107 */     Workbook wb = this.sheet.getWorkbook();
/*  81:108 */     for (Sheet sh : wb) {
/*  82:109 */       if (this.sheet != sh) {
/*  83:110 */         updateSheetFormulas(sh, shifter);
/*  84:    */       }
/*  85:    */     }
/*  86:    */   }
/*  87:    */   
/*  88:    */   private void updateSheetFormulas(Sheet sh, FormulaShifter shifter)
/*  89:    */   {
/*  90:115 */     for (Row r : sh)
/*  91:    */     {
/*  92:116 */       XSSFRow row = (XSSFRow)r;
/*  93:117 */       updateRowFormulas(row, shifter);
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   @Internal
/*  98:    */   public void updateRowFormulas(Row row, FormulaShifter shifter)
/*  99:    */   {
/* 100:129 */     XSSFSheet sheet = (XSSFSheet)row.getSheet();
/* 101:130 */     for (Cell c : row)
/* 102:    */     {
/* 103:131 */       XSSFCell cell = (XSSFCell)c;
/* 104:    */       
/* 105:133 */       CTCell ctCell = cell.getCTCell();
/* 106:134 */       if (ctCell.isSetF())
/* 107:    */       {
/* 108:135 */         CTCellFormula f = ctCell.getF();
/* 109:136 */         String formula = f.getStringValue();
/* 110:137 */         if (formula.length() > 0)
/* 111:    */         {
/* 112:138 */           String shiftedFormula = shiftFormula(row, formula, shifter);
/* 113:139 */           if (shiftedFormula != null)
/* 114:    */           {
/* 115:140 */             f.setStringValue(shiftedFormula);
/* 116:141 */             if (f.getT() == STCellFormulaType.SHARED)
/* 117:    */             {
/* 118:142 */               int si = (int)f.getSi();
/* 119:143 */               CTCellFormula sf = sheet.getSharedFormula(si);
/* 120:144 */               sf.setStringValue(shiftedFormula);
/* 121:145 */               updateRefInCTCellFormula(row, shifter, sf);
/* 122:    */             }
/* 123:    */           }
/* 124:    */         }
/* 125:152 */         updateRefInCTCellFormula(row, shifter, f);
/* 126:    */       }
/* 127:    */     }
/* 128:    */   }
/* 129:    */   
/* 130:    */   private void updateRefInCTCellFormula(Row row, FormulaShifter shifter, CTCellFormula f)
/* 131:    */   {
/* 132:159 */     if (f.isSetRef())
/* 133:    */     {
/* 134:160 */       String ref = f.getRef();
/* 135:161 */       String shiftedRef = shiftFormula(row, ref, shifter);
/* 136:162 */       if (shiftedRef != null) {
/* 137:162 */         f.setRef(shiftedRef);
/* 138:    */       }
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   private static String shiftFormula(Row row, String formula, FormulaShifter shifter)
/* 143:    */   {
/* 144:176 */     Sheet sheet = row.getSheet();
/* 145:177 */     Workbook wb = sheet.getWorkbook();
/* 146:178 */     int sheetIndex = wb.getSheetIndex(sheet);
/* 147:179 */     int rowIndex = row.getRowNum();
/* 148:180 */     XSSFEvaluationWorkbook fpb = XSSFEvaluationWorkbook.create((XSSFWorkbook)wb);
/* 149:    */     try
/* 150:    */     {
/* 151:183 */       Ptg[] ptgs = FormulaParser.parse(formula, fpb, FormulaType.CELL, sheetIndex, rowIndex);
/* 152:184 */       String shiftedFmla = null;
/* 153:185 */       if (shifter.adjustFormula(ptgs, sheetIndex)) {}
/* 154:186 */       return FormulaRenderer.toFormulaString(fpb, ptgs);
/* 155:    */     }
/* 156:    */     catch (FormulaParseException fpe)
/* 157:    */     {
/* 158:191 */       logger.log(5, new Object[] { "Error shifting formula on row ", Integer.valueOf(row.getRowNum()), fpe });
/* 159:    */     }
/* 160:192 */     return formula;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public void updateConditionalFormatting(FormulaShifter shifter)
/* 164:    */   {
/* 165:197 */     XSSFSheet xsheet = (XSSFSheet)this.sheet;
/* 166:198 */     XSSFWorkbook wb = xsheet.getWorkbook();
/* 167:199 */     int sheetIndex = wb.getSheetIndex(this.sheet);
/* 168:200 */     int rowIndex = -1;
/* 169:    */     
/* 170:202 */     XSSFEvaluationWorkbook fpb = XSSFEvaluationWorkbook.create(wb);
/* 171:203 */     CTWorksheet ctWorksheet = xsheet.getCTWorksheet();
/* 172:204 */     CTConditionalFormatting[] conditionalFormattingArray = ctWorksheet.getConditionalFormattingArray();
/* 173:206 */     for (int j = conditionalFormattingArray.length - 1; j >= 0; j--)
/* 174:    */     {
/* 175:207 */       CTConditionalFormatting cf = conditionalFormattingArray[j];
/* 176:    */       
/* 177:209 */       ArrayList<CellRangeAddress> cellRanges = new ArrayList();
/* 178:210 */       for (Object stRef : cf.getSqref())
/* 179:    */       {
/* 180:211 */         String[] regions = stRef.toString().split(" ");
/* 181:212 */         for (String region : regions) {
/* 182:213 */           cellRanges.add(CellRangeAddress.valueOf(region));
/* 183:    */         }
/* 184:    */       }
/* 185:217 */       boolean changed = false;
/* 186:218 */       List<CellRangeAddress> temp = new ArrayList();
/* 187:219 */       for (CellRangeAddress craOld : cellRanges)
/* 188:    */       {
/* 189:220 */         CellRangeAddress craNew = shiftRange(shifter, craOld, sheetIndex);
/* 190:221 */         if (craNew == null)
/* 191:    */         {
/* 192:222 */           changed = true;
/* 193:    */         }
/* 194:    */         else
/* 195:    */         {
/* 196:225 */           temp.add(craNew);
/* 197:226 */           if (craNew != craOld) {
/* 198:227 */             changed = true;
/* 199:    */           }
/* 200:    */         }
/* 201:    */       }
/* 202:231 */       if (changed)
/* 203:    */       {
/* 204:232 */         int nRanges = temp.size();
/* 205:233 */         if (nRanges == 0)
/* 206:    */         {
/* 207:234 */           ctWorksheet.removeConditionalFormatting(j);
/* 208:    */         }
/* 209:    */         else
/* 210:    */         {
/* 211:237 */           List<String> refs = new ArrayList();
/* 212:    */           CellRangeAddress a;
/* 213:238 */           for (Iterator i$ = temp.iterator(); i$.hasNext(); refs.add(a.formatAsString())) {
/* 214:238 */             a = (CellRangeAddress)i$.next();
/* 215:    */           }
/* 216:239 */           cf.setSqref(refs);
/* 217:    */         }
/* 218:    */       }
/* 219:    */       else
/* 220:    */       {
/* 221:242 */         for (CTCfRule cfRule : cf.getCfRuleArray())
/* 222:    */         {
/* 223:243 */           String[] formulaArray = cfRule.getFormulaArray();
/* 224:244 */           for (int i = 0; i < formulaArray.length; i++)
/* 225:    */           {
/* 226:245 */             String formula = formulaArray[i];
/* 227:246 */             Ptg[] ptgs = FormulaParser.parse(formula, fpb, FormulaType.CELL, sheetIndex, -1);
/* 228:247 */             if (shifter.adjustFormula(ptgs, sheetIndex))
/* 229:    */             {
/* 230:248 */               String shiftedFmla = FormulaRenderer.toFormulaString(fpb, ptgs);
/* 231:249 */               cfRule.setFormulaArray(i, shiftedFmla);
/* 232:    */             }
/* 233:    */           }
/* 234:    */         }
/* 235:    */       }
/* 236:    */     }
/* 237:    */   }
/* 238:    */   
/* 239:    */   public void updateHyperlinks(FormulaShifter shifter)
/* 240:    */   {
/* 241:264 */     int sheetIndex = this.sheet.getWorkbook().getSheetIndex(this.sheet);
/* 242:265 */     List<? extends Hyperlink> hyperlinkList = this.sheet.getHyperlinkList();
/* 243:267 */     for (Hyperlink hyperlink : hyperlinkList)
/* 244:    */     {
/* 245:268 */       XSSFHyperlink xhyperlink = (XSSFHyperlink)hyperlink;
/* 246:269 */       String cellRef = xhyperlink.getCellRef();
/* 247:270 */       CellRangeAddress cra = CellRangeAddress.valueOf(cellRef);
/* 248:271 */       CellRangeAddress shiftedRange = shiftRange(shifter, cra, sheetIndex);
/* 249:272 */       if ((shiftedRange != null) && (shiftedRange != cra)) {
/* 250:276 */         xhyperlink.setCellReference(shiftedRange.formatAsString());
/* 251:    */       }
/* 252:    */     }
/* 253:    */   }
/* 254:    */   
/* 255:    */   private static CellRangeAddress shiftRange(FormulaShifter shifter, CellRangeAddress cra, int currentExternSheetIx)
/* 256:    */   {
/* 257:283 */     AreaPtg aptg = new AreaPtg(cra.getFirstRow(), cra.getLastRow(), cra.getFirstColumn(), cra.getLastColumn(), false, false, false, false);
/* 258:284 */     Ptg[] ptgs = { aptg };
/* 259:286 */     if (!shifter.adjustFormula(ptgs, currentExternSheetIx)) {
/* 260:287 */       return cra;
/* 261:    */     }
/* 262:289 */     Ptg ptg0 = ptgs[0];
/* 263:290 */     if ((ptg0 instanceof AreaPtg))
/* 264:    */     {
/* 265:291 */       AreaPtg bptg = (AreaPtg)ptg0;
/* 266:292 */       return new CellRangeAddress(bptg.getFirstRow(), bptg.getLastRow(), bptg.getFirstColumn(), bptg.getLastColumn());
/* 267:    */     }
/* 268:294 */     if ((ptg0 instanceof AreaErrPtg)) {
/* 269:295 */       return null;
/* 270:    */     }
/* 271:297 */     throw new IllegalStateException("Unexpected shifted ptg class (" + ptg0.getClass().getName() + ")");
/* 272:    */   }
/* 273:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.helpers.XSSFRowShifter
 * JD-Core Version:    0.7.0.1
 */