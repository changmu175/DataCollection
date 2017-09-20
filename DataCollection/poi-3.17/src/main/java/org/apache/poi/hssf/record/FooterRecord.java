/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ public final class FooterRecord
/*  4:   */   extends HeaderFooterBase
/*  5:   */   implements Cloneable
/*  6:   */ {
/*  7:   */   public static final short sid = 21;
/*  8:   */   
/*  9:   */   public FooterRecord(String text)
/* 10:   */   {
/* 11:29 */     super(text);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public FooterRecord(RecordInputStream in)
/* 15:   */   {
/* 16:33 */     super(in);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public String toString()
/* 20:   */   {
/* 21:37 */     StringBuffer buffer = new StringBuffer();
/* 22:   */     
/* 23:39 */     buffer.append("[FOOTER]\n");
/* 24:40 */     buffer.append("    .footer = ").append(getText()).append("\n");
/* 25:41 */     buffer.append("[/FOOTER]\n");
/* 26:42 */     return buffer.toString();
/* 27:   */   }
/* 28:   */   
/* 29:   */   public short getSid()
/* 30:   */   {
/* 31:46 */     return 21;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public FooterRecord clone()
/* 35:   */   {
/* 36:51 */     return new FooterRecord(getText());
/* 37:   */   }
/* 38:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.FooterRecord
 * JD-Core Version:    0.7.0.1
 */