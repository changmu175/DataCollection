/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.DataValidation;
/*   4:    */ import org.apache.poi.ss.usermodel.DataValidationConstraint;
/*   5:    */ import org.apache.poi.ss.usermodel.DataValidationHelper;
/*   6:    */ import org.apache.poi.ss.util.CellRangeAddressList;
/*   7:    */ 
/*   8:    */ public class HSSFDataValidationHelper
/*   9:    */   implements DataValidationHelper
/*  10:    */ {
/*  11:    */   public HSSFDataValidationHelper(HSSFSheet sheet) {}
/*  12:    */   
/*  13:    */   public DataValidationConstraint createDateConstraint(int operatorType, String formula1, String formula2, String dateFormat)
/*  14:    */   {
/*  15: 47 */     return DVConstraint.createDateConstraint(operatorType, formula1, formula2, dateFormat);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public DataValidationConstraint createExplicitListConstraint(String[] listOfValues)
/*  19:    */   {
/*  20: 58 */     return DVConstraint.createExplicitListConstraint(listOfValues);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public DataValidationConstraint createFormulaListConstraint(String listFormula)
/*  24:    */   {
/*  25: 69 */     return DVConstraint.createFormulaListConstraint(listFormula);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public DataValidationConstraint createNumericConstraint(int validationType, int operatorType, String formula1, String formula2)
/*  29:    */   {
/*  30: 75 */     return DVConstraint.createNumericConstraint(validationType, operatorType, formula1, formula2);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public DataValidationConstraint createIntegerConstraint(int operatorType, String formula1, String formula2)
/*  34:    */   {
/*  35: 79 */     return DVConstraint.createNumericConstraint(1, operatorType, formula1, formula2);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public DataValidationConstraint createDecimalConstraint(int operatorType, String formula1, String formula2)
/*  39:    */   {
/*  40: 90 */     return DVConstraint.createNumericConstraint(2, operatorType, formula1, formula2);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public DataValidationConstraint createTextLengthConstraint(int operatorType, String formula1, String formula2)
/*  44:    */   {
/*  45:101 */     return DVConstraint.createNumericConstraint(6, operatorType, formula1, formula2);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public DataValidationConstraint createTimeConstraint(int operatorType, String formula1, String formula2)
/*  49:    */   {
/*  50:112 */     return DVConstraint.createTimeConstraint(operatorType, formula1, formula2);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public DataValidationConstraint createCustomConstraint(String formula)
/*  54:    */   {
/*  55:118 */     return DVConstraint.createCustomFormulaConstraint(formula);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public DataValidation createValidation(DataValidationConstraint constraint, CellRangeAddressList cellRangeAddressList)
/*  59:    */   {
/*  60:130 */     return new HSSFDataValidation(cellRangeAddressList, constraint);
/*  61:    */   }
/*  62:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFDataValidationHelper
 * JD-Core Version:    0.7.0.1
 */