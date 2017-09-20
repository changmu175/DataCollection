/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class RightMarginRecord
/*  6:   */   extends StandardRecord
/*  7:   */   implements Margin
/*  8:   */ {
/*  9:   */   public static final short sid = 39;
/* 10:   */   private double field_1_margin;
/* 11:   */   
/* 12:   */   public RightMarginRecord() {}
/* 13:   */   
/* 14:   */   public RightMarginRecord(RecordInputStream in)
/* 15:   */   {
/* 16:33 */     this.field_1_margin = in.readDouble();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public String toString()
/* 20:   */   {
/* 21:38 */     StringBuffer buffer = new StringBuffer();
/* 22:39 */     buffer.append("[RightMargin]\n");
/* 23:40 */     buffer.append("    .margin               = ").append(" (").append(getMargin()).append(" )\n");
/* 24:41 */     buffer.append("[/RightMargin]\n");
/* 25:42 */     return buffer.toString();
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void serialize(LittleEndianOutput out)
/* 29:   */   {
/* 30:46 */     out.writeDouble(this.field_1_margin);
/* 31:   */   }
/* 32:   */   
/* 33:   */   protected int getDataSize()
/* 34:   */   {
/* 35:50 */     return 8;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public short getSid()
/* 39:   */   {
/* 40:53 */     return 39;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public double getMargin()
/* 44:   */   {
/* 45:58 */     return this.field_1_margin;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public void setMargin(double field_1_margin)
/* 49:   */   {
/* 50:64 */     this.field_1_margin = field_1_margin;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public Object clone()
/* 54:   */   {
/* 55:68 */     RightMarginRecord rec = new RightMarginRecord();
/* 56:69 */     rec.field_1_margin = this.field_1_margin;
/* 57:70 */     return rec;
/* 58:   */   }
/* 59:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.RightMarginRecord
 * JD-Core Version:    0.7.0.1
 */