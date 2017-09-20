/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class DeltaRecord
/*  6:   */   extends StandardRecord
/*  7:   */   implements Cloneable
/*  8:   */ {
/*  9:   */   public static final short sid = 16;
/* 10:   */   public static final double DEFAULT_VALUE = 0.001D;
/* 11:   */   private double field_1_max_change;
/* 12:   */   
/* 13:   */   public DeltaRecord(double maxChange)
/* 14:   */   {
/* 15:36 */     this.field_1_max_change = maxChange;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public DeltaRecord(RecordInputStream in)
/* 19:   */   {
/* 20:40 */     this.field_1_max_change = in.readDouble();
/* 21:   */   }
/* 22:   */   
/* 23:   */   public double getMaxChange()
/* 24:   */   {
/* 25:48 */     return this.field_1_max_change;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String toString()
/* 29:   */   {
/* 30:52 */     StringBuffer buffer = new StringBuffer();
/* 31:   */     
/* 32:54 */     buffer.append("[DELTA]\n");
/* 33:55 */     buffer.append("    .maxchange = ").append(getMaxChange()).append("\n");
/* 34:56 */     buffer.append("[/DELTA]\n");
/* 35:57 */     return buffer.toString();
/* 36:   */   }
/* 37:   */   
/* 38:   */   public void serialize(LittleEndianOutput out)
/* 39:   */   {
/* 40:61 */     out.writeDouble(getMaxChange());
/* 41:   */   }
/* 42:   */   
/* 43:   */   protected int getDataSize()
/* 44:   */   {
/* 45:65 */     return 8;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public short getSid()
/* 49:   */   {
/* 50:69 */     return 16;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public DeltaRecord clone()
/* 54:   */   {
/* 55:75 */     return this;
/* 56:   */   }
/* 57:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.DeltaRecord
 * JD-Core Version:    0.7.0.1
 */