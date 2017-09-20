/*  1:   */ package org.apache.poi.sl.draw.geom;
/*  2:   */ 
/*  3:   */ import java.awt.geom.Path2D.Double;
/*  4:   */ import org.apache.poi.sl.draw.binding.CTAdjPoint2D;
/*  5:   */ 
/*  6:   */ public class CurveToCommand
/*  7:   */   implements PathCommand
/*  8:   */ {
/*  9:   */   private String arg1;
/* 10:   */   private String arg2;
/* 11:   */   private String arg3;
/* 12:   */   private String arg4;
/* 13:   */   private String arg5;
/* 14:   */   private String arg6;
/* 15:   */   
/* 16:   */   CurveToCommand(CTAdjPoint2D pt1, CTAdjPoint2D pt2, CTAdjPoint2D pt3)
/* 17:   */   {
/* 18:30 */     this.arg1 = pt1.getX();
/* 19:31 */     this.arg2 = pt1.getY();
/* 20:32 */     this.arg3 = pt2.getX();
/* 21:33 */     this.arg4 = pt2.getY();
/* 22:34 */     this.arg5 = pt3.getX();
/* 23:35 */     this.arg6 = pt3.getY();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void execute(Path2D.Double path, Context ctx)
/* 27:   */   {
/* 28:40 */     double x1 = ctx.getValue(this.arg1);
/* 29:41 */     double y1 = ctx.getValue(this.arg2);
/* 30:42 */     double x2 = ctx.getValue(this.arg3);
/* 31:43 */     double y2 = ctx.getValue(this.arg4);
/* 32:44 */     double x3 = ctx.getValue(this.arg5);
/* 33:45 */     double y3 = ctx.getValue(this.arg6);
/* 34:46 */     path.curveTo(x1, y1, x2, y2, x3, y3);
/* 35:   */   }
/* 36:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.geom.CurveToCommand
 * JD-Core Version:    0.7.0.1
 */