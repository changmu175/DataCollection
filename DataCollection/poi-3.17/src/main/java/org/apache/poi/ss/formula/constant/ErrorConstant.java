/*  1:   */ package org.apache.poi.ss.formula.constant;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.FormulaError;
/*  4:   */ import org.apache.poi.util.POILogFactory;
/*  5:   */ import org.apache.poi.util.POILogger;
/*  6:   */ 
/*  7:   */ public class ErrorConstant
/*  8:   */ {
/*  9:30 */   private static final POILogger logger = POILogFactory.getLogger(ErrorConstant.class);
/* 10:31 */   private static final ErrorConstant NULL = new ErrorConstant(FormulaError.NULL.getCode());
/* 11:32 */   private static final ErrorConstant DIV_0 = new ErrorConstant(FormulaError.DIV0.getCode());
/* 12:33 */   private static final ErrorConstant VALUE = new ErrorConstant(FormulaError.VALUE.getCode());
/* 13:34 */   private static final ErrorConstant REF = new ErrorConstant(FormulaError.REF.getCode());
/* 14:35 */   private static final ErrorConstant NAME = new ErrorConstant(FormulaError.NAME.getCode());
/* 15:36 */   private static final ErrorConstant NUM = new ErrorConstant(FormulaError.NUM.getCode());
/* 16:37 */   private static final ErrorConstant NA = new ErrorConstant(FormulaError.NA.getCode());
/* 17:   */   private final int _errorCode;
/* 18:   */   
/* 19:   */   private ErrorConstant(int errorCode)
/* 20:   */   {
/* 21:42 */     this._errorCode = errorCode;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public int getErrorCode()
/* 25:   */   {
/* 26:46 */     return this._errorCode;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String getText()
/* 30:   */   {
/* 31:50 */     if (FormulaError.isValidCode(this._errorCode)) {
/* 32:51 */       return FormulaError.forInt(this._errorCode).getString();
/* 33:   */     }
/* 34:53 */     return "unknown error code (" + this._errorCode + ")";
/* 35:   */   }
/* 36:   */   
/* 37:   */   public static ErrorConstant valueOf(int errorCode)
/* 38:   */   {
/* 39:57 */     if (FormulaError.isValidCode(errorCode)) {
/* 40:58 */       switch (1.$SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.forInt(errorCode).ordinal()])
/* 41:   */       {
/* 42:   */       case 1: 
/* 43:59 */         return NULL;
/* 44:   */       case 2: 
/* 45:60 */         return DIV_0;
/* 46:   */       case 3: 
/* 47:61 */         return VALUE;
/* 48:   */       case 4: 
/* 49:62 */         return REF;
/* 50:   */       case 5: 
/* 51:63 */         return NAME;
/* 52:   */       case 6: 
/* 53:64 */         return NUM;
/* 54:   */       case 7: 
/* 55:65 */         return NA;
/* 56:   */       }
/* 57:   */     }
/* 58:69 */     logger.log(5, new Object[] { "Warning - unexpected error code (" + errorCode + ")" });
/* 59:70 */     return new ErrorConstant(errorCode);
/* 60:   */   }
/* 61:   */   
/* 62:   */   public String toString()
/* 63:   */   {
/* 64:74 */     StringBuffer sb = new StringBuffer(64);
/* 65:75 */     sb.append(getClass().getName()).append(" [");
/* 66:76 */     sb.append(getText());
/* 67:77 */     sb.append("]");
/* 68:78 */     return sb.toString();
/* 69:   */   }
/* 70:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.constant.ErrorConstant
 * JD-Core Version:    0.7.0.1
 */