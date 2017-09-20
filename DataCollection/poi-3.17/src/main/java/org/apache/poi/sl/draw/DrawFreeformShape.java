/*  1:   */ package org.apache.poi.sl.draw;
/*  2:   */ 
/*  3:   */ import java.awt.Graphics2D;
/*  4:   */ import java.awt.Shape;
/*  5:   */ import java.awt.geom.AffineTransform;
/*  6:   */ import java.awt.geom.Path2D;
/*  7:   */ import java.util.ArrayList;
/*  8:   */ import java.util.Collection;
/*  9:   */ import java.util.List;
/* 10:   */ import org.apache.poi.sl.draw.geom.Outline;
/* 11:   */ import org.apache.poi.sl.draw.geom.Path;
/* 12:   */ import org.apache.poi.sl.usermodel.FillStyle;
/* 13:   */ import org.apache.poi.sl.usermodel.FreeformShape;
/* 14:   */ import org.apache.poi.sl.usermodel.StrokeStyle;
/* 15:   */ import org.apache.poi.sl.usermodel.TextParagraph;
/* 16:   */ import org.apache.poi.sl.usermodel.TextRun;
/* 17:   */ import org.apache.poi.sl.usermodel.TextShape;
/* 18:   */ 
/* 19:   */ public class DrawFreeformShape
/* 20:   */   extends DrawAutoShape
/* 21:   */ {
/* 22:   */   public DrawFreeformShape(FreeformShape<?, ?> shape)
/* 23:   */   {
/* 24:33 */     super(shape);
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected Collection<Outline> computeOutlines(Graphics2D graphics)
/* 28:   */   {
/* 29:37 */     List<Outline> lst = new ArrayList();
/* 30:38 */     FreeformShape<?, ?> fsh = (FreeformShape)getShape();
/* 31:39 */     Path2D sh = fsh.getPath();
/* 32:   */     
/* 33:41 */     AffineTransform tx = (AffineTransform)graphics.getRenderingHint(Drawable.GROUP_TRANSFORM);
/* 34:42 */     if (tx == null) {
/* 35:43 */       tx = new AffineTransform();
/* 36:   */     }
/* 37:46 */     Shape canvasShape = tx.createTransformedShape(sh);
/* 38:   */     
/* 39:48 */     FillStyle fs = fsh.getFillStyle();
/* 40:49 */     StrokeStyle ss = fsh.getStrokeStyle();
/* 41:50 */     Path path = new Path(fs != null, ss != null);
/* 42:51 */     lst.add(new Outline(canvasShape, path));
/* 43:52 */     return lst;
/* 44:   */   }
/* 45:   */   
/* 46:   */   protected TextShape<?, ? extends TextParagraph<?, ?, ? extends TextRun>> getShape()
/* 47:   */   {
/* 48:57 */     return (TextShape)this.shape;
/* 49:   */   }
/* 50:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.DrawFreeformShape
 * JD-Core Version:    0.7.0.1
 */