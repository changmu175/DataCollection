/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ 
/*   5:    */ public final class MMSRecord
/*   6:    */   extends StandardRecord
/*   7:    */ {
/*   8:    */   public static final short sid = 193;
/*   9:    */   private byte field_1_addMenuCount;
/*  10:    */   private byte field_2_delMenuCount;
/*  11:    */   
/*  12:    */   public MMSRecord() {}
/*  13:    */   
/*  14:    */   public MMSRecord(RecordInputStream in)
/*  15:    */   {
/*  16: 46 */     if (in.remaining() == 0) {
/*  17: 47 */       return;
/*  18:    */     }
/*  19: 50 */     this.field_1_addMenuCount = in.readByte();
/*  20: 51 */     this.field_2_delMenuCount = in.readByte();
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setAddMenuCount(byte am)
/*  24:    */   {
/*  25: 61 */     this.field_1_addMenuCount = am;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setDelMenuCount(byte dm)
/*  29:    */   {
/*  30: 71 */     this.field_2_delMenuCount = dm;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public byte getAddMenuCount()
/*  34:    */   {
/*  35: 81 */     return this.field_1_addMenuCount;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public byte getDelMenuCount()
/*  39:    */   {
/*  40: 91 */     return this.field_2_delMenuCount;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public String toString()
/*  44:    */   {
/*  45: 96 */     StringBuffer buffer = new StringBuffer();
/*  46:    */     
/*  47: 98 */     buffer.append("[MMS]\n");
/*  48: 99 */     buffer.append("    .addMenu        = ").append(Integer.toHexString(getAddMenuCount())).append("\n");
/*  49:    */     
/*  50:101 */     buffer.append("    .delMenu        = ").append(Integer.toHexString(getDelMenuCount())).append("\n");
/*  51:    */     
/*  52:103 */     buffer.append("[/MMS]\n");
/*  53:104 */     return buffer.toString();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void serialize(LittleEndianOutput out)
/*  57:    */   {
/*  58:108 */     out.writeByte(getAddMenuCount());
/*  59:109 */     out.writeByte(getDelMenuCount());
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected int getDataSize()
/*  63:    */   {
/*  64:113 */     return 2;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public short getSid()
/*  68:    */   {
/*  69:118 */     return 193;
/*  70:    */   }
/*  71:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.MMSRecord
 * JD-Core Version:    0.7.0.1
 */