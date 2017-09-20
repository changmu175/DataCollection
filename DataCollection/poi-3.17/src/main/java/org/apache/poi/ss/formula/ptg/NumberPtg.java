/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.util.NumberToTextConverter;
/*  4:   */ import org.apache.poi.util.LittleEndianInput;
/*  5:   */ import org.apache.poi.util.LittleEndianOutput;
/*  6:   */ 
/*  7:   */ public final class NumberPtg
/*  8:   */   extends ScalarConstantPtg
/*  9:   */ {
/* 10:   */   public static final int SIZE = 9;
/* 11:   */   public static final byte sid = 31;
/* 12:   */   private final double field_1_value;
/* 13:   */   
/* 14:   */   public NumberPtg(LittleEndianInput in)
/* 15:   */   {
/* 16:37 */     this(in.readDouble());
/* 17:   */   }
/* 18:   */   
/* 19:   */   public NumberPtg(String value)
/* 20:   */   {
/* 21:48 */     this(Double.parseDouble(value));
/* 22:   */   }
/* 23:   */   
/* 24:   */   public NumberPtg(double value)
/* 25:   */   {
/* 26:52 */     this.field_1_value = value;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public double getValue()
/* 30:   */   {
/* 31:56 */     return this.field_1_value;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void write(LittleEndianOutput out)
/* 35:   */   {
/* 36:60 */     out.writeByte(31 + getPtgClass());
/* 37:61 */     out.writeDouble(getValue());
/* 38:   */   }
/* 39:   */   
/* 40:   */   public int getSize()
/* 41:   */   {
/* 42:65 */     return 9;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public String toFormulaString()
/* 46:   */   {
/* 47:69 */     return NumberToTextConverter.toText(this.field_1_value);
/* 48:   */   }
/* 49:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.NumberPtg
 * JD-Core Version:    0.7.0.1
 */