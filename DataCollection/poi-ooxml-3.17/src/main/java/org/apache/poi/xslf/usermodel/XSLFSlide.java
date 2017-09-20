/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Graphics2D;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.POIXMLDocumentPart;
/*   7:    */ import org.apache.poi.POIXMLTypeLoader;
/*   8:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   9:    */ import org.apache.poi.sl.draw.DrawFactory;
/*  10:    */ import org.apache.poi.sl.draw.Drawable;
/*  11:    */ import org.apache.poi.sl.usermodel.Notes;
/*  12:    */ import org.apache.poi.sl.usermodel.Placeholder;
/*  13:    */ import org.apache.poi.sl.usermodel.Slide;
/*  14:    */ import org.apache.poi.util.DocumentHelper;
/*  15:    */ import org.apache.poi.util.NotImplemented;
/*  16:    */ import org.apache.xmlbeans.XmlException;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlip;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupTransform2D;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
/*  24:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
/*  25:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTBackground;
/*  26:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTBackgroundProperties;
/*  27:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData;
/*  28:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
/*  29:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShapeNonVisual;
/*  30:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;
/*  31:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide.Factory;
/*  32:    */ import org.openxmlformats.schemas.presentationml.x2006.main.SldDocument;
/*  33:    */ import org.openxmlformats.schemas.presentationml.x2006.main.SldDocument.Factory;
/*  34:    */ import org.w3c.dom.Document;
/*  35:    */ import org.xml.sax.SAXException;
/*  36:    */ 
/*  37:    */ public final class XSLFSlide
/*  38:    */   extends XSLFSheet
/*  39:    */   implements Slide<XSLFShape, XSLFTextParagraph>
/*  40:    */ {
/*  41:    */   private final CTSlide _slide;
/*  42:    */   private XSLFSlideLayout _layout;
/*  43:    */   private XSLFComments _comments;
/*  44:    */   private XSLFNotes _notes;
/*  45:    */   
/*  46:    */   XSLFSlide()
/*  47:    */   {
/*  48: 62 */     this._slide = prototype();
/*  49: 63 */     setCommonSlideData(this._slide.getCSld());
/*  50:    */   }
/*  51:    */   
/*  52:    */   XSLFSlide(PackagePart part)
/*  53:    */     throws IOException, XmlException
/*  54:    */   {
/*  55: 75 */     super(part);
/*  56:    */     Document _doc;
/*  57:    */     try
/*  58:    */     {
/*  59: 79 */       _doc = DocumentHelper.readDocument(getPackagePart().getInputStream());
/*  60:    */     }
/*  61:    */     catch (SAXException e)
/*  62:    */     {
/*  63: 81 */       throw new IOException(e);
/*  64:    */     }
/*  65: 84 */     SldDocument doc = SldDocument.Factory.parse(_doc, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  66: 85 */     this._slide = doc.getSld();
/*  67: 86 */     setCommonSlideData(this._slide.getCSld());
/*  68:    */   }
/*  69:    */   
/*  70:    */   private static CTSlide prototype()
/*  71:    */   {
/*  72: 90 */     CTSlide ctSlide = CTSlide.Factory.newInstance();
/*  73: 91 */     CTCommonSlideData cSld = ctSlide.addNewCSld();
/*  74: 92 */     CTGroupShape spTree = cSld.addNewSpTree();
/*  75:    */     
/*  76: 94 */     CTGroupShapeNonVisual nvGrpSpPr = spTree.addNewNvGrpSpPr();
/*  77: 95 */     CTNonVisualDrawingProps cnvPr = nvGrpSpPr.addNewCNvPr();
/*  78: 96 */     cnvPr.setId(1L);
/*  79: 97 */     cnvPr.setName("");
/*  80: 98 */     nvGrpSpPr.addNewCNvGrpSpPr();
/*  81: 99 */     nvGrpSpPr.addNewNvPr();
/*  82:    */     
/*  83:101 */     CTGroupShapeProperties grpSpr = spTree.addNewGrpSpPr();
/*  84:102 */     CTGroupTransform2D xfrm = grpSpr.addNewXfrm();
/*  85:103 */     CTPoint2D off = xfrm.addNewOff();
/*  86:104 */     off.setX(0L);
/*  87:105 */     off.setY(0L);
/*  88:106 */     CTPositiveSize2D ext = xfrm.addNewExt();
/*  89:107 */     ext.setCx(0L);
/*  90:108 */     ext.setCy(0L);
/*  91:109 */     CTPoint2D choff = xfrm.addNewChOff();
/*  92:110 */     choff.setX(0L);
/*  93:111 */     choff.setY(0L);
/*  94:112 */     CTPositiveSize2D chExt = xfrm.addNewChExt();
/*  95:113 */     chExt.setCx(0L);
/*  96:114 */     chExt.setCy(0L);
/*  97:115 */     ctSlide.addNewClrMapOvr().addNewMasterClrMapping();
/*  98:116 */     return ctSlide;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public CTSlide getXmlObject()
/* 102:    */   {
/* 103:121 */     return this._slide;
/* 104:    */   }
/* 105:    */   
/* 106:    */   protected String getRootElementName()
/* 107:    */   {
/* 108:126 */     return "sld";
/* 109:    */   }
/* 110:    */   
/* 111:    */   public XSLFSlideLayout getMasterSheet()
/* 112:    */   {
/* 113:130 */     return getSlideLayout();
/* 114:    */   }
/* 115:    */   
/* 116:    */   public XSLFSlideLayout getSlideLayout()
/* 117:    */   {
/* 118:134 */     if (this._layout == null) {
/* 119:135 */       for (POIXMLDocumentPart p : getRelations()) {
/* 120:136 */         if ((p instanceof XSLFSlideLayout)) {
/* 121:137 */           this._layout = ((XSLFSlideLayout)p);
/* 122:    */         }
/* 123:    */       }
/* 124:    */     }
/* 125:141 */     if (this._layout == null) {
/* 126:142 */       throw new IllegalArgumentException("SlideLayout was not found for " + this);
/* 127:    */     }
/* 128:144 */     return this._layout;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public XSLFSlideMaster getSlideMaster()
/* 132:    */   {
/* 133:148 */     return getSlideLayout().getSlideMaster();
/* 134:    */   }
/* 135:    */   
/* 136:    */   public XSLFComments getComments()
/* 137:    */   {
/* 138:152 */     if (this._comments == null) {
/* 139:153 */       for (POIXMLDocumentPart p : getRelations()) {
/* 140:154 */         if ((p instanceof XSLFComments)) {
/* 141:155 */           this._comments = ((XSLFComments)p);
/* 142:    */         }
/* 143:    */       }
/* 144:    */     }
/* 145:159 */     if (this._comments == null) {
/* 146:162 */       return null;
/* 147:    */     }
/* 148:164 */     return this._comments;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public XSLFNotes getNotes()
/* 152:    */   {
/* 153:168 */     if (this._notes == null) {
/* 154:169 */       for (POIXMLDocumentPart p : getRelations()) {
/* 155:170 */         if ((p instanceof XSLFNotes)) {
/* 156:171 */           this._notes = ((XSLFNotes)p);
/* 157:    */         }
/* 158:    */       }
/* 159:    */     }
/* 160:175 */     if (this._notes == null) {
/* 161:178 */       return null;
/* 162:    */     }
/* 163:180 */     return this._notes;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public String getTitle()
/* 167:    */   {
/* 168:185 */     XSLFTextShape txt = getTextShapeByType(Placeholder.TITLE);
/* 169:186 */     return txt == null ? null : txt.getText();
/* 170:    */   }
/* 171:    */   
/* 172:    */   public XSLFTheme getTheme()
/* 173:    */   {
/* 174:191 */     return getSlideLayout().getSlideMaster().getTheme();
/* 175:    */   }
/* 176:    */   
/* 177:    */   public XSLFBackground getBackground()
/* 178:    */   {
/* 179:200 */     CTBackground bg = this._slide.getCSld().getBg();
/* 180:201 */     if (bg != null) {
/* 181:202 */       return new XSLFBackground(bg, this);
/* 182:    */     }
/* 183:204 */     return getMasterSheet().getBackground();
/* 184:    */   }
/* 185:    */   
/* 186:    */   public boolean getFollowMasterGraphics()
/* 187:    */   {
/* 188:210 */     return this._slide.getShowMasterSp();
/* 189:    */   }
/* 190:    */   
/* 191:    */   public void setFollowMasterGraphics(boolean value)
/* 192:    */   {
/* 193:218 */     this._slide.setShowMasterSp(value);
/* 194:    */   }
/* 195:    */   
/* 196:    */   public boolean getFollowMasterObjects()
/* 197:    */   {
/* 198:223 */     return getFollowMasterGraphics();
/* 199:    */   }
/* 200:    */   
/* 201:    */   public void setFollowMasterObjects(boolean follow)
/* 202:    */   {
/* 203:227 */     setFollowMasterGraphics(follow);
/* 204:    */   }
/* 205:    */   
/* 206:    */   public XSLFSlide importContent(XSLFSheet src)
/* 207:    */   {
/* 208:232 */     super.importContent(src);
/* 209:233 */     if (!(src instanceof XSLFSlide)) {
/* 210:234 */       return this;
/* 211:    */     }
/* 212:238 */     CTBackground bgOther = ((XSLFSlide)src)._slide.getCSld().getBg();
/* 213:239 */     if (bgOther == null) {
/* 214:240 */       return this;
/* 215:    */     }
/* 216:243 */     CTBackground bgThis = this._slide.getCSld().getBg();
/* 217:245 */     if (bgThis != null)
/* 218:    */     {
/* 219:246 */       if ((bgThis.isSetBgPr()) && (bgThis.getBgPr().isSetBlipFill()))
/* 220:    */       {
/* 221:247 */         String oldId = bgThis.getBgPr().getBlipFill().getBlip().getEmbed();
/* 222:248 */         removeRelation(getRelationById(oldId));
/* 223:    */       }
/* 224:250 */       this._slide.getCSld().unsetBg();
/* 225:    */     }
/* 226:253 */     bgThis = (CTBackground)this._slide.getCSld().addNewBg().set(bgOther);
/* 227:255 */     if ((bgOther.isSetBgPr()) && (bgOther.getBgPr().isSetBlipFill()))
/* 228:    */     {
/* 229:256 */       String idOther = bgOther.getBgPr().getBlipFill().getBlip().getEmbed();
/* 230:257 */       String idThis = importBlip(idOther, src.getPackagePart());
/* 231:258 */       bgThis.getBgPr().getBlipFill().getBlip().setEmbed(idThis);
/* 232:    */     }
/* 233:262 */     return this;
/* 234:    */   }
/* 235:    */   
/* 236:    */   public boolean getFollowMasterBackground()
/* 237:    */   {
/* 238:266 */     return false;
/* 239:    */   }
/* 240:    */   
/* 241:    */   @NotImplemented
/* 242:    */   public void setFollowMasterBackground(boolean follow)
/* 243:    */   {
/* 244:272 */     throw new UnsupportedOperationException();
/* 245:    */   }
/* 246:    */   
/* 247:    */   public boolean getFollowMasterColourScheme()
/* 248:    */   {
/* 249:276 */     return false;
/* 250:    */   }
/* 251:    */   
/* 252:    */   @NotImplemented
/* 253:    */   public void setFollowMasterColourScheme(boolean follow)
/* 254:    */   {
/* 255:282 */     throw new UnsupportedOperationException();
/* 256:    */   }
/* 257:    */   
/* 258:    */   @NotImplemented
/* 259:    */   public void setNotes(Notes<XSLFShape, XSLFTextParagraph> notes)
/* 260:    */   {
/* 261:288 */     assert ((notes instanceof XSLFNotes));
/* 262:    */   }
/* 263:    */   
/* 264:    */   public int getSlideNumber()
/* 265:    */   {
/* 266:294 */     int idx = getSlideShow().getSlides().indexOf(this);
/* 267:295 */     return idx == -1 ? idx : idx + 1;
/* 268:    */   }
/* 269:    */   
/* 270:    */   public void draw(Graphics2D graphics)
/* 271:    */   {
/* 272:305 */     DrawFactory drawFact = DrawFactory.getInstance(graphics);
/* 273:306 */     Drawable draw = drawFact.getDrawable(this);
/* 274:307 */     draw.draw(graphics);
/* 275:    */   }
/* 276:    */   
/* 277:    */   public boolean getDisplayPlaceholder(Placeholder placeholder)
/* 278:    */   {
/* 279:312 */     return false;
/* 280:    */   }
/* 281:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFSlide
 * JD-Core Version:    0.7.0.1
 */