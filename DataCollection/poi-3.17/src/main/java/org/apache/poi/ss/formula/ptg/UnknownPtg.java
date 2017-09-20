/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public class UnknownPtg
/*  6:   */   extends Ptg
/*  7:   */ {
/*  8:26 */   private short size = 1;
/*  9:   */   private final int _sid;
/* 10:   */   
/* 11:   */   public UnknownPtg(int sid)
/* 12:   */   {
/* 13:30 */     this._sid = sid;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public boolean isBaseToken()
/* 17:   */   {
/* 18:34 */     return true;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void write(LittleEndianOutput out)
/* 22:   */   {
/* 23:37 */     out.writeByte(this._sid);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public int getSize()
/* 27:   */   {
/* 28:41 */     return this.size;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public String toFormulaString()
/* 32:   */   {
/* 33:45 */     return "UNKNOWN";
/* 34:   */   }
/* 35:   */   
/* 36:   */   public byte getDefaultOperandClass()
/* 37:   */   {
/* 38:48 */     return 32;
/* 39:   */   }
/* 40:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.UnknownPtg
 * JD-Core Version:    0.7.0.1
 */