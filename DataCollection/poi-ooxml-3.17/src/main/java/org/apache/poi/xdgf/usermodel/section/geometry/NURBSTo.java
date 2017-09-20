/*   1:    */ package org.apache.poi.xdgf.usermodel.section.geometry;
/*   2:    */ 
/*   3:    */ import com.graphbuilder.curve.ControlPath;
/*   4:    */ import com.graphbuilder.curve.ShapeMultiPath;
/*   5:    */ import com.graphbuilder.curve.ValueVector;
/*   6:    */ import com.graphbuilder.geom.PointFactory;
/*   7:    */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*   8:    */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*   9:    */ import java.awt.geom.Path2D.Double;
/*  10:    */ import java.awt.geom.Point2D;
/*  11:    */ import org.apache.poi.POIXMLException;
/*  12:    */ import org.apache.poi.xdgf.geom.SplineRenderer;
/*  13:    */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/*  14:    */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*  15:    */ 
/*  16:    */ public class NURBSTo
/*  17:    */   implements GeometryRow
/*  18:    */ {
/*  19: 36 */   NURBSTo _master = null;
/*  20: 39 */   Double x = null;
/*  21: 42 */   Double y = null;
/*  22: 45 */   Double a = null;
/*  23: 48 */   Double b = null;
/*  24: 51 */   Double c = null;
/*  25: 54 */   Double d = null;
/*  26: 57 */   String e = null;
/*  27: 59 */   Boolean deleted = null;
/*  28:    */   
/*  29:    */   public NURBSTo(RowType row)
/*  30:    */   {
/*  31: 65 */     if (row.isSetDel()) {
/*  32: 66 */       this.deleted = Boolean.valueOf(row.getDel());
/*  33:    */     }
/*  34: 68 */     for (CellType cell : row.getCellArray())
/*  35:    */     {
/*  36: 69 */       String cellName = cell.getN();
/*  37: 71 */       if (cellName.equals("X")) {
/*  38: 72 */         this.x = XDGFCell.parseDoubleValue(cell);
/*  39: 73 */       } else if (cellName.equals("Y")) {
/*  40: 74 */         this.y = XDGFCell.parseDoubleValue(cell);
/*  41: 75 */       } else if (cellName.equals("A")) {
/*  42: 76 */         this.a = XDGFCell.parseDoubleValue(cell);
/*  43: 77 */       } else if (cellName.equals("B")) {
/*  44: 78 */         this.b = XDGFCell.parseDoubleValue(cell);
/*  45: 79 */       } else if (cellName.equals("C")) {
/*  46: 80 */         this.c = XDGFCell.parseDoubleValue(cell);
/*  47: 81 */       } else if (cellName.equals("D")) {
/*  48: 82 */         this.d = XDGFCell.parseDoubleValue(cell);
/*  49: 83 */       } else if (cellName.equals("E")) {
/*  50: 84 */         this.e = cell.getV();
/*  51:    */       } else {
/*  52: 86 */         throw new POIXMLException("Invalid cell '" + cellName + "' in NURBS row");
/*  53:    */       }
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public boolean getDel()
/*  58:    */   {
/*  59: 93 */     if (this.deleted != null) {
/*  60: 94 */       return this.deleted.booleanValue();
/*  61:    */     }
/*  62: 96 */     if (this._master != null) {
/*  63: 97 */       return this._master.getDel();
/*  64:    */     }
/*  65: 99 */     return false;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public Double getX()
/*  69:    */   {
/*  70:103 */     return this.x == null ? this._master.x : this.x;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public Double getY()
/*  74:    */   {
/*  75:107 */     return this.y == null ? this._master.y : this.y;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public Double getA()
/*  79:    */   {
/*  80:111 */     return this.a == null ? this._master.a : this.a;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public Double getB()
/*  84:    */   {
/*  85:115 */     return this.b == null ? this._master.b : this.b;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public Double getC()
/*  89:    */   {
/*  90:119 */     return this.c == null ? this._master.c : this.c;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public Double getD()
/*  94:    */   {
/*  95:123 */     return this.d == null ? this._master.d : this.d;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public String getE()
/*  99:    */   {
/* 100:127 */     return this.e == null ? this._master.e : this.e;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void setupMaster(GeometryRow row)
/* 104:    */   {
/* 105:132 */     this._master = ((NURBSTo)row);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void addToPath(Path2D.Double path, XDGFShape parent)
/* 109:    */   {
/* 110:137 */     if (getDel()) {
/* 111:138 */       return;
/* 112:    */     }
/* 113:140 */     Point2D last = path.getCurrentPoint();
/* 114:    */     
/* 115:    */ 
/* 116:    */ 
/* 117:144 */     String formula = getE().trim();
/* 118:145 */     if ((!formula.startsWith("NURBS(")) || (!formula.endsWith(")"))) {
/* 119:146 */       throw new POIXMLException("Invalid NURBS formula: " + formula);
/* 120:    */     }
/* 121:148 */     String[] components = formula.substring(6, formula.length() - 1).split(",");
/* 122:151 */     if (components.length < 8) {
/* 123:152 */       throw new POIXMLException("Invalid NURBS formula (not enough arguments)");
/* 124:    */     }
/* 125:155 */     if ((components.length - 4) % 4 != 0) {
/* 126:156 */       throw new POIXMLException("Invalid NURBS formula -- need 4 + n*4 arguments, got " + components.length);
/* 127:    */     }
/* 128:160 */     double lastControlX = getX().doubleValue();
/* 129:161 */     double lastControlY = getY().doubleValue();
/* 130:162 */     double secondToLastKnot = getA().doubleValue();
/* 131:163 */     double lastWeight = getB().doubleValue();
/* 132:164 */     double firstKnot = getC().doubleValue();
/* 133:165 */     double firstWeight = getD().doubleValue();
/* 134:    */     
/* 135:167 */     double lastKnot = Double.parseDouble(components[0].trim());
/* 136:168 */     int degree = Integer.parseInt(components[1].trim());
/* 137:169 */     int xType = Integer.parseInt(components[2].trim());
/* 138:170 */     int yType = Integer.parseInt(components[3].trim());
/* 139:    */     
/* 140:172 */     double xScale = 1.0D;
/* 141:173 */     double yScale = 1.0D;
/* 142:175 */     if (xType == 0) {
/* 143:176 */       xScale = parent.getWidth().doubleValue();
/* 144:    */     }
/* 145:177 */     if (yType == 0) {
/* 146:178 */       yScale = parent.getHeight().doubleValue();
/* 147:    */     }
/* 148:181 */     ControlPath controlPath = new ControlPath();
/* 149:182 */     ValueVector knots = new ValueVector();
/* 150:183 */     ValueVector weights = new ValueVector();
/* 151:    */     
/* 152:185 */     knots.add(firstKnot);
/* 153:186 */     weights.add(firstWeight);
/* 154:187 */     controlPath.addPoint(PointFactory.create(last.getX(), last.getY()));
/* 155:    */     
/* 156:    */ 
/* 157:190 */     int sets = (components.length - 4) / 4;
/* 158:191 */     for (int i = 0; i < sets; i++)
/* 159:    */     {
/* 160:192 */       double x1 = Double.parseDouble(components[(4 + i * 4 + 0)].trim());
/* 161:193 */       double y1 = Double.parseDouble(components[(4 + i * 4 + 1)].trim());
/* 162:194 */       double k = Double.parseDouble(components[(4 + i * 4 + 2)].trim());
/* 163:195 */       double w = Double.parseDouble(components[(4 + i * 4 + 3)].trim());
/* 164:    */       
/* 165:197 */       controlPath.addPoint(PointFactory.create(x1 * xScale, y1 * yScale));
/* 166:198 */       knots.add(k);
/* 167:199 */       weights.add(w);
/* 168:    */     }
/* 169:203 */     knots.add(secondToLastKnot);
/* 170:204 */     knots.add(lastKnot);
/* 171:    */     
/* 172:206 */     weights.add(lastWeight);
/* 173:    */     
/* 174:208 */     controlPath.addPoint(PointFactory.create(lastControlX, lastControlY));
/* 175:    */     
/* 176:210 */     ShapeMultiPath shape = SplineRenderer.createNurbsSpline(controlPath, knots, weights, degree);
/* 177:    */     
/* 178:212 */     path.append(shape, true);
/* 179:    */   }
/* 180:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.NURBSTo
 * JD-Core Version:    0.7.0.1
 */