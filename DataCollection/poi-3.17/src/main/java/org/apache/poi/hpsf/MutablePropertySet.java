/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import java.io.UnsupportedEncodingException;
/*  6:   */ import org.apache.poi.util.Removal;
/*  7:   */ 
/*  8:   */ /**
/*  9:   */  * @deprecated
/* 10:   */  */
/* 11:   */ @Removal(version="3.18")
/* 12:   */ public class MutablePropertySet
/* 13:   */   extends PropertySet
/* 14:   */ {
/* 15:   */   public MutablePropertySet() {}
/* 16:   */   
/* 17:   */   public MutablePropertySet(PropertySet ps)
/* 18:   */   {
/* 19:39 */     super(ps);
/* 20:   */   }
/* 21:   */   
/* 22:   */   MutablePropertySet(InputStream stream)
/* 23:   */     throws NoPropertySetStreamException, MarkUnsupportedException, IOException, UnsupportedEncodingException
/* 24:   */   {
/* 25:44 */     super(stream);
/* 26:   */   }
/* 27:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.MutablePropertySet
 * JD-Core Version:    0.7.0.1
 */