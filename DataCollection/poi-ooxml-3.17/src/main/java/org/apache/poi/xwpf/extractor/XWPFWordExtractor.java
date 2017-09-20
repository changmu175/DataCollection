/*   1:    */ package org.apache.poi.xwpf.extractor;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.POIXMLDocument;
/*   7:    */ import org.apache.poi.POIXMLTextExtractor;
/*   8:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*   9:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  10:    */ import org.apache.poi.xwpf.model.XWPFCommentsDecorator;
/*  11:    */ import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
/*  12:    */ import org.apache.poi.xwpf.usermodel.IBodyElement;
/*  13:    */ import org.apache.poi.xwpf.usermodel.ICell;
/*  14:    */ import org.apache.poi.xwpf.usermodel.IRunElement;
/*  15:    */ import org.apache.poi.xwpf.usermodel.ISDTContent;
/*  16:    */ import org.apache.poi.xwpf.usermodel.XWPFDocument;
/*  17:    */ import org.apache.poi.xwpf.usermodel.XWPFFooter;
/*  18:    */ import org.apache.poi.xwpf.usermodel.XWPFHeader;
/*  19:    */ import org.apache.poi.xwpf.usermodel.XWPFHyperlink;
/*  20:    */ import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
/*  21:    */ import org.apache.poi.xwpf.usermodel.XWPFParagraph;
/*  22:    */ import org.apache.poi.xwpf.usermodel.XWPFRelation;
/*  23:    */ import org.apache.poi.xwpf.usermodel.XWPFRun;
/*  24:    */ import org.apache.poi.xwpf.usermodel.XWPFSDT;
/*  25:    */ import org.apache.poi.xwpf.usermodel.XWPFSDTCell;
/*  26:    */ import org.apache.poi.xwpf.usermodel.XWPFTable;
/*  27:    */ import org.apache.poi.xwpf.usermodel.XWPFTableCell;
/*  28:    */ import org.apache.poi.xwpf.usermodel.XWPFTableRow;
/*  29:    */ import org.apache.xmlbeans.XmlException;
/*  30:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
/*  31:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
/*  32:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
/*  33:    */ 
/*  34:    */ public class XWPFWordExtractor
/*  35:    */   extends POIXMLTextExtractor
/*  36:    */ {
/*  37: 49 */   public static final XWPFRelation[] SUPPORTED_TYPES = { XWPFRelation.DOCUMENT, XWPFRelation.TEMPLATE, XWPFRelation.MACRO_DOCUMENT, XWPFRelation.MACRO_TEMPLATE_DOCUMENT };
/*  38:    */   private XWPFDocument document;
/*  39: 56 */   private boolean fetchHyperlinks = false;
/*  40: 57 */   private boolean concatenatePhoneticRuns = true;
/*  41:    */   
/*  42:    */   public XWPFWordExtractor(OPCPackage container)
/*  43:    */     throws XmlException, OpenXML4JException, IOException
/*  44:    */   {
/*  45: 60 */     this(new XWPFDocument(container));
/*  46:    */   }
/*  47:    */   
/*  48:    */   public XWPFWordExtractor(XWPFDocument document)
/*  49:    */   {
/*  50: 64 */     super(document);
/*  51: 65 */     this.document = document;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public static void main(String[] args)
/*  55:    */     throws Exception
/*  56:    */   {
/*  57: 69 */     if (args.length < 1)
/*  58:    */     {
/*  59: 70 */       System.err.println("Use:");
/*  60: 71 */       System.err.println("  XWPFWordExtractor <filename.docx>");
/*  61: 72 */       System.exit(1);
/*  62:    */     }
/*  63: 74 */     POIXMLTextExtractor extractor = new XWPFWordExtractor(POIXMLDocument.openPackage(args[0]));
/*  64:    */     
/*  65:    */ 
/*  66:    */ 
/*  67: 78 */     System.out.println(extractor.getText());
/*  68: 79 */     extractor.close();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setFetchHyperlinks(boolean fetch)
/*  72:    */   {
/*  73: 88 */     this.fetchHyperlinks = fetch;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void setConcatenatePhoneticRuns(boolean concatenatePhoneticRuns)
/*  77:    */   {
/*  78: 96 */     this.concatenatePhoneticRuns = concatenatePhoneticRuns;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public String getText()
/*  82:    */   {
/*  83:100 */     StringBuffer text = new StringBuffer();
/*  84:101 */     XWPFHeaderFooterPolicy hfPolicy = this.document.getHeaderFooterPolicy();
/*  85:    */     
/*  86:    */ 
/*  87:104 */     extractHeaders(text, hfPolicy);
/*  88:107 */     for (IBodyElement e : this.document.getBodyElements())
/*  89:    */     {
/*  90:108 */       appendBodyElementText(text, e);
/*  91:109 */       text.append('\n');
/*  92:    */     }
/*  93:113 */     extractFooters(text, hfPolicy);
/*  94:    */     
/*  95:115 */     return text.toString();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void appendBodyElementText(StringBuffer text, IBodyElement e)
/*  99:    */   {
/* 100:119 */     if ((e instanceof XWPFParagraph)) {
/* 101:120 */       appendParagraphText(text, (XWPFParagraph)e);
/* 102:121 */     } else if ((e instanceof XWPFTable)) {
/* 103:122 */       appendTableText(text, (XWPFTable)e);
/* 104:123 */     } else if ((e instanceof XWPFSDT)) {
/* 105:124 */       text.append(((XWPFSDT)e).getContent().getText());
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void appendParagraphText(StringBuffer text, XWPFParagraph paragraph)
/* 110:    */   {
/* 111:129 */     CTSectPr ctSectPr = null;
/* 112:130 */     if (paragraph.getCTP().getPPr() != null) {
/* 113:131 */       ctSectPr = paragraph.getCTP().getPPr().getSectPr();
/* 114:    */     }
/* 115:134 */     XWPFHeaderFooterPolicy headerFooterPolicy = null;
/* 116:136 */     if (ctSectPr != null)
/* 117:    */     {
/* 118:137 */       headerFooterPolicy = new XWPFHeaderFooterPolicy(this.document, ctSectPr);
/* 119:138 */       extractHeaders(text, headerFooterPolicy);
/* 120:    */     }
/* 121:142 */     for (IRunElement run : paragraph.getRuns())
/* 122:    */     {
/* 123:143 */       if ((!this.concatenatePhoneticRuns) && ((run instanceof XWPFRun))) {
/* 124:144 */         text.append(((XWPFRun)run).text());
/* 125:    */       } else {
/* 126:146 */         text.append(run);
/* 127:    */       }
/* 128:148 */       if (((run instanceof XWPFHyperlinkRun)) && (this.fetchHyperlinks))
/* 129:    */       {
/* 130:149 */         XWPFHyperlink link = ((XWPFHyperlinkRun)run).getHyperlink(this.document);
/* 131:150 */         if (link != null) {
/* 132:151 */           text.append(" <").append(link.getURL()).append(">");
/* 133:    */         }
/* 134:    */       }
/* 135:    */     }
/* 136:156 */     XWPFCommentsDecorator decorator = new XWPFCommentsDecorator(paragraph, null);
/* 137:157 */     String commentText = decorator.getCommentText();
/* 138:158 */     if (commentText.length() > 0) {
/* 139:159 */       text.append(commentText).append('\n');
/* 140:    */     }
/* 141:163 */     String footnameText = paragraph.getFootnoteText();
/* 142:164 */     if ((footnameText != null) && (footnameText.length() > 0)) {
/* 143:165 */       text.append(footnameText).append('\n');
/* 144:    */     }
/* 145:168 */     if (ctSectPr != null) {
/* 146:169 */       extractFooters(text, headerFooterPolicy);
/* 147:    */     }
/* 148:    */   }
/* 149:    */   
/* 150:    */   private void appendTableText(StringBuffer text, XWPFTable table)
/* 151:    */   {
/* 152:175 */     for (XWPFTableRow row : table.getRows())
/* 153:    */     {
/* 154:176 */       List<ICell> cells = row.getTableICells();
/* 155:177 */       for (int i = 0; i < cells.size(); i++)
/* 156:    */       {
/* 157:178 */         ICell cell = (ICell)cells.get(i);
/* 158:179 */         if ((cell instanceof XWPFTableCell)) {
/* 159:180 */           text.append(((XWPFTableCell)cell).getTextRecursively());
/* 160:181 */         } else if ((cell instanceof XWPFSDTCell)) {
/* 161:182 */           text.append(((XWPFSDTCell)cell).getContent().getText());
/* 162:    */         }
/* 163:184 */         if (i < cells.size() - 1) {
/* 164:185 */           text.append("\t");
/* 165:    */         }
/* 166:    */       }
/* 167:188 */       text.append('\n');
/* 168:    */     }
/* 169:    */   }
/* 170:    */   
/* 171:    */   private void extractFooters(StringBuffer text, XWPFHeaderFooterPolicy hfPolicy)
/* 172:    */   {
/* 173:193 */     if (hfPolicy == null) {
/* 174:193 */       return;
/* 175:    */     }
/* 176:195 */     if (hfPolicy.getFirstPageFooter() != null) {
/* 177:196 */       text.append(hfPolicy.getFirstPageFooter().getText());
/* 178:    */     }
/* 179:198 */     if (hfPolicy.getEvenPageFooter() != null) {
/* 180:199 */       text.append(hfPolicy.getEvenPageFooter().getText());
/* 181:    */     }
/* 182:201 */     if (hfPolicy.getDefaultFooter() != null) {
/* 183:202 */       text.append(hfPolicy.getDefaultFooter().getText());
/* 184:    */     }
/* 185:    */   }
/* 186:    */   
/* 187:    */   private void extractHeaders(StringBuffer text, XWPFHeaderFooterPolicy hfPolicy)
/* 188:    */   {
/* 189:207 */     if (hfPolicy == null) {
/* 190:207 */       return;
/* 191:    */     }
/* 192:209 */     if (hfPolicy.getFirstPageHeader() != null) {
/* 193:210 */       text.append(hfPolicy.getFirstPageHeader().getText());
/* 194:    */     }
/* 195:212 */     if (hfPolicy.getEvenPageHeader() != null) {
/* 196:213 */       text.append(hfPolicy.getEvenPageHeader().getText());
/* 197:    */     }
/* 198:215 */     if (hfPolicy.getDefaultHeader() != null) {
/* 199:216 */       text.append(hfPolicy.getDefaultHeader().getText());
/* 200:    */     }
/* 201:    */   }
/* 202:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.extractor.XWPFWordExtractor
 * JD-Core Version:    0.7.0.1
 */