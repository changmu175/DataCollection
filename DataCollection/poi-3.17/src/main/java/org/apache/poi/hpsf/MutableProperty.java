/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ import java.io.UnsupportedEncodingException;
/*  4:   */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*  5:   */ import org.apache.poi.util.Removal;
/*  6:   */ 
/*  7:   */ @Deprecated
/*  8:   */ @Removal(version="3.18")
/*  9:   */ public class MutableProperty
/* 10:   */   extends Property
/* 11:   */ {
/* 12:   */   public MutableProperty() {}
/* 13:   */   
/* 14:   */   public MutableProperty(Property p)
/* 15:   */   {
/* 16:39 */     super(p);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public MutableProperty(long id, long type, Object value)
/* 20:   */   {
/* 21:43 */     super(id, type, value);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public MutableProperty(long id, byte[] src, long offset, int length, int codepage)
/* 25:   */     throws UnsupportedEncodingException
/* 26:   */   {
/* 27:48 */     super(id, src, offset, length, codepage);
/* 28:   */   }
/* 29:   */   
/* 30:   */   public MutableProperty(long id, LittleEndianByteArrayInputStream leis, int length, int codepage)
/* 31:   */     throws UnsupportedEncodingException
/* 32:   */   {
/* 33:53 */     super(id, leis, length, codepage);
/* 34:   */   }
/* 35:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.MutableProperty
 * JD-Core Version:    0.7.0.1
 */