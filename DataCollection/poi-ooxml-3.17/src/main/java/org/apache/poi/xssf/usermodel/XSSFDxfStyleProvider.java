/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.BorderFormatting;
/*  4:   */ import org.apache.poi.ss.usermodel.DifferentialStyleProvider;
/*  5:   */ import org.apache.poi.ss.usermodel.ExcelNumberFormat;
/*  6:   */ import org.apache.poi.ss.usermodel.FontFormatting;
/*  7:   */ import org.apache.poi.ss.usermodel.PatternFormatting;
/*  8:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf;
/*  9:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt;
/* 10:   */ 
/* 11:   */ public class XSSFDxfStyleProvider
/* 12:   */   implements DifferentialStyleProvider
/* 13:   */ {
/* 14:   */   private final IndexedColorMap colorMap;
/* 15:   */   private final BorderFormatting border;
/* 16:   */   private final FontFormatting font;
/* 17:   */   private final ExcelNumberFormat number;
/* 18:   */   private final PatternFormatting fill;
/* 19:   */   private final int stripeSize;
/* 20:   */   
/* 21:   */   public XSSFDxfStyleProvider(CTDxf dxf, int stripeSize, IndexedColorMap colorMap)
/* 22:   */   {
/* 23:46 */     this.stripeSize = stripeSize;
/* 24:47 */     this.colorMap = colorMap;
/* 25:48 */     if (dxf == null)
/* 26:   */     {
/* 27:49 */       this.border = null;
/* 28:50 */       this.font = null;
/* 29:51 */       this.number = null;
/* 30:52 */       this.fill = null;
/* 31:   */     }
/* 32:   */     else
/* 33:   */     {
/* 34:54 */       this.border = (dxf.isSetBorder() ? new XSSFBorderFormatting(dxf.getBorder(), colorMap) : null);
/* 35:55 */       this.font = (dxf.isSetFont() ? new XSSFFontFormatting(dxf.getFont(), colorMap) : null);
/* 36:56 */       if (dxf.isSetNumFmt())
/* 37:   */       {
/* 38:57 */         CTNumFmt numFmt = dxf.getNumFmt();
/* 39:58 */         this.number = new ExcelNumberFormat((int)numFmt.getNumFmtId(), numFmt.getFormatCode());
/* 40:   */       }
/* 41:   */       else
/* 42:   */       {
/* 43:60 */         this.number = null;
/* 44:   */       }
/* 45:62 */       this.fill = (dxf.isSetFill() ? new XSSFPatternFormatting(dxf.getFill(), colorMap) : null);
/* 46:   */     }
/* 47:   */   }
/* 48:   */   
/* 49:   */   public BorderFormatting getBorderFormatting()
/* 50:   */   {
/* 51:67 */     return this.border;
/* 52:   */   }
/* 53:   */   
/* 54:   */   public FontFormatting getFontFormatting()
/* 55:   */   {
/* 56:71 */     return this.font;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public ExcelNumberFormat getNumberFormat()
/* 60:   */   {
/* 61:75 */     return this.number;
/* 62:   */   }
/* 63:   */   
/* 64:   */   public PatternFormatting getPatternFormatting()
/* 65:   */   {
/* 66:79 */     return this.fill;
/* 67:   */   }
/* 68:   */   
/* 69:   */   public int getStripeSize()
/* 70:   */   {
/* 71:83 */     return this.stripeSize;
/* 72:   */   }
/* 73:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFDxfStyleProvider
 * JD-Core Version:    0.7.0.1
 */