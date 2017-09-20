/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianInput;
/*  4:   */ 
/*  5:   */ public final class RefNPtg
/*  6:   */   extends Ref2DPtgBase
/*  7:   */ {
/*  8:   */   public static final byte sid = 44;
/*  9:   */   
/* 10:   */   public RefNPtg(LittleEndianInput in)
/* 11:   */   {
/* 12:30 */     super(in);
/* 13:   */   }
/* 14:   */   
/* 15:   */   protected byte getSid()
/* 16:   */   {
/* 17:34 */     return 44;
/* 18:   */   }
/* 19:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.RefNPtg
 * JD-Core Version:    0.7.0.1
 */