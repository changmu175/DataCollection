/*  1:   */ package org.apache.poi.xssf.util;
/*  2:   */ 
/*  3:   */ public class NumericRanges
/*  4:   */ {
/*  5:   */   public static final int NO_OVERLAPS = -1;
/*  6:   */   public static final int OVERLAPS_1_MINOR = 0;
/*  7:   */   public static final int OVERLAPS_2_MINOR = 1;
/*  8:   */   public static final int OVERLAPS_1_WRAPS = 2;
/*  9:   */   public static final int OVERLAPS_2_WRAPS = 3;
/* 10:   */   
/* 11:   */   public static long[] getOverlappingRange(long[] range1, long[] range2)
/* 12:   */   {
/* 13:29 */     switch (getOverlappingType(range1, range2))
/* 14:   */     {
/* 15:   */     case 0: 
/* 16:31 */       return new long[] { range2[0], range1[1] };
/* 17:   */     case 1: 
/* 18:33 */       return new long[] { range1[0], range2[1] };
/* 19:   */     case 3: 
/* 20:35 */       return range1;
/* 21:   */     case 2: 
/* 22:37 */       return range2;
/* 23:   */     }
/* 24:40 */     return new long[] { -1L, -1L };
/* 25:   */   }
/* 26:   */   
/* 27:   */   public static int getOverlappingType(long[] range1, long[] range2)
/* 28:   */   {
/* 29:45 */     long min1 = range1[0];
/* 30:46 */     long max1 = range1[1];
/* 31:47 */     long min2 = range2[0];
/* 32:48 */     long max2 = range2[1];
/* 33:49 */     if (min1 >= min2)
/* 34:   */     {
/* 35:50 */       if (max1 <= max2) {
/* 36:51 */         return 3;
/* 37:   */       }
/* 38:52 */       if (min1 <= max2) {
/* 39:53 */         return 1;
/* 40:   */       }
/* 41:   */     }
/* 42:   */     else
/* 43:   */     {
/* 44:56 */       if (max1 >= max2) {
/* 45:57 */         return 2;
/* 46:   */       }
/* 47:58 */       if (max1 >= min2) {
/* 48:59 */         return 0;
/* 49:   */       }
/* 50:   */     }
/* 51:62 */     return -1;
/* 52:   */   }
/* 53:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.util.NumericRanges
 * JD-Core Version:    0.7.0.1
 */