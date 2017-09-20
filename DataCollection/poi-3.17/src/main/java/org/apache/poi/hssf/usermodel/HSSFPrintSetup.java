/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.PrintSetupRecord;
/*   4:    */ import org.apache.poi.ss.usermodel.PrintSetup;
/*   5:    */ 
/*   6:    */ public class HSSFPrintSetup
/*   7:    */   implements PrintSetup
/*   8:    */ {
/*   9:    */   PrintSetupRecord printSetupRecord;
/*  10:    */   
/*  11:    */   protected HSSFPrintSetup(PrintSetupRecord printSetupRecord)
/*  12:    */   {
/*  13: 48 */     this.printSetupRecord = printSetupRecord;
/*  14:    */   }
/*  15:    */   
/*  16:    */   public void setPaperSize(short size)
/*  17:    */   {
/*  18: 56 */     this.printSetupRecord.setPaperSize(size);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public void setScale(short scale)
/*  22:    */   {
/*  23: 64 */     this.printSetupRecord.setScale(scale);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setPageStart(short start)
/*  27:    */   {
/*  28: 72 */     this.printSetupRecord.setPageStart(start);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setFitWidth(short width)
/*  32:    */   {
/*  33: 80 */     this.printSetupRecord.setFitWidth(width);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setFitHeight(short height)
/*  37:    */   {
/*  38: 88 */     this.printSetupRecord.setFitHeight(height);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setOptions(short options)
/*  42:    */   {
/*  43: 96 */     this.printSetupRecord.setOptions(options);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setLeftToRight(boolean ltor)
/*  47:    */   {
/*  48:104 */     this.printSetupRecord.setLeftToRight(ltor);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setLandscape(boolean ls)
/*  52:    */   {
/*  53:112 */     this.printSetupRecord.setLandscape(!ls);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setValidSettings(boolean valid)
/*  57:    */   {
/*  58:120 */     this.printSetupRecord.setValidSettings(valid);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setNoColor(boolean mono)
/*  62:    */   {
/*  63:128 */     this.printSetupRecord.setNoColor(mono);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void setDraft(boolean d)
/*  67:    */   {
/*  68:136 */     this.printSetupRecord.setDraft(d);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setNotes(boolean printnotes)
/*  72:    */   {
/*  73:144 */     this.printSetupRecord.setNotes(printnotes);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void setNoOrientation(boolean orientation)
/*  77:    */   {
/*  78:152 */     this.printSetupRecord.setNoOrientation(orientation);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setUsePage(boolean page)
/*  82:    */   {
/*  83:160 */     this.printSetupRecord.setUsePage(page);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setHResolution(short resolution)
/*  87:    */   {
/*  88:168 */     this.printSetupRecord.setHResolution(resolution);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setVResolution(short resolution)
/*  92:    */   {
/*  93:176 */     this.printSetupRecord.setVResolution(resolution);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void setHeaderMargin(double headermargin)
/*  97:    */   {
/*  98:184 */     this.printSetupRecord.setHeaderMargin(headermargin);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void setFooterMargin(double footermargin)
/* 102:    */   {
/* 103:192 */     this.printSetupRecord.setFooterMargin(footermargin);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setCopies(short copies)
/* 107:    */   {
/* 108:200 */     this.printSetupRecord.setCopies(copies);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public short getPaperSize()
/* 112:    */   {
/* 113:208 */     return this.printSetupRecord.getPaperSize();
/* 114:    */   }
/* 115:    */   
/* 116:    */   public short getScale()
/* 117:    */   {
/* 118:216 */     return this.printSetupRecord.getScale();
/* 119:    */   }
/* 120:    */   
/* 121:    */   public short getPageStart()
/* 122:    */   {
/* 123:224 */     return this.printSetupRecord.getPageStart();
/* 124:    */   }
/* 125:    */   
/* 126:    */   public short getFitWidth()
/* 127:    */   {
/* 128:232 */     return this.printSetupRecord.getFitWidth();
/* 129:    */   }
/* 130:    */   
/* 131:    */   public short getFitHeight()
/* 132:    */   {
/* 133:240 */     return this.printSetupRecord.getFitHeight();
/* 134:    */   }
/* 135:    */   
/* 136:    */   public short getOptions()
/* 137:    */   {
/* 138:248 */     return this.printSetupRecord.getOptions();
/* 139:    */   }
/* 140:    */   
/* 141:    */   public boolean getLeftToRight()
/* 142:    */   {
/* 143:256 */     return this.printSetupRecord.getLeftToRight();
/* 144:    */   }
/* 145:    */   
/* 146:    */   public boolean getLandscape()
/* 147:    */   {
/* 148:264 */     return !this.printSetupRecord.getLandscape();
/* 149:    */   }
/* 150:    */   
/* 151:    */   public boolean getValidSettings()
/* 152:    */   {
/* 153:272 */     return this.printSetupRecord.getValidSettings();
/* 154:    */   }
/* 155:    */   
/* 156:    */   public boolean getNoColor()
/* 157:    */   {
/* 158:280 */     return this.printSetupRecord.getNoColor();
/* 159:    */   }
/* 160:    */   
/* 161:    */   public boolean getDraft()
/* 162:    */   {
/* 163:288 */     return this.printSetupRecord.getDraft();
/* 164:    */   }
/* 165:    */   
/* 166:    */   public boolean getNotes()
/* 167:    */   {
/* 168:296 */     return this.printSetupRecord.getNotes();
/* 169:    */   }
/* 170:    */   
/* 171:    */   public boolean getNoOrientation()
/* 172:    */   {
/* 173:304 */     return this.printSetupRecord.getNoOrientation();
/* 174:    */   }
/* 175:    */   
/* 176:    */   public boolean getUsePage()
/* 177:    */   {
/* 178:312 */     return this.printSetupRecord.getUsePage();
/* 179:    */   }
/* 180:    */   
/* 181:    */   public short getHResolution()
/* 182:    */   {
/* 183:320 */     return this.printSetupRecord.getHResolution();
/* 184:    */   }
/* 185:    */   
/* 186:    */   public short getVResolution()
/* 187:    */   {
/* 188:328 */     return this.printSetupRecord.getVResolution();
/* 189:    */   }
/* 190:    */   
/* 191:    */   public double getHeaderMargin()
/* 192:    */   {
/* 193:336 */     return this.printSetupRecord.getHeaderMargin();
/* 194:    */   }
/* 195:    */   
/* 196:    */   public double getFooterMargin()
/* 197:    */   {
/* 198:344 */     return this.printSetupRecord.getFooterMargin();
/* 199:    */   }
/* 200:    */   
/* 201:    */   public short getCopies()
/* 202:    */   {
/* 203:352 */     return this.printSetupRecord.getCopies();
/* 204:    */   }
/* 205:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFPrintSetup
 * JD-Core Version:    0.7.0.1
 */