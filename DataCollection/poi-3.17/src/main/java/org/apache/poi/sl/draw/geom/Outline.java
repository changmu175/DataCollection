/*  1:   */ package org.apache.poi.sl.draw.geom;
/*  2:   */ 
/*  3:   */ import java.awt.Shape;
/*  4:   */ 
/*  5:   */ public class Outline
/*  6:   */ {
/*  7:   */   private Shape shape;
/*  8:   */   private Path path;
/*  9:   */   
/* 10:   */   public Outline(Shape shape, Path path)
/* 11:   */   {
/* 12:29 */     this.shape = shape;
/* 13:30 */     this.path = path;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public Path getPath()
/* 17:   */   {
/* 18:34 */     return this.path;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Shape getOutline()
/* 22:   */   {
/* 23:38 */     return this.shape;
/* 24:   */   }
/* 25:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.geom.Outline
 * JD-Core Version:    0.7.0.1
 */