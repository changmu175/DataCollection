/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import java.util.Iterator;
/*  4:   */ 
/*  5:   */ public final class VerticalPageBreakRecord
/*  6:   */   extends PageBreakRecord
/*  7:   */ {
/*  8:   */   public static final short sid = 26;
/*  9:   */   
/* 10:   */   public VerticalPageBreakRecord() {}
/* 11:   */   
/* 12:   */   public VerticalPageBreakRecord(RecordInputStream in)
/* 13:   */   {
/* 14:42 */     super(in);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public short getSid()
/* 18:   */   {
/* 19:46 */     return 26;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Object clone()
/* 23:   */   {
/* 24:50 */     PageBreakRecord result = new VerticalPageBreakRecord();
/* 25:51 */     Iterator<Break> iterator = getBreaksIterator();
/* 26:52 */     while (iterator.hasNext())
/* 27:   */     {
/* 28:53 */       Break original = (Break)iterator.next();
/* 29:54 */       result.addBreak(original.main, original.subFrom, original.subTo);
/* 30:   */     }
/* 31:56 */     return result;
/* 32:   */   }
/* 33:   */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.VerticalPageBreakRecord

 * JD-Core Version:    0.7.0.1

 */