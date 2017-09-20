/*  1:   */ package org.apache.poi.hssf.record.chart;
/*  2:   */ 
/*  3:   */ import java.util.Arrays;
/*  4:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  5:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class SeriesListRecord
/*  9:   */   extends StandardRecord
/* 10:   */ {
/* 11:   */   public static final short sid = 4118;
/* 12:   */   private short[] field_1_seriesNumbers;
/* 13:   */   
/* 14:   */   public SeriesListRecord(short[] seriesNumbers)
/* 15:   */   {
/* 16:39 */     this.field_1_seriesNumbers = (seriesNumbers == null ? null : (short[])seriesNumbers.clone());
/* 17:   */   }
/* 18:   */   
/* 19:   */   public SeriesListRecord(RecordInputStream in)
/* 20:   */   {
/* 21:43 */     int nItems = in.readUShort();
/* 22:44 */     short[] ss = new short[nItems];
/* 23:45 */     for (int i = 0; i < nItems; i++) {
/* 24:46 */       ss[i] = in.readShort();
/* 25:   */     }
/* 26:49 */     this.field_1_seriesNumbers = ss;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String toString()
/* 30:   */   {
/* 31:53 */     StringBuffer buffer = new StringBuffer();
/* 32:   */     
/* 33:55 */     buffer.append("[SERIESLIST]\n");
/* 34:56 */     buffer.append("    .seriesNumbers= ").append(" (").append(Arrays.toString(getSeriesNumbers())).append(" )");
/* 35:57 */     buffer.append("\n");
/* 36:   */     
/* 37:59 */     buffer.append("[/SERIESLIST]\n");
/* 38:60 */     return buffer.toString();
/* 39:   */   }
/* 40:   */   
/* 41:   */   public void serialize(LittleEndianOutput out)
/* 42:   */   {
/* 43:65 */     int nItems = this.field_1_seriesNumbers.length;
/* 44:66 */     out.writeShort(nItems);
/* 45:67 */     for (int i = 0; i < nItems; i++) {
/* 46:68 */       out.writeShort(this.field_1_seriesNumbers[i]);
/* 47:   */     }
/* 48:   */   }
/* 49:   */   
/* 50:   */   protected int getDataSize()
/* 51:   */   {
/* 52:73 */     return this.field_1_seriesNumbers.length * 2 + 2;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public short getSid()
/* 56:   */   {
/* 57:77 */     return 4118;
/* 58:   */   }
/* 59:   */   
/* 60:   */   public Object clone()
/* 61:   */   {
/* 62:81 */     return new SeriesListRecord(this.field_1_seriesNumbers);
/* 63:   */   }
/* 64:   */   
/* 65:   */   public short[] getSeriesNumbers()
/* 66:   */   {
/* 67:88 */     return this.field_1_seriesNumbers;
/* 68:   */   }
/* 69:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.SeriesListRecord
 * JD-Core Version:    0.7.0.1
 */