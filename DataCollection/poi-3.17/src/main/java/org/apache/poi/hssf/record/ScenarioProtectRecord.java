/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ 
/*   5:    */ public final class ScenarioProtectRecord
/*   6:    */   extends StandardRecord
/*   7:    */ {
/*   8:    */   public static final short sid = 221;
/*   9:    */   private short field_1_protect;
/*  10:    */   
/*  11:    */   public ScenarioProtectRecord() {}
/*  12:    */   
/*  13:    */   public ScenarioProtectRecord(RecordInputStream in)
/*  14:    */   {
/*  15: 47 */     this.field_1_protect = in.readShort();
/*  16:    */   }
/*  17:    */   
/*  18:    */   public void setProtect(boolean protect)
/*  19:    */   {
/*  20: 57 */     if (protect) {
/*  21: 59 */       this.field_1_protect = 1;
/*  22:    */     } else {
/*  23: 63 */       this.field_1_protect = 0;
/*  24:    */     }
/*  25:    */   }
/*  26:    */   
/*  27:    */   public boolean getProtect()
/*  28:    */   {
/*  29: 74 */     return this.field_1_protect == 1;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String toString()
/*  33:    */   {
/*  34: 79 */     StringBuffer buffer = new StringBuffer();
/*  35:    */     
/*  36: 81 */     buffer.append("[SCENARIOPROTECT]\n");
/*  37: 82 */     buffer.append("    .protect         = ").append(getProtect()).append("\n");
/*  38:    */     
/*  39: 84 */     buffer.append("[/SCENARIOPROTECT]\n");
/*  40: 85 */     return buffer.toString();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void serialize(LittleEndianOutput out)
/*  44:    */   {
/*  45: 89 */     out.writeShort(this.field_1_protect);
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected int getDataSize()
/*  49:    */   {
/*  50: 93 */     return 2;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public short getSid()
/*  54:    */   {
/*  55: 98 */     return 221;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public Object clone()
/*  59:    */   {
/*  60:102 */     ScenarioProtectRecord rec = new ScenarioProtectRecord();
/*  61:103 */     rec.field_1_protect = this.field_1_protect;
/*  62:104 */     return rec;
/*  63:    */   }
/*  64:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.ScenarioProtectRecord
 * JD-Core Version:    0.7.0.1
 */