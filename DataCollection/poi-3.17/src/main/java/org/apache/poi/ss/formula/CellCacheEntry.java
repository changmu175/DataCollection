/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   9:    */ 
/*  10:    */ abstract class CellCacheEntry
/*  11:    */   implements IEvaluationListener.ICacheEntry
/*  12:    */ {
/*  13: 32 */   public static final CellCacheEntry[] EMPTY_ARRAY = new CellCacheEntry[0];
/*  14:    */   private final FormulaCellCacheEntrySet _consumingCells;
/*  15:    */   private ValueEval _value;
/*  16:    */   
/*  17:    */   protected CellCacheEntry()
/*  18:    */   {
/*  19: 39 */     this._consumingCells = new FormulaCellCacheEntrySet();
/*  20:    */   }
/*  21:    */   
/*  22:    */   protected final void clearValue()
/*  23:    */   {
/*  24: 42 */     this._value = null;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public final boolean updateValue(ValueEval value)
/*  28:    */   {
/*  29: 46 */     if (value == null) {
/*  30: 47 */       throw new IllegalArgumentException("Did not expect to update to null");
/*  31:    */     }
/*  32: 49 */     boolean result = !areValuesEqual(this._value, value);
/*  33: 50 */     this._value = value;
/*  34: 51 */     return result;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public final ValueEval getValue()
/*  38:    */   {
/*  39: 54 */     return this._value;
/*  40:    */   }
/*  41:    */   
/*  42:    */   private static boolean areValuesEqual(ValueEval a, ValueEval b)
/*  43:    */   {
/*  44: 58 */     if (a == null) {
/*  45: 59 */       return false;
/*  46:    */     }
/*  47: 61 */     Class<? extends ValueEval> cls = a.getClass();
/*  48: 62 */     if (cls != b.getClass()) {
/*  49: 64 */       return false;
/*  50:    */     }
/*  51: 66 */     if (a == BlankEval.instance) {
/*  52: 67 */       return b == a;
/*  53:    */     }
/*  54: 69 */     if (cls == NumberEval.class) {
/*  55: 70 */       return ((NumberEval)a).getNumberValue() == ((NumberEval)b).getNumberValue();
/*  56:    */     }
/*  57: 72 */     if (cls == StringEval.class) {
/*  58: 73 */       return ((StringEval)a).getStringValue().equals(((StringEval)b).getStringValue());
/*  59:    */     }
/*  60: 75 */     if (cls == BoolEval.class) {
/*  61: 76 */       return ((BoolEval)a).getBooleanValue() == ((BoolEval)b).getBooleanValue();
/*  62:    */     }
/*  63: 78 */     if (cls == ErrorEval.class) {
/*  64: 79 */       return ((ErrorEval)a).getErrorCode() == ((ErrorEval)b).getErrorCode();
/*  65:    */     }
/*  66: 81 */     throw new IllegalStateException("Unexpected value class (" + cls.getName() + ")");
/*  67:    */   }
/*  68:    */   
/*  69:    */   public final void addConsumingCell(FormulaCellCacheEntry cellLoc)
/*  70:    */   {
/*  71: 85 */     this._consumingCells.add(cellLoc);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public final FormulaCellCacheEntry[] getConsumingCells()
/*  75:    */   {
/*  76: 89 */     return this._consumingCells.toArray();
/*  77:    */   }
/*  78:    */   
/*  79:    */   public final void clearConsumingCell(FormulaCellCacheEntry cce)
/*  80:    */   {
/*  81: 93 */     if (!this._consumingCells.remove(cce)) {
/*  82: 94 */       throw new IllegalStateException("Specified formula cell is not consumed by this cell");
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public final void recurseClearCachedFormulaResults(IEvaluationListener listener)
/*  87:    */   {
/*  88: 98 */     if (listener == null)
/*  89:    */     {
/*  90: 99 */       recurseClearCachedFormulaResults();
/*  91:    */     }
/*  92:    */     else
/*  93:    */     {
/*  94:101 */       listener.onClearCachedValue(this);
/*  95:102 */       recurseClearCachedFormulaResults(listener, 1);
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected final void recurseClearCachedFormulaResults()
/* 100:    */   {
/* 101:113 */     FormulaCellCacheEntry[] formulaCells = getConsumingCells();
/* 102:115 */     for (int i = 0; i < formulaCells.length; i++)
/* 103:    */     {
/* 104:116 */       FormulaCellCacheEntry fc = formulaCells[i];
/* 105:117 */       fc.clearFormulaEntry();
/* 106:118 */       fc.recurseClearCachedFormulaResults();
/* 107:    */     }
/* 108:    */   }
/* 109:    */   
/* 110:    */   protected final void recurseClearCachedFormulaResults(IEvaluationListener listener, int depth)
/* 111:    */   {
/* 112:126 */     FormulaCellCacheEntry[] formulaCells = getConsumingCells();
/* 113:    */     
/* 114:128 */     listener.sortDependentCachedValues(formulaCells);
/* 115:129 */     for (int i = 0; i < formulaCells.length; i++)
/* 116:    */     {
/* 117:130 */       FormulaCellCacheEntry fc = formulaCells[i];
/* 118:131 */       listener.onClearDependentCachedValue(fc, depth);
/* 119:132 */       fc.clearFormulaEntry();
/* 120:133 */       fc.recurseClearCachedFormulaResults(listener, depth + 1);
/* 121:    */     }
/* 122:    */   }
/* 123:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.CellCacheEntry
 * JD-Core Version:    0.7.0.1
 */