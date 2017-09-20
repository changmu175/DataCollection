/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ddf.EscherClientAnchorRecord;
/*   4:    */ import org.apache.poi.ddf.EscherRecord;
/*   5:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   6:    */ import org.apache.poi.ss.usermodel.ClientAnchor;
/*   7:    */ import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
/*   8:    */ 
/*   9:    */ public final class HSSFClientAnchor
/*  10:    */   extends HSSFAnchor
/*  11:    */   implements ClientAnchor
/*  12:    */ {
/*  13: 31 */   public static final int MAX_COL = SpreadsheetVersion.EXCEL97.getLastColumnIndex();
/*  14: 32 */   public static final int MAX_ROW = SpreadsheetVersion.EXCEL97.getLastRowIndex();
/*  15:    */   private EscherClientAnchorRecord _escherClientAnchor;
/*  16:    */   
/*  17:    */   public HSSFClientAnchor(EscherClientAnchorRecord escherClientAnchorRecord)
/*  18:    */   {
/*  19: 37 */     this._escherClientAnchor = escherClientAnchorRecord;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public HSSFClientAnchor() {}
/*  23:    */   
/*  24:    */   public HSSFClientAnchor(int dx1, int dy1, int dx2, int dy2, short col1, int row1, short col2, int row2)
/*  25:    */   {
/*  26: 64 */     super(dx1, dy1, dx2, dy2);
/*  27:    */     
/*  28: 66 */     checkRange(dx1, 0, 1023, "dx1");
/*  29: 67 */     checkRange(dx2, 0, 1023, "dx2");
/*  30: 68 */     checkRange(dy1, 0, 255, "dy1");
/*  31: 69 */     checkRange(dy2, 0, 255, "dy2");
/*  32: 70 */     checkRange(col1, 0, MAX_COL, "col1");
/*  33: 71 */     checkRange(col2, 0, MAX_COL, "col2");
/*  34: 72 */     checkRange(row1, 0, MAX_ROW, "row1");
/*  35: 73 */     checkRange(row2, 0, MAX_ROW, "row2");
/*  36:    */     
/*  37: 75 */     setCol1((short)Math.min(col1, col2));
/*  38: 76 */     setCol2((short)Math.max(col1, col2));
/*  39: 77 */     setRow1(Math.min(row1, row2));
/*  40: 78 */     setRow2(Math.max(row1, row2));
/*  41: 80 */     if (col1 > col2) {
/*  42: 81 */       this._isHorizontallyFlipped = true;
/*  43:    */     }
/*  44: 83 */     if (row1 > row2) {
/*  45: 84 */       this._isVerticallyFlipped = true;
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public float getAnchorHeightInPoints(HSSFSheet sheet)
/*  50:    */   {
/*  51: 95 */     int y1 = getDy1();
/*  52: 96 */     int y2 = getDy2();
/*  53: 97 */     int row1 = Math.min(getRow1(), getRow2());
/*  54: 98 */     int row2 = Math.max(getRow1(), getRow2());
/*  55:    */     
/*  56:100 */     float points = 0.0F;
/*  57:101 */     if (row1 == row2)
/*  58:    */     {
/*  59:102 */       points = (y2 - y1) / 256.0F * getRowHeightInPoints(sheet, row2);
/*  60:    */     }
/*  61:    */     else
/*  62:    */     {
/*  63:104 */       points += (256.0F - y1) / 256.0F * getRowHeightInPoints(sheet, row1);
/*  64:105 */       for (int i = row1 + 1; i < row2; i++) {
/*  65:106 */         points += getRowHeightInPoints(sheet, i);
/*  66:    */       }
/*  67:108 */       points += y2 / 256.0F * getRowHeightInPoints(sheet, row2);
/*  68:    */     }
/*  69:111 */     return points;
/*  70:    */   }
/*  71:    */   
/*  72:    */   private float getRowHeightInPoints(HSSFSheet sheet, int rowNum)
/*  73:    */   {
/*  74:115 */     HSSFRow row = sheet.getRow(rowNum);
/*  75:116 */     if (row == null) {
/*  76:117 */       return sheet.getDefaultRowHeightInPoints();
/*  77:    */     }
/*  78:119 */     return row.getHeightInPoints();
/*  79:    */   }
/*  80:    */   
/*  81:    */   public short getCol1()
/*  82:    */   {
/*  83:126 */     return this._escherClientAnchor.getCol1();
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setCol1(short col1)
/*  87:    */   {
/*  88:133 */     checkRange(col1, 0, MAX_COL, "col1");
/*  89:134 */     this._escherClientAnchor.setCol1(col1);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setCol1(int col1)
/*  93:    */   {
/*  94:141 */     setCol1((short)col1);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public short getCol2()
/*  98:    */   {
/*  99:148 */     return this._escherClientAnchor.getCol2();
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setCol2(short col2)
/* 103:    */   {
/* 104:155 */     checkRange(col2, 0, MAX_COL, "col2");
/* 105:156 */     this._escherClientAnchor.setCol2(col2);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void setCol2(int col2)
/* 109:    */   {
/* 110:163 */     setCol2((short)col2);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public int getRow1()
/* 114:    */   {
/* 115:170 */     return unsignedValue(this._escherClientAnchor.getRow1());
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setRow1(int row1)
/* 119:    */   {
/* 120:177 */     checkRange(row1, 0, MAX_ROW, "row1");
/* 121:178 */     this._escherClientAnchor.setRow1(Integer.valueOf(row1).shortValue());
/* 122:    */   }
/* 123:    */   
/* 124:    */   public int getRow2()
/* 125:    */   {
/* 126:185 */     return unsignedValue(this._escherClientAnchor.getRow2());
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void setRow2(int row2)
/* 130:    */   {
/* 131:192 */     checkRange(row2, 0, MAX_ROW, "row2");
/* 132:193 */     this._escherClientAnchor.setRow2(Integer.valueOf(row2).shortValue());
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void setAnchor(short col1, int row1, int x1, int y1, short col2, int row2, int x2, int y2)
/* 136:    */   {
/* 137:214 */     checkRange(getDx1(), 0, 1023, "dx1");
/* 138:215 */     checkRange(getDx2(), 0, 1023, "dx2");
/* 139:216 */     checkRange(getDy1(), 0, 255, "dy1");
/* 140:217 */     checkRange(getDy2(), 0, 255, "dy2");
/* 141:218 */     checkRange(getCol1(), 0, MAX_COL, "col1");
/* 142:219 */     checkRange(getCol2(), 0, MAX_COL, "col2");
/* 143:220 */     checkRange(getRow1(), 0, MAX_ROW, "row1");
/* 144:221 */     checkRange(getRow2(), 0, MAX_ROW, "row2");
/* 145:    */     
/* 146:223 */     setCol1(col1);
/* 147:224 */     setRow1(row1);
/* 148:225 */     setDx1(x1);
/* 149:226 */     setDy1(y1);
/* 150:227 */     setCol2(col2);
/* 151:228 */     setRow2(row2);
/* 152:229 */     setDx2(x2);
/* 153:230 */     setDy2(y2);
/* 154:    */   }
/* 155:    */   
/* 156:    */   public boolean isHorizontallyFlipped()
/* 157:    */   {
/* 158:234 */     return this._isHorizontallyFlipped;
/* 159:    */   }
/* 160:    */   
/* 161:    */   public boolean isVerticallyFlipped()
/* 162:    */   {
/* 163:238 */     return this._isVerticallyFlipped;
/* 164:    */   }
/* 165:    */   
/* 166:    */   protected EscherRecord getEscherAnchor()
/* 167:    */   {
/* 168:243 */     return this._escherClientAnchor;
/* 169:    */   }
/* 170:    */   
/* 171:    */   protected void createEscherAnchor()
/* 172:    */   {
/* 173:248 */     this._escherClientAnchor = new EscherClientAnchorRecord();
/* 174:    */   }
/* 175:    */   
/* 176:    */   public AnchorType getAnchorType()
/* 177:    */   {
/* 178:258 */     return AnchorType.byId(this._escherClientAnchor.getFlag());
/* 179:    */   }
/* 180:    */   
/* 181:    */   public void setAnchorType(AnchorType anchorType)
/* 182:    */   {
/* 183:268 */     this._escherClientAnchor.setFlag(anchorType.value);
/* 184:    */   }
/* 185:    */   
/* 186:    */   private void checkRange(int value, int minRange, int maxRange, String varName)
/* 187:    */   {
/* 188:272 */     if ((value < minRange) || (value > maxRange)) {
/* 189:273 */       throw new IllegalArgumentException(varName + " must be between " + minRange + " and " + maxRange + ", but was: " + value);
/* 190:    */     }
/* 191:    */   }
/* 192:    */   
/* 193:    */   private static int unsignedValue(short s)
/* 194:    */   {
/* 195:283 */     return s < 0 ? 65536 + s : s;
/* 196:    */   }
/* 197:    */   
/* 198:    */   public boolean equals(Object obj)
/* 199:    */   {
/* 200:288 */     if (obj == null) {
/* 201:289 */       return false;
/* 202:    */     }
/* 203:290 */     if (obj == this) {
/* 204:291 */       return true;
/* 205:    */     }
/* 206:292 */     if (obj.getClass() != getClass()) {
/* 207:293 */       return false;
/* 208:    */     }
/* 209:294 */     HSSFClientAnchor anchor = (HSSFClientAnchor)obj;
/* 210:    */     
/* 211:296 */     return (anchor.getCol1() == getCol1()) && (anchor.getCol2() == getCol2()) && (anchor.getDx1() == getDx1()) && (anchor.getDx2() == getDx2()) && (anchor.getDy1() == getDy1()) && (anchor.getDy2() == getDy2()) && (anchor.getRow1() == getRow1()) && (anchor.getRow2() == getRow2()) && (anchor.getAnchorType() == getAnchorType());
/* 212:    */   }
/* 213:    */   
/* 214:    */   public int hashCode()
/* 215:    */   {
/* 216:303 */     if (!$assertionsDisabled) {
/* 217:303 */       throw new AssertionError("hashCode not designed");
/* 218:    */     }
/* 219:304 */     return 42;
/* 220:    */   }
/* 221:    */   
/* 222:    */   public int getDx1()
/* 223:    */   {
/* 224:309 */     return this._escherClientAnchor.getDx1();
/* 225:    */   }
/* 226:    */   
/* 227:    */   public void setDx1(int dx1)
/* 228:    */   {
/* 229:314 */     this._escherClientAnchor.setDx1(Integer.valueOf(dx1).shortValue());
/* 230:    */   }
/* 231:    */   
/* 232:    */   public int getDy1()
/* 233:    */   {
/* 234:319 */     return this._escherClientAnchor.getDy1();
/* 235:    */   }
/* 236:    */   
/* 237:    */   public void setDy1(int dy1)
/* 238:    */   {
/* 239:324 */     this._escherClientAnchor.setDy1(Integer.valueOf(dy1).shortValue());
/* 240:    */   }
/* 241:    */   
/* 242:    */   public int getDy2()
/* 243:    */   {
/* 244:329 */     return this._escherClientAnchor.getDy2();
/* 245:    */   }
/* 246:    */   
/* 247:    */   public void setDy2(int dy2)
/* 248:    */   {
/* 249:334 */     this._escherClientAnchor.setDy2(Integer.valueOf(dy2).shortValue());
/* 250:    */   }
/* 251:    */   
/* 252:    */   public int getDx2()
/* 253:    */   {
/* 254:339 */     return this._escherClientAnchor.getDx2();
/* 255:    */   }
/* 256:    */   
/* 257:    */   public void setDx2(int dx2)
/* 258:    */   {
/* 259:344 */     this._escherClientAnchor.setDx2(Integer.valueOf(dx2).shortValue());
/* 260:    */   }
/* 261:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFClientAnchor

 * JD-Core Version:    0.7.0.1

 */