/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ public abstract class HeaderFooter
/*   4:    */   implements org.apache.poi.ss.usermodel.HeaderFooter
/*   5:    */ {
/*   6:    */   protected abstract String getRawText();
/*   7:    */   
/*   8:    */   private String[] splitParts()
/*   9:    */   {
/*  10: 37 */     String text = getRawText();
/*  11:    */     
/*  12: 39 */     String _left = "";
/*  13: 40 */     String _center = "";
/*  14: 41 */     String _right = "";
/*  15: 45 */     while (text.length() > 1)
/*  16:    */     {
/*  17: 46 */       if (text.charAt(0) != '&')
/*  18:    */       {
/*  19: 48 */         _center = text;
/*  20: 49 */         break;
/*  21:    */       }
/*  22: 51 */       int pos = text.length();
/*  23: 52 */       switch (text.charAt(1))
/*  24:    */       {
/*  25:    */       case 'L': 
/*  26: 54 */         if (text.contains("&C")) {
/*  27: 55 */           pos = Math.min(pos, text.indexOf("&C"));
/*  28:    */         }
/*  29: 57 */         if (text.contains("&R")) {
/*  30: 58 */           pos = Math.min(pos, text.indexOf("&R"));
/*  31:    */         }
/*  32: 60 */         _left = text.substring(2, pos);
/*  33: 61 */         text = text.substring(pos);
/*  34: 62 */         break;
/*  35:    */       case 'C': 
/*  36: 64 */         if (text.contains("&L")) {
/*  37: 65 */           pos = Math.min(pos, text.indexOf("&L"));
/*  38:    */         }
/*  39: 67 */         if (text.contains("&R")) {
/*  40: 68 */           pos = Math.min(pos, text.indexOf("&R"));
/*  41:    */         }
/*  42: 70 */         _center = text.substring(2, pos);
/*  43: 71 */         text = text.substring(pos);
/*  44: 72 */         break;
/*  45:    */       case 'R': 
/*  46: 74 */         if (text.contains("&C")) {
/*  47: 75 */           pos = Math.min(pos, text.indexOf("&C"));
/*  48:    */         }
/*  49: 77 */         if (text.contains("&L")) {
/*  50: 78 */           pos = Math.min(pos, text.indexOf("&L"));
/*  51:    */         }
/*  52: 80 */         _right = text.substring(2, pos);
/*  53: 81 */         text = text.substring(pos);
/*  54: 82 */         break;
/*  55:    */       default: 
/*  56: 85 */         _center = text;
/*  57:    */         break label279;
/*  58:    */       }
/*  59:    */     }
/*  60:    */     label279:
/*  61: 89 */     return new String[] { _left, _center, _right };
/*  62:    */   }
/*  63:    */   
/*  64:    */   public final String getLeft()
/*  65:    */   {
/*  66: 96 */     return splitParts()[0];
/*  67:    */   }
/*  68:    */   
/*  69:    */   public final void setLeft(String newLeft)
/*  70:    */   {
/*  71:103 */     updatePart(0, newLeft);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public final String getCenter()
/*  75:    */   {
/*  76:110 */     return splitParts()[1];
/*  77:    */   }
/*  78:    */   
/*  79:    */   public final void setCenter(String newCenter)
/*  80:    */   {
/*  81:117 */     updatePart(1, newCenter);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public final String getRight()
/*  85:    */   {
/*  86:124 */     return splitParts()[2];
/*  87:    */   }
/*  88:    */   
/*  89:    */   public final void setRight(String newRight)
/*  90:    */   {
/*  91:131 */     updatePart(2, newRight);
/*  92:    */   }
/*  93:    */   
/*  94:    */   private void updatePart(int partIndex, String newValue)
/*  95:    */   {
/*  96:135 */     String[] parts = splitParts();
/*  97:136 */     parts[partIndex] = (newValue == null ? "" : newValue);
/*  98:137 */     updateHeaderFooterText(parts);
/*  99:    */   }
/* 100:    */   
/* 101:    */   private void updateHeaderFooterText(String[] parts)
/* 102:    */   {
/* 103:144 */     String _left = parts[0];
/* 104:145 */     String _center = parts[1];
/* 105:146 */     String _right = parts[2];
/* 106:148 */     if ((_center.length() < 1) && (_left.length() < 1) && (_right.length() < 1))
/* 107:    */     {
/* 108:149 */       setHeaderFooterText("");
/* 109:150 */       return;
/* 110:    */     }
/* 111:152 */     StringBuilder sb = new StringBuilder(64);
/* 112:153 */     sb.append("&C");
/* 113:154 */     sb.append(_center);
/* 114:155 */     sb.append("&L");
/* 115:156 */     sb.append(_left);
/* 116:157 */     sb.append("&R");
/* 117:158 */     sb.append(_right);
/* 118:159 */     String text = sb.toString();
/* 119:160 */     setHeaderFooterText(text);
/* 120:    */   }
/* 121:    */   
/* 122:    */   protected abstract void setHeaderFooterText(String paramString);
/* 123:    */   
/* 124:    */   public static String fontSize(short size)
/* 125:    */   {
/* 126:175 */     return "&" + size;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public static String font(String font, String style)
/* 130:    */   {
/* 131:187 */     return "&\"" + font + "," + style + "\"";
/* 132:    */   }
/* 133:    */   
/* 134:    */   public static String page()
/* 135:    */   {
/* 136:194 */     return MarkupTag.PAGE_FIELD.getRepresentation();
/* 137:    */   }
/* 138:    */   
/* 139:    */   public static String numPages()
/* 140:    */   {
/* 141:201 */     return MarkupTag.NUM_PAGES_FIELD.getRepresentation();
/* 142:    */   }
/* 143:    */   
/* 144:    */   public static String date()
/* 145:    */   {
/* 146:208 */     return MarkupTag.DATE_FIELD.getRepresentation();
/* 147:    */   }
/* 148:    */   
/* 149:    */   public static String time()
/* 150:    */   {
/* 151:215 */     return MarkupTag.TIME_FIELD.getRepresentation();
/* 152:    */   }
/* 153:    */   
/* 154:    */   public static String file()
/* 155:    */   {
/* 156:222 */     return MarkupTag.FILE_FIELD.getRepresentation();
/* 157:    */   }
/* 158:    */   
/* 159:    */   public static String tab()
/* 160:    */   {
/* 161:229 */     return MarkupTag.SHEET_NAME_FIELD.getRepresentation();
/* 162:    */   }
/* 163:    */   
/* 164:    */   public static String startBold()
/* 165:    */   {
/* 166:236 */     return MarkupTag.BOLD_FIELD.getRepresentation();
/* 167:    */   }
/* 168:    */   
/* 169:    */   public static String endBold()
/* 170:    */   {
/* 171:243 */     return MarkupTag.BOLD_FIELD.getRepresentation();
/* 172:    */   }
/* 173:    */   
/* 174:    */   public static String startUnderline()
/* 175:    */   {
/* 176:250 */     return MarkupTag.UNDERLINE_FIELD.getRepresentation();
/* 177:    */   }
/* 178:    */   
/* 179:    */   public static String endUnderline()
/* 180:    */   {
/* 181:257 */     return MarkupTag.UNDERLINE_FIELD.getRepresentation();
/* 182:    */   }
/* 183:    */   
/* 184:    */   public static String startDoubleUnderline()
/* 185:    */   {
/* 186:264 */     return MarkupTag.DOUBLE_UNDERLINE_FIELD.getRepresentation();
/* 187:    */   }
/* 188:    */   
/* 189:    */   public static String endDoubleUnderline()
/* 190:    */   {
/* 191:271 */     return MarkupTag.DOUBLE_UNDERLINE_FIELD.getRepresentation();
/* 192:    */   }
/* 193:    */   
/* 194:    */   public static String stripFields(String pText)
/* 195:    */   {
/* 196:283 */     if ((pText == null) || (pText.length() == 0)) {
/* 197:284 */       return pText;
/* 198:    */     }
/* 199:287 */     String text = pText;
/* 200:290 */     for (MarkupTag mt : MarkupTag.values())
/* 201:    */     {
/* 202:291 */       String seq = mt.getRepresentation();
/* 203:    */       int pos;
/* 204:292 */       while ((pos = text.indexOf(seq)) >= 0) {
/* 205:293 */         text = text.substring(0, pos) + text.substring(pos + seq.length());
/* 206:    */       }
/* 207:    */     }
/* 208:299 */     text = text.replaceAll("\\&\\d+", "");
/* 209:300 */     text = text.replaceAll("\\&\".*?,.*?\"", "");
/* 210:    */     
/* 211:    */ 
/* 212:303 */     return text;
/* 213:    */   }
/* 214:    */   
/* 215:    */   private static enum MarkupTag
/* 216:    */   {
/* 217:307 */     SHEET_NAME_FIELD("&A", false),  DATE_FIELD("&D", false),  FILE_FIELD("&F", false),  FULL_FILE_FIELD("&Z", false),  PAGE_FIELD("&P", false),  TIME_FIELD("&T", false),  NUM_PAGES_FIELD("&N", false),  PICTURE_FIELD("&G", false),  BOLD_FIELD("&B", true),  ITALIC_FIELD("&I", true),  STRIKETHROUGH_FIELD("&S", true),  SUBSCRIPT_FIELD("&Y", true),  SUPERSCRIPT_FIELD("&X", true),  UNDERLINE_FIELD("&U", true),  DOUBLE_UNDERLINE_FIELD("&E", true);
/* 218:    */     
/* 219:    */     private final String _representation;
/* 220:    */     private final boolean _occursInPairs;
/* 221:    */     
/* 222:    */     private MarkupTag(String sequence, boolean occursInPairs)
/* 223:    */     {
/* 224:329 */       this._representation = sequence;
/* 225:330 */       this._occursInPairs = occursInPairs;
/* 226:    */     }
/* 227:    */     
/* 228:    */     public String getRepresentation()
/* 229:    */     {
/* 230:336 */       return this._representation;
/* 231:    */     }
/* 232:    */     
/* 233:    */     public boolean occursPairs()
/* 234:    */     {
/* 235:344 */       return this._occursInPairs;
/* 236:    */     }
/* 237:    */   }
/* 238:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HeaderFooter
 * JD-Core Version:    0.7.0.1
 */