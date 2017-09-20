/*  1:   */ package org.apache.poi.xssf.usermodel.charts;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.charts.ChartDataFactory;
/*  4:   */ 
/*  5:   */ public class XSSFChartDataFactory
/*  6:   */   implements ChartDataFactory
/*  7:   */ {
/*  8:   */   private static XSSFChartDataFactory instance;
/*  9:   */   
/* 10:   */   public XSSFScatterChartData createScatterChartData()
/* 11:   */   {
/* 12:39 */     return new XSSFScatterChartData();
/* 13:   */   }
/* 14:   */   
/* 15:   */   public XSSFLineChartData createLineChartData()
/* 16:   */   {
/* 17:46 */     return new XSSFLineChartData();
/* 18:   */   }
/* 19:   */   
/* 20:   */   public static XSSFChartDataFactory getInstance()
/* 21:   */   {
/* 22:53 */     if (instance == null) {
/* 23:54 */       instance = new XSSFChartDataFactory();
/* 24:   */     }
/* 25:56 */     return instance;
/* 26:   */   }
/* 27:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.charts.XSSFChartDataFactory
 * JD-Core Version:    0.7.0.1
 */