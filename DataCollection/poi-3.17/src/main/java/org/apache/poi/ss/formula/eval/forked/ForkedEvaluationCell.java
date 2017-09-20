/*   1:    */ package org.apache.poi.ss.formula.eval.forked;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.EvaluationCell;
/*   4:    */ import org.apache.poi.ss.formula.EvaluationSheet;
/*   5:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  10:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  11:    */ import org.apache.poi.ss.usermodel.Cell;
/*  12:    */ import org.apache.poi.ss.usermodel.CellType;
/*  13:    */ 
/*  14:    */ final class ForkedEvaluationCell
/*  15:    */   implements EvaluationCell
/*  16:    */ {
/*  17:    */   private final EvaluationSheet _sheet;
/*  18:    */   private final EvaluationCell _masterCell;
/*  19:    */   private boolean _booleanValue;
/*  20:    */   private CellType _cellType;
/*  21:    */   private int _errorValue;
/*  22:    */   private double _numberValue;
/*  23:    */   private String _stringValue;
/*  24:    */   
/*  25:    */   public ForkedEvaluationCell(ForkedEvaluationSheet sheet, EvaluationCell masterCell)
/*  26:    */   {
/*  27: 49 */     this._sheet = sheet;
/*  28: 50 */     this._masterCell = masterCell;
/*  29:    */     
/*  30: 52 */     setValue(BlankEval.instance);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public Object getIdentityKey()
/*  34:    */   {
/*  35: 57 */     return this._masterCell.getIdentityKey();
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setValue(ValueEval value)
/*  39:    */   {
/*  40: 61 */     Class<? extends ValueEval> cls = value.getClass();
/*  41: 63 */     if (cls == NumberEval.class)
/*  42:    */     {
/*  43: 64 */       this._cellType = CellType.NUMERIC;
/*  44: 65 */       this._numberValue = ((NumberEval)value).getNumberValue();
/*  45: 66 */       return;
/*  46:    */     }
/*  47: 68 */     if (cls == StringEval.class)
/*  48:    */     {
/*  49: 69 */       this._cellType = CellType.STRING;
/*  50: 70 */       this._stringValue = ((StringEval)value).getStringValue();
/*  51: 71 */       return;
/*  52:    */     }
/*  53: 73 */     if (cls == BoolEval.class)
/*  54:    */     {
/*  55: 74 */       this._cellType = CellType.BOOLEAN;
/*  56: 75 */       this._booleanValue = ((BoolEval)value).getBooleanValue();
/*  57: 76 */       return;
/*  58:    */     }
/*  59: 78 */     if (cls == ErrorEval.class)
/*  60:    */     {
/*  61: 79 */       this._cellType = CellType.ERROR;
/*  62: 80 */       this._errorValue = ((ErrorEval)value).getErrorCode();
/*  63: 81 */       return;
/*  64:    */     }
/*  65: 83 */     if (cls == BlankEval.class)
/*  66:    */     {
/*  67: 84 */       this._cellType = CellType.BLANK;
/*  68: 85 */       return;
/*  69:    */     }
/*  70: 87 */     throw new IllegalArgumentException("Unexpected value class (" + cls.getName() + ")");
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void copyValue(Cell destCell)
/*  74:    */   {
/*  75: 90 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()])
/*  76:    */     {
/*  77:    */     case 1: 
/*  78: 91 */       destCell.setCellType(CellType.BLANK);return;
/*  79:    */     case 2: 
/*  80: 92 */       destCell.setCellValue(this._numberValue);return;
/*  81:    */     case 3: 
/*  82: 93 */       destCell.setCellValue(this._booleanValue);return;
/*  83:    */     case 4: 
/*  84: 94 */       destCell.setCellValue(this._stringValue);return;
/*  85:    */     case 5: 
/*  86: 95 */       destCell.setCellErrorValue((byte)this._errorValue);return;
/*  87:    */     }
/*  88: 96 */     throw new IllegalStateException("Unexpected data type (" + this._cellType + ")");
/*  89:    */   }
/*  90:    */   
/*  91:    */   private void checkCellType(CellType expectedCellType)
/*  92:    */   {
/*  93:101 */     if (this._cellType != expectedCellType) {
/*  94:102 */       throw new RuntimeException("Wrong data type (" + this._cellType + ")");
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   /**
/*  99:    */    * @deprecated
/* 100:    */    */
/* 101:    */   public int getCellType()
/* 102:    */   {
/* 103:114 */     return this._cellType.getCode();
/* 104:    */   }
/* 105:    */   
/* 106:    */   /**
/* 107:    */    * @deprecated
/* 108:    */    */
/* 109:    */   public CellType getCellTypeEnum()
/* 110:    */   {
/* 111:123 */     return this._cellType;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public boolean getBooleanCellValue()
/* 115:    */   {
/* 116:127 */     checkCellType(CellType.BOOLEAN);
/* 117:128 */     return this._booleanValue;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public int getErrorCellValue()
/* 121:    */   {
/* 122:132 */     checkCellType(CellType.ERROR);
/* 123:133 */     return this._errorValue;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public double getNumericCellValue()
/* 127:    */   {
/* 128:137 */     checkCellType(CellType.NUMERIC);
/* 129:138 */     return this._numberValue;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public String getStringCellValue()
/* 133:    */   {
/* 134:142 */     checkCellType(CellType.STRING);
/* 135:143 */     return this._stringValue;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public EvaluationSheet getSheet()
/* 139:    */   {
/* 140:147 */     return this._sheet;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public int getRowIndex()
/* 144:    */   {
/* 145:151 */     return this._masterCell.getRowIndex();
/* 146:    */   }
/* 147:    */   
/* 148:    */   public int getColumnIndex()
/* 149:    */   {
/* 150:155 */     return this._masterCell.getColumnIndex();
/* 151:    */   }
/* 152:    */   
/* 153:    */   /**
/* 154:    */    * @deprecated
/* 155:    */    */
/* 156:    */   public int getCachedFormulaResultType()
/* 157:    */   {
/* 158:166 */     return this._masterCell.getCachedFormulaResultType();
/* 159:    */   }
/* 160:    */   
/* 161:    */   /**
/* 162:    */    * @deprecated
/* 163:    */    */
/* 164:    */   public CellType getCachedFormulaResultTypeEnum()
/* 165:    */   {
/* 166:175 */     return this._masterCell.getCachedFormulaResultTypeEnum();
/* 167:    */   }
/* 168:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.forked.ForkedEvaluationCell
 * JD-Core Version:    0.7.0.1
 */