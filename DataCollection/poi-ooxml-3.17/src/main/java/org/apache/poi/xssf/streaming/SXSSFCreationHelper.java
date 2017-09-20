/*  1:   */ package org.apache.poi.xssf.streaming;
/*  2:   */ 
/*  3:   */ import org.apache.poi.common.usermodel.HyperlinkType;
/*  4:   */ import org.apache.poi.ss.usermodel.ClientAnchor;
/*  5:   */ import org.apache.poi.ss.usermodel.CreationHelper;
/*  6:   */ import org.apache.poi.ss.usermodel.DataFormat;
/*  7:   */ import org.apache.poi.ss.usermodel.ExtendedColor;
/*  8:   */ import org.apache.poi.ss.usermodel.Hyperlink;
/*  9:   */ import org.apache.poi.ss.util.AreaReference;
/* 10:   */ import org.apache.poi.ss.util.CellReference;
/* 11:   */ import org.apache.poi.util.Internal;
/* 12:   */ import org.apache.poi.util.POILogFactory;
/* 13:   */ import org.apache.poi.util.POILogger;
/* 14:   */ import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
/* 15:   */ import org.apache.poi.xssf.usermodel.XSSFRichTextString;
/* 16:   */ 
/* 17:   */ public class SXSSFCreationHelper
/* 18:   */   implements CreationHelper
/* 19:   */ {
/* 20:40 */   private static final POILogger logger = POILogFactory.getLogger(SXSSFCreationHelper.class);
/* 21:   */   private final SXSSFWorkbook wb;
/* 22:   */   private final XSSFCreationHelper helper;
/* 23:   */   
/* 24:   */   @Internal
/* 25:   */   public SXSSFCreationHelper(SXSSFWorkbook workbook)
/* 26:   */   {
/* 27:52 */     this.helper = new XSSFCreationHelper(workbook.getXSSFWorkbook());
/* 28:53 */     this.wb = workbook;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public XSSFRichTextString createRichTextString(String text)
/* 32:   */   {
/* 33:58 */     logger.log(3, new Object[] { "SXSSF doesn't support Rich Text Strings, any formatting information will be lost" });
/* 34:59 */     return new XSSFRichTextString(text);
/* 35:   */   }
/* 36:   */   
/* 37:   */   public SXSSFFormulaEvaluator createFormulaEvaluator()
/* 38:   */   {
/* 39:64 */     return new SXSSFFormulaEvaluator(this.wb);
/* 40:   */   }
/* 41:   */   
/* 42:   */   public DataFormat createDataFormat()
/* 43:   */   {
/* 44:70 */     return this.helper.createDataFormat();
/* 45:   */   }
/* 46:   */   
/* 47:   */   public Hyperlink createHyperlink(HyperlinkType type)
/* 48:   */   {
/* 49:74 */     return this.helper.createHyperlink(type);
/* 50:   */   }
/* 51:   */   
/* 52:   */   public ExtendedColor createExtendedColor()
/* 53:   */   {
/* 54:78 */     return this.helper.createExtendedColor();
/* 55:   */   }
/* 56:   */   
/* 57:   */   public ClientAnchor createClientAnchor()
/* 58:   */   {
/* 59:82 */     return this.helper.createClientAnchor();
/* 60:   */   }
/* 61:   */   
/* 62:   */   public AreaReference createAreaReference(String reference)
/* 63:   */   {
/* 64:90 */     return new AreaReference(reference, this.wb.getSpreadsheetVersion());
/* 65:   */   }
/* 66:   */   
/* 67:   */   public AreaReference createAreaReference(CellReference topLeft, CellReference bottomRight)
/* 68:   */   {
/* 69:98 */     return new AreaReference(topLeft, bottomRight, this.wb.getSpreadsheetVersion());
/* 70:   */   }
/* 71:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.streaming.SXSSFCreationHelper
 * JD-Core Version:    0.7.0.1
 */