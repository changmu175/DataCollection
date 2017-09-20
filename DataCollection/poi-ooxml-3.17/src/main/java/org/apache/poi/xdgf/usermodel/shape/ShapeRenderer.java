/*  1:   */ package org.apache.poi.xdgf.usermodel.shape;
/*  2:   */ 
/*  3:   */ import java.awt.Font;
/*  4:   */ import java.awt.Graphics2D;
/*  5:   */ import java.awt.geom.AffineTransform;
/*  6:   */ import java.awt.geom.Path2D;
/*  7:   */ import java.awt.geom.Path2D.Double;
/*  8:   */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*  9:   */ import org.apache.poi.xdgf.usermodel.XDGFText;
/* 10:   */ 
/* 11:   */ public class ShapeRenderer
/* 12:   */   extends ShapeVisitor
/* 13:   */ {
/* 14:   */   protected Graphics2D _graphics;
/* 15:   */   
/* 16:   */   public ShapeRenderer()
/* 17:   */   {
/* 18:37 */     this._graphics = null;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public ShapeRenderer(Graphics2D g)
/* 22:   */   {
/* 23:41 */     this._graphics = g;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void setGraphics(Graphics2D g)
/* 27:   */   {
/* 28:45 */     this._graphics = g;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void visit(XDGFShape shape, AffineTransform globalTransform, int level)
/* 32:   */   {
/* 33:52 */     AffineTransform savedTr = this._graphics.getTransform();
/* 34:53 */     this._graphics.transform(globalTransform);
/* 35:   */     
/* 36:55 */     drawPath(shape);
/* 37:56 */     drawText(shape);
/* 38:   */     
/* 39:   */ 
/* 40:59 */     this._graphics.setTransform(savedTr);
/* 41:   */   }
/* 42:   */   
/* 43:   */   protected Path2D drawPath(XDGFShape shape)
/* 44:   */   {
/* 45:63 */     Double path = shape.getPath();
/* 46:64 */     if (path != null)
/* 47:   */     {
/* 48:68 */       this._graphics.setColor(shape.getLineColor());
/* 49:69 */       this._graphics.setStroke(shape.getStroke());
/* 50:70 */       this._graphics.draw(path);
/* 51:   */     }
/* 52:73 */     return path;
/* 53:   */   }
/* 54:   */   
/* 55:   */   protected void drawText(XDGFShape shape)
/* 56:   */   {
/* 57:77 */     XDGFText text = shape.getText();
/* 58:78 */     if (text != null)
/* 59:   */     {
/* 60:80 */       if (text.getTextContent().equals("Header")) {
/* 61:81 */         text.getTextBounds();
/* 62:   */       }
/* 63:83 */       Font oldFont = this._graphics.getFont();
/* 64:   */       
/* 65:85 */       this._graphics.setFont(oldFont.deriveFont(shape.getFontSize().floatValue()));
/* 66:   */       
/* 67:87 */       this._graphics.setColor(shape.getFontColor());
/* 68:   */       
/* 69:89 */       text.draw(this._graphics);
/* 70:90 */       this._graphics.setFont(oldFont);
/* 71:   */     }
/* 72:   */   }
/* 73:   */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xdgf.usermodel.shape.ShapeRenderer

 * JD-Core Version:    0.7.0.1

 */