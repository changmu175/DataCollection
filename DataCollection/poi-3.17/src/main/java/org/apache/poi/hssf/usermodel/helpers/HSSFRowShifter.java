/*  1:   */ package org.apache.poi.hssf.usermodel.helpers;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.usermodel.HSSFSheet;
/*  4:   */ import org.apache.poi.ss.formula.FormulaShifter;
/*  5:   */ import org.apache.poi.ss.formula.eval.NotImplementedException;
/*  6:   */ import org.apache.poi.ss.usermodel.Row;
/*  7:   */ import org.apache.poi.ss.usermodel.helpers.RowShifter;
/*  8:   */ import org.apache.poi.util.Internal;
/*  9:   */ import org.apache.poi.util.NotImplemented;
/* 10:   */ import org.apache.poi.util.POILogFactory;
/* 11:   */ import org.apache.poi.util.POILogger;
/* 12:   */ 
/* 13:   */ public final class HSSFRowShifter
/* 14:   */   extends RowShifter
/* 15:   */ {
/* 16:36 */   private static final POILogger logger = POILogFactory.getLogger(HSSFRowShifter.class);
/* 17:   */   
/* 18:   */   public HSSFRowShifter(HSSFSheet sh)
/* 19:   */   {
/* 20:39 */     super(sh);
/* 21:   */   }
/* 22:   */   
/* 23:   */   @NotImplemented
/* 24:   */   public void updateNamedRanges(FormulaShifter shifter)
/* 25:   */   {
/* 26:44 */     throw new NotImplementedException("HSSFRowShifter.updateNamedRanges");
/* 27:   */   }
/* 28:   */   
/* 29:   */   @NotImplemented
/* 30:   */   public void updateFormulas(FormulaShifter shifter)
/* 31:   */   {
/* 32:49 */     throw new NotImplementedException("updateFormulas");
/* 33:   */   }
/* 34:   */   
/* 35:   */   @Internal
/* 36:   */   @NotImplemented
/* 37:   */   public void updateRowFormulas(Row row, FormulaShifter shifter)
/* 38:   */   {
/* 39:55 */     throw new NotImplementedException("updateRowFormulas");
/* 40:   */   }
/* 41:   */   
/* 42:   */   @NotImplemented
/* 43:   */   public void updateConditionalFormatting(FormulaShifter shifter)
/* 44:   */   {
/* 45:60 */     throw new NotImplementedException("updateConditionalFormatting");
/* 46:   */   }
/* 47:   */   
/* 48:   */   @NotImplemented
/* 49:   */   public void updateHyperlinks(FormulaShifter shifter)
/* 50:   */   {
/* 51:65 */     throw new NotImplementedException("updateHyperlinks");
/* 52:   */   }
/* 53:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.helpers.HSSFRowShifter
 * JD-Core Version:    0.7.0.1
 */