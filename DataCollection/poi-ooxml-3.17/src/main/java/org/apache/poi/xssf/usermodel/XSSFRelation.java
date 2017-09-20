/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.apache.poi.POIXMLDocumentPart;
/*   6:    */ import org.apache.poi.POIXMLRelation;
/*   7:    */ import org.apache.poi.xssf.model.CalculationChain;
/*   8:    */ import org.apache.poi.xssf.model.CommentsTable;
/*   9:    */ import org.apache.poi.xssf.model.ExternalLinksTable;
/*  10:    */ import org.apache.poi.xssf.model.MapInfo;
/*  11:    */ import org.apache.poi.xssf.model.SharedStringsTable;
/*  12:    */ import org.apache.poi.xssf.model.SingleXmlCells;
/*  13:    */ import org.apache.poi.xssf.model.StylesTable;
/*  14:    */ import org.apache.poi.xssf.model.ThemesTable;
/*  15:    */ 
/*  16:    */ public final class XSSFRelation
/*  17:    */   extends POIXMLRelation
/*  18:    */ {
/*  19: 44 */   private static final Map<String, XSSFRelation> _table = new HashMap();
/*  20: 47 */   public static final XSSFRelation WORKBOOK = new XSSFRelation("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/workbook", "/xl/workbook.xml", null);
/*  21: 53 */   public static final XSSFRelation MACROS_WORKBOOK = new XSSFRelation("application/vnd.ms-excel.sheet.macroEnabled.main+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument", "/xl/workbook.xml", null);
/*  22: 59 */   public static final XSSFRelation TEMPLATE_WORKBOOK = new XSSFRelation("application/vnd.openxmlformats-officedocument.spreadsheetml.template.main+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument", "/xl/workbook.xml", null);
/*  23: 66 */   public static final XSSFRelation MACRO_TEMPLATE_WORKBOOK = new XSSFRelation("application/vnd.ms-excel.template.macroEnabled.main+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument", "/xl/workbook.xml", null);
/*  24: 72 */   public static final XSSFRelation MACRO_ADDIN_WORKBOOK = new XSSFRelation("application/vnd.ms-excel.addin.macroEnabled.main+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument", "/xl/workbook.xml", null);
/*  25: 79 */   public static final XSSFRelation XLSB_BINARY_WORKBOOK = new XSSFRelation("application/vnd.ms-excel.sheet.binary.macroEnabled.main", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument", "/xl/workbook.bin", null);
/*  26: 86 */   public static final XSSFRelation WORKSHEET = new XSSFRelation("application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet", "/xl/worksheets/sheet#.xml", XSSFSheet.class);
/*  27: 93 */   public static final XSSFRelation CHARTSHEET = new XSSFRelation("application/vnd.openxmlformats-officedocument.spreadsheetml.chartsheet+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/chartsheet", "/xl/chartsheets/sheet#.xml", XSSFChartSheet.class);
/*  28:100 */   public static final XSSFRelation SHARED_STRINGS = new XSSFRelation("application/vnd.openxmlformats-officedocument.spreadsheetml.sharedStrings+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/sharedStrings", "/xl/sharedStrings.xml", SharedStringsTable.class);
/*  29:107 */   public static final XSSFRelation STYLES = new XSSFRelation("application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles", "/xl/styles.xml", StylesTable.class);
/*  30:114 */   public static final XSSFRelation DRAWINGS = new XSSFRelation("application/vnd.openxmlformats-officedocument.drawing+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/drawing", "/xl/drawings/drawing#.xml", XSSFDrawing.class);
/*  31:121 */   public static final XSSFRelation VML_DRAWINGS = new XSSFRelation("application/vnd.openxmlformats-officedocument.vmlDrawing", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/vmlDrawing", "/xl/drawings/vmlDrawing#.vml", XSSFVMLDrawing.class);
/*  32:128 */   public static final XSSFRelation CHART = new XSSFRelation("application/vnd.openxmlformats-officedocument.drawingml.chart+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/chart", "/xl/charts/chart#.xml", XSSFChart.class);
/*  33:135 */   public static final XSSFRelation CUSTOM_XML_MAPPINGS = new XSSFRelation("application/xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/xmlMaps", "/xl/xmlMaps.xml", MapInfo.class);
/*  34:142 */   public static final XSSFRelation SINGLE_XML_CELLS = new XSSFRelation("application/vnd.openxmlformats-officedocument.spreadsheetml.tableSingleCells+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/tableSingleCells", "/xl/tables/tableSingleCells#.xml", SingleXmlCells.class);
/*  35:149 */   public static final XSSFRelation TABLE = new XSSFRelation("application/vnd.openxmlformats-officedocument.spreadsheetml.table+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/table", "/xl/tables/table#.xml", XSSFTable.class);
/*  36:156 */   public static final XSSFRelation IMAGES = new XSSFRelation(null, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", null, XSSFPictureData.class);
/*  37:163 */   public static final XSSFRelation IMAGE_EMF = new XSSFRelation("image/x-emf", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/xl/media/image#.emf", XSSFPictureData.class);
/*  38:170 */   public static final XSSFRelation IMAGE_WMF = new XSSFRelation("image/x-wmf", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/xl/media/image#.wmf", XSSFPictureData.class);
/*  39:177 */   public static final XSSFRelation IMAGE_PICT = new XSSFRelation("image/pict", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/xl/media/image#.pict", XSSFPictureData.class);
/*  40:184 */   public static final XSSFRelation IMAGE_JPEG = new XSSFRelation("image/jpeg", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/xl/media/image#.jpeg", XSSFPictureData.class);
/*  41:191 */   public static final XSSFRelation IMAGE_PNG = new XSSFRelation("image/png", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/xl/media/image#.png", XSSFPictureData.class);
/*  42:198 */   public static final XSSFRelation IMAGE_DIB = new XSSFRelation("image/dib", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/xl/media/image#.dib", XSSFPictureData.class);
/*  43:205 */   public static final XSSFRelation IMAGE_GIF = new XSSFRelation("image/gif", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/xl/media/image#.gif", XSSFPictureData.class);
/*  44:212 */   public static final XSSFRelation IMAGE_TIFF = new XSSFRelation("image/tiff", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/xl/media/image#.tiff", XSSFPictureData.class);
/*  45:219 */   public static final XSSFRelation IMAGE_EPS = new XSSFRelation("image/x-eps", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/xl/media/image#.eps", XSSFPictureData.class);
/*  46:226 */   public static final XSSFRelation IMAGE_BMP = new XSSFRelation("image/x-ms-bmp", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/xl/media/image#.bmp", XSSFPictureData.class);
/*  47:233 */   public static final XSSFRelation IMAGE_WPG = new XSSFRelation("image/x-wpg", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/xl/media/image#.wpg", XSSFPictureData.class);
/*  48:240 */   public static final XSSFRelation SHEET_COMMENTS = new XSSFRelation("application/vnd.openxmlformats-officedocument.spreadsheetml.comments+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/comments", "/xl/comments#.xml", CommentsTable.class);
/*  49:247 */   public static final XSSFRelation SHEET_HYPERLINKS = new XSSFRelation(null, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink", null, null);
/*  50:254 */   public static final XSSFRelation OLEEMBEDDINGS = new XSSFRelation(null, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/oleObject", null, null);
/*  51:261 */   public static final XSSFRelation PACKEMBEDDINGS = new XSSFRelation(null, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/package", null, null);
/*  52:268 */   public static final XSSFRelation VBA_MACROS = new XSSFRelation("application/vnd.ms-office.vbaProject", "http://schemas.microsoft.com/office/2006/relationships/vbaProject", "/xl/vbaProject.bin", XSSFVBAPart.class);
/*  53:275 */   public static final XSSFRelation ACTIVEX_CONTROLS = new XSSFRelation("application/vnd.ms-office.activeX+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/control", "/xl/activeX/activeX#.xml", null);
/*  54:282 */   public static final XSSFRelation ACTIVEX_BINS = new XSSFRelation("application/vnd.ms-office.activeX", "http://schemas.microsoft.com/office/2006/relationships/activeXControlBinary", "/xl/activeX/activeX#.bin", null);
/*  55:289 */   public static final XSSFRelation MACRO_SHEET_BIN = new XSSFRelation(null, "http://schemas.microsoft.com/office/2006/relationships/xlMacrosheet", "/xl/macroSheets/sheet#.bin", null);
/*  56:296 */   public static final XSSFRelation INTL_MACRO_SHEET_BIN = new XSSFRelation(null, "http://schemas.microsoft.com/office/2006/relationships/xlIntlMacrosheet", "/xl/macroSheets/sheet#.bin", null);
/*  57:303 */   public static final XSSFRelation DIALOG_SHEET_BIN = new XSSFRelation(null, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/dialogsheet", "/xl/dialogSheets/sheet#.bin", null);
/*  58:310 */   public static final XSSFRelation THEME = new XSSFRelation("application/vnd.openxmlformats-officedocument.theme+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme", "/xl/theme/theme#.xml", ThemesTable.class);
/*  59:317 */   public static final XSSFRelation CALC_CHAIN = new XSSFRelation("application/vnd.openxmlformats-officedocument.spreadsheetml.calcChain+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/calcChain", "/xl/calcChain.xml", CalculationChain.class);
/*  60:324 */   public static final XSSFRelation EXTERNAL_LINKS = new XSSFRelation("application/vnd.openxmlformats-officedocument.spreadsheetml.externalLink+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/externalLink", "/xl/externalLinks/externalLink#.xmll", ExternalLinksTable.class);
/*  61:331 */   public static final XSSFRelation PRINTER_SETTINGS = new XSSFRelation("application/vnd.openxmlformats-officedocument.spreadsheetml.printerSettings", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/printerSettings", "/xl/printerSettings/printerSettings#.bin", null);
/*  62:337 */   public static final XSSFRelation PIVOT_TABLE = new XSSFRelation("application/vnd.openxmlformats-officedocument.spreadsheetml.pivotTable+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/pivotTable", "/xl/pivotTables/pivotTable#.xml", XSSFPivotTable.class);
/*  63:343 */   public static final XSSFRelation PIVOT_CACHE_DEFINITION = new XSSFRelation("application/vnd.openxmlformats-officedocument.spreadsheetml.pivotCacheDefinition+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/pivotCacheDefinition", "/xl/pivotCache/pivotCacheDefinition#.xml", XSSFPivotCacheDefinition.class);
/*  64:349 */   public static final XSSFRelation PIVOT_CACHE_RECORDS = new XSSFRelation("application/vnd.openxmlformats-officedocument.spreadsheetml.pivotCacheRecords+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/pivotCacheRecords", "/xl/pivotCache/pivotCacheRecords#.xml", XSSFPivotCacheRecords.class);
/*  65:356 */   public static final XSSFRelation CTRL_PROP_RECORDS = new XSSFRelation(null, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/ctrlProp", "/xl/ctrlProps/ctrlProp#.xml", null);
/*  66:363 */   public static final XSSFRelation CUSTOM_PROPERTIES = new XSSFRelation("application/vnd.openxmlformats-officedocument.spreadsheetml.customProperty", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/customProperty", "/xl/customProperty#.bin", null);
/*  67:    */   public static final String NS_SPREADSHEETML = "http://schemas.openxmlformats.org/spreadsheetml/2006/main";
/*  68:    */   public static final String NS_DRAWINGML = "http://schemas.openxmlformats.org/drawingml/2006/main";
/*  69:    */   public static final String NS_CHART = "http://schemas.openxmlformats.org/drawingml/2006/chart";
/*  70:    */   
/*  71:    */   private XSSFRelation(String type, String rel, String defaultName, Class<? extends POIXMLDocumentPart> cls)
/*  72:    */   {
/*  73:375 */     super(type, rel, defaultName, cls);
/*  74:376 */     _table.put(rel, this);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static XSSFRelation getInstance(String rel)
/*  78:    */   {
/*  79:387 */     return (XSSFRelation)_table.get(rel);
/*  80:    */   }
/*  81:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFRelation
 * JD-Core Version:    0.7.0.1
 */