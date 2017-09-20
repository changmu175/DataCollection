/*  1:   */ package org.apache.poi.sl.draw;
/*  2:   */ 
/*  3:   */ import java.awt.Graphics2D;
/*  4:   */ import org.apache.poi.sl.usermodel.MasterSheet;
/*  5:   */ import org.apache.poi.sl.usermodel.Placeholder;
/*  6:   */ import org.apache.poi.sl.usermodel.Shape;
/*  7:   */ import org.apache.poi.sl.usermodel.SimpleShape;
/*  8:   */ import org.apache.poi.sl.usermodel.Slide;
/*  9:   */ 
/* 10:   */ public class DrawMasterSheet
/* 11:   */   extends DrawSheet
/* 12:   */ {
/* 13:   */   public DrawMasterSheet(MasterSheet<?, ?> sheet)
/* 14:   */   {
/* 15:32 */     super(sheet);
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected boolean canDraw(Graphics2D graphics, Shape<?, ?> shape)
/* 19:   */   {
/* 20:43 */     Slide<?, ?> slide = (Slide)graphics.getRenderingHint(Drawable.CURRENT_SLIDE);
/* 21:44 */     if ((shape instanceof SimpleShape))
/* 22:   */     {
/* 23:46 */       Placeholder ph = ((SimpleShape)shape).getPlaceholder();
/* 24:47 */       if (ph != null) {
/* 25:48 */         return slide.getDisplayPlaceholder(ph);
/* 26:   */       }
/* 27:   */     }
/* 28:51 */     return slide.getFollowMasterGraphics();
/* 29:   */   }
/* 30:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.DrawMasterSheet
 * JD-Core Version:    0.7.0.1
 */