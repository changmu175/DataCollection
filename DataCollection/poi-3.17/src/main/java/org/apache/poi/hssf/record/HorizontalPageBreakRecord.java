/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import java.util.Iterator;
/*  4:   */ 
/*  5:   */ public final class HorizontalPageBreakRecord
/*  6:   */   extends PageBreakRecord
/*  7:   */   implements Cloneable
/*  8:   */ {
/*  9:   */   public static final short sid = 27;
/* 10:   */   
/* 11:   */   public HorizontalPageBreakRecord() {}
/* 12:   */   
/* 13:   */   public HorizontalPageBreakRecord(RecordInputStream in)
/* 14:   */   {
/* 15:41 */     super(in);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public short getSid()
/* 19:   */   {
/* 20:45 */     return 27;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public PageBreakRecord clone()
/* 24:   */   {
/* 25:50 */     PageBreakRecord result = new HorizontalPageBreakRecord();
/* 26:51 */     Iterator<Break> iterator = getBreaksIterator();
/* 27:52 */     while (iterator.hasNext())
/* 28:   */     {
/* 29:53 */       Break original = (Break)iterator.next();
/* 30:54 */       result.addBreak(original.main, original.subFrom, original.subTo);
/* 31:   */     }
/* 32:56 */     return result;
/* 33:   */   }
/* 34:   */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.HorizontalPageBreakRecord

 * JD-Core Version:    0.7.0.1

 */