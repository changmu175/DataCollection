/*   1:    */ package org.apache.poi.common.usermodel.fonts;
/*   2:    */ 
/*   3:    */ import java.nio.charset.Charset;
/*   4:    */ import java.nio.charset.UnsupportedCharsetException;
/*   5:    */ import org.apache.poi.util.POILogFactory;
/*   6:    */ import org.apache.poi.util.POILogger;
/*   7:    */ 
/*   8:    */ public enum FontCharset
/*   9:    */ {
/*  10: 34 */   ANSI(0, "Cp1252"),  DEFAULT(1, "Cp1252"),  SYMBOL(2, ""),  MAC(77, "MacRoman"),  SHIFTJIS(128, "Shift_JIS"),  HANGUL(129, "cp949"),  JOHAB(130, "x-Johab"),  GB2312(134, "GB2312"),  CHINESEBIG5(136, "Big5"),  GREEK(161, "Cp1253"),  TURKISH(162, "Cp1254"),  VIETNAMESE(163, "Cp1258"),  HEBREW(177, "Cp1255"),  ARABIC(178, "Cp1256"),  BALTIC(186, "Cp1257"),  RUSSIAN(204, "Cp1251"),  THAI_(222, "x-windows-874"),  EASTEUROPE(238, "Cp1250"),  OEM(255, "Cp1252");
/*  11:    */   
/*  12:    */   private static FontCharset[] _table;
/*  13:    */   private int nativeId;
/*  14:    */   private Charset charset;
/*  15:    */   
/*  16:    */   static
/*  17:    */   {
/*  18: 82 */     _table = new FontCharset[256];
/*  19: 89 */     for (FontCharset c : values()) {
/*  20: 90 */       _table[c.getNativeId()] = c;
/*  21:    */     }
/*  22:    */   }
/*  23:    */   
/*  24:    */   private FontCharset(int flag, String javaCharsetName)
/*  25:    */   {
/*  26: 95 */     this.nativeId = flag;
/*  27: 96 */     if (javaCharsetName.length() > 0) {
/*  28:    */       try
/*  29:    */       {
/*  30: 98 */         this.charset = Charset.forName(javaCharsetName);
/*  31: 99 */         return;
/*  32:    */       }
/*  33:    */       catch (UnsupportedCharsetException e)
/*  34:    */       {
/*  35:101 */         POILogger logger = POILogFactory.getLogger(FontCharset.class);
/*  36:102 */         logger.log(5, new Object[] { "Unsupported charset: " + javaCharsetName });
/*  37:    */       }
/*  38:    */     }
/*  39:105 */     this.charset = null;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public Charset getCharset()
/*  43:    */   {
/*  44:114 */     return this.charset;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public int getNativeId()
/*  48:    */   {
/*  49:118 */     return this.nativeId;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static FontCharset valueOf(int value)
/*  53:    */   {
/*  54:122 */     return (value < 0) || (value >= _table.length) ? null : _table[value];
/*  55:    */   }
/*  56:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.common.usermodel.fonts.FontCharset
 * JD-Core Version:    0.7.0.1
 */