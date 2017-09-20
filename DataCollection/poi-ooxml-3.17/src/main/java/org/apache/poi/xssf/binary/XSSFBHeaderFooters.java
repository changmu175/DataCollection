/*  1:   */ package org.apache.poi.xssf.binary;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ 
/*  5:   */ @Internal
/*  6:   */ class XSSFBHeaderFooters
/*  7:   */ {
/*  8:   */   private XSSFBHeaderFooter header;
/*  9:   */   private XSSFBHeaderFooter footer;
/* 10:   */   private XSSFBHeaderFooter headerEven;
/* 11:   */   private XSSFBHeaderFooter footerEven;
/* 12:   */   private XSSFBHeaderFooter headerFirst;
/* 13:   */   private XSSFBHeaderFooter footerFirst;
/* 14:   */   
/* 15:   */   public static XSSFBHeaderFooters parse(byte[] data)
/* 16:   */   {
/* 17:29 */     boolean diffOddEven = false;
/* 18:30 */     boolean diffFirst = false;
/* 19:31 */     boolean scaleWDoc = false;
/* 20:32 */     boolean alignMargins = false;
/* 21:   */     
/* 22:34 */     int offset = 2;
/* 23:35 */     XSSFBHeaderFooters xssfbHeaderFooter = new XSSFBHeaderFooters();
/* 24:36 */     xssfbHeaderFooter.header = new XSSFBHeaderFooter("header", true);
/* 25:37 */     xssfbHeaderFooter.footer = new XSSFBHeaderFooter("footer", false);
/* 26:38 */     xssfbHeaderFooter.headerEven = new XSSFBHeaderFooter("evenHeader", true);
/* 27:39 */     xssfbHeaderFooter.footerEven = new XSSFBHeaderFooter("evenFooter", false);
/* 28:40 */     xssfbHeaderFooter.headerFirst = new XSSFBHeaderFooter("firstHeader", true);
/* 29:41 */     xssfbHeaderFooter.footerFirst = new XSSFBHeaderFooter("firstFooter", false);
/* 30:42 */     offset += readHeaderFooter(data, offset, xssfbHeaderFooter.header);
/* 31:43 */     offset += readHeaderFooter(data, offset, xssfbHeaderFooter.footer);
/* 32:44 */     offset += readHeaderFooter(data, offset, xssfbHeaderFooter.headerEven);
/* 33:45 */     offset += readHeaderFooter(data, offset, xssfbHeaderFooter.footerEven);
/* 34:46 */     offset += readHeaderFooter(data, offset, xssfbHeaderFooter.headerFirst);
/* 35:47 */     readHeaderFooter(data, offset, xssfbHeaderFooter.footerFirst);
/* 36:48 */     return xssfbHeaderFooter;
/* 37:   */   }
/* 38:   */   
/* 39:   */   private static int readHeaderFooter(byte[] data, int offset, XSSFBHeaderFooter headerFooter)
/* 40:   */   {
/* 41:52 */     if (offset + 4 >= data.length) {
/* 42:53 */       return 0;
/* 43:   */     }
/* 44:55 */     StringBuilder sb = new StringBuilder();
/* 45:56 */     int bytesRead = XSSFBUtils.readXLNullableWideString(data, offset, sb);
/* 46:57 */     headerFooter.setRawString(sb.toString());
/* 47:58 */     return bytesRead;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public XSSFBHeaderFooter getHeader()
/* 51:   */   {
/* 52:69 */     return this.header;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public XSSFBHeaderFooter getFooter()
/* 56:   */   {
/* 57:73 */     return this.footer;
/* 58:   */   }
/* 59:   */   
/* 60:   */   public XSSFBHeaderFooter getHeaderEven()
/* 61:   */   {
/* 62:77 */     return this.headerEven;
/* 63:   */   }
/* 64:   */   
/* 65:   */   public XSSFBHeaderFooter getFooterEven()
/* 66:   */   {
/* 67:81 */     return this.footerEven;
/* 68:   */   }
/* 69:   */   
/* 70:   */   public XSSFBHeaderFooter getHeaderFirst()
/* 71:   */   {
/* 72:85 */     return this.headerFirst;
/* 73:   */   }
/* 74:   */   
/* 75:   */   public XSSFBHeaderFooter getFooterFirst()
/* 76:   */   {
/* 77:89 */     return this.footerFirst;
/* 78:   */   }
/* 79:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFBHeaderFooters
 * JD-Core Version:    0.7.0.1
 */