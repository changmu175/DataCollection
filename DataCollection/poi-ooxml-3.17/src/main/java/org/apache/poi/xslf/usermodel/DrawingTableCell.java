/*  1:   */ package org.apache.poi.xslf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Removal;
/*  4:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell;
/*  5:   */ 
/*  6:   */ @Removal(version="3.18")
/*  7:   */ public class DrawingTableCell
/*  8:   */ {
/*  9:   */   private final CTTableCell cell;
/* 10:   */   private final DrawingTextBody drawingTextBody;
/* 11:   */   
/* 12:   */   public DrawingTableCell(CTTableCell cell)
/* 13:   */   {
/* 14:32 */     this.cell = cell;
/* 15:33 */     this.drawingTextBody = new DrawingTextBody(this.cell.getTxBody());
/* 16:   */   }
/* 17:   */   
/* 18:   */   public DrawingTextBody getTextBody()
/* 19:   */   {
/* 20:37 */     return this.drawingTextBody;
/* 21:   */   }
/* 22:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.DrawingTableCell
 * JD-Core Version:    0.7.0.1
 */