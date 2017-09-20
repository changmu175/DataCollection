/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.common.usermodel.HyperlinkType;
/*   4:    */ import org.apache.poi.ss.usermodel.CreationHelper;
/*   5:    */ import org.apache.poi.ss.util.AreaReference;
/*   6:    */ import org.apache.poi.ss.util.CellReference;
/*   7:    */ import org.apache.poi.util.Internal;
/*   8:    */ import org.apache.poi.xssf.model.StylesTable;
/*   9:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor.Factory;
/*  10:    */ 
/*  11:    */ public class XSSFCreationHelper
/*  12:    */   implements CreationHelper
/*  13:    */ {
/*  14:    */   private final XSSFWorkbook workbook;
/*  15:    */   
/*  16:    */   @Internal
/*  17:    */   public XSSFCreationHelper(XSSFWorkbook wb)
/*  18:    */   {
/*  19: 37 */     this.workbook = wb;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public XSSFRichTextString createRichTextString(String text)
/*  23:    */   {
/*  24: 45 */     XSSFRichTextString rt = new XSSFRichTextString(text);
/*  25: 46 */     rt.setStylesTableReference(this.workbook.getStylesSource());
/*  26: 47 */     return rt;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public XSSFDataFormat createDataFormat()
/*  30:    */   {
/*  31: 52 */     return this.workbook.createDataFormat();
/*  32:    */   }
/*  33:    */   
/*  34:    */   public XSSFColor createExtendedColor()
/*  35:    */   {
/*  36: 57 */     return new XSSFColor(CTColor.Factory.newInstance(), this.workbook.getStylesSource().getIndexedColors());
/*  37:    */   }
/*  38:    */   
/*  39:    */   public XSSFHyperlink createHyperlink(HyperlinkType type)
/*  40:    */   {
/*  41: 67 */     return new XSSFHyperlink(type);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public XSSFFormulaEvaluator createFormulaEvaluator()
/*  45:    */   {
/*  46: 77 */     return new XSSFFormulaEvaluator(this.workbook);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public XSSFClientAnchor createClientAnchor()
/*  50:    */   {
/*  51: 89 */     return new XSSFClientAnchor();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public AreaReference createAreaReference(String reference)
/*  55:    */   {
/*  56: 97 */     return new AreaReference(reference, this.workbook.getSpreadsheetVersion());
/*  57:    */   }
/*  58:    */   
/*  59:    */   public AreaReference createAreaReference(CellReference topLeft, CellReference bottomRight)
/*  60:    */   {
/*  61:105 */     return new AreaReference(topLeft, bottomRight, this.workbook.getSpreadsheetVersion());
/*  62:    */   }
/*  63:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFCreationHelper
 * JD-Core Version:    0.7.0.1
 */