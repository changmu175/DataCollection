/*  1:   */ package org.apache.poi.xdgf.usermodel.section.geometry;
/*  2:   */ 
/*  3:   */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*  4:   */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*  5:   */ import java.awt.geom.Path2D.Double;
/*  6:   */ import org.apache.poi.POIXMLException;
/*  7:   */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/*  8:   */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*  9:   */ 
/* 10:   */ public class MoveTo
/* 11:   */   implements GeometryRow
/* 12:   */ {
/* 13:34 */   MoveTo _master = null;
/* 14:36 */   Double x = null;
/* 15:37 */   Double y = null;
/* 16:39 */   Boolean deleted = null;
/* 17:   */   
/* 18:   */   public MoveTo(RowType row)
/* 19:   */   {
/* 20:45 */     if (row.isSetDel()) {
/* 21:46 */       this.deleted = Boolean.valueOf(row.getDel());
/* 22:   */     }
/* 23:48 */     for (CellType cell : row.getCellArray())
/* 24:   */     {
/* 25:49 */       String cellName = cell.getN();
/* 26:51 */       if (cellName.equals("X")) {
/* 27:52 */         this.x = XDGFCell.parseDoubleValue(cell);
/* 28:53 */       } else if (cellName.equals("Y")) {
/* 29:54 */         this.y = XDGFCell.parseDoubleValue(cell);
/* 30:   */       } else {
/* 31:56 */         throw new POIXMLException("Invalid cell '" + cellName + "' in MoveTo row");
/* 32:   */       }
/* 33:   */     }
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String toString()
/* 37:   */   {
/* 38:64 */     return "MoveTo: x=" + getX() + "; y=" + getY();
/* 39:   */   }
/* 40:   */   
/* 41:   */   public boolean getDel()
/* 42:   */   {
/* 43:68 */     if (this.deleted != null) {
/* 44:69 */       return this.deleted.booleanValue();
/* 45:   */     }
/* 46:71 */     if (this._master != null) {
/* 47:72 */       return this._master.getDel();
/* 48:   */     }
/* 49:74 */     return false;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public Double getX()
/* 53:   */   {
/* 54:78 */     return this.x == null ? this._master.x : this.x;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public Double getY()
/* 58:   */   {
/* 59:82 */     return this.y == null ? this._master.y : this.y;
/* 60:   */   }
/* 61:   */   
/* 62:   */   public void setupMaster(GeometryRow row)
/* 63:   */   {
/* 64:87 */     this._master = ((MoveTo)row);
/* 65:   */   }
/* 66:   */   
/* 67:   */   public void addToPath(Path2D.Double path, XDGFShape parent)
/* 68:   */   {
/* 69:92 */     if (getDel()) {
/* 70:93 */       return;
/* 71:   */     }
/* 72:94 */     path.moveTo(getX().doubleValue(), getY().doubleValue());
/* 73:   */   }
/* 74:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.MoveTo
 * JD-Core Version:    0.7.0.1
 */