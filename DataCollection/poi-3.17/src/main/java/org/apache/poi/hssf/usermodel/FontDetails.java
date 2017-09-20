/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Properties;
/*   6:    */ import java.util.StringTokenizer;
/*   7:    */ 
/*   8:    */ public class FontDetails
/*   9:    */ {
/*  10:    */   private String _fontName;
/*  11:    */   private int _height;
/*  12: 32 */   private final Map<Character, Integer> charWidths = new HashMap();
/*  13:    */   
/*  14:    */   public FontDetails(String fontName, int height)
/*  15:    */   {
/*  16: 42 */     this._fontName = fontName;
/*  17: 43 */     this._height = height;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public String getFontName()
/*  21:    */   {
/*  22: 48 */     return this._fontName;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public int getHeight()
/*  26:    */   {
/*  27: 53 */     return this._height;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void addChar(char c, int width)
/*  31:    */   {
/*  32: 58 */     this.charWidths.put(Character.valueOf(c), Integer.valueOf(width));
/*  33:    */   }
/*  34:    */   
/*  35:    */   public int getCharWidth(char c)
/*  36:    */   {
/*  37: 68 */     Integer widthInteger = (Integer)this.charWidths.get(Character.valueOf(c));
/*  38: 69 */     if (widthInteger == null) {
/*  39: 70 */       return 'W' == c ? 0 : getCharWidth('W');
/*  40:    */     }
/*  41: 72 */     return widthInteger.intValue();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void addChars(char[] characters, int[] widths)
/*  45:    */   {
/*  46: 77 */     for (int i = 0; i < characters.length; i++) {
/*  47: 79 */       this.charWidths.put(Character.valueOf(characters[i]), Integer.valueOf(widths[i]));
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected static String buildFontHeightProperty(String fontName)
/*  52:    */   {
/*  53: 84 */     return "font." + fontName + ".height";
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected static String buildFontWidthsProperty(String fontName)
/*  57:    */   {
/*  58: 87 */     return "font." + fontName + ".widths";
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected static String buildFontCharactersProperty(String fontName)
/*  62:    */   {
/*  63: 90 */     return "font." + fontName + ".characters";
/*  64:    */   }
/*  65:    */   
/*  66:    */   public static FontDetails create(String fontName, Properties fontMetricsProps)
/*  67:    */   {
/*  68:103 */     String heightStr = fontMetricsProps.getProperty(buildFontHeightProperty(fontName));
/*  69:104 */     String widthsStr = fontMetricsProps.getProperty(buildFontWidthsProperty(fontName));
/*  70:105 */     String charactersStr = fontMetricsProps.getProperty(buildFontCharactersProperty(fontName));
/*  71:108 */     if ((heightStr == null) || (widthsStr == null) || (charactersStr == null)) {
/*  72:111 */       throw new IllegalArgumentException("The supplied FontMetrics doesn't know about the font '" + fontName + "', so we can't use it. Please add it to your font metrics file (see StaticFontMetrics.getFontDetails");
/*  73:    */     }
/*  74:114 */     int height = Integer.parseInt(heightStr);
/*  75:115 */     FontDetails d = new FontDetails(fontName, height);
/*  76:116 */     String[] charactersStrArray = split(charactersStr, ",", -1);
/*  77:117 */     String[] widthsStrArray = split(widthsStr, ",", -1);
/*  78:118 */     if (charactersStrArray.length != widthsStrArray.length) {
/*  79:119 */       throw new RuntimeException("Number of characters does not number of widths for font " + fontName);
/*  80:    */     }
/*  81:120 */     for (int i = 0; i < widthsStrArray.length; i++) {
/*  82:122 */       if (charactersStrArray[i].length() != 0) {
/*  83:123 */         d.addChar(charactersStrArray[i].charAt(0), Integer.parseInt(widthsStrArray[i]));
/*  84:    */       }
/*  85:    */     }
/*  86:125 */     return d;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public int getStringWidth(String str)
/*  90:    */   {
/*  91:136 */     int width = 0;
/*  92:137 */     for (int i = 0; i < str.length(); i++) {
/*  93:139 */       width += getCharWidth(str.charAt(i));
/*  94:    */     }
/*  95:141 */     return width;
/*  96:    */   }
/*  97:    */   
/*  98:    */   private static String[] split(String text, String separator, int max)
/*  99:    */   {
/* 100:150 */     StringTokenizer tok = new StringTokenizer(text, separator);
/* 101:151 */     int listSize = tok.countTokens();
/* 102:152 */     if ((max != -1) && (listSize > max)) {
/* 103:153 */       listSize = max;
/* 104:    */     }
/* 105:154 */     String[] list = new String[listSize];
/* 106:155 */     for (int i = 0; tok.hasMoreTokens(); i++)
/* 107:    */     {
/* 108:157 */       if ((max != -1) && (i == listSize - 1))
/* 109:    */       {
/* 110:159 */         StringBuffer buf = new StringBuffer(text.length() * (listSize - i) / listSize);
/* 111:160 */         while (tok.hasMoreTokens())
/* 112:    */         {
/* 113:162 */           buf.append(tok.nextToken());
/* 114:163 */           if (tok.hasMoreTokens()) {
/* 115:164 */             buf.append(separator);
/* 116:    */           }
/* 117:    */         }
/* 118:166 */         list[i] = buf.toString().trim();
/* 119:167 */         break;
/* 120:    */       }
/* 121:169 */       list[i] = tok.nextToken().trim();
/* 122:    */     }
/* 123:172 */     return list;
/* 124:    */   }
/* 125:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.FontDetails
 * JD-Core Version:    0.7.0.1
 */