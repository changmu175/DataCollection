/*   1:    */ package org.apache.poi.xssf.usermodel.charts;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.charts.AxisCrosses;
/*   4:    */ import org.apache.poi.ss.usermodel.charts.AxisOrientation;
/*   5:    */ import org.apache.poi.ss.usermodel.charts.AxisPosition;
/*   6:    */ import org.apache.poi.ss.usermodel.charts.AxisTickMark;
/*   7:    */ import org.apache.poi.ss.usermodel.charts.ChartAxis;
/*   8:    */ import org.apache.poi.util.Internal;
/*   9:    */ import org.apache.poi.xssf.usermodel.XSSFChart;
/*  10:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxPos;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTCrosses;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTDateAx;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTScaling;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTTickLblPos;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STTickLblPos;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  24:    */ 
/*  25:    */ public class XSSFDateAxis
/*  26:    */   extends XSSFChartAxis
/*  27:    */ {
/*  28:    */   private CTDateAx ctDateAx;
/*  29:    */   
/*  30:    */   public XSSFDateAxis(XSSFChart chart, long id, AxisPosition pos)
/*  31:    */   {
/*  32: 48 */     super(chart);
/*  33: 49 */     createAxis(id, pos);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public XSSFDateAxis(XSSFChart chart, CTDateAx ctDateAx)
/*  37:    */   {
/*  38: 53 */     super(chart);
/*  39: 54 */     this.ctDateAx = ctDateAx;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public long getId()
/*  43:    */   {
/*  44: 59 */     return this.ctDateAx.getAxId().getVal();
/*  45:    */   }
/*  46:    */   
/*  47:    */   @Internal
/*  48:    */   public CTShapeProperties getLine()
/*  49:    */   {
/*  50: 65 */     return this.ctDateAx.getSpPr();
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected CTAxPos getCTAxPos()
/*  54:    */   {
/*  55: 70 */     return this.ctDateAx.getAxPos();
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected CTNumFmt getCTNumFmt()
/*  59:    */   {
/*  60: 75 */     if (this.ctDateAx.isSetNumFmt()) {
/*  61: 76 */       return this.ctDateAx.getNumFmt();
/*  62:    */     }
/*  63: 78 */     return this.ctDateAx.addNewNumFmt();
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected CTScaling getCTScaling()
/*  67:    */   {
/*  68: 83 */     return this.ctDateAx.getScaling();
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected CTCrosses getCTCrosses()
/*  72:    */   {
/*  73: 88 */     return this.ctDateAx.getCrosses();
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected CTBoolean getDelete()
/*  77:    */   {
/*  78: 93 */     return this.ctDateAx.getDelete();
/*  79:    */   }
/*  80:    */   
/*  81:    */   protected CTTickMark getMajorCTTickMark()
/*  82:    */   {
/*  83: 98 */     return this.ctDateAx.getMajorTickMark();
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected CTTickMark getMinorCTTickMark()
/*  87:    */   {
/*  88:103 */     return this.ctDateAx.getMinorTickMark();
/*  89:    */   }
/*  90:    */   
/*  91:    */   @Internal
/*  92:    */   public CTChartLines getMajorGridLines()
/*  93:    */   {
/*  94:109 */     return this.ctDateAx.getMajorGridlines();
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void crossAxis(ChartAxis axis)
/*  98:    */   {
/*  99:114 */     this.ctDateAx.getCrossAx().setVal(axis.getId());
/* 100:    */   }
/* 101:    */   
/* 102:    */   private void createAxis(long id, AxisPosition pos)
/* 103:    */   {
/* 104:118 */     this.ctDateAx = this.chart.getCTChart().getPlotArea().addNewDateAx();
/* 105:119 */     this.ctDateAx.addNewAxId().setVal(id);
/* 106:120 */     this.ctDateAx.addNewAxPos();
/* 107:121 */     this.ctDateAx.addNewScaling();
/* 108:122 */     this.ctDateAx.addNewCrosses();
/* 109:123 */     this.ctDateAx.addNewCrossAx();
/* 110:124 */     this.ctDateAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);
/* 111:125 */     this.ctDateAx.addNewDelete();
/* 112:126 */     this.ctDateAx.addNewMajorTickMark();
/* 113:127 */     this.ctDateAx.addNewMinorTickMark();
/* 114:    */     
/* 115:129 */     setPosition(pos);
/* 116:130 */     setOrientation(AxisOrientation.MIN_MAX);
/* 117:131 */     setCrosses(AxisCrosses.AUTO_ZERO);
/* 118:132 */     setVisible(true);
/* 119:133 */     setMajorTickMark(AxisTickMark.CROSS);
/* 120:134 */     setMinorTickMark(AxisTickMark.NONE);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public boolean hasNumberFormat()
/* 124:    */   {
/* 125:139 */     return this.ctDateAx.isSetNumFmt();
/* 126:    */   }
/* 127:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.charts.XSSFDateAxis
 * JD-Core Version:    0.7.0.1
 */