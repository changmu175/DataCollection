/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Insets;
/*   4:    */ import java.net.URI;
/*   5:    */ import javax.xml.namespace.QName;
/*   6:    */ import org.apache.poi.POIXMLException;
/*   7:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   8:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*   9:    */ import org.apache.poi.sl.usermodel.PictureShape;
/*  10:    */ import org.apache.poi.sl.usermodel.Placeholder;
/*  11:    */ import org.apache.xmlbeans.XmlCursor;
/*  12:    */ import org.apache.xmlbeans.XmlException;
/*  13:    */ import org.apache.xmlbeans.XmlObject;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlip;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtension;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPictureLocking;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  24:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties;
/*  25:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STShapeType;
/*  26:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps;
/*  27:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPicture;
/*  28:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPicture.Factory;
/*  29:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPictureNonVisual;
/*  30:    */ 
/*  31:    */ public class XSLFPictureShape
/*  32:    */   extends XSLFSimpleShape
/*  33:    */   implements PictureShape<XSLFShape, XSLFTextParagraph>
/*  34:    */ {
/*  35:    */   private XSLFPictureData _data;
/*  36:    */   
/*  37:    */   XSLFPictureShape(CTPicture shape, XSLFSheet sheet)
/*  38:    */   {
/*  39: 58 */     super(shape, sheet);
/*  40:    */   }
/*  41:    */   
/*  42:    */   static CTPicture prototype(int shapeId, String rel)
/*  43:    */   {
/*  44: 67 */     CTPicture ct = CTPicture.Factory.newInstance();
/*  45: 68 */     CTPictureNonVisual nvSpPr = ct.addNewNvPicPr();
/*  46: 69 */     CTNonVisualDrawingProps cnv = nvSpPr.addNewCNvPr();
/*  47: 70 */     cnv.setName("Picture " + shapeId);
/*  48: 71 */     cnv.setId(shapeId + 1);
/*  49: 72 */     nvSpPr.addNewCNvPicPr().addNewPicLocks().setNoChangeAspect(true);
/*  50: 73 */     nvSpPr.addNewNvPr();
/*  51:    */     
/*  52: 75 */     CTBlipFillProperties blipFill = ct.addNewBlipFill();
/*  53: 76 */     CTBlip blip = blipFill.addNewBlip();
/*  54: 77 */     blip.setEmbed(rel);
/*  55: 78 */     blipFill.addNewStretch().addNewFillRect();
/*  56:    */     
/*  57: 80 */     CTShapeProperties spPr = ct.addNewSpPr();
/*  58: 81 */     CTPresetGeometry2D prst = spPr.addNewPrstGeom();
/*  59: 82 */     prst.setPrst(STShapeType.RECT);
/*  60: 83 */     prst.addNewAvLst();
/*  61: 84 */     return ct;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public boolean isExternalLinkedPicture()
/*  65:    */   {
/*  66: 94 */     if ((getBlipId() == null) && (getBlipLink() != null)) {
/*  67: 95 */       return true;
/*  68:    */     }
/*  69: 97 */     return false;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public XSLFPictureData getPictureData()
/*  73:    */   {
/*  74:105 */     if (this._data == null)
/*  75:    */     {
/*  76:106 */       String blipId = getBlipId();
/*  77:107 */       if (blipId == null) {
/*  78:107 */         return null;
/*  79:    */       }
/*  80:109 */       PackagePart p = getSheet().getPackagePart();
/*  81:110 */       PackageRelationship rel = p.getRelationship(blipId);
/*  82:111 */       if (rel != null) {
/*  83:    */         try
/*  84:    */         {
/*  85:113 */           PackagePart imgPart = p.getRelatedPart(rel);
/*  86:114 */           this._data = new XSLFPictureData(imgPart);
/*  87:    */         }
/*  88:    */         catch (Exception e)
/*  89:    */         {
/*  90:117 */           throw new POIXMLException(e);
/*  91:    */         }
/*  92:    */       }
/*  93:    */     }
/*  94:121 */     return this._data;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void setPlaceholder(Placeholder placeholder)
/*  98:    */   {
/*  99:126 */     super.setPlaceholder(placeholder);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public URI getPictureLink()
/* 103:    */   {
/* 104:136 */     if (getBlipId() != null) {
/* 105:138 */       return null;
/* 106:    */     }
/* 107:141 */     String rId = getBlipLink();
/* 108:142 */     if (rId == null) {
/* 109:144 */       return null;
/* 110:    */     }
/* 111:147 */     PackagePart p = getSheet().getPackagePart();
/* 112:148 */     PackageRelationship rel = p.getRelationship(rId);
/* 113:149 */     if (rel != null) {
/* 114:150 */       return rel.getTargetURI();
/* 115:    */     }
/* 116:152 */     return null;
/* 117:    */   }
/* 118:    */   
/* 119:    */   protected CTBlipFillProperties getBlipFill()
/* 120:    */   {
/* 121:156 */     CTPicture ct = (CTPicture)getXmlObject();
/* 122:157 */     CTBlipFillProperties bfp = ct.getBlipFill();
/* 123:158 */     if (bfp != null) {
/* 124:159 */       return bfp;
/* 125:    */     }
/* 126:162 */     String xquery = "declare namespace p='http://schemas.openxmlformats.org/presentationml/2006/main'; declare namespace mc='http://schemas.openxmlformats.org/markup-compatibility/2006' .//mc:Fallback/p:blipFill";
/* 127:    */     
/* 128:    */ 
/* 129:    */ 
/* 130:    */ 
/* 131:167 */     XmlObject xo = selectProperty(XmlObject.class, xquery);
/* 132:    */     try
/* 133:    */     {
/* 134:169 */       xo = CTPicture.Factory.parse(xo.getDomNode());
/* 135:    */     }
/* 136:    */     catch (XmlException xe)
/* 137:    */     {
/* 138:171 */       return null;
/* 139:    */     }
/* 140:173 */     return ((CTPicture)xo).getBlipFill();
/* 141:    */   }
/* 142:    */   
/* 143:    */   protected CTBlip getBlip()
/* 144:    */   {
/* 145:177 */     return getBlipFill().getBlip();
/* 146:    */   }
/* 147:    */   
/* 148:    */   protected String getBlipLink()
/* 149:    */   {
/* 150:181 */     String link = getBlip().getLink();
/* 151:182 */     if (link.isEmpty()) {
/* 152:182 */       return null;
/* 153:    */     }
/* 154:183 */     return link;
/* 155:    */   }
/* 156:    */   
/* 157:    */   protected String getBlipId()
/* 158:    */   {
/* 159:187 */     String id = getBlip().getEmbed();
/* 160:188 */     if (id.isEmpty()) {
/* 161:188 */       return null;
/* 162:    */     }
/* 163:189 */     return id;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public Insets getClipping()
/* 167:    */   {
/* 168:194 */     CTRelativeRect r = getBlipFill().getSrcRect();
/* 169:195 */     return r == null ? null : new Insets(r.getT(), r.getL(), r.getB(), r.getR());
/* 170:    */   }
/* 171:    */   
/* 172:    */   void copy(XSLFShape sh)
/* 173:    */   {
/* 174:200 */     super.copy(sh);
/* 175:    */     
/* 176:202 */     XSLFPictureShape p = (XSLFPictureShape)sh;
/* 177:203 */     String blipId = p.getBlipId();
/* 178:204 */     String relId = getSheet().importBlip(blipId, p.getSheet().getPackagePart());
/* 179:    */     
/* 180:206 */     CTPicture ct = (CTPicture)getXmlObject();
/* 181:207 */     CTBlip blip = getBlipFill().getBlip();
/* 182:208 */     blip.setEmbed(relId);
/* 183:    */     
/* 184:210 */     CTApplicationNonVisualDrawingProps nvPr = ct.getNvPicPr().getNvPr();
/* 185:211 */     if (nvPr.isSetCustDataLst()) {
/* 186:213 */       nvPr.unsetCustDataLst();
/* 187:    */     }
/* 188:215 */     if (blip.isSetExtLst())
/* 189:    */     {
/* 190:217 */       CTOfficeArtExtensionList extLst = blip.getExtLst();
/* 191:218 */       for (CTOfficeArtExtension ext : extLst.getExtArray())
/* 192:    */       {
/* 193:219 */         String xpath = "declare namespace a14='http://schemas.microsoft.com/office/drawing/2010/main' $this//a14:imgProps/a14:imgLayer";
/* 194:220 */         XmlObject[] obj = ext.selectPath(xpath);
/* 195:221 */         if ((obj != null) && (obj.length == 1))
/* 196:    */         {
/* 197:222 */           XmlCursor c = obj[0].newCursor();
/* 198:223 */           String id = c.getAttributeText(new QName("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "embed"));
/* 199:224 */           String newId = getSheet().importBlip(id, p.getSheet().getPackagePart());
/* 200:225 */           c.setAttributeText(new QName("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "embed"), newId);
/* 201:226 */           c.dispose();
/* 202:    */         }
/* 203:    */       }
/* 204:    */     }
/* 205:    */   }
/* 206:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFPictureShape
 * JD-Core Version:    0.7.0.1
 */