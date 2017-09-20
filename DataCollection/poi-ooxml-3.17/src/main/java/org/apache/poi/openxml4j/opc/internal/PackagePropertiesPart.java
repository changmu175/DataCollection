/*   1:    */ package org.apache.poi.openxml4j.opc.internal;
/*   2:    */ 
/*   3:    */ import java.io.InputStream;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.text.ParsePosition;
/*   6:    */ import java.text.SimpleDateFormat;
/*   7:    */ import java.util.Date;
/*   8:    */ import java.util.Locale;
/*   9:    */ import java.util.regex.Matcher;
/*  10:    */ import java.util.regex.Pattern;
/*  11:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  12:    */ import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
/*  13:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  14:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  15:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  16:    */ import org.apache.poi.openxml4j.opc.PackageProperties;
/*  17:    */ import org.apache.poi.openxml4j.util.Nullable;
/*  18:    */ import org.apache.poi.util.LocaleUtil;
/*  19:    */ 
/*  20:    */ public final class PackagePropertiesPart
/*  21:    */   extends PackagePart
/*  22:    */   implements PackageProperties
/*  23:    */ {
/*  24:    */   public static final String NAMESPACE_DC_URI = "http://purl.org/dc/elements/1.1/";
/*  25:    */   public static final String NAMESPACE_CP_URI = "http://schemas.openxmlformats.org/package/2006/metadata/core-properties";
/*  26:    */   public static final String NAMESPACE_DCTERMS_URI = "http://purl.org/dc/terms/";
/*  27:    */   private static final String DEFAULT_DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
/*  28: 56 */   private static final String[] DATE_FORMATS = { "yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd'T'HH:mm:ss.SS'Z'", "yyyy-MM-dd" };
/*  29: 67 */   private final String[] TZ_DATE_FORMATS = { "yyyy-MM-dd'T'HH:mm:ssz", "yyyy-MM-dd'T'HH:mm:ss.Sz", "yyyy-MM-dd'T'HH:mm:ss.SSz", "yyyy-MM-dd'T'HH:mm:ss.SSSz" };
/*  30: 74 */   private final Pattern TIME_ZONE_PAT = Pattern.compile("([-+]\\d\\d):?(\\d\\d)");
/*  31:    */   
/*  32:    */   public PackagePropertiesPart(OPCPackage pack, PackagePartName partName)
/*  33:    */     throws InvalidFormatException
/*  34:    */   {
/*  35: 87 */     super(pack, partName, "application/vnd.openxmlformats-package.core-properties+xml");
/*  36:    */   }
/*  37:    */   
/*  38: 98 */   protected Nullable<String> category = new Nullable();
/*  39:106 */   protected Nullable<String> contentStatus = new Nullable();
/*  40:116 */   protected Nullable<String> contentType = new Nullable();
/*  41:121 */   protected Nullable<Date> created = new Nullable();
/*  42:126 */   protected Nullable<String> creator = new Nullable();
/*  43:135 */   protected Nullable<String> description = new Nullable();
/*  44:140 */   protected Nullable<String> identifier = new Nullable();
/*  45:147 */   protected Nullable<String> keywords = new Nullable();
/*  46:155 */   protected Nullable<String> language = new Nullable();
/*  47:164 */   protected Nullable<String> lastModifiedBy = new Nullable();
/*  48:169 */   protected Nullable<Date> lastPrinted = new Nullable();
/*  49:174 */   protected Nullable<Date> modified = new Nullable();
/*  50:182 */   protected Nullable<String> revision = new Nullable();
/*  51:187 */   protected Nullable<String> subject = new Nullable();
/*  52:192 */   protected Nullable<String> title = new Nullable();
/*  53:197 */   protected Nullable<String> version = new Nullable();
/*  54:    */   
/*  55:    */   public Nullable<String> getCategoryProperty()
/*  56:    */   {
/*  57:209 */     return this.category;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Nullable<String> getContentStatusProperty()
/*  61:    */   {
/*  62:218 */     return this.contentStatus;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public Nullable<String> getContentTypeProperty()
/*  66:    */   {
/*  67:227 */     return this.contentType;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public Nullable<Date> getCreatedProperty()
/*  71:    */   {
/*  72:236 */     return this.created;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public String getCreatedPropertyString()
/*  76:    */   {
/*  77:245 */     return getDateValue(this.created);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public Nullable<String> getCreatorProperty()
/*  81:    */   {
/*  82:254 */     return this.creator;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public Nullable<String> getDescriptionProperty()
/*  86:    */   {
/*  87:263 */     return this.description;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public Nullable<String> getIdentifierProperty()
/*  91:    */   {
/*  92:272 */     return this.identifier;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public Nullable<String> getKeywordsProperty()
/*  96:    */   {
/*  97:281 */     return this.keywords;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public Nullable<String> getLanguageProperty()
/* 101:    */   {
/* 102:290 */     return this.language;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public Nullable<String> getLastModifiedByProperty()
/* 106:    */   {
/* 107:299 */     return this.lastModifiedBy;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public Nullable<Date> getLastPrintedProperty()
/* 111:    */   {
/* 112:308 */     return this.lastPrinted;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public String getLastPrintedPropertyString()
/* 116:    */   {
/* 117:317 */     return getDateValue(this.lastPrinted);
/* 118:    */   }
/* 119:    */   
/* 120:    */   public Nullable<Date> getModifiedProperty()
/* 121:    */   {
/* 122:326 */     return this.modified;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public String getModifiedPropertyString()
/* 126:    */   {
/* 127:335 */     if (this.modified.hasValue()) {
/* 128:336 */       return getDateValue(this.modified);
/* 129:    */     }
/* 130:338 */     return getDateValue(new Nullable(new Date()));
/* 131:    */   }
/* 132:    */   
/* 133:    */   public Nullable<String> getRevisionProperty()
/* 134:    */   {
/* 135:347 */     return this.revision;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public Nullable<String> getSubjectProperty()
/* 139:    */   {
/* 140:356 */     return this.subject;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public Nullable<String> getTitleProperty()
/* 144:    */   {
/* 145:365 */     return this.title;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public Nullable<String> getVersionProperty()
/* 149:    */   {
/* 150:374 */     return this.version;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void setCategoryProperty(String category)
/* 154:    */   {
/* 155:383 */     this.category = setStringValue(category);
/* 156:    */   }
/* 157:    */   
/* 158:    */   public void setContentStatusProperty(String contentStatus)
/* 159:    */   {
/* 160:392 */     this.contentStatus = setStringValue(contentStatus);
/* 161:    */   }
/* 162:    */   
/* 163:    */   public void setContentTypeProperty(String contentType)
/* 164:    */   {
/* 165:401 */     this.contentType = setStringValue(contentType);
/* 166:    */   }
/* 167:    */   
/* 168:    */   public void setCreatedProperty(String created)
/* 169:    */   {
/* 170:    */     try
/* 171:    */     {
/* 172:411 */       this.created = setDateValue(created);
/* 173:    */     }
/* 174:    */     catch (InvalidFormatException e)
/* 175:    */     {
/* 176:413 */       throw new IllegalArgumentException("Date for created could not be parsed: " + created, e);
/* 177:    */     }
/* 178:    */   }
/* 179:    */   
/* 180:    */   public void setCreatedProperty(Nullable<Date> created)
/* 181:    */   {
/* 182:423 */     if (created.hasValue()) {
/* 183:424 */       this.created = created;
/* 184:    */     }
/* 185:    */   }
/* 186:    */   
/* 187:    */   public void setCreatorProperty(String creator)
/* 188:    */   {
/* 189:433 */     this.creator = setStringValue(creator);
/* 190:    */   }
/* 191:    */   
/* 192:    */   public void setDescriptionProperty(String description)
/* 193:    */   {
/* 194:442 */     this.description = setStringValue(description);
/* 195:    */   }
/* 196:    */   
/* 197:    */   public void setIdentifierProperty(String identifier)
/* 198:    */   {
/* 199:451 */     this.identifier = setStringValue(identifier);
/* 200:    */   }
/* 201:    */   
/* 202:    */   public void setKeywordsProperty(String keywords)
/* 203:    */   {
/* 204:460 */     this.keywords = setStringValue(keywords);
/* 205:    */   }
/* 206:    */   
/* 207:    */   public void setLanguageProperty(String language)
/* 208:    */   {
/* 209:469 */     this.language = setStringValue(language);
/* 210:    */   }
/* 211:    */   
/* 212:    */   public void setLastModifiedByProperty(String lastModifiedBy)
/* 213:    */   {
/* 214:478 */     this.lastModifiedBy = setStringValue(lastModifiedBy);
/* 215:    */   }
/* 216:    */   
/* 217:    */   public void setLastPrintedProperty(String lastPrinted)
/* 218:    */   {
/* 219:    */     try
/* 220:    */     {
/* 221:488 */       this.lastPrinted = setDateValue(lastPrinted);
/* 222:    */     }
/* 223:    */     catch (InvalidFormatException e)
/* 224:    */     {
/* 225:490 */       throw new IllegalArgumentException("lastPrinted  : " + e.getLocalizedMessage(), e);
/* 226:    */     }
/* 227:    */   }
/* 228:    */   
/* 229:    */   public void setLastPrintedProperty(Nullable<Date> lastPrinted)
/* 230:    */   {
/* 231:501 */     if (lastPrinted.hasValue()) {
/* 232:502 */       this.lastPrinted = lastPrinted;
/* 233:    */     }
/* 234:    */   }
/* 235:    */   
/* 236:    */   public void setModifiedProperty(String modified)
/* 237:    */   {
/* 238:    */     try
/* 239:    */     {
/* 240:512 */       this.modified = setDateValue(modified);
/* 241:    */     }
/* 242:    */     catch (InvalidFormatException e)
/* 243:    */     {
/* 244:514 */       throw new IllegalArgumentException("modified  : " + e.getLocalizedMessage(), e);
/* 245:    */     }
/* 246:    */   }
/* 247:    */   
/* 248:    */   public void setModifiedProperty(Nullable<Date> modified)
/* 249:    */   {
/* 250:525 */     if (modified.hasValue()) {
/* 251:526 */       this.modified = modified;
/* 252:    */     }
/* 253:    */   }
/* 254:    */   
/* 255:    */   public void setRevisionProperty(String revision)
/* 256:    */   {
/* 257:535 */     this.revision = setStringValue(revision);
/* 258:    */   }
/* 259:    */   
/* 260:    */   public void setSubjectProperty(String subject)
/* 261:    */   {
/* 262:544 */     this.subject = setStringValue(subject);
/* 263:    */   }
/* 264:    */   
/* 265:    */   public void setTitleProperty(String title)
/* 266:    */   {
/* 267:553 */     this.title = setStringValue(title);
/* 268:    */   }
/* 269:    */   
/* 270:    */   public void setVersionProperty(String version)
/* 271:    */   {
/* 272:562 */     this.version = setStringValue(version);
/* 273:    */   }
/* 274:    */   
/* 275:    */   private Nullable<String> setStringValue(String s)
/* 276:    */   {
/* 277:569 */     if ((s == null) || (s.equals(""))) {
/* 278:570 */       return new Nullable();
/* 279:    */     }
/* 280:572 */     return new Nullable(s);
/* 281:    */   }
/* 282:    */   
/* 283:    */   private Nullable<Date> setDateValue(String dateStr)
/* 284:    */     throws InvalidFormatException
/* 285:    */   {
/* 286:582 */     if ((dateStr == null) || (dateStr.equals(""))) {
/* 287:583 */       return new Nullable();
/* 288:    */     }
/* 289:586 */     Matcher m = this.TIME_ZONE_PAT.matcher(dateStr);
/* 290:587 */     if (m.find())
/* 291:    */     {
/* 292:588 */       String dateTzStr = dateStr.substring(0, m.start()) + m.group(1) + m.group(2);
/* 293:590 */       for (String fStr : this.TZ_DATE_FORMATS)
/* 294:    */       {
/* 295:591 */         SimpleDateFormat df = new SimpleDateFormat(fStr, Locale.ROOT);
/* 296:592 */         df.setTimeZone(LocaleUtil.TIMEZONE_UTC);
/* 297:593 */         Date d = df.parse(dateTzStr, new ParsePosition(0));
/* 298:594 */         if (d != null) {
/* 299:595 */           return new Nullable(d);
/* 300:    */         }
/* 301:    */       }
/* 302:    */     }
/* 303:599 */     String dateTzStr = dateStr + "Z";
/* 304:600 */     for (String fStr : DATE_FORMATS)
/* 305:    */     {
/* 306:601 */       SimpleDateFormat df = new SimpleDateFormat(fStr, Locale.ROOT);
/* 307:602 */       df.setTimeZone(LocaleUtil.TIMEZONE_UTC);
/* 308:603 */       Date d = df.parse(dateTzStr, new ParsePosition(0));
/* 309:604 */       if (d != null) {
/* 310:605 */         return new Nullable(d);
/* 311:    */       }
/* 312:    */     }
/* 313:609 */     StringBuilder sb = new StringBuilder();
/* 314:610 */     int i = 0;
/* 315:611 */     for (String fStr : this.TZ_DATE_FORMATS)
/* 316:    */     {
/* 317:612 */       if (i++ > 0) {
/* 318:613 */         sb.append(", ");
/* 319:    */       }
/* 320:615 */       sb.append(fStr);
/* 321:    */     }
/* 322:617 */     for (String fStr : DATE_FORMATS) {
/* 323:618 */       sb.append(", ").append(fStr);
/* 324:    */     }
/* 325:620 */     throw new InvalidFormatException("Date " + dateStr + " not well formatted, " + "expected format in: " + sb);
/* 326:    */   }
/* 327:    */   
/* 328:    */   private String getDateValue(Nullable<Date> d)
/* 329:    */   {
/* 330:633 */     if (d == null) {
/* 331:634 */       return "";
/* 332:    */     }
/* 333:636 */     Date date = (Date)d.getValue();
/* 334:637 */     if (date == null) {
/* 335:638 */       return "";
/* 336:    */     }
/* 337:641 */     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT);
/* 338:642 */     df.setTimeZone(LocaleUtil.TIMEZONE_UTC);
/* 339:643 */     return df.format(date);
/* 340:    */   }
/* 341:    */   
/* 342:    */   protected InputStream getInputStreamImpl()
/* 343:    */   {
/* 344:648 */     throw new InvalidOperationException("Operation not authorized. This part may only be manipulated using the getters and setters on PackagePropertiesPart");
/* 345:    */   }
/* 346:    */   
/* 347:    */   protected OutputStream getOutputStreamImpl()
/* 348:    */   {
/* 349:653 */     throw new InvalidOperationException("Can't use output stream to set properties !");
/* 350:    */   }
/* 351:    */   
/* 352:    */   public boolean save(OutputStream zos)
/* 353:    */   {
/* 354:659 */     throw new InvalidOperationException("Operation not authorized. This part may only be manipulated using the getters and setters on PackagePropertiesPart");
/* 355:    */   }
/* 356:    */   
/* 357:    */   public boolean load(InputStream ios)
/* 358:    */   {
/* 359:664 */     throw new InvalidOperationException("Operation not authorized. This part may only be manipulated using the getters and setters on PackagePropertiesPart");
/* 360:    */   }
/* 361:    */   
/* 362:    */   public void close() {}
/* 363:    */   
/* 364:    */   public void flush() {}
/* 365:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.internal.PackagePropertiesPart
 * JD-Core Version:    0.7.0.1
 */