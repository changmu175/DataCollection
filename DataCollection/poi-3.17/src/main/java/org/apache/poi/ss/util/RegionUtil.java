/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.BorderStyle;
/*   4:    */ import org.apache.poi.ss.usermodel.Cell;
/*   5:    */ import org.apache.poi.ss.usermodel.Row;
/*   6:    */ import org.apache.poi.ss.usermodel.Sheet;
/*   7:    */ import org.apache.poi.util.Removal;
/*   8:    */ 
/*   9:    */ public final class RegionUtil
/*  10:    */ {
/*  11:    */   private static final class CellPropertySetter
/*  12:    */   {
/*  13:    */     private final String _propertyName;
/*  14:    */     private final Object _propertyValue;
/*  15:    */     
/*  16:    */     public CellPropertySetter(String propertyName, int value)
/*  17:    */     {
/*  18: 45 */       this._propertyName = propertyName;
/*  19: 46 */       this._propertyValue = Short.valueOf((short)value);
/*  20:    */     }
/*  21:    */     
/*  22:    */     public CellPropertySetter(String propertyName, BorderStyle value)
/*  23:    */     {
/*  24: 49 */       this._propertyName = propertyName;
/*  25: 50 */       this._propertyValue = value;
/*  26:    */     }
/*  27:    */     
/*  28:    */     public void setProperty(Row row, int column)
/*  29:    */     {
/*  30: 55 */       Cell cell = CellUtil.getCell(row, column);
/*  31: 56 */       CellUtil.setCellStyleProperty(cell, this._propertyName, this._propertyValue);
/*  32:    */     }
/*  33:    */   }
/*  34:    */   
/*  35:    */   /**
/*  36:    */    * @deprecated
/*  37:    */    */
/*  38:    */   @Removal(version="3.18")
/*  39:    */   public static void setBorderLeft(int border, CellRangeAddress region, Sheet sheet)
/*  40:    */   {
/*  41: 72 */     int rowStart = region.getFirstRow();
/*  42: 73 */     int rowEnd = region.getLastRow();
/*  43: 74 */     int column = region.getFirstColumn();
/*  44:    */     
/*  45: 76 */     CellPropertySetter cps = new CellPropertySetter("borderLeft", border);
/*  46: 77 */     for (int i = rowStart; i <= rowEnd; i++) {
/*  47: 78 */       cps.setProperty(CellUtil.getRow(i, sheet), column);
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static void setBorderLeft(BorderStyle border, CellRangeAddress region, Sheet sheet)
/*  52:    */   {
/*  53: 91 */     int rowStart = region.getFirstRow();
/*  54: 92 */     int rowEnd = region.getLastRow();
/*  55: 93 */     int column = region.getFirstColumn();
/*  56:    */     
/*  57: 95 */     CellPropertySetter cps = new CellPropertySetter("borderLeft", border);
/*  58: 96 */     for (int i = rowStart; i <= rowEnd; i++) {
/*  59: 97 */       cps.setProperty(CellUtil.getRow(i, sheet), column);
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   public static void setLeftBorderColor(int color, CellRangeAddress region, Sheet sheet)
/*  64:    */   {
/*  65:111 */     int rowStart = region.getFirstRow();
/*  66:112 */     int rowEnd = region.getLastRow();
/*  67:113 */     int column = region.getFirstColumn();
/*  68:    */     
/*  69:115 */     CellPropertySetter cps = new CellPropertySetter("leftBorderColor", color);
/*  70:116 */     for (int i = rowStart; i <= rowEnd; i++) {
/*  71:117 */       cps.setProperty(CellUtil.getRow(i, sheet), column);
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   /**
/*  76:    */    * @deprecated
/*  77:    */    */
/*  78:    */   @Removal(version="3.18")
/*  79:    */   public static void setBorderRight(int border, CellRangeAddress region, Sheet sheet)
/*  80:    */   {
/*  81:133 */     int rowStart = region.getFirstRow();
/*  82:134 */     int rowEnd = region.getLastRow();
/*  83:135 */     int column = region.getLastColumn();
/*  84:    */     
/*  85:137 */     CellPropertySetter cps = new CellPropertySetter("borderRight", border);
/*  86:138 */     for (int i = rowStart; i <= rowEnd; i++) {
/*  87:139 */       cps.setProperty(CellUtil.getRow(i, sheet), column);
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   public static void setBorderRight(BorderStyle border, CellRangeAddress region, Sheet sheet)
/*  92:    */   {
/*  93:152 */     int rowStart = region.getFirstRow();
/*  94:153 */     int rowEnd = region.getLastRow();
/*  95:154 */     int column = region.getLastColumn();
/*  96:    */     
/*  97:156 */     CellPropertySetter cps = new CellPropertySetter("borderRight", border);
/*  98:157 */     for (int i = rowStart; i <= rowEnd; i++) {
/*  99:158 */       cps.setProperty(CellUtil.getRow(i, sheet), column);
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   public static void setRightBorderColor(int color, CellRangeAddress region, Sheet sheet)
/* 104:    */   {
/* 105:172 */     int rowStart = region.getFirstRow();
/* 106:173 */     int rowEnd = region.getLastRow();
/* 107:174 */     int column = region.getLastColumn();
/* 108:    */     
/* 109:176 */     CellPropertySetter cps = new CellPropertySetter("rightBorderColor", color);
/* 110:177 */     for (int i = rowStart; i <= rowEnd; i++) {
/* 111:178 */       cps.setProperty(CellUtil.getRow(i, sheet), column);
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   /**
/* 116:    */    * @deprecated
/* 117:    */    */
/* 118:    */   @Removal(version="3.18")
/* 119:    */   public static void setBorderBottom(int border, CellRangeAddress region, Sheet sheet)
/* 120:    */   {
/* 121:194 */     int colStart = region.getFirstColumn();
/* 122:195 */     int colEnd = region.getLastColumn();
/* 123:196 */     int rowIndex = region.getLastRow();
/* 124:197 */     CellPropertySetter cps = new CellPropertySetter("borderBottom", border);
/* 125:198 */     Row row = CellUtil.getRow(rowIndex, sheet);
/* 126:199 */     for (int i = colStart; i <= colEnd; i++) {
/* 127:200 */       cps.setProperty(row, i);
/* 128:    */     }
/* 129:    */   }
/* 130:    */   
/* 131:    */   public static void setBorderBottom(BorderStyle border, CellRangeAddress region, Sheet sheet)
/* 132:    */   {
/* 133:213 */     int colStart = region.getFirstColumn();
/* 134:214 */     int colEnd = region.getLastColumn();
/* 135:215 */     int rowIndex = region.getLastRow();
/* 136:216 */     CellPropertySetter cps = new CellPropertySetter("borderBottom", border);
/* 137:217 */     Row row = CellUtil.getRow(rowIndex, sheet);
/* 138:218 */     for (int i = colStart; i <= colEnd; i++) {
/* 139:219 */       cps.setProperty(row, i);
/* 140:    */     }
/* 141:    */   }
/* 142:    */   
/* 143:    */   public static void setBottomBorderColor(int color, CellRangeAddress region, Sheet sheet)
/* 144:    */   {
/* 145:233 */     int colStart = region.getFirstColumn();
/* 146:234 */     int colEnd = region.getLastColumn();
/* 147:235 */     int rowIndex = region.getLastRow();
/* 148:236 */     CellPropertySetter cps = new CellPropertySetter("bottomBorderColor", color);
/* 149:237 */     Row row = CellUtil.getRow(rowIndex, sheet);
/* 150:238 */     for (int i = colStart; i <= colEnd; i++) {
/* 151:239 */       cps.setProperty(row, i);
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   /**
/* 156:    */    * @deprecated
/* 157:    */    */
/* 158:    */   @Removal(version="3.18")
/* 159:    */   public static void setBorderTop(int border, CellRangeAddress region, Sheet sheet)
/* 160:    */   {
/* 161:255 */     int colStart = region.getFirstColumn();
/* 162:256 */     int colEnd = region.getLastColumn();
/* 163:257 */     int rowIndex = region.getFirstRow();
/* 164:258 */     CellPropertySetter cps = new CellPropertySetter("borderTop", border);
/* 165:259 */     Row row = CellUtil.getRow(rowIndex, sheet);
/* 166:260 */     for (int i = colStart; i <= colEnd; i++) {
/* 167:261 */       cps.setProperty(row, i);
/* 168:    */     }
/* 169:    */   }
/* 170:    */   
/* 171:    */   public static void setBorderTop(BorderStyle border, CellRangeAddress region, Sheet sheet)
/* 172:    */   {
/* 173:274 */     int colStart = region.getFirstColumn();
/* 174:275 */     int colEnd = region.getLastColumn();
/* 175:276 */     int rowIndex = region.getFirstRow();
/* 176:277 */     CellPropertySetter cps = new CellPropertySetter("borderTop", border);
/* 177:278 */     Row row = CellUtil.getRow(rowIndex, sheet);
/* 178:279 */     for (int i = colStart; i <= colEnd; i++) {
/* 179:280 */       cps.setProperty(row, i);
/* 180:    */     }
/* 181:    */   }
/* 182:    */   
/* 183:    */   public static void setTopBorderColor(int color, CellRangeAddress region, Sheet sheet)
/* 184:    */   {
/* 185:294 */     int colStart = region.getFirstColumn();
/* 186:295 */     int colEnd = region.getLastColumn();
/* 187:296 */     int rowIndex = region.getFirstRow();
/* 188:297 */     CellPropertySetter cps = new CellPropertySetter("topBorderColor", color);
/* 189:298 */     Row row = CellUtil.getRow(rowIndex, sheet);
/* 190:299 */     for (int i = colStart; i <= colEnd; i++) {
/* 191:300 */       cps.setProperty(row, i);
/* 192:    */     }
/* 193:    */   }
/* 194:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.RegionUtil
 * JD-Core Version:    0.7.0.1
 */