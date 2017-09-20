/*  1:   */ package org.apache.poi.hssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.CFRule12Record;
/*  4:   */ import org.apache.poi.hssf.record.cf.IconMultiStateThreshold;
/*  5:   */ import org.apache.poi.hssf.record.cf.Threshold;
/*  6:   */ import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold;
/*  7:   */ import org.apache.poi.ss.usermodel.IconMultiStateFormatting.IconSet;
/*  8:   */ 
/*  9:   */ public final class HSSFIconMultiStateFormatting
/* 10:   */   implements org.apache.poi.ss.usermodel.IconMultiStateFormatting
/* 11:   */ {
/* 12:   */   private final HSSFSheet sheet;
/* 13:   */   private final CFRule12Record cfRule12Record;
/* 14:   */   private final org.apache.poi.hssf.record.cf.IconMultiStateFormatting iconFormatting;
/* 15:   */   
/* 16:   */   protected HSSFIconMultiStateFormatting(CFRule12Record cfRule12Record, HSSFSheet sheet)
/* 17:   */   {
/* 18:36 */     this.sheet = sheet;
/* 19:37 */     this.cfRule12Record = cfRule12Record;
/* 20:38 */     this.iconFormatting = this.cfRule12Record.getMultiStateFormatting();
/* 21:   */   }
/* 22:   */   
/* 23:   */   public IconMultiStateFormatting.IconSet getIconSet()
/* 24:   */   {
/* 25:42 */     return this.iconFormatting.getIconSet();
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void setIconSet(IconMultiStateFormatting.IconSet set)
/* 29:   */   {
/* 30:45 */     this.iconFormatting.setIconSet(set);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public boolean isIconOnly()
/* 34:   */   {
/* 35:49 */     return this.iconFormatting.isIconOnly();
/* 36:   */   }
/* 37:   */   
/* 38:   */   public void setIconOnly(boolean only)
/* 39:   */   {
/* 40:52 */     this.iconFormatting.setIconOnly(only);
/* 41:   */   }
/* 42:   */   
/* 43:   */   public boolean isReversed()
/* 44:   */   {
/* 45:56 */     return this.iconFormatting.isReversed();
/* 46:   */   }
/* 47:   */   
/* 48:   */   public void setReversed(boolean reversed)
/* 49:   */   {
/* 50:59 */     this.iconFormatting.setReversed(reversed);
/* 51:   */   }
/* 52:   */   
/* 53:   */   public HSSFConditionalFormattingThreshold[] getThresholds()
/* 54:   */   {
/* 55:63 */     Threshold[] t = this.iconFormatting.getThresholds();
/* 56:64 */     HSSFConditionalFormattingThreshold[] ht = new HSSFConditionalFormattingThreshold[t.length];
/* 57:65 */     for (int i = 0; i < t.length; i++) {
/* 58:66 */       ht[i] = new HSSFConditionalFormattingThreshold(t[i], this.sheet);
/* 59:   */     }
/* 60:68 */     return ht;
/* 61:   */   }
/* 62:   */   
/* 63:   */   public void setThresholds(ConditionalFormattingThreshold[] thresholds)
/* 64:   */   {
/* 65:72 */     Threshold[] t = new Threshold[thresholds.length];
/* 66:73 */     for (int i = 0; i < t.length; i++) {
/* 67:74 */       t[i] = ((HSSFConditionalFormattingThreshold)thresholds[i]).getThreshold();
/* 68:   */     }
/* 69:76 */     this.iconFormatting.setThresholds(t);
/* 70:   */   }
/* 71:   */   
/* 72:   */   public HSSFConditionalFormattingThreshold createThreshold()
/* 73:   */   {
/* 74:80 */     return new HSSFConditionalFormattingThreshold(new IconMultiStateThreshold(), this.sheet);
/* 75:   */   }
/* 76:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFIconMultiStateFormatting
 * JD-Core Version:    0.7.0.1
 */