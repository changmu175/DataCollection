/*   1:    */ package org.apache.poi.sl.draw;
/*   2:    */ 
/*   3:    */ import java.awt.BasicStroke;
/*   4:    */ import java.awt.Graphics2D;
/*   5:    */ import java.awt.geom.AffineTransform;
/*   6:    */ import java.awt.geom.Rectangle2D;
/*   7:    */ import java.util.Locale;
/*   8:    */ import org.apache.poi.sl.usermodel.PlaceableShape;
/*   9:    */ import org.apache.poi.sl.usermodel.StrokeStyle;
/*  10:    */ import org.apache.poi.sl.usermodel.StrokeStyle.LineCap;
/*  11:    */ import org.apache.poi.sl.usermodel.StrokeStyle.LineDash;
/*  12:    */ 
/*  13:    */ public class DrawShape
/*  14:    */   implements Drawable
/*  15:    */ {
/*  16:    */   protected final org.apache.poi.sl.usermodel.Shape<?, ?> shape;
/*  17:    */   
/*  18:    */   public DrawShape(org.apache.poi.sl.usermodel.Shape<?, ?> shape)
/*  19:    */   {
/*  20: 38 */     this.shape = shape;
/*  21:    */   }
/*  22:    */   
/*  23:    */   protected static boolean isHSLF(org.apache.poi.sl.usermodel.Shape<?, ?> shape)
/*  24:    */   {
/*  25: 49 */     return shape.getClass().getCanonicalName().toLowerCase(Locale.ROOT).contains("hslf");
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void applyTransform(Graphics2D graphics)
/*  29:    */   {
/*  30: 59 */     if (!(this.shape instanceof PlaceableShape)) {
/*  31: 60 */       return;
/*  32:    */     }
/*  33: 63 */     PlaceableShape<?, ?> ps = (PlaceableShape)this.shape;
/*  34: 64 */     boolean isHSLF = isHSLF(this.shape);
/*  35: 65 */     AffineTransform tx = (AffineTransform)graphics.getRenderingHint(Drawable.GROUP_TRANSFORM);
/*  36: 66 */     if (tx == null) {
/*  37: 67 */       tx = new AffineTransform();
/*  38:    */     }
/*  39: 69 */     Rectangle2D anchor = tx.createTransformedShape(ps.getAnchor()).getBounds2D();
/*  40:    */     
/*  41: 71 */     char[] cmds = { 'r', 'h', isHSLF ? new char[] { 'h', 'v', 'r' } : 118 };
/*  42: 72 */     for (char ch : cmds) {
/*  43: 73 */       switch (ch)
/*  44:    */       {
/*  45:    */       case 'h': 
/*  46: 76 */         if (ps.getFlipHorizontal())
/*  47:    */         {
/*  48: 77 */           graphics.translate(anchor.getX() + anchor.getWidth(), anchor.getY());
/*  49: 78 */           graphics.scale(-1.0D, 1.0D);
/*  50: 79 */           graphics.translate(-anchor.getX(), -anchor.getY());
/*  51:    */         }
/*  52:    */         break;
/*  53:    */       case 'v': 
/*  54: 84 */         if (ps.getFlipVertical())
/*  55:    */         {
/*  56: 85 */           graphics.translate(anchor.getX(), anchor.getY() + anchor.getHeight());
/*  57: 86 */           graphics.scale(1.0D, -1.0D);
/*  58: 87 */           graphics.translate(-anchor.getX(), -anchor.getY());
/*  59:    */         }
/*  60:    */         break;
/*  61:    */       case 'r': 
/*  62: 92 */         double rotation = ps.getRotation();
/*  63: 93 */         if (rotation != 0.0D)
/*  64:    */         {
/*  65: 95 */           double centerX = anchor.getCenterX();
/*  66: 96 */           double centerY = anchor.getCenterY();
/*  67:    */           
/*  68:    */ 
/*  69: 99 */           rotation %= 360.0D;
/*  70:100 */           if (rotation < 0.0D) {
/*  71:101 */             rotation += 360.0D;
/*  72:    */           }
/*  73:104 */           int quadrant = ((int)rotation + 45) / 90 % 4;
/*  74:105 */           double scaleX = 1.0D;double scaleY = 1.0D;
/*  75:108 */           if ((quadrant == 1) || (quadrant == 3))
/*  76:    */           {
/*  77:    */             AffineTransform txs;
/*  78:    */             AffineTransform txs;
/*  79:119 */             if (isHSLF)
/*  80:    */             {
/*  81:120 */               txs = new AffineTransform(tx);
/*  82:    */             }
/*  83:    */             else
/*  84:    */             {
/*  85:123 */               txs = new AffineTransform();
/*  86:124 */               txs.translate(centerX, centerY);
/*  87:125 */               txs.rotate(1.570796326794897D);
/*  88:126 */               txs.translate(-centerX, -centerY);
/*  89:127 */               txs.concatenate(tx);
/*  90:    */             }
/*  91:130 */             txs.translate(centerX, centerY);
/*  92:131 */             txs.rotate(1.570796326794897D);
/*  93:132 */             txs.translate(-centerX, -centerY);
/*  94:    */             
/*  95:134 */             Rectangle2D anchor2 = txs.createTransformedShape(ps.getAnchor()).getBounds2D();
/*  96:    */             
/*  97:136 */             scaleX = safeScale(anchor.getWidth(), anchor2.getWidth());
/*  98:137 */             scaleY = safeScale(anchor.getHeight(), anchor2.getHeight());
/*  99:    */           }
/* 100:    */           else
/* 101:    */           {
/* 102:139 */             quadrant = 0;
/* 103:    */           }
/* 104:143 */           graphics.translate(centerX, centerY);
/* 105:144 */           double rot = Math.toRadians(rotation - quadrant * 90.0D);
/* 106:145 */           if (rot != 0.0D) {
/* 107:146 */             graphics.rotate(rot);
/* 108:    */           }
/* 109:148 */           graphics.scale(scaleX, scaleY);
/* 110:149 */           rot = Math.toRadians(quadrant * 90.0D);
/* 111:150 */           if (rot != 0.0D) {
/* 112:151 */             graphics.rotate(rot);
/* 113:    */           }
/* 114:153 */           graphics.translate(-centerX, -centerY);
/* 115:    */         }
/* 116:154 */         break;
/* 117:    */       default: 
/* 118:157 */         throw new RuntimeException("unexpected transform code " + ch);
/* 119:    */       }
/* 120:    */     }
/* 121:    */   }
/* 122:    */   
/* 123:    */   private static double safeScale(double dim1, double dim2)
/* 124:    */   {
/* 125:163 */     if (dim1 == 0.0D) {
/* 126:164 */       return 1.0D;
/* 127:    */     }
/* 128:166 */     return dim2 == 0.0D ? 1.0D : dim1 / dim2;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void draw(Graphics2D graphics) {}
/* 132:    */   
/* 133:    */   public void drawContent(Graphics2D graphics) {}
/* 134:    */   
/* 135:    */   public static Rectangle2D getAnchor(Graphics2D graphics, PlaceableShape<?, ?> shape)
/* 136:    */   {
/* 137:178 */     return getAnchor(graphics, shape.getAnchor());
/* 138:    */   }
/* 139:    */   
/* 140:    */   public static Rectangle2D getAnchor(Graphics2D graphics, Rectangle2D anchor)
/* 141:    */   {
/* 142:182 */     if (graphics == null) {
/* 143:183 */       return anchor;
/* 144:    */     }
/* 145:186 */     AffineTransform tx = (AffineTransform)graphics.getRenderingHint(Drawable.GROUP_TRANSFORM);
/* 146:187 */     if ((tx != null) && (!tx.isIdentity())) {
/* 147:188 */       anchor = tx.createTransformedShape(anchor).getBounds2D();
/* 148:    */     }
/* 149:190 */     return anchor;
/* 150:    */   }
/* 151:    */   
/* 152:    */   protected org.apache.poi.sl.usermodel.Shape<?, ?> getShape()
/* 153:    */   {
/* 154:194 */     return this.shape;
/* 155:    */   }
/* 156:    */   
/* 157:    */   protected static BasicStroke getStroke(StrokeStyle strokeStyle)
/* 158:    */   {
/* 159:198 */     float lineWidth = (float)strokeStyle.getLineWidth();
/* 160:199 */     if (lineWidth == 0.0F) {
/* 161:201 */       lineWidth = 0.25F;
/* 162:    */     }
/* 163:204 */     StrokeStyle.LineDash lineDash = strokeStyle.getLineDash();
/* 164:205 */     if (lineDash == null) {
/* 165:206 */       lineDash = StrokeStyle.LineDash.SOLID;
/* 166:    */     }
/* 167:209 */     int[] dashPatI = lineDash.pattern;
/* 168:210 */     float dash_phase = 0.0F;
/* 169:211 */     float[] dashPatF = null;
/* 170:212 */     if (dashPatI != null)
/* 171:    */     {
/* 172:213 */       dashPatF = new float[dashPatI.length];
/* 173:214 */       for (int i = 0; i < dashPatI.length; i++) {
/* 174:215 */         dashPatF[i] = (dashPatI[i] * Math.max(1.0F, lineWidth));
/* 175:    */       }
/* 176:    */     }
/* 177:219 */     StrokeStyle.LineCap lineCapE = strokeStyle.getLineCap();
/* 178:220 */     if (lineCapE == null) {
/* 179:221 */       lineCapE = StrokeStyle.LineCap.FLAT;
/* 180:    */     }
/* 181:    */     int lineCap;
/* 182:224 */     switch (1.$SwitchMap$org$apache$poi$sl$usermodel$StrokeStyle$LineCap[lineCapE.ordinal()])
/* 183:    */     {
/* 184:    */     case 1: 
/* 185:226 */       lineCap = 1;
/* 186:227 */       break;
/* 187:    */     case 2: 
/* 188:229 */       lineCap = 2;
/* 189:230 */       break;
/* 190:    */     case 3: 
/* 191:    */     default: 
/* 192:233 */       lineCap = 0;
/* 193:    */     }
/* 194:237 */     int lineJoin = 1;
/* 195:    */     
/* 196:239 */     return new BasicStroke(lineWidth, lineCap, lineJoin, lineWidth, dashPatF, 0.0F);
/* 197:    */   }
/* 198:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.DrawShape
 * JD-Core Version:    0.7.0.1
 */