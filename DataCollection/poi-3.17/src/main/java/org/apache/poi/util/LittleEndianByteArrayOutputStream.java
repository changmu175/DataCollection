/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.OutputStream;
/*   4:    */ 
/*   5:    */ public final class LittleEndianByteArrayOutputStream
/*   6:    */   extends OutputStream
/*   7:    */   implements LittleEndianOutput, DelayableLittleEndianOutput
/*   8:    */ {
/*   9:    */   private final byte[] _buf;
/*  10:    */   private final int _endIndex;
/*  11:    */   private int _writeIndex;
/*  12:    */   
/*  13:    */   public LittleEndianByteArrayOutputStream(byte[] buf, int startOffset, int maxWriteLen)
/*  14:    */   {
/*  15: 31 */     if ((startOffset < 0) || (startOffset > buf.length)) {
/*  16: 32 */       throw new IllegalArgumentException("Specified startOffset (" + startOffset + ") is out of allowable range (0.." + buf.length + ")");
/*  17:    */     }
/*  18: 35 */     this._buf = buf;
/*  19: 36 */     this._writeIndex = startOffset;
/*  20: 37 */     this._endIndex = (startOffset + maxWriteLen);
/*  21: 38 */     if ((this._endIndex < startOffset) || (this._endIndex > buf.length)) {
/*  22: 39 */       throw new IllegalArgumentException("calculated end index (" + this._endIndex + ") is out of allowable range (" + this._writeIndex + ".." + buf.length + ")");
/*  23:    */     }
/*  24:    */   }
/*  25:    */   
/*  26:    */   public LittleEndianByteArrayOutputStream(byte[] buf, int startOffset)
/*  27:    */   {
/*  28: 44 */     this(buf, startOffset, buf.length - startOffset);
/*  29:    */   }
/*  30:    */   
/*  31:    */   private void checkPosition(int i)
/*  32:    */   {
/*  33: 48 */     if (i > this._endIndex - this._writeIndex) {
/*  34: 49 */       throw new RuntimeException("Buffer overrun");
/*  35:    */     }
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void writeByte(int v)
/*  39:    */   {
/*  40: 55 */     checkPosition(1);
/*  41: 56 */     this._buf[(this._writeIndex++)] = ((byte)v);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void writeDouble(double v)
/*  45:    */   {
/*  46: 61 */     writeLong(Double.doubleToLongBits(v));
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void writeInt(int v)
/*  50:    */   {
/*  51: 66 */     checkPosition(4);
/*  52: 67 */     int i = this._writeIndex;
/*  53: 68 */     this._buf[(i++)] = ((byte)(v >>> 0 & 0xFF));
/*  54: 69 */     this._buf[(i++)] = ((byte)(v >>> 8 & 0xFF));
/*  55: 70 */     this._buf[(i++)] = ((byte)(v >>> 16 & 0xFF));
/*  56: 71 */     this._buf[(i++)] = ((byte)(v >>> 24 & 0xFF));
/*  57: 72 */     this._writeIndex = i;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void writeLong(long v)
/*  61:    */   {
/*  62: 77 */     writeInt((int)(v >> 0));
/*  63: 78 */     writeInt((int)(v >> 32));
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void writeShort(int v)
/*  67:    */   {
/*  68: 83 */     checkPosition(2);
/*  69: 84 */     int i = this._writeIndex;
/*  70: 85 */     this._buf[(i++)] = ((byte)(v >>> 0 & 0xFF));
/*  71: 86 */     this._buf[(i++)] = ((byte)(v >>> 8 & 0xFF));
/*  72: 87 */     this._writeIndex = i;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void write(int b)
/*  76:    */   {
/*  77: 92 */     writeByte(b);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void write(byte[] b)
/*  81:    */   {
/*  82: 97 */     int len = b.length;
/*  83: 98 */     checkPosition(len);
/*  84: 99 */     System.arraycopy(b, 0, this._buf, this._writeIndex, len);
/*  85:100 */     this._writeIndex += len;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void write(byte[] b, int offset, int len)
/*  89:    */   {
/*  90:105 */     checkPosition(len);
/*  91:106 */     System.arraycopy(b, offset, this._buf, this._writeIndex, len);
/*  92:107 */     this._writeIndex += len;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public int getWriteIndex()
/*  96:    */   {
/*  97:111 */     return this._writeIndex;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public LittleEndianOutput createDelayedOutput(int size)
/* 101:    */   {
/* 102:116 */     checkPosition(size);
/* 103:117 */     LittleEndianOutput result = new LittleEndianByteArrayOutputStream(this._buf, this._writeIndex, size);
/* 104:118 */     this._writeIndex += size;
/* 105:119 */     return result;
/* 106:    */   }
/* 107:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.LittleEndianByteArrayOutputStream
 * JD-Core Version:    0.7.0.1
 */