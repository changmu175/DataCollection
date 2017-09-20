/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.constant.ConstantValueParser;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class CRNRecord
/*  7:   */   extends StandardRecord
/*  8:   */ {
/*  9:   */   public static final short sid = 90;
/* 10:   */   private int field_1_last_column_index;
/* 11:   */   private int field_2_first_column_index;
/* 12:   */   private int field_3_row_index;
/* 13:   */   private Object[] field_4_constant_values;
/* 14:   */   
/* 15:   */   public CRNRecord()
/* 16:   */   {
/* 17:37 */     throw new RuntimeException("incomplete code");
/* 18:   */   }
/* 19:   */   
/* 20:   */   public int getNumberOfCRNs()
/* 21:   */   {
/* 22:41 */     return this.field_1_last_column_index;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public CRNRecord(RecordInputStream in)
/* 26:   */   {
/* 27:46 */     this.field_1_last_column_index = in.readUByte();
/* 28:47 */     this.field_2_first_column_index = in.readUByte();
/* 29:48 */     this.field_3_row_index = in.readShort();
/* 30:49 */     int nValues = this.field_1_last_column_index - this.field_2_first_column_index + 1;
/* 31:50 */     this.field_4_constant_values = ConstantValueParser.parse(in, nValues);
/* 32:   */   }
/* 33:   */   
/* 34:   */   public String toString()
/* 35:   */   {
/* 36:55 */     StringBuffer sb = new StringBuffer();
/* 37:56 */     sb.append(getClass().getName()).append(" [CRN");
/* 38:57 */     sb.append(" rowIx=").append(this.field_3_row_index);
/* 39:58 */     sb.append(" firstColIx=").append(this.field_2_first_column_index);
/* 40:59 */     sb.append(" lastColIx=").append(this.field_1_last_column_index);
/* 41:60 */     sb.append("]");
/* 42:61 */     return sb.toString();
/* 43:   */   }
/* 44:   */   
/* 45:   */   protected int getDataSize()
/* 46:   */   {
/* 47:64 */     return 4 + ConstantValueParser.getEncodedSize(this.field_4_constant_values);
/* 48:   */   }
/* 49:   */   
/* 50:   */   public void serialize(LittleEndianOutput out)
/* 51:   */   {
/* 52:68 */     out.writeByte(this.field_1_last_column_index);
/* 53:69 */     out.writeByte(this.field_2_first_column_index);
/* 54:70 */     out.writeShort(this.field_3_row_index);
/* 55:71 */     ConstantValueParser.encode(out, this.field_4_constant_values);
/* 56:   */   }
/* 57:   */   
/* 58:   */   public short getSid()
/* 59:   */   {
/* 60:78 */     return 90;
/* 61:   */   }
/* 62:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.CRNRecord
 * JD-Core Version:    0.7.0.1
 */