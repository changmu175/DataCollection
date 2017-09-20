/*  1:   */ package org.apache.poi.sl.draw;
/*  2:   */ 
/*  3:   */ import java.awt.Graphics2D;
/*  4:   */ import org.apache.poi.sl.usermodel.Shape;
/*  5:   */ 
/*  6:   */ public class DrawNothing
/*  7:   */   implements Drawable
/*  8:   */ {
/*  9:   */   protected final Shape<?, ?> shape;
/* 10:   */   
/* 11:   */   public DrawNothing(Shape<?, ?> shape)
/* 12:   */   {
/* 13:30 */     this.shape = shape;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void applyTransform(Graphics2D graphics) {}
/* 17:   */   
/* 18:   */   public void draw(Graphics2D graphics) {}
/* 19:   */   
/* 20:   */   public void drawContent(Graphics2D context) {}
/* 21:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.DrawNothing
 * JD-Core Version:    0.7.0.1
 */