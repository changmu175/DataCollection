/*   1:    */ package org.apache.poi.xssf.usermodel.charts;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.charts.LayoutMode;
/*   4:    */ import org.apache.poi.ss.usermodel.charts.LayoutTarget;
/*   5:    */ import org.apache.poi.ss.usermodel.charts.ManualLayout;
/*   6:    */ import org.apache.poi.util.Internal;
/*   7:    */ import org.apache.poi.xssf.usermodel.XSSFChart;
/*   8:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
/*   9:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble;
/*  10:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STLayoutMode;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STLayoutMode.Enum;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STLayoutTarget;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.STLayoutTarget.Enum;
/*  19:    */ 
/*  20:    */ public final class XSSFManualLayout
/*  21:    */   implements ManualLayout
/*  22:    */ {
/*  23:    */   private CTManualLayout layout;
/*  24: 46 */   private static final LayoutMode defaultLayoutMode = LayoutMode.EDGE;
/*  25: 47 */   private static final LayoutTarget defaultLayoutTarget = LayoutTarget.INNER;
/*  26:    */   
/*  27:    */   public XSSFManualLayout(CTLayout ctLayout)
/*  28:    */   {
/*  29: 54 */     initLayout(ctLayout);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public XSSFManualLayout(XSSFChart chart)
/*  33:    */   {
/*  34: 62 */     CTPlotArea ctPlotArea = chart.getCTChart().getPlotArea();
/*  35: 63 */     CTLayout ctLayout = ctPlotArea.isSetLayout() ? ctPlotArea.getLayout() : ctPlotArea.addNewLayout();
/*  36:    */     
/*  37:    */ 
/*  38: 66 */     initLayout(ctLayout);
/*  39:    */   }
/*  40:    */   
/*  41:    */   @Internal
/*  42:    */   public CTManualLayout getCTManualLayout()
/*  43:    */   {
/*  44: 75 */     return this.layout;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setWidthRatio(double ratio)
/*  48:    */   {
/*  49: 79 */     if (!this.layout.isSetW()) {
/*  50: 80 */       this.layout.addNewW();
/*  51:    */     }
/*  52: 82 */     this.layout.getW().setVal(ratio);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public double getWidthRatio()
/*  56:    */   {
/*  57: 86 */     if (!this.layout.isSetW()) {
/*  58: 87 */       return 0.0D;
/*  59:    */     }
/*  60: 89 */     return this.layout.getW().getVal();
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setHeightRatio(double ratio)
/*  64:    */   {
/*  65: 93 */     if (!this.layout.isSetH()) {
/*  66: 94 */       this.layout.addNewH();
/*  67:    */     }
/*  68: 96 */     this.layout.getH().setVal(ratio);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public double getHeightRatio()
/*  72:    */   {
/*  73:100 */     if (!this.layout.isSetH()) {
/*  74:101 */       return 0.0D;
/*  75:    */     }
/*  76:103 */     return this.layout.getH().getVal();
/*  77:    */   }
/*  78:    */   
/*  79:    */   public LayoutTarget getTarget()
/*  80:    */   {
/*  81:107 */     if (!this.layout.isSetLayoutTarget()) {
/*  82:108 */       return defaultLayoutTarget;
/*  83:    */     }
/*  84:110 */     return toLayoutTarget(this.layout.getLayoutTarget());
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setTarget(LayoutTarget target)
/*  88:    */   {
/*  89:114 */     if (!this.layout.isSetLayoutTarget()) {
/*  90:115 */       this.layout.addNewLayoutTarget();
/*  91:    */     }
/*  92:117 */     this.layout.getLayoutTarget().setVal(fromLayoutTarget(target));
/*  93:    */   }
/*  94:    */   
/*  95:    */   public LayoutMode getXMode()
/*  96:    */   {
/*  97:121 */     if (!this.layout.isSetXMode()) {
/*  98:122 */       return defaultLayoutMode;
/*  99:    */     }
/* 100:124 */     return toLayoutMode(this.layout.getXMode());
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void setXMode(LayoutMode mode)
/* 104:    */   {
/* 105:128 */     if (!this.layout.isSetXMode()) {
/* 106:129 */       this.layout.addNewXMode();
/* 107:    */     }
/* 108:131 */     this.layout.getXMode().setVal(fromLayoutMode(mode));
/* 109:    */   }
/* 110:    */   
/* 111:    */   public LayoutMode getYMode()
/* 112:    */   {
/* 113:135 */     if (!this.layout.isSetYMode()) {
/* 114:136 */       return defaultLayoutMode;
/* 115:    */     }
/* 116:138 */     return toLayoutMode(this.layout.getYMode());
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setYMode(LayoutMode mode)
/* 120:    */   {
/* 121:142 */     if (!this.layout.isSetYMode()) {
/* 122:143 */       this.layout.addNewYMode();
/* 123:    */     }
/* 124:145 */     this.layout.getYMode().setVal(fromLayoutMode(mode));
/* 125:    */   }
/* 126:    */   
/* 127:    */   public double getX()
/* 128:    */   {
/* 129:149 */     if (!this.layout.isSetX()) {
/* 130:150 */       return 0.0D;
/* 131:    */     }
/* 132:152 */     return this.layout.getX().getVal();
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void setX(double x)
/* 136:    */   {
/* 137:156 */     if (!this.layout.isSetX()) {
/* 138:157 */       this.layout.addNewX();
/* 139:    */     }
/* 140:159 */     this.layout.getX().setVal(x);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public double getY()
/* 144:    */   {
/* 145:163 */     if (!this.layout.isSetY()) {
/* 146:164 */       return 0.0D;
/* 147:    */     }
/* 148:166 */     return this.layout.getY().getVal();
/* 149:    */   }
/* 150:    */   
/* 151:    */   public void setY(double y)
/* 152:    */   {
/* 153:170 */     if (!this.layout.isSetY()) {
/* 154:171 */       this.layout.addNewY();
/* 155:    */     }
/* 156:173 */     this.layout.getY().setVal(y);
/* 157:    */   }
/* 158:    */   
/* 159:    */   public LayoutMode getWidthMode()
/* 160:    */   {
/* 161:177 */     if (!this.layout.isSetWMode()) {
/* 162:178 */       return defaultLayoutMode;
/* 163:    */     }
/* 164:180 */     return toLayoutMode(this.layout.getWMode());
/* 165:    */   }
/* 166:    */   
/* 167:    */   public void setWidthMode(LayoutMode mode)
/* 168:    */   {
/* 169:184 */     if (!this.layout.isSetWMode()) {
/* 170:185 */       this.layout.addNewWMode();
/* 171:    */     }
/* 172:187 */     this.layout.getWMode().setVal(fromLayoutMode(mode));
/* 173:    */   }
/* 174:    */   
/* 175:    */   public LayoutMode getHeightMode()
/* 176:    */   {
/* 177:191 */     if (!this.layout.isSetHMode()) {
/* 178:192 */       return defaultLayoutMode;
/* 179:    */     }
/* 180:194 */     return toLayoutMode(this.layout.getHMode());
/* 181:    */   }
/* 182:    */   
/* 183:    */   public void setHeightMode(LayoutMode mode)
/* 184:    */   {
/* 185:198 */     if (!this.layout.isSetHMode()) {
/* 186:199 */       this.layout.addNewHMode();
/* 187:    */     }
/* 188:201 */     this.layout.getHMode().setVal(fromLayoutMode(mode));
/* 189:    */   }
/* 190:    */   
/* 191:    */   private void initLayout(CTLayout ctLayout)
/* 192:    */   {
/* 193:205 */     if (ctLayout.isSetManualLayout()) {
/* 194:206 */       this.layout = ctLayout.getManualLayout();
/* 195:    */     } else {
/* 196:208 */       this.layout = ctLayout.addNewManualLayout();
/* 197:    */     }
/* 198:    */   }
/* 199:    */   
/* 200:    */   private STLayoutMode.Enum fromLayoutMode(LayoutMode mode)
/* 201:    */   {
/* 202:213 */     switch (mode)
/* 203:    */     {
/* 204:    */     case EDGE: 
/* 205:214 */       return STLayoutMode.EDGE;
/* 206:    */     case FACTOR: 
/* 207:215 */       return STLayoutMode.FACTOR;
/* 208:    */     }
/* 209:217 */     throw new IllegalArgumentException();
/* 210:    */   }
/* 211:    */   
/* 212:    */   private LayoutMode toLayoutMode(CTLayoutMode ctLayoutMode)
/* 213:    */   {
/* 214:222 */     switch (ctLayoutMode.getVal().intValue())
/* 215:    */     {
/* 216:    */     case 1: 
/* 217:223 */       return LayoutMode.EDGE;
/* 218:    */     case 2: 
/* 219:224 */       return LayoutMode.FACTOR;
/* 220:    */     }
/* 221:226 */     throw new IllegalArgumentException();
/* 222:    */   }
/* 223:    */   
/* 224:    */   private STLayoutTarget.Enum fromLayoutTarget(LayoutTarget target)
/* 225:    */   {
/* 226:231 */     switch (target)
/* 227:    */     {
/* 228:    */     case INNER: 
/* 229:232 */       return STLayoutTarget.INNER;
/* 230:    */     case OUTER: 
/* 231:233 */       return STLayoutTarget.OUTER;
/* 232:    */     }
/* 233:235 */     throw new IllegalArgumentException();
/* 234:    */   }
/* 235:    */   
/* 236:    */   private LayoutTarget toLayoutTarget(CTLayoutTarget ctLayoutTarget)
/* 237:    */   {
/* 238:240 */     switch (ctLayoutTarget.getVal().intValue())
/* 239:    */     {
/* 240:    */     case 1: 
/* 241:241 */       return LayoutTarget.INNER;
/* 242:    */     case 2: 
/* 243:242 */       return LayoutTarget.OUTER;
/* 244:    */     }
/* 245:244 */     throw new IllegalArgumentException();
/* 246:    */   }
/* 247:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.charts.XSSFManualLayout
 * JD-Core Version:    0.7.0.1
 */