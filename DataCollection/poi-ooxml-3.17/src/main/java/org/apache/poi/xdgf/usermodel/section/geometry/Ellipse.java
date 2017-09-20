/*   1:    */ package org.apache.poi.xdgf.usermodel.section.geometry;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*   4:    */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*   5:    */ import java.awt.geom.AffineTransform;
/*   6:    */ import java.awt.geom.Ellipse2D.Double;
/*   7:    */ import java.awt.geom.Path2D.Double;
/*   8:    */ import org.apache.poi.POIXMLException;
/*   9:    */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/*  10:    */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*  11:    */ 
/*  12:    */ public class Ellipse
/*  13:    */   implements GeometryRow
/*  14:    */ {
/*  15: 33 */   Ellipse _master = null;
/*  16: 36 */   Double x = null;
/*  17: 38 */   Double y = null;
/*  18: 41 */   Double a = null;
/*  19: 43 */   Double b = null;
/*  20: 46 */   Double c = null;
/*  21: 48 */   Double d = null;
/*  22: 50 */   Boolean deleted = null;
/*  23:    */   
/*  24:    */   public Ellipse(RowType row)
/*  25:    */   {
/*  26: 56 */     if (row.isSetDel()) {
/*  27: 57 */       this.deleted = Boolean.valueOf(row.getDel());
/*  28:    */     }
/*  29: 59 */     for (CellType cell : row.getCellArray())
/*  30:    */     {
/*  31: 60 */       String cellName = cell.getN();
/*  32: 62 */       if (cellName.equals("X")) {
/*  33: 63 */         this.x = XDGFCell.parseDoubleValue(cell);
/*  34: 64 */       } else if (cellName.equals("Y")) {
/*  35: 65 */         this.y = XDGFCell.parseDoubleValue(cell);
/*  36: 66 */       } else if (cellName.equals("A")) {
/*  37: 67 */         this.a = XDGFCell.parseDoubleValue(cell);
/*  38: 68 */       } else if (cellName.equals("B")) {
/*  39: 69 */         this.b = XDGFCell.parseDoubleValue(cell);
/*  40: 70 */       } else if (cellName.equals("C")) {
/*  41: 71 */         this.c = XDGFCell.parseDoubleValue(cell);
/*  42: 72 */       } else if (cellName.equals("D")) {
/*  43: 73 */         this.d = XDGFCell.parseDoubleValue(cell);
/*  44:    */       } else {
/*  45: 75 */         throw new POIXMLException("Invalid cell '" + cellName + "' in Ellipse row");
/*  46:    */       }
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public boolean getDel()
/*  51:    */   {
/*  52: 82 */     if (this.deleted != null) {
/*  53: 83 */       return this.deleted.booleanValue();
/*  54:    */     }
/*  55: 85 */     if (this._master != null) {
/*  56: 86 */       return this._master.getDel();
/*  57:    */     }
/*  58: 88 */     return false;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public Double getX()
/*  62:    */   {
/*  63: 92 */     return this.x == null ? this._master.x : this.x;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public Double getY()
/*  67:    */   {
/*  68: 96 */     return this.y == null ? this._master.y : this.y;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public Double getA()
/*  72:    */   {
/*  73:100 */     return this.a == null ? this._master.a : this.a;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public Double getB()
/*  77:    */   {
/*  78:104 */     return this.b == null ? this._master.b : this.b;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public Double getC()
/*  82:    */   {
/*  83:108 */     return this.c == null ? this._master.c : this.c;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public Double getD()
/*  87:    */   {
/*  88:112 */     return this.d == null ? this._master.d : this.d;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setupMaster(GeometryRow row)
/*  92:    */   {
/*  93:117 */     this._master = ((Ellipse)row);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public Path2D.Double getPath()
/*  97:    */   {
/*  98:122 */     if (getDel()) {
/*  99:123 */       return null;
/* 100:    */     }
/* 101:126 */     double cx = getX().doubleValue();
/* 102:127 */     double cy = getY().doubleValue();
/* 103:128 */     double a = getA().doubleValue();
/* 104:129 */     double b = getB().doubleValue();
/* 105:130 */     double c = getC().doubleValue();
/* 106:131 */     double d = getD().doubleValue();
/* 107:    */     
/* 108:    */ 
/* 109:134 */     double rx = Math.hypot(a - cx, b - cy);
/* 110:135 */     double ry = Math.hypot(c - cx, d - cy);
/* 111:    */     
/* 112:    */ 
/* 113:138 */     double angle = (6.283185307179586D + (cy > b ? 1.0D : -1.0D) * Math.acos((cx - a) / rx)) % 6.283185307179586D;
/* 114:    */     
/* 115:    */ 
/* 116:    */ 
/* 117:    */ 
/* 118:143 */     Ellipse2D.Double ellipse = new Ellipse2D.Double(cx - rx, cy - ry, rx * 2.0D, ry * 2.0D);
/* 119:    */     
/* 120:    */ 
/* 121:    */ 
/* 122:147 */     Path2D.Double path = new Path2D.Double(ellipse);
/* 123:    */     
/* 124:149 */     AffineTransform tr = new AffineTransform();
/* 125:150 */     tr.rotate(angle, cx, cy);
/* 126:151 */     path.transform(tr);
/* 127:    */     
/* 128:153 */     return path;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void addToPath(Path2D.Double path, XDGFShape parent)
/* 132:    */   {
/* 133:158 */     throw new POIXMLException("Ellipse elements cannot be part of a path");
/* 134:    */   }
/* 135:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.Ellipse
 * JD-Core Version:    0.7.0.1
 */