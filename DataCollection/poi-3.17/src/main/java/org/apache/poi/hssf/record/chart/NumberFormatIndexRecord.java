/*  1:   */ package org.apache.poi.hssf.record.chart;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class NumberFormatIndexRecord
/*  9:   */   extends StandardRecord
/* 10:   */   implements Cloneable
/* 11:   */ {
/* 12:   */   public static final short sid = 4174;
/* 13:   */   private short field_1_formatIndex;
/* 14:   */   
/* 15:   */   public NumberFormatIndexRecord() {}
/* 16:   */   
/* 17:   */   public NumberFormatIndexRecord(RecordInputStream in)
/* 18:   */   {
/* 19:40 */     this.field_1_formatIndex = in.readShort();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public String toString()
/* 23:   */   {
/* 24:45 */     StringBuffer buffer = new StringBuffer();
/* 25:   */     
/* 26:47 */     buffer.append("[IFMT]\n");
/* 27:48 */     buffer.append("    .formatIndex          = ").append("0x").append(HexDump.toHex(getFormatIndex())).append(" (").append(getFormatIndex()).append(" )");
/* 28:   */     
/* 29:   */ 
/* 30:51 */     buffer.append(System.getProperty("line.separator"));
/* 31:   */     
/* 32:53 */     buffer.append("[/IFMT]\n");
/* 33:54 */     return buffer.toString();
/* 34:   */   }
/* 35:   */   
/* 36:   */   public void serialize(LittleEndianOutput out)
/* 37:   */   {
/* 38:58 */     out.writeShort(this.field_1_formatIndex);
/* 39:   */   }
/* 40:   */   
/* 41:   */   protected int getDataSize()
/* 42:   */   {
/* 43:62 */     return 2;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public short getSid()
/* 47:   */   {
/* 48:67 */     return 4174;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public NumberFormatIndexRecord clone()
/* 52:   */   {
/* 53:72 */     NumberFormatIndexRecord rec = new NumberFormatIndexRecord();
/* 54:   */     
/* 55:74 */     rec.field_1_formatIndex = this.field_1_formatIndex;
/* 56:75 */     return rec;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public short getFormatIndex()
/* 60:   */   {
/* 61:86 */     return this.field_1_formatIndex;
/* 62:   */   }
/* 63:   */   
/* 64:   */   public void setFormatIndex(short field_1_formatIndex)
/* 65:   */   {
/* 66:94 */     this.field_1_formatIndex = field_1_formatIndex;
/* 67:   */   }
/* 68:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.NumberFormatIndexRecord
 * JD-Core Version:    0.7.0.1
 */