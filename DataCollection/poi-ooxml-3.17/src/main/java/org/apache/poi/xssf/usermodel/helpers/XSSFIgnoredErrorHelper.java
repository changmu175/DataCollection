/*   1:    */ package org.apache.poi.xssf.usermodel.helpers;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.LinkedHashSet;
/*   5:    */ import java.util.Set;
/*   6:    */ import org.apache.poi.ss.usermodel.IgnoredErrorType;
/*   7:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError;
/*   8:    */ 
/*   9:    */ public class XSSFIgnoredErrorHelper
/*  10:    */ {
/*  11:    */   public static boolean isSet(IgnoredErrorType errorType, CTIgnoredError error)
/*  12:    */   {
/*  13: 32 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$IgnoredErrorType[errorType.ordinal()])
/*  14:    */     {
/*  15:    */     case 1: 
/*  16: 34 */       return error.isSetCalculatedColumn();
/*  17:    */     case 2: 
/*  18: 36 */       return error.isSetEmptyCellReference();
/*  19:    */     case 3: 
/*  20: 38 */       return error.isSetEvalError();
/*  21:    */     case 4: 
/*  22: 40 */       return error.isSetFormula();
/*  23:    */     case 5: 
/*  24: 42 */       return error.isSetFormulaRange();
/*  25:    */     case 6: 
/*  26: 44 */       return error.isSetListDataValidation();
/*  27:    */     case 7: 
/*  28: 46 */       return error.isSetNumberStoredAsText();
/*  29:    */     case 8: 
/*  30: 48 */       return error.isSetTwoDigitTextYear();
/*  31:    */     case 9: 
/*  32: 50 */       return error.isSetUnlockedFormula();
/*  33:    */     }
/*  34: 52 */     throw new IllegalStateException();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public static void set(IgnoredErrorType errorType, CTIgnoredError error)
/*  38:    */   {
/*  39: 57 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$IgnoredErrorType[errorType.ordinal()])
/*  40:    */     {
/*  41:    */     case 1: 
/*  42: 59 */       error.setCalculatedColumn(true);
/*  43: 60 */       break;
/*  44:    */     case 2: 
/*  45: 62 */       error.setEmptyCellReference(true);
/*  46: 63 */       break;
/*  47:    */     case 3: 
/*  48: 65 */       error.setEvalError(true);
/*  49: 66 */       break;
/*  50:    */     case 4: 
/*  51: 68 */       error.setFormula(true);
/*  52: 69 */       break;
/*  53:    */     case 5: 
/*  54: 71 */       error.setFormulaRange(true);
/*  55: 72 */       break;
/*  56:    */     case 6: 
/*  57: 74 */       error.setListDataValidation(true);
/*  58: 75 */       break;
/*  59:    */     case 7: 
/*  60: 77 */       error.setNumberStoredAsText(true);
/*  61: 78 */       break;
/*  62:    */     case 8: 
/*  63: 80 */       error.setTwoDigitTextYear(true);
/*  64: 81 */       break;
/*  65:    */     case 9: 
/*  66: 83 */       error.setUnlockedFormula(true);
/*  67: 84 */       break;
/*  68:    */     default: 
/*  69: 86 */       throw new IllegalStateException();
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   public static void addIgnoredErrors(CTIgnoredError err, String ref, IgnoredErrorType... ignoredErrorTypes)
/*  74:    */   {
/*  75: 91 */     err.setSqref(Arrays.asList(new String[] { ref }));
/*  76: 92 */     for (IgnoredErrorType errType : ignoredErrorTypes) {
/*  77: 93 */       set(errType, err);
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static Set<IgnoredErrorType> getErrorTypes(CTIgnoredError err)
/*  82:    */   {
/*  83: 98 */     Set<IgnoredErrorType> result = new LinkedHashSet();
/*  84: 99 */     for (IgnoredErrorType errType : IgnoredErrorType.values()) {
/*  85:100 */       if (isSet(errType, err)) {
/*  86:101 */         result.add(errType);
/*  87:    */       }
/*  88:    */     }
/*  89:104 */     return result;
/*  90:    */   }
/*  91:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.helpers.XSSFIgnoredErrorHelper
 * JD-Core Version:    0.7.0.1
 */