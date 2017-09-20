/*   1:    */ package org.apache.poi.sl.usermodel;
/*   2:    */ 
/*   3:    */ public abstract interface StrokeStyle
/*   4:    */ {
/*   5:    */   public abstract PaintStyle getPaint();
/*   6:    */   
/*   7:    */   public abstract LineCap getLineCap();
/*   8:    */   
/*   9:    */   public abstract LineDash getLineDash();
/*  10:    */   
/*  11:    */   public abstract LineCompound getLineCompound();
/*  12:    */   
/*  13:    */   public abstract double getLineWidth();
/*  14:    */   
/*  15:    */   public static enum LineCap
/*  16:    */   {
/*  17: 26 */     ROUND(0, 1),  SQUARE(1, 2),  FLAT(2, 3);
/*  18:    */     
/*  19:    */     public final int nativeId;
/*  20:    */     public final int ooxmlId;
/*  21:    */     
/*  22:    */     private LineCap(int nativeId, int ooxmlId)
/*  23:    */     {
/*  24: 36 */       this.nativeId = nativeId;
/*  25: 37 */       this.ooxmlId = ooxmlId;
/*  26:    */     }
/*  27:    */     
/*  28:    */     public static LineCap fromNativeId(int nativeId)
/*  29:    */     {
/*  30: 41 */       for (LineCap ld : ) {
/*  31: 42 */         if (ld.nativeId == nativeId) {
/*  32: 42 */           return ld;
/*  33:    */         }
/*  34:    */       }
/*  35: 44 */       return null;
/*  36:    */     }
/*  37:    */     
/*  38:    */     public static LineCap fromOoxmlId(int ooxmlId)
/*  39:    */     {
/*  40: 48 */       for (LineCap lc : ) {
/*  41: 49 */         if (lc.ooxmlId == ooxmlId) {
/*  42: 49 */           return lc;
/*  43:    */         }
/*  44:    */       }
/*  45: 51 */       return null;
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public static enum LineDash
/*  50:    */   {
/*  51: 62 */     SOLID(1, 1, null),  DOT(6, 2, new int[] { 1, 1 }),  DASH(7, 3, new int[] { 3, 4 }),  DASH_DOT(9, 5, new int[] { 4, 3, 1, 3 }),  LG_DASH(8, 4, new int[] { 8, 3 }),  LG_DASH_DOT(10, 6, new int[] { 8, 3, 1, 3 }),  LG_DASH_DOT_DOT(11, 7, new int[] { 8, 3, 1, 3, 1, 3 }),  SYS_DASH(2, 8, new int[] { 2, 2 }),  SYS_DOT(3, 9, new int[] { 1, 1 }),  SYS_DASH_DOT(4, 10, new int[] { 2, 2, 1, 1 }),  SYS_DASH_DOT_DOT(5, 11, new int[] { 2, 2, 1, 1, 1, 1 });
/*  52:    */     
/*  53:    */     public final int[] pattern;
/*  54:    */     public final int nativeId;
/*  55:    */     public final int ooxmlId;
/*  56:    */     
/*  57:    */     private LineDash(int nativeId, int ooxmlId, int... pattern)
/*  58:    */     {
/*  59: 89 */       this.nativeId = nativeId;
/*  60: 90 */       this.ooxmlId = ooxmlId;
/*  61: 91 */       this.pattern = ((pattern == null) || (pattern.length == 0) ? null : pattern);
/*  62:    */     }
/*  63:    */     
/*  64:    */     public static LineDash fromNativeId(int nativeId)
/*  65:    */     {
/*  66: 95 */       for (LineDash ld : ) {
/*  67: 96 */         if (ld.nativeId == nativeId) {
/*  68: 96 */           return ld;
/*  69:    */         }
/*  70:    */       }
/*  71: 98 */       return null;
/*  72:    */     }
/*  73:    */     
/*  74:    */     public static LineDash fromOoxmlId(int ooxmlId)
/*  75:    */     {
/*  76:102 */       for (LineDash ld : ) {
/*  77:103 */         if (ld.ooxmlId == ooxmlId) {
/*  78:103 */           return ld;
/*  79:    */         }
/*  80:    */       }
/*  81:105 */       return null;
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   public static enum LineCompound
/*  86:    */   {
/*  87:111 */     SINGLE(0, 1),  DOUBLE(1, 2),  THICK_THIN(2, 3),  THIN_THICK(3, 4),  TRIPLE(4, 5);
/*  88:    */     
/*  89:    */     public final int nativeId;
/*  90:    */     public final int ooxmlId;
/*  91:    */     
/*  92:    */     private LineCompound(int nativeId, int ooxmlId)
/*  93:    */     {
/*  94:125 */       this.nativeId = nativeId;
/*  95:126 */       this.ooxmlId = ooxmlId;
/*  96:    */     }
/*  97:    */     
/*  98:    */     public static LineCompound fromNativeId(int nativeId)
/*  99:    */     {
/* 100:130 */       for (LineCompound lc : ) {
/* 101:131 */         if (lc.nativeId == nativeId) {
/* 102:131 */           return lc;
/* 103:    */         }
/* 104:    */       }
/* 105:133 */       return null;
/* 106:    */     }
/* 107:    */     
/* 108:    */     public static LineCompound fromOoxmlId(int ooxmlId)
/* 109:    */     {
/* 110:137 */       for (LineCompound lc : ) {
/* 111:138 */         if (lc.ooxmlId == ooxmlId) {
/* 112:138 */           return lc;
/* 113:    */         }
/* 114:    */       }
/* 115:140 */       return null;
/* 116:    */     }
/* 117:    */   }
/* 118:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.StrokeStyle
 * JD-Core Version:    0.7.0.1
 */