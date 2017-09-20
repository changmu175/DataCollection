/*  1:   */ package org.apache.poi.xssf.binary;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ 
/*  5:   */ @Internal
/*  6:   */ class XSSFBRichStr
/*  7:   */ {
/*  8:   */   private final String string;
/*  9:   */   private final String phoneticString;
/* 10:   */   
/* 11:   */   public static XSSFBRichStr build(byte[] bytes, int offset)
/* 12:   */     throws XSSFBParseException
/* 13:   */   {
/* 14:29 */     byte first = bytes[offset];
/* 15:30 */     boolean dwSizeStrRunExists = (first >> 7 & 0x1) == 1;
/* 16:31 */     boolean phoneticExists = (first >> 6 & 0x1) == 1;
/* 17:32 */     StringBuilder sb = new StringBuilder();
/* 18:   */     
/* 19:34 */     int read = XSSFBUtils.readXLWideString(bytes, offset + 1, sb);
/* 20:   */     
/* 21:36 */     return new XSSFBRichStr(sb.toString(), "");
/* 22:   */   }
/* 23:   */   
/* 24:   */   XSSFBRichStr(String string, String phoneticString)
/* 25:   */   {
/* 26:43 */     this.string = string;
/* 27:44 */     this.phoneticString = phoneticString;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String getString()
/* 31:   */   {
/* 32:48 */     return this.string;
/* 33:   */   }
/* 34:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFBRichStr
 * JD-Core Version:    0.7.0.1
 */