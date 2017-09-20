/*   1:    */ package org.apache.poi.ss.formula.constant;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianInput;
/*   4:    */ import org.apache.poi.util.LittleEndianOutput;
/*   5:    */ import org.apache.poi.util.StringUtil;
/*   6:    */ 
/*   7:    */ public final class ConstantValueParser
/*   8:    */ {
/*   9:    */   private static final int TYPE_EMPTY = 0;
/*  10:    */   private static final int TYPE_NUMBER = 1;
/*  11:    */   private static final int TYPE_STRING = 2;
/*  12:    */   private static final int TYPE_BOOLEAN = 4;
/*  13:    */   private static final int TYPE_ERROR_CODE = 16;
/*  14:    */   private static final int TRUE_ENCODING = 1;
/*  15:    */   private static final int FALSE_ENCODING = 0;
/*  16: 43 */   private static final Object EMPTY_REPRESENTATION = null;
/*  17:    */   
/*  18:    */   public static Object[] parse(LittleEndianInput in, int nValues)
/*  19:    */   {
/*  20: 50 */     Object[] result = new Object[nValues];
/*  21: 51 */     for (int i = 0; i < result.length; i++) {
/*  22: 52 */       result[i] = readAConstantValue(in);
/*  23:    */     }
/*  24: 54 */     return result;
/*  25:    */   }
/*  26:    */   
/*  27:    */   private static Object readAConstantValue(LittleEndianInput in)
/*  28:    */   {
/*  29: 58 */     byte grbit = in.readByte();
/*  30: 59 */     switch (grbit)
/*  31:    */     {
/*  32:    */     case 0: 
/*  33: 61 */       in.readLong();
/*  34: 62 */       return EMPTY_REPRESENTATION;
/*  35:    */     case 1: 
/*  36: 64 */       return new Double(in.readDouble());
/*  37:    */     case 2: 
/*  38: 66 */       return StringUtil.readUnicodeString(in);
/*  39:    */     case 4: 
/*  40: 68 */       return readBoolean(in);
/*  41:    */     case 16: 
/*  42: 70 */       int errCode = in.readUShort();
/*  43:    */       
/*  44: 72 */       in.readUShort();
/*  45: 73 */       in.readInt();
/*  46: 74 */       return ErrorConstant.valueOf(errCode);
/*  47:    */     }
/*  48: 76 */     throw new RuntimeException("Unknown grbit value (" + grbit + ")");
/*  49:    */   }
/*  50:    */   
/*  51:    */   private static Object readBoolean(LittleEndianInput in)
/*  52:    */   {
/*  53: 80 */     byte val = (byte)(int)in.readLong();
/*  54: 81 */     switch (val)
/*  55:    */     {
/*  56:    */     case 0: 
/*  57: 83 */       return Boolean.FALSE;
/*  58:    */     case 1: 
/*  59: 85 */       return Boolean.TRUE;
/*  60:    */     }
/*  61: 88 */     throw new RuntimeException("unexpected boolean encoding (" + val + ")");
/*  62:    */   }
/*  63:    */   
/*  64:    */   public static int getEncodedSize(Object[] values)
/*  65:    */   {
/*  66: 93 */     int result = values.length * 1;
/*  67: 94 */     for (int i = 0; i < values.length; i++) {
/*  68: 95 */       result += getEncodedSize(values[i]);
/*  69:    */     }
/*  70: 97 */     return result;
/*  71:    */   }
/*  72:    */   
/*  73:    */   private static int getEncodedSize(Object object)
/*  74:    */   {
/*  75:104 */     if (object == EMPTY_REPRESENTATION) {
/*  76:105 */       return 8;
/*  77:    */     }
/*  78:107 */     Class<?> cls = object.getClass();
/*  79:109 */     if ((cls == Boolean.class) || (cls == Double.class) || (cls == ErrorConstant.class)) {
/*  80:110 */       return 8;
/*  81:    */     }
/*  82:112 */     String strVal = (String)object;
/*  83:113 */     return StringUtil.getEncodedSize(strVal);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static void encode(LittleEndianOutput out, Object[] values)
/*  87:    */   {
/*  88:117 */     for (int i = 0; i < values.length; i++) {
/*  89:118 */       encodeSingleValue(out, values[i]);
/*  90:    */     }
/*  91:    */   }
/*  92:    */   
/*  93:    */   private static void encodeSingleValue(LittleEndianOutput out, Object value)
/*  94:    */   {
/*  95:123 */     if (value == EMPTY_REPRESENTATION)
/*  96:    */     {
/*  97:124 */       out.writeByte(0);
/*  98:125 */       out.writeLong(0L);
/*  99:126 */       return;
/* 100:    */     }
/* 101:128 */     if ((value instanceof Boolean))
/* 102:    */     {
/* 103:129 */       Boolean bVal = (Boolean)value;
/* 104:130 */       out.writeByte(4);
/* 105:131 */       long longVal = bVal.booleanValue() ? 1L : 0L;
/* 106:132 */       out.writeLong(longVal);
/* 107:133 */       return;
/* 108:    */     }
/* 109:135 */     if ((value instanceof Double))
/* 110:    */     {
/* 111:136 */       Double dVal = (Double)value;
/* 112:137 */       out.writeByte(1);
/* 113:138 */       out.writeDouble(dVal.doubleValue());
/* 114:139 */       return;
/* 115:    */     }
/* 116:141 */     if ((value instanceof String))
/* 117:    */     {
/* 118:142 */       String val = (String)value;
/* 119:143 */       out.writeByte(2);
/* 120:144 */       StringUtil.writeUnicodeString(out, val);
/* 121:145 */       return;
/* 122:    */     }
/* 123:147 */     if ((value instanceof ErrorConstant))
/* 124:    */     {
/* 125:148 */       ErrorConstant ecVal = (ErrorConstant)value;
/* 126:149 */       out.writeByte(16);
/* 127:150 */       long longVal = ecVal.getErrorCode();
/* 128:151 */       out.writeLong(longVal);
/* 129:152 */       return;
/* 130:    */     }
/* 131:155 */     throw new IllegalStateException("Unexpected value type (" + value.getClass().getName() + "'");
/* 132:    */   }
/* 133:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.constant.ConstantValueParser
 * JD-Core Version:    0.7.0.1
 */