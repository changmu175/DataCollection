/*  1:   */ package org.apache.poi.hssf.record.common;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class FeatSmartTag
/*  7:   */   implements SharedFeature
/*  8:   */ {
/*  9:   */   private byte[] data;
/* 10:   */   
/* 11:   */   public FeatSmartTag()
/* 12:   */   {
/* 13:41 */     this.data = new byte[0];
/* 14:   */   }
/* 15:   */   
/* 16:   */   public FeatSmartTag(RecordInputStream in)
/* 17:   */   {
/* 18:45 */     this.data = in.readRemainder();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String toString()
/* 22:   */   {
/* 23:49 */     StringBuffer buffer = new StringBuffer();
/* 24:50 */     buffer.append(" [FEATURE SMART TAGS]\n");
/* 25:51 */     buffer.append(" [/FEATURE SMART TAGS]\n");
/* 26:52 */     return buffer.toString();
/* 27:   */   }
/* 28:   */   
/* 29:   */   public int getDataSize()
/* 30:   */   {
/* 31:56 */     return this.data.length;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void serialize(LittleEndianOutput out)
/* 35:   */   {
/* 36:60 */     out.write(this.data);
/* 37:   */   }
/* 38:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.common.FeatSmartTag
 * JD-Core Version:    0.7.0.1
 */