/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   4:    */ import org.apache.poi.ss.formula.FormulaParser;
/*   5:    */ import org.apache.poi.ss.formula.FormulaType;
/*   6:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   7:    */ import org.apache.poi.ss.usermodel.Name;
/*   8:    */ import org.apache.poi.ss.util.AreaReference;
/*   9:    */ import org.apache.poi.ss.util.CellReference;
/*  10:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedName;
/*  11:    */ 
/*  12:    */ public final class XSSFName
/*  13:    */   implements Name
/*  14:    */ {
/*  15:    */   public static final String BUILTIN_PRINT_AREA = "_xlnm.Print_Area";
/*  16:    */   public static final String BUILTIN_PRINT_TITLE = "_xlnm.Print_Titles";
/*  17:    */   public static final String BUILTIN_CRITERIA = "_xlnm.Criteria:";
/*  18:    */   public static final String BUILTIN_EXTRACT = "_xlnm.Extract:";
/*  19:    */   public static final String BUILTIN_FILTER_DB = "_xlnm._FilterDatabase";
/*  20:    */   public static final String BUILTIN_CONSOLIDATE_AREA = "_xlnm.Consolidate_Area";
/*  21:    */   public static final String BUILTIN_DATABASE = "_xlnm.Database";
/*  22:    */   public static final String BUILTIN_SHEET_TITLE = "_xlnm.Sheet_Title";
/*  23:    */   private XSSFWorkbook _workbook;
/*  24:    */   private CTDefinedName _ctName;
/*  25:    */   
/*  26:    */   protected XSSFName(CTDefinedName name, XSSFWorkbook workbook)
/*  27:    */   {
/*  28:119 */     this._workbook = workbook;
/*  29:120 */     this._ctName = name;
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected CTDefinedName getCTName()
/*  33:    */   {
/*  34:127 */     return this._ctName;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public String getNameName()
/*  38:    */   {
/*  39:136 */     return this._ctName.getName();
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setNameName(String name)
/*  43:    */   {
/*  44:170 */     validateName(name);
/*  45:    */     
/*  46:172 */     String oldName = getNameName();
/*  47:173 */     int sheetIndex = getSheetIndex();
/*  48:175 */     for (XSSFName foundName : this._workbook.getNames(name)) {
/*  49:176 */       if ((foundName.getSheetIndex() == sheetIndex) && (foundName != this))
/*  50:    */       {
/*  51:177 */         String msg = "The " + (sheetIndex == -1 ? "workbook" : "sheet") + " already contains this name: " + name;
/*  52:178 */         throw new IllegalArgumentException(msg);
/*  53:    */       }
/*  54:    */     }
/*  55:181 */     this._ctName.setName(name);
/*  56:    */     
/*  57:183 */     this._workbook.updateName(this, oldName);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public String getRefersToFormula()
/*  61:    */   {
/*  62:187 */     String result = this._ctName.getStringValue();
/*  63:188 */     if ((result == null) || (result.length() < 1)) {
/*  64:189 */       return null;
/*  65:    */     }
/*  66:191 */     return result;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setRefersToFormula(String formulaText)
/*  70:    */   {
/*  71:195 */     XSSFEvaluationWorkbook fpb = XSSFEvaluationWorkbook.create(this._workbook);
/*  72:    */     
/*  73:197 */     FormulaParser.parse(formulaText, fpb, FormulaType.NAMEDRANGE, getSheetIndex(), -1);
/*  74:    */     
/*  75:199 */     this._ctName.setStringValue(formulaText);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public boolean isDeleted()
/*  79:    */   {
/*  80:203 */     String formulaText = getRefersToFormula();
/*  81:204 */     if (formulaText == null) {
/*  82:205 */       return false;
/*  83:    */     }
/*  84:207 */     XSSFEvaluationWorkbook fpb = XSSFEvaluationWorkbook.create(this._workbook);
/*  85:208 */     Ptg[] ptgs = FormulaParser.parse(formulaText, fpb, FormulaType.NAMEDRANGE, getSheetIndex(), -1);
/*  86:209 */     return Ptg.doesFormulaReferToDeletedCell(ptgs);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setSheetIndex(int index)
/*  90:    */   {
/*  91:218 */     int lastSheetIx = this._workbook.getNumberOfSheets() - 1;
/*  92:219 */     if ((index < -1) || (index > lastSheetIx)) {
/*  93:220 */       throw new IllegalArgumentException("Sheet index (" + index + ") is out of range" + (lastSheetIx == -1 ? "" : new StringBuilder().append(" (0..").append(lastSheetIx).append(")").toString()));
/*  94:    */     }
/*  95:224 */     if (index == -1)
/*  96:    */     {
/*  97:225 */       if (this._ctName.isSetLocalSheetId()) {
/*  98:225 */         this._ctName.unsetLocalSheetId();
/*  99:    */       }
/* 100:    */     }
/* 101:    */     else {
/* 102:227 */       this._ctName.setLocalSheetId(index);
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   public int getSheetIndex()
/* 107:    */   {
/* 108:237 */     return this._ctName.isSetLocalSheetId() ? (int)this._ctName.getLocalSheetId() : -1;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void setFunction(boolean value)
/* 112:    */   {
/* 113:247 */     this._ctName.setFunction(value);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public boolean getFunction()
/* 117:    */   {
/* 118:257 */     return this._ctName.getFunction();
/* 119:    */   }
/* 120:    */   
/* 121:    */   public void setFunctionGroupId(int functionGroupId)
/* 122:    */   {
/* 123:268 */     this._ctName.setFunctionGroupId(functionGroupId);
/* 124:    */   }
/* 125:    */   
/* 126:    */   public int getFunctionGroupId()
/* 127:    */   {
/* 128:279 */     return (int)this._ctName.getFunctionGroupId();
/* 129:    */   }
/* 130:    */   
/* 131:    */   public String getSheetName()
/* 132:    */   {
/* 133:289 */     if (this._ctName.isSetLocalSheetId())
/* 134:    */     {
/* 135:291 */       int sheetId = (int)this._ctName.getLocalSheetId();
/* 136:292 */       return this._workbook.getSheetName(sheetId);
/* 137:    */     }
/* 138:294 */     String ref = getRefersToFormula();
/* 139:295 */     AreaReference areaRef = new AreaReference(ref, SpreadsheetVersion.EXCEL2007);
/* 140:296 */     return areaRef.getFirstCell().getSheetName();
/* 141:    */   }
/* 142:    */   
/* 143:    */   public boolean isFunctionName()
/* 144:    */   {
/* 145:305 */     return getFunction();
/* 146:    */   }
/* 147:    */   
/* 148:    */   public String getComment()
/* 149:    */   {
/* 150:314 */     return this._ctName.getComment();
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void setComment(String comment)
/* 154:    */   {
/* 155:323 */     this._ctName.setComment(comment);
/* 156:    */   }
/* 157:    */   
/* 158:    */   public int hashCode()
/* 159:    */   {
/* 160:328 */     return this._ctName.toString().hashCode();
/* 161:    */   }
/* 162:    */   
/* 163:    */   public boolean equals(Object o)
/* 164:    */   {
/* 165:342 */     if (o == this) {
/* 166:342 */       return true;
/* 167:    */     }
/* 168:344 */     if (!(o instanceof XSSFName)) {
/* 169:344 */       return false;
/* 170:    */     }
/* 171:346 */     XSSFName cf = (XSSFName)o;
/* 172:347 */     return this._ctName.toString().equals(cf.getCTName().toString());
/* 173:    */   }
/* 174:    */   
/* 175:    */   private static void validateName(String name)
/* 176:    */   {
/* 177:373 */     if (name.length() == 0) {
/* 178:374 */       throw new IllegalArgumentException("Name cannot be blank");
/* 179:    */     }
/* 180:376 */     if (name.length() > 255) {
/* 181:377 */       throw new IllegalArgumentException("Invalid name: '" + name + "': cannot exceed 255 characters in length");
/* 182:    */     }
/* 183:379 */     if ((name.equalsIgnoreCase("R")) || (name.equalsIgnoreCase("C"))) {
/* 184:380 */       throw new IllegalArgumentException("Invalid name: '" + name + "': cannot be special shorthand R or C");
/* 185:    */     }
/* 186:384 */     char c = name.charAt(0);
/* 187:385 */     String allowedSymbols = "_\\";
/* 188:386 */     boolean characterIsValid = (Character.isLetter(c)) || (allowedSymbols.indexOf(c) != -1);
/* 189:387 */     if (!characterIsValid) {
/* 190:388 */       throw new IllegalArgumentException("Invalid name: '" + name + "': first character must be underscore or a letter");
/* 191:    */     }
/* 192:392 */     allowedSymbols = "_.\\";
/* 193:393 */     for (char ch : name.toCharArray())
/* 194:    */     {
/* 195:394 */       characterIsValid = (Character.isLetterOrDigit(ch)) || (allowedSymbols.indexOf(ch) != -1);
/* 196:395 */       if (!characterIsValid) {
/* 197:396 */         throw new IllegalArgumentException("Invalid name: '" + name + "': name must be letter, digit, period, or underscore");
/* 198:    */       }
/* 199:    */     }
/* 200:402 */     if (name.matches("[A-Za-z]+\\d+"))
/* 201:    */     {
/* 202:403 */       String col = name.replaceAll("\\d", "");
/* 203:404 */       String row = name.replaceAll("[A-Za-z]", "");
/* 204:    */       try
/* 205:    */       {
/* 206:407 */         if (CellReference.cellReferenceIsWithinRange(col, row, SpreadsheetVersion.EXCEL2007)) {
/* 207:408 */           throw new IllegalArgumentException("Invalid name: '" + name + "': cannot be $A$1-style cell reference");
/* 208:    */         }
/* 209:    */       }
/* 210:    */       catch (NumberFormatException e) {}
/* 211:    */     }
/* 212:417 */     if (name.matches("[Rr]\\d+[Cc]\\d+")) {
/* 213:418 */       throw new IllegalArgumentException("Invalid name: '" + name + "': cannot be R1C1-style cell reference");
/* 214:    */     }
/* 215:    */   }
/* 216:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFName
 * JD-Core Version:    0.7.0.1
 */