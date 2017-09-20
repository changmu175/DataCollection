/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.nio.charset.Charset;
/*   6:    */ import java.util.EnumMap;
/*   7:    */ import java.util.Map;
/*   8:    */ import org.apache.poi.ss.usermodel.DifferentialStyleProvider;
/*   9:    */ import org.apache.poi.ss.usermodel.TableStyle;
/*  10:    */ import org.apache.poi.ss.usermodel.TableStyleType;
/*  11:    */ import org.apache.poi.util.DocumentHelper;
/*  12:    */ import org.apache.poi.util.IOUtils;
/*  13:    */ import org.apache.poi.xssf.model.StylesTable;
/*  14:    */ import org.w3c.dom.DOMConfiguration;
/*  15:    */ import org.w3c.dom.DOMImplementation;
/*  16:    */ import org.w3c.dom.Document;
/*  17:    */ import org.w3c.dom.Element;
/*  18:    */ import org.w3c.dom.Node;
/*  19:    */ import org.w3c.dom.NodeList;
/*  20:    */ import org.w3c.dom.ls.DOMImplementationLS;
/*  21:    */ import org.w3c.dom.ls.LSSerializer;
/*  22:    */ 
/*  23:    */ public enum XSSFBuiltinTableStyle
/*  24:    */ {
/*  25: 45 */   TableStyleDark1,  TableStyleDark2,  TableStyleDark3,  TableStyleDark4,  TableStyleDark5,  TableStyleDark6,  TableStyleDark7,  TableStyleDark8,  TableStyleDark9,  TableStyleDark10,  TableStyleDark11,  TableStyleLight1,  TableStyleLight2,  TableStyleLight3,  TableStyleLight4,  TableStyleLight5,  TableStyleLight6,  TableStyleLight7,  TableStyleLight8,  TableStyleLight9,  TableStyleLight10,  TableStyleLight11,  TableStyleLight12,  TableStyleLight13,  TableStyleLight14,  TableStyleLight15,  TableStyleLight16,  TableStyleLight17,  TableStyleLight18,  TableStyleLight19,  TableStyleLight20,  TableStyleLight21,  TableStyleMedium1,  TableStyleMedium2,  TableStyleMedium3,  TableStyleMedium4,  TableStyleMedium5,  TableStyleMedium6,  TableStyleMedium7,  TableStyleMedium8,  TableStyleMedium9,  TableStyleMedium10,  TableStyleMedium11,  TableStyleMedium12,  TableStyleMedium13,  TableStyleMedium14,  TableStyleMedium15,  TableStyleMedium16,  TableStyleMedium17,  TableStyleMedium18,  TableStyleMedium19,  TableStyleMedium20,  TableStyleMedium21,  TableStyleMedium22,  TableStyleMedium23,  TableStyleMedium24,  TableStyleMedium25,  TableStyleMedium26,  TableStyleMedium27,  TableStyleMedium28,  PivotStyleMedium1,  PivotStyleMedium2,  PivotStyleMedium3,  PivotStyleMedium4,  PivotStyleMedium5,  PivotStyleMedium6,  PivotStyleMedium7,  PivotStyleMedium8,  PivotStyleMedium9,  PivotStyleMedium10,  PivotStyleMedium11,  PivotStyleMedium12,  PivotStyleMedium13,  PivotStyleMedium14,  PivotStyleMedium15,  PivotStyleMedium16,  PivotStyleMedium17,  PivotStyleMedium18,  PivotStyleMedium19,  PivotStyleMedium20,  PivotStyleMedium21,  PivotStyleMedium22,  PivotStyleMedium23,  PivotStyleMedium24,  PivotStyleMedium25,  PivotStyleMedium26,  PivotStyleMedium27,  PivotStyleMedium28,  PivotStyleLight1,  PivotStyleLight2,  PivotStyleLight3,  PivotStyleLight4,  PivotStyleLight5,  PivotStyleLight6,  PivotStyleLight7,  PivotStyleLight8,  PivotStyleLight9,  PivotStyleLight10,  PivotStyleLight11,  PivotStyleLight12,  PivotStyleLight13,  PivotStyleLight14,  PivotStyleLight15,  PivotStyleLight16,  PivotStyleLight17,  PivotStyleLight18,  PivotStyleLight19,  PivotStyleLight20,  PivotStyleLight21,  PivotStyleLight22,  PivotStyleLight23,  PivotStyleLight24,  PivotStyleLight25,  PivotStyleLight26,  PivotStyleLight27,  PivotStyleLight28,  PivotStyleDark1,  PivotStyleDark2,  PivotStyleDark3,  PivotStyleDark4,  PivotStyleDark5,  PivotStyleDark6,  PivotStyleDark7,  PivotStyleDark8,  PivotStyleDark9,  PivotStyleDark10,  PivotStyleDark11,  PivotStyleDark12,  PivotStyleDark13,  PivotStyleDark14,  PivotStyleDark15,  PivotStyleDark16,  PivotStyleDark17,  PivotStyleDark18,  PivotStyleDark19,  PivotStyleDark20,  PivotStyleDark21,  PivotStyleDark22,  PivotStyleDark23,  PivotStyleDark24,  PivotStyleDark25,  PivotStyleDark26,  PivotStyleDark27,  PivotStyleDark28;
/*  26:    */   
/*  27:337 */   private static final Map<XSSFBuiltinTableStyle, TableStyle> styleMap = new EnumMap(XSSFBuiltinTableStyle.class);
/*  28:    */   
/*  29:    */   private XSSFBuiltinTableStyle() {}
/*  30:    */   
/*  31:    */   public TableStyle getStyle()
/*  32:    */   {
/*  33:346 */     init();
/*  34:347 */     return (TableStyle)styleMap.get(this);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public static boolean isBuiltinStyle(TableStyle style)
/*  38:    */   {
/*  39:356 */     if (style == null) {
/*  40:356 */       return false;
/*  41:    */     }
/*  42:    */     try
/*  43:    */     {
/*  44:358 */       valueOf(style.getName());
/*  45:359 */       return true;
/*  46:    */     }
/*  47:    */     catch (IllegalArgumentException e) {}
/*  48:361 */     return false;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static final synchronized void init()
/*  52:    */   {
/*  53:373 */     if (!styleMap.isEmpty()) {
/*  54:373 */       return;
/*  55:    */     }
/*  56:    */     try
/*  57:    */     {
/*  58:388 */       InputStream is = XSSFBuiltinTableStyle.class.getResourceAsStream("presetTableStyles.xml");
/*  59:    */       try
/*  60:    */       {
/*  61:390 */         Document doc = DocumentHelper.readDocument(is);
/*  62:    */         
/*  63:392 */         NodeList styleNodes = doc.getDocumentElement().getChildNodes();
/*  64:393 */         for (int i = 0; i < styleNodes.getLength(); i++)
/*  65:    */         {
/*  66:394 */           Node node = styleNodes.item(i);
/*  67:395 */           if (node.getNodeType() == 1)
/*  68:    */           {
/*  69:396 */             Element tag = (Element)node;
/*  70:397 */             String styleName = tag.getTagName();
/*  71:398 */             XSSFBuiltinTableStyle builtIn = valueOf(styleName);
/*  72:    */             
/*  73:400 */             Node dxfsNode = tag.getElementsByTagName("dxfs").item(0);
/*  74:401 */             Node tableStyleNode = tag.getElementsByTagName("tableStyles").item(0);
/*  75:    */             
/*  76:    */ 
/*  77:    */ 
/*  78:405 */             StylesTable styles = new StylesTable();
/*  79:406 */             styles.readFrom(new ByteArrayInputStream(styleXML(dxfsNode, tableStyleNode).getBytes(Charset.forName("UTF-8"))));
/*  80:407 */             styleMap.put(builtIn, new XSSFBuiltinTypeStyleStyle(builtIn, styles.getExplicitTableStyle(styleName)));
/*  81:    */           }
/*  82:    */         }
/*  83:    */       }
/*  84:    */       finally
/*  85:    */       {
/*  86:410 */         IOUtils.closeQuietly(is);
/*  87:    */       }
/*  88:    */     }
/*  89:    */     catch (Exception e)
/*  90:    */     {
/*  91:413 */       throw new RuntimeException(e);
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   private static String styleXML(Node dxfsNode, Node tableStyleNode)
/*  96:    */   {
/*  97:420 */     dxfsNode.insertBefore(dxfsNode.getOwnerDocument().createElement("dxf"), dxfsNode.getFirstChild());
/*  98:    */     
/*  99:422 */     DOMImplementationLS lsImpl = (DOMImplementationLS)dxfsNode.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
/* 100:423 */     LSSerializer lsSerializer = lsImpl.createLSSerializer();
/* 101:424 */     lsSerializer.getDomConfig().setParameter("xml-declaration", Boolean.valueOf(false));
/* 102:425 */     StringBuilder sb = new StringBuilder();
/* 103:426 */     sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n").append("<styleSheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\" xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" xmlns:x14ac=\"http://schemas.microsoft.com/office/spreadsheetml/2009/9/ac\" xmlns:x16r2=\"http://schemas.microsoft.com/office/spreadsheetml/2015/02/main\" mc:Ignorable=\"x14ac x16r2\">\n");
/* 104:    */     
/* 105:    */ 
/* 106:    */ 
/* 107:    */ 
/* 108:    */ 
/* 109:432 */     sb.append(lsSerializer.writeToString(dxfsNode));
/* 110:433 */     sb.append(lsSerializer.writeToString(tableStyleNode));
/* 111:434 */     sb.append("</styleSheet>");
/* 112:435 */     return sb.toString();
/* 113:    */   }
/* 114:    */   
/* 115:    */   protected static class XSSFBuiltinTypeStyleStyle
/* 116:    */     implements TableStyle
/* 117:    */   {
/* 118:    */     private final XSSFBuiltinTableStyle builtIn;
/* 119:    */     private final TableStyle style;
/* 120:    */     
/* 121:    */     protected XSSFBuiltinTypeStyleStyle(XSSFBuiltinTableStyle builtIn, TableStyle style)
/* 122:    */     {
/* 123:451 */       this.builtIn = builtIn;
/* 124:452 */       this.style = style;
/* 125:    */     }
/* 126:    */     
/* 127:    */     public String getName()
/* 128:    */     {
/* 129:456 */       return this.style.getName();
/* 130:    */     }
/* 131:    */     
/* 132:    */     public int getIndex()
/* 133:    */     {
/* 134:460 */       return this.builtIn.ordinal();
/* 135:    */     }
/* 136:    */     
/* 137:    */     public boolean isBuiltin()
/* 138:    */     {
/* 139:464 */       return true;
/* 140:    */     }
/* 141:    */     
/* 142:    */     public DifferentialStyleProvider getStyle(TableStyleType type)
/* 143:    */     {
/* 144:468 */       return this.style.getStyle(type);
/* 145:    */     }
/* 146:    */   }
/* 147:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFBuiltinTableStyle
 * JD-Core Version:    0.7.0.1
 */