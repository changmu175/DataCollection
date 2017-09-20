/*   1:    */ package org.apache.poi.hssf.record.aggregates;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.hssf.model.RecordStream;
/*   6:    */ import org.apache.poi.hssf.record.CFHeader12Record;
/*   7:    */ import org.apache.poi.hssf.record.CFHeaderBase;
/*   8:    */ import org.apache.poi.hssf.record.CFHeaderRecord;
/*   9:    */ import org.apache.poi.hssf.record.CFRule12Record;
/*  10:    */ import org.apache.poi.hssf.record.CFRuleBase;
/*  11:    */ import org.apache.poi.hssf.record.CFRuleRecord;
/*  12:    */ import org.apache.poi.hssf.record.Record;
/*  13:    */ import org.apache.poi.ss.formula.FormulaShifter;
/*  14:    */ import org.apache.poi.ss.formula.ptg.AreaErrPtg;
/*  15:    */ import org.apache.poi.ss.formula.ptg.AreaPtg;
/*  16:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  17:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*  18:    */ import org.apache.poi.util.POILogFactory;
/*  19:    */ import org.apache.poi.util.POILogger;
/*  20:    */ import org.apache.poi.util.RecordFormatException;
/*  21:    */ 
/*  22:    */ public final class CFRecordsAggregate
/*  23:    */   extends RecordAggregate
/*  24:    */ {
/*  25:    */   private static final int MAX_97_2003_CONDTIONAL_FORMAT_RULES = 3;
/*  26: 51 */   private static final POILogger logger = POILogFactory.getLogger(CFRecordsAggregate.class);
/*  27:    */   private final CFHeaderBase header;
/*  28:    */   private final List<CFRuleBase> rules;
/*  29:    */   
/*  30:    */   private CFRecordsAggregate(CFHeaderBase pHeader, CFRuleBase[] pRules)
/*  31:    */   {
/*  32: 59 */     if (pHeader == null) {
/*  33: 60 */       throw new IllegalArgumentException("header must not be null");
/*  34:    */     }
/*  35: 62 */     if (pRules == null) {
/*  36: 63 */       throw new IllegalArgumentException("rules must not be null");
/*  37:    */     }
/*  38: 65 */     if (pRules.length > 3) {
/*  39: 66 */       logger.log(5, new Object[] { "Excel versions before 2007 require that No more than 3 rules may be specified, " + pRules.length + " were found," + " this file will cause problems with old Excel versions" });
/*  40:    */     }
/*  41: 71 */     if (pRules.length != pHeader.getNumberOfConditionalFormats()) {
/*  42: 72 */       throw new RecordFormatException("Mismatch number of rules");
/*  43:    */     }
/*  44: 74 */     this.header = pHeader;
/*  45: 75 */     this.rules = new ArrayList(pRules.length);
/*  46: 76 */     for (CFRuleBase pRule : pRules)
/*  47:    */     {
/*  48: 77 */       checkRuleType(pRule);
/*  49: 78 */       this.rules.add(pRule);
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   public CFRecordsAggregate(CellRangeAddress[] regions, CFRuleBase[] rules)
/*  54:    */   {
/*  55: 83 */     this(createHeader(regions, rules), rules);
/*  56:    */   }
/*  57:    */   
/*  58:    */   private static CFHeaderBase createHeader(CellRangeAddress[] regions, CFRuleBase[] rules)
/*  59:    */   {
/*  60:    */     CFHeaderBase header;
/*  61:    */     CFHeaderBase header;
/*  62: 87 */     if ((rules.length == 0) || ((rules[0] instanceof CFRuleRecord))) {
/*  63: 88 */       header = new CFHeaderRecord(regions, rules.length);
/*  64:    */     } else {
/*  65: 90 */       header = new CFHeader12Record(regions, rules.length);
/*  66:    */     }
/*  67: 95 */     header.setNeedRecalculation(true);
/*  68:    */     
/*  69: 97 */     return header;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static CFRecordsAggregate createCFAggregate(RecordStream rs)
/*  73:    */   {
/*  74:106 */     Record rec = rs.getNext();
/*  75:107 */     if ((rec.getSid() != 432) && (rec.getSid() != 2169)) {
/*  76:109 */       throw new IllegalStateException("next record sid was " + rec.getSid() + " instead of " + 432 + " or " + 2169 + " as expected");
/*  77:    */     }
/*  78:114 */     CFHeaderBase header = (CFHeaderBase)rec;
/*  79:115 */     int nRules = header.getNumberOfConditionalFormats();
/*  80:    */     
/*  81:117 */     CFRuleBase[] rules = new CFRuleBase[nRules];
/*  82:118 */     for (int i = 0; i < rules.length; i++) {
/*  83:119 */       rules[i] = ((CFRuleBase)rs.getNext());
/*  84:    */     }
/*  85:122 */     return new CFRecordsAggregate(header, rules);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public CFRecordsAggregate cloneCFAggregate()
/*  89:    */   {
/*  90:129 */     CFRuleBase[] newRecs = new CFRuleBase[this.rules.size()];
/*  91:130 */     for (int i = 0; i < newRecs.length; i++) {
/*  92:131 */       newRecs[i] = getRule(i).clone();
/*  93:    */     }
/*  94:133 */     return new CFRecordsAggregate(this.header.clone(), newRecs);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public CFHeaderBase getHeader()
/*  98:    */   {
/*  99:140 */     return this.header;
/* 100:    */   }
/* 101:    */   
/* 102:    */   private void checkRuleIndex(int idx)
/* 103:    */   {
/* 104:144 */     if ((idx < 0) || (idx >= this.rules.size())) {
/* 105:145 */       throw new IllegalArgumentException("Bad rule record index (" + idx + ") nRules=" + this.rules.size());
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   private void checkRuleType(CFRuleBase r)
/* 110:    */   {
/* 111:150 */     if (((this.header instanceof CFHeaderRecord)) && ((r instanceof CFRuleRecord))) {
/* 112:152 */       return;
/* 113:    */     }
/* 114:154 */     if (((this.header instanceof CFHeader12Record)) && ((r instanceof CFRule12Record))) {
/* 115:156 */       return;
/* 116:    */     }
/* 117:158 */     throw new IllegalArgumentException("Header and Rule must both be CF or both be CF12, can't mix");
/* 118:    */   }
/* 119:    */   
/* 120:    */   public CFRuleBase getRule(int idx)
/* 121:    */   {
/* 122:162 */     checkRuleIndex(idx);
/* 123:163 */     return (CFRuleBase)this.rules.get(idx);
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void setRule(int idx, CFRuleBase r)
/* 127:    */   {
/* 128:166 */     if (r == null) {
/* 129:167 */       throw new IllegalArgumentException("r must not be null");
/* 130:    */     }
/* 131:169 */     checkRuleIndex(idx);
/* 132:170 */     checkRuleType(r);
/* 133:171 */     this.rules.set(idx, r);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void addRule(CFRuleBase r)
/* 137:    */   {
/* 138:174 */     if (r == null) {
/* 139:175 */       throw new IllegalArgumentException("r must not be null");
/* 140:    */     }
/* 141:177 */     if (this.rules.size() >= 3) {
/* 142:178 */       logger.log(5, new Object[] { "Excel versions before 2007 cannot cope with any more than 3 - this file will cause problems with old Excel versions" });
/* 143:    */     }
/* 144:182 */     checkRuleType(r);
/* 145:183 */     this.rules.add(r);
/* 146:184 */     this.header.setNumberOfConditionalFormats(this.rules.size());
/* 147:    */   }
/* 148:    */   
/* 149:    */   public int getNumberOfRules()
/* 150:    */   {
/* 151:187 */     return this.rules.size();
/* 152:    */   }
/* 153:    */   
/* 154:    */   public String toString()
/* 155:    */   {
/* 156:194 */     StringBuilder buffer = new StringBuilder();
/* 157:195 */     String type = "CF";
/* 158:196 */     if ((this.header instanceof CFHeader12Record)) {
/* 159:197 */       type = "CF12";
/* 160:    */     }
/* 161:200 */     buffer.append("[").append(type).append("]\n");
/* 162:201 */     if (this.header != null) {
/* 163:202 */       buffer.append(this.header);
/* 164:    */     }
/* 165:204 */     for (CFRuleBase cfRule : this.rules) {
/* 166:205 */       buffer.append(cfRule);
/* 167:    */     }
/* 168:207 */     buffer.append("[/").append(type).append("]\n");
/* 169:208 */     return buffer.toString();
/* 170:    */   }
/* 171:    */   
/* 172:    */   public void visitContainedRecords(RecordVisitor rv)
/* 173:    */   {
/* 174:212 */     rv.visitRecord(this.header);
/* 175:213 */     for (CFRuleBase rule : this.rules) {
/* 176:214 */       rv.visitRecord(rule);
/* 177:    */     }
/* 178:    */   }
/* 179:    */   
/* 180:    */   public boolean updateFormulasAfterCellShift(FormulaShifter shifter, int currentExternSheetIx)
/* 181:    */   {
/* 182:222 */     CellRangeAddress[] cellRanges = this.header.getCellRanges();
/* 183:223 */     boolean changed = false;
/* 184:224 */     List<CellRangeAddress> temp = new ArrayList();
/* 185:225 */     for (CellRangeAddress craOld : cellRanges)
/* 186:    */     {
/* 187:226 */       CellRangeAddress craNew = shiftRange(shifter, craOld, currentExternSheetIx);
/* 188:227 */       if (craNew == null)
/* 189:    */       {
/* 190:228 */         changed = true;
/* 191:    */       }
/* 192:    */       else
/* 193:    */       {
/* 194:231 */         temp.add(craNew);
/* 195:232 */         if (craNew != craOld) {
/* 196:233 */           changed = true;
/* 197:    */         }
/* 198:    */       }
/* 199:    */     }
/* 200:237 */     if (changed)
/* 201:    */     {
/* 202:238 */       int nRanges = temp.size();
/* 203:239 */       if (nRanges == 0) {
/* 204:240 */         return false;
/* 205:    */       }
/* 206:242 */       CellRangeAddress[] newRanges = new CellRangeAddress[nRanges];
/* 207:243 */       temp.toArray(newRanges);
/* 208:244 */       this.header.setCellRanges(newRanges);
/* 209:    */     }
/* 210:247 */     for (CFRuleBase rule : this.rules)
/* 211:    */     {
/* 212:249 */       Ptg[] ptgs = rule.getParsedExpression1();
/* 213:250 */       if ((ptgs != null) && (shifter.adjustFormula(ptgs, currentExternSheetIx))) {
/* 214:251 */         rule.setParsedExpression1(ptgs);
/* 215:    */       }
/* 216:253 */       ptgs = rule.getParsedExpression2();
/* 217:254 */       if ((ptgs != null) && (shifter.adjustFormula(ptgs, currentExternSheetIx))) {
/* 218:255 */         rule.setParsedExpression2(ptgs);
/* 219:    */       }
/* 220:257 */       if ((rule instanceof CFRule12Record))
/* 221:    */       {
/* 222:258 */         CFRule12Record rule12 = (CFRule12Record)rule;
/* 223:259 */         ptgs = rule12.getParsedExpressionScale();
/* 224:260 */         if ((ptgs != null) && (shifter.adjustFormula(ptgs, currentExternSheetIx))) {
/* 225:261 */           rule12.setParsedExpressionScale(ptgs);
/* 226:    */         }
/* 227:    */       }
/* 228:    */     }
/* 229:265 */     return true;
/* 230:    */   }
/* 231:    */   
/* 232:    */   private static CellRangeAddress shiftRange(FormulaShifter shifter, CellRangeAddress cra, int currentExternSheetIx)
/* 233:    */   {
/* 234:270 */     AreaPtg aptg = new AreaPtg(cra.getFirstRow(), cra.getLastRow(), cra.getFirstColumn(), cra.getLastColumn(), false, false, false, false);
/* 235:271 */     Ptg[] ptgs = { aptg };
/* 236:273 */     if (!shifter.adjustFormula(ptgs, currentExternSheetIx)) {
/* 237:274 */       return cra;
/* 238:    */     }
/* 239:276 */     Ptg ptg0 = ptgs[0];
/* 240:277 */     if ((ptg0 instanceof AreaPtg))
/* 241:    */     {
/* 242:278 */       AreaPtg bptg = (AreaPtg)ptg0;
/* 243:279 */       return new CellRangeAddress(bptg.getFirstRow(), bptg.getLastRow(), bptg.getFirstColumn(), bptg.getLastColumn());
/* 244:    */     }
/* 245:281 */     if ((ptg0 instanceof AreaErrPtg)) {
/* 246:282 */       return null;
/* 247:    */     }
/* 248:284 */     throw new IllegalStateException("Unexpected shifted ptg class (" + ptg0.getClass().getName() + ")");
/* 249:    */   }
/* 250:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.aggregates.CFRecordsAggregate

 * JD-Core Version:    0.7.0.1

 */