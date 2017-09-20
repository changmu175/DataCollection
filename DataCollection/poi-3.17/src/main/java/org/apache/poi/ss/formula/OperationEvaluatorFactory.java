/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Constructor;
/*   4:    */ import java.lang.reflect.Modifier;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Map;
/*   7:    */ import org.apache.poi.ss.formula.eval.ConcatEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.FunctionEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.IntersectionEval;
/*  10:    */ import org.apache.poi.ss.formula.eval.PercentEval;
/*  11:    */ import org.apache.poi.ss.formula.eval.RangeEval;
/*  12:    */ import org.apache.poi.ss.formula.eval.RelationalOperationEval;
/*  13:    */ import org.apache.poi.ss.formula.eval.TwoOperandNumericOperation;
/*  14:    */ import org.apache.poi.ss.formula.eval.UnaryMinusEval;
/*  15:    */ import org.apache.poi.ss.formula.eval.UnaryPlusEval;
/*  16:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  17:    */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/*  18:    */ import org.apache.poi.ss.formula.functions.Function;
/*  19:    */ import org.apache.poi.ss.formula.functions.Indirect;
/*  20:    */ import org.apache.poi.ss.formula.ptg.AbstractFunctionPtg;
/*  21:    */ import org.apache.poi.ss.formula.ptg.AddPtg;
/*  22:    */ import org.apache.poi.ss.formula.ptg.ConcatPtg;
/*  23:    */ import org.apache.poi.ss.formula.ptg.DividePtg;
/*  24:    */ import org.apache.poi.ss.formula.ptg.EqualPtg;
/*  25:    */ import org.apache.poi.ss.formula.ptg.GreaterEqualPtg;
/*  26:    */ import org.apache.poi.ss.formula.ptg.GreaterThanPtg;
/*  27:    */ import org.apache.poi.ss.formula.ptg.IntersectionPtg;
/*  28:    */ import org.apache.poi.ss.formula.ptg.LessEqualPtg;
/*  29:    */ import org.apache.poi.ss.formula.ptg.LessThanPtg;
/*  30:    */ import org.apache.poi.ss.formula.ptg.MultiplyPtg;
/*  31:    */ import org.apache.poi.ss.formula.ptg.NotEqualPtg;
/*  32:    */ import org.apache.poi.ss.formula.ptg.OperationPtg;
/*  33:    */ import org.apache.poi.ss.formula.ptg.PercentPtg;
/*  34:    */ import org.apache.poi.ss.formula.ptg.PowerPtg;
/*  35:    */ import org.apache.poi.ss.formula.ptg.RangePtg;
/*  36:    */ import org.apache.poi.ss.formula.ptg.SubtractPtg;
/*  37:    */ import org.apache.poi.ss.formula.ptg.UnaryMinusPtg;
/*  38:    */ import org.apache.poi.ss.formula.ptg.UnaryPlusPtg;
/*  39:    */ 
/*  40:    */ final class OperationEvaluatorFactory
/*  41:    */ {
/*  42: 66 */   private static final Map<OperationPtg, Function> _instancesByPtgClass = ;
/*  43:    */   
/*  44:    */   private static Map<OperationPtg, Function> initialiseInstancesMap()
/*  45:    */   {
/*  46: 73 */     Map<OperationPtg, Function> m = new HashMap(32);
/*  47:    */     
/*  48: 75 */     put(m, EqualPtg.instance, RelationalOperationEval.EqualEval);
/*  49: 76 */     put(m, GreaterEqualPtg.instance, RelationalOperationEval.GreaterEqualEval);
/*  50: 77 */     put(m, GreaterThanPtg.instance, RelationalOperationEval.GreaterThanEval);
/*  51: 78 */     put(m, LessEqualPtg.instance, RelationalOperationEval.LessEqualEval);
/*  52: 79 */     put(m, LessThanPtg.instance, RelationalOperationEval.LessThanEval);
/*  53: 80 */     put(m, NotEqualPtg.instance, RelationalOperationEval.NotEqualEval);
/*  54:    */     
/*  55: 82 */     put(m, ConcatPtg.instance, ConcatEval.instance);
/*  56: 83 */     put(m, AddPtg.instance, TwoOperandNumericOperation.AddEval);
/*  57: 84 */     put(m, DividePtg.instance, TwoOperandNumericOperation.DivideEval);
/*  58: 85 */     put(m, MultiplyPtg.instance, TwoOperandNumericOperation.MultiplyEval);
/*  59: 86 */     put(m, PercentPtg.instance, PercentEval.instance);
/*  60: 87 */     put(m, PowerPtg.instance, TwoOperandNumericOperation.PowerEval);
/*  61: 88 */     put(m, SubtractPtg.instance, TwoOperandNumericOperation.SubtractEval);
/*  62: 89 */     put(m, UnaryMinusPtg.instance, UnaryMinusEval.instance);
/*  63: 90 */     put(m, UnaryPlusPtg.instance, UnaryPlusEval.instance);
/*  64: 91 */     put(m, RangePtg.instance, RangeEval.instance);
/*  65: 92 */     put(m, IntersectionPtg.instance, IntersectionEval.instance);
/*  66: 93 */     return m;
/*  67:    */   }
/*  68:    */   
/*  69:    */   private static void put(Map<OperationPtg, Function> m, OperationPtg ptgKey, Function instance)
/*  70:    */   {
/*  71: 99 */     Constructor<?>[] cc = ptgKey.getClass().getDeclaredConstructors();
/*  72:100 */     if ((cc.length > 1) || (!Modifier.isPrivate(cc[0].getModifiers()))) {
/*  73:101 */       throw new RuntimeException("Failed to verify instance (" + ptgKey.getClass().getName() + ") is a singleton.");
/*  74:    */     }
/*  75:104 */     m.put(ptgKey, instance);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public static ValueEval evaluate(OperationPtg ptg, ValueEval[] args, OperationEvaluationContext ec)
/*  79:    */   {
/*  80:113 */     if (ptg == null) {
/*  81:114 */       throw new IllegalArgumentException("ptg must not be null");
/*  82:    */     }
/*  83:116 */     Function result = (Function)_instancesByPtgClass.get(ptg);
/*  84:118 */     if (result != null) {
/*  85:119 */       return result.evaluate(args, ec.getRowIndex(), (short)ec.getColumnIndex());
/*  86:    */     }
/*  87:122 */     if ((ptg instanceof AbstractFunctionPtg))
/*  88:    */     {
/*  89:123 */       AbstractFunctionPtg fptg = (AbstractFunctionPtg)ptg;
/*  90:124 */       int functionIndex = fptg.getFunctionIndex();
/*  91:125 */       switch (functionIndex)
/*  92:    */       {
/*  93:    */       case 148: 
/*  94:127 */         return Indirect.instance.evaluate(args, ec);
/*  95:    */       case 255: 
/*  96:129 */         return UserDefinedFunction.instance.evaluate(args, ec);
/*  97:    */       }
/*  98:132 */       return FunctionEval.getBasicFunction(functionIndex).evaluate(args, ec.getRowIndex(), (short)ec.getColumnIndex());
/*  99:    */     }
/* 100:134 */     throw new RuntimeException("Unexpected operation ptg class (" + ptg.getClass().getName() + ")");
/* 101:    */   }
/* 102:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.OperationEvaluatorFactory
 * JD-Core Version:    0.7.0.1
 */