/*  1:   */ package org.apache.poi.sl.draw;
/*  2:   */ 
/*  3:   */ import java.awt.Graphics2D;
/*  4:   */ import org.apache.poi.sl.usermodel.Background;
/*  5:   */ import org.apache.poi.sl.usermodel.Sheet;
/*  6:   */ import org.apache.poi.sl.usermodel.Slide;
/*  7:   */ 
/*  8:   */ public class DrawSlide
/*  9:   */   extends DrawSheet
/* 10:   */ {
/* 11:   */   public DrawSlide(Slide<?, ?> slide)
/* 12:   */   {
/* 13:28 */     super(slide);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void draw(Graphics2D graphics)
/* 17:   */   {
/* 18:32 */     graphics.setRenderingHint(Drawable.CURRENT_SLIDE, this.sheet);
/* 19:   */     
/* 20:34 */     Background<?, ?> bg = this.sheet.getBackground();
/* 21:35 */     if (bg != null)
/* 22:   */     {
/* 23:36 */       DrawFactory drawFact = DrawFactory.getInstance(graphics);
/* 24:37 */       Drawable db = drawFact.getDrawable(bg);
/* 25:38 */       db.draw(graphics);
/* 26:   */     }
/* 27:41 */     super.draw(graphics);
/* 28:42 */     graphics.setRenderingHint(Drawable.CURRENT_SLIDE, null);
/* 29:   */   }
/* 30:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.DrawSlide
 * JD-Core Version:    0.7.0.1
 */