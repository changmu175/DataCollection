/*   1:    */ package org.apache.poi.xdgf.usermodel.section.geometry;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*   4:    */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*   5:    */ import java.awt.geom.AffineTransform;
/*   6:    */ import java.awt.geom.Arc2D;
/*   7:    */ import java.awt.geom.Arc2D.Double;
/*   8:    */ import java.awt.geom.Path2D.Double;
/*   9:    */ import java.awt.geom.Point2D;
/*  10:    */ import org.apache.poi.POIXMLException;
/*  11:    */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/*  12:    */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*  13:    */ 
/*  14:    */ public class EllipticalArcTo
/*  15:    */   implements GeometryRow
/*  16:    */ {
/*  17: 33 */   EllipticalArcTo _master = null;
/*  18: 36 */   Double x = null;
/*  19: 39 */   Double y = null;
/*  20: 46 */   Double a = null;
/*  21: 49 */   Double b = null;
/*  22: 53 */   Double c = null;
/*  23: 60 */   Double d = null;
/*  24: 62 */   Boolean deleted = null;
/*  25:    */   
/*  26:    */   public EllipticalArcTo(RowType row)
/*  27:    */   {
/*  28: 68 */     if (row.isSetDel()) {
/*  29: 69 */       this.deleted = Boolean.valueOf(row.getDel());
/*  30:    */     }
/*  31: 71 */     for (CellType cell : row.getCellArray())
/*  32:    */     {
/*  33: 72 */       String cellName = cell.getN();
/*  34: 74 */       if (cellName.equals("X")) {
/*  35: 75 */         this.x = XDGFCell.parseDoubleValue(cell);
/*  36: 76 */       } else if (cellName.equals("Y")) {
/*  37: 77 */         this.y = XDGFCell.parseDoubleValue(cell);
/*  38: 78 */       } else if (cellName.equals("A")) {
/*  39: 79 */         this.a = XDGFCell.parseDoubleValue(cell);
/*  40: 80 */       } else if (cellName.equals("B")) {
/*  41: 81 */         this.b = XDGFCell.parseDoubleValue(cell);
/*  42: 82 */       } else if (cellName.equals("C")) {
/*  43: 83 */         this.c = XDGFCell.parseDoubleValue(cell);
/*  44: 84 */       } else if (cellName.equals("D")) {
/*  45: 85 */         this.d = XDGFCell.parseDoubleValue(cell);
/*  46:    */       } else {
/*  47: 87 */         throw new POIXMLException("Invalid cell '" + cellName + "' in EllipticalArcTo row");
/*  48:    */       }
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public boolean getDel()
/*  53:    */   {
/*  54: 94 */     if (this.deleted != null) {
/*  55: 95 */       return this.deleted.booleanValue();
/*  56:    */     }
/*  57: 97 */     if (this._master != null) {
/*  58: 98 */       return this._master.getDel();
/*  59:    */     }
/*  60:100 */     return false;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public Double getX()
/*  64:    */   {
/*  65:104 */     return this.x == null ? this._master.x : this.x;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public Double getY()
/*  69:    */   {
/*  70:108 */     return this.y == null ? this._master.y : this.y;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public Double getA()
/*  74:    */   {
/*  75:112 */     return this.a == null ? this._master.a : this.a;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public Double getB()
/*  79:    */   {
/*  80:116 */     return this.b == null ? this._master.b : this.b;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public Double getC()
/*  84:    */   {
/*  85:120 */     return this.c == null ? this._master.c : this.c;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public Double getD()
/*  89:    */   {
/*  90:124 */     return this.d == null ? this._master.d : this.d;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void setupMaster(GeometryRow row)
/*  94:    */   {
/*  95:129 */     this._master = ((EllipticalArcTo)row);
/*  96:    */   }
/*  97:    */   
/*  98:132 */   public static int draw = 0;
/*  99:    */   
/* 100:    */   public void addToPath(Path2D.Double path, XDGFShape parent)
/* 101:    */   {
/* 102:137 */     if (getDel()) {
/* 103:138 */       return;
/* 104:    */     }
/* 105:141 */     double x = getX().doubleValue();
/* 106:142 */     double y = getY().doubleValue();
/* 107:143 */     double a = getA().doubleValue();
/* 108:144 */     double b = getB().doubleValue();
/* 109:145 */     double c = getC().doubleValue();
/* 110:146 */     double d = getD().doubleValue();
/* 111:    */     
/* 112:148 */     createEllipticalArc(x, y, a, b, c, d, path);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public static void createEllipticalArc(double x, double y, double a, double b, double c, double d, Path2D.Double path)
/* 116:    */   {
/* 117:166 */     Point2D last = path.getCurrentPoint();
/* 118:167 */     double x0 = last.getX();
/* 119:168 */     double y0 = last.getY();
/* 120:    */     
/* 121:    */ 
/* 122:171 */     AffineTransform at = AffineTransform.getRotateInstance(-c);
/* 123:172 */     double[] pts = { x0, y0, x, y, a, b };
/* 124:173 */     at.transform(pts, 0, pts, 0, 3);
/* 125:    */     
/* 126:175 */     x0 = pts[0];
/* 127:176 */     y0 = pts[1];
/* 128:177 */     x = pts[2];
/* 129:178 */     y = pts[3];
/* 130:179 */     a = pts[4];
/* 131:180 */     b = pts[5];
/* 132:    */     
/* 133:    */ 
/* 134:    */ 
/* 135:184 */     double d2 = d * d;
/* 136:185 */     double cx = ((x0 - x) * (x0 + x) * (y - b) - (x - a) * (x + a) * (y0 - y) + d2 * (y0 - y) * (y - b) * (y0 - b)) / (2.0D * ((x0 - x) * (y - b) - (x - a) * (y0 - y)));
/* 137:    */     
/* 138:    */ 
/* 139:188 */     double cy = ((x0 - x) * (x - a) * (x0 - a) / d2 + (x - a) * (y0 - y) * (y0 + y) - (x0 - x) * (y - b) * (y + b)) / (2.0D * ((x - a) * (y0 - y) - (x0 - x) * (y - b)));
/* 140:    */     
/* 141:    */ 
/* 142:    */ 
/* 143:    */ 
/* 144:193 */     double rx = Math.sqrt(Math.pow(x0 - cx, 2.0D) + Math.pow(y0 - cy, 2.0D) * d2);
/* 145:194 */     double ry = rx / d;
/* 146:    */     
/* 147:    */ 
/* 148:    */ 
/* 149:    */ 
/* 150:    */ 
/* 151:    */ 
/* 152:    */ 
/* 153:202 */     double ctrlAngle = Math.toDegrees(Math.atan2((b - cy) / ry, (a - cx) / rx));
/* 154:    */     
/* 155:204 */     double startAngle = Math.toDegrees(Math.atan2((y0 - cy) / ry, (x0 - cx) / rx));
/* 156:    */     
/* 157:206 */     double endAngle = Math.toDegrees(Math.atan2((y - cy) / ry, (x - cx) / rx));
/* 158:    */     
/* 159:    */ 
/* 160:209 */     double sweep = computeSweep(startAngle, endAngle, ctrlAngle);
/* 161:    */     
/* 162:    */ 
/* 163:212 */     Arc2D arc = new Arc2D.Double(cx - rx, cy - ry, rx * 2.0D, ry * 2.0D, -startAngle, sweep, 0);
/* 164:    */     
/* 165:    */ 
/* 166:    */ 
/* 167:216 */     at.setToRotation(c);
/* 168:217 */     path.append(at.createTransformedShape(arc), false);
/* 169:    */   }
/* 170:    */   
/* 171:    */   protected static double computeSweep(double startAngle, double endAngle, double ctrlAngle)
/* 172:    */   {
/* 173:224 */     startAngle = (360.0D + startAngle) % 360.0D;
/* 174:225 */     endAngle = (360.0D + endAngle) % 360.0D;
/* 175:226 */     ctrlAngle = (360.0D + ctrlAngle) % 360.0D;
/* 176:    */     double sweep;
/* 177:    */     double sweep;
/* 178:230 */     if (startAngle < endAngle)
/* 179:    */     {
/* 180:    */       double sweep;
/* 181:231 */       if ((startAngle < ctrlAngle) && (ctrlAngle < endAngle)) {
/* 182:232 */         sweep = startAngle - endAngle;
/* 183:    */       } else {
/* 184:234 */         sweep = 360.0D + (startAngle - endAngle);
/* 185:    */       }
/* 186:    */     }
/* 187:    */     else
/* 188:    */     {
/* 189:    */       double sweep;
/* 190:237 */       if ((endAngle < ctrlAngle) && (ctrlAngle < startAngle)) {
/* 191:238 */         sweep = startAngle - endAngle;
/* 192:    */       } else {
/* 193:240 */         sweep = -(360.0D - (startAngle - endAngle));
/* 194:    */       }
/* 195:    */     }
/* 196:244 */     return sweep;
/* 197:    */   }
/* 198:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.EllipticalArcTo
 * JD-Core Version:    0.7.0.1
 */