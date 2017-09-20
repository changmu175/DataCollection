/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ 
/*   5:    */ public class LittleEndianByteArrayInputStream
/*   6:    */   extends ByteArrayInputStream
/*   7:    */   implements LittleEndianInput
/*   8:    */ {
/*   9:    */   public LittleEndianByteArrayInputStream(byte[] buf, int startOffset, int maxReadLen)
/*  10:    */   {
/*  11: 27 */     super(buf, startOffset, maxReadLen);
/*  12:    */   }
/*  13:    */   
/*  14:    */   public LittleEndianByteArrayInputStream(byte[] buf, int startOffset)
/*  15:    */   {
/*  16: 31 */     this(buf, startOffset, buf.length - startOffset);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public LittleEndianByteArrayInputStream(byte[] buf)
/*  20:    */   {
/*  21: 35 */     this(buf, 0);
/*  22:    */   }
/*  23:    */   
/*  24:    */   protected void checkPosition(int i)
/*  25:    */   {
/*  26: 39 */     if (i > this.count - this.pos) {
/*  27: 40 */       throw new RuntimeException("Buffer overrun");
/*  28:    */     }
/*  29:    */   }
/*  30:    */   
/*  31:    */   public int getReadIndex()
/*  32:    */   {
/*  33: 45 */     return this.pos;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setReadIndex(int pos)
/*  37:    */   {
/*  38: 49 */     if ((pos < 0) || (pos >= this.count)) {
/*  39: 50 */       throw new IndexOutOfBoundsException();
/*  40:    */     }
/*  41: 52 */     this.pos = pos;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public byte readByte()
/*  45:    */   {
/*  46: 58 */     checkPosition(1);
/*  47: 59 */     return (byte)read();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public int readInt()
/*  51:    */   {
/*  52: 64 */     int size = 4;
/*  53: 65 */     checkPosition(4);
/*  54: 66 */     int le = LittleEndian.getInt(this.buf, this.pos);
/*  55: 67 */     long skipped = super.skip(4L);
/*  56: 68 */     assert (skipped == 4L) : "Buffer overrun";
/*  57: 69 */     return le;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public long readLong()
/*  61:    */   {
/*  62: 74 */     int size = 8;
/*  63: 75 */     checkPosition(8);
/*  64: 76 */     long le = LittleEndian.getLong(this.buf, this.pos);
/*  65: 77 */     long skipped = super.skip(8L);
/*  66: 78 */     assert (skipped == 8L) : "Buffer overrun";
/*  67: 79 */     return le;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public short readShort()
/*  71:    */   {
/*  72: 84 */     int size = 2;
/*  73: 85 */     checkPosition(2);
/*  74: 86 */     short le = LittleEndian.getShort(this.buf, this.pos);
/*  75: 87 */     long skipped = super.skip(2L);
/*  76: 88 */     assert (skipped == 2L) : "Buffer overrun";
/*  77: 89 */     return le;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public int readUByte()
/*  81:    */   {
/*  82: 94 */     return readByte() & 0xFF;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public int readUShort()
/*  86:    */   {
/*  87: 99 */     return readShort() & 0xFFFF;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public long readUInt()
/*  91:    */   {
/*  92:103 */     return readInt() & 0xFFFFFFFF;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public double readDouble()
/*  96:    */   {
/*  97:108 */     return Double.longBitsToDouble(readLong());
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void readFully(byte[] buffer, int off, int len)
/* 101:    */   {
/* 102:113 */     checkPosition(len);
/* 103:114 */     read(buffer, off, len);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void readFully(byte[] buffer)
/* 107:    */   {
/* 108:119 */     checkPosition(buffer.length);
/* 109:120 */     read(buffer, 0, buffer.length);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void readPlain(byte[] buf, int off, int len)
/* 113:    */   {
/* 114:125 */     readFully(buf, off, len);
/* 115:    */   }
/* 116:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.LittleEndianByteArrayInputStream
 * JD-Core Version:    0.7.0.1
 */