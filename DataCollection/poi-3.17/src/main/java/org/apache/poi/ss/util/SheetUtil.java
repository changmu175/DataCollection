/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import java.awt.Shape;
/*   4:    */ import java.awt.font.FontRenderContext;
/*   5:    */ import java.awt.font.TextAttribute;
/*   6:    */ import java.awt.font.TextLayout;
/*   7:    */ import java.awt.geom.AffineTransform;
/*   8:    */ import java.awt.geom.Rectangle2D;
/*   9:    */ import java.text.AttributedString;
/*  10:    */ import java.util.Locale;
/*  11:    */ import java.util.Map;
/*  12:    */ import org.apache.poi.ss.usermodel.Cell;
/*  13:    */ import org.apache.poi.ss.usermodel.CellStyle;
/*  14:    */ import org.apache.poi.ss.usermodel.CellType;
/*  15:    */ import org.apache.poi.ss.usermodel.CellValue;
/*  16:    */ import org.apache.poi.ss.usermodel.DataFormatter;
/*  17:    */ import org.apache.poi.ss.usermodel.Font;
/*  18:    */ import org.apache.poi.ss.usermodel.FormulaEvaluator;
/*  19:    */ import org.apache.poi.ss.usermodel.RichTextString;
/*  20:    */ import org.apache.poi.ss.usermodel.Row;
/*  21:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  22:    */ import org.apache.poi.ss.usermodel.Workbook;
/*  23:    */ import org.apache.poi.util.Internal;
/*  24:    */ 
/*  25:    */ public class SheetUtil
/*  26:    */ {
/*  27:    */   private static final char defaultChar = '0';
/*  28:    */   private static final double fontHeightMultiple = 2.0D;
/*  29: 71 */   private static final FormulaEvaluator dummyEvaluator = new FormulaEvaluator()
/*  30:    */   {
/*  31:    */     public void clearAllCachedResultValues() {}
/*  32:    */     
/*  33:    */     public void notifySetFormula(Cell cell) {}
/*  34:    */     
/*  35:    */     public void notifyDeleteCell(Cell cell) {}
/*  36:    */     
/*  37:    */     public void notifyUpdateCell(Cell cell) {}
/*  38:    */     
/*  39:    */     public CellValue evaluate(Cell cell)
/*  40:    */     {
/*  41: 81 */       return null;
/*  42:    */     }
/*  43:    */     
/*  44:    */     public Cell evaluateInCell(Cell cell)
/*  45:    */     {
/*  46: 83 */       return null;
/*  47:    */     }
/*  48:    */     
/*  49:    */     public void setupReferencedWorkbooks(Map<String, FormulaEvaluator> workbooks) {}
/*  50:    */     
/*  51:    */     public void setDebugEvaluationOutputForNextEval(boolean value) {}
/*  52:    */     
/*  53:    */     public void setIgnoreMissingWorkbooks(boolean ignore) {}
/*  54:    */     
/*  55:    */     public void evaluateAll() {}
/*  56:    */     
/*  57:    */     public int evaluateFormulaCell(Cell cell)
/*  58:    */     {
/*  59: 95 */       return cell.getCachedFormulaResultType();
/*  60:    */     }
/*  61:    */     
/*  62:    */     /**
/*  63:    */      * @deprecated
/*  64:    */      */
/*  65:    */     @Internal(since="POI 3.15 beta 3")
/*  66:    */     public CellType evaluateFormulaCellEnum(Cell cell)
/*  67:    */     {
/*  68:104 */       return cell.getCachedFormulaResultTypeEnum();
/*  69:    */     }
/*  70:    */   };
/*  71:111 */   private static final FontRenderContext fontRenderContext = new FontRenderContext(null, true, true);
/*  72:    */   
/*  73:    */   public static double getCellWidth(Cell cell, int defaultCharWidth, DataFormatter formatter, boolean useMergedCells)
/*  74:    */   {
/*  75:123 */     Sheet sheet = cell.getSheet();
/*  76:124 */     Workbook wb = sheet.getWorkbook();
/*  77:125 */     Row row = cell.getRow();
/*  78:126 */     int column = cell.getColumnIndex();
/*  79:    */     
/*  80:    */ 
/*  81:    */ 
/*  82:130 */     int colspan = 1;
/*  83:131 */     for (CellRangeAddress region : sheet.getMergedRegions()) {
/*  84:132 */       if (region.isInRange(row.getRowNum(), column))
/*  85:    */       {
/*  86:133 */         if (!useMergedCells) {
/*  87:135 */           return -1.0D;
/*  88:    */         }
/*  89:137 */         cell = row.getCell(region.getFirstColumn());
/*  90:138 */         colspan = 1 + region.getLastColumn() - region.getFirstColumn();
/*  91:    */       }
/*  92:    */     }
/*  93:142 */     CellStyle style = cell.getCellStyle();
/*  94:143 */     CellType cellType = cell.getCellTypeEnum();
/*  95:146 */     if (cellType == CellType.FORMULA) {
/*  96:147 */       cellType = cell.getCachedFormulaResultTypeEnum();
/*  97:    */     }
/*  98:149 */     Font font = wb.getFontAt(style.getFontIndex());
/*  99:    */     
/* 100:151 */     double width = -1.0D;
/* 101:152 */     if (cellType == CellType.STRING)
/* 102:    */     {
/* 103:153 */       RichTextString rt = cell.getRichStringCellValue();
/* 104:154 */       String[] lines = rt.getString().split("\\n");
/* 105:155 */       for (String line : lines)
/* 106:    */       {
/* 107:156 */         String txt = line + '0';
/* 108:    */         
/* 109:158 */         AttributedString str = new AttributedString(txt);
/* 110:159 */         copyAttributes(font, str, 0, txt.length());
/* 111:    */         
/* 112:    */ 
/* 113:    */ 
/* 114:    */ 
/* 115:    */ 
/* 116:165 */         width = getCellWidth(defaultCharWidth, colspan, style, width, str);
/* 117:    */       }
/* 118:    */     }
/* 119:    */     else
/* 120:    */     {
/* 121:168 */       String sval = null;
/* 122:169 */       if (cellType == CellType.NUMERIC) {
/* 123:    */         try
/* 124:    */         {
/* 125:172 */           sval = formatter.formatCellValue(cell, dummyEvaluator);
/* 126:    */         }
/* 127:    */         catch (Exception e)
/* 128:    */         {
/* 129:174 */           sval = String.valueOf(cell.getNumericCellValue());
/* 130:    */         }
/* 131:176 */       } else if (cellType == CellType.BOOLEAN) {
/* 132:177 */         sval = String.valueOf(cell.getBooleanCellValue()).toUpperCase(Locale.ROOT);
/* 133:    */       }
/* 134:179 */       if (sval != null)
/* 135:    */       {
/* 136:180 */         String txt = sval + '0';
/* 137:181 */         AttributedString str = new AttributedString(txt);
/* 138:182 */         copyAttributes(font, str, 0, txt.length());
/* 139:    */         
/* 140:184 */         width = getCellWidth(defaultCharWidth, colspan, style, width, str);
/* 141:    */       }
/* 142:    */     }
/* 143:187 */     return width;
/* 144:    */   }
/* 145:    */   
/* 146:    */   private static double getCellWidth(int defaultCharWidth, int colspan, CellStyle style, double minWidth, AttributedString str)
/* 147:    */   {
/* 148:203 */     TextLayout layout = new TextLayout(str.getIterator(), fontRenderContext);
/* 149:    */     Rectangle2D bounds;
/* 150:    */     Rectangle2D bounds;
/* 151:205 */     if (style.getRotation() != 0)
/* 152:    */     {
/* 153:212 */       AffineTransform trans = new AffineTransform();
/* 154:213 */       trans.concatenate(AffineTransform.getRotateInstance(style.getRotation() * 2.0D * 3.141592653589793D / 360.0D));
/* 155:214 */       trans.concatenate(AffineTransform.getScaleInstance(1.0D, 2.0D));
/* 156:    */       
/* 157:    */ 
/* 158:217 */       bounds = layout.getOutline(trans).getBounds();
/* 159:    */     }
/* 160:    */     else
/* 161:    */     {
/* 162:219 */       bounds = layout.getBounds();
/* 163:    */     }
/* 164:222 */     double frameWidth = bounds.getX() + bounds.getWidth();
/* 165:223 */     return Math.max(minWidth, frameWidth / colspan / defaultCharWidth + style.getIndention());
/* 166:    */   }
/* 167:    */   
/* 168:    */   public static double getColumnWidth(Sheet sheet, int column, boolean useMergedCells)
/* 169:    */   {
/* 170:235 */     return getColumnWidth(sheet, column, useMergedCells, sheet.getFirstRowNum(), sheet.getLastRowNum());
/* 171:    */   }
/* 172:    */   
/* 173:    */   public static double getColumnWidth(Sheet sheet, int column, boolean useMergedCells, int firstRow, int lastRow)
/* 174:    */   {
/* 175:249 */     DataFormatter formatter = new DataFormatter();
/* 176:250 */     int defaultCharWidth = getDefaultCharWidth(sheet.getWorkbook());
/* 177:    */     
/* 178:252 */     double width = -1.0D;
/* 179:253 */     for (int rowIdx = firstRow; rowIdx <= lastRow; rowIdx++)
/* 180:    */     {
/* 181:254 */       Row row = sheet.getRow(rowIdx);
/* 182:255 */       if (row != null)
/* 183:    */       {
/* 184:256 */         double cellWidth = getColumnWidthForRow(row, column, defaultCharWidth, formatter, useMergedCells);
/* 185:257 */         width = Math.max(width, cellWidth);
/* 186:    */       }
/* 187:    */     }
/* 188:260 */     return width;
/* 189:    */   }
/* 190:    */   
/* 191:    */   @Internal
/* 192:    */   public static int getDefaultCharWidth(Workbook wb)
/* 193:    */   {
/* 194:271 */     Font defaultFont = wb.getFontAt((short)0);
/* 195:    */     
/* 196:273 */     AttributedString str = new AttributedString(String.valueOf('0'));
/* 197:274 */     copyAttributes(defaultFont, str, 0, 1);
/* 198:275 */     TextLayout layout = new TextLayout(str.getIterator(), fontRenderContext);
/* 199:276 */     return (int)layout.getAdvance();
/* 200:    */   }
/* 201:    */   
/* 202:    */   private static double getColumnWidthForRow(Row row, int column, int defaultCharWidth, DataFormatter formatter, boolean useMergedCells)
/* 203:    */   {
/* 204:292 */     if (row == null) {
/* 205:293 */       return -1.0D;
/* 206:    */     }
/* 207:296 */     Cell cell = row.getCell(column);
/* 208:298 */     if (cell == null) {
/* 209:299 */       return -1.0D;
/* 210:    */     }
/* 211:302 */     return getCellWidth(cell, defaultCharWidth, formatter, useMergedCells);
/* 212:    */   }
/* 213:    */   
/* 214:    */   public static boolean canComputeColumnWidth(Font font)
/* 215:    */   {
/* 216:319 */     AttributedString str = new AttributedString("1w");
/* 217:320 */     copyAttributes(font, str, 0, "1w".length());
/* 218:    */     
/* 219:322 */     TextLayout layout = new TextLayout(str.getIterator(), fontRenderContext);
/* 220:323 */     if (layout.getBounds().getWidth() > 0.0D) {
/* 221:324 */       return true;
/* 222:    */     }
/* 223:327 */     return false;
/* 224:    */   }
/* 225:    */   
/* 226:    */   private static void copyAttributes(Font font, AttributedString str, int startIdx, int endIdx)
/* 227:    */   {
/* 228:334 */     str.addAttribute(TextAttribute.FAMILY, font.getFontName(), startIdx, endIdx);
/* 229:335 */     str.addAttribute(TextAttribute.SIZE, Float.valueOf(font.getFontHeightInPoints()));
/* 230:336 */     if (font.getBold()) {
/* 231:336 */       str.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, startIdx, endIdx);
/* 232:    */     }
/* 233:337 */     if (font.getItalic()) {
/* 234:337 */       str.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, startIdx, endIdx);
/* 235:    */     }
/* 236:338 */     if (font.getUnderline() == 1) {
/* 237:338 */       str.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, startIdx, endIdx);
/* 238:    */     }
/* 239:    */   }
/* 240:    */   
/* 241:    */   @Deprecated
/* 242:    */   public static boolean containsCell(CellRangeAddress cr, int rowIx, int colIx)
/* 243:    */   {
/* 244:352 */     return cr.isInRange(rowIx, colIx);
/* 245:    */   }
/* 246:    */   
/* 247:    */   public static Cell getCell(Sheet sheet, int rowIx, int colIx)
/* 248:    */   {
/* 249:371 */     Row r = sheet.getRow(rowIx);
/* 250:372 */     if (r != null) {
/* 251:373 */       return r.getCell(colIx);
/* 252:    */     }
/* 253:375 */     return null;
/* 254:    */   }
/* 255:    */   
/* 256:    */   public static Cell getCellWithMerges(Sheet sheet, int rowIx, int colIx)
/* 257:    */   {
/* 258:396 */     Cell c = getCell(sheet, rowIx, colIx);
/* 259:397 */     if (c != null) {
/* 260:397 */       return c;
/* 261:    */     }
/* 262:399 */     for (CellRangeAddress mergedRegion : sheet.getMergedRegions()) {
/* 263:400 */       if (mergedRegion.isInRange(rowIx, colIx))
/* 264:    */       {
/* 265:403 */         Row r = sheet.getRow(mergedRegion.getFirstRow());
/* 266:404 */         if (r != null) {
/* 267:405 */           return r.getCell(mergedRegion.getFirstColumn());
/* 268:    */         }
/* 269:    */       }
/* 270:    */     }
/* 271:412 */     return null;
/* 272:    */   }
/* 273:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.SheetUtil
 * JD-Core Version:    0.7.0.1
 */