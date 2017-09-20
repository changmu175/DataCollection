/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ 
/*   6:    */ public class BoundedInputStream
/*   7:    */   extends InputStream
/*   8:    */ {
/*   9:    */   private final InputStream in;
/*  10:    */   private final long max;
/*  11: 45 */   private long pos = 0L;
/*  12: 48 */   private long mark = -1L;
/*  13: 51 */   private boolean propagateClose = true;
/*  14:    */   
/*  15:    */   public BoundedInputStream(InputStream in, long size)
/*  16:    */   {
/*  17: 63 */     this.max = size;
/*  18: 64 */     this.in = in;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public BoundedInputStream(InputStream in)
/*  22:    */   {
/*  23: 74 */     this(in, -1L);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public int read()
/*  27:    */     throws IOException
/*  28:    */   {
/*  29: 86 */     if ((this.max >= 0L) && (this.pos == this.max)) {
/*  30: 87 */       return -1;
/*  31:    */     }
/*  32: 89 */     int result = this.in.read();
/*  33: 90 */     this.pos += 1L;
/*  34: 91 */     return result;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public int read(byte[] b)
/*  38:    */     throws IOException
/*  39:    */   {
/*  40:103 */     return read(b, 0, b.length);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public int read(byte[] b, int off, int len)
/*  44:    */     throws IOException
/*  45:    */   {
/*  46:117 */     if ((this.max >= 0L) && (this.pos >= this.max)) {
/*  47:118 */       return -1;
/*  48:    */     }
/*  49:120 */     long maxRead = this.max >= 0L ? Math.min(len, this.max - this.pos) : len;
/*  50:121 */     int bytesRead = this.in.read(b, off, (int)maxRead);
/*  51:123 */     if (bytesRead == -1) {
/*  52:124 */       return -1;
/*  53:    */     }
/*  54:127 */     this.pos += bytesRead;
/*  55:128 */     return bytesRead;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public long skip(long n)
/*  59:    */     throws IOException
/*  60:    */   {
/*  61:139 */     long toSkip = this.max >= 0L ? Math.min(n, this.max - this.pos) : n;
/*  62:140 */     long skippedBytes = this.in.skip(toSkip);
/*  63:141 */     this.pos += skippedBytes;
/*  64:142 */     return skippedBytes;
/*  65:    */   }
/*  66:    */   
/*  67:    */   @SuppressForbidden("just delegating")
/*  68:    */   public int available()
/*  69:    */     throws IOException
/*  70:    */   {
/*  71:151 */     if ((this.max >= 0L) && (this.pos >= this.max)) {
/*  72:152 */       return 0;
/*  73:    */     }
/*  74:154 */     return this.in.available();
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String toString()
/*  78:    */   {
/*  79:163 */     return this.in.toString();
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void close()
/*  83:    */     throws IOException
/*  84:    */   {
/*  85:173 */     if (this.propagateClose) {
/*  86:174 */       this.in.close();
/*  87:    */     }
/*  88:    */   }
/*  89:    */   
/*  90:    */   public synchronized void reset()
/*  91:    */     throws IOException
/*  92:    */   {
/*  93:184 */     this.in.reset();
/*  94:185 */     this.pos = this.mark;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public synchronized void mark(int readlimit)
/*  98:    */   {
/*  99:194 */     this.in.mark(readlimit);
/* 100:195 */     this.mark = this.pos;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public boolean markSupported()
/* 104:    */   {
/* 105:204 */     return this.in.markSupported();
/* 106:    */   }
/* 107:    */   
/* 108:    */   public boolean isPropagateClose()
/* 109:    */   {
/* 110:216 */     return this.propagateClose;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setPropagateClose(boolean propagateClose)
/* 114:    */   {
/* 115:229 */     this.propagateClose = propagateClose;
/* 116:    */   }
/* 117:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.BoundedInputStream
 * JD-Core Version:    0.7.0.1
 */