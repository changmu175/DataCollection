/*  1:   */ package org.apache.poi.openxml4j.opc.internal;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayOutputStream;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.OutputStream;
/*  6:   */ 
/*  7:   */ public final class MemoryPackagePartOutputStream
/*  8:   */   extends OutputStream
/*  9:   */ {
/* 10:   */   private MemoryPackagePart _part;
/* 11:   */   private ByteArrayOutputStream _buff;
/* 12:   */   
/* 13:   */   public MemoryPackagePartOutputStream(MemoryPackagePart part)
/* 14:   */   {
/* 15:36 */     this._part = part;
/* 16:37 */     this._buff = new ByteArrayOutputStream();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void write(int b)
/* 20:   */   {
/* 21:42 */     this._buff.write(b);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void close()
/* 25:   */     throws IOException
/* 26:   */   {
/* 27:51 */     flush();
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void flush()
/* 31:   */     throws IOException
/* 32:   */   {
/* 33:61 */     this._buff.flush();
/* 34:62 */     if (this._part.data != null)
/* 35:   */     {
/* 36:63 */       byte[] newArray = new byte[this._part.data.length + this._buff.size()];
/* 37:   */       
/* 38:65 */       System.arraycopy(this._part.data, 0, newArray, 0, this._part.data.length);
/* 39:   */       
/* 40:   */ 
/* 41:68 */       byte[] buffArr = this._buff.toByteArray();
/* 42:69 */       System.arraycopy(buffArr, 0, newArray, this._part.data.length, buffArr.length);
/* 43:   */       
/* 44:   */ 
/* 45:   */ 
/* 46:73 */       this._part.data = newArray;
/* 47:   */     }
/* 48:   */     else
/* 49:   */     {
/* 50:76 */       this._part.data = this._buff.toByteArray();
/* 51:   */     }
/* 52:83 */     this._buff.reset();
/* 53:   */   }
/* 54:   */   
/* 55:   */   public void write(byte[] b, int off, int len)
/* 56:   */   {
/* 57:88 */     this._buff.write(b, off, len);
/* 58:   */   }
/* 59:   */   
/* 60:   */   public void write(byte[] b)
/* 61:   */     throws IOException
/* 62:   */   {
/* 63:93 */     this._buff.write(b);
/* 64:   */   }
/* 65:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.internal.MemoryPackagePartOutputStream
 * JD-Core Version:    0.7.0.1
 */