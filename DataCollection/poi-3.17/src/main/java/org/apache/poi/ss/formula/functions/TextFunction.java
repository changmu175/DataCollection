/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   7:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   9:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  10:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  11:    */ import org.apache.poi.ss.usermodel.DataFormatter;
/*  12:    */ 
/*  13:    */ public abstract class TextFunction
/*  14:    */   implements Function
/*  15:    */ {
/*  16: 31 */   protected static final DataFormatter formatter = new DataFormatter();
/*  17:    */   
/*  18:    */   protected static String evaluateStringArg(ValueEval eval, int srcRow, int srcCol)
/*  19:    */     throws EvaluationException
/*  20:    */   {
/*  21: 34 */     ValueEval ve = OperandResolver.getSingleValue(eval, srcRow, srcCol);
/*  22: 35 */     return OperandResolver.coerceValueToString(ve);
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected static int evaluateIntArg(ValueEval arg, int srcCellRow, int srcCellCol)
/*  26:    */     throws EvaluationException
/*  27:    */   {
/*  28: 38 */     ValueEval ve = OperandResolver.getSingleValue(arg, srcCellRow, srcCellCol);
/*  29: 39 */     return OperandResolver.coerceValueToInt(ve);
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected static double evaluateDoubleArg(ValueEval arg, int srcCellRow, int srcCellCol)
/*  33:    */     throws EvaluationException
/*  34:    */   {
/*  35: 43 */     ValueEval ve = OperandResolver.getSingleValue(arg, srcCellRow, srcCellCol);
/*  36: 44 */     return OperandResolver.coerceValueToDouble(ve);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public final ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol)
/*  40:    */   {
/*  41:    */     try
/*  42:    */     {
/*  43: 49 */       return evaluateFunc(args, srcCellRow, srcCellCol);
/*  44:    */     }
/*  45:    */     catch (EvaluationException e)
/*  46:    */     {
/*  47: 51 */       return e.getErrorEval();
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected abstract ValueEval evaluateFunc(ValueEval[] paramArrayOfValueEval, int paramInt1, int paramInt2)
/*  52:    */     throws EvaluationException;
/*  53:    */   
/*  54:    */   private static abstract class SingleArgTextFunc
/*  55:    */     extends Fixed1ArgFunction
/*  56:    */   {
/*  57:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/*  58:    */     {
/*  59:    */       String arg;
/*  60:    */       try
/*  61:    */       {
/*  62: 67 */         arg = TextFunction.evaluateStringArg(arg0, srcRowIndex, srcColumnIndex);
/*  63:    */       }
/*  64:    */       catch (EvaluationException e)
/*  65:    */       {
/*  66: 69 */         return e.getErrorEval();
/*  67:    */       }
/*  68: 71 */       return evaluate(arg);
/*  69:    */     }
/*  70:    */     
/*  71:    */     protected abstract ValueEval evaluate(String paramString);
/*  72:    */   }
/*  73:    */   
/*  74: 79 */   public static final Function CHAR = new Fixed1ArgFunction()
/*  75:    */   {
/*  76:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/*  77:    */     {
/*  78:    */       int arg;
/*  79:    */       try
/*  80:    */       {
/*  81: 83 */         arg = TextFunction.evaluateIntArg(arg0, srcRowIndex, srcColumnIndex);
/*  82: 84 */         if ((arg < 0) || (arg >= 256)) {
/*  83: 85 */           throw new EvaluationException(ErrorEval.VALUE_INVALID);
/*  84:    */         }
/*  85:    */       }
/*  86:    */       catch (EvaluationException e)
/*  87:    */       {
/*  88: 89 */         return e.getErrorEval();
/*  89:    */       }
/*  90: 91 */       return new StringEval(String.valueOf((char)arg));
/*  91:    */     }
/*  92:    */   };
/*  93: 95 */   public static final Function LEN = new SingleArgTextFunc()
/*  94:    */   {
/*  95:    */     protected ValueEval evaluate(String arg)
/*  96:    */     {
/*  97: 97 */       return new NumberEval(arg.length());
/*  98:    */     }
/*  99:    */   };
/* 100:100 */   public static final Function LOWER = new SingleArgTextFunc()
/* 101:    */   {
/* 102:    */     protected ValueEval evaluate(String arg)
/* 103:    */     {
/* 104:102 */       return new StringEval(arg.toLowerCase(Locale.ROOT));
/* 105:    */     }
/* 106:    */   };
/* 107:105 */   public static final Function UPPER = new SingleArgTextFunc()
/* 108:    */   {
/* 109:    */     protected ValueEval evaluate(String arg)
/* 110:    */     {
/* 111:107 */       return new StringEval(arg.toUpperCase(Locale.ROOT));
/* 112:    */     }
/* 113:    */   };
/* 114:118 */   public static final Function PROPER = new SingleArgTextFunc()
/* 115:    */   {
/* 116:    */     protected ValueEval evaluate(String text)
/* 117:    */     {
/* 118:120 */       StringBuilder sb = new StringBuilder();
/* 119:121 */       boolean shouldMakeUppercase = true;
/* 120:122 */       for (char ch : text.toCharArray())
/* 121:    */       {
/* 122:127 */         if (shouldMakeUppercase) {
/* 123:128 */           sb.append(String.valueOf(ch).toUpperCase(Locale.ROOT));
/* 124:    */         } else {
/* 125:131 */           sb.append(String.valueOf(ch).toLowerCase(Locale.ROOT));
/* 126:    */         }
/* 127:133 */         shouldMakeUppercase = !Character.isLetter(ch);
/* 128:    */       }
/* 129:135 */       return new StringEval(sb.toString());
/* 130:    */     }
/* 131:    */   };
/* 132:145 */   public static final Function TRIM = new SingleArgTextFunc()
/* 133:    */   {
/* 134:    */     protected ValueEval evaluate(String arg)
/* 135:    */     {
/* 136:147 */       return new StringEval(arg.trim());
/* 137:    */     }
/* 138:    */   };
/* 139:157 */   public static final Function CLEAN = new SingleArgTextFunc()
/* 140:    */   {
/* 141:    */     protected ValueEval evaluate(String arg)
/* 142:    */     {
/* 143:159 */       StringBuilder result = new StringBuilder();
/* 144:160 */       for (char c : arg.toCharArray()) {
/* 145:161 */         if (isPrintable(c)) {
/* 146:162 */           result.append(c);
/* 147:    */         }
/* 148:    */       }
/* 149:165 */       return new StringEval(result.toString());
/* 150:    */     }
/* 151:    */     
/* 152:    */     private boolean isPrintable(char c)
/* 153:    */     {
/* 154:180 */       return c >= ' ';
/* 155:    */     }
/* 156:    */   };
/* 157:194 */   public static final Function MID = new Fixed3ArgFunction()
/* 158:    */   {
/* 159:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/* 160:    */     {
/* 161:    */       String text;
/* 162:    */       int startCharNum;
/* 163:    */       int numChars;
/* 164:    */       try
/* 165:    */       {
/* 166:202 */         text = TextFunction.evaluateStringArg(arg0, srcRowIndex, srcColumnIndex);
/* 167:203 */         startCharNum = TextFunction.evaluateIntArg(arg1, srcRowIndex, srcColumnIndex);
/* 168:204 */         numChars = TextFunction.evaluateIntArg(arg2, srcRowIndex, srcColumnIndex);
/* 169:    */       }
/* 170:    */       catch (EvaluationException e)
/* 171:    */       {
/* 172:206 */         return e.getErrorEval();
/* 173:    */       }
/* 174:208 */       int startIx = startCharNum - 1;
/* 175:212 */       if (startIx < 0) {
/* 176:213 */         return ErrorEval.VALUE_INVALID;
/* 177:    */       }
/* 178:215 */       if (numChars < 0) {
/* 179:216 */         return ErrorEval.VALUE_INVALID;
/* 180:    */       }
/* 181:218 */       int len = text.length();
/* 182:219 */       if ((numChars < 0) || (startIx > len)) {
/* 183:220 */         return new StringEval("");
/* 184:    */       }
/* 185:222 */       int endIx = Math.min(startIx + numChars, len);
/* 186:223 */       String result = text.substring(startIx, endIx);
/* 187:224 */       return new StringEval(result);
/* 188:    */     }
/* 189:    */   };
/* 190:    */   
/* 191:    */   private static final class LeftRight
/* 192:    */     extends Var1or2ArgFunction
/* 193:    */   {
/* 194:229 */     private static final ValueEval DEFAULT_ARG1 = new NumberEval(1.0D);
/* 195:    */     private final boolean _isLeft;
/* 196:    */     
/* 197:    */     protected LeftRight(boolean isLeft)
/* 198:    */     {
/* 199:232 */       this._isLeft = isLeft;
/* 200:    */     }
/* 201:    */     
/* 202:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/* 203:    */     {
/* 204:235 */       return evaluate(srcRowIndex, srcColumnIndex, arg0, DEFAULT_ARG1);
/* 205:    */     }
/* 206:    */     
/* 207:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 208:    */     {
/* 209:    */       String arg;
/* 210:    */       int index;
/* 211:    */       try
/* 212:    */       {
/* 213:242 */         arg = TextFunction.evaluateStringArg(arg0, srcRowIndex, srcColumnIndex);
/* 214:243 */         index = TextFunction.evaluateIntArg(arg1, srcRowIndex, srcColumnIndex);
/* 215:    */       }
/* 216:    */       catch (EvaluationException e)
/* 217:    */       {
/* 218:245 */         return e.getErrorEval();
/* 219:    */       }
/* 220:248 */       if (index < 0) {
/* 221:249 */         return ErrorEval.VALUE_INVALID;
/* 222:    */       }
/* 223:    */       String result;
/* 224:    */       String result;
/* 225:253 */       if (this._isLeft) {
/* 226:254 */         result = arg.substring(0, Math.min(arg.length(), index));
/* 227:    */       } else {
/* 228:256 */         result = arg.substring(Math.max(0, arg.length() - index));
/* 229:    */       }
/* 230:258 */       return new StringEval(result);
/* 231:    */     }
/* 232:    */   }
/* 233:    */   
/* 234:262 */   public static final Function LEFT = new LeftRight(true);
/* 235:263 */   public static final Function RIGHT = new LeftRight(false);
/* 236:265 */   public static final Function CONCATENATE = new Function()
/* 237:    */   {
/* 238:    */     public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex)
/* 239:    */     {
/* 240:268 */       StringBuilder sb = new StringBuilder();
/* 241:269 */       for (ValueEval arg : args) {
/* 242:    */         try
/* 243:    */         {
/* 244:271 */           sb.append(TextFunction.evaluateStringArg(arg, srcRowIndex, srcColumnIndex));
/* 245:    */         }
/* 246:    */         catch (EvaluationException e)
/* 247:    */         {
/* 248:273 */           return e.getErrorEval();
/* 249:    */         }
/* 250:    */       }
/* 251:276 */       return new StringEval(sb.toString());
/* 252:    */     }
/* 253:    */   };
/* 254:280 */   public static final Function EXACT = new Fixed2ArgFunction()
/* 255:    */   {
/* 256:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 257:    */     {
/* 258:    */       String s0;
/* 259:    */       String s1;
/* 260:    */       try
/* 261:    */       {
/* 262:287 */         s0 = TextFunction.evaluateStringArg(arg0, srcRowIndex, srcColumnIndex);
/* 263:288 */         s1 = TextFunction.evaluateStringArg(arg1, srcRowIndex, srcColumnIndex);
/* 264:    */       }
/* 265:    */       catch (EvaluationException e)
/* 266:    */       {
/* 267:290 */         return e.getErrorEval();
/* 268:    */       }
/* 269:292 */       return BoolEval.valueOf(s0.equals(s1));
/* 270:    */     }
/* 271:    */   };
/* 272:306 */   public static final Function TEXT = new Fixed2ArgFunction()
/* 273:    */   {
/* 274:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 275:    */     {
/* 276:    */       double s0;
/* 277:    */       String s1;
/* 278:    */       try
/* 279:    */       {
/* 280:312 */         s0 = TextFunction.evaluateDoubleArg(arg0, srcRowIndex, srcColumnIndex);
/* 281:313 */         s1 = TextFunction.evaluateStringArg(arg1, srcRowIndex, srcColumnIndex);
/* 282:    */       }
/* 283:    */       catch (EvaluationException e)
/* 284:    */       {
/* 285:315 */         return e.getErrorEval();
/* 286:    */       }
/* 287:    */       try
/* 288:    */       {
/* 289:320 */         String formattedStr = TextFunction.formatter.formatRawCellContents(s0, -1, s1);
/* 290:321 */         return new StringEval(formattedStr);
/* 291:    */       }
/* 292:    */       catch (Exception e) {}
/* 293:323 */       return ErrorEval.VALUE_INVALID;
/* 294:    */     }
/* 295:    */   };
/* 296:    */   
/* 297:    */   private static final class SearchFind
/* 298:    */     extends Var2or3ArgFunction
/* 299:    */   {
/* 300:    */     private final boolean _isCaseSensitive;
/* 301:    */     
/* 302:    */     public SearchFind(boolean isCaseSensitive)
/* 303:    */     {
/* 304:333 */       this._isCaseSensitive = isCaseSensitive;
/* 305:    */     }
/* 306:    */     
/* 307:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 308:    */     {
/* 309:    */       try
/* 310:    */       {
/* 311:337 */         String needle = TextFunction.evaluateStringArg(arg0, srcRowIndex, srcColumnIndex);
/* 312:338 */         String haystack = TextFunction.evaluateStringArg(arg1, srcRowIndex, srcColumnIndex);
/* 313:339 */         return eval(haystack, needle, 0);
/* 314:    */       }
/* 315:    */       catch (EvaluationException e)
/* 316:    */       {
/* 317:341 */         return e.getErrorEval();
/* 318:    */       }
/* 319:    */     }
/* 320:    */     
/* 321:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/* 322:    */     {
/* 323:    */       try
/* 324:    */       {
/* 325:347 */         String needle = TextFunction.evaluateStringArg(arg0, srcRowIndex, srcColumnIndex);
/* 326:348 */         String haystack = TextFunction.evaluateStringArg(arg1, srcRowIndex, srcColumnIndex);
/* 327:    */         
/* 328:350 */         int startpos = TextFunction.evaluateIntArg(arg2, srcRowIndex, srcColumnIndex) - 1;
/* 329:351 */         if (startpos < 0) {
/* 330:352 */           return ErrorEval.VALUE_INVALID;
/* 331:    */         }
/* 332:354 */         return eval(haystack, needle, startpos);
/* 333:    */       }
/* 334:    */       catch (EvaluationException e)
/* 335:    */       {
/* 336:356 */         return e.getErrorEval();
/* 337:    */       }
/* 338:    */     }
/* 339:    */     
/* 340:    */     private ValueEval eval(String haystack, String needle, int startIndex)
/* 341:    */     {
/* 342:    */       int result;
/* 343:    */       int result;
/* 344:361 */       if (this._isCaseSensitive) {
/* 345:362 */         result = haystack.indexOf(needle, startIndex);
/* 346:    */       } else {
/* 347:364 */         result = haystack.toUpperCase(Locale.ROOT).indexOf(needle.toUpperCase(Locale.ROOT), startIndex);
/* 348:    */       }
/* 349:367 */       if (result == -1) {
/* 350:368 */         return ErrorEval.VALUE_INVALID;
/* 351:    */       }
/* 352:370 */       return new NumberEval(result + 1);
/* 353:    */     }
/* 354:    */   }
/* 355:    */   
/* 356:386 */   public static final Function FIND = new SearchFind(true);
/* 357:395 */   public static final Function SEARCH = new SearchFind(false);
/* 358:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.TextFunction
 * JD-Core Version:    0.7.0.1
 */