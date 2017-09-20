/*  1:   */ package org.apache.poi.hssf.usermodel;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import org.apache.poi.ss.usermodel.DataFormatter;
/*  5:   */ import org.apache.poi.util.LocaleUtil;
/*  6:   */ 
/*  7:   */ public final class HSSFDataFormatter
/*  8:   */   extends DataFormatter
/*  9:   */ {
/* 10:   */   public HSSFDataFormatter(Locale locale)
/* 11:   */   {
/* 12:72 */     super(locale);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public HSSFDataFormatter()
/* 16:   */   {
/* 17:79 */     this(LocaleUtil.getUserLocale());
/* 18:   */   }
/* 19:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFDataFormatter
 * JD-Core Version:    0.7.0.1
 */