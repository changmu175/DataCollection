/*  1:   */ package org.apache.poi.xslf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Removal;
/*  4:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell;
/*  5:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow;
/*  6:   */ 
/*  7:   */ @Removal(version="3.18")
/*  8:   */ public class DrawingTableRow
/*  9:   */ {
/* 10:   */   private final CTTableRow row;
/* 11:   */   
/* 12:   */   public DrawingTableRow(CTTableRow row)
/* 13:   */   {
/* 14:32 */     this.row = row;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public DrawingTableCell[] getCells()
/* 18:   */   {
/* 19:36 */     CTTableCell[] ctTableCells = this.row.getTcArray();
/* 20:37 */     DrawingTableCell[] o = new DrawingTableCell[ctTableCells.length];
/* 21:39 */     for (int i = 0; i < o.length; i++) {
/* 22:40 */       o[i] = new DrawingTableCell(ctTableCells[i]);
/* 23:   */     }
/* 24:43 */     return o;
/* 25:   */   }
/* 26:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.DrawingTableRow
 * JD-Core Version:    0.7.0.1
 */