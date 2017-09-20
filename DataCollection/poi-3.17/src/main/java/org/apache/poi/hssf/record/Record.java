/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayInputStream;
/*  4:   */ 
/*  5:   */ public abstract class Record
/*  6:   */   extends RecordBase
/*  7:   */ {
/*  8:   */   public final byte[] serialize()
/*  9:   */   {
/* 10:39 */     byte[] retval = new byte[getRecordSize()];
/* 11:   */     
/* 12:41 */     serialize(0, retval);
/* 13:42 */     return retval;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public String toString()
/* 17:   */   {
/* 18:50 */     return super.toString();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public abstract short getSid();
/* 22:   */   
/* 23:   */   public Object clone()
/* 24:   */     throws CloneNotSupportedException
/* 25:   */   {
/* 26:62 */     throw new CloneNotSupportedException("The class " + getClass().getName() + " needs to define a clone method");
/* 27:   */   }
/* 28:   */   
/* 29:   */   public Record cloneViaReserialise()
/* 30:   */   {
/* 31:79 */     byte[] b = serialize();
/* 32:80 */     RecordInputStream rinp = new RecordInputStream(new ByteArrayInputStream(b));
/* 33:81 */     rinp.nextRecord();
/* 34:   */     
/* 35:83 */     Record[] r = RecordFactory.createRecord(rinp);
/* 36:84 */     if (r.length != 1) {
/* 37:85 */       throw new IllegalStateException("Re-serialised a record to clone it, but got " + r.length + " records back!");
/* 38:   */     }
/* 39:87 */     return r[0];
/* 40:   */   }
/* 41:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.Record
 * JD-Core Version:    0.7.0.1
 */