/*  1:   */ package org.apache.poi.ss.usermodel;
/*  2:   */ 
/*  3:   */ import java.util.regex.Pattern;
/*  4:   */ 
/*  5:   */ public abstract interface Table
/*  6:   */ {
/*  7:34 */   public static final Pattern isStructuredReference = Pattern.compile("[a-zA-Z_\\\\][a-zA-Z0-9._]*\\[.*\\]");
/*  8:   */   
/*  9:   */   public abstract int getStartColIndex();
/* 10:   */   
/* 11:   */   public abstract int getStartRowIndex();
/* 12:   */   
/* 13:   */   public abstract int getEndColIndex();
/* 14:   */   
/* 15:   */   public abstract int getEndRowIndex();
/* 16:   */   
/* 17:   */   public abstract String getName();
/* 18:   */   
/* 19:   */   public abstract String getStyleName();
/* 20:   */   
/* 21:   */   public abstract int findColumnIndex(String paramString);
/* 22:   */   
/* 23:   */   public abstract String getSheetName();
/* 24:   */   
/* 25:   */   public abstract boolean isHasTotalsRow();
/* 26:   */   
/* 27:   */   public abstract int getTotalsRowCount();
/* 28:   */   
/* 29:   */   public abstract int getHeaderRowCount();
/* 30:   */   
/* 31:   */   public abstract TableStyleInfo getStyle();
/* 32:   */   
/* 33:   */   public abstract boolean contains(Cell paramCell);
/* 34:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.Table
 * JD-Core Version:    0.7.0.1
 */