/*   1:    */ package org.apache.poi.xslf.extractor;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import org.apache.poi.POIXMLTextExtractor;
/*   6:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*   7:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*   8:    */ import org.apache.poi.xslf.usermodel.XMLSlideShow;
/*   9:    */ import org.apache.poi.xslf.usermodel.XSLFCommentAuthors;
/*  10:    */ import org.apache.poi.xslf.usermodel.XSLFComments;
/*  11:    */ import org.apache.poi.xslf.usermodel.XSLFNotes;
/*  12:    */ import org.apache.poi.xslf.usermodel.XSLFRelation;
/*  13:    */ import org.apache.poi.xslf.usermodel.XSLFShape;
/*  14:    */ import org.apache.poi.xslf.usermodel.XSLFShapeContainer;
/*  15:    */ import org.apache.poi.xslf.usermodel.XSLFSlide;
/*  16:    */ import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
/*  17:    */ import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
/*  18:    */ import org.apache.poi.xslf.usermodel.XSLFSlideShow;
/*  19:    */ import org.apache.poi.xslf.usermodel.XSLFTable;
/*  20:    */ import org.apache.poi.xslf.usermodel.XSLFTableCell;
/*  21:    */ import org.apache.poi.xslf.usermodel.XSLFTableRow;
/*  22:    */ import org.apache.poi.xslf.usermodel.XSLFTextShape;
/*  23:    */ import org.apache.xmlbeans.XmlException;
/*  24:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTComment;
/*  25:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor;
/*  26:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList;
/*  27:    */ 
/*  28:    */ public class XSLFPowerPointExtractor
/*  29:    */   extends POIXMLTextExtractor
/*  30:    */ {
/*  31: 44 */   public static final XSLFRelation[] SUPPORTED_TYPES = { XSLFRelation.MAIN, XSLFRelation.MACRO, XSLFRelation.MACRO_TEMPLATE, XSLFRelation.PRESENTATIONML, XSLFRelation.PRESENTATIONML_TEMPLATE, XSLFRelation.PRESENTATION_MACRO };
/*  32:    */   private XMLSlideShow slideshow;
/*  33: 51 */   private boolean slidesByDefault = true;
/*  34: 52 */   private boolean notesByDefault = false;
/*  35: 53 */   private boolean masterByDefault = false;
/*  36:    */   
/*  37:    */   public XSLFPowerPointExtractor(XMLSlideShow slideshow)
/*  38:    */   {
/*  39: 56 */     super(slideshow);
/*  40: 57 */     this.slideshow = slideshow;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public XSLFPowerPointExtractor(XSLFSlideShow slideshow)
/*  44:    */     throws XmlException, IOException
/*  45:    */   {
/*  46: 60 */     this(new XMLSlideShow(slideshow.getPackage()));
/*  47:    */   }
/*  48:    */   
/*  49:    */   public XSLFPowerPointExtractor(OPCPackage container)
/*  50:    */     throws XmlException, OpenXML4JException, IOException
/*  51:    */   {
/*  52: 63 */     this(new XSLFSlideShow(container));
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static void main(String[] args)
/*  56:    */     throws Exception
/*  57:    */   {
/*  58: 67 */     if (args.length < 1)
/*  59:    */     {
/*  60: 68 */       System.err.println("Use:");
/*  61: 69 */       System.err.println("  XSLFPowerPointExtractor <filename.pptx>");
/*  62: 70 */       System.exit(1);
/*  63:    */     }
/*  64: 72 */     POIXMLTextExtractor extractor = new XSLFPowerPointExtractor(new XSLFSlideShow(args[0]));
/*  65:    */     
/*  66:    */ 
/*  67: 75 */     System.out.println(extractor.getText());
/*  68: 76 */     extractor.close();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setSlidesByDefault(boolean slidesByDefault)
/*  72:    */   {
/*  73: 84 */     this.slidesByDefault = slidesByDefault;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void setNotesByDefault(boolean notesByDefault)
/*  77:    */   {
/*  78: 91 */     this.notesByDefault = notesByDefault;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setMasterByDefault(boolean masterByDefault)
/*  82:    */   {
/*  83: 98 */     this.masterByDefault = masterByDefault;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public String getText()
/*  87:    */   {
/*  88:106 */     return getText(this.slidesByDefault, this.notesByDefault);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public String getText(boolean slideText, boolean notesText)
/*  92:    */   {
/*  93:115 */     return getText(slideText, notesText, this.masterByDefault);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public String getText(boolean slideText, boolean notesText, boolean masterText)
/*  97:    */   {
/*  98:128 */     StringBuilder text = new StringBuilder();
/*  99:130 */     for (XSLFSlide slide : this.slideshow.getSlides()) {
/* 100:131 */       text.append(getText(slide, slideText, notesText, masterText));
/* 101:    */     }
/* 102:134 */     return text.toString();
/* 103:    */   }
/* 104:    */   
/* 105:    */   public static String getText(XSLFSlide slide, boolean slideText, boolean notesText, boolean masterText)
/* 106:    */   {
/* 107:148 */     StringBuilder text = new StringBuilder();
/* 108:    */     
/* 109:150 */     XSLFCommentAuthors commentAuthors = slide.getSlideShow().getCommentAuthors();
/* 110:    */     
/* 111:152 */     XSLFNotes notes = slide.getNotes();
/* 112:153 */     XSLFComments comments = slide.getComments();
/* 113:154 */     XSLFSlideLayout layout = slide.getSlideLayout();
/* 114:155 */     XSLFSlideMaster master = layout.getSlideMaster();
/* 115:161 */     if (slideText)
/* 116:    */     {
/* 117:162 */       extractText(slide, false, text);
/* 118:165 */       if (masterText)
/* 119:    */       {
/* 120:166 */         assert (layout != null);
/* 121:167 */         extractText(layout, true, text);
/* 122:168 */         assert (master != null);
/* 123:169 */         extractText(master, true, text);
/* 124:    */       }
/* 125:173 */       if (comments != null) {
/* 126:174 */         for (CTComment comment : comments.getCTCommentsList().getCmArray())
/* 127:    */         {
/* 128:176 */           if (commentAuthors != null)
/* 129:    */           {
/* 130:177 */             CTCommentAuthor author = commentAuthors.getAuthorById(comment.getAuthorId());
/* 131:178 */             if (author != null) {
/* 132:179 */               text.append(author.getName() + ": ");
/* 133:    */             }
/* 134:    */           }
/* 135:184 */           text.append(comment.getText());
/* 136:185 */           text.append("\n");
/* 137:    */         }
/* 138:    */       }
/* 139:    */     }
/* 140:191 */     if ((notesText) && (notes != null)) {
/* 141:192 */       extractText(notes, false, text);
/* 142:    */     }
/* 143:195 */     return text.toString();
/* 144:    */   }
/* 145:    */   
/* 146:    */   private static void extractText(XSLFShapeContainer data, boolean skipPlaceholders, StringBuilder text)
/* 147:    */   {
/* 148:199 */     for (XSLFShape s : data) {
/* 149:200 */       if ((s instanceof XSLFShapeContainer))
/* 150:    */       {
/* 151:201 */         extractText((XSLFShapeContainer)s, skipPlaceholders, text);
/* 152:    */       }
/* 153:202 */       else if ((s instanceof XSLFTextShape))
/* 154:    */       {
/* 155:203 */         XSLFTextShape ts = (XSLFTextShape)s;
/* 156:205 */         if ((!skipPlaceholders) || (!ts.isPlaceholder()))
/* 157:    */         {
/* 158:206 */           text.append(ts.getText());
/* 159:207 */           text.append("\n");
/* 160:    */         }
/* 161:    */       }
/* 162:209 */       else if ((s instanceof XSLFTable))
/* 163:    */       {
/* 164:210 */         XSLFTable ts = (XSLFTable)s;
/* 165:212 */         for (XSLFTableRow r : ts)
/* 166:    */         {
/* 167:213 */           for (XSLFTableCell c : r)
/* 168:    */           {
/* 169:214 */             text.append(c.getText());
/* 170:215 */             text.append("\t");
/* 171:    */           }
/* 172:217 */           text.append("\n");
/* 173:    */         }
/* 174:    */       }
/* 175:    */     }
/* 176:    */   }
/* 177:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.extractor.XSLFPowerPointExtractor
 * JD-Core Version:    0.7.0.1
 */