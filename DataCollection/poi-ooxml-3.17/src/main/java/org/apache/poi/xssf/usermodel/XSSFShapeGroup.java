/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*   6:    */ import org.apache.poi.ss.usermodel.ShapeContainer;
/*   7:    */ import org.apache.poi.util.Internal;
/*   8:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties;
/*   9:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupTransform2D;
/*  10:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape.Factory;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShapeNonVisual;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape;
/*  21:    */ 
/*  22:    */ public final class XSSFShapeGroup
/*  23:    */   extends XSSFShape
/*  24:    */   implements ShapeContainer<XSSFShape>
/*  25:    */ {
/*  26: 45 */   private static CTGroupShape prototype = null;
/*  27:    */   private CTGroupShape ctGroup;
/*  28:    */   
/*  29:    */   protected XSSFShapeGroup(XSSFDrawing drawing, CTGroupShape ctGroup)
/*  30:    */   {
/*  31: 56 */     this.drawing = drawing;
/*  32: 57 */     this.ctGroup = ctGroup;
/*  33:    */   }
/*  34:    */   
/*  35:    */   protected static CTGroupShape prototype()
/*  36:    */   {
/*  37: 64 */     if (prototype == null)
/*  38:    */     {
/*  39: 65 */       CTGroupShape shape = CTGroupShape.Factory.newInstance();
/*  40:    */       
/*  41: 67 */       CTGroupShapeNonVisual nv = shape.addNewNvGrpSpPr();
/*  42: 68 */       CTNonVisualDrawingProps nvpr = nv.addNewCNvPr();
/*  43: 69 */       nvpr.setId(0L);
/*  44: 70 */       nvpr.setName("Group 0");
/*  45: 71 */       nv.addNewCNvGrpSpPr();
/*  46: 72 */       CTGroupShapeProperties sp = shape.addNewGrpSpPr();
/*  47: 73 */       CTGroupTransform2D t2d = sp.addNewXfrm();
/*  48: 74 */       CTPositiveSize2D p1 = t2d.addNewExt();
/*  49: 75 */       p1.setCx(0L);
/*  50: 76 */       p1.setCy(0L);
/*  51: 77 */       CTPoint2D p2 = t2d.addNewOff();
/*  52: 78 */       p2.setX(0L);
/*  53: 79 */       p2.setY(0L);
/*  54: 80 */       CTPositiveSize2D p3 = t2d.addNewChExt();
/*  55: 81 */       p3.setCx(0L);
/*  56: 82 */       p3.setCy(0L);
/*  57: 83 */       CTPoint2D p4 = t2d.addNewChOff();
/*  58: 84 */       p4.setX(0L);
/*  59: 85 */       p4.setY(0L);
/*  60:    */       
/*  61: 87 */       prototype = shape;
/*  62:    */     }
/*  63: 89 */     return prototype;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public XSSFTextBox createTextbox(XSSFChildAnchor anchor)
/*  67:    */   {
/*  68:100 */     CTShape ctShape = this.ctGroup.addNewSp();
/*  69:101 */     ctShape.set(XSSFSimpleShape.prototype());
/*  70:    */     
/*  71:103 */     XSSFTextBox shape = new XSSFTextBox(getDrawing(), ctShape);
/*  72:104 */     shape.parent = this;
/*  73:105 */     shape.anchor = anchor;
/*  74:106 */     shape.getCTShape().getSpPr().setXfrm(anchor.getCTTransform2D());
/*  75:107 */     return shape;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public XSSFSimpleShape createSimpleShape(XSSFChildAnchor anchor)
/*  79:    */   {
/*  80:119 */     CTShape ctShape = this.ctGroup.addNewSp();
/*  81:120 */     ctShape.set(XSSFSimpleShape.prototype());
/*  82:    */     
/*  83:122 */     XSSFSimpleShape shape = new XSSFSimpleShape(getDrawing(), ctShape);
/*  84:123 */     shape.parent = this;
/*  85:124 */     shape.anchor = anchor;
/*  86:125 */     shape.getCTShape().getSpPr().setXfrm(anchor.getCTTransform2D());
/*  87:126 */     return shape;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public XSSFConnector createConnector(XSSFChildAnchor anchor)
/*  91:    */   {
/*  92:138 */     CTConnector ctShape = this.ctGroup.addNewCxnSp();
/*  93:139 */     ctShape.set(XSSFConnector.prototype());
/*  94:    */     
/*  95:141 */     XSSFConnector shape = new XSSFConnector(getDrawing(), ctShape);
/*  96:142 */     shape.parent = this;
/*  97:143 */     shape.anchor = anchor;
/*  98:144 */     shape.getCTConnector().getSpPr().setXfrm(anchor.getCTTransform2D());
/*  99:145 */     return shape;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public XSSFPicture createPicture(XSSFClientAnchor anchor, int pictureIndex)
/* 103:    */   {
/* 104:157 */     PackageRelationship rel = getDrawing().addPictureReference(pictureIndex);
/* 105:    */     
/* 106:159 */     CTPicture ctShape = this.ctGroup.addNewPic();
/* 107:160 */     ctShape.set(XSSFPicture.prototype());
/* 108:    */     
/* 109:162 */     XSSFPicture shape = new XSSFPicture(getDrawing(), ctShape);
/* 110:163 */     shape.parent = this;
/* 111:164 */     shape.anchor = anchor;
/* 112:165 */     shape.setPictureReference(rel);
/* 113:166 */     return shape;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public XSSFShapeGroup createGroup(XSSFChildAnchor anchor)
/* 117:    */   {
/* 118:176 */     CTGroupShape ctShape = this.ctGroup.addNewGrpSp();
/* 119:177 */     ctShape.set(prototype());
/* 120:    */     
/* 121:179 */     XSSFShapeGroup shape = new XSSFShapeGroup(getDrawing(), ctShape);
/* 122:180 */     shape.parent = this;
/* 123:181 */     shape.anchor = anchor;
/* 124:    */     
/* 125:    */ 
/* 126:    */ 
/* 127:185 */     CTGroupTransform2D xfrm = shape.getCTGroupShape().getGrpSpPr().getXfrm();
/* 128:186 */     CTTransform2D t2 = anchor.getCTTransform2D();
/* 129:187 */     xfrm.setOff(t2.getOff());
/* 130:188 */     xfrm.setExt(t2.getExt());
/* 131:    */     
/* 132:190 */     xfrm.setChExt(t2.getExt());
/* 133:191 */     xfrm.setFlipH(t2.getFlipH());
/* 134:192 */     xfrm.setFlipV(t2.getFlipV());
/* 135:    */     
/* 136:194 */     return shape;
/* 137:    */   }
/* 138:    */   
/* 139:    */   @Internal
/* 140:    */   public CTGroupShape getCTGroupShape()
/* 141:    */   {
/* 142:199 */     return this.ctGroup;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void setCoordinates(int x1, int y1, int x2, int y2)
/* 146:    */   {
/* 147:207 */     CTGroupTransform2D t2d = this.ctGroup.getGrpSpPr().getXfrm();
/* 148:208 */     CTPoint2D off = t2d.getOff();
/* 149:209 */     off.setX(x1);
/* 150:210 */     off.setY(y1);
/* 151:211 */     CTPositiveSize2D ext = t2d.getExt();
/* 152:212 */     ext.setCx(x2);
/* 153:213 */     ext.setCy(y2);
/* 154:    */     
/* 155:215 */     CTPoint2D chOff = t2d.getChOff();
/* 156:216 */     chOff.setX(x1);
/* 157:217 */     chOff.setY(y1);
/* 158:218 */     CTPositiveSize2D chExt = t2d.getChExt();
/* 159:219 */     chExt.setCx(x2);
/* 160:220 */     chExt.setCy(y2);
/* 161:    */   }
/* 162:    */   
/* 163:    */   protected CTShapeProperties getShapeProperties()
/* 164:    */   {
/* 165:224 */     throw new IllegalStateException("Not supported for shape group");
/* 166:    */   }
/* 167:    */   
/* 168:    */   public Iterator<XSSFShape> iterator()
/* 169:    */   {
/* 170:229 */     return getDrawing().getShapes(this).iterator();
/* 171:    */   }
/* 172:    */   
/* 173:    */   public String getShapeName()
/* 174:    */   {
/* 175:234 */     return this.ctGroup.getNvGrpSpPr().getCNvPr().getName();
/* 176:    */   }
/* 177:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFShapeGroup
 * JD-Core Version:    0.7.0.1
 */