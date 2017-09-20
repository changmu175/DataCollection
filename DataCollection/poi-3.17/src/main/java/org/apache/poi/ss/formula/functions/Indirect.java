/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import java.util.regex.Matcher;
/*   4:    */ import java.util.regex.Pattern;
/*   5:    */ import org.apache.poi.ss.formula.FormulaParseException;
/*   6:    */ import org.apache.poi.ss.formula.FormulaParser;
/*   7:    */ import org.apache.poi.ss.formula.FormulaParsingWorkbook;
/*   8:    */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*   9:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*  10:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  11:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  12:    */ import org.apache.poi.ss.formula.eval.MissingArgEval;
/*  13:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  14:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  15:    */ import org.apache.poi.ss.formula.ptg.Area3DPxg;
/*  16:    */ import org.apache.poi.ss.usermodel.Table;
/*  17:    */ 
/*  18:    */ public final class Indirect
/*  19:    */   implements FreeRefFunction
/*  20:    */ {
/*  21: 50 */   public static final FreeRefFunction instance = new Indirect();
/*  22:    */   
/*  23:    */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/*  24:    */   {
/*  25: 57 */     if (args.length < 1) {
/*  26: 58 */       return ErrorEval.VALUE_INVALID;
/*  27:    */     }
/*  28:    */     String text;
/*  29:    */     boolean isA1style;
/*  30:    */     try
/*  31:    */     {
/*  32: 64 */       ValueEval ve = OperandResolver.getSingleValue(args[0], ec.getRowIndex(), ec.getColumnIndex());
/*  33:    */       
/*  34: 66 */       text = OperandResolver.coerceValueToString(ve);
/*  35: 67 */       switch (args.length)
/*  36:    */       {
/*  37:    */       case 1: 
/*  38: 69 */         isA1style = true;
/*  39: 70 */         break;
/*  40:    */       case 2: 
/*  41: 72 */         isA1style = evaluateBooleanArg(args[1], ec);
/*  42: 73 */         break;
/*  43:    */       default: 
/*  44: 75 */         return ErrorEval.VALUE_INVALID;
/*  45:    */       }
/*  46:    */     }
/*  47:    */     catch (EvaluationException e)
/*  48:    */     {
/*  49: 78 */       return e.getErrorEval();
/*  50:    */     }
/*  51: 81 */     return evaluateIndirect(ec, text, isA1style);
/*  52:    */   }
/*  53:    */   
/*  54:    */   private static boolean evaluateBooleanArg(ValueEval arg, OperationEvaluationContext ec)
/*  55:    */     throws EvaluationException
/*  56:    */   {
/*  57: 86 */     ValueEval ve = OperandResolver.getSingleValue(arg, ec.getRowIndex(), ec.getColumnIndex());
/*  58: 88 */     if ((ve == BlankEval.instance) || (ve == MissingArgEval.instance)) {
/*  59: 89 */       return false;
/*  60:    */     }
/*  61: 93 */     return OperandResolver.coerceValueToBoolean(ve, false).booleanValue();
/*  62:    */   }
/*  63:    */   
/*  64:    */   private static ValueEval evaluateIndirect(OperationEvaluationContext ec, String text, boolean isA1style)
/*  65:    */   {
/*  66:100 */     int plingPos = text.lastIndexOf('!');
/*  67:    */     String refText;
/*  68:    */     String workbookName;
/*  69:    */     String sheetName;
/*  70:    */     String refText;
/*  71:105 */     if (plingPos < 0)
/*  72:    */     {
/*  73:106 */       String workbookName = null;
/*  74:107 */       String sheetName = null;
/*  75:108 */       refText = text;
/*  76:    */     }
/*  77:    */     else
/*  78:    */     {
/*  79:110 */       String[] parts = parseWorkbookAndSheetName(text.subSequence(0, plingPos));
/*  80:111 */       if (parts == null) {
/*  81:112 */         return ErrorEval.REF_INVALID;
/*  82:    */       }
/*  83:114 */       workbookName = parts[0];
/*  84:115 */       sheetName = parts[1];
/*  85:116 */       refText = text.substring(plingPos + 1);
/*  86:    */     }
/*  87:119 */     if (Table.isStructuredReference.matcher(refText).matches())
/*  88:    */     {
/*  89:121 */       Area3DPxg areaPtg = null;
/*  90:    */       try
/*  91:    */       {
/*  92:123 */         areaPtg = FormulaParser.parseStructuredReference(refText, (FormulaParsingWorkbook)ec.getWorkbook(), ec.getRowIndex());
/*  93:    */       }
/*  94:    */       catch (FormulaParseException e)
/*  95:    */       {
/*  96:125 */         return ErrorEval.REF_INVALID;
/*  97:    */       }
/*  98:127 */       return ec.getArea3DEval(areaPtg);
/*  99:    */     }
/* 100:132 */     int colonPos = refText.indexOf(':');
/* 101:    */     String refStrPart2;
/* 102:    */     String refStrPart1;
/* 103:    */     String refStrPart2;
/* 104:133 */     if (colonPos < 0)
/* 105:    */     {
/* 106:134 */       String refStrPart1 = refText.trim();
/* 107:135 */       refStrPart2 = null;
/* 108:    */     }
/* 109:    */     else
/* 110:    */     {
/* 111:137 */       refStrPart1 = refText.substring(0, colonPos).trim();
/* 112:138 */       refStrPart2 = refText.substring(colonPos + 1).trim();
/* 113:    */     }
/* 114:140 */     return ec.getDynamicReference(workbookName, sheetName, refStrPart1, refStrPart2, isA1style);
/* 115:    */   }
/* 116:    */   
/* 117:    */   private static String[] parseWorkbookAndSheetName(CharSequence text)
/* 118:    */   {
/* 119:150 */     int lastIx = text.length() - 1;
/* 120:151 */     if (lastIx < 0) {
/* 121:152 */       return null;
/* 122:    */     }
/* 123:154 */     if (canTrim(text)) {
/* 124:155 */       return null;
/* 125:    */     }
/* 126:157 */     char firstChar = text.charAt(0);
/* 127:158 */     if (Character.isWhitespace(firstChar)) {
/* 128:159 */       return null;
/* 129:    */     }
/* 130:161 */     if (firstChar == '\'')
/* 131:    */     {
/* 132:164 */       if (text.charAt(lastIx) != '\'') {
/* 133:165 */         return null;
/* 134:    */       }
/* 135:167 */       firstChar = text.charAt(1);
/* 136:168 */       if (Character.isWhitespace(firstChar)) {
/* 137:169 */         return null;
/* 138:    */       }
/* 139:    */       int sheetStartPos;
/* 140:    */       String wbName;
/* 141:    */       int sheetStartPos;
/* 142:173 */       if (firstChar == '[')
/* 143:    */       {
/* 144:174 */         int rbPos = text.toString().lastIndexOf(']');
/* 145:175 */         if (rbPos < 0) {
/* 146:176 */           return null;
/* 147:    */         }
/* 148:178 */         String wbName = unescapeString(text.subSequence(2, rbPos));
/* 149:179 */         if ((wbName == null) || (canTrim(wbName))) {
/* 150:180 */           return null;
/* 151:    */         }
/* 152:182 */         sheetStartPos = rbPos + 1;
/* 153:    */       }
/* 154:    */       else
/* 155:    */       {
/* 156:184 */         wbName = null;
/* 157:185 */         sheetStartPos = 1;
/* 158:    */       }
/* 159:189 */       String sheetName = unescapeString(text.subSequence(sheetStartPos, lastIx));
/* 160:190 */       if (sheetName == null) {
/* 161:192 */         return null;
/* 162:    */       }
/* 163:194 */       return new String[] { wbName, sheetName };
/* 164:    */     }
/* 165:197 */     if (firstChar == '[')
/* 166:    */     {
/* 167:198 */       int rbPos = text.toString().lastIndexOf(']');
/* 168:199 */       if (rbPos < 0) {
/* 169:200 */         return null;
/* 170:    */       }
/* 171:202 */       CharSequence wbName = text.subSequence(1, rbPos);
/* 172:203 */       if (canTrim(wbName)) {
/* 173:204 */         return null;
/* 174:    */       }
/* 175:206 */       CharSequence sheetName = text.subSequence(rbPos + 1, text.length());
/* 176:207 */       if (canTrim(sheetName)) {
/* 177:208 */         return null;
/* 178:    */       }
/* 179:210 */       return new String[] { wbName.toString(), sheetName.toString() };
/* 180:    */     }
/* 181:213 */     return new String[] { null, text.toString() };
/* 182:    */   }
/* 183:    */   
/* 184:    */   private static String unescapeString(CharSequence text)
/* 185:    */   {
/* 186:221 */     int len = text.length();
/* 187:222 */     StringBuilder sb = new StringBuilder(len);
/* 188:223 */     int i = 0;
/* 189:224 */     while (i < len)
/* 190:    */     {
/* 191:225 */       char ch = text.charAt(i);
/* 192:226 */       if (ch == '\'')
/* 193:    */       {
/* 194:228 */         i++;
/* 195:229 */         if (i >= len) {
/* 196:230 */           return null;
/* 197:    */         }
/* 198:232 */         ch = text.charAt(i);
/* 199:233 */         if (ch != '\'') {
/* 200:234 */           return null;
/* 201:    */         }
/* 202:    */       }
/* 203:237 */       sb.append(ch);
/* 204:238 */       i++;
/* 205:    */     }
/* 206:240 */     return sb.toString();
/* 207:    */   }
/* 208:    */   
/* 209:    */   private static boolean canTrim(CharSequence text)
/* 210:    */   {
/* 211:244 */     int lastIx = text.length() - 1;
/* 212:245 */     if (lastIx < 0) {
/* 213:246 */       return false;
/* 214:    */     }
/* 215:248 */     if (Character.isWhitespace(text.charAt(0))) {
/* 216:249 */       return true;
/* 217:    */     }
/* 218:251 */     if (Character.isWhitespace(text.charAt(lastIx))) {
/* 219:252 */       return true;
/* 220:    */     }
/* 221:254 */     return false;
/* 222:    */   }
/* 223:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Indirect
 * JD-Core Version:    0.7.0.1
 */