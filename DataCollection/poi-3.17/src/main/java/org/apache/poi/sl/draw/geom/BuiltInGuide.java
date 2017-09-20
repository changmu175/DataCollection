/*   1:    */ package org.apache.poi.sl.draw.geom;
/*   2:    */ 
/*   3:    */ import java.awt.geom.Rectangle2D;
/*   4:    */ 
/*   5:    */  enum BuiltInGuide
/*   6:    */   implements Formula
/*   7:    */ {
/*   8: 25 */   _3cd4,  _3cd8,  _5cd8,  _7cd8,  _b,  _cd2,  _cd4,  _cd8,  _hc,  _h,  _hd2,  _hd3,  _hd4,  _hd5,  _hd6,  _hd8,  _l,  _ls,  _r,  _ss,  _ssd2,  _ssd4,  _ssd6,  _ssd8,  _ssd16,  _ssd32,  _t,  _vc,  _w,  _wd2,  _wd3,  _wd4,  _wd5,  _wd6,  _wd8,  _wd10,  _wd32;
/*   9:    */   
/*  10:    */   private BuiltInGuide() {}
/*  11:    */   
/*  12:    */   public String getName()
/*  13:    */   {
/*  14: 30 */     return name().substring(1);
/*  15:    */   }
/*  16:    */   
/*  17:    */   public double evaluate(Context ctx)
/*  18:    */   {
/*  19: 35 */     Rectangle2D anchor = ctx.getShapeAnchor();
/*  20: 36 */     double height = anchor.getHeight();double width = anchor.getWidth();double ss = Math.min(width, height);
/*  21: 37 */     switch (1.$SwitchMap$org$apache$poi$sl$draw$geom$BuiltInGuide[ordinal()])
/*  22:    */     {
/*  23:    */     case 1: 
/*  24: 40 */       return 16200000.0D;
/*  25:    */     case 2: 
/*  26: 43 */       return 8100000.0D;
/*  27:    */     case 3: 
/*  28: 46 */       return 13500000.0D;
/*  29:    */     case 4: 
/*  30: 49 */       return 18900000.0D;
/*  31:    */     case 5: 
/*  32: 52 */       return anchor.getY();
/*  33:    */     case 6: 
/*  34: 55 */       return anchor.getMaxY();
/*  35:    */     case 7: 
/*  36: 58 */       return anchor.getX();
/*  37:    */     case 8: 
/*  38: 61 */       return anchor.getMaxX();
/*  39:    */     case 9: 
/*  40: 64 */       return 10800000.0D;
/*  41:    */     case 10: 
/*  42: 67 */       return 5400000.0D;
/*  43:    */     case 11: 
/*  44: 70 */       return 2700000.0D;
/*  45:    */     case 12: 
/*  46: 73 */       return anchor.getCenterX();
/*  47:    */     case 13: 
/*  48: 76 */       return height;
/*  49:    */     case 14: 
/*  50: 79 */       return height / 2.0D;
/*  51:    */     case 15: 
/*  52: 82 */       return height / 3.0D;
/*  53:    */     case 16: 
/*  54: 85 */       return height / 4.0D;
/*  55:    */     case 17: 
/*  56: 88 */       return height / 5.0D;
/*  57:    */     case 18: 
/*  58: 91 */       return height / 6.0D;
/*  59:    */     case 19: 
/*  60: 94 */       return height / 8.0D;
/*  61:    */     case 20: 
/*  62: 97 */       return Math.max(width, height);
/*  63:    */     case 21: 
/*  64:100 */       return ss;
/*  65:    */     case 22: 
/*  66:103 */       return ss / 2.0D;
/*  67:    */     case 23: 
/*  68:106 */       return ss / 4.0D;
/*  69:    */     case 24: 
/*  70:109 */       return ss / 6.0D;
/*  71:    */     case 25: 
/*  72:112 */       return ss / 8.0D;
/*  73:    */     case 26: 
/*  74:115 */       return ss / 16.0D;
/*  75:    */     case 27: 
/*  76:118 */       return ss / 32.0D;
/*  77:    */     case 28: 
/*  78:121 */       return anchor.getCenterY();
/*  79:    */     case 29: 
/*  80:124 */       return width;
/*  81:    */     case 30: 
/*  82:127 */       return width / 2.0D;
/*  83:    */     case 31: 
/*  84:130 */       return width / 3.0D;
/*  85:    */     case 32: 
/*  86:133 */       return width / 4.0D;
/*  87:    */     case 33: 
/*  88:136 */       return width / 5.0D;
/*  89:    */     case 34: 
/*  90:139 */       return width / 6.0D;
/*  91:    */     case 35: 
/*  92:142 */       return width / 8.0D;
/*  93:    */     case 36: 
/*  94:145 */       return width / 10.0D;
/*  95:    */     case 37: 
/*  96:148 */       return width / 32.0D;
/*  97:    */     }
/*  98:150 */     return 0.0D;
/*  99:    */   }
/* 100:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.geom.BuiltInGuide
 * JD-Core Version:    0.7.0.1
 */