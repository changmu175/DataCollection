/*  1:   */ package org.apache.poi.xdgf.usermodel.shape;
/*  2:   */ 
/*  3:   */ import java.awt.geom.AffineTransform;
/*  4:   */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*  5:   */ import org.apache.poi.xdgf.usermodel.XDGFText;
/*  6:   */ 
/*  7:   */ public class ShapeTextVisitor
/*  8:   */   extends ShapeVisitor
/*  9:   */ {
/* 10:   */   protected StringBuilder text;
/* 11:   */   
/* 12:   */   public ShapeTextVisitor()
/* 13:   */   {
/* 14:32 */     this.text = new StringBuilder();
/* 15:   */   }
/* 16:   */   
/* 17:   */   public static class TextAcceptor
/* 18:   */     implements ShapeVisitorAcceptor
/* 19:   */   {
/* 20:   */     public boolean accept(XDGFShape shape)
/* 21:   */     {
/* 22:36 */       return shape.hasText();
/* 23:   */     }
/* 24:   */   }
/* 25:   */   
/* 26:   */   protected ShapeVisitorAcceptor getAcceptor()
/* 27:   */   {
/* 28:41 */     return new TextAcceptor();
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void visit(XDGFShape shape, AffineTransform globalTransform, int level)
/* 32:   */   {
/* 33:46 */     this.text.append(shape.getText().getTextContent().trim());
/* 34:47 */     this.text.append('\n');
/* 35:   */   }
/* 36:   */   
/* 37:   */   public String getText()
/* 38:   */   {
/* 39:54 */     return this.text.toString();
/* 40:   */   }
/* 41:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.shape.ShapeTextVisitor
 * JD-Core Version:    0.7.0.1
 */