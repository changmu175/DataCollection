/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Dimension;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ import org.apache.poi.POIXMLDocumentPart;
/*   8:    */ import org.apache.poi.POIXMLException;
/*   9:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  10:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  11:    */ import org.apache.poi.sl.image.ImageHeaderBitmap;
/*  12:    */ import org.apache.poi.sl.image.ImageHeaderEMF;
/*  13:    */ import org.apache.poi.sl.image.ImageHeaderPICT;
/*  14:    */ import org.apache.poi.sl.image.ImageHeaderWMF;
/*  15:    */ import org.apache.poi.sl.usermodel.PictureData;
/*  16:    */ import org.apache.poi.sl.usermodel.PictureData.PictureType;
/*  17:    */ import org.apache.poi.util.IOUtils;
/*  18:    */ import org.apache.poi.util.LittleEndian;
/*  19:    */ import org.apache.poi.util.Units;
/*  20:    */ 
/*  21:    */ public final class XSLFPictureData
/*  22:    */   extends POIXMLDocumentPart
/*  23:    */   implements PictureData
/*  24:    */ {
/*  25: 46 */   private Long checksum = null;
/*  26: 49 */   private Dimension origSize = null;
/*  27: 50 */   private int index = -1;
/*  28:    */   
/*  29:    */   protected XSLFPictureData() {}
/*  30:    */   
/*  31:    */   public XSLFPictureData(PackagePart part)
/*  32:    */   {
/*  33: 67 */     super(part);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public InputStream getInputStream()
/*  37:    */     throws IOException
/*  38:    */   {
/*  39: 77 */     return getPackagePart().getInputStream();
/*  40:    */   }
/*  41:    */   
/*  42:    */   public byte[] getData()
/*  43:    */   {
/*  44:    */     try
/*  45:    */     {
/*  46: 89 */       return IOUtils.toByteArray(getInputStream());
/*  47:    */     }
/*  48:    */     catch (IOException e)
/*  49:    */     {
/*  50: 91 */       throw new POIXMLException(e);
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   public String getFileName()
/*  55:    */   {
/*  56:101 */     String name = getPackagePart().getPartName().getName();
/*  57:102 */     return name.substring(name.lastIndexOf('/') + 1);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public String suggestFileExtension()
/*  61:    */   {
/*  62:111 */     return getPackagePart().getPartName().getExtension();
/*  63:    */   }
/*  64:    */   
/*  65:    */   public byte[] getChecksum()
/*  66:    */   {
/*  67:116 */     cacheProperties();
/*  68:117 */     byte[] cs = new byte[8];
/*  69:118 */     LittleEndian.putLong(cs, 0, this.checksum.longValue());
/*  70:119 */     return cs;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public Dimension getImageDimension()
/*  74:    */   {
/*  75:124 */     cacheProperties();
/*  76:125 */     return this.origSize;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public Dimension getImageDimensionInPixels()
/*  80:    */   {
/*  81:130 */     Dimension dim = getImageDimension();
/*  82:131 */     return new Dimension(Units.pointsToPixel(dim.getWidth()), Units.pointsToPixel(dim.getHeight()));
/*  83:    */   }
/*  84:    */   
/*  85:    */   protected void cacheProperties()
/*  86:    */   {
/*  87:141 */     if ((this.origSize == null) || (this.checksum == null))
/*  88:    */     {
/*  89:142 */       byte[] data = getData();
/*  90:143 */       this.checksum = Long.valueOf(IOUtils.calculateChecksum(data));
/*  91:    */       
/*  92:145 */       PictureData.PictureType pt = getType();
/*  93:146 */       if (pt == null)
/*  94:    */       {
/*  95:147 */         this.origSize = new Dimension(1, 1);
/*  96:148 */         return;
/*  97:    */       }
/*  98:151 */       switch (1.$SwitchMap$org$apache$poi$sl$usermodel$PictureData$PictureType[pt.ordinal()])
/*  99:    */       {
/* 100:    */       case 1: 
/* 101:153 */         this.origSize = new ImageHeaderEMF(data, 0).getSize();
/* 102:154 */         break;
/* 103:    */       case 2: 
/* 104:158 */         this.origSize = new ImageHeaderWMF(data, 0).getSize();
/* 105:159 */         break;
/* 106:    */       case 3: 
/* 107:161 */         this.origSize = new ImageHeaderPICT(data, 0).getSize();
/* 108:162 */         break;
/* 109:    */       default: 
/* 110:164 */         this.origSize = new ImageHeaderBitmap(data, 0).getSize();
/* 111:    */       }
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   protected void prepareForCommit() {}
/* 116:    */   
/* 117:    */   public String getContentType()
/* 118:    */   {
/* 119:181 */     return getPackagePart().getContentType();
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setData(byte[] data)
/* 123:    */     throws IOException
/* 124:    */   {
/* 125:185 */     OutputStream os = getPackagePart().getOutputStream();
/* 126:186 */     os.write(data);
/* 127:187 */     os.close();
/* 128:    */     
/* 129:189 */     this.checksum = Long.valueOf(IOUtils.calculateChecksum(data));
/* 130:    */     
/* 131:191 */     this.origSize = null;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public PictureData.PictureType getType()
/* 135:    */   {
/* 136:196 */     String ct = getContentType();
/* 137:197 */     if (XSLFRelation.IMAGE_EMF.getContentType().equals(ct)) {
/* 138:198 */       return PictureData.PictureType.EMF;
/* 139:    */     }
/* 140:199 */     if (XSLFRelation.IMAGE_WMF.getContentType().equals(ct)) {
/* 141:200 */       return PictureData.PictureType.WMF;
/* 142:    */     }
/* 143:201 */     if (XSLFRelation.IMAGE_PICT.getContentType().equals(ct)) {
/* 144:202 */       return PictureData.PictureType.PICT;
/* 145:    */     }
/* 146:203 */     if (XSLFRelation.IMAGE_JPEG.getContentType().equals(ct)) {
/* 147:204 */       return PictureData.PictureType.JPEG;
/* 148:    */     }
/* 149:205 */     if (XSLFRelation.IMAGE_PNG.getContentType().equals(ct)) {
/* 150:206 */       return PictureData.PictureType.PNG;
/* 151:    */     }
/* 152:207 */     if (XSLFRelation.IMAGE_DIB.getContentType().equals(ct)) {
/* 153:208 */       return PictureData.PictureType.DIB;
/* 154:    */     }
/* 155:209 */     if (XSLFRelation.IMAGE_GIF.getContentType().equals(ct)) {
/* 156:210 */       return PictureData.PictureType.GIF;
/* 157:    */     }
/* 158:211 */     if (XSLFRelation.IMAGE_EPS.getContentType().equals(ct)) {
/* 159:212 */       return PictureData.PictureType.EPS;
/* 160:    */     }
/* 161:213 */     if (XSLFRelation.IMAGE_BMP.getContentType().equals(ct)) {
/* 162:214 */       return PictureData.PictureType.BMP;
/* 163:    */     }
/* 164:215 */     if (XSLFRelation.IMAGE_WPG.getContentType().equals(ct)) {
/* 165:216 */       return PictureData.PictureType.WPG;
/* 166:    */     }
/* 167:217 */     if (XSLFRelation.IMAGE_WDP.getContentType().equals(ct)) {
/* 168:218 */       return PictureData.PictureType.WDP;
/* 169:    */     }
/* 170:219 */     if (XSLFRelation.IMAGE_TIFF.getContentType().equals(ct)) {
/* 171:220 */       return PictureData.PictureType.TIFF;
/* 172:    */     }
/* 173:222 */     return null;
/* 174:    */   }
/* 175:    */   
/* 176:    */   static XSLFRelation getRelationForType(PictureData.PictureType pt)
/* 177:    */   {
/* 178:227 */     switch (1.$SwitchMap$org$apache$poi$sl$usermodel$PictureData$PictureType[pt.ordinal()])
/* 179:    */     {
/* 180:    */     case 1: 
/* 181:228 */       return XSLFRelation.IMAGE_EMF;
/* 182:    */     case 2: 
/* 183:229 */       return XSLFRelation.IMAGE_WMF;
/* 184:    */     case 3: 
/* 185:230 */       return XSLFRelation.IMAGE_PICT;
/* 186:    */     case 4: 
/* 187:231 */       return XSLFRelation.IMAGE_JPEG;
/* 188:    */     case 5: 
/* 189:232 */       return XSLFRelation.IMAGE_PNG;
/* 190:    */     case 6: 
/* 191:233 */       return XSLFRelation.IMAGE_DIB;
/* 192:    */     case 7: 
/* 193:234 */       return XSLFRelation.IMAGE_GIF;
/* 194:    */     case 8: 
/* 195:235 */       return XSLFRelation.IMAGE_EPS;
/* 196:    */     case 9: 
/* 197:236 */       return XSLFRelation.IMAGE_BMP;
/* 198:    */     case 10: 
/* 199:237 */       return XSLFRelation.IMAGE_WPG;
/* 200:    */     case 11: 
/* 201:238 */       return XSLFRelation.IMAGE_WDP;
/* 202:    */     case 12: 
/* 203:239 */       return XSLFRelation.IMAGE_TIFF;
/* 204:    */     }
/* 205:240 */     return null;
/* 206:    */   }
/* 207:    */   
/* 208:    */   public int getIndex()
/* 209:    */   {
/* 210:248 */     return this.index;
/* 211:    */   }
/* 212:    */   
/* 213:    */   public void setIndex(int index)
/* 214:    */   {
/* 215:255 */     this.index = index;
/* 216:    */   }
/* 217:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFPictureData
 * JD-Core Version:    0.7.0.1
 */