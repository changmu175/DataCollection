/*  1:   */ package org.apache.poi.xdgf.usermodel.section.geometry;
/*  2:   */ 
/*  3:   */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*  4:   */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*  5:   */ import java.awt.geom.Path2D.Double;
/*  6:   */ import org.apache.poi.POIXMLException;
/*  7:   */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/*  8:   */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*  9:   */ 
/* 10:   */ public class RelLineTo
/* 11:   */   implements GeometryRow
/* 12:   */ {
/* 13:33 */   RelLineTo _master = null;
/* 14:35 */   Double x = null;
/* 15:36 */   Double y = null;
/* 16:38 */   Boolean deleted = null;
/* 17:   */   
/* 18:   */   public RelLineTo(RowType row)
/* 19:   */   {
/* 20:44 */     if (row.isSetDel()) {
/* 21:45 */       this.deleted = Boolean.valueOf(row.getDel());
/* 22:   */     }
/* 23:47 */     for (CellType cell : row.getCellArray())
/* 24:   */     {
/* 25:48 */       String cellName = cell.getN();
/* 26:50 */       if (cellName.equals("X")) {
/* 27:51 */         this.x = XDGFCell.parseDoubleValue(cell);
/* 28:52 */       } else if (cellName.equals("Y")) {
/* 29:53 */         this.y = XDGFCell.parseDoubleValue(cell);
/* 30:   */       } else {
/* 31:55 */         throw new POIXMLException("Invalid cell '" + cellName + "' in RelLineTo row");
/* 32:   */       }
/* 33:   */     }
/* 34:   */   }
/* 35:   */   
/* 36:   */   public boolean getDel()
/* 37:   */   {
/* 38:62 */     if (this.deleted != null) {
/* 39:63 */       return this.deleted.booleanValue();
/* 40:   */     }
/* 41:65 */     if (this._master != null) {
/* 42:66 */       return this._master.getDel();
/* 43:   */     }
/* 44:68 */     return false;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public Double getX()
/* 48:   */   {
/* 49:72 */     return this.x == null ? this._master.x : this.x;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public Double getY()
/* 53:   */   {
/* 54:76 */     return this.y == null ? this._master.y : this.y;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public void setupMaster(GeometryRow row)
/* 58:   */   {
/* 59:81 */     this._master = ((RelLineTo)row);
/* 60:   */   }
/* 61:   */   
/* 62:   */   public void addToPath(Path2D.Double path, XDGFShape parent)
/* 63:   */   {
/* 64:87 */     if (getDel()) {
/* 65:88 */       return;
/* 66:   */     }
/* 67:90 */     path.lineTo(getX().doubleValue() * parent.getWidth().doubleValue(), getY().doubleValue() * parent.getHeight().doubleValue());
/* 68:   */   }
/* 69:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.RelLineTo
 * JD-Core Version:    0.7.0.1
 */