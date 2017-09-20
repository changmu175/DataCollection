/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.util.NumberToTextConverter;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class NumberRecord
/*  7:   */   extends CellRecord
/*  8:   */   implements Cloneable
/*  9:   */ {
/* 10:   */   public static final short sid = 515;
/* 11:   */   private double field_4_value;
/* 12:   */   
/* 13:   */   public NumberRecord() {}
/* 14:   */   
/* 15:   */   public NumberRecord(RecordInputStream in)
/* 16:   */   {
/* 17:42 */     super(in);
/* 18:43 */     this.field_4_value = in.readDouble();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void setValue(double value)
/* 22:   */   {
/* 23:52 */     this.field_4_value = value;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public double getValue()
/* 27:   */   {
/* 28:61 */     return this.field_4_value;
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected String getRecordName()
/* 32:   */   {
/* 33:66 */     return "NUMBER";
/* 34:   */   }
/* 35:   */   
/* 36:   */   protected void appendValueText(StringBuilder sb)
/* 37:   */   {
/* 38:71 */     sb.append("  .value= ").append(NumberToTextConverter.toText(this.field_4_value));
/* 39:   */   }
/* 40:   */   
/* 41:   */   protected void serializeValue(LittleEndianOutput out)
/* 42:   */   {
/* 43:76 */     out.writeDouble(getValue());
/* 44:   */   }
/* 45:   */   
/* 46:   */   protected int getValueDataSize()
/* 47:   */   {
/* 48:81 */     return 8;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public short getSid()
/* 52:   */   {
/* 53:86 */     return 515;
/* 54:   */   }
/* 55:   */   
/* 56:   */   public NumberRecord clone()
/* 57:   */   {
/* 58:91 */     NumberRecord rec = new NumberRecord();
/* 59:92 */     copyBaseFields(rec);
/* 60:93 */     rec.field_4_value = this.field_4_value;
/* 61:94 */     return rec;
/* 62:   */   }
/* 63:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.NumberRecord
 * JD-Core Version:    0.7.0.1
 */