/*  1:   */ package org.apache.poi.util;
/*  2:   */ 
/*  3:   */ import java.io.FilterOutputStream;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.OutputStream;
/*  6:   */ 
/*  7:   */ public final class LittleEndianOutputStream
/*  8:   */   extends FilterOutputStream
/*  9:   */   implements LittleEndianOutput
/* 10:   */ {
/* 11:   */   public LittleEndianOutputStream(OutputStream out)
/* 12:   */   {
/* 13:30 */     super(out);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void writeByte(int v)
/* 17:   */   {
/* 18:   */     try
/* 19:   */     {
/* 20:36 */       this.out.write(v);
/* 21:   */     }
/* 22:   */     catch (IOException e)
/* 23:   */     {
/* 24:38 */       throw new RuntimeException(e);
/* 25:   */     }
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void writeDouble(double v)
/* 29:   */   {
/* 30:44 */     writeLong(Double.doubleToLongBits(v));
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void writeInt(int v)
/* 34:   */   {
/* 35:49 */     int b3 = v >>> 24 & 0xFF;
/* 36:50 */     int b2 = v >>> 16 & 0xFF;
/* 37:51 */     int b1 = v >>> 8 & 0xFF;
/* 38:52 */     int b0 = v & 0xFF;
/* 39:   */     try
/* 40:   */     {
/* 41:54 */       this.out.write(b0);
/* 42:55 */       this.out.write(b1);
/* 43:56 */       this.out.write(b2);
/* 44:57 */       this.out.write(b3);
/* 45:   */     }
/* 46:   */     catch (IOException e)
/* 47:   */     {
/* 48:59 */       throw new RuntimeException(e);
/* 49:   */     }
/* 50:   */   }
/* 51:   */   
/* 52:   */   public void writeLong(long v)
/* 53:   */   {
/* 54:65 */     writeInt((int)v);
/* 55:66 */     writeInt((int)(v >> 32));
/* 56:   */   }
/* 57:   */   
/* 58:   */   public void writeShort(int v)
/* 59:   */   {
/* 60:71 */     int b1 = v >>> 8 & 0xFF;
/* 61:72 */     int b0 = v & 0xFF;
/* 62:   */     try
/* 63:   */     {
/* 64:74 */       this.out.write(b0);
/* 65:75 */       this.out.write(b1);
/* 66:   */     }
/* 67:   */     catch (IOException e)
/* 68:   */     {
/* 69:77 */       throw new RuntimeException(e);
/* 70:   */     }
/* 71:   */   }
/* 72:   */   
/* 73:   */   public void write(byte[] b)
/* 74:   */   {
/* 75:   */     try
/* 76:   */     {
/* 77:84 */       super.write(b);
/* 78:   */     }
/* 79:   */     catch (IOException e)
/* 80:   */     {
/* 81:86 */       throw new RuntimeException(e);
/* 82:   */     }
/* 83:   */   }
/* 84:   */   
/* 85:   */   public void write(byte[] b, int off, int len)
/* 86:   */   {
/* 87:   */     try
/* 88:   */     {
/* 89:93 */       super.write(b, off, len);
/* 90:   */     }
/* 91:   */     catch (IOException e)
/* 92:   */     {
/* 93:95 */       throw new RuntimeException(e);
/* 94:   */     }
/* 95:   */   }
/* 96:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.LittleEndianOutputStream
 * JD-Core Version:    0.7.0.1
 */