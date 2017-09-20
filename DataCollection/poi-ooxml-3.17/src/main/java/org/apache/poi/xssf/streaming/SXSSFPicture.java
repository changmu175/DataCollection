/*   1:    */ package org.apache.poi.xssf.streaming;
/*   2:    */ 
/*   3:    */ import java.awt.Dimension;
/*   4:    */ import java.io.IOException;
/*   5:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   6:    */ import org.apache.poi.ss.usermodel.Picture;
/*   7:    */ import org.apache.poi.ss.usermodel.Row;
/*   8:    */ import org.apache.poi.ss.usermodel.Shape;
/*   9:    */ import org.apache.poi.ss.util.ImageUtils;
/*  10:    */ import org.apache.poi.util.Internal;
/*  11:    */ import org.apache.poi.util.POILogFactory;
/*  12:    */ import org.apache.poi.util.POILogger;
/*  13:    */ import org.apache.poi.xssf.usermodel.XSSFAnchor;
/*  14:    */ import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
/*  15:    */ import org.apache.poi.xssf.usermodel.XSSFDrawing;
/*  16:    */ import org.apache.poi.xssf.usermodel.XSSFPicture;
/*  17:    */ import org.apache.poi.xssf.usermodel.XSSFPictureData;
/*  18:    */ import org.apache.poi.xssf.usermodel.XSSFSheet;
/*  19:    */ import org.apache.poi.xssf.usermodel.helpers.ColumnHelper;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture;
/*  24:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;
/*  25:    */ 
/*  26:    */ public final class SXSSFPicture
/*  27:    */   implements Picture
/*  28:    */ {
/*  29: 52 */   private static final POILogger logger = POILogFactory.getLogger(SXSSFPicture.class);
/*  30: 60 */   private static float DEFAULT_COLUMN_WIDTH = 9.140625F;
/*  31:    */   private final SXSSFWorkbook _wb;
/*  32:    */   private final XSSFPicture _picture;
/*  33:    */   
/*  34:    */   SXSSFPicture(SXSSFWorkbook _wb, XSSFPicture _picture)
/*  35:    */   {
/*  36: 66 */     this._wb = _wb;
/*  37: 67 */     this._picture = _picture;
/*  38:    */   }
/*  39:    */   
/*  40:    */   @Internal
/*  41:    */   public CTPicture getCTPicture()
/*  42:    */   {
/*  43: 77 */     return this._picture.getCTPicture();
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void resize()
/*  47:    */   {
/*  48: 91 */     resize(1.0D);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void resize(double scale)
/*  52:    */   {
/*  53:108 */     XSSFClientAnchor anchor = getClientAnchor();
/*  54:    */     
/*  55:110 */     XSSFClientAnchor pref = getPreferredSize(scale);
/*  56:    */     
/*  57:112 */     int row2 = anchor.getRow1() + (pref.getRow2() - pref.getRow1());
/*  58:113 */     int col2 = anchor.getCol1() + (pref.getCol2() - pref.getCol1());
/*  59:    */     
/*  60:115 */     anchor.setCol2(col2);
/*  61:116 */     anchor.setDx1(0);
/*  62:117 */     anchor.setDx2(pref.getDx2());
/*  63:    */     
/*  64:119 */     anchor.setRow2(row2);
/*  65:120 */     anchor.setDy1(0);
/*  66:121 */     anchor.setDy2(pref.getDy2());
/*  67:    */   }
/*  68:    */   
/*  69:    */   public XSSFClientAnchor getPreferredSize()
/*  70:    */   {
/*  71:131 */     return getPreferredSize(1.0D);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public XSSFClientAnchor getPreferredSize(double scale)
/*  75:    */   {
/*  76:141 */     XSSFClientAnchor anchor = getClientAnchor();
/*  77:    */     
/*  78:143 */     XSSFPictureData data = getPictureData();
/*  79:144 */     Dimension size = getImageDimension(data.getPackagePart(), data.getPictureType());
/*  80:145 */     double scaledWidth = size.getWidth() * scale;
/*  81:146 */     double scaledHeight = size.getHeight() * scale;
/*  82:    */     
/*  83:148 */     float w = 0.0F;
/*  84:149 */     int col2 = anchor.getCol1() - 1;
/*  85:151 */     while (w <= scaledWidth) {
/*  86:152 */       w += getColumnWidthInPixels(++col2);
/*  87:    */     }
/*  88:155 */     assert (w > scaledWidth);
/*  89:156 */     double cw = getColumnWidthInPixels(col2);
/*  90:157 */     double deltaW = w - scaledWidth;
/*  91:158 */     int dx2 = (int)(9525.0D * (cw - deltaW));
/*  92:    */     
/*  93:160 */     anchor.setCol2(col2);
/*  94:161 */     anchor.setDx2(dx2);
/*  95:    */     
/*  96:163 */     double h = 0.0D;
/*  97:164 */     int row2 = anchor.getRow1() - 1;
/*  98:166 */     while (h <= scaledHeight) {
/*  99:167 */       h += getRowHeightInPixels(++row2);
/* 100:    */     }
/* 101:170 */     assert (h > scaledHeight);
/* 102:171 */     double ch = getRowHeightInPixels(row2);
/* 103:172 */     double deltaH = h - scaledHeight;
/* 104:173 */     int dy2 = (int)(9525.0D * (ch - deltaH));
/* 105:174 */     anchor.setRow2(row2);
/* 106:175 */     anchor.setDy2(dy2);
/* 107:    */     
/* 108:177 */     CTPositiveSize2D size2d = getCTPicture().getSpPr().getXfrm().getExt();
/* 109:178 */     size2d.setCx((scaledWidth * 9525.0D));
/* 110:179 */     size2d.setCy((scaledHeight * 9525.0D));
/* 111:    */     
/* 112:181 */     return anchor;
/* 113:    */   }
/* 114:    */   
/* 115:    */   private float getColumnWidthInPixels(int columnIndex)
/* 116:    */   {
/* 117:185 */     XSSFSheet sheet = getSheet();
/* 118:    */     
/* 119:187 */     CTCol col = sheet.getColumnHelper().getColumn(columnIndex, false);
/* 120:188 */     double numChars = (col == null) || (!col.isSetWidth()) ? DEFAULT_COLUMN_WIDTH : col.getWidth();
/* 121:    */     
/* 122:190 */     return (float)numChars * 7.0017F;
/* 123:    */   }
/* 124:    */   
/* 125:    */   private float getRowHeightInPixels(int rowIndex)
/* 126:    */   {
/* 127:196 */     XSSFSheet xssfSheet = getSheet();
/* 128:197 */     SXSSFSheet sheet = this._wb.getSXSSFSheet(xssfSheet);
/* 129:198 */     Row row = sheet.getRow(rowIndex);
/* 130:199 */     float height = row != null ? row.getHeightInPoints() : sheet.getDefaultRowHeightInPoints();
/* 131:200 */     return height * 96.0F / 72.0F;
/* 132:    */   }
/* 133:    */   
/* 134:    */   protected static Dimension getImageDimension(PackagePart part, int type)
/* 135:    */   {
/* 136:    */     try
/* 137:    */     {
/* 138:213 */       return ImageUtils.getImageDimension(part.getInputStream(), type);
/* 139:    */     }
/* 140:    */     catch (IOException e)
/* 141:    */     {
/* 142:216 */       logger.log(5, new Object[] { e });
/* 143:    */     }
/* 144:217 */     return new Dimension();
/* 145:    */   }
/* 146:    */   
/* 147:    */   public XSSFPictureData getPictureData()
/* 148:    */   {
/* 149:228 */     return this._picture.getPictureData();
/* 150:    */   }
/* 151:    */   
/* 152:    */   protected CTShapeProperties getShapeProperties()
/* 153:    */   {
/* 154:232 */     return getCTPicture().getSpPr();
/* 155:    */   }
/* 156:    */   
/* 157:    */   public XSSFAnchor getAnchor()
/* 158:    */   {
/* 159:237 */     return this._picture.getAnchor();
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void resize(double scaleX, double scaleY)
/* 163:    */   {
/* 164:242 */     this._picture.resize(scaleX, scaleY);
/* 165:    */   }
/* 166:    */   
/* 167:    */   public XSSFClientAnchor getPreferredSize(double scaleX, double scaleY)
/* 168:    */   {
/* 169:247 */     return this._picture.getPreferredSize(scaleX, scaleY);
/* 170:    */   }
/* 171:    */   
/* 172:    */   public Dimension getImageDimension()
/* 173:    */   {
/* 174:252 */     return this._picture.getImageDimension();
/* 175:    */   }
/* 176:    */   
/* 177:    */   public XSSFClientAnchor getClientAnchor()
/* 178:    */   {
/* 179:257 */     XSSFAnchor a = getAnchor();
/* 180:258 */     return (a instanceof XSSFClientAnchor) ? (XSSFClientAnchor)a : null;
/* 181:    */   }
/* 182:    */   
/* 183:    */   public XSSFDrawing getDrawing()
/* 184:    */   {
/* 185:262 */     return this._picture.getDrawing();
/* 186:    */   }
/* 187:    */   
/* 188:    */   public XSSFSheet getSheet()
/* 189:    */   {
/* 190:267 */     return this._picture.getSheet();
/* 191:    */   }
/* 192:    */   
/* 193:    */   public String getShapeName()
/* 194:    */   {
/* 195:272 */     return this._picture.getShapeName();
/* 196:    */   }
/* 197:    */   
/* 198:    */   public Shape getParent()
/* 199:    */   {
/* 200:277 */     return this._picture.getParent();
/* 201:    */   }
/* 202:    */   
/* 203:    */   public boolean isNoFill()
/* 204:    */   {
/* 205:282 */     return this._picture.isNoFill();
/* 206:    */   }
/* 207:    */   
/* 208:    */   public void setNoFill(boolean noFill)
/* 209:    */   {
/* 210:287 */     this._picture.setNoFill(noFill);
/* 211:    */   }
/* 212:    */   
/* 213:    */   public void setFillColor(int red, int green, int blue)
/* 214:    */   {
/* 215:292 */     this._picture.setFillColor(red, green, blue);
/* 216:    */   }
/* 217:    */   
/* 218:    */   public void setLineStyleColor(int red, int green, int blue)
/* 219:    */   {
/* 220:297 */     this._picture.setLineStyleColor(red, green, blue);
/* 221:    */   }
/* 222:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.streaming.SXSSFPicture
 * JD-Core Version:    0.7.0.1
 */