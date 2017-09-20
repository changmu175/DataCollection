/*  1:   */ package org.apache.poi.xdgf.usermodel.section.geometry;
/*  2:   */ 
/*  3:   */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*  4:   */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*  5:   */ import java.awt.geom.Path2D.Double;
/*  6:   */ import org.apache.poi.POIXMLException;
/*  7:   */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/*  8:   */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*  9:   */ 
/* 10:   */ public class LineTo
/* 11:   */   implements GeometryRow
/* 12:   */ {
/* 13:29 */   LineTo _master = null;
/* 14:31 */   Double x = null;
/* 15:32 */   Double y = null;
/* 16:34 */   Boolean deleted = null;
/* 17:   */   
/* 18:   */   public LineTo(RowType row)
/* 19:   */   {
/* 20:40 */     if (row.isSetDel()) {
/* 21:41 */       this.deleted = Boolean.valueOf(row.getDel());
/* 22:   */     }
/* 23:43 */     for (CellType cell : row.getCellArray())
/* 24:   */     {
/* 25:44 */       String cellName = cell.getN();
/* 26:46 */       if (cellName.equals("X")) {
/* 27:47 */         this.x = XDGFCell.parseDoubleValue(cell);
/* 28:48 */       } else if (cellName.equals("Y")) {
/* 29:49 */         this.y = XDGFCell.parseDoubleValue(cell);
/* 30:   */       } else {
/* 31:51 */         throw new POIXMLException("Invalid cell '" + cellName + "' in LineTo row");
/* 32:   */       }
/* 33:   */     }
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String toString()
/* 37:   */   {
/* 38:59 */     return "LineTo: x=" + getX() + "; y=" + getY();
/* 39:   */   }
/* 40:   */   
/* 41:   */   public boolean getDel()
/* 42:   */   {
/* 43:63 */     if (this.deleted != null) {
/* 44:64 */       return this.deleted.booleanValue();
/* 45:   */     }
/* 46:66 */     if (this._master != null) {
/* 47:67 */       return this._master.getDel();
/* 48:   */     }
/* 49:69 */     return false;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public Double getX()
/* 53:   */   {
/* 54:73 */     return this.x == null ? this._master.x : this.x;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public Double getY()
/* 58:   */   {
/* 59:77 */     return this.y == null ? this._master.y : this.y;
/* 60:   */   }
/* 61:   */   
/* 62:   */   public void setupMaster(GeometryRow row)
/* 63:   */   {
/* 64:82 */     this._master = ((LineTo)row);
/* 65:   */   }
/* 66:   */   
/* 67:   */   public void addToPath(Path2D.Double path, XDGFShape parent)
/* 68:   */   {
/* 69:87 */     if (getDel()) {
/* 70:88 */       return;
/* 71:   */     }
/* 72:89 */     path.lineTo(getX().doubleValue(), getY().doubleValue());
/* 73:   */   }
/* 74:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.LineTo
 * JD-Core Version:    0.7.0.1
 */