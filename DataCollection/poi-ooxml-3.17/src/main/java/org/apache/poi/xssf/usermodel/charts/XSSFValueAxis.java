/*   1:    */ package org.apache.poi.xssf.usermodel.charts;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.charts.AxisCrossBetween;
/*   4:    */ import org.apache.poi.ss.usermodel.charts.AxisCrosses;
/*   5:    */ import org.apache.poi.ss.usermodel.charts.AxisOrientation;
/*   6:    */ import org.apache.poi.ss.usermodel.charts.AxisPosition;
/*   7:    */ import org.apache.poi.ss.usermodel.charts.AxisTickMark;
/*   8:    */ import org.apache.poi.ss.usermodel.charts.ChartAxis;
/*   9:    */ import org.apache.poi.ss.usermodel.charts.ValueAxis;
/*  10:    */ import org.apache.poi.util.Internal;
/*  11:    */ import org.apache.poi.xssf.usermodel.XSSFChart;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxPos;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTCrossBetween;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTCrosses;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTScaling;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTTickLblPos;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt;
/*  24:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx;
/*  25:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STCrossBetween;
/*  26:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STCrossBetween.Enum;
/*  27:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STTickLblPos;
/*  28:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  29:    */ 
/*  30:    */ public class XSSFValueAxis
/*  31:    */   extends XSSFChartAxis
/*  32:    */   implements ValueAxis
/*  33:    */ {
/*  34:    */   private CTValAx ctValAx;
/*  35:    */   
/*  36:    */   public XSSFValueAxis(XSSFChart chart, long id, AxisPosition pos)
/*  37:    */   {
/*  38: 53 */     super(chart);
/*  39: 54 */     createAxis(id, pos);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public XSSFValueAxis(XSSFChart chart, CTValAx ctValAx)
/*  43:    */   {
/*  44: 58 */     super(chart);
/*  45: 59 */     this.ctValAx = ctValAx;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public long getId()
/*  49:    */   {
/*  50: 64 */     return this.ctValAx.getAxId().getVal();
/*  51:    */   }
/*  52:    */   
/*  53:    */   @Internal
/*  54:    */   public CTShapeProperties getLine()
/*  55:    */   {
/*  56: 70 */     return this.ctValAx.getSpPr();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setCrossBetween(AxisCrossBetween crossBetween)
/*  60:    */   {
/*  61: 75 */     this.ctValAx.getCrossBetween().setVal(fromCrossBetween(crossBetween));
/*  62:    */   }
/*  63:    */   
/*  64:    */   public AxisCrossBetween getCrossBetween()
/*  65:    */   {
/*  66: 80 */     return toCrossBetween(this.ctValAx.getCrossBetween().getVal());
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected CTAxPos getCTAxPos()
/*  70:    */   {
/*  71: 85 */     return this.ctValAx.getAxPos();
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected CTNumFmt getCTNumFmt()
/*  75:    */   {
/*  76: 90 */     if (this.ctValAx.isSetNumFmt()) {
/*  77: 91 */       return this.ctValAx.getNumFmt();
/*  78:    */     }
/*  79: 93 */     return this.ctValAx.addNewNumFmt();
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected CTScaling getCTScaling()
/*  83:    */   {
/*  84: 98 */     return this.ctValAx.getScaling();
/*  85:    */   }
/*  86:    */   
/*  87:    */   protected CTCrosses getCTCrosses()
/*  88:    */   {
/*  89:103 */     return this.ctValAx.getCrosses();
/*  90:    */   }
/*  91:    */   
/*  92:    */   protected CTBoolean getDelete()
/*  93:    */   {
/*  94:108 */     return this.ctValAx.getDelete();
/*  95:    */   }
/*  96:    */   
/*  97:    */   protected CTTickMark getMajorCTTickMark()
/*  98:    */   {
/*  99:113 */     return this.ctValAx.getMajorTickMark();
/* 100:    */   }
/* 101:    */   
/* 102:    */   protected CTTickMark getMinorCTTickMark()
/* 103:    */   {
/* 104:118 */     return this.ctValAx.getMinorTickMark();
/* 105:    */   }
/* 106:    */   
/* 107:    */   @Internal
/* 108:    */   public CTChartLines getMajorGridLines()
/* 109:    */   {
/* 110:124 */     return this.ctValAx.getMajorGridlines();
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void crossAxis(ChartAxis axis)
/* 114:    */   {
/* 115:129 */     this.ctValAx.getCrossAx().setVal(axis.getId());
/* 116:    */   }
/* 117:    */   
/* 118:    */   private void createAxis(long id, AxisPosition pos)
/* 119:    */   {
/* 120:133 */     this.ctValAx = this.chart.getCTChart().getPlotArea().addNewValAx();
/* 121:134 */     this.ctValAx.addNewAxId().setVal(id);
/* 122:135 */     this.ctValAx.addNewAxPos();
/* 123:136 */     this.ctValAx.addNewScaling();
/* 124:137 */     this.ctValAx.addNewCrossBetween();
/* 125:138 */     this.ctValAx.addNewCrosses();
/* 126:139 */     this.ctValAx.addNewCrossAx();
/* 127:140 */     this.ctValAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);
/* 128:141 */     this.ctValAx.addNewDelete();
/* 129:142 */     this.ctValAx.addNewMajorTickMark();
/* 130:143 */     this.ctValAx.addNewMinorTickMark();
/* 131:    */     
/* 132:145 */     setPosition(pos);
/* 133:146 */     setOrientation(AxisOrientation.MIN_MAX);
/* 134:147 */     setCrossBetween(AxisCrossBetween.MIDPOINT_CATEGORY);
/* 135:148 */     setCrosses(AxisCrosses.AUTO_ZERO);
/* 136:149 */     setVisible(true);
/* 137:150 */     setMajorTickMark(AxisTickMark.CROSS);
/* 138:151 */     setMinorTickMark(AxisTickMark.NONE);
/* 139:    */   }
/* 140:    */   
/* 141:    */   private static STCrossBetween.Enum fromCrossBetween(AxisCrossBetween crossBetween)
/* 142:    */   {
/* 143:155 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$charts$AxisCrossBetween[crossBetween.ordinal()])
/* 144:    */     {
/* 145:    */     case 1: 
/* 146:156 */       return STCrossBetween.BETWEEN;
/* 147:    */     case 2: 
/* 148:157 */       return STCrossBetween.MID_CAT;
/* 149:    */     }
/* 150:159 */     throw new IllegalArgumentException();
/* 151:    */   }
/* 152:    */   
/* 153:    */   private static AxisCrossBetween toCrossBetween(STCrossBetween.Enum ctCrossBetween)
/* 154:    */   {
/* 155:164 */     switch (ctCrossBetween.intValue())
/* 156:    */     {
/* 157:    */     case 1: 
/* 158:165 */       return AxisCrossBetween.BETWEEN;
/* 159:    */     case 2: 
/* 160:166 */       return AxisCrossBetween.MIDPOINT_CATEGORY;
/* 161:    */     }
/* 162:168 */     throw new IllegalArgumentException();
/* 163:    */   }
/* 164:    */   
/* 165:    */   public boolean hasNumberFormat()
/* 166:    */   {
/* 167:174 */     return this.ctValAx.isSetNumFmt();
/* 168:    */   }
/* 169:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.charts.XSSFValueAxis
 * JD-Core Version:    0.7.0.1
 */