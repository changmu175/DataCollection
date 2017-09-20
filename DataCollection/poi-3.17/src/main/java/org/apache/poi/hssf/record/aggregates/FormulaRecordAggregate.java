/*   1:    */ package org.apache.poi.hssf.record.aggregates;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.ArrayRecord;
/*   4:    */ import org.apache.poi.hssf.record.CellValueRecordInterface;
/*   5:    */ import org.apache.poi.hssf.record.FormulaRecord;
/*   6:    */ import org.apache.poi.hssf.record.Record;
/*   7:    */ import org.apache.poi.hssf.record.SharedFormulaRecord;
/*   8:    */ import org.apache.poi.hssf.record.StringRecord;
/*   9:    */ import org.apache.poi.hssf.util.CellRangeAddress8Bit;
/*  10:    */ import org.apache.poi.ss.formula.Formula;
/*  11:    */ import org.apache.poi.ss.formula.ptg.ExpPtg;
/*  12:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  13:    */ import org.apache.poi.ss.usermodel.FormulaError;
/*  14:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*  15:    */ import org.apache.poi.ss.util.CellReference;
/*  16:    */ import org.apache.poi.util.RecordFormatException;
/*  17:    */ 
/*  18:    */ public final class FormulaRecordAggregate
/*  19:    */   extends RecordAggregate
/*  20:    */   implements CellValueRecordInterface
/*  21:    */ {
/*  22:    */   private final FormulaRecord _formulaRecord;
/*  23:    */   private SharedValueManager _sharedValueManager;
/*  24:    */   private StringRecord _stringRecord;
/*  25:    */   private SharedFormulaRecord _sharedFormulaRecord;
/*  26:    */   
/*  27:    */   public FormulaRecordAggregate(FormulaRecord formulaRec, StringRecord stringRec, SharedValueManager svm)
/*  28:    */   {
/*  29: 56 */     if (svm == null) {
/*  30: 57 */       throw new IllegalArgumentException("sfm must not be null");
/*  31:    */     }
/*  32: 59 */     if (formulaRec.hasCachedResultString())
/*  33:    */     {
/*  34: 60 */       if (stringRec == null) {
/*  35: 61 */         throw new RecordFormatException("Formula record flag is set but String record was not found");
/*  36:    */       }
/*  37: 63 */       this._stringRecord = stringRec;
/*  38:    */     }
/*  39:    */     else
/*  40:    */     {
/*  41: 68 */       this._stringRecord = null;
/*  42:    */     }
/*  43: 71 */     this._formulaRecord = formulaRec;
/*  44: 72 */     this._sharedValueManager = svm;
/*  45: 73 */     if (formulaRec.isSharedFormula())
/*  46:    */     {
/*  47: 74 */       CellReference firstCell = formulaRec.getFormula().getExpReference();
/*  48: 75 */       if (firstCell == null) {
/*  49: 76 */         handleMissingSharedFormulaRecord(formulaRec);
/*  50:    */       } else {
/*  51: 78 */         this._sharedFormulaRecord = svm.linkSharedFormulaRecord(firstCell, this);
/*  52:    */       }
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   private static void handleMissingSharedFormulaRecord(FormulaRecord formula)
/*  57:    */   {
/*  58: 96 */     Ptg firstToken = formula.getParsedExpression()[0];
/*  59: 97 */     if ((firstToken instanceof ExpPtg)) {
/*  60: 98 */       throw new RecordFormatException("SharedFormulaRecord not found for FormulaRecord with (isSharedFormula=true)");
/*  61:    */     }
/*  62:102 */     formula.setSharedFormula(false);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public FormulaRecord getFormulaRecord()
/*  66:    */   {
/*  67:106 */     return this._formulaRecord;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public StringRecord getStringRecord()
/*  71:    */   {
/*  72:114 */     return this._stringRecord;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public short getXFIndex()
/*  76:    */   {
/*  77:118 */     return this._formulaRecord.getXFIndex();
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setXFIndex(short xf)
/*  81:    */   {
/*  82:122 */     this._formulaRecord.setXFIndex(xf);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setColumn(short col)
/*  86:    */   {
/*  87:126 */     this._formulaRecord.setColumn(col);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setRow(int row)
/*  91:    */   {
/*  92:130 */     this._formulaRecord.setRow(row);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public short getColumn()
/*  96:    */   {
/*  97:134 */     return this._formulaRecord.getColumn();
/*  98:    */   }
/*  99:    */   
/* 100:    */   public int getRow()
/* 101:    */   {
/* 102:138 */     return this._formulaRecord.getRow();
/* 103:    */   }
/* 104:    */   
/* 105:    */   public String toString()
/* 106:    */   {
/* 107:142 */     return this._formulaRecord.toString();
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void visitContainedRecords(RecordVisitor rv)
/* 111:    */   {
/* 112:146 */     rv.visitRecord(this._formulaRecord);
/* 113:147 */     Record sharedFormulaRecord = this._sharedValueManager.getRecordForFirstCell(this);
/* 114:148 */     if (sharedFormulaRecord != null) {
/* 115:149 */       rv.visitRecord(sharedFormulaRecord);
/* 116:    */     }
/* 117:151 */     if ((this._formulaRecord.hasCachedResultString()) && (this._stringRecord != null)) {
/* 118:152 */       rv.visitRecord(this._stringRecord);
/* 119:    */     }
/* 120:    */   }
/* 121:    */   
/* 122:    */   public String getStringValue()
/* 123:    */   {
/* 124:157 */     if (this._stringRecord == null) {
/* 125:158 */       return null;
/* 126:    */     }
/* 127:160 */     return this._stringRecord.getString();
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void setCachedStringResult(String value)
/* 131:    */   {
/* 132:166 */     if (this._stringRecord == null) {
/* 133:167 */       this._stringRecord = new StringRecord();
/* 134:    */     }
/* 135:169 */     this._stringRecord.setString(value);
/* 136:170 */     if (value.length() < 1) {
/* 137:171 */       this._formulaRecord.setCachedResultTypeEmptyString();
/* 138:    */     } else {
/* 139:173 */       this._formulaRecord.setCachedResultTypeString();
/* 140:    */     }
/* 141:    */   }
/* 142:    */   
/* 143:    */   public void setCachedBooleanResult(boolean value)
/* 144:    */   {
/* 145:177 */     this._stringRecord = null;
/* 146:178 */     this._formulaRecord.setCachedResultBoolean(value);
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void setCachedErrorResult(int errorCode)
/* 150:    */   {
/* 151:181 */     this._stringRecord = null;
/* 152:182 */     this._formulaRecord.setCachedResultErrorCode(errorCode);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void setCachedErrorResult(FormulaError error)
/* 156:    */   {
/* 157:185 */     setCachedErrorResult(error.getCode());
/* 158:    */   }
/* 159:    */   
/* 160:    */   public void setCachedDoubleResult(double value)
/* 161:    */   {
/* 162:188 */     this._stringRecord = null;
/* 163:189 */     this._formulaRecord.setValue(value);
/* 164:    */   }
/* 165:    */   
/* 166:    */   public Ptg[] getFormulaTokens()
/* 167:    */   {
/* 168:193 */     if (this._sharedFormulaRecord != null) {
/* 169:194 */       return this._sharedFormulaRecord.getFormulaTokens(this._formulaRecord);
/* 170:    */     }
/* 171:196 */     CellReference expRef = this._formulaRecord.getFormula().getExpReference();
/* 172:197 */     if (expRef != null)
/* 173:    */     {
/* 174:198 */       ArrayRecord arec = this._sharedValueManager.getArrayRecord(expRef.getRow(), expRef.getCol());
/* 175:199 */       return arec.getFormulaTokens();
/* 176:    */     }
/* 177:201 */     return this._formulaRecord.getParsedExpression();
/* 178:    */   }
/* 179:    */   
/* 180:    */   public void setParsedExpression(Ptg[] ptgs)
/* 181:    */   {
/* 182:208 */     notifyFormulaChanging();
/* 183:209 */     this._formulaRecord.setParsedExpression(ptgs);
/* 184:    */   }
/* 185:    */   
/* 186:    */   public void unlinkSharedFormula()
/* 187:    */   {
/* 188:213 */     SharedFormulaRecord sfr = this._sharedFormulaRecord;
/* 189:214 */     if (sfr == null) {
/* 190:215 */       throw new IllegalStateException("Formula not linked to shared formula");
/* 191:    */     }
/* 192:217 */     Ptg[] ptgs = sfr.getFormulaTokens(this._formulaRecord);
/* 193:218 */     this._formulaRecord.setParsedExpression(ptgs);
/* 194:    */     
/* 195:220 */     this._formulaRecord.setSharedFormula(false);
/* 196:221 */     this._sharedFormulaRecord = null;
/* 197:    */   }
/* 198:    */   
/* 199:    */   public void notifyFormulaChanging()
/* 200:    */   {
/* 201:229 */     if (this._sharedFormulaRecord != null) {
/* 202:230 */       this._sharedValueManager.unlink(this._sharedFormulaRecord);
/* 203:    */     }
/* 204:    */   }
/* 205:    */   
/* 206:    */   public boolean isPartOfArrayFormula()
/* 207:    */   {
/* 208:234 */     if (this._sharedFormulaRecord != null) {
/* 209:235 */       return false;
/* 210:    */     }
/* 211:237 */     CellReference expRef = this._formulaRecord.getFormula().getExpReference();
/* 212:238 */     ArrayRecord arec = expRef == null ? null : this._sharedValueManager.getArrayRecord(expRef.getRow(), expRef.getCol());
/* 213:239 */     return arec != null;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public CellRangeAddress getArrayFormulaRange()
/* 217:    */   {
/* 218:243 */     if (this._sharedFormulaRecord != null) {
/* 219:244 */       throw new IllegalStateException("not an array formula cell.");
/* 220:    */     }
/* 221:246 */     CellReference expRef = this._formulaRecord.getFormula().getExpReference();
/* 222:247 */     if (expRef == null) {
/* 223:248 */       throw new IllegalStateException("not an array formula cell.");
/* 224:    */     }
/* 225:250 */     ArrayRecord arec = this._sharedValueManager.getArrayRecord(expRef.getRow(), expRef.getCol());
/* 226:251 */     if (arec == null) {
/* 227:252 */       throw new IllegalStateException("ArrayRecord was not found for the locator " + expRef.formatAsString());
/* 228:    */     }
/* 229:254 */     CellRangeAddress8Bit a = arec.getRange();
/* 230:255 */     return new CellRangeAddress(a.getFirstRow(), a.getLastRow(), a.getFirstColumn(), a.getLastColumn());
/* 231:    */   }
/* 232:    */   
/* 233:    */   public void setArrayFormula(CellRangeAddress r, Ptg[] ptgs)
/* 234:    */   {
/* 235:260 */     ArrayRecord arr = new ArrayRecord(Formula.create(ptgs), new CellRangeAddress8Bit(r.getFirstRow(), r.getLastRow(), r.getFirstColumn(), r.getLastColumn()));
/* 236:261 */     this._sharedValueManager.addArrayRecord(arr);
/* 237:    */   }
/* 238:    */   
/* 239:    */   public CellRangeAddress removeArrayFormula(int rowIndex, int columnIndex)
/* 240:    */   {
/* 241:268 */     CellRangeAddress8Bit a = this._sharedValueManager.removeArrayFormula(rowIndex, columnIndex);
/* 242:    */     
/* 243:270 */     this._formulaRecord.setParsedExpression(null);
/* 244:271 */     return new CellRangeAddress(a.getFirstRow(), a.getLastRow(), a.getFirstColumn(), a.getLastColumn());
/* 245:    */   }
/* 246:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.aggregates.FormulaRecordAggregate

 * JD-Core Version:    0.7.0.1

 */