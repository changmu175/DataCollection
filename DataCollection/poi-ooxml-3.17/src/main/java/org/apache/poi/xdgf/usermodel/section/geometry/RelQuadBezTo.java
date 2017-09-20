/*   1:    */ package org.apache.poi.xdgf.usermodel.section.geometry;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*   4:    */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*   5:    */ import java.awt.geom.Path2D.Double;
/*   6:    */ import org.apache.poi.POIXMLException;
/*   7:    */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/*   8:    */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*   9:    */ 
/*  10:    */ public class RelQuadBezTo
/*  11:    */   implements GeometryRow
/*  12:    */ {
/*  13: 34 */   RelQuadBezTo _master = null;
/*  14: 38 */   Double x = null;
/*  15: 42 */   Double y = null;
/*  16: 47 */   Double a = null;
/*  17: 51 */   Double b = null;
/*  18: 53 */   Boolean deleted = null;
/*  19:    */   
/*  20:    */   public RelQuadBezTo(RowType row)
/*  21:    */   {
/*  22: 59 */     if (row.isSetDel()) {
/*  23: 60 */       this.deleted = Boolean.valueOf(row.getDel());
/*  24:    */     }
/*  25: 62 */     for (CellType cell : row.getCellArray())
/*  26:    */     {
/*  27: 63 */       String cellName = cell.getN();
/*  28: 65 */       if (cellName.equals("X")) {
/*  29: 66 */         this.x = XDGFCell.parseDoubleValue(cell);
/*  30: 67 */       } else if (cellName.equals("Y")) {
/*  31: 68 */         this.y = XDGFCell.parseDoubleValue(cell);
/*  32: 69 */       } else if (cellName.equals("A")) {
/*  33: 70 */         this.a = XDGFCell.parseDoubleValue(cell);
/*  34: 71 */       } else if (cellName.equals("B")) {
/*  35: 72 */         this.b = XDGFCell.parseDoubleValue(cell);
/*  36:    */       } else {
/*  37: 74 */         throw new POIXMLException("Invalid cell '" + cellName + "' in RelQuadBezTo row");
/*  38:    */       }
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public boolean getDel()
/*  43:    */   {
/*  44: 81 */     if (this.deleted != null) {
/*  45: 82 */       return this.deleted.booleanValue();
/*  46:    */     }
/*  47: 84 */     if (this._master != null) {
/*  48: 85 */       return this._master.getDel();
/*  49:    */     }
/*  50: 87 */     return false;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public Double getX()
/*  54:    */   {
/*  55: 91 */     return this.x == null ? this._master.x : this.x;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public Double getY()
/*  59:    */   {
/*  60: 95 */     return this.y == null ? this._master.y : this.y;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public Double getA()
/*  64:    */   {
/*  65: 99 */     return this.a == null ? this._master.a : this.a;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public Double getB()
/*  69:    */   {
/*  70:103 */     return this.b == null ? this._master.b : this.b;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setupMaster(GeometryRow row)
/*  74:    */   {
/*  75:108 */     this._master = ((RelQuadBezTo)row);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void addToPath(Path2D.Double path, XDGFShape parent)
/*  79:    */   {
/*  80:114 */     if (getDel()) {
/*  81:115 */       return;
/*  82:    */     }
/*  83:117 */     double w = parent.getWidth().doubleValue();
/*  84:118 */     double h = parent.getHeight().doubleValue();
/*  85:    */     
/*  86:120 */     path.quadTo(getA().doubleValue() * w, getB().doubleValue() * h, getX().doubleValue() * w, getY().doubleValue() * h);
/*  87:    */   }
/*  88:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.RelQuadBezTo
 * JD-Core Version:    0.7.0.1
 */