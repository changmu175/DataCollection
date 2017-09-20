/*  1:   */ package org.apache.poi.xslf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.POIXMLException;
/*  4:   */ import org.apache.poi.sl.usermodel.ConnectorShape;
/*  5:   */ import org.apache.poi.sl.usermodel.Placeholder;
/*  6:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*  7:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D;
/*  8:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  9:   */ import org.openxmlformats.schemas.drawingml.x2006.main.STShapeType;
/* 10:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CTConnector;
/* 11:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CTConnector.Factory;
/* 12:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CTConnectorNonVisual;
/* 13:   */ 
/* 14:   */ public class XSLFConnectorShape
/* 15:   */   extends XSLFSimpleShape
/* 16:   */   implements ConnectorShape<XSLFShape, XSLFTextParagraph>
/* 17:   */ {
/* 18:   */   XSLFConnectorShape(CTConnector shape, XSLFSheet sheet)
/* 19:   */   {
/* 20:43 */     super(shape, sheet);
/* 21:   */   }
/* 22:   */   
/* 23:   */   static CTConnector prototype(int shapeId)
/* 24:   */   {
/* 25:50 */     CTConnector ct = CTConnector.Factory.newInstance();
/* 26:51 */     CTConnectorNonVisual nvSpPr = ct.addNewNvCxnSpPr();
/* 27:52 */     CTNonVisualDrawingProps cnv = nvSpPr.addNewCNvPr();
/* 28:53 */     cnv.setName("Connector " + shapeId);
/* 29:54 */     cnv.setId(shapeId + 1);
/* 30:55 */     nvSpPr.addNewCNvCxnSpPr();
/* 31:56 */     nvSpPr.addNewNvPr();
/* 32:57 */     CTShapeProperties spPr = ct.addNewSpPr();
/* 33:58 */     CTPresetGeometry2D prst = spPr.addNewPrstGeom();
/* 34:59 */     prst.setPrst(STShapeType.LINE);
/* 35:60 */     prst.addNewAvLst();
/* 36:61 */     spPr.addNewLn();
/* 37:62 */     return ct;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public XSLFShadow getShadow()
/* 41:   */   {
/* 42:71 */     return null;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public void setPlaceholder(Placeholder placeholder)
/* 46:   */   {
/* 47:76 */     throw new POIXMLException("A connector shape can't be a placeholder.");
/* 48:   */   }
/* 49:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFConnectorShape
 * JD-Core Version:    0.7.0.1
 */