/*   1:    */ package org.apache.poi.xssf.usermodel.helpers;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.NavigableSet;
/*   7:    */ import java.util.TreeSet;
/*   8:    */ import org.apache.poi.ss.usermodel.CellStyle;
/*   9:    */ import org.apache.poi.xssf.util.CTColComparator;
/*  10:    */ import org.apache.poi.xssf.util.NumericRanges;
/*  11:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;
/*  12:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols;
/*  13:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols.Factory;
/*  14:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
/*  15:    */ 
/*  16:    */ public class ColumnHelper
/*  17:    */ {
/*  18:    */   private CTWorksheet worksheet;
/*  19:    */   
/*  20:    */   public ColumnHelper(CTWorksheet worksheet)
/*  21:    */   {
/*  22: 45 */     this.worksheet = worksheet;
/*  23: 46 */     cleanColumns();
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void cleanColumns()
/*  27:    */   {
/*  28: 50 */     TreeSet<CTCol> trackedCols = new TreeSet(CTColComparator.BY_MIN_MAX);
/*  29: 51 */     CTCols newCols = CTCols.Factory.newInstance();
/*  30: 52 */     CTCols[] colsArray = this.worksheet.getColsArray();
/*  31: 53 */     int i = 0;
/*  32: 54 */     for (i = 0; i < colsArray.length; i++)
/*  33:    */     {
/*  34: 55 */       CTCols cols = colsArray[i];
/*  35: 56 */       CTCol[] colArray = cols.getColArray();
/*  36: 57 */       for (CTCol col : colArray) {
/*  37: 58 */         addCleanColIntoCols(newCols, col, trackedCols);
/*  38:    */       }
/*  39:    */     }
/*  40: 61 */     for (int y = i - 1; y >= 0; y--) {
/*  41: 62 */       this.worksheet.removeCols(y);
/*  42:    */     }
/*  43: 65 */     newCols.setColArray((CTCol[])trackedCols.toArray(new CTCol[trackedCols.size()]));
/*  44: 66 */     this.worksheet.addNewCols();
/*  45: 67 */     this.worksheet.setColsArray(0, newCols);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public CTCols addCleanColIntoCols(CTCols cols, CTCol newCol)
/*  49:    */   {
/*  50: 75 */     TreeSet<CTCol> trackedCols = new TreeSet(CTColComparator.BY_MIN_MAX);
/*  51:    */     
/*  52: 77 */     trackedCols.addAll(cols.getColList());
/*  53: 78 */     addCleanColIntoCols(cols, newCol, trackedCols);
/*  54: 79 */     cols.setColArray((CTCol[])trackedCols.toArray(new CTCol[0]));
/*  55: 80 */     return cols;
/*  56:    */   }
/*  57:    */   
/*  58:    */   private void addCleanColIntoCols(CTCols cols, CTCol newCol, TreeSet<CTCol> trackedCols)
/*  59:    */   {
/*  60: 84 */     List<CTCol> overlapping = getOverlappingCols(newCol, trackedCols);
/*  61: 85 */     if (overlapping.isEmpty())
/*  62:    */     {
/*  63: 86 */       trackedCols.add(cloneCol(cols, newCol));
/*  64: 87 */       return;
/*  65:    */     }
/*  66: 90 */     trackedCols.removeAll(overlapping);
/*  67: 91 */     for (CTCol existing : overlapping)
/*  68:    */     {
/*  69: 94 */       long[] overlap = getOverlap(newCol, existing);
/*  70:    */       
/*  71: 96 */       CTCol overlapCol = cloneCol(cols, existing, overlap);
/*  72: 97 */       setColumnAttributes(newCol, overlapCol);
/*  73: 98 */       trackedCols.add(overlapCol);
/*  74:    */       
/*  75:100 */       CTCol beforeCol = existing.getMin() < newCol.getMin() ? existing : newCol;
/*  76:    */       
/*  77:102 */       long[] before = { Math.min(existing.getMin(), newCol.getMin()), overlap[0] - 1L };
/*  78:105 */       if (before[0] <= before[1]) {
/*  79:106 */         trackedCols.add(cloneCol(cols, beforeCol, before));
/*  80:    */       }
/*  81:109 */       CTCol afterCol = existing.getMax() > newCol.getMax() ? existing : newCol;
/*  82:    */       
/*  83:111 */       long[] after = { overlap[1] + 1L, Math.max(existing.getMax(), newCol.getMax()) };
/*  84:113 */       if (after[0] <= after[1]) {
/*  85:114 */         trackedCols.add(cloneCol(cols, afterCol, after));
/*  86:    */       }
/*  87:    */     }
/*  88:    */   }
/*  89:    */   
/*  90:    */   private CTCol cloneCol(CTCols cols, CTCol col, long[] newRange)
/*  91:    */   {
/*  92:120 */     CTCol cloneCol = cloneCol(cols, col);
/*  93:121 */     cloneCol.setMin(newRange[0]);
/*  94:122 */     cloneCol.setMax(newRange[1]);
/*  95:123 */     return cloneCol;
/*  96:    */   }
/*  97:    */   
/*  98:    */   private long[] getOverlap(CTCol col1, CTCol col2)
/*  99:    */   {
/* 100:127 */     return getOverlappingRange(col1, col2);
/* 101:    */   }
/* 102:    */   
/* 103:    */   private List<CTCol> getOverlappingCols(CTCol newCol, TreeSet<CTCol> trackedCols)
/* 104:    */   {
/* 105:131 */     CTCol lower = (CTCol)trackedCols.lower(newCol);
/* 106:132 */     NavigableSet<CTCol> potentiallyOverlapping = lower == null ? trackedCols : trackedCols.tailSet(lower, overlaps(lower, newCol));
/* 107:133 */     List<CTCol> overlapping = new ArrayList();
/* 108:134 */     for (CTCol existing : potentiallyOverlapping)
/* 109:    */     {
/* 110:135 */       if (!overlaps(newCol, existing)) {
/* 111:    */         break;
/* 112:    */       }
/* 113:136 */       overlapping.add(existing);
/* 114:    */     }
/* 115:141 */     return overlapping;
/* 116:    */   }
/* 117:    */   
/* 118:    */   private boolean overlaps(CTCol col1, CTCol col2)
/* 119:    */   {
/* 120:145 */     return NumericRanges.getOverlappingType(toRange(col1), toRange(col2)) != -1;
/* 121:    */   }
/* 122:    */   
/* 123:    */   private long[] getOverlappingRange(CTCol col1, CTCol col2)
/* 124:    */   {
/* 125:149 */     return NumericRanges.getOverlappingRange(toRange(col1), toRange(col2));
/* 126:    */   }
/* 127:    */   
/* 128:    */   private long[] toRange(CTCol col)
/* 129:    */   {
/* 130:153 */     return new long[] { col.getMin(), col.getMax() };
/* 131:    */   }
/* 132:    */   
/* 133:    */   public static void sortColumns(CTCols newCols)
/* 134:    */   {
/* 135:157 */     CTCol[] colArray = newCols.getColArray();
/* 136:158 */     Arrays.sort(colArray, CTColComparator.BY_MIN_MAX);
/* 137:159 */     newCols.setColArray(colArray);
/* 138:    */   }
/* 139:    */   
/* 140:    */   public CTCol cloneCol(CTCols cols, CTCol col)
/* 141:    */   {
/* 142:163 */     CTCol newCol = cols.addNewCol();
/* 143:164 */     newCol.setMin(col.getMin());
/* 144:165 */     newCol.setMax(col.getMax());
/* 145:166 */     setColumnAttributes(col, newCol);
/* 146:167 */     return newCol;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public CTCol getColumn(long index, boolean splitColumns)
/* 150:    */   {
/* 151:174 */     return getColumn1Based(index + 1L, splitColumns);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public CTCol getColumn1Based(long index1, boolean splitColumns)
/* 155:    */   {
/* 156:183 */     CTCols cols = this.worksheet.getColsArray(0);
/* 157:    */     
/* 158:    */ 
/* 159:    */ 
/* 160:    */ 
/* 161:188 */     CTCol[] colArray = cols.getColArray();
/* 162:190 */     for (CTCol col : colArray)
/* 163:    */     {
/* 164:191 */       long colMin = col.getMin();
/* 165:192 */       long colMax = col.getMax();
/* 166:193 */       if ((colMin <= index1) && (colMax >= index1))
/* 167:    */       {
/* 168:194 */         if (splitColumns)
/* 169:    */         {
/* 170:195 */           if (colMin < index1) {
/* 171:196 */             insertCol(cols, colMin, index1 - 1L, new CTCol[] { col });
/* 172:    */           }
/* 173:198 */           if (colMax > index1) {
/* 174:199 */             insertCol(cols, index1 + 1L, colMax, new CTCol[] { col });
/* 175:    */           }
/* 176:201 */           col.setMin(index1);
/* 177:202 */           col.setMax(index1);
/* 178:    */         }
/* 179:204 */         return col;
/* 180:    */       }
/* 181:    */     }
/* 182:207 */     return null;
/* 183:    */   }
/* 184:    */   
/* 185:    */   private CTCol insertCol(CTCols cols, long min, long max, CTCol[] colsWithAttributes)
/* 186:    */   {
/* 187:215 */     return insertCol(cols, min, max, colsWithAttributes, false, null);
/* 188:    */   }
/* 189:    */   
/* 190:    */   private CTCol insertCol(CTCols cols, long min, long max, CTCol[] colsWithAttributes, boolean ignoreExistsCheck, CTCol overrideColumn)
/* 191:    */   {
/* 192:220 */     if ((ignoreExistsCheck) || (!columnExists(cols, min, max)))
/* 193:    */     {
/* 194:221 */       CTCol newCol = cols.insertNewCol(0);
/* 195:222 */       newCol.setMin(min);
/* 196:223 */       newCol.setMax(max);
/* 197:224 */       for (CTCol col : colsWithAttributes) {
/* 198:225 */         setColumnAttributes(col, newCol);
/* 199:    */       }
/* 200:227 */       if (overrideColumn != null) {
/* 201:227 */         setColumnAttributes(overrideColumn, newCol);
/* 202:    */       }
/* 203:228 */       return newCol;
/* 204:    */     }
/* 205:230 */     return null;
/* 206:    */   }
/* 207:    */   
/* 208:    */   public boolean columnExists(CTCols cols, long index)
/* 209:    */   {
/* 210:238 */     return columnExists1Based(cols, index + 1L);
/* 211:    */   }
/* 212:    */   
/* 213:    */   private boolean columnExists1Based(CTCols cols, long index1)
/* 214:    */   {
/* 215:242 */     for (CTCol col : cols.getColArray()) {
/* 216:243 */       if (col.getMin() == index1) {
/* 217:244 */         return true;
/* 218:    */       }
/* 219:    */     }
/* 220:247 */     return false;
/* 221:    */   }
/* 222:    */   
/* 223:    */   public void setColumnAttributes(CTCol fromCol, CTCol toCol)
/* 224:    */   {
/* 225:251 */     if (fromCol.isSetBestFit()) {
/* 226:251 */       toCol.setBestFit(fromCol.getBestFit());
/* 227:    */     }
/* 228:252 */     if (fromCol.isSetCustomWidth()) {
/* 229:252 */       toCol.setCustomWidth(fromCol.getCustomWidth());
/* 230:    */     }
/* 231:253 */     if (fromCol.isSetHidden()) {
/* 232:253 */       toCol.setHidden(fromCol.getHidden());
/* 233:    */     }
/* 234:254 */     if (fromCol.isSetStyle()) {
/* 235:254 */       toCol.setStyle(fromCol.getStyle());
/* 236:    */     }
/* 237:255 */     if (fromCol.isSetWidth()) {
/* 238:255 */       toCol.setWidth(fromCol.getWidth());
/* 239:    */     }
/* 240:256 */     if (fromCol.isSetCollapsed()) {
/* 241:256 */       toCol.setCollapsed(fromCol.getCollapsed());
/* 242:    */     }
/* 243:257 */     if (fromCol.isSetPhonetic()) {
/* 244:257 */       toCol.setPhonetic(fromCol.getPhonetic());
/* 245:    */     }
/* 246:258 */     if (fromCol.isSetOutlineLevel()) {
/* 247:258 */       toCol.setOutlineLevel(fromCol.getOutlineLevel());
/* 248:    */     }
/* 249:259 */     toCol.setCollapsed(fromCol.isSetCollapsed());
/* 250:    */   }
/* 251:    */   
/* 252:    */   public void setColBestFit(long index, boolean bestFit)
/* 253:    */   {
/* 254:263 */     CTCol col = getOrCreateColumn1Based(index + 1L, false);
/* 255:264 */     col.setBestFit(bestFit);
/* 256:    */   }
/* 257:    */   
/* 258:    */   public void setCustomWidth(long index, boolean bestFit)
/* 259:    */   {
/* 260:267 */     CTCol col = getOrCreateColumn1Based(index + 1L, true);
/* 261:268 */     col.setCustomWidth(bestFit);
/* 262:    */   }
/* 263:    */   
/* 264:    */   public void setColWidth(long index, double width)
/* 265:    */   {
/* 266:272 */     CTCol col = getOrCreateColumn1Based(index + 1L, true);
/* 267:273 */     col.setWidth(width);
/* 268:    */   }
/* 269:    */   
/* 270:    */   public void setColHidden(long index, boolean hidden)
/* 271:    */   {
/* 272:277 */     CTCol col = getOrCreateColumn1Based(index + 1L, true);
/* 273:278 */     col.setHidden(hidden);
/* 274:    */   }
/* 275:    */   
/* 276:    */   protected CTCol getOrCreateColumn1Based(long index1, boolean splitColumns)
/* 277:    */   {
/* 278:286 */     CTCol col = getColumn1Based(index1, splitColumns);
/* 279:287 */     if (col == null)
/* 280:    */     {
/* 281:288 */       col = this.worksheet.getColsArray(0).addNewCol();
/* 282:289 */       col.setMin(index1);
/* 283:290 */       col.setMax(index1);
/* 284:    */     }
/* 285:292 */     return col;
/* 286:    */   }
/* 287:    */   
/* 288:    */   public void setColDefaultStyle(long index, CellStyle style)
/* 289:    */   {
/* 290:296 */     setColDefaultStyle(index, style.getIndex());
/* 291:    */   }
/* 292:    */   
/* 293:    */   public void setColDefaultStyle(long index, int styleId)
/* 294:    */   {
/* 295:300 */     CTCol col = getOrCreateColumn1Based(index + 1L, true);
/* 296:301 */     col.setStyle(styleId);
/* 297:    */   }
/* 298:    */   
/* 299:    */   public int getColDefaultStyle(long index)
/* 300:    */   {
/* 301:306 */     if (getColumn(index, false) != null) {
/* 302:307 */       return (int)getColumn(index, false).getStyle();
/* 303:    */     }
/* 304:309 */     return -1;
/* 305:    */   }
/* 306:    */   
/* 307:    */   private boolean columnExists(CTCols cols, long min, long max)
/* 308:    */   {
/* 309:313 */     for (CTCol col : cols.getColArray()) {
/* 310:314 */       if ((col.getMin() == min) && (col.getMax() == max)) {
/* 311:315 */         return true;
/* 312:    */       }
/* 313:    */     }
/* 314:318 */     return false;
/* 315:    */   }
/* 316:    */   
/* 317:    */   public int getIndexOfColumn(CTCols cols, CTCol searchCol)
/* 318:    */   {
/* 319:322 */     if ((cols == null) || (searchCol == null)) {
/* 320:322 */       return -1;
/* 321:    */     }
/* 322:323 */     int i = 0;
/* 323:324 */     for (CTCol col : cols.getColArray())
/* 324:    */     {
/* 325:325 */       if ((col.getMin() == searchCol.getMin()) && (col.getMax() == searchCol.getMax())) {
/* 326:326 */         return i;
/* 327:    */       }
/* 328:328 */       i++;
/* 329:    */     }
/* 330:330 */     return -1;
/* 331:    */   }
/* 332:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.helpers.ColumnHelper
 * JD-Core Version:    0.7.0.1
 */