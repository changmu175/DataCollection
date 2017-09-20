/*   1:    */ package org.apache.poi.xssf.streaming;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.HashSet;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Map.Entry;
/*  10:    */ import java.util.Set;
/*  11:    */ import java.util.SortedSet;
/*  12:    */ import java.util.TreeSet;
/*  13:    */ import org.apache.poi.ss.usermodel.Cell;
/*  14:    */ import org.apache.poi.ss.usermodel.DataFormatter;
/*  15:    */ import org.apache.poi.ss.usermodel.Row;
/*  16:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  17:    */ import org.apache.poi.ss.util.SheetUtil;
/*  18:    */ import org.apache.poi.util.Internal;
/*  19:    */ 
/*  20:    */ @Internal
/*  21:    */ class AutoSizeColumnTracker
/*  22:    */ {
/*  23:    */   private final int defaultCharWidth;
/*  24: 51 */   private final DataFormatter dataFormatter = new DataFormatter();
/*  25: 58 */   private final Map<Integer, ColumnWidthPair> maxColumnWidths = new HashMap();
/*  26: 61 */   private final Set<Integer> untrackedColumns = new HashSet();
/*  27: 62 */   private boolean trackAllColumns = false;
/*  28:    */   
/*  29:    */   private static class ColumnWidthPair
/*  30:    */   {
/*  31:    */     private double withSkipMergedCells;
/*  32:    */     private double withUseMergedCells;
/*  33:    */     
/*  34:    */     public ColumnWidthPair()
/*  35:    */     {
/*  36: 79 */       this(-1.0D, -1.0D);
/*  37:    */     }
/*  38:    */     
/*  39:    */     public ColumnWidthPair(double columnWidthSkipMergedCells, double columnWidthUseMergedCells)
/*  40:    */     {
/*  41: 83 */       this.withSkipMergedCells = columnWidthSkipMergedCells;
/*  42: 84 */       this.withUseMergedCells = columnWidthUseMergedCells;
/*  43:    */     }
/*  44:    */     
/*  45:    */     public double getMaxColumnWidth(boolean useMergedCells)
/*  46:    */     {
/*  47: 94 */       return useMergedCells ? this.withUseMergedCells : this.withSkipMergedCells;
/*  48:    */     }
/*  49:    */     
/*  50:    */     public void setMaxColumnWidths(double unmergedWidth, double mergedWidth)
/*  51:    */     {
/*  52:104 */       this.withUseMergedCells = Math.max(this.withUseMergedCells, mergedWidth);
/*  53:105 */       this.withSkipMergedCells = Math.max(this.withUseMergedCells, unmergedWidth);
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public AutoSizeColumnTracker(Sheet sheet)
/*  58:    */   {
/*  59:117 */     this.defaultCharWidth = SheetUtil.getDefaultCharWidth(sheet.getWorkbook());
/*  60:    */   }
/*  61:    */   
/*  62:    */   public SortedSet<Integer> getTrackedColumns()
/*  63:    */   {
/*  64:129 */     SortedSet<Integer> sorted = new TreeSet(this.maxColumnWidths.keySet());
/*  65:130 */     return Collections.unmodifiableSortedSet(sorted);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public boolean isColumnTracked(int column)
/*  69:    */   {
/*  70:141 */     return (this.trackAllColumns) || (this.maxColumnWidths.containsKey(Integer.valueOf(column)));
/*  71:    */   }
/*  72:    */   
/*  73:    */   public boolean isAllColumnsTracked()
/*  74:    */   {
/*  75:151 */     return this.trackAllColumns;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void trackAllColumns()
/*  79:    */   {
/*  80:160 */     this.trackAllColumns = true;
/*  81:161 */     this.untrackedColumns.clear();
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void untrackAllColumns()
/*  85:    */   {
/*  86:170 */     this.trackAllColumns = false;
/*  87:171 */     this.maxColumnWidths.clear();
/*  88:172 */     this.untrackedColumns.clear();
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void trackColumns(Collection<Integer> columns)
/*  92:    */   {
/*  93:185 */     for (Iterator i$ = columns.iterator(); i$.hasNext();)
/*  94:    */     {
/*  95:185 */       int column = ((Integer)i$.next()).intValue();
/*  96:186 */       trackColumn(column);
/*  97:    */     }
/*  98:    */   }
/*  99:    */   
/* 100:    */   public boolean trackColumn(int column)
/* 101:    */   {
/* 102:200 */     this.untrackedColumns.remove(Integer.valueOf(column));
/* 103:201 */     if (!this.maxColumnWidths.containsKey(Integer.valueOf(column)))
/* 104:    */     {
/* 105:202 */       this.maxColumnWidths.put(Integer.valueOf(column), new ColumnWidthPair());
/* 106:203 */       return true;
/* 107:    */     }
/* 108:205 */     return false;
/* 109:    */   }
/* 110:    */   
/* 111:    */   private boolean implicitlyTrackColumn(int column)
/* 112:    */   {
/* 113:217 */     if (!this.untrackedColumns.contains(Integer.valueOf(column)))
/* 114:    */     {
/* 115:218 */       trackColumn(column);
/* 116:219 */       return true;
/* 117:    */     }
/* 118:221 */     return false;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public boolean untrackColumns(Collection<Integer> columns)
/* 122:    */   {
/* 123:235 */     this.untrackedColumns.addAll(columns);
/* 124:236 */     return this.maxColumnWidths.keySet().removeAll(columns);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public boolean untrackColumn(int column)
/* 128:    */   {
/* 129:249 */     this.untrackedColumns.add(Integer.valueOf(column));
/* 130:250 */     return this.maxColumnWidths.keySet().remove(Integer.valueOf(column));
/* 131:    */   }
/* 132:    */   
/* 133:    */   public int getBestFitColumnWidth(int column, boolean useMergedCells)
/* 134:    */   {
/* 135:263 */     if (!this.maxColumnWidths.containsKey(Integer.valueOf(column))) {
/* 136:265 */       if (this.trackAllColumns)
/* 137:    */       {
/* 138:266 */         if (!implicitlyTrackColumn(column))
/* 139:    */         {
/* 140:267 */           Throwable reason = new IllegalStateException("Column was explicitly untracked after trackAllColumns() was called.");
/* 141:    */           
/* 142:269 */           throw new IllegalStateException("Cannot get best fit column width on explicitly untracked column " + column + ". " + "Either explicitly track the column or track all columns.", reason);
/* 143:    */         }
/* 144:    */       }
/* 145:    */       else
/* 146:    */       {
/* 147:275 */         Throwable reason = new IllegalStateException("Column was never explicitly tracked and isAllColumnsTracked() is false (trackAllColumns() was never called or untrackAllColumns() was called after trackAllColumns() was called).");
/* 148:    */         
/* 149:    */ 
/* 150:278 */         throw new IllegalStateException("Cannot get best fit column width on untracked column " + column + ". " + "Either explicitly track the column or track all columns.", reason);
/* 151:    */       }
/* 152:    */     }
/* 153:283 */     double width = ((ColumnWidthPair)this.maxColumnWidths.get(Integer.valueOf(column))).getMaxColumnWidth(useMergedCells);
/* 154:284 */     return (int)(256.0D * width);
/* 155:    */   }
/* 156:    */   
/* 157:    */   public void updateColumnWidths(Row row)
/* 158:    */   {
/* 159:297 */     implicitlyTrackColumnsInRow(row);
/* 160:302 */     if (this.maxColumnWidths.size() < row.getPhysicalNumberOfCells()) {
/* 161:304 */       for (Entry<Integer, ColumnWidthPair> e : this.maxColumnWidths.entrySet())
/* 162:    */       {
/* 163:305 */         int column = ((Integer)e.getKey()).intValue();
/* 164:306 */         Cell cell = row.getCell(column);
/* 165:314 */         if (cell != null)
/* 166:    */         {
/* 167:315 */           ColumnWidthPair pair = (ColumnWidthPair)e.getValue();
/* 168:316 */           updateColumnWidth(cell, pair);
/* 169:    */         }
/* 170:    */       }
/* 171:    */     } else {
/* 172:322 */       for (Cell cell : row)
/* 173:    */       {
/* 174:323 */         int column = cell.getColumnIndex();
/* 175:331 */         if (this.maxColumnWidths.containsKey(Integer.valueOf(column)))
/* 176:    */         {
/* 177:332 */           ColumnWidthPair pair = (ColumnWidthPair)this.maxColumnWidths.get(Integer.valueOf(column));
/* 178:333 */           updateColumnWidth(cell, pair);
/* 179:    */         }
/* 180:    */       }
/* 181:    */     }
/* 182:    */   }
/* 183:    */   
/* 184:    */   private void implicitlyTrackColumnsInRow(Row row)
/* 185:    */   {
/* 186:350 */     if (this.trackAllColumns) {
/* 187:352 */       for (Cell cell : row)
/* 188:    */       {
/* 189:353 */         int column = cell.getColumnIndex();
/* 190:354 */         implicitlyTrackColumn(column);
/* 191:    */       }
/* 192:    */     }
/* 193:    */   }
/* 194:    */   
/* 195:    */   private void updateColumnWidth(Cell cell, ColumnWidthPair pair)
/* 196:    */   {
/* 197:367 */     double unmergedWidth = SheetUtil.getCellWidth(cell, this.defaultCharWidth, this.dataFormatter, false);
/* 198:368 */     double mergedWidth = SheetUtil.getCellWidth(cell, this.defaultCharWidth, this.dataFormatter, true);
/* 199:369 */     pair.setMaxColumnWidths(unmergedWidth, mergedWidth);
/* 200:    */   }
/* 201:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xssf.streaming.AutoSizeColumnTracker

 * JD-Core Version:    0.7.0.1

 */