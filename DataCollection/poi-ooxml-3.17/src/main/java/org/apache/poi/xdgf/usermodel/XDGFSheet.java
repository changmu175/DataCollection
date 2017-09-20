/*   1:    */ package org.apache.poi.xdgf.usermodel;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*   4:    */ import com.microsoft.schemas.office.visio.x2012.main.SectionType;
/*   5:    */ import com.microsoft.schemas.office.visio.x2012.main.SheetType;
/*   6:    */ import java.awt.Color;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.SortedMap;
/*  10:    */ import java.util.TreeMap;
/*  11:    */ import org.apache.poi.POIXMLException;
/*  12:    */ import org.apache.poi.xdgf.exceptions.XDGFException;
/*  13:    */ import org.apache.poi.xdgf.usermodel.section.CharacterSection;
/*  14:    */ import org.apache.poi.xdgf.usermodel.section.GeometrySection;
/*  15:    */ import org.apache.poi.xdgf.usermodel.section.XDGFSection;
/*  16:    */ 
/*  17:    */ public abstract class XDGFSheet
/*  18:    */ {
/*  19:    */   protected XDGFDocument _document;
/*  20:    */   protected SheetType _sheet;
/*  21: 46 */   protected Map<String, XDGFCell> _cells = new HashMap();
/*  22: 49 */   protected Map<String, XDGFSection> _sections = new HashMap();
/*  23: 52 */   protected SortedMap<Long, GeometrySection> _geometry = new TreeMap();
/*  24: 55 */   protected CharacterSection _character = null;
/*  25:    */   
/*  26:    */   public XDGFSheet(SheetType sheet, XDGFDocument document)
/*  27:    */   {
/*  28:    */     try
/*  29:    */     {
/*  30: 59 */       this._sheet = sheet;
/*  31: 60 */       this._document = document;
/*  32: 62 */       for (CellType cell : sheet.getCellArray())
/*  33:    */       {
/*  34: 63 */         if (this._cells.containsKey(cell.getN())) {
/*  35: 64 */           throw new POIXMLException("Unexpected duplicate cell " + cell.getN());
/*  36:    */         }
/*  37: 66 */         this._cells.put(cell.getN(), new XDGFCell(cell));
/*  38:    */       }
/*  39: 74 */       for (SectionType section : sheet.getSectionArray())
/*  40:    */       {
/*  41: 75 */         String name = section.getN();
/*  42: 76 */         if (name.equals("Geometry")) {
/*  43: 77 */           this._geometry.put(Long.valueOf(section.getIX()), new GeometrySection(section, this));
/*  44: 78 */         } else if (name.equals("Character")) {
/*  45: 79 */           this._character = new CharacterSection(section, this);
/*  46:    */         } else {
/*  47: 81 */           this._sections.put(name, XDGFSection.load(section, this));
/*  48:    */         }
/*  49:    */       }
/*  50:    */     }
/*  51:    */     catch (POIXMLException e)
/*  52:    */     {
/*  53: 85 */       throw XDGFException.wrap(toString(), e);
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   abstract SheetType getXmlObject();
/*  58:    */   
/*  59:    */   public XDGFDocument getDocument()
/*  60:    */   {
/*  61: 92 */     return this._document;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public XDGFCell getCell(String cellName)
/*  65:    */   {
/*  66:101 */     return (XDGFCell)this._cells.get(cellName);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public XDGFSection getSection(String sectionName)
/*  70:    */   {
/*  71:105 */     return (XDGFSection)this._sections.get(sectionName);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public XDGFStyleSheet getLineStyle()
/*  75:    */   {
/*  76:109 */     if (!this._sheet.isSetLineStyle()) {
/*  77:110 */       return null;
/*  78:    */     }
/*  79:112 */     return this._document.getStyleById(this._sheet.getLineStyle());
/*  80:    */   }
/*  81:    */   
/*  82:    */   public XDGFStyleSheet getFillStyle()
/*  83:    */   {
/*  84:116 */     if (!this._sheet.isSetFillStyle()) {
/*  85:117 */       return null;
/*  86:    */     }
/*  87:119 */     return this._document.getStyleById(this._sheet.getFillStyle());
/*  88:    */   }
/*  89:    */   
/*  90:    */   public XDGFStyleSheet getTextStyle()
/*  91:    */   {
/*  92:123 */     if (!this._sheet.isSetTextStyle()) {
/*  93:124 */       return null;
/*  94:    */     }
/*  95:126 */     return this._document.getStyleById(this._sheet.getTextStyle());
/*  96:    */   }
/*  97:    */   
/*  98:    */   public Color getFontColor()
/*  99:    */   {
/* 100:130 */     Color fontColor = null;
/* 101:132 */     if (this._character != null)
/* 102:    */     {
/* 103:133 */       fontColor = this._character.getFontColor();
/* 104:134 */       if (fontColor != null) {
/* 105:135 */         return fontColor;
/* 106:    */       }
/* 107:    */     }
/* 108:138 */     XDGFStyleSheet style = getTextStyle();
/* 109:139 */     if (style != null) {
/* 110:140 */       return style.getFontColor();
/* 111:    */     }
/* 112:142 */     return null;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public Double getFontSize()
/* 116:    */   {
/* 117:146 */     Double fontSize = null;
/* 118:148 */     if (this._character != null)
/* 119:    */     {
/* 120:149 */       fontSize = this._character.getFontSize();
/* 121:150 */       if (fontSize != null) {
/* 122:151 */         return fontSize;
/* 123:    */       }
/* 124:    */     }
/* 125:154 */     XDGFStyleSheet style = getTextStyle();
/* 126:155 */     if (style != null) {
/* 127:156 */       return style.getFontSize();
/* 128:    */     }
/* 129:158 */     return null;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public Integer getLineCap()
/* 133:    */   {
/* 134:162 */     Integer lineCap = XDGFCell.maybeGetInteger(this._cells, "LineCap");
/* 135:163 */     if (lineCap != null) {
/* 136:164 */       return lineCap;
/* 137:    */     }
/* 138:166 */     XDGFStyleSheet style = getLineStyle();
/* 139:167 */     if (style != null) {
/* 140:168 */       return style.getLineCap();
/* 141:    */     }
/* 142:170 */     return null;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public Color getLineColor()
/* 146:    */   {
/* 147:174 */     String lineColor = XDGFCell.maybeGetString(this._cells, "LineColor");
/* 148:175 */     if (lineColor != null) {
/* 149:176 */       return Color.decode(lineColor);
/* 150:    */     }
/* 151:178 */     XDGFStyleSheet style = getLineStyle();
/* 152:179 */     if (style != null) {
/* 153:180 */       return style.getLineColor();
/* 154:    */     }
/* 155:182 */     return null;
/* 156:    */   }
/* 157:    */   
/* 158:    */   public Integer getLinePattern()
/* 159:    */   {
/* 160:186 */     Integer linePattern = XDGFCell.maybeGetInteger(this._cells, "LinePattern");
/* 161:187 */     if (linePattern != null) {
/* 162:188 */       return linePattern;
/* 163:    */     }
/* 164:190 */     XDGFStyleSheet style = getLineStyle();
/* 165:191 */     if (style != null) {
/* 166:192 */       return style.getLinePattern();
/* 167:    */     }
/* 168:194 */     return null;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public Double getLineWeight()
/* 172:    */   {
/* 173:198 */     Double lineWeight = XDGFCell.maybeGetDouble(this._cells, "LineWeight");
/* 174:199 */     if (lineWeight != null) {
/* 175:200 */       return lineWeight;
/* 176:    */     }
/* 177:202 */     XDGFStyleSheet style = getLineStyle();
/* 178:203 */     if (style != null) {
/* 179:204 */       return style.getLineWeight();
/* 180:    */     }
/* 181:206 */     return null;
/* 182:    */   }
/* 183:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFSheet
 * JD-Core Version:    0.7.0.1
 */