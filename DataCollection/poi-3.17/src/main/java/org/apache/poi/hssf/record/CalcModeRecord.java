/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ 
/*   5:    */ public final class CalcModeRecord
/*   6:    */   extends StandardRecord
/*   7:    */   implements Cloneable
/*   8:    */ {
/*   9:    */   public static final short sid = 13;
/*  10:    */   public static final short MANUAL = 0;
/*  11:    */   public static final short AUTOMATIC = 1;
/*  12:    */   public static final short AUTOMATIC_EXCEPT_TABLES = -1;
/*  13:    */   private short field_1_calcmode;
/*  14:    */   
/*  15:    */   public CalcModeRecord() {}
/*  16:    */   
/*  17:    */   public CalcModeRecord(RecordInputStream in)
/*  18:    */   {
/*  19: 64 */     this.field_1_calcmode = in.readShort();
/*  20:    */   }
/*  21:    */   
/*  22:    */   public void setCalcMode(short calcmode)
/*  23:    */   {
/*  24: 79 */     this.field_1_calcmode = calcmode;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public short getCalcMode()
/*  28:    */   {
/*  29: 94 */     return this.field_1_calcmode;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String toString()
/*  33:    */   {
/*  34: 99 */     StringBuffer buffer = new StringBuffer();
/*  35:    */     
/*  36:101 */     buffer.append("[CALCMODE]\n");
/*  37:102 */     buffer.append("    .calcmode       = ").append(Integer.toHexString(getCalcMode())).append("\n");
/*  38:    */     
/*  39:104 */     buffer.append("[/CALCMODE]\n");
/*  40:105 */     return buffer.toString();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void serialize(LittleEndianOutput out)
/*  44:    */   {
/*  45:109 */     out.writeShort(getCalcMode());
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected int getDataSize()
/*  49:    */   {
/*  50:113 */     return 2;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public short getSid()
/*  54:    */   {
/*  55:118 */     return 13;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public CalcModeRecord clone()
/*  59:    */   {
/*  60:123 */     CalcModeRecord rec = new CalcModeRecord();
/*  61:124 */     rec.field_1_calcmode = this.field_1_calcmode;
/*  62:125 */     return rec;
/*  63:    */   }
/*  64:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.CalcModeRecord
 * JD-Core Version:    0.7.0.1
 */