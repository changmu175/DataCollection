/*  1:   */ package org.apache.poi.xdgf.usermodel.shape;
/*  2:   */ 
/*  3:   */ import java.awt.Font;
/*  4:   */ import java.awt.Graphics2D;
/*  5:   */ import java.awt.geom.Path2D;
/*  6:   */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*  7:   */ 
/*  8:   */ public class ShapeDebuggerRenderer
/*  9:   */   extends ShapeRenderer
/* 10:   */ {
/* 11:28 */   ShapeVisitorAcceptor _debugAcceptor = null;
/* 12:   */   
/* 13:   */   public ShapeDebuggerRenderer() {}
/* 14:   */   
/* 15:   */   public ShapeDebuggerRenderer(Graphics2D g)
/* 16:   */   {
/* 17:35 */     super(g);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void setDebugAcceptor(ShapeVisitorAcceptor acceptor)
/* 21:   */   {
/* 22:39 */     this._debugAcceptor = acceptor;
/* 23:   */   }
/* 24:   */   
/* 25:   */   protected Path2D drawPath(XDGFShape shape)
/* 26:   */   {
/* 27:45 */     Path2D path = super.drawPath(shape);
/* 28:46 */     if ((this._debugAcceptor == null) || (this._debugAcceptor.accept(shape)))
/* 29:   */     {
/* 30:49 */       Font f = this._graphics.getFont();
/* 31:50 */       this._graphics.scale(1.0D, -1.0D);
/* 32:51 */       this._graphics.setFont(f.deriveFont(0.05F));
/* 33:   */       
/* 34:53 */       String shapeId = "" + shape.getID();
/* 35:54 */       float shapeOffset = -0.1F;
/* 36:56 */       if (shape.hasMasterShape())
/* 37:   */       {
/* 38:57 */         shapeId = shapeId + " MS:" + shape.getMasterShape().getID();
/* 39:58 */         shapeOffset -= 0.15F;
/* 40:   */       }
/* 41:61 */       this._graphics.drawString(shapeId, shapeOffset, 0.0F);
/* 42:62 */       this._graphics.setFont(f);
/* 43:63 */       this._graphics.scale(1.0D, -1.0D);
/* 44:   */     }
/* 45:66 */     return path;
/* 46:   */   }
/* 47:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.shape.ShapeDebuggerRenderer
 * JD-Core Version:    0.7.0.1
 */