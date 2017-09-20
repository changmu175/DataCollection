/*   1:    */ package org.apache.poi.ss.formula.eval;
/*   2:    */ 
/*   3:    */ import java.util.regex.Pattern;
/*   4:    */ 
/*   5:    */ public final class OperandResolver
/*   6:    */ {
/*   7:    */   private static final String Digits = "(\\p{Digit}+)";
/*   8:    */   private static final String Exp = "[eE][+-]?(\\p{Digit}+)";
/*   9:    */   private static final String fpRegex = "[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?))))[\\x00-\\x20]*";
/*  10:    */   
/*  11:    */   public static ValueEval getSingleValue(ValueEval arg, int srcCellRow, int srcCellCol)
/*  12:    */     throws EvaluationException
/*  13:    */   {
/*  14:    */     ValueEval result;
/*  15:    */     ValueEval result;
/*  16: 61 */     if ((arg instanceof RefEval))
/*  17:    */     {
/*  18: 62 */       result = chooseSingleElementFromRef((RefEval)arg);
/*  19:    */     }
/*  20:    */     else
/*  21:    */     {
/*  22:    */       ValueEval result;
/*  23: 63 */       if ((arg instanceof AreaEval)) {
/*  24: 64 */         result = chooseSingleElementFromArea((AreaEval)arg, srcCellRow, srcCellCol);
/*  25:    */       } else {
/*  26: 66 */         result = arg;
/*  27:    */       }
/*  28:    */     }
/*  29: 68 */     if ((result instanceof ErrorEval)) {
/*  30: 69 */       throw new EvaluationException((ErrorEval)result);
/*  31:    */     }
/*  32: 71 */     return result;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static ValueEval chooseSingleElementFromArea(AreaEval ae, int srcCellRow, int srcCellCol)
/*  36:    */     throws EvaluationException
/*  37:    */   {
/*  38:120 */     ValueEval result = chooseSingleElementFromAreaInternal(ae, srcCellRow, srcCellCol);
/*  39:121 */     if ((result instanceof ErrorEval)) {
/*  40:122 */       throw new EvaluationException((ErrorEval)result);
/*  41:    */     }
/*  42:124 */     return result;
/*  43:    */   }
/*  44:    */   
/*  45:    */   private static ValueEval chooseSingleElementFromAreaInternal(AreaEval ae, int srcCellRow, int srcCellCol)
/*  46:    */     throws EvaluationException
/*  47:    */   {
/*  48:156 */     if (ae.isColumn())
/*  49:    */     {
/*  50:157 */       if (ae.isRow()) {
/*  51:158 */         return ae.getRelativeValue(0, 0);
/*  52:    */       }
/*  53:160 */       if (!ae.containsRow(srcCellRow)) {
/*  54:161 */         throw EvaluationException.invalidValue();
/*  55:    */       }
/*  56:163 */       return ae.getAbsoluteValue(srcCellRow, ae.getFirstColumn());
/*  57:    */     }
/*  58:165 */     if (!ae.isRow())
/*  59:    */     {
/*  60:167 */       if ((ae.containsRow(srcCellRow)) && (ae.containsColumn(srcCellCol))) {
/*  61:168 */         return ae.getAbsoluteValue(ae.getFirstRow(), ae.getFirstColumn());
/*  62:    */       }
/*  63:170 */       throw EvaluationException.invalidValue();
/*  64:    */     }
/*  65:172 */     if (!ae.containsColumn(srcCellCol)) {
/*  66:173 */       throw EvaluationException.invalidValue();
/*  67:    */     }
/*  68:175 */     return ae.getAbsoluteValue(ae.getFirstRow(), srcCellCol);
/*  69:    */   }
/*  70:    */   
/*  71:    */   private static ValueEval chooseSingleElementFromRef(RefEval ref)
/*  72:    */   {
/*  73:179 */     return ref.getInnerValueEval(ref.getFirstSheetIndex());
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static int coerceValueToInt(ValueEval ev)
/*  77:    */     throws EvaluationException
/*  78:    */   {
/*  79:195 */     if (ev == BlankEval.instance) {
/*  80:196 */       return 0;
/*  81:    */     }
/*  82:198 */     double d = coerceValueToDouble(ev);
/*  83:    */     
/*  84:    */ 
/*  85:201 */     return (int)Math.floor(d);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public static double coerceValueToDouble(ValueEval ev)
/*  89:    */     throws EvaluationException
/*  90:    */   {
/*  91:217 */     if (ev == BlankEval.instance) {
/*  92:218 */       return 0.0D;
/*  93:    */     }
/*  94:220 */     if ((ev instanceof NumericValueEval)) {
/*  95:222 */       return ((NumericValueEval)ev).getNumberValue();
/*  96:    */     }
/*  97:224 */     if ((ev instanceof StringEval))
/*  98:    */     {
/*  99:225 */       Double dd = parseDouble(((StringEval)ev).getStringValue());
/* 100:226 */       if (dd == null) {
/* 101:227 */         throw EvaluationException.invalidValue();
/* 102:    */       }
/* 103:229 */       return dd.doubleValue();
/* 104:    */     }
/* 105:231 */     throw new RuntimeException("Unexpected arg eval type (" + ev.getClass().getName() + ")");
/* 106:    */   }
/* 107:    */   
/* 108:    */   public static Double parseDouble(String pText)
/* 109:    */   {
/* 110:255 */     if (Pattern.matches("[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?))))[\\x00-\\x20]*", pText)) {
/* 111:    */       try
/* 112:    */       {
/* 113:257 */         return Double.valueOf(Double.parseDouble(pText));
/* 114:    */       }
/* 115:    */       catch (NumberFormatException e)
/* 116:    */       {
/* 117:259 */         return null;
/* 118:    */       }
/* 119:    */     }
/* 120:262 */     return null;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public static String coerceValueToString(ValueEval ve)
/* 124:    */   {
/* 125:272 */     if ((ve instanceof StringValueEval))
/* 126:    */     {
/* 127:273 */       StringValueEval sve = (StringValueEval)ve;
/* 128:274 */       return sve.getStringValue();
/* 129:    */     }
/* 130:276 */     if (ve == BlankEval.instance) {
/* 131:277 */       return "";
/* 132:    */     }
/* 133:279 */     throw new IllegalArgumentException("Unexpected eval class (" + ve.getClass().getName() + ")");
/* 134:    */   }
/* 135:    */   
/* 136:    */   public static Boolean coerceValueToBoolean(ValueEval ve, boolean stringsAreBlanks)
/* 137:    */     throws EvaluationException
/* 138:    */   {
/* 139:288 */     if ((ve == null) || (ve == BlankEval.instance)) {
/* 140:290 */       return null;
/* 141:    */     }
/* 142:292 */     if ((ve instanceof BoolEval)) {
/* 143:293 */       return Boolean.valueOf(((BoolEval)ve).getBooleanValue());
/* 144:    */     }
/* 145:296 */     if (ve == BlankEval.instance) {
/* 146:297 */       return null;
/* 147:    */     }
/* 148:300 */     if ((ve instanceof StringEval))
/* 149:    */     {
/* 150:301 */       if (stringsAreBlanks) {
/* 151:302 */         return null;
/* 152:    */       }
/* 153:304 */       String str = ((StringEval)ve).getStringValue();
/* 154:305 */       if (str.equalsIgnoreCase("true")) {
/* 155:306 */         return Boolean.TRUE;
/* 156:    */       }
/* 157:308 */       if (str.equalsIgnoreCase("false")) {
/* 158:309 */         return Boolean.FALSE;
/* 159:    */       }
/* 160:312 */       throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 161:    */     }
/* 162:315 */     if ((ve instanceof NumericValueEval))
/* 163:    */     {
/* 164:316 */       NumericValueEval ne = (NumericValueEval)ve;
/* 165:317 */       double d = ne.getNumberValue();
/* 166:318 */       if (Double.isNaN(d)) {
/* 167:319 */         throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 168:    */       }
/* 169:321 */       return Boolean.valueOf(d != 0.0D);
/* 170:    */     }
/* 171:323 */     if ((ve instanceof ErrorEval)) {
/* 172:324 */       throw new EvaluationException((ErrorEval)ve);
/* 173:    */     }
/* 174:326 */     throw new RuntimeException("Unexpected eval (" + ve.getClass().getName() + ")");
/* 175:    */   }
/* 176:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.OperandResolver
 * JD-Core Version:    0.7.0.1
 */