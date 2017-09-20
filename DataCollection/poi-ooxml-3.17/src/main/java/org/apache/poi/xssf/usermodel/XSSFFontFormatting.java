/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import org.apache.poi.ss.usermodel.Color;
/*   5:    */ import org.apache.poi.ss.usermodel.FontFormatting;
/*   6:    */ import org.apache.poi.ss.usermodel.FontUnderline;
/*   7:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty;
/*   8:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
/*   9:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont;
/*  10:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont.Factory;
/*  11:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize;
/*  12:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty;
/*  13:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty;
/*  14:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnderlineValues.Enum;
/*  15:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignRun.Enum;
/*  16:    */ 
/*  17:    */ public class XSSFFontFormatting
/*  18:    */   implements FontFormatting
/*  19:    */ {
/*  20:    */   private IndexedColorMap _colorMap;
/*  21:    */   private CTFont _font;
/*  22:    */   
/*  23:    */   XSSFFontFormatting(CTFont font, IndexedColorMap colorMap)
/*  24:    */   {
/*  25: 40 */     this._font = font;
/*  26: 41 */     this._colorMap = colorMap;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public short getEscapementType()
/*  30:    */   {
/*  31: 54 */     if (this._font.sizeOfVertAlignArray() == 0) {
/*  32: 54 */       return 0;
/*  33:    */     }
/*  34: 56 */     CTVerticalAlignFontProperty prop = this._font.getVertAlignArray(0);
/*  35: 57 */     return (short)(prop.getVal().intValue() - 1);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setEscapementType(short escapementType)
/*  39:    */   {
/*  40: 70 */     this._font.setVertAlignArray(null);
/*  41: 71 */     if (escapementType != 0) {
/*  42: 72 */       this._font.addNewVertAlign().setVal(STVerticalAlignRun.Enum.forInt(escapementType + 1));
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public boolean isStruckout()
/*  47:    */   {
/*  48: 84 */     CTBooleanProperty[] arr$ = this._font.getStrikeArray();int len$ = arr$.length;int i$ = 0;
/*  49: 84 */     if (i$ < len$)
/*  50:    */     {
/*  51: 84 */       CTBooleanProperty bProp = arr$[i$];return bProp.getVal();
/*  52:    */     }
/*  53: 85 */     return false;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public short getFontColorIndex()
/*  57:    */   {
/*  58: 93 */     if (this._font.sizeOfColorArray() == 0) {
/*  59: 93 */       return -1;
/*  60:    */     }
/*  61: 95 */     int idx = 0;
/*  62: 96 */     CTColor color = this._font.getColorArray(0);
/*  63: 97 */     if (color.isSetIndexed()) {
/*  64: 97 */       idx = (int)color.getIndexed();
/*  65:    */     }
/*  66: 98 */     return (short)idx;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setFontColorIndex(short color)
/*  70:    */   {
/*  71:106 */     this._font.setColorArray(null);
/*  72:107 */     if (color != -1) {
/*  73:108 */       this._font.addNewColor().setIndexed(color);
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public XSSFColor getFontColor()
/*  78:    */   {
/*  79:114 */     if (this._font.sizeOfColorArray() == 0) {
/*  80:114 */       return null;
/*  81:    */     }
/*  82:116 */     return new XSSFColor(this._font.getColorArray(0), this._colorMap);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setFontColor(Color color)
/*  86:    */   {
/*  87:121 */     XSSFColor xcolor = XSSFColor.toXSSFColor(color);
/*  88:122 */     if (xcolor == null) {
/*  89:123 */       this._font.getColorList().clear();
/*  90:    */     } else {
/*  91:125 */       this._font.setColorArray(0, xcolor.getCTColor());
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   public int getFontHeight()
/*  96:    */   {
/*  97:136 */     if (this._font.sizeOfSzArray() == 0) {
/*  98:136 */       return -1;
/*  99:    */     }
/* 100:138 */     CTFontSize sz = this._font.getSzArray(0);
/* 101:139 */     return (short)(int)(20.0D * sz.getVal());
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setFontHeight(int height)
/* 105:    */   {
/* 106:149 */     this._font.setSzArray(null);
/* 107:150 */     if (height != -1) {
/* 108:151 */       this._font.addNewSz().setVal(height / 20.0D);
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   public short getUnderlineType()
/* 113:    */   {
/* 114:168 */     if (this._font.sizeOfUArray() == 0) {
/* 115:168 */       return 0;
/* 116:    */     }
/* 117:169 */     CTUnderlineProperty u = this._font.getUArray(0);
/* 118:170 */     switch (u.getVal().intValue())
/* 119:    */     {
/* 120:    */     case 1: 
/* 121:171 */       return 1;
/* 122:    */     case 2: 
/* 123:172 */       return 2;
/* 124:    */     case 3: 
/* 125:173 */       return 33;
/* 126:    */     case 4: 
/* 127:174 */       return 34;
/* 128:    */     }
/* 129:175 */     return 0;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void setUnderlineType(short underlineType)
/* 133:    */   {
/* 134:192 */     this._font.setUArray(null);
/* 135:193 */     if (underlineType != 0)
/* 136:    */     {
/* 137:194 */       FontUnderline fenum = FontUnderline.valueOf(underlineType);
/* 138:195 */       STUnderlineValues.Enum val = STUnderlineValues.Enum.forInt(fenum.getValue());
/* 139:196 */       this._font.addNewU().setVal(val);
/* 140:    */     }
/* 141:    */   }
/* 142:    */   
/* 143:    */   public boolean isBold()
/* 144:    */   {
/* 145:207 */     return (this._font.sizeOfBArray() == 1) && (this._font.getBArray(0).getVal());
/* 146:    */   }
/* 147:    */   
/* 148:    */   public boolean isItalic()
/* 149:    */   {
/* 150:215 */     return (this._font.sizeOfIArray() == 1) && (this._font.getIArray(0).getVal());
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void setFontStyle(boolean italic, boolean bold)
/* 154:    */   {
/* 155:226 */     this._font.setIArray(null);
/* 156:227 */     this._font.setBArray(null);
/* 157:228 */     if (italic) {
/* 158:228 */       this._font.addNewI().setVal(true);
/* 159:    */     }
/* 160:229 */     if (bold) {
/* 161:229 */       this._font.addNewB().setVal(true);
/* 162:    */     }
/* 163:    */   }
/* 164:    */   
/* 165:    */   public void resetFontStyle()
/* 166:    */   {
/* 167:237 */     this._font.set(CTFont.Factory.newInstance());
/* 168:    */   }
/* 169:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFFontFormatting
 * JD-Core Version:    0.7.0.1
 */