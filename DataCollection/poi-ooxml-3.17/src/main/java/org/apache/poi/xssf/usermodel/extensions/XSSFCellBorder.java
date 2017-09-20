/*   1:    */ package org.apache.poi.xssf.usermodel.extensions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.BorderStyle;
/*   4:    */ import org.apache.poi.util.Internal;
/*   5:    */ import org.apache.poi.xssf.model.ThemesTable;
/*   6:    */ import org.apache.poi.xssf.usermodel.IndexedColorMap;
/*   7:    */ import org.apache.poi.xssf.usermodel.XSSFColor;
/*   8:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder;
/*   9:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder.Factory;
/*  10:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr;
/*  11:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderStyle;
/*  12:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderStyle.Enum;
/*  13:    */ 
/*  14:    */ public class XSSFCellBorder
/*  15:    */ {
/*  16:    */   private IndexedColorMap _indexedColorMap;
/*  17:    */   private ThemesTable _theme;
/*  18:    */   private CTBorder border;
/*  19:    */   
/*  20:    */   public XSSFCellBorder(CTBorder border, ThemesTable theme, IndexedColorMap colorMap)
/*  21:    */   {
/*  22: 46 */     this(border, colorMap);
/*  23: 47 */     this._theme = theme;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public XSSFCellBorder(CTBorder border)
/*  27:    */   {
/*  28: 55 */     this(border, null);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public XSSFCellBorder(CTBorder border, IndexedColorMap colorMap)
/*  32:    */   {
/*  33: 64 */     this.border = border;
/*  34: 65 */     this._indexedColorMap = colorMap;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public XSSFCellBorder()
/*  38:    */   {
/*  39: 73 */     this.border = CTBorder.Factory.newInstance();
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setThemesTable(ThemesTable themes)
/*  43:    */   {
/*  44: 82 */     this._theme = themes;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static enum BorderSide
/*  48:    */   {
/*  49: 89 */     TOP,  RIGHT,  BOTTOM,  LEFT;
/*  50:    */     
/*  51:    */     private BorderSide() {}
/*  52:    */   }
/*  53:    */   
/*  54:    */   @Internal
/*  55:    */   public CTBorder getCTBorder()
/*  56:    */   {
/*  57: 99 */     return this.border;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public BorderStyle getBorderStyle(BorderSide side)
/*  61:    */   {
/*  62:110 */     CTBorderPr ctBorder = getBorder(side);
/*  63:111 */     STBorderStyle.Enum border = ctBorder == null ? STBorderStyle.NONE : ctBorder.getStyle();
/*  64:112 */     return BorderStyle.values()[(border.intValue() - 1)];
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setBorderStyle(BorderSide side, BorderStyle style)
/*  68:    */   {
/*  69:123 */     getBorder(side, true).setStyle(STBorderStyle.Enum.forInt(style.ordinal() + 1));
/*  70:    */   }
/*  71:    */   
/*  72:    */   public XSSFColor getBorderColor(BorderSide side)
/*  73:    */   {
/*  74:133 */     CTBorderPr borderPr = getBorder(side);
/*  75:135 */     if ((borderPr != null) && (borderPr.isSetColor()))
/*  76:    */     {
/*  77:136 */       XSSFColor clr = new XSSFColor(borderPr.getColor(), this._indexedColorMap);
/*  78:137 */       if (this._theme != null) {
/*  79:138 */         this._theme.inheritFromThemeAsRequired(clr);
/*  80:    */       }
/*  81:140 */       return clr;
/*  82:    */     }
/*  83:143 */     return null;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setBorderColor(BorderSide side, XSSFColor color)
/*  87:    */   {
/*  88:154 */     CTBorderPr borderPr = getBorder(side, true);
/*  89:155 */     if (color == null) {
/*  90:155 */       borderPr.unsetColor();
/*  91:    */     } else {
/*  92:157 */       borderPr.setColor(color.getCTColor());
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   private CTBorderPr getBorder(BorderSide side)
/*  97:    */   {
/*  98:161 */     return getBorder(side, false);
/*  99:    */   }
/* 100:    */   
/* 101:    */   private CTBorderPr getBorder(BorderSide side, boolean ensure)
/* 102:    */   {
/* 103:    */     CTBorderPr borderPr;
/* 104:167 */     switch (1.$SwitchMap$org$apache$poi$xssf$usermodel$extensions$XSSFCellBorder$BorderSide[side.ordinal()])
/* 105:    */     {
/* 106:    */     case 1: 
/* 107:169 */       borderPr = this.border.getTop();
/* 108:170 */       if ((ensure) && (borderPr == null)) {
/* 109:170 */         borderPr = this.border.addNewTop();
/* 110:    */       }
/* 111:    */       break;
/* 112:    */     case 2: 
/* 113:173 */       borderPr = this.border.getRight();
/* 114:174 */       if ((ensure) && (borderPr == null)) {
/* 115:174 */         borderPr = this.border.addNewRight();
/* 116:    */       }
/* 117:    */       break;
/* 118:    */     case 3: 
/* 119:177 */       borderPr = this.border.getBottom();
/* 120:178 */       if ((ensure) && (borderPr == null)) {
/* 121:178 */         borderPr = this.border.addNewBottom();
/* 122:    */       }
/* 123:    */       break;
/* 124:    */     case 4: 
/* 125:181 */       borderPr = this.border.getLeft();
/* 126:182 */       if ((ensure) && (borderPr == null)) {
/* 127:182 */         borderPr = this.border.addNewLeft();
/* 128:    */       }
/* 129:    */       break;
/* 130:    */     default: 
/* 131:185 */       throw new IllegalArgumentException("No suitable side specified for the border");
/* 132:    */     }
/* 133:187 */     return borderPr;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public int hashCode()
/* 137:    */   {
/* 138:192 */     return this.border.toString().hashCode();
/* 139:    */   }
/* 140:    */   
/* 141:    */   public boolean equals(Object o)
/* 142:    */   {
/* 143:196 */     if (!(o instanceof XSSFCellBorder)) {
/* 144:196 */       return false;
/* 145:    */     }
/* 146:198 */     XSSFCellBorder cf = (XSSFCellBorder)o;
/* 147:199 */     return this.border.toString().equals(cf.getCTBorder().toString());
/* 148:    */   }
/* 149:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder
 * JD-Core Version:    0.7.0.1
 */