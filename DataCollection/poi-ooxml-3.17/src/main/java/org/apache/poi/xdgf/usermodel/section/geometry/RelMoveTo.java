/*  1:   */ package org.apache.poi.xdgf.usermodel.section.geometry;
/*  2:   */ 
/*  3:   */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*  4:   */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*  5:   */ import java.awt.geom.Path2D.Double;
/*  6:   */ import org.apache.poi.POIXMLException;
/*  7:   */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/*  8:   */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*  9:   */ 
/* 10:   */ public class RelMoveTo
/* 11:   */   implements GeometryRow
/* 12:   */ {
/* 13:29 */   RelMoveTo _master = null;
/* 14:31 */   Double x = null;
/* 15:32 */   Double y = null;
/* 16:34 */   Boolean deleted = null;
/* 17:   */   
/* 18:   */   public RelMoveTo(RowType row)
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
/* 31:51 */         throw new POIXMLException("Invalid cell '" + cellName + "' in RelMoveTo row");
/* 32:   */       }
/* 33:   */     }
/* 34:   */   }
/* 35:   */   
/* 36:   */   public boolean getDel()
/* 37:   */   {
/* 38:58 */     if (this.deleted != null) {
/* 39:59 */       return this.deleted.booleanValue();
/* 40:   */     }
/* 41:61 */     if (this._master != null) {
/* 42:62 */       return this._master.getDel();
/* 43:   */     }
/* 44:64 */     return false;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public Double getX()
/* 48:   */   {
/* 49:68 */     return this.x == null ? this._master.x : this.x;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public Double getY()
/* 53:   */   {
/* 54:72 */     return this.y == null ? this._master.y : this.y;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public void setupMaster(GeometryRow row)
/* 58:   */   {
/* 59:77 */     this._master = ((RelMoveTo)row);
/* 60:   */   }
/* 61:   */   
/* 62:   */   public void addToPath(Path2D.Double path, XDGFShape parent)
/* 63:   */   {
/* 64:83 */     if (getDel()) {
/* 65:84 */       return;
/* 66:   */     }
/* 67:86 */     path.moveTo(getX().doubleValue() * parent.getWidth().doubleValue(), getY().doubleValue() * parent.getHeight().doubleValue());
/* 68:   */   }
/* 69:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.RelMoveTo
 * JD-Core Version:    0.7.0.1
 */