/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.geom.Rectangle2D;
/*   5:    */ import org.apache.poi.sl.draw.DrawPaint;
/*   6:    */ import org.apache.poi.sl.usermodel.PaintStyle.SolidPaint;
/*   7:    */ import org.apache.poi.sl.usermodel.Shadow;
/*   8:    */ import org.apache.poi.util.Units;
/*   9:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect;
/*  10:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor;
/*  11:    */ 
/*  12:    */ public class XSLFShadow
/*  13:    */   extends XSLFShape
/*  14:    */   implements Shadow<XSLFShape, XSLFTextParagraph>
/*  15:    */ {
/*  16:    */   private XSLFSimpleShape _parent;
/*  17:    */   
/*  18:    */   XSLFShadow(CTOuterShadowEffect shape, XSLFSimpleShape parentShape)
/*  19:    */   {
/*  20: 40 */     super(shape, parentShape.getSheet());
/*  21:    */     
/*  22: 42 */     this._parent = parentShape;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public XSLFSimpleShape getShadowParent()
/*  26:    */   {
/*  27: 47 */     return this._parent;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Rectangle2D getAnchor()
/*  31:    */   {
/*  32: 52 */     return this._parent.getAnchor();
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setAnchor(Rectangle2D anchor)
/*  36:    */   {
/*  37: 56 */     throw new IllegalStateException("You can't set anchor of a shadow");
/*  38:    */   }
/*  39:    */   
/*  40:    */   public double getDistance()
/*  41:    */   {
/*  42: 63 */     CTOuterShadowEffect ct = (CTOuterShadowEffect)getXmlObject();
/*  43: 64 */     return ct.isSetDist() ? Units.toPoints(ct.getDist()) : 0.0D;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public double getAngle()
/*  47:    */   {
/*  48: 72 */     CTOuterShadowEffect ct = (CTOuterShadowEffect)getXmlObject();
/*  49: 73 */     return ct.isSetDir() ? ct.getDir() / 60000.0D : 0.0D;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public double getBlur()
/*  53:    */   {
/*  54: 82 */     CTOuterShadowEffect ct = (CTOuterShadowEffect)getXmlObject();
/*  55: 83 */     return ct.isSetBlurRad() ? Units.toPoints(ct.getBlurRad()) : 0.0D;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public Color getFillColor()
/*  59:    */   {
/*  60: 91 */     PaintStyle.SolidPaint ps = getFillStyle();
/*  61: 92 */     if (ps == null) {
/*  62: 92 */       return null;
/*  63:    */     }
/*  64: 93 */     Color col = DrawPaint.applyColorTransform(ps.getSolidColor());
/*  65: 94 */     return col;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public PaintStyle.SolidPaint getFillStyle()
/*  69:    */   {
/*  70: 99 */     XSLFTheme theme = getSheet().getTheme();
/*  71:100 */     CTOuterShadowEffect ct = (CTOuterShadowEffect)getXmlObject();
/*  72:101 */     if (ct == null) {
/*  73:101 */       return null;
/*  74:    */     }
/*  75:103 */     CTSchemeColor phClr = ct.getSchemeClr();
/*  76:104 */     XSLFColor xc = new XSLFColor(ct, theme, phClr);
/*  77:105 */     return DrawPaint.createSolidPaint(xc.getColorStyle());
/*  78:    */   }
/*  79:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFShadow
 * JD-Core Version:    0.7.0.1
 */