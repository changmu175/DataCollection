/*   1:    */ package org.apache.poi.xssf.extractor;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import org.apache.poi.POIXMLTextExtractor;
/*   7:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*   8:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*   9:    */ import org.apache.poi.ss.extractor.ExcelExtractor;
/*  10:    */ import org.apache.poi.ss.usermodel.DataFormatter;
/*  11:    */ import org.apache.poi.util.POILogFactory;
/*  12:    */ import org.apache.poi.util.POILogger;
/*  13:    */ import org.apache.poi.xssf.binary.XSSFBCommentsTable;
/*  14:    */ import org.apache.poi.xssf.binary.XSSFBHyperlinksTable;
/*  15:    */ import org.apache.poi.xssf.binary.XSSFBSharedStringsTable;
/*  16:    */ import org.apache.poi.xssf.binary.XSSFBSheetHandler;
/*  17:    */ import org.apache.poi.xssf.binary.XSSFBStylesTable;
/*  18:    */ import org.apache.poi.xssf.eventusermodel.XSSFBReader;
/*  19:    */ import org.apache.poi.xssf.eventusermodel.XSSFBReader.SheetIterator;
/*  20:    */ import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
/*  21:    */ import org.apache.poi.xssf.usermodel.XSSFRelation;
/*  22:    */ import org.apache.xmlbeans.XmlException;
/*  23:    */ import org.xml.sax.SAXException;
/*  24:    */ 
/*  25:    */ public class XSSFBEventBasedExcelExtractor
/*  26:    */   extends XSSFEventBasedExcelExtractor
/*  27:    */   implements ExcelExtractor
/*  28:    */ {
/*  29: 48 */   private static final POILogger LOGGER = POILogFactory.getLogger(XSSFBEventBasedExcelExtractor.class);
/*  30: 50 */   public static final XSSFRelation[] SUPPORTED_TYPES = { XSSFRelation.XLSB_BINARY_WORKBOOK };
/*  31: 54 */   private boolean handleHyperlinksInCells = false;
/*  32:    */   
/*  33:    */   public XSSFBEventBasedExcelExtractor(String path)
/*  34:    */     throws XmlException, OpenXML4JException, IOException
/*  35:    */   {
/*  36: 57 */     super(path);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public XSSFBEventBasedExcelExtractor(OPCPackage container)
/*  40:    */     throws XmlException, OpenXML4JException, IOException
/*  41:    */   {
/*  42: 61 */     super(container);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public static void main(String[] args)
/*  46:    */     throws Exception
/*  47:    */   {
/*  48: 65 */     if (args.length < 1)
/*  49:    */     {
/*  50: 66 */       System.err.println("Use:");
/*  51: 67 */       System.err.println("  XSSFBEventBasedExcelExtractor <filename.xlsb>");
/*  52: 68 */       System.exit(1);
/*  53:    */     }
/*  54: 70 */     POIXMLTextExtractor extractor = new XSSFBEventBasedExcelExtractor(args[0]);
/*  55:    */     
/*  56: 72 */     System.out.println(extractor.getText());
/*  57: 73 */     extractor.close();
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setHandleHyperlinksInCells(boolean handleHyperlinksInCells)
/*  61:    */   {
/*  62: 77 */     this.handleHyperlinksInCells = handleHyperlinksInCells;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setFormulasNotResults(boolean formulasNotResults)
/*  66:    */   {
/*  67: 87 */     throw new IllegalArgumentException("Not currently supported");
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void processSheet(XSSFSheetXMLHandler.SheetContentsHandler sheetContentsExtractor, XSSFBStylesTable styles, XSSFBCommentsTable comments, XSSFBSharedStringsTable strings, InputStream sheetInputStream)
/*  71:    */     throws IOException, SAXException
/*  72:    */   {
/*  73:    */     DataFormatter formatter;
/*  74:    */     DataFormatter formatter;
/*  75:102 */     if (getLocale() == null) {
/*  76:103 */       formatter = new DataFormatter();
/*  77:    */     } else {
/*  78:105 */       formatter = new DataFormatter(getLocale());
/*  79:    */     }
/*  80:108 */     XSSFBSheetHandler xssfbSheetHandler = new XSSFBSheetHandler(sheetInputStream, styles, comments, strings, sheetContentsExtractor, formatter, getFormulasNotResults());
/*  81:    */     
/*  82:    */ 
/*  83:    */ 
/*  84:112 */     xssfbSheetHandler.parse();
/*  85:    */   }
/*  86:    */   
/*  87:    */   public String getText()
/*  88:    */   {
/*  89:    */     try
/*  90:    */     {
/*  91:120 */       XSSFBSharedStringsTable strings = new XSSFBSharedStringsTable(getPackage());
/*  92:121 */       XSSFBReader xssfbReader = new XSSFBReader(getPackage());
/*  93:122 */       XSSFBStylesTable styles = xssfbReader.getXSSFBStylesTable();
/*  94:123 */       XSSFBReader.SheetIterator iter = (XSSFBReader.SheetIterator)xssfbReader.getSheetsData();
/*  95:    */       
/*  96:125 */       StringBuffer text = new StringBuffer();
/*  97:126 */       SheetTextExtractor sheetExtractor = new SheetTextExtractor(this);
/*  98:127 */       XSSFBHyperlinksTable hyperlinksTable = null;
/*  99:128 */       while (iter.hasNext())
/* 100:    */       {
/* 101:129 */         InputStream stream = iter.next();
/* 102:130 */         if (getIncludeSheetNames())
/* 103:    */         {
/* 104:131 */           text.append(iter.getSheetName());
/* 105:132 */           text.append('\n');
/* 106:    */         }
/* 107:134 */         if (this.handleHyperlinksInCells) {
/* 108:135 */           hyperlinksTable = new XSSFBHyperlinksTable(iter.getSheetPart());
/* 109:    */         }
/* 110:137 */         XSSFBCommentsTable comments = getIncludeCellComments() ? iter.getXSSFBSheetComments() : null;
/* 111:138 */         processSheet(sheetExtractor, styles, comments, strings, stream);
/* 112:139 */         if (getIncludeHeadersFooters()) {
/* 113:140 */           sheetExtractor.appendHeaderText(text);
/* 114:    */         }
/* 115:142 */         sheetExtractor.appendCellText(text);
/* 116:143 */         if (getIncludeTextBoxes()) {
/* 117:144 */           processShapes(iter.getShapes(), text);
/* 118:    */         }
/* 119:146 */         if (getIncludeHeadersFooters()) {
/* 120:147 */           sheetExtractor.appendFooterText(text);
/* 121:    */         }
/* 122:149 */         sheetExtractor.reset();
/* 123:150 */         stream.close();
/* 124:    */       }
/* 125:153 */       return text.toString();
/* 126:    */     }
/* 127:    */     catch (IOException e)
/* 128:    */     {
/* 129:155 */       LOGGER.log(5, new Object[] { e });
/* 130:156 */       return null;
/* 131:    */     }
/* 132:    */     catch (SAXException se)
/* 133:    */     {
/* 134:158 */       LOGGER.log(5, new Object[] { se });
/* 135:159 */       return null;
/* 136:    */     }
/* 137:    */     catch (OpenXML4JException o4je)
/* 138:    */     {
/* 139:161 */       LOGGER.log(5, new Object[] { o4je });
/* 140:    */     }
/* 141:162 */     return null;
/* 142:    */   }
/* 143:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xssf.extractor.XSSFBEventBasedExcelExtractor

 * JD-Core Version:    0.7.0.1

 */