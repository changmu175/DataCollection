/*   1:    */ package org.apache.poi.xdgf.usermodel.section.geometry;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*   4:    */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*   5:    */ import java.awt.geom.Path2D.Double;
/*   6:    */ import org.apache.poi.POIXMLException;
/*   7:    */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/*   8:    */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*   9:    */ 
/*  10:    */ public class SplineStart
/*  11:    */   implements GeometryRow
/*  12:    */ {
/*  13: 32 */   SplineStart _master = null;
/*  14: 35 */   Double x = null;
/*  15: 38 */   Double y = null;
/*  16: 41 */   Double a = null;
/*  17: 44 */   Double b = null;
/*  18: 47 */   Double c = null;
/*  19: 50 */   Integer d = null;
/*  20: 52 */   Boolean deleted = null;
/*  21:    */   
/*  22:    */   public SplineStart(RowType row)
/*  23:    */   {
/*  24: 58 */     if (row.isSetDel()) {
/*  25: 59 */       this.deleted = Boolean.valueOf(row.getDel());
/*  26:    */     }
/*  27: 61 */     for (CellType cell : row.getCellArray())
/*  28:    */     {
/*  29: 62 */       String cellName = cell.getN();
/*  30: 64 */       if (cellName.equals("X")) {
/*  31: 65 */         this.x = XDGFCell.parseDoubleValue(cell);
/*  32: 66 */       } else if (cellName.equals("Y")) {
/*  33: 67 */         this.y = XDGFCell.parseDoubleValue(cell);
/*  34: 68 */       } else if (cellName.equals("A")) {
/*  35: 69 */         this.a = XDGFCell.parseDoubleValue(cell);
/*  36: 70 */       } else if (cellName.equals("B")) {
/*  37: 71 */         this.b = XDGFCell.parseDoubleValue(cell);
/*  38: 72 */       } else if (cellName.equals("C")) {
/*  39: 73 */         this.c = XDGFCell.parseDoubleValue(cell);
/*  40: 74 */       } else if (cellName.equals("D")) {
/*  41: 75 */         this.d = XDGFCell.parseIntegerValue(cell);
/*  42:    */       } else {
/*  43: 77 */         throw new POIXMLException("Invalid cell '" + cellName + "' in SplineStart row");
/*  44:    */       }
/*  45:    */     }
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean getDel()
/*  49:    */   {
/*  50: 84 */     if (this.deleted != null) {
/*  51: 85 */       return this.deleted.booleanValue();
/*  52:    */     }
/*  53: 87 */     if (this._master != null) {
/*  54: 88 */       return this._master.getDel();
/*  55:    */     }
/*  56: 90 */     return false;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public Double getX()
/*  60:    */   {
/*  61: 94 */     return this.x == null ? this._master.x : this.x;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public Double getY()
/*  65:    */   {
/*  66: 98 */     return this.y == null ? this._master.y : this.y;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public Double getA()
/*  70:    */   {
/*  71:102 */     return this.a == null ? this._master.a : this.a;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public Double getB()
/*  75:    */   {
/*  76:106 */     return this.b == null ? this._master.b : this.b;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public Double getC()
/*  80:    */   {
/*  81:110 */     return this.c == null ? this._master.c : this.c;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public Integer getD()
/*  85:    */   {
/*  86:114 */     return this.d == null ? this._master.d : this.d;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setupMaster(GeometryRow row)
/*  90:    */   {
/*  91:119 */     this._master = ((SplineStart)row);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void addToPath(Path2D.Double path, XDGFShape parent)
/*  95:    */   {
/*  96:124 */     throw new POIXMLException("Error: Use SplineRenderer!");
/*  97:    */   }
/*  98:    */   
/*  99:    */   public String toString()
/* 100:    */   {
/* 101:129 */     return "{SplineStart x=" + getX() + " y=" + getY() + " a=" + getA() + " b=" + getB() + " c=" + getC() + " d=" + getD() + "}";
/* 102:    */   }
/* 103:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.SplineStart
 * JD-Core Version:    0.7.0.1
 */