/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianInput;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class IntPtg
/*  7:   */   extends ScalarConstantPtg
/*  8:   */ {
/*  9:   */   private static final int MIN_VALUE = 0;
/* 10:   */   private static final int MAX_VALUE = 65535;
/* 11:   */   public static final int SIZE = 3;
/* 12:   */   public static final byte sid = 30;
/* 13:   */   private final int field_1_value;
/* 14:   */   
/* 15:   */   public static boolean isInRange(int i)
/* 16:   */   {
/* 17:42 */     return (i >= 0) && (i <= 65535);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public IntPtg(LittleEndianInput in)
/* 21:   */   {
/* 22:50 */     this(in.readUShort());
/* 23:   */   }
/* 24:   */   
/* 25:   */   public IntPtg(int value)
/* 26:   */   {
/* 27:54 */     if (!isInRange(value)) {
/* 28:55 */       throw new IllegalArgumentException("value is out of range: " + value);
/* 29:   */     }
/* 30:57 */     this.field_1_value = value;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public int getValue()
/* 34:   */   {
/* 35:61 */     return this.field_1_value;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public void write(LittleEndianOutput out)
/* 39:   */   {
/* 40:65 */     out.writeByte(30 + getPtgClass());
/* 41:66 */     out.writeShort(getValue());
/* 42:   */   }
/* 43:   */   
/* 44:   */   public int getSize()
/* 45:   */   {
/* 46:70 */     return 3;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public String toFormulaString()
/* 50:   */   {
/* 51:74 */     return String.valueOf(getValue());
/* 52:   */   }
/* 53:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.IntPtg
 * JD-Core Version:    0.7.0.1
 */