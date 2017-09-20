/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.util.Date;
/*   7:    */ import javax.xml.namespace.QName;
/*   8:    */ import org.apache.poi.POIXMLDocumentPart;
/*   9:    */ import org.apache.poi.POIXMLTypeLoader;
/*  10:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  11:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*  12:    */ import org.apache.poi.ss.usermodel.Cell;
/*  13:    */ import org.apache.poi.ss.usermodel.CellType;
/*  14:    */ import org.apache.poi.ss.usermodel.Name;
/*  15:    */ import org.apache.poi.ss.usermodel.Row;
/*  16:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  17:    */ import org.apache.poi.ss.usermodel.Workbook;
/*  18:    */ import org.apache.poi.ss.util.AreaReference;
/*  19:    */ import org.apache.poi.ss.util.CellReference;
/*  20:    */ import org.apache.poi.util.Internal;
/*  21:    */ import org.apache.xmlbeans.SchemaType;
/*  22:    */ import org.apache.xmlbeans.XmlException;
/*  23:    */ import org.apache.xmlbeans.XmlOptions;
/*  24:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheField;
/*  25:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields;
/*  26:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource;
/*  27:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition;
/*  28:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition.Factory;
/*  29:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheetSource;
/*  30:    */ 
/*  31:    */ public class XSSFPivotCacheDefinition
/*  32:    */   extends POIXMLDocumentPart
/*  33:    */ {
/*  34:    */   private CTPivotCacheDefinition ctPivotCacheDefinition;
/*  35:    */   
/*  36:    */   public XSSFPivotCacheDefinition()
/*  37:    */   {
/*  38: 55 */     this.ctPivotCacheDefinition = CTPivotCacheDefinition.Factory.newInstance();
/*  39: 56 */     createDefaultValues();
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected XSSFPivotCacheDefinition(PackagePart part)
/*  43:    */     throws IOException
/*  44:    */   {
/*  45: 69 */     super(part);
/*  46: 70 */     readFrom(part.getInputStream());
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void readFrom(InputStream is)
/*  50:    */     throws IOException
/*  51:    */   {
/*  52:    */     try
/*  53:    */     {
/*  54: 76 */       XmlOptions options = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  55:    */       
/*  56: 78 */       options.setLoadReplaceDocumentElement(null);
/*  57: 79 */       this.ctPivotCacheDefinition = CTPivotCacheDefinition.Factory.parse(is, options);
/*  58:    */     }
/*  59:    */     catch (XmlException e)
/*  60:    */     {
/*  61: 81 */       throw new IOException(e.getLocalizedMessage(), e);
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   @Internal
/*  66:    */   public CTPivotCacheDefinition getCTPivotCacheDefinition()
/*  67:    */   {
/*  68: 88 */     return this.ctPivotCacheDefinition;
/*  69:    */   }
/*  70:    */   
/*  71:    */   private void createDefaultValues()
/*  72:    */   {
/*  73: 93 */     this.ctPivotCacheDefinition.setCreatedVersion((short)3);
/*  74: 94 */     this.ctPivotCacheDefinition.setMinRefreshableVersion((short)3);
/*  75: 95 */     this.ctPivotCacheDefinition.setRefreshedVersion((short)3);
/*  76: 96 */     this.ctPivotCacheDefinition.setRefreshedBy("Apache POI");
/*  77: 97 */     this.ctPivotCacheDefinition.setRefreshedDate(new Date().getTime());
/*  78: 98 */     this.ctPivotCacheDefinition.setRefreshOnLoad(true);
/*  79:    */   }
/*  80:    */   
/*  81:    */   protected void commit()
/*  82:    */     throws IOException
/*  83:    */   {
/*  84:104 */     PackagePart part = getPackagePart();
/*  85:105 */     OutputStream out = part.getOutputStream();
/*  86:106 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  87:    */     
/*  88:108 */     xmlOptions.setSaveSyntheticDocumentElement(new QName(CTPivotCacheDefinition.type.getName().getNamespaceURI(), "pivotCacheDefinition"));
/*  89:    */     
/*  90:110 */     this.ctPivotCacheDefinition.save(out, xmlOptions);
/*  91:111 */     out.close();
/*  92:    */   }
/*  93:    */   
/*  94:    */   public AreaReference getPivotArea(Workbook wb)
/*  95:    */     throws IllegalArgumentException
/*  96:    */   {
/*  97:121 */     CTWorksheetSource wsSource = this.ctPivotCacheDefinition.getCacheSource().getWorksheetSource();
/*  98:    */     
/*  99:123 */     String ref = wsSource.getRef();
/* 100:124 */     String name = wsSource.getName();
/* 101:126 */     if ((ref == null) && (name == null)) {
/* 102:127 */       throw new IllegalArgumentException("Pivot cache must reference an area, named range, or table.");
/* 103:    */     }
/* 104:131 */     if (ref != null) {
/* 105:132 */       return new AreaReference(ref, SpreadsheetVersion.EXCEL2007);
/* 106:    */     }
/* 107:135 */     assert (name != null);
/* 108:    */     
/* 109:    */ 
/* 110:138 */     Name range = wb.getName(name);
/* 111:139 */     if (range != null) {
/* 112:140 */       return new AreaReference(range.getRefersToFormula(), SpreadsheetVersion.EXCEL2007);
/* 113:    */     }
/* 114:145 */     XSSFSheet sheet = (XSSFSheet)wb.getSheet(wsSource.getSheet());
/* 115:146 */     for (XSSFTable table : sheet.getTables()) {
/* 116:148 */       if (name.equals(table.getName())) {
/* 117:149 */         return new AreaReference(table.getStartCellReference(), table.getEndCellReference(), SpreadsheetVersion.EXCEL2007);
/* 118:    */       }
/* 119:    */     }
/* 120:154 */     throw new IllegalArgumentException("Name '" + name + "' was not found.");
/* 121:    */   }
/* 122:    */   
/* 123:    */   protected void createCacheFields(Sheet sheet)
/* 124:    */   {
/* 125:164 */     AreaReference ar = getPivotArea(sheet.getWorkbook());
/* 126:165 */     CellReference firstCell = ar.getFirstCell();
/* 127:166 */     CellReference lastCell = ar.getLastCell();
/* 128:167 */     int columnStart = firstCell.getCol();
/* 129:168 */     int columnEnd = lastCell.getCol();
/* 130:169 */     Row row = sheet.getRow(firstCell.getRow());
/* 131:    */     CTCacheFields cFields;
/* 132:    */     CTCacheFields cFields;
/* 133:171 */     if (this.ctPivotCacheDefinition.getCacheFields() != null) {
/* 134:172 */       cFields = this.ctPivotCacheDefinition.getCacheFields();
/* 135:    */     } else {
/* 136:174 */       cFields = this.ctPivotCacheDefinition.addNewCacheFields();
/* 137:    */     }
/* 138:177 */     for (int i = columnStart; i <= columnEnd; i++)
/* 139:    */     {
/* 140:178 */       CTCacheField cf = cFields.addNewCacheField();
/* 141:179 */       if (i == columnEnd) {
/* 142:180 */         cFields.setCount(cFields.sizeOfCacheFieldArray());
/* 143:    */       }
/* 144:183 */       cf.setNumFmtId(0L);
/* 145:184 */       Cell cell = row.getCell(i);
/* 146:185 */       cell.setCellType(CellType.STRING);
/* 147:186 */       cf.setName(row.getCell(i).getStringCellValue());
/* 148:187 */       cf.addNewSharedItems();
/* 149:    */     }
/* 150:    */   }
/* 151:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFPivotCacheDefinition
 * JD-Core Version:    0.7.0.1
 */