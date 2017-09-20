/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.TwoDEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   7:    */ import org.apache.poi.ss.formula.eval.MissingArgEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   9:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*  10:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  11:    */ 
/*  12:    */ public final class Index
/*  13:    */   implements Function2Arg, Function3Arg, Function4Arg
/*  14:    */ {
/*  15:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/*  16:    */   {
/*  17: 50 */     TwoDEval reference = convertFirstArg(arg0);
/*  18:    */     
/*  19: 52 */     int columnIx = 0;
/*  20:    */     try
/*  21:    */     {
/*  22: 54 */       int rowIx = resolveIndexArg(arg1, srcRowIndex, srcColumnIndex);
/*  23: 56 */       if (!reference.isColumn())
/*  24:    */       {
/*  25: 57 */         if (!reference.isRow()) {
/*  26: 60 */           return ErrorEval.REF_INVALID;
/*  27:    */         }
/*  28: 64 */         columnIx = rowIx;
/*  29: 65 */         rowIx = 0;
/*  30:    */       }
/*  31: 68 */       return getValueFromArea(reference, rowIx, columnIx);
/*  32:    */     }
/*  33:    */     catch (EvaluationException e)
/*  34:    */     {
/*  35: 70 */       return e.getErrorEval();
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/*  40:    */   {
/*  41: 75 */     TwoDEval reference = convertFirstArg(arg0);
/*  42:    */     try
/*  43:    */     {
/*  44: 78 */       int columnIx = resolveIndexArg(arg2, srcRowIndex, srcColumnIndex);
/*  45: 79 */       int rowIx = resolveIndexArg(arg1, srcRowIndex, srcColumnIndex);
/*  46: 80 */       return getValueFromArea(reference, rowIx, columnIx);
/*  47:    */     }
/*  48:    */     catch (EvaluationException e)
/*  49:    */     {
/*  50: 82 */       return e.getErrorEval();
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2, ValueEval arg3)
/*  55:    */   {
/*  56: 87 */     throw new RuntimeException("Incomplete code - don't know how to support the 'area_num' parameter yet)");
/*  57:    */   }
/*  58:    */   
/*  59:    */   private static TwoDEval convertFirstArg(ValueEval arg0)
/*  60:    */   {
/*  61: 96 */     ValueEval firstArg = arg0;
/*  62: 97 */     if ((firstArg instanceof RefEval)) {
/*  63: 99 */       return ((RefEval)firstArg).offset(0, 0, 0, 0);
/*  64:    */     }
/*  65:101 */     if ((firstArg instanceof TwoDEval)) {
/*  66:102 */       return (TwoDEval)firstArg;
/*  67:    */     }
/*  68:106 */     throw new RuntimeException("Incomplete code - cannot handle first arg of type (" + firstArg.getClass().getName() + ")");
/*  69:    */   }
/*  70:    */   
/*  71:    */   public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex)
/*  72:    */   {
/*  73:112 */     switch (args.length)
/*  74:    */     {
/*  75:    */     case 2: 
/*  76:114 */       return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1]);
/*  77:    */     case 3: 
/*  78:116 */       return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2]);
/*  79:    */     case 4: 
/*  80:118 */       return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2], args[3]);
/*  81:    */     }
/*  82:120 */     return ErrorEval.VALUE_INVALID;
/*  83:    */   }
/*  84:    */   
/*  85:    */   private static ValueEval getValueFromArea(TwoDEval ae, int pRowIx, int pColumnIx)
/*  86:    */     throws EvaluationException
/*  87:    */   {
/*  88:125 */     assert (pRowIx >= 0);
/*  89:126 */     assert (pColumnIx >= 0);
/*  90:    */     
/*  91:128 */     TwoDEval result = ae;
/*  92:130 */     if (pRowIx != 0)
/*  93:    */     {
/*  94:132 */       if (pRowIx > ae.getHeight()) {
/*  95:134 */         throw new EvaluationException(ErrorEval.REF_INVALID);
/*  96:    */       }
/*  97:136 */       result = result.getRow(pRowIx - 1);
/*  98:    */     }
/*  99:139 */     if (pColumnIx != 0)
/* 100:    */     {
/* 101:141 */       if (pColumnIx > ae.getWidth()) {
/* 102:143 */         throw new EvaluationException(ErrorEval.REF_INVALID);
/* 103:    */       }
/* 104:145 */       result = result.getColumn(pColumnIx - 1);
/* 105:    */     }
/* 106:147 */     return result;
/* 107:    */   }
/* 108:    */   
/* 109:    */   private static int resolveIndexArg(ValueEval arg, int srcCellRow, int srcCellCol)
/* 110:    */     throws EvaluationException
/* 111:    */   {
/* 112:158 */     ValueEval ev = OperandResolver.getSingleValue(arg, srcCellRow, srcCellCol);
/* 113:159 */     if (ev == MissingArgEval.instance) {
/* 114:160 */       return 0;
/* 115:    */     }
/* 116:162 */     if (ev == BlankEval.instance) {
/* 117:163 */       return 0;
/* 118:    */     }
/* 119:165 */     int result = OperandResolver.coerceValueToInt(ev);
/* 120:166 */     if (result < 0) {
/* 121:167 */       throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 122:    */     }
/* 123:169 */     return result;
/* 124:    */   }
/* 125:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Index
 * JD-Core Version:    0.7.0.1
 */