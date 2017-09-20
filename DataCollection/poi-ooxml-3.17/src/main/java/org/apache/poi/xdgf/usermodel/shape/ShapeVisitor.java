/*  1:   */ package org.apache.poi.xdgf.usermodel.shape;
/*  2:   */ 
/*  3:   */ import java.awt.geom.AffineTransform;
/*  4:   */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*  5:   */ 
/*  6:   */ public abstract class ShapeVisitor
/*  7:   */ {
/*  8:   */   protected ShapeVisitorAcceptor _acceptor;
/*  9:   */   
/* 10:   */   public ShapeVisitor()
/* 11:   */   {
/* 12:38 */     this._acceptor = getAcceptor();
/* 13:   */   }
/* 14:   */   
/* 15:   */   protected ShapeVisitorAcceptor getAcceptor()
/* 16:   */   {
/* 17:46 */     new ShapeVisitorAcceptor()
/* 18:   */     {
/* 19:   */       public boolean accept(XDGFShape shape)
/* 20:   */       {
/* 21:49 */         return !shape.isDeleted();
/* 22:   */       }
/* 23:   */     };
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void setAcceptor(ShapeVisitorAcceptor acceptor)
/* 27:   */   {
/* 28:55 */     this._acceptor = acceptor;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public boolean accept(XDGFShape shape)
/* 32:   */   {
/* 33:59 */     return this._acceptor.accept(shape);
/* 34:   */   }
/* 35:   */   
/* 36:   */   public abstract void visit(XDGFShape paramXDGFShape, AffineTransform paramAffineTransform, int paramInt);
/* 37:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.shape.ShapeVisitor
 * JD-Core Version:    0.7.0.1
 */