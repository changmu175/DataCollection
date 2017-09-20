/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import java.util.Calendar;
/*   4:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   7:    */ import org.apache.poi.ss.formula.eval.MissingArgEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  10:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  11:    */ import org.apache.poi.ss.usermodel.DateUtil;
/*  12:    */ 
/*  13:    */ public final class WeekdayFunc
/*  14:    */   implements Function
/*  15:    */ {
/*  16: 42 */   public static final Function instance = new WeekdayFunc();
/*  17:    */   
/*  18:    */   public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex)
/*  19:    */   {
/*  20:    */     try
/*  21:    */     {
/*  22: 83 */       if ((args.length < 1) || (args.length > 2)) {
/*  23: 84 */         return ErrorEval.VALUE_INVALID;
/*  24:    */       }
/*  25: 88 */       ValueEval serialDateVE = OperandResolver.getSingleValue(args[0], srcRowIndex, srcColumnIndex);
/*  26: 89 */       double serialDate = OperandResolver.coerceValueToDouble(serialDateVE);
/*  27: 90 */       if (!DateUtil.isValidExcelDate(serialDate)) {
/*  28: 91 */         return ErrorEval.NUM_ERROR;
/*  29:    */       }
/*  30: 93 */       Calendar date = DateUtil.getJavaCalendar(serialDate, false);
/*  31: 94 */       int weekday = date.get(7);
/*  32:    */       
/*  33:    */ 
/*  34: 97 */       int returnOption = 1;
/*  35: 98 */       if (args.length == 2)
/*  36:    */       {
/*  37: 99 */         ValueEval ve = OperandResolver.getSingleValue(args[1], srcRowIndex, srcColumnIndex);
/*  38:100 */         if ((ve == MissingArgEval.instance) || (ve == BlankEval.instance)) {
/*  39:101 */           return ErrorEval.NUM_ERROR;
/*  40:    */         }
/*  41:103 */         returnOption = OperandResolver.coerceValueToInt(ve);
/*  42:104 */         if (returnOption == 2) {
/*  43:105 */           returnOption = 11;
/*  44:    */         }
/*  45:    */       }
/*  46:    */       double result;
/*  47:111 */       if (returnOption == 1)
/*  48:    */       {
/*  49:112 */         result = weekday;
/*  50:    */       }
/*  51:    */       else
/*  52:    */       {
/*  53:    */         double result;
/*  54:114 */         if (returnOption == 3)
/*  55:    */         {
/*  56:115 */           result = (weekday + 6 - 1) % 7;
/*  57:    */         }
/*  58:    */         else
/*  59:    */         {
/*  60:    */           double result;
/*  61:116 */           if ((returnOption >= 11) && (returnOption <= 17)) {
/*  62:117 */             result = (weekday + 6 - (returnOption - 10)) % 7 + 1;
/*  63:    */           } else {
/*  64:119 */             return ErrorEval.NUM_ERROR;
/*  65:    */           }
/*  66:    */         }
/*  67:    */       }
/*  68:    */       double result;
/*  69:122 */       return new NumberEval(result);
/*  70:    */     }
/*  71:    */     catch (EvaluationException e)
/*  72:    */     {
/*  73:124 */       return e.getErrorEval();
/*  74:    */     }
/*  75:    */   }
/*  76:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.WeekdayFunc
 * JD-Core Version:    0.7.0.1
 */