/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianInput;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class BoolPtg
/*  7:   */   extends ScalarConstantPtg
/*  8:   */ {
/*  9:   */   public static final int SIZE = 2;
/* 10:   */   public static final byte sid = 29;
/* 11:34 */   private static final BoolPtg FALSE = new BoolPtg(false);
/* 12:35 */   private static final BoolPtg TRUE = new BoolPtg(true);
/* 13:   */   private final boolean _value;
/* 14:   */   
/* 15:   */   private BoolPtg(boolean b)
/* 16:   */   {
/* 17:40 */     this._value = b;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public static BoolPtg valueOf(boolean b)
/* 21:   */   {
/* 22:44 */     return b ? TRUE : FALSE;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public static BoolPtg read(LittleEndianInput in)
/* 26:   */   {
/* 27:47 */     return valueOf(in.readByte() == 1);
/* 28:   */   }
/* 29:   */   
/* 30:   */   public boolean getValue()
/* 31:   */   {
/* 32:51 */     return this._value;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void write(LittleEndianOutput out)
/* 36:   */   {
/* 37:55 */     out.writeByte(29 + getPtgClass());
/* 38:56 */     out.writeByte(this._value ? 1 : 0);
/* 39:   */   }
/* 40:   */   
/* 41:   */   public int getSize()
/* 42:   */   {
/* 43:60 */     return 2;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public String toFormulaString()
/* 47:   */   {
/* 48:64 */     return this._value ? "TRUE" : "FALSE";
/* 49:   */   }
/* 50:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.BoolPtg
 * JD-Core Version:    0.7.0.1
 */