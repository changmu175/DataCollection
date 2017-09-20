/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.Sheet;
/*   4:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDialogsheet;
/*   5:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDialogsheet.Factory;
/*   6:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter;
/*   7:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter.Factory;
/*   8:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak;
/*   9:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins;
/*  10:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins.Factory;
/*  11:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPrintOptions;
/*  12:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPrintOptions.Factory;
/*  13:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr;
/*  14:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr.Factory;
/*  15:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr;
/*  16:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr.Factory;
/*  17:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetProtection;
/*  18:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetProtection.Factory;
/*  19:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews;
/*  20:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews.Factory;
/*  21:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet.Factory;
/*  22:    */ 
/*  23:    */ public class XSSFDialogsheet
/*  24:    */   extends XSSFSheet
/*  25:    */   implements Sheet
/*  26:    */ {
/*  27:    */   protected CTDialogsheet dialogsheet;
/*  28:    */   
/*  29:    */   protected XSSFDialogsheet(XSSFSheet sheet)
/*  30:    */   {
/*  31: 37 */     super(sheet.getPackagePart());
/*  32: 38 */     this.dialogsheet = CTDialogsheet.Factory.newInstance();
/*  33: 39 */     this.worksheet = CTWorksheet.Factory.newInstance();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public XSSFRow createRow(int rowNum)
/*  37:    */   {
/*  38: 43 */     return null;
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected CTHeaderFooter getSheetTypeHeaderFooter()
/*  42:    */   {
/*  43: 47 */     if (this.dialogsheet.getHeaderFooter() == null) {
/*  44: 48 */       this.dialogsheet.setHeaderFooter(CTHeaderFooter.Factory.newInstance());
/*  45:    */     }
/*  46: 50 */     return this.dialogsheet.getHeaderFooter();
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected CTSheetPr getSheetTypeSheetPr()
/*  50:    */   {
/*  51: 54 */     if (this.dialogsheet.getSheetPr() == null) {
/*  52: 55 */       this.dialogsheet.setSheetPr(CTSheetPr.Factory.newInstance());
/*  53:    */     }
/*  54: 57 */     return this.dialogsheet.getSheetPr();
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected CTPageBreak getSheetTypeColumnBreaks()
/*  58:    */   {
/*  59: 61 */     return null;
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected CTSheetFormatPr getSheetTypeSheetFormatPr()
/*  63:    */   {
/*  64: 65 */     if (this.dialogsheet.getSheetFormatPr() == null) {
/*  65: 66 */       this.dialogsheet.setSheetFormatPr(CTSheetFormatPr.Factory.newInstance());
/*  66:    */     }
/*  67: 68 */     return this.dialogsheet.getSheetFormatPr();
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected CTPageMargins getSheetTypePageMargins()
/*  71:    */   {
/*  72: 72 */     if (this.dialogsheet.getPageMargins() == null) {
/*  73: 73 */       this.dialogsheet.setPageMargins(CTPageMargins.Factory.newInstance());
/*  74:    */     }
/*  75: 75 */     return this.dialogsheet.getPageMargins();
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected CTPageBreak getSheetTypeRowBreaks()
/*  79:    */   {
/*  80: 79 */     return null;
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected CTSheetViews getSheetTypeSheetViews()
/*  84:    */   {
/*  85: 83 */     if (this.dialogsheet.getSheetViews() == null)
/*  86:    */     {
/*  87: 84 */       this.dialogsheet.setSheetViews(CTSheetViews.Factory.newInstance());
/*  88: 85 */       this.dialogsheet.getSheetViews().addNewSheetView();
/*  89:    */     }
/*  90: 87 */     return this.dialogsheet.getSheetViews();
/*  91:    */   }
/*  92:    */   
/*  93:    */   protected CTPrintOptions getSheetTypePrintOptions()
/*  94:    */   {
/*  95: 91 */     if (this.dialogsheet.getPrintOptions() == null) {
/*  96: 92 */       this.dialogsheet.setPrintOptions(CTPrintOptions.Factory.newInstance());
/*  97:    */     }
/*  98: 94 */     return this.dialogsheet.getPrintOptions();
/*  99:    */   }
/* 100:    */   
/* 101:    */   protected CTSheetProtection getSheetTypeProtection()
/* 102:    */   {
/* 103: 98 */     if (this.dialogsheet.getSheetProtection() == null) {
/* 104: 99 */       this.dialogsheet.setSheetProtection(CTSheetProtection.Factory.newInstance());
/* 105:    */     }
/* 106:101 */     return this.dialogsheet.getSheetProtection();
/* 107:    */   }
/* 108:    */   
/* 109:    */   public boolean getDialog()
/* 110:    */   {
/* 111:105 */     return true;
/* 112:    */   }
/* 113:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFDialogsheet
 * JD-Core Version:    0.7.0.1
 */