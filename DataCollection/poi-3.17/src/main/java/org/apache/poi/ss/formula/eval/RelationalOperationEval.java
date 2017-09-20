/*   1:    */ package org.apache.poi.ss.formula.eval;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.functions.Fixed2ArgFunction;
/*   4:    */ import org.apache.poi.ss.formula.functions.Function;
/*   5:    */ import org.apache.poi.ss.util.NumberComparer;
/*   6:    */ 
/*   7:    */ public abstract class RelationalOperationEval
/*   8:    */   extends Fixed2ArgFunction
/*   9:    */ {
/*  10:    */   protected abstract boolean convertComparisonResult(int paramInt);
/*  11:    */   
/*  12:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/*  13:    */   {
/*  14:    */     ValueEval vA;
/*  15:    */     ValueEval vB;
/*  16:    */     try
/*  17:    */     {
/*  18: 64 */       vA = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
/*  19: 65 */       vB = OperandResolver.getSingleValue(arg1, srcRowIndex, srcColumnIndex);
/*  20:    */     }
/*  21:    */     catch (EvaluationException e)
/*  22:    */     {
/*  23: 67 */       return e.getErrorEval();
/*  24:    */     }
/*  25: 69 */     int cmpResult = doCompare(vA, vB);
/*  26: 70 */     boolean result = convertComparisonResult(cmpResult);
/*  27: 71 */     return BoolEval.valueOf(result);
/*  28:    */   }
/*  29:    */   
/*  30:    */   private static int doCompare(ValueEval va, ValueEval vb)
/*  31:    */   {
/*  32: 76 */     if (va == BlankEval.instance) {
/*  33: 77 */       return compareBlank(vb);
/*  34:    */     }
/*  35: 79 */     if (vb == BlankEval.instance) {
/*  36: 80 */       return -compareBlank(va);
/*  37:    */     }
/*  38: 83 */     if ((va instanceof BoolEval))
/*  39:    */     {
/*  40: 84 */       if ((vb instanceof BoolEval))
/*  41:    */       {
/*  42: 85 */         BoolEval bA = (BoolEval)va;
/*  43: 86 */         BoolEval bB = (BoolEval)vb;
/*  44: 87 */         if (bA.getBooleanValue() == bB.getBooleanValue()) {
/*  45: 88 */           return 0;
/*  46:    */         }
/*  47: 90 */         return bA.getBooleanValue() ? 1 : -1;
/*  48:    */       }
/*  49: 92 */       return 1;
/*  50:    */     }
/*  51: 94 */     if ((vb instanceof BoolEval)) {
/*  52: 95 */       return -1;
/*  53:    */     }
/*  54: 97 */     if ((va instanceof StringEval))
/*  55:    */     {
/*  56: 98 */       if ((vb instanceof StringEval))
/*  57:    */       {
/*  58: 99 */         StringEval sA = (StringEval)va;
/*  59:100 */         StringEval sB = (StringEval)vb;
/*  60:101 */         return sA.getStringValue().compareToIgnoreCase(sB.getStringValue());
/*  61:    */       }
/*  62:103 */       return 1;
/*  63:    */     }
/*  64:105 */     if ((vb instanceof StringEval)) {
/*  65:106 */       return -1;
/*  66:    */     }
/*  67:108 */     if (((va instanceof NumberEval)) && 
/*  68:109 */       ((vb instanceof NumberEval)))
/*  69:    */     {
/*  70:110 */       NumberEval nA = (NumberEval)va;
/*  71:111 */       NumberEval nB = (NumberEval)vb;
/*  72:112 */       return NumberComparer.compare(nA.getNumberValue(), nB.getNumberValue());
/*  73:    */     }
/*  74:115 */     throw new IllegalArgumentException("Bad operand types (" + va.getClass().getName() + "), (" + vb.getClass().getName() + ")");
/*  75:    */   }
/*  76:    */   
/*  77:    */   private static int compareBlank(ValueEval v)
/*  78:    */   {
/*  79:120 */     if (v == BlankEval.instance) {
/*  80:121 */       return 0;
/*  81:    */     }
/*  82:123 */     if ((v instanceof BoolEval))
/*  83:    */     {
/*  84:124 */       BoolEval boolEval = (BoolEval)v;
/*  85:125 */       return boolEval.getBooleanValue() ? -1 : 0;
/*  86:    */     }
/*  87:127 */     if ((v instanceof NumberEval))
/*  88:    */     {
/*  89:128 */       NumberEval ne = (NumberEval)v;
/*  90:129 */       return NumberComparer.compare(0.0D, ne.getNumberValue());
/*  91:    */     }
/*  92:131 */     if ((v instanceof StringEval))
/*  93:    */     {
/*  94:132 */       StringEval se = (StringEval)v;
/*  95:133 */       return se.getStringValue().length() < 1 ? 0 : -1;
/*  96:    */     }
/*  97:135 */     throw new IllegalArgumentException("bad value class (" + v.getClass().getName() + ")");
/*  98:    */   }
/*  99:    */   
/* 100:138 */   public static final Function EqualEval = new RelationalOperationEval()
/* 101:    */   {
/* 102:    */     protected boolean convertComparisonResult(int cmpResult)
/* 103:    */     {
/* 104:140 */       return cmpResult == 0;
/* 105:    */     }
/* 106:    */   };
/* 107:143 */   public static final Function GreaterEqualEval = new RelationalOperationEval()
/* 108:    */   {
/* 109:    */     protected boolean convertComparisonResult(int cmpResult)
/* 110:    */     {
/* 111:145 */       return cmpResult >= 0;
/* 112:    */     }
/* 113:    */   };
/* 114:148 */   public static final Function GreaterThanEval = new RelationalOperationEval()
/* 115:    */   {
/* 116:    */     protected boolean convertComparisonResult(int cmpResult)
/* 117:    */     {
/* 118:150 */       return cmpResult > 0;
/* 119:    */     }
/* 120:    */   };
/* 121:153 */   public static final Function LessEqualEval = new RelationalOperationEval()
/* 122:    */   {
/* 123:    */     protected boolean convertComparisonResult(int cmpResult)
/* 124:    */     {
/* 125:155 */       return cmpResult <= 0;
/* 126:    */     }
/* 127:    */   };
/* 128:158 */   public static final Function LessThanEval = new RelationalOperationEval()
/* 129:    */   {
/* 130:    */     protected boolean convertComparisonResult(int cmpResult)
/* 131:    */     {
/* 132:160 */       return cmpResult < 0;
/* 133:    */     }
/* 134:    */   };
/* 135:163 */   public static final Function NotEqualEval = new RelationalOperationEval()
/* 136:    */   {
/* 137:    */     protected boolean convertComparisonResult(int cmpResult)
/* 138:    */     {
/* 139:165 */       return cmpResult != 0;
/* 140:    */     }
/* 141:    */   };
/* 142:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.RelationalOperationEval
 * JD-Core Version:    0.7.0.1
 */