/*  1:   */ package org.apache.poi.sl.draw;
/*  2:   */ 
/*  3:   */ import java.awt.Dimension;
/*  4:   */ import java.awt.Graphics2D;
/*  5:   */ import java.awt.Paint;
/*  6:   */ import java.awt.geom.Rectangle2D;
/*  7:   */ import java.awt.geom.Rectangle2D.Double;
/*  8:   */ import org.apache.poi.sl.usermodel.Background;
/*  9:   */ import org.apache.poi.sl.usermodel.FillStyle;
/* 10:   */ import org.apache.poi.sl.usermodel.PlaceableShape;
/* 11:   */ import org.apache.poi.sl.usermodel.Shape;
/* 12:   */ import org.apache.poi.sl.usermodel.ShapeContainer;
/* 13:   */ import org.apache.poi.sl.usermodel.Sheet;
/* 14:   */ import org.apache.poi.sl.usermodel.SlideShow;
/* 15:   */ 
/* 16:   */ public class DrawBackground
/* 17:   */   extends DrawShape
/* 18:   */ {
/* 19:   */   public DrawBackground(Background<?, ?> shape)
/* 20:   */   {
/* 21:33 */     super(shape);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void draw(Graphics2D graphics)
/* 25:   */   {
/* 26:38 */     Dimension pg = this.shape.getSheet().getSlideShow().getPageSize();
/* 27:39 */     final Rectangle2D anchor = new Double(0.0D, 0.0D, pg.getWidth(), pg.getHeight());
/* 28:   */     
/* 29:41 */     PlaceableShape<?, ?> ps = new PlaceableShape()
/* 30:   */     {
/* 31:   */       public ShapeContainer<?, ?> getParent()
/* 32:   */       {
/* 33:42 */         return null;
/* 34:   */       }
/* 35:   */       
/* 36:   */       public Rectangle2D getAnchor()
/* 37:   */       {
/* 38:43 */         return anchor;
/* 39:   */       }
/* 40:   */       
/* 41:   */       public void setAnchor(Rectangle2D newAnchor) {}
/* 42:   */       
/* 43:   */       public double getRotation()
/* 44:   */       {
/* 45:45 */         return 0.0D;
/* 46:   */       }
/* 47:   */       
/* 48:   */       public void setRotation(double theta) {}
/* 49:   */       
/* 50:   */       public void setFlipHorizontal(boolean flip) {}
/* 51:   */       
/* 52:   */       public void setFlipVertical(boolean flip) {}
/* 53:   */       
/* 54:   */       public boolean getFlipHorizontal()
/* 55:   */       {
/* 56:49 */         return false;
/* 57:   */       }
/* 58:   */       
/* 59:   */       public boolean getFlipVertical()
/* 60:   */       {
/* 61:50 */         return false;
/* 62:   */       }
/* 63:   */       
/* 64:   */       public Sheet<?, ?> getSheet()
/* 65:   */       {
/* 66:51 */         return DrawBackground.this.shape.getSheet();
/* 67:   */       }
/* 68:53 */     };
/* 69:54 */     DrawFactory drawFact = DrawFactory.getInstance(graphics);
/* 70:55 */     DrawPaint dp = drawFact.getPaint(ps);
/* 71:56 */     Paint fill = dp.getPaint(graphics, getShape().getFillStyle().getPaint());
/* 72:57 */     Rectangle2D anchor2 = getAnchor(graphics, anchor);
/* 73:59 */     if (fill != null)
/* 74:   */     {
/* 75:60 */       graphics.setRenderingHint(Drawable.GRADIENT_SHAPE, anchor);
/* 76:61 */       graphics.setPaint(fill);
/* 77:62 */       graphics.fill(anchor2);
/* 78:   */     }
/* 79:   */   }
/* 80:   */   
/* 81:   */   protected Background<?, ?> getShape()
/* 82:   */   {
/* 83:67 */     return (Background)this.shape;
/* 84:   */   }
/* 85:   */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.sl.draw.DrawBackground

 * JD-Core Version:    0.7.0.1

 */