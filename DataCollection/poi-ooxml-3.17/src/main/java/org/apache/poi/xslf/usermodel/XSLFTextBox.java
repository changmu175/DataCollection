/*  1:   */ package org.apache.poi.xslf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.sl.usermodel.TextBox;
/*  4:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*  5:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingShapeProps;
/*  6:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D;
/*  7:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  8:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
/*  9:   */ import org.openxmlformats.schemas.drawingml.x2006.main.STShapeType;
/* 10:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
/* 11:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CTShape.Factory;
/* 12:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CTShapeNonVisual;
/* 13:   */ 
/* 14:   */ public class XSLFTextBox
/* 15:   */   extends XSLFAutoShape
/* 16:   */   implements TextBox<XSLFShape, XSLFTextParagraph>
/* 17:   */ {
/* 18:   */   XSLFTextBox(CTShape shape, XSLFSheet sheet)
/* 19:   */   {
/* 20:37 */     super(shape, sheet);
/* 21:   */   }
/* 22:   */   
/* 23:   */   static CTShape prototype(int shapeId)
/* 24:   */   {
/* 25:45 */     CTShape ct = CTShape.Factory.newInstance();
/* 26:46 */     CTShapeNonVisual nvSpPr = ct.addNewNvSpPr();
/* 27:47 */     CTNonVisualDrawingProps cnv = nvSpPr.addNewCNvPr();
/* 28:48 */     cnv.setName("TextBox " + shapeId);
/* 29:49 */     cnv.setId(shapeId + 1);
/* 30:50 */     nvSpPr.addNewCNvSpPr().setTxBox(true);
/* 31:51 */     nvSpPr.addNewNvPr();
/* 32:52 */     CTShapeProperties spPr = ct.addNewSpPr();
/* 33:53 */     CTPresetGeometry2D prst = spPr.addNewPrstGeom();
/* 34:54 */     prst.setPrst(STShapeType.RECT);
/* 35:55 */     prst.addNewAvLst();
/* 36:56 */     CTTextBody txBody = ct.addNewTxBody();
/* 37:57 */     XSLFAutoShape.initTextBody(txBody);
/* 38:   */     
/* 39:59 */     return ct;
/* 40:   */   }
/* 41:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFTextBox
 * JD-Core Version:    0.7.0.1
 */