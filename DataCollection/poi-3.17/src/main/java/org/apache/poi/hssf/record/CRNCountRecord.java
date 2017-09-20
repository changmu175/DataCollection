/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class CRNCountRecord
/*  6:   */   extends StandardRecord
/*  7:   */ {
/*  8:   */   public static final short sid = 89;
/*  9:   */   private static final short DATA_SIZE = 4;
/* 10:   */   private int field_1_number_crn_records;
/* 11:   */   private int field_2_sheet_table_index;
/* 12:   */   
/* 13:   */   public CRNCountRecord()
/* 14:   */   {
/* 15:38 */     throw new RuntimeException("incomplete code");
/* 16:   */   }
/* 17:   */   
/* 18:   */   public int getNumberOfCRNs()
/* 19:   */   {
/* 20:42 */     return this.field_1_number_crn_records;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public CRNCountRecord(RecordInputStream in)
/* 24:   */   {
/* 25:47 */     this.field_1_number_crn_records = in.readShort();
/* 26:48 */     if (this.field_1_number_crn_records < 0) {
/* 27:51 */       this.field_1_number_crn_records = ((short)-this.field_1_number_crn_records);
/* 28:   */     }
/* 29:53 */     this.field_2_sheet_table_index = in.readShort();
/* 30:   */   }
/* 31:   */   
/* 32:   */   public String toString()
/* 33:   */   {
/* 34:58 */     StringBuffer sb = new StringBuffer();
/* 35:59 */     sb.append(getClass().getName()).append(" [XCT");
/* 36:60 */     sb.append(" nCRNs=").append(this.field_1_number_crn_records);
/* 37:61 */     sb.append(" sheetIx=").append(this.field_2_sheet_table_index);
/* 38:62 */     sb.append("]");
/* 39:63 */     return sb.toString();
/* 40:   */   }
/* 41:   */   
/* 42:   */   public void serialize(LittleEndianOutput out)
/* 43:   */   {
/* 44:67 */     out.writeShort((short)this.field_1_number_crn_records);
/* 45:68 */     out.writeShort((short)this.field_2_sheet_table_index);
/* 46:   */   }
/* 47:   */   
/* 48:   */   protected int getDataSize()
/* 49:   */   {
/* 50:71 */     return 4;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public short getSid()
/* 54:   */   {
/* 55:78 */     return 89;
/* 56:   */   }
/* 57:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.CRNCountRecord
 * JD-Core Version:    0.7.0.1
 */