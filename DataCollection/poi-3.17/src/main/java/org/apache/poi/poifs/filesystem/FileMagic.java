/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.BufferedInputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import org.apache.poi.poifs.common.POIFSConstants;
/*   7:    */ import org.apache.poi.util.IOUtils;
/*   8:    */ import org.apache.poi.util.LittleEndian;
/*   9:    */ import org.apache.poi.util.LocaleUtil;
/*  10:    */ 
/*  11:    */ public enum FileMagic
/*  12:    */ {
/*  13: 38 */   OLE2(-2226271756974174256L),  OOXML(new byte[][] { POIFSConstants.OOXML_FILE_HEADER }),  XML(new byte[][] { POIFSConstants.RAW_XML_FILE_HEADER }),  BIFF2(new byte[][] { { 9, 0, 4, 0, 0, 0, 112, 0 } }),  BIFF3(new byte[][] { { 9, 2, 6, 0, 0, 0, 112, 0 } }),  BIFF4(new byte[][] { { 9, 4, 6, 0, 0, 0, 112, 0 }, { 9, 4, 6, 0, 0, 0, 0, 1 } }),  MSWRITE(new byte[][] { { 49, -66, 0, 0 }, { 50, -66, 0, 0 } }),  RTF("{\\rtf"),  PDF("%PDF"),  UNKNOWN(new byte[][] { new byte[0] });
/*  14:    */   
/*  15:    */   final byte[][] magic;
/*  16:    */   
/*  17:    */   private FileMagic(long magic)
/*  18:    */   {
/*  19: 84 */     this.magic = new byte[1][8];
/*  20: 85 */     LittleEndian.putLong(this.magic[0], 0, magic);
/*  21:    */   }
/*  22:    */   
/*  23:    */   private FileMagic(byte[]... magic)
/*  24:    */   {
/*  25: 89 */     this.magic = magic;
/*  26:    */   }
/*  27:    */   
/*  28:    */   private FileMagic(String magic)
/*  29:    */   {
/*  30: 93 */     this(new byte[][] { magic.getBytes(LocaleUtil.CHARSET_1252) });
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static FileMagic valueOf(byte[] magic)
/*  34:    */   {
/*  35: 97 */     for (FileMagic fm : )
/*  36:    */     {
/*  37: 98 */       int i = 0;
/*  38: 99 */       boolean found = true;
/*  39:100 */       for (byte[] ma : fm.magic)
/*  40:    */       {
/*  41:101 */         for (byte m : ma)
/*  42:    */         {
/*  43:102 */           byte d = magic[(i++)];
/*  44:103 */           if ((d != m) && ((m != 112) || ((d != 16) && (d != 32) && (d != 64))))
/*  45:    */           {
/*  46:104 */             found = false;
/*  47:105 */             break;
/*  48:    */           }
/*  49:    */         }
/*  50:108 */         if (found) {
/*  51:109 */           return fm;
/*  52:    */         }
/*  53:    */       }
/*  54:    */     }
/*  55:113 */     return UNKNOWN;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public static FileMagic valueOf(InputStream inp)
/*  59:    */     throws IOException
/*  60:    */   {
/*  61:130 */     if (!inp.markSupported()) {
/*  62:131 */       throw new IOException("getFileMagic() only operates on streams which support mark(int)");
/*  63:    */     }
/*  64:135 */     byte[] data = IOUtils.peekFirst8Bytes(inp);
/*  65:    */     
/*  66:137 */     return valueOf(data);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static InputStream prepareToCheckMagic(InputStream stream)
/*  70:    */   {
/*  71:148 */     if (stream.markSupported()) {
/*  72:149 */       return stream;
/*  73:    */     }
/*  74:153 */     return new BufferedInputStream(stream);
/*  75:    */   }
/*  76:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.FileMagic
 * JD-Core Version:    0.7.0.1
 */