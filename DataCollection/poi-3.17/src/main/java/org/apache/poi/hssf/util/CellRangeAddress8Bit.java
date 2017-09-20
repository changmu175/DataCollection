/*  1:   */ package org.apache.poi.hssf.util;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.util.CellRangeAddressBase;
/*  4:   */ import org.apache.poi.util.LittleEndianInput;
/*  5:   */ import org.apache.poi.util.LittleEndianOutput;
/*  6:   */ 
/*  7:   */ public final class CellRangeAddress8Bit
/*  8:   */   extends CellRangeAddressBase
/*  9:   */ {
/* 10:   */   public static final int ENCODED_SIZE = 6;
/* 11:   */   
/* 12:   */   public CellRangeAddress8Bit(int firstRow, int lastRow, int firstCol, int lastCol)
/* 13:   */   {
/* 14:34 */     super(firstRow, lastRow, firstCol, lastCol);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public CellRangeAddress8Bit(LittleEndianInput in)
/* 18:   */   {
/* 19:38 */     super(readUShortAndCheck(in), in.readUShort(), in.readUByte(), in.readUByte());
/* 20:   */   }
/* 21:   */   
/* 22:   */   private static int readUShortAndCheck(LittleEndianInput in)
/* 23:   */   {
/* 24:42 */     if (in.available() < 6) {
/* 25:44 */       throw new RuntimeException("Ran out of data reading CellRangeAddress");
/* 26:   */     }
/* 27:46 */     return in.readUShort();
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void serialize(LittleEndianOutput out)
/* 31:   */   {
/* 32:50 */     out.writeShort(getFirstRow());
/* 33:51 */     out.writeShort(getLastRow());
/* 34:52 */     out.writeByte(getFirstColumn());
/* 35:53 */     out.writeByte(getLastColumn());
/* 36:   */   }
/* 37:   */   
/* 38:   */   public CellRangeAddress8Bit copy()
/* 39:   */   {
/* 40:57 */     return new CellRangeAddress8Bit(getFirstRow(), getLastRow(), getFirstColumn(), getLastColumn());
/* 41:   */   }
/* 42:   */   
/* 43:   */   public static int getEncodedSize(int numberOfItems)
/* 44:   */   {
/* 45:61 */     return numberOfItems * 6;
/* 46:   */   }
/* 47:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.util.CellRangeAddress8Bit
 * JD-Core Version:    0.7.0.1
 */