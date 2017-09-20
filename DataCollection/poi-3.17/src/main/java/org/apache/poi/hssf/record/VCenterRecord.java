/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class VCenterRecord
/*  6:   */   extends StandardRecord
/*  7:   */ {
/*  8:   */   public static final short sid = 132;
/*  9:   */   private int field_1_vcenter;
/* 10:   */   
/* 11:   */   public VCenterRecord() {}
/* 12:   */   
/* 13:   */   public VCenterRecord(RecordInputStream in)
/* 14:   */   {
/* 15:41 */     this.field_1_vcenter = in.readShort();
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void setVCenter(boolean hc)
/* 19:   */   {
/* 20:51 */     this.field_1_vcenter = (hc ? 1 : 0);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public boolean getVCenter()
/* 24:   */   {
/* 25:61 */     return this.field_1_vcenter == 1;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String toString()
/* 29:   */   {
/* 30:66 */     StringBuffer buffer = new StringBuffer();
/* 31:   */     
/* 32:68 */     buffer.append("[VCENTER]\n");
/* 33:69 */     buffer.append("    .vcenter        = ").append(getVCenter()).append("\n");
/* 34:   */     
/* 35:71 */     buffer.append("[/VCENTER]\n");
/* 36:72 */     return buffer.toString();
/* 37:   */   }
/* 38:   */   
/* 39:   */   public void serialize(LittleEndianOutput out)
/* 40:   */   {
/* 41:76 */     out.writeShort(this.field_1_vcenter);
/* 42:   */   }
/* 43:   */   
/* 44:   */   protected int getDataSize()
/* 45:   */   {
/* 46:80 */     return 2;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public short getSid()
/* 50:   */   {
/* 51:85 */     return 132;
/* 52:   */   }
/* 53:   */   
/* 54:   */   public Object clone()
/* 55:   */   {
/* 56:89 */     VCenterRecord rec = new VCenterRecord();
/* 57:90 */     rec.field_1_vcenter = this.field_1_vcenter;
/* 58:91 */     return rec;
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.VCenterRecord
 * JD-Core Version:    0.7.0.1
 */