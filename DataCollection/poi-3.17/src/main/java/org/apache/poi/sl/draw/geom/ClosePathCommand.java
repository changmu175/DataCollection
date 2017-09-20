/*  1:   */ package org.apache.poi.sl.draw.geom;
/*  2:   */ 
/*  3:   */ import java.awt.geom.Path2D.Double;
/*  4:   */ 
/*  5:   */ public class ClosePathCommand
/*  6:   */   implements PathCommand
/*  7:   */ {
/*  8:   */   public void execute(Path2D.Double path, Context ctx)
/*  9:   */   {
/* 10:31 */     path.closePath();
/* 11:   */   }
/* 12:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.geom.ClosePathCommand
 * JD-Core Version:    0.7.0.1
 */