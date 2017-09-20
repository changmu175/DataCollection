/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.sl.usermodel.AutoShape;
/*   4:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*   5:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingShapeProps;
/*   6:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D;
/*   7:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
/*   8:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*   9:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
/*  10:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STShapeType;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType;
/*  17:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
/*  18:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTShape.Factory;
/*  19:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTShapeNonVisual;
/*  20:    */ 
/*  21:    */ public class XSLFAutoShape
/*  22:    */   extends XSLFTextShape
/*  23:    */   implements AutoShape<XSLFShape, XSLFTextParagraph>
/*  24:    */ {
/*  25:    */   XSLFAutoShape(CTShape shape, XSLFSheet sheet)
/*  26:    */   {
/*  27: 48 */     super(shape, sheet);
/*  28:    */   }
/*  29:    */   
/*  30:    */   static XSLFAutoShape create(CTShape shape, XSLFSheet sheet)
/*  31:    */   {
/*  32: 53 */     if (shape.getSpPr().isSetCustGeom()) {
/*  33: 54 */       return new XSLFFreeformShape(shape, sheet);
/*  34:    */     }
/*  35: 55 */     if (shape.getNvSpPr().getCNvSpPr().isSetTxBox()) {
/*  36: 56 */       return new XSLFTextBox(shape, sheet);
/*  37:    */     }
/*  38: 58 */     return new XSLFAutoShape(shape, sheet);
/*  39:    */   }
/*  40:    */   
/*  41:    */   static CTShape prototype(int shapeId)
/*  42:    */   {
/*  43: 66 */     CTShape ct = CTShape.Factory.newInstance();
/*  44: 67 */     CTShapeNonVisual nvSpPr = ct.addNewNvSpPr();
/*  45: 68 */     CTNonVisualDrawingProps cnv = nvSpPr.addNewCNvPr();
/*  46: 69 */     cnv.setName("AutoShape " + shapeId);
/*  47: 70 */     cnv.setId(shapeId + 1);
/*  48: 71 */     nvSpPr.addNewCNvSpPr();
/*  49: 72 */     nvSpPr.addNewNvPr();
/*  50: 73 */     CTShapeProperties spPr = ct.addNewSpPr();
/*  51: 74 */     CTPresetGeometry2D prst = spPr.addNewPrstGeom();
/*  52: 75 */     prst.setPrst(STShapeType.RECT);
/*  53: 76 */     prst.addNewAvLst();
/*  54: 77 */     return ct;
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected static void initTextBody(CTTextBody txBody)
/*  58:    */   {
/*  59: 81 */     CTTextBodyProperties bodypr = txBody.addNewBodyPr();
/*  60: 82 */     bodypr.setAnchor(STTextAnchoringType.T);
/*  61: 83 */     bodypr.setRtlCol(false);
/*  62: 84 */     CTTextParagraph p = txBody.addNewP();
/*  63: 85 */     p.addNewPPr().setAlgn(STTextAlignType.L);
/*  64: 86 */     CTTextCharacterProperties endPr = p.addNewEndParaRPr();
/*  65: 87 */     endPr.setLang("en-US");
/*  66: 88 */     endPr.setSz(1100);
/*  67: 89 */     p.addNewR().setT("");
/*  68: 90 */     txBody.addNewLstStyle();
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected CTTextBody getTextBody(boolean create)
/*  72:    */   {
/*  73: 94 */     CTShape shape = (CTShape)getXmlObject();
/*  74: 95 */     CTTextBody txBody = shape.getTxBody();
/*  75: 96 */     if ((txBody == null) && (create))
/*  76:    */     {
/*  77: 97 */       txBody = shape.addNewTxBody();
/*  78: 98 */       initTextBody(txBody);
/*  79:    */     }
/*  80:100 */     return txBody;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public String toString()
/*  84:    */   {
/*  85:105 */     return "[" + getClass().getSimpleName() + "] " + getShapeName();
/*  86:    */   }
/*  87:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFAutoShape
 * JD-Core Version:    0.7.0.1
 */