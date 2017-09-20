/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.LinkedList;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.POIXMLDocument;
/*   7:    */ import org.apache.poi.POIXMLTypeLoader;
/*   8:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*   9:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  10:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  11:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  12:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  13:    */ import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
/*  14:    */ import org.apache.poi.openxml4j.opc.TargetMode;
/*  15:    */ import org.apache.poi.util.Internal;
/*  16:    */ import org.apache.xmlbeans.XmlException;
/*  17:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList;
/*  18:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide;
/*  19:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation;
/*  20:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;
/*  21:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdList;
/*  22:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdList.Factory;
/*  23:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry;
/*  24:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster;
/*  25:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdList;
/*  26:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry;
/*  27:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CmLstDocument;
/*  28:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CmLstDocument.Factory;
/*  29:    */ import org.openxmlformats.schemas.presentationml.x2006.main.NotesDocument;
/*  30:    */ import org.openxmlformats.schemas.presentationml.x2006.main.NotesDocument.Factory;
/*  31:    */ import org.openxmlformats.schemas.presentationml.x2006.main.PresentationDocument;
/*  32:    */ import org.openxmlformats.schemas.presentationml.x2006.main.PresentationDocument.Factory;
/*  33:    */ import org.openxmlformats.schemas.presentationml.x2006.main.SldDocument;
/*  34:    */ import org.openxmlformats.schemas.presentationml.x2006.main.SldDocument.Factory;
/*  35:    */ import org.openxmlformats.schemas.presentationml.x2006.main.SldMasterDocument;
/*  36:    */ import org.openxmlformats.schemas.presentationml.x2006.main.SldMasterDocument.Factory;
/*  37:    */ 
/*  38:    */ public class XSLFSlideShow
/*  39:    */   extends POIXMLDocument
/*  40:    */ {
/*  41:    */   private PresentationDocument presentationDoc;
/*  42:    */   private List<PackagePart> embedds;
/*  43:    */   
/*  44:    */   public XSLFSlideShow(OPCPackage container)
/*  45:    */     throws OpenXML4JException, IOException, XmlException
/*  46:    */   {
/*  47: 71 */     super(container);
/*  48: 73 */     if (getCorePart().getContentType().equals(XSLFRelation.THEME_MANAGER.getContentType())) {
/*  49: 74 */       rebase(getPackage());
/*  50:    */     }
/*  51: 77 */     this.presentationDoc = PresentationDocument.Factory.parse(getCorePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  52:    */     
/*  53:    */ 
/*  54: 80 */     this.embedds = new LinkedList();
/*  55:    */     PackagePart slidePart;
/*  56: 81 */     for (CTSlideIdListEntry ctSlide : getSlideReferences().getSldIdArray())
/*  57:    */     {
/*  58: 82 */       PackagePart corePart = getCorePart();
/*  59: 83 */       slidePart = corePart.getRelatedPart(corePart.getRelationship(ctSlide.getId2()));
/*  60: 85 */       for (PackageRelationship rel : slidePart.getRelationshipsByType("http://schemas.openxmlformats.org/officeDocument/2006/relationships/oleObject")) {
/*  61: 86 */         if (TargetMode.EXTERNAL != rel.getTargetMode()) {
/*  62: 90 */           this.embedds.add(slidePart.getRelatedPart(rel));
/*  63:    */         }
/*  64:    */       }
/*  65: 93 */       for (PackageRelationship rel : slidePart.getRelationshipsByType("http://schemas.openxmlformats.org/officeDocument/2006/relationships/package")) {
/*  66: 94 */         this.embedds.add(slidePart.getRelatedPart(rel));
/*  67:    */       }
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71:    */   public XSLFSlideShow(String file)
/*  72:    */     throws OpenXML4JException, IOException, XmlException
/*  73:    */   {
/*  74: 99 */     this(openPackage(file));
/*  75:    */   }
/*  76:    */   
/*  77:    */   @Internal
/*  78:    */   public CTPresentation getPresentation()
/*  79:    */   {
/*  80:107 */     return this.presentationDoc.getPresentation();
/*  81:    */   }
/*  82:    */   
/*  83:    */   @Internal
/*  84:    */   public CTSlideIdList getSlideReferences()
/*  85:    */   {
/*  86:118 */     if (!getPresentation().isSetSldIdLst()) {
/*  87:119 */       getPresentation().setSldIdLst(CTSlideIdList.Factory.newInstance());
/*  88:    */     }
/*  89:121 */     return getPresentation().getSldIdLst();
/*  90:    */   }
/*  91:    */   
/*  92:    */   @Internal
/*  93:    */   public CTSlideMasterIdList getSlideMasterReferences()
/*  94:    */   {
/*  95:132 */     return getPresentation().getSldMasterIdLst();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public PackagePart getSlideMasterPart(CTSlideMasterIdListEntry master)
/*  99:    */     throws IOException, XmlException
/* 100:    */   {
/* 101:    */     try
/* 102:    */     {
/* 103:137 */       PackagePart corePart = getCorePart();
/* 104:138 */       return corePart.getRelatedPart(corePart.getRelationship(master.getId2()));
/* 105:    */     }
/* 106:    */     catch (InvalidFormatException e)
/* 107:    */     {
/* 108:142 */       throw new XmlException(e);
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   @Internal
/* 113:    */   public CTSlideMaster getSlideMaster(CTSlideMasterIdListEntry master)
/* 114:    */     throws IOException, XmlException
/* 115:    */   {
/* 116:151 */     PackagePart masterPart = getSlideMasterPart(master);
/* 117:152 */     SldMasterDocument masterDoc = SldMasterDocument.Factory.parse(masterPart.getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 118:    */     
/* 119:154 */     return masterDoc.getSldMaster();
/* 120:    */   }
/* 121:    */   
/* 122:    */   public PackagePart getSlidePart(CTSlideIdListEntry slide)
/* 123:    */     throws IOException, XmlException
/* 124:    */   {
/* 125:    */     try
/* 126:    */     {
/* 127:159 */       PackagePart corePart = getCorePart();
/* 128:160 */       return corePart.getRelatedPart(corePart.getRelationship(slide.getId2()));
/* 129:    */     }
/* 130:    */     catch (InvalidFormatException e)
/* 131:    */     {
/* 132:162 */       throw new XmlException(e);
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   @Internal
/* 137:    */   public CTSlide getSlide(CTSlideIdListEntry slide)
/* 138:    */     throws IOException, XmlException
/* 139:    */   {
/* 140:171 */     PackagePart slidePart = getSlidePart(slide);
/* 141:172 */     SldDocument slideDoc = SldDocument.Factory.parse(slidePart.getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 142:    */     
/* 143:174 */     return slideDoc.getSld();
/* 144:    */   }
/* 145:    */   
/* 146:    */   public PackagePart getNodesPart(CTSlideIdListEntry parentSlide)
/* 147:    */     throws IOException, XmlException
/* 148:    */   {
/* 149:183 */     PackagePart slidePart = getSlidePart(parentSlide);
/* 150:    */     PackageRelationshipCollection notes;
/* 151:    */     try
/* 152:    */     {
/* 153:186 */       notes = slidePart.getRelationshipsByType(XSLFRelation.NOTES.getRelation());
/* 154:    */     }
/* 155:    */     catch (InvalidFormatException e)
/* 156:    */     {
/* 157:188 */       throw new IllegalStateException(e);
/* 158:    */     }
/* 159:191 */     if (notes.size() == 0) {
/* 160:193 */       return null;
/* 161:    */     }
/* 162:195 */     if (notes.size() > 1) {
/* 163:196 */       throw new IllegalStateException("Expecting 0 or 1 notes for a slide, but found " + notes.size());
/* 164:    */     }
/* 165:    */     try
/* 166:    */     {
/* 167:200 */       return slidePart.getRelatedPart(notes.getRelationship(0));
/* 168:    */     }
/* 169:    */     catch (InvalidFormatException e)
/* 170:    */     {
/* 171:202 */       throw new IllegalStateException(e);
/* 172:    */     }
/* 173:    */   }
/* 174:    */   
/* 175:    */   @Internal
/* 176:    */   public CTNotesSlide getNotes(CTSlideIdListEntry slide)
/* 177:    */     throws IOException, XmlException
/* 178:    */   {
/* 179:211 */     PackagePart notesPart = getNodesPart(slide);
/* 180:212 */     if (notesPart == null) {
/* 181:213 */       return null;
/* 182:    */     }
/* 183:215 */     NotesDocument notesDoc = NotesDocument.Factory.parse(notesPart.getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 184:    */     
/* 185:    */ 
/* 186:218 */     return notesDoc.getNotes();
/* 187:    */   }
/* 188:    */   
/* 189:    */   @Internal
/* 190:    */   public CTCommentList getSlideComments(CTSlideIdListEntry slide)
/* 191:    */     throws IOException, XmlException
/* 192:    */   {
/* 193:227 */     PackagePart slidePart = getSlidePart(slide);
/* 194:    */     PackageRelationshipCollection commentRels;
/* 195:    */     try
/* 196:    */     {
/* 197:230 */       commentRels = slidePart.getRelationshipsByType(XSLFRelation.COMMENTS.getRelation());
/* 198:    */     }
/* 199:    */     catch (InvalidFormatException e)
/* 200:    */     {
/* 201:232 */       throw new IllegalStateException(e);
/* 202:    */     }
/* 203:235 */     if (commentRels.size() == 0) {
/* 204:237 */       return null;
/* 205:    */     }
/* 206:239 */     if (commentRels.size() > 1) {
/* 207:240 */       throw new IllegalStateException("Expecting 0 or 1 comments for a slide, but found " + commentRels.size());
/* 208:    */     }
/* 209:    */     try
/* 210:    */     {
/* 211:244 */       PackagePart cPart = slidePart.getRelatedPart(commentRels.getRelationship(0));
/* 212:    */       
/* 213:    */ 
/* 214:247 */       CmLstDocument commDoc = CmLstDocument.Factory.parse(cPart.getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 215:    */       
/* 216:249 */       return commDoc.getCmLst();
/* 217:    */     }
/* 218:    */     catch (InvalidFormatException e)
/* 219:    */     {
/* 220:251 */       throw new IllegalStateException(e);
/* 221:    */     }
/* 222:    */   }
/* 223:    */   
/* 224:    */   public List<PackagePart> getAllEmbedds()
/* 225:    */     throws OpenXML4JException
/* 226:    */   {
/* 227:260 */     return this.embedds;
/* 228:    */   }
/* 229:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFSlideShow
 * JD-Core Version:    0.7.0.1
 */