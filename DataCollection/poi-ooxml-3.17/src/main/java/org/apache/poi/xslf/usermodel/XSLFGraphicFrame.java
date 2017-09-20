/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.geom.Rectangle2D;
/*   4:    */ import java.awt.geom.Rectangle2D.Double;
/*   5:    */ import javax.xml.namespace.QName;
/*   6:    */ import org.apache.poi.POIXMLException;
/*   7:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*   8:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   9:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  10:    */ import org.apache.poi.sl.usermodel.GraphicalFrame;
/*  11:    */ import org.apache.poi.sl.usermodel.ShapeType;
/*  12:    */ import org.apache.poi.util.POILogFactory;
/*  13:    */ import org.apache.poi.util.POILogger;
/*  14:    */ import org.apache.poi.util.Units;
/*  15:    */ import org.apache.xmlbeans.XmlCursor;
/*  16:    */ import org.apache.xmlbeans.XmlException;
/*  17:    */ import org.apache.xmlbeans.XmlObject;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
/*  23:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame;
/*  24:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
/*  25:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape.Factory;
/*  26:    */ 
/*  27:    */ public class XSLFGraphicFrame
/*  28:    */   extends XSLFShape
/*  29:    */   implements GraphicalFrame<XSLFShape, XSLFTextParagraph>
/*  30:    */ {
/*  31: 48 */   private static final POILogger LOG = POILogFactory.getLogger(XSLFGraphicFrame.class);
/*  32:    */   
/*  33:    */   XSLFGraphicFrame(CTGraphicalObjectFrame shape, XSLFSheet sheet)
/*  34:    */   {
/*  35: 51 */     super(shape, sheet);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public ShapeType getShapeType()
/*  39:    */   {
/*  40: 55 */     throw new UnsupportedOperationException();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public Rectangle2D getAnchor()
/*  44:    */   {
/*  45: 60 */     CTTransform2D xfrm = ((CTGraphicalObjectFrame)getXmlObject()).getXfrm();
/*  46: 61 */     CTPoint2D off = xfrm.getOff();
/*  47: 62 */     double x = Units.toPoints(off.getX());
/*  48: 63 */     double y = Units.toPoints(off.getY());
/*  49: 64 */     CTPositiveSize2D ext = xfrm.getExt();
/*  50: 65 */     double cx = Units.toPoints(ext.getCx());
/*  51: 66 */     double cy = Units.toPoints(ext.getCy());
/*  52: 67 */     return new Double(x, y, cx, cy);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setAnchor(Rectangle2D anchor)
/*  56:    */   {
/*  57: 72 */     CTTransform2D xfrm = ((CTGraphicalObjectFrame)getXmlObject()).getXfrm();
/*  58: 73 */     CTPoint2D off = xfrm.isSetOff() ? xfrm.getOff() : xfrm.addNewOff();
/*  59: 74 */     long x = Units.toEMU(anchor.getX());
/*  60: 75 */     long y = Units.toEMU(anchor.getY());
/*  61: 76 */     off.setX(x);
/*  62: 77 */     off.setY(y);
/*  63: 78 */     CTPositiveSize2D ext = xfrm.isSetExt() ? xfrm.getExt() : xfrm.addNewExt();
/*  64:    */     
/*  65: 80 */     long cx = Units.toEMU(anchor.getWidth());
/*  66: 81 */     long cy = Units.toEMU(anchor.getHeight());
/*  67: 82 */     ext.setCx(cx);
/*  68: 83 */     ext.setCy(cy);
/*  69:    */   }
/*  70:    */   
/*  71:    */   static XSLFGraphicFrame create(CTGraphicalObjectFrame shape, XSLFSheet sheet)
/*  72:    */   {
/*  73: 88 */     String uri = shape.getGraphic().getGraphicData().getUri();
/*  74: 89 */     if ("http://schemas.openxmlformats.org/drawingml/2006/table".equals(uri)) {
/*  75: 90 */       return new XSLFTable(shape, sheet);
/*  76:    */     }
/*  77: 92 */     return new XSLFGraphicFrame(shape, sheet);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setRotation(double theta)
/*  81:    */   {
/*  82:106 */     throw new IllegalArgumentException("Operation not supported");
/*  83:    */   }
/*  84:    */   
/*  85:    */   public double getRotation()
/*  86:    */   {
/*  87:119 */     return 0.0D;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setFlipHorizontal(boolean flip)
/*  91:    */   {
/*  92:123 */     throw new IllegalArgumentException("Operation not supported");
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setFlipVertical(boolean flip)
/*  96:    */   {
/*  97:127 */     throw new IllegalArgumentException("Operation not supported");
/*  98:    */   }
/*  99:    */   
/* 100:    */   public boolean getFlipHorizontal()
/* 101:    */   {
/* 102:136 */     return false;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public boolean getFlipVertical()
/* 106:    */   {
/* 107:140 */     return false;
/* 108:    */   }
/* 109:    */   
/* 110:    */   void copy(XSLFShape sh)
/* 111:    */   {
/* 112:145 */     super.copy(sh);
/* 113:    */     
/* 114:147 */     CTGraphicalObjectData data = ((CTGraphicalObjectFrame)getXmlObject()).getGraphic().getGraphicData();
/* 115:148 */     String uri = data.getUri();
/* 116:149 */     if (uri.equals("http://schemas.openxmlformats.org/drawingml/2006/diagram")) {
/* 117:150 */       copyDiagram(data, (XSLFGraphicFrame)sh);
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   private void copyDiagram(CTGraphicalObjectData objData, XSLFGraphicFrame srcShape)
/* 122:    */   {
/* 123:159 */     String xpath = "declare namespace dgm='http://schemas.openxmlformats.org/drawingml/2006/diagram' $this//dgm:relIds";
/* 124:160 */     XmlObject[] obj = objData.selectPath(xpath);
/* 125:161 */     if ((obj != null) && (obj.length == 1))
/* 126:    */     {
/* 127:162 */       XmlCursor c = obj[0].newCursor();
/* 128:    */       
/* 129:164 */       XSLFSheet sheet = srcShape.getSheet();
/* 130:    */       try
/* 131:    */       {
/* 132:166 */         String dm = c.getAttributeText(new QName("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "dm"));
/* 133:167 */         PackageRelationship dmRel = sheet.getPackagePart().getRelationship(dm);
/* 134:168 */         PackagePart dmPart = sheet.getPackagePart().getRelatedPart(dmRel);
/* 135:169 */         getSheet().importPart(dmRel, dmPart);
/* 136:    */         
/* 137:171 */         String lo = c.getAttributeText(new QName("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "lo"));
/* 138:172 */         PackageRelationship loRel = sheet.getPackagePart().getRelationship(lo);
/* 139:173 */         PackagePart loPart = sheet.getPackagePart().getRelatedPart(loRel);
/* 140:174 */         getSheet().importPart(loRel, loPart);
/* 141:    */         
/* 142:176 */         String qs = c.getAttributeText(new QName("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "qs"));
/* 143:177 */         PackageRelationship qsRel = sheet.getPackagePart().getRelationship(qs);
/* 144:178 */         PackagePart qsPart = sheet.getPackagePart().getRelatedPart(qsRel);
/* 145:179 */         getSheet().importPart(qsRel, qsPart);
/* 146:    */         
/* 147:181 */         String cs = c.getAttributeText(new QName("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "cs"));
/* 148:182 */         PackageRelationship csRel = sheet.getPackagePart().getRelationship(cs);
/* 149:183 */         PackagePart csPart = sheet.getPackagePart().getRelatedPart(csRel);
/* 150:184 */         getSheet().importPart(csRel, csPart);
/* 151:    */       }
/* 152:    */       catch (InvalidFormatException e)
/* 153:    */       {
/* 154:187 */         throw new POIXMLException(e);
/* 155:    */       }
/* 156:189 */       c.dispose();
/* 157:    */     }
/* 158:    */   }
/* 159:    */   
/* 160:    */   public XSLFPictureShape getFallbackPicture()
/* 161:    */   {
/* 162:195 */     String xquery = "declare namespace p='http://schemas.openxmlformats.org/presentationml/2006/main'; declare namespace mc='http://schemas.openxmlformats.org/markup-compatibility/2006' .//mc:Fallback/*/p:pic";
/* 163:    */     
/* 164:    */ 
/* 165:    */ 
/* 166:    */ 
/* 167:200 */     XmlObject xo = selectProperty(XmlObject.class, xquery);
/* 168:201 */     if (xo == null) {
/* 169:202 */       return null;
/* 170:    */     }
/* 171:    */     CTGroupShape gs;
/* 172:    */     try
/* 173:    */     {
/* 174:207 */       gs = CTGroupShape.Factory.parse(xo.newDomNode());
/* 175:    */     }
/* 176:    */     catch (XmlException e)
/* 177:    */     {
/* 178:209 */       LOG.log(5, new Object[] { "Can't parse fallback picture stream of graphical frame", e });
/* 179:210 */       return null;
/* 180:    */     }
/* 181:213 */     if (gs.sizeOfPicArray() == 0) {
/* 182:214 */       return null;
/* 183:    */     }
/* 184:217 */     return new XSLFPictureShape(gs.getPicArray(0), getSheet());
/* 185:    */   }
/* 186:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFGraphicFrame

 * JD-Core Version:    0.7.0.1

 */