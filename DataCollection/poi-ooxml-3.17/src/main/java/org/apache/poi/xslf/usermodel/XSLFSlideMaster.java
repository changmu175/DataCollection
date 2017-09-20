/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Locale;
/*   7:    */ import java.util.Map;
/*   8:    */ import org.apache.poi.POIXMLDocumentPart;
/*   9:    */ import org.apache.poi.POIXMLTypeLoader;
/*  10:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  11:    */ import org.apache.poi.sl.usermodel.MasterSheet;
/*  12:    */ import org.apache.poi.sl.usermodel.Placeholder;
/*  13:    */ import org.apache.xmlbeans.XmlException;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle;
/*  16:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTBackground;
/*  17:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData;
/*  18:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder;
/*  19:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster;
/*  20:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster.Factory;
/*  21:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterTextStyles;
/*  22:    */ import org.openxmlformats.schemas.presentationml.x2006.main.SldMasterDocument;
/*  23:    */ import org.openxmlformats.schemas.presentationml.x2006.main.SldMasterDocument.Factory;
/*  24:    */ 
/*  25:    */ public class XSLFSlideMaster
/*  26:    */   extends XSLFSheet
/*  27:    */   implements MasterSheet<XSLFShape, XSLFTextParagraph>
/*  28:    */ {
/*  29:    */   private CTSlideMaster _slide;
/*  30:    */   private Map<String, XSLFSlideLayout> _layouts;
/*  31:    */   private XSLFTheme _theme;
/*  32:    */   
/*  33:    */   XSLFSlideMaster()
/*  34:    */   {
/*  35: 68 */     this._slide = CTSlideMaster.Factory.newInstance();
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected XSLFSlideMaster(PackagePart part)
/*  39:    */     throws IOException, XmlException
/*  40:    */   {
/*  41: 75 */     super(part);
/*  42: 76 */     SldMasterDocument doc = SldMasterDocument.Factory.parse(getPackagePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  43:    */     
/*  44: 78 */     this._slide = doc.getSldMaster();
/*  45: 79 */     setCommonSlideData(this._slide.getCSld());
/*  46:    */   }
/*  47:    */   
/*  48:    */   public CTSlideMaster getXmlObject()
/*  49:    */   {
/*  50: 84 */     return this._slide;
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected String getRootElementName()
/*  54:    */   {
/*  55: 89 */     return "sldMaster";
/*  56:    */   }
/*  57:    */   
/*  58:    */   public XSLFSlideMaster getMasterSheet()
/*  59:    */   {
/*  60: 94 */     return null;
/*  61:    */   }
/*  62:    */   
/*  63:    */   private Map<String, XSLFSlideLayout> getLayouts()
/*  64:    */   {
/*  65: 98 */     if (this._layouts == null)
/*  66:    */     {
/*  67: 99 */       this._layouts = new HashMap();
/*  68:100 */       for (POIXMLDocumentPart p : getRelations()) {
/*  69:101 */         if ((p instanceof XSLFSlideLayout))
/*  70:    */         {
/*  71:102 */           XSLFSlideLayout layout = (XSLFSlideLayout)p;
/*  72:103 */           this._layouts.put(layout.getName().toLowerCase(Locale.ROOT), layout);
/*  73:    */         }
/*  74:    */       }
/*  75:    */     }
/*  76:107 */     return this._layouts;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public XSLFSlideLayout[] getSlideLayouts()
/*  80:    */   {
/*  81:115 */     return (XSLFSlideLayout[])getLayouts().values().toArray(new XSLFSlideLayout[this._layouts.size()]);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public XSLFSlideLayout getLayout(SlideLayout type)
/*  85:    */   {
/*  86:126 */     for (XSLFSlideLayout layout : getLayouts().values()) {
/*  87:127 */       if (layout.getType() == type) {
/*  88:128 */         return layout;
/*  89:    */       }
/*  90:    */     }
/*  91:131 */     return null;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public XSLFSlideLayout getLayout(String name)
/*  95:    */   {
/*  96:142 */     return (XSLFSlideLayout)getLayouts().get(name.toLowerCase(Locale.ROOT));
/*  97:    */   }
/*  98:    */   
/*  99:    */   public XSLFTheme getTheme()
/* 100:    */   {
/* 101:148 */     if (this._theme == null) {
/* 102:149 */       for (POIXMLDocumentPart p : getRelations()) {
/* 103:150 */         if ((p instanceof XSLFTheme))
/* 104:    */         {
/* 105:151 */           this._theme = ((XSLFTheme)p);
/* 106:152 */           CTColorMapping cmap = this._slide.getClrMap();
/* 107:153 */           if (cmap == null) {
/* 108:    */             break;
/* 109:    */           }
/* 110:154 */           this._theme.initColorMap(cmap); break;
/* 111:    */         }
/* 112:    */       }
/* 113:    */     }
/* 114:160 */     return this._theme;
/* 115:    */   }
/* 116:    */   
/* 117:    */   protected CTTextListStyle getTextProperties(Placeholder textType)
/* 118:    */   {
/* 119:165 */     CTSlideMasterTextStyles txStyles = getXmlObject().getTxStyles();
/* 120:    */     CTTextListStyle props;
/* 121:166 */     switch (1.$SwitchMap$org$apache$poi$sl$usermodel$Placeholder[textType.ordinal()])
/* 122:    */     {
/* 123:    */     case 1: 
/* 124:    */     case 2: 
/* 125:    */     case 3: 
/* 126:170 */       props = txStyles.getTitleStyle();
/* 127:171 */       break;
/* 128:    */     case 4: 
/* 129:173 */       props = txStyles.getBodyStyle();
/* 130:174 */       break;
/* 131:    */     default: 
/* 132:176 */       props = txStyles.getOtherStyle();
/* 133:    */     }
/* 134:179 */     return props;
/* 135:    */   }
/* 136:    */   
/* 137:    */   protected boolean canDraw(XSLFShape shape)
/* 138:    */   {
/* 139:188 */     if ((shape instanceof XSLFSimpleShape))
/* 140:    */     {
/* 141:189 */       XSLFSimpleShape txt = (XSLFSimpleShape)shape;
/* 142:190 */       CTPlaceholder ph = txt.getCTPlaceholder();
/* 143:191 */       if (ph != null) {
/* 144:192 */         return false;
/* 145:    */       }
/* 146:    */     }
/* 147:195 */     return true;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public XSLFBackground getBackground()
/* 151:    */   {
/* 152:200 */     CTBackground bg = this._slide.getCSld().getBg();
/* 153:201 */     if (bg != null) {
/* 154:202 */       return new XSLFBackground(bg, this);
/* 155:    */     }
/* 156:204 */     return null;
/* 157:    */   }
/* 158:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFSlideMaster
 * JD-Core Version:    0.7.0.1
 */