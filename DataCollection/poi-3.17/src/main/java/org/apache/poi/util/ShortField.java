/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ 
/*   6:    */ public class ShortField
/*   7:    */   implements FixedField
/*   8:    */ {
/*   9:    */   private short _value;
/*  10:    */   private final int _offset;
/*  11:    */   
/*  12:    */   public ShortField(int offset)
/*  13:    */     throws ArrayIndexOutOfBoundsException
/*  14:    */   {
/*  15: 51 */     if (offset < 0) {
/*  16: 53 */       throw new ArrayIndexOutOfBoundsException("Illegal offset: " + offset);
/*  17:    */     }
/*  18: 56 */     this._offset = offset;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public ShortField(int offset, short value)
/*  22:    */     throws ArrayIndexOutOfBoundsException
/*  23:    */   {
/*  24: 72 */     this(offset);
/*  25: 73 */     set(value);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public ShortField(int offset, byte[] data)
/*  29:    */     throws ArrayIndexOutOfBoundsException
/*  30:    */   {
/*  31: 90 */     this(offset);
/*  32: 91 */     readFromBytes(data);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public ShortField(int offset, short value, byte[] data)
/*  36:    */     throws ArrayIndexOutOfBoundsException
/*  37:    */   {
/*  38:109 */     this(offset);
/*  39:110 */     set(value, data);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public short get()
/*  43:    */   {
/*  44:121 */     return this._value;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void set(short value)
/*  48:    */   {
/*  49:132 */     this._value = value;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void set(short value, byte[] data)
/*  53:    */     throws ArrayIndexOutOfBoundsException
/*  54:    */   {
/*  55:148 */     this._value = value;
/*  56:149 */     writeToBytes(data);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void readFromBytes(byte[] data)
/*  60:    */     throws ArrayIndexOutOfBoundsException
/*  61:    */   {
/*  62:166 */     this._value = LittleEndian.getShort(data, this._offset);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void readFromStream(InputStream stream)
/*  66:    */     throws IOException
/*  67:    */   {
/*  68:184 */     this._value = LittleEndian.readShort(stream);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void writeToBytes(byte[] data)
/*  72:    */     throws ArrayIndexOutOfBoundsException
/*  73:    */   {
/*  74:201 */     LittleEndian.putShort(data, this._offset, this._value);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String toString()
/*  78:    */   {
/*  79:212 */     return String.valueOf(this._value);
/*  80:    */   }
/*  81:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.ShortField
 * JD-Core Version:    0.7.0.1
 */