/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.TwoDEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   5:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   6:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   7:    */ 
/*   8:    */ public final class Lookup
/*   9:    */   extends Var2or3ArgFunction
/*  10:    */ {
/*  11:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/*  12:    */   {
/*  13:    */     try
/*  14:    */     {
/*  15: 53 */       ValueEval lookupValue = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
/*  16: 54 */       TwoDEval lookupArray = LookupUtils.resolveTableArrayArg(arg1);
/*  17:    */       LookupUtils.ValueVector resultVector;
/*  18:    */       LookupUtils.ValueVector lookupVector;
/*  19:    */       LookupUtils.ValueVector resultVector;
/*  20: 58 */       if (lookupArray.getWidth() > lookupArray.getHeight())
/*  21:    */       {
/*  22: 60 */         LookupUtils.ValueVector lookupVector = createVector(lookupArray.getRow(0));
/*  23: 61 */         resultVector = createVector(lookupArray.getRow(lookupArray.getHeight() - 1));
/*  24:    */       }
/*  25:    */       else
/*  26:    */       {
/*  27: 64 */         lookupVector = createVector(lookupArray.getColumn(0));
/*  28: 65 */         resultVector = createVector(lookupArray.getColumn(lookupArray.getWidth() - 1));
/*  29:    */       }
/*  30: 68 */       assert (lookupVector.getSize() == resultVector.getSize());
/*  31:    */       
/*  32: 70 */       int index = LookupUtils.lookupIndexOfValue(lookupValue, lookupVector, true);
/*  33: 71 */       return resultVector.getItem(index);
/*  34:    */     }
/*  35:    */     catch (EvaluationException e)
/*  36:    */     {
/*  37: 73 */       return e.getErrorEval();
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/*  42:    */   {
/*  43:    */     try
/*  44:    */     {
/*  45: 81 */       ValueEval lookupValue = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
/*  46: 82 */       TwoDEval aeLookupVector = LookupUtils.resolveTableArrayArg(arg1);
/*  47: 83 */       TwoDEval aeResultVector = LookupUtils.resolveTableArrayArg(arg2);
/*  48:    */       
/*  49: 85 */       LookupUtils.ValueVector lookupVector = createVector(aeLookupVector);
/*  50: 86 */       LookupUtils.ValueVector resultVector = createVector(aeResultVector);
/*  51: 87 */       if (lookupVector.getSize() > resultVector.getSize()) {
/*  52: 89 */         throw new RuntimeException("Lookup vector and result vector of differing sizes not supported yet");
/*  53:    */       }
/*  54: 91 */       int index = LookupUtils.lookupIndexOfValue(lookupValue, lookupVector, true);
/*  55:    */       
/*  56: 93 */       return resultVector.getItem(index);
/*  57:    */     }
/*  58:    */     catch (EvaluationException e)
/*  59:    */     {
/*  60: 95 */       return e.getErrorEval();
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   private static LookupUtils.ValueVector createVector(TwoDEval ae)
/*  65:    */   {
/*  66:100 */     LookupUtils.ValueVector result = LookupUtils.createVector(ae);
/*  67:101 */     if (result != null) {
/*  68:102 */       return result;
/*  69:    */     }
/*  70:105 */     throw new RuntimeException("non-vector lookup or result areas not supported yet");
/*  71:    */   }
/*  72:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Lookup
 * JD-Core Version:    0.7.0.1
 */