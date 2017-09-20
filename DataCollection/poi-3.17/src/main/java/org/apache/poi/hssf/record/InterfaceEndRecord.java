/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ import org.apache.poi.util.RecordFormatException;
/*  5:   */ 
/*  6:   */ public final class InterfaceEndRecord
/*  7:   */   extends StandardRecord
/*  8:   */ {
/*  9:   */   public static final short sid = 226;
/* 10:33 */   public static final InterfaceEndRecord instance = new InterfaceEndRecord();
/* 11:   */   
/* 12:   */   public static Record create(RecordInputStream in)
/* 13:   */   {
/* 14:40 */     switch (in.remaining())
/* 15:   */     {
/* 16:   */     case 0: 
/* 17:42 */       return instance;
/* 18:   */     case 2: 
/* 19:44 */       return new InterfaceHdrRecord(in);
/* 20:   */     }
/* 21:46 */     throw new RecordFormatException("Invalid record data size: " + in.remaining());
/* 22:   */   }
/* 23:   */   
/* 24:   */   public String toString()
/* 25:   */   {
/* 26:50 */     return "[INTERFACEEND/]\n";
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void serialize(LittleEndianOutput out) {}
/* 30:   */   
/* 31:   */   protected int getDataSize()
/* 32:   */   {
/* 33:58 */     return 0;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public short getSid()
/* 37:   */   {
/* 38:62 */     return 226;
/* 39:   */   }
/* 40:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.InterfaceEndRecord
 * JD-Core Version:    0.7.0.1
 */