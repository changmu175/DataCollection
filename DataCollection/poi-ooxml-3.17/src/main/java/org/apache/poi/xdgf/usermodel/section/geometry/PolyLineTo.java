/*  1:   */ package org.apache.poi.xdgf.usermodel.section.geometry;
/*  2:   */ 
/*  3:   */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*  4:   */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*  5:   */ import java.awt.geom.Path2D.Double;
/*  6:   */ import org.apache.poi.POIXMLException;
/*  7:   */ import org.apache.poi.util.NotImplemented;
/*  8:   */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/*  9:   */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/* 10:   */ 
/* 11:   */ public class PolyLineTo
/* 12:   */   implements GeometryRow
/* 13:   */ {
/* 14:30 */   PolyLineTo _master = null;
/* 15:33 */   Double x = null;
/* 16:36 */   Double y = null;
/* 17:39 */   String a = null;
/* 18:41 */   Boolean deleted = null;
/* 19:   */   
/* 20:   */   public PolyLineTo(RowType row)
/* 21:   */   {
/* 22:47 */     if (row.isSetDel()) {
/* 23:48 */       this.deleted = Boolean.valueOf(row.getDel());
/* 24:   */     }
/* 25:50 */     for (CellType cell : row.getCellArray())
/* 26:   */     {
/* 27:51 */       String cellName = cell.getN();
/* 28:53 */       if (cellName.equals("X")) {
/* 29:54 */         this.x = XDGFCell.parseDoubleValue(cell);
/* 30:55 */       } else if (cellName.equals("Y")) {
/* 31:56 */         this.y = XDGFCell.parseDoubleValue(cell);
/* 32:57 */       } else if (cellName.equals("A")) {
/* 33:58 */         this.a = cell.getV();
/* 34:   */       } else {
/* 35:60 */         throw new POIXMLException("Invalid cell '" + cellName + "' in ArcTo row");
/* 36:   */       }
/* 37:   */     }
/* 38:   */   }
/* 39:   */   
/* 40:   */   public boolean getDel()
/* 41:   */   {
/* 42:67 */     if (this.deleted != null) {
/* 43:68 */       return this.deleted.booleanValue();
/* 44:   */     }
/* 45:70 */     if (this._master != null) {
/* 46:71 */       return this._master.getDel();
/* 47:   */     }
/* 48:73 */     return false;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public Double getX()
/* 52:   */   {
/* 53:77 */     return this.x == null ? this._master.x : this.x;
/* 54:   */   }
/* 55:   */   
/* 56:   */   public Double getY()
/* 57:   */   {
/* 58:81 */     return this.y == null ? this._master.y : this.y;
/* 59:   */   }
/* 60:   */   
/* 61:   */   public String getA()
/* 62:   */   {
/* 63:85 */     return this.a == null ? this._master.a : this.a;
/* 64:   */   }
/* 65:   */   
/* 66:   */   public void setupMaster(GeometryRow row)
/* 67:   */   {
/* 68:90 */     this._master = ((PolyLineTo)row);
/* 69:   */   }
/* 70:   */   
/* 71:   */   @NotImplemented
/* 72:   */   public void addToPath(Path2D.Double path, XDGFShape parent)
/* 73:   */   {
/* 74:96 */     if (getDel()) {
/* 75:97 */       return;
/* 76:   */     }
/* 77:98 */     throw new POIXMLException("Polyline support not implemented");
/* 78:   */   }
/* 79:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.geometry.PolyLineTo
 * JD-Core Version:    0.7.0.1
 */