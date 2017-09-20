/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.util.Arrays;
/*   6:    */ 
/*   7:    */ public final class DocumentOutputStream
/*   8:    */   extends OutputStream
/*   9:    */ {
/*  10:    */   private final OutputStream _stream;
/*  11:    */   private final int _limit;
/*  12:    */   private int _written;
/*  13:    */   
/*  14:    */   DocumentOutputStream(OutputStream stream, int limit)
/*  15:    */   {
/*  16: 44 */     this._stream = stream;
/*  17: 45 */     this._limit = limit;
/*  18: 46 */     this._written = 0;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public void write(int b)
/*  22:    */     throws IOException
/*  23:    */   {
/*  24: 64 */     limitCheck(1);
/*  25: 65 */     this._stream.write(b);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void write(byte[] b)
/*  29:    */     throws IOException
/*  30:    */   {
/*  31: 78 */     write(b, 0, b.length);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void write(byte[] b, int off, int len)
/*  35:    */     throws IOException
/*  36:    */   {
/*  37:104 */     limitCheck(len);
/*  38:105 */     this._stream.write(b, off, len);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void flush()
/*  42:    */     throws IOException
/*  43:    */   {
/*  44:117 */     this._stream.flush();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void close() {}
/*  48:    */   
/*  49:    */   void writeFiller(int totalLimit, byte fill)
/*  50:    */     throws IOException
/*  51:    */   {
/*  52:145 */     if (totalLimit > this._written)
/*  53:    */     {
/*  54:147 */       byte[] filler = new byte[totalLimit - this._written];
/*  55:    */       
/*  56:149 */       Arrays.fill(filler, fill);
/*  57:150 */       this._stream.write(filler);
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   private void limitCheck(int toBeWritten)
/*  62:    */     throws IOException
/*  63:    */   {
/*  64:157 */     if (this._written + toBeWritten > this._limit) {
/*  65:159 */       throw new IOException("tried to write too much data");
/*  66:    */     }
/*  67:161 */     this._written += toBeWritten;
/*  68:    */   }
/*  69:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.DocumentOutputStream
 * JD-Core Version:    0.7.0.1
 */