/*  1:   */ package org.apache.poi.sl.draw.geom;
/*  2:   */ 
/*  3:   */ import java.awt.geom.Path2D.Double;
/*  4:   */ import org.apache.poi.sl.draw.binding.CTAdjPoint2D;
/*  5:   */ 
/*  6:   */ public class MoveToCommand
/*  7:   */   implements PathCommand
/*  8:   */ {
/*  9:   */   private String arg1;
/* 10:   */   private String arg2;
/* 11:   */   
/* 12:   */   MoveToCommand(CTAdjPoint2D pt)
/* 13:   */   {
/* 14:30 */     this.arg1 = pt.getX();
/* 15:31 */     this.arg2 = pt.getY();
/* 16:   */   }
/* 17:   */   
/* 18:   */   MoveToCommand(String s1, String s2)
/* 19:   */   {
/* 20:35 */     this.arg1 = s1;
/* 21:36 */     this.arg2 = s2;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void execute(Path2D.Double path, Context ctx)
/* 25:   */   {
/* 26:41 */     double x = ctx.getValue(this.arg1);
/* 27:42 */     double y = ctx.getValue(this.arg2);
/* 28:43 */     path.moveTo(x, y);
/* 29:   */   }
/* 30:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.geom.MoveToCommand
 * JD-Core Version:    0.7.0.1
 */