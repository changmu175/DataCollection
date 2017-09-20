/*   1:    */ package org.apache.poi.xssf.usermodel.charts;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.charts.ChartLegend;
/*   4:    */ import org.apache.poi.ss.usermodel.charts.LegendPosition;
/*   5:    */ import org.apache.poi.util.Internal;
/*   6:    */ import org.apache.poi.xssf.usermodel.XSSFChart;
/*   7:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean;
/*   8:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
/*   9:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTLegend;
/*  10:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTLegendPos;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STLegendPos;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STLegendPos.Enum;
/*  13:    */ 
/*  14:    */ public final class XSSFChartLegend
/*  15:    */   implements ChartLegend
/*  16:    */ {
/*  17:    */   private CTLegend legend;
/*  18:    */   
/*  19:    */   public XSSFChartLegend(XSSFChart chart)
/*  20:    */   {
/*  21: 47 */     CTChart ctChart = chart.getCTChart();
/*  22: 48 */     this.legend = (ctChart.isSetLegend() ? ctChart.getLegend() : ctChart.addNewLegend());
/*  23:    */     
/*  24:    */ 
/*  25:    */ 
/*  26: 52 */     setDefaults();
/*  27:    */   }
/*  28:    */   
/*  29:    */   private void setDefaults()
/*  30:    */   {
/*  31: 59 */     if (!this.legend.isSetOverlay()) {
/*  32: 60 */       this.legend.addNewOverlay();
/*  33:    */     }
/*  34: 62 */     this.legend.getOverlay().setVal(false);
/*  35:    */   }
/*  36:    */   
/*  37:    */   @Internal
/*  38:    */   public CTLegend getCTLegend()
/*  39:    */   {
/*  40: 72 */     return this.legend;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setPosition(LegendPosition position)
/*  44:    */   {
/*  45: 76 */     if (!this.legend.isSetLegendPos()) {
/*  46: 77 */       this.legend.addNewLegendPos();
/*  47:    */     }
/*  48: 79 */     this.legend.getLegendPos().setVal(fromLegendPosition(position));
/*  49:    */   }
/*  50:    */   
/*  51:    */   public LegendPosition getPosition()
/*  52:    */   {
/*  53: 86 */     if (this.legend.isSetLegendPos()) {
/*  54: 87 */       return toLegendPosition(this.legend.getLegendPos());
/*  55:    */     }
/*  56: 89 */     return LegendPosition.RIGHT;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public XSSFManualLayout getManualLayout()
/*  60:    */   {
/*  61: 94 */     if (!this.legend.isSetLayout()) {
/*  62: 95 */       this.legend.addNewLayout();
/*  63:    */     }
/*  64: 97 */     return new XSSFManualLayout(this.legend.getLayout());
/*  65:    */   }
/*  66:    */   
/*  67:    */   public boolean isOverlay()
/*  68:    */   {
/*  69:101 */     return this.legend.getOverlay().getVal();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setOverlay(boolean value)
/*  73:    */   {
/*  74:105 */     this.legend.getOverlay().setVal(value);
/*  75:    */   }
/*  76:    */   
/*  77:    */   private STLegendPos.Enum fromLegendPosition(LegendPosition position)
/*  78:    */   {
/*  79:109 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$charts$LegendPosition[position.ordinal()])
/*  80:    */     {
/*  81:    */     case 1: 
/*  82:110 */       return STLegendPos.B;
/*  83:    */     case 2: 
/*  84:111 */       return STLegendPos.L;
/*  85:    */     case 3: 
/*  86:112 */       return STLegendPos.R;
/*  87:    */     case 4: 
/*  88:113 */       return STLegendPos.T;
/*  89:    */     case 5: 
/*  90:114 */       return STLegendPos.TR;
/*  91:    */     }
/*  92:116 */     throw new IllegalArgumentException();
/*  93:    */   }
/*  94:    */   
/*  95:    */   private LegendPosition toLegendPosition(CTLegendPos ctLegendPos)
/*  96:    */   {
/*  97:121 */     switch (ctLegendPos.getVal().intValue())
/*  98:    */     {
/*  99:    */     case 1: 
/* 100:122 */       return LegendPosition.BOTTOM;
/* 101:    */     case 3: 
/* 102:123 */       return LegendPosition.LEFT;
/* 103:    */     case 4: 
/* 104:124 */       return LegendPosition.RIGHT;
/* 105:    */     case 5: 
/* 106:125 */       return LegendPosition.TOP;
/* 107:    */     case 2: 
/* 108:126 */       return LegendPosition.TOP_RIGHT;
/* 109:    */     }
/* 110:128 */     throw new IllegalArgumentException();
/* 111:    */   }
/* 112:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.charts.XSSFChartLegend
 * JD-Core Version:    0.7.0.1
 */