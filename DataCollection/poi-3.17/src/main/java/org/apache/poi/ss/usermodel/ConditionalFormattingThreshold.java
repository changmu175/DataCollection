/*  1:   */ package org.apache.poi.ss.usermodel;
/*  2:   */ 
/*  3:   */ public abstract interface ConditionalFormattingThreshold
/*  4:   */ {
/*  5:   */   public abstract RangeType getRangeType();
/*  6:   */   
/*  7:   */   public abstract void setRangeType(RangeType paramRangeType);
/*  8:   */   
/*  9:   */   public abstract String getFormula();
/* 10:   */   
/* 11:   */   public abstract void setFormula(String paramString);
/* 12:   */   
/* 13:   */   public abstract Double getValue();
/* 14:   */   
/* 15:   */   public abstract void setValue(Double paramDouble);
/* 16:   */   
/* 17:   */   public static enum RangeType
/* 18:   */   {
/* 19:32 */     NUMBER(1, "num"),  MIN(2, "min"),  MAX(3, "max"),  PERCENT(4, "percent"),  PERCENTILE(5, "percentile"),  UNALLOCATED(6, null),  FORMULA(7, "formula");
/* 20:   */     
/* 21:   */     public final int id;
/* 22:   */     public final String name;
/* 23:   */     
/* 24:   */     public String toString()
/* 25:   */     {
/* 26:51 */       return this.id + " - " + this.name;
/* 27:   */     }
/* 28:   */     
/* 29:   */     public static RangeType byId(int id)
/* 30:   */     {
/* 31:55 */       return values()[(id - 1)];
/* 32:   */     }
/* 33:   */     
/* 34:   */     public static RangeType byName(String name)
/* 35:   */     {
/* 36:58 */       for (RangeType t : ) {
/* 37:59 */         if (t.name.equals(name)) {
/* 38:59 */           return t;
/* 39:   */         }
/* 40:   */       }
/* 41:61 */       return null;
/* 42:   */     }
/* 43:   */     
/* 44:   */     private RangeType(int id, String name)
/* 45:   */     {
/* 46:65 */       this.id = id;this.name = name;
/* 47:   */     }
/* 48:   */   }
/* 49:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.ConditionalFormattingThreshold
 * JD-Core Version:    0.7.0.1
 */