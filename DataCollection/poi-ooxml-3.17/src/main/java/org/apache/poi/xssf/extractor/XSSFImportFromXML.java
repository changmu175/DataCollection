/*   1:    */ package org.apache.poi.xssf.extractor;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.StringReader;
/*   5:    */ import java.text.DateFormat;
/*   6:    */ import java.text.ParseException;
/*   7:    */ import java.text.SimpleDateFormat;
/*   8:    */ import java.util.Arrays;
/*   9:    */ import java.util.Date;
/*  10:    */ import java.util.HashSet;
/*  11:    */ import java.util.Iterator;
/*  12:    */ import java.util.List;
/*  13:    */ import java.util.Set;
/*  14:    */ import javax.xml.namespace.NamespaceContext;
/*  15:    */ import javax.xml.parsers.DocumentBuilder;
/*  16:    */ import javax.xml.xpath.XPath;
/*  17:    */ import javax.xml.xpath.XPathConstants;
/*  18:    */ import javax.xml.xpath.XPathExpressionException;
/*  19:    */ import javax.xml.xpath.XPathFactory;
/*  20:    */ import org.apache.poi.ss.usermodel.DateUtil;
/*  21:    */ import org.apache.poi.ss.util.CellReference;
/*  22:    */ import org.apache.poi.util.DocumentHelper;
/*  23:    */ import org.apache.poi.util.LocaleUtil;
/*  24:    */ import org.apache.poi.util.POILogFactory;
/*  25:    */ import org.apache.poi.util.POILogger;
/*  26:    */ import org.apache.poi.xssf.usermodel.XSSFCell;
/*  27:    */ import org.apache.poi.xssf.usermodel.XSSFMap;
/*  28:    */ import org.apache.poi.xssf.usermodel.XSSFRow;
/*  29:    */ import org.apache.poi.xssf.usermodel.XSSFSheet;
/*  30:    */ import org.apache.poi.xssf.usermodel.XSSFTable;
/*  31:    */ import org.apache.poi.xssf.usermodel.helpers.XSSFSingleXmlCell;
/*  32:    */ import org.apache.poi.xssf.usermodel.helpers.XSSFXmlColumnPr;
/*  33:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STXmlDataType;
/*  34:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STXmlDataType.Enum;
/*  35:    */ import org.w3c.dom.Document;
/*  36:    */ import org.w3c.dom.Element;
/*  37:    */ import org.w3c.dom.NamedNodeMap;
/*  38:    */ import org.w3c.dom.Node;
/*  39:    */ import org.w3c.dom.NodeList;
/*  40:    */ import org.xml.sax.InputSource;
/*  41:    */ import org.xml.sax.SAXException;
/*  42:    */ 
/*  43:    */ public class XSSFImportFromXML
/*  44:    */ {
/*  45:    */   private final XSSFMap _map;
/*  46: 74 */   private static final POILogger logger = POILogFactory.getLogger(XSSFImportFromXML.class);
/*  47:    */   
/*  48:    */   public XSSFImportFromXML(XSSFMap map)
/*  49:    */   {
/*  50: 77 */     this._map = map;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void importFromXML(String xmlInputString)
/*  54:    */     throws SAXException, XPathExpressionException, IOException
/*  55:    */   {
/*  56: 91 */     DocumentBuilder builder = DocumentHelper.newDocumentBuilder();
/*  57:    */     
/*  58: 93 */     Document doc = builder.parse(new InputSource(new StringReader(xmlInputString.trim())));
/*  59:    */     
/*  60: 95 */     List<XSSFSingleXmlCell> singleXmlCells = this._map.getRelatedSingleXMLCell();
/*  61:    */     
/*  62: 97 */     List<XSSFTable> tables = this._map.getRelatedTables();
/*  63:    */     
/*  64: 99 */     XPathFactory xpathFactory = XPathFactory.newInstance();
/*  65:100 */     XPath xpath = xpathFactory.newXPath();
/*  66:    */     
/*  67:    */ 
/*  68:    */ 
/*  69:    */ 
/*  70:105 */     xpath.setNamespaceContext(new DefaultNamespaceContext(doc));
/*  71:107 */     for (XSSFSingleXmlCell singleXmlCell : singleXmlCells)
/*  72:    */     {
/*  73:109 */       STXmlDataType.Enum xmlDataType = singleXmlCell.getXmlDataType();
/*  74:110 */       String xpathString = singleXmlCell.getXpath();
/*  75:111 */       Node result = (Node)xpath.evaluate(xpathString, doc, XPathConstants.NODE);
/*  76:113 */       if (result != null)
/*  77:    */       {
/*  78:114 */         String textContent = result.getTextContent();
/*  79:115 */         logger.log(1, new Object[] { "Extracting with xpath " + xpathString + " : value is '" + textContent + "'" });
/*  80:116 */         XSSFCell cell = singleXmlCell.getReferencedCell();
/*  81:117 */         logger.log(1, new Object[] { "Setting '" + textContent + "' to cell " + cell.getColumnIndex() + "-" + cell.getRowIndex() + " in sheet " + cell.getSheet().getSheetName() });
/*  82:    */         
/*  83:119 */         setCellValue(textContent, cell, xmlDataType);
/*  84:    */       }
/*  85:    */     }
/*  86:123 */     for (XSSFTable table : tables)
/*  87:    */     {
/*  88:125 */       String commonXPath = table.getCommonXpath();
/*  89:126 */       NodeList result = (NodeList)xpath.evaluate(commonXPath, doc, XPathConstants.NODESET);
/*  90:127 */       int rowOffset = table.getStartCellReference().getRow() + 1;
/*  91:128 */       int columnOffset = table.getStartCellReference().getCol() - 1;
/*  92:    */       Node singleNode;
/*  93:130 */       for (int i = 0; i < result.getLength(); i++)
/*  94:    */       {
/*  95:135 */         singleNode = result.item(i).cloneNode(true);
/*  96:136 */         for (XSSFXmlColumnPr xmlColumnPr : table.getXmlColumnPrs())
/*  97:    */         {
/*  98:138 */           int localColumnId = (int)xmlColumnPr.getId();
/*  99:139 */           int rowId = rowOffset + i;
/* 100:140 */           int columnId = columnOffset + localColumnId;
/* 101:141 */           String localXPath = xmlColumnPr.getLocalXPath();
/* 102:142 */           localXPath = localXPath.substring(localXPath.substring(1).indexOf('/') + 2);
/* 103:    */           
/* 104:    */ 
/* 105:145 */           String value = (String)xpath.evaluate(localXPath, singleNode, XPathConstants.STRING);
/* 106:146 */           logger.log(1, new Object[] { "Extracting with xpath " + localXPath + " : value is '" + value + "'" });
/* 107:147 */           XSSFRow row = table.getXSSFSheet().getRow(rowId);
/* 108:148 */           if (row == null) {
/* 109:149 */             row = table.getXSSFSheet().createRow(rowId);
/* 110:    */           }
/* 111:152 */           XSSFCell cell = row.getCell(columnId);
/* 112:153 */           if (cell == null) {
/* 113:154 */             cell = row.createCell(columnId);
/* 114:    */           }
/* 115:156 */           logger.log(1, new Object[] { "Setting '" + value + "' to cell " + cell.getColumnIndex() + "-" + cell.getRowIndex() + " in sheet " + table.getXSSFSheet().getSheetName() });
/* 116:    */           
/* 117:158 */           setCellValue(value, cell, xmlColumnPr.getXmlDataType());
/* 118:    */         }
/* 119:    */       }
/* 120:    */     }
/* 121:    */   }
/* 122:    */   
/* 123:    */   private static enum DataType
/* 124:    */   {
/* 125:165 */     BOOLEAN(new STXmlDataType.Enum[] { STXmlDataType.BOOLEAN }),  DOUBLE(new STXmlDataType.Enum[] { STXmlDataType.DOUBLE }),  INTEGER(new STXmlDataType.Enum[] { STXmlDataType.INT, STXmlDataType.UNSIGNED_INT, STXmlDataType.INTEGER }),  STRING(new STXmlDataType.Enum[] { STXmlDataType.STRING }),  DATE(new STXmlDataType.Enum[] { STXmlDataType.DATE });
/* 126:    */     
/* 127:    */     private Set<STXmlDataType.Enum> xmlDataTypes;
/* 128:    */     
/* 129:    */     private DataType(STXmlDataType.Enum... xmlDataTypes)
/* 130:    */     {
/* 131:174 */       this.xmlDataTypes = new HashSet(Arrays.asList(xmlDataTypes));
/* 132:    */     }
/* 133:    */     
/* 134:    */     public static DataType getDataType(STXmlDataType.Enum xmlDataType)
/* 135:    */     {
/* 136:178 */       for (DataType dataType : ) {
/* 137:179 */         if (dataType.xmlDataTypes.contains(xmlDataType)) {
/* 138:180 */           return dataType;
/* 139:    */         }
/* 140:    */       }
/* 141:183 */       return null;
/* 142:    */     }
/* 143:    */   }
/* 144:    */   
/* 145:    */   private void setCellValue(String value, XSSFCell cell, STXmlDataType.Enum xmlDataType)
/* 146:    */   {
/* 147:188 */     DataType type = DataType.getDataType(xmlDataType);
/* 148:    */     try
/* 149:    */     {
/* 150:190 */       if ((value.isEmpty()) || (type == null)) {
/* 151:191 */         cell.setCellValue((String)null);
/* 152:    */       } else {
/* 153:193 */         switch (1.$SwitchMap$org$apache$poi$xssf$extractor$XSSFImportFromXML$DataType[type.ordinal()])
/* 154:    */         {
/* 155:    */         case 1: 
/* 156:195 */           cell.setCellValue(Boolean.parseBoolean(value));
/* 157:196 */           break;
/* 158:    */         case 2: 
/* 159:198 */           cell.setCellValue(Double.parseDouble(value));
/* 160:199 */           break;
/* 161:    */         case 3: 
/* 162:201 */           cell.setCellValue(Integer.parseInt(value));
/* 163:202 */           break;
/* 164:    */         case 4: 
/* 165:204 */           DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", LocaleUtil.getUserLocale());
/* 166:205 */           Date date = sdf.parse(value);
/* 167:206 */           cell.setCellValue(date);
/* 168:207 */           if (!DateUtil.isValidExcelDate(cell.getNumericCellValue())) {
/* 169:208 */             cell.setCellValue(value);
/* 170:    */           }
/* 171:    */           break;
/* 172:    */         case 5: 
/* 173:    */         default: 
/* 174:213 */           cell.setCellValue(value.trim());
/* 175:    */         }
/* 176:    */       }
/* 177:    */     }
/* 178:    */     catch (IllegalArgumentException e)
/* 179:    */     {
/* 180:218 */       throw new IllegalArgumentException(String.format(LocaleUtil.getUserLocale(), "Unable to format value '%s' as %s for cell %s", new Object[] { value, type, new CellReference(cell).formatAsString() }));
/* 181:    */     }
/* 182:    */     catch (ParseException e)
/* 183:    */     {
/* 184:221 */       throw new IllegalArgumentException(String.format(LocaleUtil.getUserLocale(), "Unable to format value '%s' as %s for cell %s", new Object[] { value, type, new CellReference(cell).formatAsString() }));
/* 185:    */     }
/* 186:    */   }
/* 187:    */   
/* 188:    */   private static final class DefaultNamespaceContext
/* 189:    */     implements NamespaceContext
/* 190:    */   {
/* 191:    */     private final Element _docElem;
/* 192:    */     
/* 193:    */     public DefaultNamespaceContext(Document doc)
/* 194:    */     {
/* 195:234 */       this._docElem = doc.getDocumentElement();
/* 196:    */     }
/* 197:    */     
/* 198:    */     public String getNamespaceURI(String prefix)
/* 199:    */     {
/* 200:239 */       return getNamespaceForPrefix(prefix);
/* 201:    */     }
/* 202:    */     
/* 203:    */     private String getNamespaceForPrefix(String prefix)
/* 204:    */     {
/* 205:251 */       if (prefix.equals("xml")) {
/* 206:252 */         return "http://www.w3.org/XML/1998/namespace";
/* 207:    */       }
/* 208:255 */       Node parent = this._docElem;
/* 209:257 */       while (parent != null)
/* 210:    */       {
/* 211:259 */         int type = parent.getNodeType();
/* 212:260 */         if (type == 1)
/* 213:    */         {
/* 214:261 */           if (parent.getNodeName().startsWith(prefix + ":")) {
/* 215:262 */             return parent.getNamespaceURI();
/* 216:    */           }
/* 217:264 */           NamedNodeMap nnm = parent.getAttributes();
/* 218:266 */           for (int i = 0; i < nnm.getLength(); i++)
/* 219:    */           {
/* 220:267 */             Node attr = nnm.item(i);
/* 221:268 */             String aname = attr.getNodeName();
/* 222:269 */             boolean isPrefix = aname.startsWith("xmlns:");
/* 223:271 */             if ((isPrefix) || (aname.equals("xmlns")))
/* 224:    */             {
/* 225:272 */               int index = aname.indexOf(':');
/* 226:273 */               String p = isPrefix ? aname.substring(index + 1) : "";
/* 227:275 */               if (p.equals(prefix)) {
/* 228:276 */                 return attr.getNodeValue();
/* 229:    */               }
/* 230:    */             }
/* 231:    */           }
/* 232:    */         }
/* 233:    */         else
/* 234:    */         {
/* 235:280 */           if (type != 5) {
/* 236:    */             break;
/* 237:    */           }
/* 238:281 */           continue;
/* 239:    */         }
/* 240:285 */         parent = parent.getParentNode();
/* 241:    */       }
/* 242:288 */       return null;
/* 243:    */     }
/* 244:    */     
/* 245:    */     public Iterator<?> getPrefixes(String val)
/* 246:    */     {
/* 247:294 */       return null;
/* 248:    */     }
/* 249:    */     
/* 250:    */     public String getPrefix(String uri)
/* 251:    */     {
/* 252:300 */       return null;
/* 253:    */     }
/* 254:    */   }
/* 255:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.extractor.XSSFImportFromXML
 * JD-Core Version:    0.7.0.1
 */