/*  1:   */ package org.apache.poi.sl.draw;
/*  2:   */ 
/*  3:   */ import java.awt.Color;
/*  4:   */ import java.awt.Dimension;
/*  5:   */ import java.awt.Graphics2D;
/*  6:   */ import java.awt.geom.AffineTransform;
/*  7:   */ import org.apache.poi.sl.usermodel.MasterSheet;
/*  8:   */ import org.apache.poi.sl.usermodel.Shape;
/*  9:   */ import org.apache.poi.sl.usermodel.Sheet;
/* 10:   */ import org.apache.poi.sl.usermodel.SlideShow;
/* 11:   */ 
/* 12:   */ public class DrawSheet
/* 13:   */   implements Drawable
/* 14:   */ {
/* 15:   */   protected final Sheet<?, ?> sheet;
/* 16:   */   
/* 17:   */   public DrawSheet(Sheet<?, ?> sheet)
/* 18:   */   {
/* 19:35 */     this.sheet = sheet;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void draw(Graphics2D graphics)
/* 23:   */   {
/* 24:40 */     Dimension dim = this.sheet.getSlideShow().getPageSize();
/* 25:41 */     Color whiteTrans = new Color(1.0F, 1.0F, 1.0F, 0.0F);
/* 26:42 */     graphics.setColor(whiteTrans);
/* 27:43 */     graphics.fillRect(0, 0, (int)dim.getWidth(), (int)dim.getHeight());
/* 28:   */     
/* 29:45 */     DrawFactory drawFact = DrawFactory.getInstance(graphics);
/* 30:46 */     MasterSheet<?, ?> master = this.sheet.getMasterSheet();
/* 31:48 */     if ((this.sheet.getFollowMasterGraphics()) && (master != null))
/* 32:   */     {
/* 33:49 */       Drawable drawer = drawFact.getDrawable(master);
/* 34:50 */       drawer.draw(graphics);
/* 35:   */     }
/* 36:53 */     graphics.setRenderingHint(Drawable.GROUP_TRANSFORM, new AffineTransform());
/* 37:55 */     for (Shape<?, ?> shape : this.sheet.getShapes()) {
/* 38:56 */       if (canDraw(graphics, shape))
/* 39:   */       {
/* 40:61 */         AffineTransform at = graphics.getTransform();
/* 41:   */         
/* 42:   */ 
/* 43:   */ 
/* 44:65 */         graphics.setRenderingHint(Drawable.GSAVE, Boolean.valueOf(true));
/* 45:   */         
/* 46:   */ 
/* 47:68 */         Drawable drawer = drawFact.getDrawable(shape);
/* 48:69 */         drawer.applyTransform(graphics);
/* 49:   */         
/* 50:71 */         drawer.draw(graphics);
/* 51:   */         
/* 52:   */ 
/* 53:74 */         graphics.setTransform(at);
/* 54:   */         
/* 55:76 */         graphics.setRenderingHint(Drawable.GRESTORE, Boolean.valueOf(true));
/* 56:   */       }
/* 57:   */     }
/* 58:   */   }
/* 59:   */   
/* 60:   */   public void applyTransform(Graphics2D context) {}
/* 61:   */   
/* 62:   */   public void drawContent(Graphics2D context) {}
/* 63:   */   
/* 64:   */   protected boolean canDraw(Graphics2D graphics, Shape<?, ?> shape)
/* 65:   */   {
/* 66:95 */     return true;
/* 67:   */   }
/* 68:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.DrawSheet
 * JD-Core Version:    0.7.0.1
 */