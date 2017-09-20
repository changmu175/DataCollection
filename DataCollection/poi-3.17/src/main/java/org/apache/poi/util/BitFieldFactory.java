/*  1:   */ package org.apache.poi.util;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ 
/*  6:   */ public class BitFieldFactory
/*  7:   */ {
/*  8:27 */   private static Map<Integer, BitField> instances = new HashMap();
/*  9:   */   
/* 10:   */   public static BitField getInstance(int mask)
/* 11:   */   {
/* 12:30 */     BitField f = (BitField)instances.get(Integer.valueOf(mask));
/* 13:31 */     if (f == null)
/* 14:   */     {
/* 15:32 */       f = new BitField(mask);
/* 16:33 */       instances.put(Integer.valueOf(mask), f);
/* 17:   */     }
/* 18:35 */     return f;
/* 19:   */   }
/* 20:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.BitFieldFactory
 * JD-Core Version:    0.7.0.1
 */