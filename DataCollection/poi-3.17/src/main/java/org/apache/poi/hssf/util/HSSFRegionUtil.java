/*   1:    */ package org.apache.poi.hssf.util;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.usermodel.HSSFSheet;
/*   4:    */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*   5:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*   6:    */ import org.apache.poi.ss.util.RegionUtil;
/*   7:    */ 
/*   8:    */ public final class HSSFRegionUtil
/*   9:    */ {
/*  10:    */   public static void setBorderLeft(int border, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook)
/*  11:    */   {
/*  12: 45 */     RegionUtil.setBorderLeft(border, region, sheet);
/*  13:    */   }
/*  14:    */   
/*  15:    */   public static void setLeftBorderColor(int color, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook)
/*  16:    */   {
/*  17: 58 */     RegionUtil.setLeftBorderColor(color, region, sheet);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public static void setBorderRight(int border, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook)
/*  21:    */   {
/*  22: 71 */     RegionUtil.setBorderRight(border, region, sheet);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static void setRightBorderColor(int color, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook)
/*  26:    */   {
/*  27: 84 */     RegionUtil.setRightBorderColor(color, region, sheet);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static void setBorderBottom(int border, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook)
/*  31:    */   {
/*  32: 97 */     RegionUtil.setBorderBottom(border, region, sheet);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static void setBottomBorderColor(int color, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook)
/*  36:    */   {
/*  37:110 */     RegionUtil.setBottomBorderColor(color, region, sheet);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static void setBorderTop(int border, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook)
/*  41:    */   {
/*  42:123 */     RegionUtil.setBorderTop(border, region, sheet);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public static void setTopBorderColor(int color, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook)
/*  46:    */   {
/*  47:136 */     RegionUtil.setTopBorderColor(color, region, sheet);
/*  48:    */   }
/*  49:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.util.HSSFRegionUtil
 * JD-Core Version:    0.7.0.1
 */