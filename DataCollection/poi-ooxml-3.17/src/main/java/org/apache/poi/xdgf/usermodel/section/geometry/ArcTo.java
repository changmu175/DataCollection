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
/*  14:    */ public class ArcTo
/*  15:    */   implements GeometryRow
/*  16:    */ {
/*  17: 34 */   ArcTo _master = null;
/*  18: 37 */   Double x = null;
/*  19: 40 */   Double y = null;
/*  20: 43 */   Double a = null;
/*  21: 45 */   Boolean deleted = null;
/*  22:    */   
/*  23:    */   public ArcTo(RowType row)
/*  24:    */   {
/*  25: 51 */     if (row.isSetDel()) {
/*  26: 52 */       this.deleted = Boolean.valueOf(row.getDel());
/*  27:    */     }
/*  28: 54 */     for (CellType cell : row.getCellArray())
/*  29:    */     {
/*  30: 55 */       String cellName = cell.getN();
/*  31: 57 */       if (cellName.equals("X")) {
/*  32: 58 */         this.x = XDGFCell.parseDoubleValue(cell);
/*  33: 59 */       } else if (cellName.equals("Y")) {
/*  34: 60 */         this.y = XDGFCell.parseDoubleValue(cell);
/*  35: 61 */       } else if (cellName.equals("A")) {
/*  36: 62 */         this.a = XDGFCell.parseDoubleValue(cell);
/*  37:    */       } else {
/*  38: 64 */         throw new POIXMLException("Invalid cell '" + cellName + "' in ArcTo row");
/*  39:    */       }
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   public boolean getDel()
/*  44:    */   {
/*  45: 71 */     if (this.deleted != null) {
/*  46: 72 */       return this.deleted.booleanValue();
/*  47:    */     }
/*  48: 74 */     if (this._master != null) {
/*  49: 75 */       return this._master.getDel();
/*  50:    */     }
/*  51: 77 */     return false;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public Double getX()
/*  55:    */   {
/*  56: 81 */     return this.x == null ? this._master.x : this.x;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public Double getY()
/*  60:    */   {
/*  61: 85 */     return this.y == null ? this._master.y : this.y;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public Double getA()
/*  65:    */   {
/*  66: 89 */     return this.a == null ? this._master.a : this.a;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setupMaster(GeometryRow row)
/*  70:    */   {
/*  71: 94 */     this._master = ((ArcTo)row);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void addToPath(Path2D.Double path, XDGFShape parent)
/*  75:    */   {
/*  76:100 */     if (getDel()) {
/*  77:101 */       return;
/*  78:    */     }
/*  79:103 */     Point2D last = path.getCurrentPoint();
/*  80:    */     
/*  81:    */ 
/*  82:106 */     double x = getX().doubleValue();
/*  83:107 */     double y = getY().doubleValue();
/*  84:108 */     double a = getA().doubleValue();
/*  85:110 */     if (a == 0.0D)
/*  86:    */     {
/*  87:111 */       path.lineTo(x, y);
/*  88:112 */       return;
/*  89:    */     }
/*  90:115 */     double x0 = last.getX();
/*  91:116 */     double y0 = last.getY();
/*  92:    */     
/*  93:118 */     double chordLength = Math.hypot(y - y0, x - x0);
/*  94:119 */     double radius = (4.0D * a * a + chordLength * chordLength) / (8.0D * Math.abs(a));
/*  95:    */     
/*  96:    */ 
/*  97:    */ 
/*  98:123 */     double cx = x0 + (x - x0) / 2.0D;
/*  99:124 */     double cy = y0 + (y - y0) / 2.0D;
/* 100:    */     
/* 101:126 */     double rotate = Math.atan2(y - cy, x - cx);
/* 102:    */     
/* 103:128 */     Arc2D arc = new Arc2D.Double(x0, y0 - radius, chordLength, 2.0D * radius, 180.0D, x0 < x ? 180.0D : -180.0D, 0);
/* 104:    */     
/* 105:    */ 
/* 106:131 */     path.append(AffineTransform.getRotateInstance(rotate, x0, y0).createTransformedShape(arc), true);
/* 107:    */   }
/* 108:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.ArcTo
 * JD-Core Version:    0.7.0.1
 */