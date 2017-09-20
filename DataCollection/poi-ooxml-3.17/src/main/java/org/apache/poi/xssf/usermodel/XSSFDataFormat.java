/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.BuiltinFormats;
/*  4:   */ import org.apache.poi.ss.usermodel.DataFormat;
/*  5:   */ import org.apache.poi.util.Removal;
/*  6:   */ import org.apache.poi.xssf.model.StylesTable;
/*  7:   */ 
/*  8:   */ public class XSSFDataFormat
/*  9:   */   implements DataFormat
/* 10:   */ {
/* 11:   */   private final StylesTable stylesSource;
/* 12:   */   
/* 13:   */   protected XSSFDataFormat(StylesTable stylesSource)
/* 14:   */   {
/* 15:39 */     this.stylesSource = stylesSource;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public short getFormat(String format)
/* 19:   */   {
/* 20:52 */     int idx = BuiltinFormats.getBuiltinFormat(format);
/* 21:53 */     if (idx == -1) {
/* 22:53 */       idx = this.stylesSource.putNumberFormat(format);
/* 23:   */     }
/* 24:54 */     return (short)idx;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public String getFormat(short index)
/* 28:   */   {
/* 29:70 */     String fmt = this.stylesSource.getNumberFormatAt(index);
/* 30:71 */     if (fmt == null) {
/* 31:71 */       fmt = BuiltinFormats.getBuiltinFormat(index);
/* 32:   */     }
/* 33:72 */     return fmt;
/* 34:   */   }
/* 35:   */   
/* 36:   */   /**
/* 37:   */    * @deprecated
/* 38:   */    */
/* 39:   */   @Removal(version="3.18")
/* 40:   */   public String getFormat(int index)
/* 41:   */   {
/* 42:83 */     return getFormat((short)index);
/* 43:   */   }
/* 44:   */   
/* 45:   */   public void putFormat(short index, String format)
/* 46:   */   {
/* 47:96 */     this.stylesSource.putNumberFormat(index, format);
/* 48:   */   }
/* 49:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFDataFormat
 * JD-Core Version:    0.7.0.1
 */