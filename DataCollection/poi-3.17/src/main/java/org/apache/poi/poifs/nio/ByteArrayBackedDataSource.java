/*  1:   */ package org.apache.poi.poifs.nio;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.OutputStream;
/*  5:   */ import java.nio.ByteBuffer;
/*  6:   */ 
/*  7:   */ public class ByteArrayBackedDataSource
/*  8:   */   extends DataSource
/*  9:   */ {
/* 10:   */   private byte[] buffer;
/* 11:   */   private long size;
/* 12:   */   
/* 13:   */   public ByteArrayBackedDataSource(byte[] data, int size)
/* 14:   */   {
/* 15:32 */     this.buffer = data;
/* 16:33 */     this.size = size;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public ByteArrayBackedDataSource(byte[] data)
/* 20:   */   {
/* 21:36 */     this(data, data.length);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public ByteBuffer read(int length, long position)
/* 25:   */   {
/* 26:41 */     if (position >= this.size) {
/* 27:42 */       throw new IndexOutOfBoundsException("Unable to read " + length + " bytes from " + position + " in stream of length " + this.size);
/* 28:   */     }
/* 29:48 */     int toRead = (int)Math.min(length, this.size - position);
/* 30:49 */     return ByteBuffer.wrap(this.buffer, (int)position, toRead);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void write(ByteBuffer src, long position)
/* 34:   */   {
/* 35:55 */     long endPosition = position + src.capacity();
/* 36:56 */     if (endPosition > this.buffer.length) {
/* 37:57 */       extend(endPosition);
/* 38:   */     }
/* 39:61 */     src.get(this.buffer, (int)position, src.capacity());
/* 40:64 */     if (endPosition > this.size) {
/* 41:65 */       this.size = endPosition;
/* 42:   */     }
/* 43:   */   }
/* 44:   */   
/* 45:   */   private void extend(long length)
/* 46:   */   {
/* 47:71 */     long difference = length - this.buffer.length;
/* 48:72 */     if (difference < this.buffer.length * 0.25D) {
/* 49:73 */       difference = (this.buffer.length * 0.25D);
/* 50:   */     }
/* 51:75 */     if (difference < 4096L) {
/* 52:76 */       difference = 4096L;
/* 53:   */     }
/* 54:79 */     byte[] nb = new byte[(int)(difference + this.buffer.length)];
/* 55:80 */     System.arraycopy(this.buffer, 0, nb, 0, (int)this.size);
/* 56:81 */     this.buffer = nb;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public void copyTo(OutputStream stream)
/* 60:   */     throws IOException
/* 61:   */   {
/* 62:86 */     stream.write(this.buffer, 0, (int)this.size);
/* 63:   */   }
/* 64:   */   
/* 65:   */   public long size()
/* 66:   */   {
/* 67:91 */     return this.size;
/* 68:   */   }
/* 69:   */   
/* 70:   */   public void close()
/* 71:   */   {
/* 72:96 */     this.buffer = null;
/* 73:97 */     this.size = -1L;
/* 74:   */   }
/* 75:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.nio.ByteArrayBackedDataSource
 * JD-Core Version:    0.7.0.1
 */