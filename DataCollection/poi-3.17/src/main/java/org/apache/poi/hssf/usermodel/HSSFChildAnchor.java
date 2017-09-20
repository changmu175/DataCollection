/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ddf.EscherChildAnchorRecord;
/*   4:    */ import org.apache.poi.ddf.EscherRecord;
/*   5:    */ 
/*   6:    */ public final class HSSFChildAnchor
/*   7:    */   extends HSSFAnchor
/*   8:    */ {
/*   9:    */   private EscherChildAnchorRecord _escherChildAnchor;
/*  10:    */   
/*  11:    */   public HSSFChildAnchor(EscherChildAnchorRecord escherChildAnchorRecord)
/*  12:    */   {
/*  13: 33 */     this._escherChildAnchor = escherChildAnchorRecord;
/*  14:    */   }
/*  15:    */   
/*  16:    */   public HSSFChildAnchor()
/*  17:    */   {
/*  18: 37 */     this._escherChildAnchor = new EscherChildAnchorRecord();
/*  19:    */   }
/*  20:    */   
/*  21:    */   public HSSFChildAnchor(int dx1, int dy1, int dx2, int dy2)
/*  22:    */   {
/*  23: 48 */     super(Math.min(dx1, dx2), Math.min(dy1, dy2), Math.max(dx1, dx2), Math.max(dy1, dy2));
/*  24: 49 */     if (dx1 > dx2) {
/*  25: 50 */       this._isHorizontallyFlipped = true;
/*  26:    */     }
/*  27: 52 */     if (dy1 > dy2) {
/*  28: 53 */       this._isVerticallyFlipped = true;
/*  29:    */     }
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int getDx1()
/*  33:    */   {
/*  34: 59 */     return this._escherChildAnchor.getDx1();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setDx1(int dx1)
/*  38:    */   {
/*  39: 64 */     this._escherChildAnchor.setDx1(dx1);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int getDy1()
/*  43:    */   {
/*  44: 69 */     return this._escherChildAnchor.getDy1();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setDy1(int dy1)
/*  48:    */   {
/*  49: 74 */     this._escherChildAnchor.setDy1(dy1);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int getDy2()
/*  53:    */   {
/*  54: 79 */     return this._escherChildAnchor.getDy2();
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setDy2(int dy2)
/*  58:    */   {
/*  59: 84 */     this._escherChildAnchor.setDy2(dy2);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public int getDx2()
/*  63:    */   {
/*  64: 89 */     return this._escherChildAnchor.getDx2();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setDx2(int dx2)
/*  68:    */   {
/*  69: 94 */     this._escherChildAnchor.setDx2(dx2);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setAnchor(int dx1, int dy1, int dx2, int dy2)
/*  73:    */   {
/*  74:104 */     setDx1(Math.min(dx1, dx2));
/*  75:105 */     setDy1(Math.min(dy1, dy2));
/*  76:106 */     setDx2(Math.max(dx1, dx2));
/*  77:107 */     setDy2(Math.max(dy1, dy2));
/*  78:    */   }
/*  79:    */   
/*  80:    */   public boolean isHorizontallyFlipped()
/*  81:    */   {
/*  82:112 */     return this._isHorizontallyFlipped;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public boolean isVerticallyFlipped()
/*  86:    */   {
/*  87:117 */     return this._isVerticallyFlipped;
/*  88:    */   }
/*  89:    */   
/*  90:    */   protected EscherRecord getEscherAnchor()
/*  91:    */   {
/*  92:122 */     return this._escherChildAnchor;
/*  93:    */   }
/*  94:    */   
/*  95:    */   protected void createEscherAnchor()
/*  96:    */   {
/*  97:127 */     this._escherChildAnchor = new EscherChildAnchorRecord();
/*  98:    */   }
/*  99:    */   
/* 100:    */   public boolean equals(Object obj)
/* 101:    */   {
/* 102:132 */     if (obj == null) {
/* 103:133 */       return false;
/* 104:    */     }
/* 105:134 */     if (obj == this) {
/* 106:135 */       return true;
/* 107:    */     }
/* 108:136 */     if (obj.getClass() != getClass()) {
/* 109:137 */       return false;
/* 110:    */     }
/* 111:138 */     HSSFChildAnchor anchor = (HSSFChildAnchor)obj;
/* 112:    */     
/* 113:140 */     return (anchor.getDx1() == getDx1()) && (anchor.getDx2() == getDx2()) && (anchor.getDy1() == getDy1()) && (anchor.getDy2() == getDy2());
/* 114:    */   }
/* 115:    */   
/* 116:    */   public int hashCode()
/* 117:    */   {
/* 118:146 */     if (!$assertionsDisabled) {
/* 119:146 */       throw new AssertionError("hashCode not designed");
/* 120:    */     }
/* 121:147 */     return 42;
/* 122:    */   }
/* 123:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFChildAnchor
 * JD-Core Version:    0.7.0.1
 */