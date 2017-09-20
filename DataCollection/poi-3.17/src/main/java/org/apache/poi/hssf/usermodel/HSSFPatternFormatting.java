/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.CFRuleBase;
/*   4:    */ import org.apache.poi.hssf.util.HSSFColor;
/*   5:    */ import org.apache.poi.ss.usermodel.Color;
/*   6:    */ 
/*   7:    */ public class HSSFPatternFormatting
/*   8:    */   implements org.apache.poi.ss.usermodel.PatternFormatting
/*   9:    */ {
/*  10:    */   private final HSSFWorkbook workbook;
/*  11:    */   private final CFRuleBase cfRuleRecord;
/*  12:    */   private final org.apache.poi.hssf.record.cf.PatternFormatting patternFormatting;
/*  13:    */   
/*  14:    */   protected HSSFPatternFormatting(CFRuleBase cfRuleRecord, HSSFWorkbook workbook)
/*  15:    */   {
/*  16: 34 */     this.workbook = workbook;
/*  17: 35 */     this.cfRuleRecord = cfRuleRecord;
/*  18: 36 */     this.patternFormatting = cfRuleRecord.getPatternFormatting();
/*  19:    */   }
/*  20:    */   
/*  21:    */   protected org.apache.poi.hssf.record.cf.PatternFormatting getPatternFormattingBlock()
/*  22:    */   {
/*  23: 41 */     return this.patternFormatting;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public HSSFColor getFillBackgroundColorColor()
/*  27:    */   {
/*  28: 45 */     return this.workbook.getCustomPalette().getColor(getFillBackgroundColor());
/*  29:    */   }
/*  30:    */   
/*  31:    */   public HSSFColor getFillForegroundColorColor()
/*  32:    */   {
/*  33: 49 */     return this.workbook.getCustomPalette().getColor(getFillForegroundColor());
/*  34:    */   }
/*  35:    */   
/*  36:    */   public short getFillBackgroundColor()
/*  37:    */   {
/*  38: 57 */     return (short)this.patternFormatting.getFillBackgroundColor();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public short getFillForegroundColor()
/*  42:    */   {
/*  43: 65 */     return (short)this.patternFormatting.getFillForegroundColor();
/*  44:    */   }
/*  45:    */   
/*  46:    */   public short getFillPattern()
/*  47:    */   {
/*  48: 73 */     return (short)this.patternFormatting.getFillPattern();
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setFillBackgroundColor(Color bg)
/*  52:    */   {
/*  53: 77 */     HSSFColor hcolor = HSSFColor.toHSSFColor(bg);
/*  54: 78 */     if (hcolor == null) {
/*  55: 79 */       setFillBackgroundColor((short)0);
/*  56:    */     } else {
/*  57: 81 */       setFillBackgroundColor(hcolor.getIndex());
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setFillForegroundColor(Color fg)
/*  62:    */   {
/*  63: 86 */     HSSFColor hcolor = HSSFColor.toHSSFColor(fg);
/*  64: 87 */     if (hcolor == null) {
/*  65: 88 */       setFillForegroundColor((short)0);
/*  66:    */     } else {
/*  67: 90 */       setFillForegroundColor(hcolor.getIndex());
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setFillBackgroundColor(short bg)
/*  72:    */   {
/*  73:100 */     this.patternFormatting.setFillBackgroundColor(bg);
/*  74:101 */     if (bg != 0) {
/*  75:103 */       this.cfRuleRecord.setPatternBackgroundColorModified(true);
/*  76:    */     }
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setFillForegroundColor(short fg)
/*  80:    */   {
/*  81:113 */     this.patternFormatting.setFillForegroundColor(fg);
/*  82:114 */     if (fg != 0) {
/*  83:116 */       this.cfRuleRecord.setPatternColorModified(true);
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setFillPattern(short fp)
/*  88:    */   {
/*  89:126 */     this.patternFormatting.setFillPattern(fp);
/*  90:127 */     if (fp != 0) {
/*  91:129 */       this.cfRuleRecord.setPatternStyleModified(true);
/*  92:    */     }
/*  93:    */   }
/*  94:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFPatternFormatting
 * JD-Core Version:    0.7.0.1
 */