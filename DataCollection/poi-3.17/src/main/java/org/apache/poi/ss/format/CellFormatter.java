/*   1:    */ package org.apache.poi.ss.format;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import java.util.logging.Logger;
/*   5:    */ import org.apache.poi.util.LocaleUtil;
/*   6:    */ 
/*   7:    */ public abstract class CellFormatter
/*   8:    */ {
/*   9:    */   protected final String format;
/*  10:    */   protected final Locale locale;
/*  11:    */   
/*  12:    */   public CellFormatter(String format)
/*  13:    */   {
/*  14: 40 */     this(LocaleUtil.getUserLocale(), format);
/*  15:    */   }
/*  16:    */   
/*  17:    */   public CellFormatter(Locale locale, String format)
/*  18:    */   {
/*  19: 50 */     this.locale = locale;
/*  20: 51 */     this.format = format;
/*  21:    */   }
/*  22:    */   
/*  23: 55 */   static final Logger logger = Logger.getLogger(CellFormatter.class.getName());
/*  24:    */   
/*  25:    */   public abstract void formatValue(StringBuffer paramStringBuffer, Object paramObject);
/*  26:    */   
/*  27:    */   public abstract void simpleValue(StringBuffer paramStringBuffer, Object paramObject);
/*  28:    */   
/*  29:    */   public String format(Object value)
/*  30:    */   {
/*  31: 82 */     StringBuffer sb = new StringBuffer();
/*  32: 83 */     formatValue(sb, value);
/*  33: 84 */     return sb.toString();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public String simpleFormat(Object value)
/*  37:    */   {
/*  38: 95 */     StringBuffer sb = new StringBuffer();
/*  39: 96 */     simpleValue(sb, value);
/*  40: 97 */     return sb.toString();
/*  41:    */   }
/*  42:    */   
/*  43:    */   static String quote(String str)
/*  44:    */   {
/*  45:108 */     return '"' + str + '"';
/*  46:    */   }
/*  47:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.format.CellFormatter
 * JD-Core Version:    0.7.0.1
 */