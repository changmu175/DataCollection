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
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTCrosses;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTScaling;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTTickLblPos;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STTickLblPos;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  24:    */ 
/*  25:    */ public class XSSFCategoryAxis
/*  26:    */   extends XSSFChartAxis
/*  27:    */ {
/*  28:    */   private CTCatAx ctCatAx;
/*  29:    */   
/*  30:    */   public XSSFCategoryAxis(XSSFChart chart, long id, AxisPosition pos)
/*  31:    */   {
/*  32: 50 */     super(chart);
/*  33: 51 */     createAxis(id, pos);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public XSSFCategoryAxis(XSSFChart chart, CTCatAx ctCatAx)
/*  37:    */   {
/*  38: 55 */     super(chart);
/*  39: 56 */     this.ctCatAx = ctCatAx;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public long getId()
/*  43:    */   {
/*  44: 61 */     return this.ctCatAx.getAxId().getVal();
/*  45:    */   }
/*  46:    */   
/*  47:    */   @Internal
/*  48:    */   public CTShapeProperties getLine()
/*  49:    */   {
/*  50: 67 */     return this.ctCatAx.getSpPr();
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected CTAxPos getCTAxPos()
/*  54:    */   {
/*  55: 72 */     return this.ctCatAx.getAxPos();
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected CTNumFmt getCTNumFmt()
/*  59:    */   {
/*  60: 77 */     if (this.ctCatAx.isSetNumFmt()) {
/*  61: 78 */       return this.ctCatAx.getNumFmt();
/*  62:    */     }
/*  63: 80 */     return this.ctCatAx.addNewNumFmt();
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected CTScaling getCTScaling()
/*  67:    */   {
/*  68: 85 */     return this.ctCatAx.getScaling();
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected CTCrosses getCTCrosses()
/*  72:    */   {
/*  73: 90 */     return this.ctCatAx.getCrosses();
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected CTBoolean getDelete()
/*  77:    */   {
/*  78: 95 */     return this.ctCatAx.getDelete();
/*  79:    */   }
/*  80:    */   
/*  81:    */   protected CTTickMark getMajorCTTickMark()
/*  82:    */   {
/*  83:100 */     return this.ctCatAx.getMajorTickMark();
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected CTTickMark getMinorCTTickMark()
/*  87:    */   {
/*  88:105 */     return this.ctCatAx.getMinorTickMark();
/*  89:    */   }
/*  90:    */   
/*  91:    */   @Internal
/*  92:    */   public CTChartLines getMajorGridLines()
/*  93:    */   {
/*  94:111 */     return this.ctCatAx.getMajorGridlines();
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void crossAxis(ChartAxis axis)
/*  98:    */   {
/*  99:116 */     this.ctCatAx.getCrossAx().setVal(axis.getId());
/* 100:    */   }
/* 101:    */   
/* 102:    */   private void createAxis(long id, AxisPosition pos)
/* 103:    */   {
/* 104:120 */     this.ctCatAx = this.chart.getCTChart().getPlotArea().addNewCatAx();
/* 105:121 */     this.ctCatAx.addNewAxId().setVal(id);
/* 106:122 */     this.ctCatAx.addNewAxPos();
/* 107:123 */     this.ctCatAx.addNewScaling();
/* 108:124 */     this.ctCatAx.addNewCrosses();
/* 109:125 */     this.ctCatAx.addNewCrossAx();
/* 110:126 */     this.ctCatAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);
/* 111:127 */     this.ctCatAx.addNewDelete();
/* 112:128 */     this.ctCatAx.addNewMajorTickMark();
/* 113:129 */     this.ctCatAx.addNewMinorTickMark();
/* 114:    */     
/* 115:131 */     setPosition(pos);
/* 116:132 */     setOrientation(AxisOrientation.MIN_MAX);
/* 117:133 */     setCrosses(AxisCrosses.AUTO_ZERO);
/* 118:134 */     setVisible(true);
/* 119:135 */     setMajorTickMark(AxisTickMark.CROSS);
/* 120:136 */     setMinorTickMark(AxisTickMark.NONE);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public boolean hasNumberFormat()
/* 124:    */   {
/* 125:141 */     return this.ctCatAx.isSetNumFmt();
/* 126:    */   }
/* 127:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.charts.XSSFCategoryAxis
 * JD-Core Version:    0.7.0.1
 */