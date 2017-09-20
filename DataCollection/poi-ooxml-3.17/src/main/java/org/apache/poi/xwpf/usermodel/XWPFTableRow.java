/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import java.math.BigInteger;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.util.Internal;
/*   7:    */ import org.apache.poi.xwpf.model.WMLHelper;
/*   8:    */ import org.apache.xmlbeans.XmlCursor;
/*   9:    */ import org.apache.xmlbeans.XmlObject;
/*  10:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
/*  11:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
/*  12:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
/*  13:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell;
/*  14:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
/*  15:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
/*  16:    */ 
/*  17:    */ public class XWPFTableRow
/*  18:    */ {
/*  19:    */   private CTRow ctRow;
/*  20:    */   private XWPFTable table;
/*  21:    */   private List<XWPFTableCell> tableCells;
/*  22:    */   
/*  23:    */   public XWPFTableRow(CTRow row, XWPFTable table)
/*  24:    */   {
/*  25: 46 */     this.table = table;
/*  26: 47 */     this.ctRow = row;
/*  27: 48 */     getTableCells();
/*  28:    */   }
/*  29:    */   
/*  30:    */   @Internal
/*  31:    */   public CTRow getCtRow()
/*  32:    */   {
/*  33: 53 */     return this.ctRow;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public XWPFTableCell createCell()
/*  37:    */   {
/*  38: 62 */     XWPFTableCell tableCell = new XWPFTableCell(this.ctRow.addNewTc(), this, this.table.getBody());
/*  39: 63 */     this.tableCells.add(tableCell);
/*  40: 64 */     return tableCell;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public XWPFTableCell getCell(int pos)
/*  44:    */   {
/*  45: 68 */     if ((pos >= 0) && (pos < this.ctRow.sizeOfTcArray())) {
/*  46: 69 */       return (XWPFTableCell)getTableCells().get(pos);
/*  47:    */     }
/*  48: 71 */     return null;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void removeCell(int pos)
/*  52:    */   {
/*  53: 75 */     if ((pos >= 0) && (pos < this.ctRow.sizeOfTcArray())) {
/*  54: 76 */       this.tableCells.remove(pos);
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   public XWPFTableCell addNewTableCell()
/*  59:    */   {
/*  60: 84 */     CTTc cell = this.ctRow.addNewTc();
/*  61: 85 */     XWPFTableCell tableCell = new XWPFTableCell(cell, this, this.table.getBody());
/*  62: 86 */     this.tableCells.add(tableCell);
/*  63: 87 */     return tableCell;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public int getHeight()
/*  67:    */   {
/*  68:101 */     CTTrPr properties = getTrPr();
/*  69:102 */     return properties.sizeOfTrHeightArray() == 0 ? 0 : properties.getTrHeightArray(0).getVal().intValue();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setHeight(int height)
/*  73:    */   {
/*  74:116 */     CTTrPr properties = getTrPr();
/*  75:117 */     CTHeight h = properties.sizeOfTrHeightArray() == 0 ? properties.addNewTrHeight() : properties.getTrHeightArray(0);
/*  76:118 */     h.setVal(new BigInteger("" + height));
/*  77:    */   }
/*  78:    */   
/*  79:    */   private CTTrPr getTrPr()
/*  80:    */   {
/*  81:122 */     return this.ctRow.isSetTrPr() ? this.ctRow.getTrPr() : this.ctRow.addNewTrPr();
/*  82:    */   }
/*  83:    */   
/*  84:    */   public XWPFTable getTable()
/*  85:    */   {
/*  86:126 */     return this.table;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public List<ICell> getTableICells()
/*  90:    */   {
/*  91:137 */     List<ICell> cells = new ArrayList();
/*  92:    */     
/*  93:    */ 
/*  94:140 */     XmlCursor cursor = this.ctRow.newCursor();
/*  95:141 */     cursor.selectPath("./*");
/*  96:142 */     while (cursor.toNextSelection())
/*  97:    */     {
/*  98:143 */       XmlObject o = cursor.getObject();
/*  99:144 */       if ((o instanceof CTTc)) {
/* 100:145 */         cells.add(new XWPFTableCell((CTTc)o, this, this.table.getBody()));
/* 101:146 */       } else if ((o instanceof CTSdtCell)) {
/* 102:147 */         cells.add(new XWPFSDTCell((CTSdtCell)o, this, this.table.getBody()));
/* 103:    */       }
/* 104:    */     }
/* 105:150 */     cursor.dispose();
/* 106:151 */     return cells;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public List<XWPFTableCell> getTableCells()
/* 110:    */   {
/* 111:161 */     if (this.tableCells == null)
/* 112:    */     {
/* 113:162 */       List<XWPFTableCell> cells = new ArrayList();
/* 114:163 */       for (CTTc tableCell : this.ctRow.getTcArray()) {
/* 115:164 */         cells.add(new XWPFTableCell(tableCell, this, this.table.getBody()));
/* 116:    */       }
/* 117:169 */       this.tableCells = cells;
/* 118:    */     }
/* 119:171 */     return this.tableCells;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public XWPFTableCell getTableCell(CTTc cell)
/* 123:    */   {
/* 124:179 */     for (int i = 0; i < this.tableCells.size(); i++) {
/* 125:180 */       if (((XWPFTableCell)this.tableCells.get(i)).getCTTc() == cell) {
/* 126:181 */         return (XWPFTableCell)this.tableCells.get(i);
/* 127:    */       }
/* 128:    */     }
/* 129:183 */     return null;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public boolean isCantSplitRow()
/* 133:    */   {
/* 134:194 */     boolean isCant = false;
/* 135:195 */     if (this.ctRow.isSetTrPr())
/* 136:    */     {
/* 137:196 */       CTTrPr trpr = getTrPr();
/* 138:197 */       if (trpr.sizeOfCantSplitArray() > 0)
/* 139:    */       {
/* 140:198 */         CTOnOff onoff = trpr.getCantSplitArray(0);
/* 141:199 */         isCant = onoff.isSetVal() ? WMLHelper.convertSTOnOffToBoolean(onoff.getVal()) : true;
/* 142:    */       }
/* 143:    */     }
/* 144:202 */     return isCant;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public void setCantSplitRow(boolean split)
/* 148:    */   {
/* 149:214 */     CTTrPr trpr = getTrPr();
/* 150:215 */     CTOnOff onoff = trpr.sizeOfCantSplitArray() > 0 ? trpr.getCantSplitArray(0) : trpr.addNewCantSplit();
/* 151:216 */     onoff.setVal(WMLHelper.convertBooleanToSTOnOff(split));
/* 152:    */   }
/* 153:    */   
/* 154:    */   public boolean isRepeatHeader()
/* 155:    */   {
/* 156:230 */     boolean repeat = false;
/* 157:231 */     for (XWPFTableRow row : this.table.getRows())
/* 158:    */     {
/* 159:232 */       repeat = row.getRepeat();
/* 160:233 */       if ((row == this) || (!repeat)) {
/* 161:    */         break;
/* 162:    */       }
/* 163:    */     }
/* 164:237 */     return repeat;
/* 165:    */   }
/* 166:    */   
/* 167:    */   private boolean getRepeat()
/* 168:    */   {
/* 169:241 */     boolean repeat = false;
/* 170:242 */     if (this.ctRow.isSetTrPr())
/* 171:    */     {
/* 172:243 */       CTTrPr trpr = getTrPr();
/* 173:244 */       if (trpr.sizeOfTblHeaderArray() > 0)
/* 174:    */       {
/* 175:245 */         CTOnOff rpt = trpr.getTblHeaderArray(0);
/* 176:246 */         repeat = rpt.isSetVal() ? WMLHelper.convertSTOnOffToBoolean(rpt.getVal()) : true;
/* 177:    */       }
/* 178:    */     }
/* 179:249 */     return repeat;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public void setRepeatHeader(boolean repeat)
/* 183:    */   {
/* 184:261 */     CTTrPr trpr = getTrPr();
/* 185:262 */     CTOnOff onoff = trpr.sizeOfTblHeaderArray() > 0 ? trpr.getTblHeaderArray(0) : trpr.addNewTblHeader();
/* 186:263 */     onoff.setVal(WMLHelper.convertBooleanToSTOnOff(repeat));
/* 187:    */   }
/* 188:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFTableRow
 * JD-Core Version:    0.7.0.1
 */