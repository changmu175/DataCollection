/*  1:   */ package org.apache.poi.ddf;
/*  2:   */ 
/*  3:   */ public class EscherPropertyMetaData
/*  4:   */ {
/*  5:   */   public static final byte TYPE_UNKNOWN = 0;
/*  6:   */   public static final byte TYPE_BOOLEAN = 1;
/*  7:   */   public static final byte TYPE_RGB = 2;
/*  8:   */   public static final byte TYPE_SHAPEPATH = 3;
/*  9:   */   public static final byte TYPE_SIMPLE = 4;
/* 10:   */   public static final byte TYPE_ARRAY = 5;
/* 11:   */   private String description;
/* 12:   */   private byte type;
/* 13:   */   
/* 14:   */   public EscherPropertyMetaData(String description)
/* 15:   */   {
/* 16:45 */     this.description = description;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public EscherPropertyMetaData(String description, byte type)
/* 20:   */   {
/* 21:55 */     this.description = description;
/* 22:56 */     this.type = type;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String getDescription()
/* 26:   */   {
/* 27:61 */     return this.description;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public byte getType()
/* 31:   */   {
/* 32:66 */     return this.type;
/* 33:   */   }
/* 34:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherPropertyMetaData
 * JD-Core Version:    0.7.0.1
 */