/*  1:   */ package org.apache.poi.hssf.record.chart;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class FontIndexRecord
/*  9:   */   extends StandardRecord
/* 10:   */   implements Cloneable
/* 11:   */ {
/* 12:   */   public static final short sid = 4134;
/* 13:   */   private short field_1_fontIndex;
/* 14:   */   
/* 15:   */   public FontIndexRecord() {}
/* 16:   */   
/* 17:   */   public FontIndexRecord(RecordInputStream in)
/* 18:   */   {
/* 19:42 */     this.field_1_fontIndex = in.readShort();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public String toString()
/* 23:   */   {
/* 24:47 */     StringBuffer buffer = new StringBuffer();
/* 25:   */     
/* 26:49 */     buffer.append("[FONTX]\n");
/* 27:50 */     buffer.append("    .fontIndex            = ").append("0x").append(HexDump.toHex(getFontIndex())).append(" (").append(getFontIndex()).append(" )");
/* 28:   */     
/* 29:   */ 
/* 30:53 */     buffer.append(System.getProperty("line.separator"));
/* 31:   */     
/* 32:55 */     buffer.append("[/FONTX]\n");
/* 33:56 */     return buffer.toString();
/* 34:   */   }
/* 35:   */   
/* 36:   */   public void serialize(LittleEndianOutput out)
/* 37:   */   {
/* 38:60 */     out.writeShort(this.field_1_fontIndex);
/* 39:   */   }
/* 40:   */   
/* 41:   */   protected int getDataSize()
/* 42:   */   {
/* 43:64 */     return 2;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public short getSid()
/* 47:   */   {
/* 48:69 */     return 4134;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public FontIndexRecord clone()
/* 52:   */   {
/* 53:74 */     FontIndexRecord rec = new FontIndexRecord();
/* 54:   */     
/* 55:76 */     rec.field_1_fontIndex = this.field_1_fontIndex;
/* 56:77 */     return rec;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public short getFontIndex()
/* 60:   */   {
/* 61:88 */     return this.field_1_fontIndex;
/* 62:   */   }
/* 63:   */   
/* 64:   */   public void setFontIndex(short field_1_fontIndex)
/* 65:   */   {
/* 66:96 */     this.field_1_fontIndex = field_1_fontIndex;
/* 67:   */   }
/* 68:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.FontIndexRecord
 * JD-Core Version:    0.7.0.1
 */