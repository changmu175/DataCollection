/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.util.RKUtil;
/*   4:    */ import org.apache.poi.util.LittleEndianOutput;
/*   5:    */ 
/*   6:    */ public final class RKRecord
/*   7:    */   extends CellRecord
/*   8:    */ {
/*   9:    */   public static final short sid = 638;
/*  10:    */   public static final short RK_IEEE_NUMBER = 0;
/*  11:    */   public static final short RK_IEEE_NUMBER_TIMES_100 = 1;
/*  12:    */   public static final short RK_INTEGER = 2;
/*  13:    */   public static final short RK_INTEGER_TIMES_100 = 3;
/*  14:    */   private int field_4_rk_number;
/*  15:    */   
/*  16:    */   private RKRecord() {}
/*  17:    */   
/*  18:    */   public RKRecord(RecordInputStream in)
/*  19:    */   {
/*  20: 49 */     super(in);
/*  21: 50 */     this.field_4_rk_number = in.readInt();
/*  22:    */   }
/*  23:    */   
/*  24:    */   public double getRKNumber()
/*  25:    */   {
/*  26: 71 */     return RKUtil.decodeNumber(this.field_4_rk_number);
/*  27:    */   }
/*  28:    */   
/*  29:    */   protected String getRecordName()
/*  30:    */   {
/*  31: 76 */     return "RK";
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected void appendValueText(StringBuilder sb)
/*  35:    */   {
/*  36: 81 */     sb.append("  .value= ").append(getRKNumber());
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected void serializeValue(LittleEndianOutput out)
/*  40:    */   {
/*  41: 86 */     out.writeInt(this.field_4_rk_number);
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected int getValueDataSize()
/*  45:    */   {
/*  46: 91 */     return 4;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public short getSid()
/*  50:    */   {
/*  51: 96 */     return 638;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public Object clone()
/*  55:    */   {
/*  56:101 */     RKRecord rec = new RKRecord();
/*  57:102 */     copyBaseFields(rec);
/*  58:103 */     rec.field_4_rk_number = this.field_4_rk_number;
/*  59:104 */     return rec;
/*  60:    */   }
/*  61:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.RKRecord
 * JD-Core Version:    0.7.0.1
 */