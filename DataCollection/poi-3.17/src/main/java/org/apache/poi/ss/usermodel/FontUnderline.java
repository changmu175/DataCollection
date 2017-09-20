/*   1:    */ package org.apache.poi.ss.usermodel;
/*   2:    */ 
/*   3:    */ public enum FontUnderline
/*   4:    */ {
/*   5: 32 */   SINGLE(1),  DOUBLE(2),  SINGLE_ACCOUNTING(3),  DOUBLE_ACCOUNTING(4),  NONE(5);
/*   6:    */   
/*   7:    */   private int value;
/*   8:    */   private static FontUnderline[] _table;
/*   9:    */   
/*  10:    */   private FontUnderline(int val)
/*  11:    */   {
/*  12: 64 */     this.value = val;
/*  13:    */   }
/*  14:    */   
/*  15:    */   public int getValue()
/*  16:    */   {
/*  17: 68 */     return this.value;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public byte getByteValue()
/*  21:    */   {
/*  22: 72 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$FontUnderline[ordinal()])
/*  23:    */     {
/*  24:    */     case 1: 
/*  25: 74 */       return 2;
/*  26:    */     case 2: 
/*  27: 76 */       return 34;
/*  28:    */     case 3: 
/*  29: 78 */       return 33;
/*  30:    */     case 4: 
/*  31: 80 */       return 0;
/*  32:    */     case 5: 
/*  33: 82 */       return 1;
/*  34:    */     }
/*  35: 84 */     return 1;
/*  36:    */   }
/*  37:    */   
/*  38:    */   static
/*  39:    */   {
/*  40: 88 */     _table = new FontUnderline[6];
/*  41: 90 */     for (FontUnderline c : values()) {
/*  42: 91 */       _table[c.getValue()] = c;
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static FontUnderline valueOf(int value)
/*  47:    */   {
/*  48: 96 */     return _table[value];
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static FontUnderline valueOf(byte value)
/*  52:    */   {
/*  53:    */     FontUnderline val;
/*  54:101 */     switch (value)
/*  55:    */     {
/*  56:    */     case 2: 
/*  57:103 */       val = DOUBLE;
/*  58:104 */       break;
/*  59:    */     case 34: 
/*  60:106 */       val = DOUBLE_ACCOUNTING;
/*  61:107 */       break;
/*  62:    */     case 33: 
/*  63:109 */       val = SINGLE_ACCOUNTING;
/*  64:110 */       break;
/*  65:    */     case 1: 
/*  66:112 */       val = SINGLE;
/*  67:113 */       break;
/*  68:    */     default: 
/*  69:115 */       val = NONE;
/*  70:    */     }
/*  71:118 */     return val;
/*  72:    */   }
/*  73:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.FontUnderline
 * JD-Core Version:    0.7.0.1
 */