/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.POIXMLDocumentPart;
/*   7:    */ import org.apache.poi.POIXMLTypeLoader;
/*   8:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   9:    */ import org.apache.poi.sl.usermodel.Notes;
/*  10:    */ import org.apache.xmlbeans.XmlException;
/*  11:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData;
/*  12:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide;
/*  13:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide.Factory;
/*  14:    */ import org.openxmlformats.schemas.presentationml.x2006.main.NotesDocument;
/*  15:    */ import org.openxmlformats.schemas.presentationml.x2006.main.NotesDocument.Factory;
/*  16:    */ 
/*  17:    */ public final class XSLFNotes
/*  18:    */   extends XSLFSheet
/*  19:    */   implements Notes<XSLFShape, XSLFTextParagraph>
/*  20:    */ {
/*  21:    */   private CTNotesSlide _notes;
/*  22:    */   
/*  23:    */   XSLFNotes()
/*  24:    */   {
/*  25: 44 */     this._notes = prototype();
/*  26: 45 */     setCommonSlideData(this._notes.getCSld());
/*  27:    */   }
/*  28:    */   
/*  29:    */   XSLFNotes(PackagePart part)
/*  30:    */     throws IOException, XmlException
/*  31:    */   {
/*  32: 59 */     super(part);
/*  33:    */     
/*  34: 61 */     NotesDocument doc = NotesDocument.Factory.parse(getPackagePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  35:    */     
/*  36: 63 */     this._notes = doc.getNotes();
/*  37: 64 */     setCommonSlideData(this._notes.getCSld());
/*  38:    */   }
/*  39:    */   
/*  40:    */   private static CTNotesSlide prototype()
/*  41:    */   {
/*  42: 68 */     CTNotesSlide ctNotes = CTNotesSlide.Factory.newInstance();
/*  43: 69 */     CTCommonSlideData cSld = ctNotes.addNewCSld();
/*  44: 70 */     cSld.addNewSpTree();
/*  45:    */     
/*  46: 72 */     return ctNotes;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public CTNotesSlide getXmlObject()
/*  50:    */   {
/*  51: 77 */     return this._notes;
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected String getRootElementName()
/*  55:    */   {
/*  56: 82 */     return "notes";
/*  57:    */   }
/*  58:    */   
/*  59:    */   public XSLFTheme getTheme()
/*  60:    */   {
/*  61: 87 */     return getMasterSheet().getTheme();
/*  62:    */   }
/*  63:    */   
/*  64:    */   public XSLFNotesMaster getMasterSheet()
/*  65:    */   {
/*  66: 92 */     for (POIXMLDocumentPart p : getRelations()) {
/*  67: 93 */       if ((p instanceof XSLFNotesMaster)) {
/*  68: 94 */         return (XSLFNotesMaster)p;
/*  69:    */       }
/*  70:    */     }
/*  71: 97 */     return null;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public List<List<XSLFTextParagraph>> getTextParagraphs()
/*  75:    */   {
/*  76:102 */     List<List<XSLFTextParagraph>> tp = new ArrayList();
/*  77:103 */     for (XSLFShape sh : super.getShapes()) {
/*  78:104 */       if ((sh instanceof XSLFTextShape))
/*  79:    */       {
/*  80:105 */         XSLFTextShape txt = (XSLFTextShape)sh;
/*  81:106 */         tp.add(txt.getTextParagraphs());
/*  82:    */       }
/*  83:    */     }
/*  84:109 */     return tp;
/*  85:    */   }
/*  86:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFNotes
 * JD-Core Version:    0.7.0.1
 */