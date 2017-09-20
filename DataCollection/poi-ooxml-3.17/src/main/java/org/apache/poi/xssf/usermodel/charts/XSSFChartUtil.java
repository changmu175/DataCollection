/*   1:    */ package org.apache.poi.xssf.usermodel.charts;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.charts.ChartDataSource;
/*   4:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource;
/*   5:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData;
/*   6:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource;
/*   7:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef;
/*   8:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumVal;
/*   9:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTStrData;
/*  10:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTStrVal;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt;
/*  13:    */ 
/*  14:    */ class XSSFChartUtil
/*  15:    */ {
/*  16:    */   public static void buildAxDataSource(CTAxDataSource ctAxDataSource, ChartDataSource<?> dataSource)
/*  17:    */   {
/*  18: 40 */     if (dataSource.isNumeric())
/*  19:    */     {
/*  20: 41 */       if (dataSource.isReference()) {
/*  21: 42 */         buildNumRef(ctAxDataSource.addNewNumRef(), dataSource);
/*  22:    */       } else {
/*  23: 44 */         buildNumLit(ctAxDataSource.addNewNumLit(), dataSource);
/*  24:    */       }
/*  25:    */     }
/*  26: 47 */     else if (dataSource.isReference()) {
/*  27: 48 */       buildStrRef(ctAxDataSource.addNewStrRef(), dataSource);
/*  28:    */     } else {
/*  29: 50 */       buildStrLit(ctAxDataSource.addNewStrLit(), dataSource);
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static void buildNumDataSource(CTNumDataSource ctNumDataSource, ChartDataSource<? extends Number> dataSource)
/*  34:    */   {
/*  35: 62 */     if (dataSource.isReference()) {
/*  36: 63 */       buildNumRef(ctNumDataSource.addNewNumRef(), dataSource);
/*  37:    */     } else {
/*  38: 65 */       buildNumLit(ctNumDataSource.addNewNumLit(), dataSource);
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   private static void buildNumRef(CTNumRef ctNumRef, ChartDataSource<?> dataSource)
/*  43:    */   {
/*  44: 70 */     ctNumRef.setF(dataSource.getFormulaString());
/*  45: 71 */     CTNumData cache = ctNumRef.addNewNumCache();
/*  46: 72 */     fillNumCache(cache, dataSource);
/*  47:    */   }
/*  48:    */   
/*  49:    */   private static void buildNumLit(CTNumData ctNumData, ChartDataSource<?> dataSource)
/*  50:    */   {
/*  51: 76 */     fillNumCache(ctNumData, dataSource);
/*  52:    */   }
/*  53:    */   
/*  54:    */   private static void buildStrRef(CTStrRef ctStrRef, ChartDataSource<?> dataSource)
/*  55:    */   {
/*  56: 80 */     ctStrRef.setF(dataSource.getFormulaString());
/*  57: 81 */     CTStrData cache = ctStrRef.addNewStrCache();
/*  58: 82 */     fillStringCache(cache, dataSource);
/*  59:    */   }
/*  60:    */   
/*  61:    */   private static void buildStrLit(CTStrData ctStrData, ChartDataSource<?> dataSource)
/*  62:    */   {
/*  63: 86 */     fillStringCache(ctStrData, dataSource);
/*  64:    */   }
/*  65:    */   
/*  66:    */   private static void fillStringCache(CTStrData cache, ChartDataSource<?> dataSource)
/*  67:    */   {
/*  68: 90 */     int numOfPoints = dataSource.getPointCount();
/*  69: 91 */     cache.addNewPtCount().setVal(numOfPoints);
/*  70: 92 */     for (int i = 0; i < numOfPoints; i++)
/*  71:    */     {
/*  72: 93 */       Object value = dataSource.getPointAt(i);
/*  73: 94 */       if (value != null)
/*  74:    */       {
/*  75: 95 */         CTStrVal ctStrVal = cache.addNewPt();
/*  76: 96 */         ctStrVal.setIdx(i);
/*  77: 97 */         ctStrVal.setV(value.toString());
/*  78:    */       }
/*  79:    */     }
/*  80:    */   }
/*  81:    */   
/*  82:    */   private static void fillNumCache(CTNumData cache, ChartDataSource<?> dataSource)
/*  83:    */   {
/*  84:104 */     int numOfPoints = dataSource.getPointCount();
/*  85:105 */     cache.addNewPtCount().setVal(numOfPoints);
/*  86:106 */     for (int i = 0; i < numOfPoints; i++)
/*  87:    */     {
/*  88:107 */       Number value = (Number)dataSource.getPointAt(i);
/*  89:108 */       if (value != null)
/*  90:    */       {
/*  91:109 */         CTNumVal ctNumVal = cache.addNewPt();
/*  92:110 */         ctNumVal.setIdx(i);
/*  93:111 */         ctNumVal.setV(value.toString());
/*  94:    */       }
/*  95:    */     }
/*  96:    */   }
/*  97:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.charts.XSSFChartUtil
 * JD-Core Version:    0.7.0.1
 */