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
/* 12:   */ public class SpecialPropertySet
/* 13:   */   extends MutablePropertySet
/* 14:   */ {
/* 15:   */   public SpecialPropertySet() {}
/* 16:   */   
/* 17:   */   public SpecialPropertySet(PropertySet ps)
/* 18:   */     throws UnexpectedPropertySetTypeException
/* 19:   */   {
/* 20:42 */     super(ps);
/* 21:   */   }
/* 22:   */   
/* 23:   */   SpecialPropertySet(InputStream stream)
/* 24:   */     throws NoPropertySetStreamException, MarkUnsupportedException, IOException, UnsupportedEncodingException
/* 25:   */   {
/* 26:47 */     super(stream);
/* 27:   */   }
/* 28:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.SpecialPropertySet
 * JD-Core Version:    0.7.0.1
 */