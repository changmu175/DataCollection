/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.FilterInputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ 
/*   7:    */ public class LittleEndianInputStream
/*   8:    */   extends FilterInputStream
/*   9:    */   implements LittleEndianInput
/*  10:    */ {
/*  11:    */   private static final int EOF = -1;
/*  12:    */   
/*  13:    */   public LittleEndianInputStream(InputStream is)
/*  14:    */   {
/*  15: 35 */     super(is);
/*  16:    */   }
/*  17:    */   
/*  18:    */   @SuppressForbidden("just delegating")
/*  19:    */   public int available()
/*  20:    */   {
/*  21:    */     try
/*  22:    */     {
/*  23: 42 */       return super.available();
/*  24:    */     }
/*  25:    */     catch (IOException e)
/*  26:    */     {
/*  27: 44 */       throw new RuntimeException(e);
/*  28:    */     }
/*  29:    */   }
/*  30:    */   
/*  31:    */   public byte readByte()
/*  32:    */   {
/*  33: 50 */     return (byte)readUByte();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public int readUByte()
/*  37:    */   {
/*  38: 55 */     byte[] buf = new byte[1];
/*  39:    */     try
/*  40:    */     {
/*  41: 57 */       checkEOF(read(buf), 1);
/*  42:    */     }
/*  43:    */     catch (IOException e)
/*  44:    */     {
/*  45: 59 */       throw new RuntimeException(e);
/*  46:    */     }
/*  47: 61 */     return LittleEndian.getUByte(buf);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public double readDouble()
/*  51:    */   {
/*  52: 66 */     return Double.longBitsToDouble(readLong());
/*  53:    */   }
/*  54:    */   
/*  55:    */   public int readInt()
/*  56:    */   {
/*  57: 71 */     byte[] buf = new byte[4];
/*  58:    */     try
/*  59:    */     {
/*  60: 73 */       checkEOF(read(buf), buf.length);
/*  61:    */     }
/*  62:    */     catch (IOException e)
/*  63:    */     {
/*  64: 75 */       throw new RuntimeException(e);
/*  65:    */     }
/*  66: 77 */     return LittleEndian.getInt(buf);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public long readUInt()
/*  70:    */   {
/*  71: 89 */     long retNum = readInt();
/*  72: 90 */     return retNum & 0xFFFFFFFF;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public long readLong()
/*  76:    */   {
/*  77: 95 */     byte[] buf = new byte[8];
/*  78:    */     try
/*  79:    */     {
/*  80: 97 */       checkEOF(read(buf), 8);
/*  81:    */     }
/*  82:    */     catch (IOException e)
/*  83:    */     {
/*  84: 99 */       throw new RuntimeException(e);
/*  85:    */     }
/*  86:101 */     return LittleEndian.getLong(buf);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public short readShort()
/*  90:    */   {
/*  91:106 */     return (short)readUShort();
/*  92:    */   }
/*  93:    */   
/*  94:    */   public int readUShort()
/*  95:    */   {
/*  96:111 */     byte[] buf = new byte[2];
/*  97:    */     try
/*  98:    */     {
/*  99:113 */       checkEOF(read(buf), 2);
/* 100:    */     }
/* 101:    */     catch (IOException e)
/* 102:    */     {
/* 103:115 */       throw new RuntimeException(e);
/* 104:    */     }
/* 105:117 */     return LittleEndian.getUShort(buf);
/* 106:    */   }
/* 107:    */   
/* 108:    */   private static void checkEOF(int actualBytes, int expectedBytes)
/* 109:    */   {
/* 110:121 */     if ((expectedBytes != 0) && ((actualBytes == -1) || (actualBytes != expectedBytes))) {
/* 111:122 */       throw new RuntimeException("Unexpected end-of-file");
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void readFully(byte[] buf)
/* 116:    */   {
/* 117:128 */     readFully(buf, 0, buf.length);
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void readFully(byte[] buf, int off, int len)
/* 121:    */   {
/* 122:    */     try
/* 123:    */     {
/* 124:134 */       checkEOF(_read(buf, off, len), len);
/* 125:    */     }
/* 126:    */     catch (IOException e)
/* 127:    */     {
/* 128:136 */       throw new RuntimeException(e);
/* 129:    */     }
/* 130:    */   }
/* 131:    */   
/* 132:    */   private int _read(byte[] buffer, int offset, int length)
/* 133:    */     throws IOException
/* 134:    */   {
/* 135:143 */     int remaining = length;
/* 136:144 */     while (remaining > 0)
/* 137:    */     {
/* 138:145 */       int location = length - remaining;
/* 139:146 */       int count = read(buffer, offset + location, remaining);
/* 140:147 */       if (-1 == count) {
/* 141:    */         break;
/* 142:    */       }
/* 143:150 */       remaining -= count;
/* 144:    */     }
/* 145:153 */     return length - remaining;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public void readPlain(byte[] buf, int off, int len)
/* 149:    */   {
/* 150:158 */     readFully(buf, off, len);
/* 151:    */   }
/* 152:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.LittleEndianInputStream
 * JD-Core Version:    0.7.0.1
 */