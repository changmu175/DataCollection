/*   1:    */ package org.apache.poi.xssf.usermodel.helpers;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.FormulaParser;
/*   4:    */ import org.apache.poi.ss.formula.FormulaRenderer;
/*   5:    */ import org.apache.poi.ss.formula.FormulaType;
/*   6:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   7:    */ import org.apache.poi.ss.formula.ptg.Pxg;
/*   8:    */ import org.apache.poi.ss.formula.ptg.Pxg3D;
/*   9:    */ import org.apache.poi.ss.usermodel.Cell;
/*  10:    */ import org.apache.poi.ss.usermodel.CellType;
/*  11:    */ import org.apache.poi.ss.usermodel.Row;
/*  12:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  13:    */ import org.apache.poi.xssf.usermodel.XSSFCell;
/*  14:    */ import org.apache.poi.xssf.usermodel.XSSFEvaluationWorkbook;
/*  15:    */ import org.apache.poi.xssf.usermodel.XSSFName;
/*  16:    */ import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/*  17:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCell;
/*  18:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellFormula;
/*  19:    */ 
/*  20:    */ public final class XSSFFormulaUtils
/*  21:    */ {
/*  22:    */   private final XSSFWorkbook _wb;
/*  23:    */   private final XSSFEvaluationWorkbook _fpwb;
/*  24:    */   
/*  25:    */   public XSSFFormulaUtils(XSSFWorkbook wb)
/*  26:    */   {
/*  27: 48 */     this._wb = wb;
/*  28: 49 */     this._fpwb = XSSFEvaluationWorkbook.create(this._wb);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void updateSheetName(int sheetIndex, String oldName, String newName)
/*  32:    */   {
/*  33: 68 */     for (XSSFName nm : this._wb.getAllNames()) {
/*  34: 69 */       if ((nm.getSheetIndex() == -1) || (nm.getSheetIndex() == sheetIndex)) {
/*  35: 70 */         updateName(nm, oldName, newName);
/*  36:    */       }
/*  37:    */     }
/*  38: 75 */     for (Sheet sh : this._wb) {
/*  39: 76 */       for (Row row : sh) {
/*  40: 77 */         for (Cell cell : row) {
/*  41: 78 */           if (cell.getCellTypeEnum() == CellType.FORMULA) {
/*  42: 79 */             updateFormula((XSSFCell)cell, oldName, newName);
/*  43:    */           }
/*  44:    */         }
/*  45:    */       }
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   private void updateFormula(XSSFCell cell, String oldName, String newName)
/*  50:    */   {
/*  51: 92 */     CTCellFormula f = cell.getCTCell().getF();
/*  52: 93 */     if (f != null)
/*  53:    */     {
/*  54: 94 */       String formula = f.getStringValue();
/*  55: 95 */       if ((formula != null) && (formula.length() > 0))
/*  56:    */       {
/*  57: 96 */         int sheetIndex = this._wb.getSheetIndex(cell.getSheet());
/*  58: 97 */         Ptg[] ptgs = FormulaParser.parse(formula, this._fpwb, FormulaType.CELL, sheetIndex, cell.getRowIndex());
/*  59: 98 */         for (Ptg ptg : ptgs) {
/*  60: 99 */           updatePtg(ptg, oldName, newName);
/*  61:    */         }
/*  62:101 */         String updatedFormula = FormulaRenderer.toFormulaString(this._fpwb, ptgs);
/*  63:102 */         if (!formula.equals(updatedFormula)) {
/*  64:102 */           f.setStringValue(updatedFormula);
/*  65:    */         }
/*  66:    */       }
/*  67:    */     }
/*  68:    */   }
/*  69:    */   
/*  70:    */   private void updateName(XSSFName name, String oldName, String newName)
/*  71:    */   {
/*  72:113 */     String formula = name.getRefersToFormula();
/*  73:114 */     if (formula != null)
/*  74:    */     {
/*  75:115 */       int sheetIndex = name.getSheetIndex();
/*  76:116 */       int rowIndex = -1;
/*  77:117 */       Ptg[] ptgs = FormulaParser.parse(formula, this._fpwb, FormulaType.NAMEDRANGE, sheetIndex, rowIndex);
/*  78:118 */       for (Ptg ptg : ptgs) {
/*  79:119 */         updatePtg(ptg, oldName, newName);
/*  80:    */       }
/*  81:121 */       String updatedFormula = FormulaRenderer.toFormulaString(this._fpwb, ptgs);
/*  82:122 */       if (!formula.equals(updatedFormula)) {
/*  83:122 */         name.setRefersToFormula(updatedFormula);
/*  84:    */       }
/*  85:    */     }
/*  86:    */   }
/*  87:    */   
/*  88:    */   private void updatePtg(Ptg ptg, String oldName, String newName)
/*  89:    */   {
/*  90:127 */     if ((ptg instanceof Pxg))
/*  91:    */     {
/*  92:128 */       Pxg pxg = (Pxg)ptg;
/*  93:129 */       if (pxg.getExternalWorkbookNumber() < 1)
/*  94:    */       {
/*  95:130 */         if ((pxg.getSheetName() != null) && (pxg.getSheetName().equals(oldName))) {
/*  96:132 */           pxg.setSheetName(newName);
/*  97:    */         }
/*  98:134 */         if ((pxg instanceof Pxg3D))
/*  99:    */         {
/* 100:135 */           Pxg3D pxg3D = (Pxg3D)pxg;
/* 101:136 */           if ((pxg3D.getLastSheetName() != null) && (pxg3D.getLastSheetName().equals(oldName))) {
/* 102:138 */             pxg3D.setLastSheetName(newName);
/* 103:    */           }
/* 104:    */         }
/* 105:    */       }
/* 106:    */     }
/* 107:    */   }
/* 108:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.helpers.XSSFFormulaUtils
 * JD-Core Version:    0.7.0.1
 */