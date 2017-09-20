/*   1:    */ package org.apache.poi.ss.usermodel;
/*   2:    */ 
/*   3:    */ public abstract interface DataValidationConstraint
/*   4:    */ {
/*   5:    */   public abstract int getValidationType();
/*   6:    */   
/*   7:    */   public abstract int getOperator();
/*   8:    */   
/*   9:    */   public abstract void setOperator(int paramInt);
/*  10:    */   
/*  11:    */   public abstract String[] getExplicitListValues();
/*  12:    */   
/*  13:    */   public abstract void setExplicitListValues(String[] paramArrayOfString);
/*  14:    */   
/*  15:    */   public abstract String getFormula1();
/*  16:    */   
/*  17:    */   public abstract void setFormula1(String paramString);
/*  18:    */   
/*  19:    */   public abstract String getFormula2();
/*  20:    */   
/*  21:    */   public abstract void setFormula2(String paramString);
/*  22:    */   
/*  23:    */   public static final class OperatorType
/*  24:    */   {
/*  25:    */     public static final int BETWEEN = 0;
/*  26:    */     public static final int NOT_BETWEEN = 1;
/*  27:    */     public static final int EQUAL = 2;
/*  28:    */     public static final int NOT_EQUAL = 3;
/*  29:    */     public static final int GREATER_THAN = 4;
/*  30:    */     public static final int LESS_THAN = 5;
/*  31:    */     public static final int GREATER_OR_EQUAL = 6;
/*  32:    */     public static final int LESS_OR_EQUAL = 7;
/*  33:    */     public static final int IGNORED = 0;
/*  34:    */     
/*  35:    */     public static void validateSecondArg(int comparisonOperator, String paramValue)
/*  36:    */     {
/*  37:114 */       switch (comparisonOperator)
/*  38:    */       {
/*  39:    */       case 0: 
/*  40:    */       case 1: 
/*  41:117 */         if (paramValue == null) {
/*  42:118 */           throw new IllegalArgumentException("expr2 must be supplied for 'between' comparisons");
/*  43:    */         }
/*  44:    */         break;
/*  45:    */       }
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public static final class ValidationType
/*  50:    */   {
/*  51:    */     public static final int ANY = 0;
/*  52:    */     public static final int INTEGER = 1;
/*  53:    */     public static final int DECIMAL = 2;
/*  54:    */     public static final int LIST = 3;
/*  55:    */     public static final int DATE = 4;
/*  56:    */     public static final int TIME = 5;
/*  57:    */     public static final int TEXT_LENGTH = 6;
/*  58:    */     public static final int FORMULA = 7;
/*  59:    */   }
/*  60:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.DataValidationConstraint
 * JD-Core Version:    0.7.0.1
 */