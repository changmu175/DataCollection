/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.math.BigInteger;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.List;
/*   9:    */ import javax.xml.namespace.QName;
/*  10:    */ import org.apache.poi.POIXMLDocumentPart;
/*  11:    */ import org.apache.poi.POIXMLException;
/*  12:    */ import org.apache.poi.POIXMLTypeLoader;
/*  13:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  14:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  15:    */ import org.apache.xmlbeans.SchemaType;
/*  16:    */ import org.apache.xmlbeans.XmlException;
/*  17:    */ import org.apache.xmlbeans.XmlOptions;
/*  18:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFootnotes;
/*  19:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn;
/*  20:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.FootnotesDocument;
/*  21:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.FootnotesDocument.Factory;
/*  22:    */ 
/*  23:    */ public class XWPFFootnotes
/*  24:    */   extends POIXMLDocumentPart
/*  25:    */ {
/*  26:    */   protected XWPFDocument document;
/*  27: 45 */   private List<XWPFFootnote> listFootnote = new ArrayList();
/*  28:    */   private CTFootnotes ctFootnotes;
/*  29:    */   
/*  30:    */   public XWPFFootnotes(PackagePart part)
/*  31:    */     throws IOException, OpenXML4JException
/*  32:    */   {
/*  33: 56 */     super(part);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public XWPFFootnotes() {}
/*  37:    */   
/*  38:    */   protected void onDocumentRead()
/*  39:    */     throws IOException
/*  40:    */   {
/*  41: 71 */     InputStream is = null;
/*  42:    */     try
/*  43:    */     {
/*  44: 73 */       is = getPackagePart().getInputStream();
/*  45: 74 */       FootnotesDocument notesDoc = FootnotesDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  46: 75 */       this.ctFootnotes = notesDoc.getFootnotes();
/*  47:    */     }
/*  48:    */     catch (XmlException e)
/*  49:    */     {
/*  50: 77 */       throw new POIXMLException();
/*  51:    */     }
/*  52:    */     finally
/*  53:    */     {
/*  54: 79 */       if (is != null) {
/*  55: 80 */         is.close();
/*  56:    */       }
/*  57:    */     }
/*  58: 85 */     for (CTFtnEdn note : this.ctFootnotes.getFootnoteArray()) {
/*  59: 86 */       this.listFootnote.add(new XWPFFootnote(note, this));
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected void commit()
/*  64:    */     throws IOException
/*  65:    */   {
/*  66: 92 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  67: 93 */     xmlOptions.setSaveSyntheticDocumentElement(new QName(CTFootnotes.type.getName().getNamespaceURI(), "footnotes"));
/*  68: 94 */     PackagePart part = getPackagePart();
/*  69: 95 */     OutputStream out = part.getOutputStream();
/*  70: 96 */     this.ctFootnotes.save(out, xmlOptions);
/*  71: 97 */     out.close();
/*  72:    */   }
/*  73:    */   
/*  74:    */   public List<XWPFFootnote> getFootnotesList()
/*  75:    */   {
/*  76:101 */     return this.listFootnote;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public XWPFFootnote getFootnoteById(int id)
/*  80:    */   {
/*  81:105 */     for (XWPFFootnote note : this.listFootnote) {
/*  82:106 */       if (note.getCTFtnEdn().getId().intValue() == id) {
/*  83:107 */         return note;
/*  84:    */       }
/*  85:    */     }
/*  86:109 */     return null;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setFootnotes(CTFootnotes footnotes)
/*  90:    */   {
/*  91:118 */     this.ctFootnotes = footnotes;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void addFootnote(XWPFFootnote footnote)
/*  95:    */   {
/*  96:128 */     this.listFootnote.add(footnote);
/*  97:129 */     this.ctFootnotes.addNewFootnote().set(footnote.getCTFtnEdn());
/*  98:    */   }
/*  99:    */   
/* 100:    */   public XWPFFootnote addFootnote(CTFtnEdn note)
/* 101:    */   {
/* 102:139 */     CTFtnEdn newNote = this.ctFootnotes.addNewFootnote();
/* 103:140 */     newNote.set(note);
/* 104:141 */     XWPFFootnote xNote = new XWPFFootnote(newNote, this);
/* 105:142 */     this.listFootnote.add(xNote);
/* 106:143 */     return xNote;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public XWPFDocument getXWPFDocument()
/* 110:    */   {
/* 111:150 */     if (this.document != null) {
/* 112:151 */       return this.document;
/* 113:    */     }
/* 114:153 */     return (XWPFDocument)getParent();
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void setXWPFDocument(XWPFDocument doc)
/* 118:    */   {
/* 119:158 */     this.document = doc;
/* 120:    */   }
/* 121:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFFootnotes
 * JD-Core Version:    0.7.0.1
 */