/*   1:    */ package org.apache.poi.hpsf;
/*   2:    */ 
/*   3:    */ import java.util.Collections;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ 
/*   7:    */ public class Variant
/*   8:    */ {
/*   9:    */   public static final int VT_EMPTY = 0;
/*  10:    */   public static final int VT_NULL = 1;
/*  11:    */   public static final int VT_I2 = 2;
/*  12:    */   public static final int VT_I4 = 3;
/*  13:    */   public static final int VT_R4 = 4;
/*  14:    */   public static final int VT_R8 = 5;
/*  15:    */   public static final int VT_CY = 6;
/*  16:    */   public static final int VT_DATE = 7;
/*  17:    */   public static final int VT_BSTR = 8;
/*  18:    */   public static final int VT_DISPATCH = 9;
/*  19:    */   public static final int VT_ERROR = 10;
/*  20:    */   public static final int VT_BOOL = 11;
/*  21:    */   public static final int VT_VARIANT = 12;
/*  22:    */   public static final int VT_UNKNOWN = 13;
/*  23:    */   public static final int VT_DECIMAL = 14;
/*  24:    */   public static final int VT_I1 = 16;
/*  25:    */   public static final int VT_UI1 = 17;
/*  26:    */   public static final int VT_UI2 = 18;
/*  27:    */   public static final int VT_UI4 = 19;
/*  28:    */   public static final int VT_I8 = 20;
/*  29:    */   public static final int VT_UI8 = 21;
/*  30:    */   public static final int VT_INT = 22;
/*  31:    */   public static final int VT_UINT = 23;
/*  32:    */   public static final int VT_VOID = 24;
/*  33:    */   public static final int VT_HRESULT = 25;
/*  34:    */   public static final int VT_PTR = 26;
/*  35:    */   public static final int VT_SAFEARRAY = 27;
/*  36:    */   public static final int VT_CARRAY = 28;
/*  37:    */   public static final int VT_USERDEFINED = 29;
/*  38:    */   public static final int VT_LPSTR = 30;
/*  39:    */   public static final int VT_LPWSTR = 31;
/*  40:    */   public static final int VT_FILETIME = 64;
/*  41:    */   public static final int VT_BLOB = 65;
/*  42:    */   public static final int VT_STREAM = 66;
/*  43:    */   public static final int VT_STORAGE = 67;
/*  44:    */   public static final int VT_STREAMED_OBJECT = 68;
/*  45:    */   public static final int VT_STORED_OBJECT = 69;
/*  46:    */   public static final int VT_BLOB_OBJECT = 70;
/*  47:    */   public static final int VT_CF = 71;
/*  48:    */   public static final int VT_CLSID = 72;
/*  49:    */   public static final int VT_VERSIONED_STREAM = 73;
/*  50:    */   public static final int VT_VECTOR = 4096;
/*  51:    */   public static final int VT_ARRAY = 8192;
/*  52:    */   public static final int VT_BYREF = 16384;
/*  53:    */   public static final int VT_RESERVED = 32768;
/*  54:    */   public static final int VT_ILLEGAL = 65535;
/*  55:    */   public static final int VT_ILLEGALMASKED = 4095;
/*  56:    */   public static final int VT_TYPEMASK = 4095;
/*  57:    */   private static final Map<Long, String> numberToName;
/*  58:    */   private static final Map<Long, Integer> numberToLength;
/*  59:318 */   public static final Integer LENGTH_UNKNOWN = Integer.valueOf(-2);
/*  60:323 */   public static final Integer LENGTH_VARIABLE = Integer.valueOf(-1);
/*  61:328 */   public static final Integer LENGTH_0 = Integer.valueOf(0);
/*  62:333 */   public static final Integer LENGTH_2 = Integer.valueOf(2);
/*  63:338 */   public static final Integer LENGTH_4 = Integer.valueOf(4);
/*  64:343 */   public static final Integer LENGTH_8 = Integer.valueOf(8);
/*  65:345 */   private static final Object[][] NUMBER_TO_NAME_LIST = { { Long.valueOf(0L), "VT_EMPTY", LENGTH_0 }, { Long.valueOf(1L), "VT_NULL", LENGTH_UNKNOWN }, { Long.valueOf(2L), "VT_I2", LENGTH_2 }, { Long.valueOf(3L), "VT_I4", LENGTH_4 }, { Long.valueOf(4L), "VT_R4", LENGTH_4 }, { Long.valueOf(5L), "VT_R8", LENGTH_8 }, { Long.valueOf(6L), "VT_CY", LENGTH_UNKNOWN }, { Long.valueOf(7L), "VT_DATE", LENGTH_UNKNOWN }, { Long.valueOf(8L), "VT_BSTR", LENGTH_UNKNOWN }, { Long.valueOf(9L), "VT_DISPATCH", LENGTH_UNKNOWN }, { Long.valueOf(10L), "VT_ERROR", LENGTH_UNKNOWN }, { Long.valueOf(11L), "VT_BOOL", LENGTH_UNKNOWN }, { Long.valueOf(12L), "VT_VARIANT", LENGTH_UNKNOWN }, { Long.valueOf(13L), "VT_UNKNOWN", LENGTH_UNKNOWN }, { Long.valueOf(14L), "VT_DECIMAL", LENGTH_UNKNOWN }, { Long.valueOf(16L), "VT_I1", LENGTH_UNKNOWN }, { Long.valueOf(17L), "VT_UI1", LENGTH_UNKNOWN }, { Long.valueOf(18L), "VT_UI2", LENGTH_UNKNOWN }, { Long.valueOf(19L), "VT_UI4", LENGTH_UNKNOWN }, { Long.valueOf(20L), "VT_I8", LENGTH_UNKNOWN }, { Long.valueOf(21L), "VT_UI8", LENGTH_UNKNOWN }, { Long.valueOf(22L), "VT_INT", LENGTH_UNKNOWN }, { Long.valueOf(23L), "VT_UINT", LENGTH_UNKNOWN }, { Long.valueOf(24L), "VT_VOID", LENGTH_UNKNOWN }, { Long.valueOf(25L), "VT_HRESULT", LENGTH_UNKNOWN }, { Long.valueOf(26L), "VT_PTR", LENGTH_UNKNOWN }, { Long.valueOf(27L), "VT_SAFEARRAY", LENGTH_UNKNOWN }, { Long.valueOf(28L), "VT_CARRAY", LENGTH_UNKNOWN }, { Long.valueOf(29L), "VT_USERDEFINED", LENGTH_UNKNOWN }, { Long.valueOf(30L), "VT_LPSTR", LENGTH_VARIABLE }, { Long.valueOf(31L), "VT_LPWSTR", LENGTH_UNKNOWN }, { Long.valueOf(64L), "VT_FILETIME", LENGTH_8 }, { Long.valueOf(65L), "VT_BLOB", LENGTH_UNKNOWN }, { Long.valueOf(66L), "VT_STREAM", LENGTH_UNKNOWN }, { Long.valueOf(67L), "VT_STORAGE", LENGTH_UNKNOWN }, { Long.valueOf(68L), "VT_STREAMED_OBJECT", LENGTH_UNKNOWN }, { Long.valueOf(69L), "VT_STORED_OBJECT", LENGTH_UNKNOWN }, { Long.valueOf(70L), "VT_BLOB_OBJECT", LENGTH_UNKNOWN }, { Long.valueOf(71L), "VT_CF", LENGTH_UNKNOWN }, { Long.valueOf(72L), "VT_CLSID", LENGTH_UNKNOWN } };
/*  66:    */   
/*  67:    */   static
/*  68:    */   {
/*  69:390 */     Map<Long, String> number2Name = new HashMap(NUMBER_TO_NAME_LIST.length, 1.0F);
/*  70:391 */     Map<Long, Integer> number2Len = new HashMap(NUMBER_TO_NAME_LIST.length, 1.0F);
/*  71:393 */     for (Object[] nn : NUMBER_TO_NAME_LIST)
/*  72:    */     {
/*  73:394 */       number2Name.put((Long)nn[0], (String)nn[1]);
/*  74:395 */       number2Len.put((Long)nn[0], (Integer)nn[2]);
/*  75:    */     }
/*  76:397 */     numberToName = Collections.unmodifiableMap(number2Name);
/*  77:398 */     numberToLength = Collections.unmodifiableMap(number2Len);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static String getVariantName(long variantType)
/*  81:    */   {
/*  82:409 */     long vt = variantType;
/*  83:410 */     String name = "";
/*  84:411 */     if ((vt & 0x1000) != 0L)
/*  85:    */     {
/*  86:412 */       name = "Vector of ";
/*  87:413 */       vt -= 4096L;
/*  88:    */     }
/*  89:414 */     else if ((vt & 0x2000) != 0L)
/*  90:    */     {
/*  91:415 */       name = "Array of ";
/*  92:416 */       vt -= 8192L;
/*  93:    */     }
/*  94:417 */     else if ((vt & 0x4000) != 0L)
/*  95:    */     {
/*  96:418 */       name = "ByRef of ";
/*  97:419 */       vt -= 16384L;
/*  98:    */     }
/*  99:422 */     name = name + (String)numberToName.get(Long.valueOf(vt));
/* 100:423 */     return (name != null) && (!"".equals(name)) ? name : "unknown variant type";
/* 101:    */   }
/* 102:    */   
/* 103:    */   public static int getVariantLength(long variantType)
/* 104:    */   {
/* 105:436 */     Integer length = (Integer)numberToLength.get(Long.valueOf(variantType));
/* 106:437 */     return (length != null ? length : LENGTH_UNKNOWN).intValue();
/* 107:    */   }
/* 108:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.Variant
 * JD-Core Version:    0.7.0.1
 */