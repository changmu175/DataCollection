/*  1:   */ package org.apache.poi;
/*  2:   */ 
/*  3:   */ public final class POIXMLException
/*  4:   */   extends RuntimeException
/*  5:   */ {
/*  6:   */   public POIXMLException() {}
/*  7:   */   
/*  8:   */   public POIXMLException(String msg)
/*  9:   */   {
/* 10:41 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public POIXMLException(String msg, Throwable cause)
/* 14:   */   {
/* 15:55 */     super(msg, cause);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public POIXMLException(Throwable cause)
/* 19:   */   {
/* 20:68 */     super(cause);
/* 21:   */   }
/* 22:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.POIXMLException
 * JD-Core Version:    0.7.0.1
 */