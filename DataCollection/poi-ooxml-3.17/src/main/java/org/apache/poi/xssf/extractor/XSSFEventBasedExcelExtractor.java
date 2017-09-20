/*   1:    */ package org.apache.poi.xssf.extractor;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Locale;
/*   9:    */ import java.util.Map;
/*  10:    */ import javax.xml.parsers.ParserConfigurationException;
/*  11:    */ import org.apache.poi.POIXMLProperties;
/*  12:    */ import org.apache.poi.POIXMLProperties.CoreProperties;
/*  13:    */ import org.apache.poi.POIXMLProperties.CustomProperties;
/*  14:    */ import org.apache.poi.POIXMLProperties.ExtendedProperties;
/*  15:    */ import org.apache.poi.POIXMLTextExtractor;
/*  16:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  17:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  18:    */ import org.apache.poi.ss.extractor.ExcelExtractor;
/*  19:    */ import org.apache.poi.ss.usermodel.DataFormatter;
/*  20:    */ import org.apache.poi.util.POILogFactory;
/*  21:    */ import org.apache.poi.util.POILogger;
/*  22:    */ import org.apache.poi.util.SAXHelper;
/*  23:    */ import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
/*  24:    */ import org.apache.poi.xssf.eventusermodel.XSSFReader;
/*  25:    */ import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
/*  26:    */ import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
/*  27:    */ import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
/*  28:    */ import org.apache.poi.xssf.model.CommentsTable;
/*  29:    */ import org.apache.poi.xssf.model.StylesTable;
/*  30:    */ import org.apache.poi.xssf.usermodel.XSSFComment;
/*  31:    */ import org.apache.poi.xssf.usermodel.XSSFRichTextString;
/*  32:    */ import org.apache.poi.xssf.usermodel.XSSFShape;
/*  33:    */ import org.apache.poi.xssf.usermodel.XSSFSimpleShape;
/*  34:    */ import org.apache.xmlbeans.XmlException;
/*  35:    */ import org.xml.sax.ContentHandler;
/*  36:    */ import org.xml.sax.InputSource;
/*  37:    */ import org.xml.sax.SAXException;
/*  38:    */ import org.xml.sax.XMLReader;
/*  39:    */ 
/*  40:    */ public class XSSFEventBasedExcelExtractor
/*  41:    */   extends POIXMLTextExtractor
/*  42:    */   implements ExcelExtractor
/*  43:    */ {
/*  44: 60 */   private static final POILogger LOGGER = POILogFactory.getLogger(XSSFEventBasedExcelExtractor.class);
/*  45:    */   private OPCPackage container;
/*  46:    */   private POIXMLProperties properties;
/*  47:    */   private Locale locale;
/*  48: 66 */   private boolean includeTextBoxes = true;
/*  49: 67 */   private boolean includeSheetNames = true;
/*  50: 68 */   private boolean includeCellComments = false;
/*  51: 69 */   private boolean includeHeadersFooters = true;
/*  52: 70 */   private boolean formulasNotResults = false;
/*  53: 71 */   private boolean concatenatePhoneticRuns = true;
/*  54:    */   
/*  55:    */   public XSSFEventBasedExcelExtractor(String path)
/*  56:    */     throws XmlException, OpenXML4JException, IOException
/*  57:    */   {
/*  58: 74 */     this(OPCPackage.open(path));
/*  59:    */   }
/*  60:    */   
/*  61:    */   public XSSFEventBasedExcelExtractor(OPCPackage container)
/*  62:    */     throws XmlException, OpenXML4JException, IOException
/*  63:    */   {
/*  64: 77 */     super(null);
/*  65: 78 */     this.container = container;
/*  66:    */     
/*  67: 80 */     this.properties = new POIXMLProperties(container);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static void main(String[] args)
/*  71:    */     throws Exception
/*  72:    */   {
/*  73: 84 */     if (args.length < 1)
/*  74:    */     {
/*  75: 85 */       System.err.println("Use:");
/*  76: 86 */       System.err.println("  XSSFEventBasedExcelExtractor <filename.xlsx>");
/*  77: 87 */       System.exit(1);
/*  78:    */     }
/*  79: 89 */     POIXMLTextExtractor extractor = new XSSFEventBasedExcelExtractor(args[0]);
/*  80:    */     
/*  81: 91 */     System.out.println(extractor.getText());
/*  82: 92 */     extractor.close();
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setIncludeSheetNames(boolean includeSheetNames)
/*  86:    */   {
/*  87: 99 */     this.includeSheetNames = includeSheetNames;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public boolean getIncludeSheetNames()
/*  91:    */   {
/*  92:110 */     return this.includeSheetNames;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setFormulasNotResults(boolean formulasNotResults)
/*  96:    */   {
/*  97:118 */     this.formulasNotResults = formulasNotResults;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public boolean getFormulasNotResults()
/* 101:    */   {
/* 102:128 */     return this.formulasNotResults;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setIncludeHeadersFooters(boolean includeHeadersFooters)
/* 106:    */   {
/* 107:135 */     this.includeHeadersFooters = includeHeadersFooters;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public boolean getIncludeHeadersFooters()
/* 111:    */   {
/* 112:145 */     return this.includeHeadersFooters;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void setIncludeTextBoxes(boolean includeTextBoxes)
/* 116:    */   {
/* 117:151 */     this.includeTextBoxes = includeTextBoxes;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public boolean getIncludeTextBoxes()
/* 121:    */   {
/* 122:161 */     return this.includeTextBoxes;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void setIncludeCellComments(boolean includeCellComments)
/* 126:    */   {
/* 127:167 */     this.includeCellComments = includeCellComments;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public boolean getIncludeCellComments()
/* 131:    */   {
/* 132:176 */     return this.includeCellComments;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void setConcatenatePhoneticRuns(boolean concatenatePhoneticRuns)
/* 136:    */   {
/* 137:184 */     this.concatenatePhoneticRuns = concatenatePhoneticRuns;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public void setLocale(Locale locale)
/* 141:    */   {
/* 142:187 */     this.locale = locale;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public Locale getLocale()
/* 146:    */   {
/* 147:196 */     return this.locale;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public OPCPackage getPackage()
/* 151:    */   {
/* 152:203 */     return this.container;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public POIXMLProperties.CoreProperties getCoreProperties()
/* 156:    */   {
/* 157:211 */     return this.properties.getCoreProperties();
/* 158:    */   }
/* 159:    */   
/* 160:    */   public POIXMLProperties.ExtendedProperties getExtendedProperties()
/* 161:    */   {
/* 162:218 */     return this.properties.getExtendedProperties();
/* 163:    */   }
/* 164:    */   
/* 165:    */   public POIXMLProperties.CustomProperties getCustomProperties()
/* 166:    */   {
/* 167:225 */     return this.properties.getCustomProperties();
/* 168:    */   }
/* 169:    */   
/* 170:    */   public void processSheet(XSSFSheetXMLHandler.SheetContentsHandler sheetContentsExtractor, StylesTable styles, CommentsTable comments, ReadOnlySharedStringsTable strings, InputStream sheetInputStream)
/* 171:    */     throws IOException, SAXException
/* 172:    */   {
/* 173:    */     DataFormatter formatter;
/* 174:    */     DataFormatter formatter;
/* 175:242 */     if (this.locale == null) {
/* 176:243 */       formatter = new DataFormatter();
/* 177:    */     } else {
/* 178:245 */       formatter = new DataFormatter(this.locale);
/* 179:    */     }
/* 180:248 */     InputSource sheetSource = new InputSource(sheetInputStream);
/* 181:    */     try
/* 182:    */     {
/* 183:250 */       XMLReader sheetParser = SAXHelper.newXMLReader();
/* 184:251 */       ContentHandler handler = new XSSFSheetXMLHandler(styles, comments, strings, sheetContentsExtractor, formatter, this.formulasNotResults);
/* 185:    */       
/* 186:253 */       sheetParser.setContentHandler(handler);
/* 187:254 */       sheetParser.parse(sheetSource);
/* 188:    */     }
/* 189:    */     catch (ParserConfigurationException e)
/* 190:    */     {
/* 191:256 */       throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
/* 192:    */     }
/* 193:    */   }
/* 194:    */   
/* 195:    */   public String getText()
/* 196:    */   {
/* 197:    */     try
/* 198:    */     {
/* 199:265 */       ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.container, this.concatenatePhoneticRuns);
/* 200:266 */       XSSFReader xssfReader = new XSSFReader(this.container);
/* 201:267 */       StylesTable styles = xssfReader.getStylesTable();
/* 202:268 */       XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator)xssfReader.getSheetsData();
/* 203:    */       
/* 204:270 */       StringBuffer text = new StringBuffer();
/* 205:271 */       SheetTextExtractor sheetExtractor = new SheetTextExtractor();
/* 206:273 */       while (iter.hasNext())
/* 207:    */       {
/* 208:274 */         InputStream stream = iter.next();
/* 209:275 */         if (this.includeSheetNames)
/* 210:    */         {
/* 211:276 */           text.append(iter.getSheetName());
/* 212:277 */           text.append('\n');
/* 213:    */         }
/* 214:279 */         CommentsTable comments = this.includeCellComments ? iter.getSheetComments() : null;
/* 215:280 */         processSheet(sheetExtractor, styles, comments, strings, stream);
/* 216:281 */         if (this.includeHeadersFooters) {
/* 217:282 */           sheetExtractor.appendHeaderText(text);
/* 218:    */         }
/* 219:284 */         sheetExtractor.appendCellText(text);
/* 220:285 */         if (this.includeTextBoxes) {
/* 221:286 */           processShapes(iter.getShapes(), text);
/* 222:    */         }
/* 223:288 */         if (this.includeHeadersFooters) {
/* 224:289 */           sheetExtractor.appendFooterText(text);
/* 225:    */         }
/* 226:291 */         sheetExtractor.reset();
/* 227:292 */         stream.close();
/* 228:    */       }
/* 229:295 */       return text.toString();
/* 230:    */     }
/* 231:    */     catch (IOException e)
/* 232:    */     {
/* 233:297 */       LOGGER.log(5, new Object[] { e });
/* 234:298 */       return null;
/* 235:    */     }
/* 236:    */     catch (SAXException se)
/* 237:    */     {
/* 238:300 */       LOGGER.log(5, new Object[] { se });
/* 239:301 */       return null;
/* 240:    */     }
/* 241:    */     catch (OpenXML4JException o4je)
/* 242:    */     {
/* 243:303 */       LOGGER.log(5, new Object[] { o4je });
/* 244:    */     }
/* 245:304 */     return null;
/* 246:    */   }
/* 247:    */   
/* 248:    */   void processShapes(List<XSSFShape> shapes, StringBuffer text)
/* 249:    */   {
/* 250:309 */     if (shapes == null) {
/* 251:310 */       return;
/* 252:    */     }
/* 253:312 */     for (XSSFShape shape : shapes) {
/* 254:313 */       if ((shape instanceof XSSFSimpleShape))
/* 255:    */       {
/* 256:314 */         String sText = ((XSSFSimpleShape)shape).getText();
/* 257:315 */         if ((sText != null) && (sText.length() > 0)) {
/* 258:316 */           text.append(sText).append('\n');
/* 259:    */         }
/* 260:    */       }
/* 261:    */     }
/* 262:    */   }
/* 263:    */   
/* 264:    */   public void close()
/* 265:    */     throws IOException
/* 266:    */   {
/* 267:323 */     if (this.container != null)
/* 268:    */     {
/* 269:324 */       this.container.close();
/* 270:325 */       this.container = null;
/* 271:    */     }
/* 272:327 */     super.close();
/* 273:    */   }
/* 274:    */   
/* 275:    */   protected class SheetTextExtractor
/* 276:    */     implements XSSFSheetXMLHandler.SheetContentsHandler
/* 277:    */   {
/* 278:    */     private final StringBuffer output;
/* 279:    */     private boolean firstCellOfRow;
/* 280:    */     private final Map<String, String> headerFooterMap;
/* 281:    */     
/* 282:    */     protected SheetTextExtractor()
/* 283:    */     {
/* 284:336 */       this.output = new StringBuffer();
/* 285:337 */       this.firstCellOfRow = true;
/* 286:338 */       this.headerFooterMap = (XSSFEventBasedExcelExtractor.this.includeHeadersFooters ? new HashMap() : null);
/* 287:    */     }
/* 288:    */     
/* 289:    */     public void startRow(int rowNum)
/* 290:    */     {
/* 291:343 */       this.firstCellOfRow = true;
/* 292:    */     }
/* 293:    */     
/* 294:    */     public void endRow(int rowNum)
/* 295:    */     {
/* 296:348 */       this.output.append('\n');
/* 297:    */     }
/* 298:    */     
/* 299:    */     public void cell(String cellRef, String formattedValue, XSSFComment comment)
/* 300:    */     {
/* 301:353 */       if (this.firstCellOfRow) {
/* 302:354 */         this.firstCellOfRow = false;
/* 303:    */       } else {
/* 304:356 */         this.output.append('\t');
/* 305:    */       }
/* 306:358 */       if (formattedValue != null)
/* 307:    */       {
/* 308:359 */         XSSFEventBasedExcelExtractor.this.checkMaxTextSize(this.output, formattedValue);
/* 309:360 */         this.output.append(formattedValue);
/* 310:    */       }
/* 311:362 */       if ((XSSFEventBasedExcelExtractor.this.includeCellComments) && (comment != null))
/* 312:    */       {
/* 313:363 */         String commentText = comment.getString().getString().replace('\n', ' ');
/* 314:364 */         this.output.append(formattedValue != null ? " Comment by " : "Comment by ");
/* 315:365 */         XSSFEventBasedExcelExtractor.this.checkMaxTextSize(this.output, commentText);
/* 316:366 */         if (commentText.startsWith(comment.getAuthor() + ": ")) {
/* 317:367 */           this.output.append(commentText);
/* 318:    */         } else {
/* 319:369 */           this.output.append(comment.getAuthor()).append(": ").append(commentText);
/* 320:    */         }
/* 321:    */       }
/* 322:    */     }
/* 323:    */     
/* 324:    */     public void headerFooter(String text, boolean isHeader, String tagName)
/* 325:    */     {
/* 326:376 */       if (this.headerFooterMap != null) {
/* 327:377 */         this.headerFooterMap.put(tagName, text);
/* 328:    */       }
/* 329:    */     }
/* 330:    */     
/* 331:    */     private void appendHeaderFooterText(StringBuffer buffer, String name)
/* 332:    */     {
/* 333:385 */       String text = (String)this.headerFooterMap.get(name);
/* 334:386 */       if ((text != null) && (text.length() > 0))
/* 335:    */       {
/* 336:390 */         text = handleHeaderFooterDelimiter(text, "&L");
/* 337:391 */         text = handleHeaderFooterDelimiter(text, "&C");
/* 338:392 */         text = handleHeaderFooterDelimiter(text, "&R");
/* 339:393 */         buffer.append(text).append('\n');
/* 340:    */       }
/* 341:    */     }
/* 342:    */     
/* 343:    */     private String handleHeaderFooterDelimiter(String text, String delimiter)
/* 344:    */     {
/* 345:401 */       int index = text.indexOf(delimiter);
/* 346:402 */       if (index == 0) {
/* 347:403 */         text = text.substring(2);
/* 348:404 */       } else if (index > 0) {
/* 349:405 */         text = text.substring(0, index) + "\t" + text.substring(index + 2);
/* 350:    */       }
/* 351:407 */       return text;
/* 352:    */     }
/* 353:    */     
/* 354:    */     void appendHeaderText(StringBuffer buffer)
/* 355:    */     {
/* 356:418 */       appendHeaderFooterText(buffer, "firstHeader");
/* 357:419 */       appendHeaderFooterText(buffer, "oddHeader");
/* 358:420 */       appendHeaderFooterText(buffer, "evenHeader");
/* 359:    */     }
/* 360:    */     
/* 361:    */     void appendFooterText(StringBuffer buffer)
/* 362:    */     {
/* 363:432 */       appendHeaderFooterText(buffer, "firstFooter");
/* 364:433 */       appendHeaderFooterText(buffer, "oddFooter");
/* 365:434 */       appendHeaderFooterText(buffer, "evenFooter");
/* 366:    */     }
/* 367:    */     
/* 368:    */     void appendCellText(StringBuffer buffer)
/* 369:    */     {
/* 370:441 */       XSSFEventBasedExcelExtractor.this.checkMaxTextSize(buffer, this.output.toString());
/* 371:442 */       buffer.append(this.output);
/* 372:    */     }
/* 373:    */     
/* 374:    */     void reset()
/* 375:    */     {
/* 376:449 */       this.output.setLength(0);
/* 377:450 */       this.firstCellOfRow = true;
/* 378:451 */       if (this.headerFooterMap != null) {
/* 379:452 */         this.headerFooterMap.clear();
/* 380:    */       }
/* 381:    */     }
/* 382:    */   }
/* 383:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor
 * JD-Core Version:    0.7.0.1
 */