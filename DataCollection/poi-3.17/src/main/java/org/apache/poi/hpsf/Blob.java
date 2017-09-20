/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ import org.apache.poi.util.LittleEndianInput;
/*  5:   */ 
/*  6:   */ @Internal
/*  7:   */ class Blob
/*  8:   */ {
/*  9:   */   private byte[] _value;
/* 10:   */   
/* 11:   */   void read(LittleEndianInput lei)
/* 12:   */   {
/* 13:29 */     int size = lei.readInt();
/* 14:30 */     this._value = new byte[size];
/* 15:31 */     if (size > 0) {
/* 16:32 */       lei.readFully(this._value);
/* 17:   */     }
/* 18:   */   }
/* 19:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.Blob
 * JD-Core Version:    0.7.0.1
 */