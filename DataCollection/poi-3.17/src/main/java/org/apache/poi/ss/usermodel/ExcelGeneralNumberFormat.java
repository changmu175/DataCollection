/*  1:   */ package org.apache.poi.ss.usermodel;
/*  2:   */ 
/*  3:   */ import java.math.BigDecimal;
/*  4:   */ import java.math.MathContext;
/*  5:   */ import java.math.RoundingMode;
/*  6:   */ import java.text.DecimalFormat;
/*  7:   */ import java.text.DecimalFormatSymbols;
/*  8:   */ import java.text.FieldPosition;
/*  9:   */ import java.text.Format;
/* 10:   */ import java.text.ParsePosition;
/* 11:   */ import java.util.Locale;
/* 12:   */ 
/* 13:   */ public class ExcelGeneralNumberFormat
/* 14:   */   extends Format
/* 15:   */ {
/* 16:   */   private static final long serialVersionUID = 1L;
/* 17:41 */   private static final MathContext TO_10_SF = new MathContext(10, RoundingMode.HALF_UP);
/* 18:   */   private final DecimalFormatSymbols decimalSymbols;
/* 19:   */   private final DecimalFormat integerFormat;
/* 20:   */   private final DecimalFormat decimalFormat;
/* 21:   */   private final DecimalFormat scientificFormat;
/* 22:   */   
/* 23:   */   public ExcelGeneralNumberFormat(Locale locale)
/* 24:   */   {
/* 25:49 */     this.decimalSymbols = DecimalFormatSymbols.getInstance(locale);
/* 26:50 */     this.scientificFormat = new DecimalFormat("0.#####E0", this.decimalSymbols);
/* 27:51 */     DataFormatter.setExcelStyleRoundingMode(this.scientificFormat);
/* 28:52 */     this.integerFormat = new DecimalFormat("#", this.decimalSymbols);
/* 29:53 */     DataFormatter.setExcelStyleRoundingMode(this.integerFormat);
/* 30:54 */     this.decimalFormat = new DecimalFormat("#.##########", this.decimalSymbols);
/* 31:55 */     DataFormatter.setExcelStyleRoundingMode(this.decimalFormat);
/* 32:   */   }
/* 33:   */   
/* 34:   */   public StringBuffer format(Object number, StringBuffer toAppendTo, FieldPosition pos)
/* 35:   */   {
/* 36:60 */     if ((number instanceof Number))
/* 37:   */     {
/* 38:61 */       double value = ((Number)number).doubleValue();
/* 39:62 */       if ((Double.isInfinite(value)) || (Double.isNaN(value))) {
/* 40:63 */         return this.integerFormat.format(number, toAppendTo, pos);
/* 41:   */       }
/* 42:   */     }
/* 43:   */     else
/* 44:   */     {
/* 45:67 */       return this.integerFormat.format(number, toAppendTo, pos);
/* 46:   */     }
/* 47:   */     double value;
/* 48:70 */     double abs = Math.abs(value);
/* 49:71 */     if ((abs >= 100000000000.0D) || ((abs <= 1.0E-010D) && (abs > 0.0D))) {
/* 50:72 */       return this.scientificFormat.format(number, toAppendTo, pos);
/* 51:   */     }
/* 52:73 */     if ((Math.floor(value) == value) || (abs >= 10000000000.0D)) {
/* 53:75 */       return this.integerFormat.format(number, toAppendTo, pos);
/* 54:   */     }
/* 55:81 */     double rounded = new BigDecimal(value).round(TO_10_SF).doubleValue();
/* 56:82 */     return this.decimalFormat.format(rounded, toAppendTo, pos);
/* 57:   */   }
/* 58:   */   
/* 59:   */   public Object parseObject(String source, ParsePosition pos)
/* 60:   */   {
/* 61:86 */     throw new UnsupportedOperationException();
/* 62:   */   }
/* 63:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.ExcelGeneralNumberFormat
 * JD-Core Version:    0.7.0.1
 */