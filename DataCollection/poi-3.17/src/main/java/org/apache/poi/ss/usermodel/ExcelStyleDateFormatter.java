/*   1:    */ package org.apache.poi.ss.usermodel;
/*   2:    */ 
/*   3:    */ import java.math.RoundingMode;
/*   4:    */ import java.text.DateFormatSymbols;
/*   5:    */ import java.text.DecimalFormat;
/*   6:    */ import java.text.DecimalFormatSymbols;
/*   7:    */ import java.text.FieldPosition;
/*   8:    */ import java.text.SimpleDateFormat;
/*   9:    */ import java.util.Date;
/*  10:    */ import java.util.Locale;
/*  11:    */ import org.apache.poi.util.LocaleUtil;
/*  12:    */ 
/*  13:    */ public class ExcelStyleDateFormatter
/*  14:    */   extends SimpleDateFormat
/*  15:    */ {
/*  16:    */   public static final char MMMMM_START_SYMBOL = '';
/*  17:    */   public static final char MMMMM_TRUNCATE_SYMBOL = '';
/*  18:    */   public static final char H_BRACKET_SYMBOL = '';
/*  19:    */   public static final char HH_BRACKET_SYMBOL = '';
/*  20:    */   public static final char M_BRACKET_SYMBOL = '';
/*  21:    */   public static final char MM_BRACKET_SYMBOL = '';
/*  22:    */   public static final char S_BRACKET_SYMBOL = '';
/*  23:    */   public static final char SS_BRACKET_SYMBOL = '';
/*  24:    */   public static final char L_BRACKET_SYMBOL = '';
/*  25:    */   public static final char LL_BRACKET_SYMBOL = '';
/*  26:    */   private static final DecimalFormat format1digit;
/*  27:    */   private static final DecimalFormat format2digits;
/*  28:    */   private static final DecimalFormat format3digit;
/*  29:    */   private static final DecimalFormat format4digits;
/*  30:    */   private double dateToBeFormatted;
/*  31:    */   
/*  32:    */   static
/*  33:    */   {
/*  34: 53 */     DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(Locale.ROOT);
/*  35: 54 */     format1digit = new DecimalFormat("0", dfs);
/*  36: 55 */     format2digits = new DecimalFormat("00", dfs);
/*  37: 56 */     format3digit = new DecimalFormat("0", dfs);
/*  38: 57 */     format4digits = new DecimalFormat("00", dfs);
/*  39: 58 */     DataFormatter.setExcelStyleRoundingMode(format1digit, RoundingMode.DOWN);
/*  40: 59 */     DataFormatter.setExcelStyleRoundingMode(format2digits, RoundingMode.DOWN);
/*  41: 60 */     DataFormatter.setExcelStyleRoundingMode(format3digit);
/*  42: 61 */     DataFormatter.setExcelStyleRoundingMode(format4digits);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public ExcelStyleDateFormatter(String pattern)
/*  46:    */   {
/*  47: 73 */     super(processFormatPattern(pattern), LocaleUtil.getUserLocale());setTimeZone(LocaleUtil.getUserTimeZone());
/*  48:    */   }
/*  49:    */   
/*  50:    */   public ExcelStyleDateFormatter(String pattern, DateFormatSymbols formatSymbols)
/*  51:    */   {
/*  52: 78 */     super(processFormatPattern(pattern), formatSymbols);setTimeZone(LocaleUtil.getUserTimeZone());
/*  53:    */   }
/*  54:    */   
/*  55:    */   public ExcelStyleDateFormatter(String pattern, Locale locale)
/*  56:    */   {
/*  57: 82 */     super(processFormatPattern(pattern), locale);setTimeZone(LocaleUtil.getUserTimeZone());
/*  58:    */   }
/*  59:    */   
/*  60:    */   private static String processFormatPattern(String f)
/*  61:    */   {
/*  62: 90 */     String t = f.replaceAll("MMMMM", "MMM");
/*  63: 91 */     t = t.replaceAll("\\[H]", String.valueOf(57360));
/*  64: 92 */     t = t.replaceAll("\\[HH]", String.valueOf(57361));
/*  65: 93 */     t = t.replaceAll("\\[m]", String.valueOf(57362));
/*  66: 94 */     t = t.replaceAll("\\[mm]", String.valueOf(57363));
/*  67: 95 */     t = t.replaceAll("\\[s]", String.valueOf(57364));
/*  68: 96 */     t = t.replaceAll("\\[ss]", String.valueOf(57365));
/*  69: 97 */     t = t.replaceAll("s.000", "s.SSS");
/*  70: 98 */     t = t.replaceAll("s.00", "s.");
/*  71: 99 */     t = t.replaceAll("s.0", "s.");
/*  72:100 */     return t;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setDateToBeFormatted(double date)
/*  76:    */   {
/*  77:110 */     this.dateToBeFormatted = date;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public StringBuffer format(Date date, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition)
/*  81:    */   {
/*  82:117 */     String s = super.format(date, paramStringBuffer, paramFieldPosition).toString();
/*  83:120 */     if (s.indexOf(57345) != -1) {
/*  84:121 */       s = s.replaceAll("(\\p{L}|\\p{P})[\\p{L}\\p{P}]+", "$1");
/*  85:    */     }
/*  86:127 */     if ((s.indexOf(57360) != -1) || (s.indexOf(57361) != -1))
/*  87:    */     {
/*  88:129 */       float hours = (float)this.dateToBeFormatted * 24.0F;
/*  89:    */       
/*  90:131 */       s = s.replaceAll(String.valueOf(57360), format1digit.format(hours));
/*  91:    */       
/*  92:    */ 
/*  93:    */ 
/*  94:135 */       s = s.replaceAll(String.valueOf(57361), format2digits.format(hours));
/*  95:    */     }
/*  96:141 */     if ((s.indexOf(57362) != -1) || (s.indexOf(57363) != -1))
/*  97:    */     {
/*  98:143 */       float minutes = (float)this.dateToBeFormatted * 24.0F * 60.0F;
/*  99:144 */       s = s.replaceAll(String.valueOf(57362), format1digit.format(minutes));
/* 100:    */       
/* 101:    */ 
/* 102:    */ 
/* 103:148 */       s = s.replaceAll(String.valueOf(57363), format2digits.format(minutes));
/* 104:    */     }
/* 105:153 */     if ((s.indexOf(57364) != -1) || (s.indexOf(57365) != -1))
/* 106:    */     {
/* 107:155 */       float seconds = (float)(this.dateToBeFormatted * 24.0D * 60.0D * 60.0D);
/* 108:156 */       s = s.replaceAll(String.valueOf(57364), format1digit.format(seconds));
/* 109:    */       
/* 110:    */ 
/* 111:    */ 
/* 112:160 */       s = s.replaceAll(String.valueOf(57365), format2digits.format(seconds));
/* 113:    */     }
/* 114:166 */     if ((s.indexOf(57366) != -1) || (s.indexOf(57367) != -1))
/* 115:    */     {
/* 116:168 */       float millisTemp = (float)((this.dateToBeFormatted - Math.floor(this.dateToBeFormatted)) * 24.0D * 60.0D * 60.0D);
/* 117:169 */       float millis = millisTemp - (int)millisTemp;
/* 118:170 */       s = s.replaceAll(String.valueOf(57366), format3digit.format(millis * 10.0F));
/* 119:    */       
/* 120:    */ 
/* 121:    */ 
/* 122:174 */       s = s.replaceAll(String.valueOf(57367), format4digits.format(millis * 100.0F));
/* 123:    */     }
/* 124:180 */     return new StringBuffer(s);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public boolean equals(Object o)
/* 128:    */   {
/* 129:185 */     if (!(o instanceof ExcelStyleDateFormatter)) {
/* 130:186 */       return false;
/* 131:    */     }
/* 132:189 */     ExcelStyleDateFormatter other = (ExcelStyleDateFormatter)o;
/* 133:190 */     return this.dateToBeFormatted == other.dateToBeFormatted;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public int hashCode()
/* 137:    */   {
/* 138:195 */     return new Double(this.dateToBeFormatted).hashCode();
/* 139:    */   }
/* 140:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.ExcelStyleDateFormatter
 * JD-Core Version:    0.7.0.1
 */