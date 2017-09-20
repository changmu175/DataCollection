/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ 
/*   6:    */ public class IntegerField
/*   7:    */   implements FixedField
/*   8:    */ {
/*   9:    */   private int _value;
/*  10:    */   private final int _offset;
/*  11:    */   
/*  12:    */   public IntegerField(int offset)
/*  13:    */     throws ArrayIndexOutOfBoundsException
/*  14:    */   {
/*  15: 52 */     if (offset < 0) {
/*  16: 54 */       throw new ArrayIndexOutOfBoundsException("negative offset");
/*  17:    */     }
/*  18: 56 */     this._offset = offset;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public IntegerField(int offset, int value)
/*  22:    */     throws ArrayIndexOutOfBoundsException
/*  23:    */   {
/*  24: 73 */     this(offset);
/*  25: 74 */     set(value);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public IntegerField(int offset, byte[] data)
/*  29:    */     throws ArrayIndexOutOfBoundsException
/*  30:    */   {
/*  31: 91 */     this(offset);
/*  32: 92 */     readFromBytes(data);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public IntegerField(int offset, int value, byte[] data)
/*  36:    */     throws ArrayIndexOutOfBoundsException
/*  37:    */   {
/*  38:111 */     this(offset);
/*  39:112 */     set(value, data);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int get()
/*  43:    */   {
/*  44:123 */     return this._value;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void set(int value)
/*  48:    */   {
/*  49:134 */     this._value = value;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void set(int value, byte[] data)
/*  53:    */     throws ArrayIndexOutOfBoundsException
/*  54:    */   {
/*  55:151 */     this._value = value;
/*  56:152 */     writeToBytes(data);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void readFromBytes(byte[] data)
/*  60:    */     throws ArrayIndexOutOfBoundsException
/*  61:    */   {
/*  62:169 */     this._value = LittleEndian.getInt(data, this._offset);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void readFromStream(InputStream stream)
/*  66:    */     throws IOException
/*  67:    */   {
/*  68:187 */     this._value = LittleEndian.readInt(stream);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void writeToBytes(byte[] data)
/*  72:    */     throws ArrayIndexOutOfBoundsException
/*  73:    */   {
/*  74:204 */     LittleEndian.putInt(data, this._offset, this._value);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String toString()
/*  78:    */   {
/*  79:215 */     return String.valueOf(this._value);
/*  80:    */   }
/*  81:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.IntegerField
 * JD-Core Version:    0.7.0.1
 */