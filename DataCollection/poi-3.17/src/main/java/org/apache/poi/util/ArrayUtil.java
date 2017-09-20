/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ public class ArrayUtil
/*   4:    */ {
/*   5:    */   public static void arraycopy(byte[] src, int src_position, byte[] dst, int dst_position, int length)
/*   6:    */   {
/*   7: 35 */     if (src_position < 0) {
/*   8: 36 */       throw new IllegalArgumentException("src_position was less than 0.  Actual value " + src_position);
/*   9:    */     }
/*  10: 37 */     if (src_position >= src.length) {
/*  11: 38 */       throw new IllegalArgumentException("src_position was greater than src array size.  Tried to write starting at position " + src_position + " but the array length is " + src.length);
/*  12:    */     }
/*  13: 39 */     if (src_position + length > src.length) {
/*  14: 40 */       throw new IllegalArgumentException("src_position + length would overrun the src array.  Expected end at " + (src_position + length) + " actual end at " + src.length);
/*  15:    */     }
/*  16: 41 */     if (dst_position < 0) {
/*  17: 42 */       throw new IllegalArgumentException("dst_position was less than 0.  Actual value " + dst_position);
/*  18:    */     }
/*  19: 43 */     if (dst_position >= dst.length) {
/*  20: 44 */       throw new IllegalArgumentException("dst_position was greater than dst array size.  Tried to write starting at position " + dst_position + " but the array length is " + dst.length);
/*  21:    */     }
/*  22: 45 */     if (dst_position + length > dst.length) {
/*  23: 46 */       throw new IllegalArgumentException("dst_position + length would overrun the dst array.  Expected end at " + (dst_position + length) + " actual end at " + dst.length);
/*  24:    */     }
/*  25: 48 */     System.arraycopy(src, src_position, dst, dst_position, length);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public static void arrayMoveWithin(Object[] array, int moveFrom, int moveTo, int numToMove)
/*  29:    */   {
/*  30: 61 */     if (numToMove <= 0) {
/*  31: 61 */       return;
/*  32:    */     }
/*  33: 62 */     if (moveFrom == moveTo) {
/*  34: 62 */       return;
/*  35:    */     }
/*  36: 65 */     if ((moveFrom < 0) || (moveFrom >= array.length)) {
/*  37: 66 */       throw new IllegalArgumentException("The moveFrom must be a valid array index");
/*  38:    */     }
/*  39: 68 */     if ((moveTo < 0) || (moveTo >= array.length)) {
/*  40: 69 */       throw new IllegalArgumentException("The moveTo must be a valid array index");
/*  41:    */     }
/*  42: 71 */     if (moveFrom + numToMove > array.length) {
/*  43: 72 */       throw new IllegalArgumentException("Asked to move more entries than the array has");
/*  44:    */     }
/*  45: 74 */     if (moveTo + numToMove > array.length) {
/*  46: 75 */       throw new IllegalArgumentException("Asked to move to a position that doesn't have enough space");
/*  47:    */     }
/*  48: 79 */     Object[] toMove = new Object[numToMove];
/*  49: 80 */     System.arraycopy(array, moveFrom, toMove, 0, numToMove);
/*  50:    */     int shiftTo;
/*  51:    */     Object[] toShift;
/*  52:    */     int shiftTo;
/*  53: 85 */     if (moveFrom > moveTo)
/*  54:    */     {
/*  55: 88 */       Object[] toShift = new Object[moveFrom - moveTo];
/*  56: 89 */       System.arraycopy(array, moveTo, toShift, 0, toShift.length);
/*  57: 90 */       shiftTo = moveTo + numToMove;
/*  58:    */     }
/*  59:    */     else
/*  60:    */     {
/*  61: 94 */       toShift = new Object[moveTo - moveFrom];
/*  62: 95 */       System.arraycopy(array, moveFrom + numToMove, toShift, 0, toShift.length);
/*  63: 96 */       shiftTo = moveFrom;
/*  64:    */     }
/*  65:100 */     System.arraycopy(toMove, 0, array, moveTo, toMove.length);
/*  66:    */     
/*  67:    */ 
/*  68:103 */     System.arraycopy(toShift, 0, array, shiftTo, toShift.length);
/*  69:    */   }
/*  70:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.ArrayUtil
 * JD-Core Version:    0.7.0.1
 */