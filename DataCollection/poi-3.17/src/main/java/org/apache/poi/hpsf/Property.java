/*   1:    */ package org.apache.poi.hpsf;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.io.UnsupportedEncodingException;
/*   7:    */ import java.nio.charset.Charset;
/*   8:    */ import java.nio.charset.CharsetEncoder;
/*   9:    */ import java.util.Calendar;
/*  10:    */ import java.util.Date;
/*  11:    */ import java.util.Locale;
/*  12:    */ import java.util.concurrent.TimeUnit;
/*  13:    */ import javax.xml.bind.DatatypeConverter;
/*  14:    */ import org.apache.poi.hpsf.wellknown.PropertyIDMap;
/*  15:    */ import org.apache.poi.util.CodePageUtil;
/*  16:    */ import org.apache.poi.util.HexDump;
/*  17:    */ import org.apache.poi.util.LittleEndian;
/*  18:    */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*  19:    */ import org.apache.poi.util.LocaleUtil;
/*  20:    */ import org.apache.poi.util.POILogFactory;
/*  21:    */ import org.apache.poi.util.POILogger;
/*  22:    */ 
/*  23:    */ public class Property
/*  24:    */ {
/*  25:    */   public static final int DEFAULT_CODEPAGE = 1252;
/*  26: 73 */   private static final POILogger LOG = POILogFactory.getLogger(Property.class);
/*  27:    */   private long id;
/*  28:    */   private long type;
/*  29:    */   private Object value;
/*  30:    */   
/*  31:    */   public Property() {}
/*  32:    */   
/*  33:    */   public Property(Property p)
/*  34:    */   {
/*  35: 97 */     this(p.id, p.type, p.value);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public Property(long id, long type, Object value)
/*  39:    */   {
/*  40:109 */     this.id = id;
/*  41:110 */     this.type = type;
/*  42:111 */     this.value = value;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public Property(long id, byte[] src, long offset, int length, int codepage)
/*  46:    */     throws UnsupportedEncodingException
/*  47:    */   {
/*  48:130 */     this.id = id;
/*  49:136 */     if (id == 0L) {
/*  50:137 */       throw new UnsupportedEncodingException("Dictionary not allowed here");
/*  51:    */     }
/*  52:140 */     int o = (int)offset;
/*  53:141 */     this.type = LittleEndian.getUInt(src, o);
/*  54:142 */     o += 4;
/*  55:    */     try
/*  56:    */     {
/*  57:145 */       this.value = VariantSupport.read(src, o, length, (int)this.type, codepage);
/*  58:    */     }
/*  59:    */     catch (UnsupportedVariantTypeException ex)
/*  60:    */     {
/*  61:147 */       VariantSupport.writeUnsupportedTypeMessage(ex);
/*  62:148 */       this.value = ex.getValue();
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   public Property(long id, LittleEndianByteArrayInputStream leis, int length, int codepage)
/*  67:    */     throws UnsupportedEncodingException
/*  68:    */   {
/*  69:166 */     this.id = id;
/*  70:172 */     if (id == 0L) {
/*  71:173 */       throw new UnsupportedEncodingException("Dictionary not allowed here");
/*  72:    */     }
/*  73:176 */     this.type = leis.readUInt();
/*  74:    */     try
/*  75:    */     {
/*  76:179 */       this.value = VariantSupport.read(leis, length, (int)this.type, codepage);
/*  77:    */     }
/*  78:    */     catch (UnsupportedVariantTypeException ex)
/*  79:    */     {
/*  80:181 */       VariantSupport.writeUnsupportedTypeMessage(ex);
/*  81:182 */       this.value = ex.getValue();
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   public long getID()
/*  86:    */   {
/*  87:193 */     return this.id;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setID(long id)
/*  91:    */   {
/*  92:202 */     this.id = id;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public long getType()
/*  96:    */   {
/*  97:211 */     return this.type;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setType(long type)
/* 101:    */   {
/* 102:220 */     this.type = type;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public Object getValue()
/* 106:    */   {
/* 107:229 */     return this.value;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setValue(Object value)
/* 111:    */   {
/* 112:238 */     this.value = value;
/* 113:    */   }
/* 114:    */   
/* 115:    */   protected int getSize(int codepage)
/* 116:    */     throws WritingNotSupportedException
/* 117:    */   {
/* 118:258 */     int length = Variant.getVariantLength(this.type);
/* 119:259 */     if ((length >= 0) || (this.type == 0L)) {
/* 120:261 */       return length;
/* 121:    */     }
/* 122:263 */     if (length == -2) {
/* 123:265 */       throw new WritingNotSupportedException(this.type, null);
/* 124:    */     }
/* 125:269 */     if ((this.type == 30L) || (this.type == 31L))
/* 126:    */     {
/* 127:270 */       ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 128:    */       try
/* 129:    */       {
/* 130:272 */         length = write(bos, codepage) - 8;
/* 131:    */         
/* 132:274 */         return length + (4 - (length & 0x3) & 0x3);
/* 133:    */       }
/* 134:    */       catch (IOException e)
/* 135:    */       {
/* 136:277 */         throw new WritingNotSupportedException(this.type, this.value);
/* 137:    */       }
/* 138:    */     }
/* 139:281 */     throw new WritingNotSupportedException(this.type, this.value);
/* 140:    */   }
/* 141:    */   
/* 142:    */   public boolean equals(Object o)
/* 143:    */   {
/* 144:298 */     if (!(o instanceof Property)) {
/* 145:299 */       return false;
/* 146:    */     }
/* 147:301 */     Property p = (Property)o;
/* 148:302 */     Object pValue = p.getValue();
/* 149:303 */     long pId = p.getID();
/* 150:304 */     if ((this.id != pId) || ((this.id != 0L) && (!typesAreEqual(this.type, p.getType())))) {
/* 151:305 */       return false;
/* 152:    */     }
/* 153:307 */     if ((this.value == null) && (pValue == null)) {
/* 154:308 */       return true;
/* 155:    */     }
/* 156:310 */     if ((this.value == null) || (pValue == null)) {
/* 157:311 */       return false;
/* 158:    */     }
/* 159:315 */     Class<?> valueClass = this.value.getClass();
/* 160:316 */     Class<?> pValueClass = pValue.getClass();
/* 161:317 */     if ((!valueClass.isAssignableFrom(pValueClass)) && (!pValueClass.isAssignableFrom(valueClass))) {
/* 162:319 */       return false;
/* 163:    */     }
/* 164:322 */     if ((this.value instanceof byte[]))
/* 165:    */     {
/* 166:324 */       byte[] thisVal = (byte[])this.value;byte[] otherVal = (byte[])pValue;
/* 167:325 */       int len = unpaddedLength(thisVal);
/* 168:326 */       if (len != unpaddedLength(otherVal)) {
/* 169:327 */         return false;
/* 170:    */       }
/* 171:329 */       for (int i = 0; i < len; i++) {
/* 172:330 */         if (thisVal[i] != otherVal[i]) {
/* 173:331 */           return false;
/* 174:    */         }
/* 175:    */       }
/* 176:334 */       return true;
/* 177:    */     }
/* 178:337 */     return this.value.equals(pValue);
/* 179:    */   }
/* 180:    */   
/* 181:    */   private static int unpaddedLength(byte[] buf)
/* 182:    */   {
/* 183:349 */     for (int len = buf.length; (len > 0) && (len > buf.length - 4) && (buf[(len - 1)] == 0); len--) {}
/* 184:350 */     return len;
/* 185:    */   }
/* 186:    */   
/* 187:    */   private boolean typesAreEqual(long t1, long t2)
/* 188:    */   {
/* 189:355 */     return (t1 == t2) || ((t1 == 30L) && (t2 == 31L)) || ((t2 == 30L) && (t1 == 31L));
/* 190:    */   }
/* 191:    */   
/* 192:    */   public int hashCode()
/* 193:    */   {
/* 194:367 */     long hashCode = 0L;
/* 195:368 */     hashCode += this.id;
/* 196:369 */     hashCode += this.type;
/* 197:370 */     if (this.value != null) {
/* 198:371 */       hashCode += this.value.hashCode();
/* 199:    */     }
/* 200:373 */     return (int)(hashCode & 0xFFFFFFFF);
/* 201:    */   }
/* 202:    */   
/* 203:    */   public String toString()
/* 204:    */   {
/* 205:384 */     return toString(1252, null);
/* 206:    */   }
/* 207:    */   
/* 208:    */   public String toString(int codepage, PropertyIDMap idMap)
/* 209:    */   {
/* 210:388 */     StringBuilder b = new StringBuilder();
/* 211:389 */     b.append("Property[");
/* 212:390 */     b.append("id: ");
/* 213:391 */     b.append(this.id);
/* 214:392 */     String idName = idMap == null ? null : idMap.get(Long.valueOf(this.id));
/* 215:393 */     if (idName == null) {
/* 216:394 */       idName = PropertyIDMap.getFallbackProperties().get(Long.valueOf(this.id));
/* 217:    */     }
/* 218:396 */     if (idName != null)
/* 219:    */     {
/* 220:397 */       b.append(" (");
/* 221:398 */       b.append(idName);
/* 222:399 */       b.append(")");
/* 223:    */     }
/* 224:401 */     b.append(", type: ");
/* 225:402 */     b.append(getType());
/* 226:403 */     b.append(" (");
/* 227:404 */     b.append(getVariantName());
/* 228:405 */     b.append(") ");
/* 229:406 */     Object value = getValue();
/* 230:407 */     b.append(", value: ");
/* 231:408 */     if ((value instanceof String))
/* 232:    */     {
/* 233:409 */       b.append((String)value);
/* 234:410 */       b.append("\n");
/* 235:411 */       ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 236:    */       try
/* 237:    */       {
/* 238:413 */         write(bos, codepage);
/* 239:    */       }
/* 240:    */       catch (Exception e)
/* 241:    */       {
/* 242:415 */         LOG.log(5, new Object[] { "can't serialize string", e });
/* 243:    */       }
/* 244:419 */       if (bos.size() > 8)
/* 245:    */       {
/* 246:420 */         String hex = HexDump.dump(bos.toByteArray(), -8L, 8);
/* 247:421 */         b.append(hex);
/* 248:    */       }
/* 249:    */     }
/* 250:423 */     else if ((value instanceof byte[]))
/* 251:    */     {
/* 252:424 */       b.append("\n");
/* 253:425 */       byte[] bytes = (byte[])value;
/* 254:426 */       if (bytes.length > 0)
/* 255:    */       {
/* 256:427 */         String hex = HexDump.dump(bytes, 0L, 0);
/* 257:428 */         b.append(hex);
/* 258:    */       }
/* 259:    */     }
/* 260:430 */     else if ((value instanceof Date))
/* 261:    */     {
/* 262:431 */       Date d = (Date)value;
/* 263:432 */       long filetime = Filetime.dateToFileTime(d);
/* 264:433 */       if (Filetime.isUndefined(d))
/* 265:    */       {
/* 266:434 */         b.append("<undefined>");
/* 267:    */       }
/* 268:435 */       else if (filetime >>> 32 == 0L)
/* 269:    */       {
/* 270:437 */         long l = filetime * 100L;
/* 271:438 */         TimeUnit tu = TimeUnit.NANOSECONDS;
/* 272:439 */         long hr = tu.toHours(l);
/* 273:440 */         l -= TimeUnit.HOURS.toNanos(hr);
/* 274:441 */         long min = tu.toMinutes(l);
/* 275:442 */         l -= TimeUnit.MINUTES.toNanos(min);
/* 276:443 */         long sec = tu.toSeconds(l);
/* 277:444 */         l -= TimeUnit.SECONDS.toNanos(sec);
/* 278:445 */         long ms = tu.toMillis(l);
/* 279:    */         
/* 280:447 */         String str = String.format(Locale.ROOT, "%02d:%02d:%02d.%03d", new Object[] { Long.valueOf(hr), Long.valueOf(min), Long.valueOf(sec), Long.valueOf(ms) });
/* 281:448 */         b.append(str);
/* 282:    */       }
/* 283:    */       else
/* 284:    */       {
/* 285:450 */         Calendar cal = Calendar.getInstance(LocaleUtil.TIMEZONE_UTC, Locale.ROOT);
/* 286:451 */         cal.setTime(d);
/* 287:    */         
/* 288:453 */         b.append(DatatypeConverter.printDateTime(cal));
/* 289:    */       }
/* 290:    */     }
/* 291:455 */     else if ((this.type == 0L) || (this.type == 1L) || (value == null))
/* 292:    */     {
/* 293:456 */       b.append("null");
/* 294:    */     }
/* 295:    */     else
/* 296:    */     {
/* 297:458 */       b.append(value.toString());
/* 298:    */       
/* 299:460 */       String decoded = decodeValueFromID();
/* 300:461 */       if (decoded != null)
/* 301:    */       {
/* 302:462 */         b.append(" (");
/* 303:463 */         b.append(decoded);
/* 304:464 */         b.append(")");
/* 305:    */       }
/* 306:    */     }
/* 307:467 */     b.append(']');
/* 308:468 */     return b.toString();
/* 309:    */   }
/* 310:    */   
/* 311:    */   private String getVariantName()
/* 312:    */   {
/* 313:472 */     if (getID() == 0L) {
/* 314:473 */       return "dictionary";
/* 315:    */     }
/* 316:475 */     return Variant.getVariantName(getType());
/* 317:    */   }
/* 318:    */   
/* 319:    */   private String decodeValueFromID()
/* 320:    */   {
/* 321:    */     try
/* 322:    */     {
/* 323:480 */       switch ((int)getID())
/* 324:    */       {
/* 325:    */       case 1: 
/* 326:482 */         return CodePageUtil.codepageToEncoding(((Number)this.value).intValue());
/* 327:    */       case -2147483648: 
/* 328:484 */         return LocaleUtil.getLocaleFromLCID(((Number)this.value).intValue());
/* 329:    */       }
/* 330:    */     }
/* 331:    */     catch (Exception e)
/* 332:    */     {
/* 333:487 */       LOG.log(5, new Object[] { "Can't decode id " + getID() });
/* 334:    */     }
/* 335:489 */     return null;
/* 336:    */   }
/* 337:    */   
/* 338:    */   public int write(OutputStream out, int codepage)
/* 339:    */     throws IOException, WritingNotSupportedException
/* 340:    */   {
/* 341:505 */     int length = 0;
/* 342:506 */     long variantType = getType();
/* 343:513 */     if ((variantType == 30L) && (codepage != 1200))
/* 344:    */     {
/* 345:514 */       String csStr = CodePageUtil.codepageToEncoding(codepage > 0 ? codepage : 1252);
/* 346:515 */       if (!Charset.forName(csStr).newEncoder().canEncode((String)this.value)) {
/* 347:516 */         variantType = 31L;
/* 348:    */       }
/* 349:    */     }
/* 350:520 */     LittleEndian.putUInt(variantType, out);
/* 351:521 */     length += 4;
/* 352:522 */     length += VariantSupport.write(out, variantType, getValue(), codepage);
/* 353:523 */     return length;
/* 354:    */   }
/* 355:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.Property
 * JD-Core Version:    0.7.0.1
 */