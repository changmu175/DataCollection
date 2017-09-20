/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.POIXMLException;
/*   4:    */ import org.apache.poi.ss.usermodel.PageOrder;
/*   5:    */ import org.apache.poi.ss.usermodel.PaperSize;
/*   6:    */ import org.apache.poi.ss.usermodel.PrintCellComments;
/*   7:    */ import org.apache.poi.ss.usermodel.PrintOrientation;
/*   8:    */ import org.apache.poi.ss.usermodel.PrintSetup;
/*   9:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins;
/*  10:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup;
/*  11:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
/*  12:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellComments;
/*  13:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellComments.Enum;
/*  14:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STOrientation.Enum;
/*  15:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPageOrder.Enum;
/*  16:    */ 
/*  17:    */ public class XSSFPrintSetup
/*  18:    */   implements PrintSetup
/*  19:    */ {
/*  20:    */   private CTWorksheet ctWorksheet;
/*  21:    */   private CTPageSetup pageSetup;
/*  22:    */   private CTPageMargins pageMargins;
/*  23:    */   
/*  24:    */   protected XSSFPrintSetup(CTWorksheet worksheet)
/*  25:    */   {
/*  26: 43 */     this.ctWorksheet = worksheet;
/*  27: 45 */     if (this.ctWorksheet.isSetPageSetup()) {
/*  28: 46 */       this.pageSetup = this.ctWorksheet.getPageSetup();
/*  29:    */     } else {
/*  30: 48 */       this.pageSetup = this.ctWorksheet.addNewPageSetup();
/*  31:    */     }
/*  32: 50 */     if (this.ctWorksheet.isSetPageMargins()) {
/*  33: 51 */       this.pageMargins = this.ctWorksheet.getPageMargins();
/*  34:    */     } else {
/*  35: 53 */       this.pageMargins = this.ctWorksheet.addNewPageMargins();
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setPaperSize(short size)
/*  40:    */   {
/*  41: 63 */     this.pageSetup.setPaperSize(size);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setPaperSize(PaperSize size)
/*  45:    */   {
/*  46: 72 */     setPaperSize((short)(size.ordinal() + 1));
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setScale(short scale)
/*  50:    */   {
/*  51: 83 */     if ((scale < 10) || (scale > 400)) {
/*  52: 83 */       throw new POIXMLException("Scale value not accepted: you must choose a value between 10 and 400.");
/*  53:    */     }
/*  54: 84 */     this.pageSetup.setScale(scale);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setPageStart(short start)
/*  58:    */   {
/*  59: 94 */     this.pageSetup.setFirstPageNumber(start);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setFitWidth(short width)
/*  63:    */   {
/*  64:103 */     this.pageSetup.setFitToWidth(width);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setFitHeight(short height)
/*  68:    */   {
/*  69:112 */     this.pageSetup.setFitToHeight(height);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setLeftToRight(boolean ltor)
/*  73:    */   {
/*  74:121 */     if (ltor) {
/*  75:122 */       setPageOrder(PageOrder.OVER_THEN_DOWN);
/*  76:    */     } else {
/*  77:124 */       setPageOrder(PageOrder.DOWN_THEN_OVER);
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setLandscape(boolean ls)
/*  82:    */   {
/*  83:133 */     if (ls) {
/*  84:134 */       setOrientation(PrintOrientation.LANDSCAPE);
/*  85:    */     } else {
/*  86:136 */       setOrientation(PrintOrientation.PORTRAIT);
/*  87:    */     }
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setValidSettings(boolean valid)
/*  91:    */   {
/*  92:148 */     this.pageSetup.setUsePrinterDefaults(valid);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setNoColor(boolean mono)
/*  96:    */   {
/*  97:157 */     this.pageSetup.setBlackAndWhite(mono);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setDraft(boolean d)
/* 101:    */   {
/* 102:166 */     this.pageSetup.setDraft(d);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setNotes(boolean printnotes)
/* 106:    */   {
/* 107:175 */     if (printnotes) {
/* 108:176 */       this.pageSetup.setCellComments(STCellComments.AS_DISPLAYED);
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void setNoOrientation(boolean orientation)
/* 113:    */   {
/* 114:186 */     if (orientation) {
/* 115:187 */       setOrientation(PrintOrientation.DEFAULT);
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setUsePage(boolean page)
/* 120:    */   {
/* 121:197 */     this.pageSetup.setUseFirstPageNumber(page);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void setHResolution(short resolution)
/* 125:    */   {
/* 126:206 */     this.pageSetup.setHorizontalDpi(resolution);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void setVResolution(short resolution)
/* 130:    */   {
/* 131:215 */     this.pageSetup.setVerticalDpi(resolution);
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void setHeaderMargin(double headermargin)
/* 135:    */   {
/* 136:224 */     this.pageMargins.setHeader(headermargin);
/* 137:    */   }
/* 138:    */   
/* 139:    */   public void setFooterMargin(double footermargin)
/* 140:    */   {
/* 141:233 */     this.pageMargins.setFooter(footermargin);
/* 142:    */   }
/* 143:    */   
/* 144:    */   public void setCopies(short copies)
/* 145:    */   {
/* 146:242 */     this.pageSetup.setCopies(copies);
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void setOrientation(PrintOrientation orientation)
/* 150:    */   {
/* 151:252 */     STOrientation.Enum v = STOrientation.Enum.forInt(orientation.getValue());
/* 152:253 */     this.pageSetup.setOrientation(v);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public PrintOrientation getOrientation()
/* 156:    */   {
/* 157:263 */     STOrientation.Enum val = this.pageSetup.getOrientation();
/* 158:264 */     return val == null ? PrintOrientation.DEFAULT : PrintOrientation.valueOf(val.intValue());
/* 159:    */   }
/* 160:    */   
/* 161:    */   public PrintCellComments getCellComment()
/* 162:    */   {
/* 163:269 */     STCellComments.Enum val = this.pageSetup.getCellComments();
/* 164:270 */     return val == null ? PrintCellComments.NONE : PrintCellComments.valueOf(val.intValue());
/* 165:    */   }
/* 166:    */   
/* 167:    */   public void setPageOrder(PageOrder pageOrder)
/* 168:    */   {
/* 169:279 */     STPageOrder.Enum v = STPageOrder.Enum.forInt(pageOrder.getValue());
/* 170:280 */     this.pageSetup.setPageOrder(v);
/* 171:    */   }
/* 172:    */   
/* 173:    */   public PageOrder getPageOrder()
/* 174:    */   {
/* 175:289 */     return this.pageSetup.getPageOrder() == null ? null : PageOrder.valueOf(this.pageSetup.getPageOrder().intValue());
/* 176:    */   }
/* 177:    */   
/* 178:    */   public short getPaperSize()
/* 179:    */   {
/* 180:298 */     return (short)(int)this.pageSetup.getPaperSize();
/* 181:    */   }
/* 182:    */   
/* 183:    */   public PaperSize getPaperSizeEnum()
/* 184:    */   {
/* 185:308 */     return PaperSize.values()[(getPaperSize() - 1)];
/* 186:    */   }
/* 187:    */   
/* 188:    */   public short getScale()
/* 189:    */   {
/* 190:317 */     return (short)(int)this.pageSetup.getScale();
/* 191:    */   }
/* 192:    */   
/* 193:    */   public short getPageStart()
/* 194:    */   {
/* 195:327 */     return (short)(int)this.pageSetup.getFirstPageNumber();
/* 196:    */   }
/* 197:    */   
/* 198:    */   public short getFitWidth()
/* 199:    */   {
/* 200:336 */     return (short)(int)this.pageSetup.getFitToWidth();
/* 201:    */   }
/* 202:    */   
/* 203:    */   public short getFitHeight()
/* 204:    */   {
/* 205:345 */     return (short)(int)this.pageSetup.getFitToHeight();
/* 206:    */   }
/* 207:    */   
/* 208:    */   public boolean getLeftToRight()
/* 209:    */   {
/* 210:354 */     return getPageOrder() == PageOrder.OVER_THEN_DOWN;
/* 211:    */   }
/* 212:    */   
/* 213:    */   public boolean getLandscape()
/* 214:    */   {
/* 215:363 */     return getOrientation() == PrintOrientation.LANDSCAPE;
/* 216:    */   }
/* 217:    */   
/* 218:    */   public boolean getValidSettings()
/* 219:    */   {
/* 220:375 */     return this.pageSetup.getUsePrinterDefaults();
/* 221:    */   }
/* 222:    */   
/* 223:    */   public boolean getNoColor()
/* 224:    */   {
/* 225:384 */     return this.pageSetup.getBlackAndWhite();
/* 226:    */   }
/* 227:    */   
/* 228:    */   public boolean getDraft()
/* 229:    */   {
/* 230:393 */     return this.pageSetup.getDraft();
/* 231:    */   }
/* 232:    */   
/* 233:    */   public boolean getNotes()
/* 234:    */   {
/* 235:402 */     return getCellComment() == PrintCellComments.AS_DISPLAYED;
/* 236:    */   }
/* 237:    */   
/* 238:    */   public boolean getNoOrientation()
/* 239:    */   {
/* 240:411 */     return getOrientation() == PrintOrientation.DEFAULT;
/* 241:    */   }
/* 242:    */   
/* 243:    */   public boolean getUsePage()
/* 244:    */   {
/* 245:420 */     return this.pageSetup.getUseFirstPageNumber();
/* 246:    */   }
/* 247:    */   
/* 248:    */   public short getHResolution()
/* 249:    */   {
/* 250:429 */     return (short)(int)this.pageSetup.getHorizontalDpi();
/* 251:    */   }
/* 252:    */   
/* 253:    */   public short getVResolution()
/* 254:    */   {
/* 255:438 */     return (short)(int)this.pageSetup.getVerticalDpi();
/* 256:    */   }
/* 257:    */   
/* 258:    */   public double getHeaderMargin()
/* 259:    */   {
/* 260:447 */     return this.pageMargins.getHeader();
/* 261:    */   }
/* 262:    */   
/* 263:    */   public double getFooterMargin()
/* 264:    */   {
/* 265:456 */     return this.pageMargins.getFooter();
/* 266:    */   }
/* 267:    */   
/* 268:    */   public short getCopies()
/* 269:    */   {
/* 270:465 */     return (short)(int)this.pageSetup.getCopies();
/* 271:    */   }
/* 272:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFPrintSetup
 * JD-Core Version:    0.7.0.1
 */