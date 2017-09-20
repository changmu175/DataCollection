/*   1:    */ package org.apache.poi.hssf.record.common;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.util.BitField;
/*   5:    */ import org.apache.poi.util.BitFieldFactory;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ 
/*   8:    */ public final class FeatFormulaErr2
/*   9:    */   implements SharedFeature
/*  10:    */ {
/*  11: 39 */   private static final BitField CHECK_CALCULATION_ERRORS = BitFieldFactory.getInstance(1);
/*  12: 40 */   private static final BitField CHECK_EMPTY_CELL_REF = BitFieldFactory.getInstance(2);
/*  13: 41 */   private static final BitField CHECK_NUMBERS_AS_TEXT = BitFieldFactory.getInstance(4);
/*  14: 42 */   private static final BitField CHECK_INCONSISTENT_RANGES = BitFieldFactory.getInstance(8);
/*  15: 43 */   private static final BitField CHECK_INCONSISTENT_FORMULAS = BitFieldFactory.getInstance(16);
/*  16: 44 */   private static final BitField CHECK_DATETIME_FORMATS = BitFieldFactory.getInstance(32);
/*  17: 45 */   private static final BitField CHECK_UNPROTECTED_FORMULAS = BitFieldFactory.getInstance(64);
/*  18: 46 */   private static final BitField PERFORM_DATA_VALIDATION = BitFieldFactory.getInstance(128);
/*  19:    */   private int errorCheck;
/*  20:    */   
/*  21:    */   public FeatFormulaErr2() {}
/*  22:    */   
/*  23:    */   public FeatFormulaErr2(RecordInputStream in)
/*  24:    */   {
/*  25: 57 */     this.errorCheck = in.readInt();
/*  26:    */   }
/*  27:    */   
/*  28:    */   public String toString()
/*  29:    */   {
/*  30: 61 */     StringBuffer buffer = new StringBuffer();
/*  31: 62 */     buffer.append(" [FEATURE FORMULA ERRORS]\n");
/*  32: 63 */     buffer.append("  checkCalculationErrors    = ");
/*  33: 64 */     buffer.append("  checkEmptyCellRef         = ");
/*  34: 65 */     buffer.append("  checkNumbersAsText        = ");
/*  35: 66 */     buffer.append("  checkInconsistentRanges   = ");
/*  36: 67 */     buffer.append("  checkInconsistentFormulas = ");
/*  37: 68 */     buffer.append("  checkDateTimeFormats      = ");
/*  38: 69 */     buffer.append("  checkUnprotectedFormulas  = ");
/*  39: 70 */     buffer.append("  performDataValidation     = ");
/*  40: 71 */     buffer.append(" [/FEATURE FORMULA ERRORS]\n");
/*  41: 72 */     return buffer.toString();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void serialize(LittleEndianOutput out)
/*  45:    */   {
/*  46: 76 */     out.writeInt(this.errorCheck);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public int getDataSize()
/*  50:    */   {
/*  51: 80 */     return 4;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public int _getRawErrorCheckValue()
/*  55:    */   {
/*  56: 84 */     return this.errorCheck;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean getCheckCalculationErrors()
/*  60:    */   {
/*  61: 88 */     return CHECK_CALCULATION_ERRORS.isSet(this.errorCheck);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setCheckCalculationErrors(boolean checkCalculationErrors)
/*  65:    */   {
/*  66: 91 */     this.errorCheck = CHECK_CALCULATION_ERRORS.setBoolean(this.errorCheck, checkCalculationErrors);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public boolean getCheckEmptyCellRef()
/*  70:    */   {
/*  71: 95 */     return CHECK_EMPTY_CELL_REF.isSet(this.errorCheck);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setCheckEmptyCellRef(boolean checkEmptyCellRef)
/*  75:    */   {
/*  76: 98 */     this.errorCheck = CHECK_EMPTY_CELL_REF.setBoolean(this.errorCheck, checkEmptyCellRef);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public boolean getCheckNumbersAsText()
/*  80:    */   {
/*  81:102 */     return CHECK_NUMBERS_AS_TEXT.isSet(this.errorCheck);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void setCheckNumbersAsText(boolean checkNumbersAsText)
/*  85:    */   {
/*  86:105 */     this.errorCheck = CHECK_NUMBERS_AS_TEXT.setBoolean(this.errorCheck, checkNumbersAsText);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public boolean getCheckInconsistentRanges()
/*  90:    */   {
/*  91:109 */     return CHECK_INCONSISTENT_RANGES.isSet(this.errorCheck);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void setCheckInconsistentRanges(boolean checkInconsistentRanges)
/*  95:    */   {
/*  96:112 */     this.errorCheck = CHECK_INCONSISTENT_RANGES.setBoolean(this.errorCheck, checkInconsistentRanges);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public boolean getCheckInconsistentFormulas()
/* 100:    */   {
/* 101:116 */     return CHECK_INCONSISTENT_FORMULAS.isSet(this.errorCheck);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setCheckInconsistentFormulas(boolean checkInconsistentFormulas)
/* 105:    */   {
/* 106:119 */     this.errorCheck = CHECK_INCONSISTENT_FORMULAS.setBoolean(this.errorCheck, checkInconsistentFormulas);
/* 107:    */   }
/* 108:    */   
/* 109:    */   public boolean getCheckDateTimeFormats()
/* 110:    */   {
/* 111:123 */     return CHECK_DATETIME_FORMATS.isSet(this.errorCheck);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void setCheckDateTimeFormats(boolean checkDateTimeFormats)
/* 115:    */   {
/* 116:126 */     this.errorCheck = CHECK_DATETIME_FORMATS.setBoolean(this.errorCheck, checkDateTimeFormats);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public boolean getCheckUnprotectedFormulas()
/* 120:    */   {
/* 121:130 */     return CHECK_UNPROTECTED_FORMULAS.isSet(this.errorCheck);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void setCheckUnprotectedFormulas(boolean checkUnprotectedFormulas)
/* 125:    */   {
/* 126:133 */     this.errorCheck = CHECK_UNPROTECTED_FORMULAS.setBoolean(this.errorCheck, checkUnprotectedFormulas);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public boolean getPerformDataValidation()
/* 130:    */   {
/* 131:137 */     return PERFORM_DATA_VALIDATION.isSet(this.errorCheck);
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void setPerformDataValidation(boolean performDataValidation)
/* 135:    */   {
/* 136:140 */     this.errorCheck = PERFORM_DATA_VALIDATION.setBoolean(this.errorCheck, performDataValidation);
/* 137:    */   }
/* 138:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.common.FeatFormulaErr2
 * JD-Core Version:    0.7.0.1
 */