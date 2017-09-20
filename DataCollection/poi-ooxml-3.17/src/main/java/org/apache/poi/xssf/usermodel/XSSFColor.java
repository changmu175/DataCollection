/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.poi.ss.usermodel.ExtendedColor;
/*   5:    */ import org.apache.poi.ss.usermodel.IndexedColors;
/*   6:    */ import org.apache.poi.util.Internal;
/*   7:    */ import org.apache.poi.util.Removal;
/*   8:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
/*   9:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor.Factory;
/*  10:    */ 
/*  11:    */ public class XSSFColor
/*  12:    */   extends ExtendedColor
/*  13:    */ {
/*  14:    */   private final CTColor ctColor;
/*  15:    */   private final IndexedColorMap indexedColorMap;
/*  16:    */   
/*  17:    */   @Deprecated
/*  18:    */   @Removal(version="3.19")
/*  19:    */   public XSSFColor(CTColor color)
/*  20:    */   {
/*  21: 43 */     this(color, new DefaultIndexedColorMap());
/*  22:    */   }
/*  23:    */   
/*  24:    */   public XSSFColor(CTColor color, IndexedColorMap map)
/*  25:    */   {
/*  26: 52 */     this.ctColor = color;
/*  27: 53 */     this.indexedColorMap = map;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public XSSFColor()
/*  31:    */   {
/*  32: 61 */     this(CTColor.Factory.newInstance(), null);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public XSSFColor(java.awt.Color clr)
/*  36:    */   {
/*  37: 69 */     this();
/*  38: 70 */     setColor(clr);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public XSSFColor(byte[] rgb, IndexedColorMap colorMap)
/*  42:    */   {
/*  43: 79 */     this(CTColor.Factory.newInstance(), colorMap);
/*  44: 80 */     this.ctColor.setRgb(rgb);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public XSSFColor(IndexedColors indexedColor, IndexedColorMap colorMap)
/*  48:    */   {
/*  49: 88 */     this(CTColor.Factory.newInstance(), colorMap);
/*  50: 89 */     this.ctColor.setIndexed(indexedColor.index);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public boolean isAuto()
/*  54:    */   {
/*  55: 97 */     return this.ctColor.getAuto();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setAuto(boolean auto)
/*  59:    */   {
/*  60:103 */     this.ctColor.setAuto(auto);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public boolean isIndexed()
/*  64:    */   {
/*  65:111 */     return this.ctColor.isSetIndexed();
/*  66:    */   }
/*  67:    */   
/*  68:    */   public boolean isRGB()
/*  69:    */   {
/*  70:119 */     return this.ctColor.isSetRgb();
/*  71:    */   }
/*  72:    */   
/*  73:    */   public boolean isThemed()
/*  74:    */   {
/*  75:127 */     return this.ctColor.isSetTheme();
/*  76:    */   }
/*  77:    */   
/*  78:    */   public boolean hasAlpha()
/*  79:    */   {
/*  80:134 */     if (!this.ctColor.isSetRgb()) {
/*  81:135 */       return false;
/*  82:    */     }
/*  83:137 */     return this.ctColor.getRgb().length == 4;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public boolean hasTint()
/*  87:    */   {
/*  88:144 */     if (!this.ctColor.isSetTint()) {
/*  89:145 */       return false;
/*  90:    */     }
/*  91:147 */     return this.ctColor.getTint() != 0.0D;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public short getIndex()
/*  95:    */   {
/*  96:155 */     return (short)(int)this.ctColor.getIndexed();
/*  97:    */   }
/*  98:    */   
/*  99:    */   public short getIndexed()
/* 100:    */   {
/* 101:161 */     return getIndex();
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setIndexed(int indexed)
/* 105:    */   {
/* 106:169 */     this.ctColor.setIndexed(indexed);
/* 107:    */   }
/* 108:    */   
/* 109:    */   public byte[] getRGB()
/* 110:    */   {
/* 111:178 */     byte[] rgb = getRGBOrARGB();
/* 112:179 */     if (rgb == null) {
/* 113:180 */       return null;
/* 114:    */     }
/* 115:183 */     if (rgb.length == 4)
/* 116:    */     {
/* 117:185 */       byte[] tmp = new byte[3];
/* 118:186 */       System.arraycopy(rgb, 1, tmp, 0, 3);
/* 119:187 */       return tmp;
/* 120:    */     }
/* 121:189 */     return rgb;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public byte[] getARGB()
/* 125:    */   {
/* 126:198 */     byte[] rgb = getRGBOrARGB();
/* 127:199 */     if (rgb == null) {
/* 128:200 */       return null;
/* 129:    */     }
/* 130:203 */     if (rgb.length == 3)
/* 131:    */     {
/* 132:205 */       byte[] tmp = new byte[4];
/* 133:206 */       tmp[0] = -1;
/* 134:207 */       System.arraycopy(rgb, 0, tmp, 1, 3);
/* 135:208 */       return tmp;
/* 136:    */     }
/* 137:210 */     return rgb;
/* 138:    */   }
/* 139:    */   
/* 140:    */   protected byte[] getStoredRBG()
/* 141:    */   {
/* 142:216 */     return this.ctColor.getRgb();
/* 143:    */   }
/* 144:    */   
/* 145:    */   protected byte[] getIndexedRGB()
/* 146:    */   {
/* 147:220 */     if (isIndexed())
/* 148:    */     {
/* 149:221 */       if (this.indexedColorMap != null) {
/* 150:221 */         return this.indexedColorMap.getRGB(getIndex());
/* 151:    */       }
/* 152:222 */       return DefaultIndexedColorMap.getDefaultRGB(getIndex());
/* 153:    */     }
/* 154:224 */     return null;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public void setRGB(byte[] rgb)
/* 158:    */   {
/* 159:232 */     this.ctColor.setRgb(rgb);
/* 160:    */   }
/* 161:    */   
/* 162:    */   public int getTheme()
/* 163:    */   {
/* 164:241 */     return (int)this.ctColor.getTheme();
/* 165:    */   }
/* 166:    */   
/* 167:    */   public void setTheme(int theme)
/* 168:    */   {
/* 169:250 */     this.ctColor.setTheme(theme);
/* 170:    */   }
/* 171:    */   
/* 172:    */   public double getTint()
/* 173:    */   {
/* 174:296 */     return this.ctColor.getTint();
/* 175:    */   }
/* 176:    */   
/* 177:    */   public void setTint(double tint)
/* 178:    */   {
/* 179:342 */     this.ctColor.setTint(tint);
/* 180:    */   }
/* 181:    */   
/* 182:    */   @Internal
/* 183:    */   public CTColor getCTColor()
/* 184:    */   {
/* 185:352 */     return this.ctColor;
/* 186:    */   }
/* 187:    */   
/* 188:    */   public static XSSFColor toXSSFColor(org.apache.poi.ss.usermodel.Color color)
/* 189:    */   {
/* 190:366 */     if ((color != null) && (!(color instanceof XSSFColor))) {
/* 191:367 */       throw new IllegalArgumentException("Only XSSFColor objects are supported");
/* 192:    */     }
/* 193:369 */     return (XSSFColor)color;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public int hashCode()
/* 197:    */   {
/* 198:374 */     return this.ctColor.toString().hashCode();
/* 199:    */   }
/* 200:    */   
/* 201:    */   private boolean sameIndexed(XSSFColor other)
/* 202:    */   {
/* 203:379 */     if (isIndexed() == other.isIndexed())
/* 204:    */     {
/* 205:380 */       if (isIndexed()) {
/* 206:381 */         return getIndexed() == other.getIndexed();
/* 207:    */       }
/* 208:383 */       return true;
/* 209:    */     }
/* 210:385 */     return false;
/* 211:    */   }
/* 212:    */   
/* 213:    */   private boolean sameARGB(XSSFColor other)
/* 214:    */   {
/* 215:388 */     if (isRGB() == other.isRGB())
/* 216:    */     {
/* 217:389 */       if (isRGB()) {
/* 218:390 */         return Arrays.equals(getARGB(), other.getARGB());
/* 219:    */       }
/* 220:392 */       return true;
/* 221:    */     }
/* 222:394 */     return false;
/* 223:    */   }
/* 224:    */   
/* 225:    */   private boolean sameTheme(XSSFColor other)
/* 226:    */   {
/* 227:397 */     if (isThemed() == other.isThemed())
/* 228:    */     {
/* 229:398 */       if (isThemed()) {
/* 230:399 */         return getTheme() == other.getTheme();
/* 231:    */       }
/* 232:401 */       return true;
/* 233:    */     }
/* 234:403 */     return false;
/* 235:    */   }
/* 236:    */   
/* 237:    */   private boolean sameTint(XSSFColor other)
/* 238:    */   {
/* 239:406 */     if (hasTint() == other.hasTint())
/* 240:    */     {
/* 241:407 */       if (hasTint()) {
/* 242:408 */         return getTint() == other.getTint();
/* 243:    */       }
/* 244:410 */       return true;
/* 245:    */     }
/* 246:412 */     return false;
/* 247:    */   }
/* 248:    */   
/* 249:    */   private boolean sameAuto(XSSFColor other)
/* 250:    */   {
/* 251:415 */     return isAuto() == other.isAuto();
/* 252:    */   }
/* 253:    */   
/* 254:    */   public boolean equals(Object o)
/* 255:    */   {
/* 256:420 */     if (!(o instanceof XSSFColor)) {
/* 257:421 */       return false;
/* 258:    */     }
/* 259:424 */     XSSFColor other = (XSSFColor)o;
/* 260:    */     
/* 261:    */ 
/* 262:    */ 
/* 263:    */ 
/* 264:429 */     return (sameARGB(other)) && (sameTheme(other)) && (sameIndexed(other)) && (sameTint(other)) && (sameAuto(other));
/* 265:    */   }
/* 266:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFColor
 * JD-Core Version:    0.7.0.1
 */