/*  1:   */ package org.apache.poi.hssf.model;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook;
/*  4:   */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*  5:   */ import org.apache.poi.ss.formula.FormulaParseException;
/*  6:   */ import org.apache.poi.ss.formula.FormulaParser;
/*  7:   */ import org.apache.poi.ss.formula.FormulaParsingWorkbook;
/*  8:   */ import org.apache.poi.ss.formula.FormulaRenderer;
/*  9:   */ import org.apache.poi.ss.formula.FormulaType;
/* 10:   */ import org.apache.poi.ss.formula.ptg.Ptg;
/* 11:   */ import org.apache.poi.util.Internal;
/* 12:   */ 
/* 13:   */ @Internal
/* 14:   */ public final class HSSFFormulaParser
/* 15:   */ {
/* 16:   */   private static FormulaParsingWorkbook createParsingWorkbook(HSSFWorkbook book)
/* 17:   */   {
/* 18:38 */     return HSSFEvaluationWorkbook.create(book);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public static Ptg[] parse(String formula, HSSFWorkbook workbook)
/* 22:   */     throws FormulaParseException
/* 23:   */   {
/* 24:53 */     return parse(formula, workbook, FormulaType.CELL);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public static Ptg[] parse(String formula, HSSFWorkbook workbook, FormulaType formulaType)
/* 28:   */     throws FormulaParseException
/* 29:   */   {
/* 30:64 */     return parse(formula, workbook, formulaType, -1);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public static Ptg[] parse(String formula, HSSFWorkbook workbook, FormulaType formulaType, int sheetIndex)
/* 34:   */     throws FormulaParseException
/* 35:   */   {
/* 36:79 */     return FormulaParser.parse(formula, createParsingWorkbook(workbook), formulaType, sheetIndex);
/* 37:   */   }
/* 38:   */   
/* 39:   */   public static String toFormulaString(HSSFWorkbook book, Ptg[] ptgs)
/* 40:   */   {
/* 41:90 */     return FormulaRenderer.toFormulaString(HSSFEvaluationWorkbook.create(book), ptgs);
/* 42:   */   }
/* 43:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.model.HSSFFormulaParser
 * JD-Core Version:    0.7.0.1
 */