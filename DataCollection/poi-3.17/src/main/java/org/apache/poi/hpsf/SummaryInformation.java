/*   1:    */ package org.apache.poi.hpsf;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.UnsupportedEncodingException;
/*   6:    */ import java.util.Date;
/*   7:    */ import org.apache.poi.hpsf.wellknown.PropertyIDMap;
/*   8:    */ import org.apache.poi.hpsf.wellknown.SectionIDMap;
/*   9:    */ 
/*  10:    */ public final class SummaryInformation
/*  11:    */   extends SpecialPropertySet
/*  12:    */ {
/*  13:    */   public static final String DEFAULT_STREAM_NAME = "\005SummaryInformation";
/*  14:    */   
/*  15:    */   public PropertyIDMap getPropertySetIDMap()
/*  16:    */   {
/*  17: 42 */     return PropertyIDMap.getSummaryInformationProperties();
/*  18:    */   }
/*  19:    */   
/*  20:    */   public SummaryInformation()
/*  21:    */   {
/*  22: 49 */     getFirstSection().setFormatID(SectionIDMap.SUMMARY_INFORMATION_ID);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public SummaryInformation(PropertySet ps)
/*  26:    */     throws UnexpectedPropertySetTypeException
/*  27:    */   {
/*  28: 62 */     super(ps);
/*  29: 63 */     if (!isSummaryInformation()) {
/*  30: 64 */       throw new UnexpectedPropertySetTypeException("Not a " + getClass().getName());
/*  31:    */     }
/*  32:    */   }
/*  33:    */   
/*  34:    */   public SummaryInformation(InputStream stream)
/*  35:    */     throws NoPropertySetStreamException, MarkUnsupportedException, IOException, UnsupportedEncodingException
/*  36:    */   {
/*  37: 92 */     super(stream);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public String getTitle()
/*  41:    */   {
/*  42:100 */     return getPropertyStringValue(2);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setTitle(String title)
/*  46:    */   {
/*  47:111 */     set1stProperty(2L, title);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void removeTitle()
/*  51:    */   {
/*  52:120 */     remove1stProperty(2L);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public String getSubject()
/*  56:    */   {
/*  57:131 */     return getPropertyStringValue(3);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setSubject(String subject)
/*  61:    */   {
/*  62:142 */     set1stProperty(3L, subject);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void removeSubject()
/*  66:    */   {
/*  67:151 */     remove1stProperty(3L);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public String getAuthor()
/*  71:    */   {
/*  72:162 */     return getPropertyStringValue(4);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setAuthor(String author)
/*  76:    */   {
/*  77:173 */     set1stProperty(4L, author);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void removeAuthor()
/*  81:    */   {
/*  82:182 */     remove1stProperty(4L);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public String getKeywords()
/*  86:    */   {
/*  87:193 */     return getPropertyStringValue(5);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setKeywords(String keywords)
/*  91:    */   {
/*  92:204 */     set1stProperty(5L, keywords);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void removeKeywords()
/*  96:    */   {
/*  97:213 */     remove1stProperty(5L);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public String getComments()
/* 101:    */   {
/* 102:224 */     return getPropertyStringValue(6);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setComments(String comments)
/* 106:    */   {
/* 107:235 */     set1stProperty(6L, comments);
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void removeComments()
/* 111:    */   {
/* 112:244 */     remove1stProperty(6L);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public String getTemplate()
/* 116:    */   {
/* 117:255 */     return getPropertyStringValue(7);
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void setTemplate(String template)
/* 121:    */   {
/* 122:266 */     set1stProperty(7L, template);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void removeTemplate()
/* 126:    */   {
/* 127:275 */     remove1stProperty(7L);
/* 128:    */   }
/* 129:    */   
/* 130:    */   public String getLastAuthor()
/* 131:    */   {
/* 132:286 */     return getPropertyStringValue(8);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void setLastAuthor(String lastAuthor)
/* 136:    */   {
/* 137:297 */     set1stProperty(8L, lastAuthor);
/* 138:    */   }
/* 139:    */   
/* 140:    */   public void removeLastAuthor()
/* 141:    */   {
/* 142:306 */     remove1stProperty(8L);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public String getRevNumber()
/* 146:    */   {
/* 147:317 */     return getPropertyStringValue(9);
/* 148:    */   }
/* 149:    */   
/* 150:    */   public void setRevNumber(String revNumber)
/* 151:    */   {
/* 152:328 */     set1stProperty(9L, revNumber);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void removeRevNumber()
/* 156:    */   {
/* 157:337 */     remove1stProperty(9L);
/* 158:    */   }
/* 159:    */   
/* 160:    */   public long getEditTime()
/* 161:    */   {
/* 162:350 */     Date d = (Date)getProperty(10);
/* 163:351 */     if (d == null) {
/* 164:352 */       return 0L;
/* 165:    */     }
/* 166:354 */     return Filetime.dateToFileTime(d);
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void setEditTime(long time)
/* 170:    */   {
/* 171:365 */     Date d = Filetime.filetimeToDate(time);
/* 172:366 */     getFirstSection().setProperty(10, 64L, d);
/* 173:    */   }
/* 174:    */   
/* 175:    */   public void removeEditTime()
/* 176:    */   {
/* 177:375 */     remove1stProperty(10L);
/* 178:    */   }
/* 179:    */   
/* 180:    */   public Date getLastPrinted()
/* 181:    */   {
/* 182:386 */     return (Date)getProperty(11);
/* 183:    */   }
/* 184:    */   
/* 185:    */   public void setLastPrinted(Date lastPrinted)
/* 186:    */   {
/* 187:397 */     getFirstSection().setProperty(11, 64L, lastPrinted);
/* 188:    */   }
/* 189:    */   
/* 190:    */   public void removeLastPrinted()
/* 191:    */   {
/* 192:406 */     remove1stProperty(11L);
/* 193:    */   }
/* 194:    */   
/* 195:    */   public Date getCreateDateTime()
/* 196:    */   {
/* 197:417 */     return (Date)getProperty(12);
/* 198:    */   }
/* 199:    */   
/* 200:    */   public void setCreateDateTime(Date createDateTime)
/* 201:    */   {
/* 202:428 */     getFirstSection().setProperty(12, 64L, createDateTime);
/* 203:    */   }
/* 204:    */   
/* 205:    */   public void removeCreateDateTime()
/* 206:    */   {
/* 207:437 */     remove1stProperty(12L);
/* 208:    */   }
/* 209:    */   
/* 210:    */   public Date getLastSaveDateTime()
/* 211:    */   {
/* 212:448 */     return (Date)getProperty(13);
/* 213:    */   }
/* 214:    */   
/* 215:    */   public void setLastSaveDateTime(Date time)
/* 216:    */   {
/* 217:459 */     Section s = getFirstSection();
/* 218:460 */     s.setProperty(13, 64L, time);
/* 219:    */   }
/* 220:    */   
/* 221:    */   public void removeLastSaveDateTime()
/* 222:    */   {
/* 223:471 */     remove1stProperty(13L);
/* 224:    */   }
/* 225:    */   
/* 226:    */   public int getPageCount()
/* 227:    */   {
/* 228:484 */     return getPropertyIntValue(14);
/* 229:    */   }
/* 230:    */   
/* 231:    */   public void setPageCount(int pageCount)
/* 232:    */   {
/* 233:495 */     set1stProperty(14L, pageCount);
/* 234:    */   }
/* 235:    */   
/* 236:    */   public void removePageCount()
/* 237:    */   {
/* 238:504 */     remove1stProperty(14L);
/* 239:    */   }
/* 240:    */   
/* 241:    */   public int getWordCount()
/* 242:    */   {
/* 243:516 */     return getPropertyIntValue(15);
/* 244:    */   }
/* 245:    */   
/* 246:    */   public void setWordCount(int wordCount)
/* 247:    */   {
/* 248:527 */     set1stProperty(15L, wordCount);
/* 249:    */   }
/* 250:    */   
/* 251:    */   public void removeWordCount()
/* 252:    */   {
/* 253:536 */     remove1stProperty(15L);
/* 254:    */   }
/* 255:    */   
/* 256:    */   public int getCharCount()
/* 257:    */   {
/* 258:548 */     return getPropertyIntValue(16);
/* 259:    */   }
/* 260:    */   
/* 261:    */   public void setCharCount(int charCount)
/* 262:    */   {
/* 263:559 */     set1stProperty(16L, charCount);
/* 264:    */   }
/* 265:    */   
/* 266:    */   public void removeCharCount()
/* 267:    */   {
/* 268:568 */     remove1stProperty(16L);
/* 269:    */   }
/* 270:    */   
/* 271:    */   public byte[] getThumbnail()
/* 272:    */   {
/* 273:585 */     return (byte[])getProperty(17);
/* 274:    */   }
/* 275:    */   
/* 276:    */   public Thumbnail getThumbnailThumbnail()
/* 277:    */   {
/* 278:596 */     byte[] data = getThumbnail();
/* 279:597 */     if (data == null) {
/* 280:597 */       return null;
/* 281:    */     }
/* 282:598 */     return new Thumbnail(data);
/* 283:    */   }
/* 284:    */   
/* 285:    */   public void setThumbnail(byte[] thumbnail)
/* 286:    */   {
/* 287:609 */     getFirstSection().setProperty(17, 30L, thumbnail);
/* 288:    */   }
/* 289:    */   
/* 290:    */   public void removeThumbnail()
/* 291:    */   {
/* 292:618 */     remove1stProperty(17L);
/* 293:    */   }
/* 294:    */   
/* 295:    */   public String getApplicationName()
/* 296:    */   {
/* 297:629 */     return getPropertyStringValue(18);
/* 298:    */   }
/* 299:    */   
/* 300:    */   public void setApplicationName(String applicationName)
/* 301:    */   {
/* 302:640 */     set1stProperty(18L, applicationName);
/* 303:    */   }
/* 304:    */   
/* 305:    */   public void removeApplicationName()
/* 306:    */   {
/* 307:649 */     remove1stProperty(18L);
/* 308:    */   }
/* 309:    */   
/* 310:    */   public int getSecurity()
/* 311:    */   {
/* 312:677 */     return getPropertyIntValue(19);
/* 313:    */   }
/* 314:    */   
/* 315:    */   public void setSecurity(int security)
/* 316:    */   {
/* 317:688 */     set1stProperty(19L, security);
/* 318:    */   }
/* 319:    */   
/* 320:    */   public void removeSecurity()
/* 321:    */   {
/* 322:697 */     remove1stProperty(19L);
/* 323:    */   }
/* 324:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.SummaryInformation
 * JD-Core Version:    0.7.0.1
 */