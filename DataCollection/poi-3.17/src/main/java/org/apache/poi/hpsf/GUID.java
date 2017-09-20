/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*  5:   */ 
/*  6:   */ @Internal
/*  7:   */ class GUID
/*  8:   */ {
/*  9:   */   private int _data1;
/* 10:   */   private short _data2;
/* 11:   */   private short _data3;
/* 12:   */   private long _data4;
/* 13:   */   
/* 14:   */   void read(LittleEndianByteArrayInputStream lei)
/* 15:   */   {
/* 16:32 */     this._data1 = lei.readInt();
/* 17:33 */     this._data2 = lei.readShort();
/* 18:34 */     this._data3 = lei.readShort();
/* 19:35 */     this._data4 = lei.readLong();
/* 20:   */   }
/* 21:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.GUID
 * JD-Core Version:    0.7.0.1
 */