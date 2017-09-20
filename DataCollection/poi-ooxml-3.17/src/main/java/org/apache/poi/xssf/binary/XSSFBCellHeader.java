/*  1:   */ package org.apache.poi.xssf.binary;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ import org.apache.poi.util.LittleEndian;
/*  5:   */ 
/*  6:   */ @Internal
/*  7:   */ class XSSFBCellHeader
/*  8:   */ {
/*  9:32 */   public static int length = 8;
/* 10:   */   private int rowNum;
/* 11:   */   private int colNum;
/* 12:   */   private int styleIdx;
/* 13:   */   private boolean showPhonetic;
/* 14:   */   
/* 15:   */   public static void parse(byte[] data, int offset, int currentRow, XSSFBCellHeader cell)
/* 16:   */   {
/* 17:42 */     int colNum = XSSFBUtils.castToInt(LittleEndian.getUInt(data, offset));offset += 4;
/* 18:43 */     int styleIdx = XSSFBUtils.get24BitInt(data, offset);offset += 3;
/* 19:   */     
/* 20:45 */     boolean showPhonetic = false;
/* 21:46 */     cell.reset(currentRow, colNum, styleIdx, showPhonetic);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void reset(int rowNum, int colNum, int styleIdx, boolean showPhonetic)
/* 25:   */   {
/* 26:55 */     this.rowNum = rowNum;
/* 27:56 */     this.colNum = colNum;
/* 28:57 */     this.styleIdx = styleIdx;
/* 29:58 */     this.showPhonetic = showPhonetic;
/* 30:   */   }
/* 31:   */   
/* 32:   */   int getColNum()
/* 33:   */   {
/* 34:62 */     return this.colNum;
/* 35:   */   }
/* 36:   */   
/* 37:   */   int getStyleIdx()
/* 38:   */   {
/* 39:66 */     return this.styleIdx;
/* 40:   */   }
/* 41:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFBCellHeader
 * JD-Core Version:    0.7.0.1
 */