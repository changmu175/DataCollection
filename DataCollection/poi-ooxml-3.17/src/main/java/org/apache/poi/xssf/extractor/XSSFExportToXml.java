/*   1:    */ package org.apache.poi.xssf.extractor;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.text.DateFormat;
/*   6:    */ import java.text.SimpleDateFormat;
/*   7:    */ import java.util.Collections;
/*   8:    */ import java.util.Comparator;
/*   9:    */ import java.util.HashMap;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Locale;
/*  12:    */ import java.util.Map;
/*  13:    */ import java.util.Vector;
/*  14:    */ import javax.xml.transform.Source;
/*  15:    */ import javax.xml.transform.Transformer;
/*  16:    */ import javax.xml.transform.TransformerException;
/*  17:    */ import javax.xml.transform.TransformerFactory;
/*  18:    */ import javax.xml.transform.dom.DOMSource;
/*  19:    */ import javax.xml.transform.stream.StreamResult;
/*  20:    */ import javax.xml.validation.Schema;
/*  21:    */ import javax.xml.validation.SchemaFactory;
/*  22:    */ import javax.xml.validation.Validator;
/*  23:    */ import org.apache.poi.ss.usermodel.CellType;
/*  24:    */ import org.apache.poi.ss.usermodel.DateUtil;
/*  25:    */ import org.apache.poi.ss.util.CellReference;
/*  26:    */ import org.apache.poi.util.DocumentHelper;
/*  27:    */ import org.apache.poi.util.LocaleUtil;
/*  28:    */ import org.apache.poi.util.POILogFactory;
/*  29:    */ import org.apache.poi.util.POILogger;
/*  30:    */ import org.apache.poi.xssf.usermodel.XSSFCell;
/*  31:    */ import org.apache.poi.xssf.usermodel.XSSFMap;
/*  32:    */ import org.apache.poi.xssf.usermodel.XSSFRow;
/*  33:    */ import org.apache.poi.xssf.usermodel.XSSFSheet;
/*  34:    */ import org.apache.poi.xssf.usermodel.XSSFTable;
/*  35:    */ import org.apache.poi.xssf.usermodel.helpers.XSSFSingleXmlCell;
/*  36:    */ import org.apache.poi.xssf.usermodel.helpers.XSSFXmlColumnPr;
/*  37:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap;
/*  38:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema;
/*  39:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
/*  40:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
/*  41:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
/*  42:    */ import org.w3c.dom.Document;
/*  43:    */ import org.w3c.dom.Element;
/*  44:    */ import org.w3c.dom.NamedNodeMap;
/*  45:    */ import org.w3c.dom.Node;
/*  46:    */ import org.w3c.dom.NodeList;
/*  47:    */ import org.xml.sax.SAXException;
/*  48:    */ 
/*  49:    */ public class XSSFExportToXml
/*  50:    */   implements Comparator<String>
/*  51:    */ {
/*  52: 83 */   private static final POILogger LOG = POILogFactory.getLogger(XSSFExportToXml.class);
/*  53:    */   private XSSFMap map;
/*  54:    */   
/*  55:    */   public XSSFExportToXml(XSSFMap map)
/*  56:    */   {
/*  57: 93 */     this.map = map;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void exportToXML(OutputStream os, boolean validate)
/*  61:    */     throws SAXException, TransformerException
/*  62:    */   {
/*  63:106 */     exportToXML(os, "UTF-8", validate);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void exportToXML(OutputStream os, String encoding, boolean validate)
/*  67:    */     throws SAXException, TransformerException
/*  68:    */   {
/*  69:119 */     List<XSSFSingleXmlCell> singleXMLCells = this.map.getRelatedSingleXMLCell();
/*  70:120 */     List<XSSFTable> tables = this.map.getRelatedTables();
/*  71:    */     
/*  72:122 */     String rootElement = this.map.getCtMap().getRootElement();
/*  73:    */     
/*  74:124 */     Document doc = DocumentHelper.createDocument();
/*  75:    */     Element root;
/*  76:    */     Element root;
/*  77:128 */     if (isNamespaceDeclared()) {
/*  78:129 */       root = doc.createElementNS(getNamespace(), rootElement);
/*  79:    */     } else {
/*  80:131 */       root = doc.createElementNS("", rootElement);
/*  81:    */     }
/*  82:133 */     doc.appendChild(root);
/*  83:    */     
/*  84:    */ 
/*  85:136 */     List<String> xpaths = new Vector();
/*  86:137 */     Map<String, XSSFSingleXmlCell> singleXmlCellsMappings = new HashMap();
/*  87:138 */     Map<String, XSSFTable> tableMappings = new HashMap();
/*  88:140 */     for (XSSFSingleXmlCell simpleXmlCell : singleXMLCells)
/*  89:    */     {
/*  90:141 */       xpaths.add(simpleXmlCell.getXpath());
/*  91:142 */       singleXmlCellsMappings.put(simpleXmlCell.getXpath(), simpleXmlCell);
/*  92:    */     }
/*  93:144 */     for (XSSFTable table : tables)
/*  94:    */     {
/*  95:145 */       String commonXPath = table.getCommonXpath();
/*  96:146 */       xpaths.add(commonXPath);
/*  97:147 */       tableMappings.put(commonXPath, table);
/*  98:    */     }
/*  99:150 */     Collections.sort(xpaths, this);
/* 100:152 */     for (String xpath : xpaths)
/* 101:    */     {
/* 102:154 */       XSSFSingleXmlCell simpleXmlCell = (XSSFSingleXmlCell)singleXmlCellsMappings.get(xpath);
/* 103:155 */       XSSFTable table = (XSSFTable)tableMappings.get(xpath);
/* 104:157 */       if (!xpath.matches(".*\\[.*"))
/* 105:    */       {
/* 106:160 */         if (simpleXmlCell != null)
/* 107:    */         {
/* 108:161 */           XSSFCell cell = simpleXmlCell.getReferencedCell();
/* 109:162 */           if (cell != null)
/* 110:    */           {
/* 111:163 */             Node currentNode = getNodeByXPath(xpath, doc.getFirstChild(), doc, false);
/* 112:164 */             mapCellOnNode(cell, currentNode);
/* 113:167 */             if (("".equals(currentNode.getTextContent())) && (currentNode.getParentNode() != null)) {
/* 114:168 */               currentNode.getParentNode().removeChild(currentNode);
/* 115:    */             }
/* 116:    */           }
/* 117:    */         }
/* 118:174 */         if (table != null)
/* 119:    */         {
/* 120:176 */           List<CTTableColumn> tableColumns = table.getCTTable().getTableColumns().getTableColumnList();
/* 121:    */           
/* 122:178 */           XSSFSheet sheet = table.getXSSFSheet();
/* 123:    */           
/* 124:180 */           int startRow = table.getStartCellReference().getRow();
/* 125:    */           
/* 126:182 */           startRow++;
/* 127:    */           
/* 128:184 */           int endRow = table.getEndCellReference().getRow();
/* 129:186 */           for (int i = startRow; i <= endRow; i++)
/* 130:    */           {
/* 131:187 */             XSSFRow row = sheet.getRow(i);
/* 132:    */             
/* 133:189 */             Node tableRootNode = getNodeByXPath(table.getCommonXpath(), doc.getFirstChild(), doc, true);
/* 134:    */             
/* 135:191 */             short startColumnIndex = table.getStartCellReference().getCol();
/* 136:192 */             for (int j = startColumnIndex; j <= table.getEndCellReference().getCol(); j++)
/* 137:    */             {
/* 138:193 */               XSSFCell cell = row.getCell(j);
/* 139:194 */               if (cell != null)
/* 140:    */               {
/* 141:195 */                 int tableColumnIndex = j - startColumnIndex;
/* 142:196 */                 if (tableColumnIndex < tableColumns.size())
/* 143:    */                 {
/* 144:197 */                   CTTableColumn ctTableColumn = (CTTableColumn)tableColumns.get(tableColumnIndex);
/* 145:198 */                   if (ctTableColumn.getXmlColumnPr() != null)
/* 146:    */                   {
/* 147:199 */                     XSSFXmlColumnPr pointer = new XSSFXmlColumnPr(table, ctTableColumn, ctTableColumn.getXmlColumnPr());
/* 148:    */                     
/* 149:201 */                     String localXPath = pointer.getLocalXPath();
/* 150:202 */                     Node currentNode = getNodeByXPath(localXPath, tableRootNode, doc, false);
/* 151:203 */                     mapCellOnNode(cell, currentNode);
/* 152:    */                   }
/* 153:    */                 }
/* 154:    */               }
/* 155:    */             }
/* 156:    */           }
/* 157:    */         }
/* 158:    */       }
/* 159:    */     }
/* 160:215 */     boolean isValid = true;
/* 161:216 */     if (validate) {
/* 162:217 */       isValid = isValid(doc);
/* 163:    */     }
/* 164:220 */     if (isValid)
/* 165:    */     {
/* 166:226 */       TransformerFactory transfac = TransformerFactory.newInstance();
/* 167:227 */       Transformer trans = transfac.newTransformer();
/* 168:228 */       trans.setOutputProperty("omit-xml-declaration", "yes");
/* 169:229 */       trans.setOutputProperty("indent", "yes");
/* 170:230 */       trans.setOutputProperty("encoding", encoding);
/* 171:    */       
/* 172:    */ 
/* 173:    */ 
/* 174:234 */       StreamResult result = new StreamResult(os);
/* 175:235 */       DOMSource source = new DOMSource(doc);
/* 176:236 */       trans.transform(source, result);
/* 177:    */     }
/* 178:    */   }
/* 179:    */   
/* 180:    */   private boolean isValid(Document xml)
/* 181:    */     throws SAXException
/* 182:    */   {
/* 183:    */     try
/* 184:    */     {
/* 185:251 */       String language = "http://www.w3.org/2001/XMLSchema";
/* 186:252 */       SchemaFactory factory = SchemaFactory.newInstance(language);
/* 187:    */       
/* 188:254 */       Source source = new DOMSource(this.map.getSchema());
/* 189:255 */       Schema schema = factory.newSchema(source);
/* 190:256 */       Validator validator = schema.newValidator();
/* 191:257 */       validator.validate(new DOMSource(xml));
/* 192:    */       
/* 193:    */ 
/* 194:260 */       return true;
/* 195:    */     }
/* 196:    */     catch (IOException e)
/* 197:    */     {
/* 198:262 */       LOG.log(7, new Object[] { "document is not valid", e });
/* 199:    */     }
/* 200:265 */     return false;
/* 201:    */   }
/* 202:    */   
/* 203:    */   private void mapCellOnNode(XSSFCell cell, Node node)
/* 204:    */   {
/* 205:271 */     String value = "";
/* 206:272 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cell.getCellTypeEnum().ordinal()])
/* 207:    */     {
/* 208:    */     case 1: 
/* 209:274 */       value = cell.getStringCellValue(); break;
/* 210:    */     case 2: 
/* 211:275 */       value = value + cell.getBooleanCellValue(); break;
/* 212:    */     case 3: 
/* 213:276 */       value = cell.getErrorCellString(); break;
/* 214:    */     case 4: 
/* 215:278 */       if (cell.getCachedFormulaResultTypeEnum() == CellType.STRING) {
/* 216:279 */         value = cell.getStringCellValue();
/* 217:281 */       } else if (DateUtil.isCellDateFormatted(cell)) {
/* 218:282 */         value = getFormattedDate(cell);
/* 219:    */       } else {
/* 220:284 */         value = value + cell.getNumericCellValue();
/* 221:    */       }
/* 222:287 */       break;
/* 223:    */     case 5: 
/* 224:290 */       if (DateUtil.isCellDateFormatted(cell)) {
/* 225:291 */         value = getFormattedDate(cell);
/* 226:    */       } else {
/* 227:293 */         value = value + cell.getRawValue();
/* 228:    */       }
/* 229:295 */       break;
/* 230:    */     }
/* 231:300 */     if ((node instanceof Element))
/* 232:    */     {
/* 233:301 */       Element currentElement = (Element)node;
/* 234:302 */       currentElement.setTextContent(value);
/* 235:    */     }
/* 236:    */     else
/* 237:    */     {
/* 238:304 */       node.setNodeValue(value);
/* 239:    */     }
/* 240:    */   }
/* 241:    */   
/* 242:    */   private String removeNamespace(String elementName)
/* 243:    */   {
/* 244:309 */     return elementName.matches(".*:.*") ? elementName.split(":")[1] : elementName;
/* 245:    */   }
/* 246:    */   
/* 247:    */   private String getFormattedDate(XSSFCell cell)
/* 248:    */   {
/* 249:313 */     DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
/* 250:314 */     sdf.setTimeZone(LocaleUtil.getUserTimeZone());
/* 251:315 */     return sdf.format(cell.getDateCellValue());
/* 252:    */   }
/* 253:    */   
/* 254:    */   private Node getNodeByXPath(String xpath, Node rootNode, Document doc, boolean createMultipleInstances)
/* 255:    */   {
/* 256:319 */     String[] xpathTokens = xpath.split("/");
/* 257:    */     
/* 258:    */ 
/* 259:322 */     Node currentNode = rootNode;
/* 260:324 */     for (int i = 2; i < xpathTokens.length; i++)
/* 261:    */     {
/* 262:326 */       String axisName = removeNamespace(xpathTokens[i]);
/* 263:329 */       if (!axisName.startsWith("@"))
/* 264:    */       {
/* 265:331 */         NodeList list = currentNode.getChildNodes();
/* 266:    */         
/* 267:333 */         Node selectedNode = null;
/* 268:334 */         if ((!createMultipleInstances) || (i != xpathTokens.length - 1)) {
/* 269:336 */           selectedNode = selectNode(axisName, list);
/* 270:    */         }
/* 271:338 */         if (selectedNode == null) {
/* 272:339 */           selectedNode = createElement(doc, currentNode, axisName);
/* 273:    */         }
/* 274:341 */         currentNode = selectedNode;
/* 275:    */       }
/* 276:    */       else
/* 277:    */       {
/* 278:343 */         currentNode = createAttribute(doc, currentNode, axisName);
/* 279:    */       }
/* 280:    */     }
/* 281:346 */     return currentNode;
/* 282:    */   }
/* 283:    */   
/* 284:    */   private Node createAttribute(Document doc, Node currentNode, String axisName)
/* 285:    */   {
/* 286:350 */     String attributeName = axisName.substring(1);
/* 287:351 */     NamedNodeMap attributesMap = currentNode.getAttributes();
/* 288:352 */     Node attribute = attributesMap.getNamedItem(attributeName);
/* 289:353 */     if (attribute == null)
/* 290:    */     {
/* 291:354 */       attribute = doc.createAttributeNS("", attributeName);
/* 292:355 */       attributesMap.setNamedItem(attribute);
/* 293:    */     }
/* 294:357 */     return attribute;
/* 295:    */   }
/* 296:    */   
/* 297:    */   private Node createElement(Document doc, Node currentNode, String axisName)
/* 298:    */   {
/* 299:    */     Node selectedNode;
/* 300:    */     Node selectedNode;
/* 301:362 */     if (isNamespaceDeclared()) {
/* 302:363 */       selectedNode = doc.createElementNS(getNamespace(), axisName);
/* 303:    */     } else {
/* 304:365 */       selectedNode = doc.createElementNS("", axisName);
/* 305:    */     }
/* 306:367 */     currentNode.appendChild(selectedNode);
/* 307:368 */     return selectedNode;
/* 308:    */   }
/* 309:    */   
/* 310:    */   private Node selectNode(String axisName, NodeList list)
/* 311:    */   {
/* 312:372 */     Node selectedNode = null;
/* 313:373 */     for (int j = 0; j < list.getLength(); j++)
/* 314:    */     {
/* 315:374 */       Node node = list.item(j);
/* 316:375 */       if (node.getNodeName().equals(axisName))
/* 317:    */       {
/* 318:376 */         selectedNode = node;
/* 319:377 */         break;
/* 320:    */       }
/* 321:    */     }
/* 322:380 */     return selectedNode;
/* 323:    */   }
/* 324:    */   
/* 325:    */   private boolean isNamespaceDeclared()
/* 326:    */   {
/* 327:385 */     String schemaNamespace = getNamespace();
/* 328:386 */     return (schemaNamespace != null) && (!schemaNamespace.equals(""));
/* 329:    */   }
/* 330:    */   
/* 331:    */   private String getNamespace()
/* 332:    */   {
/* 333:390 */     return this.map.getCTSchema().getNamespace();
/* 334:    */   }
/* 335:    */   
/* 336:    */   public int compare(String leftXpath, String rightXpath)
/* 337:    */   {
/* 338:400 */     Node xmlSchema = this.map.getSchema();
/* 339:    */     
/* 340:402 */     String[] leftTokens = leftXpath.split("/");
/* 341:403 */     String[] rightTokens = rightXpath.split("/");
/* 342:    */     
/* 343:405 */     int minLength = leftTokens.length < rightTokens.length ? leftTokens.length : rightTokens.length;
/* 344:    */     
/* 345:407 */     Node localComplexTypeRootNode = xmlSchema;
/* 346:409 */     for (int i = 1; i < minLength; i++)
/* 347:    */     {
/* 348:411 */       String leftElementName = leftTokens[i];
/* 349:412 */       String rightElementName = rightTokens[i];
/* 350:414 */       if (leftElementName.equals(rightElementName))
/* 351:    */       {
/* 352:415 */         localComplexTypeRootNode = getComplexTypeForElement(leftElementName, xmlSchema, localComplexTypeRootNode);
/* 353:    */       }
/* 354:    */       else
/* 355:    */       {
/* 356:417 */         int leftIndex = indexOfElementInComplexType(leftElementName, localComplexTypeRootNode);
/* 357:418 */         int rightIndex = indexOfElementInComplexType(rightElementName, localComplexTypeRootNode);
/* 358:419 */         if ((leftIndex != -1) && (rightIndex != -1))
/* 359:    */         {
/* 360:420 */           if (leftIndex < rightIndex) {
/* 361:421 */             return -1;
/* 362:    */           }
/* 363:422 */           if (leftIndex > rightIndex) {
/* 364:423 */             return 1;
/* 365:    */           }
/* 366:    */         }
/* 367:    */       }
/* 368:    */     }
/* 369:431 */     return 0;
/* 370:    */   }
/* 371:    */   
/* 372:    */   private int indexOfElementInComplexType(String elementName, Node complexType)
/* 373:    */   {
/* 374:435 */     if (complexType == null) {
/* 375:436 */       return -1;
/* 376:    */     }
/* 377:439 */     NodeList list = complexType.getChildNodes();
/* 378:440 */     int indexOf = -1;
/* 379:442 */     for (int i = 0; i < list.getLength(); i++)
/* 380:    */     {
/* 381:443 */       Node node = list.item(i);
/* 382:444 */       if (((node instanceof Element)) && 
/* 383:445 */         (node.getLocalName().equals("element")))
/* 384:    */       {
/* 385:446 */         Node element = getNameOrRefElement(node);
/* 386:447 */         if (element.getNodeValue().equals(removeNamespace(elementName)))
/* 387:    */         {
/* 388:448 */           indexOf = i;
/* 389:449 */           break;
/* 390:    */         }
/* 391:    */       }
/* 392:    */     }
/* 393:455 */     return indexOf;
/* 394:    */   }
/* 395:    */   
/* 396:    */   private Node getNameOrRefElement(Node node)
/* 397:    */   {
/* 398:459 */     Node returnNode = node.getAttributes().getNamedItem("name");
/* 399:460 */     if (returnNode != null) {
/* 400:461 */       return returnNode;
/* 401:    */     }
/* 402:464 */     return node.getAttributes().getNamedItem("ref");
/* 403:    */   }
/* 404:    */   
/* 405:    */   private Node getComplexTypeForElement(String elementName, Node xmlSchema, Node localComplexTypeRootNode)
/* 406:    */   {
/* 407:468 */     String elementNameWithoutNamespace = removeNamespace(elementName);
/* 408:    */     
/* 409:470 */     String complexTypeName = getComplexTypeNameFromChildren(localComplexTypeRootNode, elementNameWithoutNamespace);
/* 410:    */     
/* 411:    */ 
/* 412:473 */     Node complexTypeNode = null;
/* 413:474 */     if (!"".equals(complexTypeName)) {
/* 414:475 */       complexTypeNode = getComplexTypeNodeFromSchemaChildren(xmlSchema, null, complexTypeName);
/* 415:    */     }
/* 416:478 */     return complexTypeNode;
/* 417:    */   }
/* 418:    */   
/* 419:    */   private String getComplexTypeNameFromChildren(Node localComplexTypeRootNode, String elementNameWithoutNamespace)
/* 420:    */   {
/* 421:483 */     if (localComplexTypeRootNode == null) {
/* 422:484 */       return "";
/* 423:    */     }
/* 424:487 */     NodeList list = localComplexTypeRootNode.getChildNodes();
/* 425:488 */     String complexTypeName = "";
/* 426:490 */     for (int i = 0; i < list.getLength(); i++)
/* 427:    */     {
/* 428:491 */       Node node = list.item(i);
/* 429:492 */       if (((node instanceof Element)) && 
/* 430:493 */         (node.getLocalName().equals("element")))
/* 431:    */       {
/* 432:494 */         Node nameAttribute = getNameOrRefElement(node);
/* 433:495 */         if (nameAttribute.getNodeValue().equals(elementNameWithoutNamespace))
/* 434:    */         {
/* 435:496 */           Node complexTypeAttribute = node.getAttributes().getNamedItem("type");
/* 436:497 */           if (complexTypeAttribute != null)
/* 437:    */           {
/* 438:498 */             complexTypeName = complexTypeAttribute.getNodeValue();
/* 439:499 */             break;
/* 440:    */           }
/* 441:    */         }
/* 442:    */       }
/* 443:    */     }
/* 444:505 */     return complexTypeName;
/* 445:    */   }
/* 446:    */   
/* 447:    */   private Node getComplexTypeNodeFromSchemaChildren(Node xmlSchema, Node complexTypeNode, String complexTypeName)
/* 448:    */   {
/* 449:510 */     NodeList complexTypeList = xmlSchema.getChildNodes();
/* 450:511 */     for (int i = 0; i < complexTypeList.getLength(); i++)
/* 451:    */     {
/* 452:512 */       Node node = complexTypeList.item(i);
/* 453:513 */       if (((node instanceof Element)) && 
/* 454:514 */         (node.getLocalName().equals("complexType")))
/* 455:    */       {
/* 456:515 */         Node nameAttribute = getNameOrRefElement(node);
/* 457:516 */         if (nameAttribute.getNodeValue().equals(complexTypeName))
/* 458:    */         {
/* 459:518 */           NodeList complexTypeChildList = node.getChildNodes();
/* 460:519 */           for (int j = 0; j < complexTypeChildList.getLength(); j++)
/* 461:    */           {
/* 462:520 */             Node sequence = complexTypeChildList.item(j);
/* 463:522 */             if (((sequence instanceof Element)) && (
/* 464:523 */               (sequence.getLocalName().equals("sequence")) || (sequence.getLocalName().equals("all"))))
/* 465:    */             {
/* 466:524 */               complexTypeNode = sequence;
/* 467:525 */               break;
/* 468:    */             }
/* 469:    */           }
/* 470:529 */           if (complexTypeNode != null) {
/* 471:    */             break;
/* 472:    */           }
/* 473:    */         }
/* 474:    */       }
/* 475:    */     }
/* 476:537 */     return complexTypeNode;
/* 477:    */   }
/* 478:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.extractor.XSSFExportToXml
 * JD-Core Version:    0.7.0.1
 */