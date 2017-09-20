/*  1:   */ package org.apache.poi.sl.draw.geom;
/*  2:   */ 
/*  3:   */ import java.awt.geom.Path2D.Double;
/*  4:   */ import org.apache.poi.sl.draw.binding.CTAdjPoint2D;
/*  5:   */ 
/*  6:   */ public class QuadToCommand
/*  7:   */   implements PathCommand
/*  8:   */ {
/*  9:   */   private String arg1;
/* 10:   */   private String arg2;
/* 11:   */   private String arg3;
/* 12:   */   private String arg4;
/* 13:   */   
/* 14:   */   QuadToCommand(CTAdjPoint2D pt1, CTAdjPoint2D pt2)
/* 15:   */   {
/* 16:30 */     this.arg1 = pt1.getX();
/* 17:31 */     this.arg2 = pt1.getY();
/* 18:32 */     this.arg3 = pt2.getX();
/* 19:33 */     this.arg4 = pt2.getY();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void execute(Path2D.Double path, Context ctx)
/* 23:   */   {
/* 24:38 */     double x1 = ctx.getValue(this.arg1);
/* 25:39 */     double y1 = ctx.getValue(this.arg2);
/* 26:40 */     double x2 = ctx.getValue(this.arg3);
/* 27:41 */     double y2 = ctx.getValue(this.arg4);
/* 28:42 */     path.quadTo(x1, y1, x2, y2);
/* 29:   */   }
/* 30:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.geom.QuadToCommand
 * JD-Core Version:    0.7.0.1
 */