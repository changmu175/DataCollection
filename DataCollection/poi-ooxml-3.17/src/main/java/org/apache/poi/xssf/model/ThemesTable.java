/*   1:    */ package org.apache.poi.xssf.model;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import org.apache.poi.POIXMLDocumentPart;
/*   6:    */ import org.apache.poi.POIXMLTypeLoader;
/*   7:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   8:    */ import org.apache.poi.xssf.usermodel.IndexedColorMap;
/*   9:    */ import org.apache.poi.xssf.usermodel.XSSFColor;
/*  10:    */ import org.apache.xmlbeans.XmlException;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument.Factory;
/*  18:    */ 
/*  19:    */ public class ThemesTable
/*  20:    */   extends POIXMLDocumentPart
/*  21:    */ {
/*  22:    */   private IndexedColorMap colorMap;
/*  23:    */   private ThemeDocument theme;
/*  24:    */   
/*  25:    */   public static enum ThemeElement
/*  26:    */   {
/*  27: 39 */     LT1(0, "Lt1"),  DK1(1, "Dk1"),  LT2(2, "Lt2"),  DK2(3, "Dk2"),  ACCENT1(4, "Accent1"),  ACCENT2(5, "Accent2"),  ACCENT3(6, "Accent3"),  ACCENT4(7, "Accent4"),  ACCENT5(8, "Accent5"),  ACCENT6(9, "Accent6"),  HLINK(10, "Hlink"),  FOLHLINK(11, "FolHlink"),  UNKNOWN(-1, null);
/*  28:    */     
/*  29:    */     public final int idx;
/*  30:    */     public final String name;
/*  31:    */     
/*  32:    */     public static ThemeElement byId(int idx)
/*  33:    */     {
/*  34: 54 */       if ((idx >= values().length) || (idx < 0)) {
/*  35: 54 */         return UNKNOWN;
/*  36:    */       }
/*  37: 55 */       return values()[idx];
/*  38:    */     }
/*  39:    */     
/*  40:    */     private ThemeElement(int idx, String name)
/*  41:    */     {
/*  42: 58 */       this.idx = idx;this.name = name;
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public ThemesTable()
/*  47:    */   {
/*  48: 72 */     this.theme = ThemeDocument.Factory.newInstance();
/*  49: 73 */     this.theme.addNewTheme().addNewThemeElements();
/*  50:    */   }
/*  51:    */   
/*  52:    */   public ThemesTable(PackagePart part)
/*  53:    */     throws IOException
/*  54:    */   {
/*  55: 83 */     super(part);
/*  56:    */     try
/*  57:    */     {
/*  58: 86 */       this.theme = ThemeDocument.Factory.parse(part.getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  59:    */     }
/*  60:    */     catch (XmlException e)
/*  61:    */     {
/*  62: 88 */       throw new IOException(e.getLocalizedMessage(), e);
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   public ThemesTable(ThemeDocument theme)
/*  67:    */   {
/*  68: 97 */     this.theme = theme;
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected void setColorMap(IndexedColorMap colorMap)
/*  72:    */   {
/*  73:105 */     this.colorMap = colorMap;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public XSSFColor getThemeColor(int idx)
/*  77:    */   {
/*  78:117 */     CTColorScheme colorScheme = this.theme.getTheme().getThemeElements().getClrScheme();
/*  79:    */     org.openxmlformats.schemas.drawingml.x2006.main.CTColor ctColor;
/*  80:119 */     switch (1.$SwitchMap$org$apache$poi$xssf$model$ThemesTable$ThemeElement[ThemeElement.byId(idx).ordinal()])
/*  81:    */     {
/*  82:    */     case 1: 
/*  83:120 */       ctColor = colorScheme.getLt1(); break;
/*  84:    */     case 2: 
/*  85:121 */       ctColor = colorScheme.getDk1(); break;
/*  86:    */     case 3: 
/*  87:122 */       ctColor = colorScheme.getLt2(); break;
/*  88:    */     case 4: 
/*  89:123 */       ctColor = colorScheme.getDk2(); break;
/*  90:    */     case 5: 
/*  91:124 */       ctColor = colorScheme.getAccent1(); break;
/*  92:    */     case 6: 
/*  93:125 */       ctColor = colorScheme.getAccent2(); break;
/*  94:    */     case 7: 
/*  95:126 */       ctColor = colorScheme.getAccent3(); break;
/*  96:    */     case 8: 
/*  97:127 */       ctColor = colorScheme.getAccent4(); break;
/*  98:    */     case 9: 
/*  99:128 */       ctColor = colorScheme.getAccent5(); break;
/* 100:    */     case 10: 
/* 101:129 */       ctColor = colorScheme.getAccent6(); break;
/* 102:    */     case 11: 
/* 103:130 */       ctColor = colorScheme.getHlink(); break;
/* 104:    */     case 12: 
/* 105:131 */       ctColor = colorScheme.getFolHlink(); break;
/* 106:    */     default: 
/* 107:132 */       return null;
/* 108:    */     }
/* 109:135 */     byte[] rgb = null;
/* 110:136 */     if (ctColor.isSetSrgbClr()) {
/* 111:138 */       rgb = ctColor.getSrgbClr().getVal();
/* 112:139 */     } else if (ctColor.isSetSysClr()) {
/* 113:141 */       rgb = ctColor.getSysClr().getLastClr();
/* 114:    */     } else {
/* 115:143 */       return null;
/* 116:    */     }
/* 117:145 */     return new XSSFColor(rgb, this.colorMap);
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void inheritFromThemeAsRequired(XSSFColor color)
/* 121:    */   {
/* 122:154 */     if (color == null) {
/* 123:156 */       return;
/* 124:    */     }
/* 125:158 */     if (!color.getCTColor().isSetTheme()) {
/* 126:160 */       return;
/* 127:    */     }
/* 128:164 */     XSSFColor themeColor = getThemeColor(color.getTheme());
/* 129:    */     
/* 130:    */ 
/* 131:167 */     color.getCTColor().setRgb(themeColor.getCTColor().getRgb());
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void writeTo(OutputStream out)
/* 135:    */     throws IOException
/* 136:    */   {
/* 137:179 */     this.theme.save(out, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 138:    */   }
/* 139:    */   
/* 140:    */   protected void commit()
/* 141:    */     throws IOException
/* 142:    */   {
/* 143:184 */     PackagePart part = getPackagePart();
/* 144:185 */     OutputStream out = part.getOutputStream();
/* 145:186 */     writeTo(out);
/* 146:187 */     out.close();
/* 147:    */   }
/* 148:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.model.ThemesTable
 * JD-Core Version:    0.7.0.1
 */