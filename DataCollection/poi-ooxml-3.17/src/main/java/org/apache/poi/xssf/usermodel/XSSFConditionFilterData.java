/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.ConditionFilterData;
/*  4:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule;
/*  5:   */ 
/*  6:   */ public class XSSFConditionFilterData
/*  7:   */   implements ConditionFilterData
/*  8:   */ {
/*  9:   */   private final CTCfRule _cfRule;
/* 10:   */   
/* 11:   */   XSSFConditionFilterData(CTCfRule cfRule)
/* 12:   */   {
/* 13:30 */     this._cfRule = cfRule;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public boolean getAboveAverage()
/* 17:   */   {
/* 18:34 */     return this._cfRule.getAboveAverage();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public boolean getBottom()
/* 22:   */   {
/* 23:38 */     return this._cfRule.getBottom();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public boolean getEqualAverage()
/* 27:   */   {
/* 28:42 */     return this._cfRule.getEqualAverage();
/* 29:   */   }
/* 30:   */   
/* 31:   */   public boolean getPercent()
/* 32:   */   {
/* 33:46 */     return this._cfRule.getPercent();
/* 34:   */   }
/* 35:   */   
/* 36:   */   public long getRank()
/* 37:   */   {
/* 38:50 */     return this._cfRule.getRank();
/* 39:   */   }
/* 40:   */   
/* 41:   */   public int getStdDev()
/* 42:   */   {
/* 43:54 */     return this._cfRule.getStdDev();
/* 44:   */   }
/* 45:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFConditionFilterData
 * JD-Core Version:    0.7.0.1
 */