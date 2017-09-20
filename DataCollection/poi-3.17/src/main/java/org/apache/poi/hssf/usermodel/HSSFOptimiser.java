/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.HashSet;
/*   4:    */ import org.apache.poi.hssf.model.InternalWorkbook;
/*   5:    */ import org.apache.poi.hssf.record.CellValueRecordInterface;
/*   6:    */ import org.apache.poi.hssf.record.ExtendedFormatRecord;
/*   7:    */ import org.apache.poi.hssf.record.FontRecord;
/*   8:    */ import org.apache.poi.hssf.record.common.UnicodeString;
/*   9:    */ import org.apache.poi.ss.usermodel.Cell;
/*  10:    */ import org.apache.poi.ss.usermodel.CellType;
/*  11:    */ import org.apache.poi.ss.usermodel.Row;
/*  12:    */ 
/*  13:    */ public class HSSFOptimiser
/*  14:    */ {
/*  15:    */   public static void optimiseFonts(HSSFWorkbook workbook)
/*  16:    */   {
/*  17: 54 */     short[] newPos = new short[workbook.getWorkbook().getNumberOfFontRecords() + 1];
/*  18:    */     
/*  19: 56 */     boolean[] zapRecords = new boolean[newPos.length];
/*  20: 57 */     for (int i = 0; i < newPos.length; i++)
/*  21:    */     {
/*  22: 58 */       newPos[i] = ((short)i);
/*  23: 59 */       zapRecords[i] = false;
/*  24:    */     }
/*  25: 64 */     FontRecord[] frecs = new FontRecord[newPos.length];
/*  26: 65 */     for (int i = 0; i < newPos.length; i++) {
/*  27: 67 */       if (i != 4) {
/*  28: 69 */         frecs[i] = workbook.getWorkbook().getFontRecordAt(i);
/*  29:    */       }
/*  30:    */     }
/*  31: 77 */     for (int i = 5; i < newPos.length; i++)
/*  32:    */     {
/*  33: 80 */       int earlierDuplicate = -1;
/*  34: 81 */       for (int j = 0; (j < i) && (earlierDuplicate == -1); j++) {
/*  35: 82 */         if (j != 4)
/*  36:    */         {
/*  37: 84 */           FontRecord frCheck = workbook.getWorkbook().getFontRecordAt(j);
/*  38: 85 */           if (frCheck.sameProperties(frecs[i])) {
/*  39: 86 */             earlierDuplicate = j;
/*  40:    */           }
/*  41:    */         }
/*  42:    */       }
/*  43: 91 */       if (earlierDuplicate != -1)
/*  44:    */       {
/*  45: 92 */         newPos[i] = ((short)earlierDuplicate);
/*  46: 93 */         zapRecords[i] = true;
/*  47:    */       }
/*  48:    */     }
/*  49:101 */     for (int i = 5; i < newPos.length; i++)
/*  50:    */     {
/*  51:104 */       short preDeletePos = newPos[i];
/*  52:105 */       short newPosition = preDeletePos;
/*  53:106 */       for (int j = 0; j < preDeletePos; j++) {
/*  54:107 */         if (zapRecords[j] != 0) {
/*  55:107 */           newPosition = (short)(newPosition - 1);
/*  56:    */         }
/*  57:    */       }
/*  58:111 */       newPos[i] = newPosition;
/*  59:    */     }
/*  60:115 */     for (int i = 5; i < newPos.length; i++) {
/*  61:116 */       if (zapRecords[i] != 0) {
/*  62:117 */         workbook.getWorkbook().removeFontRecord(frecs[i]);
/*  63:    */       }
/*  64:    */     }
/*  65:125 */     workbook.resetFontCache();
/*  66:129 */     for (int i = 0; i < workbook.getWorkbook().getNumExFormats(); i++)
/*  67:    */     {
/*  68:130 */       ExtendedFormatRecord xfr = workbook.getWorkbook().getExFormatAt(i);
/*  69:131 */       xfr.setFontIndex(newPos[xfr.getFontIndex()]);
/*  70:    */     }
/*  71:140 */     HashSet<UnicodeString> doneUnicodeStrings = new HashSet();
/*  72:141 */     for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++)
/*  73:    */     {
/*  74:142 */       HSSFSheet s = workbook.getSheetAt(sheetNum);
/*  75:143 */       for (Row row : s) {
/*  76:144 */         for (Cell cell : row) {
/*  77:145 */           if (cell.getCellTypeEnum() == CellType.STRING)
/*  78:    */           {
/*  79:146 */             HSSFRichTextString rtr = (HSSFRichTextString)cell.getRichStringCellValue();
/*  80:147 */             UnicodeString u = rtr.getRawUnicodeString();
/*  81:150 */             if (!doneUnicodeStrings.contains(u))
/*  82:    */             {
/*  83:152 */               for (short i = 5; i < newPos.length; i = (short)(i + 1)) {
/*  84:153 */                 if (i != newPos[i]) {
/*  85:154 */                   u.swapFontUse(i, newPos[i]);
/*  86:    */                 }
/*  87:    */               }
/*  88:159 */               doneUnicodeStrings.add(u);
/*  89:    */             }
/*  90:    */           }
/*  91:    */         }
/*  92:    */       }
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   public static void optimiseCellStyles(HSSFWorkbook workbook)
/*  97:    */   {
/*  98:177 */     short[] newPos = new short[workbook.getWorkbook().getNumExFormats()];
/*  99:178 */     boolean[] isUsed = new boolean[newPos.length];
/* 100:179 */     boolean[] zapRecords = new boolean[newPos.length];
/* 101:180 */     for (int i = 0; i < newPos.length; i++)
/* 102:    */     {
/* 103:181 */       isUsed[i] = false;
/* 104:182 */       newPos[i] = ((short)i);
/* 105:183 */       zapRecords[i] = false;
/* 106:    */     }
/* 107:188 */     ExtendedFormatRecord[] xfrs = new ExtendedFormatRecord[newPos.length];
/* 108:189 */     for (int i = 0; i < newPos.length; i++) {
/* 109:190 */       xfrs[i] = workbook.getWorkbook().getExFormatAt(i);
/* 110:    */     }
/* 111:198 */     for (int i = 21; i < newPos.length; i++)
/* 112:    */     {
/* 113:201 */       int earlierDuplicate = -1;
/* 114:202 */       for (int j = 0; (j < i) && (earlierDuplicate == -1); j++)
/* 115:    */       {
/* 116:203 */         ExtendedFormatRecord xfCheck = workbook.getWorkbook().getExFormatAt(j);
/* 117:204 */         if (xfCheck.equals(xfrs[i])) {
/* 118:205 */           earlierDuplicate = j;
/* 119:    */         }
/* 120:    */       }
/* 121:210 */       if (earlierDuplicate != -1)
/* 122:    */       {
/* 123:211 */         newPos[i] = ((short)earlierDuplicate);
/* 124:212 */         zapRecords[i] = true;
/* 125:    */       }
/* 126:215 */       if (earlierDuplicate != -1) {
/* 127:216 */         isUsed[earlierDuplicate] = true;
/* 128:    */       }
/* 129:    */     }
/* 130:222 */     for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++)
/* 131:    */     {
/* 132:223 */       HSSFSheet s = workbook.getSheetAt(sheetNum);
/* 133:224 */       for (Row row : s) {
/* 134:225 */         for (Cell cellI : row)
/* 135:    */         {
/* 136:226 */           HSSFCell cell = (HSSFCell)cellI;
/* 137:227 */           short oldXf = cell.getCellValueRecord().getXFIndex();
/* 138:228 */           isUsed[oldXf] = true;
/* 139:    */         }
/* 140:    */       }
/* 141:    */     }
/* 142:233 */     for (int i = 21; i < isUsed.length; i++) {
/* 143:234 */       if (isUsed[i] == 0)
/* 144:    */       {
/* 145:236 */         zapRecords[i] = true;
/* 146:237 */         newPos[i] = 0;
/* 147:    */       }
/* 148:    */     }
/* 149:245 */     for (int i = 21; i < newPos.length; i++)
/* 150:    */     {
/* 151:248 */       short preDeletePos = newPos[i];
/* 152:249 */       short newPosition = preDeletePos;
/* 153:250 */       for (int j = 0; j < preDeletePos; j++) {
/* 154:251 */         if (zapRecords[j] != 0) {
/* 155:251 */           newPosition = (short)(newPosition - 1);
/* 156:    */         }
/* 157:    */       }
/* 158:255 */       newPos[i] = newPosition;
/* 159:    */     }
/* 160:261 */     int max = newPos.length;
/* 161:262 */     int removed = 0;
/* 162:263 */     for (int i = 21; i < max; i++) {
/* 163:264 */       if (zapRecords[(i + removed)] != 0)
/* 164:    */       {
/* 165:265 */         workbook.getWorkbook().removeExFormatRecord(i);
/* 166:266 */         i--;
/* 167:267 */         max--;
/* 168:268 */         removed++;
/* 169:    */       }
/* 170:    */     }
/* 171:273 */     for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++)
/* 172:    */     {
/* 173:274 */       HSSFSheet s = workbook.getSheetAt(sheetNum);
/* 174:275 */       for (Row row : s) {
/* 175:276 */         for (Cell cellI : row)
/* 176:    */         {
/* 177:277 */           HSSFCell cell = (HSSFCell)cellI;
/* 178:278 */           short oldXf = cell.getCellValueRecord().getXFIndex();
/* 179:    */           
/* 180:280 */           HSSFCellStyle newStyle = workbook.getCellStyleAt(newPos[oldXf]);
/* 181:    */           
/* 182:    */ 
/* 183:283 */           cell.setCellStyle(newStyle);
/* 184:    */         }
/* 185:    */       }
/* 186:    */     }
/* 187:    */   }
/* 188:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFOptimiser
 * JD-Core Version:    0.7.0.1
 */