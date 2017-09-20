/*  1:   */ package org.apache.poi.ddf;
/*  2:   */ 
/*  3:   */ public class EscherShapePathProperty
/*  4:   */   extends EscherSimpleProperty
/*  5:   */ {
/*  6:   */   public static final int LINE_OF_STRAIGHT_SEGMENTS = 0;
/*  7:   */   public static final int CLOSED_POLYGON = 1;
/*  8:   */   public static final int CURVES = 2;
/*  9:   */   public static final int CLOSED_CURVES = 3;
/* 10:   */   public static final int COMPLEX = 4;
/* 11:   */   
/* 12:   */   public EscherShapePathProperty(short propertyNumber, int shapePath)
/* 13:   */   {
/* 14:35 */     super(propertyNumber, false, false, shapePath);
/* 15:   */   }
/* 16:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherShapePathProperty
 * JD-Core Version:    0.7.0.1
 */