/*  1:   */ package org.apache.poi.xssf.binary;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import java.util.BitSet;
/*  6:   */ import org.apache.poi.util.Internal;
/*  7:   */ import org.apache.poi.util.LittleEndianInputStream;
/*  8:   */ 
/*  9:   */ @Internal
/* 10:   */ public abstract class XSSFBParser
/* 11:   */ {
/* 12:   */   private final LittleEndianInputStream is;
/* 13:   */   private final BitSet records;
/* 14:   */   
/* 15:   */   public XSSFBParser(InputStream is)
/* 16:   */   {
/* 17:41 */     this.is = new LittleEndianInputStream(is);
/* 18:42 */     this.records = null;
/* 19:   */   }
/* 20:   */   
/* 21:   */   protected XSSFBParser(InputStream is, BitSet bitSet)
/* 22:   */   {
/* 23:51 */     this.is = new LittleEndianInputStream(is);
/* 24:52 */     this.records = bitSet;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void parse()
/* 28:   */     throws IOException
/* 29:   */   {
/* 30:   */     for (;;)
/* 31:   */     {
/* 32:58 */       int bInt = this.is.read();
/* 33:59 */       if (bInt == -1) {
/* 34:60 */         return;
/* 35:   */       }
/* 36:62 */       readNext((byte)bInt);
/* 37:   */     }
/* 38:   */   }
/* 39:   */   
/* 40:   */   private void readNext(byte b1)
/* 41:   */     throws IOException
/* 42:   */   {
/* 43:67 */     int recordId = 0;
/* 44:70 */     if ((b1 >> 7 & 0x1) == 1)
/* 45:   */     {
/* 46:71 */       byte b2 = this.is.readByte();
/* 47:72 */       b1 = (byte)(b1 & 0xFFFFFF7F);
/* 48:73 */       b2 = (byte)(b2 & 0xFFFFFF7F);
/* 49:74 */       recordId = (b2 << 7) + b1;
/* 50:   */     }
/* 51:   */     else
/* 52:   */     {
/* 53:76 */       recordId = b1;
/* 54:   */     }
/* 55:79 */     long recordLength = 0L;
/* 56:80 */     int i = 0;
/* 57:81 */     boolean halt = false;
/* 58:82 */     while ((i < 4) && (!halt))
/* 59:   */     {
/* 60:83 */       byte b = this.is.readByte();
/* 61:84 */       halt = (b >> 7 & 0x1) == 0;
/* 62:85 */       b = (byte)(b & 0xFFFFFF7F);
/* 63:86 */       recordLength += (b << i * 7);
/* 64:87 */       i++;
/* 65:   */     }
/* 66:90 */     if ((this.records == null) || (this.records.get(recordId)))
/* 67:   */     {
/* 68:92 */       byte[] buff = new byte[(int)recordLength];
/* 69:93 */       this.is.readFully(buff);
/* 70:94 */       handleRecord(recordId, buff);
/* 71:   */     }
/* 72:   */     else
/* 73:   */     {
/* 74:96 */       long length = this.is.skip(recordLength);
/* 75:97 */       if (length != recordLength) {
/* 76:98 */         throw new XSSFBParseException("End of file reached before expected.\tTried to skip " + recordLength + ", but only skipped " + length);
/* 77:   */       }
/* 78:   */     }
/* 79:   */   }
/* 80:   */   
/* 81:   */   public abstract void handleRecord(int paramInt, byte[] paramArrayOfByte)
/* 82:   */     throws XSSFBParseException;
/* 83:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFBParser
 * JD-Core Version:    0.7.0.1
 */