/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class HCenterRecord
/*  6:   */   extends StandardRecord
/*  7:   */   implements Cloneable
/*  8:   */ {
/*  9:   */   public static final short sid = 131;
/* 10:   */   private short field_1_hcenter;
/* 11:   */   
/* 12:   */   public HCenterRecord() {}
/* 13:   */   
/* 14:   */   public HCenterRecord(RecordInputStream in)
/* 15:   */   {
/* 16:40 */     this.field_1_hcenter = in.readShort();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void setHCenter(boolean hc)
/* 20:   */   {
/* 21:50 */     if (hc == true) {
/* 22:52 */       this.field_1_hcenter = 1;
/* 23:   */     } else {
/* 24:56 */       this.field_1_hcenter = 0;
/* 25:   */     }
/* 26:   */   }
/* 27:   */   
/* 28:   */   public boolean getHCenter()
/* 29:   */   {
/* 30:67 */     return this.field_1_hcenter == 1;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public String toString()
/* 34:   */   {
/* 35:72 */     StringBuffer buffer = new StringBuffer();
/* 36:   */     
/* 37:74 */     buffer.append("[HCENTER]\n");
/* 38:75 */     buffer.append("    .hcenter        = ").append(getHCenter()).append("\n");
/* 39:   */     
/* 40:77 */     buffer.append("[/HCENTER]\n");
/* 41:78 */     return buffer.toString();
/* 42:   */   }
/* 43:   */   
/* 44:   */   public void serialize(LittleEndianOutput out)
/* 45:   */   {
/* 46:82 */     out.writeShort(this.field_1_hcenter);
/* 47:   */   }
/* 48:   */   
/* 49:   */   protected int getDataSize()
/* 50:   */   {
/* 51:86 */     return 2;
/* 52:   */   }
/* 53:   */   
/* 54:   */   public short getSid()
/* 55:   */   {
/* 56:91 */     return 131;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public HCenterRecord clone()
/* 60:   */   {
/* 61:96 */     HCenterRecord rec = new HCenterRecord();
/* 62:97 */     rec.field_1_hcenter = this.field_1_hcenter;
/* 63:98 */     return rec;
/* 64:   */   }
/* 65:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.HCenterRecord
 * JD-Core Version:    0.7.0.1
 */