/*   1:    */ package org.apache.poi.xssf.usermodel.charts;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.ss.usermodel.Chart;
/*   6:    */ import org.apache.poi.ss.usermodel.charts.ChartAxis;
/*   7:    */ import org.apache.poi.ss.usermodel.charts.ChartDataSource;
/*   8:    */ import org.apache.poi.ss.usermodel.charts.LineChartData;
/*   9:    */ import org.apache.poi.ss.usermodel.charts.LineChartSeries;
/*  10:    */ import org.apache.poi.xssf.usermodel.XSSFChart;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTMarker;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTMarkerStyle;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STMarkerStyle;
/*  22:    */ 
/*  23:    */ public class XSSFLineChartData
/*  24:    */   implements LineChartData
/*  25:    */ {
/*  26:    */   private List<Series> series;
/*  27:    */   
/*  28:    */   public XSSFLineChartData()
/*  29:    */   {
/*  30: 49 */     this.series = new ArrayList();
/*  31:    */   }
/*  32:    */   
/*  33:    */   static class Series
/*  34:    */     extends AbstractXSSFChartSeries
/*  35:    */     implements LineChartSeries
/*  36:    */   {
/*  37:    */     private int id;
/*  38:    */     private int order;
/*  39:    */     private ChartDataSource<?> categories;
/*  40:    */     private ChartDataSource<? extends Number> values;
/*  41:    */     
/*  42:    */     protected Series(int id, int order, ChartDataSource<?> categories, ChartDataSource<? extends Number> values)
/*  43:    */     {
/*  44: 61 */       this.id = id;
/*  45: 62 */       this.order = order;
/*  46: 63 */       this.categories = categories;
/*  47: 64 */       this.values = values;
/*  48:    */     }
/*  49:    */     
/*  50:    */     public ChartDataSource<?> getCategoryAxisData()
/*  51:    */     {
/*  52: 68 */       return this.categories;
/*  53:    */     }
/*  54:    */     
/*  55:    */     public ChartDataSource<? extends Number> getValues()
/*  56:    */     {
/*  57: 72 */       return this.values;
/*  58:    */     }
/*  59:    */     
/*  60:    */     protected void addToChart(CTLineChart ctLineChart)
/*  61:    */     {
/*  62: 76 */       CTLineSer ctLineSer = ctLineChart.addNewSer();
/*  63: 77 */       ctLineSer.addNewIdx().setVal(this.id);
/*  64: 78 */       ctLineSer.addNewOrder().setVal(this.order);
/*  65:    */       
/*  66:    */ 
/*  67: 81 */       ctLineSer.addNewMarker().addNewSymbol().setVal(STMarkerStyle.NONE);
/*  68:    */       
/*  69: 83 */       CTAxDataSource catDS = ctLineSer.addNewCat();
/*  70: 84 */       XSSFChartUtil.buildAxDataSource(catDS, this.categories);
/*  71: 85 */       CTNumDataSource valueDS = ctLineSer.addNewVal();
/*  72: 86 */       XSSFChartUtil.buildNumDataSource(valueDS, this.values);
/*  73: 88 */       if (isTitleSet()) {
/*  74: 89 */         ctLineSer.setTx(getCTSerTx());
/*  75:    */       }
/*  76:    */     }
/*  77:    */   }
/*  78:    */   
/*  79:    */   public LineChartSeries addSeries(ChartDataSource<?> categoryAxisData, ChartDataSource<? extends Number> values)
/*  80:    */   {
/*  81: 95 */     if (!values.isNumeric()) {
/*  82: 96 */       throw new IllegalArgumentException("Value data source must be numeric.");
/*  83:    */     }
/*  84: 98 */     int numOfSeries = this.series.size();
/*  85: 99 */     Series newSeries = new Series(numOfSeries, numOfSeries, categoryAxisData, values);
/*  86:100 */     this.series.add(newSeries);
/*  87:101 */     return newSeries;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public List<? extends LineChartSeries> getSeries()
/*  91:    */   {
/*  92:105 */     return this.series;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void fillChart(Chart chart, ChartAxis... axis)
/*  96:    */   {
/*  97:109 */     if (!(chart instanceof XSSFChart)) {
/*  98:110 */       throw new IllegalArgumentException("Chart must be instance of XSSFChart");
/*  99:    */     }
/* 100:113 */     XSSFChart xssfChart = (XSSFChart)chart;
/* 101:114 */     CTPlotArea plotArea = xssfChart.getCTChart().getPlotArea();
/* 102:115 */     CTLineChart lineChart = plotArea.addNewLineChart();
/* 103:116 */     lineChart.addNewVaryColors().setVal(false);
/* 104:118 */     for (Series s : this.series) {
/* 105:119 */       s.addToChart(lineChart);
/* 106:    */     }
/* 107:122 */     for (ChartAxis ax : axis) {
/* 108:123 */       lineChart.addNewAxId().setVal(ax.getId());
/* 109:    */     }
/* 110:    */   }
/* 111:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.charts.XSSFLineChartData
 * JD-Core Version:    0.7.0.1
 */