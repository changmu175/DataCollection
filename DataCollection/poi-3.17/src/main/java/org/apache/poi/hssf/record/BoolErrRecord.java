/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.FormulaError;
/*   4:    */ import org.apache.poi.util.HexDump;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ import org.apache.poi.util.RecordFormatException;
/*   7:    */ 
/*   8:    */ public final class BoolErrRecord
/*   9:    */   extends CellRecord
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12:    */   public static final short sid = 517;
/*  13:    */   private int _value;
/*  14:    */   private boolean _isError;
/*  15:    */   
/*  16:    */   public BoolErrRecord() {}
/*  17:    */   
/*  18:    */   public BoolErrRecord(RecordInputStream in)
/*  19:    */   {
/*  20: 48 */     super(in);
/*  21: 49 */     switch (in.remaining())
/*  22:    */     {
/*  23:    */     case 2: 
/*  24: 51 */       this._value = in.readByte();
/*  25: 52 */       break;
/*  26:    */     case 3: 
/*  27: 54 */       this._value = in.readUShort();
/*  28: 55 */       break;
/*  29:    */     default: 
/*  30: 57 */       throw new RecordFormatException("Unexpected size (" + in.remaining() + ") for BOOLERR record.");
/*  31:    */     }
/*  32: 60 */     int flag = in.readUByte();
/*  33: 61 */     switch (flag)
/*  34:    */     {
/*  35:    */     case 0: 
/*  36: 63 */       this._isError = false;
/*  37: 64 */       break;
/*  38:    */     case 1: 
/*  39: 66 */       this._isError = true;
/*  40: 67 */       break;
/*  41:    */     default: 
/*  42: 69 */       throw new RecordFormatException("Unexpected isError flag (" + flag + ") for BOOLERR record.");
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setValue(boolean value)
/*  47:    */   {
/*  48: 80 */     this._value = (value ? 1 : 0);
/*  49: 81 */     this._isError = false;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setValue(byte value)
/*  53:    */   {
/*  54: 92 */     setValue(FormulaError.forInt(value));
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setValue(FormulaError value)
/*  58:    */   {
/*  59:103 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$FormulaError[value.ordinal()])
/*  60:    */     {
/*  61:    */     case 1: 
/*  62:    */     case 2: 
/*  63:    */     case 3: 
/*  64:    */     case 4: 
/*  65:    */     case 5: 
/*  66:    */     case 6: 
/*  67:    */     case 7: 
/*  68:111 */       this._value = value.getCode();
/*  69:112 */       this._isError = true;
/*  70:113 */       return;
/*  71:    */     }
/*  72:115 */     throw new IllegalArgumentException("Error Value can only be 0,7,15,23,29,36 or 42. It cannot be " + value.getCode() + " (" + value + ")");
/*  73:    */   }
/*  74:    */   
/*  75:    */   public boolean getBooleanValue()
/*  76:    */   {
/*  77:125 */     return this._value != 0;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public byte getErrorValue()
/*  81:    */   {
/*  82:134 */     return (byte)this._value;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public boolean isBoolean()
/*  86:    */   {
/*  87:143 */     return !this._isError;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public boolean isError()
/*  91:    */   {
/*  92:152 */     return this._isError;
/*  93:    */   }
/*  94:    */   
/*  95:    */   protected String getRecordName()
/*  96:    */   {
/*  97:157 */     return "BOOLERR";
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected void appendValueText(StringBuilder sb)
/* 101:    */   {
/* 102:161 */     if (isBoolean())
/* 103:    */     {
/* 104:162 */       sb.append("  .boolVal = ");
/* 105:163 */       sb.append(getBooleanValue());
/* 106:    */     }
/* 107:    */     else
/* 108:    */     {
/* 109:165 */       sb.append("  .errCode = ");
/* 110:166 */       sb.append(FormulaError.forInt(getErrorValue()).getString());
/* 111:167 */       sb.append(" (").append(HexDump.byteToHex(getErrorValue())).append(")");
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   protected void serializeValue(LittleEndianOutput out)
/* 116:    */   {
/* 117:172 */     out.writeByte(this._value);
/* 118:173 */     out.writeByte(this._isError ? 1 : 0);
/* 119:    */   }
/* 120:    */   
/* 121:    */   protected int getValueDataSize()
/* 122:    */   {
/* 123:178 */     return 2;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public short getSid()
/* 127:    */   {
/* 128:182 */     return 517;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public BoolErrRecord clone()
/* 132:    */   {
/* 133:187 */     BoolErrRecord rec = new BoolErrRecord();
/* 134:188 */     copyBaseFields(rec);
/* 135:189 */     rec._value = this._value;
/* 136:190 */     rec._isError = this._isError;
/* 137:191 */     return rec;
/* 138:    */   }
/* 139:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.BoolErrRecord
 * JD-Core Version:    0.7.0.1
 */