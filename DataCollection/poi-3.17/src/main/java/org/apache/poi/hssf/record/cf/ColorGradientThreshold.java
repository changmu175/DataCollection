/*  1:   */ package org.apache.poi.hssf.record.cf;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianInput;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class ColorGradientThreshold
/*  7:   */   extends Threshold
/*  8:   */   implements Cloneable
/*  9:   */ {
/* 10:   */   private double position;
/* 11:   */   
/* 12:   */   public ColorGradientThreshold()
/* 13:   */   {
/* 14:32 */     this.position = 0.0D;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public ColorGradientThreshold(LittleEndianInput in)
/* 18:   */   {
/* 19:37 */     super(in);
/* 20:38 */     this.position = in.readDouble();
/* 21:   */   }
/* 22:   */   
/* 23:   */   public double getPosition()
/* 24:   */   {
/* 25:42 */     return this.position;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void setPosition(double position)
/* 29:   */   {
/* 30:45 */     this.position = position;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public int getDataLength()
/* 34:   */   {
/* 35:49 */     return super.getDataLength() + 8;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public ColorGradientThreshold clone()
/* 39:   */   {
/* 40:54 */     ColorGradientThreshold rec = new ColorGradientThreshold();
/* 41:55 */     super.copyTo(rec);
/* 42:56 */     rec.position = this.position;
/* 43:57 */     return rec;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void serialize(LittleEndianOutput out)
/* 47:   */   {
/* 48:61 */     super.serialize(out);
/* 49:62 */     out.writeDouble(this.position);
/* 50:   */   }
/* 51:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.cf.ColorGradientThreshold
 * JD-Core Version:    0.7.0.1
 */