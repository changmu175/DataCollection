/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ import java.io.UnsupportedEncodingException;
/*  4:   */ import org.apache.poi.util.Removal;
/*  5:   */ 
/*  6:   */ /**
/*  7:   */  * @deprecated
/*  8:   */  */
/*  9:   */ @Removal(version="3.18")
/* 10:   */ public class MutableSection
/* 11:   */   extends Section
/* 12:   */ {
/* 13:   */   public MutableSection() {}
/* 14:   */   
/* 15:   */   public MutableSection(Section s)
/* 16:   */   {
/* 17:37 */     super(s);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public MutableSection(byte[] src, int offset)
/* 21:   */     throws UnsupportedEncodingException
/* 22:   */   {
/* 23:41 */     super(src, offset);
/* 24:   */   }
/* 25:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.MutableSection
 * JD-Core Version:    0.7.0.1
 */