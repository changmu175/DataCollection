/*  1:   */ package org.apache.poi.xssf.streaming;
/*  2:   */ 
/*  3:   */ import java.util.Iterator;
/*  4:   */ import java.util.List;
/*  5:   */ import org.apache.poi.ss.usermodel.Chart;
/*  6:   */ import org.apache.poi.ss.usermodel.ClientAnchor;
/*  7:   */ import org.apache.poi.ss.usermodel.Comment;
/*  8:   */ import org.apache.poi.ss.usermodel.Drawing;
/*  9:   */ import org.apache.poi.ss.usermodel.ObjectData;
/* 10:   */ import org.apache.poi.xssf.usermodel.XSSFDrawing;
/* 11:   */ import org.apache.poi.xssf.usermodel.XSSFPicture;
/* 12:   */ import org.apache.poi.xssf.usermodel.XSSFShape;
/* 13:   */ 
/* 14:   */ public class SXSSFDrawing
/* 15:   */   implements Drawing<XSSFShape>
/* 16:   */ {
/* 17:   */   private final SXSSFWorkbook _wb;
/* 18:   */   private final XSSFDrawing _drawing;
/* 19:   */   
/* 20:   */   public SXSSFDrawing(SXSSFWorkbook workbook, XSSFDrawing drawing)
/* 21:   */   {
/* 22:41 */     this._wb = workbook;
/* 23:42 */     this._drawing = drawing;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public SXSSFPicture createPicture(ClientAnchor anchor, int pictureIndex)
/* 27:   */   {
/* 28:47 */     XSSFPicture pict = this._drawing.createPicture(anchor, pictureIndex);
/* 29:48 */     return new SXSSFPicture(this._wb, pict);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public Comment createCellComment(ClientAnchor anchor)
/* 33:   */   {
/* 34:53 */     return this._drawing.createCellComment(anchor);
/* 35:   */   }
/* 36:   */   
/* 37:   */   public Chart createChart(ClientAnchor anchor)
/* 38:   */   {
/* 39:58 */     return this._drawing.createChart(anchor);
/* 40:   */   }
/* 41:   */   
/* 42:   */   public ClientAnchor createAnchor(int dx1, int dy1, int dx2, int dy2, int col1, int row1, int col2, int row2)
/* 43:   */   {
/* 44:63 */     return this._drawing.createAnchor(dx1, dy1, dx2, dy2, col1, row1, col2, row2);
/* 45:   */   }
/* 46:   */   
/* 47:   */   public ObjectData createObjectData(ClientAnchor anchor, int storageId, int pictureIndex)
/* 48:   */   {
/* 49:68 */     return this._drawing.createObjectData(anchor, storageId, pictureIndex);
/* 50:   */   }
/* 51:   */   
/* 52:   */   public Iterator<XSSFShape> iterator()
/* 53:   */   {
/* 54:73 */     return this._drawing.getShapes().iterator();
/* 55:   */   }
/* 56:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.streaming.SXSSFDrawing
 * JD-Core Version:    0.7.0.1
 */