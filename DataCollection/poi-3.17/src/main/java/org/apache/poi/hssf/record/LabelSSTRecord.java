/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.HexDump;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class LabelSSTRecord
/*  7:   */   extends CellRecord
/*  8:   */   implements Cloneable
/*  9:   */ {
/* 10:   */   public static final short sid = 253;
/* 11:   */   private int field_4_sst_index;
/* 12:   */   
/* 13:   */   public LabelSSTRecord() {}
/* 14:   */   
/* 15:   */   public LabelSSTRecord(RecordInputStream in)
/* 16:   */   {
/* 17:37 */     super(in);
/* 18:38 */     this.field_4_sst_index = in.readInt();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void setSSTIndex(int index)
/* 22:   */   {
/* 23:48 */     this.field_4_sst_index = index;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public int getSSTIndex()
/* 27:   */   {
/* 28:59 */     return this.field_4_sst_index;
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected String getRecordName()
/* 32:   */   {
/* 33:64 */     return "LABELSST";
/* 34:   */   }
/* 35:   */   
/* 36:   */   protected void appendValueText(StringBuilder sb)
/* 37:   */   {
/* 38:69 */     sb.append("  .sstIndex = ");
/* 39:70 */     sb.append(HexDump.shortToHex(getSSTIndex()));
/* 40:   */   }
/* 41:   */   
/* 42:   */   protected void serializeValue(LittleEndianOutput out)
/* 43:   */   {
/* 44:74 */     out.writeInt(getSSTIndex());
/* 45:   */   }
/* 46:   */   
/* 47:   */   protected int getValueDataSize()
/* 48:   */   {
/* 49:79 */     return 4;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public short getSid()
/* 53:   */   {
/* 54:84 */     return 253;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public LabelSSTRecord clone()
/* 58:   */   {
/* 59:89 */     LabelSSTRecord rec = new LabelSSTRecord();
/* 60:90 */     copyBaseFields(rec);
/* 61:91 */     rec.field_4_sst_index = this.field_4_sst_index;
/* 62:92 */     return rec;
/* 63:   */   }
/* 64:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.LabelSSTRecord
 * JD-Core Version:    0.7.0.1
 */