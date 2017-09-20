/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.DVRecord;
/*   4:    */ import org.apache.poi.ss.usermodel.DataValidation;
/*   5:    */ import org.apache.poi.ss.usermodel.DataValidationConstraint;
/*   6:    */ import org.apache.poi.ss.util.CellRangeAddressList;
/*   7:    */ 
/*   8:    */ public final class HSSFDataValidation
/*   9:    */   implements DataValidation
/*  10:    */ {
/*  11:    */   private String _prompt_title;
/*  12:    */   private String _prompt_text;
/*  13:    */   private String _error_title;
/*  14:    */   private String _error_text;
/*  15: 36 */   private int _errorStyle = 0;
/*  16: 37 */   private boolean _emptyCellAllowed = true;
/*  17: 38 */   private boolean _suppress_dropdown_arrow = false;
/*  18: 39 */   private boolean _showPromptBox = true;
/*  19: 40 */   private boolean _showErrorBox = true;
/*  20:    */   private CellRangeAddressList _regions;
/*  21:    */   private DVConstraint _constraint;
/*  22:    */   
/*  23:    */   public HSSFDataValidation(CellRangeAddressList regions, DataValidationConstraint constraint)
/*  24:    */   {
/*  25: 52 */     this._regions = regions;
/*  26:    */     
/*  27:    */ 
/*  28: 55 */     this._constraint = ((DVConstraint)constraint);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public DataValidationConstraint getValidationConstraint()
/*  32:    */   {
/*  33: 63 */     return this._constraint;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public DVConstraint getConstraint()
/*  37:    */   {
/*  38: 67 */     return this._constraint;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public CellRangeAddressList getRegions()
/*  42:    */   {
/*  43: 71 */     return this._regions;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setErrorStyle(int error_style)
/*  47:    */   {
/*  48: 79 */     this._errorStyle = error_style;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public int getErrorStyle()
/*  52:    */   {
/*  53: 86 */     return this._errorStyle;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setEmptyCellAllowed(boolean allowed)
/*  57:    */   {
/*  58: 93 */     this._emptyCellAllowed = allowed;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public boolean getEmptyCellAllowed()
/*  62:    */   {
/*  63:100 */     return this._emptyCellAllowed;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void setSuppressDropDownArrow(boolean suppress)
/*  67:    */   {
/*  68:107 */     this._suppress_dropdown_arrow = suppress;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public boolean getSuppressDropDownArrow()
/*  72:    */   {
/*  73:115 */     if (this._constraint.getValidationType() == 3) {
/*  74:116 */       return this._suppress_dropdown_arrow;
/*  75:    */     }
/*  76:118 */     return false;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setShowPromptBox(boolean show)
/*  80:    */   {
/*  81:125 */     this._showPromptBox = show;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public boolean getShowPromptBox()
/*  85:    */   {
/*  86:132 */     return this._showPromptBox;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setShowErrorBox(boolean show)
/*  90:    */   {
/*  91:139 */     this._showErrorBox = show;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public boolean getShowErrorBox()
/*  95:    */   {
/*  96:146 */     return this._showErrorBox;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void createPromptBox(String title, String text)
/* 100:    */   {
/* 101:155 */     if ((title != null) && (title.length() > 32)) {
/* 102:156 */       throw new IllegalStateException("Prompt-title cannot be longer than 32 characters, but had: " + title);
/* 103:    */     }
/* 104:158 */     if ((text != null) && (text.length() > 255)) {
/* 105:159 */       throw new IllegalStateException("Prompt-text cannot be longer than 255 characters, but had: " + text);
/* 106:    */     }
/* 107:161 */     this._prompt_title = title;
/* 108:162 */     this._prompt_text = text;
/* 109:163 */     setShowPromptBox(true);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public String getPromptBoxTitle()
/* 113:    */   {
/* 114:170 */     return this._prompt_title;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public String getPromptBoxText()
/* 118:    */   {
/* 119:177 */     return this._prompt_text;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void createErrorBox(String title, String text)
/* 123:    */   {
/* 124:184 */     if ((title != null) && (title.length() > 32)) {
/* 125:185 */       throw new IllegalStateException("Error-title cannot be longer than 32 characters, but had: " + title);
/* 126:    */     }
/* 127:187 */     if ((text != null) && (text.length() > 255)) {
/* 128:188 */       throw new IllegalStateException("Error-text cannot be longer than 255 characters, but had: " + text);
/* 129:    */     }
/* 130:190 */     this._error_title = title;
/* 131:191 */     this._error_text = text;
/* 132:192 */     setShowErrorBox(true);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public String getErrorBoxTitle()
/* 136:    */   {
/* 137:199 */     return this._error_title;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public String getErrorBoxText()
/* 141:    */   {
/* 142:206 */     return this._error_text;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public DVRecord createDVRecord(HSSFSheet sheet)
/* 146:    */   {
/* 147:211 */     DVConstraint.FormulaPair fp = this._constraint.createFormulas(sheet);
/* 148:    */     
/* 149:213 */     return new DVRecord(this._constraint.getValidationType(), this._constraint.getOperator(), this._errorStyle, this._emptyCellAllowed, getSuppressDropDownArrow(), (this._constraint.getValidationType() == 3) && (this._constraint.getExplicitListValues() != null), this._showPromptBox, this._prompt_title, this._prompt_text, this._showErrorBox, this._error_title, this._error_text, fp.getFormula1(), fp.getFormula2(), this._regions);
/* 150:    */   }
/* 151:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFDataValidation
 * JD-Core Version:    0.7.0.1
 */