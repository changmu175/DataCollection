/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.SheetNameFormatter;
/*   4:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   6:    */ import org.apache.poi.ss.formula.eval.MissingArgEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   8:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  10:    */ import org.apache.poi.ss.util.CellReference;
/*  11:    */ 
/*  12:    */ public class Address
/*  13:    */   implements Function
/*  14:    */ {
/*  15:    */   public static final int REF_ABSOLUTE = 1;
/*  16:    */   public static final int REF_ROW_ABSOLUTE_COLUMN_RELATIVE = 2;
/*  17:    */   public static final int REF_ROW_RELATIVE_RELATIVE_ABSOLUTE = 3;
/*  18:    */   public static final int REF_RELATIVE = 4;
/*  19:    */   
/*  20:    */   public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex)
/*  21:    */   {
/*  22: 36 */     if ((args.length < 2) || (args.length > 5)) {
/*  23: 37 */       return ErrorEval.VALUE_INVALID;
/*  24:    */     }
/*  25:    */     try
/*  26:    */     {
/*  27: 42 */       int row = (int)NumericFunction.singleOperandEvaluate(args[0], srcRowIndex, srcColumnIndex);
/*  28: 43 */       int col = (int)NumericFunction.singleOperandEvaluate(args[1], srcRowIndex, srcColumnIndex);
/*  29:    */       int refType;
/*  30:    */       int refType;
/*  31: 46 */       if ((args.length > 2) && (args[2] != MissingArgEval.instance)) {
/*  32: 47 */         refType = (int)NumericFunction.singleOperandEvaluate(args[2], srcRowIndex, srcColumnIndex);
/*  33:    */       } else {
/*  34: 49 */         refType = 1;
/*  35:    */       }
/*  36:    */       boolean pAbsRow;
/*  37:    */       boolean pAbsCol;
/*  38: 51 */       switch (refType)
/*  39:    */       {
/*  40:    */       case 1: 
/*  41: 53 */         pAbsRow = true;
/*  42: 54 */         pAbsCol = true;
/*  43: 55 */         break;
/*  44:    */       case 2: 
/*  45: 57 */         pAbsRow = true;
/*  46: 58 */         pAbsCol = false;
/*  47: 59 */         break;
/*  48:    */       case 3: 
/*  49: 61 */         pAbsRow = false;
/*  50: 62 */         pAbsCol = true;
/*  51: 63 */         break;
/*  52:    */       case 4: 
/*  53: 65 */         pAbsRow = false;
/*  54: 66 */         pAbsCol = false;
/*  55: 67 */         break;
/*  56:    */       default: 
/*  57: 69 */         throw new EvaluationException(ErrorEval.VALUE_INVALID);
/*  58:    */       }
/*  59:    */       String sheetName;
/*  60:    */       String sheetName;
/*  61: 82 */       if (args.length == 5)
/*  62:    */       {
/*  63: 83 */         ValueEval ve = OperandResolver.getSingleValue(args[4], srcRowIndex, srcColumnIndex);
/*  64: 84 */         sheetName = ve == MissingArgEval.instance ? null : OperandResolver.coerceValueToString(ve);
/*  65:    */       }
/*  66:    */       else
/*  67:    */       {
/*  68: 86 */         sheetName = null;
/*  69:    */       }
/*  70: 89 */       CellReference ref = new CellReference(row - 1, col - 1, pAbsRow, pAbsCol);
/*  71: 90 */       StringBuffer sb = new StringBuffer(32);
/*  72: 91 */       if (sheetName != null)
/*  73:    */       {
/*  74: 92 */         SheetNameFormatter.appendFormat(sb, sheetName);
/*  75: 93 */         sb.append('!');
/*  76:    */       }
/*  77: 95 */       sb.append(ref.formatAsString());
/*  78:    */       
/*  79: 97 */       return new StringEval(sb.toString());
/*  80:    */     }
/*  81:    */     catch (EvaluationException e)
/*  82:    */     {
/*  83:100 */       return e.getErrorEval();
/*  84:    */     }
/*  85:    */   }
/*  86:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Address
 * JD-Core Version:    0.7.0.1
 */