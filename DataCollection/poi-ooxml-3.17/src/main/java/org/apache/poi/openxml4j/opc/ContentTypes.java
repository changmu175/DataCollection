/*   1:    */ package org.apache.poi.openxml4j.opc;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ 
/*   5:    */ public final class ContentTypes
/*   6:    */ {
/*   7:    */   public static final String CORE_PROPERTIES_PART = "application/vnd.openxmlformats-package.core-properties+xml";
/*   8:    */   public static final String DIGITAL_SIGNATURE_CERTIFICATE_PART = "application/vnd.openxmlformats-package.digital-signature-certificate";
/*   9:    */   public static final String DIGITAL_SIGNATURE_ORIGIN_PART = "application/vnd.openxmlformats-package.digital-signature-origin";
/*  10:    */   public static final String DIGITAL_SIGNATURE_XML_SIGNATURE_PART = "application/vnd.openxmlformats-package.digital-signature-xmlsignature+xml";
/*  11:    */   public static final String RELATIONSHIPS_PART = "application/vnd.openxmlformats-package.relationships+xml";
/*  12:    */   public static final String CUSTOM_XML_PART = "application/vnd.openxmlformats-officedocument.customXmlProperties+xml";
/*  13:    */   public static final String PLAIN_OLD_XML = "application/xml";
/*  14:    */   public static final String IMAGE_JPEG = "image/jpeg";
/*  15:    */   public static final String EXTENSION_JPG_1 = "jpg";
/*  16:    */   public static final String EXTENSION_JPG_2 = "jpeg";
/*  17:    */   public static final String IMAGE_PNG = "image/png";
/*  18:    */   public static final String EXTENSION_PNG = "png";
/*  19:    */   public static final String IMAGE_GIF = "image/gif";
/*  20:    */   public static final String EXTENSION_GIF = "gif";
/*  21:    */   public static final String IMAGE_TIFF = "image/tiff";
/*  22:    */   public static final String EXTENSION_TIFF = "tiff";
/*  23:    */   public static final String IMAGE_PICT = "image/pict";
/*  24:    */   public static final String EXTENSION_PICT = "tiff";
/*  25:    */   public static final String XML = "text/xml";
/*  26:    */   public static final String EXTENSION_XML = "xml";
/*  27:    */   
/*  28:    */   public static String getContentTypeFromFileExtension(String filename)
/*  29:    */   {
/*  30:114 */     String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase(Locale.ROOT);
/*  31:116 */     if ((extension.equals("jpg")) || (extension.equals("jpeg"))) {
/*  32:118 */       return "image/jpeg";
/*  33:    */     }
/*  34:119 */     if (extension.equals("gif")) {
/*  35:120 */       return "image/gif";
/*  36:    */     }
/*  37:121 */     if (extension.equals("tiff")) {
/*  38:122 */       return "image/pict";
/*  39:    */     }
/*  40:123 */     if (extension.equals("png")) {
/*  41:124 */       return "image/png";
/*  42:    */     }
/*  43:125 */     if (extension.equals("tiff")) {
/*  44:126 */       return "image/tiff";
/*  45:    */     }
/*  46:127 */     if (extension.equals("xml")) {
/*  47:128 */       return "text/xml";
/*  48:    */     }
/*  49:130 */     return null;
/*  50:    */   }
/*  51:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.ContentTypes
 * JD-Core Version:    0.7.0.1
 */