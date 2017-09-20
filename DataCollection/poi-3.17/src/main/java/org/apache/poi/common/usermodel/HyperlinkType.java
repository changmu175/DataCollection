/*   1:    */ package org.apache.poi.common.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.apache.poi.util.Internal;
/*   6:    */ 
/*   7:    */ public enum HyperlinkType
/*   8:    */ {
/*   9: 29 */   NONE(-1),  URL(1),  DOCUMENT(2),  EMAIL(3),  FILE(4);
/*  10:    */   
/*  11:    */   @Internal(since="3.15 beta 3")
/*  12:    */   @Deprecated
/*  13:    */   private final int code;
/*  14:    */   private static final Map<Integer, HyperlinkType> map;
/*  15:    */   
/*  16:    */   @Internal(since="3.15 beta 3")
/*  17:    */   @Deprecated
/*  18:    */   private HyperlinkType(int code)
/*  19:    */   {
/*  20: 70 */     this.code = code;
/*  21:    */   }
/*  22:    */   
/*  23:    */   static
/*  24:    */   {
/*  25: 73 */     map = new HashMap();
/*  26: 75 */     for (HyperlinkType type : values()) {
/*  27: 76 */       map.put(Integer.valueOf(type.getCode()), type);
/*  28:    */     }
/*  29:    */   }
/*  30:    */   
/*  31:    */   @Internal(since="3.15 beta 3")
/*  32:    */   @Deprecated
/*  33:    */   public int getCode()
/*  34:    */   {
/*  35: 88 */     return this.code;
/*  36:    */   }
/*  37:    */   
/*  38:    */   @Internal(since="3.15 beta 3")
/*  39:    */   @Deprecated
/*  40:    */   public static HyperlinkType forInt(int code)
/*  41:    */   {
/*  42:101 */     HyperlinkType type = (HyperlinkType)map.get(Integer.valueOf(code));
/*  43:102 */     if (type == null) {
/*  44:103 */       throw new IllegalArgumentException("Invalid type: " + code);
/*  45:    */     }
/*  46:105 */     return type;
/*  47:    */   }
/*  48:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.common.usermodel.HyperlinkType
 * JD-Core Version:    0.7.0.1
 */