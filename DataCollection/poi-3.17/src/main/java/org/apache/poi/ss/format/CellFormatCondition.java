/*   1:    */ package org.apache.poi.ss.format;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ 
/*   6:    */ public abstract class CellFormatCondition
/*   7:    */ {
/*   8:    */   private static final int LT = 0;
/*   9:    */   private static final int LE = 1;
/*  10:    */   private static final int GT = 2;
/*  11:    */   private static final int GE = 3;
/*  12:    */   private static final int EQ = 4;
/*  13:    */   private static final int NE = 5;
/*  14: 38 */   private static final Map<String, Integer> TESTS = new HashMap();
/*  15:    */   
/*  16:    */   static
/*  17:    */   {
/*  18: 39 */     TESTS.put("<", Integer.valueOf(0));
/*  19: 40 */     TESTS.put("<=", Integer.valueOf(1));
/*  20: 41 */     TESTS.put(">", Integer.valueOf(2));
/*  21: 42 */     TESTS.put(">=", Integer.valueOf(3));
/*  22: 43 */     TESTS.put("=", Integer.valueOf(4));
/*  23: 44 */     TESTS.put("==", Integer.valueOf(4));
/*  24: 45 */     TESTS.put("!=", Integer.valueOf(5));
/*  25: 46 */     TESTS.put("<>", Integer.valueOf(5));
/*  26:    */   }
/*  27:    */   
/*  28:    */   public abstract boolean pass(double paramDouble);
/*  29:    */   
/*  30:    */   public static CellFormatCondition getInstance(String opString, String constStr)
/*  31:    */   {
/*  32: 63 */     if (!TESTS.containsKey(opString)) {
/*  33: 64 */       throw new IllegalArgumentException("Unknown test: " + opString);
/*  34:    */     }
/*  35: 65 */     int test = ((Integer)TESTS.get(opString)).intValue();
/*  36:    */     
/*  37: 67 */     double c = Double.parseDouble(constStr);
/*  38: 69 */     switch (test)
/*  39:    */     {
/*  40:    */     case 0: 
/*  41: 71 */       new CellFormatCondition()
/*  42:    */       {
/*  43:    */         public boolean pass(double value)
/*  44:    */         {
/*  45: 73 */           return value < this.val$c;
/*  46:    */         }
/*  47:    */       };
/*  48:    */     case 1: 
/*  49: 77 */       new CellFormatCondition()
/*  50:    */       {
/*  51:    */         public boolean pass(double value)
/*  52:    */         {
/*  53: 79 */           return value <= this.val$c;
/*  54:    */         }
/*  55:    */       };
/*  56:    */     case 2: 
/*  57: 83 */       new CellFormatCondition()
/*  58:    */       {
/*  59:    */         public boolean pass(double value)
/*  60:    */         {
/*  61: 85 */           return value > this.val$c;
/*  62:    */         }
/*  63:    */       };
/*  64:    */     case 3: 
/*  65: 89 */       new CellFormatCondition()
/*  66:    */       {
/*  67:    */         public boolean pass(double value)
/*  68:    */         {
/*  69: 91 */           return value >= this.val$c;
/*  70:    */         }
/*  71:    */       };
/*  72:    */     case 4: 
/*  73: 95 */       new CellFormatCondition()
/*  74:    */       {
/*  75:    */         public boolean pass(double value)
/*  76:    */         {
/*  77: 97 */           return value == this.val$c;
/*  78:    */         }
/*  79:    */       };
/*  80:    */     case 5: 
/*  81:101 */       new CellFormatCondition()
/*  82:    */       {
/*  83:    */         public boolean pass(double value)
/*  84:    */         {
/*  85:103 */           return value != this.val$c;
/*  86:    */         }
/*  87:    */       };
/*  88:    */     }
/*  89:107 */     throw new IllegalArgumentException("Cannot create for test number " + test + "(\"" + opString + "\")");
/*  90:    */   }
/*  91:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.format.CellFormatCondition
 * JD-Core Version:    0.7.0.1
 */