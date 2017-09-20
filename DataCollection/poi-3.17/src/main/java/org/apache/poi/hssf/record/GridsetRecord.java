/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ 
/*   5:    */ public final class GridsetRecord
/*   6:    */   extends StandardRecord
/*   7:    */   implements Cloneable
/*   8:    */ {
/*   9:    */   public static final short sid = 130;
/*  10:    */   public short field_1_gridset_flag;
/*  11:    */   
/*  12:    */   public GridsetRecord() {}
/*  13:    */   
/*  14:    */   public GridsetRecord(RecordInputStream in)
/*  15:    */   {
/*  16: 47 */     this.field_1_gridset_flag = in.readShort();
/*  17:    */   }
/*  18:    */   
/*  19:    */   public void setGridset(boolean gridset)
/*  20:    */   {
/*  21: 58 */     if (gridset == true) {
/*  22: 60 */       this.field_1_gridset_flag = 1;
/*  23:    */     } else {
/*  24: 64 */       this.field_1_gridset_flag = 0;
/*  25:    */     }
/*  26:    */   }
/*  27:    */   
/*  28:    */   public boolean getGridset()
/*  29:    */   {
/*  30: 76 */     return this.field_1_gridset_flag == 1;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String toString()
/*  34:    */   {
/*  35: 81 */     StringBuffer buffer = new StringBuffer();
/*  36:    */     
/*  37: 83 */     buffer.append("[GRIDSET]\n");
/*  38: 84 */     buffer.append("    .gridset        = ").append(getGridset()).append("\n");
/*  39:    */     
/*  40: 86 */     buffer.append("[/GRIDSET]\n");
/*  41: 87 */     return buffer.toString();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void serialize(LittleEndianOutput out)
/*  45:    */   {
/*  46: 91 */     out.writeShort(this.field_1_gridset_flag);
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected int getDataSize()
/*  50:    */   {
/*  51: 95 */     return 2;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public short getSid()
/*  55:    */   {
/*  56:100 */     return 130;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public GridsetRecord clone()
/*  60:    */   {
/*  61:105 */     GridsetRecord rec = new GridsetRecord();
/*  62:106 */     rec.field_1_gridset_flag = this.field_1_gridset_flag;
/*  63:107 */     return rec;
/*  64:    */   }
/*  65:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.GridsetRecord
 * JD-Core Version:    0.7.0.1
 */