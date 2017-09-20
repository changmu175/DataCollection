/*  1:   */ package org.apache.poi.ss.format;
/*  2:   */ 
/*  3:   */ import java.util.Formatter;
/*  4:   */ import java.util.Locale;
/*  5:   */ import org.apache.poi.util.LocaleUtil;
/*  6:   */ 
/*  7:   */ public class CellGeneralFormatter
/*  8:   */   extends CellFormatter
/*  9:   */ {
/* 10:   */   public CellGeneralFormatter()
/* 11:   */   {
/* 12:32 */     this(LocaleUtil.getUserLocale());
/* 13:   */   }
/* 14:   */   
/* 15:   */   public CellGeneralFormatter(Locale locale)
/* 16:   */   {
/* 17:36 */     super(locale, "General");
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void formatValue(StringBuffer toAppendTo, Object value)
/* 21:   */   {
/* 22:47 */     if ((value instanceof Number))
/* 23:   */     {
/* 24:48 */       double val = ((Number)value).doubleValue();
/* 25:49 */       if (val == 0.0D)
/* 26:   */       {
/* 27:50 */         toAppendTo.append('0');
/* 28:51 */         return;
/* 29:   */       }
/* 30:55 */       double exp = Math.log10(Math.abs(val));
/* 31:56 */       boolean stripZeros = true;
/* 32:   */       String fmt;
/* 33:   */       String fmt;
/* 34:57 */       if ((exp > 10.0D) || (exp < -9.0D))
/* 35:   */       {
/* 36:58 */         fmt = "%1.5E";
/* 37:   */       }
/* 38:   */       else
/* 39:   */       {
/* 40:   */         String fmt;
/* 41:59 */         if (val != val)
/* 42:   */         {
/* 43:60 */           fmt = "%1.9f";
/* 44:   */         }
/* 45:   */         else
/* 46:   */         {
/* 47:62 */           fmt = "%1.0f";
/* 48:63 */           stripZeros = false;
/* 49:   */         }
/* 50:   */       }
/* 51:66 */       Formatter formatter = new Formatter(toAppendTo, this.locale);
/* 52:   */       try
/* 53:   */       {
/* 54:68 */         formatter.format(this.locale, fmt, new Object[] { value });
/* 55:   */       }
/* 56:   */       finally
/* 57:   */       {
/* 58:70 */         formatter.close();
/* 59:   */       }
/* 60:72 */       if (stripZeros)
/* 61:   */       {
/* 62:   */         int removeFrom;
/* 63:   */         int removeFrom;
/* 64:75 */         if (fmt.endsWith("E")) {
/* 65:76 */           removeFrom = toAppendTo.lastIndexOf("E") - 1;
/* 66:   */         } else {
/* 67:78 */           removeFrom = toAppendTo.length() - 1;
/* 68:   */         }
/* 69:79 */         while (toAppendTo.charAt(removeFrom) == '0') {
/* 70:80 */           toAppendTo.deleteCharAt(removeFrom--);
/* 71:   */         }
/* 72:82 */         if (toAppendTo.charAt(removeFrom) == '.') {
/* 73:83 */           toAppendTo.deleteCharAt(removeFrom--);
/* 74:   */         }
/* 75:   */       }
/* 76:   */     }
/* 77:86 */     else if ((value instanceof Boolean))
/* 78:   */     {
/* 79:87 */       toAppendTo.append(value.toString().toUpperCase(Locale.ROOT));
/* 80:   */     }
/* 81:   */     else
/* 82:   */     {
/* 83:89 */       toAppendTo.append(value);
/* 84:   */     }
/* 85:   */   }
/* 86:   */   
/* 87:   */   public void simpleValue(StringBuffer toAppendTo, Object value)
/* 88:   */   {
/* 89:95 */     formatValue(toAppendTo, value);
/* 90:   */   }
/* 91:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.format.CellGeneralFormatter
 * JD-Core Version:    0.7.0.1
 */