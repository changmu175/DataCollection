/*   1:    */ package org.apache.poi.sl.draw;
/*   2:    */ 
/*   3:    */ import java.awt.Graphics2D;
/*   4:    */ import java.awt.font.TextLayout;
/*   5:    */ import java.text.AttributedCharacterIterator;
/*   6:    */ import java.text.AttributedString;
/*   7:    */ 
/*   8:    */ public class DrawTextFragment
/*   9:    */   implements Drawable
/*  10:    */ {
/*  11:    */   final TextLayout layout;
/*  12:    */   final AttributedString str;
/*  13:    */   double x;
/*  14:    */   double y;
/*  15:    */   
/*  16:    */   public DrawTextFragment(TextLayout layout, AttributedString str)
/*  17:    */   {
/*  18: 30 */     this.layout = layout;
/*  19: 31 */     this.str = str;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public void setPosition(double x, double y)
/*  23:    */   {
/*  24: 36 */     this.x = x;
/*  25: 37 */     this.y = y;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void draw(Graphics2D graphics)
/*  29:    */   {
/*  30: 41 */     if (this.str == null) {
/*  31: 42 */       return;
/*  32:    */     }
/*  33: 45 */     double yBaseline = this.y + this.layout.getAscent();
/*  34:    */     
/*  35: 47 */     Integer textMode = (Integer)graphics.getRenderingHint(Drawable.TEXT_RENDERING_MODE);
/*  36: 48 */     if ((textMode != null) && (textMode.intValue() == 2)) {
/*  37: 49 */       this.layout.draw(graphics, (float)this.x, (float)yBaseline);
/*  38:    */     } else {
/*  39: 51 */       graphics.drawString(this.str.getIterator(), (float)this.x, (float)yBaseline);
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void applyTransform(Graphics2D graphics) {}
/*  44:    */   
/*  45:    */   public void drawContent(Graphics2D graphics) {}
/*  46:    */   
/*  47:    */   public TextLayout getLayout()
/*  48:    */   {
/*  49: 62 */     return this.layout;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public AttributedString getAttributedString()
/*  53:    */   {
/*  54: 66 */     return this.str;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public float getHeight()
/*  58:    */   {
/*  59: 73 */     double h = this.layout.getAscent() + this.layout.getDescent() + getLeading();
/*  60: 74 */     return (float)h;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public float getLeading()
/*  64:    */   {
/*  65: 82 */     double l = this.layout.getLeading();
/*  66: 83 */     if (l == 0.0D) {
/*  67: 86 */       l = (this.layout.getAscent() + this.layout.getDescent()) * 0.15D;
/*  68:    */     }
/*  69: 88 */     return (float)l;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public float getWidth()
/*  73:    */   {
/*  74: 96 */     return this.layout.getAdvance();
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String getString()
/*  78:    */   {
/*  79:104 */     if (this.str == null) {
/*  80:104 */       return "";
/*  81:    */     }
/*  82:106 */     AttributedCharacterIterator it = this.str.getIterator();
/*  83:107 */     StringBuilder buf = new StringBuilder();
/*  84:108 */     for (char c = it.first(); c != 65535; c = it.next()) {
/*  85:109 */       buf.append(c);
/*  86:    */     }
/*  87:111 */     return buf.toString();
/*  88:    */   }
/*  89:    */   
/*  90:    */   public String toString()
/*  91:    */   {
/*  92:116 */     return "[" + getClass().getSimpleName() + "] " + getString();
/*  93:    */   }
/*  94:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.DrawTextFragment
 * JD-Core Version:    0.7.0.1
 */