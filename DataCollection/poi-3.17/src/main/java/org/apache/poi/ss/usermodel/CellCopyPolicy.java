/*   1:    */ package org.apache.poi.ss.usermodel;
/*   2:    */ 
/*   3:    */ public class CellCopyPolicy
/*   4:    */ {
/*   5:    */   public static final boolean DEFAULT_COPY_CELL_VALUE_POLICY = true;
/*   6:    */   public static final boolean DEFAULT_COPY_CELL_STYLE_POLICY = true;
/*   7:    */   public static final boolean DEFAULT_COPY_CELL_FORMULA_POLICY = true;
/*   8:    */   public static final boolean DEFAULT_COPY_HYPERLINK_POLICY = true;
/*   9:    */   public static final boolean DEFAULT_MERGE_HYPERLINK_POLICY = false;
/*  10:    */   public static final boolean DEFAULT_COPY_ROW_HEIGHT_POLICY = true;
/*  11:    */   public static final boolean DEFAULT_CONDENSE_ROWS_POLICY = false;
/*  12:    */   public static final boolean DEFAULT_COPY_MERGED_REGIONS_POLICY = true;
/*  13: 39 */   private boolean copyCellValue = true;
/*  14: 40 */   private boolean copyCellStyle = true;
/*  15: 41 */   private boolean copyCellFormula = true;
/*  16: 42 */   private boolean copyHyperlink = true;
/*  17: 43 */   private boolean mergeHyperlink = false;
/*  18: 46 */   private boolean copyRowHeight = true;
/*  19: 47 */   private boolean condenseRows = false;
/*  20: 50 */   private boolean copyMergedRegions = true;
/*  21:    */   
/*  22:    */   public CellCopyPolicy() {}
/*  23:    */   
/*  24:    */   public CellCopyPolicy(CellCopyPolicy other)
/*  25:    */   {
/*  26: 64 */     this.copyCellValue = other.isCopyCellValue();
/*  27: 65 */     this.copyCellStyle = other.isCopyCellStyle();
/*  28: 66 */     this.copyCellFormula = other.isCopyCellFormula();
/*  29: 67 */     this.copyHyperlink = other.isCopyHyperlink();
/*  30: 68 */     this.mergeHyperlink = other.isMergeHyperlink();
/*  31:    */     
/*  32: 70 */     this.copyRowHeight = other.isCopyRowHeight();
/*  33: 71 */     this.condenseRows = other.isCondenseRows();
/*  34:    */     
/*  35: 73 */     this.copyMergedRegions = other.isCopyMergedRegions();
/*  36:    */   }
/*  37:    */   
/*  38:    */   private CellCopyPolicy(Builder builder)
/*  39:    */   {
/*  40: 80 */     this.copyCellValue = builder.copyCellValue;
/*  41: 81 */     this.copyCellStyle = builder.copyCellStyle;
/*  42: 82 */     this.copyCellFormula = builder.copyCellFormula;
/*  43: 83 */     this.copyHyperlink = builder.copyHyperlink;
/*  44: 84 */     this.mergeHyperlink = builder.mergeHyperlink;
/*  45:    */     
/*  46: 86 */     this.copyRowHeight = builder.copyRowHeight;
/*  47: 87 */     this.condenseRows = builder.condenseRows;
/*  48:    */     
/*  49: 89 */     this.copyMergedRegions = builder.copyMergedRegions;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static class Builder
/*  53:    */   {
/*  54: 94 */     private boolean copyCellValue = true;
/*  55: 95 */     private boolean copyCellStyle = true;
/*  56: 96 */     private boolean copyCellFormula = true;
/*  57: 97 */     private boolean copyHyperlink = true;
/*  58: 98 */     private boolean mergeHyperlink = false;
/*  59:101 */     private boolean copyRowHeight = true;
/*  60:102 */     private boolean condenseRows = false;
/*  61:105 */     private boolean copyMergedRegions = true;
/*  62:    */     
/*  63:    */     public Builder cellValue(boolean copyCellValue)
/*  64:    */     {
/*  65:115 */       this.copyCellValue = copyCellValue;
/*  66:116 */       return this;
/*  67:    */     }
/*  68:    */     
/*  69:    */     public Builder cellStyle(boolean copyCellStyle)
/*  70:    */     {
/*  71:119 */       this.copyCellStyle = copyCellStyle;
/*  72:120 */       return this;
/*  73:    */     }
/*  74:    */     
/*  75:    */     public Builder cellFormula(boolean copyCellFormula)
/*  76:    */     {
/*  77:123 */       this.copyCellFormula = copyCellFormula;
/*  78:124 */       return this;
/*  79:    */     }
/*  80:    */     
/*  81:    */     public Builder copyHyperlink(boolean copyHyperlink)
/*  82:    */     {
/*  83:127 */       this.copyHyperlink = copyHyperlink;
/*  84:128 */       return this;
/*  85:    */     }
/*  86:    */     
/*  87:    */     public Builder mergeHyperlink(boolean mergeHyperlink)
/*  88:    */     {
/*  89:131 */       this.mergeHyperlink = mergeHyperlink;
/*  90:132 */       return this;
/*  91:    */     }
/*  92:    */     
/*  93:    */     public Builder rowHeight(boolean copyRowHeight)
/*  94:    */     {
/*  95:137 */       this.copyRowHeight = copyRowHeight;
/*  96:138 */       return this;
/*  97:    */     }
/*  98:    */     
/*  99:    */     public Builder condenseRows(boolean condenseRows)
/* 100:    */     {
/* 101:141 */       this.condenseRows = condenseRows;
/* 102:142 */       return this;
/* 103:    */     }
/* 104:    */     
/* 105:    */     public Builder mergedRegions(boolean copyMergedRegions)
/* 106:    */     {
/* 107:147 */       this.copyMergedRegions = copyMergedRegions;
/* 108:148 */       return this;
/* 109:    */     }
/* 110:    */     
/* 111:    */     public CellCopyPolicy build()
/* 112:    */     {
/* 113:151 */       return new CellCopyPolicy(this, null);
/* 114:    */     }
/* 115:    */   }
/* 116:    */   
/* 117:    */   public Builder createBuilder()
/* 118:    */   {
/* 119:156 */     Builder builder = new Builder().cellValue(this.copyCellValue).cellStyle(this.copyCellStyle).cellFormula(this.copyCellFormula).copyHyperlink(this.copyHyperlink).mergeHyperlink(this.mergeHyperlink).rowHeight(this.copyRowHeight).condenseRows(this.condenseRows).mergedRegions(this.copyMergedRegions);
/* 120:    */     
/* 121:    */ 
/* 122:    */ 
/* 123:    */ 
/* 124:    */ 
/* 125:    */ 
/* 126:    */ 
/* 127:    */ 
/* 128:165 */     return builder;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public boolean isCopyCellValue()
/* 132:    */   {
/* 133:175 */     return this.copyCellValue;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void setCopyCellValue(boolean copyCellValue)
/* 137:    */   {
/* 138:182 */     this.copyCellValue = copyCellValue;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public boolean isCopyCellStyle()
/* 142:    */   {
/* 143:189 */     return this.copyCellStyle;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void setCopyCellStyle(boolean copyCellStyle)
/* 147:    */   {
/* 148:196 */     this.copyCellStyle = copyCellStyle;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public boolean isCopyCellFormula()
/* 152:    */   {
/* 153:203 */     return this.copyCellFormula;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public void setCopyCellFormula(boolean copyCellFormula)
/* 157:    */   {
/* 158:210 */     this.copyCellFormula = copyCellFormula;
/* 159:    */   }
/* 160:    */   
/* 161:    */   public boolean isCopyHyperlink()
/* 162:    */   {
/* 163:217 */     return this.copyHyperlink;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public void setCopyHyperlink(boolean copyHyperlink)
/* 167:    */   {
/* 168:224 */     this.copyHyperlink = copyHyperlink;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public boolean isMergeHyperlink()
/* 172:    */   {
/* 173:231 */     return this.mergeHyperlink;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public void setMergeHyperlink(boolean mergeHyperlink)
/* 177:    */   {
/* 178:238 */     this.mergeHyperlink = mergeHyperlink;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public boolean isCopyRowHeight()
/* 182:    */   {
/* 183:248 */     return this.copyRowHeight;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public void setCopyRowHeight(boolean copyRowHeight)
/* 187:    */   {
/* 188:255 */     this.copyRowHeight = copyRowHeight;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public boolean isCondenseRows()
/* 192:    */   {
/* 193:266 */     return this.condenseRows;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public void setCondenseRows(boolean condenseRows)
/* 197:    */   {
/* 198:273 */     this.condenseRows = condenseRows;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public boolean isCopyMergedRegions()
/* 202:    */   {
/* 203:284 */     return this.copyMergedRegions;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public void setCopyMergedRegions(boolean copyMergedRegions)
/* 207:    */   {
/* 208:291 */     this.copyMergedRegions = copyMergedRegions;
/* 209:    */   }
/* 210:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.CellCopyPolicy
 * JD-Core Version:    0.7.0.1
 */