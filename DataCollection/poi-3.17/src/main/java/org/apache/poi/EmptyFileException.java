/*  1:   */ package org.apache.poi;
/*  2:   */ 
/*  3:   */ public class EmptyFileException
/*  4:   */   extends IllegalArgumentException
/*  5:   */ {
/*  6:   */   private static final long serialVersionUID = 1536449292174360166L;
/*  7:   */   
/*  8:   */   public EmptyFileException()
/*  9:   */   {
/* 10:26 */     super("The supplied file was empty (zero bytes long)");
/* 11:   */   }
/* 12:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.EmptyFileException
 * JD-Core Version:    0.7.0.1
 */