/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class PrecisionRecord
/*  6:   */   extends StandardRecord
/*  7:   */ {
/*  8:   */   public static final short sid = 14;
/*  9:   */   public short field_1_precision;
/* 10:   */   
/* 11:   */   public PrecisionRecord() {}
/* 12:   */   
/* 13:   */   public PrecisionRecord(RecordInputStream in)
/* 14:   */   {
/* 15:44 */     this.field_1_precision = in.readShort();
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void setFullPrecision(boolean fullprecision)
/* 19:   */   {
/* 20:55 */     if (fullprecision == true) {
/* 21:57 */       this.field_1_precision = 1;
/* 22:   */     } else {
/* 23:61 */       this.field_1_precision = 0;
/* 24:   */     }
/* 25:   */   }
/* 26:   */   
/* 27:   */   public boolean getFullPrecision()
/* 28:   */   {
/* 29:73 */     return this.field_1_precision == 1;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public String toString()
/* 33:   */   {
/* 34:78 */     StringBuffer buffer = new StringBuffer();
/* 35:   */     
/* 36:80 */     buffer.append("[PRECISION]\n");
/* 37:81 */     buffer.append("    .precision       = ").append(getFullPrecision()).append("\n");
/* 38:   */     
/* 39:83 */     buffer.append("[/PRECISION]\n");
/* 40:84 */     return buffer.toString();
/* 41:   */   }
/* 42:   */   
/* 43:   */   public void serialize(LittleEndianOutput out)
/* 44:   */   {
/* 45:88 */     out.writeShort(this.field_1_precision);
/* 46:   */   }
/* 47:   */   
/* 48:   */   protected int getDataSize()
/* 49:   */   {
/* 50:92 */     return 2;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public short getSid()
/* 54:   */   {
/* 55:97 */     return 14;
/* 56:   */   }
/* 57:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.PrecisionRecord
 * JD-Core Version:    0.7.0.1
 */