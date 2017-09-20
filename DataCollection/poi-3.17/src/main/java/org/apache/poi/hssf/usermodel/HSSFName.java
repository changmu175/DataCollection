/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.model.HSSFFormulaParser;
/*   4:    */ import org.apache.poi.hssf.model.InternalWorkbook;
/*   5:    */ import org.apache.poi.hssf.record.NameCommentRecord;
/*   6:    */ import org.apache.poi.hssf.record.NameRecord;
/*   7:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   8:    */ import org.apache.poi.ss.formula.FormulaType;
/*   9:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  10:    */ import org.apache.poi.ss.usermodel.Name;
/*  11:    */ import org.apache.poi.ss.util.CellReference;
/*  12:    */ 
/*  13:    */ public final class HSSFName
/*  14:    */   implements Name
/*  15:    */ {
/*  16:    */   private HSSFWorkbook _book;
/*  17:    */   private NameRecord _definedNameRec;
/*  18:    */   private NameCommentRecord _commentRec;
/*  19:    */   
/*  20:    */   HSSFName(HSSFWorkbook book, NameRecord name)
/*  21:    */   {
/*  22: 49 */     this(book, name, null);
/*  23:    */   }
/*  24:    */   
/*  25:    */   HSSFName(HSSFWorkbook book, NameRecord name, NameCommentRecord comment)
/*  26:    */   {
/*  27: 61 */     this._book = book;
/*  28: 62 */     this._definedNameRec = name;
/*  29: 63 */     this._commentRec = comment;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String getSheetName()
/*  33:    */   {
/*  34: 70 */     int indexToExternSheet = this._definedNameRec.getExternSheetNumber();
/*  35:    */     
/*  36: 72 */     return this._book.getWorkbook().findSheetFirstNameFromExternSheet(indexToExternSheet);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getNameName()
/*  40:    */   {
/*  41: 79 */     return this._definedNameRec.getNameText();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setNameName(String nameName)
/*  45:    */   {
/*  46:132 */     validateName(nameName);
/*  47:    */     
/*  48:134 */     InternalWorkbook wb = this._book.getWorkbook();
/*  49:135 */     this._definedNameRec.setNameText(nameName);
/*  50:    */     
/*  51:137 */     int sheetNumber = this._definedNameRec.getSheetNumber();
/*  52:    */     
/*  53:    */ 
/*  54:140 */     int lastNameIndex = wb.getNumNames() - 1;
/*  55:141 */     for (int i = lastNameIndex; i >= 0; i--)
/*  56:    */     {
/*  57:143 */       NameRecord rec = wb.getNameRecord(i);
/*  58:144 */       if ((rec != this._definedNameRec) && 
/*  59:145 */         (rec.getNameText().equalsIgnoreCase(nameName)) && (sheetNumber == rec.getSheetNumber()))
/*  60:    */       {
/*  61:146 */         String msg = "The " + (sheetNumber == 0 ? "workbook" : "sheet") + " already contains this name: " + nameName;
/*  62:147 */         this._definedNameRec.setNameText(nameName + "(2)");
/*  63:148 */         throw new IllegalArgumentException(msg);
/*  64:    */       }
/*  65:    */     }
/*  66:154 */     if (this._commentRec != null)
/*  67:    */     {
/*  68:155 */       this._commentRec.setNameText(nameName);
/*  69:156 */       this._book.getWorkbook().updateNameCommentRecordCache(this._commentRec);
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   private static void validateName(String name)
/*  74:    */   {
/*  75:183 */     if (name.length() == 0) {
/*  76:184 */       throw new IllegalArgumentException("Name cannot be blank");
/*  77:    */     }
/*  78:186 */     if (name.length() > 255) {
/*  79:187 */       throw new IllegalArgumentException("Invalid name: '" + name + "': cannot exceed 255 characters in length");
/*  80:    */     }
/*  81:189 */     if ((name.equalsIgnoreCase("R")) || (name.equalsIgnoreCase("C"))) {
/*  82:190 */       throw new IllegalArgumentException("Invalid name: '" + name + "': cannot be special shorthand R or C");
/*  83:    */     }
/*  84:194 */     char c = name.charAt(0);
/*  85:195 */     String allowedSymbols = "_\\";
/*  86:196 */     boolean characterIsValid = (Character.isLetter(c)) || (allowedSymbols.indexOf(c) != -1);
/*  87:197 */     if (!characterIsValid) {
/*  88:198 */       throw new IllegalArgumentException("Invalid name: '" + name + "': first character must be underscore or a letter");
/*  89:    */     }
/*  90:202 */     allowedSymbols = "_.\\";
/*  91:203 */     for (char ch : name.toCharArray())
/*  92:    */     {
/*  93:204 */       characterIsValid = (Character.isLetterOrDigit(ch)) || (allowedSymbols.indexOf(ch) != -1);
/*  94:205 */       if (!characterIsValid) {
/*  95:206 */         throw new IllegalArgumentException("Invalid name: '" + name + "': name must be letter, digit, period, or underscore");
/*  96:    */       }
/*  97:    */     }
/*  98:212 */     if (name.matches("[A-Za-z]+\\d+"))
/*  99:    */     {
/* 100:213 */       String col = name.replaceAll("\\d", "");
/* 101:214 */       String row = name.replaceAll("[A-Za-z]", "");
/* 102:215 */       if (CellReference.cellReferenceIsWithinRange(col, row, SpreadsheetVersion.EXCEL97)) {
/* 103:216 */         throw new IllegalArgumentException("Invalid name: '" + name + "': cannot be $A$1-style cell reference");
/* 104:    */       }
/* 105:    */     }
/* 106:221 */     if (name.matches("[Rr]\\d+[Cc]\\d+")) {
/* 107:222 */       throw new IllegalArgumentException("Invalid name: '" + name + "': cannot be R1C1-style cell reference");
/* 108:    */     }
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void setRefersToFormula(String formulaText)
/* 112:    */   {
/* 113:227 */     Ptg[] ptgs = HSSFFormulaParser.parse(formulaText, this._book, FormulaType.NAMEDRANGE, getSheetIndex());
/* 114:228 */     this._definedNameRec.setNameDefinition(ptgs);
/* 115:    */   }
/* 116:    */   
/* 117:    */   public String getRefersToFormula()
/* 118:    */   {
/* 119:232 */     if (this._definedNameRec.isFunctionName()) {
/* 120:233 */       throw new IllegalStateException("Only applicable to named ranges");
/* 121:    */     }
/* 122:235 */     Ptg[] ptgs = this._definedNameRec.getNameDefinition();
/* 123:236 */     if (ptgs.length < 1) {
/* 124:238 */       return null;
/* 125:    */     }
/* 126:240 */     return HSSFFormulaParser.toFormulaString(this._book, ptgs);
/* 127:    */   }
/* 128:    */   
/* 129:    */   void setNameDefinition(Ptg[] ptgs)
/* 130:    */   {
/* 131:251 */     this._definedNameRec.setNameDefinition(ptgs);
/* 132:    */   }
/* 133:    */   
/* 134:    */   public boolean isDeleted()
/* 135:    */   {
/* 136:256 */     Ptg[] ptgs = this._definedNameRec.getNameDefinition();
/* 137:257 */     return Ptg.doesFormulaReferToDeletedCell(ptgs);
/* 138:    */   }
/* 139:    */   
/* 140:    */   public boolean isFunctionName()
/* 141:    */   {
/* 142:266 */     return this._definedNameRec.isFunctionName();
/* 143:    */   }
/* 144:    */   
/* 145:    */   public String toString()
/* 146:    */   {
/* 147:270 */     StringBuffer sb = new StringBuffer(64);
/* 148:271 */     sb.append(getClass().getName()).append(" [");
/* 149:272 */     sb.append(this._definedNameRec.getNameText());
/* 150:273 */     sb.append("]");
/* 151:274 */     return sb.toString();
/* 152:    */   }
/* 153:    */   
/* 154:    */   public void setSheetIndex(int index)
/* 155:    */   {
/* 156:285 */     int lastSheetIx = this._book.getNumberOfSheets() - 1;
/* 157:286 */     if ((index < -1) || (index > lastSheetIx)) {
/* 158:287 */       throw new IllegalArgumentException("Sheet index (" + index + ") is out of range" + (lastSheetIx == -1 ? "" : new StringBuilder().append(" (0..").append(lastSheetIx).append(")").toString()));
/* 159:    */     }
/* 160:291 */     this._definedNameRec.setSheetNumber(index + 1);
/* 161:    */   }
/* 162:    */   
/* 163:    */   public int getSheetIndex()
/* 164:    */   {
/* 165:300 */     return this._definedNameRec.getSheetNumber() - 1;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public String getComment()
/* 169:    */   {
/* 170:309 */     if (this._commentRec != null) {
/* 171:311 */       if ((this._commentRec.getCommentText() != null) && (this._commentRec.getCommentText().length() > 0)) {
/* 172:313 */         return this._commentRec.getCommentText();
/* 173:    */       }
/* 174:    */     }
/* 175:316 */     return this._definedNameRec.getDescriptionText();
/* 176:    */   }
/* 177:    */   
/* 178:    */   public void setComment(String comment)
/* 179:    */   {
/* 180:326 */     this._definedNameRec.setDescriptionText(comment);
/* 181:328 */     if (this._commentRec != null) {
/* 182:329 */       this._commentRec.setCommentText(comment);
/* 183:    */     }
/* 184:    */   }
/* 185:    */   
/* 186:    */   public void setFunction(boolean value)
/* 187:    */   {
/* 188:340 */     this._definedNameRec.setFunction(value);
/* 189:    */   }
/* 190:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFName
 * JD-Core Version:    0.7.0.1
 */