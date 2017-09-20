/*  1:   */ package org.apache.poi.util;
/*  2:   */ 
/*  3:   */ @Internal
/*  4:   */ public class NullLogger
/*  5:   */   extends POILogger
/*  6:   */ {
/*  7:   */   public void initialize(String cat) {}
/*  8:   */   
/*  9:   */   protected void _log(int level, Object obj1) {}
/* 10:   */   
/* 11:   */   protected void _log(int level, Object obj1, Throwable exception) {}
/* 12:   */   
/* 13:   */   public void log(int level, Object... objs) {}
/* 14:   */   
/* 15:   */   public boolean check(int level)
/* 16:   */   {
/* 17:77 */     return false;
/* 18:   */   }
/* 19:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.NullLogger
 * JD-Core Version:    0.7.0.1
 */