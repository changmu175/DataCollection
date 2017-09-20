/*  1:   */ package org.apache.poi.hssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ddf.EscherChildAnchorRecord;
/*  4:   */ import org.apache.poi.ddf.EscherClientAnchorRecord;
/*  5:   */ import org.apache.poi.ddf.EscherContainerRecord;
/*  6:   */ import org.apache.poi.ddf.EscherRecord;
/*  7:   */ import org.apache.poi.ss.usermodel.ChildAnchor;
/*  8:   */ 
/*  9:   */ public abstract class HSSFAnchor
/* 10:   */   implements ChildAnchor
/* 11:   */ {
/* 12:33 */   protected boolean _isHorizontallyFlipped = false;
/* 13:34 */   protected boolean _isVerticallyFlipped = false;
/* 14:   */   
/* 15:   */   public HSSFAnchor()
/* 16:   */   {
/* 17:37 */     createEscherAnchor();
/* 18:   */   }
/* 19:   */   
/* 20:   */   public HSSFAnchor(int dx1, int dy1, int dx2, int dy2)
/* 21:   */   {
/* 22:41 */     createEscherAnchor();
/* 23:42 */     setDx1(dx1);
/* 24:43 */     setDy1(dy1);
/* 25:44 */     setDx2(dx2);
/* 26:45 */     setDy2(dy2);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public static HSSFAnchor createAnchorFromEscher(EscherContainerRecord container)
/* 30:   */   {
/* 31:49 */     if (null != container.getChildById((short)-4081)) {
/* 32:50 */       return new HSSFChildAnchor((EscherChildAnchorRecord)container.getChildById((short)-4081));
/* 33:   */     }
/* 34:52 */     if (null != container.getChildById((short)-4080)) {
/* 35:53 */       return new HSSFClientAnchor((EscherClientAnchorRecord)container.getChildById((short)-4080));
/* 36:   */     }
/* 37:55 */     return null;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public abstract boolean isHorizontallyFlipped();
/* 41:   */   
/* 42:   */   public abstract boolean isVerticallyFlipped();
/* 43:   */   
/* 44:   */   protected abstract EscherRecord getEscherAnchor();
/* 45:   */   
/* 46:   */   protected abstract void createEscherAnchor();
/* 47:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFAnchor
 * JD-Core Version:    0.7.0.1
 */