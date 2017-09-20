/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ 
/*   5:    */ public final class RefModeRecord
/*   6:    */   extends StandardRecord
/*   7:    */ {
/*   8:    */   public static final short sid = 15;
/*   9:    */   public static final short USE_A1_MODE = 1;
/*  10:    */   public static final short USE_R1C1_MODE = 0;
/*  11:    */   private short field_1_mode;
/*  12:    */   
/*  13:    */   public RefModeRecord() {}
/*  14:    */   
/*  15:    */   public RefModeRecord(RecordInputStream in)
/*  16:    */   {
/*  17: 47 */     this.field_1_mode = in.readShort();
/*  18:    */   }
/*  19:    */   
/*  20:    */   public void setMode(short mode)
/*  21:    */   {
/*  22: 60 */     this.field_1_mode = mode;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public short getMode()
/*  26:    */   {
/*  27: 72 */     return this.field_1_mode;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public String toString()
/*  31:    */   {
/*  32: 77 */     StringBuffer buffer = new StringBuffer();
/*  33:    */     
/*  34: 79 */     buffer.append("[REFMODE]\n");
/*  35: 80 */     buffer.append("    .mode           = ").append(Integer.toHexString(getMode())).append("\n");
/*  36:    */     
/*  37: 82 */     buffer.append("[/REFMODE]\n");
/*  38: 83 */     return buffer.toString();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void serialize(LittleEndianOutput out)
/*  42:    */   {
/*  43: 87 */     out.writeShort(getMode());
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected int getDataSize()
/*  47:    */   {
/*  48: 91 */     return 2;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public short getSid()
/*  52:    */   {
/*  53: 96 */     return 15;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public Object clone()
/*  57:    */   {
/*  58:100 */     RefModeRecord rec = new RefModeRecord();
/*  59:101 */     rec.field_1_mode = this.field_1_mode;
/*  60:102 */     return rec;
/*  61:    */   }
/*  62:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.RefModeRecord
 * JD-Core Version:    0.7.0.1
 */