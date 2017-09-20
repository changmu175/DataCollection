/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.util.CellRangeAddress;
/*  4:   */ 
/*  5:   */ public final class CFHeaderRecord
/*  6:   */   extends CFHeaderBase
/*  7:   */   implements Cloneable
/*  8:   */ {
/*  9:   */   public static final short sid = 432;
/* 10:   */   
/* 11:   */   public CFHeaderRecord()
/* 12:   */   {
/* 13:32 */     createEmpty();
/* 14:   */   }
/* 15:   */   
/* 16:   */   public CFHeaderRecord(CellRangeAddress[] regions, int nRules)
/* 17:   */   {
/* 18:35 */     super(regions, nRules);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public CFHeaderRecord(RecordInputStream in)
/* 22:   */   {
/* 23:39 */     read(in);
/* 24:   */   }
/* 25:   */   
/* 26:   */   protected String getRecordName()
/* 27:   */   {
/* 28:43 */     return "CFHEADER";
/* 29:   */   }
/* 30:   */   
/* 31:   */   public short getSid()
/* 32:   */   {
/* 33:47 */     return 432;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public CFHeaderRecord clone()
/* 37:   */   {
/* 38:52 */     CFHeaderRecord result = new CFHeaderRecord();
/* 39:53 */     super.copyTo(result);
/* 40:54 */     return result;
/* 41:   */   }
/* 42:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.CFHeaderRecord
 * JD-Core Version:    0.7.0.1
 */