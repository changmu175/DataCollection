/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import javax.xml.namespace.QName;
/*   7:    */ import org.apache.poi.POIXMLDocumentPart;
/*   8:    */ import org.apache.poi.POIXMLException;
/*   9:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  10:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  11:    */ import org.apache.poi.poifs.filesystem.DirectoryEntry;
/*  12:    */ import org.apache.poi.poifs.filesystem.FileMagic;
/*  13:    */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*  14:    */ import org.apache.poi.ss.usermodel.ObjectData;
/*  15:    */ import org.apache.poi.util.IOUtils;
/*  16:    */ import org.apache.poi.util.POILogFactory;
/*  17:    */ import org.apache.poi.util.POILogger;
/*  18:    */ import org.apache.xmlbeans.XmlCursor;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtension;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
/*  24:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D;
/*  25:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  26:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
/*  27:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STShapeType;
/*  28:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape;
/*  29:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape.Factory;
/*  30:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShapeNonVisual;
/*  31:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject;
/*  32:    */ 
/*  33:    */ public class XSSFObjectData
/*  34:    */   extends XSSFSimpleShape
/*  35:    */   implements ObjectData
/*  36:    */ {
/*  37: 55 */   private static final POILogger LOG = POILogFactory.getLogger(XSSFObjectData.class);
/*  38: 60 */   private static CTShape prototype = null;
/*  39:    */   private CTOleObject oleObject;
/*  40:    */   
/*  41:    */   protected XSSFObjectData(XSSFDrawing drawing, CTShape ctShape)
/*  42:    */   {
/*  43: 65 */     super(drawing, ctShape);
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected static CTShape prototype()
/*  47:    */   {
/*  48: 75 */     String drawNS = "http://schemas.microsoft.com/office/drawing/2010/main";
/*  49: 77 */     if (prototype == null)
/*  50:    */     {
/*  51: 78 */       CTShape shape = CTShape.Factory.newInstance();
/*  52:    */       
/*  53: 80 */       CTShapeNonVisual nv = shape.addNewNvSpPr();
/*  54: 81 */       CTNonVisualDrawingProps nvp = nv.addNewCNvPr();
/*  55: 82 */       nvp.setId(1L);
/*  56: 83 */       nvp.setName("Shape 1");
/*  57:    */       
/*  58: 85 */       CTOfficeArtExtensionList extLst = nvp.addNewExtLst();
/*  59:    */       
/*  60: 87 */       CTOfficeArtExtension ext = extLst.addNewExt();
/*  61: 88 */       ext.setUri("{63B3BB69-23CF-44E3-9099-C40C66FF867C}");
/*  62: 89 */       XmlCursor cur = ext.newCursor();
/*  63: 90 */       cur.toEndToken();
/*  64: 91 */       cur.beginElement(new QName("http://schemas.microsoft.com/office/drawing/2010/main", "compatExt", "a14"));
/*  65: 92 */       cur.insertNamespace("a14", "http://schemas.microsoft.com/office/drawing/2010/main");
/*  66: 93 */       cur.insertAttributeWithValue("spid", "_x0000_s1");
/*  67: 94 */       cur.dispose();
/*  68:    */       
/*  69: 96 */       nv.addNewCNvSpPr();
/*  70:    */       
/*  71: 98 */       CTShapeProperties sp = shape.addNewSpPr();
/*  72: 99 */       CTTransform2D t2d = sp.addNewXfrm();
/*  73:100 */       CTPositiveSize2D p1 = t2d.addNewExt();
/*  74:101 */       p1.setCx(0L);
/*  75:102 */       p1.setCy(0L);
/*  76:103 */       CTPoint2D p2 = t2d.addNewOff();
/*  77:104 */       p2.setX(0L);
/*  78:105 */       p2.setY(0L);
/*  79:    */       
/*  80:107 */       CTPresetGeometry2D geom = sp.addNewPrstGeom();
/*  81:108 */       geom.setPrst(STShapeType.RECT);
/*  82:109 */       geom.addNewAvLst();
/*  83:    */       
/*  84:111 */       prototype = shape;
/*  85:    */     }
/*  86:113 */     return prototype;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public String getOLE2ClassName()
/*  90:    */   {
/*  91:121 */     return getOleObject().getProgId();
/*  92:    */   }
/*  93:    */   
/*  94:    */   public CTOleObject getOleObject()
/*  95:    */   {
/*  96:128 */     if (this.oleObject == null)
/*  97:    */     {
/*  98:129 */       long shapeId = getCTShape().getNvSpPr().getCNvPr().getId();
/*  99:130 */       this.oleObject = getSheet().readOleObject(shapeId);
/* 100:131 */       if (this.oleObject == null) {
/* 101:132 */         throw new POIXMLException("Ole object not found in sheet container - it's probably a control element");
/* 102:    */       }
/* 103:    */     }
/* 104:135 */     return this.oleObject;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public byte[] getObjectData()
/* 108:    */     throws IOException
/* 109:    */   {
/* 110:140 */     InputStream is = getObjectPart().getInputStream();
/* 111:141 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 112:142 */     IOUtils.copy(is, bos);
/* 113:143 */     is.close();
/* 114:144 */     return bos.toByteArray();
/* 115:    */   }
/* 116:    */   
/* 117:    */   public PackagePart getObjectPart()
/* 118:    */   {
/* 119:151 */     if (!getOleObject().isSetId()) {
/* 120:152 */       throw new POIXMLException("Invalid ole object found in sheet container");
/* 121:    */     }
/* 122:154 */     POIXMLDocumentPart pdp = getSheet().getRelationById(getOleObject().getId());
/* 123:155 */     return pdp == null ? null : pdp.getPackagePart();
/* 124:    */   }
/* 125:    */   
/* 126:    */   public boolean hasDirectoryEntry()
/* 127:    */   {
/* 128:160 */     InputStream is = null;
/* 129:    */     try
/* 130:    */     {
/* 131:162 */       is = getObjectPart().getInputStream();
/* 132:163 */       is = FileMagic.prepareToCheckMagic(is);
/* 133:164 */       return FileMagic.valueOf(is) == FileMagic.OLE2;
/* 134:    */     }
/* 135:    */     catch (IOException e)
/* 136:    */     {
/* 137:166 */       LOG.log(5, new Object[] { "can't determine if directory entry exists", e });
/* 138:167 */       return false;
/* 139:    */     }
/* 140:    */     finally
/* 141:    */     {
/* 142:169 */       IOUtils.closeQuietly(is);
/* 143:    */     }
/* 144:    */   }
/* 145:    */   
/* 146:    */   public DirectoryEntry getDirectory()
/* 147:    */     throws IOException
/* 148:    */   {
/* 149:176 */     InputStream is = null;
/* 150:    */     try
/* 151:    */     {
/* 152:178 */       is = getObjectPart().getInputStream();
/* 153:179 */       return new POIFSFileSystem(is).getRoot();
/* 154:    */     }
/* 155:    */     finally
/* 156:    */     {
/* 157:181 */       IOUtils.closeQuietly(is);
/* 158:    */     }
/* 159:    */   }
/* 160:    */   
/* 161:    */   public String getFileName()
/* 162:    */   {
/* 163:190 */     return getObjectPart().getPartName().getName();
/* 164:    */   }
/* 165:    */   
/* 166:    */   protected XSSFSheet getSheet()
/* 167:    */   {
/* 168:194 */     return (XSSFSheet)getDrawing().getParent();
/* 169:    */   }
/* 170:    */   
/* 171:    */   public XSSFPictureData getPictureData()
/* 172:    */   {
/* 173:199 */     XmlCursor cur = getOleObject().newCursor();
/* 174:    */     try
/* 175:    */     {
/* 176:    */       String blipId;
/* 177:201 */       if (cur.toChild("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "objectPr"))
/* 178:    */       {
/* 179:202 */         blipId = cur.getAttributeText(new QName("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "id"));
/* 180:203 */         return (XSSFPictureData)getSheet().getRelationById(blipId);
/* 181:    */       }
/* 182:205 */       return null;
/* 183:    */     }
/* 184:    */     finally
/* 185:    */     {
/* 186:207 */       cur.dispose();
/* 187:    */     }
/* 188:    */   }
/* 189:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFObjectData
 * JD-Core Version:    0.7.0.1
 */