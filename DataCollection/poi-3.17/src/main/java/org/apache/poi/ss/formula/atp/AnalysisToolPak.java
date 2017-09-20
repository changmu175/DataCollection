/*   1:    */ package org.apache.poi.ss.formula.atp;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Locale;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import java.util.TreeSet;
/*  10:    */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*  11:    */ import org.apache.poi.ss.formula.eval.NotImplementedFunctionException;
/*  12:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  13:    */ import org.apache.poi.ss.formula.function.FunctionMetadata;
/*  14:    */ import org.apache.poi.ss.formula.function.FunctionMetadataRegistry;
/*  15:    */ import org.apache.poi.ss.formula.functions.Bin2Dec;
/*  16:    */ import org.apache.poi.ss.formula.functions.Complex;
/*  17:    */ import org.apache.poi.ss.formula.functions.Countifs;
/*  18:    */ import org.apache.poi.ss.formula.functions.Dec2Bin;
/*  19:    */ import org.apache.poi.ss.formula.functions.Dec2Hex;
/*  20:    */ import org.apache.poi.ss.formula.functions.Delta;
/*  21:    */ import org.apache.poi.ss.formula.functions.EDate;
/*  22:    */ import org.apache.poi.ss.formula.functions.EOMonth;
/*  23:    */ import org.apache.poi.ss.formula.functions.FactDouble;
/*  24:    */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/*  25:    */ import org.apache.poi.ss.formula.functions.Hex2Dec;
/*  26:    */ import org.apache.poi.ss.formula.functions.ImReal;
/*  27:    */ import org.apache.poi.ss.formula.functions.Imaginary;
/*  28:    */ import org.apache.poi.ss.formula.functions.Oct2Dec;
/*  29:    */ import org.apache.poi.ss.formula.functions.Quotient;
/*  30:    */ import org.apache.poi.ss.formula.functions.Sumifs;
/*  31:    */ import org.apache.poi.ss.formula.functions.WeekNum;
/*  32:    */ import org.apache.poi.ss.formula.udf.UDFFinder;
/*  33:    */ 
/*  34:    */ public final class AnalysisToolPak
/*  35:    */   implements UDFFinder
/*  36:    */ {
/*  37: 49 */   public static final UDFFinder instance = new AnalysisToolPak();
/*  38:    */   
/*  39:    */   private static final class NotImplemented
/*  40:    */     implements FreeRefFunction
/*  41:    */   {
/*  42:    */     private final String _functionName;
/*  43:    */     
/*  44:    */     public NotImplemented(String functionName)
/*  45:    */     {
/*  46: 55 */       this._functionName = functionName;
/*  47:    */     }
/*  48:    */     
/*  49:    */     public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/*  50:    */     {
/*  51: 59 */       throw new NotImplementedFunctionException(this._functionName);
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55: 63 */   private final Map<String, FreeRefFunction> _functionsByName = createFunctionsMap();
/*  56:    */   
/*  57:    */   public FreeRefFunction findFunction(String name)
/*  58:    */   {
/*  59: 72 */     String prefix = "_xlfn.";
/*  60: 74 */     if (name.startsWith("_xlfn.")) {
/*  61: 74 */       name = name.substring("_xlfn.".length());
/*  62:    */     }
/*  63: 77 */     return (FreeRefFunction)this._functionsByName.get(name.toUpperCase(Locale.ROOT));
/*  64:    */   }
/*  65:    */   
/*  66:    */   private Map<String, FreeRefFunction> createFunctionsMap()
/*  67:    */   {
/*  68: 81 */     Map<String, FreeRefFunction> m = new HashMap(108);
/*  69:    */     
/*  70: 83 */     r(m, "ACCRINT", null);
/*  71: 84 */     r(m, "ACCRINTM", null);
/*  72: 85 */     r(m, "AMORDEGRC", null);
/*  73: 86 */     r(m, "AMORLINC", null);
/*  74: 87 */     r(m, "AVERAGEIF", null);
/*  75: 88 */     r(m, "AVERAGEIFS", null);
/*  76: 89 */     r(m, "BAHTTEXT", null);
/*  77: 90 */     r(m, "BESSELI", null);
/*  78: 91 */     r(m, "BESSELJ", null);
/*  79: 92 */     r(m, "BESSELK", null);
/*  80: 93 */     r(m, "BESSELY", null);
/*  81: 94 */     r(m, "BIN2DEC", Bin2Dec.instance);
/*  82: 95 */     r(m, "BIN2HEX", null);
/*  83: 96 */     r(m, "BIN2OCT", null);
/*  84: 97 */     r(m, "COMPLEX", Complex.instance);
/*  85: 98 */     r(m, "CONVERT", null);
/*  86: 99 */     r(m, "COUNTIFS", Countifs.instance);
/*  87:100 */     r(m, "COUPDAYBS", null);
/*  88:101 */     r(m, "COUPDAYS", null);
/*  89:102 */     r(m, "COUPDAYSNC", null);
/*  90:103 */     r(m, "COUPNCD", null);
/*  91:104 */     r(m, "COUPNUM", null);
/*  92:105 */     r(m, "COUPPCD", null);
/*  93:106 */     r(m, "CUBEKPIMEMBER", null);
/*  94:107 */     r(m, "CUBEMEMBER", null);
/*  95:108 */     r(m, "CUBEMEMBERPROPERTY", null);
/*  96:109 */     r(m, "CUBERANKEDMEMBER", null);
/*  97:110 */     r(m, "CUBESET", null);
/*  98:111 */     r(m, "CUBESETCOUNT", null);
/*  99:112 */     r(m, "CUBEVALUE", null);
/* 100:113 */     r(m, "CUMIPMT", null);
/* 101:114 */     r(m, "CUMPRINC", null);
/* 102:115 */     r(m, "DEC2BIN", Dec2Bin.instance);
/* 103:116 */     r(m, "DEC2HEX", Dec2Hex.instance);
/* 104:117 */     r(m, "DEC2OCT", null);
/* 105:118 */     r(m, "DELTA", Delta.instance);
/* 106:119 */     r(m, "DISC", null);
/* 107:120 */     r(m, "DOLLARDE", null);
/* 108:121 */     r(m, "DOLLARFR", null);
/* 109:122 */     r(m, "DURATION", null);
/* 110:123 */     r(m, "EDATE", EDate.instance);
/* 111:124 */     r(m, "EFFECT", null);
/* 112:125 */     r(m, "EOMONTH", EOMonth.instance);
/* 113:126 */     r(m, "ERF", null);
/* 114:127 */     r(m, "ERFC", null);
/* 115:128 */     r(m, "FACTDOUBLE", FactDouble.instance);
/* 116:129 */     r(m, "FVSCHEDULE", null);
/* 117:130 */     r(m, "GCD", null);
/* 118:131 */     r(m, "GESTEP", null);
/* 119:132 */     r(m, "HEX2BIN", null);
/* 120:133 */     r(m, "HEX2DEC", Hex2Dec.instance);
/* 121:134 */     r(m, "HEX2OCT", null);
/* 122:135 */     r(m, "IFERROR", IfError.instance);
/* 123:136 */     r(m, "IMABS", null);
/* 124:137 */     r(m, "IMAGINARY", Imaginary.instance);
/* 125:138 */     r(m, "IMARGUMENT", null);
/* 126:139 */     r(m, "IMCONJUGATE", null);
/* 127:140 */     r(m, "IMCOS", null);
/* 128:141 */     r(m, "IMDIV", null);
/* 129:142 */     r(m, "IMEXP", null);
/* 130:143 */     r(m, "IMLN", null);
/* 131:144 */     r(m, "IMLOG10", null);
/* 132:145 */     r(m, "IMLOG2", null);
/* 133:146 */     r(m, "IMPOWER", null);
/* 134:147 */     r(m, "IMPRODUCT", null);
/* 135:148 */     r(m, "IMREAL", ImReal.instance);
/* 136:149 */     r(m, "IMSIN", null);
/* 137:150 */     r(m, "IMSQRT", null);
/* 138:151 */     r(m, "IMSUB", null);
/* 139:152 */     r(m, "IMSUM", null);
/* 140:153 */     r(m, "INTRATE", null);
/* 141:154 */     r(m, "ISEVEN", ParityFunction.IS_EVEN);
/* 142:155 */     r(m, "ISODD", ParityFunction.IS_ODD);
/* 143:156 */     r(m, "JIS", null);
/* 144:157 */     r(m, "LCM", null);
/* 145:158 */     r(m, "MDURATION", null);
/* 146:159 */     r(m, "MROUND", MRound.instance);
/* 147:160 */     r(m, "MULTINOMIAL", null);
/* 148:161 */     r(m, "NETWORKDAYS", NetworkdaysFunction.instance);
/* 149:162 */     r(m, "NOMINAL", null);
/* 150:163 */     r(m, "OCT2BIN", null);
/* 151:164 */     r(m, "OCT2DEC", Oct2Dec.instance);
/* 152:165 */     r(m, "OCT2HEX", null);
/* 153:166 */     r(m, "ODDFPRICE", null);
/* 154:167 */     r(m, "ODDFYIELD", null);
/* 155:168 */     r(m, "ODDLPRICE", null);
/* 156:169 */     r(m, "ODDLYIELD", null);
/* 157:170 */     r(m, "PRICE", null);
/* 158:171 */     r(m, "PRICEDISC", null);
/* 159:172 */     r(m, "PRICEMAT", null);
/* 160:173 */     r(m, "QUOTIENT", Quotient.instance);
/* 161:174 */     r(m, "RANDBETWEEN", RandBetween.instance);
/* 162:175 */     r(m, "RECEIVED", null);
/* 163:176 */     r(m, "RTD", null);
/* 164:177 */     r(m, "SERIESSUM", null);
/* 165:178 */     r(m, "SQRTPI", null);
/* 166:179 */     r(m, "SUMIFS", Sumifs.instance);
/* 167:180 */     r(m, "TBILLEQ", null);
/* 168:181 */     r(m, "TBILLPRICE", null);
/* 169:182 */     r(m, "TBILLYIELD", null);
/* 170:183 */     r(m, "WEEKNUM", WeekNum.instance);
/* 171:184 */     r(m, "WORKDAY", WorkdayFunction.instance);
/* 172:185 */     r(m, "XIRR", null);
/* 173:186 */     r(m, "XNPV", null);
/* 174:187 */     r(m, "YEARFRAC", YearFrac.instance);
/* 175:188 */     r(m, "YIELD", null);
/* 176:189 */     r(m, "YIELDDISC", null);
/* 177:190 */     r(m, "YIELDMAT", null);
/* 178:    */     
/* 179:192 */     return m;
/* 180:    */   }
/* 181:    */   
/* 182:    */   private static void r(Map<String, FreeRefFunction> m, String functionName, FreeRefFunction pFunc)
/* 183:    */   {
/* 184:196 */     FreeRefFunction func = pFunc == null ? new NotImplemented(functionName) : pFunc;
/* 185:197 */     m.put(functionName, func);
/* 186:    */   }
/* 187:    */   
/* 188:    */   public static boolean isATPFunction(String name)
/* 189:    */   {
/* 190:201 */     AnalysisToolPak inst = (AnalysisToolPak)instance;
/* 191:    */     
/* 192:203 */     return inst._functionsByName.containsKey(name);
/* 193:    */   }
/* 194:    */   
/* 195:    */   public static Collection<String> getSupportedFunctionNames()
/* 196:    */   {
/* 197:213 */     AnalysisToolPak inst = (AnalysisToolPak)instance;
/* 198:214 */     Collection<String> lst = new TreeSet();
/* 199:215 */     for (Entry<String, FreeRefFunction> me : inst._functionsByName.entrySet())
/* 200:    */     {
/* 201:216 */       FreeRefFunction func = (FreeRefFunction)me.getValue();
/* 202:217 */       if ((func != null) && (!(func instanceof NotImplemented))) {
/* 203:218 */         lst.add(me.getKey());
/* 204:    */       }
/* 205:    */     }
/* 206:221 */     return Collections.unmodifiableCollection(lst);
/* 207:    */   }
/* 208:    */   
/* 209:    */   public static Collection<String> getNotSupportedFunctionNames()
/* 210:    */   {
/* 211:231 */     AnalysisToolPak inst = (AnalysisToolPak)instance;
/* 212:232 */     Collection<String> lst = new TreeSet();
/* 213:233 */     for (Entry<String, FreeRefFunction> me : inst._functionsByName.entrySet())
/* 214:    */     {
/* 215:234 */       FreeRefFunction func = (FreeRefFunction)me.getValue();
/* 216:235 */       if ((func instanceof NotImplemented)) {
/* 217:236 */         lst.add(me.getKey());
/* 218:    */       }
/* 219:    */     }
/* 220:239 */     return Collections.unmodifiableCollection(lst);
/* 221:    */   }
/* 222:    */   
/* 223:    */   public static void registerFunction(String name, FreeRefFunction func)
/* 224:    */   {
/* 225:251 */     AnalysisToolPak inst = (AnalysisToolPak)instance;
/* 226:252 */     if (!isATPFunction(name))
/* 227:    */     {
/* 228:253 */       FunctionMetadata metaData = FunctionMetadataRegistry.getFunctionByName(name);
/* 229:254 */       if (metaData != null) {
/* 230:255 */         throw new IllegalArgumentException(name + " is a built-in Excel function. " + "Use FunctoinEval.registerFunction(String name, Function func) instead.");
/* 231:    */       }
/* 232:259 */       throw new IllegalArgumentException(name + " is not a function from the Excel Analysis Toolpack.");
/* 233:    */     }
/* 234:261 */     FreeRefFunction f = inst.findFunction(name);
/* 235:262 */     if ((f != null) && (!(f instanceof NotImplemented))) {
/* 236:263 */       throw new IllegalArgumentException("POI already implememts " + name + ". You cannot override POI's implementations of Excel functions");
/* 237:    */     }
/* 238:268 */     inst._functionsByName.put(name, func);
/* 239:    */   }
/* 240:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.formula.atp.AnalysisToolPak

 * JD-Core Version:    0.7.0.1

 */