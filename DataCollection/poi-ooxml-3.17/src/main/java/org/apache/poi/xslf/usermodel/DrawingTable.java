/*  1:   */ package org.apache.poi.xslf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Removal;
/*  4:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTable;
/*  5:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow;
/*  6:   */ 
/*  7:   */ @Removal(version="3.18")
/*  8:   */ public class DrawingTable
/*  9:   */ {
/* 10:   */   private final CTTable table;
/* 11:   */   
/* 12:   */   public DrawingTable(CTTable table)
/* 13:   */   {
/* 14:32 */     this.table = table;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public DrawingTableRow[] getRows()
/* 18:   */   {
/* 19:36 */     CTTableRow[] ctTableRows = this.table.getTrArray();
/* 20:37 */     DrawingTableRow[] o = new DrawingTableRow[ctTableRows.length];
/* 21:39 */     for (int i = 0; i < o.length; i++) {
/* 22:40 */       o[i] = new DrawingTableRow(ctTableRows[i]);
/* 23:   */     }
/* 24:43 */     return o;
/* 25:   */   }
/* 26:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.DrawingTable
 * JD-Core Version:    0.7.0.1
 */