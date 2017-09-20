/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class SaveRecalcRecord
/*  6:   */   extends StandardRecord
/*  7:   */ {
/*  8:   */   public static final short sid = 95;
/*  9:   */   private short field_1_recalc;
/* 10:   */   
/* 11:   */   public SaveRecalcRecord() {}
/* 12:   */   
/* 13:   */   public SaveRecalcRecord(RecordInputStream in)
/* 14:   */   {
/* 15:45 */     this.field_1_recalc = in.readShort();
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void setRecalc(boolean recalc)
/* 19:   */   {
/* 20:55 */     this.field_1_recalc = ((short)(recalc == true ? 1 : 0));
/* 21:   */   }
/* 22:   */   
/* 23:   */   public boolean getRecalc()
/* 24:   */   {
/* 25:66 */     return this.field_1_recalc == 1;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String toString()
/* 29:   */   {
/* 30:71 */     StringBuffer buffer = new StringBuffer();
/* 31:   */     
/* 32:73 */     buffer.append("[SAVERECALC]\n");
/* 33:74 */     buffer.append("    .recalc         = ").append(getRecalc()).append("\n");
/* 34:   */     
/* 35:76 */     buffer.append("[/SAVERECALC]\n");
/* 36:77 */     return buffer.toString();
/* 37:   */   }
/* 38:   */   
/* 39:   */   public void serialize(LittleEndianOutput out)
/* 40:   */   {
/* 41:81 */     out.writeShort(this.field_1_recalc);
/* 42:   */   }
/* 43:   */   
/* 44:   */   protected int getDataSize()
/* 45:   */   {
/* 46:85 */     return 2;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public short getSid()
/* 50:   */   {
/* 51:90 */     return 95;
/* 52:   */   }
/* 53:   */   
/* 54:   */   public Object clone()
/* 55:   */   {
/* 56:94 */     SaveRecalcRecord rec = new SaveRecalcRecord();
/* 57:95 */     rec.field_1_recalc = this.field_1_recalc;
/* 58:96 */     return rec;
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.SaveRecalcRecord
 * JD-Core Version:    0.7.0.1
 */