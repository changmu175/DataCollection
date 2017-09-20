/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   5:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   6:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   8:    */ 
/*   9:    */ public class Roman
/*  10:    */   extends Fixed2ArgFunction
/*  11:    */ {
/*  12: 46 */   public static final int[] VALUES = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
/*  13: 47 */   public static final String[] ROMAN = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
/*  14:    */   
/*  15:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval numberVE, ValueEval formVE)
/*  16:    */   {
/*  17: 52 */     int number = 0;
/*  18:    */     try
/*  19:    */     {
/*  20: 54 */       ValueEval ve = OperandResolver.getSingleValue(numberVE, srcRowIndex, srcColumnIndex);
/*  21: 55 */       number = OperandResolver.coerceValueToInt(ve);
/*  22:    */     }
/*  23:    */     catch (EvaluationException e)
/*  24:    */     {
/*  25: 57 */       return ErrorEval.VALUE_INVALID;
/*  26:    */     }
/*  27: 59 */     if (number < 0) {
/*  28: 60 */       return ErrorEval.VALUE_INVALID;
/*  29:    */     }
/*  30: 62 */     if (number > 3999) {
/*  31: 63 */       return ErrorEval.VALUE_INVALID;
/*  32:    */     }
/*  33: 65 */     if (number == 0) {
/*  34: 66 */       return new StringEval("");
/*  35:    */     }
/*  36: 69 */     int form = 0;
/*  37:    */     try
/*  38:    */     {
/*  39: 71 */       ValueEval ve = OperandResolver.getSingleValue(formVE, srcRowIndex, srcColumnIndex);
/*  40: 72 */       form = OperandResolver.coerceValueToInt(ve);
/*  41:    */     }
/*  42:    */     catch (EvaluationException e)
/*  43:    */     {
/*  44: 74 */       return ErrorEval.NUM_ERROR;
/*  45:    */     }
/*  46: 77 */     if ((form > 4) || (form < 0)) {
/*  47: 78 */       return ErrorEval.VALUE_INVALID;
/*  48:    */     }
/*  49: 81 */     String result = integerToRoman(number);
/*  50: 83 */     if (form == 0) {
/*  51: 84 */       return new StringEval(result);
/*  52:    */     }
/*  53: 87 */     return new StringEval(makeConcise(result, form));
/*  54:    */   }
/*  55:    */   
/*  56:    */   private String integerToRoman(int number)
/*  57:    */   {
/*  58: 96 */     StringBuilder result = new StringBuilder();
/*  59: 97 */     for (int i = 0; i < 13; i++) {
/*  60: 98 */       while (number >= VALUES[i])
/*  61:    */       {
/*  62: 99 */         number -= VALUES[i];
/*  63:100 */         result.append(ROMAN[i]);
/*  64:    */       }
/*  65:    */     }
/*  66:103 */     return result.toString();
/*  67:    */   }
/*  68:    */   
/*  69:    */   public String makeConcise(String result, int form)
/*  70:    */   {
/*  71:113 */     if (form > 0)
/*  72:    */     {
/*  73:114 */       result = result.replaceAll("XLV", "VL");
/*  74:115 */       result = result.replaceAll("XCV", "VC");
/*  75:116 */       result = result.replaceAll("CDL", "LD");
/*  76:117 */       result = result.replaceAll("CML", "LM");
/*  77:118 */       result = result.replaceAll("CMVC", "LMVL");
/*  78:    */     }
/*  79:120 */     if (form == 1)
/*  80:    */     {
/*  81:121 */       result = result.replaceAll("CDXC", "LDXL");
/*  82:122 */       result = result.replaceAll("CDVC", "LDVL");
/*  83:123 */       result = result.replaceAll("CMXC", "LMXL");
/*  84:124 */       result = result.replaceAll("XCIX", "VCIV");
/*  85:125 */       result = result.replaceAll("XLIX", "VLIV");
/*  86:    */     }
/*  87:127 */     if (form > 1)
/*  88:    */     {
/*  89:128 */       result = result.replaceAll("XLIX", "IL");
/*  90:129 */       result = result.replaceAll("XCIX", "IC");
/*  91:130 */       result = result.replaceAll("CDXC", "XD");
/*  92:131 */       result = result.replaceAll("CDVC", "XDV");
/*  93:132 */       result = result.replaceAll("CDIC", "XDIX");
/*  94:133 */       result = result.replaceAll("LMVL", "XMV");
/*  95:134 */       result = result.replaceAll("CMIC", "XMIX");
/*  96:135 */       result = result.replaceAll("CMXC", "XM");
/*  97:    */     }
/*  98:137 */     if (form > 2)
/*  99:    */     {
/* 100:138 */       result = result.replaceAll("XDV", "VD");
/* 101:139 */       result = result.replaceAll("XDIX", "VDIV");
/* 102:140 */       result = result.replaceAll("XMV", "VM");
/* 103:141 */       result = result.replaceAll("XMIX", "VMIV");
/* 104:    */     }
/* 105:143 */     if (form == 4)
/* 106:    */     {
/* 107:144 */       result = result.replaceAll("VDIV", "ID");
/* 108:145 */       result = result.replaceAll("VMIV", "IM");
/* 109:    */     }
/* 110:148 */     return result;
/* 111:    */   }
/* 112:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Roman
 * JD-Core Version:    0.7.0.1
 */