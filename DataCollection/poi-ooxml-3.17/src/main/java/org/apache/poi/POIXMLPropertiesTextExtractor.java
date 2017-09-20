/*   1:    */ package org.apache.poi;
/*   2:    */ 
/*   3:    */ import java.math.BigDecimal;
/*   4:    */ import java.math.BigInteger;
/*   5:    */ import java.text.DateFormat;
/*   6:    */ import java.text.DateFormatSymbols;
/*   7:    */ import java.text.SimpleDateFormat;
/*   8:    */ import java.util.Calendar;
/*   9:    */ import java.util.Date;
/*  10:    */ import java.util.Locale;
/*  11:    */ import org.apache.poi.openxml4j.opc.internal.PackagePropertiesPart;
/*  12:    */ import org.apache.poi.openxml4j.util.Nullable;
/*  13:    */ import org.apache.poi.util.LocaleUtil;
/*  14:    */ import org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty;
/*  15:    */ 
/*  16:    */ public class POIXMLPropertiesTextExtractor
/*  17:    */   extends POIXMLTextExtractor
/*  18:    */ {
/*  19:    */   private final DateFormat dateFormat;
/*  20:    */   
/*  21:    */   public POIXMLPropertiesTextExtractor(POIXMLDocument doc)
/*  22:    */   {
/*  23: 46 */     super(doc);
/*  24: 47 */     DateFormatSymbols dfs = DateFormatSymbols.getInstance(Locale.ROOT);
/*  25: 48 */     this.dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", dfs);
/*  26: 49 */     this.dateFormat.setTimeZone(LocaleUtil.TIMEZONE_UTC);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public POIXMLPropertiesTextExtractor(POIXMLTextExtractor otherExtractor)
/*  30:    */   {
/*  31: 60 */     this(otherExtractor.getDocument());
/*  32:    */   }
/*  33:    */   
/*  34:    */   private void appendIfPresent(StringBuffer text, String thing, boolean value)
/*  35:    */   {
/*  36: 64 */     appendIfPresent(text, thing, Boolean.toString(value));
/*  37:    */   }
/*  38:    */   
/*  39:    */   private void appendIfPresent(StringBuffer text, String thing, int value)
/*  40:    */   {
/*  41: 67 */     appendIfPresent(text, thing, Integer.toString(value));
/*  42:    */   }
/*  43:    */   
/*  44:    */   private void appendIfPresent(StringBuffer text, String thing, Date value)
/*  45:    */   {
/*  46: 70 */     if (value == null) {
/*  47: 70 */       return;
/*  48:    */     }
/*  49: 71 */     appendIfPresent(text, thing, this.dateFormat.format(value));
/*  50:    */   }
/*  51:    */   
/*  52:    */   private void appendIfPresent(StringBuffer text, String thing, String value)
/*  53:    */   {
/*  54: 74 */     if (value == null) {
/*  55: 74 */       return;
/*  56:    */     }
/*  57: 75 */     text.append(thing);
/*  58: 76 */     text.append(" = ");
/*  59: 77 */     text.append(value);
/*  60: 78 */     text.append("\n");
/*  61:    */   }
/*  62:    */   
/*  63:    */   public String getCorePropertiesText()
/*  64:    */   {
/*  65: 88 */     POIXMLDocument document = getDocument();
/*  66: 89 */     if (document == null) {
/*  67: 90 */       return "";
/*  68:    */     }
/*  69: 93 */     StringBuffer text = new StringBuffer();
/*  70: 94 */     PackagePropertiesPart props = document.getProperties().getCoreProperties().getUnderlyingProperties();
/*  71:    */     
/*  72:    */ 
/*  73: 97 */     appendIfPresent(text, "Category", (String)props.getCategoryProperty().getValue());
/*  74: 98 */     appendIfPresent(text, "Category", (String)props.getCategoryProperty().getValue());
/*  75: 99 */     appendIfPresent(text, "ContentStatus", (String)props.getContentStatusProperty().getValue());
/*  76:100 */     appendIfPresent(text, "ContentType", (String)props.getContentTypeProperty().getValue());
/*  77:101 */     appendIfPresent(text, "Created", (Date)props.getCreatedProperty().getValue());
/*  78:102 */     appendIfPresent(text, "CreatedString", props.getCreatedPropertyString());
/*  79:103 */     appendIfPresent(text, "Creator", (String)props.getCreatorProperty().getValue());
/*  80:104 */     appendIfPresent(text, "Description", (String)props.getDescriptionProperty().getValue());
/*  81:105 */     appendIfPresent(text, "Identifier", (String)props.getIdentifierProperty().getValue());
/*  82:106 */     appendIfPresent(text, "Keywords", (String)props.getKeywordsProperty().getValue());
/*  83:107 */     appendIfPresent(text, "Language", (String)props.getLanguageProperty().getValue());
/*  84:108 */     appendIfPresent(text, "LastModifiedBy", (String)props.getLastModifiedByProperty().getValue());
/*  85:109 */     appendIfPresent(text, "LastPrinted", (Date)props.getLastPrintedProperty().getValue());
/*  86:110 */     appendIfPresent(text, "LastPrintedString", props.getLastPrintedPropertyString());
/*  87:111 */     appendIfPresent(text, "Modified", (Date)props.getModifiedProperty().getValue());
/*  88:112 */     appendIfPresent(text, "ModifiedString", props.getModifiedPropertyString());
/*  89:113 */     appendIfPresent(text, "Revision", (String)props.getRevisionProperty().getValue());
/*  90:114 */     appendIfPresent(text, "Subject", (String)props.getSubjectProperty().getValue());
/*  91:115 */     appendIfPresent(text, "Title", (String)props.getTitleProperty().getValue());
/*  92:116 */     appendIfPresent(text, "Version", (String)props.getVersionProperty().getValue());
/*  93:    */     
/*  94:118 */     return text.toString();
/*  95:    */   }
/*  96:    */   
/*  97:    */   public String getExtendedPropertiesText()
/*  98:    */   {
/*  99:127 */     POIXMLDocument document = getDocument();
/* 100:128 */     if (document == null) {
/* 101:129 */       return "";
/* 102:    */     }
/* 103:132 */     StringBuffer text = new StringBuffer();
/* 104:    */     
/* 105:134 */     org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTProperties props = document.getProperties().getExtendedProperties().getUnderlyingProperties();
/* 106:    */     
/* 107:136 */     appendIfPresent(text, "Application", props.getApplication());
/* 108:137 */     appendIfPresent(text, "AppVersion", props.getAppVersion());
/* 109:138 */     appendIfPresent(text, "Characters", props.getCharacters());
/* 110:139 */     appendIfPresent(text, "CharactersWithSpaces", props.getCharactersWithSpaces());
/* 111:140 */     appendIfPresent(text, "Company", props.getCompany());
/* 112:141 */     appendIfPresent(text, "HyperlinkBase", props.getHyperlinkBase());
/* 113:142 */     appendIfPresent(text, "HyperlinksChanged", props.getHyperlinksChanged());
/* 114:143 */     appendIfPresent(text, "Lines", props.getLines());
/* 115:144 */     appendIfPresent(text, "LinksUpToDate", props.getLinksUpToDate());
/* 116:145 */     appendIfPresent(text, "Manager", props.getManager());
/* 117:146 */     appendIfPresent(text, "Pages", props.getPages());
/* 118:147 */     appendIfPresent(text, "Paragraphs", props.getParagraphs());
/* 119:148 */     appendIfPresent(text, "PresentationFormat", props.getPresentationFormat());
/* 120:149 */     appendIfPresent(text, "Template", props.getTemplate());
/* 121:150 */     appendIfPresent(text, "TotalTime", props.getTotalTime());
/* 122:    */     
/* 123:152 */     return text.toString();
/* 124:    */   }
/* 125:    */   
/* 126:    */   public String getCustomPropertiesText()
/* 127:    */   {
/* 128:161 */     POIXMLDocument document = getDocument();
/* 129:162 */     if (document == null) {
/* 130:163 */       return "";
/* 131:    */     }
/* 132:166 */     StringBuilder text = new StringBuilder();
/* 133:    */     
/* 134:168 */     org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties props = document.getProperties().getCustomProperties().getUnderlyingProperties();
/* 135:170 */     for (CTProperty property : props.getPropertyArray())
/* 136:    */     {
/* 137:171 */       String val = "(not implemented!)";
/* 138:173 */       if (property.isSetLpwstr())
/* 139:    */       {
/* 140:174 */         val = property.getLpwstr();
/* 141:    */       }
/* 142:176 */       else if (property.isSetLpstr())
/* 143:    */       {
/* 144:177 */         val = property.getLpstr();
/* 145:    */       }
/* 146:179 */       else if (property.isSetDate())
/* 147:    */       {
/* 148:180 */         val = property.getDate().toString();
/* 149:    */       }
/* 150:182 */       else if (property.isSetFiletime())
/* 151:    */       {
/* 152:183 */         val = property.getFiletime().toString();
/* 153:    */       }
/* 154:185 */       else if (property.isSetBool())
/* 155:    */       {
/* 156:186 */         val = Boolean.toString(property.getBool());
/* 157:    */       }
/* 158:190 */       else if (property.isSetI1())
/* 159:    */       {
/* 160:191 */         val = Integer.toString(property.getI1());
/* 161:    */       }
/* 162:193 */       else if (property.isSetI2())
/* 163:    */       {
/* 164:194 */         val = Integer.toString(property.getI2());
/* 165:    */       }
/* 166:196 */       else if (property.isSetI4())
/* 167:    */       {
/* 168:197 */         val = Integer.toString(property.getI4());
/* 169:    */       }
/* 170:199 */       else if (property.isSetI8())
/* 171:    */       {
/* 172:200 */         val = Long.toString(property.getI8());
/* 173:    */       }
/* 174:202 */       else if (property.isSetInt())
/* 175:    */       {
/* 176:203 */         val = Integer.toString(property.getInt());
/* 177:    */       }
/* 178:207 */       else if (property.isSetUi1())
/* 179:    */       {
/* 180:208 */         val = Integer.toString(property.getUi1());
/* 181:    */       }
/* 182:210 */       else if (property.isSetUi2())
/* 183:    */       {
/* 184:211 */         val = Integer.toString(property.getUi2());
/* 185:    */       }
/* 186:213 */       else if (property.isSetUi4())
/* 187:    */       {
/* 188:214 */         val = Long.toString(property.getUi4());
/* 189:    */       }
/* 190:216 */       else if (property.isSetUi8())
/* 191:    */       {
/* 192:217 */         val = property.getUi8().toString();
/* 193:    */       }
/* 194:219 */       else if (property.isSetUint())
/* 195:    */       {
/* 196:220 */         val = Long.toString(property.getUint());
/* 197:    */       }
/* 198:224 */       else if (property.isSetR4())
/* 199:    */       {
/* 200:225 */         val = Float.toString(property.getR4());
/* 201:    */       }
/* 202:227 */       else if (property.isSetR8())
/* 203:    */       {
/* 204:228 */         val = Double.toString(property.getR8());
/* 205:    */       }
/* 206:230 */       else if (property.isSetDecimal())
/* 207:    */       {
/* 208:231 */         BigDecimal d = property.getDecimal();
/* 209:232 */         if (d == null) {
/* 210:233 */           val = null;
/* 211:    */         } else {
/* 212:235 */           val = d.toPlainString();
/* 213:    */         }
/* 214:    */       }
/* 215:257 */       text.append(property.getName()).append(" = ").append(val).append("\n");
/* 216:    */     }
/* 217:260 */     return text.toString();
/* 218:    */   }
/* 219:    */   
/* 220:    */   public String getText()
/* 221:    */   {
/* 222:    */     try
/* 223:    */     {
/* 224:266 */       return getCorePropertiesText() + getExtendedPropertiesText() + getCustomPropertiesText();
/* 225:    */     }
/* 226:    */     catch (Exception e)
/* 227:    */     {
/* 228:271 */       throw new RuntimeException(e);
/* 229:    */     }
/* 230:    */   }
/* 231:    */   
/* 232:    */   public POIXMLPropertiesTextExtractor getMetadataTextExtractor()
/* 233:    */   {
/* 234:277 */     throw new IllegalStateException("You already have the Metadata Text Extractor, not recursing!");
/* 235:    */   }
/* 236:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.POIXMLPropertiesTextExtractor
 * JD-Core Version:    0.7.0.1
 */