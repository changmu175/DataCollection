/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Dimension;
/*   4:    */ import java.io.IOException;
/*   5:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   6:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*   7:    */ import org.apache.poi.ss.usermodel.Picture;
/*   8:    */ import org.apache.poi.ss.util.ImageUtils;
/*   9:    */ import org.apache.poi.util.Internal;
/*  10:    */ import org.apache.poi.util.POILogFactory;
/*  11:    */ import org.apache.poi.util.POILogger;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlip;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPictureLocking;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STShapeType;
/*  24:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture;
/*  25:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture.Factory;
/*  26:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPictureNonVisual;
/*  27:    */ 
/*  28:    */ public final class XSSFPicture
/*  29:    */   extends XSSFShape
/*  30:    */   implements Picture
/*  31:    */ {
/*  32: 49 */   private static final POILogger logger = POILogFactory.getLogger(XSSFPicture.class);
/*  33: 63 */   private static CTPicture prototype = null;
/*  34:    */   private CTPicture ctPicture;
/*  35:    */   
/*  36:    */   protected XSSFPicture(XSSFDrawing drawing, CTPicture ctPicture)
/*  37:    */   {
/*  38: 77 */     this.drawing = drawing;
/*  39: 78 */     this.ctPicture = ctPicture;
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected static CTPicture prototype()
/*  43:    */   {
/*  44: 87 */     if (prototype == null)
/*  45:    */     {
/*  46: 88 */       CTPicture pic = CTPicture.Factory.newInstance();
/*  47: 89 */       CTPictureNonVisual nvpr = pic.addNewNvPicPr();
/*  48: 90 */       CTNonVisualDrawingProps nvProps = nvpr.addNewCNvPr();
/*  49: 91 */       nvProps.setId(1L);
/*  50: 92 */       nvProps.setName("Picture 1");
/*  51: 93 */       nvProps.setDescr("Picture");
/*  52: 94 */       CTNonVisualPictureProperties nvPicProps = nvpr.addNewCNvPicPr();
/*  53: 95 */       nvPicProps.addNewPicLocks().setNoChangeAspect(true);
/*  54:    */       
/*  55: 97 */       CTBlipFillProperties blip = pic.addNewBlipFill();
/*  56: 98 */       blip.addNewBlip().setEmbed("");
/*  57: 99 */       blip.addNewStretch().addNewFillRect();
/*  58:    */       
/*  59:101 */       CTShapeProperties sppr = pic.addNewSpPr();
/*  60:102 */       CTTransform2D t2d = sppr.addNewXfrm();
/*  61:103 */       CTPositiveSize2D ext = t2d.addNewExt();
/*  62:    */       
/*  63:105 */       ext.setCx(0L);
/*  64:106 */       ext.setCy(0L);
/*  65:    */       
/*  66:108 */       CTPoint2D off = t2d.addNewOff();
/*  67:109 */       off.setX(0L);
/*  68:110 */       off.setY(0L);
/*  69:    */       
/*  70:112 */       CTPresetGeometry2D prstGeom = sppr.addNewPrstGeom();
/*  71:113 */       prstGeom.setPrst(STShapeType.RECT);
/*  72:114 */       prstGeom.addNewAvLst();
/*  73:    */       
/*  74:116 */       prototype = pic;
/*  75:    */     }
/*  76:118 */     return prototype;
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected void setPictureReference(PackageRelationship rel)
/*  80:    */   {
/*  81:127 */     this.ctPicture.getBlipFill().getBlip().setEmbed(rel.getId());
/*  82:    */   }
/*  83:    */   
/*  84:    */   @Internal
/*  85:    */   public CTPicture getCTPicture()
/*  86:    */   {
/*  87:137 */     return this.ctPicture;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void resize()
/*  91:    */   {
/*  92:146 */     resize(1.7976931348623157E+308D);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void resize(double scale)
/*  96:    */   {
/*  97:155 */     resize(scale, scale);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void resize(double scaleX, double scaleY)
/* 101:    */   {
/* 102:178 */     XSSFClientAnchor anchor = getClientAnchor();
/* 103:    */     
/* 104:180 */     XSSFClientAnchor pref = getPreferredSize(scaleX, scaleY);
/* 105:    */     
/* 106:182 */     int row2 = anchor.getRow1() + (pref.getRow2() - pref.getRow1());
/* 107:183 */     int col2 = anchor.getCol1() + (pref.getCol2() - pref.getCol1());
/* 108:    */     
/* 109:185 */     anchor.setCol2(col2);
/* 110:    */     
/* 111:187 */     anchor.setDx2(pref.getDx2());
/* 112:    */     
/* 113:189 */     anchor.setRow2(row2);
/* 114:    */     
/* 115:191 */     anchor.setDy2(pref.getDy2());
/* 116:    */   }
/* 117:    */   
/* 118:    */   public XSSFClientAnchor getPreferredSize()
/* 119:    */   {
/* 120:200 */     return getPreferredSize(1.0D);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public XSSFClientAnchor getPreferredSize(double scale)
/* 124:    */   {
/* 125:210 */     return getPreferredSize(scale, scale);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public XSSFClientAnchor getPreferredSize(double scaleX, double scaleY)
/* 129:    */   {
/* 130:221 */     Dimension dim = ImageUtils.setPreferredSize(this, scaleX, scaleY);
/* 131:222 */     CTPositiveSize2D size2d = this.ctPicture.getSpPr().getXfrm().getExt();
/* 132:223 */     size2d.setCx((int)dim.getWidth());
/* 133:224 */     size2d.setCy((int)dim.getHeight());
/* 134:225 */     return getClientAnchor();
/* 135:    */   }
/* 136:    */   
/* 137:    */   protected static Dimension getImageDimension(PackagePart part, int type)
/* 138:    */   {
/* 139:    */     try
/* 140:    */     {
/* 141:239 */       return ImageUtils.getImageDimension(part.getInputStream(), type);
/* 142:    */     }
/* 143:    */     catch (IOException e)
/* 144:    */     {
/* 145:242 */       logger.log(5, new Object[] { e });
/* 146:    */     }
/* 147:243 */     return new Dimension();
/* 148:    */   }
/* 149:    */   
/* 150:    */   public Dimension getImageDimension()
/* 151:    */   {
/* 152:253 */     XSSFPictureData picData = getPictureData();
/* 153:254 */     return getImageDimension(picData.getPackagePart(), picData.getPictureType());
/* 154:    */   }
/* 155:    */   
/* 156:    */   public XSSFPictureData getPictureData()
/* 157:    */   {
/* 158:263 */     String blipId = this.ctPicture.getBlipFill().getBlip().getEmbed();
/* 159:264 */     return (XSSFPictureData)getDrawing().getRelationById(blipId);
/* 160:    */   }
/* 161:    */   
/* 162:    */   protected CTShapeProperties getShapeProperties()
/* 163:    */   {
/* 164:268 */     return this.ctPicture.getSpPr();
/* 165:    */   }
/* 166:    */   
/* 167:    */   public XSSFClientAnchor getClientAnchor()
/* 168:    */   {
/* 169:276 */     XSSFAnchor a = getAnchor();
/* 170:277 */     return (a instanceof XSSFClientAnchor) ? (XSSFClientAnchor)a : null;
/* 171:    */   }
/* 172:    */   
/* 173:    */   public XSSFSheet getSheet()
/* 174:    */   {
/* 175:285 */     return (XSSFSheet)getDrawing().getParent();
/* 176:    */   }
/* 177:    */   
/* 178:    */   public String getShapeName()
/* 179:    */   {
/* 180:290 */     return this.ctPicture.getNvPicPr().getCNvPr().getName();
/* 181:    */   }
/* 182:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFPicture
 * JD-Core Version:    0.7.0.1
 */