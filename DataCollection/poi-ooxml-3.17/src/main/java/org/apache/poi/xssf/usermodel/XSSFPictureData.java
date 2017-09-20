/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import org.apache.poi.POIXMLDocumentPart;
/*   5:    */ import org.apache.poi.POIXMLException;
/*   6:    */ import org.apache.poi.POIXMLRelation;
/*   7:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   8:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*   9:    */ import org.apache.poi.ss.usermodel.PictureData;
/*  10:    */ import org.apache.poi.util.IOUtils;
/*  11:    */ 
/*  12:    */ public class XSSFPictureData
/*  13:    */   extends POIXMLDocumentPart
/*  14:    */   implements PictureData
/*  15:    */ {
/*  16: 41 */   protected static final POIXMLRelation[] RELATIONS = new POIXMLRelation[13];
/*  17:    */   
/*  18:    */   static
/*  19:    */   {
/*  20: 42 */     RELATIONS[2] = XSSFRelation.IMAGE_EMF;
/*  21: 43 */     RELATIONS[3] = XSSFRelation.IMAGE_WMF;
/*  22: 44 */     RELATIONS[4] = XSSFRelation.IMAGE_PICT;
/*  23: 45 */     RELATIONS[5] = XSSFRelation.IMAGE_JPEG;
/*  24: 46 */     RELATIONS[6] = XSSFRelation.IMAGE_PNG;
/*  25: 47 */     RELATIONS[7] = XSSFRelation.IMAGE_DIB;
/*  26: 48 */     RELATIONS[8] = XSSFRelation.IMAGE_GIF;
/*  27: 49 */     RELATIONS[9] = XSSFRelation.IMAGE_TIFF;
/*  28: 50 */     RELATIONS[10] = XSSFRelation.IMAGE_EPS;
/*  29: 51 */     RELATIONS[11] = XSSFRelation.IMAGE_BMP;
/*  30: 52 */     RELATIONS[12] = XSSFRelation.IMAGE_WPG;
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected XSSFPictureData(PackagePart part)
/*  34:    */   {
/*  35: 72 */     super(part);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public byte[] getData()
/*  39:    */   {
/*  40:    */     try
/*  41:    */     {
/*  42: 90 */       return IOUtils.toByteArray(getPackagePart().getInputStream());
/*  43:    */     }
/*  44:    */     catch (IOException e)
/*  45:    */     {
/*  46: 92 */       throw new POIXMLException(e);
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public String suggestFileExtension()
/*  51:    */   {
/*  52:102 */     return getPackagePart().getPartName().getExtension();
/*  53:    */   }
/*  54:    */   
/*  55:    */   public int getPictureType()
/*  56:    */   {
/*  57:117 */     String contentType = getPackagePart().getContentType();
/*  58:118 */     for (int i = 0; i < RELATIONS.length; i++) {
/*  59:119 */       if (RELATIONS[i] != null) {
/*  60:121 */         if (RELATIONS[i].getContentType().equals(contentType)) {
/*  61:122 */           return i;
/*  62:    */         }
/*  63:    */       }
/*  64:    */     }
/*  65:125 */     return 0;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public String getMimeType()
/*  69:    */   {
/*  70:129 */     return getPackagePart().getContentType();
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected XSSFPictureData() {}
/*  74:    */   
/*  75:    */   protected void prepareForCommit() {}
/*  76:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFPictureData
 * JD-Core Version:    0.7.0.1
 */