/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ 
/*   5:    */ public final class ObjectProtectRecord
/*   6:    */   extends StandardRecord
/*   7:    */   implements Cloneable
/*   8:    */ {
/*   9:    */   public static final short sid = 99;
/*  10:    */   private short field_1_protect;
/*  11:    */   
/*  12:    */   public ObjectProtectRecord() {}
/*  13:    */   
/*  14:    */   public ObjectProtectRecord(RecordInputStream in)
/*  15:    */   {
/*  16: 44 */     this.field_1_protect = in.readShort();
/*  17:    */   }
/*  18:    */   
/*  19:    */   public void setProtect(boolean protect)
/*  20:    */   {
/*  21: 54 */     if (protect) {
/*  22: 56 */       this.field_1_protect = 1;
/*  23:    */     } else {
/*  24: 60 */       this.field_1_protect = 0;
/*  25:    */     }
/*  26:    */   }
/*  27:    */   
/*  28:    */   public boolean getProtect()
/*  29:    */   {
/*  30: 71 */     return this.field_1_protect == 1;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String toString()
/*  34:    */   {
/*  35: 76 */     StringBuffer buffer = new StringBuffer();
/*  36:    */     
/*  37: 78 */     buffer.append("[SCENARIOPROTECT]\n");
/*  38: 79 */     buffer.append("    .protect         = ").append(getProtect()).append("\n");
/*  39:    */     
/*  40: 81 */     buffer.append("[/SCENARIOPROTECT]\n");
/*  41: 82 */     return buffer.toString();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void serialize(LittleEndianOutput out)
/*  45:    */   {
/*  46: 86 */     out.writeShort(this.field_1_protect);
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected int getDataSize()
/*  50:    */   {
/*  51: 90 */     return 2;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public short getSid()
/*  55:    */   {
/*  56: 95 */     return 99;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public ObjectProtectRecord clone()
/*  60:    */   {
/*  61:100 */     ObjectProtectRecord rec = new ObjectProtectRecord();
/*  62:101 */     rec.field_1_protect = this.field_1_protect;
/*  63:102 */     return rec;
/*  64:    */   }
/*  65:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.ObjectProtectRecord
 * JD-Core Version:    0.7.0.1
 */