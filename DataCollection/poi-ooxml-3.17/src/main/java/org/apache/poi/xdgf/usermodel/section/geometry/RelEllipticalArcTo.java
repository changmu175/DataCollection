/*   1:    */ package org.apache.poi.xdgf.usermodel.section.geometry;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*   4:    */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*   5:    */ import java.awt.geom.Path2D.Double;
/*   6:    */ import org.apache.poi.POIXMLException;
/*   7:    */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/*   8:    */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*   9:    */ 
/*  10:    */ public class RelEllipticalArcTo
/*  11:    */   implements GeometryRow
/*  12:    */ {
/*  13: 29 */   RelEllipticalArcTo _master = null;
/*  14: 33 */   Double x = null;
/*  15: 37 */   Double y = null;
/*  16: 41 */   Double a = null;
/*  17: 44 */   Double b = null;
/*  18: 47 */   Double c = null;
/*  19: 52 */   Double d = null;
/*  20: 54 */   Boolean deleted = null;
/*  21:    */   
/*  22:    */   public RelEllipticalArcTo(RowType row)
/*  23:    */   {
/*  24: 60 */     if (row.isSetDel()) {
/*  25: 61 */       this.deleted = Boolean.valueOf(row.getDel());
/*  26:    */     }
/*  27: 63 */     for (CellType cell : row.getCellArray())
/*  28:    */     {
/*  29: 64 */       String cellName = cell.getN();
/*  30: 66 */       if (cellName.equals("X")) {
/*  31: 67 */         this.x = XDGFCell.parseDoubleValue(cell);
/*  32: 68 */       } else if (cellName.equals("Y")) {
/*  33: 69 */         this.y = XDGFCell.parseDoubleValue(cell);
/*  34: 70 */       } else if (cellName.equals("A")) {
/*  35: 71 */         this.a = XDGFCell.parseDoubleValue(cell);
/*  36: 72 */       } else if (cellName.equals("B")) {
/*  37: 73 */         this.b = XDGFCell.parseDoubleValue(cell);
/*  38: 74 */       } else if (cellName.equals("C")) {
/*  39: 75 */         this.c = XDGFCell.parseDoubleValue(cell);
/*  40: 76 */       } else if (cellName.equals("D")) {
/*  41: 77 */         this.d = XDGFCell.parseDoubleValue(cell);
/*  42:    */       } else {
/*  43: 79 */         throw new POIXMLException("Invalid cell '" + cellName + "' in RelEllipticalArcTo row");
/*  44:    */       }
/*  45:    */     }
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean getDel()
/*  49:    */   {
/*  50: 86 */     if (this.deleted != null) {
/*  51: 87 */       return this.deleted.booleanValue();
/*  52:    */     }
/*  53: 89 */     if (this._master != null) {
/*  54: 90 */       return this._master.getDel();
/*  55:    */     }
/*  56: 92 */     return false;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public Double getX()
/*  60:    */   {
/*  61: 96 */     return this.x == null ? this._master.x : this.x;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public Double getY()
/*  65:    */   {
/*  66:100 */     return this.y == null ? this._master.y : this.y;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public Double getA()
/*  70:    */   {
/*  71:104 */     return this.a == null ? this._master.a : this.a;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public Double getB()
/*  75:    */   {
/*  76:108 */     return this.b == null ? this._master.b : this.b;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public Double getC()
/*  80:    */   {
/*  81:112 */     return this.c == null ? this._master.c : this.c;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public Double getD()
/*  85:    */   {
/*  86:116 */     return this.d == null ? this._master.d : this.d;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setupMaster(GeometryRow row)
/*  90:    */   {
/*  91:121 */     this._master = ((RelEllipticalArcTo)row);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void addToPath(Path2D.Double path, XDGFShape parent)
/*  95:    */   {
/*  96:127 */     if (getDel()) {
/*  97:128 */       return;
/*  98:    */     }
/*  99:130 */     double w = parent.getWidth().doubleValue();
/* 100:131 */     double h = parent.getHeight().doubleValue();
/* 101:    */     
/* 102:    */ 
/* 103:134 */     double x = getX().doubleValue() * w;
/* 104:135 */     double y = getY().doubleValue() * h;
/* 105:136 */     double a = getA().doubleValue() * w;
/* 106:137 */     double b = getB().doubleValue() * h;
/* 107:138 */     double c = getC().doubleValue();
/* 108:139 */     double d = getD().doubleValue();
/* 109:    */     
/* 110:141 */     EllipticalArcTo.createEllipticalArc(x, y, a, b, c, d, path);
/* 111:    */   }
/* 112:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.RelEllipticalArcTo
 * JD-Core Version:    0.7.0.1
 */