/*  1:   */ package org.apache.poi.xssf.usermodel.charts;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.charts.ChartSeries;
/*  4:   */ import org.apache.poi.ss.usermodel.charts.TitleType;
/*  5:   */ import org.apache.poi.ss.util.CellReference;
/*  6:   */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx;
/*  7:   */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx.Factory;
/*  8:   */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef;
/*  9:   */ 
/* 10:   */ public abstract class AbstractXSSFChartSeries
/* 11:   */   implements ChartSeries
/* 12:   */ {
/* 13:   */   private String titleValue;
/* 14:   */   private CellReference titleRef;
/* 15:   */   private TitleType titleType;
/* 16:   */   
/* 17:   */   public void setTitle(CellReference titleReference)
/* 18:   */   {
/* 19:35 */     this.titleType = TitleType.CELL_REFERENCE;
/* 20:36 */     this.titleRef = titleReference;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void setTitle(String title)
/* 24:   */   {
/* 25:40 */     this.titleType = TitleType.STRING;
/* 26:41 */     this.titleValue = title;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public CellReference getTitleCellReference()
/* 30:   */   {
/* 31:45 */     if (TitleType.CELL_REFERENCE.equals(this.titleType)) {
/* 32:46 */       return this.titleRef;
/* 33:   */     }
/* 34:48 */     throw new IllegalStateException("Title type is not CellReference.");
/* 35:   */   }
/* 36:   */   
/* 37:   */   public String getTitleString()
/* 38:   */   {
/* 39:52 */     if (TitleType.STRING.equals(this.titleType)) {
/* 40:53 */       return this.titleValue;
/* 41:   */     }
/* 42:55 */     throw new IllegalStateException("Title type is not String.");
/* 43:   */   }
/* 44:   */   
/* 45:   */   public TitleType getTitleType()
/* 46:   */   {
/* 47:59 */     return this.titleType;
/* 48:   */   }
/* 49:   */   
/* 50:   */   protected boolean isTitleSet()
/* 51:   */   {
/* 52:63 */     return this.titleType != null;
/* 53:   */   }
/* 54:   */   
/* 55:   */   protected CTSerTx getCTSerTx()
/* 56:   */   {
/* 57:67 */     CTSerTx tx = CTSerTx.Factory.newInstance();
/* 58:68 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$charts$TitleType[this.titleType.ordinal()])
/* 59:   */     {
/* 60:   */     case 1: 
/* 61:70 */       tx.addNewStrRef().setF(this.titleRef.formatAsString());
/* 62:71 */       return tx;
/* 63:   */     case 2: 
/* 64:73 */       tx.setV(this.titleValue);
/* 65:74 */       return tx;
/* 66:   */     }
/* 67:76 */     throw new IllegalStateException("Unkown title type: " + this.titleType);
/* 68:   */   }
/* 69:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.charts.AbstractXSSFChartSeries
 * JD-Core Version:    0.7.0.1
 */