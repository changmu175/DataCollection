/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.geom.Rectangle2D.Double;
/*   5:    */ import org.apache.xmlbeans.XmlObject;
/*   6:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*   7:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTConnector;
/*   8:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame;
/*   9:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
/*  10:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPicture;
/*  11:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
/*  12:    */ 
/*  13:    */ public class XSLFDrawing
/*  14:    */ {
/*  15:    */   private XSLFSheet _sheet;
/*  16: 38 */   private int _shapeId = 1;
/*  17:    */   private CTGroupShape _spTree;
/*  18:    */   
/*  19:    */   XSLFDrawing(XSLFSheet sheet, CTGroupShape spTree)
/*  20:    */   {
/*  21: 42 */     this._sheet = sheet;
/*  22: 43 */     this._spTree = spTree;
/*  23: 44 */     XmlObject[] cNvPr = sheet.getSpTree().selectPath("declare namespace p='http://schemas.openxmlformats.org/presentationml/2006/main' .//*/p:cNvPr");
/*  24: 46 */     for (XmlObject o : cNvPr) {
/*  25: 49 */       if ((o instanceof CTNonVisualDrawingProps))
/*  26:    */       {
/*  27: 50 */         CTNonVisualDrawingProps p = (CTNonVisualDrawingProps)o;
/*  28: 51 */         this._shapeId = ((int)Math.max(this._shapeId, p.getId()));
/*  29:    */       }
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public XSLFAutoShape createAutoShape()
/*  34:    */   {
/*  35: 57 */     CTShape sp = this._spTree.addNewSp();
/*  36: 58 */     sp.set(XSLFAutoShape.prototype(this._shapeId++));
/*  37: 59 */     XSLFAutoShape shape = new XSLFAutoShape(sp, this._sheet);
/*  38: 60 */     shape.setAnchor(new Rectangle2D.Double());
/*  39: 61 */     return shape;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public XSLFFreeformShape createFreeform()
/*  43:    */   {
/*  44: 65 */     CTShape sp = this._spTree.addNewSp();
/*  45: 66 */     sp.set(XSLFFreeformShape.prototype(this._shapeId++));
/*  46: 67 */     XSLFFreeformShape shape = new XSLFFreeformShape(sp, this._sheet);
/*  47: 68 */     shape.setAnchor(new Rectangle2D.Double());
/*  48: 69 */     return shape;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public XSLFTextBox createTextBox()
/*  52:    */   {
/*  53: 73 */     CTShape sp = this._spTree.addNewSp();
/*  54: 74 */     sp.set(XSLFTextBox.prototype(this._shapeId++));
/*  55: 75 */     XSLFTextBox shape = new XSLFTextBox(sp, this._sheet);
/*  56: 76 */     shape.setAnchor(new Rectangle2D.Double());
/*  57: 77 */     return shape;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public XSLFConnectorShape createConnector()
/*  61:    */   {
/*  62: 81 */     CTConnector sp = this._spTree.addNewCxnSp();
/*  63: 82 */     sp.set(XSLFConnectorShape.prototype(this._shapeId++));
/*  64: 83 */     XSLFConnectorShape shape = new XSLFConnectorShape(sp, this._sheet);
/*  65: 84 */     shape.setAnchor(new Rectangle2D.Double());
/*  66: 85 */     shape.setLineColor(Color.black);
/*  67: 86 */     shape.setLineWidth(0.75D);
/*  68: 87 */     return shape;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public XSLFGroupShape createGroup()
/*  72:    */   {
/*  73: 91 */     CTGroupShape obj = this._spTree.addNewGrpSp();
/*  74: 92 */     obj.set(XSLFGroupShape.prototype(this._shapeId++));
/*  75: 93 */     XSLFGroupShape shape = new XSLFGroupShape(obj, this._sheet);
/*  76: 94 */     shape.setAnchor(new Rectangle2D.Double());
/*  77: 95 */     return shape;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public XSLFPictureShape createPicture(String rel)
/*  81:    */   {
/*  82: 99 */     CTPicture obj = this._spTree.addNewPic();
/*  83:100 */     obj.set(XSLFPictureShape.prototype(this._shapeId++, rel));
/*  84:101 */     XSLFPictureShape shape = new XSLFPictureShape(obj, this._sheet);
/*  85:102 */     shape.setAnchor(new Rectangle2D.Double());
/*  86:103 */     return shape;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public XSLFTable createTable()
/*  90:    */   {
/*  91:107 */     CTGraphicalObjectFrame obj = this._spTree.addNewGraphicFrame();
/*  92:108 */     obj.set(XSLFTable.prototype(this._shapeId++));
/*  93:109 */     XSLFTable shape = new XSLFTable(obj, this._sheet);
/*  94:110 */     shape.setAnchor(new Rectangle2D.Double());
/*  95:111 */     return shape;
/*  96:    */   }
/*  97:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFDrawing
 * JD-Core Version:    0.7.0.1
 */