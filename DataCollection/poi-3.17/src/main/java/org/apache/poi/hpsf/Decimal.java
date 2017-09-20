/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*  5:   */ 
/*  6:   */ @Internal
/*  7:   */ class Decimal
/*  8:   */ {
/*  9:   */   private short field_1_wReserved;
/* 10:   */   private byte field_2_scale;
/* 11:   */   private byte field_3_sign;
/* 12:   */   private int field_4_hi32;
/* 13:   */   private long field_5_lo64;
/* 14:   */   
/* 15:   */   void read(LittleEndianByteArrayInputStream lei)
/* 16:   */   {
/* 17:36 */     this.field_1_wReserved = lei.readShort();
/* 18:37 */     this.field_2_scale = lei.readByte();
/* 19:38 */     this.field_3_sign = lei.readByte();
/* 20:39 */     this.field_4_hi32 = lei.readInt();
/* 21:40 */     this.field_5_lo64 = lei.readLong();
/* 22:   */   }
/* 23:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.Decimal
 * JD-Core Version:    0.7.0.1
 */