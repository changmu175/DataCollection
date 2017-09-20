/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.util.Arrays;
/*   6:    */ import org.apache.poi.POIXMLDocumentPart;
/*   7:    */ import org.apache.poi.POIXMLException;
/*   8:    */ import org.apache.poi.POIXMLRelation;
/*   9:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  10:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  11:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  12:    */ import org.apache.poi.util.IOUtils;
/*  13:    */ 
/*  14:    */ public class XWPFPictureData
/*  15:    */   extends POIXMLDocumentPart
/*  16:    */ {
/*  17: 45 */   protected static final POIXMLRelation[] RELATIONS = new POIXMLRelation[13];
/*  18:    */   private Long checksum;
/*  19:    */   
/*  20:    */   static
/*  21:    */   {
/*  22: 46 */     RELATIONS[2] = XWPFRelation.IMAGE_EMF;
/*  23: 47 */     RELATIONS[3] = XWPFRelation.IMAGE_WMF;
/*  24: 48 */     RELATIONS[4] = XWPFRelation.IMAGE_PICT;
/*  25: 49 */     RELATIONS[5] = XWPFRelation.IMAGE_JPEG;
/*  26: 50 */     RELATIONS[6] = XWPFRelation.IMAGE_PNG;
/*  27: 51 */     RELATIONS[7] = XWPFRelation.IMAGE_DIB;
/*  28: 52 */     RELATIONS[8] = XWPFRelation.IMAGE_GIF;
/*  29: 53 */     RELATIONS[9] = XWPFRelation.IMAGE_TIFF;
/*  30: 54 */     RELATIONS[10] = XWPFRelation.IMAGE_EPS;
/*  31: 55 */     RELATIONS[11] = XWPFRelation.IMAGE_BMP;
/*  32: 56 */     RELATIONS[12] = XWPFRelation.IMAGE_WPG;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public XWPFPictureData(PackagePart part)
/*  36:    */   {
/*  37: 76 */     super(part);
/*  38:    */   }
/*  39:    */   
/*  40:    */   protected void onDocumentRead()
/*  41:    */     throws IOException
/*  42:    */   {
/*  43: 81 */     super.onDocumentRead();
/*  44:    */   }
/*  45:    */   
/*  46:    */   public byte[] getData()
/*  47:    */   {
/*  48:    */     try
/*  49:    */     {
/*  50: 99 */       return IOUtils.toByteArray(getPackagePart().getInputStream());
/*  51:    */     }
/*  52:    */     catch (IOException e)
/*  53:    */     {
/*  54:101 */       throw new POIXMLException(e);
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String getFileName()
/*  59:    */   {
/*  60:111 */     String name = getPackagePart().getPartName().getName();
/*  61:112 */     return name.substring(name.lastIndexOf('/') + 1);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String suggestFileExtension()
/*  65:    */   {
/*  66:121 */     return getPackagePart().getPartName().getExtension();
/*  67:    */   }
/*  68:    */   
/*  69:    */   public int getPictureType()
/*  70:    */   {
/*  71:136 */     String contentType = getPackagePart().getContentType();
/*  72:137 */     for (int i = 0; i < RELATIONS.length; i++) {
/*  73:138 */       if (RELATIONS[i] != null) {
/*  74:142 */         if (RELATIONS[i].getContentType().equals(contentType)) {
/*  75:143 */           return i;
/*  76:    */         }
/*  77:    */       }
/*  78:    */     }
/*  79:146 */     return 0;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public Long getChecksum()
/*  83:    */   {
/*  84:150 */     if (this.checksum == null)
/*  85:    */     {
/*  86:151 */       InputStream is = null;
/*  87:    */       byte[] data;
/*  88:    */       try
/*  89:    */       {
/*  90:154 */         is = getPackagePart().getInputStream();
/*  91:155 */         data = IOUtils.toByteArray(is);
/*  92:    */       }
/*  93:    */       catch (IOException e)
/*  94:    */       {
/*  95:157 */         throw new POIXMLException(e);
/*  96:    */       }
/*  97:    */       finally
/*  98:    */       {
/*  99:159 */         IOUtils.closeQuietly(is);
/* 100:    */       }
/* 101:161 */       this.checksum = Long.valueOf(IOUtils.calculateChecksum(data));
/* 102:    */     }
/* 103:163 */     return this.checksum;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public boolean equals(Object obj)
/* 107:    */   {
/* 108:180 */     if (obj == this) {
/* 109:181 */       return true;
/* 110:    */     }
/* 111:184 */     if (obj == null) {
/* 112:185 */       return false;
/* 113:    */     }
/* 114:188 */     if (!(obj instanceof XWPFPictureData)) {
/* 115:189 */       return false;
/* 116:    */     }
/* 117:192 */     XWPFPictureData picData = (XWPFPictureData)obj;
/* 118:193 */     PackagePart foreignPackagePart = picData.getPackagePart();
/* 119:194 */     PackagePart ownPackagePart = getPackagePart();
/* 120:196 */     if (((foreignPackagePart != null) && (ownPackagePart == null)) || ((foreignPackagePart == null) && (ownPackagePart != null))) {
/* 121:198 */       return false;
/* 122:    */     }
/* 123:201 */     if (ownPackagePart != null)
/* 124:    */     {
/* 125:202 */       OPCPackage foreignPackage = foreignPackagePart.getPackage();
/* 126:203 */       OPCPackage ownPackage = ownPackagePart.getPackage();
/* 127:205 */       if (((foreignPackage != null) && (ownPackage == null)) || ((foreignPackage == null) && (ownPackage != null))) {
/* 128:207 */         return false;
/* 129:    */       }
/* 130:209 */       if (ownPackage != null) {
/* 131:211 */         if (!ownPackage.equals(foreignPackage)) {
/* 132:212 */           return false;
/* 133:    */         }
/* 134:    */       }
/* 135:    */     }
/* 136:217 */     Long foreignChecksum = picData.getChecksum();
/* 137:218 */     Long localChecksum = getChecksum();
/* 138:220 */     if (!localChecksum.equals(foreignChecksum)) {
/* 139:221 */       return false;
/* 140:    */     }
/* 141:223 */     return Arrays.equals(getData(), picData.getData());
/* 142:    */   }
/* 143:    */   
/* 144:    */   public int hashCode()
/* 145:    */   {
/* 146:228 */     return getChecksum().hashCode();
/* 147:    */   }
/* 148:    */   
/* 149:    */   protected XWPFPictureData() {}
/* 150:    */   
/* 151:    */   protected void prepareForCommit() {}
/* 152:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFPictureData
 * JD-Core Version:    0.7.0.1
 */