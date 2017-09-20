/*   1:    */ package org.apache.poi.xssf.binary;
/*   2:    */ 
/*   3:    */ import java.nio.charset.Charset;
/*   4:    */ import org.apache.poi.POIXMLException;
/*   5:    */ import org.apache.poi.util.Internal;
/*   6:    */ import org.apache.poi.util.LittleEndian;
/*   7:    */ 
/*   8:    */ @Internal
/*   9:    */ public class XSSFBUtils
/*  10:    */ {
/*  11:    */   static int readXLNullableWideString(byte[] data, int offset, StringBuilder sb)
/*  12:    */     throws XSSFBParseException
/*  13:    */   {
/*  14: 42 */     long numChars = LittleEndian.getUInt(data, offset);
/*  15: 43 */     if (numChars < 0L) {
/*  16: 44 */       throw new XSSFBParseException("too few chars to read");
/*  17:    */     }
/*  18: 45 */     if (numChars == 4294967295L) {
/*  19: 46 */       return 0;
/*  20:    */     }
/*  21: 47 */     if (numChars > 4294967295L) {
/*  22: 48 */       throw new XSSFBParseException("too many chars to read");
/*  23:    */     }
/*  24: 51 */     int numBytes = 2 * (int)numChars;
/*  25: 52 */     offset += 4;
/*  26: 53 */     if (offset + numBytes > data.length) {
/*  27: 54 */       throw new XSSFBParseException("trying to read beyond data length:offset=" + offset + ", numBytes=" + numBytes + ", data.length=" + data.length);
/*  28:    */     }
/*  29: 57 */     sb.append(new String(data, offset, numBytes, Charset.forName("UTF-16LE")));
/*  30: 58 */     numBytes += 4;
/*  31: 59 */     return numBytes;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static int readXLWideString(byte[] data, int offset, StringBuilder sb)
/*  35:    */     throws XSSFBParseException
/*  36:    */   {
/*  37: 72 */     long numChars = LittleEndian.getUInt(data, offset);
/*  38: 73 */     if (numChars < 0L) {
/*  39: 74 */       throw new XSSFBParseException("too few chars to read");
/*  40:    */     }
/*  41: 75 */     if (numChars > 4294967295L) {
/*  42: 76 */       throw new XSSFBParseException("too many chars to read");
/*  43:    */     }
/*  44: 78 */     int numBytes = 2 * (int)numChars;
/*  45: 79 */     offset += 4;
/*  46: 80 */     if (offset + numBytes > data.length) {
/*  47: 81 */       throw new XSSFBParseException("trying to read beyond data length");
/*  48:    */     }
/*  49: 83 */     sb.append(new String(data, offset, numBytes, Charset.forName("UTF-16LE")));
/*  50: 84 */     numBytes += 4;
/*  51: 85 */     return numBytes;
/*  52:    */   }
/*  53:    */   
/*  54:    */   static int castToInt(long val)
/*  55:    */   {
/*  56: 89 */     if ((val < 2147483647L) && (val > -2147483648L)) {
/*  57: 90 */       return (int)val;
/*  58:    */     }
/*  59: 92 */     throw new POIXMLException("val (" + val + ") can't be cast to int");
/*  60:    */   }
/*  61:    */   
/*  62:    */   static short castToShort(int val)
/*  63:    */   {
/*  64: 96 */     if ((val < 32767) && (val > -32768)) {
/*  65: 97 */       return (short)val;
/*  66:    */     }
/*  67: 99 */     throw new POIXMLException("val (" + val + ") can't be cast to short");
/*  68:    */   }
/*  69:    */   
/*  70:    */   static int get24BitInt(byte[] data, int offset)
/*  71:    */   {
/*  72:105 */     int i = offset;
/*  73:106 */     int b0 = data[(i++)] & 0xFF;
/*  74:107 */     int b1 = data[(i++)] & 0xFF;
/*  75:108 */     int b2 = data[i] & 0xFF;
/*  76:109 */     return (b2 << 16) + (b1 << 8) + b0;
/*  77:    */   }
/*  78:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFBUtils
 * JD-Core Version:    0.7.0.1
 */