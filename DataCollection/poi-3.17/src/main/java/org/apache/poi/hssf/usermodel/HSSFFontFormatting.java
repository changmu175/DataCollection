/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.CFRuleBase;
/*   4:    */ import org.apache.poi.hssf.util.HSSFColor;
/*   5:    */ import org.apache.poi.ss.usermodel.Color;
/*   6:    */ 
/*   7:    */ public final class HSSFFontFormatting
/*   8:    */   implements org.apache.poi.ss.usermodel.FontFormatting
/*   9:    */ {
/*  10:    */   public static final byte U_NONE = 0;
/*  11:    */   public static final byte U_SINGLE = 1;
/*  12:    */   public static final byte U_DOUBLE = 2;
/*  13:    */   public static final byte U_SINGLE_ACCOUNTING = 33;
/*  14:    */   public static final byte U_DOUBLE_ACCOUNTING = 34;
/*  15:    */   private final org.apache.poi.hssf.record.cf.FontFormatting fontFormatting;
/*  16:    */   private final HSSFWorkbook workbook;
/*  17:    */   
/*  18:    */   protected HSSFFontFormatting(CFRuleBase cfRuleRecord, HSSFWorkbook workbook)
/*  19:    */   {
/*  20: 44 */     this.fontFormatting = cfRuleRecord.getFontFormatting();
/*  21: 45 */     this.workbook = workbook;
/*  22:    */   }
/*  23:    */   
/*  24:    */   protected org.apache.poi.hssf.record.cf.FontFormatting getFontFormattingBlock()
/*  25:    */   {
/*  26: 49 */     return this.fontFormatting;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public short getEscapementType()
/*  30:    */   {
/*  31: 62 */     return this.fontFormatting.getEscapementType();
/*  32:    */   }
/*  33:    */   
/*  34:    */   public short getFontColorIndex()
/*  35:    */   {
/*  36: 70 */     return this.fontFormatting.getFontColorIndex();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public HSSFColor getFontColor()
/*  40:    */   {
/*  41: 74 */     return this.workbook.getCustomPalette().getColor(getFontColorIndex());
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setFontColor(Color color)
/*  45:    */   {
/*  46: 80 */     HSSFColor hcolor = HSSFColor.toHSSFColor(color);
/*  47: 81 */     if (hcolor == null) {
/*  48: 82 */       this.fontFormatting.setFontColorIndex((short)0);
/*  49:    */     } else {
/*  50: 84 */       this.fontFormatting.setFontColorIndex(hcolor.getIndex());
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   public int getFontHeight()
/*  55:    */   {
/*  56: 94 */     return this.fontFormatting.getFontHeight();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public short getFontWeight()
/*  60:    */   {
/*  61:104 */     return this.fontFormatting.getFontWeight();
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected byte[] getRawRecord()
/*  65:    */   {
/*  66:111 */     return this.fontFormatting.getRawRecord();
/*  67:    */   }
/*  68:    */   
/*  69:    */   public short getUnderlineType()
/*  70:    */   {
/*  71:127 */     return this.fontFormatting.getUnderlineType();
/*  72:    */   }
/*  73:    */   
/*  74:    */   public boolean isBold()
/*  75:    */   {
/*  76:137 */     return (this.fontFormatting.isFontWeightModified()) && (this.fontFormatting.isBold());
/*  77:    */   }
/*  78:    */   
/*  79:    */   public boolean isEscapementTypeModified()
/*  80:    */   {
/*  81:145 */     return this.fontFormatting.isEscapementTypeModified();
/*  82:    */   }
/*  83:    */   
/*  84:    */   public boolean isFontCancellationModified()
/*  85:    */   {
/*  86:153 */     return this.fontFormatting.isFontCancellationModified();
/*  87:    */   }
/*  88:    */   
/*  89:    */   public boolean isFontOutlineModified()
/*  90:    */   {
/*  91:161 */     return this.fontFormatting.isFontOutlineModified();
/*  92:    */   }
/*  93:    */   
/*  94:    */   public boolean isFontShadowModified()
/*  95:    */   {
/*  96:169 */     return this.fontFormatting.isFontShadowModified();
/*  97:    */   }
/*  98:    */   
/*  99:    */   public boolean isFontStyleModified()
/* 100:    */   {
/* 101:177 */     return this.fontFormatting.isFontStyleModified();
/* 102:    */   }
/* 103:    */   
/* 104:    */   public boolean isItalic()
/* 105:    */   {
/* 106:185 */     return (this.fontFormatting.isFontStyleModified()) && (this.fontFormatting.isItalic());
/* 107:    */   }
/* 108:    */   
/* 109:    */   public boolean isOutlineOn()
/* 110:    */   {
/* 111:193 */     return (this.fontFormatting.isFontOutlineModified()) && (this.fontFormatting.isOutlineOn());
/* 112:    */   }
/* 113:    */   
/* 114:    */   public boolean isShadowOn()
/* 115:    */   {
/* 116:201 */     return (this.fontFormatting.isFontOutlineModified()) && (this.fontFormatting.isShadowOn());
/* 117:    */   }
/* 118:    */   
/* 119:    */   public boolean isStruckout()
/* 120:    */   {
/* 121:209 */     return (this.fontFormatting.isFontCancellationModified()) && (this.fontFormatting.isStruckout());
/* 122:    */   }
/* 123:    */   
/* 124:    */   public boolean isUnderlineTypeModified()
/* 125:    */   {
/* 126:217 */     return this.fontFormatting.isUnderlineTypeModified();
/* 127:    */   }
/* 128:    */   
/* 129:    */   public boolean isFontWeightModified()
/* 130:    */   {
/* 131:225 */     return this.fontFormatting.isFontWeightModified();
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void setFontStyle(boolean italic, boolean bold)
/* 135:    */   {
/* 136:237 */     boolean modified = (italic) || (bold);
/* 137:238 */     this.fontFormatting.setItalic(italic);
/* 138:239 */     this.fontFormatting.setBold(bold);
/* 139:240 */     this.fontFormatting.setFontStyleModified(modified);
/* 140:241 */     this.fontFormatting.setFontWieghtModified(modified);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public void resetFontStyle()
/* 144:    */   {
/* 145:249 */     setFontStyle(false, false);
/* 146:    */   }
/* 147:    */   
/* 148:    */   public void setEscapementType(short escapementType)
/* 149:    */   {
/* 150:261 */     switch (escapementType)
/* 151:    */     {
/* 152:    */     case 1: 
/* 153:    */     case 2: 
/* 154:264 */       this.fontFormatting.setEscapementType(escapementType);
/* 155:265 */       this.fontFormatting.setEscapementTypeModified(true);
/* 156:266 */       break;
/* 157:    */     case 0: 
/* 158:268 */       this.fontFormatting.setEscapementType(escapementType);
/* 159:269 */       this.fontFormatting.setEscapementTypeModified(false);
/* 160:270 */       break;
/* 161:    */     }
/* 162:    */   }
/* 163:    */   
/* 164:    */   public void setEscapementTypeModified(boolean modified)
/* 165:    */   {
/* 166:280 */     this.fontFormatting.setEscapementTypeModified(modified);
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void setFontCancellationModified(boolean modified)
/* 170:    */   {
/* 171:289 */     this.fontFormatting.setFontCancellationModified(modified);
/* 172:    */   }
/* 173:    */   
/* 174:    */   public void setFontColorIndex(short fci)
/* 175:    */   {
/* 176:298 */     this.fontFormatting.setFontColorIndex(fci);
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void setFontHeight(int height)
/* 180:    */   {
/* 181:307 */     this.fontFormatting.setFontHeight(height);
/* 182:    */   }
/* 183:    */   
/* 184:    */   public void setFontOutlineModified(boolean modified)
/* 185:    */   {
/* 186:316 */     this.fontFormatting.setFontOutlineModified(modified);
/* 187:    */   }
/* 188:    */   
/* 189:    */   public void setFontShadowModified(boolean modified)
/* 190:    */   {
/* 191:325 */     this.fontFormatting.setFontShadowModified(modified);
/* 192:    */   }
/* 193:    */   
/* 194:    */   public void setFontStyleModified(boolean modified)
/* 195:    */   {
/* 196:334 */     this.fontFormatting.setFontStyleModified(modified);
/* 197:    */   }
/* 198:    */   
/* 199:    */   public void setOutline(boolean on)
/* 200:    */   {
/* 201:343 */     this.fontFormatting.setOutline(on);
/* 202:344 */     this.fontFormatting.setFontOutlineModified(on);
/* 203:    */   }
/* 204:    */   
/* 205:    */   public void setShadow(boolean on)
/* 206:    */   {
/* 207:353 */     this.fontFormatting.setShadow(on);
/* 208:354 */     this.fontFormatting.setFontShadowModified(on);
/* 209:    */   }
/* 210:    */   
/* 211:    */   public void setStrikeout(boolean strike)
/* 212:    */   {
/* 213:363 */     this.fontFormatting.setStrikeout(strike);
/* 214:364 */     this.fontFormatting.setFontCancellationModified(strike);
/* 215:    */   }
/* 216:    */   
/* 217:    */   public void setUnderlineType(short underlineType)
/* 218:    */   {
/* 219:379 */     switch (underlineType)
/* 220:    */     {
/* 221:    */     case 1: 
/* 222:    */     case 2: 
/* 223:    */     case 33: 
/* 224:    */     case 34: 
/* 225:384 */       this.fontFormatting.setUnderlineType(underlineType);
/* 226:385 */       setUnderlineTypeModified(true);
/* 227:386 */       break;
/* 228:    */     case 0: 
/* 229:389 */       this.fontFormatting.setUnderlineType(underlineType);
/* 230:390 */       setUnderlineTypeModified(false);
/* 231:391 */       break;
/* 232:    */     }
/* 233:    */   }
/* 234:    */   
/* 235:    */   public void setUnderlineTypeModified(boolean modified)
/* 236:    */   {
/* 237:402 */     this.fontFormatting.setUnderlineTypeModified(modified);
/* 238:    */   }
/* 239:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFFontFormatting
 * JD-Core Version:    0.7.0.1
 */