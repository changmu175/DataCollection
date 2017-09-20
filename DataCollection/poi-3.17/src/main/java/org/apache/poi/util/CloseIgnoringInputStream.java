/*  1:   */ package org.apache.poi.util;
/*  2:   */ 
/*  3:   */ import java.io.FilterInputStream;
/*  4:   */ import java.io.InputStream;
/*  5:   */ 
/*  6:   */ public class CloseIgnoringInputStream
/*  7:   */   extends FilterInputStream
/*  8:   */ {
/*  9:   */   public CloseIgnoringInputStream(InputStream in)
/* 10:   */   {
/* 11:32 */     super(in);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public void close() {}
/* 15:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.CloseIgnoringInputStream
 * JD-Core Version:    0.7.0.1
 */