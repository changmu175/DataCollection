/*   1:    */ package org.apache.poi;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*   5:    */ import org.apache.poi.openxml4j.util.ZipSecureFile;
/*   6:    */ 
/*   7:    */ public abstract class POIXMLTextExtractor
/*   8:    */   extends POITextExtractor
/*   9:    */ {
/*  10:    */   private final POIXMLDocument _document;
/*  11:    */   
/*  12:    */   public POIXMLTextExtractor(POIXMLDocument document)
/*  13:    */   {
/*  14: 38 */     this._document = document;
/*  15:    */   }
/*  16:    */   
/*  17:    */   public POIXMLProperties.CoreProperties getCoreProperties()
/*  18:    */   {
/*  19: 47 */     return this._document.getProperties().getCoreProperties();
/*  20:    */   }
/*  21:    */   
/*  22:    */   public POIXMLProperties.ExtendedProperties getExtendedProperties()
/*  23:    */   {
/*  24: 55 */     return this._document.getProperties().getExtendedProperties();
/*  25:    */   }
/*  26:    */   
/*  27:    */   public POIXMLProperties.CustomProperties getCustomProperties()
/*  28:    */   {
/*  29: 63 */     return this._document.getProperties().getCustomProperties();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public final POIXMLDocument getDocument()
/*  33:    */   {
/*  34: 72 */     return this._document;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public OPCPackage getPackage()
/*  38:    */   {
/*  39: 81 */     return this._document.getPackage();
/*  40:    */   }
/*  41:    */   
/*  42:    */   public POIXMLPropertiesTextExtractor getMetadataTextExtractor()
/*  43:    */   {
/*  44: 90 */     return new POIXMLPropertiesTextExtractor(this._document);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void close()
/*  48:    */     throws IOException
/*  49:    */   {
/*  50: 96 */     if (this._document != null)
/*  51:    */     {
/*  52: 98 */       OPCPackage pkg = this._document.getPackage();
/*  53: 99 */       if (pkg != null) {
/*  54:101 */         pkg.revert();
/*  55:    */       }
/*  56:    */     }
/*  57:104 */     super.close();
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected void checkMaxTextSize(StringBuffer text, String string)
/*  61:    */   {
/*  62:108 */     if (string == null) {
/*  63:109 */       return;
/*  64:    */     }
/*  65:112 */     int size = text.length() + string.length();
/*  66:113 */     if (size > ZipSecureFile.getMaxTextSize()) {
/*  67:114 */       throw new IllegalStateException("The text would exceed the max allowed overall size of extracted text. By default this is prevented as some documents may exhaust available memory and it may indicate that the file is used to inflate memory usage and thus could pose a security risk. You can adjust this limit via ZipSecureFile.setMaxTextSize() if you need to work with files which have a lot of text. Size: " + size + ", limit: MAX_TEXT_SIZE: " + ZipSecureFile.getMaxTextSize());
/*  68:    */     }
/*  69:    */   }
/*  70:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.POIXMLTextExtractor
 * JD-Core Version:    0.7.0.1
 */