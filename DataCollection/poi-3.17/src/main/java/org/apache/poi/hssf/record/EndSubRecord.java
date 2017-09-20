/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianInput;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ import org.apache.poi.util.RecordFormatException;
/*  6:   */ 
/*  7:   */ public final class EndSubRecord
/*  8:   */   extends SubRecord
/*  9:   */   implements Cloneable
/* 10:   */ {
/* 11:   */   public static final short sid = 0;
/* 12:   */   private static final int ENCODED_SIZE = 0;
/* 13:   */   
/* 14:   */   public EndSubRecord() {}
/* 15:   */   
/* 16:   */   public EndSubRecord(LittleEndianInput in, int size)
/* 17:   */   {
/* 18:44 */     if ((size & 0xFF) != 0) {
/* 19:45 */       throw new RecordFormatException("Unexpected size (" + size + ")");
/* 20:   */     }
/* 21:   */   }
/* 22:   */   
/* 23:   */   public boolean isTerminating()
/* 24:   */   {
/* 25:51 */     return true;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String toString()
/* 29:   */   {
/* 30:56 */     StringBuffer buffer = new StringBuffer();
/* 31:   */     
/* 32:58 */     buffer.append("[ftEnd]\n");
/* 33:   */     
/* 34:60 */     buffer.append("[/ftEnd]\n");
/* 35:61 */     return buffer.toString();
/* 36:   */   }
/* 37:   */   
/* 38:   */   public void serialize(LittleEndianOutput out)
/* 39:   */   {
/* 40:65 */     out.writeShort(0);
/* 41:66 */     out.writeShort(0);
/* 42:   */   }
/* 43:   */   
/* 44:   */   protected int getDataSize()
/* 45:   */   {
/* 46:70 */     return 0;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public short getSid()
/* 50:   */   {
/* 51:75 */     return 0;
/* 52:   */   }
/* 53:   */   
/* 54:   */   public EndSubRecord clone()
/* 55:   */   {
/* 56:80 */     EndSubRecord rec = new EndSubRecord();
/* 57:   */     
/* 58:82 */     return rec;
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.EndSubRecord
 * JD-Core Version:    0.7.0.1
 */