/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.Internal;
/*   4:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference;
/*   5:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*   6:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
/*   7:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
/*   8:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D;
/*   9:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor;
/*  10:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STFontCollectionIndex;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STSchemeColorVal;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STShapeType;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STShapeType.Enum;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector.Factory;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnectorNonVisual;
/*  21:    */ 
/*  22:    */ public final class XSSFConnector
/*  23:    */   extends XSSFShape
/*  24:    */ {
/*  25: 45 */   private static CTConnector prototype = null;
/*  26:    */   private CTConnector ctShape;
/*  27:    */   
/*  28:    */   protected XSSFConnector(XSSFDrawing drawing, CTConnector ctShape)
/*  29:    */   {
/*  30: 56 */     this.drawing = drawing;
/*  31: 57 */     this.ctShape = ctShape;
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected static CTConnector prototype()
/*  35:    */   {
/*  36: 65 */     if (prototype == null)
/*  37:    */     {
/*  38: 66 */       CTConnector shape = CTConnector.Factory.newInstance();
/*  39: 67 */       CTConnectorNonVisual nv = shape.addNewNvCxnSpPr();
/*  40: 68 */       CTNonVisualDrawingProps nvp = nv.addNewCNvPr();
/*  41: 69 */       nvp.setId(1L);
/*  42: 70 */       nvp.setName("Shape 1");
/*  43: 71 */       nv.addNewCNvCxnSpPr();
/*  44:    */       
/*  45: 73 */       CTShapeProperties sp = shape.addNewSpPr();
/*  46: 74 */       CTTransform2D t2d = sp.addNewXfrm();
/*  47: 75 */       CTPositiveSize2D p1 = t2d.addNewExt();
/*  48: 76 */       p1.setCx(0L);
/*  49: 77 */       p1.setCy(0L);
/*  50: 78 */       CTPoint2D p2 = t2d.addNewOff();
/*  51: 79 */       p2.setX(0L);
/*  52: 80 */       p2.setY(0L);
/*  53:    */       
/*  54: 82 */       CTPresetGeometry2D geom = sp.addNewPrstGeom();
/*  55: 83 */       geom.setPrst(STShapeType.LINE);
/*  56: 84 */       geom.addNewAvLst();
/*  57:    */       
/*  58: 86 */       CTShapeStyle style = shape.addNewStyle();
/*  59: 87 */       CTSchemeColor scheme = style.addNewLnRef().addNewSchemeClr();
/*  60: 88 */       scheme.setVal(STSchemeColorVal.ACCENT_1);
/*  61: 89 */       style.getLnRef().setIdx(1L);
/*  62:    */       
/*  63: 91 */       CTStyleMatrixReference fillref = style.addNewFillRef();
/*  64: 92 */       fillref.setIdx(0L);
/*  65: 93 */       fillref.addNewSchemeClr().setVal(STSchemeColorVal.ACCENT_1);
/*  66:    */       
/*  67: 95 */       CTStyleMatrixReference effectRef = style.addNewEffectRef();
/*  68: 96 */       effectRef.setIdx(0L);
/*  69: 97 */       effectRef.addNewSchemeClr().setVal(STSchemeColorVal.ACCENT_1);
/*  70:    */       
/*  71: 99 */       CTFontReference fontRef = style.addNewFontRef();
/*  72:100 */       fontRef.setIdx(STFontCollectionIndex.MINOR);
/*  73:101 */       fontRef.addNewSchemeClr().setVal(STSchemeColorVal.TX_1);
/*  74:    */       
/*  75:103 */       prototype = shape;
/*  76:    */     }
/*  77:105 */     return prototype;
/*  78:    */   }
/*  79:    */   
/*  80:    */   @Internal
/*  81:    */   public CTConnector getCTConnector()
/*  82:    */   {
/*  83:110 */     return this.ctShape;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public int getShapeType()
/*  87:    */   {
/*  88:120 */     return this.ctShape.getSpPr().getPrstGeom().getPrst().intValue();
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setShapeType(int type)
/*  92:    */   {
/*  93:130 */     this.ctShape.getSpPr().getPrstGeom().setPrst(STShapeType.Enum.forInt(type));
/*  94:    */   }
/*  95:    */   
/*  96:    */   protected CTShapeProperties getShapeProperties()
/*  97:    */   {
/*  98:134 */     return this.ctShape.getSpPr();
/*  99:    */   }
/* 100:    */   
/* 101:    */   public String getShapeName()
/* 102:    */   {
/* 103:139 */     return this.ctShape.getNvCxnSpPr().getCNvPr().getName();
/* 104:    */   }
/* 105:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFConnector
 * JD-Core Version:    0.7.0.1
 */