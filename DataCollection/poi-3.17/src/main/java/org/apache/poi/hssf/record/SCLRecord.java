/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.LittleEndianOutput;
/*   5:    */ 
/*   6:    */ public final class SCLRecord
/*   7:    */   extends StandardRecord
/*   8:    */ {
/*   9:    */   public static final short sid = 160;
/*  10:    */   private short field_1_numerator;
/*  11:    */   private short field_2_denominator;
/*  12:    */   
/*  13:    */   public SCLRecord() {}
/*  14:    */   
/*  15:    */   public SCLRecord(RecordInputStream in)
/*  16:    */   {
/*  17: 40 */     this.field_1_numerator = in.readShort();
/*  18: 41 */     this.field_2_denominator = in.readShort();
/*  19:    */   }
/*  20:    */   
/*  21:    */   public String toString()
/*  22:    */   {
/*  23: 47 */     StringBuffer buffer = new StringBuffer();
/*  24:    */     
/*  25: 49 */     buffer.append("[SCL]\n");
/*  26: 50 */     buffer.append("    .numerator            = ").append("0x").append(HexDump.toHex(getNumerator())).append(" (").append(getNumerator()).append(" )");
/*  27:    */     
/*  28:    */ 
/*  29: 53 */     buffer.append(System.getProperty("line.separator"));
/*  30: 54 */     buffer.append("    .denominator          = ").append("0x").append(HexDump.toHex(getDenominator())).append(" (").append(getDenominator()).append(" )");
/*  31:    */     
/*  32:    */ 
/*  33: 57 */     buffer.append(System.getProperty("line.separator"));
/*  34:    */     
/*  35: 59 */     buffer.append("[/SCL]\n");
/*  36: 60 */     return buffer.toString();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void serialize(LittleEndianOutput out)
/*  40:    */   {
/*  41: 65 */     out.writeShort(this.field_1_numerator);
/*  42: 66 */     out.writeShort(this.field_2_denominator);
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected int getDataSize()
/*  46:    */   {
/*  47: 71 */     return 4;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public short getSid()
/*  51:    */   {
/*  52: 77 */     return 160;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public Object clone()
/*  56:    */   {
/*  57: 82 */     SCLRecord rec = new SCLRecord();
/*  58:    */     
/*  59: 84 */     rec.field_1_numerator = this.field_1_numerator;
/*  60: 85 */     rec.field_2_denominator = this.field_2_denominator;
/*  61: 86 */     return rec;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public short getNumerator()
/*  65:    */   {
/*  66: 96 */     return this.field_1_numerator;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setNumerator(short field_1_numerator)
/*  70:    */   {
/*  71:106 */     this.field_1_numerator = field_1_numerator;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public short getDenominator()
/*  75:    */   {
/*  76:116 */     return this.field_2_denominator;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setDenominator(short field_2_denominator)
/*  80:    */   {
/*  81:126 */     this.field_2_denominator = field_2_denominator;
/*  82:    */   }
/*  83:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.SCLRecord
 * JD-Core Version:    0.7.0.1
 */