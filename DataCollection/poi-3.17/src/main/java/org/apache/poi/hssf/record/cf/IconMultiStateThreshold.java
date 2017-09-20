/*  1:   */ package org.apache.poi.hssf.record.cf;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianInput;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class IconMultiStateThreshold
/*  7:   */   extends Threshold
/*  8:   */   implements Cloneable
/*  9:   */ {
/* 10:   */   public static final byte EQUALS_EXCLUDE = 0;
/* 11:   */   public static final byte EQUALS_INCLUDE = 1;
/* 12:   */   private byte equals;
/* 13:   */   
/* 14:   */   public IconMultiStateThreshold()
/* 15:   */   {
/* 16:41 */     this.equals = 1;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public IconMultiStateThreshold(LittleEndianInput in)
/* 20:   */   {
/* 21:46 */     super(in);
/* 22:47 */     this.equals = in.readByte();
/* 23:   */     
/* 24:49 */     in.readInt();
/* 25:   */   }
/* 26:   */   
/* 27:   */   public byte getEquals()
/* 28:   */   {
/* 29:53 */     return this.equals;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void setEquals(byte equals)
/* 33:   */   {
/* 34:56 */     this.equals = equals;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public int getDataLength()
/* 38:   */   {
/* 39:60 */     return super.getDataLength() + 5;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public IconMultiStateThreshold clone()
/* 43:   */   {
/* 44:65 */     IconMultiStateThreshold rec = new IconMultiStateThreshold();
/* 45:66 */     super.copyTo(rec);
/* 46:67 */     rec.equals = this.equals;
/* 47:68 */     return rec;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public void serialize(LittleEndianOutput out)
/* 51:   */   {
/* 52:72 */     super.serialize(out);
/* 53:73 */     out.writeByte(this.equals);
/* 54:74 */     out.writeInt(0);
/* 55:   */   }
/* 56:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.cf.IconMultiStateThreshold
 * JD-Core Version:    0.7.0.1
 */