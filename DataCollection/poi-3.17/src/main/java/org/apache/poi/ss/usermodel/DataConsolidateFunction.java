/*  1:   */ package org.apache.poi.ss.usermodel;
/*  2:   */ 
/*  3:   */ public enum DataConsolidateFunction
/*  4:   */ {
/*  5:29 */   AVERAGE(1, "Average"),  COUNT(2, "Count"),  COUNT_NUMS(3, "Count"),  MAX(4, "Max"),  MIN(5, "Min"),  PRODUCT(6, "Product"),  STD_DEV(7, "StdDev"),  STD_DEVP(8, "StdDevp"),  SUM(9, "Sum"),  VAR(10, "Var"),  VARP(11, "Varp");
/*  6:   */   
/*  7:   */   private final int value;
/*  8:   */   private final String name;
/*  9:   */   
/* 10:   */   private DataConsolidateFunction(int value, String name)
/* 11:   */   {
/* 12:45 */     this.value = value;
/* 13:46 */     this.name = name;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public String getName()
/* 17:   */   {
/* 18:50 */     return this.name;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public int getValue()
/* 22:   */   {
/* 23:54 */     return this.value;
/* 24:   */   }
/* 25:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.DataConsolidateFunction
 * JD-Core Version:    0.7.0.1
 */