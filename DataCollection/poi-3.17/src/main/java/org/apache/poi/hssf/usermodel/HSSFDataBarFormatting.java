/*  1:   */ package org.apache.poi.hssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.CFRule12Record;
/*  4:   */ import org.apache.poi.hssf.record.cf.DataBarThreshold;
/*  5:   */ import org.apache.poi.ss.usermodel.Color;
/*  6:   */ 
/*  7:   */ public final class HSSFDataBarFormatting
/*  8:   */   implements org.apache.poi.ss.usermodel.DataBarFormatting
/*  9:   */ {
/* 10:   */   private final HSSFSheet sheet;
/* 11:   */   private final CFRule12Record cfRule12Record;
/* 12:   */   private final org.apache.poi.hssf.record.cf.DataBarFormatting databarFormatting;
/* 13:   */   
/* 14:   */   protected HSSFDataBarFormatting(CFRule12Record cfRule12Record, HSSFSheet sheet)
/* 15:   */   {
/* 16:35 */     this.sheet = sheet;
/* 17:36 */     this.cfRule12Record = cfRule12Record;
/* 18:37 */     this.databarFormatting = this.cfRule12Record.getDataBarFormatting();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public boolean isLeftToRight()
/* 22:   */   {
/* 23:41 */     return !this.databarFormatting.isReversed();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void setLeftToRight(boolean ltr)
/* 27:   */   {
/* 28:44 */     this.databarFormatting.setReversed(!ltr);
/* 29:   */   }
/* 30:   */   
/* 31:   */   public int getWidthMin()
/* 32:   */   {
/* 33:48 */     return this.databarFormatting.getPercentMin();
/* 34:   */   }
/* 35:   */   
/* 36:   */   public void setWidthMin(int width)
/* 37:   */   {
/* 38:51 */     this.databarFormatting.setPercentMin((byte)width);
/* 39:   */   }
/* 40:   */   
/* 41:   */   public int getWidthMax()
/* 42:   */   {
/* 43:55 */     return this.databarFormatting.getPercentMax();
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void setWidthMax(int width)
/* 47:   */   {
/* 48:58 */     this.databarFormatting.setPercentMax((byte)width);
/* 49:   */   }
/* 50:   */   
/* 51:   */   public HSSFExtendedColor getColor()
/* 52:   */   {
/* 53:62 */     return new HSSFExtendedColor(this.databarFormatting.getColor());
/* 54:   */   }
/* 55:   */   
/* 56:   */   public void setColor(Color color)
/* 57:   */   {
/* 58:65 */     HSSFExtendedColor hcolor = (HSSFExtendedColor)color;
/* 59:66 */     this.databarFormatting.setColor(hcolor.getExtendedColor());
/* 60:   */   }
/* 61:   */   
/* 62:   */   public HSSFConditionalFormattingThreshold getMinThreshold()
/* 63:   */   {
/* 64:70 */     return new HSSFConditionalFormattingThreshold(this.databarFormatting.getThresholdMin(), this.sheet);
/* 65:   */   }
/* 66:   */   
/* 67:   */   public HSSFConditionalFormattingThreshold getMaxThreshold()
/* 68:   */   {
/* 69:73 */     return new HSSFConditionalFormattingThreshold(this.databarFormatting.getThresholdMax(), this.sheet);
/* 70:   */   }
/* 71:   */   
/* 72:   */   public boolean isIconOnly()
/* 73:   */   {
/* 74:77 */     return this.databarFormatting.isIconOnly();
/* 75:   */   }
/* 76:   */   
/* 77:   */   public void setIconOnly(boolean only)
/* 78:   */   {
/* 79:80 */     this.databarFormatting.setIconOnly(only);
/* 80:   */   }
/* 81:   */   
/* 82:   */   public HSSFConditionalFormattingThreshold createThreshold()
/* 83:   */   {
/* 84:84 */     return new HSSFConditionalFormattingThreshold(new DataBarThreshold(), this.sheet);
/* 85:   */   }
/* 86:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFDataBarFormatting
 * JD-Core Version:    0.7.0.1
 */