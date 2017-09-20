/*   1:    */ package org.apache.poi.xdgf.usermodel.section.geometry;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*   4:    */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*   5:    */ import java.awt.geom.Path2D.Double;
/*   6:    */ import org.apache.poi.POIXMLException;
/*   7:    */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/*   8:    */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*   9:    */ 
/*  10:    */ public class InfiniteLine
/*  11:    */   implements GeometryRow
/*  12:    */ {
/*  13: 34 */   InfiniteLine _master = null;
/*  14: 38 */   Double x = null;
/*  15: 42 */   Double y = null;
/*  16: 46 */   Double a = null;
/*  17: 50 */   Double b = null;
/*  18: 52 */   Boolean deleted = null;
/*  19:    */   
/*  20:    */   public InfiniteLine(RowType row)
/*  21:    */   {
/*  22: 58 */     if (row.isSetDel()) {
/*  23: 59 */       this.deleted = Boolean.valueOf(row.getDel());
/*  24:    */     }
/*  25: 61 */     for (CellType cell : row.getCellArray())
/*  26:    */     {
/*  27: 62 */       String cellName = cell.getN();
/*  28: 64 */       if (cellName.equals("X")) {
/*  29: 65 */         this.x = XDGFCell.parseDoubleValue(cell);
/*  30: 66 */       } else if (cellName.equals("Y")) {
/*  31: 67 */         this.y = XDGFCell.parseDoubleValue(cell);
/*  32: 68 */       } else if (cellName.equals("A")) {
/*  33: 69 */         this.a = XDGFCell.parseDoubleValue(cell);
/*  34: 70 */       } else if (cellName.equals("B")) {
/*  35: 71 */         this.b = XDGFCell.parseDoubleValue(cell);
/*  36:    */       } else {
/*  37: 73 */         throw new POIXMLException("Invalid cell '" + cellName + "' in InfiniteLine row");
/*  38:    */       }
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public boolean getDel()
/*  43:    */   {
/*  44: 80 */     if (this.deleted != null) {
/*  45: 81 */       return this.deleted.booleanValue();
/*  46:    */     }
/*  47: 83 */     if (this._master != null) {
/*  48: 84 */       return this._master.getDel();
/*  49:    */     }
/*  50: 86 */     return false;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public Double getX()
/*  54:    */   {
/*  55: 90 */     return this.x == null ? this._master.x : this.x;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public Double getY()
/*  59:    */   {
/*  60: 94 */     return this.y == null ? this._master.y : this.y;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public Double getA()
/*  64:    */   {
/*  65: 98 */     return this.a == null ? this._master.a : this.a;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public Double getB()
/*  69:    */   {
/*  70:102 */     return this.b == null ? this._master.b : this.b;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setupMaster(GeometryRow row)
/*  74:    */   {
/*  75:107 */     this._master = ((InfiniteLine)row);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void addToPath(Path2D.Double path, XDGFShape parent)
/*  79:    */   {
/*  80:113 */     if (getDel()) {
/*  81:114 */       return;
/*  82:    */     }
/*  83:116 */     throw new POIXMLException("InfiniteLine elements cannot be part of a path");
/*  84:    */   }
/*  85:    */   
/*  86:    */   public Path2D.Double getPath()
/*  87:    */   {
/*  88:123 */     Path2D.Double path = new Path2D.Double();
/*  89:    */     
/*  90:    */ 
/*  91:126 */     double max_val = 100000.0D;
/*  92:    */     
/*  93:    */ 
/*  94:129 */     double x0 = getX().doubleValue();
/*  95:130 */     double y0 = getY().doubleValue();
/*  96:131 */     double x1 = getA().doubleValue();
/*  97:132 */     double y1 = getB().doubleValue();
/*  98:134 */     if (x0 == x1)
/*  99:    */     {
/* 100:135 */       path.moveTo(x0, -max_val);
/* 101:136 */       path.lineTo(x0, max_val);
/* 102:    */     }
/* 103:137 */     else if (y0 == y1)
/* 104:    */     {
/* 105:138 */       path.moveTo(-max_val, y0);
/* 106:139 */       path.lineTo(max_val, y0);
/* 107:    */     }
/* 108:    */     else
/* 109:    */     {
/* 110:143 */       double m = (y1 - y0) / (x1 - x0);
/* 111:144 */       double c = y0 - m * x0;
/* 112:    */       
/* 113:    */ 
/* 114:    */ 
/* 115:148 */       path.moveTo(max_val, m * max_val + c);
/* 116:149 */       path.lineTo(max_val, (max_val - c) / m);
/* 117:    */     }
/* 118:152 */     return path;
/* 119:    */   }
/* 120:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.InfiniteLine
 * JD-Core Version:    0.7.0.1
 */