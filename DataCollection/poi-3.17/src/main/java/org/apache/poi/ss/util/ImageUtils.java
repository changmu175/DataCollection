/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import java.awt.Dimension;
/*   4:    */ import java.awt.image.BufferedImage;
/*   5:    */ import java.io.ByteArrayInputStream;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStream;
/*   8:    */ import java.util.Iterator;
/*   9:    */ import javax.imageio.ImageIO;
/*  10:    */ import javax.imageio.ImageReader;
/*  11:    */ import javax.imageio.metadata.IIOMetadata;
/*  12:    */ import javax.imageio.stream.ImageInputStream;
/*  13:    */ import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
/*  14:    */ import org.apache.poi.ss.usermodel.ClientAnchor;
/*  15:    */ import org.apache.poi.ss.usermodel.Picture;
/*  16:    */ import org.apache.poi.ss.usermodel.PictureData;
/*  17:    */ import org.apache.poi.ss.usermodel.Row;
/*  18:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  19:    */ import org.apache.poi.util.POILogFactory;
/*  20:    */ import org.apache.poi.util.POILogger;
/*  21:    */ import org.apache.poi.util.Units;
/*  22:    */ import org.w3c.dom.Element;
/*  23:    */ import org.w3c.dom.NodeList;
/*  24:    */ 
/*  25:    */ public class ImageUtils
/*  26:    */ {
/*  27: 49 */   private static final POILogger logger = POILogFactory.getLogger(ImageUtils.class);
/*  28:    */   public static final int PIXEL_DPI = 96;
/*  29:    */   
/*  30:    */   public static Dimension getImageDimension(InputStream is, int type)
/*  31:    */   {
/*  32: 63 */     Dimension size = new Dimension();
/*  33: 65 */     switch (type)
/*  34:    */     {
/*  35:    */     case 5: 
/*  36:    */     case 6: 
/*  37:    */     case 7: 
/*  38:    */       try
/*  39:    */       {
/*  40: 73 */         ImageInputStream iis = ImageIO.createImageInputStream(is);
/*  41:    */         try
/*  42:    */         {
/*  43: 75 */           Iterator<ImageReader> i = ImageIO.getImageReaders(iis);
/*  44: 76 */           ImageReader r = (ImageReader)i.next();
/*  45:    */           try
/*  46:    */           {
/*  47: 78 */             r.setInput(iis);
/*  48: 79 */             BufferedImage img = r.read(0);
/*  49:    */             
/*  50: 81 */             int[] dpi = getResolution(r);
/*  51: 85 */             if (dpi[0] == 0) {
/*  52: 85 */               dpi[0] = 96;
/*  53:    */             }
/*  54: 86 */             if (dpi[1] == 0) {
/*  55: 86 */               dpi[1] = 96;
/*  56:    */             }
/*  57: 88 */             size.width = (img.getWidth() * 96 / dpi[0]);
/*  58: 89 */             size.height = (img.getHeight() * 96 / dpi[1]);
/*  59:    */           }
/*  60:    */           finally
/*  61:    */           {
/*  62: 91 */             r.dispose();
/*  63:    */           }
/*  64:    */         }
/*  65:    */         finally
/*  66:    */         {
/*  67: 94 */           iis.close();
/*  68:    */         }
/*  69:    */       }
/*  70:    */       catch (IOException e)
/*  71:    */       {
/*  72: 99 */         logger.log(5, new Object[] { e });
/*  73:    */       }
/*  74:    */     default: 
/*  75:104 */       logger.log(5, new Object[] { "Only JPEG, PNG and DIB pictures can be automatically sized" });
/*  76:    */     }
/*  77:106 */     return size;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static int[] getResolution(ImageReader r)
/*  81:    */     throws IOException
/*  82:    */   {
/*  83:118 */     int hdpi = 96;int vdpi = 96;
/*  84:119 */     double mm2inch = 25.399999999999999D;
/*  85:    */     
/*  86:    */ 
/*  87:122 */     Element node = (Element)r.getImageMetadata(0).getAsTree("javax_imageio_1.0");
/*  88:123 */     NodeList lst = node.getElementsByTagName("HorizontalPixelSize");
/*  89:124 */     if ((lst != null) && (lst.getLength() == 1)) {
/*  90:124 */       hdpi = (int)(mm2inch / Float.parseFloat(((Element)lst.item(0)).getAttribute("value")));
/*  91:    */     }
/*  92:126 */     lst = node.getElementsByTagName("VerticalPixelSize");
/*  93:127 */     if ((lst != null) && (lst.getLength() == 1)) {
/*  94:127 */       vdpi = (int)(mm2inch / Float.parseFloat(((Element)lst.item(0)).getAttribute("value")));
/*  95:    */     }
/*  96:129 */     return new int[] { hdpi, vdpi };
/*  97:    */   }
/*  98:    */   
/*  99:    */   public static Dimension setPreferredSize(Picture picture, double scaleX, double scaleY)
/* 100:    */   {
/* 101:140 */     ClientAnchor anchor = picture.getClientAnchor();
/* 102:141 */     boolean isHSSF = anchor instanceof HSSFClientAnchor;
/* 103:142 */     PictureData data = picture.getPictureData();
/* 104:143 */     Sheet sheet = picture.getSheet();
/* 105:    */     
/* 106:    */ 
/* 107:146 */     Dimension imgSize = getImageDimension(new ByteArrayInputStream(data.getData()), data.getPictureType());
/* 108:    */     
/* 109:148 */     Dimension anchorSize = getDimensionFromAnchor(picture);
/* 110:149 */     double scaledWidth = scaleX == 1.7976931348623157E+308D ? imgSize.getWidth() : anchorSize.getWidth() / 9525.0D * scaleX;
/* 111:    */     
/* 112:151 */     double scaledHeight = scaleY == 1.7976931348623157E+308D ? imgSize.getHeight() : anchorSize.getHeight() / 9525.0D * scaleY;
/* 113:    */     
/* 114:    */ 
/* 115:154 */     double w = 0.0D;
/* 116:155 */     int col2 = anchor.getCol1();
/* 117:156 */     int dx2 = 0;
/* 118:    */     
/* 119:    */ 
/* 120:159 */     w = sheet.getColumnWidthInPixels(col2++);
/* 121:160 */     if (isHSSF) {
/* 122:161 */       w *= (1.0D - anchor.getDx1() / 1024.0D);
/* 123:    */     } else {
/* 124:163 */       w -= anchor.getDx1() / 9525.0D;
/* 125:    */     }
/* 126:166 */     while (w < scaledWidth) {
/* 127:167 */       w += sheet.getColumnWidthInPixels(col2++);
/* 128:    */     }
/* 129:170 */     if (w > scaledWidth)
/* 130:    */     {
/* 131:172 */       double cw = sheet.getColumnWidthInPixels(--col2);
/* 132:173 */       double delta = w - scaledWidth;
/* 133:174 */       if (isHSSF) {
/* 134:175 */         dx2 = (int)((cw - delta) / cw * 1024.0D);
/* 135:    */       } else {
/* 136:177 */         dx2 = (int)((cw - delta) * 9525.0D);
/* 137:    */       }
/* 138:179 */       if (dx2 < 0) {
/* 139:179 */         dx2 = 0;
/* 140:    */       }
/* 141:    */     }
/* 142:181 */     anchor.setCol2(col2);
/* 143:182 */     anchor.setDx2(dx2);
/* 144:    */     
/* 145:184 */     double h = 0.0D;
/* 146:185 */     int row2 = anchor.getRow1();
/* 147:186 */     int dy2 = 0;
/* 148:    */     
/* 149:188 */     h = getRowHeightInPixels(sheet, row2++);
/* 150:189 */     if (isHSSF) {
/* 151:190 */       h *= (1.0D - anchor.getDy1() / 256.0D);
/* 152:    */     } else {
/* 153:192 */       h -= anchor.getDy1() / 9525.0D;
/* 154:    */     }
/* 155:195 */     while (h < scaledHeight) {
/* 156:196 */       h += getRowHeightInPixels(sheet, row2++);
/* 157:    */     }
/* 158:199 */     if (h > scaledHeight)
/* 159:    */     {
/* 160:200 */       double ch = getRowHeightInPixels(sheet, --row2);
/* 161:201 */       double delta = h - scaledHeight;
/* 162:202 */       if (isHSSF) {
/* 163:203 */         dy2 = (int)((ch - delta) / ch * 256.0D);
/* 164:    */       } else {
/* 165:205 */         dy2 = (int)((ch - delta) * 9525.0D);
/* 166:    */       }
/* 167:207 */       if (dy2 < 0) {
/* 168:207 */         dy2 = 0;
/* 169:    */       }
/* 170:    */     }
/* 171:210 */     anchor.setRow2(row2);
/* 172:211 */     anchor.setDy2(dy2);
/* 173:    */     
/* 174:213 */     Dimension dim = new Dimension((int)Math.round(scaledWidth * 9525.0D), (int)Math.round(scaledHeight * 9525.0D));
/* 175:    */     
/* 176:    */ 
/* 177:    */ 
/* 178:    */ 
/* 179:218 */     return dim;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public static Dimension getDimensionFromAnchor(Picture picture)
/* 183:    */   {
/* 184:228 */     ClientAnchor anchor = picture.getClientAnchor();
/* 185:229 */     boolean isHSSF = anchor instanceof HSSFClientAnchor;
/* 186:230 */     Sheet sheet = picture.getSheet();
/* 187:    */     
/* 188:232 */     double w = 0.0D;
/* 189:233 */     int col2 = anchor.getCol1();
/* 190:    */     
/* 191:    */ 
/* 192:236 */     w = sheet.getColumnWidthInPixels(col2++);
/* 193:237 */     if (isHSSF) {
/* 194:238 */       w *= (1.0D - anchor.getDx1() / 1024.0D);
/* 195:    */     } else {
/* 196:240 */       w -= anchor.getDx1() / 9525.0D;
/* 197:    */     }
/* 198:243 */     while (col2 < anchor.getCol2()) {
/* 199:244 */       w += sheet.getColumnWidthInPixels(col2++);
/* 200:    */     }
/* 201:247 */     if (isHSSF) {
/* 202:248 */       w += sheet.getColumnWidthInPixels(col2) * anchor.getDx2() / 1024.0D;
/* 203:    */     } else {
/* 204:250 */       w += anchor.getDx2() / 9525.0D;
/* 205:    */     }
/* 206:253 */     double h = 0.0D;
/* 207:254 */     int row2 = anchor.getRow1();
/* 208:    */     
/* 209:256 */     h = getRowHeightInPixels(sheet, row2++);
/* 210:257 */     if (isHSSF) {
/* 211:258 */       h *= (1.0D - anchor.getDy1() / 256.0D);
/* 212:    */     } else {
/* 213:260 */       h -= anchor.getDy1() / 9525.0D;
/* 214:    */     }
/* 215:263 */     while (row2 < anchor.getRow2()) {
/* 216:264 */       h += getRowHeightInPixels(sheet, row2++);
/* 217:    */     }
/* 218:267 */     if (isHSSF) {
/* 219:268 */       h += getRowHeightInPixels(sheet, row2) * anchor.getDy2() / 256.0D;
/* 220:    */     } else {
/* 221:270 */       h += anchor.getDy2() / 9525.0D;
/* 222:    */     }
/* 223:273 */     w *= 9525.0D;
/* 224:274 */     h *= 9525.0D;
/* 225:    */     
/* 226:276 */     return new Dimension((int)Math.rint(w), (int)Math.rint(h));
/* 227:    */   }
/* 228:    */   
/* 229:    */   public static double getRowHeightInPixels(Sheet sheet, int rowNum)
/* 230:    */   {
/* 231:281 */     Row r = sheet.getRow(rowNum);
/* 232:282 */     double points = r == null ? sheet.getDefaultRowHeightInPoints() : r.getHeightInPoints();
/* 233:283 */     return Units.toEMU(points) / 9525.0D;
/* 234:    */   }
/* 235:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.ImageUtils
 * JD-Core Version:    0.7.0.1
 */