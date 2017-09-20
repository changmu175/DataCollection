/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.model.InternalWorkbook;
/*   4:    */ import org.apache.poi.hssf.record.NameRecord;
/*   5:    */ import org.apache.poi.hssf.record.aggregates.FormulaRecordAggregate;
/*   6:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   7:    */ import org.apache.poi.ss.formula.EvaluationCell;
/*   8:    */ import org.apache.poi.ss.formula.EvaluationName;
/*   9:    */ import org.apache.poi.ss.formula.EvaluationSheet;
/*  10:    */ import org.apache.poi.ss.formula.EvaluationWorkbook;
/*  11:    */ import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalName;
/*  12:    */ import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalSheet;
/*  13:    */ import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalSheetRange;
/*  14:    */ import org.apache.poi.ss.formula.FormulaParsingWorkbook;
/*  15:    */ import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
/*  16:    */ import org.apache.poi.ss.formula.NameIdentifier;
/*  17:    */ import org.apache.poi.ss.formula.SheetIdentifier;
/*  18:    */ import org.apache.poi.ss.formula.SheetRangeIdentifier;
/*  19:    */ import org.apache.poi.ss.formula.ptg.Area3DPtg;
/*  20:    */ import org.apache.poi.ss.formula.ptg.NamePtg;
/*  21:    */ import org.apache.poi.ss.formula.ptg.NameXPtg;
/*  22:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  23:    */ import org.apache.poi.ss.formula.ptg.Ref3DPtg;
/*  24:    */ import org.apache.poi.ss.formula.udf.UDFFinder;
/*  25:    */ import org.apache.poi.ss.usermodel.Table;
/*  26:    */ import org.apache.poi.ss.util.AreaReference;
/*  27:    */ import org.apache.poi.ss.util.CellReference;
/*  28:    */ import org.apache.poi.util.Internal;
/*  29:    */ 
/*  30:    */ @Internal
/*  31:    */ public final class HSSFEvaluationWorkbook
/*  32:    */   implements FormulaRenderingWorkbook, EvaluationWorkbook, FormulaParsingWorkbook
/*  33:    */ {
/*  34:    */   private final HSSFWorkbook _uBook;
/*  35:    */   private final InternalWorkbook _iBook;
/*  36:    */   
/*  37:    */   public static HSSFEvaluationWorkbook create(HSSFWorkbook book)
/*  38:    */   {
/*  39: 52 */     if (book == null) {
/*  40: 53 */       return null;
/*  41:    */     }
/*  42: 55 */     return new HSSFEvaluationWorkbook(book);
/*  43:    */   }
/*  44:    */   
/*  45:    */   private HSSFEvaluationWorkbook(HSSFWorkbook book)
/*  46:    */   {
/*  47: 59 */     this._uBook = book;
/*  48: 60 */     this._iBook = book.getWorkbook();
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void clearAllCachedResultValues() {}
/*  52:    */   
/*  53:    */   public HSSFName createName()
/*  54:    */   {
/*  55: 73 */     return this._uBook.createName();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public int getExternalSheetIndex(String sheetName)
/*  59:    */   {
/*  60: 78 */     int sheetIndex = this._uBook.getSheetIndex(sheetName);
/*  61: 79 */     return this._iBook.checkExternSheet(sheetIndex);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public int getExternalSheetIndex(String workbookName, String sheetName)
/*  65:    */   {
/*  66: 83 */     return this._iBook.getExternalSheetIndex(workbookName, sheetName);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public Ptg get3DReferencePtg(CellReference cr, SheetIdentifier sheet)
/*  70:    */   {
/*  71: 88 */     int extIx = getSheetExtIx(sheet);
/*  72: 89 */     return new Ref3DPtg(cr, extIx);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public Ptg get3DReferencePtg(AreaReference areaRef, SheetIdentifier sheet)
/*  76:    */   {
/*  77: 93 */     int extIx = getSheetExtIx(sheet);
/*  78: 94 */     return new Area3DPtg(areaRef, extIx);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public NameXPtg getNameXPtg(String name, SheetIdentifier sheet)
/*  82:    */   {
/*  83:101 */     int sheetRefIndex = getSheetExtIx(sheet);
/*  84:102 */     return this._iBook.getNameXPtg(name, sheetRefIndex, this._uBook.getUDFFinder());
/*  85:    */   }
/*  86:    */   
/*  87:    */   public EvaluationName getName(String name, int sheetIndex)
/*  88:    */   {
/*  89:114 */     for (int i = 0; i < this._iBook.getNumNames(); i++)
/*  90:    */     {
/*  91:115 */       NameRecord nr = this._iBook.getNameRecord(i);
/*  92:116 */       if ((nr.getSheetNumber() == sheetIndex + 1) && (name.equalsIgnoreCase(nr.getNameText()))) {
/*  93:117 */         return new Name(nr, i);
/*  94:    */       }
/*  95:    */     }
/*  96:120 */     return sheetIndex == -1 ? null : getName(name, -1);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public int getSheetIndex(EvaluationSheet evalSheet)
/* 100:    */   {
/* 101:125 */     HSSFSheet sheet = ((HSSFEvaluationSheet)evalSheet).getHSSFSheet();
/* 102:126 */     return this._uBook.getSheetIndex(sheet);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public int getSheetIndex(String sheetName)
/* 106:    */   {
/* 107:130 */     return this._uBook.getSheetIndex(sheetName);
/* 108:    */   }
/* 109:    */   
/* 110:    */   public String getSheetName(int sheetIndex)
/* 111:    */   {
/* 112:135 */     return this._uBook.getSheetName(sheetIndex);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public EvaluationSheet getSheet(int sheetIndex)
/* 116:    */   {
/* 117:141 */     return new HSSFEvaluationSheet(this._uBook.getSheetAt(sheetIndex));
/* 118:    */   }
/* 119:    */   
/* 120:    */   public int convertFromExternSheetIndex(int externSheetIndex)
/* 121:    */   {
/* 122:146 */     return this._iBook.getFirstSheetIndexFromExternSheetIndex(externSheetIndex);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public ExternalSheet getExternalSheet(int externSheetIndex)
/* 126:    */   {
/* 127:151 */     ExternalSheet sheet = this._iBook.getExternalSheet(externSheetIndex);
/* 128:152 */     if (sheet == null)
/* 129:    */     {
/* 130:154 */       int localSheetIndex = convertFromExternSheetIndex(externSheetIndex);
/* 131:155 */       if (localSheetIndex == -1) {
/* 132:157 */         return null;
/* 133:    */       }
/* 134:159 */       if (localSheetIndex == -2) {
/* 135:161 */         return null;
/* 136:    */       }
/* 137:165 */       String sheetName = getSheetName(localSheetIndex);
/* 138:    */       
/* 139:    */ 
/* 140:168 */       int lastLocalSheetIndex = this._iBook.getLastSheetIndexFromExternSheetIndex(externSheetIndex);
/* 141:169 */       if (lastLocalSheetIndex == localSheetIndex)
/* 142:    */       {
/* 143:170 */         sheet = new ExternalSheet(null, sheetName);
/* 144:    */       }
/* 145:    */       else
/* 146:    */       {
/* 147:172 */         String lastSheetName = getSheetName(lastLocalSheetIndex);
/* 148:173 */         sheet = new ExternalSheetRange(null, sheetName, lastSheetName);
/* 149:    */       }
/* 150:    */     }
/* 151:176 */     return sheet;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public ExternalSheet getExternalSheet(String firstSheetName, String lastSheetName, int externalWorkbookNumber)
/* 155:    */   {
/* 156:184 */     throw new IllegalStateException("XSSF-style external references are not supported for HSSF");
/* 157:    */   }
/* 158:    */   
/* 159:    */   public ExternalName getExternalName(int externSheetIndex, int externNameIndex)
/* 160:    */   {
/* 161:189 */     return this._iBook.getExternalName(externSheetIndex, externNameIndex);
/* 162:    */   }
/* 163:    */   
/* 164:    */   public ExternalName getExternalName(String nameName, String sheetName, int externalWorkbookNumber)
/* 165:    */   {
/* 166:197 */     throw new IllegalStateException("XSSF-style external names are not supported for HSSF");
/* 167:    */   }
/* 168:    */   
/* 169:    */   public String resolveNameXText(NameXPtg n)
/* 170:    */   {
/* 171:202 */     return this._iBook.resolveNameXText(n.getSheetRefIndex(), n.getNameIndex());
/* 172:    */   }
/* 173:    */   
/* 174:    */   public String getSheetFirstNameByExternSheet(int externSheetIndex)
/* 175:    */   {
/* 176:207 */     return this._iBook.findSheetFirstNameFromExternSheet(externSheetIndex);
/* 177:    */   }
/* 178:    */   
/* 179:    */   public String getSheetLastNameByExternSheet(int externSheetIndex)
/* 180:    */   {
/* 181:211 */     return this._iBook.findSheetLastNameFromExternSheet(externSheetIndex);
/* 182:    */   }
/* 183:    */   
/* 184:    */   public String getNameText(NamePtg namePtg)
/* 185:    */   {
/* 186:215 */     return this._iBook.getNameRecord(namePtg.getIndex()).getNameText();
/* 187:    */   }
/* 188:    */   
/* 189:    */   public EvaluationName getName(NamePtg namePtg)
/* 190:    */   {
/* 191:219 */     int ix = namePtg.getIndex();
/* 192:220 */     return new Name(this._iBook.getNameRecord(ix), ix);
/* 193:    */   }
/* 194:    */   
/* 195:    */   public Ptg[] getFormulaTokens(EvaluationCell evalCell)
/* 196:    */   {
/* 197:225 */     HSSFCell cell = ((HSSFEvaluationCell)evalCell).getHSSFCell();
/* 198:    */     
/* 199:    */ 
/* 200:    */ 
/* 201:    */ 
/* 202:230 */     FormulaRecordAggregate fra = (FormulaRecordAggregate)cell.getCellValueRecord();
/* 203:231 */     return fra.getFormulaTokens();
/* 204:    */   }
/* 205:    */   
/* 206:    */   public UDFFinder getUDFFinder()
/* 207:    */   {
/* 208:236 */     return this._uBook.getUDFFinder();
/* 209:    */   }
/* 210:    */   
/* 211:    */   private static final class Name
/* 212:    */     implements EvaluationName
/* 213:    */   {
/* 214:    */     private final NameRecord _nameRecord;
/* 215:    */     private final int _index;
/* 216:    */     
/* 217:    */     public Name(NameRecord nameRecord, int index)
/* 218:    */     {
/* 219:244 */       this._nameRecord = nameRecord;
/* 220:245 */       this._index = index;
/* 221:    */     }
/* 222:    */     
/* 223:    */     public Ptg[] getNameDefinition()
/* 224:    */     {
/* 225:249 */       return this._nameRecord.getNameDefinition();
/* 226:    */     }
/* 227:    */     
/* 228:    */     public String getNameText()
/* 229:    */     {
/* 230:253 */       return this._nameRecord.getNameText();
/* 231:    */     }
/* 232:    */     
/* 233:    */     public boolean hasFormula()
/* 234:    */     {
/* 235:257 */       return this._nameRecord.hasFormula();
/* 236:    */     }
/* 237:    */     
/* 238:    */     public boolean isFunctionName()
/* 239:    */     {
/* 240:261 */       return this._nameRecord.isFunctionName();
/* 241:    */     }
/* 242:    */     
/* 243:    */     public boolean isRange()
/* 244:    */     {
/* 245:265 */       return this._nameRecord.hasFormula();
/* 246:    */     }
/* 247:    */     
/* 248:    */     public NamePtg createPtg()
/* 249:    */     {
/* 250:269 */       return new NamePtg(this._index);
/* 251:    */     }
/* 252:    */   }
/* 253:    */   
/* 254:    */   private int getSheetExtIx(SheetIdentifier sheetIden)
/* 255:    */   {
/* 256:    */     int extIx;
/* 257:    */     int extIx;
/* 258:275 */     if (sheetIden == null)
/* 259:    */     {
/* 260:276 */       extIx = -1;
/* 261:    */     }
/* 262:    */     else
/* 263:    */     {
/* 264:278 */       String workbookName = sheetIden.getBookName();
/* 265:279 */       String firstSheetName = sheetIden.getSheetIdentifier().getName();
/* 266:280 */       String lastSheetName = firstSheetName;
/* 267:282 */       if ((sheetIden instanceof SheetRangeIdentifier)) {
/* 268:283 */         lastSheetName = ((SheetRangeIdentifier)sheetIden).getLastSheetIdentifier().getName();
/* 269:    */       }
/* 270:    */       int extIx;
/* 271:286 */       if (workbookName == null)
/* 272:    */       {
/* 273:287 */         int firstSheetIndex = this._uBook.getSheetIndex(firstSheetName);
/* 274:288 */         int lastSheetIndex = this._uBook.getSheetIndex(lastSheetName);
/* 275:289 */         extIx = this._iBook.checkExternSheet(firstSheetIndex, lastSheetIndex);
/* 276:    */       }
/* 277:    */       else
/* 278:    */       {
/* 279:291 */         extIx = this._iBook.getExternalSheetIndex(workbookName, firstSheetName, lastSheetName);
/* 280:    */       }
/* 281:    */     }
/* 282:294 */     return extIx;
/* 283:    */   }
/* 284:    */   
/* 285:    */   public SpreadsheetVersion getSpreadsheetVersion()
/* 286:    */   {
/* 287:299 */     return SpreadsheetVersion.EXCEL97;
/* 288:    */   }
/* 289:    */   
/* 290:    */   public Table getTable(String name)
/* 291:    */   {
/* 292:307 */     throw new IllegalStateException("XSSF-style tables are not supported for HSSF");
/* 293:    */   }
/* 294:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook

 * JD-Core Version:    0.7.0.1

 */