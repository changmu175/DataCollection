/*  1:   */ package org.apache.poi.hssf.usermodel;
/*  2:   */ 
/*  3:   */ import java.util.Calendar;
/*  4:   */ import org.apache.poi.ss.usermodel.DateUtil;
/*  5:   */ 
/*  6:   */ public class HSSFDateUtil
/*  7:   */   extends DateUtil
/*  8:   */ {
/*  9:   */   protected static int absoluteDay(Calendar cal, boolean use1904windowing)
/* 10:   */   {
/* 11:36 */     return DateUtil.absoluteDay(cal, use1904windowing);
/* 12:   */   }
/* 13:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFDateUtil
 * JD-Core Version:    0.7.0.1
 */