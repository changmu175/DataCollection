/*  1:   */ package org.apache.poi.xssf.binary;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ import org.apache.poi.xssf.usermodel.helpers.HeaderFooterHelper;
/*  5:   */ 
/*  6:   */ @Internal
/*  7:   */ class XSSFBHeaderFooter
/*  8:   */ {
/*  9:29 */   private static final HeaderFooterHelper HEADER_FOOTER_HELPER = new HeaderFooterHelper();
/* 10:   */   private final String headerFooterTypeLabel;
/* 11:   */   private final boolean isHeader;
/* 12:   */   private String rawString;
/* 13:   */   
/* 14:   */   XSSFBHeaderFooter(String headerFooterTypeLabel, boolean isHeader)
/* 15:   */   {
/* 16:37 */     this.headerFooterTypeLabel = headerFooterTypeLabel;
/* 17:38 */     this.isHeader = isHeader;
/* 18:   */   }
/* 19:   */   
/* 20:   */   String getHeaderFooterTypeLabel()
/* 21:   */   {
/* 22:42 */     return this.headerFooterTypeLabel;
/* 23:   */   }
/* 24:   */   
/* 25:   */   String getRawString()
/* 26:   */   {
/* 27:46 */     return this.rawString;
/* 28:   */   }
/* 29:   */   
/* 30:   */   String getString()
/* 31:   */   {
/* 32:50 */     StringBuilder sb = new StringBuilder();
/* 33:51 */     String left = HEADER_FOOTER_HELPER.getLeftSection(this.rawString);
/* 34:52 */     String center = HEADER_FOOTER_HELPER.getCenterSection(this.rawString);
/* 35:53 */     String right = HEADER_FOOTER_HELPER.getRightSection(this.rawString);
/* 36:54 */     if ((left != null) && (left.length() > 0)) {
/* 37:55 */       sb.append(left);
/* 38:   */     }
/* 39:57 */     if ((center != null) && (center.length() > 0))
/* 40:   */     {
/* 41:58 */       if (sb.length() > 0) {
/* 42:59 */         sb.append(" ");
/* 43:   */       }
/* 44:61 */       sb.append(center);
/* 45:   */     }
/* 46:63 */     if ((right != null) && (right.length() > 0))
/* 47:   */     {
/* 48:64 */       if (sb.length() > 0) {
/* 49:65 */         sb.append(" ");
/* 50:   */       }
/* 51:67 */       sb.append(right);
/* 52:   */     }
/* 53:69 */     return sb.toString();
/* 54:   */   }
/* 55:   */   
/* 56:   */   void setRawString(String rawString)
/* 57:   */   {
/* 58:73 */     this.rawString = rawString;
/* 59:   */   }
/* 60:   */   
/* 61:   */   boolean isHeader()
/* 62:   */   {
/* 63:77 */     return this.isHeader;
/* 64:   */   }
/* 65:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFBHeaderFooter
 * JD-Core Version:    0.7.0.1
 */