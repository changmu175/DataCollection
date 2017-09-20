/*  1:   */ package org.apache.poi.hssf.util;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ 
/*  6:   */ public class LazilyConcatenatedByteArray
/*  7:   */ {
/*  8:29 */   private final List<byte[]> arrays = new ArrayList(1);
/*  9:   */   
/* 10:   */   public void clear()
/* 11:   */   {
/* 12:35 */     this.arrays.clear();
/* 13:   */   }
/* 14:   */   
/* 15:   */   public void concatenate(byte[] array)
/* 16:   */   {
/* 17:46 */     if (array == null) {
/* 18:47 */       throw new IllegalArgumentException("array cannot be null");
/* 19:   */     }
/* 20:49 */     this.arrays.add(array);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public byte[] toArray()
/* 24:   */   {
/* 25:61 */     if (this.arrays.isEmpty()) {
/* 26:62 */       return null;
/* 27:   */     }
/* 28:63 */     if (this.arrays.size() > 1)
/* 29:   */     {
/* 30:64 */       int totalLength = 0;
/* 31:65 */       for (byte[] array : this.arrays) {
/* 32:66 */         totalLength += array.length;
/* 33:   */       }
/* 34:69 */       byte[] concatenated = new byte[totalLength];
/* 35:70 */       int destPos = 0;
/* 36:71 */       for (byte[] array : this.arrays)
/* 37:   */       {
/* 38:72 */         System.arraycopy(array, 0, concatenated, destPos, array.length);
/* 39:73 */         destPos += array.length;
/* 40:   */       }
/* 41:76 */       this.arrays.clear();
/* 42:77 */       this.arrays.add(concatenated);
/* 43:   */     }
/* 44:80 */     return (byte[])this.arrays.get(0);
/* 45:   */   }
/* 46:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.util.LazilyConcatenatedByteArray
 * JD-Core Version:    0.7.0.1
 */