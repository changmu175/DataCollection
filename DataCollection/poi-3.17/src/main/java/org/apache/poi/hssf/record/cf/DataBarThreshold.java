/*  1:   */ package org.apache.poi.hssf.record.cf;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianInput;
/*  4:   */ 
/*  5:   */ public final class DataBarThreshold
/*  6:   */   extends Threshold
/*  7:   */   implements Cloneable
/*  8:   */ {
/*  9:   */   public DataBarThreshold() {}
/* 10:   */   
/* 11:   */   public DataBarThreshold(LittleEndianInput in)
/* 12:   */   {
/* 13:33 */     super(in);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public DataBarThreshold clone()
/* 17:   */   {
/* 18:38 */     DataBarThreshold rec = new DataBarThreshold();
/* 19:39 */     super.copyTo(rec);
/* 20:40 */     return rec;
/* 21:   */   }
/* 22:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.cf.DataBarThreshold
 * JD-Core Version:    0.7.0.1
 */