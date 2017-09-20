/*   1:    */ package org.apache.poi.xssf.usermodel.charts;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.ss.usermodel.Chart;
/*   6:    */ import org.apache.poi.ss.usermodel.charts.ChartAxis;
/*   7:    */ import org.apache.poi.ss.usermodel.charts.ChartDataSource;
/*   8:    */ import org.apache.poi.ss.usermodel.charts.ScatterChartData;
/*   9:    */ import org.apache.poi.ss.usermodel.charts.ScatterChartSeries;
/*  10:    */ import org.apache.poi.xssf.usermodel.XSSFChart;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterStyle;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STScatterStyle;
/*  20:    */ 
/*  21:    */ public class XSSFScatterChartData
/*  22:    */   implements ScatterChartData
/*  23:    */ {
/*  24:    */   private List<Series> series;
/*  25:    */   
/*  26:    */   public XSSFScatterChartData()
/*  27:    */   {
/*  28: 51 */     this.series = new ArrayList();
/*  29:    */   }
/*  30:    */   
/*  31:    */   static class Series
/*  32:    */     extends AbstractXSSFChartSeries
/*  33:    */     implements ScatterChartSeries
/*  34:    */   {
/*  35:    */     private int id;
/*  36:    */     private int order;
/*  37:    */     private ChartDataSource<?> xs;
/*  38:    */     private ChartDataSource<? extends Number> ys;
/*  39:    */     
/*  40:    */     protected Series(int id, int order, ChartDataSource<?> xs, ChartDataSource<? extends Number> ys)
/*  41:    */     {
/*  42: 67 */       this.id = id;
/*  43: 68 */       this.order = order;
/*  44: 69 */       this.xs = xs;
/*  45: 70 */       this.ys = ys;
/*  46:    */     }
/*  47:    */     
/*  48:    */     public ChartDataSource<?> getXValues()
/*  49:    */     {
/*  50: 78 */       return this.xs;
/*  51:    */     }
/*  52:    */     
/*  53:    */     public ChartDataSource<? extends Number> getYValues()
/*  54:    */     {
/*  55: 86 */       return this.ys;
/*  56:    */     }
/*  57:    */     
/*  58:    */     protected void addToChart(CTScatterChart ctScatterChart)
/*  59:    */     {
/*  60: 90 */       CTScatterSer scatterSer = ctScatterChart.addNewSer();
/*  61: 91 */       scatterSer.addNewIdx().setVal(this.id);
/*  62: 92 */       scatterSer.addNewOrder().setVal(this.order);
/*  63:    */       
/*  64: 94 */       CTAxDataSource xVal = scatterSer.addNewXVal();
/*  65: 95 */       XSSFChartUtil.buildAxDataSource(xVal, this.xs);
/*  66:    */       
/*  67: 97 */       CTNumDataSource yVal = scatterSer.addNewYVal();
/*  68: 98 */       XSSFChartUtil.buildNumDataSource(yVal, this.ys);
/*  69:100 */       if (isTitleSet()) {
/*  70:101 */         scatterSer.setTx(getCTSerTx());
/*  71:    */       }
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   public ScatterChartSeries addSerie(ChartDataSource<?> xs, ChartDataSource<? extends Number> ys)
/*  76:    */   {
/*  77:108 */     if (!ys.isNumeric()) {
/*  78:109 */       throw new IllegalArgumentException("Y axis data source must be numeric.");
/*  79:    */     }
/*  80:111 */     int numOfSeries = this.series.size();
/*  81:112 */     Series newSerie = new Series(numOfSeries, numOfSeries, xs, ys);
/*  82:113 */     this.series.add(newSerie);
/*  83:114 */     return newSerie;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void fillChart(Chart chart, ChartAxis... axis)
/*  87:    */   {
/*  88:118 */     if (!(chart instanceof XSSFChart)) {
/*  89:119 */       throw new IllegalArgumentException("Chart must be instance of XSSFChart");
/*  90:    */     }
/*  91:122 */     XSSFChart xssfChart = (XSSFChart)chart;
/*  92:123 */     CTPlotArea plotArea = xssfChart.getCTChart().getPlotArea();
/*  93:124 */     CTScatterChart scatterChart = plotArea.addNewScatterChart();
/*  94:125 */     addStyle(scatterChart);
/*  95:127 */     for (Series s : this.series) {
/*  96:128 */       s.addToChart(scatterChart);
/*  97:    */     }
/*  98:131 */     for (ChartAxis ax : axis) {
/*  99:132 */       scatterChart.addNewAxId().setVal(ax.getId());
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   public List<? extends Series> getSeries()
/* 104:    */   {
/* 105:137 */     return this.series;
/* 106:    */   }
/* 107:    */   
/* 108:    */   private void addStyle(CTScatterChart ctScatterChart)
/* 109:    */   {
/* 110:141 */     CTScatterStyle scatterStyle = ctScatterChart.addNewScatterStyle();
/* 111:142 */     scatterStyle.setVal(STScatterStyle.LINE_MARKER);
/* 112:    */   }
/* 113:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.charts.XSSFScatterChartData
 * JD-Core Version:    0.7.0.1
 */