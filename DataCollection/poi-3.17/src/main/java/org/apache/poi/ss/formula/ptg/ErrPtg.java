/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.FormulaError;
/*  4:   */ import org.apache.poi.util.LittleEndianInput;
/*  5:   */ import org.apache.poi.util.LittleEndianOutput;
/*  6:   */ 
/*  7:   */ public final class ErrPtg
/*  8:   */   extends ScalarConstantPtg
/*  9:   */ {
/* 10:27 */   public static final ErrPtg NULL_INTERSECTION = new ErrPtg(FormulaError.NULL.getCode());
/* 11:29 */   public static final ErrPtg DIV_ZERO = new ErrPtg(FormulaError.DIV0.getCode());
/* 12:31 */   public static final ErrPtg VALUE_INVALID = new ErrPtg(FormulaError.VALUE.getCode());
/* 13:33 */   public static final ErrPtg REF_INVALID = new ErrPtg(FormulaError.REF.getCode());
/* 14:35 */   public static final ErrPtg NAME_INVALID = new ErrPtg(FormulaError.NAME.getCode());
/* 15:37 */   public static final ErrPtg NUM_ERROR = new ErrPtg(FormulaError.NUM.getCode());
/* 16:39 */   public static final ErrPtg N_A = new ErrPtg(FormulaError.NA.getCode());
/* 17:   */   public static final short sid = 28;
/* 18:   */   private static final int SIZE = 2;
/* 19:   */   private final int field_1_error_code;
/* 20:   */   
/* 21:   */   private ErrPtg(int errorCode)
/* 22:   */   {
/* 23:49 */     if (!FormulaError.isValidCode(errorCode)) {
/* 24:50 */       throw new IllegalArgumentException("Invalid error code (" + errorCode + ")");
/* 25:   */     }
/* 26:52 */     this.field_1_error_code = errorCode;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public static ErrPtg read(LittleEndianInput in)
/* 30:   */   {
/* 31:56 */     return valueOf(in.readByte());
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void write(LittleEndianOutput out)
/* 35:   */   {
/* 36:60 */     out.writeByte(28 + getPtgClass());
/* 37:61 */     out.writeByte(this.field_1_error_code);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public String toFormulaString()
/* 41:   */   {
/* 42:65 */     return FormulaError.forInt(this.field_1_error_code).getString();
/* 43:   */   }
/* 44:   */   
/* 45:   */   public int getSize()
/* 46:   */   {
/* 47:69 */     return 2;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public int getErrorCode()
/* 51:   */   {
/* 52:73 */     return this.field_1_error_code;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public static ErrPtg valueOf(int code)
/* 56:   */   {
/* 57:77 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.forInt(code).ordinal()])
/* 58:   */     {
/* 59:   */     case 1: 
/* 60:78 */       return DIV_ZERO;
/* 61:   */     case 2: 
/* 62:79 */       return N_A;
/* 63:   */     case 3: 
/* 64:80 */       return NAME_INVALID;
/* 65:   */     case 4: 
/* 66:81 */       return NULL_INTERSECTION;
/* 67:   */     case 5: 
/* 68:82 */       return NUM_ERROR;
/* 69:   */     case 6: 
/* 70:83 */       return REF_INVALID;
/* 71:   */     case 7: 
/* 72:84 */       return VALUE_INVALID;
/* 73:   */     }
/* 74:86 */     throw new RuntimeException("Unexpected error code (" + code + ")");
/* 75:   */   }
/* 76:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.ErrPtg
 * JD-Core Version:    0.7.0.1
 */