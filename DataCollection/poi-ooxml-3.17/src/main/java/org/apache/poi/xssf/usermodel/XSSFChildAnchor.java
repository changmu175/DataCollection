/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
/*  5:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
/*  6:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
/*  7:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D.Factory;
/*  8:   */ 
/*  9:   */ public final class XSSFChildAnchor
/* 10:   */   extends XSSFAnchor
/* 11:   */ {
/* 12:   */   private CTTransform2D t2d;
/* 13:   */   
/* 14:   */   public XSSFChildAnchor(int x, int y, int cx, int cy)
/* 15:   */   {
/* 16:32 */     this.t2d = CTTransform2D.Factory.newInstance();
/* 17:33 */     CTPoint2D off = this.t2d.addNewOff();
/* 18:34 */     CTPositiveSize2D ext = this.t2d.addNewExt();
/* 19:   */     
/* 20:36 */     off.setX(x);
/* 21:37 */     off.setY(y);
/* 22:38 */     ext.setCx(Math.abs(cx - x));
/* 23:39 */     ext.setCy(Math.abs(cy - y));
/* 24:40 */     if (x > cx) {
/* 25:40 */       this.t2d.setFlipH(true);
/* 26:   */     }
/* 27:41 */     if (y > cy) {
/* 28:41 */       this.t2d.setFlipV(true);
/* 29:   */     }
/* 30:   */   }
/* 31:   */   
/* 32:   */   public XSSFChildAnchor(CTTransform2D t2d)
/* 33:   */   {
/* 34:45 */     this.t2d = t2d;
/* 35:   */   }
/* 36:   */   
/* 37:   */   @Internal
/* 38:   */   public CTTransform2D getCTTransform2D()
/* 39:   */   {
/* 40:50 */     return this.t2d;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public int getDx1()
/* 44:   */   {
/* 45:54 */     return (int)this.t2d.getOff().getX();
/* 46:   */   }
/* 47:   */   
/* 48:   */   public void setDx1(int dx1)
/* 49:   */   {
/* 50:58 */     this.t2d.getOff().setX(dx1);
/* 51:   */   }
/* 52:   */   
/* 53:   */   public int getDy1()
/* 54:   */   {
/* 55:62 */     return (int)this.t2d.getOff().getY();
/* 56:   */   }
/* 57:   */   
/* 58:   */   public void setDy1(int dy1)
/* 59:   */   {
/* 60:66 */     this.t2d.getOff().setY(dy1);
/* 61:   */   }
/* 62:   */   
/* 63:   */   public int getDy2()
/* 64:   */   {
/* 65:70 */     return (int)(getDy1() + this.t2d.getExt().getCy());
/* 66:   */   }
/* 67:   */   
/* 68:   */   public void setDy2(int dy2)
/* 69:   */   {
/* 70:74 */     this.t2d.getExt().setCy(dy2 - getDy1());
/* 71:   */   }
/* 72:   */   
/* 73:   */   public int getDx2()
/* 74:   */   {
/* 75:78 */     return (int)(getDx1() + this.t2d.getExt().getCx());
/* 76:   */   }
/* 77:   */   
/* 78:   */   public void setDx2(int dx2)
/* 79:   */   {
/* 80:82 */     this.t2d.getExt().setCx(dx2 - getDx1());
/* 81:   */   }
/* 82:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFChildAnchor
 * JD-Core Version:    0.7.0.1
 */