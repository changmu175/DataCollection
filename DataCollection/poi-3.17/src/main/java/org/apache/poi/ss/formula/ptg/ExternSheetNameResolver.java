/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalSheet;
/*  4:   */ import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalSheetRange;
/*  5:   */ import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
/*  6:   */ import org.apache.poi.ss.formula.SheetNameFormatter;
/*  7:   */ 
/*  8:   */ final class ExternSheetNameResolver
/*  9:   */ {
/* 10:   */   public static String prependSheetName(FormulaRenderingWorkbook book, int field_1_index_extern_sheet, String cellRefText)
/* 11:   */   {
/* 12:34 */     EvaluationWorkbook.ExternalSheet externalSheet = book.getExternalSheet(field_1_index_extern_sheet);
/* 13:   */     StringBuffer sb;
/* 14:36 */     if (externalSheet != null)
/* 15:   */     {
/* 16:37 */       String wbName = externalSheet.getWorkbookName();
/* 17:38 */       String sheetName = externalSheet.getSheetName();
/* 18:   */       StringBuffer sb;
/* 19:39 */       if (wbName != null)
/* 20:   */       {
/* 21:40 */         StringBuffer sb = new StringBuffer(wbName.length() + sheetName.length() + cellRefText.length() + 4);
/* 22:41 */         SheetNameFormatter.appendFormat(sb, wbName, sheetName);
/* 23:   */       }
/* 24:   */       else
/* 25:   */       {
/* 26:43 */         sb = new StringBuffer(sheetName.length() + cellRefText.length() + 4);
/* 27:44 */         SheetNameFormatter.appendFormat(sb, sheetName);
/* 28:   */       }
/* 29:46 */       if ((externalSheet instanceof EvaluationWorkbook.ExternalSheetRange))
/* 30:   */       {
/* 31:47 */         EvaluationWorkbook.ExternalSheetRange r = (EvaluationWorkbook.ExternalSheetRange)externalSheet;
/* 32:48 */         if (!r.getFirstSheetName().equals(r.getLastSheetName()))
/* 33:   */         {
/* 34:49 */           sb.append(':');
/* 35:50 */           SheetNameFormatter.appendFormat(sb, r.getLastSheetName());
/* 36:   */         }
/* 37:   */       }
/* 38:   */     }
/* 39:   */     else
/* 40:   */     {
/* 41:54 */       String firstSheetName = book.getSheetFirstNameByExternSheet(field_1_index_extern_sheet);
/* 42:55 */       String lastSheetName = book.getSheetLastNameByExternSheet(field_1_index_extern_sheet);
/* 43:56 */       sb = new StringBuffer(firstSheetName.length() + cellRefText.length() + 4);
/* 44:57 */       if (firstSheetName.length() < 1)
/* 45:   */       {
/* 46:59 */         sb.append("#REF");
/* 47:   */       }
/* 48:   */       else
/* 49:   */       {
/* 50:61 */         SheetNameFormatter.appendFormat(sb, firstSheetName);
/* 51:62 */         if (!firstSheetName.equals(lastSheetName))
/* 52:   */         {
/* 53:63 */           sb.append(':');
/* 54:64 */           sb.append(lastSheetName);
/* 55:   */         }
/* 56:   */       }
/* 57:   */     }
/* 58:68 */     sb.append('!');
/* 59:69 */     sb.append(cellRefText);
/* 60:70 */     return sb.toString();
/* 61:   */   }
/* 62:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.ExternSheetNameResolver
 * JD-Core Version:    0.7.0.1
 */