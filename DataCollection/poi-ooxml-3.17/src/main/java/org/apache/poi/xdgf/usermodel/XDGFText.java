/*   1:    */ package org.apache.poi.xdgf.usermodel;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.TextType;
/*   4:    */ import com.microsoft.schemas.office.visio.x2012.main.impl.TextTypeImpl;
/*   5:    */ import java.awt.Font;
/*   6:    */ import java.awt.FontMetrics;
/*   7:    */ import java.awt.Graphics2D;
/*   8:    */ import java.awt.font.FontRenderContext;
/*   9:    */ import java.awt.font.TextLayout;
/*  10:    */ import java.awt.geom.AffineTransform;
/*  11:    */ import java.awt.geom.Path2D.Double;
/*  12:    */ import java.awt.geom.Point2D.Double;
/*  13:    */ import java.awt.geom.Rectangle2D;
/*  14:    */ import java.awt.geom.Rectangle2D.Double;
/*  15:    */ import org.apache.poi.util.Internal;
/*  16:    */ 
/*  17:    */ public class XDGFText
/*  18:    */ {
/*  19:    */   TextType _text;
/*  20:    */   XDGFShape _parent;
/*  21:    */   
/*  22:    */   public XDGFText(TextType text, XDGFShape parent)
/*  23:    */   {
/*  24: 40 */     this._text = text;
/*  25: 41 */     this._parent = parent;
/*  26:    */   }
/*  27:    */   
/*  28:    */   @Internal
/*  29:    */   TextType getXmlObject()
/*  30:    */   {
/*  31: 46 */     return this._text;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public String getTextContent()
/*  35:    */   {
/*  36: 53 */     return ((TextTypeImpl)this._text).getStringValue();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public Rectangle2D.Double getTextBounds()
/*  40:    */   {
/*  41: 63 */     double txtPinX = this._parent.getTxtPinX().doubleValue();
/*  42: 64 */     double txtPinY = this._parent.getTxtPinY().doubleValue();
/*  43:    */     
/*  44: 66 */     double txtLocPinX = this._parent.getTxtLocPinX().doubleValue();
/*  45: 67 */     double txtLocPinY = this._parent.getTxtLocPinY().doubleValue();
/*  46:    */     
/*  47: 69 */     double txtWidth = this._parent.getTxtWidth().doubleValue();
/*  48: 70 */     double txtHeight = this._parent.getTxtHeight().doubleValue();
/*  49:    */     
/*  50: 72 */     double x = txtPinX - txtLocPinX;
/*  51: 73 */     double y = txtPinY - txtLocPinY;
/*  52:    */     
/*  53: 75 */     return new Rectangle2D.Double(x, y, txtWidth, txtHeight);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public Path2D.Double getBoundsAsPath()
/*  57:    */   {
/*  58: 84 */     Rectangle2D.Double rect = getTextBounds();
/*  59: 85 */     Double w = Double.valueOf(rect.getWidth());
/*  60: 86 */     Double h = Double.valueOf(rect.getHeight());
/*  61:    */     
/*  62: 88 */     Path2D.Double bounds = new Path2D.Double();
/*  63: 89 */     bounds.moveTo(0.0D, 0.0D);
/*  64: 90 */     bounds.lineTo(w.doubleValue(), 0.0D);
/*  65: 91 */     bounds.lineTo(w.doubleValue(), h.doubleValue());
/*  66: 92 */     bounds.lineTo(0.0D, h.doubleValue());
/*  67: 93 */     bounds.lineTo(0.0D, 0.0D);
/*  68:    */     
/*  69: 95 */     return bounds;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public Point2D.Double getTextCenter()
/*  73:    */   {
/*  74:102 */     return new Point2D.Double(this._parent.getTxtLocPinX().doubleValue(), this._parent.getTxtLocPinY().doubleValue());
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void draw(Graphics2D graphics)
/*  78:    */   {
/*  79:112 */     String textContent = getTextContent();
/*  80:113 */     if (textContent.length() == 0) {
/*  81:114 */       return;
/*  82:    */     }
/*  83:116 */     Rectangle2D.Double bounds = getTextBounds();
/*  84:    */     
/*  85:118 */     String[] lines = textContent.trim().split("\n");
/*  86:119 */     FontRenderContext frc = graphics.getFontRenderContext();
/*  87:120 */     Font font = graphics.getFont();
/*  88:    */     
/*  89:122 */     AffineTransform oldTr = graphics.getTransform();
/*  90:    */     
/*  91:    */ 
/*  92:    */ 
/*  93:126 */     Boolean flipX = this._parent.getFlipX();
/*  94:127 */     Boolean flipY = this._parent.getFlipY();
/*  95:129 */     if ((flipY == null) || (!this._parent.getFlipY().booleanValue()))
/*  96:    */     {
/*  97:130 */       graphics.translate(bounds.x, bounds.y);
/*  98:131 */       graphics.scale(1.0D, -1.0D);
/*  99:132 */       graphics.translate(0.0D, -bounds.height + graphics.getFontMetrics().getMaxCharBounds(graphics).getHeight());
/* 100:    */     }
/* 101:137 */     if ((flipX != null) && (this._parent.getFlipX().booleanValue()))
/* 102:    */     {
/* 103:138 */       graphics.scale(-1.0D, 1.0D);
/* 104:139 */       graphics.translate(-bounds.width, 0.0D);
/* 105:    */     }
/* 106:142 */     Double txtAngle = this._parent.getTxtAngle();
/* 107:143 */     if ((txtAngle != null) && (Math.abs(txtAngle.doubleValue()) > 0.01D)) {
/* 108:144 */       graphics.rotate(txtAngle.doubleValue());
/* 109:    */     }
/* 110:146 */     float nextY = 0.0F;
/* 111:147 */     for (String line : lines) {
/* 112:149 */       if (line.length() != 0)
/* 113:    */       {
/* 114:152 */         TextLayout layout = new TextLayout(line, font, frc);
/* 115:154 */         if (layout.isLeftToRight()) {
/* 116:155 */           layout.draw(graphics, 0.0F, nextY);
/* 117:    */         } else {
/* 118:157 */           layout.draw(graphics, (float)(bounds.width - layout.getAdvance()), nextY);
/* 119:    */         }
/* 120:160 */         nextY += layout.getAscent() + layout.getDescent() + layout.getLeading();
/* 121:    */       }
/* 122:    */     }
/* 123:164 */     graphics.setTransform(oldTr);
/* 124:    */   }
/* 125:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFText
 * JD-Core Version:    0.7.0.1
 */