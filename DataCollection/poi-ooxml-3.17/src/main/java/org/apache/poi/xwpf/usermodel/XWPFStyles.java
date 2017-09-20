/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.List;
/*   8:    */ import javax.xml.namespace.QName;
/*   9:    */ import org.apache.poi.POIXMLDocumentPart;
/*  10:    */ import org.apache.poi.POIXMLException;
/*  11:    */ import org.apache.poi.POIXMLTypeLoader;
/*  12:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  13:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  14:    */ import org.apache.xmlbeans.SchemaType;
/*  15:    */ import org.apache.xmlbeans.XmlException;
/*  16:    */ import org.apache.xmlbeans.XmlOptions;
/*  17:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
/*  18:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
/*  19:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage;
/*  20:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault;
/*  21:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
/*  22:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault;
/*  23:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
/*  24:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
/*  25:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.StylesDocument;
/*  26:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.StylesDocument.Factory;
/*  27:    */ 
/*  28:    */ public class XWPFStyles
/*  29:    */   extends POIXMLDocumentPart
/*  30:    */ {
/*  31:    */   private CTStyles ctStyles;
/*  32: 54 */   private List<XWPFStyle> listStyle = new ArrayList();
/*  33:    */   private XWPFLatentStyles latentStyles;
/*  34:    */   private XWPFDefaultRunStyle defaultRunStyle;
/*  35:    */   private XWPFDefaultParagraphStyle defaultParaStyle;
/*  36:    */   
/*  37:    */   public XWPFStyles(PackagePart part)
/*  38:    */     throws IOException, OpenXML4JException
/*  39:    */   {
/*  40: 68 */     super(part);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public XWPFStyles() {}
/*  44:    */   
/*  45:    */   protected void onDocumentRead()
/*  46:    */     throws IOException
/*  47:    */   {
/*  48: 83 */     InputStream is = getPackagePart().getInputStream();
/*  49:    */     try
/*  50:    */     {
/*  51: 85 */       StylesDocument stylesDoc = StylesDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  52: 86 */       setStyles(stylesDoc.getStyles());
/*  53: 87 */       this.latentStyles = new XWPFLatentStyles(this.ctStyles.getLatentStyles(), this);
/*  54:    */     }
/*  55:    */     catch (XmlException e)
/*  56:    */     {
/*  57: 89 */       throw new POIXMLException("Unable to read styles", e);
/*  58:    */     }
/*  59:    */     finally
/*  60:    */     {
/*  61: 91 */       is.close();
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected void commit()
/*  66:    */     throws IOException
/*  67:    */   {
/*  68: 97 */     if (this.ctStyles == null) {
/*  69: 98 */       throw new IllegalStateException("Unable to write out styles that were never read in!");
/*  70:    */     }
/*  71:101 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  72:102 */     xmlOptions.setSaveSyntheticDocumentElement(new QName(CTStyles.type.getName().getNamespaceURI(), "styles"));
/*  73:103 */     PackagePart part = getPackagePart();
/*  74:104 */     OutputStream out = part.getOutputStream();
/*  75:105 */     this.ctStyles.save(out, xmlOptions);
/*  76:106 */     out.close();
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected void ensureDocDefaults()
/*  80:    */   {
/*  81:110 */     if (!this.ctStyles.isSetDocDefaults()) {
/*  82:111 */       this.ctStyles.addNewDocDefaults();
/*  83:    */     }
/*  84:114 */     CTDocDefaults docDefaults = this.ctStyles.getDocDefaults();
/*  85:115 */     if (!docDefaults.isSetPPrDefault()) {
/*  86:116 */       docDefaults.addNewPPrDefault();
/*  87:    */     }
/*  88:117 */     if (!docDefaults.isSetRPrDefault()) {
/*  89:118 */       docDefaults.addNewRPrDefault();
/*  90:    */     }
/*  91:120 */     CTPPrDefault pprd = docDefaults.getPPrDefault();
/*  92:121 */     CTRPrDefault rprd = docDefaults.getRPrDefault();
/*  93:122 */     if (!pprd.isSetPPr()) {
/*  94:122 */       pprd.addNewPPr();
/*  95:    */     }
/*  96:123 */     if (!rprd.isSetRPr()) {
/*  97:123 */       rprd.addNewRPr();
/*  98:    */     }
/*  99:125 */     this.defaultRunStyle = new XWPFDefaultRunStyle(rprd.getRPr());
/* 100:126 */     this.defaultParaStyle = new XWPFDefaultParagraphStyle(pprd.getPPr());
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void setStyles(CTStyles styles)
/* 104:    */   {
/* 105:135 */     this.ctStyles = styles;
/* 106:138 */     for (CTStyle style : this.ctStyles.getStyleArray()) {
/* 107:139 */       this.listStyle.add(new XWPFStyle(style, this));
/* 108:    */     }
/* 109:141 */     if (this.ctStyles.isSetDocDefaults())
/* 110:    */     {
/* 111:142 */       CTDocDefaults docDefaults = this.ctStyles.getDocDefaults();
/* 112:143 */       if ((docDefaults.isSetRPrDefault()) && (docDefaults.getRPrDefault().isSetRPr())) {
/* 113:144 */         this.defaultRunStyle = new XWPFDefaultRunStyle(docDefaults.getRPrDefault().getRPr());
/* 114:    */       }
/* 115:147 */       if ((docDefaults.isSetPPrDefault()) && (docDefaults.getPPrDefault().isSetPPr())) {
/* 116:148 */         this.defaultParaStyle = new XWPFDefaultParagraphStyle(docDefaults.getPPrDefault().getPPr());
/* 117:    */       }
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   public boolean styleExist(String styleID)
/* 122:    */   {
/* 123:161 */     for (XWPFStyle style : this.listStyle) {
/* 124:162 */       if (style.getStyleId().equals(styleID)) {
/* 125:163 */         return true;
/* 126:    */       }
/* 127:    */     }
/* 128:165 */     return false;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void addStyle(XWPFStyle style)
/* 132:    */   {
/* 133:175 */     this.listStyle.add(style);
/* 134:176 */     this.ctStyles.addNewStyle();
/* 135:177 */     int pos = this.ctStyles.sizeOfStyleArray() - 1;
/* 136:178 */     this.ctStyles.setStyleArray(pos, style.getCTStyle());
/* 137:    */   }
/* 138:    */   
/* 139:    */   public XWPFStyle getStyle(String styleID)
/* 140:    */   {
/* 141:188 */     for (XWPFStyle style : this.listStyle) {
/* 142:    */       try
/* 143:    */       {
/* 144:190 */         if (style.getStyleId().equals(styleID)) {
/* 145:191 */           return style;
/* 146:    */         }
/* 147:    */       }
/* 148:    */       catch (NullPointerException e) {}
/* 149:    */     }
/* 150:196 */     return null;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public int getNumberOfStyles()
/* 154:    */   {
/* 155:200 */     return this.listStyle.size();
/* 156:    */   }
/* 157:    */   
/* 158:    */   public List<XWPFStyle> getUsedStyleList(XWPFStyle style)
/* 159:    */   {
/* 160:211 */     List<XWPFStyle> usedStyleList = new ArrayList();
/* 161:212 */     usedStyleList.add(style);
/* 162:213 */     return getUsedStyleList(style, usedStyleList);
/* 163:    */   }
/* 164:    */   
/* 165:    */   private List<XWPFStyle> getUsedStyleList(XWPFStyle style, List<XWPFStyle> usedStyleList)
/* 166:    */   {
/* 167:223 */     String basisStyleID = style.getBasisStyleID();
/* 168:224 */     XWPFStyle basisStyle = getStyle(basisStyleID);
/* 169:225 */     if ((basisStyle != null) && (!usedStyleList.contains(basisStyle)))
/* 170:    */     {
/* 171:226 */       usedStyleList.add(basisStyle);
/* 172:227 */       getUsedStyleList(basisStyle, usedStyleList);
/* 173:    */     }
/* 174:229 */     String linkStyleID = style.getLinkStyleID();
/* 175:230 */     XWPFStyle linkStyle = getStyle(linkStyleID);
/* 176:231 */     if ((linkStyle != null) && (!usedStyleList.contains(linkStyle)))
/* 177:    */     {
/* 178:232 */       usedStyleList.add(linkStyle);
/* 179:233 */       getUsedStyleList(linkStyle, usedStyleList);
/* 180:    */     }
/* 181:236 */     String nextStyleID = style.getNextStyleID();
/* 182:237 */     XWPFStyle nextStyle = getStyle(nextStyleID);
/* 183:238 */     if ((nextStyle != null) && (!usedStyleList.contains(nextStyle)))
/* 184:    */     {
/* 185:239 */       usedStyleList.add(linkStyle);
/* 186:240 */       getUsedStyleList(linkStyle, usedStyleList);
/* 187:    */     }
/* 188:242 */     return usedStyleList;
/* 189:    */   }
/* 190:    */   
/* 191:    */   protected CTLanguage getCTLanguage()
/* 192:    */   {
/* 193:246 */     ensureDocDefaults();
/* 194:    */     
/* 195:248 */     CTLanguage lang = null;
/* 196:249 */     if (this.defaultRunStyle.getRPr().isSetLang()) {
/* 197:250 */       lang = this.defaultRunStyle.getRPr().getLang();
/* 198:    */     } else {
/* 199:252 */       lang = this.defaultRunStyle.getRPr().addNewLang();
/* 200:    */     }
/* 201:255 */     return lang;
/* 202:    */   }
/* 203:    */   
/* 204:    */   public void setSpellingLanguage(String strSpellingLanguage)
/* 205:    */   {
/* 206:264 */     CTLanguage lang = getCTLanguage();
/* 207:265 */     lang.setVal(strSpellingLanguage);
/* 208:266 */     lang.setBidi(strSpellingLanguage);
/* 209:    */   }
/* 210:    */   
/* 211:    */   public void setEastAsia(String strEastAsia)
/* 212:    */   {
/* 213:275 */     CTLanguage lang = getCTLanguage();
/* 214:276 */     lang.setEastAsia(strEastAsia);
/* 215:    */   }
/* 216:    */   
/* 217:    */   public void setDefaultFonts(CTFonts fonts)
/* 218:    */   {
/* 219:285 */     ensureDocDefaults();
/* 220:    */     
/* 221:287 */     CTRPr runProps = this.defaultRunStyle.getRPr();
/* 222:288 */     runProps.setRFonts(fonts);
/* 223:    */   }
/* 224:    */   
/* 225:    */   public XWPFStyle getStyleWithSameName(XWPFStyle style)
/* 226:    */   {
/* 227:296 */     for (XWPFStyle ownStyle : this.listStyle) {
/* 228:297 */       if (ownStyle.hasSameName(style)) {
/* 229:298 */         return ownStyle;
/* 230:    */       }
/* 231:    */     }
/* 232:301 */     return null;
/* 233:    */   }
/* 234:    */   
/* 235:    */   public XWPFDefaultRunStyle getDefaultRunStyle()
/* 236:    */   {
/* 237:308 */     ensureDocDefaults();
/* 238:309 */     return this.defaultRunStyle;
/* 239:    */   }
/* 240:    */   
/* 241:    */   public XWPFDefaultParagraphStyle getDefaultParagraphStyle()
/* 242:    */   {
/* 243:316 */     ensureDocDefaults();
/* 244:317 */     return this.defaultParaStyle;
/* 245:    */   }
/* 246:    */   
/* 247:    */   public XWPFLatentStyles getLatentStyles()
/* 248:    */   {
/* 249:324 */     return this.latentStyles;
/* 250:    */   }
/* 251:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFStyles
 * JD-Core Version:    0.7.0.1
 */