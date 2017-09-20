/*   1:    */ package org.apache.poi.xdgf.usermodel.section.geometry;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*   4:    */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*   5:    */ import java.awt.geom.Path2D.Double;
/*   6:    */ import org.apache.poi.POIXMLException;
/*   7:    */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/*   8:    */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*   9:    */ 
/*  10:    */ public class RelCubBezTo
/*  11:    */   implements GeometryRow
/*  12:    */ {
/*  13: 29 */   RelCubBezTo _master = null;
/*  14: 33 */   Double x = null;
/*  15: 37 */   Double y = null;
/*  16: 42 */   Double a = null;
/*  17: 46 */   Double b = null;
/*  18: 51 */   Double c = null;
/*  19: 55 */   Double d = null;
/*  20: 57 */   Boolean deleted = null;
/*  21:    */   
/*  22:    */   public RelCubBezTo(RowType row)
/*  23:    */   {
/*  24: 63 */     if (row.isSetDel()) {
/*  25: 64 */       this.deleted = Boolean.valueOf(row.getDel());
/*  26:    */     }
/*  27: 66 */     for (CellType cell : row.getCellArray())
/*  28:    */     {
/*  29: 67 */       String cellName = cell.getN();
/*  30: 69 */       if (cellName.equals("X")) {
/*  31: 70 */         this.x = XDGFCell.parseDoubleValue(cell);
/*  32: 71 */       } else if (cellName.equals("Y")) {
/*  33: 72 */         this.y = XDGFCell.parseDoubleValue(cell);
/*  34: 73 */       } else if (cellName.equals("A")) {
/*  35: 74 */         this.a = XDGFCell.parseDoubleValue(cell);
/*  36: 75 */       } else if (cellName.equals("B")) {
/*  37: 76 */         this.b = XDGFCell.parseDoubleValue(cell);
/*  38: 77 */       } else if (cellName.equals("C")) {
/*  39: 78 */         this.c = XDGFCell.parseDoubleValue(cell);
/*  40: 79 */       } else if (cellName.equals("D")) {
/*  41: 80 */         this.d = XDGFCell.parseDoubleValue(cell);
/*  42:    */       } else {
/*  43: 82 */         throw new POIXMLException("Invalid cell '" + cellName + "' in RelCubBezTo row");
/*  44:    */       }
/*  45:    */     }
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean getDel()
/*  49:    */   {
/*  50: 89 */     if (this.deleted != null) {
/*  51: 90 */       return this.deleted.booleanValue();
/*  52:    */     }
/*  53: 92 */     if (this._master != null) {
/*  54: 93 */       return this._master.getDel();
/*  55:    */     }
/*  56: 95 */     return false;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public Double getX()
/*  60:    */   {
/*  61: 99 */     return this.x == null ? this._master.x : this.x;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public Double getY()
/*  65:    */   {
/*  66:103 */     return this.y == null ? this._master.y : this.y;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public Double getA()
/*  70:    */   {
/*  71:107 */     return this.a == null ? this._master.a : this.a;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public Double getB()
/*  75:    */   {
/*  76:111 */     return this.b == null ? this._master.b : this.b;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public Double getC()
/*  80:    */   {
/*  81:115 */     return this.c == null ? this._master.c : this.c;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public Double getD()
/*  85:    */   {
/*  86:119 */     return this.d == null ? this._master.d : this.d;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setupMaster(GeometryRow row)
/*  90:    */   {
/*  91:124 */     this._master = ((RelCubBezTo)row);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void addToPath(Path2D.Double path, XDGFShape parent)
/*  95:    */   {
/*  96:130 */     if (getDel()) {
/*  97:131 */       return;
/*  98:    */     }
/*  99:133 */     double w = parent.getWidth().doubleValue();
/* 100:134 */     double h = parent.getHeight().doubleValue();
/* 101:    */     
/* 102:136 */     path.curveTo(getA().doubleValue() * w, getB().doubleValue() * h, getC().doubleValue() * w, getD().doubleValue() * h, getX().doubleValue() * w, getY().doubleValue() * h);
/* 103:    */   }
/* 104:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.RelCubBezTo
 * JD-Core Version:    0.7.0.1
 */