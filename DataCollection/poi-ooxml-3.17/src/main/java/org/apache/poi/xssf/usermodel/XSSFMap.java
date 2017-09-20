/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.POIXMLDocumentPart;
/*   6:    */ import org.apache.poi.POIXMLDocumentPart.RelationPart;
/*   7:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*   8:    */ import org.apache.poi.ss.usermodel.Sheet;
/*   9:    */ import org.apache.poi.util.Internal;
/*  10:    */ import org.apache.poi.xssf.model.MapInfo;
/*  11:    */ import org.apache.poi.xssf.model.SingleXmlCells;
/*  12:    */ import org.apache.poi.xssf.usermodel.helpers.XSSFSingleXmlCell;
/*  13:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap;
/*  14:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema;
/*  15:    */ import org.w3c.dom.Node;
/*  16:    */ 
/*  17:    */ public class XSSFMap
/*  18:    */ {
/*  19:    */   private CTMap ctMap;
/*  20:    */   private MapInfo mapInfo;
/*  21:    */   
/*  22:    */   public XSSFMap(CTMap ctMap, MapInfo mapInfo)
/*  23:    */   {
/*  24: 50 */     this.ctMap = ctMap;
/*  25: 51 */     this.mapInfo = mapInfo;
/*  26:    */   }
/*  27:    */   
/*  28:    */   @Internal
/*  29:    */   public CTMap getCtMap()
/*  30:    */   {
/*  31: 57 */     return this.ctMap;
/*  32:    */   }
/*  33:    */   
/*  34:    */   @Internal
/*  35:    */   public CTSchema getCTSchema()
/*  36:    */   {
/*  37: 62 */     String schemaId = this.ctMap.getSchemaID();
/*  38: 63 */     return this.mapInfo.getCTSchemaById(schemaId);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public Node getSchema()
/*  42:    */   {
/*  43: 67 */     Node xmlSchema = null;
/*  44:    */     
/*  45: 69 */     CTSchema schema = getCTSchema();
/*  46: 70 */     xmlSchema = schema.getDomNode().getFirstChild();
/*  47:    */     
/*  48: 72 */     return xmlSchema;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public List<XSSFSingleXmlCell> getRelatedSingleXMLCell()
/*  52:    */   {
/*  53: 79 */     List<XSSFSingleXmlCell> relatedSimpleXmlCells = new ArrayList();
/*  54:    */     
/*  55: 81 */     int sheetNumber = this.mapInfo.getWorkbook().getNumberOfSheets();
/*  56: 82 */     for (int i = 0; i < sheetNumber; i++)
/*  57:    */     {
/*  58: 83 */       XSSFSheet sheet = this.mapInfo.getWorkbook().getSheetAt(i);
/*  59: 84 */       for (POIXMLDocumentPart p : sheet.getRelations()) {
/*  60: 85 */         if ((p instanceof SingleXmlCells))
/*  61:    */         {
/*  62: 86 */           SingleXmlCells singleXMLCells = (SingleXmlCells)p;
/*  63: 87 */           for (XSSFSingleXmlCell cell : singleXMLCells.getAllSimpleXmlCell()) {
/*  64: 88 */             if (cell.getMapId() == this.ctMap.getID()) {
/*  65: 89 */               relatedSimpleXmlCells.add(cell);
/*  66:    */             }
/*  67:    */           }
/*  68:    */         }
/*  69:    */       }
/*  70:    */     }
/*  71: 95 */     return relatedSimpleXmlCells;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public List<XSSFTable> getRelatedTables()
/*  75:    */   {
/*  76:102 */     List<XSSFTable> tables = new ArrayList();
/*  77:103 */     for (Sheet sheet : this.mapInfo.getWorkbook()) {
/*  78:104 */       for (POIXMLDocumentPart.RelationPart rp : ((XSSFSheet)sheet).getRelationParts()) {
/*  79:105 */         if (rp.getRelationship().getRelationshipType().equals(XSSFRelation.TABLE.getRelation()))
/*  80:    */         {
/*  81:106 */           XSSFTable table = (XSSFTable)rp.getDocumentPart();
/*  82:107 */           if (table.mapsTo(this.ctMap.getID())) {
/*  83:108 */             tables.add(table);
/*  84:    */           }
/*  85:    */         }
/*  86:    */       }
/*  87:    */     }
/*  88:114 */     return tables;
/*  89:    */   }
/*  90:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFMap
 * JD-Core Version:    0.7.0.1
 */