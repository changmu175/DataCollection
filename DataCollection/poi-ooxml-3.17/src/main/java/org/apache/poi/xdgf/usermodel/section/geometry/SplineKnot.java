/*   1:    */ package org.apache.poi.xdgf.usermodel.section.geometry;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*   4:    */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*   5:    */ import java.awt.geom.Path2D.Double;
/*   6:    */ import org.apache.poi.POIXMLException;
/*   7:    */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/*   8:    */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*   9:    */ 
/*  10:    */ public class SplineKnot
/*  11:    */   implements GeometryRow
/*  12:    */ {
/*  13: 33 */   SplineKnot _master = null;
/*  14: 36 */   Double x = null;
/*  15: 39 */   Double y = null;
/*  16: 42 */   Double a = null;
/*  17: 44 */   Boolean deleted = null;
/*  18:    */   
/*  19:    */   public SplineKnot(RowType row)
/*  20:    */   {
/*  21: 50 */     if (row.isSetDel()) {
/*  22: 51 */       this.deleted = Boolean.valueOf(row.getDel());
/*  23:    */     }
/*  24: 53 */     for (CellType cell : row.getCellArray())
/*  25:    */     {
/*  26: 54 */       String cellName = cell.getN();
/*  27: 56 */       if (cellName.equals("X")) {
/*  28: 57 */         this.x = XDGFCell.parseDoubleValue(cell);
/*  29: 58 */       } else if (cellName.equals("Y")) {
/*  30: 59 */         this.y = XDGFCell.parseDoubleValue(cell);
/*  31: 60 */       } else if (cellName.equals("A")) {
/*  32: 61 */         this.a = XDGFCell.parseDoubleValue(cell);
/*  33:    */       } else {
/*  34: 63 */         throw new POIXMLException("Invalid cell '" + cellName + "' in SplineKnot row");
/*  35:    */       }
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   public boolean getDel()
/*  40:    */   {
/*  41: 70 */     if (this.deleted != null) {
/*  42: 71 */       return this.deleted.booleanValue();
/*  43:    */     }
/*  44: 73 */     if (this._master != null) {
/*  45: 74 */       return this._master.getDel();
/*  46:    */     }
/*  47: 76 */     return false;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public Double getX()
/*  51:    */   {
/*  52: 80 */     return this.x == null ? this._master.x : this.x;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public Double getY()
/*  56:    */   {
/*  57: 84 */     return this.y == null ? this._master.y : this.y;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Double getA()
/*  61:    */   {
/*  62: 88 */     return this.a == null ? this._master.a : this.a;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setupMaster(GeometryRow row)
/*  66:    */   {
/*  67: 93 */     this._master = ((SplineKnot)row);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void addToPath(Path2D.Double path, XDGFShape parent)
/*  71:    */   {
/*  72: 98 */     throw new POIXMLException("Error: Use SplineRenderer!");
/*  73:    */   }
/*  74:    */   
/*  75:    */   public String toString()
/*  76:    */   {
/*  77:103 */     return "{SplineKnot x=" + getX() + " y=" + getY() + " a=" + getA() + "}";
/*  78:    */   }
/*  79:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.SplineKnot
 * JD-Core Version:    0.7.0.1
 */