/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import java.util.Map;
/*   6:    */ import org.apache.poi.ss.usermodel.DataValidation;
/*   7:    */ import org.apache.poi.ss.usermodel.DataValidationConstraint;
/*   8:    */ import org.apache.poi.ss.usermodel.DataValidationHelper;
/*   9:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*  10:    */ import org.apache.poi.ss.util.CellRangeAddressList;
/*  11:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidation;
/*  12:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidation.Factory;
/*  13:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationErrorStyle;
/*  14:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator.Enum;
/*  15:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType;
/*  16:    */ 
/*  17:    */ public class XSSFDataValidationHelper
/*  18:    */   implements DataValidationHelper
/*  19:    */ {
/*  20:    */   public XSSFDataValidationHelper(XSSFSheet xssfSheet) {}
/*  21:    */   
/*  22:    */   public DataValidationConstraint createDateConstraint(int operatorType, String formula1, String formula2, String dateFormat)
/*  23:    */   {
/*  24: 52 */     return new XSSFDataValidationConstraint(4, operatorType, formula1, formula2);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public DataValidationConstraint createDecimalConstraint(int operatorType, String formula1, String formula2)
/*  28:    */   {
/*  29: 59 */     return new XSSFDataValidationConstraint(2, operatorType, formula1, formula2);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public DataValidationConstraint createExplicitListConstraint(String[] listOfValues)
/*  33:    */   {
/*  34: 66 */     return new XSSFDataValidationConstraint(listOfValues);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public DataValidationConstraint createFormulaListConstraint(String listFormula)
/*  38:    */   {
/*  39: 73 */     return new XSSFDataValidationConstraint(3, listFormula);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public DataValidationConstraint createNumericConstraint(int validationType, int operatorType, String formula1, String formula2)
/*  43:    */   {
/*  44: 79 */     if (validationType == 1) {
/*  45: 80 */       return createIntegerConstraint(operatorType, formula1, formula2);
/*  46:    */     }
/*  47: 81 */     if (validationType == 2) {
/*  48: 82 */       return createDecimalConstraint(operatorType, formula1, formula2);
/*  49:    */     }
/*  50: 83 */     if (validationType == 6) {
/*  51: 84 */       return createTextLengthConstraint(operatorType, formula1, formula2);
/*  52:    */     }
/*  53: 86 */     return null;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public DataValidationConstraint createIntegerConstraint(int operatorType, String formula1, String formula2)
/*  57:    */   {
/*  58: 93 */     return new XSSFDataValidationConstraint(1, operatorType, formula1, formula2);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public DataValidationConstraint createTextLengthConstraint(int operatorType, String formula1, String formula2)
/*  62:    */   {
/*  63:100 */     return new XSSFDataValidationConstraint(6, operatorType, formula1, formula2);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public DataValidationConstraint createTimeConstraint(int operatorType, String formula1, String formula2)
/*  67:    */   {
/*  68:107 */     return new XSSFDataValidationConstraint(5, operatorType, formula1, formula2);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public DataValidationConstraint createCustomConstraint(String formula)
/*  72:    */   {
/*  73:111 */     return new XSSFDataValidationConstraint(7, formula);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public DataValidation createValidation(DataValidationConstraint constraint, CellRangeAddressList cellRangeAddressList)
/*  77:    */   {
/*  78:118 */     XSSFDataValidationConstraint dataValidationConstraint = (XSSFDataValidationConstraint)constraint;
/*  79:119 */     CTDataValidation newDataValidation = CTDataValidation.Factory.newInstance();
/*  80:    */     
/*  81:121 */     int validationType = constraint.getValidationType();
/*  82:122 */     switch (validationType)
/*  83:    */     {
/*  84:    */     case 3: 
/*  85:124 */       newDataValidation.setType(STDataValidationType.LIST);
/*  86:125 */       newDataValidation.setFormula1(constraint.getFormula1());
/*  87:126 */       break;
/*  88:    */     case 0: 
/*  89:128 */       newDataValidation.setType(STDataValidationType.NONE);
/*  90:129 */       break;
/*  91:    */     case 6: 
/*  92:131 */       newDataValidation.setType(STDataValidationType.TEXT_LENGTH);
/*  93:132 */       break;
/*  94:    */     case 4: 
/*  95:134 */       newDataValidation.setType(STDataValidationType.DATE);
/*  96:135 */       break;
/*  97:    */     case 1: 
/*  98:137 */       newDataValidation.setType(STDataValidationType.WHOLE);
/*  99:138 */       break;
/* 100:    */     case 2: 
/* 101:140 */       newDataValidation.setType(STDataValidationType.DECIMAL);
/* 102:141 */       break;
/* 103:    */     case 5: 
/* 104:143 */       newDataValidation.setType(STDataValidationType.TIME);
/* 105:144 */       break;
/* 106:    */     case 7: 
/* 107:146 */       newDataValidation.setType(STDataValidationType.CUSTOM);
/* 108:147 */       break;
/* 109:    */     default: 
/* 110:149 */       newDataValidation.setType(STDataValidationType.NONE);
/* 111:    */     }
/* 112:152 */     if ((validationType != 0) && (validationType != 3))
/* 113:    */     {
/* 114:153 */       STDataValidationOperator.Enum op = (STDataValidationOperator.Enum)XSSFDataValidation.operatorTypeMappings.get(Integer.valueOf(constraint.getOperator()));
/* 115:154 */       if (op != null) {
/* 116:155 */         newDataValidation.setOperator(op);
/* 117:    */       }
/* 118:157 */       if (constraint.getFormula1() != null) {
/* 119:158 */         newDataValidation.setFormula1(constraint.getFormula1());
/* 120:    */       }
/* 121:160 */       if (constraint.getFormula2() != null) {
/* 122:161 */         newDataValidation.setFormula2(constraint.getFormula2());
/* 123:    */       }
/* 124:    */     }
/* 125:165 */     CellRangeAddress[] cellRangeAddresses = cellRangeAddressList.getCellRangeAddresses();
/* 126:166 */     List<String> sqref = new ArrayList();
/* 127:167 */     for (int i = 0; i < cellRangeAddresses.length; i++)
/* 128:    */     {
/* 129:168 */       CellRangeAddress cellRangeAddress = cellRangeAddresses[i];
/* 130:169 */       sqref.add(cellRangeAddress.formatAsString());
/* 131:    */     }
/* 132:171 */     newDataValidation.setSqref(sqref);
/* 133:172 */     newDataValidation.setAllowBlank(true);
/* 134:173 */     newDataValidation.setErrorStyle(STDataValidationErrorStyle.STOP);
/* 135:    */     
/* 136:175 */     return new XSSFDataValidation(dataValidationConstraint, cellRangeAddressList, newDataValidation);
/* 137:    */   }
/* 138:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFDataValidationHelper
 * JD-Core Version:    0.7.0.1
 */