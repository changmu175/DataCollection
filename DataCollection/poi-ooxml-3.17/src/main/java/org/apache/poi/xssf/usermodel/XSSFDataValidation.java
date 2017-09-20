/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Map.Entry;
/*   6:    */ import org.apache.poi.ss.usermodel.DataValidation;
/*   7:    */ import org.apache.poi.ss.usermodel.DataValidationConstraint;
/*   8:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*   9:    */ import org.apache.poi.ss.util.CellRangeAddressList;
/*  10:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidation;
/*  11:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationErrorStyle;
/*  12:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationErrorStyle.Enum;
/*  13:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator;
/*  14:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator.Enum;
/*  15:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType;
/*  16:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType.Enum;
/*  17:    */ 
/*  18:    */ public class XSSFDataValidation
/*  19:    */   implements DataValidation
/*  20:    */ {
/*  21:    */   private static final int MAX_TEXT_LENGTH = 255;
/*  22:    */   private CTDataValidation ctDdataValidation;
/*  23:    */   private XSSFDataValidationConstraint validationConstraint;
/*  24:    */   private CellRangeAddressList regions;
/*  25: 44 */   static Map<Integer, STDataValidationOperator.Enum> operatorTypeMappings = new HashMap();
/*  26: 45 */   static Map<STDataValidationOperator.Enum, Integer> operatorTypeReverseMappings = new HashMap();
/*  27: 46 */   static Map<Integer, STDataValidationType.Enum> validationTypeMappings = new HashMap();
/*  28: 47 */   static Map<STDataValidationType.Enum, Integer> validationTypeReverseMappings = new HashMap();
/*  29: 48 */   static Map<Integer, STDataValidationErrorStyle.Enum> errorStyleMappings = new HashMap();
/*  30:    */   
/*  31:    */   static
/*  32:    */   {
/*  33: 51 */     errorStyleMappings.put(Integer.valueOf(2), STDataValidationErrorStyle.INFORMATION);
/*  34: 52 */     errorStyleMappings.put(Integer.valueOf(0), STDataValidationErrorStyle.STOP);
/*  35: 53 */     errorStyleMappings.put(Integer.valueOf(1), STDataValidationErrorStyle.WARNING);
/*  36:    */     
/*  37: 55 */     operatorTypeMappings.put(Integer.valueOf(0), STDataValidationOperator.BETWEEN);
/*  38: 56 */     operatorTypeMappings.put(Integer.valueOf(1), STDataValidationOperator.NOT_BETWEEN);
/*  39: 57 */     operatorTypeMappings.put(Integer.valueOf(2), STDataValidationOperator.EQUAL);
/*  40: 58 */     operatorTypeMappings.put(Integer.valueOf(3), STDataValidationOperator.NOT_EQUAL);
/*  41: 59 */     operatorTypeMappings.put(Integer.valueOf(4), STDataValidationOperator.GREATER_THAN);
/*  42: 60 */     operatorTypeMappings.put(Integer.valueOf(6), STDataValidationOperator.GREATER_THAN_OR_EQUAL);
/*  43: 61 */     operatorTypeMappings.put(Integer.valueOf(5), STDataValidationOperator.LESS_THAN);
/*  44: 62 */     operatorTypeMappings.put(Integer.valueOf(7), STDataValidationOperator.LESS_THAN_OR_EQUAL);
/*  45: 64 */     for (Entry<Integer, STDataValidationOperator.Enum> entry : operatorTypeMappings.entrySet()) {
/*  46: 65 */       operatorTypeReverseMappings.put(entry.getValue(), entry.getKey());
/*  47:    */     }
/*  48: 68 */     validationTypeMappings.put(Integer.valueOf(7), STDataValidationType.CUSTOM);
/*  49: 69 */     validationTypeMappings.put(Integer.valueOf(4), STDataValidationType.DATE);
/*  50: 70 */     validationTypeMappings.put(Integer.valueOf(2), STDataValidationType.DECIMAL);
/*  51: 71 */     validationTypeMappings.put(Integer.valueOf(3), STDataValidationType.LIST);
/*  52: 72 */     validationTypeMappings.put(Integer.valueOf(0), STDataValidationType.NONE);
/*  53: 73 */     validationTypeMappings.put(Integer.valueOf(6), STDataValidationType.TEXT_LENGTH);
/*  54: 74 */     validationTypeMappings.put(Integer.valueOf(5), STDataValidationType.TIME);
/*  55: 75 */     validationTypeMappings.put(Integer.valueOf(1), STDataValidationType.WHOLE);
/*  56: 77 */     for (Entry<Integer, STDataValidationType.Enum> entry : validationTypeMappings.entrySet()) {
/*  57: 78 */       validationTypeReverseMappings.put(entry.getValue(), entry.getKey());
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   XSSFDataValidation(CellRangeAddressList regions, CTDataValidation ctDataValidation)
/*  62:    */   {
/*  63: 83 */     this(getConstraint(ctDataValidation), regions, ctDataValidation);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public XSSFDataValidation(XSSFDataValidationConstraint constraint, CellRangeAddressList regions, CTDataValidation ctDataValidation)
/*  67:    */   {
/*  68: 88 */     this.validationConstraint = constraint;
/*  69: 89 */     this.ctDdataValidation = ctDataValidation;
/*  70: 90 */     this.regions = regions;
/*  71:    */   }
/*  72:    */   
/*  73:    */   CTDataValidation getCtDdataValidation()
/*  74:    */   {
/*  75: 94 */     return this.ctDdataValidation;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void createErrorBox(String title, String text)
/*  79:    */   {
/*  80:104 */     if ((title != null) && (title.length() > 255)) {
/*  81:105 */       throw new IllegalStateException("Error-title cannot be longer than 32 characters, but had: " + title);
/*  82:    */     }
/*  83:107 */     if ((text != null) && (text.length() > 255)) {
/*  84:108 */       throw new IllegalStateException("Error-text cannot be longer than 255 characters, but had: " + text);
/*  85:    */     }
/*  86:110 */     this.ctDdataValidation.setErrorTitle(encodeUtf(title));
/*  87:111 */     this.ctDdataValidation.setError(encodeUtf(text));
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void createPromptBox(String title, String text)
/*  91:    */   {
/*  92:119 */     if ((title != null) && (title.length() > 255)) {
/*  93:120 */       throw new IllegalStateException("Error-title cannot be longer than 32 characters, but had: " + title);
/*  94:    */     }
/*  95:122 */     if ((text != null) && (text.length() > 255)) {
/*  96:123 */       throw new IllegalStateException("Error-text cannot be longer than 255 characters, but had: " + text);
/*  97:    */     }
/*  98:125 */     this.ctDdataValidation.setPromptTitle(encodeUtf(title));
/*  99:126 */     this.ctDdataValidation.setPrompt(encodeUtf(text));
/* 100:    */   }
/* 101:    */   
/* 102:    */   private String encodeUtf(String text)
/* 103:    */   {
/* 104:143 */     if (text == null) {
/* 105:144 */       return null;
/* 106:    */     }
/* 107:147 */     StringBuilder builder = new StringBuilder();
/* 108:148 */     for (char c : text.toCharArray()) {
/* 109:150 */       if (c < ' ') {
/* 110:151 */         builder.append("_x").append(c < '\020' ? "000" : "00").append(Integer.toHexString(c)).append("_");
/* 111:    */       } else {
/* 112:153 */         builder.append(c);
/* 113:    */       }
/* 114:    */     }
/* 115:157 */     return builder.toString();
/* 116:    */   }
/* 117:    */   
/* 118:    */   public boolean getEmptyCellAllowed()
/* 119:    */   {
/* 120:164 */     return this.ctDdataValidation.getAllowBlank();
/* 121:    */   }
/* 122:    */   
/* 123:    */   public String getErrorBoxText()
/* 124:    */   {
/* 125:171 */     return this.ctDdataValidation.getError();
/* 126:    */   }
/* 127:    */   
/* 128:    */   public String getErrorBoxTitle()
/* 129:    */   {
/* 130:178 */     return this.ctDdataValidation.getErrorTitle();
/* 131:    */   }
/* 132:    */   
/* 133:    */   public int getErrorStyle()
/* 134:    */   {
/* 135:185 */     return this.ctDdataValidation.getErrorStyle().intValue();
/* 136:    */   }
/* 137:    */   
/* 138:    */   public String getPromptBoxText()
/* 139:    */   {
/* 140:192 */     return this.ctDdataValidation.getPrompt();
/* 141:    */   }
/* 142:    */   
/* 143:    */   public String getPromptBoxTitle()
/* 144:    */   {
/* 145:199 */     return this.ctDdataValidation.getPromptTitle();
/* 146:    */   }
/* 147:    */   
/* 148:    */   public boolean getShowErrorBox()
/* 149:    */   {
/* 150:206 */     return this.ctDdataValidation.getShowErrorMessage();
/* 151:    */   }
/* 152:    */   
/* 153:    */   public boolean getShowPromptBox()
/* 154:    */   {
/* 155:213 */     return this.ctDdataValidation.getShowInputMessage();
/* 156:    */   }
/* 157:    */   
/* 158:    */   public boolean getSuppressDropDownArrow()
/* 159:    */   {
/* 160:220 */     return !this.ctDdataValidation.getShowDropDown();
/* 161:    */   }
/* 162:    */   
/* 163:    */   public DataValidationConstraint getValidationConstraint()
/* 164:    */   {
/* 165:227 */     return this.validationConstraint;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public void setEmptyCellAllowed(boolean allowed)
/* 169:    */   {
/* 170:234 */     this.ctDdataValidation.setAllowBlank(allowed);
/* 171:    */   }
/* 172:    */   
/* 173:    */   public void setErrorStyle(int errorStyle)
/* 174:    */   {
/* 175:241 */     this.ctDdataValidation.setErrorStyle((STDataValidationErrorStyle.Enum)errorStyleMappings.get(Integer.valueOf(errorStyle)));
/* 176:    */   }
/* 177:    */   
/* 178:    */   public void setShowErrorBox(boolean show)
/* 179:    */   {
/* 180:248 */     this.ctDdataValidation.setShowErrorMessage(show);
/* 181:    */   }
/* 182:    */   
/* 183:    */   public void setShowPromptBox(boolean show)
/* 184:    */   {
/* 185:255 */     this.ctDdataValidation.setShowInputMessage(show);
/* 186:    */   }
/* 187:    */   
/* 188:    */   public void setSuppressDropDownArrow(boolean suppress)
/* 189:    */   {
/* 190:262 */     if (this.validationConstraint.getValidationType() == 3) {
/* 191:263 */       this.ctDdataValidation.setShowDropDown(!suppress);
/* 192:    */     }
/* 193:    */   }
/* 194:    */   
/* 195:    */   public CellRangeAddressList getRegions()
/* 196:    */   {
/* 197:268 */     return this.regions;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public String prettyPrint()
/* 201:    */   {
/* 202:272 */     StringBuilder builder = new StringBuilder();
/* 203:273 */     for (CellRangeAddress address : this.regions.getCellRangeAddresses()) {
/* 204:274 */       builder.append(address.formatAsString());
/* 205:    */     }
/* 206:276 */     builder.append(" => ");
/* 207:277 */     builder.append(this.validationConstraint.prettyPrint());
/* 208:278 */     return builder.toString();
/* 209:    */   }
/* 210:    */   
/* 211:    */   private static XSSFDataValidationConstraint getConstraint(CTDataValidation ctDataValidation)
/* 212:    */   {
/* 213:282 */     String formula1 = ctDataValidation.getFormula1();
/* 214:283 */     String formula2 = ctDataValidation.getFormula2();
/* 215:284 */     STDataValidationOperator.Enum operator = ctDataValidation.getOperator();
/* 216:285 */     STDataValidationType.Enum type = ctDataValidation.getType();
/* 217:286 */     Integer validationType = (Integer)validationTypeReverseMappings.get(type);
/* 218:287 */     Integer operatorType = (Integer)operatorTypeReverseMappings.get(operator);
/* 219:288 */     return new XSSFDataValidationConstraint(validationType.intValue(), operatorType.intValue(), formula1, formula2);
/* 220:    */   }
/* 221:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFDataValidation

 * JD-Core Version:    0.7.0.1

 */