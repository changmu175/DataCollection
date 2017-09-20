/*   1:    */ package org.apache.poi.hssf.model;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.hssf.record.Record;
/*   6:    */ 
/*   7:    */ public final class WorkbookRecordList
/*   8:    */ {
/*   9: 26 */   private List<Record> records = new ArrayList();
/*  10: 29 */   private int protpos = 0;
/*  11: 31 */   private int bspos = 0;
/*  12: 33 */   private int tabpos = 0;
/*  13: 35 */   private int fontpos = 0;
/*  14: 37 */   private int xfpos = 0;
/*  15: 39 */   private int backuppos = 0;
/*  16: 41 */   private int namepos = 0;
/*  17: 43 */   private int supbookpos = 0;
/*  18: 45 */   private int externsheetPos = 0;
/*  19: 47 */   private int palettepos = -1;
/*  20:    */   
/*  21:    */   public void setRecords(List<Record> records)
/*  22:    */   {
/*  23: 51 */     this.records = records;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public int size()
/*  27:    */   {
/*  28: 55 */     return this.records.size();
/*  29:    */   }
/*  30:    */   
/*  31:    */   public Record get(int i)
/*  32:    */   {
/*  33: 59 */     return (Record)this.records.get(i);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void add(int pos, Record r)
/*  37:    */   {
/*  38: 63 */     this.records.add(pos, r);
/*  39: 64 */     updateRecordPos(pos, true);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public List<Record> getRecords()
/*  43:    */   {
/*  44: 68 */     return this.records;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void remove(Object record)
/*  48:    */   {
/*  49: 78 */     int i = 0;
/*  50: 79 */     for (Record r : this.records)
/*  51:    */     {
/*  52: 80 */       if (r == record)
/*  53:    */       {
/*  54: 81 */         remove(i);
/*  55: 82 */         break;
/*  56:    */       }
/*  57: 84 */       i++;
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void remove(int pos)
/*  62:    */   {
/*  63: 89 */     this.records.remove(pos);
/*  64: 90 */     updateRecordPos(pos, false);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public int getProtpos()
/*  68:    */   {
/*  69: 94 */     return this.protpos;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setProtpos(int protpos)
/*  73:    */   {
/*  74: 98 */     this.protpos = protpos;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public int getBspos()
/*  78:    */   {
/*  79:102 */     return this.bspos;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void setBspos(int bspos)
/*  83:    */   {
/*  84:106 */     this.bspos = bspos;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public int getTabpos()
/*  88:    */   {
/*  89:110 */     return this.tabpos;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setTabpos(int tabpos)
/*  93:    */   {
/*  94:114 */     this.tabpos = tabpos;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public int getFontpos()
/*  98:    */   {
/*  99:118 */     return this.fontpos;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setFontpos(int fontpos)
/* 103:    */   {
/* 104:122 */     this.fontpos = fontpos;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public int getXfpos()
/* 108:    */   {
/* 109:126 */     return this.xfpos;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void setXfpos(int xfpos)
/* 113:    */   {
/* 114:130 */     this.xfpos = xfpos;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public int getBackuppos()
/* 118:    */   {
/* 119:134 */     return this.backuppos;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setBackuppos(int backuppos)
/* 123:    */   {
/* 124:138 */     this.backuppos = backuppos;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public int getPalettepos()
/* 128:    */   {
/* 129:142 */     return this.palettepos;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void setPalettepos(int palettepos)
/* 133:    */   {
/* 134:146 */     this.palettepos = palettepos;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public int getNamepos()
/* 138:    */   {
/* 139:155 */     return this.namepos;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public int getSupbookpos()
/* 143:    */   {
/* 144:163 */     return this.supbookpos;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public void setNamepos(int namepos)
/* 148:    */   {
/* 149:171 */     this.namepos = namepos;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void setSupbookpos(int supbookpos)
/* 153:    */   {
/* 154:179 */     this.supbookpos = supbookpos;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public int getExternsheetPos()
/* 158:    */   {
/* 159:187 */     return this.externsheetPos;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void setExternsheetPos(int externsheetPos)
/* 163:    */   {
/* 164:195 */     this.externsheetPos = externsheetPos;
/* 165:    */   }
/* 166:    */   
/* 167:    */   private void updateRecordPos(int pos, boolean add)
/* 168:    */   {
/* 169:199 */     int delta = add ? 1 : -1;
/* 170:200 */     int p = getProtpos();
/* 171:201 */     if (p >= pos) {
/* 172:202 */       setProtpos(p + delta);
/* 173:    */     }
/* 174:204 */     p = getBspos();
/* 175:205 */     if (p >= pos) {
/* 176:206 */       setBspos(p + delta);
/* 177:    */     }
/* 178:208 */     p = getTabpos();
/* 179:209 */     if (p >= pos) {
/* 180:210 */       setTabpos(p + delta);
/* 181:    */     }
/* 182:212 */     p = getFontpos();
/* 183:213 */     if (p >= pos) {
/* 184:214 */       setFontpos(p + delta);
/* 185:    */     }
/* 186:216 */     p = getXfpos();
/* 187:217 */     if (p >= pos) {
/* 188:218 */       setXfpos(p + delta);
/* 189:    */     }
/* 190:220 */     p = getBackuppos();
/* 191:221 */     if (p >= pos) {
/* 192:222 */       setBackuppos(p + delta);
/* 193:    */     }
/* 194:224 */     p = getNamepos();
/* 195:225 */     if (p >= pos) {
/* 196:226 */       setNamepos(p + delta);
/* 197:    */     }
/* 198:228 */     p = getSupbookpos();
/* 199:229 */     if (p >= pos) {
/* 200:230 */       setSupbookpos(p + delta);
/* 201:    */     }
/* 202:232 */     p = getPalettepos();
/* 203:233 */     if ((p != -1) && (p >= pos)) {
/* 204:234 */       setPalettepos(p + delta);
/* 205:    */     }
/* 206:236 */     p = getExternsheetPos();
/* 207:237 */     if (p >= pos) {
/* 208:238 */       setExternsheetPos(p + delta);
/* 209:    */     }
/* 210:    */   }
/* 211:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.model.WorkbookRecordList
 * JD-Core Version:    0.7.0.1
 */