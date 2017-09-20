/*  1:   */ package org.apache.poi.ss.formula.udf;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.atp.AnalysisToolPak;
/*  4:   */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/*  5:   */ 
/*  6:   */ public abstract interface UDFFinder
/*  7:   */ {
/*  8:   */   @Deprecated
/*  9:35 */   public static final UDFFinder DEFAULT = new AggregatingUDFFinder(new UDFFinder[] { AnalysisToolPak.instance });
/* 10:   */   
/* 11:   */   public abstract FreeRefFunction findFunction(String paramString);
/* 12:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.udf.UDFFinder
 * JD-Core Version:    0.7.0.1
 */