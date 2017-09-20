/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ 
/*   6:    */ public final class CellRangeUtil
/*   7:    */ {
/*   8:    */   public static final int NO_INTERSECTION = 1;
/*   9:    */   public static final int OVERLAP = 2;
/*  10:    */   public static final int INSIDE = 3;
/*  11:    */   public static final int ENCLOSES = 4;
/*  12:    */   
/*  13:    */   public static int intersect(CellRangeAddress crA, CellRangeAddress crB)
/*  14:    */   {
/*  15: 57 */     int firstRow = crB.getFirstRow();
/*  16: 58 */     int lastRow = crB.getLastRow();
/*  17: 59 */     int firstCol = crB.getFirstColumn();
/*  18: 60 */     int lastCol = crB.getLastColumn();
/*  19: 62 */     if ((gt(crA.getFirstRow(), lastRow)) || (lt(crA.getLastRow(), firstRow)) || (gt(crA.getFirstColumn(), lastCol)) || (lt(crA.getLastColumn(), firstCol))) {
/*  20: 67 */       return 1;
/*  21:    */     }
/*  22: 69 */     if (contains(crA, crB)) {
/*  23: 71 */       return 3;
/*  24:    */     }
/*  25: 73 */     if (contains(crB, crA)) {
/*  26: 75 */       return 4;
/*  27:    */     }
/*  28: 79 */     return 2;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static CellRangeAddress[] mergeCellRanges(CellRangeAddress[] cellRanges)
/*  32:    */   {
/*  33: 93 */     if (cellRanges.length < 1) {
/*  34: 94 */       return new CellRangeAddress[0];
/*  35:    */     }
/*  36: 96 */     List<CellRangeAddress> list = toList(cellRanges);
/*  37: 97 */     List<CellRangeAddress> temp = mergeCellRanges(list);
/*  38: 98 */     return toArray(temp);
/*  39:    */   }
/*  40:    */   
/*  41:    */   private static List<CellRangeAddress> mergeCellRanges(List<CellRangeAddress> cellRangeList)
/*  42:    */   {
/*  43:104 */     while (cellRangeList.size() > 1)
/*  44:    */     {
/*  45:105 */       boolean somethingGotMerged = false;
/*  46:108 */       for (int i = 0; i < cellRangeList.size(); i++)
/*  47:    */       {
/*  48:109 */         CellRangeAddress range1 = (CellRangeAddress)cellRangeList.get(i);
/*  49:112 */         for (int j = i + 1; j < cellRangeList.size(); j++)
/*  50:    */         {
/*  51:113 */           CellRangeAddress range2 = (CellRangeAddress)cellRangeList.get(j);
/*  52:    */           
/*  53:115 */           CellRangeAddress[] mergeResult = mergeRanges(range1, range2);
/*  54:116 */           if (mergeResult != null)
/*  55:    */           {
/*  56:119 */             somethingGotMerged = true;
/*  57:    */             
/*  58:121 */             cellRangeList.set(i, mergeResult[0]);
/*  59:    */             
/*  60:123 */             cellRangeList.remove(j--);
/*  61:125 */             for (int k = 1; k < mergeResult.length; k++)
/*  62:    */             {
/*  63:126 */               j++;
/*  64:127 */               cellRangeList.add(j, mergeResult[k]);
/*  65:    */             }
/*  66:    */           }
/*  67:    */         }
/*  68:    */       }
/*  69:131 */       if (!somethingGotMerged) {
/*  70:    */         break;
/*  71:    */       }
/*  72:    */     }
/*  73:136 */     return cellRangeList;
/*  74:    */   }
/*  75:    */   
/*  76:    */   private static CellRangeAddress[] mergeRanges(CellRangeAddress range1, CellRangeAddress range2)
/*  77:    */   {
/*  78:143 */     int x = intersect(range1, range2);
/*  79:144 */     switch (x)
/*  80:    */     {
/*  81:    */     case 1: 
/*  82:148 */       if (hasExactSharedBorder(range1, range2)) {
/*  83:149 */         return new CellRangeAddress[] { createEnclosingCellRange(range1, range2) };
/*  84:    */       }
/*  85:152 */       return null;
/*  86:    */     case 2: 
/*  87:157 */       return null;
/*  88:    */     case 3: 
/*  89:160 */       return new CellRangeAddress[] { range1 };
/*  90:    */     case 4: 
/*  91:163 */       return new CellRangeAddress[] { range2 };
/*  92:    */     }
/*  93:165 */     throw new RuntimeException("unexpected intersection result (" + x + ")");
/*  94:    */   }
/*  95:    */   
/*  96:    */   private static CellRangeAddress[] toArray(List<CellRangeAddress> temp)
/*  97:    */   {
/*  98:169 */     CellRangeAddress[] result = new CellRangeAddress[temp.size()];
/*  99:170 */     temp.toArray(result);
/* 100:171 */     return result;
/* 101:    */   }
/* 102:    */   
/* 103:    */   private static List<CellRangeAddress> toList(CellRangeAddress[] temp)
/* 104:    */   {
/* 105:174 */     List<CellRangeAddress> result = new ArrayList(temp.length);
/* 106:175 */     for (CellRangeAddress range : temp) {
/* 107:176 */       result.add(range);
/* 108:    */     }
/* 109:178 */     return result;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public static boolean contains(CellRangeAddress crA, CellRangeAddress crB)
/* 113:    */   {
/* 114:192 */     return (le(crA.getFirstRow(), crB.getFirstRow())) && (ge(crA.getLastRow(), crB.getLastRow())) && (le(crA.getFirstColumn(), crB.getFirstColumn())) && (ge(crA.getLastColumn(), crB.getLastColumn()));
/* 115:    */   }
/* 116:    */   
/* 117:    */   public static boolean hasExactSharedBorder(CellRangeAddress crA, CellRangeAddress crB)
/* 118:    */   {
/* 119:205 */     int oFirstRow = crB.getFirstRow();
/* 120:206 */     int oLastRow = crB.getLastRow();
/* 121:207 */     int oFirstCol = crB.getFirstColumn();
/* 122:208 */     int oLastCol = crB.getLastColumn();
/* 123:210 */     if (((crA.getFirstRow() > 0) && (crA.getFirstRow() - 1 == oLastRow)) || ((oFirstRow > 0) && (oFirstRow - 1 == crA.getLastRow()))) {
/* 124:214 */       return (crA.getFirstColumn() == oFirstCol) && (crA.getLastColumn() == oLastCol);
/* 125:    */     }
/* 126:217 */     if (((crA.getFirstColumn() > 0) && (crA.getFirstColumn() - 1 == oLastCol)) || ((oFirstCol > 0) && (crA.getLastColumn() == oFirstCol - 1))) {
/* 127:221 */       return (crA.getFirstRow() == oFirstRow) && (crA.getLastRow() == oLastRow);
/* 128:    */     }
/* 129:223 */     return false;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public static CellRangeAddress createEnclosingCellRange(CellRangeAddress crA, CellRangeAddress crB)
/* 133:    */   {
/* 134:232 */     if (crB == null) {
/* 135:233 */       return crA.copy();
/* 136:    */     }
/* 137:236 */     int minRow = lt(crB.getFirstRow(), crA.getFirstRow()) ? crB.getFirstRow() : crA.getFirstRow();
/* 138:237 */     int maxRow = gt(crB.getLastRow(), crA.getLastRow()) ? crB.getLastRow() : crA.getLastRow();
/* 139:238 */     int minCol = lt(crB.getFirstColumn(), crA.getFirstColumn()) ? crB.getFirstColumn() : crA.getFirstColumn();
/* 140:239 */     int maxCol = gt(crB.getLastColumn(), crA.getLastColumn()) ? crB.getLastColumn() : crA.getLastColumn();
/* 141:    */     
/* 142:241 */     return new CellRangeAddress(minRow, maxRow, minCol, maxCol);
/* 143:    */   }
/* 144:    */   
/* 145:    */   private static boolean lt(int a, int b)
/* 146:    */   {
/* 147:249 */     return a != -1;
/* 148:    */   }
/* 149:    */   
/* 150:    */   private static boolean le(int a, int b)
/* 151:    */   {
/* 152:257 */     return (a == b) || (lt(a, b));
/* 153:    */   }
/* 154:    */   
/* 155:    */   private static boolean gt(int a, int b)
/* 156:    */   {
/* 157:265 */     return lt(b, a);
/* 158:    */   }
/* 159:    */   
/* 160:    */   private static boolean ge(int a, int b)
/* 161:    */   {
/* 162:273 */     return !lt(a, b);
/* 163:    */   }
/* 164:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.CellRangeUtil
 * JD-Core Version:    0.7.0.1
 */