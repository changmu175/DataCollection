/*  1:   */ package org.apache.poi.xdgf.usermodel.shape;
/*  2:   */ 
/*  3:   */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*  4:   */ 
/*  5:   */ public class ShapeDataAcceptor
/*  6:   */   implements ShapeVisitorAcceptor
/*  7:   */ {
/*  8:   */   public boolean accept(XDGFShape shape)
/*  9:   */   {
/* 10:37 */     if (shape.isDeleted()) {
/* 11:38 */       return false;
/* 12:   */     }
/* 13:41 */     if ((shape.hasText()) && (shape.getTextAsString().length() != 0)) {
/* 14:42 */       return true;
/* 15:   */     }
/* 16:45 */     if (shape.isShape1D()) {
/* 17:46 */       return true;
/* 18:   */     }
/* 19:49 */     if ((!shape.hasMaster()) && (!shape.hasMasterShape())) {
/* 20:50 */       return true;
/* 21:   */     }
/* 22:52 */     if ((shape.hasMaster()) && (!shape.hasMasterShape())) {
/* 23:53 */       return true;
/* 24:   */     }
/* 25:59 */     if ((shape.hasMasterShape()) && (shape.getMasterShape().isTopmost())) {
/* 26:60 */       return true;
/* 27:   */     }
/* 28:62 */     return false;
/* 29:   */   }
/* 30:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.shape.ShapeDataAcceptor
 * JD-Core Version:    0.7.0.1
 */