/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class CalcCountRecord
/*  6:   */   extends StandardRecord
/*  7:   */   implements Cloneable
/*  8:   */ {
/*  9:   */   public static final short sid = 12;
/* 10:   */   private short field_1_iterations;
/* 11:   */   
/* 12:   */   public CalcCountRecord() {}
/* 13:   */   
/* 14:   */   public CalcCountRecord(RecordInputStream in)
/* 15:   */   {
/* 16:46 */     this.field_1_iterations = in.readShort();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void setIterations(short iterations)
/* 20:   */   {
/* 21:56 */     this.field_1_iterations = iterations;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public short getIterations()
/* 25:   */   {
/* 26:66 */     return this.field_1_iterations;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String toString()
/* 30:   */   {
/* 31:71 */     StringBuffer buffer = new StringBuffer();
/* 32:   */     
/* 33:73 */     buffer.append("[CALCCOUNT]\n");
/* 34:74 */     buffer.append("    .iterations     = ").append(Integer.toHexString(getIterations())).append("\n");
/* 35:   */     
/* 36:76 */     buffer.append("[/CALCCOUNT]\n");
/* 37:77 */     return buffer.toString();
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void serialize(LittleEndianOutput out)
/* 41:   */   {
/* 42:81 */     out.writeShort(getIterations());
/* 43:   */   }
/* 44:   */   
/* 45:   */   protected int getDataSize()
/* 46:   */   {
/* 47:85 */     return 2;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public short getSid()
/* 51:   */   {
/* 52:90 */     return 12;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public CalcCountRecord clone()
/* 56:   */   {
/* 57:95 */     CalcCountRecord rec = new CalcCountRecord();
/* 58:96 */     rec.field_1_iterations = this.field_1_iterations;
/* 59:97 */     return rec;
/* 60:   */   }
/* 61:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.CalcCountRecord
 * JD-Core Version:    0.7.0.1
 */