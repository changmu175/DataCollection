/*  1:   */ package org.apache.poi.sl.draw;
/*  2:   */ 
/*  3:   */ import java.awt.Graphics2D;
/*  4:   */ import java.awt.geom.AffineTransform;
/*  5:   */ import java.awt.geom.Rectangle2D;
/*  6:   */ import org.apache.poi.sl.usermodel.GroupShape;
/*  7:   */ import org.apache.poi.sl.usermodel.Shape;
/*  8:   */ 
/*  9:   */ public class DrawGroupShape
/* 10:   */   extends DrawShape
/* 11:   */ {
/* 12:   */   public DrawGroupShape(GroupShape<?, ?> shape)
/* 13:   */   {
/* 14:30 */     super(shape);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public void draw(Graphics2D graphics)
/* 18:   */   {
/* 19:36 */     Rectangle2D interior = getShape().getInteriorAnchor();
/* 20:   */     
/* 21:38 */     Rectangle2D exterior = getShape().getAnchor();
/* 22:   */     
/* 23:40 */     AffineTransform tx = (AffineTransform)graphics.getRenderingHint(Drawable.GROUP_TRANSFORM);
/* 24:41 */     AffineTransform tx0 = new AffineTransform(tx);
/* 25:   */     
/* 26:43 */     double scaleX = interior.getWidth() == 0.0D ? 1.0D : exterior.getWidth() / interior.getWidth();
/* 27:44 */     double scaleY = interior.getHeight() == 0.0D ? 1.0D : exterior.getHeight() / interior.getHeight();
/* 28:   */     
/* 29:46 */     tx.translate(exterior.getX(), exterior.getY());
/* 30:47 */     tx.scale(scaleX, scaleY);
/* 31:48 */     tx.translate(-interior.getX(), -interior.getY());
/* 32:   */     
/* 33:50 */     DrawFactory drawFact = DrawFactory.getInstance(graphics);
/* 34:51 */     AffineTransform at2 = graphics.getTransform();
/* 35:53 */     for (Shape<?, ?> child : getShape())
/* 36:   */     {
/* 37:55 */       AffineTransform at = graphics.getTransform();
/* 38:56 */       graphics.setRenderingHint(Drawable.GSAVE, Boolean.valueOf(true));
/* 39:   */       
/* 40:58 */       Drawable draw = drawFact.getDrawable(child);
/* 41:59 */       draw.applyTransform(graphics);
/* 42:60 */       draw.draw(graphics);
/* 43:   */       
/* 44:   */ 
/* 45:63 */       graphics.setTransform(at);
/* 46:64 */       graphics.setRenderingHint(Drawable.GRESTORE, Boolean.valueOf(true));
/* 47:   */     }
/* 48:67 */     graphics.setTransform(at2);
/* 49:68 */     graphics.setRenderingHint(Drawable.GROUP_TRANSFORM, tx0);
/* 50:   */   }
/* 51:   */   
/* 52:   */   protected GroupShape<?, ?> getShape()
/* 53:   */   {
/* 54:73 */     return (GroupShape)this.shape;
/* 55:   */   }
/* 56:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.DrawGroupShape
 * JD-Core Version:    0.7.0.1
 */