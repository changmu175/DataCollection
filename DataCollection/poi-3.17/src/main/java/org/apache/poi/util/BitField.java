/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ @Internal(since="POI 3.15 beta 3")
/*   4:    */ public class BitField
/*   5:    */ {
/*   6:    */   private final int _mask;
/*   7:    */   private final int _shift_count;
/*   8:    */   
/*   9:    */   public BitField(int mask)
/*  10:    */   {
/*  11: 44 */     this._mask = mask;
/*  12: 45 */     int count = 0;
/*  13: 46 */     int bit_pattern = mask;
/*  14: 48 */     if (bit_pattern != 0) {
/*  15: 50 */       while ((bit_pattern & 0x1) == 0)
/*  16:    */       {
/*  17: 52 */         count++;
/*  18: 53 */         bit_pattern >>= 1;
/*  19:    */       }
/*  20:    */     }
/*  21: 56 */     this._shift_count = count;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public int getValue(int holder)
/*  25:    */   {
/*  26: 74 */     return getRawValue(holder) >>> this._shift_count;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public short getShortValue(short holder)
/*  30:    */   {
/*  31: 92 */     return (short)getValue(holder);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public int getRawValue(int holder)
/*  35:    */   {
/*  36:106 */     return holder & this._mask;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public short getShortRawValue(short holder)
/*  40:    */   {
/*  41:120 */     return (short)getRawValue(holder);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public boolean isSet(int holder)
/*  45:    */   {
/*  46:136 */     return (holder & this._mask) != 0;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean isAllSet(int holder)
/*  50:    */   {
/*  51:151 */     return (holder & this._mask) == this._mask;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public int setValue(int holder, int value)
/*  55:    */   {
/*  56:166 */     return holder & (this._mask ^ 0xFFFFFFFF) | value << this._shift_count & this._mask;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public short setShortValue(short holder, short value)
/*  60:    */   {
/*  61:181 */     return (short)setValue(holder, value);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public int clear(int holder)
/*  65:    */   {
/*  66:195 */     return holder & (this._mask ^ 0xFFFFFFFF);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public short clearShort(short holder)
/*  70:    */   {
/*  71:210 */     return (short)clear(holder);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public byte clearByte(byte holder)
/*  75:    */   {
/*  76:225 */     return (byte)clear(holder);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public int set(int holder)
/*  80:    */   {
/*  81:239 */     return holder | this._mask;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public short setShort(short holder)
/*  85:    */   {
/*  86:253 */     return (short)set(holder);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public byte setByte(byte holder)
/*  90:    */   {
/*  91:267 */     return (byte)set(holder);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public int setBoolean(int holder, boolean flag)
/*  95:    */   {
/*  96:283 */     return flag ? set(holder) : clear(holder);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public short setShortBoolean(short holder, boolean flag)
/* 100:    */   {
/* 101:300 */     return flag ? setShort(holder) : clearShort(holder);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public byte setByteBoolean(byte holder, boolean flag)
/* 105:    */   {
/* 106:317 */     return flag ? setByte(holder) : clearByte(holder);
/* 107:    */   }
/* 108:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.BitField
 * JD-Core Version:    0.7.0.1
 */