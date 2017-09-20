/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.ThreeDEval;
/*   4:    */ import org.apache.poi.ss.formula.TwoDEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   7:    */ 
/*   8:    */ final class CountUtils
/*   9:    */ {
/*  10:    */   public static int countMatchingCellsInArea(ThreeDEval areaEval, I_MatchPredicate criteriaPredicate)
/*  11:    */   {
/*  12: 50 */     int result = 0;
/*  13:    */     
/*  14: 52 */     int firstSheetIndex = areaEval.getFirstSheetIndex();
/*  15: 53 */     int lastSheetIndex = areaEval.getLastSheetIndex();
/*  16: 54 */     for (int sIx = firstSheetIndex; sIx <= lastSheetIndex; sIx++)
/*  17:    */     {
/*  18: 55 */       int height = areaEval.getHeight();
/*  19: 56 */       int width = areaEval.getWidth();
/*  20: 57 */       for (int rrIx = 0; rrIx < height; rrIx++) {
/*  21: 58 */         for (int rcIx = 0; rcIx < width; rcIx++)
/*  22:    */         {
/*  23: 59 */           ValueEval ve = areaEval.getValue(sIx, rrIx, rcIx);
/*  24: 61 */           if ((criteriaPredicate instanceof I_MatchAreaPredicate))
/*  25:    */           {
/*  26: 62 */             I_MatchAreaPredicate areaPredicate = (I_MatchAreaPredicate)criteriaPredicate;
/*  27: 63 */             if (!areaPredicate.matches(areaEval, rrIx, rcIx)) {}
/*  28:    */           }
/*  29: 66 */           else if (criteriaPredicate.matches(ve))
/*  30:    */           {
/*  31: 67 */             result++;
/*  32:    */           }
/*  33:    */         }
/*  34:    */       }
/*  35:    */     }
/*  36: 72 */     return result;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static int countMatchingCellsInRef(RefEval refEval, I_MatchPredicate criteriaPredicate)
/*  40:    */   {
/*  41: 78 */     int result = 0;
/*  42:    */     
/*  43: 80 */     int firstSheetIndex = refEval.getFirstSheetIndex();
/*  44: 81 */     int lastSheetIndex = refEval.getLastSheetIndex();
/*  45: 82 */     for (int sIx = firstSheetIndex; sIx <= lastSheetIndex; sIx++)
/*  46:    */     {
/*  47: 83 */       ValueEval ve = refEval.getInnerValueEval(sIx);
/*  48: 84 */       if (criteriaPredicate.matches(ve)) {
/*  49: 85 */         result++;
/*  50:    */       }
/*  51:    */     }
/*  52: 88 */     return result;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static int countArg(ValueEval eval, I_MatchPredicate criteriaPredicate)
/*  56:    */   {
/*  57: 91 */     if (eval == null) {
/*  58: 92 */       throw new IllegalArgumentException("eval must not be null");
/*  59:    */     }
/*  60: 94 */     if ((eval instanceof ThreeDEval)) {
/*  61: 95 */       return countMatchingCellsInArea((ThreeDEval)eval, criteriaPredicate);
/*  62:    */     }
/*  63: 97 */     if ((eval instanceof TwoDEval)) {
/*  64: 98 */       throw new IllegalArgumentException("Count requires 3D Evals, 2D ones aren't supported");
/*  65:    */     }
/*  66:100 */     if ((eval instanceof RefEval)) {
/*  67:101 */       return countMatchingCellsInRef((RefEval)eval, criteriaPredicate);
/*  68:    */     }
/*  69:103 */     return criteriaPredicate.matches(eval) ? 1 : 0;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static abstract interface I_MatchAreaPredicate
/*  73:    */     extends I_MatchPredicate
/*  74:    */   {
/*  75:    */     public abstract boolean matches(TwoDEval paramTwoDEval, int paramInt1, int paramInt2);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public static abstract interface I_MatchPredicate
/*  79:    */   {
/*  80:    */     public abstract boolean matches(ValueEval paramValueEval);
/*  81:    */   }
/*  82:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.formula.functions.CountUtils

 * JD-Core Version:    0.7.0.1

 */