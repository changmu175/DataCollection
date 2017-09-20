/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class BottomMarginRecord
/*  6:   */   extends StandardRecord
/*  7:   */   implements Margin, Cloneable
/*  8:   */ {
/*  9:   */   public static final short sid = 41;
/* 10:   */   private double field_1_margin;
/* 11:   */   
/* 12:   */   public BottomMarginRecord() {}
/* 13:   */   
/* 14:   */   public BottomMarginRecord(RecordInputStream in)
/* 15:   */   {
/* 16:39 */     this.field_1_margin = in.readDouble();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public String toString()
/* 20:   */   {
/* 21:44 */     StringBuffer buffer = new StringBuffer();
/* 22:45 */     buffer.append("[BottomMargin]\n");
/* 23:46 */     buffer.append("    .margin               = ").append(" (").append(getMargin()).append(" )\n");
/* 24:   */     
/* 25:48 */     buffer.append("[/BottomMargin]\n");
/* 26:49 */     return buffer.toString();
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void serialize(LittleEndianOutput out)
/* 30:   */   {
/* 31:53 */     out.writeDouble(this.field_1_margin);
/* 32:   */   }
/* 33:   */   
/* 34:   */   protected int getDataSize()
/* 35:   */   {
/* 36:57 */     return 8;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public short getSid()
/* 40:   */   {
/* 41:62 */     return 41;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public double getMargin()
/* 45:   */   {
/* 46:70 */     return this.field_1_margin;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public void setMargin(double field_1_margin)
/* 50:   */   {
/* 51:78 */     this.field_1_margin = field_1_margin;
/* 52:   */   }
/* 53:   */   
/* 54:   */   public BottomMarginRecord clone()
/* 55:   */   {
/* 56:83 */     BottomMarginRecord rec = new BottomMarginRecord();
/* 57:84 */     rec.field_1_margin = this.field_1_margin;
/* 58:85 */     return rec;
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.BottomMarginRecord
 * JD-Core Version:    0.7.0.1
 */