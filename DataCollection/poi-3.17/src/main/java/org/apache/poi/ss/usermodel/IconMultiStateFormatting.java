/*  1:   */ package org.apache.poi.ss.usermodel;
/*  2:   */ 
/*  3:   */ public abstract interface IconMultiStateFormatting
/*  4:   */ {
/*  5:   */   public abstract IconSet getIconSet();
/*  6:   */   
/*  7:   */   public abstract void setIconSet(IconSet paramIconSet);
/*  8:   */   
/*  9:   */   public abstract boolean isIconOnly();
/* 10:   */   
/* 11:   */   public abstract void setIconOnly(boolean paramBoolean);
/* 12:   */   
/* 13:   */   public abstract boolean isReversed();
/* 14:   */   
/* 15:   */   public abstract void setReversed(boolean paramBoolean);
/* 16:   */   
/* 17:   */   public abstract ConditionalFormattingThreshold[] getThresholds();
/* 18:   */   
/* 19:   */   public abstract void setThresholds(ConditionalFormattingThreshold[] paramArrayOfConditionalFormattingThreshold);
/* 20:   */   
/* 21:   */   public abstract ConditionalFormattingThreshold createThreshold();
/* 22:   */   
/* 23:   */   public static enum IconSet
/* 24:   */   {
/* 25:29 */     GYR_3_ARROW(0, 3, "3Arrows"),  GREY_3_ARROWS(1, 3, "3ArrowsGray"),  GYR_3_FLAGS(2, 3, "3Flags"),  GYR_3_TRAFFIC_LIGHTS(3, 3, "3TrafficLights1"),  GYR_3_TRAFFIC_LIGHTS_BOX(4, 3, "3TrafficLights2"),  GYR_3_SHAPES(5, 3, "3Signs"),  GYR_3_SYMBOLS_CIRCLE(6, 3, "3Symbols"),  GYR_3_SYMBOLS(7, 3, "3Symbols2"),  GYR_4_ARROWS(8, 4, "4Arrows"),  GREY_4_ARROWS(9, 4, "4ArrowsGray"),  RB_4_TRAFFIC_LIGHTS(10, 4, "4RedToBlack"),  RATINGS_4(11, 4, "4Rating"),  GYRB_4_TRAFFIC_LIGHTS(12, 4, "4TrafficLights"),  GYYYR_5_ARROWS(13, 5, "5Arrows"),  GREY_5_ARROWS(14, 5, "5ArrowsGray"),  RATINGS_5(15, 5, "5Rating"),  QUARTERS_5(16, 5, "5Quarters");
/* 26:   */     
/* 27:60 */     protected static final IconSet DEFAULT_ICONSET = GYR_3_TRAFFIC_LIGHTS;
/* 28:   */     public final int id;
/* 29:   */     public final int num;
/* 30:   */     public final String name;
/* 31:   */     
/* 32:   */     public String toString()
/* 33:   */     {
/* 34:70 */       return this.id + " - " + this.name;
/* 35:   */     }
/* 36:   */     
/* 37:   */     public static IconSet byId(int id)
/* 38:   */     {
/* 39:74 */       return values()[id];
/* 40:   */     }
/* 41:   */     
/* 42:   */     public static IconSet byName(String name)
/* 43:   */     {
/* 44:77 */       for (IconSet set : ) {
/* 45:78 */         if (set.name.equals(name)) {
/* 46:78 */           return set;
/* 47:   */         }
/* 48:   */       }
/* 49:80 */       return null;
/* 50:   */     }
/* 51:   */     
/* 52:   */     private IconSet(int id, int num, String name)
/* 53:   */     {
/* 54:84 */       this.id = id;this.num = num;this.name = name;
/* 55:   */     }
/* 56:   */   }
/* 57:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.IconMultiStateFormatting
 * JD-Core Version:    0.7.0.1
 */