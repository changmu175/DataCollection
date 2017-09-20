/*  1:   */ package org.apache.poi.hssf.model;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ddf.EscherChildAnchorRecord;
/*  4:   */ import org.apache.poi.ddf.EscherClientAnchorRecord;
/*  5:   */ import org.apache.poi.ddf.EscherRecord;
/*  6:   */ import org.apache.poi.hssf.usermodel.HSSFAnchor;
/*  7:   */ import org.apache.poi.hssf.usermodel.HSSFChildAnchor;
/*  8:   */ import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
/*  9:   */ import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
/* 10:   */ 
/* 11:   */ public class ConvertAnchor
/* 12:   */ {
/* 13:   */   public static EscherRecord createAnchor(HSSFAnchor userAnchor)
/* 14:   */   {
/* 15:34 */     if ((userAnchor instanceof HSSFClientAnchor))
/* 16:   */     {
/* 17:36 */       HSSFClientAnchor a = (HSSFClientAnchor)userAnchor;
/* 18:   */       
/* 19:38 */       EscherClientAnchorRecord anchor = new EscherClientAnchorRecord();
/* 20:39 */       anchor.setRecordId((short)-4080);
/* 21:40 */       anchor.setOptions((short)0);
/* 22:41 */       anchor.setFlag(a.getAnchorType().value);
/* 23:42 */       anchor.setCol1((short)Math.min(a.getCol1(), a.getCol2()));
/* 24:43 */       anchor.setDx1((short)a.getDx1());
/* 25:44 */       anchor.setRow1((short)Math.min(a.getRow1(), a.getRow2()));
/* 26:45 */       anchor.setDy1((short)a.getDy1());
/* 27:   */       
/* 28:47 */       anchor.setCol2((short)Math.max(a.getCol1(), a.getCol2()));
/* 29:48 */       anchor.setDx2((short)a.getDx2());
/* 30:49 */       anchor.setRow2((short)Math.max(a.getRow1(), a.getRow2()));
/* 31:50 */       anchor.setDy2((short)a.getDy2());
/* 32:51 */       return anchor;
/* 33:   */     }
/* 34:53 */     HSSFChildAnchor a = (HSSFChildAnchor)userAnchor;
/* 35:54 */     EscherChildAnchorRecord anchor = new EscherChildAnchorRecord();
/* 36:55 */     anchor.setRecordId((short)-4081);
/* 37:56 */     anchor.setOptions((short)0);
/* 38:57 */     anchor.setDx1((short)Math.min(a.getDx1(), a.getDx2()));
/* 39:58 */     anchor.setDy1((short)Math.min(a.getDy1(), a.getDy2()));
/* 40:59 */     anchor.setDx2((short)Math.max(a.getDx2(), a.getDx1()));
/* 41:60 */     anchor.setDy2((short)Math.max(a.getDy2(), a.getDy1()));
/* 42:61 */     return anchor;
/* 43:   */   }
/* 44:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.model.ConvertAnchor
 * JD-Core Version:    0.7.0.1
 */