/*  1:   */ package org.apache.poi.ss.formula.function;
/*  2:   */ 
/*  3:   */ public final class FunctionMetadata
/*  4:   */ {
/*  5:   */   private static final short FUNCTION_MAX_PARAMS = 30;
/*  6:   */   private final int _index;
/*  7:   */   private final String _name;
/*  8:   */   private final int _minParams;
/*  9:   */   private final int _maxParams;
/* 10:   */   private final byte _returnClassCode;
/* 11:   */   private final byte[] _parameterClassCodes;
/* 12:   */   
/* 13:   */   FunctionMetadata(int index, String name, int minParams, int maxParams, byte returnClassCode, byte[] parameterClassCodes)
/* 14:   */   {
/* 15:45 */     this._index = index;
/* 16:46 */     this._name = name;
/* 17:47 */     this._minParams = minParams;
/* 18:48 */     this._maxParams = maxParams;
/* 19:49 */     this._returnClassCode = returnClassCode;
/* 20:50 */     this._parameterClassCodes = (parameterClassCodes == null ? null : (byte[])parameterClassCodes.clone());
/* 21:   */   }
/* 22:   */   
/* 23:   */   public int getIndex()
/* 24:   */   {
/* 25:53 */     return this._index;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String getName()
/* 29:   */   {
/* 30:56 */     return this._name;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public int getMinParams()
/* 34:   */   {
/* 35:59 */     return this._minParams;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public int getMaxParams()
/* 39:   */   {
/* 40:62 */     return this._maxParams;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public boolean hasFixedArgsLength()
/* 44:   */   {
/* 45:65 */     return this._minParams == this._maxParams;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public byte getReturnClassCode()
/* 49:   */   {
/* 50:68 */     return this._returnClassCode;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public byte[] getParameterClassCodes()
/* 54:   */   {
/* 55:71 */     return (byte[])this._parameterClassCodes.clone();
/* 56:   */   }
/* 57:   */   
/* 58:   */   public boolean hasUnlimitedVarags()
/* 59:   */   {
/* 60:81 */     return 30 == this._maxParams;
/* 61:   */   }
/* 62:   */   
/* 63:   */   public String toString()
/* 64:   */   {
/* 65:84 */     StringBuffer sb = new StringBuffer(64);
/* 66:85 */     sb.append(getClass().getName()).append(" [");
/* 67:86 */     sb.append(this._index).append(" ").append(this._name);
/* 68:87 */     sb.append("]");
/* 69:88 */     return sb.toString();
/* 70:   */   }
/* 71:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.function.FunctionMetadata
 * JD-Core Version:    0.7.0.1
 */