/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.nio.BufferUnderflowException;
/*   6:    */ 
/*   7:    */ public class ByteField
/*   8:    */   implements FixedField
/*   9:    */ {
/*  10:    */   private static final byte _default_value = 0;
/*  11:    */   private byte _value;
/*  12:    */   private final int _offset;
/*  13:    */   
/*  14:    */   public ByteField(int offset)
/*  15:    */     throws ArrayIndexOutOfBoundsException
/*  16:    */   {
/*  17: 53 */     this(offset, (byte)0);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public ByteField(int offset, byte value)
/*  21:    */     throws ArrayIndexOutOfBoundsException
/*  22:    */   {
/*  23: 69 */     if (offset < 0) {
/*  24: 71 */       throw new ArrayIndexOutOfBoundsException("offset cannot be negative");
/*  25:    */     }
/*  26: 74 */     this._offset = offset;
/*  27: 75 */     set(value);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public ByteField(int offset, byte[] data)
/*  31:    */     throws ArrayIndexOutOfBoundsException
/*  32:    */   {
/*  33: 92 */     this(offset);
/*  34: 93 */     readFromBytes(data);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public ByteField(int offset, byte value, byte[] data)
/*  38:    */     throws ArrayIndexOutOfBoundsException
/*  39:    */   {
/*  40:112 */     this(offset, value);
/*  41:113 */     writeToBytes(data);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public byte get()
/*  45:    */   {
/*  46:124 */     return this._value;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void set(byte value)
/*  50:    */   {
/*  51:135 */     this._value = value;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void set(byte value, byte[] data)
/*  55:    */     throws ArrayIndexOutOfBoundsException
/*  56:    */   {
/*  57:151 */     set(value);
/*  58:152 */     writeToBytes(data);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void readFromBytes(byte[] data)
/*  62:    */     throws ArrayIndexOutOfBoundsException
/*  63:    */   {
/*  64:169 */     this._value = data[this._offset];
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void readFromStream(InputStream stream)
/*  68:    */     throws IOException
/*  69:    */   {
/*  70:188 */     int ib = stream.read();
/*  71:189 */     if (ib < 0) {
/*  72:190 */       throw new BufferUnderflowException();
/*  73:    */     }
/*  74:192 */     this._value = ((byte)ib);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void writeToBytes(byte[] data)
/*  78:    */     throws ArrayIndexOutOfBoundsException
/*  79:    */   {
/*  80:209 */     data[this._offset] = this._value;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public String toString()
/*  84:    */   {
/*  85:220 */     return String.valueOf(this._value);
/*  86:    */   }
/*  87:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.ByteField
 * JD-Core Version:    0.7.0.1
 */