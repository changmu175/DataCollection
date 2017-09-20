/*  1:   */ package org.apache.poi.sl.draw;
/*  2:   */ 
/*  3:   */ import java.awt.Graphics2D;
/*  4:   */ import org.apache.poi.sl.usermodel.GraphicalFrame;
/*  5:   */ import org.apache.poi.sl.usermodel.PictureShape;
/*  6:   */ 
/*  7:   */ public class DrawGraphicalFrame
/*  8:   */   extends DrawShape
/*  9:   */ {
/* 10:   */   public DrawGraphicalFrame(GraphicalFrame<?, ?> shape)
/* 11:   */   {
/* 12:29 */     super(shape);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public void draw(Graphics2D context)
/* 16:   */   {
/* 17:33 */     PictureShape<?, ?> ps = ((GraphicalFrame)getShape()).getFallbackPicture();
/* 18:34 */     if (ps == null) {
/* 19:35 */       return;
/* 20:   */     }
/* 21:37 */     DrawPictureShape dps = DrawFactory.getInstance(context).getDrawable(ps);
/* 22:38 */     dps.draw(context);
/* 23:   */   }
/* 24:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.DrawGraphicalFrame
 * JD-Core Version:    0.7.0.1
 */