/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*  5:   */ 
/*  6:   */ @Internal
/*  7:   */ class Currency
/*  8:   */ {
/*  9:   */   private static final int SIZE = 8;
/* 10:26 */   private final byte[] _value = new byte[8];
/* 11:   */   
/* 12:   */   void read(LittleEndianByteArrayInputStream lei)
/* 13:   */   {
/* 14:31 */     lei.readFully(this._value);
/* 15:   */   }
/* 16:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.Currency
 * JD-Core Version:    0.7.0.1
 */