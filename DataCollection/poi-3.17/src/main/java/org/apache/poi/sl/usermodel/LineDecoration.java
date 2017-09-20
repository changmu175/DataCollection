/*  1:   */ package org.apache.poi.sl.usermodel;
/*  2:   */ 
/*  3:   */ public abstract interface LineDecoration
/*  4:   */ {
/*  5:   */   public abstract DecorationShape getHeadShape();
/*  6:   */   
/*  7:   */   public abstract DecorationSize getHeadWidth();
/*  8:   */   
/*  9:   */   public abstract DecorationSize getHeadLength();
/* 10:   */   
/* 11:   */   public abstract DecorationShape getTailShape();
/* 12:   */   
/* 13:   */   public abstract DecorationSize getTailWidth();
/* 14:   */   
/* 15:   */   public abstract DecorationSize getTailLength();
/* 16:   */   
/* 17:   */   public static enum DecorationShape
/* 18:   */   {
/* 19:25 */     NONE(0, 1),  TRIANGLE(1, 2),  STEALTH(2, 3),  DIAMOND(3, 4),  OVAL(4, 5),  ARROW(5, 6);
/* 20:   */     
/* 21:   */     public final int nativeId;
/* 22:   */     public final int ooxmlId;
/* 23:   */     
/* 24:   */     private DecorationShape(int nativeId, int ooxmlId)
/* 25:   */     {
/* 26:36 */       this.nativeId = nativeId;
/* 27:37 */       this.ooxmlId = ooxmlId;
/* 28:   */     }
/* 29:   */     
/* 30:   */     public static DecorationShape fromNativeId(int nativeId)
/* 31:   */     {
/* 32:41 */       for (DecorationShape ld : ) {
/* 33:42 */         if (ld.nativeId == nativeId) {
/* 34:42 */           return ld;
/* 35:   */         }
/* 36:   */       }
/* 37:44 */       return null;
/* 38:   */     }
/* 39:   */     
/* 40:   */     public static DecorationShape fromOoxmlId(int ooxmlId)
/* 41:   */     {
/* 42:48 */       for (DecorationShape ds : ) {
/* 43:49 */         if (ds.ooxmlId == ooxmlId) {
/* 44:49 */           return ds;
/* 45:   */         }
/* 46:   */       }
/* 47:51 */       return null;
/* 48:   */     }
/* 49:   */   }
/* 50:   */   
/* 51:   */   public static enum DecorationSize
/* 52:   */   {
/* 53:56 */     SMALL(0, 1),  MEDIUM(1, 2),  LARGE(2, 3);
/* 54:   */     
/* 55:   */     public final int nativeId;
/* 56:   */     public final int ooxmlId;
/* 57:   */     
/* 58:   */     private DecorationSize(int nativeId, int ooxmlId)
/* 59:   */     {
/* 60:64 */       this.nativeId = nativeId;
/* 61:65 */       this.ooxmlId = ooxmlId;
/* 62:   */     }
/* 63:   */     
/* 64:   */     public static DecorationSize fromNativeId(int nativeId)
/* 65:   */     {
/* 66:69 */       for (DecorationSize ld : ) {
/* 67:70 */         if (ld.nativeId == nativeId) {
/* 68:70 */           return ld;
/* 69:   */         }
/* 70:   */       }
/* 71:72 */       return null;
/* 72:   */     }
/* 73:   */     
/* 74:   */     public static DecorationSize fromOoxmlId(int ooxmlId)
/* 75:   */     {
/* 76:76 */       for (DecorationSize ds : ) {
/* 77:77 */         if (ds.ooxmlId == ooxmlId) {
/* 78:77 */           return ds;
/* 79:   */         }
/* 80:   */       }
/* 81:79 */       return null;
/* 82:   */     }
/* 83:   */   }
/* 84:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.LineDecoration
 * JD-Core Version:    0.7.0.1
 */