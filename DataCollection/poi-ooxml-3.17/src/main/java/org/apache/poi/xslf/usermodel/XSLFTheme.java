/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Map;
/*   7:    */ import javax.xml.namespace.QName;
/*   8:    */ import org.apache.poi.POIXMLDocumentPart;
/*   9:    */ import org.apache.poi.POIXMLTypeLoader;
/*  10:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  11:    */ import org.apache.poi.util.Internal;
/*  12:    */ import org.apache.xmlbeans.XmlException;
/*  13:    */ import org.apache.xmlbeans.XmlObject;
/*  14:    */ import org.apache.xmlbeans.XmlOptions;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTColor;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet.Factory;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
/*  24:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;
/*  25:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum;
/*  26:    */ import org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument;
/*  27:    */ import org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument.Factory;
/*  28:    */ import org.w3c.dom.Node;
/*  29:    */ 
/*  30:    */ public class XSLFTheme
/*  31:    */   extends POIXMLDocumentPart
/*  32:    */ {
/*  33:    */   private CTOfficeStyleSheet _theme;
/*  34:    */   private Map<String, CTColor> _schemeColors;
/*  35:    */   
/*  36:    */   XSLFTheme()
/*  37:    */   {
/*  38: 55 */     this._theme = CTOfficeStyleSheet.Factory.newInstance();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public XSLFTheme(PackagePart part)
/*  42:    */     throws IOException, XmlException
/*  43:    */   {
/*  44: 62 */     super(part);
/*  45: 63 */     ThemeDocument doc = ThemeDocument.Factory.parse(getPackagePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  46:    */     
/*  47: 65 */     this._theme = doc.getTheme();
/*  48: 66 */     initialize();
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void importTheme(XSLFTheme theme)
/*  52:    */   {
/*  53: 70 */     this._theme = theme.getXmlObject();
/*  54: 71 */     this._schemeColors = theme._schemeColors;
/*  55:    */   }
/*  56:    */   
/*  57:    */   private void initialize()
/*  58:    */   {
/*  59: 75 */     CTBaseStyles elems = this._theme.getThemeElements();
/*  60: 76 */     CTColorScheme scheme = elems.getClrScheme();
/*  61:    */     
/*  62: 78 */     this._schemeColors = new HashMap(12);
/*  63: 79 */     for (XmlObject o : scheme.selectPath("*"))
/*  64:    */     {
/*  65: 80 */       CTColor c = (CTColor)o;
/*  66: 81 */       String name = c.getDomNode().getLocalName();
/*  67: 82 */       this._schemeColors.put(name, c);
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71:    */   void initColorMap(CTColorMapping cmap)
/*  72:    */   {
/*  73: 92 */     this._schemeColors.put("bg1", this._schemeColors.get(cmap.getBg1().toString()));
/*  74: 93 */     this._schemeColors.put("bg2", this._schemeColors.get(cmap.getBg2().toString()));
/*  75: 94 */     this._schemeColors.put("tx1", this._schemeColors.get(cmap.getTx1().toString()));
/*  76: 95 */     this._schemeColors.put("tx2", this._schemeColors.get(cmap.getTx2().toString()));
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String getName()
/*  80:    */   {
/*  81:103 */     return this._theme.getName();
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void setName(String name)
/*  85:    */   {
/*  86:112 */     this._theme.setName(name);
/*  87:    */   }
/*  88:    */   
/*  89:    */   CTColor getCTColor(String name)
/*  90:    */   {
/*  91:121 */     return (CTColor)this._schemeColors.get(name);
/*  92:    */   }
/*  93:    */   
/*  94:    */   @Internal
/*  95:    */   public CTOfficeStyleSheet getXmlObject()
/*  96:    */   {
/*  97:129 */     return this._theme;
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected final void commit()
/* 101:    */     throws IOException
/* 102:    */   {
/* 103:133 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 104:134 */     xmlOptions.setSaveSyntheticDocumentElement(new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "theme"));
/* 105:    */     
/* 106:    */ 
/* 107:137 */     PackagePart part = getPackagePart();
/* 108:138 */     OutputStream out = part.getOutputStream();
/* 109:139 */     getXmlObject().save(out, xmlOptions);
/* 110:140 */     out.close();
/* 111:    */   }
/* 112:    */   
/* 113:    */   public String getMajorFont()
/* 114:    */   {
/* 115:149 */     return this._theme.getThemeElements().getFontScheme().getMajorFont().getLatin().getTypeface();
/* 116:    */   }
/* 117:    */   
/* 118:    */   public String getMinorFont()
/* 119:    */   {
/* 120:158 */     return this._theme.getThemeElements().getFontScheme().getMinorFont().getLatin().getTypeface();
/* 121:    */   }
/* 122:    */   
/* 123:    */   CTTextParagraphProperties getDefaultParagraphStyle()
/* 124:    */   {
/* 125:163 */     XmlObject[] o = this._theme.selectPath("declare namespace p='http://schemas.openxmlformats.org/presentationml/2006/main' declare namespace a='http://schemas.openxmlformats.org/drawingml/2006/main' .//a:objectDefaults/a:spDef/a:lstStyle/a:defPPr");
/* 126:167 */     if (o.length == 1) {
/* 127:168 */       return (CTTextParagraphProperties)o[0];
/* 128:    */     }
/* 129:170 */     return null;
/* 130:    */   }
/* 131:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFTheme
 * JD-Core Version:    0.7.0.1
 */