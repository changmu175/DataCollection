/*   1:    */ package org.apache.poi.ss.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   4:    */ import org.apache.poi.util.Removal;
/*   5:    */ 
/*   6:    */ public final class CellValue
/*   7:    */ {
/*   8: 29 */   public static final CellValue TRUE = new CellValue(CellType.BOOLEAN, 0.0D, true, null, 0);
/*   9: 30 */   public static final CellValue FALSE = new CellValue(CellType.BOOLEAN, 0.0D, false, null, 0);
/*  10:    */   private final CellType _cellType;
/*  11:    */   private final double _numberValue;
/*  12:    */   private final boolean _booleanValue;
/*  13:    */   private final String _textValue;
/*  14:    */   private final int _errorCode;
/*  15:    */   
/*  16:    */   private CellValue(CellType cellType, double numberValue, boolean booleanValue, String textValue, int errorCode)
/*  17:    */   {
/*  18: 40 */     this._cellType = cellType;
/*  19: 41 */     this._numberValue = numberValue;
/*  20: 42 */     this._booleanValue = booleanValue;
/*  21: 43 */     this._textValue = textValue;
/*  22: 44 */     this._errorCode = errorCode;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public CellValue(double numberValue)
/*  26:    */   {
/*  27: 49 */     this(CellType.NUMERIC, numberValue, false, null, 0);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static CellValue valueOf(boolean booleanValue)
/*  31:    */   {
/*  32: 53 */     return booleanValue ? TRUE : FALSE;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public CellValue(String stringValue)
/*  36:    */   {
/*  37: 57 */     this(CellType.STRING, 0.0D, false, stringValue, 0);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static CellValue getError(int errorCode)
/*  41:    */   {
/*  42: 61 */     return new CellValue(CellType.ERROR, 0.0D, false, null, errorCode);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public boolean getBooleanValue()
/*  46:    */   {
/*  47: 69 */     return this._booleanValue;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public double getNumberValue()
/*  51:    */   {
/*  52: 76 */     return this._numberValue;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public String getStringValue()
/*  56:    */   {
/*  57: 83 */     return this._textValue;
/*  58:    */   }
/*  59:    */   
/*  60:    */   @Removal(version="4.2")
/*  61:    */   public CellType getCellTypeEnum()
/*  62:    */   {
/*  63: 95 */     return this._cellType;
/*  64:    */   }
/*  65:    */   
/*  66:    */   @Deprecated
/*  67:    */   public int getCellType()
/*  68:    */   {
/*  69:110 */     return this._cellType.getCode();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public byte getErrorValue()
/*  73:    */   {
/*  74:117 */     return (byte)this._errorCode;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String toString()
/*  78:    */   {
/*  79:121 */     StringBuffer sb = new StringBuffer(64);
/*  80:122 */     sb.append(getClass().getName()).append(" [");
/*  81:123 */     sb.append(formatAsString());
/*  82:124 */     sb.append("]");
/*  83:125 */     return sb.toString();
/*  84:    */   }
/*  85:    */   
/*  86:    */   public String formatAsString()
/*  87:    */   {
/*  88:129 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()])
/*  89:    */     {
/*  90:    */     case 1: 
/*  91:131 */       return String.valueOf(this._numberValue);
/*  92:    */     case 2: 
/*  93:133 */       return '"' + this._textValue + '"';
/*  94:    */     case 3: 
/*  95:135 */       return this._booleanValue ? "TRUE" : "FALSE";
/*  96:    */     case 4: 
/*  97:137 */       return ErrorEval.getText(this._errorCode);
/*  98:    */     }
/*  99:139 */     return "<error unexpected cell type " + this._cellType + ">";
/* 100:    */   }
/* 101:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.CellValue
 * JD-Core Version:    0.7.0.1
 */