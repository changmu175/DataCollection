/*  1:   */ package org.apache.poi.xssf.binary;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.Font;
/*  4:   */ import org.apache.poi.util.Internal;
/*  5:   */ import org.apache.poi.util.NotImplemented;
/*  6:   */ import org.apache.poi.xssf.usermodel.XSSFRichTextString;
/*  7:   */ 
/*  8:   */ @Internal
/*  9:   */ class XSSFBRichTextString
/* 10:   */   extends XSSFRichTextString
/* 11:   */ {
/* 12:   */   private final String string;
/* 13:   */   
/* 14:   */   XSSFBRichTextString(String string)
/* 15:   */   {
/* 16:37 */     this.string = string;
/* 17:   */   }
/* 18:   */   
/* 19:   */   @NotImplemented
/* 20:   */   public void applyFont(int startIndex, int endIndex, short fontIndex) {}
/* 21:   */   
/* 22:   */   @NotImplemented
/* 23:   */   public void applyFont(int startIndex, int endIndex, Font font) {}
/* 24:   */   
/* 25:   */   @NotImplemented
/* 26:   */   public void applyFont(Font font) {}
/* 27:   */   
/* 28:   */   @NotImplemented
/* 29:   */   public void clearFormatting() {}
/* 30:   */   
/* 31:   */   public String getString()
/* 32:   */   {
/* 33:66 */     return this.string;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public int length()
/* 37:   */   {
/* 38:71 */     return this.string.length();
/* 39:   */   }
/* 40:   */   
/* 41:   */   @NotImplemented
/* 42:   */   public int numFormattingRuns()
/* 43:   */   {
/* 44:77 */     return 0;
/* 45:   */   }
/* 46:   */   
/* 47:   */   @NotImplemented
/* 48:   */   public int getIndexOfFormattingRun(int index)
/* 49:   */   {
/* 50:83 */     return 0;
/* 51:   */   }
/* 52:   */   
/* 53:   */   @NotImplemented
/* 54:   */   public void applyFont(short fontIndex) {}
/* 55:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFBRichTextString
 * JD-Core Version:    0.7.0.1
 */