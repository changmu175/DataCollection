/*   1:    */ package org.apache.poi.ss.formula.ptg;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import org.apache.poi.ss.formula.function.FunctionMetadata;
/*   5:    */ import org.apache.poi.ss.formula.function.FunctionMetadataRegistry;
/*   6:    */ 
/*   7:    */ public abstract class AbstractFunctionPtg
/*   8:    */   extends OperationPtg
/*   9:    */ {
/*  10:    */   public static final String FUNCTION_NAME_IF = "IF";
/*  11:    */   private static final short FUNCTION_INDEX_EXTERNAL = 255;
/*  12:    */   private final byte returnClass;
/*  13:    */   private final byte[] paramClass;
/*  14:    */   private final int _numberOfArgs;
/*  15:    */   private final short _functionIndex;
/*  16:    */   
/*  17:    */   protected AbstractFunctionPtg(int functionIndex, int pReturnClass, byte[] paramTypes, int nParams)
/*  18:    */   {
/*  19: 48 */     this._numberOfArgs = nParams;
/*  20: 49 */     if ((functionIndex < -32768) || (functionIndex > 32767)) {
/*  21: 50 */       throw new RuntimeException("functionIndex " + functionIndex + " cannot be cast to short");
/*  22:    */     }
/*  23: 51 */     this._functionIndex = ((short)functionIndex);
/*  24: 52 */     if ((pReturnClass < -128) || (pReturnClass > 127)) {
/*  25: 53 */       throw new RuntimeException("pReturnClass " + pReturnClass + " cannot be cast to byte");
/*  26:    */     }
/*  27: 54 */     this.returnClass = ((byte)pReturnClass);
/*  28: 55 */     this.paramClass = paramTypes;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public final boolean isBaseToken()
/*  32:    */   {
/*  33: 58 */     return false;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public final String toString()
/*  37:    */   {
/*  38: 62 */     StringBuilder sb = new StringBuilder(64);
/*  39: 63 */     sb.append(getClass().getName()).append(" [");
/*  40: 64 */     sb.append(lookupName(this._functionIndex));
/*  41: 65 */     sb.append(" nArgs=").append(this._numberOfArgs);
/*  42: 66 */     sb.append("]");
/*  43: 67 */     return sb.toString();
/*  44:    */   }
/*  45:    */   
/*  46:    */   public final short getFunctionIndex()
/*  47:    */   {
/*  48: 71 */     return this._functionIndex;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public final int getNumberOfOperands()
/*  52:    */   {
/*  53: 74 */     return this._numberOfArgs;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public final String getName()
/*  57:    */   {
/*  58: 78 */     return lookupName(this._functionIndex);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public final boolean isExternalFunction()
/*  62:    */   {
/*  63: 85 */     return this._functionIndex == 255;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public final String toFormulaString()
/*  67:    */   {
/*  68: 89 */     return getName();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public String toFormulaString(String[] operands)
/*  72:    */   {
/*  73: 93 */     StringBuilder buf = new StringBuilder();
/*  74: 95 */     if (isExternalFunction())
/*  75:    */     {
/*  76: 96 */       buf.append(operands[0]);
/*  77: 97 */       appendArgs(buf, 1, operands);
/*  78:    */     }
/*  79:    */     else
/*  80:    */     {
/*  81: 99 */       buf.append(getName());
/*  82:100 */       appendArgs(buf, 0, operands);
/*  83:    */     }
/*  84:102 */     return buf.toString();
/*  85:    */   }
/*  86:    */   
/*  87:    */   private static void appendArgs(StringBuilder buf, int firstArgIx, String[] operands)
/*  88:    */   {
/*  89:106 */     buf.append('(');
/*  90:107 */     for (int i = firstArgIx; i < operands.length; i++)
/*  91:    */     {
/*  92:108 */       if (i > firstArgIx) {
/*  93:109 */         buf.append(',');
/*  94:    */       }
/*  95:111 */       buf.append(operands[i]);
/*  96:    */     }
/*  97:113 */     buf.append(")");
/*  98:    */   }
/*  99:    */   
/* 100:    */   public abstract int getSize();
/* 101:    */   
/* 102:    */   public static final boolean isBuiltInFunctionName(String name)
/* 103:    */   {
/* 104:127 */     short ix = FunctionMetadataRegistry.lookupIndexByName(name.toUpperCase(Locale.ROOT));
/* 105:128 */     return ix >= 0;
/* 106:    */   }
/* 107:    */   
/* 108:    */   protected final String lookupName(short index)
/* 109:    */   {
/* 110:132 */     if (index == 255) {
/* 111:133 */       return "#external#";
/* 112:    */     }
/* 113:135 */     FunctionMetadata fm = FunctionMetadataRegistry.getFunctionByIndex(index);
/* 114:136 */     if (fm == null) {
/* 115:137 */       throw new RuntimeException("bad function index (" + index + ")");
/* 116:    */     }
/* 117:139 */     return fm.getName();
/* 118:    */   }
/* 119:    */   
/* 120:    */   protected static short lookupIndex(String name)
/* 121:    */   {
/* 122:149 */     short ix = FunctionMetadataRegistry.lookupIndexByName(name.toUpperCase(Locale.ROOT));
/* 123:150 */     if (ix < 0) {
/* 124:151 */       return 255;
/* 125:    */     }
/* 126:153 */     return ix;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public byte getDefaultOperandClass()
/* 130:    */   {
/* 131:157 */     return this.returnClass;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public final byte getParameterClass(int index)
/* 135:    */   {
/* 136:161 */     if (index >= this.paramClass.length) {
/* 137:165 */       return this.paramClass[(this.paramClass.length - 1)];
/* 138:    */     }
/* 139:167 */     return this.paramClass[index];
/* 140:    */   }
/* 141:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.AbstractFunctionPtg
 * JD-Core Version:    0.7.0.1
 */