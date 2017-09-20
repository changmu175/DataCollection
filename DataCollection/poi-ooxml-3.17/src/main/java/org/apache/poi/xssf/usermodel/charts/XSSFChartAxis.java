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
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTCrosses;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTScaling;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STAxPos;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STAxPos.Enum;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STCrosses;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STCrosses.Enum;
/*  24:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STOrientation;
/*  25:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STOrientation.Enum;
/*  26:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STTickMark;
/*  27:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STTickMark.Enum;
/*  28:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  29:    */ 
/*  30:    */ public abstract class XSSFChartAxis
/*  31:    */   implements ChartAxis
/*  32:    */ {
/*  33:    */   protected XSSFChart chart;
/*  34:    */   private static final double MIN_LOG_BASE = 2.0D;
/*  35:    */   private static final double MAX_LOG_BASE = 1000.0D;
/*  36:    */   
/*  37:    */   protected XSSFChartAxis(XSSFChart chart)
/*  38:    */   {
/*  39: 57 */     this.chart = chart;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public AxisPosition getPosition()
/*  43:    */   {
/*  44: 62 */     return toAxisPosition(getCTAxPos());
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setPosition(AxisPosition position)
/*  48:    */   {
/*  49: 67 */     getCTAxPos().setVal(fromAxisPosition(position));
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setNumberFormat(String format)
/*  53:    */   {
/*  54: 72 */     getCTNumFmt().setFormatCode(format);
/*  55: 73 */     getCTNumFmt().setSourceLinked(true);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String getNumberFormat()
/*  59:    */   {
/*  60: 78 */     return getCTNumFmt().getFormatCode();
/*  61:    */   }
/*  62:    */   
/*  63:    */   public boolean isSetLogBase()
/*  64:    */   {
/*  65: 83 */     return getCTScaling().isSetLogBase();
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setLogBase(double logBase)
/*  69:    */   {
/*  70: 88 */     if ((logBase < 2.0D) || (1000.0D < logBase)) {
/*  71: 90 */       throw new IllegalArgumentException("Axis log base must be between 2 and 1000 (inclusive), got: " + logBase);
/*  72:    */     }
/*  73: 92 */     CTScaling scaling = getCTScaling();
/*  74: 93 */     if (scaling.isSetLogBase()) {
/*  75: 94 */       scaling.getLogBase().setVal(logBase);
/*  76:    */     } else {
/*  77: 96 */       scaling.addNewLogBase().setVal(logBase);
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public double getLogBase()
/*  82:    */   {
/*  83:102 */     CTLogBase logBase = getCTScaling().getLogBase();
/*  84:103 */     if (logBase != null) {
/*  85:104 */       return logBase.getVal();
/*  86:    */     }
/*  87:106 */     return 0.0D;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public boolean isSetMinimum()
/*  91:    */   {
/*  92:111 */     return getCTScaling().isSetMin();
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setMinimum(double min)
/*  96:    */   {
/*  97:116 */     CTScaling scaling = getCTScaling();
/*  98:117 */     if (scaling.isSetMin()) {
/*  99:118 */       scaling.getMin().setVal(min);
/* 100:    */     } else {
/* 101:120 */       scaling.addNewMin().setVal(min);
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   public double getMinimum()
/* 106:    */   {
/* 107:126 */     CTScaling scaling = getCTScaling();
/* 108:127 */     if (scaling.isSetMin()) {
/* 109:128 */       return scaling.getMin().getVal();
/* 110:    */     }
/* 111:130 */     return 0.0D;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public boolean isSetMaximum()
/* 115:    */   {
/* 116:136 */     return getCTScaling().isSetMax();
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setMaximum(double max)
/* 120:    */   {
/* 121:141 */     CTScaling scaling = getCTScaling();
/* 122:142 */     if (scaling.isSetMax()) {
/* 123:143 */       scaling.getMax().setVal(max);
/* 124:    */     } else {
/* 125:145 */       scaling.addNewMax().setVal(max);
/* 126:    */     }
/* 127:    */   }
/* 128:    */   
/* 129:    */   public double getMaximum()
/* 130:    */   {
/* 131:151 */     CTScaling scaling = getCTScaling();
/* 132:152 */     if (scaling.isSetMax()) {
/* 133:153 */       return scaling.getMax().getVal();
/* 134:    */     }
/* 135:155 */     return 0.0D;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public AxisOrientation getOrientation()
/* 139:    */   {
/* 140:161 */     return toAxisOrientation(getCTScaling().getOrientation());
/* 141:    */   }
/* 142:    */   
/* 143:    */   public void setOrientation(AxisOrientation orientation)
/* 144:    */   {
/* 145:166 */     CTScaling scaling = getCTScaling();
/* 146:167 */     STOrientation.Enum stOrientation = fromAxisOrientation(orientation);
/* 147:168 */     if (scaling.isSetOrientation()) {
/* 148:169 */       scaling.getOrientation().setVal(stOrientation);
/* 149:    */     } else {
/* 150:171 */       getCTScaling().addNewOrientation().setVal(stOrientation);
/* 151:    */     }
/* 152:    */   }
/* 153:    */   
/* 154:    */   public AxisCrosses getCrosses()
/* 155:    */   {
/* 156:177 */     return toAxisCrosses(getCTCrosses());
/* 157:    */   }
/* 158:    */   
/* 159:    */   public void setCrosses(AxisCrosses crosses)
/* 160:    */   {
/* 161:182 */     getCTCrosses().setVal(fromAxisCrosses(crosses));
/* 162:    */   }
/* 163:    */   
/* 164:    */   public boolean isVisible()
/* 165:    */   {
/* 166:187 */     return !getDelete().getVal();
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void setVisible(boolean value)
/* 170:    */   {
/* 171:192 */     getDelete().setVal(!value);
/* 172:    */   }
/* 173:    */   
/* 174:    */   public AxisTickMark getMajorTickMark()
/* 175:    */   {
/* 176:197 */     return toAxisTickMark(getMajorCTTickMark());
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void setMajorTickMark(AxisTickMark tickMark)
/* 180:    */   {
/* 181:202 */     getMajorCTTickMark().setVal(fromAxisTickMark(tickMark));
/* 182:    */   }
/* 183:    */   
/* 184:    */   public AxisTickMark getMinorTickMark()
/* 185:    */   {
/* 186:207 */     return toAxisTickMark(getMinorCTTickMark());
/* 187:    */   }
/* 188:    */   
/* 189:    */   public void setMinorTickMark(AxisTickMark tickMark)
/* 190:    */   {
/* 191:212 */     getMinorCTTickMark().setVal(fromAxisTickMark(tickMark));
/* 192:    */   }
/* 193:    */   
/* 194:    */   protected abstract CTAxPos getCTAxPos();
/* 195:    */   
/* 196:    */   protected abstract CTNumFmt getCTNumFmt();
/* 197:    */   
/* 198:    */   protected abstract CTScaling getCTScaling();
/* 199:    */   
/* 200:    */   protected abstract CTCrosses getCTCrosses();
/* 201:    */   
/* 202:    */   protected abstract CTBoolean getDelete();
/* 203:    */   
/* 204:    */   protected abstract CTTickMark getMajorCTTickMark();
/* 205:    */   
/* 206:    */   protected abstract CTTickMark getMinorCTTickMark();
/* 207:    */   
/* 208:    */   @Internal
/* 209:    */   public abstract CTChartLines getMajorGridLines();
/* 210:    */   
/* 211:    */   @Internal
/* 212:    */   public abstract CTShapeProperties getLine();
/* 213:    */   
/* 214:    */   private static STOrientation.Enum fromAxisOrientation(AxisOrientation orientation)
/* 215:    */   {
/* 216:226 */     switch (orientation)
/* 217:    */     {
/* 218:    */     case MIN_MAX: 
/* 219:227 */       return STOrientation.MIN_MAX;
/* 220:    */     case MAX_MIN: 
/* 221:228 */       return STOrientation.MAX_MIN;
/* 222:    */     }
/* 223:230 */     throw new IllegalArgumentException();
/* 224:    */   }
/* 225:    */   
/* 226:    */   private static AxisOrientation toAxisOrientation(CTOrientation ctOrientation)
/* 227:    */   {
/* 228:235 */     switch (ctOrientation.getVal().intValue())
/* 229:    */     {
/* 230:    */     case 2: 
/* 231:236 */       return AxisOrientation.MIN_MAX;
/* 232:    */     case 1: 
/* 233:237 */       return AxisOrientation.MAX_MIN;
/* 234:    */     }
/* 235:239 */     throw new IllegalArgumentException();
/* 236:    */   }
/* 237:    */   
/* 238:    */   private static STCrosses.Enum fromAxisCrosses(AxisCrosses crosses)
/* 239:    */   {
/* 240:244 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$charts$AxisCrosses[crosses.ordinal()])
/* 241:    */     {
/* 242:    */     case 1: 
/* 243:245 */       return STCrosses.AUTO_ZERO;
/* 244:    */     case 2: 
/* 245:246 */       return STCrosses.MIN;
/* 246:    */     case 3: 
/* 247:247 */       return STCrosses.MAX;
/* 248:    */     }
/* 249:249 */     throw new IllegalArgumentException();
/* 250:    */   }
/* 251:    */   
/* 252:    */   private static AxisCrosses toAxisCrosses(CTCrosses ctCrosses)
/* 253:    */   {
/* 254:254 */     switch (ctCrosses.getVal().intValue())
/* 255:    */     {
/* 256:    */     case 1: 
/* 257:255 */       return AxisCrosses.AUTO_ZERO;
/* 258:    */     case 2: 
/* 259:256 */       return AxisCrosses.MAX;
/* 260:    */     case 3: 
/* 261:257 */       return AxisCrosses.MIN;
/* 262:    */     }
/* 263:259 */     throw new IllegalArgumentException();
/* 264:    */   }
/* 265:    */   
/* 266:    */   private static STAxPos.Enum fromAxisPosition(AxisPosition position)
/* 267:    */   {
/* 268:264 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$charts$AxisPosition[position.ordinal()])
/* 269:    */     {
/* 270:    */     case 1: 
/* 271:265 */       return STAxPos.B;
/* 272:    */     case 2: 
/* 273:266 */       return STAxPos.L;
/* 274:    */     case 3: 
/* 275:267 */       return STAxPos.R;
/* 276:    */     case 4: 
/* 277:268 */       return STAxPos.T;
/* 278:    */     }
/* 279:270 */     throw new IllegalArgumentException();
/* 280:    */   }
/* 281:    */   
/* 282:    */   private static AxisPosition toAxisPosition(CTAxPos ctAxPos)
/* 283:    */   {
/* 284:275 */     switch (ctAxPos.getVal().intValue())
/* 285:    */     {
/* 286:    */     case 1: 
/* 287:276 */       return AxisPosition.BOTTOM;
/* 288:    */     case 2: 
/* 289:277 */       return AxisPosition.LEFT;
/* 290:    */     case 3: 
/* 291:278 */       return AxisPosition.RIGHT;
/* 292:    */     case 4: 
/* 293:279 */       return AxisPosition.TOP;
/* 294:    */     }
/* 295:280 */     return AxisPosition.BOTTOM;
/* 296:    */   }
/* 297:    */   
/* 298:    */   private static STTickMark.Enum fromAxisTickMark(AxisTickMark tickMark)
/* 299:    */   {
/* 300:285 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$charts$AxisTickMark[tickMark.ordinal()])
/* 301:    */     {
/* 302:    */     case 1: 
/* 303:286 */       return STTickMark.NONE;
/* 304:    */     case 2: 
/* 305:287 */       return STTickMark.IN;
/* 306:    */     case 3: 
/* 307:288 */       return STTickMark.OUT;
/* 308:    */     case 4: 
/* 309:289 */       return STTickMark.CROSS;
/* 310:    */     }
/* 311:291 */     throw new IllegalArgumentException("Unknown AxisTickMark: " + tickMark);
/* 312:    */   }
/* 313:    */   
/* 314:    */   private static AxisTickMark toAxisTickMark(CTTickMark ctTickMark)
/* 315:    */   {
/* 316:296 */     switch (ctTickMark.getVal().intValue())
/* 317:    */     {
/* 318:    */     case 3: 
/* 319:297 */       return AxisTickMark.NONE;
/* 320:    */     case 2: 
/* 321:298 */       return AxisTickMark.IN;
/* 322:    */     case 4: 
/* 323:299 */       return AxisTickMark.OUT;
/* 324:    */     case 1: 
/* 325:300 */       return AxisTickMark.CROSS;
/* 326:    */     }
/* 327:301 */     return AxisTickMark.CROSS;
/* 328:    */   }
/* 329:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.charts.XSSFChartAxis
 * JD-Core Version:    0.7.0.1
 */