/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import java.text.DateFormat;
/*   4:    */ import java.text.SimpleDateFormat;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Locale;
/*   9:    */ import java.util.Map;
/*  10:    */ import org.apache.poi.util.POILogFactory;
/*  11:    */ import org.apache.poi.util.POILogger;
/*  12:    */ 
/*  13:    */ public class DateFormatConverter
/*  14:    */ {
/*  15: 50 */   private static POILogger logger = POILogFactory.getLogger(DateFormatConverter.class);
/*  16:    */   
/*  17:    */   public static class DateFormatTokenizer
/*  18:    */   {
/*  19:    */     String format;
/*  20:    */     int pos;
/*  21:    */     
/*  22:    */     public DateFormatTokenizer(String format)
/*  23:    */     {
/*  24: 57 */       this.format = format;
/*  25:    */     }
/*  26:    */     
/*  27:    */     public String getNextToken()
/*  28:    */     {
/*  29: 61 */       if (this.pos >= this.format.length()) {
/*  30: 62 */         return null;
/*  31:    */       }
/*  32: 64 */       int subStart = this.pos;
/*  33: 65 */       char curChar = this.format.charAt(this.pos);
/*  34: 66 */       this.pos += 1;
/*  35: 67 */       if (curChar == '\'')
/*  36:    */       {
/*  37: 68 */         while ((this.pos < this.format.length()) && ((curChar = this.format.charAt(this.pos)) != '\'')) {
/*  38: 69 */           this.pos += 1;
/*  39:    */         }
/*  40: 71 */         if (this.pos < this.format.length()) {
/*  41: 72 */           this.pos += 1;
/*  42:    */         }
/*  43:    */       }
/*  44:    */       else
/*  45:    */       {
/*  46: 75 */         char activeChar = curChar;
/*  47: 76 */         while ((this.pos < this.format.length()) && ((curChar = this.format.charAt(this.pos)) == activeChar)) {
/*  48: 77 */           this.pos += 1;
/*  49:    */         }
/*  50:    */       }
/*  51: 80 */       return this.format.substring(subStart, this.pos);
/*  52:    */     }
/*  53:    */     
/*  54:    */     public static String[] tokenize(String format)
/*  55:    */     {
/*  56: 84 */       List<String> result = new ArrayList();
/*  57:    */       
/*  58: 86 */       DateFormatTokenizer tokenizer = new DateFormatTokenizer(format);
/*  59:    */       String token;
/*  60: 88 */       while ((token = tokenizer.getNextToken()) != null) {
/*  61: 89 */         result.add(token);
/*  62:    */       }
/*  63: 92 */       return (String[])result.toArray(new String[0]);
/*  64:    */     }
/*  65:    */     
/*  66:    */     public String toString()
/*  67:    */     {
/*  68: 97 */       StringBuilder result = new StringBuilder();
/*  69:    */       
/*  70: 99 */       DateFormatTokenizer tokenizer = new DateFormatTokenizer(this.format);
/*  71:    */       String token;
/*  72:101 */       while ((token = tokenizer.getNextToken()) != null)
/*  73:    */       {
/*  74:102 */         if (result.length() > 0) {
/*  75:103 */           result.append(", ");
/*  76:    */         }
/*  77:105 */         result.append("[").append(token).append("]");
/*  78:    */       }
/*  79:108 */       return result.toString();
/*  80:    */     }
/*  81:    */   }
/*  82:    */   
/*  83:112 */   private static Map<String, String> tokenConversions = prepareTokenConversions();
/*  84:113 */   private static Map<String, String> localePrefixes = prepareLocalePrefixes();
/*  85:    */   
/*  86:    */   private static Map<String, String> prepareTokenConversions()
/*  87:    */   {
/*  88:116 */     Map<String, String> result = new HashMap();
/*  89:    */     
/*  90:118 */     result.put("EEEE", "dddd");
/*  91:119 */     result.put("EEE", "ddd");
/*  92:120 */     result.put("EE", "ddd");
/*  93:121 */     result.put("E", "d");
/*  94:122 */     result.put("Z", "");
/*  95:123 */     result.put("z", "");
/*  96:124 */     result.put("a", "am/pm");
/*  97:125 */     result.put("A", "AM/PM");
/*  98:126 */     result.put("K", "H");
/*  99:127 */     result.put("KK", "HH");
/* 100:128 */     result.put("k", "h");
/* 101:129 */     result.put("kk", "hh");
/* 102:130 */     result.put("S", "0");
/* 103:131 */     result.put("SS", "00");
/* 104:132 */     result.put("SSS", "000");
/* 105:    */     
/* 106:134 */     return result;
/* 107:    */   }
/* 108:    */   
/* 109:    */   private static Map<String, String> prepareLocalePrefixes()
/* 110:    */   {
/* 111:138 */     Map<String, String> result = new HashMap();
/* 112:    */     
/* 113:140 */     result.put("af", "[$-0436]");
/* 114:141 */     result.put("am", "[$-45E]");
/* 115:142 */     result.put("ar_ae", "[$-3801]");
/* 116:143 */     result.put("ar_bh", "[$-3C01]");
/* 117:144 */     result.put("ar_dz", "[$-1401]");
/* 118:145 */     result.put("ar_eg", "[$-C01]");
/* 119:146 */     result.put("ar_iq", "[$-0801]");
/* 120:147 */     result.put("ar_jo", "[$-2C01]");
/* 121:148 */     result.put("ar_kw", "[$-3401]");
/* 122:149 */     result.put("ar_lb", "[$-3001]");
/* 123:150 */     result.put("ar_ly", "[$-1001]");
/* 124:151 */     result.put("ar_ma", "[$-1801]");
/* 125:152 */     result.put("ar_om", "[$-2001]");
/* 126:153 */     result.put("ar_qa", "[$-4001]");
/* 127:154 */     result.put("ar_sa", "[$-0401]");
/* 128:155 */     result.put("ar_sy", "[$-2801]");
/* 129:156 */     result.put("ar_tn", "[$-1C01]");
/* 130:157 */     result.put("ar_ye", "[$-2401]");
/* 131:158 */     result.put("as", "[$-44D]");
/* 132:159 */     result.put("az_az", "[$-82C]");
/* 133:160 */     result.put("az_az", "[$-42C]");
/* 134:161 */     result.put("be", "[$-0423]");
/* 135:162 */     result.put("bg", "[$-0402]");
/* 136:163 */     result.put("bn", "[$-0845]");
/* 137:164 */     result.put("bn", "[$-0445]");
/* 138:165 */     result.put("bo", "[$-0451]");
/* 139:166 */     result.put("bs", "[$-141A]");
/* 140:167 */     result.put("ca", "[$-0403]");
/* 141:168 */     result.put("cs", "[$-0405]");
/* 142:169 */     result.put("cy", "[$-0452]");
/* 143:170 */     result.put("da", "[$-0406]");
/* 144:171 */     result.put("de_at", "[$-C07]");
/* 145:172 */     result.put("de_ch", "[$-0807]");
/* 146:173 */     result.put("de_de", "[$-0407]");
/* 147:174 */     result.put("de_li", "[$-1407]");
/* 148:175 */     result.put("de_lu", "[$-1007]");
/* 149:176 */     result.put("dv", "[$-0465]");
/* 150:177 */     result.put("el", "[$-0408]");
/* 151:178 */     result.put("en_au", "[$-C09]");
/* 152:179 */     result.put("en_bz", "[$-2809]");
/* 153:180 */     result.put("en_ca", "[$-1009]");
/* 154:181 */     result.put("en_cb", "[$-2409]");
/* 155:182 */     result.put("en_gb", "[$-0809]");
/* 156:183 */     result.put("en_ie", "[$-1809]");
/* 157:184 */     result.put("en_in", "[$-4009]");
/* 158:185 */     result.put("en_jm", "[$-2009]");
/* 159:186 */     result.put("en_nz", "[$-1409]");
/* 160:187 */     result.put("en_ph", "[$-3409]");
/* 161:188 */     result.put("en_tt", "[$-2C09]");
/* 162:189 */     result.put("en_us", "[$-0409]");
/* 163:190 */     result.put("en_za", "[$-1C09]");
/* 164:191 */     result.put("es_ar", "[$-2C0A]");
/* 165:192 */     result.put("es_bo", "[$-400A]");
/* 166:193 */     result.put("es_cl", "[$-340A]");
/* 167:194 */     result.put("es_co", "[$-240A]");
/* 168:195 */     result.put("es_cr", "[$-140A]");
/* 169:196 */     result.put("es_do", "[$-1C0A]");
/* 170:197 */     result.put("es_ec", "[$-300A]");
/* 171:198 */     result.put("es_es", "[$-40A]");
/* 172:199 */     result.put("es_gt", "[$-100A]");
/* 173:200 */     result.put("es_hn", "[$-480A]");
/* 174:201 */     result.put("es_mx", "[$-80A]");
/* 175:202 */     result.put("es_ni", "[$-4C0A]");
/* 176:203 */     result.put("es_pa", "[$-180A]");
/* 177:204 */     result.put("es_pe", "[$-280A]");
/* 178:205 */     result.put("es_pr", "[$-500A]");
/* 179:206 */     result.put("es_py", "[$-3C0A]");
/* 180:207 */     result.put("es_sv", "[$-440A]");
/* 181:208 */     result.put("es_uy", "[$-380A]");
/* 182:209 */     result.put("es_ve", "[$-200A]");
/* 183:210 */     result.put("et", "[$-0425]");
/* 184:211 */     result.put("eu", "[$-42D]");
/* 185:212 */     result.put("fa", "[$-0429]");
/* 186:213 */     result.put("fi", "[$-40B]");
/* 187:214 */     result.put("fo", "[$-0438]");
/* 188:215 */     result.put("fr_be", "[$-80C]");
/* 189:216 */     result.put("fr_ca", "[$-C0C]");
/* 190:217 */     result.put("fr_ch", "[$-100C]");
/* 191:218 */     result.put("fr_fr", "[$-40C]");
/* 192:219 */     result.put("fr_lu", "[$-140C]");
/* 193:220 */     result.put("gd", "[$-43C]");
/* 194:221 */     result.put("gd_ie", "[$-83C]");
/* 195:222 */     result.put("gn", "[$-0474]");
/* 196:223 */     result.put("gu", "[$-0447]");
/* 197:224 */     result.put("he", "[$-40D]");
/* 198:225 */     result.put("hi", "[$-0439]");
/* 199:226 */     result.put("hr", "[$-41A]");
/* 200:227 */     result.put("hu", "[$-40E]");
/* 201:228 */     result.put("hy", "[$-42B]");
/* 202:229 */     result.put("id", "[$-0421]");
/* 203:230 */     result.put("is", "[$-40F]");
/* 204:231 */     result.put("it_ch", "[$-0810]");
/* 205:232 */     result.put("it_it", "[$-0410]");
/* 206:233 */     result.put("ja", "[$-0411]");
/* 207:234 */     result.put("kk", "[$-43F]");
/* 208:235 */     result.put("km", "[$-0453]");
/* 209:236 */     result.put("kn", "[$-44B]");
/* 210:237 */     result.put("ko", "[$-0412]");
/* 211:238 */     result.put("ks", "[$-0460]");
/* 212:239 */     result.put("la", "[$-0476]");
/* 213:240 */     result.put("lo", "[$-0454]");
/* 214:241 */     result.put("lt", "[$-0427]");
/* 215:242 */     result.put("lv", "[$-0426]");
/* 216:243 */     result.put("mi", "[$-0481]");
/* 217:244 */     result.put("mk", "[$-42F]");
/* 218:245 */     result.put("ml", "[$-44C]");
/* 219:246 */     result.put("mn", "[$-0850]");
/* 220:247 */     result.put("mn", "[$-0450]");
/* 221:248 */     result.put("mr", "[$-44E]");
/* 222:249 */     result.put("ms_bn", "[$-83E]");
/* 223:250 */     result.put("ms_my", "[$-43E]");
/* 224:251 */     result.put("mt", "[$-43A]");
/* 225:252 */     result.put("my", "[$-0455]");
/* 226:253 */     result.put("ne", "[$-0461]");
/* 227:254 */     result.put("nl_be", "[$-0813]");
/* 228:255 */     result.put("nl_nl", "[$-0413]");
/* 229:256 */     result.put("no_no", "[$-0814]");
/* 230:257 */     result.put("or", "[$-0448]");
/* 231:258 */     result.put("pa", "[$-0446]");
/* 232:259 */     result.put("pl", "[$-0415]");
/* 233:260 */     result.put("pt_br", "[$-0416]");
/* 234:261 */     result.put("pt_pt", "[$-0816]");
/* 235:262 */     result.put("rm", "[$-0417]");
/* 236:263 */     result.put("ro", "[$-0418]");
/* 237:264 */     result.put("ro_mo", "[$-0818]");
/* 238:265 */     result.put("ru", "[$-0419]");
/* 239:266 */     result.put("ru_mo", "[$-0819]");
/* 240:267 */     result.put("sa", "[$-44F]");
/* 241:268 */     result.put("sb", "[$-42E]");
/* 242:269 */     result.put("sd", "[$-0459]");
/* 243:270 */     result.put("si", "[$-45B]");
/* 244:271 */     result.put("sk", "[$-41B]");
/* 245:272 */     result.put("sl", "[$-0424]");
/* 246:273 */     result.put("so", "[$-0477]");
/* 247:274 */     result.put("sq", "[$-41C]");
/* 248:275 */     result.put("sr_sp", "[$-C1A]");
/* 249:276 */     result.put("sr_sp", "[$-81A]");
/* 250:277 */     result.put("sv_fi", "[$-81D]");
/* 251:278 */     result.put("sv_se", "[$-41D]");
/* 252:279 */     result.put("sw", "[$-0441]");
/* 253:280 */     result.put("ta", "[$-0449]");
/* 254:281 */     result.put("te", "[$-44A]");
/* 255:282 */     result.put("tg", "[$-0428]");
/* 256:283 */     result.put("th", "[$-41E]");
/* 257:284 */     result.put("tk", "[$-0442]");
/* 258:285 */     result.put("tn", "[$-0432]");
/* 259:286 */     result.put("tr", "[$-41F]");
/* 260:287 */     result.put("ts", "[$-0431]");
/* 261:288 */     result.put("tt", "[$-0444]");
/* 262:289 */     result.put("uk", "[$-0422]");
/* 263:290 */     result.put("ur", "[$-0420]");
/* 264:291 */     result.put("UTF_8", "[$-0000]");
/* 265:292 */     result.put("uz_uz", "[$-0843]");
/* 266:293 */     result.put("uz_uz", "[$-0443]");
/* 267:294 */     result.put("vi", "[$-42A]");
/* 268:295 */     result.put("xh", "[$-0434]");
/* 269:296 */     result.put("yi", "[$-43D]");
/* 270:297 */     result.put("zh_cn", "[$-0804]");
/* 271:298 */     result.put("zh_hk", "[$-C04]");
/* 272:299 */     result.put("zh_mo", "[$-1404]");
/* 273:300 */     result.put("zh_sg", "[$-1004]");
/* 274:301 */     result.put("zh_tw", "[$-0404]");
/* 275:302 */     result.put("zu", "[$-0435]");
/* 276:    */     
/* 277:304 */     result.put("ar", "[$-0401]");
/* 278:305 */     result.put("bn", "[$-0845]");
/* 279:306 */     result.put("de", "[$-0407]");
/* 280:307 */     result.put("en", "[$-0409]");
/* 281:308 */     result.put("es", "[$-40A]");
/* 282:309 */     result.put("fr", "[$-40C]");
/* 283:310 */     result.put("it", "[$-0410]");
/* 284:311 */     result.put("ms", "[$-43E]");
/* 285:312 */     result.put("nl", "[$-0413]");
/* 286:313 */     result.put("nn", "[$-0814]");
/* 287:314 */     result.put("no", "[$-0414]");
/* 288:315 */     result.put("pt", "[$-0816]");
/* 289:316 */     result.put("sr", "[$-C1A]");
/* 290:317 */     result.put("sv", "[$-41D]");
/* 291:318 */     result.put("uz", "[$-0843]");
/* 292:319 */     result.put("zh", "[$-0804]");
/* 293:    */     
/* 294:321 */     result.put("ga", "[$-43C]");
/* 295:322 */     result.put("ga_ie", "[$-83C]");
/* 296:323 */     result.put("in", "[$-0421]");
/* 297:324 */     result.put("iw", "[$-40D]");
/* 298:    */     
/* 299:    */ 
/* 300:327 */     result.put("", "[$-0409]");
/* 301:    */     
/* 302:329 */     return result;
/* 303:    */   }
/* 304:    */   
/* 305:    */   public static String getPrefixForLocale(Locale locale)
/* 306:    */   {
/* 307:333 */     String localeString = locale.toString().toLowerCase(locale);
/* 308:334 */     String result = (String)localePrefixes.get(localeString);
/* 309:335 */     if (result == null)
/* 310:    */     {
/* 311:336 */       result = (String)localePrefixes.get(localeString.substring(0, 2));
/* 312:337 */       if (result == null)
/* 313:    */       {
/* 314:338 */         Locale parentLocale = new Locale(localeString.substring(0, 2));
/* 315:339 */         logger.log(7, new Object[] { "Unable to find prefix for " + locale + "(" + locale.getDisplayName(Locale.ROOT) + ") or " + localeString.substring(0, 2) + "(" + parentLocale.getDisplayName(Locale.ROOT) + ")" });
/* 316:    */         
/* 317:341 */         return "";
/* 318:    */       }
/* 319:    */     }
/* 320:344 */     return result;
/* 321:    */   }
/* 322:    */   
/* 323:    */   public static String convert(Locale locale, DateFormat df)
/* 324:    */   {
/* 325:348 */     String ptrn = ((SimpleDateFormat)df).toPattern();
/* 326:349 */     return convert(locale, ptrn);
/* 327:    */   }
/* 328:    */   
/* 329:    */   public static String convert(Locale locale, String format)
/* 330:    */   {
/* 331:353 */     StringBuilder result = new StringBuilder();
/* 332:    */     
/* 333:355 */     result.append(getPrefixForLocale(locale));
/* 334:356 */     DateFormatTokenizer tokenizer = new DateFormatTokenizer(format);
/* 335:    */     String token;
/* 336:358 */     while ((token = tokenizer.getNextToken()) != null) {
/* 337:359 */       if (token.startsWith("'"))
/* 338:    */       {
/* 339:360 */         result.append(token.replaceAll("'", "\""));
/* 340:    */       }
/* 341:361 */       else if (!Character.isLetter(token.charAt(0)))
/* 342:    */       {
/* 343:362 */         result.append(token);
/* 344:    */       }
/* 345:    */       else
/* 346:    */       {
/* 347:365 */         String mappedToken = (String)tokenConversions.get(token);
/* 348:366 */         result.append(mappedToken == null ? token : mappedToken);
/* 349:    */       }
/* 350:    */     }
/* 351:369 */     result.append(";@");
/* 352:370 */     return result.toString().trim();
/* 353:    */   }
/* 354:    */   
/* 355:    */   public static String getJavaDatePattern(int style, Locale locale)
/* 356:    */   {
/* 357:374 */     DateFormat df = DateFormat.getDateInstance(style, locale);
/* 358:375 */     if ((df instanceof SimpleDateFormat)) {
/* 359:376 */       return ((SimpleDateFormat)df).toPattern();
/* 360:    */     }
/* 361:378 */     switch (style)
/* 362:    */     {
/* 363:    */     case 3: 
/* 364:380 */       return "d/MM/yy";
/* 365:    */     case 2: 
/* 366:382 */       return "MMM d, yyyy";
/* 367:    */     case 1: 
/* 368:384 */       return "MMMM d, yyyy";
/* 369:    */     case 0: 
/* 370:386 */       return "dddd, MMMM d, yyyy";
/* 371:    */     }
/* 372:388 */     return "MMM d, yyyy";
/* 373:    */   }
/* 374:    */   
/* 375:    */   public static String getJavaTimePattern(int style, Locale locale)
/* 376:    */   {
/* 377:394 */     DateFormat df = DateFormat.getTimeInstance(style, locale);
/* 378:395 */     if ((df instanceof SimpleDateFormat)) {
/* 379:396 */       return ((SimpleDateFormat)df).toPattern();
/* 380:    */     }
/* 381:398 */     switch (style)
/* 382:    */     {
/* 383:    */     case 3: 
/* 384:400 */       return "h:mm a";
/* 385:    */     case 2: 
/* 386:402 */       return "h:mm:ss a";
/* 387:    */     case 1: 
/* 388:404 */       return "h:mm:ss a";
/* 389:    */     case 0: 
/* 390:406 */       return "h:mm:ss a";
/* 391:    */     }
/* 392:408 */     return "h:mm:ss a";
/* 393:    */   }
/* 394:    */   
/* 395:    */   public static String getJavaDateTimePattern(int style, Locale locale)
/* 396:    */   {
/* 397:414 */     DateFormat df = DateFormat.getDateTimeInstance(style, style, locale);
/* 398:415 */     if ((df instanceof SimpleDateFormat)) {
/* 399:416 */       return ((SimpleDateFormat)df).toPattern();
/* 400:    */     }
/* 401:418 */     switch (style)
/* 402:    */     {
/* 403:    */     case 3: 
/* 404:420 */       return "M/d/yy h:mm a";
/* 405:    */     case 2: 
/* 406:422 */       return "MMM d, yyyy h:mm:ss a";
/* 407:    */     case 1: 
/* 408:424 */       return "MMMM d, yyyy h:mm:ss a";
/* 409:    */     case 0: 
/* 410:426 */       return "dddd, MMMM d, yyyy h:mm:ss a";
/* 411:    */     }
/* 412:428 */     return "MMM d, yyyy h:mm:ss a";
/* 413:    */   }
/* 414:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.DateFormatConverter
 * JD-Core Version:    0.7.0.1
 */