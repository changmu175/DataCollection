/*   1:    */ package org.apache.poi.xssf.model;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.util.Collection;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.Map;
/*   9:    */ import org.apache.poi.POIXMLDocumentPart;
/*  10:    */ import org.apache.poi.POIXMLTypeLoader;
/*  11:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  12:    */ import org.apache.poi.xssf.usermodel.XSSFMap;
/*  13:    */ import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/*  14:    */ import org.apache.xmlbeans.XmlException;
/*  15:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap;
/*  16:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo;
/*  17:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo.Factory;
/*  18:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema;
/*  19:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.MapInfoDocument;
/*  20:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.MapInfoDocument.Factory;
/*  21:    */ 
/*  22:    */ public class MapInfo
/*  23:    */   extends POIXMLDocumentPart
/*  24:    */ {
/*  25:    */   private CTMapInfo mapInfo;
/*  26:    */   private Map<Integer, XSSFMap> maps;
/*  27:    */   
/*  28:    */   public MapInfo()
/*  29:    */   {
/*  30: 59 */     this.mapInfo = CTMapInfo.Factory.newInstance();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public MapInfo(PackagePart part)
/*  34:    */     throws IOException
/*  35:    */   {
/*  36: 67 */     super(part);
/*  37: 68 */     readFrom(part.getInputStream());
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void readFrom(InputStream is)
/*  41:    */     throws IOException
/*  42:    */   {
/*  43:    */     try
/*  44:    */     {
/*  45: 73 */       MapInfoDocument doc = MapInfoDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  46: 74 */       this.mapInfo = doc.getMapInfo();
/*  47:    */       
/*  48: 76 */       this.maps = new HashMap();
/*  49: 77 */       for (CTMap map : this.mapInfo.getMapArray()) {
/*  50: 78 */         this.maps.put(Integer.valueOf((int)map.getID()), new XSSFMap(map, this));
/*  51:    */       }
/*  52:    */     }
/*  53:    */     catch (XmlException e)
/*  54:    */     {
/*  55: 82 */       throw new IOException(e.getLocalizedMessage());
/*  56:    */     }
/*  57:    */   }
/*  58:    */   
/*  59:    */   public XSSFWorkbook getWorkbook()
/*  60:    */   {
/*  61: 92 */     return (XSSFWorkbook)getParent();
/*  62:    */   }
/*  63:    */   
/*  64:    */   public CTMapInfo getCTMapInfo()
/*  65:    */   {
/*  66:100 */     return this.mapInfo;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public CTSchema getCTSchemaById(String schemaId)
/*  70:    */   {
/*  71:110 */     CTSchema xmlSchema = null;
/*  72:112 */     for (CTSchema schema : this.mapInfo.getSchemaArray()) {
/*  73:113 */       if (schema.getID().equals(schemaId))
/*  74:    */       {
/*  75:114 */         xmlSchema = schema;
/*  76:115 */         break;
/*  77:    */       }
/*  78:    */     }
/*  79:118 */     return xmlSchema;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public XSSFMap getXSSFMapById(int id)
/*  83:    */   {
/*  84:123 */     return (XSSFMap)this.maps.get(Integer.valueOf(id));
/*  85:    */   }
/*  86:    */   
/*  87:    */   public XSSFMap getXSSFMapByName(String name)
/*  88:    */   {
/*  89:128 */     XSSFMap matchedMap = null;
/*  90:130 */     for (XSSFMap map : this.maps.values()) {
/*  91:131 */       if ((map.getCtMap().getName() != null) && (map.getCtMap().getName().equals(name))) {
/*  92:132 */         matchedMap = map;
/*  93:    */       }
/*  94:    */     }
/*  95:136 */     return matchedMap;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public Collection<XSSFMap> getAllXSSFMaps()
/*  99:    */   {
/* 100:144 */     return this.maps.values();
/* 101:    */   }
/* 102:    */   
/* 103:    */   protected void writeTo(OutputStream out)
/* 104:    */     throws IOException
/* 105:    */   {
/* 106:148 */     MapInfoDocument doc = MapInfoDocument.Factory.newInstance();
/* 107:149 */     doc.setMapInfo(this.mapInfo);
/* 108:150 */     doc.save(out, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 109:    */   }
/* 110:    */   
/* 111:    */   protected void commit()
/* 112:    */     throws IOException
/* 113:    */   {
/* 114:155 */     PackagePart part = getPackagePart();
/* 115:156 */     OutputStream out = part.getOutputStream();
/* 116:157 */     writeTo(out);
/* 117:158 */     out.close();
/* 118:    */   }
/* 119:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.model.MapInfo
 * JD-Core Version:    0.7.0.1
 */