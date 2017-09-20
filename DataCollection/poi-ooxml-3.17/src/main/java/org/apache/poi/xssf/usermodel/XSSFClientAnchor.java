/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.ClientAnchor;
/*   4:    */ import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
/*   5:    */ import org.apache.poi.util.Internal;
/*   6:    */ import org.apache.poi.util.Units;
/*   7:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
/*   8:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
/*   9:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
/*  10:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker.Factory;
/*  11:    */ 
/*  12:    */ public class XSSFClientAnchor
/*  13:    */   extends XSSFAnchor
/*  14:    */   implements ClientAnchor
/*  15:    */ {
/*  16: 43 */   private static final CTMarker EMPTY_MARKER = CTMarker.Factory.newInstance();
/*  17:    */   private ClientAnchor.AnchorType anchorType;
/*  18:    */   private CTMarker cell1;
/*  19:    */   private CTMarker cell2;
/*  20:    */   private CTPositiveSize2D size;
/*  21:    */   private CTPoint2D position;
/*  22:    */   private XSSFSheet sheet;
/*  23:    */   
/*  24:    */   public XSSFClientAnchor()
/*  25:    */   {
/*  26: 80 */     this(0, 0, 0, 0, 0, 0, 0, 0);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public XSSFClientAnchor(int dx1, int dy1, int dx2, int dy2, int col1, int row1, int col2, int row2)
/*  30:    */   {
/*  31: 98 */     this.anchorType = ClientAnchor.AnchorType.MOVE_AND_RESIZE;
/*  32: 99 */     this.cell1 = CTMarker.Factory.newInstance();
/*  33:100 */     this.cell1.setCol(col1);
/*  34:101 */     this.cell1.setColOff(dx1);
/*  35:102 */     this.cell1.setRow(row1);
/*  36:103 */     this.cell1.setRowOff(dy1);
/*  37:104 */     this.cell2 = CTMarker.Factory.newInstance();
/*  38:105 */     this.cell2.setCol(col2);
/*  39:106 */     this.cell2.setColOff(dx2);
/*  40:107 */     this.cell2.setRow(row2);
/*  41:108 */     this.cell2.setRowOff(dy2);
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected XSSFClientAnchor(CTMarker cell1, CTMarker cell2)
/*  45:    */   {
/*  46:118 */     this.anchorType = ClientAnchor.AnchorType.MOVE_AND_RESIZE;
/*  47:119 */     this.cell1 = cell1;
/*  48:120 */     this.cell2 = cell2;
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected XSSFClientAnchor(XSSFSheet sheet, CTMarker cell1, CTPositiveSize2D size)
/*  52:    */   {
/*  53:132 */     this.anchorType = ClientAnchor.AnchorType.MOVE_DONT_RESIZE;
/*  54:133 */     this.sheet = sheet;
/*  55:134 */     this.size = size;
/*  56:135 */     this.cell1 = cell1;
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected XSSFClientAnchor(XSSFSheet sheet, CTPoint2D position, CTPositiveSize2D size)
/*  60:    */   {
/*  61:148 */     this.anchorType = ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE;
/*  62:149 */     this.sheet = sheet;
/*  63:150 */     this.position = position;
/*  64:151 */     this.size = size;
/*  65:    */   }
/*  66:    */   
/*  67:    */   private CTMarker calcCell(CTMarker cell, long w, long h)
/*  68:    */   {
/*  69:164 */     CTMarker c2 = CTMarker.Factory.newInstance();
/*  70:    */     
/*  71:166 */     int r = cell.getRow();
/*  72:167 */     int c = cell.getCol();
/*  73:    */     
/*  74:169 */     int cw = Units.columnWidthToEMU(this.sheet.getColumnWidth(c));
/*  75:    */     
/*  76:    */ 
/*  77:172 */     long wPos = cw - cell.getColOff();
/*  78:174 */     while (wPos < w)
/*  79:    */     {
/*  80:175 */       c++;
/*  81:176 */       cw = Units.columnWidthToEMU(this.sheet.getColumnWidth(c));
/*  82:177 */       wPos += cw;
/*  83:    */     }
/*  84:180 */     c2.setCol(c);
/*  85:181 */     c2.setColOff(cw - (wPos - w));
/*  86:    */     
/*  87:183 */     int rh = Units.toEMU(getRowHeight(this.sheet, r));
/*  88:    */     
/*  89:185 */     long hPos = rh - cell.getRowOff();
/*  90:187 */     while (hPos < h)
/*  91:    */     {
/*  92:188 */       r++;
/*  93:189 */       rh = Units.toEMU(getRowHeight(this.sheet, r));
/*  94:190 */       hPos += rh;
/*  95:    */     }
/*  96:193 */     c2.setRow(r);
/*  97:194 */     c2.setRowOff(rh - (hPos - h));
/*  98:    */     
/*  99:196 */     return c2;
/* 100:    */   }
/* 101:    */   
/* 102:    */   private static float getRowHeight(XSSFSheet sheet, int row)
/* 103:    */   {
/* 104:205 */     XSSFRow r = sheet.getRow(row);
/* 105:206 */     return r == null ? sheet.getDefaultRowHeightInPoints() : r.getHeightInPoints();
/* 106:    */   }
/* 107:    */   
/* 108:    */   private CTMarker getCell1()
/* 109:    */   {
/* 110:210 */     return this.cell1 != null ? this.cell1 : calcCell(EMPTY_MARKER, this.position.getX(), this.position.getY());
/* 111:    */   }
/* 112:    */   
/* 113:    */   private CTMarker getCell2()
/* 114:    */   {
/* 115:214 */     return this.cell2 != null ? this.cell2 : calcCell(getCell1(), this.size.getCx(), this.size.getCy());
/* 116:    */   }
/* 117:    */   
/* 118:    */   public short getCol1()
/* 119:    */   {
/* 120:218 */     return (short)getCell1().getCol();
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void setCol1(int col1)
/* 124:    */   {
/* 125:226 */     this.cell1.setCol(col1);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public short getCol2()
/* 129:    */   {
/* 130:230 */     return (short)getCell2().getCol();
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void setCol2(int col2)
/* 134:    */   {
/* 135:238 */     this.cell2.setCol(col2);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public int getRow1()
/* 139:    */   {
/* 140:242 */     return getCell1().getRow();
/* 141:    */   }
/* 142:    */   
/* 143:    */   public void setRow1(int row1)
/* 144:    */   {
/* 145:250 */     this.cell1.setRow(row1);
/* 146:    */   }
/* 147:    */   
/* 148:    */   public int getRow2()
/* 149:    */   {
/* 150:254 */     return getCell2().getRow();
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void setRow2(int row2)
/* 154:    */   {
/* 155:262 */     this.cell2.setRow(row2);
/* 156:    */   }
/* 157:    */   
/* 158:    */   public int getDx1()
/* 159:    */   {
/* 160:266 */     return (int)getCell1().getColOff();
/* 161:    */   }
/* 162:    */   
/* 163:    */   public void setDx1(int dx1)
/* 164:    */   {
/* 165:274 */     this.cell1.setColOff(dx1);
/* 166:    */   }
/* 167:    */   
/* 168:    */   public int getDy1()
/* 169:    */   {
/* 170:278 */     return (int)getCell1().getRowOff();
/* 171:    */   }
/* 172:    */   
/* 173:    */   public void setDy1(int dy1)
/* 174:    */   {
/* 175:286 */     this.cell1.setRowOff(dy1);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public int getDy2()
/* 179:    */   {
/* 180:290 */     return (int)getCell2().getRowOff();
/* 181:    */   }
/* 182:    */   
/* 183:    */   public void setDy2(int dy2)
/* 184:    */   {
/* 185:298 */     this.cell2.setRowOff(dy2);
/* 186:    */   }
/* 187:    */   
/* 188:    */   public int getDx2()
/* 189:    */   {
/* 190:302 */     return (int)getCell2().getColOff();
/* 191:    */   }
/* 192:    */   
/* 193:    */   public void setDx2(int dx2)
/* 194:    */   {
/* 195:310 */     this.cell2.setColOff(dx2);
/* 196:    */   }
/* 197:    */   
/* 198:    */   public boolean equals(Object o)
/* 199:    */   {
/* 200:315 */     if ((o == null) || (!(o instanceof XSSFClientAnchor))) {
/* 201:315 */       return false;
/* 202:    */     }
/* 203:317 */     XSSFClientAnchor anchor = (XSSFClientAnchor)o;
/* 204:318 */     return (getDx1() == anchor.getDx1()) && (getDx2() == anchor.getDx2()) && (getDy1() == anchor.getDy1()) && (getDy2() == anchor.getDy2()) && (getCol1() == anchor.getCol1()) && (getCol2() == anchor.getCol2()) && (getRow1() == anchor.getRow1()) && (getRow2() == anchor.getRow2());
/* 205:    */   }
/* 206:    */   
/* 207:    */   public int hashCode()
/* 208:    */   {
/* 209:331 */     if (!$assertionsDisabled) {
/* 210:331 */       throw new AssertionError("hashCode not designed");
/* 211:    */     }
/* 212:332 */     return 42;
/* 213:    */   }
/* 214:    */   
/* 215:    */   public String toString()
/* 216:    */   {
/* 217:337 */     return "from : " + getCell1() + "; to: " + getCell2();
/* 218:    */   }
/* 219:    */   
/* 220:    */   @Internal
/* 221:    */   public CTMarker getFrom()
/* 222:    */   {
/* 223:347 */     return getCell1();
/* 224:    */   }
/* 225:    */   
/* 226:    */   protected void setFrom(CTMarker from)
/* 227:    */   {
/* 228:351 */     this.cell1 = from;
/* 229:    */   }
/* 230:    */   
/* 231:    */   @Internal
/* 232:    */   public CTMarker getTo()
/* 233:    */   {
/* 234:361 */     return getCell2();
/* 235:    */   }
/* 236:    */   
/* 237:    */   protected void setTo(CTMarker to)
/* 238:    */   {
/* 239:365 */     this.cell2 = to;
/* 240:    */   }
/* 241:    */   
/* 242:    */   public CTPoint2D getPosition()
/* 243:    */   {
/* 244:373 */     return this.position;
/* 245:    */   }
/* 246:    */   
/* 247:    */   public void setPosition(CTPoint2D position)
/* 248:    */   {
/* 249:382 */     this.position = position;
/* 250:    */   }
/* 251:    */   
/* 252:    */   public CTPositiveSize2D getSize()
/* 253:    */   {
/* 254:391 */     return this.size;
/* 255:    */   }
/* 256:    */   
/* 257:    */   public void setSize(CTPositiveSize2D size)
/* 258:    */   {
/* 259:400 */     this.size = size;
/* 260:    */   }
/* 261:    */   
/* 262:    */   public void setAnchorType(ClientAnchor.AnchorType anchorType)
/* 263:    */   {
/* 264:411 */     this.anchorType = anchorType;
/* 265:    */   }
/* 266:    */   
/* 267:    */   public ClientAnchor.AnchorType getAnchorType()
/* 268:    */   {
/* 269:422 */     return this.anchorType;
/* 270:    */   }
/* 271:    */   
/* 272:    */   public boolean isSet()
/* 273:    */   {
/* 274:426 */     CTMarker c1 = getCell1();
/* 275:427 */     CTMarker c2 = getCell2();
/* 276:428 */     return (c1.getCol() != 0) || (c2.getCol() != 0) || (c1.getRow() != 0) || (c2.getRow() != 0);
/* 277:    */   }
/* 278:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFClientAnchor
 * JD-Core Version:    0.7.0.1
 */