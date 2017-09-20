/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.common.FtrHeader;
/*  4:   */ import org.apache.poi.hssf.record.common.FutureRecord;
/*  5:   */ import org.apache.poi.ss.util.CellRangeAddress;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class CFHeader12Record
/*  9:   */   extends CFHeaderBase
/* 10:   */   implements FutureRecord, Cloneable
/* 11:   */ {
/* 12:   */   public static final short sid = 2169;
/* 13:   */   private FtrHeader futureHeader;
/* 14:   */   
/* 15:   */   public CFHeader12Record()
/* 16:   */   {
/* 17:36 */     createEmpty();
/* 18:37 */     this.futureHeader = new FtrHeader();
/* 19:38 */     this.futureHeader.setRecordType((short)2169);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public CFHeader12Record(CellRangeAddress[] regions, int nRules)
/* 23:   */   {
/* 24:41 */     super(regions, nRules);
/* 25:42 */     this.futureHeader = new FtrHeader();
/* 26:43 */     this.futureHeader.setRecordType((short)2169);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public CFHeader12Record(RecordInputStream in)
/* 30:   */   {
/* 31:46 */     this.futureHeader = new FtrHeader(in);
/* 32:47 */     read(in);
/* 33:   */   }
/* 34:   */   
/* 35:   */   protected String getRecordName()
/* 36:   */   {
/* 37:52 */     return "CFHEADER12";
/* 38:   */   }
/* 39:   */   
/* 40:   */   protected int getDataSize()
/* 41:   */   {
/* 42:56 */     return FtrHeader.getDataSize() + super.getDataSize();
/* 43:   */   }
/* 44:   */   
/* 45:   */   public void serialize(LittleEndianOutput out)
/* 46:   */   {
/* 47:61 */     this.futureHeader.setAssociatedRange(getEnclosingCellRange());
/* 48:   */     
/* 49:63 */     this.futureHeader.serialize(out);
/* 50:   */     
/* 51:65 */     super.serialize(out);
/* 52:   */   }
/* 53:   */   
/* 54:   */   public short getSid()
/* 55:   */   {
/* 56:69 */     return 2169;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public short getFutureRecordType()
/* 60:   */   {
/* 61:73 */     return this.futureHeader.getRecordType();
/* 62:   */   }
/* 63:   */   
/* 64:   */   public FtrHeader getFutureHeader()
/* 65:   */   {
/* 66:76 */     return this.futureHeader;
/* 67:   */   }
/* 68:   */   
/* 69:   */   public CellRangeAddress getAssociatedRange()
/* 70:   */   {
/* 71:79 */     return this.futureHeader.getAssociatedRange();
/* 72:   */   }
/* 73:   */   
/* 74:   */   public CFHeader12Record clone()
/* 75:   */   {
/* 76:84 */     CFHeader12Record result = new CFHeader12Record();
/* 77:85 */     result.futureHeader = ((FtrHeader)this.futureHeader.clone());
/* 78:86 */     super.copyTo(result);
/* 79:87 */     return result;
/* 80:   */   }
/* 81:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.CFHeader12Record
 * JD-Core Version:    0.7.0.1
 */