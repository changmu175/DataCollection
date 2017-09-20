/*  1:   */ package org.apache.poi.hssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.CFRule12Record;
/*  4:   */ import org.apache.poi.hssf.record.cf.ColorGradientFormatting;
/*  5:   */ import org.apache.poi.hssf.record.cf.ColorGradientThreshold;
/*  6:   */ import org.apache.poi.hssf.record.cf.Threshold;
/*  7:   */ import org.apache.poi.hssf.record.common.ExtendedColor;
/*  8:   */ import org.apache.poi.ss.usermodel.Color;
/*  9:   */ import org.apache.poi.ss.usermodel.ColorScaleFormatting;
/* 10:   */ import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold;
/* 11:   */ 
/* 12:   */ public final class HSSFColorScaleFormatting
/* 13:   */   implements ColorScaleFormatting
/* 14:   */ {
/* 15:   */   private final HSSFSheet sheet;
/* 16:   */   private final CFRule12Record cfRule12Record;
/* 17:   */   private final ColorGradientFormatting colorFormatting;
/* 18:   */   
/* 19:   */   protected HSSFColorScaleFormatting(CFRule12Record cfRule12Record, HSSFSheet sheet)
/* 20:   */   {
/* 21:38 */     this.sheet = sheet;
/* 22:39 */     this.cfRule12Record = cfRule12Record;
/* 23:40 */     this.colorFormatting = this.cfRule12Record.getColorGradientFormatting();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public int getNumControlPoints()
/* 27:   */   {
/* 28:44 */     return this.colorFormatting.getNumControlPoints();
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void setNumControlPoints(int num)
/* 32:   */   {
/* 33:47 */     this.colorFormatting.setNumControlPoints(num);
/* 34:   */   }
/* 35:   */   
/* 36:   */   public HSSFExtendedColor[] getColors()
/* 37:   */   {
/* 38:51 */     ExtendedColor[] colors = this.colorFormatting.getColors();
/* 39:52 */     HSSFExtendedColor[] hcolors = new HSSFExtendedColor[colors.length];
/* 40:53 */     for (int i = 0; i < colors.length; i++) {
/* 41:54 */       hcolors[i] = new HSSFExtendedColor(colors[i]);
/* 42:   */     }
/* 43:56 */     return hcolors;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void setColors(Color[] colors)
/* 47:   */   {
/* 48:59 */     ExtendedColor[] cr = new ExtendedColor[colors.length];
/* 49:60 */     for (int i = 0; i < colors.length; i++) {
/* 50:61 */       cr[i] = ((HSSFExtendedColor)colors[i]).getExtendedColor();
/* 51:   */     }
/* 52:63 */     this.colorFormatting.setColors(cr);
/* 53:   */   }
/* 54:   */   
/* 55:   */   public HSSFConditionalFormattingThreshold[] getThresholds()
/* 56:   */   {
/* 57:67 */     Threshold[] t = this.colorFormatting.getThresholds();
/* 58:68 */     HSSFConditionalFormattingThreshold[] ht = new HSSFConditionalFormattingThreshold[t.length];
/* 59:69 */     for (int i = 0; i < t.length; i++) {
/* 60:70 */       ht[i] = new HSSFConditionalFormattingThreshold(t[i], this.sheet);
/* 61:   */     }
/* 62:72 */     return ht;
/* 63:   */   }
/* 64:   */   
/* 65:   */   public void setThresholds(ConditionalFormattingThreshold[] thresholds)
/* 66:   */   {
/* 67:76 */     ColorGradientThreshold[] t = new ColorGradientThreshold[thresholds.length];
/* 68:77 */     for (int i = 0; i < t.length; i++)
/* 69:   */     {
/* 70:78 */       HSSFConditionalFormattingThreshold hssfT = (HSSFConditionalFormattingThreshold)thresholds[i];
/* 71:79 */       t[i] = ((ColorGradientThreshold)hssfT.getThreshold());
/* 72:   */     }
/* 73:81 */     this.colorFormatting.setThresholds(t);
/* 74:   */   }
/* 75:   */   
/* 76:   */   public HSSFConditionalFormattingThreshold createThreshold()
/* 77:   */   {
/* 78:85 */     return new HSSFConditionalFormattingThreshold(new ColorGradientThreshold(), this.sheet);
/* 79:   */   }
/* 80:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFColorScaleFormatting
 * JD-Core Version:    0.7.0.1
 */