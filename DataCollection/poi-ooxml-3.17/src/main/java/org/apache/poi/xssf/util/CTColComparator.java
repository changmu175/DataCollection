/*  1:   */ package org.apache.poi.xssf.util;
/*  2:   */ 
/*  3:   */ import java.util.Comparator;
/*  4:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;
/*  5:   */ 
/*  6:   */ public class CTColComparator
/*  7:   */ {
/*  8:28 */   public static final Comparator<CTCol> BY_MAX = new Comparator()
/*  9:   */   {
/* 10:   */     public int compare(CTCol col1, CTCol col2)
/* 11:   */     {
/* 12:31 */       long col1max = col1.getMax();
/* 13:32 */       long col2max = col2.getMax();
/* 14:33 */       return col1max > col2max ? 1 : col1max < col2max ? -1 : 0;
/* 15:   */     }
/* 16:   */   };
/* 17:37 */   public static final Comparator<CTCol> BY_MIN_MAX = new Comparator()
/* 18:   */   {
/* 19:   */     public int compare(CTCol col1, CTCol col2)
/* 20:   */     {
/* 21:40 */       long col11min = col1.getMin();
/* 22:41 */       long col2min = col2.getMin();
/* 23:42 */       return col11min > col2min ? 1 : col11min < col2min ? -1 : CTColComparator.BY_MAX.compare(col1, col2);
/* 24:   */     }
/* 25:   */   };
/* 26:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.util.CTColComparator
 * JD-Core Version:    0.7.0.1
 */