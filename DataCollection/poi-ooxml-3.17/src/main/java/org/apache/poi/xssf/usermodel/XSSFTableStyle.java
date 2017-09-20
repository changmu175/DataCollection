/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.EnumMap;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import org.apache.poi.ss.usermodel.DifferentialStyleProvider;
/*   8:    */ import org.apache.poi.ss.usermodel.TableStyle;
/*   9:    */ import org.apache.poi.ss.usermodel.TableStyleType;
/*  10:    */ import org.apache.poi.util.POILogFactory;
/*  11:    */ import org.apache.poi.util.POILogger;
/*  12:    */ import org.apache.xmlbeans.XmlCursor;
/*  13:    */ import org.apache.xmlbeans.XmlException;
/*  14:    */ import org.apache.xmlbeans.XmlObject;
/*  15:    */ import org.apache.xmlbeans.XmlOptions;
/*  16:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf;
/*  17:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf.Factory;
/*  18:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxfs;
/*  19:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle;
/*  20:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleElement;
/*  21:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STTableStyleType.Enum;
/*  22:    */ import org.w3c.dom.Node;
/*  23:    */ 
/*  24:    */ public class XSSFTableStyle
/*  25:    */   implements TableStyle
/*  26:    */ {
/*  27: 44 */   private static final POILogger logger = POILogFactory.getLogger(XSSFTableStyle.class);
/*  28:    */   private final String name;
/*  29:    */   private final int index;
/*  30: 48 */   private final Map<TableStyleType, DifferentialStyleProvider> elementMap = new EnumMap(TableStyleType.class);
/*  31:    */   
/*  32:    */   public XSSFTableStyle(int index, CTDxfs dxfs, CTTableStyle tableStyle, IndexedColorMap colorMap)
/*  33:    */   {
/*  34: 58 */     this.name = tableStyle.getName();
/*  35: 59 */     this.index = index;
/*  36:    */     
/*  37: 61 */     List<CTDxf> dxfList = new ArrayList();
/*  38:    */     
/*  39:    */ 
/*  40: 64 */     XmlCursor cur = dxfs.newCursor();
/*  41:    */     
/*  42: 66 */     String xquery = "declare namespace x='http://schemas.openxmlformats.org/spreadsheetml/2006/main' .//x:dxf | .//dxf";
/*  43: 67 */     cur.selectPath(xquery);
/*  44: 68 */     while (cur.toNextSelection())
/*  45:    */     {
/*  46: 69 */       XmlObject obj = cur.getObject();
/*  47: 70 */       String parentName = obj.getDomNode().getParentNode().getNodeName();
/*  48: 72 */       if ((parentName.equals("mc:Fallback")) || (parentName.equals("x:dxfs")) || (parentName.contentEquals("dxfs"))) {
/*  49:    */         try
/*  50:    */         {
/*  51:    */           CTDxf dxf;
/*  52:    */           CTDxf dxf;
/*  53: 75 */           if ((obj instanceof CTDxf)) {
/*  54: 76 */             dxf = (CTDxf)obj;
/*  55:    */           } else {
/*  56: 78 */             dxf = CTDxf.Factory.parse(obj.newXMLStreamReader(), new XmlOptions().setDocumentType(CTDxf.type));
/*  57:    */           }
/*  58: 80 */           if (dxf != null) {
/*  59: 80 */             dxfList.add(dxf);
/*  60:    */           }
/*  61:    */         }
/*  62:    */         catch (XmlException e)
/*  63:    */         {
/*  64: 82 */           logger.log(5, new Object[] { "Error parsing XSSFTableStyle", e });
/*  65:    */         }
/*  66:    */       }
/*  67:    */     }
/*  68: 87 */     for (CTTableStyleElement element : tableStyle.getTableStyleElementList())
/*  69:    */     {
/*  70: 88 */       TableStyleType type = TableStyleType.valueOf(element.getType().toString());
/*  71: 89 */       DifferentialStyleProvider dstyle = null;
/*  72: 90 */       if (element.isSetDxfId())
/*  73:    */       {
/*  74: 91 */         int idx = (int)element.getDxfId();
/*  75:    */         
/*  76: 93 */         CTDxf dxf = (CTDxf)dxfList.get(idx);
/*  77: 94 */         int stripeSize = 0;
/*  78: 95 */         if (element.isSetSize()) {
/*  79: 95 */           stripeSize = (int)element.getSize();
/*  80:    */         }
/*  81: 96 */         if (dxf != null) {
/*  82: 96 */           dstyle = new XSSFDxfStyleProvider(dxf, stripeSize, colorMap);
/*  83:    */         }
/*  84:    */       }
/*  85: 98 */       this.elementMap.put(type, dstyle);
/*  86:    */     }
/*  87:    */   }
/*  88:    */   
/*  89:    */   public String getName()
/*  90:    */   {
/*  91:103 */     return this.name;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public int getIndex()
/*  95:    */   {
/*  96:107 */     return this.index;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public boolean isBuiltin()
/* 100:    */   {
/* 101:114 */     return false;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public DifferentialStyleProvider getStyle(TableStyleType type)
/* 105:    */   {
/* 106:118 */     return (DifferentialStyleProvider)this.elementMap.get(type);
/* 107:    */   }
/* 108:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFTableStyle
 * JD-Core Version:    0.7.0.1
 */