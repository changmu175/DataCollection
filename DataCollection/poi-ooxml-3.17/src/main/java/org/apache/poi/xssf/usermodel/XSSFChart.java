/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.List;
/*   7:    */ import javax.xml.namespace.QName;
/*   8:    */ import org.apache.poi.POIXMLDocumentPart;
/*   9:    */ import org.apache.poi.POIXMLTypeLoader;
/*  10:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  11:    */ import org.apache.poi.ss.usermodel.Chart;
/*  12:    */ import org.apache.poi.ss.usermodel.charts.AxisPosition;
/*  13:    */ import org.apache.poi.ss.usermodel.charts.ChartAxis;
/*  14:    */ import org.apache.poi.ss.usermodel.charts.ChartAxisFactory;
/*  15:    */ import org.apache.poi.ss.usermodel.charts.ChartData;
/*  16:    */ import org.apache.poi.util.Internal;
/*  17:    */ import org.apache.poi.util.Removal;
/*  18:    */ import org.apache.poi.xssf.usermodel.charts.XSSFCategoryAxis;
/*  19:    */ import org.apache.poi.xssf.usermodel.charts.XSSFChartAxis;
/*  20:    */ import org.apache.poi.xssf.usermodel.charts.XSSFChartDataFactory;
/*  21:    */ import org.apache.poi.xssf.usermodel.charts.XSSFChartLegend;
/*  22:    */ import org.apache.poi.xssf.usermodel.charts.XSSFDateAxis;
/*  23:    */ import org.apache.poi.xssf.usermodel.charts.XSSFManualLayout;
/*  24:    */ import org.apache.poi.xssf.usermodel.charts.XSSFValueAxis;
/*  25:    */ import org.apache.xmlbeans.SchemaType;
/*  26:    */ import org.apache.xmlbeans.XmlException;
/*  27:    */ import org.apache.xmlbeans.XmlObject;
/*  28:    */ import org.apache.xmlbeans.XmlOptions;
/*  29:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean;
/*  30:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx;
/*  31:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
/*  32:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace;
/*  33:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace.Factory;
/*  34:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTDateAx;
/*  35:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTPageMargins;
/*  36:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
/*  37:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTPrintSettings;
/*  38:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef;
/*  39:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle;
/*  40:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTTx;
/*  41:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx;
/*  42:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.ChartSpaceDocument;
/*  43:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.ChartSpaceDocument.Factory;
/*  44:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
/*  45:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
/*  46:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextField;
/*  47:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
/*  48:    */ import org.w3c.dom.Node;
/*  49:    */ import org.w3c.dom.NodeList;
/*  50:    */ import org.w3c.dom.Text;
/*  51:    */ 
/*  52:    */ public final class XSSFChart
/*  53:    */   extends POIXMLDocumentPart
/*  54:    */   implements Chart, ChartAxisFactory
/*  55:    */ {
/*  56:    */   private XSSFGraphicFrame frame;
/*  57:    */   private CTChartSpace chartSpace;
/*  58:    */   private CTChart chart;
/*  59: 87 */   List<XSSFChartAxis> axis = new ArrayList();
/*  60:    */   
/*  61:    */   protected XSSFChart()
/*  62:    */   {
/*  63: 94 */     createChart();
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected XSSFChart(PackagePart part)
/*  67:    */     throws IOException, XmlException
/*  68:    */   {
/*  69:106 */     super(part);
/*  70:    */     
/*  71:108 */     this.chartSpace = ChartSpaceDocument.Factory.parse(part.getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS).getChartSpace();
/*  72:109 */     this.chart = this.chartSpace.getChart();
/*  73:    */   }
/*  74:    */   
/*  75:    */   private void createChart()
/*  76:    */   {
/*  77:117 */     this.chartSpace = CTChartSpace.Factory.newInstance();
/*  78:118 */     this.chart = this.chartSpace.addNewChart();
/*  79:119 */     CTPlotArea plotArea = this.chart.addNewPlotArea();
/*  80:    */     
/*  81:121 */     plotArea.addNewLayout();
/*  82:122 */     this.chart.addNewPlotVisOnly().setVal(true);
/*  83:    */     
/*  84:124 */     CTPrintSettings printSettings = this.chartSpace.addNewPrintSettings();
/*  85:125 */     printSettings.addNewHeaderFooter();
/*  86:    */     
/*  87:127 */     CTPageMargins pageMargins = printSettings.addNewPageMargins();
/*  88:128 */     pageMargins.setB(0.75D);
/*  89:129 */     pageMargins.setL(0.7D);
/*  90:130 */     pageMargins.setR(0.7D);
/*  91:131 */     pageMargins.setT(0.75D);
/*  92:132 */     pageMargins.setHeader(0.3D);
/*  93:133 */     pageMargins.setFooter(0.3D);
/*  94:134 */     printSettings.addNewPageSetup();
/*  95:    */   }
/*  96:    */   
/*  97:    */   @Internal
/*  98:    */   public CTChartSpace getCTChartSpace()
/*  99:    */   {
/* 100:144 */     return this.chartSpace;
/* 101:    */   }
/* 102:    */   
/* 103:    */   @Internal
/* 104:    */   public CTChart getCTChart()
/* 105:    */   {
/* 106:154 */     return this.chart;
/* 107:    */   }
/* 108:    */   
/* 109:    */   protected void commit()
/* 110:    */     throws IOException
/* 111:    */   {
/* 112:159 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 113:    */     
/* 114:    */ 
/* 115:    */ 
/* 116:    */ 
/* 117:    */ 
/* 118:    */ 
/* 119:    */ 
/* 120:    */ 
/* 121:168 */     xmlOptions.setSaveSyntheticDocumentElement(new QName(CTChartSpace.type.getName().getNamespaceURI(), "chartSpace", "c"));
/* 122:    */     
/* 123:170 */     PackagePart part = getPackagePart();
/* 124:171 */     OutputStream out = part.getOutputStream();
/* 125:172 */     this.chartSpace.save(out, xmlOptions);
/* 126:173 */     out.close();
/* 127:    */   }
/* 128:    */   
/* 129:    */   public XSSFGraphicFrame getGraphicFrame()
/* 130:    */   {
/* 131:181 */     return this.frame;
/* 132:    */   }
/* 133:    */   
/* 134:    */   protected void setGraphicFrame(XSSFGraphicFrame frame)
/* 135:    */   {
/* 136:188 */     this.frame = frame;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public XSSFChartDataFactory getChartDataFactory()
/* 140:    */   {
/* 141:192 */     return XSSFChartDataFactory.getInstance();
/* 142:    */   }
/* 143:    */   
/* 144:    */   public XSSFChart getChartAxisFactory()
/* 145:    */   {
/* 146:196 */     return this;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void plot(ChartData data, ChartAxis... chartAxis)
/* 150:    */   {
/* 151:200 */     data.fillChart(this, chartAxis);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public XSSFValueAxis createValueAxis(AxisPosition pos)
/* 155:    */   {
/* 156:204 */     long id = this.axis.size() + 1;
/* 157:205 */     XSSFValueAxis valueAxis = new XSSFValueAxis(this, id, pos);
/* 158:206 */     if (this.axis.size() == 1)
/* 159:    */     {
/* 160:207 */       ChartAxis ax = (ChartAxis)this.axis.get(0);
/* 161:208 */       ax.crossAxis(valueAxis);
/* 162:209 */       valueAxis.crossAxis(ax);
/* 163:    */     }
/* 164:211 */     this.axis.add(valueAxis);
/* 165:212 */     return valueAxis;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public XSSFCategoryAxis createCategoryAxis(AxisPosition pos)
/* 169:    */   {
/* 170:216 */     long id = this.axis.size() + 1;
/* 171:217 */     XSSFCategoryAxis categoryAxis = new XSSFCategoryAxis(this, id, pos);
/* 172:218 */     if (this.axis.size() == 1)
/* 173:    */     {
/* 174:219 */       ChartAxis ax = (ChartAxis)this.axis.get(0);
/* 175:220 */       ax.crossAxis(categoryAxis);
/* 176:221 */       categoryAxis.crossAxis(ax);
/* 177:    */     }
/* 178:223 */     this.axis.add(categoryAxis);
/* 179:224 */     return categoryAxis;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public XSSFDateAxis createDateAxis(AxisPosition pos)
/* 183:    */   {
/* 184:228 */     long id = this.axis.size() + 1;
/* 185:229 */     XSSFDateAxis dateAxis = new XSSFDateAxis(this, id, pos);
/* 186:230 */     if (this.axis.size() == 1)
/* 187:    */     {
/* 188:231 */       ChartAxis ax = (ChartAxis)this.axis.get(0);
/* 189:232 */       ax.crossAxis(dateAxis);
/* 190:233 */       dateAxis.crossAxis(ax);
/* 191:    */     }
/* 192:235 */     this.axis.add(dateAxis);
/* 193:236 */     return dateAxis;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public List<? extends XSSFChartAxis> getAxis()
/* 197:    */   {
/* 198:240 */     if ((this.axis.isEmpty()) && (hasAxis())) {
/* 199:241 */       parseAxis();
/* 200:    */     }
/* 201:243 */     return this.axis;
/* 202:    */   }
/* 203:    */   
/* 204:    */   public XSSFManualLayout getManualLayout()
/* 205:    */   {
/* 206:247 */     return new XSSFManualLayout(this);
/* 207:    */   }
/* 208:    */   
/* 209:    */   public boolean isPlotOnlyVisibleCells()
/* 210:    */   {
/* 211:255 */     return this.chart.getPlotVisOnly().getVal();
/* 212:    */   }
/* 213:    */   
/* 214:    */   public void setPlotOnlyVisibleCells(boolean plotVisOnly)
/* 215:    */   {
/* 216:263 */     this.chart.getPlotVisOnly().setVal(plotVisOnly);
/* 217:    */   }
/* 218:    */   
/* 219:    */   @Deprecated
/* 220:    */   @Removal(version="4.0")
/* 221:    */   public XSSFRichTextString getTitle()
/* 222:    */   {
/* 223:275 */     return getTitleText();
/* 224:    */   }
/* 225:    */   
/* 226:    */   public XSSFRichTextString getTitleText()
/* 227:    */   {
/* 228:288 */     if (!this.chart.isSetTitle()) {
/* 229:289 */       return null;
/* 230:    */     }
/* 231:293 */     CTTitle title = this.chart.getTitle();
/* 232:    */     
/* 233:295 */     StringBuffer text = new StringBuffer();
/* 234:296 */     XmlObject[] t = title.selectPath("declare namespace a='http://schemas.openxmlformats.org/drawingml/2006/main' .//a:t");
/* 235:298 */     for (int m = 0; m < t.length; m++)
/* 236:    */     {
/* 237:299 */       NodeList kids = t[m].getDomNode().getChildNodes();
/* 238:300 */       int count = kids.getLength();
/* 239:301 */       for (int n = 0; n < count; n++)
/* 240:    */       {
/* 241:302 */         Node kid = kids.item(n);
/* 242:303 */         if ((kid instanceof Text)) {
/* 243:304 */           text.append(kid.getNodeValue());
/* 244:    */         }
/* 245:    */       }
/* 246:    */     }
/* 247:309 */     return new XSSFRichTextString(text.toString());
/* 248:    */   }
/* 249:    */   
/* 250:    */   @Deprecated
/* 251:    */   @Removal(version="4.0")
/* 252:    */   public void setTitle(String newTitle) {}
/* 253:    */   
/* 254:    */   public void setTitleText(String newTitle)
/* 255:    */   {
/* 256:    */     CTTitle ctTitle;
/* 257:    */     CTTitle ctTitle;
/* 258:329 */     if (this.chart.isSetTitle()) {
/* 259:330 */       ctTitle = this.chart.getTitle();
/* 260:    */     } else {
/* 261:332 */       ctTitle = this.chart.addNewTitle();
/* 262:    */     }
/* 263:    */     CTTx tx;
/* 264:    */     CTTx tx;
/* 265:336 */     if (ctTitle.isSetTx()) {
/* 266:337 */       tx = ctTitle.getTx();
/* 267:    */     } else {
/* 268:339 */       tx = ctTitle.addNewTx();
/* 269:    */     }
/* 270:342 */     if (tx.isSetStrRef()) {
/* 271:343 */       tx.unsetStrRef();
/* 272:    */     }
/* 273:    */     CTTextBody rich;
/* 274:    */     CTTextBody rich;
/* 275:347 */     if (tx.isSetRich())
/* 276:    */     {
/* 277:348 */       rich = tx.getRich();
/* 278:    */     }
/* 279:    */     else
/* 280:    */     {
/* 281:350 */       rich = tx.addNewRich();
/* 282:351 */       rich.addNewBodyPr();
/* 283:    */     }
/* 284:    */     CTTextParagraph para;
/* 285:    */     CTTextParagraph para;
/* 286:355 */     if (rich.sizeOfPArray() > 0) {
/* 287:356 */       para = rich.getPArray(0);
/* 288:    */     } else {
/* 289:358 */       para = rich.addNewP();
/* 290:    */     }
/* 291:361 */     if (para.sizeOfRArray() > 0)
/* 292:    */     {
/* 293:362 */       CTRegularTextRun run = para.getRArray(0);
/* 294:363 */       run.setT(newTitle);
/* 295:    */     }
/* 296:364 */     else if (para.sizeOfFldArray() > 0)
/* 297:    */     {
/* 298:365 */       CTTextField fld = para.getFldArray(0);
/* 299:366 */       fld.setT(newTitle);
/* 300:    */     }
/* 301:    */     else
/* 302:    */     {
/* 303:368 */       CTRegularTextRun run = para.addNewR();
/* 304:369 */       run.setT(newTitle);
/* 305:    */     }
/* 306:    */   }
/* 307:    */   
/* 308:    */   public String getTitleFormula()
/* 309:    */   {
/* 310:378 */     if (!this.chart.isSetTitle()) {
/* 311:379 */       return null;
/* 312:    */     }
/* 313:382 */     CTTitle title = this.chart.getTitle();
/* 314:384 */     if (!title.isSetTx()) {
/* 315:385 */       return null;
/* 316:    */     }
/* 317:388 */     CTTx tx = title.getTx();
/* 318:390 */     if (!tx.isSetStrRef()) {
/* 319:391 */       return null;
/* 320:    */     }
/* 321:394 */     return tx.getStrRef().getF();
/* 322:    */   }
/* 323:    */   
/* 324:    */   public void setTitleFormula(String formula)
/* 325:    */   {
/* 326:    */     CTTitle ctTitle;
/* 327:    */     CTTitle ctTitle;
/* 328:403 */     if (this.chart.isSetTitle()) {
/* 329:404 */       ctTitle = this.chart.getTitle();
/* 330:    */     } else {
/* 331:406 */       ctTitle = this.chart.addNewTitle();
/* 332:    */     }
/* 333:    */     CTTx tx;
/* 334:    */     CTTx tx;
/* 335:410 */     if (ctTitle.isSetTx()) {
/* 336:411 */       tx = ctTitle.getTx();
/* 337:    */     } else {
/* 338:413 */       tx = ctTitle.addNewTx();
/* 339:    */     }
/* 340:416 */     if (tx.isSetRich()) {
/* 341:417 */       tx.unsetRich();
/* 342:    */     }
/* 343:    */     CTStrRef strRef;
/* 344:    */     CTStrRef strRef;
/* 345:421 */     if (tx.isSetStrRef()) {
/* 346:422 */       strRef = tx.getStrRef();
/* 347:    */     } else {
/* 348:424 */       strRef = tx.addNewStrRef();
/* 349:    */     }
/* 350:427 */     strRef.setF(formula);
/* 351:    */   }
/* 352:    */   
/* 353:    */   public XSSFChartLegend getOrCreateLegend()
/* 354:    */   {
/* 355:431 */     return new XSSFChartLegend(this);
/* 356:    */   }
/* 357:    */   
/* 358:    */   public void deleteLegend()
/* 359:    */   {
/* 360:435 */     if (this.chart.isSetLegend()) {
/* 361:436 */       this.chart.unsetLegend();
/* 362:    */     }
/* 363:    */   }
/* 364:    */   
/* 365:    */   private boolean hasAxis()
/* 366:    */   {
/* 367:441 */     CTPlotArea ctPlotArea = this.chart.getPlotArea();
/* 368:442 */     int totalAxisCount = ctPlotArea.sizeOfValAxArray() + ctPlotArea.sizeOfCatAxArray() + ctPlotArea.sizeOfDateAxArray() + ctPlotArea.sizeOfSerAxArray();
/* 369:    */     
/* 370:    */ 
/* 371:    */ 
/* 372:    */ 
/* 373:447 */     return totalAxisCount > 0;
/* 374:    */   }
/* 375:    */   
/* 376:    */   private void parseAxis()
/* 377:    */   {
/* 378:452 */     parseCategoryAxis();
/* 379:453 */     parseDateAxis();
/* 380:454 */     parseValueAxis();
/* 381:    */   }
/* 382:    */   
/* 383:    */   private void parseCategoryAxis()
/* 384:    */   {
/* 385:458 */     for (CTCatAx catAx : this.chart.getPlotArea().getCatAxArray()) {
/* 386:459 */       this.axis.add(new XSSFCategoryAxis(this, catAx));
/* 387:    */     }
/* 388:    */   }
/* 389:    */   
/* 390:    */   private void parseDateAxis()
/* 391:    */   {
/* 392:464 */     for (CTDateAx dateAx : this.chart.getPlotArea().getDateAxArray()) {
/* 393:465 */       this.axis.add(new XSSFDateAxis(this, dateAx));
/* 394:    */     }
/* 395:    */   }
/* 396:    */   
/* 397:    */   private void parseValueAxis()
/* 398:    */   {
/* 399:470 */     for (CTValAx valAx : this.chart.getPlotArea().getValAxArray()) {
/* 400:471 */       this.axis.add(new XSSFValueAxis(this, valAx));
/* 401:    */     }
/* 402:    */   }
/* 403:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFChart
 * JD-Core Version:    0.7.0.1
 */