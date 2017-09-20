/*   1:    */ package org.apache.poi.ss.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.apache.poi.util.Internal;
/*   6:    */ 
/*   7:    */ public enum FormulaError
/*   8:    */ {
/*   9: 31 */   _NO_ERROR(-1, "(no error)"),  NULL(0, "#NULL!"),  DIV0(7, "#DIV/0!"),  VALUE(15, "#VALUE!"),  REF(23, "#REF!"),  NAME(29, "#NAME?"),  NUM(36, "#NUM!"),  NA(42, "#N/A"),  CIRCULAR_REF(-60, "~CIRCULAR~REF~"),  FUNCTION_NOT_IMPLEMENTED(-30, "~FUNCTION~NOT~IMPLEMENTED~");
/*  10:    */   
/*  11:    */   private final byte type;
/*  12:    */   private final int longType;
/*  13:    */   private final String repr;
/*  14:    */   private static final Map<String, FormulaError> smap;
/*  15:    */   private static final Map<Byte, FormulaError> bmap;
/*  16:    */   private static final Map<Integer, FormulaError> imap;
/*  17:    */   
/*  18:    */   private FormulaError(int type, String repr)
/*  19:    */   {
/*  20:125 */     this.type = ((byte)type);
/*  21:126 */     this.longType = type;
/*  22:127 */     this.repr = repr;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public byte getCode()
/*  26:    */   {
/*  27:134 */     return this.type;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public int getLongCode()
/*  31:    */   {
/*  32:140 */     return this.longType;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public String getString()
/*  36:    */   {
/*  37:147 */     return this.repr;
/*  38:    */   }
/*  39:    */   
/*  40:    */   static
/*  41:    */   {
/*  42:150 */     smap = new HashMap();
/*  43:151 */     bmap = new HashMap();
/*  44:152 */     imap = new HashMap();
/*  45:154 */     for (FormulaError error : values())
/*  46:    */     {
/*  47:155 */       bmap.put(Byte.valueOf(error.getCode()), error);
/*  48:156 */       imap.put(Integer.valueOf(error.getLongCode()), error);
/*  49:157 */       smap.put(error.getString(), error);
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   public static final boolean isValidCode(int errorCode)
/*  54:    */   {
/*  55:162 */     for (FormulaError error : )
/*  56:    */     {
/*  57:163 */       if (error.getCode() == errorCode) {
/*  58:163 */         return true;
/*  59:    */       }
/*  60:164 */       if (error.getLongCode() == errorCode) {
/*  61:164 */         return true;
/*  62:    */       }
/*  63:    */     }
/*  64:166 */     return false;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public static FormulaError forInt(byte type)
/*  68:    */     throws IllegalArgumentException
/*  69:    */   {
/*  70:170 */     FormulaError err = (FormulaError)bmap.get(Byte.valueOf(type));
/*  71:171 */     if (err == null) {
/*  72:171 */       throw new IllegalArgumentException("Unknown error type: " + type);
/*  73:    */     }
/*  74:172 */     return err;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static FormulaError forInt(int type)
/*  78:    */     throws IllegalArgumentException
/*  79:    */   {
/*  80:175 */     FormulaError err = (FormulaError)imap.get(Integer.valueOf(type));
/*  81:176 */     if (err == null) {
/*  82:176 */       err = (FormulaError)bmap.get(Byte.valueOf((byte)type));
/*  83:    */     }
/*  84:177 */     if (err == null) {
/*  85:177 */       throw new IllegalArgumentException("Unknown error type: " + type);
/*  86:    */     }
/*  87:178 */     return err;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public static FormulaError forString(String code)
/*  91:    */     throws IllegalArgumentException
/*  92:    */   {
/*  93:182 */     FormulaError err = (FormulaError)smap.get(code);
/*  94:183 */     if (err == null) {
/*  95:183 */       throw new IllegalArgumentException("Unknown error code: " + code);
/*  96:    */     }
/*  97:184 */     return err;
/*  98:    */   }
/*  99:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.FormulaError
 * JD-Core Version:    0.7.0.1
 */