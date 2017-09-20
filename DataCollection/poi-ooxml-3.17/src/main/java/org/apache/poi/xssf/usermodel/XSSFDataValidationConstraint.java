/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.regex.Pattern;
/*   6:    */ import org.apache.poi.ss.usermodel.DataValidationConstraint;
/*   7:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator.Enum;
/*   8:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType.Enum;
/*   9:    */ 
/*  10:    */ public class XSSFDataValidationConstraint
/*  11:    */   implements DataValidationConstraint
/*  12:    */ {
/*  13:    */   private static final String LIST_SEPARATOR = ",";
/*  14: 36 */   private static final Pattern LIST_SPLIT_REGEX = Pattern.compile("\\s*,\\s*");
/*  15:    */   private static final String QUOTE = "\"";
/*  16:    */   private String formula1;
/*  17:    */   private String formula2;
/*  18: 41 */   private int validationType = -1;
/*  19: 42 */   private int operator = -1;
/*  20:    */   private String[] explicitListOfValues;
/*  21:    */   
/*  22:    */   public XSSFDataValidationConstraint(String[] explicitListOfValues)
/*  23:    */   {
/*  24: 49 */     if ((explicitListOfValues == null) || (explicitListOfValues.length == 0)) {
/*  25: 50 */       throw new IllegalArgumentException("List validation with explicit values must specify at least one value");
/*  26:    */     }
/*  27: 52 */     this.validationType = 3;
/*  28: 53 */     setExplicitListValues(explicitListOfValues);
/*  29:    */     
/*  30: 55 */     validate();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public XSSFDataValidationConstraint(int validationType, String formula1)
/*  34:    */   {
/*  35: 60 */     setFormula1(formula1);
/*  36: 61 */     this.validationType = validationType;
/*  37: 62 */     validate();
/*  38:    */   }
/*  39:    */   
/*  40:    */   public XSSFDataValidationConstraint(int validationType, int operator, String formula1)
/*  41:    */   {
/*  42: 69 */     setFormula1(formula1);
/*  43: 70 */     this.validationType = validationType;
/*  44: 71 */     this.operator = operator;
/*  45: 72 */     validate();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public XSSFDataValidationConstraint(int validationType, int operator, String formula1, String formula2)
/*  49:    */   {
/*  50: 86 */     setFormula1(formula1);
/*  51: 87 */     setFormula2(formula2);
/*  52: 88 */     this.validationType = validationType;
/*  53: 89 */     this.operator = operator;
/*  54:    */     
/*  55: 91 */     validate();
/*  56: 96 */     if ((3 == validationType) && (this.formula1 != null) && (isQuoted(this.formula1))) {
/*  57: 99 */       this.explicitListOfValues = LIST_SPLIT_REGEX.split(unquote(this.formula1));
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   public String[] getExplicitListValues()
/*  62:    */   {
/*  63:107 */     return this.explicitListOfValues;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public String getFormula1()
/*  67:    */   {
/*  68:114 */     return this.formula1;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public String getFormula2()
/*  72:    */   {
/*  73:121 */     return this.formula2;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public int getOperator()
/*  77:    */   {
/*  78:128 */     return this.operator;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public int getValidationType()
/*  82:    */   {
/*  83:135 */     return this.validationType;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setExplicitListValues(String[] explicitListValues)
/*  87:    */   {
/*  88:142 */     this.explicitListOfValues = explicitListValues;
/*  89:146 */     if ((this.explicitListOfValues != null) && (this.explicitListOfValues.length > 0))
/*  90:    */     {
/*  91:147 */       StringBuilder builder = new StringBuilder("\"");
/*  92:148 */       for (int i = 0; i < explicitListValues.length; i++)
/*  93:    */       {
/*  94:149 */         String string = explicitListValues[i];
/*  95:150 */         if (builder.length() > 1) {
/*  96:151 */           builder.append(",");
/*  97:    */         }
/*  98:153 */         builder.append(string);
/*  99:    */       }
/* 100:155 */       builder.append("\"");
/* 101:156 */       setFormula1(builder.toString());
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setFormula1(String formula1)
/* 106:    */   {
/* 107:164 */     this.formula1 = removeLeadingEquals(formula1);
/* 108:    */   }
/* 109:    */   
/* 110:    */   protected static String removeLeadingEquals(String formula1)
/* 111:    */   {
/* 112:168 */     return formula1.charAt(0) == '=' ? formula1.substring(1) : isFormulaEmpty(formula1) ? formula1 : formula1;
/* 113:    */   }
/* 114:    */   
/* 115:    */   private static boolean isQuoted(String s)
/* 116:    */   {
/* 117:171 */     return (s.startsWith("\"")) && (s.endsWith("\""));
/* 118:    */   }
/* 119:    */   
/* 120:    */   private static String unquote(String s)
/* 121:    */   {
/* 122:175 */     if (isQuoted(s)) {
/* 123:176 */       return s.substring(1, s.length() - 1);
/* 124:    */     }
/* 125:178 */     return s;
/* 126:    */   }
/* 127:    */   
/* 128:    */   protected static boolean isFormulaEmpty(String formula1)
/* 129:    */   {
/* 130:181 */     return (formula1 == null) || (formula1.trim().length() == 0);
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void setFormula2(String formula2)
/* 134:    */   {
/* 135:188 */     this.formula2 = removeLeadingEquals(formula2);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void setOperator(int operator)
/* 139:    */   {
/* 140:195 */     this.operator = operator;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public void validate()
/* 144:    */   {
/* 145:199 */     if (this.validationType == 0) {
/* 146:200 */       return;
/* 147:    */     }
/* 148:203 */     if (this.validationType == 3)
/* 149:    */     {
/* 150:204 */       if (isFormulaEmpty(this.formula1)) {
/* 151:205 */         throw new IllegalArgumentException("A valid formula or a list of values must be specified for list validation.");
/* 152:    */       }
/* 153:    */     }
/* 154:    */     else
/* 155:    */     {
/* 156:208 */       if (isFormulaEmpty(this.formula1)) {
/* 157:209 */         throw new IllegalArgumentException("Formula is not specified. Formula is required for all validation types except explicit list validation.");
/* 158:    */       }
/* 159:212 */       if (this.validationType != 7)
/* 160:    */       {
/* 161:213 */         if (this.operator == -1) {
/* 162:214 */           throw new IllegalArgumentException("This validation type requires an operator to be specified.");
/* 163:    */         }
/* 164:215 */         if (((this.operator == 0) || (this.operator == 1)) && (isFormulaEmpty(this.formula2))) {
/* 165:216 */           throw new IllegalArgumentException("Between and not between comparisons require two formulae to be specified.");
/* 166:    */         }
/* 167:    */       }
/* 168:    */     }
/* 169:    */   }
/* 170:    */   
/* 171:    */   public String prettyPrint()
/* 172:    */   {
/* 173:224 */     StringBuilder builder = new StringBuilder();
/* 174:225 */     STDataValidationType.Enum vt = (STDataValidationType.Enum)XSSFDataValidation.validationTypeMappings.get(Integer.valueOf(this.validationType));
/* 175:226 */     STDataValidationOperator.Enum ot = (STDataValidationOperator.Enum)XSSFDataValidation.operatorTypeMappings.get(Integer.valueOf(this.operator));
/* 176:227 */     builder.append(vt);
/* 177:228 */     builder.append(' ');
/* 178:229 */     if (this.validationType != 0)
/* 179:    */     {
/* 180:230 */       if ((this.validationType != 3) && (this.validationType != 7)) {
/* 181:232 */         builder.append(",").append(ot).append(", ");
/* 182:    */       }
/* 183:234 */       String NOQUOTE = "";
/* 184:235 */       if ((this.validationType == 3) && (this.explicitListOfValues != null)) {
/* 185:236 */         builder.append("").append(Arrays.asList(this.explicitListOfValues)).append("").append(' ');
/* 186:    */       } else {
/* 187:238 */         builder.append("").append(this.formula1).append("").append(' ');
/* 188:    */       }
/* 189:240 */       if (this.formula2 != null) {
/* 190:241 */         builder.append("").append(this.formula2).append("").append(' ');
/* 191:    */       }
/* 192:    */     }
/* 193:244 */     return builder.toString();
/* 194:    */   }
/* 195:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint
 * JD-Core Version:    0.7.0.1
 */