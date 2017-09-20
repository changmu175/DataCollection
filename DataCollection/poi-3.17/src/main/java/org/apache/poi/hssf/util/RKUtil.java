/*  1:   */ package org.apache.poi.hssf.util;
/*  2:   */ 
/*  3:   */ public final class RKUtil
/*  4:   */ {
/*  5:   */   public static double decodeNumber(int number)
/*  6:   */   {
/*  7:36 */     long raw_number = number;
/*  8:   */     
/*  9:   */ 
/* 10:   */ 
/* 11:40 */     raw_number >>= 2;
/* 12:41 */     double rvalue = 0.0D;
/* 13:43 */     if ((number & 0x2) == 2) {
/* 14:47 */       rvalue = raw_number;
/* 15:   */     } else {
/* 16:55 */       rvalue = Double.longBitsToDouble(raw_number << 34);
/* 17:   */     }
/* 18:57 */     if ((number & 0x1) == 1) {
/* 19:63 */       rvalue /= 100.0D;
/* 20:   */     }
/* 21:66 */     return rvalue;
/* 22:   */   }
/* 23:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.util.RKUtil
 * JD-Core Version:    0.7.0.1
 */