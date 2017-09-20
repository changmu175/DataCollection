/*    1:     */ package org.apache.poi.xwpf.usermodel;
/*    2:     */ 
/*    3:     */ import java.io.ByteArrayOutputStream;
/*    4:     */ import java.io.IOException;
/*    5:     */ import java.io.InputStream;
/*    6:     */ import java.io.OutputStream;
/*    7:     */ import java.math.BigInteger;
/*    8:     */ import java.net.URI;
/*    9:     */ import java.util.ArrayList;
/*   10:     */ import java.util.Arrays;
/*   11:     */ import java.util.Collection;
/*   12:     */ import java.util.Collections;
/*   13:     */ import java.util.HashMap;
/*   14:     */ import java.util.Iterator;
/*   15:     */ import java.util.LinkedList;
/*   16:     */ import java.util.List;
/*   17:     */ import java.util.Map;
/*   18:     */ import javax.xml.namespace.QName;
/*   19:     */ import org.apache.poi.POIXMLDocument;
/*   20:     */ import org.apache.poi.POIXMLDocumentPart;
/*   21:     */ import org.apache.poi.POIXMLDocumentPart.RelationPart;
/*   22:     */ import org.apache.poi.POIXMLException;
/*   23:     */ import org.apache.poi.POIXMLProperties;
/*   24:     */ import org.apache.poi.POIXMLProperties.ExtendedProperties;
/*   25:     */ import org.apache.poi.POIXMLRelation;
/*   26:     */ import org.apache.poi.POIXMLTypeLoader;
/*   27:     */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*   28:     */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*   29:     */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*   30:     */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   31:     */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*   32:     */ import org.apache.poi.openxml4j.opc.PackageProperties;
/*   33:     */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*   34:     */ import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
/*   35:     */ import org.apache.poi.openxml4j.opc.PackagingURIHelper;
/*   36:     */ import org.apache.poi.openxml4j.opc.TargetMode;
/*   37:     */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*   38:     */ import org.apache.poi.util.IOUtils;
/*   39:     */ import org.apache.poi.util.IdentifierManager;
/*   40:     */ import org.apache.poi.util.Internal;
/*   41:     */ import org.apache.poi.util.POILogFactory;
/*   42:     */ import org.apache.poi.util.POILogger;
/*   43:     */ import org.apache.poi.util.PackageHelper;
/*   44:     */ import org.apache.poi.wp.usermodel.HeaderFooterType;
/*   45:     */ import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
/*   46:     */ import org.apache.xmlbeans.SchemaType;
/*   47:     */ import org.apache.xmlbeans.XmlCursor;
/*   48:     */ import org.apache.xmlbeans.XmlException;
/*   49:     */ import org.apache.xmlbeans.XmlObject;
/*   50:     */ import org.apache.xmlbeans.XmlOptions;
/*   51:     */ import org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTProperties;
/*   52:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
/*   53:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment;
/*   54:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComments;
/*   55:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1;
/*   56:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1.Factory;
/*   57:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEndnotes;
/*   58:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn;
/*   59:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
/*   60:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
/*   61:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
/*   62:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
/*   63:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
/*   64:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
/*   65:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
/*   66:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
/*   67:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CommentsDocument;
/*   68:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CommentsDocument.Factory;
/*   69:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.DocumentDocument;
/*   70:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.DocumentDocument.Factory;
/*   71:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.EndnotesDocument;
/*   72:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.EndnotesDocument.Factory;
/*   73:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.FootnotesDocument;
/*   74:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.FootnotesDocument.Factory;
/*   75:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.NumberingDocument;
/*   76:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.NumberingDocument.Factory;
/*   77:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocProtect;
/*   78:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr.Enum;
/*   79:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
/*   80:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.StylesDocument;
/*   81:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.StylesDocument.Factory;
/*   82:     */ 
/*   83:     */ public class XWPFDocument
/*   84:     */   extends POIXMLDocument
/*   85:     */   implements Document, IBody
/*   86:     */ {
/*   87:  81 */   private static final POILogger LOG = POILogFactory.getLogger(XWPFDocument.class);
/*   88:  83 */   protected List<XWPFFooter> footers = new ArrayList();
/*   89:  84 */   protected List<XWPFHeader> headers = new ArrayList();
/*   90:  85 */   protected List<XWPFComment> comments = new ArrayList();
/*   91:  86 */   protected List<XWPFHyperlink> hyperlinks = new ArrayList();
/*   92:  87 */   protected List<XWPFParagraph> paragraphs = new ArrayList();
/*   93:  88 */   protected List<XWPFTable> tables = new ArrayList();
/*   94:  89 */   protected List<XWPFSDT> contentControls = new ArrayList();
/*   95:  90 */   protected List<IBodyElement> bodyElements = new ArrayList();
/*   96:  91 */   protected List<XWPFPictureData> pictures = new ArrayList();
/*   97:  92 */   protected Map<Long, List<XWPFPictureData>> packagePictures = new HashMap();
/*   98:  93 */   protected Map<Integer, XWPFFootnote> endnotes = new HashMap();
/*   99:     */   protected XWPFNumbering numbering;
/*  100:     */   protected XWPFStyles styles;
/*  101:     */   protected XWPFFootnotes footnotes;
/*  102:     */   private CTDocument1 ctDocument;
/*  103:     */   private XWPFSettings settings;
/*  104: 102 */   private IdentifierManager drawingIdManager = new IdentifierManager(0L, 4294967295L);
/*  105:     */   private XWPFHeaderFooterPolicy headerFooterPolicy;
/*  106:     */   
/*  107:     */   public XWPFDocument(OPCPackage pkg)
/*  108:     */     throws IOException
/*  109:     */   {
/*  110: 109 */     super(pkg);
/*  111:     */     
/*  112:     */ 
/*  113: 112 */     load(XWPFFactory.getInstance());
/*  114:     */   }
/*  115:     */   
/*  116:     */   public XWPFDocument(InputStream is)
/*  117:     */     throws IOException
/*  118:     */   {
/*  119: 116 */     super(PackageHelper.open(is));
/*  120:     */     
/*  121:     */ 
/*  122: 119 */     load(XWPFFactory.getInstance());
/*  123:     */   }
/*  124:     */   
/*  125:     */   public XWPFDocument()
/*  126:     */   {
/*  127: 123 */     super(newPackage());
/*  128: 124 */     onDocumentCreate();
/*  129:     */   }
/*  130:     */   
/*  131:     */   protected static OPCPackage newPackage()
/*  132:     */   {
/*  133:     */     try
/*  134:     */     {
/*  135: 132 */       OPCPackage pkg = OPCPackage.create(new ByteArrayOutputStream());
/*  136:     */       
/*  137: 134 */       PackagePartName corePartName = PackagingURIHelper.createPartName(XWPFRelation.DOCUMENT.getDefaultFileName());
/*  138:     */       
/*  139: 136 */       pkg.addRelationship(corePartName, TargetMode.INTERNAL, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument");
/*  140:     */       
/*  141: 138 */       pkg.createPart(corePartName, XWPFRelation.DOCUMENT.getContentType());
/*  142:     */       
/*  143: 140 */       pkg.getPackageProperties().setCreatorProperty("Apache POI");
/*  144:     */       
/*  145: 142 */       return pkg;
/*  146:     */     }
/*  147:     */     catch (Exception e)
/*  148:     */     {
/*  149: 144 */       throw new POIXMLException(e);
/*  150:     */     }
/*  151:     */   }
/*  152:     */   
/*  153:     */   protected void onDocumentRead()
/*  154:     */     throws IOException
/*  155:     */   {
/*  156:     */     try
/*  157:     */     {
/*  158: 152 */       DocumentDocument doc = DocumentDocument.Factory.parse(getPackagePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  159: 153 */       this.ctDocument = doc.getDocument();
/*  160:     */       
/*  161: 155 */       initFootnotes();
/*  162:     */       
/*  163:     */ 
/*  164:     */ 
/*  165: 159 */       XmlCursor docCursor = this.ctDocument.newCursor();
/*  166: 160 */       docCursor.selectPath("./*");
/*  167: 161 */       while (docCursor.toNextSelection())
/*  168:     */       {
/*  169: 162 */         XmlObject o = docCursor.getObject();
/*  170: 163 */         if ((o instanceof CTBody))
/*  171:     */         {
/*  172: 164 */           XmlCursor bodyCursor = o.newCursor();
/*  173: 165 */           bodyCursor.selectPath("./*");
/*  174: 166 */           while (bodyCursor.toNextSelection())
/*  175:     */           {
/*  176: 167 */             XmlObject bodyObj = bodyCursor.getObject();
/*  177: 168 */             if ((bodyObj instanceof CTP))
/*  178:     */             {
/*  179: 169 */               XWPFParagraph p = new XWPFParagraph((CTP)bodyObj, this);
/*  180:     */               
/*  181: 171 */               this.bodyElements.add(p);
/*  182: 172 */               this.paragraphs.add(p);
/*  183:     */             }
/*  184: 173 */             else if ((bodyObj instanceof CTTbl))
/*  185:     */             {
/*  186: 174 */               XWPFTable t = new XWPFTable((CTTbl)bodyObj, this);
/*  187: 175 */               this.bodyElements.add(t);
/*  188: 176 */               this.tables.add(t);
/*  189:     */             }
/*  190: 177 */             else if ((bodyObj instanceof CTSdtBlock))
/*  191:     */             {
/*  192: 178 */               XWPFSDT c = new XWPFSDT((CTSdtBlock)bodyObj, this);
/*  193: 179 */               this.bodyElements.add(c);
/*  194: 180 */               this.contentControls.add(c);
/*  195:     */             }
/*  196:     */           }
/*  197: 183 */           bodyCursor.dispose();
/*  198:     */         }
/*  199:     */       }
/*  200: 186 */       docCursor.dispose();
/*  201: 188 */       if (doc.getDocument().getBody().getSectPr() != null) {
/*  202: 189 */         this.headerFooterPolicy = new XWPFHeaderFooterPolicy(this);
/*  203:     */       }
/*  204: 192 */       for (POIXMLDocumentPart.RelationPart rp : getRelationParts())
/*  205:     */       {
/*  206: 193 */         POIXMLDocumentPart p = rp.getDocumentPart();
/*  207: 194 */         String relation = rp.getRelationship().getRelationshipType();
/*  208: 195 */         if (relation.equals(XWPFRelation.STYLES.getRelation()))
/*  209:     */         {
/*  210: 196 */           this.styles = ((XWPFStyles)p);
/*  211: 197 */           this.styles.onDocumentRead();
/*  212:     */         }
/*  213: 198 */         else if (relation.equals(XWPFRelation.NUMBERING.getRelation()))
/*  214:     */         {
/*  215: 199 */           this.numbering = ((XWPFNumbering)p);
/*  216: 200 */           this.numbering.onDocumentRead();
/*  217:     */         }
/*  218: 201 */         else if (relation.equals(XWPFRelation.FOOTER.getRelation()))
/*  219:     */         {
/*  220: 202 */           XWPFFooter footer = (XWPFFooter)p;
/*  221: 203 */           this.footers.add(footer);
/*  222: 204 */           footer.onDocumentRead();
/*  223:     */         }
/*  224: 205 */         else if (relation.equals(XWPFRelation.HEADER.getRelation()))
/*  225:     */         {
/*  226: 206 */           XWPFHeader header = (XWPFHeader)p;
/*  227: 207 */           this.headers.add(header);
/*  228: 208 */           header.onDocumentRead();
/*  229:     */         }
/*  230: 209 */         else if (relation.equals(XWPFRelation.COMMENT.getRelation()))
/*  231:     */         {
/*  232: 211 */           CommentsDocument cmntdoc = CommentsDocument.Factory.parse(p.getPackagePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  233: 212 */           for (CTComment ctcomment : cmntdoc.getComments().getCommentArray()) {
/*  234: 213 */             this.comments.add(new XWPFComment(ctcomment, this));
/*  235:     */           }
/*  236:     */         }
/*  237: 215 */         else if (relation.equals(XWPFRelation.SETTINGS.getRelation()))
/*  238:     */         {
/*  239: 216 */           this.settings = ((XWPFSettings)p);
/*  240: 217 */           this.settings.onDocumentRead();
/*  241:     */         }
/*  242: 218 */         else if (relation.equals(XWPFRelation.IMAGES.getRelation()))
/*  243:     */         {
/*  244: 219 */           XWPFPictureData picData = (XWPFPictureData)p;
/*  245: 220 */           picData.onDocumentRead();
/*  246: 221 */           registerPackagePictureData(picData);
/*  247: 222 */           this.pictures.add(picData);
/*  248:     */         }
/*  249: 223 */         else if (relation.equals(XWPFRelation.GLOSSARY_DOCUMENT.getRelation()))
/*  250:     */         {
/*  251: 226 */           for (POIXMLDocumentPart gp : p.getRelations()) {
/*  252: 231 */             POIXMLDocumentPart._invokeOnDocumentRead(gp);
/*  253:     */           }
/*  254:     */         }
/*  255:     */       }
/*  256: 235 */       initHyperlinks();
/*  257:     */     }
/*  258:     */     catch (XmlException e)
/*  259:     */     {
/*  260: 237 */       throw new POIXMLException(e);
/*  261:     */     }
/*  262:     */   }
/*  263:     */   
/*  264:     */   private void initHyperlinks()
/*  265:     */   {
/*  266:     */     try
/*  267:     */     {
/*  268: 245 */       Iterator<PackageRelationship> relIter = getPackagePart().getRelationshipsByType(XWPFRelation.HYPERLINK.getRelation()).iterator();
/*  269: 247 */       while (relIter.hasNext())
/*  270:     */       {
/*  271: 248 */         PackageRelationship rel = (PackageRelationship)relIter.next();
/*  272: 249 */         this.hyperlinks.add(new XWPFHyperlink(rel.getId(), rel.getTargetURI().toString()));
/*  273:     */       }
/*  274:     */     }
/*  275:     */     catch (InvalidFormatException e)
/*  276:     */     {
/*  277: 252 */       throw new POIXMLException(e);
/*  278:     */     }
/*  279:     */   }
/*  280:     */   
/*  281:     */   private void initFootnotes()
/*  282:     */     throws XmlException, IOException
/*  283:     */   {
/*  284: 257 */     for (POIXMLDocumentPart.RelationPart rp : getRelationParts())
/*  285:     */     {
/*  286: 258 */       POIXMLDocumentPart p = rp.getDocumentPart();
/*  287: 259 */       String relation = rp.getRelationship().getRelationshipType();
/*  288: 260 */       if (relation.equals(XWPFRelation.FOOTNOTE.getRelation()))
/*  289:     */       {
/*  290: 261 */         this.footnotes = ((XWPFFootnotes)p);
/*  291: 262 */         this.footnotes.onDocumentRead();
/*  292:     */       }
/*  293: 263 */       else if (relation.equals(XWPFRelation.ENDNOTE.getRelation()))
/*  294:     */       {
/*  295: 264 */         EndnotesDocument endnotesDocument = EndnotesDocument.Factory.parse(p.getPackagePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  296: 266 */         for (CTFtnEdn ctFtnEdn : endnotesDocument.getEndnotes().getEndnoteArray()) {
/*  297: 267 */           this.endnotes.put(Integer.valueOf(ctFtnEdn.getId().intValue()), new XWPFFootnote(this, ctFtnEdn));
/*  298:     */         }
/*  299:     */       }
/*  300:     */     }
/*  301:     */   }
/*  302:     */   
/*  303:     */   protected void onDocumentCreate()
/*  304:     */   {
/*  305: 278 */     this.ctDocument = CTDocument1.Factory.newInstance();
/*  306: 279 */     this.ctDocument.addNewBody();
/*  307:     */     
/*  308: 281 */     this.settings = ((XWPFSettings)createRelationship(XWPFRelation.SETTINGS, XWPFFactory.getInstance()));
/*  309:     */     
/*  310: 283 */     POIXMLProperties.ExtendedProperties expProps = getProperties().getExtendedProperties();
/*  311: 284 */     expProps.getUnderlyingProperties().setApplication("Apache POI");
/*  312:     */   }
/*  313:     */   
/*  314:     */   @Internal
/*  315:     */   public CTDocument1 getDocument()
/*  316:     */   {
/*  317: 292 */     return this.ctDocument;
/*  318:     */   }
/*  319:     */   
/*  320:     */   IdentifierManager getDrawingIdManager()
/*  321:     */   {
/*  322: 296 */     return this.drawingIdManager;
/*  323:     */   }
/*  324:     */   
/*  325:     */   public List<IBodyElement> getBodyElements()
/*  326:     */   {
/*  327: 306 */     return Collections.unmodifiableList(this.bodyElements);
/*  328:     */   }
/*  329:     */   
/*  330:     */   public Iterator<IBodyElement> getBodyElementsIterator()
/*  331:     */   {
/*  332: 310 */     return this.bodyElements.iterator();
/*  333:     */   }
/*  334:     */   
/*  335:     */   public List<XWPFParagraph> getParagraphs()
/*  336:     */   {
/*  337: 318 */     return Collections.unmodifiableList(this.paragraphs);
/*  338:     */   }
/*  339:     */   
/*  340:     */   public List<XWPFTable> getTables()
/*  341:     */   {
/*  342: 326 */     return Collections.unmodifiableList(this.tables);
/*  343:     */   }
/*  344:     */   
/*  345:     */   public XWPFTable getTableArray(int pos)
/*  346:     */   {
/*  347: 334 */     if ((pos >= 0) && (pos < this.tables.size())) {
/*  348: 335 */       return (XWPFTable)this.tables.get(pos);
/*  349:     */     }
/*  350: 337 */     return null;
/*  351:     */   }
/*  352:     */   
/*  353:     */   public List<XWPFFooter> getFooterList()
/*  354:     */   {
/*  355: 344 */     return Collections.unmodifiableList(this.footers);
/*  356:     */   }
/*  357:     */   
/*  358:     */   public XWPFFooter getFooterArray(int pos)
/*  359:     */   {
/*  360: 348 */     if ((pos >= 0) && (pos < this.footers.size())) {
/*  361: 349 */       return (XWPFFooter)this.footers.get(pos);
/*  362:     */     }
/*  363: 351 */     return null;
/*  364:     */   }
/*  365:     */   
/*  366:     */   public List<XWPFHeader> getHeaderList()
/*  367:     */   {
/*  368: 358 */     return Collections.unmodifiableList(this.headers);
/*  369:     */   }
/*  370:     */   
/*  371:     */   public XWPFHeader getHeaderArray(int pos)
/*  372:     */   {
/*  373: 362 */     if ((pos >= 0) && (pos < this.headers.size())) {
/*  374: 363 */       return (XWPFHeader)this.headers.get(pos);
/*  375:     */     }
/*  376: 365 */     return null;
/*  377:     */   }
/*  378:     */   
/*  379:     */   public String getTblStyle(XWPFTable table)
/*  380:     */   {
/*  381: 369 */     return table.getStyleID();
/*  382:     */   }
/*  383:     */   
/*  384:     */   public XWPFHyperlink getHyperlinkByID(String id)
/*  385:     */   {
/*  386: 373 */     for (XWPFHyperlink link : this.hyperlinks) {
/*  387: 374 */       if (link.getId().equals(id)) {
/*  388: 375 */         return link;
/*  389:     */       }
/*  390:     */     }
/*  391: 378 */     return null;
/*  392:     */   }
/*  393:     */   
/*  394:     */   public XWPFFootnote getFootnoteByID(int id)
/*  395:     */   {
/*  396: 382 */     if (this.footnotes == null) {
/*  397: 382 */       return null;
/*  398:     */     }
/*  399: 383 */     return this.footnotes.getFootnoteById(id);
/*  400:     */   }
/*  401:     */   
/*  402:     */   public XWPFFootnote getEndnoteByID(int id)
/*  403:     */   {
/*  404: 387 */     if (this.endnotes == null) {
/*  405: 387 */       return null;
/*  406:     */     }
/*  407: 388 */     return (XWPFFootnote)this.endnotes.get(Integer.valueOf(id));
/*  408:     */   }
/*  409:     */   
/*  410:     */   public List<XWPFFootnote> getFootnotes()
/*  411:     */   {
/*  412: 392 */     if (this.footnotes == null) {
/*  413: 393 */       return Collections.emptyList();
/*  414:     */     }
/*  415: 395 */     return this.footnotes.getFootnotesList();
/*  416:     */   }
/*  417:     */   
/*  418:     */   public XWPFHyperlink[] getHyperlinks()
/*  419:     */   {
/*  420: 399 */     return (XWPFHyperlink[])this.hyperlinks.toArray(new XWPFHyperlink[this.hyperlinks.size()]);
/*  421:     */   }
/*  422:     */   
/*  423:     */   public XWPFComment getCommentByID(String id)
/*  424:     */   {
/*  425: 403 */     for (XWPFComment comment : this.comments) {
/*  426: 404 */       if (comment.getId().equals(id)) {
/*  427: 405 */         return comment;
/*  428:     */       }
/*  429:     */     }
/*  430: 408 */     return null;
/*  431:     */   }
/*  432:     */   
/*  433:     */   public XWPFComment[] getComments()
/*  434:     */   {
/*  435: 412 */     return (XWPFComment[])this.comments.toArray(new XWPFComment[this.comments.size()]);
/*  436:     */   }
/*  437:     */   
/*  438:     */   public PackagePart getPartById(String id)
/*  439:     */   {
/*  440:     */     try
/*  441:     */     {
/*  442: 421 */       PackagePart corePart = getCorePart();
/*  443: 422 */       return corePart.getRelatedPart(corePart.getRelationship(id));
/*  444:     */     }
/*  445:     */     catch (InvalidFormatException e)
/*  446:     */     {
/*  447: 424 */       throw new IllegalArgumentException(e);
/*  448:     */     }
/*  449:     */   }
/*  450:     */   
/*  451:     */   public XWPFHeaderFooterPolicy getHeaderFooterPolicy()
/*  452:     */   {
/*  453: 433 */     return this.headerFooterPolicy;
/*  454:     */   }
/*  455:     */   
/*  456:     */   public XWPFHeaderFooterPolicy createHeaderFooterPolicy()
/*  457:     */   {
/*  458: 436 */     if (this.headerFooterPolicy == null) {
/*  459: 440 */       this.headerFooterPolicy = new XWPFHeaderFooterPolicy(this);
/*  460:     */     }
/*  461: 442 */     return this.headerFooterPolicy;
/*  462:     */   }
/*  463:     */   
/*  464:     */   public XWPFHeader createHeader(HeaderFooterType type)
/*  465:     */   {
/*  466: 452 */     XWPFHeaderFooterPolicy hfPolicy = createHeaderFooterPolicy();
/*  467: 454 */     if (type == HeaderFooterType.FIRST)
/*  468:     */     {
/*  469: 455 */       CTSectPr ctSectPr = getSection();
/*  470: 456 */       if (!ctSectPr.isSetTitlePg())
/*  471:     */       {
/*  472: 457 */         CTOnOff titlePg = ctSectPr.addNewTitlePg();
/*  473: 458 */         titlePg.setVal(STOnOff.ON);
/*  474:     */       }
/*  475:     */     }
/*  476: 463 */     return hfPolicy.createHeader(STHdrFtr.Enum.forInt(type.toInt()));
/*  477:     */   }
/*  478:     */   
/*  479:     */   public XWPFFooter createFooter(HeaderFooterType type)
/*  480:     */   {
/*  481: 474 */     XWPFHeaderFooterPolicy hfPolicy = createHeaderFooterPolicy();
/*  482: 476 */     if (type == HeaderFooterType.FIRST)
/*  483:     */     {
/*  484: 477 */       CTSectPr ctSectPr = getSection();
/*  485: 478 */       if (!ctSectPr.isSetTitlePg())
/*  486:     */       {
/*  487: 479 */         CTOnOff titlePg = ctSectPr.addNewTitlePg();
/*  488: 480 */         titlePg.setVal(STOnOff.ON);
/*  489:     */       }
/*  490:     */     }
/*  491: 485 */     return hfPolicy.createFooter(STHdrFtr.Enum.forInt(type.toInt()));
/*  492:     */   }
/*  493:     */   
/*  494:     */   private CTSectPr getSection()
/*  495:     */   {
/*  496: 495 */     CTBody ctBody = getDocument().getBody();
/*  497: 496 */     return ctBody.isSetSectPr() ? ctBody.getSectPr() : ctBody.addNewSectPr();
/*  498:     */   }
/*  499:     */   
/*  500:     */   @Internal
/*  501:     */   public CTStyles getStyle()
/*  502:     */     throws XmlException, IOException
/*  503:     */   {
/*  504:     */     PackagePart[] parts;
/*  505:     */     try
/*  506:     */     {
/*  507: 508 */       parts = getRelatedByType(XWPFRelation.STYLES.getRelation());
/*  508:     */     }
/*  509:     */     catch (InvalidFormatException e)
/*  510:     */     {
/*  511: 510 */       throw new IllegalStateException(e);
/*  512:     */     }
/*  513: 512 */     if (parts.length != 1) {
/*  514: 513 */       throw new IllegalStateException("Expecting one Styles document part, but found " + parts.length);
/*  515:     */     }
/*  516: 516 */     StylesDocument sd = StylesDocument.Factory.parse(parts[0].getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  517: 517 */     return sd.getStyles();
/*  518:     */   }
/*  519:     */   
/*  520:     */   public List<PackagePart> getAllEmbedds()
/*  521:     */     throws OpenXML4JException
/*  522:     */   {
/*  523: 525 */     List<PackagePart> embedds = new LinkedList();
/*  524:     */     
/*  525:     */ 
/*  526: 528 */     PackagePart part = getPackagePart();
/*  527: 529 */     for (PackageRelationship rel : getPackagePart().getRelationshipsByType("http://schemas.openxmlformats.org/officeDocument/2006/relationships/oleObject")) {
/*  528: 530 */       embedds.add(part.getRelatedPart(rel));
/*  529:     */     }
/*  530: 533 */     for (PackageRelationship rel : getPackagePart().getRelationshipsByType("http://schemas.openxmlformats.org/officeDocument/2006/relationships/package")) {
/*  531: 534 */       embedds.add(part.getRelatedPart(rel));
/*  532:     */     }
/*  533: 537 */     return embedds;
/*  534:     */   }
/*  535:     */   
/*  536:     */   private int getBodyElementSpecificPos(int pos, List<? extends IBodyElement> list)
/*  537:     */   {
/*  538: 545 */     if (list.size() == 0) {
/*  539: 546 */       return -1;
/*  540:     */     }
/*  541: 549 */     if ((pos >= 0) && (pos < this.bodyElements.size()))
/*  542:     */     {
/*  543: 551 */       IBodyElement needle = (IBodyElement)this.bodyElements.get(pos);
/*  544: 552 */       if (needle.getElementType() != ((IBodyElement)list.get(0)).getElementType()) {
/*  545: 554 */         return -1;
/*  546:     */       }
/*  547: 558 */       int startPos = Math.min(pos, list.size() - 1);
/*  548: 559 */       for (int i = startPos; i >= 0; i--) {
/*  549: 560 */         if (list.get(i) == needle) {
/*  550: 561 */           return i;
/*  551:     */         }
/*  552:     */       }
/*  553:     */     }
/*  554: 567 */     return -1;
/*  555:     */   }
/*  556:     */   
/*  557:     */   public int getParagraphPos(int pos)
/*  558:     */   {
/*  559: 581 */     return getBodyElementSpecificPos(pos, this.paragraphs);
/*  560:     */   }
/*  561:     */   
/*  562:     */   public int getTablePos(int pos)
/*  563:     */   {
/*  564: 593 */     return getBodyElementSpecificPos(pos, this.tables);
/*  565:     */   }
/*  566:     */   
/*  567:     */   public XWPFParagraph insertNewParagraph(XmlCursor cursor)
/*  568:     */   {
/*  569: 609 */     if (isCursorInBody(cursor))
/*  570:     */     {
/*  571: 610 */       String uri = CTP.type.getName().getNamespaceURI();
/*  572:     */       
/*  573:     */ 
/*  574:     */ 
/*  575:     */ 
/*  576:     */ 
/*  577: 616 */       String localPart = "p";
/*  578:     */       
/*  579:     */ 
/*  580: 619 */       cursor.beginElement(localPart, uri);
/*  581:     */       
/*  582: 621 */       cursor.toParent();
/*  583: 622 */       CTP p = (CTP)cursor.getObject();
/*  584: 623 */       XWPFParagraph newP = new XWPFParagraph(p, this);
/*  585: 624 */       XmlObject o = null;
/*  586: 629 */       while ((!(o instanceof CTP)) && (cursor.toPrevSibling())) {
/*  587: 630 */         o = cursor.getObject();
/*  588:     */       }
/*  589: 640 */       if ((!(o instanceof CTP)) || ((CTP)o == p))
/*  590:     */       {
/*  591: 641 */         this.paragraphs.add(0, newP);
/*  592:     */       }
/*  593:     */       else
/*  594:     */       {
/*  595: 643 */         int pos = this.paragraphs.indexOf(getParagraph((CTP)o)) + 1;
/*  596: 644 */         this.paragraphs.add(pos, newP);
/*  597:     */       }
/*  598: 651 */       XmlCursor newParaPos = p.newCursor();
/*  599:     */       try
/*  600:     */       {
/*  601: 657 */         int i = 0;
/*  602: 658 */         cursor.toCursor(newParaPos);
/*  603: 659 */         while (cursor.toPrevSibling())
/*  604:     */         {
/*  605: 660 */           o = cursor.getObject();
/*  606: 661 */           if (((o instanceof CTP)) || ((o instanceof CTTbl))) {
/*  607: 662 */             i++;
/*  608:     */           }
/*  609:     */         }
/*  610: 664 */         this.bodyElements.add(i, newP);
/*  611: 665 */         cursor.toCursor(newParaPos);
/*  612: 666 */         cursor.toEndToken();
/*  613: 667 */         return newP;
/*  614:     */       }
/*  615:     */       finally
/*  616:     */       {
/*  617: 669 */         newParaPos.dispose();
/*  618:     */       }
/*  619:     */     }
/*  620: 672 */     return null;
/*  621:     */   }
/*  622:     */   
/*  623:     */   public XWPFTable insertNewTbl(XmlCursor cursor)
/*  624:     */   {
/*  625: 677 */     if (isCursorInBody(cursor))
/*  626:     */     {
/*  627: 678 */       String uri = CTTbl.type.getName().getNamespaceURI();
/*  628: 679 */       String localPart = "tbl";
/*  629: 680 */       cursor.beginElement(localPart, uri);
/*  630: 681 */       cursor.toParent();
/*  631: 682 */       CTTbl t = (CTTbl)cursor.getObject();
/*  632: 683 */       XWPFTable newT = new XWPFTable(t, this);
/*  633: 684 */       XmlObject o = null;
/*  634: 685 */       while ((!(o instanceof CTTbl)) && (cursor.toPrevSibling())) {
/*  635: 686 */         o = cursor.getObject();
/*  636:     */       }
/*  637: 688 */       if (!(o instanceof CTTbl))
/*  638:     */       {
/*  639: 689 */         this.tables.add(0, newT);
/*  640:     */       }
/*  641:     */       else
/*  642:     */       {
/*  643: 691 */         int pos = this.tables.indexOf(getTable((CTTbl)o)) + 1;
/*  644: 692 */         this.tables.add(pos, newT);
/*  645:     */       }
/*  646: 694 */       int i = 0;
/*  647: 695 */       XmlCursor tableCursor = t.newCursor();
/*  648:     */       try
/*  649:     */       {
/*  650: 697 */         cursor.toCursor(tableCursor);
/*  651: 698 */         while (cursor.toPrevSibling())
/*  652:     */         {
/*  653: 699 */           o = cursor.getObject();
/*  654: 700 */           if (((o instanceof CTP)) || ((o instanceof CTTbl))) {
/*  655: 701 */             i++;
/*  656:     */           }
/*  657:     */         }
/*  658: 703 */         this.bodyElements.add(i, newT);
/*  659: 704 */         cursor.toCursor(tableCursor);
/*  660: 705 */         cursor.toEndToken();
/*  661: 706 */         return newT;
/*  662:     */       }
/*  663:     */       finally
/*  664:     */       {
/*  665: 708 */         tableCursor.dispose();
/*  666:     */       }
/*  667:     */     }
/*  668: 711 */     return null;
/*  669:     */   }
/*  670:     */   
/*  671:     */   private boolean isCursorInBody(XmlCursor cursor)
/*  672:     */   {
/*  673: 720 */     XmlCursor verify = cursor.newCursor();
/*  674: 721 */     verify.toParent();
/*  675: 722 */     boolean result = verify.getObject() == this.ctDocument.getBody();
/*  676: 723 */     verify.dispose();
/*  677: 724 */     return result;
/*  678:     */   }
/*  679:     */   
/*  680:     */   private int getPosOfBodyElement(IBodyElement needle)
/*  681:     */   {
/*  682: 728 */     BodyElementType type = needle.getElementType();
/*  683: 730 */     for (int i = 0; i < this.bodyElements.size(); i++)
/*  684:     */     {
/*  685: 731 */       IBodyElement current = (IBodyElement)this.bodyElements.get(i);
/*  686: 732 */       if ((current.getElementType() == type) && 
/*  687: 733 */         (current.equals(needle))) {
/*  688: 734 */         return i;
/*  689:     */       }
/*  690:     */     }
/*  691: 738 */     return -1;
/*  692:     */   }
/*  693:     */   
/*  694:     */   public int getPosOfParagraph(XWPFParagraph p)
/*  695:     */   {
/*  696: 749 */     return getPosOfBodyElement(p);
/*  697:     */   }
/*  698:     */   
/*  699:     */   public int getPosOfTable(XWPFTable t)
/*  700:     */   {
/*  701: 760 */     return getPosOfBodyElement(t);
/*  702:     */   }
/*  703:     */   
/*  704:     */   protected void commit()
/*  705:     */     throws IOException
/*  706:     */   {
/*  707: 768 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  708: 769 */     xmlOptions.setSaveSyntheticDocumentElement(new QName(CTDocument1.type.getName().getNamespaceURI(), "document"));
/*  709:     */     
/*  710: 771 */     PackagePart part = getPackagePart();
/*  711: 772 */     OutputStream out = part.getOutputStream();
/*  712: 773 */     this.ctDocument.save(out, xmlOptions);
/*  713: 774 */     out.close();
/*  714:     */   }
/*  715:     */   
/*  716:     */   private int getRelationIndex(XWPFRelation relation)
/*  717:     */   {
/*  718: 784 */     int i = 1;
/*  719: 785 */     for (POIXMLDocumentPart.RelationPart rp : getRelationParts()) {
/*  720: 786 */       if (rp.getRelationship().getRelationshipType().equals(relation.getRelation())) {
/*  721: 787 */         i++;
/*  722:     */       }
/*  723:     */     }
/*  724: 790 */     return i;
/*  725:     */   }
/*  726:     */   
/*  727:     */   public XWPFParagraph createParagraph()
/*  728:     */   {
/*  729: 799 */     XWPFParagraph p = new XWPFParagraph(this.ctDocument.getBody().addNewP(), this);
/*  730: 800 */     this.bodyElements.add(p);
/*  731: 801 */     this.paragraphs.add(p);
/*  732: 802 */     return p;
/*  733:     */   }
/*  734:     */   
/*  735:     */   public XWPFNumbering createNumbering()
/*  736:     */   {
/*  737: 811 */     if (this.numbering == null)
/*  738:     */     {
/*  739: 812 */       NumberingDocument numberingDoc = NumberingDocument.Factory.newInstance();
/*  740:     */       
/*  741: 814 */       XWPFRelation relation = XWPFRelation.NUMBERING;
/*  742: 815 */       int i = getRelationIndex(relation);
/*  743:     */       
/*  744: 817 */       XWPFNumbering wrapper = (XWPFNumbering)createRelationship(relation, XWPFFactory.getInstance(), i);
/*  745: 818 */       wrapper.setNumbering(numberingDoc.addNewNumbering());
/*  746: 819 */       this.numbering = wrapper;
/*  747:     */     }
/*  748: 822 */     return this.numbering;
/*  749:     */   }
/*  750:     */   
/*  751:     */   public XWPFStyles createStyles()
/*  752:     */   {
/*  753: 831 */     if (this.styles == null)
/*  754:     */     {
/*  755: 832 */       StylesDocument stylesDoc = StylesDocument.Factory.newInstance();
/*  756:     */       
/*  757: 834 */       XWPFRelation relation = XWPFRelation.STYLES;
/*  758: 835 */       int i = getRelationIndex(relation);
/*  759:     */       
/*  760: 837 */       XWPFStyles wrapper = (XWPFStyles)createRelationship(relation, XWPFFactory.getInstance(), i);
/*  761: 838 */       wrapper.setStyles(stylesDoc.addNewStyles());
/*  762: 839 */       this.styles = wrapper;
/*  763:     */     }
/*  764: 842 */     return this.styles;
/*  765:     */   }
/*  766:     */   
/*  767:     */   public XWPFFootnotes createFootnotes()
/*  768:     */   {
/*  769: 851 */     if (this.footnotes == null)
/*  770:     */     {
/*  771: 852 */       FootnotesDocument footnotesDoc = FootnotesDocument.Factory.newInstance();
/*  772:     */       
/*  773: 854 */       XWPFRelation relation = XWPFRelation.FOOTNOTE;
/*  774: 855 */       int i = getRelationIndex(relation);
/*  775:     */       
/*  776: 857 */       XWPFFootnotes wrapper = (XWPFFootnotes)createRelationship(relation, XWPFFactory.getInstance(), i);
/*  777: 858 */       wrapper.setFootnotes(footnotesDoc.addNewFootnotes());
/*  778: 859 */       this.footnotes = wrapper;
/*  779:     */     }
/*  780: 862 */     return this.footnotes;
/*  781:     */   }
/*  782:     */   
/*  783:     */   public XWPFFootnote addFootnote(CTFtnEdn note)
/*  784:     */   {
/*  785: 866 */     return this.footnotes.addFootnote(note);
/*  786:     */   }
/*  787:     */   
/*  788:     */   public XWPFFootnote addEndnote(CTFtnEdn note)
/*  789:     */   {
/*  790: 870 */     XWPFFootnote endnote = new XWPFFootnote(this, note);
/*  791: 871 */     this.endnotes.put(Integer.valueOf(note.getId().intValue()), endnote);
/*  792: 872 */     return endnote;
/*  793:     */   }
/*  794:     */   
/*  795:     */   public boolean removeBodyElement(int pos)
/*  796:     */   {
/*  797: 882 */     if ((pos >= 0) && (pos < this.bodyElements.size()))
/*  798:     */     {
/*  799: 883 */       BodyElementType type = ((IBodyElement)this.bodyElements.get(pos)).getElementType();
/*  800: 884 */       if (type == BodyElementType.TABLE)
/*  801:     */       {
/*  802: 885 */         int tablePos = getTablePos(pos);
/*  803: 886 */         this.tables.remove(tablePos);
/*  804: 887 */         this.ctDocument.getBody().removeTbl(tablePos);
/*  805:     */       }
/*  806: 889 */       if (type == BodyElementType.PARAGRAPH)
/*  807:     */       {
/*  808: 890 */         int paraPos = getParagraphPos(pos);
/*  809: 891 */         this.paragraphs.remove(paraPos);
/*  810: 892 */         this.ctDocument.getBody().removeP(paraPos);
/*  811:     */       }
/*  812: 894 */       this.bodyElements.remove(pos);
/*  813: 895 */       return true;
/*  814:     */     }
/*  815: 897 */     return false;
/*  816:     */   }
/*  817:     */   
/*  818:     */   public void setParagraph(XWPFParagraph paragraph, int pos)
/*  819:     */   {
/*  820: 907 */     this.paragraphs.set(pos, paragraph);
/*  821: 908 */     this.ctDocument.getBody().setPArray(pos, paragraph.getCTP());
/*  822:     */   }
/*  823:     */   
/*  824:     */   public XWPFParagraph getLastParagraph()
/*  825:     */   {
/*  826: 919 */     int lastPos = this.paragraphs.toArray().length - 1;
/*  827: 920 */     return (XWPFParagraph)this.paragraphs.get(lastPos);
/*  828:     */   }
/*  829:     */   
/*  830:     */   public XWPFTable createTable()
/*  831:     */   {
/*  832: 929 */     XWPFTable table = new XWPFTable(this.ctDocument.getBody().addNewTbl(), this);
/*  833: 930 */     this.bodyElements.add(table);
/*  834: 931 */     this.tables.add(table);
/*  835: 932 */     return table;
/*  836:     */   }
/*  837:     */   
/*  838:     */   public XWPFTable createTable(int rows, int cols)
/*  839:     */   {
/*  840: 943 */     XWPFTable table = new XWPFTable(this.ctDocument.getBody().addNewTbl(), this, rows, cols);
/*  841: 944 */     this.bodyElements.add(table);
/*  842: 945 */     this.tables.add(table);
/*  843: 946 */     return table;
/*  844:     */   }
/*  845:     */   
/*  846:     */   public void createTOC()
/*  847:     */   {
/*  848: 953 */     CTSdtBlock block = getDocument().getBody().addNewSdt();
/*  849: 954 */     TOC toc = new TOC(block);
/*  850: 955 */     for (XWPFParagraph par : this.paragraphs)
/*  851:     */     {
/*  852: 956 */       String parStyle = par.getStyle();
/*  853: 957 */       if ((parStyle != null) && (parStyle.startsWith("Heading"))) {
/*  854:     */         try
/*  855:     */         {
/*  856: 959 */           int level = Integer.parseInt(parStyle.substring("Heading".length()));
/*  857: 960 */           toc.addRow(level, par.getText(), 1, "112723803");
/*  858:     */         }
/*  859:     */         catch (NumberFormatException e)
/*  860:     */         {
/*  861: 962 */           LOG.log(7, new Object[] { "can't format number in TOC heading", e });
/*  862:     */         }
/*  863:     */       }
/*  864:     */     }
/*  865:     */   }
/*  866:     */   
/*  867:     */   public void setTable(int pos, XWPFTable table)
/*  868:     */   {
/*  869: 975 */     this.tables.set(pos, table);
/*  870: 976 */     this.ctDocument.getBody().setTblArray(pos, table.getCTTbl());
/*  871:     */   }
/*  872:     */   
/*  873:     */   public boolean isEnforcedProtection()
/*  874:     */   {
/*  875: 992 */     return this.settings.isEnforcedWith();
/*  876:     */   }
/*  877:     */   
/*  878:     */   public boolean isEnforcedReadonlyProtection()
/*  879:     */   {
/*  880:1009 */     return this.settings.isEnforcedWith(STDocProtect.READ_ONLY);
/*  881:     */   }
/*  882:     */   
/*  883:     */   public boolean isEnforcedFillingFormsProtection()
/*  884:     */   {
/*  885:1026 */     return this.settings.isEnforcedWith(STDocProtect.FORMS);
/*  886:     */   }
/*  887:     */   
/*  888:     */   public boolean isEnforcedCommentsProtection()
/*  889:     */   {
/*  890:1043 */     return this.settings.isEnforcedWith(STDocProtect.COMMENTS);
/*  891:     */   }
/*  892:     */   
/*  893:     */   public boolean isEnforcedTrackedChangesProtection()
/*  894:     */   {
/*  895:1060 */     return this.settings.isEnforcedWith(STDocProtect.TRACKED_CHANGES);
/*  896:     */   }
/*  897:     */   
/*  898:     */   public boolean isEnforcedUpdateFields()
/*  899:     */   {
/*  900:1064 */     return this.settings.isUpdateFields();
/*  901:     */   }
/*  902:     */   
/*  903:     */   public void enforceReadonlyProtection()
/*  904:     */   {
/*  905:1080 */     this.settings.setEnforcementEditValue(STDocProtect.READ_ONLY);
/*  906:     */   }
/*  907:     */   
/*  908:     */   public void enforceReadonlyProtection(String password, HashAlgorithm hashAlgo)
/*  909:     */   {
/*  910:1100 */     this.settings.setEnforcementEditValue(STDocProtect.READ_ONLY, password, hashAlgo);
/*  911:     */   }
/*  912:     */   
/*  913:     */   public void enforceFillingFormsProtection()
/*  914:     */   {
/*  915:1116 */     this.settings.setEnforcementEditValue(STDocProtect.FORMS);
/*  916:     */   }
/*  917:     */   
/*  918:     */   public void enforceFillingFormsProtection(String password, HashAlgorithm hashAlgo)
/*  919:     */   {
/*  920:1136 */     this.settings.setEnforcementEditValue(STDocProtect.FORMS, password, hashAlgo);
/*  921:     */   }
/*  922:     */   
/*  923:     */   public void enforceCommentsProtection()
/*  924:     */   {
/*  925:1152 */     this.settings.setEnforcementEditValue(STDocProtect.COMMENTS);
/*  926:     */   }
/*  927:     */   
/*  928:     */   public void enforceCommentsProtection(String password, HashAlgorithm hashAlgo)
/*  929:     */   {
/*  930:1172 */     this.settings.setEnforcementEditValue(STDocProtect.COMMENTS, password, hashAlgo);
/*  931:     */   }
/*  932:     */   
/*  933:     */   public void enforceTrackedChangesProtection()
/*  934:     */   {
/*  935:1188 */     this.settings.setEnforcementEditValue(STDocProtect.TRACKED_CHANGES);
/*  936:     */   }
/*  937:     */   
/*  938:     */   public void enforceTrackedChangesProtection(String password, HashAlgorithm hashAlgo)
/*  939:     */   {
/*  940:1208 */     this.settings.setEnforcementEditValue(STDocProtect.TRACKED_CHANGES, password, hashAlgo);
/*  941:     */   }
/*  942:     */   
/*  943:     */   public boolean validateProtectionPassword(String password)
/*  944:     */   {
/*  945:1218 */     return this.settings.validateProtectionPassword(password);
/*  946:     */   }
/*  947:     */   
/*  948:     */   public void removeProtectionEnforcement()
/*  949:     */   {
/*  950:1227 */     this.settings.removeEnforcement();
/*  951:     */   }
/*  952:     */   
/*  953:     */   public void enforceUpdateFields()
/*  954:     */   {
/*  955:1243 */     this.settings.setUpdateFields();
/*  956:     */   }
/*  957:     */   
/*  958:     */   public boolean isTrackRevisions()
/*  959:     */   {
/*  960:1252 */     return this.settings.isTrackRevisions();
/*  961:     */   }
/*  962:     */   
/*  963:     */   public void setTrackRevisions(boolean enable)
/*  964:     */   {
/*  965:1261 */     this.settings.setTrackRevisions(enable);
/*  966:     */   }
/*  967:     */   
/*  968:     */   public long getZoomPercent()
/*  969:     */   {
/*  970:1271 */     return this.settings.getZoomPercent();
/*  971:     */   }
/*  972:     */   
/*  973:     */   public void setZoomPercent(long zoomPercent)
/*  974:     */   {
/*  975:1280 */     this.settings.setZoomPercent(zoomPercent);
/*  976:     */   }
/*  977:     */   
/*  978:     */   public void insertTable(int pos, XWPFTable table)
/*  979:     */   {
/*  980:1291 */     this.bodyElements.add(pos, table);
/*  981:1292 */     int i = 0;
/*  982:1293 */     for (CTTbl tbl : this.ctDocument.getBody().getTblArray())
/*  983:     */     {
/*  984:1294 */       if (tbl == table.getCTTbl()) {
/*  985:     */         break;
/*  986:     */       }
/*  987:1297 */       i++;
/*  988:     */     }
/*  989:1299 */     this.tables.add(i, table);
/*  990:     */   }
/*  991:     */   
/*  992:     */   public List<XWPFPictureData> getAllPictures()
/*  993:     */   {
/*  994:1308 */     return Collections.unmodifiableList(this.pictures);
/*  995:     */   }
/*  996:     */   
/*  997:     */   public List<XWPFPictureData> getAllPackagePictures()
/*  998:     */   {
/*  999:1315 */     List<XWPFPictureData> result = new ArrayList();
/* 1000:1316 */     Collection<List<XWPFPictureData>> values = this.packagePictures.values();
/* 1001:1317 */     for (List<XWPFPictureData> list : values) {
/* 1002:1318 */       result.addAll(list);
/* 1003:     */     }
/* 1004:1320 */     return Collections.unmodifiableList(result);
/* 1005:     */   }
/* 1006:     */   
/* 1007:     */   void registerPackagePictureData(XWPFPictureData picData)
/* 1008:     */   {
/* 1009:1324 */     List<XWPFPictureData> list = (List)this.packagePictures.get(picData.getChecksum());
/* 1010:1325 */     if (list == null)
/* 1011:     */     {
/* 1012:1326 */       list = new ArrayList(1);
/* 1013:1327 */       this.packagePictures.put(picData.getChecksum(), list);
/* 1014:     */     }
/* 1015:1329 */     if (!list.contains(picData)) {
/* 1016:1330 */       list.add(picData);
/* 1017:     */     }
/* 1018:     */   }
/* 1019:     */   
/* 1020:     */   XWPFPictureData findPackagePictureData(byte[] pictureData, int format)
/* 1021:     */   {
/* 1022:1335 */     long checksum = IOUtils.calculateChecksum(pictureData);
/* 1023:1336 */     XWPFPictureData xwpfPicData = null;
/* 1024:     */     
/* 1025:     */ 
/* 1026:     */ 
/* 1027:     */ 
/* 1028:1341 */     List<XWPFPictureData> xwpfPicDataList = (List)this.packagePictures.get(Long.valueOf(checksum));
/* 1029:1342 */     if (xwpfPicDataList != null)
/* 1030:     */     {
/* 1031:1343 */       Iterator<XWPFPictureData> iter = xwpfPicDataList.iterator();
/* 1032:1344 */       while ((iter.hasNext()) && (xwpfPicData == null))
/* 1033:     */       {
/* 1034:1345 */         XWPFPictureData curElem = (XWPFPictureData)iter.next();
/* 1035:1346 */         if (Arrays.equals(pictureData, curElem.getData())) {
/* 1036:1347 */           xwpfPicData = curElem;
/* 1037:     */         }
/* 1038:     */       }
/* 1039:     */     }
/* 1040:1351 */     return xwpfPicData;
/* 1041:     */   }
/* 1042:     */   
/* 1043:     */   public String addPictureData(byte[] pictureData, int format)
/* 1044:     */     throws InvalidFormatException
/* 1045:     */   {
/* 1046:1355 */     XWPFPictureData xwpfPicData = findPackagePictureData(pictureData, format);
/* 1047:1356 */     POIXMLRelation relDesc = XWPFPictureData.RELATIONS[format];
/* 1048:1358 */     if (xwpfPicData == null)
/* 1049:     */     {
/* 1050:1360 */       int idx = getNextPicNameNumber(format);
/* 1051:1361 */       xwpfPicData = (XWPFPictureData)createRelationship(relDesc, XWPFFactory.getInstance(), idx);
/* 1052:     */       
/* 1053:1363 */       PackagePart picDataPart = xwpfPicData.getPackagePart();
/* 1054:1364 */       OutputStream out = null;
/* 1055:     */       try
/* 1056:     */       {
/* 1057:1366 */         out = picDataPart.getOutputStream();
/* 1058:1367 */         out.write(pictureData);
/* 1059:     */         try
/* 1060:     */         {
/* 1061:1372 */           if (out != null) {
/* 1062:1372 */             out.close();
/* 1063:     */           }
/* 1064:     */         }
/* 1065:     */         catch (IOException e) {}
/* 1066:1378 */         registerPackagePictureData(xwpfPicData);
/* 1067:     */       }
/* 1068:     */       catch (IOException e)
/* 1069:     */       {
/* 1070:1369 */         throw new POIXMLException(e);
/* 1071:     */       }
/* 1072:     */       finally
/* 1073:     */       {
/* 1074:     */         try
/* 1075:     */         {
/* 1076:1372 */           if (out != null) {
/* 1077:1372 */             out.close();
/* 1078:     */           }
/* 1079:     */         }
/* 1080:     */         catch (IOException e) {}
/* 1081:     */       }
/* 1082:1379 */       this.pictures.add(xwpfPicData);
/* 1083:     */       
/* 1084:1381 */       return getRelationId(xwpfPicData);
/* 1085:     */     }
/* 1086:1382 */     if (!getRelations().contains(xwpfPicData))
/* 1087:     */     {
/* 1088:1389 */       POIXMLDocumentPart.RelationPart rp = addRelation(null, XWPFRelation.IMAGES, xwpfPicData);
/* 1089:1390 */       return rp.getRelationship().getId();
/* 1090:     */     }
/* 1091:1393 */     return getRelationId(xwpfPicData);
/* 1092:     */   }
/* 1093:     */   
/* 1094:     */   public String addPictureData(InputStream is, int format)
/* 1095:     */     throws InvalidFormatException
/* 1096:     */   {
/* 1097:     */     try
/* 1098:     */     {
/* 1099:1399 */       byte[] data = IOUtils.toByteArray(is);
/* 1100:1400 */       return addPictureData(data, format);
/* 1101:     */     }
/* 1102:     */     catch (IOException e)
/* 1103:     */     {
/* 1104:1402 */       throw new POIXMLException(e);
/* 1105:     */     }
/* 1106:     */   }
/* 1107:     */   
/* 1108:     */   public int getNextPicNameNumber(int format)
/* 1109:     */     throws InvalidFormatException
/* 1110:     */   {
/* 1111:1414 */     int img = getAllPackagePictures().size() + 1;
/* 1112:1415 */     String proposal = XWPFPictureData.RELATIONS[format].getFileName(img);
/* 1113:1416 */     PackagePartName createPartName = PackagingURIHelper.createPartName(proposal);
/* 1114:1417 */     while (getPackage().getPart(createPartName) != null)
/* 1115:     */     {
/* 1116:1418 */       img++;
/* 1117:1419 */       proposal = XWPFPictureData.RELATIONS[format].getFileName(img);
/* 1118:1420 */       createPartName = PackagingURIHelper.createPartName(proposal);
/* 1119:     */     }
/* 1120:1422 */     return img;
/* 1121:     */   }
/* 1122:     */   
/* 1123:     */   public XWPFPictureData getPictureDataByID(String blipID)
/* 1124:     */   {
/* 1125:1432 */     POIXMLDocumentPart relatedPart = getRelationById(blipID);
/* 1126:1433 */     if ((relatedPart instanceof XWPFPictureData))
/* 1127:     */     {
/* 1128:1434 */       XWPFPictureData xwpfPicData = (XWPFPictureData)relatedPart;
/* 1129:1435 */       return xwpfPicData;
/* 1130:     */     }
/* 1131:1437 */     return null;
/* 1132:     */   }
/* 1133:     */   
/* 1134:     */   public XWPFNumbering getNumbering()
/* 1135:     */   {
/* 1136:1446 */     return this.numbering;
/* 1137:     */   }
/* 1138:     */   
/* 1139:     */   public XWPFStyles getStyles()
/* 1140:     */   {
/* 1141:1455 */     return this.styles;
/* 1142:     */   }
/* 1143:     */   
/* 1144:     */   public XWPFParagraph getParagraph(CTP p)
/* 1145:     */   {
/* 1146:1466 */     for (int i = 0; i < getParagraphs().size(); i++) {
/* 1147:1467 */       if (((XWPFParagraph)getParagraphs().get(i)).getCTP() == p) {
/* 1148:1468 */         return (XWPFParagraph)getParagraphs().get(i);
/* 1149:     */       }
/* 1150:     */     }
/* 1151:1471 */     return null;
/* 1152:     */   }
/* 1153:     */   
/* 1154:     */   public XWPFTable getTable(CTTbl ctTbl)
/* 1155:     */   {
/* 1156:1483 */     for (int i = 0; i < this.tables.size(); i++) {
/* 1157:1484 */       if (((XWPFTable)getTables().get(i)).getCTTbl() == ctTbl) {
/* 1158:1485 */         return (XWPFTable)getTables().get(i);
/* 1159:     */       }
/* 1160:     */     }
/* 1161:1488 */     return null;
/* 1162:     */   }
/* 1163:     */   
/* 1164:     */   public Iterator<XWPFTable> getTablesIterator()
/* 1165:     */   {
/* 1166:1492 */     return this.tables.iterator();
/* 1167:     */   }
/* 1168:     */   
/* 1169:     */   public Iterator<XWPFParagraph> getParagraphsIterator()
/* 1170:     */   {
/* 1171:1496 */     return this.paragraphs.iterator();
/* 1172:     */   }
/* 1173:     */   
/* 1174:     */   public XWPFParagraph getParagraphArray(int pos)
/* 1175:     */   {
/* 1176:1506 */     if ((pos >= 0) && (pos < this.paragraphs.size())) {
/* 1177:1507 */       return (XWPFParagraph)this.paragraphs.get(pos);
/* 1178:     */     }
/* 1179:1509 */     return null;
/* 1180:     */   }
/* 1181:     */   
/* 1182:     */   public POIXMLDocumentPart getPart()
/* 1183:     */   {
/* 1184:1521 */     return this;
/* 1185:     */   }
/* 1186:     */   
/* 1187:     */   public BodyType getPartType()
/* 1188:     */   {
/* 1189:1533 */     return BodyType.DOCUMENT;
/* 1190:     */   }
/* 1191:     */   
/* 1192:     */   public XWPFTableCell getTableCell(CTTc cell)
/* 1193:     */   {
/* 1194:1543 */     XmlCursor cursor = cell.newCursor();
/* 1195:1544 */     cursor.toParent();
/* 1196:1545 */     XmlObject o = cursor.getObject();
/* 1197:1546 */     if (!(o instanceof CTRow)) {
/* 1198:1547 */       return null;
/* 1199:     */     }
/* 1200:1549 */     CTRow row = (CTRow)o;
/* 1201:1550 */     cursor.toParent();
/* 1202:1551 */     o = cursor.getObject();
/* 1203:1552 */     cursor.dispose();
/* 1204:1553 */     if (!(o instanceof CTTbl)) {
/* 1205:1554 */       return null;
/* 1206:     */     }
/* 1207:1556 */     CTTbl tbl = (CTTbl)o;
/* 1208:1557 */     XWPFTable table = getTable(tbl);
/* 1209:1558 */     if (table == null) {
/* 1210:1559 */       return null;
/* 1211:     */     }
/* 1212:1561 */     XWPFTableRow tableRow = table.getRow(row);
/* 1213:1562 */     if (tableRow == null) {
/* 1214:1563 */       return null;
/* 1215:     */     }
/* 1216:1565 */     return tableRow.getTableCell(cell);
/* 1217:     */   }
/* 1218:     */   
/* 1219:     */   public XWPFDocument getXWPFDocument()
/* 1220:     */   {
/* 1221:1570 */     return this;
/* 1222:     */   }
/* 1223:     */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFDocument
 * JD-Core Version:    0.7.0.1
 */