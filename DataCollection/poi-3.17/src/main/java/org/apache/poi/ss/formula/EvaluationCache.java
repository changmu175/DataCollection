/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   9:    */ import org.apache.poi.ss.usermodel.CellType;
/*  10:    */ 
/*  11:    */ final class EvaluationCache
/*  12:    */ {
/*  13:    */   private final PlainCellCache _plainCellCache;
/*  14:    */   private final FormulaCellCache _formulaCellCache;
/*  15:    */   final IEvaluationListener _evaluationListener;
/*  16:    */   
/*  17:    */   EvaluationCache(IEvaluationListener evaluationListener)
/*  18:    */   {
/*  19: 46 */     this._evaluationListener = evaluationListener;
/*  20: 47 */     this._plainCellCache = new PlainCellCache();
/*  21: 48 */     this._formulaCellCache = new FormulaCellCache();
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void notifyUpdateCell(int bookIndex, int sheetIndex, EvaluationCell cell)
/*  25:    */   {
/*  26: 52 */     FormulaCellCacheEntry fcce = this._formulaCellCache.get(cell);
/*  27:    */     
/*  28: 54 */     int rowIndex = cell.getRowIndex();
/*  29: 55 */     int columnIndex = cell.getColumnIndex();
/*  30: 56 */     PlainCellCache.Loc loc = new PlainCellCache.Loc(bookIndex, sheetIndex, rowIndex, columnIndex);
/*  31: 57 */     PlainValueCellCacheEntry pcce = this._plainCellCache.get(loc);
/*  32: 59 */     if (cell.getCellTypeEnum() == CellType.FORMULA)
/*  33:    */     {
/*  34: 60 */       if (fcce == null)
/*  35:    */       {
/*  36: 61 */         fcce = new FormulaCellCacheEntry();
/*  37: 62 */         if (pcce == null)
/*  38:    */         {
/*  39: 63 */           if (this._evaluationListener != null) {
/*  40: 64 */             this._evaluationListener.onChangeFromBlankValue(sheetIndex, rowIndex, columnIndex, cell, fcce);
/*  41:    */           }
/*  42: 67 */           updateAnyBlankReferencingFormulas(bookIndex, sheetIndex, rowIndex, columnIndex);
/*  43:    */         }
/*  44: 70 */         this._formulaCellCache.put(cell, fcce);
/*  45:    */       }
/*  46:    */       else
/*  47:    */       {
/*  48: 72 */         fcce.recurseClearCachedFormulaResults(this._evaluationListener);
/*  49: 73 */         fcce.clearFormulaEntry();
/*  50:    */       }
/*  51: 75 */       if (pcce != null)
/*  52:    */       {
/*  53: 79 */         pcce.recurseClearCachedFormulaResults(this._evaluationListener);
/*  54: 80 */         this._plainCellCache.remove(loc);
/*  55:    */       }
/*  56:    */     }
/*  57:    */     else
/*  58:    */     {
/*  59: 83 */       ValueEval value = WorkbookEvaluator.getValueFromNonFormulaCell(cell);
/*  60: 84 */       if (pcce == null)
/*  61:    */       {
/*  62: 85 */         if (value != BlankEval.instance)
/*  63:    */         {
/*  64: 89 */           pcce = new PlainValueCellCacheEntry(value);
/*  65: 90 */           if (fcce == null)
/*  66:    */           {
/*  67: 91 */             if (this._evaluationListener != null) {
/*  68: 92 */               this._evaluationListener.onChangeFromBlankValue(sheetIndex, rowIndex, columnIndex, cell, pcce);
/*  69:    */             }
/*  70: 94 */             updateAnyBlankReferencingFormulas(bookIndex, sheetIndex, rowIndex, columnIndex);
/*  71:    */           }
/*  72: 97 */           this._plainCellCache.put(loc, pcce);
/*  73:    */         }
/*  74:    */       }
/*  75:    */       else
/*  76:    */       {
/*  77:100 */         if (pcce.updateValue(value)) {
/*  78:101 */           pcce.recurseClearCachedFormulaResults(this._evaluationListener);
/*  79:    */         }
/*  80:103 */         if (value == BlankEval.instance) {
/*  81:104 */           this._plainCellCache.remove(loc);
/*  82:    */         }
/*  83:    */       }
/*  84:107 */       if (fcce != null)
/*  85:    */       {
/*  86:111 */         this._formulaCellCache.remove(cell);
/*  87:112 */         fcce.setSensitiveInputCells(null);
/*  88:113 */         fcce.recurseClearCachedFormulaResults(this._evaluationListener);
/*  89:    */       }
/*  90:    */     }
/*  91:    */   }
/*  92:    */   
/*  93:    */   private void updateAnyBlankReferencingFormulas(int bookIndex, int sheetIndex, final int rowIndex, final int columnIndex)
/*  94:    */   {
/*  95:120 */     final FormulaUsedBlankCellSet.BookSheetKey bsk = new FormulaUsedBlankCellSet.BookSheetKey(bookIndex, sheetIndex);
/*  96:121 */     this._formulaCellCache.applyOperation(new FormulaCellCache.IEntryOperation()
/*  97:    */     {
/*  98:    */       public void processEntry(FormulaCellCacheEntry entry)
/*  99:    */       {
/* 100:124 */         entry.notifyUpdatedBlankCell(bsk, rowIndex, columnIndex, EvaluationCache.this._evaluationListener);
/* 101:    */       }
/* 102:    */     });
/* 103:    */   }
/* 104:    */   
/* 105:    */   public PlainValueCellCacheEntry getPlainValueEntry(int bookIndex, int sheetIndex, int rowIndex, int columnIndex, ValueEval value)
/* 106:    */   {
/* 107:132 */     PlainCellCache.Loc loc = new PlainCellCache.Loc(bookIndex, sheetIndex, rowIndex, columnIndex);
/* 108:133 */     PlainValueCellCacheEntry result = this._plainCellCache.get(loc);
/* 109:134 */     if (result == null)
/* 110:    */     {
/* 111:135 */       result = new PlainValueCellCacheEntry(value);
/* 112:136 */       this._plainCellCache.put(loc, result);
/* 113:137 */       if (this._evaluationListener != null) {
/* 114:138 */         this._evaluationListener.onReadPlainValue(sheetIndex, rowIndex, columnIndex, result);
/* 115:    */       }
/* 116:    */     }
/* 117:    */     else
/* 118:    */     {
/* 119:142 */       if (!areValuesEqual(result.getValue(), value)) {
/* 120:143 */         throw new IllegalStateException("value changed");
/* 121:    */       }
/* 122:145 */       if (this._evaluationListener != null) {
/* 123:146 */         this._evaluationListener.onCacheHit(sheetIndex, rowIndex, columnIndex, value);
/* 124:    */       }
/* 125:    */     }
/* 126:149 */     return result;
/* 127:    */   }
/* 128:    */   
/* 129:    */   private boolean areValuesEqual(ValueEval a, ValueEval b)
/* 130:    */   {
/* 131:152 */     if (a == null) {
/* 132:153 */       return false;
/* 133:    */     }
/* 134:155 */     Class<?> cls = a.getClass();
/* 135:156 */     if (cls != b.getClass()) {
/* 136:158 */       return false;
/* 137:    */     }
/* 138:160 */     if (a == BlankEval.instance) {
/* 139:161 */       return b == a;
/* 140:    */     }
/* 141:163 */     if (cls == NumberEval.class) {
/* 142:164 */       return ((NumberEval)a).getNumberValue() == ((NumberEval)b).getNumberValue();
/* 143:    */     }
/* 144:166 */     if (cls == StringEval.class) {
/* 145:167 */       return ((StringEval)a).getStringValue().equals(((StringEval)b).getStringValue());
/* 146:    */     }
/* 147:169 */     if (cls == BoolEval.class) {
/* 148:170 */       return ((BoolEval)a).getBooleanValue() == ((BoolEval)b).getBooleanValue();
/* 149:    */     }
/* 150:172 */     if (cls == ErrorEval.class) {
/* 151:173 */       return ((ErrorEval)a).getErrorCode() == ((ErrorEval)b).getErrorCode();
/* 152:    */     }
/* 153:175 */     throw new IllegalStateException("Unexpected value class (" + cls.getName() + ")");
/* 154:    */   }
/* 155:    */   
/* 156:    */   public FormulaCellCacheEntry getOrCreateFormulaCellEntry(EvaluationCell cell)
/* 157:    */   {
/* 158:179 */     FormulaCellCacheEntry result = this._formulaCellCache.get(cell);
/* 159:180 */     if (result == null)
/* 160:    */     {
/* 161:182 */       result = new FormulaCellCacheEntry();
/* 162:183 */       this._formulaCellCache.put(cell, result);
/* 163:    */     }
/* 164:185 */     return result;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public void clear()
/* 168:    */   {
/* 169:192 */     if (this._evaluationListener != null) {
/* 170:193 */       this._evaluationListener.onClearWholeCache();
/* 171:    */     }
/* 172:195 */     this._plainCellCache.clear();
/* 173:196 */     this._formulaCellCache.clear();
/* 174:    */   }
/* 175:    */   
/* 176:    */   public void notifyDeleteCell(int bookIndex, int sheetIndex, EvaluationCell cell)
/* 177:    */   {
/* 178:200 */     if (cell.getCellTypeEnum() == CellType.FORMULA)
/* 179:    */     {
/* 180:201 */       FormulaCellCacheEntry fcce = this._formulaCellCache.remove(cell);
/* 181:202 */       if (fcce != null)
/* 182:    */       {
/* 183:205 */         fcce.setSensitiveInputCells(null);
/* 184:206 */         fcce.recurseClearCachedFormulaResults(this._evaluationListener);
/* 185:    */       }
/* 186:    */     }
/* 187:    */     else
/* 188:    */     {
/* 189:209 */       PlainCellCache.Loc loc = new PlainCellCache.Loc(bookIndex, sheetIndex, cell.getRowIndex(), cell.getColumnIndex());
/* 190:210 */       PlainValueCellCacheEntry pcce = this._plainCellCache.get(loc);
/* 191:212 */       if (pcce != null) {
/* 192:215 */         pcce.recurseClearCachedFormulaResults(this._evaluationListener);
/* 193:    */       }
/* 194:    */     }
/* 195:    */   }
/* 196:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.EvaluationCache
 * JD-Core Version:    0.7.0.1
 */