/*  1:   */ package org.apache.poi.xssf.binary;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ import org.apache.poi.util.LittleEndian;
/*  5:   */ 
/*  6:   */ @Internal
/*  7:   */ class XSSFBCellRange
/*  8:   */ {
/*  9:   */   public static final int length = 16;
/* 10:   */   int firstRow;
/* 11:   */   int lastRow;
/* 12:   */   int firstCol;
/* 13:   */   int lastCol;
/* 14:   */   
/* 15:   */   public static XSSFBCellRange parse(byte[] data, int offset, XSSFBCellRange cellRange)
/* 16:   */   {
/* 17:41 */     if (cellRange == null) {
/* 18:42 */       cellRange = new XSSFBCellRange();
/* 19:   */     }
/* 20:44 */     cellRange.firstRow = XSSFBUtils.castToInt(LittleEndian.getUInt(data, offset));offset += 4;
/* 21:45 */     cellRange.lastRow = XSSFBUtils.castToInt(LittleEndian.getUInt(data, offset));offset += 4;
/* 22:46 */     cellRange.firstCol = XSSFBUtils.castToInt(LittleEndian.getUInt(data, offset));offset += 4;
/* 23:47 */     cellRange.lastCol = XSSFBUtils.castToInt(LittleEndian.getUInt(data, offset));
/* 24:   */     
/* 25:49 */     return cellRange;
/* 26:   */   }
/* 27:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFBCellRange
 * JD-Core Version:    0.7.0.1
 */