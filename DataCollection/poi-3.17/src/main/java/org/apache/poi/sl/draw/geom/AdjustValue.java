/*  1:   */ package org.apache.poi.sl.draw.geom;
/*  2:   */ 
/*  3:   */ import org.apache.poi.sl.draw.binding.CTGeomGuide;
/*  4:   */ 
/*  5:   */ public class AdjustValue
/*  6:   */   extends Guide
/*  7:   */ {
/*  8:   */   public AdjustValue(CTGeomGuide gd)
/*  9:   */   {
/* 10:30 */     super(gd.getName(), gd.getFmla());
/* 11:   */   }
/* 12:   */   
/* 13:   */   public double evaluate(Context ctx)
/* 14:   */   {
/* 15:35 */     String name = getName();
/* 16:36 */     Guide adj = ctx.getAdjustValue(name);
/* 17:37 */     return adj != null ? adj.evaluate(ctx) : super.evaluate(ctx);
/* 18:   */   }
/* 19:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.geom.AdjustValue
 * JD-Core Version:    0.7.0.1
 */