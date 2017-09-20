/*  1:   */ package org.apache.poi.hssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.common.usermodel.HyperlinkType;
/*  4:   */ import org.apache.poi.hssf.record.common.ExtendedColor;
/*  5:   */ import org.apache.poi.ss.usermodel.CreationHelper;
/*  6:   */ import org.apache.poi.ss.util.AreaReference;
/*  7:   */ import org.apache.poi.ss.util.CellReference;
/*  8:   */ import org.apache.poi.util.Internal;
/*  9:   */ 
/* 10:   */ public class HSSFCreationHelper
/* 11:   */   implements CreationHelper
/* 12:   */ {
/* 13:   */   private final HSSFWorkbook workbook;
/* 14:   */   
/* 15:   */   @Internal(since="3.15 beta 3")
/* 16:   */   HSSFCreationHelper(HSSFWorkbook wb)
/* 17:   */   {
/* 18:37 */     this.workbook = wb;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public HSSFRichTextString createRichTextString(String text)
/* 22:   */   {
/* 23:42 */     return new HSSFRichTextString(text);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public HSSFDataFormat createDataFormat()
/* 27:   */   {
/* 28:47 */     return this.workbook.createDataFormat();
/* 29:   */   }
/* 30:   */   
/* 31:   */   public HSSFHyperlink createHyperlink(HyperlinkType type)
/* 32:   */   {
/* 33:52 */     return new HSSFHyperlink(type);
/* 34:   */   }
/* 35:   */   
/* 36:   */   public HSSFExtendedColor createExtendedColor()
/* 37:   */   {
/* 38:57 */     return new HSSFExtendedColor(new ExtendedColor());
/* 39:   */   }
/* 40:   */   
/* 41:   */   public HSSFFormulaEvaluator createFormulaEvaluator()
/* 42:   */   {
/* 43:67 */     return new HSSFFormulaEvaluator(this.workbook);
/* 44:   */   }
/* 45:   */   
/* 46:   */   public HSSFClientAnchor createClientAnchor()
/* 47:   */   {
/* 48:78 */     return new HSSFClientAnchor();
/* 49:   */   }
/* 50:   */   
/* 51:   */   public AreaReference createAreaReference(String reference)
/* 52:   */   {
/* 53:86 */     return new AreaReference(reference, this.workbook.getSpreadsheetVersion());
/* 54:   */   }
/* 55:   */   
/* 56:   */   public AreaReference createAreaReference(CellReference topLeft, CellReference bottomRight)
/* 57:   */   {
/* 58:94 */     return new AreaReference(topLeft, bottomRight, this.workbook.getSpreadsheetVersion());
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFCreationHelper
 * JD-Core Version:    0.7.0.1
 */