/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class DefaultColWidthRecord
/*  6:   */   extends StandardRecord
/*  7:   */   implements Cloneable
/*  8:   */ {
/*  9:   */   public static final short sid = 85;
/* 10:   */   private int field_1_col_width;
/* 11:   */   public static final int DEFAULT_COLUMN_WIDTH = 8;
/* 12:   */   
/* 13:   */   public DefaultColWidthRecord()
/* 14:   */   {
/* 15:42 */     this.field_1_col_width = 8;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public DefaultColWidthRecord(RecordInputStream in)
/* 19:   */   {
/* 20:47 */     this.field_1_col_width = in.readUShort();
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void setColWidth(int width)
/* 24:   */   {
/* 25:57 */     this.field_1_col_width = width;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public int getColWidth()
/* 29:   */   {
/* 30:67 */     return this.field_1_col_width;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public String toString()
/* 34:   */   {
/* 35:72 */     StringBuffer buffer = new StringBuffer();
/* 36:   */     
/* 37:74 */     buffer.append("[DEFAULTCOLWIDTH]\n");
/* 38:75 */     buffer.append("    .colwidth      = ").append(Integer.toHexString(getColWidth())).append("\n");
/* 39:   */     
/* 40:77 */     buffer.append("[/DEFAULTCOLWIDTH]\n");
/* 41:78 */     return buffer.toString();
/* 42:   */   }
/* 43:   */   
/* 44:   */   public void serialize(LittleEndianOutput out)
/* 45:   */   {
/* 46:82 */     out.writeShort(getColWidth());
/* 47:   */   }
/* 48:   */   
/* 49:   */   protected int getDataSize()
/* 50:   */   {
/* 51:86 */     return 2;
/* 52:   */   }
/* 53:   */   
/* 54:   */   public short getSid()
/* 55:   */   {
/* 56:91 */     return 85;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public DefaultColWidthRecord clone()
/* 60:   */   {
/* 61:96 */     DefaultColWidthRecord rec = new DefaultColWidthRecord();
/* 62:97 */     rec.field_1_col_width = this.field_1_col_width;
/* 63:98 */     return rec;
/* 64:   */   }
/* 65:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.DefaultColWidthRecord
 * JD-Core Version:    0.7.0.1
 */