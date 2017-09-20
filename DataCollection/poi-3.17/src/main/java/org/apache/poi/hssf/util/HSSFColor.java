/*    1:     */ package org.apache.poi.hssf.util;
/*    2:     */ 
/*    3:     */ import java.util.Collections;
/*    4:     */ import java.util.EnumMap;
/*    5:     */ import java.util.HashMap;
/*    6:     */ import java.util.Locale;
/*    7:     */ import java.util.Map;
/*    8:     */ import java.util.Map.Entry;
/*    9:     */ import org.apache.poi.util.Removal;
/*   10:     */ 
/*   11:     */ public class HSSFColor
/*   12:     */   implements org.apache.poi.ss.usermodel.Color
/*   13:     */ {
/*   14:     */   private static Map<Integer, HSSFColor> indexHash;
/*   15:     */   private static Map<HSSFColorPredefined, HSSFColor> enumList;
/*   16:     */   private java.awt.Color color;
/*   17:     */   private int index;
/*   18:     */   private int index2;
/*   19:     */   
/*   20:     */   public static enum HSSFColorPredefined
/*   21:     */   {
/*   22:  55 */     BLACK(8, -1, 0),  BROWN(60, -1, 10040064),  OLIVE_GREEN(59, -1, 3355392),  DARK_GREEN(58, -1, 13056),  DARK_TEAL(56, -1, 13158),  DARK_BLUE(18, 32, 128),  INDIGO(62, -1, 3355545),  GREY_80_PERCENT(63, -1, 3355443),  ORANGE(53, -1, 16737792),  DARK_YELLOW(19, -1, 8421376),  GREEN(17, -1, 32768),  TEAL(21, 38, 32896),  BLUE(12, 39, 255),  BLUE_GREY(54, -1, 6710937),  GREY_50_PERCENT(23, -1, 8421504),  RED(10, -1, 16711680),  LIGHT_ORANGE(52, -1, 16750848),  LIME(50, -1, 10079232),  SEA_GREEN(57, -1, 3381606),  AQUA(49, -1, 3394764),  LIGHT_BLUE(48, -1, 3368703),  VIOLET(20, 36, 8388736),  GREY_40_PERCENT(55, -1, 9868950),  PINK(14, 33, 16711935),  GOLD(51, -1, 16763904),  YELLOW(13, 34, 16776960),  BRIGHT_GREEN(11, -1, 65280),  TURQUOISE(15, 35, 65535),  DARK_RED(16, 37, 8388608),  SKY_BLUE(40, -1, 52479),  PLUM(61, 25, 10040166),  GREY_25_PERCENT(22, -1, 12632256),  ROSE(45, -1, 16751052),  LIGHT_YELLOW(43, -1, 16777113),  LIGHT_GREEN(42, -1, 13434828),  LIGHT_TURQUOISE(41, 27, 13434879),  PALE_BLUE(44, -1, 10079487),  LAVENDER(46, -1, 13408767),  WHITE(9, -1, 16777215),  CORNFLOWER_BLUE(24, -1, 10066431),  LEMON_CHIFFON(26, -1, 16777164),  MAROON(25, -1, 8323072),  ORCHID(28, -1, 6684774),  CORAL(29, -1, 16744576),  ROYAL_BLUE(30, -1, 26316),  LIGHT_CORNFLOWER_BLUE(31, -1, 13421823),  TAN(47, -1, 16764057),  AUTOMATIC(64, -1, 0);
/*   23:     */     
/*   24:     */     private HSSFColor color;
/*   25:     */     
/*   26:     */     private HSSFColorPredefined(int index, int index2, int rgb)
/*   27:     */     {
/*   28: 113 */       this.color = new HSSFColor(index, index2, new java.awt.Color(rgb));
/*   29:     */     }
/*   30:     */     
/*   31:     */     public short getIndex()
/*   32:     */     {
/*   33: 120 */       return this.color.getIndex();
/*   34:     */     }
/*   35:     */     
/*   36:     */     public short getIndex2()
/*   37:     */     {
/*   38: 127 */       return this.color.getIndex2();
/*   39:     */     }
/*   40:     */     
/*   41:     */     public short[] getTriplet()
/*   42:     */     {
/*   43: 134 */       return this.color.getTriplet();
/*   44:     */     }
/*   45:     */     
/*   46:     */     public String getHexString()
/*   47:     */     {
/*   48: 141 */       return this.color.getHexString();
/*   49:     */     }
/*   50:     */     
/*   51:     */     public HSSFColor getColor()
/*   52:     */     {
/*   53: 148 */       return new HSSFColor(getIndex(), getIndex2(), this.color.color);
/*   54:     */     }
/*   55:     */   }
/*   56:     */   
/*   57:     */   public HSSFColor()
/*   58:     */   {
/*   59: 156 */     this(64, -1, java.awt.Color.BLACK);
/*   60:     */   }
/*   61:     */   
/*   62:     */   public HSSFColor(int index, int index2, java.awt.Color color)
/*   63:     */   {
/*   64: 160 */     this.index = index;
/*   65: 161 */     this.index2 = index2;
/*   66: 162 */     this.color = color;
/*   67:     */   }
/*   68:     */   
/*   69:     */   public static final synchronized Map<Integer, HSSFColor> getIndexHash()
/*   70:     */   {
/*   71: 172 */     if (indexHash == null) {
/*   72: 173 */       indexHash = Collections.unmodifiableMap(createColorsByIndexMap());
/*   73:     */     }
/*   74: 176 */     return indexHash;
/*   75:     */   }
/*   76:     */   
/*   77:     */   public static final Map<Integer, HSSFColor> getMutableIndexHash()
/*   78:     */   {
/*   79: 185 */     return createColorsByIndexMap();
/*   80:     */   }
/*   81:     */   
/*   82:     */   private static Map<Integer, HSSFColor> createColorsByIndexMap()
/*   83:     */   {
/*   84: 189 */     Map<HSSFColorPredefined, HSSFColor> eList = mapEnumToColorClass();
/*   85: 190 */     Map<Integer, HSSFColor> result = new HashMap(eList.size() * 3 / 2);
/*   86: 192 */     for (Entry<HSSFColorPredefined, HSSFColor> colorRef : eList.entrySet())
/*   87:     */     {
/*   88: 193 */       Integer index1 = Integer.valueOf(((HSSFColorPredefined)colorRef.getKey()).getIndex());
/*   89: 194 */       if (!result.containsKey(index1)) {
/*   90: 195 */         result.put(index1, colorRef.getValue());
/*   91:     */       }
/*   92: 197 */       Integer index2 = Integer.valueOf(((HSSFColorPredefined)colorRef.getKey()).getIndex2());
/*   93: 198 */       if ((index2.intValue() != -1) && (!result.containsKey(index2))) {
/*   94: 199 */         result.put(index2, colorRef.getValue());
/*   95:     */       }
/*   96:     */     }
/*   97: 202 */     return result;
/*   98:     */   }
/*   99:     */   
/*  100:     */   public static Map<String, HSSFColor> getTripletHash()
/*  101:     */   {
/*  102: 216 */     return createColorsByHexStringMap();
/*  103:     */   }
/*  104:     */   
/*  105:     */   private static Map<String, HSSFColor> createColorsByHexStringMap()
/*  106:     */   {
/*  107: 220 */     Map<HSSFColorPredefined, HSSFColor> eList = mapEnumToColorClass();
/*  108: 221 */     Map<String, HSSFColor> result = new HashMap(eList.size());
/*  109: 223 */     for (Entry<HSSFColorPredefined, HSSFColor> colorRef : eList.entrySet())
/*  110:     */     {
/*  111: 224 */       String hexString = ((HSSFColorPredefined)colorRef.getKey()).getHexString();
/*  112: 225 */       if (!result.containsKey(hexString)) {
/*  113: 226 */         result.put(hexString, colorRef.getValue());
/*  114:     */       }
/*  115:     */     }
/*  116: 229 */     return result;
/*  117:     */   }
/*  118:     */   
/*  119:     */   @Deprecated
/*  120:     */   @Removal(version="3.18")
/*  121:     */   private static synchronized Map<HSSFColorPredefined, HSSFColor> mapEnumToColorClass()
/*  122:     */   {
/*  123: 241 */     if (enumList == null)
/*  124:     */     {
/*  125: 242 */       enumList = new EnumMap(HSSFColorPredefined.class);
/*  126:     */       
/*  127: 244 */       enumList.put(HSSFColorPredefined.BLACK, new BLACK());
/*  128: 245 */       enumList.put(HSSFColorPredefined.BROWN, new BROWN());
/*  129: 246 */       enumList.put(HSSFColorPredefined.OLIVE_GREEN, new OLIVE_GREEN());
/*  130: 247 */       enumList.put(HSSFColorPredefined.DARK_GREEN, new DARK_GREEN());
/*  131: 248 */       enumList.put(HSSFColorPredefined.DARK_TEAL, new DARK_TEAL());
/*  132: 249 */       enumList.put(HSSFColorPredefined.DARK_BLUE, new DARK_BLUE());
/*  133: 250 */       enumList.put(HSSFColorPredefined.INDIGO, new INDIGO());
/*  134: 251 */       enumList.put(HSSFColorPredefined.GREY_80_PERCENT, new GREY_80_PERCENT());
/*  135: 252 */       enumList.put(HSSFColorPredefined.ORANGE, new ORANGE());
/*  136: 253 */       enumList.put(HSSFColorPredefined.DARK_YELLOW, new DARK_YELLOW());
/*  137: 254 */       enumList.put(HSSFColorPredefined.GREEN, new GREEN());
/*  138: 255 */       enumList.put(HSSFColorPredefined.TEAL, new TEAL());
/*  139: 256 */       enumList.put(HSSFColorPredefined.BLUE, new BLUE());
/*  140: 257 */       enumList.put(HSSFColorPredefined.BLUE_GREY, new BLUE_GREY());
/*  141: 258 */       enumList.put(HSSFColorPredefined.GREY_50_PERCENT, new GREY_50_PERCENT());
/*  142: 259 */       enumList.put(HSSFColorPredefined.RED, new RED());
/*  143: 260 */       enumList.put(HSSFColorPredefined.LIGHT_ORANGE, new LIGHT_ORANGE());
/*  144: 261 */       enumList.put(HSSFColorPredefined.LIME, new LIME());
/*  145: 262 */       enumList.put(HSSFColorPredefined.SEA_GREEN, new SEA_GREEN());
/*  146: 263 */       enumList.put(HSSFColorPredefined.AQUA, new AQUA());
/*  147: 264 */       enumList.put(HSSFColorPredefined.LIGHT_BLUE, new LIGHT_BLUE());
/*  148: 265 */       enumList.put(HSSFColorPredefined.VIOLET, new VIOLET());
/*  149: 266 */       enumList.put(HSSFColorPredefined.GREY_40_PERCENT, new GREY_40_PERCENT());
/*  150: 267 */       enumList.put(HSSFColorPredefined.PINK, new PINK());
/*  151: 268 */       enumList.put(HSSFColorPredefined.GOLD, new GOLD());
/*  152: 269 */       enumList.put(HSSFColorPredefined.YELLOW, new YELLOW());
/*  153: 270 */       enumList.put(HSSFColorPredefined.BRIGHT_GREEN, new BRIGHT_GREEN());
/*  154: 271 */       enumList.put(HSSFColorPredefined.TURQUOISE, new TURQUOISE());
/*  155: 272 */       enumList.put(HSSFColorPredefined.DARK_RED, new DARK_RED());
/*  156: 273 */       enumList.put(HSSFColorPredefined.SKY_BLUE, new SKY_BLUE());
/*  157: 274 */       enumList.put(HSSFColorPredefined.PLUM, new PLUM());
/*  158: 275 */       enumList.put(HSSFColorPredefined.GREY_25_PERCENT, new GREY_25_PERCENT());
/*  159: 276 */       enumList.put(HSSFColorPredefined.ROSE, new ROSE());
/*  160: 277 */       enumList.put(HSSFColorPredefined.LIGHT_YELLOW, new LIGHT_YELLOW());
/*  161: 278 */       enumList.put(HSSFColorPredefined.LIGHT_GREEN, new LIGHT_GREEN());
/*  162: 279 */       enumList.put(HSSFColorPredefined.LIGHT_TURQUOISE, new LIGHT_TURQUOISE());
/*  163: 280 */       enumList.put(HSSFColorPredefined.PALE_BLUE, new PALE_BLUE());
/*  164: 281 */       enumList.put(HSSFColorPredefined.LAVENDER, new LAVENDER());
/*  165: 282 */       enumList.put(HSSFColorPredefined.WHITE, new WHITE());
/*  166: 283 */       enumList.put(HSSFColorPredefined.CORNFLOWER_BLUE, new CORNFLOWER_BLUE());
/*  167: 284 */       enumList.put(HSSFColorPredefined.LEMON_CHIFFON, new LEMON_CHIFFON());
/*  168: 285 */       enumList.put(HSSFColorPredefined.MAROON, new MAROON());
/*  169: 286 */       enumList.put(HSSFColorPredefined.ORCHID, new ORCHID());
/*  170: 287 */       enumList.put(HSSFColorPredefined.CORAL, new CORAL());
/*  171: 288 */       enumList.put(HSSFColorPredefined.ROYAL_BLUE, new ROYAL_BLUE());
/*  172: 289 */       enumList.put(HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE, new LIGHT_CORNFLOWER_BLUE());
/*  173: 290 */       enumList.put(HSSFColorPredefined.TAN, new TAN());
/*  174:     */     }
/*  175: 292 */     return enumList;
/*  176:     */   }
/*  177:     */   
/*  178:     */   public short getIndex()
/*  179:     */   {
/*  180: 301 */     return (short)this.index;
/*  181:     */   }
/*  182:     */   
/*  183:     */   public short getIndex2()
/*  184:     */   {
/*  185: 310 */     return (short)this.index2;
/*  186:     */   }
/*  187:     */   
/*  188:     */   public short[] getTriplet()
/*  189:     */   {
/*  190: 319 */     return new short[] { (short)this.color.getRed(), (short)this.color.getGreen(), (short)this.color.getBlue() };
/*  191:     */   }
/*  192:     */   
/*  193:     */   public String getHexString()
/*  194:     */   {
/*  195: 328 */     return (Integer.toHexString(this.color.getRed() * 257) + ":" + Integer.toHexString(this.color.getGreen() * 257) + ":" + Integer.toHexString(this.color.getBlue() * 257)).toUpperCase(Locale.ROOT);
/*  196:     */   }
/*  197:     */   
/*  198:     */   public static HSSFColor toHSSFColor(org.apache.poi.ss.usermodel.Color color)
/*  199:     */   {
/*  200: 344 */     if ((color != null) && (!(color instanceof HSSFColor))) {
/*  201: 345 */       throw new IllegalArgumentException("Only HSSFColor objects are supported");
/*  202:     */     }
/*  203: 347 */     return (HSSFColor)color;
/*  204:     */   }
/*  205:     */   
/*  206:     */   @Deprecated
/*  207:     */   @Removal(version="3.18")
/*  208:     */   private static class HSSFColorRef
/*  209:     */     extends HSSFColor
/*  210:     */   {
/*  211:     */     HSSFColorRef(HSSFColorPredefined colorEnum)
/*  212:     */     {
/*  213: 357 */       super(colorEnum.getIndex2(), HSSFColorPredefined.access$100(colorEnum).color);
/*  214:     */     }
/*  215:     */   }
/*  216:     */   
/*  217:     */   @Deprecated
/*  218:     */   @Removal(version="3.18")
/*  219:     */   public static class BLACK
/*  220:     */     extends HSSFColorRef
/*  221:     */   {
/*  222: 367 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.BLACK;
/*  223: 368 */     public static final short index = ref.getIndex();
/*  224: 369 */     public static final int index2 = ref.getIndex2();
/*  225: 370 */     public static final short[] triplet = ref.getTriplet();
/*  226: 371 */     public static final String hexString = ref.getHexString();
/*  227:     */     
/*  228:     */     public BLACK()
/*  229:     */     {
/*  230: 372 */       super();
/*  231:     */     }
/*  232:     */   }
/*  233:     */   
/*  234:     */   @Deprecated
/*  235:     */   @Removal(version="3.18")
/*  236:     */   public static class BROWN
/*  237:     */     extends HSSFColorRef
/*  238:     */   {
/*  239: 381 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.BROWN;
/*  240: 382 */     public static final short index = ref.getIndex();
/*  241: 383 */     public static final int index2 = ref.getIndex2();
/*  242: 384 */     public static final short[] triplet = ref.getTriplet();
/*  243: 385 */     public static final String hexString = ref.getHexString();
/*  244:     */     
/*  245:     */     public BROWN()
/*  246:     */     {
/*  247: 386 */       super();
/*  248:     */     }
/*  249:     */   }
/*  250:     */   
/*  251:     */   @Deprecated
/*  252:     */   @Removal(version="3.18")
/*  253:     */   public static class OLIVE_GREEN
/*  254:     */     extends HSSFColorRef
/*  255:     */   {
/*  256: 395 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.OLIVE_GREEN;
/*  257: 396 */     public static final short index = ref.getIndex();
/*  258: 397 */     public static final int index2 = ref.getIndex2();
/*  259: 398 */     public static final short[] triplet = ref.getTriplet();
/*  260: 399 */     public static final String hexString = ref.getHexString();
/*  261:     */     
/*  262:     */     public OLIVE_GREEN()
/*  263:     */     {
/*  264: 400 */       super();
/*  265:     */     }
/*  266:     */   }
/*  267:     */   
/*  268:     */   @Deprecated
/*  269:     */   @Removal(version="3.18")
/*  270:     */   public static class DARK_GREEN
/*  271:     */     extends HSSFColorRef
/*  272:     */   {
/*  273: 409 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.DARK_GREEN;
/*  274: 410 */     public static final short index = ref.getIndex();
/*  275: 411 */     public static final int index2 = ref.getIndex2();
/*  276: 412 */     public static final short[] triplet = ref.getTriplet();
/*  277: 413 */     public static final String hexString = ref.getHexString();
/*  278:     */     
/*  279:     */     public DARK_GREEN()
/*  280:     */     {
/*  281: 414 */       super();
/*  282:     */     }
/*  283:     */   }
/*  284:     */   
/*  285:     */   @Deprecated
/*  286:     */   @Removal(version="3.18")
/*  287:     */   public static class DARK_TEAL
/*  288:     */     extends HSSFColorRef
/*  289:     */   {
/*  290: 423 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.DARK_TEAL;
/*  291: 424 */     public static final short index = ref.getIndex();
/*  292: 425 */     public static final int index2 = ref.getIndex2();
/*  293: 426 */     public static final short[] triplet = ref.getTriplet();
/*  294: 427 */     public static final String hexString = ref.getHexString();
/*  295:     */     
/*  296:     */     public DARK_TEAL()
/*  297:     */     {
/*  298: 428 */       super();
/*  299:     */     }
/*  300:     */   }
/*  301:     */   
/*  302:     */   @Deprecated
/*  303:     */   @Removal(version="3.18")
/*  304:     */   public static class DARK_BLUE
/*  305:     */     extends HSSFColorRef
/*  306:     */   {
/*  307: 437 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.DARK_BLUE;
/*  308: 438 */     public static final short index = ref.getIndex();
/*  309: 439 */     public static final int index2 = ref.getIndex2();
/*  310: 440 */     public static final short[] triplet = ref.getTriplet();
/*  311: 441 */     public static final String hexString = ref.getHexString();
/*  312:     */     
/*  313:     */     public DARK_BLUE()
/*  314:     */     {
/*  315: 442 */       super();
/*  316:     */     }
/*  317:     */   }
/*  318:     */   
/*  319:     */   @Deprecated
/*  320:     */   @Removal(version="3.18")
/*  321:     */   public static class INDIGO
/*  322:     */     extends HSSFColorRef
/*  323:     */   {
/*  324: 451 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.INDIGO;
/*  325: 452 */     public static final short index = ref.getIndex();
/*  326: 453 */     public static final int index2 = ref.getIndex2();
/*  327: 454 */     public static final short[] triplet = ref.getTriplet();
/*  328: 455 */     public static final String hexString = ref.getHexString();
/*  329:     */     
/*  330:     */     public INDIGO()
/*  331:     */     {
/*  332: 456 */       super();
/*  333:     */     }
/*  334:     */   }
/*  335:     */   
/*  336:     */   @Deprecated
/*  337:     */   @Removal(version="3.18")
/*  338:     */   public static class GREY_80_PERCENT
/*  339:     */     extends HSSFColorRef
/*  340:     */   {
/*  341: 465 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.GREY_80_PERCENT;
/*  342: 466 */     public static final short index = ref.getIndex();
/*  343: 467 */     public static final int index2 = ref.getIndex2();
/*  344: 468 */     public static final short[] triplet = ref.getTriplet();
/*  345: 469 */     public static final String hexString = ref.getHexString();
/*  346:     */     
/*  347:     */     public GREY_80_PERCENT()
/*  348:     */     {
/*  349: 470 */       super();
/*  350:     */     }
/*  351:     */   }
/*  352:     */   
/*  353:     */   @Deprecated
/*  354:     */   @Removal(version="3.18")
/*  355:     */   public static class DARK_RED
/*  356:     */     extends HSSFColorRef
/*  357:     */   {
/*  358: 479 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.DARK_RED;
/*  359: 480 */     public static final short index = ref.getIndex();
/*  360: 481 */     public static final int index2 = ref.getIndex2();
/*  361: 482 */     public static final short[] triplet = ref.getTriplet();
/*  362: 483 */     public static final String hexString = ref.getHexString();
/*  363:     */     
/*  364:     */     public DARK_RED()
/*  365:     */     {
/*  366: 484 */       super();
/*  367:     */     }
/*  368:     */   }
/*  369:     */   
/*  370:     */   @Deprecated
/*  371:     */   @Removal(version="3.18")
/*  372:     */   public static class ORANGE
/*  373:     */     extends HSSFColorRef
/*  374:     */   {
/*  375: 493 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.ORANGE;
/*  376: 494 */     public static final short index = ref.getIndex();
/*  377: 495 */     public static final int index2 = ref.getIndex2();
/*  378: 496 */     public static final short[] triplet = ref.getTriplet();
/*  379: 497 */     public static final String hexString = ref.getHexString();
/*  380:     */     
/*  381:     */     public ORANGE()
/*  382:     */     {
/*  383: 498 */       super();
/*  384:     */     }
/*  385:     */   }
/*  386:     */   
/*  387:     */   @Deprecated
/*  388:     */   @Removal(version="3.18")
/*  389:     */   public static class DARK_YELLOW
/*  390:     */     extends HSSFColorRef
/*  391:     */   {
/*  392: 507 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.DARK_YELLOW;
/*  393: 508 */     public static final short index = ref.getIndex();
/*  394: 509 */     public static final int index2 = ref.getIndex2();
/*  395: 510 */     public static final short[] triplet = ref.getTriplet();
/*  396: 511 */     public static final String hexString = ref.getHexString();
/*  397:     */     
/*  398:     */     public DARK_YELLOW()
/*  399:     */     {
/*  400: 512 */       super();
/*  401:     */     }
/*  402:     */   }
/*  403:     */   
/*  404:     */   @Deprecated
/*  405:     */   @Removal(version="3.18")
/*  406:     */   public static class GREEN
/*  407:     */     extends HSSFColorRef
/*  408:     */   {
/*  409: 521 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.GREEN;
/*  410: 522 */     public static final short index = ref.getIndex();
/*  411: 523 */     public static final int index2 = ref.getIndex2();
/*  412: 524 */     public static final short[] triplet = ref.getTriplet();
/*  413: 525 */     public static final String hexString = ref.getHexString();
/*  414:     */     
/*  415:     */     public GREEN()
/*  416:     */     {
/*  417: 526 */       super();
/*  418:     */     }
/*  419:     */   }
/*  420:     */   
/*  421:     */   @Deprecated
/*  422:     */   @Removal(version="3.18")
/*  423:     */   public static class TEAL
/*  424:     */     extends HSSFColorRef
/*  425:     */   {
/*  426: 535 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.TEAL;
/*  427: 536 */     public static final short index = ref.getIndex();
/*  428: 537 */     public static final int index2 = ref.getIndex2();
/*  429: 538 */     public static final short[] triplet = ref.getTriplet();
/*  430: 539 */     public static final String hexString = ref.getHexString();
/*  431:     */     
/*  432:     */     public TEAL()
/*  433:     */     {
/*  434: 540 */       super();
/*  435:     */     }
/*  436:     */   }
/*  437:     */   
/*  438:     */   @Deprecated
/*  439:     */   @Removal(version="3.18")
/*  440:     */   public static class BLUE
/*  441:     */     extends HSSFColorRef
/*  442:     */   {
/*  443: 549 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.BLUE;
/*  444: 550 */     public static final short index = ref.getIndex();
/*  445: 551 */     public static final int index2 = ref.getIndex2();
/*  446: 552 */     public static final short[] triplet = ref.getTriplet();
/*  447: 553 */     public static final String hexString = ref.getHexString();
/*  448:     */     
/*  449:     */     public BLUE()
/*  450:     */     {
/*  451: 554 */       super();
/*  452:     */     }
/*  453:     */   }
/*  454:     */   
/*  455:     */   @Deprecated
/*  456:     */   @Removal(version="3.18")
/*  457:     */   public static class BLUE_GREY
/*  458:     */     extends HSSFColorRef
/*  459:     */   {
/*  460: 563 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.BLUE_GREY;
/*  461: 564 */     public static final short index = ref.getIndex();
/*  462: 565 */     public static final int index2 = ref.getIndex2();
/*  463: 566 */     public static final short[] triplet = ref.getTriplet();
/*  464: 567 */     public static final String hexString = ref.getHexString();
/*  465:     */     
/*  466:     */     public BLUE_GREY()
/*  467:     */     {
/*  468: 568 */       super();
/*  469:     */     }
/*  470:     */   }
/*  471:     */   
/*  472:     */   @Deprecated
/*  473:     */   @Removal(version="3.18")
/*  474:     */   public static class GREY_50_PERCENT
/*  475:     */     extends HSSFColorRef
/*  476:     */   {
/*  477: 577 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.GREY_50_PERCENT;
/*  478: 578 */     public static final short index = ref.getIndex();
/*  479: 579 */     public static final int index2 = ref.getIndex2();
/*  480: 580 */     public static final short[] triplet = ref.getTriplet();
/*  481: 581 */     public static final String hexString = ref.getHexString();
/*  482:     */     
/*  483:     */     public GREY_50_PERCENT()
/*  484:     */     {
/*  485: 582 */       super();
/*  486:     */     }
/*  487:     */   }
/*  488:     */   
/*  489:     */   @Deprecated
/*  490:     */   @Removal(version="3.18")
/*  491:     */   public static class RED
/*  492:     */     extends HSSFColorRef
/*  493:     */   {
/*  494: 591 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.RED;
/*  495: 592 */     public static final short index = ref.getIndex();
/*  496: 593 */     public static final int index2 = ref.getIndex2();
/*  497: 594 */     public static final short[] triplet = ref.getTriplet();
/*  498: 595 */     public static final String hexString = ref.getHexString();
/*  499:     */     
/*  500:     */     public RED()
/*  501:     */     {
/*  502: 596 */       super();
/*  503:     */     }
/*  504:     */   }
/*  505:     */   
/*  506:     */   @Deprecated
/*  507:     */   @Removal(version="3.18")
/*  508:     */   public static class LIGHT_ORANGE
/*  509:     */     extends HSSFColorRef
/*  510:     */   {
/*  511: 605 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.LIGHT_ORANGE;
/*  512: 606 */     public static final short index = ref.getIndex();
/*  513: 607 */     public static final int index2 = ref.getIndex2();
/*  514: 608 */     public static final short[] triplet = ref.getTriplet();
/*  515: 609 */     public static final String hexString = ref.getHexString();
/*  516:     */     
/*  517:     */     public LIGHT_ORANGE()
/*  518:     */     {
/*  519: 610 */       super();
/*  520:     */     }
/*  521:     */   }
/*  522:     */   
/*  523:     */   @Deprecated
/*  524:     */   @Removal(version="3.18")
/*  525:     */   public static class LIME
/*  526:     */     extends HSSFColorRef
/*  527:     */   {
/*  528: 619 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.LIME;
/*  529: 620 */     public static final short index = ref.getIndex();
/*  530: 621 */     public static final int index2 = ref.getIndex2();
/*  531: 622 */     public static final short[] triplet = ref.getTriplet();
/*  532: 623 */     public static final String hexString = ref.getHexString();
/*  533:     */     
/*  534:     */     public LIME()
/*  535:     */     {
/*  536: 624 */       super();
/*  537:     */     }
/*  538:     */   }
/*  539:     */   
/*  540:     */   @Deprecated
/*  541:     */   @Removal(version="3.18")
/*  542:     */   public static class SEA_GREEN
/*  543:     */     extends HSSFColorRef
/*  544:     */   {
/*  545: 633 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.SEA_GREEN;
/*  546: 634 */     public static final short index = ref.getIndex();
/*  547: 635 */     public static final int index2 = ref.getIndex2();
/*  548: 636 */     public static final short[] triplet = ref.getTriplet();
/*  549: 637 */     public static final String hexString = ref.getHexString();
/*  550:     */     
/*  551:     */     public SEA_GREEN()
/*  552:     */     {
/*  553: 638 */       super();
/*  554:     */     }
/*  555:     */   }
/*  556:     */   
/*  557:     */   @Deprecated
/*  558:     */   @Removal(version="3.18")
/*  559:     */   public static class AQUA
/*  560:     */     extends HSSFColorRef
/*  561:     */   {
/*  562: 647 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.AQUA;
/*  563: 648 */     public static final short index = ref.getIndex();
/*  564: 649 */     public static final int index2 = ref.getIndex2();
/*  565: 650 */     public static final short[] triplet = ref.getTriplet();
/*  566: 651 */     public static final String hexString = ref.getHexString();
/*  567:     */     
/*  568:     */     public AQUA()
/*  569:     */     {
/*  570: 652 */       super();
/*  571:     */     }
/*  572:     */   }
/*  573:     */   
/*  574:     */   @Deprecated
/*  575:     */   @Removal(version="3.18")
/*  576:     */   public static class LIGHT_BLUE
/*  577:     */     extends HSSFColorRef
/*  578:     */   {
/*  579: 661 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.LIGHT_BLUE;
/*  580: 662 */     public static final short index = ref.getIndex();
/*  581: 663 */     public static final int index2 = ref.getIndex2();
/*  582: 664 */     public static final short[] triplet = ref.getTriplet();
/*  583: 665 */     public static final String hexString = ref.getHexString();
/*  584:     */     
/*  585:     */     public LIGHT_BLUE()
/*  586:     */     {
/*  587: 666 */       super();
/*  588:     */     }
/*  589:     */   }
/*  590:     */   
/*  591:     */   @Deprecated
/*  592:     */   @Removal(version="3.18")
/*  593:     */   public static class VIOLET
/*  594:     */     extends HSSFColorRef
/*  595:     */   {
/*  596: 675 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.VIOLET;
/*  597: 676 */     public static final short index = ref.getIndex();
/*  598: 677 */     public static final int index2 = ref.getIndex2();
/*  599: 678 */     public static final short[] triplet = ref.getTriplet();
/*  600: 679 */     public static final String hexString = ref.getHexString();
/*  601:     */     
/*  602:     */     public VIOLET()
/*  603:     */     {
/*  604: 680 */       super();
/*  605:     */     }
/*  606:     */   }
/*  607:     */   
/*  608:     */   @Deprecated
/*  609:     */   @Removal(version="3.18")
/*  610:     */   public static class GREY_40_PERCENT
/*  611:     */     extends HSSFColorRef
/*  612:     */   {
/*  613: 689 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.GREY_40_PERCENT;
/*  614: 690 */     public static final short index = ref.getIndex();
/*  615: 691 */     public static final int index2 = ref.getIndex2();
/*  616: 692 */     public static final short[] triplet = ref.getTriplet();
/*  617: 693 */     public static final String hexString = ref.getHexString();
/*  618:     */     
/*  619:     */     public GREY_40_PERCENT()
/*  620:     */     {
/*  621: 694 */       super();
/*  622:     */     }
/*  623:     */   }
/*  624:     */   
/*  625:     */   @Deprecated
/*  626:     */   @Removal(version="3.18")
/*  627:     */   public static class PINK
/*  628:     */     extends HSSFColorRef
/*  629:     */   {
/*  630: 703 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.PINK;
/*  631: 704 */     public static final short index = ref.getIndex();
/*  632: 705 */     public static final int index2 = ref.getIndex2();
/*  633: 706 */     public static final short[] triplet = ref.getTriplet();
/*  634: 707 */     public static final String hexString = ref.getHexString();
/*  635:     */     
/*  636:     */     public PINK()
/*  637:     */     {
/*  638: 708 */       super();
/*  639:     */     }
/*  640:     */   }
/*  641:     */   
/*  642:     */   @Deprecated
/*  643:     */   @Removal(version="3.18")
/*  644:     */   public static class GOLD
/*  645:     */     extends HSSFColorRef
/*  646:     */   {
/*  647: 717 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.GOLD;
/*  648: 718 */     public static final short index = ref.getIndex();
/*  649: 719 */     public static final int index2 = ref.getIndex2();
/*  650: 720 */     public static final short[] triplet = ref.getTriplet();
/*  651: 721 */     public static final String hexString = ref.getHexString();
/*  652:     */     
/*  653:     */     public GOLD()
/*  654:     */     {
/*  655: 722 */       super();
/*  656:     */     }
/*  657:     */   }
/*  658:     */   
/*  659:     */   @Deprecated
/*  660:     */   @Removal(version="3.18")
/*  661:     */   public static class YELLOW
/*  662:     */     extends HSSFColorRef
/*  663:     */   {
/*  664: 731 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.YELLOW;
/*  665: 732 */     public static final short index = ref.getIndex();
/*  666: 733 */     public static final int index2 = ref.getIndex2();
/*  667: 734 */     public static final short[] triplet = ref.getTriplet();
/*  668: 735 */     public static final String hexString = ref.getHexString();
/*  669:     */     
/*  670:     */     public YELLOW()
/*  671:     */     {
/*  672: 736 */       super();
/*  673:     */     }
/*  674:     */   }
/*  675:     */   
/*  676:     */   @Deprecated
/*  677:     */   @Removal(version="3.18")
/*  678:     */   public static class BRIGHT_GREEN
/*  679:     */     extends HSSFColorRef
/*  680:     */   {
/*  681: 745 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.BRIGHT_GREEN;
/*  682: 746 */     public static final short index = ref.getIndex();
/*  683: 747 */     public static final int index2 = ref.getIndex2();
/*  684: 748 */     public static final short[] triplet = ref.getTriplet();
/*  685: 749 */     public static final String hexString = ref.getHexString();
/*  686:     */     
/*  687:     */     public BRIGHT_GREEN()
/*  688:     */     {
/*  689: 750 */       super();
/*  690:     */     }
/*  691:     */   }
/*  692:     */   
/*  693:     */   @Deprecated
/*  694:     */   @Removal(version="3.18")
/*  695:     */   public static class TURQUOISE
/*  696:     */     extends HSSFColorRef
/*  697:     */   {
/*  698: 759 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.TURQUOISE;
/*  699: 760 */     public static final short index = ref.getIndex();
/*  700: 761 */     public static final int index2 = ref.getIndex2();
/*  701: 762 */     public static final short[] triplet = ref.getTriplet();
/*  702: 763 */     public static final String hexString = ref.getHexString();
/*  703:     */     
/*  704:     */     public TURQUOISE()
/*  705:     */     {
/*  706: 764 */       super();
/*  707:     */     }
/*  708:     */   }
/*  709:     */   
/*  710:     */   @Deprecated
/*  711:     */   @Removal(version="3.18")
/*  712:     */   public static class SKY_BLUE
/*  713:     */     extends HSSFColorRef
/*  714:     */   {
/*  715: 773 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.SKY_BLUE;
/*  716: 774 */     public static final short index = ref.getIndex();
/*  717: 775 */     public static final int index2 = ref.getIndex2();
/*  718: 776 */     public static final short[] triplet = ref.getTriplet();
/*  719: 777 */     public static final String hexString = ref.getHexString();
/*  720:     */     
/*  721:     */     public SKY_BLUE()
/*  722:     */     {
/*  723: 778 */       super();
/*  724:     */     }
/*  725:     */   }
/*  726:     */   
/*  727:     */   @Deprecated
/*  728:     */   @Removal(version="3.18")
/*  729:     */   public static class PLUM
/*  730:     */     extends HSSFColorRef
/*  731:     */   {
/*  732: 787 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.PLUM;
/*  733: 788 */     public static final short index = ref.getIndex();
/*  734: 789 */     public static final int index2 = ref.getIndex2();
/*  735: 790 */     public static final short[] triplet = ref.getTriplet();
/*  736: 791 */     public static final String hexString = ref.getHexString();
/*  737:     */     
/*  738:     */     public PLUM()
/*  739:     */     {
/*  740: 792 */       super();
/*  741:     */     }
/*  742:     */   }
/*  743:     */   
/*  744:     */   @Deprecated
/*  745:     */   @Removal(version="3.18")
/*  746:     */   public static class GREY_25_PERCENT
/*  747:     */     extends HSSFColorRef
/*  748:     */   {
/*  749: 801 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.GREY_25_PERCENT;
/*  750: 802 */     public static final short index = ref.getIndex();
/*  751: 803 */     public static final int index2 = ref.getIndex2();
/*  752: 804 */     public static final short[] triplet = ref.getTriplet();
/*  753: 805 */     public static final String hexString = ref.getHexString();
/*  754:     */     
/*  755:     */     public GREY_25_PERCENT()
/*  756:     */     {
/*  757: 806 */       super();
/*  758:     */     }
/*  759:     */   }
/*  760:     */   
/*  761:     */   @Deprecated
/*  762:     */   @Removal(version="3.18")
/*  763:     */   public static class ROSE
/*  764:     */     extends HSSFColorRef
/*  765:     */   {
/*  766: 815 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.ROSE;
/*  767: 816 */     public static final short index = ref.getIndex();
/*  768: 817 */     public static final int index2 = ref.getIndex2();
/*  769: 818 */     public static final short[] triplet = ref.getTriplet();
/*  770: 819 */     public static final String hexString = ref.getHexString();
/*  771:     */     
/*  772:     */     public ROSE()
/*  773:     */     {
/*  774: 820 */       super();
/*  775:     */     }
/*  776:     */   }
/*  777:     */   
/*  778:     */   @Deprecated
/*  779:     */   @Removal(version="3.18")
/*  780:     */   public static class TAN
/*  781:     */     extends HSSFColorRef
/*  782:     */   {
/*  783: 829 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.TAN;
/*  784: 830 */     public static final short index = ref.getIndex();
/*  785: 831 */     public static final int index2 = ref.getIndex2();
/*  786: 832 */     public static final short[] triplet = ref.getTriplet();
/*  787: 833 */     public static final String hexString = ref.getHexString();
/*  788:     */     
/*  789:     */     public TAN()
/*  790:     */     {
/*  791: 834 */       super();
/*  792:     */     }
/*  793:     */   }
/*  794:     */   
/*  795:     */   @Deprecated
/*  796:     */   @Removal(version="3.18")
/*  797:     */   public static class LIGHT_YELLOW
/*  798:     */     extends HSSFColorRef
/*  799:     */   {
/*  800: 843 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.LIGHT_YELLOW;
/*  801: 844 */     public static final short index = ref.getIndex();
/*  802: 845 */     public static final int index2 = ref.getIndex2();
/*  803: 846 */     public static final short[] triplet = ref.getTriplet();
/*  804: 847 */     public static final String hexString = ref.getHexString();
/*  805:     */     
/*  806:     */     public LIGHT_YELLOW()
/*  807:     */     {
/*  808: 848 */       super();
/*  809:     */     }
/*  810:     */   }
/*  811:     */   
/*  812:     */   @Deprecated
/*  813:     */   @Removal(version="3.18")
/*  814:     */   public static class LIGHT_GREEN
/*  815:     */     extends HSSFColorRef
/*  816:     */   {
/*  817: 857 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.LIGHT_GREEN;
/*  818: 858 */     public static final short index = ref.getIndex();
/*  819: 859 */     public static final int index2 = ref.getIndex2();
/*  820: 860 */     public static final short[] triplet = ref.getTriplet();
/*  821: 861 */     public static final String hexString = ref.getHexString();
/*  822:     */     
/*  823:     */     public LIGHT_GREEN()
/*  824:     */     {
/*  825: 862 */       super();
/*  826:     */     }
/*  827:     */   }
/*  828:     */   
/*  829:     */   @Deprecated
/*  830:     */   @Removal(version="3.18")
/*  831:     */   public static class LIGHT_TURQUOISE
/*  832:     */     extends HSSFColorRef
/*  833:     */   {
/*  834: 871 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.LIGHT_TURQUOISE;
/*  835: 872 */     public static final short index = ref.getIndex();
/*  836: 873 */     public static final int index2 = ref.getIndex2();
/*  837: 874 */     public static final short[] triplet = ref.getTriplet();
/*  838: 875 */     public static final String hexString = ref.getHexString();
/*  839:     */     
/*  840:     */     public LIGHT_TURQUOISE()
/*  841:     */     {
/*  842: 876 */       super();
/*  843:     */     }
/*  844:     */   }
/*  845:     */   
/*  846:     */   @Deprecated
/*  847:     */   @Removal(version="3.18")
/*  848:     */   public static class PALE_BLUE
/*  849:     */     extends HSSFColorRef
/*  850:     */   {
/*  851: 885 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.PALE_BLUE;
/*  852: 886 */     public static final short index = ref.getIndex();
/*  853: 887 */     public static final int index2 = ref.getIndex2();
/*  854: 888 */     public static final short[] triplet = ref.getTriplet();
/*  855: 889 */     public static final String hexString = ref.getHexString();
/*  856:     */     
/*  857:     */     public PALE_BLUE()
/*  858:     */     {
/*  859: 890 */       super();
/*  860:     */     }
/*  861:     */   }
/*  862:     */   
/*  863:     */   @Deprecated
/*  864:     */   @Removal(version="3.18")
/*  865:     */   public static class LAVENDER
/*  866:     */     extends HSSFColorRef
/*  867:     */   {
/*  868: 899 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.LAVENDER;
/*  869: 900 */     public static final short index = ref.getIndex();
/*  870: 901 */     public static final int index2 = ref.getIndex2();
/*  871: 902 */     public static final short[] triplet = ref.getTriplet();
/*  872: 903 */     public static final String hexString = ref.getHexString();
/*  873:     */     
/*  874:     */     public LAVENDER()
/*  875:     */     {
/*  876: 904 */       super();
/*  877:     */     }
/*  878:     */   }
/*  879:     */   
/*  880:     */   @Deprecated
/*  881:     */   @Removal(version="3.18")
/*  882:     */   public static class WHITE
/*  883:     */     extends HSSFColorRef
/*  884:     */   {
/*  885: 913 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.WHITE;
/*  886: 914 */     public static final short index = ref.getIndex();
/*  887: 915 */     public static final int index2 = ref.getIndex2();
/*  888: 916 */     public static final short[] triplet = ref.getTriplet();
/*  889: 917 */     public static final String hexString = ref.getHexString();
/*  890:     */     
/*  891:     */     public WHITE()
/*  892:     */     {
/*  893: 918 */       super();
/*  894:     */     }
/*  895:     */   }
/*  896:     */   
/*  897:     */   @Deprecated
/*  898:     */   @Removal(version="3.18")
/*  899:     */   public static class CORNFLOWER_BLUE
/*  900:     */     extends HSSFColorRef
/*  901:     */   {
/*  902: 927 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.CORNFLOWER_BLUE;
/*  903: 928 */     public static final short index = ref.getIndex();
/*  904: 929 */     public static final int index2 = ref.getIndex2();
/*  905: 930 */     public static final short[] triplet = ref.getTriplet();
/*  906: 931 */     public static final String hexString = ref.getHexString();
/*  907:     */     
/*  908:     */     public CORNFLOWER_BLUE()
/*  909:     */     {
/*  910: 932 */       super();
/*  911:     */     }
/*  912:     */   }
/*  913:     */   
/*  914:     */   @Deprecated
/*  915:     */   @Removal(version="3.18")
/*  916:     */   public static class LEMON_CHIFFON
/*  917:     */     extends HSSFColorRef
/*  918:     */   {
/*  919: 941 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.LEMON_CHIFFON;
/*  920: 942 */     public static final short index = ref.getIndex();
/*  921: 943 */     public static final int index2 = ref.getIndex2();
/*  922: 944 */     public static final short[] triplet = ref.getTriplet();
/*  923: 945 */     public static final String hexString = ref.getHexString();
/*  924:     */     
/*  925:     */     public LEMON_CHIFFON()
/*  926:     */     {
/*  927: 946 */       super();
/*  928:     */     }
/*  929:     */   }
/*  930:     */   
/*  931:     */   @Deprecated
/*  932:     */   @Removal(version="3.18")
/*  933:     */   public static class MAROON
/*  934:     */     extends HSSFColorRef
/*  935:     */   {
/*  936: 955 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.MAROON;
/*  937: 956 */     public static final short index = ref.getIndex();
/*  938: 957 */     public static final int index2 = ref.getIndex2();
/*  939: 958 */     public static final short[] triplet = ref.getTriplet();
/*  940: 959 */     public static final String hexString = ref.getHexString();
/*  941:     */     
/*  942:     */     public MAROON()
/*  943:     */     {
/*  944: 960 */       super();
/*  945:     */     }
/*  946:     */   }
/*  947:     */   
/*  948:     */   @Deprecated
/*  949:     */   @Removal(version="3.18")
/*  950:     */   public static class ORCHID
/*  951:     */     extends HSSFColorRef
/*  952:     */   {
/*  953: 969 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.ORCHID;
/*  954: 970 */     public static final short index = ref.getIndex();
/*  955: 971 */     public static final int index2 = ref.getIndex2();
/*  956: 972 */     public static final short[] triplet = ref.getTriplet();
/*  957: 973 */     public static final String hexString = ref.getHexString();
/*  958:     */     
/*  959:     */     public ORCHID()
/*  960:     */     {
/*  961: 974 */       super();
/*  962:     */     }
/*  963:     */   }
/*  964:     */   
/*  965:     */   @Deprecated
/*  966:     */   @Removal(version="3.18")
/*  967:     */   public static class CORAL
/*  968:     */     extends HSSFColorRef
/*  969:     */   {
/*  970: 983 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.CORAL;
/*  971: 984 */     public static final short index = ref.getIndex();
/*  972: 985 */     public static final int index2 = ref.getIndex2();
/*  973: 986 */     public static final short[] triplet = ref.getTriplet();
/*  974: 987 */     public static final String hexString = ref.getHexString();
/*  975:     */     
/*  976:     */     public CORAL()
/*  977:     */     {
/*  978: 988 */       super();
/*  979:     */     }
/*  980:     */   }
/*  981:     */   
/*  982:     */   @Deprecated
/*  983:     */   @Removal(version="3.18")
/*  984:     */   public static class ROYAL_BLUE
/*  985:     */     extends HSSFColorRef
/*  986:     */   {
/*  987: 997 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.ROYAL_BLUE;
/*  988: 998 */     public static final short index = ref.getIndex();
/*  989: 999 */     public static final int index2 = ref.getIndex2();
/*  990:1000 */     public static final short[] triplet = ref.getTriplet();
/*  991:1001 */     public static final String hexString = ref.getHexString();
/*  992:     */     
/*  993:     */     public ROYAL_BLUE()
/*  994:     */     {
/*  995:1002 */       super();
/*  996:     */     }
/*  997:     */   }
/*  998:     */   
/*  999:     */   @Deprecated
/* 1000:     */   @Removal(version="3.18")
/* 1001:     */   public static class LIGHT_CORNFLOWER_BLUE
/* 1002:     */     extends HSSFColorRef
/* 1003:     */   {
/* 1004:1011 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE;
/* 1005:1012 */     public static final short index = ref.getIndex();
/* 1006:1013 */     public static final int index2 = ref.getIndex2();
/* 1007:1014 */     public static final short[] triplet = ref.getTriplet();
/* 1008:1015 */     public static final String hexString = ref.getHexString();
/* 1009:     */     
/* 1010:     */     public LIGHT_CORNFLOWER_BLUE()
/* 1011:     */     {
/* 1012:1016 */       super();
/* 1013:     */     }
/* 1014:     */   }
/* 1015:     */   
/* 1016:     */   @Deprecated
/* 1017:     */   @Removal(version="3.18")
/* 1018:     */   public static class AUTOMATIC
/* 1019:     */     extends HSSFColorRef
/* 1020:     */   {
/* 1021:1025 */     private static final HSSFColorPredefined ref = HSSFColorPredefined.AUTOMATIC;
/* 1022:1026 */     public static final short index = ref.getIndex();
/* 1023:1027 */     public static final int index2 = ref.getIndex2();
/* 1024:1028 */     public static final short[] triplet = ref.getTriplet();
/* 1025:1029 */     public static final String hexString = ref.getHexString();
/* 1026:     */     
/* 1027:     */     public AUTOMATIC()
/* 1028:     */     {
/* 1029:1030 */       super();
/* 1030:     */     }
/* 1031:     */     
/* 1032:     */     public static HSSFColor getInstance()
/* 1033:     */     {
/* 1034:1033 */       return HSSFColorPredefined.access$100(ref);
/* 1035:     */     }
/* 1036:     */   }
/* 1037:     */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.util.HSSFColor

 * JD-Core Version:    0.7.0.1

 */