/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ public final class HeaderRecord
/*  4:   */   extends HeaderFooterBase
/*  5:   */   implements Cloneable
/*  6:   */ {
/*  7:   */   public static final short sid = 20;
/*  8:   */   
/*  9:   */   public HeaderRecord(String text)
/* 10:   */   {
/* 11:32 */     super(text);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public HeaderRecord(RecordInputStream in)
/* 15:   */   {
/* 16:36 */     super(in);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public String toString()
/* 20:   */   {
/* 21:40 */     StringBuffer buffer = new StringBuffer();
/* 22:   */     
/* 23:42 */     buffer.append("[HEADER]\n");
/* 24:43 */     buffer.append("    .header = ").append(getText()).append("\n");
/* 25:44 */     buffer.append("[/HEADER]\n");
/* 26:45 */     return buffer.toString();
/* 27:   */   }
/* 28:   */   
/* 29:   */   public short getSid()
/* 30:   */   {
/* 31:49 */     return 20;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public HeaderRecord clone()
/* 35:   */   {
/* 36:54 */     return new HeaderRecord(getText());
/* 37:   */   }
/* 38:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.HeaderRecord
 * JD-Core Version:    0.7.0.1
 */