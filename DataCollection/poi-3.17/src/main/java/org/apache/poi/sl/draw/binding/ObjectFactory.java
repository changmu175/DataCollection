/*    1:     */ package org.apache.poi.sl.draw.binding;
/*    2:     */ 
/*    3:     */ import javax.xml.bind.JAXBElement;
/*    4:     */ import javax.xml.bind.annotation.XmlElementDecl;
/*    5:     */ import javax.xml.bind.annotation.XmlRegistry;
/*    6:     */ import javax.xml.namespace.QName;
/*    7:     */ 
/*    8:     */ @XmlRegistry
/*    9:     */ public class ObjectFactory
/*   10:     */ {
/*   11:  43 */   private static final QName _CTSRgbColorAlpha_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "alpha");
/*   12:  44 */   private static final QName _CTSRgbColorLum_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lum");
/*   13:  45 */   private static final QName _CTSRgbColorGamma_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "gamma");
/*   14:  46 */   private static final QName _CTSRgbColorInvGamma_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "invGamma");
/*   15:  47 */   private static final QName _CTSRgbColorRedOff_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "redOff");
/*   16:  48 */   private static final QName _CTSRgbColorAlphaMod_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "alphaMod");
/*   17:  49 */   private static final QName _CTSRgbColorAlphaOff_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "alphaOff");
/*   18:  50 */   private static final QName _CTSRgbColorGreenOff_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "greenOff");
/*   19:  51 */   private static final QName _CTSRgbColorRedMod_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "redMod");
/*   20:  52 */   private static final QName _CTSRgbColorHue_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "hue");
/*   21:  53 */   private static final QName _CTSRgbColorSatOff_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "satOff");
/*   22:  54 */   private static final QName _CTSRgbColorGreenMod_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "greenMod");
/*   23:  55 */   private static final QName _CTSRgbColorSat_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "sat");
/*   24:  56 */   private static final QName _CTSRgbColorBlue_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "blue");
/*   25:  57 */   private static final QName _CTSRgbColorRed_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "red");
/*   26:  58 */   private static final QName _CTSRgbColorSatMod_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "satMod");
/*   27:  59 */   private static final QName _CTSRgbColorHueOff_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "hueOff");
/*   28:  60 */   private static final QName _CTSRgbColorBlueMod_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "blueMod");
/*   29:  61 */   private static final QName _CTSRgbColorShade_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "shade");
/*   30:  62 */   private static final QName _CTSRgbColorLumMod_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lumMod");
/*   31:  63 */   private static final QName _CTSRgbColorInv_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "inv");
/*   32:  64 */   private static final QName _CTSRgbColorLumOff_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lumOff");
/*   33:  65 */   private static final QName _CTSRgbColorTint_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tint");
/*   34:  66 */   private static final QName _CTSRgbColorGreen_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "green");
/*   35:  67 */   private static final QName _CTSRgbColorComp_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "comp");
/*   36:  68 */   private static final QName _CTSRgbColorBlueOff_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "blueOff");
/*   37:  69 */   private static final QName _CTSRgbColorHueMod_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "hueMod");
/*   38:  70 */   private static final QName _CTSRgbColorGray_QNAME = new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "gray");
/*   39:     */   
/*   40:     */   public CTPositiveSize2D createCTPositiveSize2D()
/*   41:     */   {
/*   42:  84 */     return new CTPositiveSize2D();
/*   43:     */   }
/*   44:     */   
/*   45:     */   public CTSphereCoords createCTSphereCoords()
/*   46:     */   {
/*   47:  92 */     return new CTSphereCoords();
/*   48:     */   }
/*   49:     */   
/*   50:     */   public CTPositivePercentage createCTPositivePercentage()
/*   51:     */   {
/*   52: 100 */     return new CTPositivePercentage();
/*   53:     */   }
/*   54:     */   
/*   55:     */   public CTAdjPoint2D createCTAdjPoint2D()
/*   56:     */   {
/*   57: 108 */     return new CTAdjPoint2D();
/*   58:     */   }
/*   59:     */   
/*   60:     */   public CTPath2DCubicBezierTo createCTPath2DCubicBezierTo()
/*   61:     */   {
/*   62: 116 */     return new CTPath2DCubicBezierTo();
/*   63:     */   }
/*   64:     */   
/*   65:     */   public CTEmbeddedWAVAudioFile createCTEmbeddedWAVAudioFile()
/*   66:     */   {
/*   67: 124 */     return new CTEmbeddedWAVAudioFile();
/*   68:     */   }
/*   69:     */   
/*   70:     */   public CTPresetGeometry2D createCTPresetGeometry2D()
/*   71:     */   {
/*   72: 132 */     return new CTPresetGeometry2D();
/*   73:     */   }
/*   74:     */   
/*   75:     */   public CTSchemeColor createCTSchemeColor()
/*   76:     */   {
/*   77: 140 */     return new CTSchemeColor();
/*   78:     */   }
/*   79:     */   
/*   80:     */   public CTInverseTransform createCTInverseTransform()
/*   81:     */   {
/*   82: 148 */     return new CTInverseTransform();
/*   83:     */   }
/*   84:     */   
/*   85:     */   public CTScRgbColor createCTScRgbColor()
/*   86:     */   {
/*   87: 156 */     return new CTScRgbColor();
/*   88:     */   }
/*   89:     */   
/*   90:     */   public CTPositiveFixedAngle createCTPositiveFixedAngle()
/*   91:     */   {
/*   92: 164 */     return new CTPositiveFixedAngle();
/*   93:     */   }
/*   94:     */   
/*   95:     */   public CTInverseGammaTransform createCTInverseGammaTransform()
/*   96:     */   {
/*   97: 172 */     return new CTInverseGammaTransform();
/*   98:     */   }
/*   99:     */   
/*  100:     */   public CTColorMRU createCTColorMRU()
/*  101:     */   {
/*  102: 180 */     return new CTColorMRU();
/*  103:     */   }
/*  104:     */   
/*  105:     */   public CTPath2DArcTo createCTPath2DArcTo()
/*  106:     */   {
/*  107: 188 */     return new CTPath2DArcTo();
/*  108:     */   }
/*  109:     */   
/*  110:     */   public CTSystemColor createCTSystemColor()
/*  111:     */   {
/*  112: 196 */     return new CTSystemColor();
/*  113:     */   }
/*  114:     */   
/*  115:     */   public CTGroupTransform2D createCTGroupTransform2D()
/*  116:     */   {
/*  117: 204 */     return new CTGroupTransform2D();
/*  118:     */   }
/*  119:     */   
/*  120:     */   public CTPoint2D createCTPoint2D()
/*  121:     */   {
/*  122: 212 */     return new CTPoint2D();
/*  123:     */   }
/*  124:     */   
/*  125:     */   public CTGeomRect createCTGeomRect()
/*  126:     */   {
/*  127: 220 */     return new CTGeomRect();
/*  128:     */   }
/*  129:     */   
/*  130:     */   public CTScale2D createCTScale2D()
/*  131:     */   {
/*  132: 228 */     return new CTScale2D();
/*  133:     */   }
/*  134:     */   
/*  135:     */   public CTGeomGuide createCTGeomGuide()
/*  136:     */   {
/*  137: 236 */     return new CTGeomGuide();
/*  138:     */   }
/*  139:     */   
/*  140:     */   public CTXYAdjustHandle createCTXYAdjustHandle()
/*  141:     */   {
/*  142: 244 */     return new CTXYAdjustHandle();
/*  143:     */   }
/*  144:     */   
/*  145:     */   public CTCustomGeometry2D createCTCustomGeometry2D()
/*  146:     */   {
/*  147: 252 */     return new CTCustomGeometry2D();
/*  148:     */   }
/*  149:     */   
/*  150:     */   public CTOfficeArtExtension createCTOfficeArtExtension()
/*  151:     */   {
/*  152: 260 */     return new CTOfficeArtExtension();
/*  153:     */   }
/*  154:     */   
/*  155:     */   public CTGrayscaleTransform createCTGrayscaleTransform()
/*  156:     */   {
/*  157: 268 */     return new CTGrayscaleTransform();
/*  158:     */   }
/*  159:     */   
/*  160:     */   public CTPath2DClose createCTPath2DClose()
/*  161:     */   {
/*  162: 276 */     return new CTPath2DClose();
/*  163:     */   }
/*  164:     */   
/*  165:     */   public CTComplementTransform createCTComplementTransform()
/*  166:     */   {
/*  167: 284 */     return new CTComplementTransform();
/*  168:     */   }
/*  169:     */   
/*  170:     */   public CTPoint3D createCTPoint3D()
/*  171:     */   {
/*  172: 292 */     return new CTPoint3D();
/*  173:     */   }
/*  174:     */   
/*  175:     */   public CTPositiveFixedPercentage createCTPositiveFixedPercentage()
/*  176:     */   {
/*  177: 300 */     return new CTPositiveFixedPercentage();
/*  178:     */   }
/*  179:     */   
/*  180:     */   public CTPath2D createCTPath2D()
/*  181:     */   {
/*  182: 308 */     return new CTPath2D();
/*  183:     */   }
/*  184:     */   
/*  185:     */   public CTAdjustHandleList createCTAdjustHandleList()
/*  186:     */   {
/*  187: 316 */     return new CTAdjustHandleList();
/*  188:     */   }
/*  189:     */   
/*  190:     */   public CTConnectionSiteList createCTConnectionSiteList()
/*  191:     */   {
/*  192: 324 */     return new CTConnectionSiteList();
/*  193:     */   }
/*  194:     */   
/*  195:     */   public CTPresetTextShape createCTPresetTextShape()
/*  196:     */   {
/*  197: 332 */     return new CTPresetTextShape();
/*  198:     */   }
/*  199:     */   
/*  200:     */   public CTSRgbColor createCTSRgbColor()
/*  201:     */   {
/*  202: 340 */     return new CTSRgbColor();
/*  203:     */   }
/*  204:     */   
/*  205:     */   public CTPath2DMoveTo createCTPath2DMoveTo()
/*  206:     */   {
/*  207: 348 */     return new CTPath2DMoveTo();
/*  208:     */   }
/*  209:     */   
/*  210:     */   public CTRelativeRect createCTRelativeRect()
/*  211:     */   {
/*  212: 356 */     return new CTRelativeRect();
/*  213:     */   }
/*  214:     */   
/*  215:     */   public CTPath2DList createCTPath2DList()
/*  216:     */   {
/*  217: 364 */     return new CTPath2DList();
/*  218:     */   }
/*  219:     */   
/*  220:     */   public CTPolarAdjustHandle createCTPolarAdjustHandle()
/*  221:     */   {
/*  222: 372 */     return new CTPolarAdjustHandle();
/*  223:     */   }
/*  224:     */   
/*  225:     */   public CTPercentage createCTPercentage()
/*  226:     */   {
/*  227: 380 */     return new CTPercentage();
/*  228:     */   }
/*  229:     */   
/*  230:     */   public CTHslColor createCTHslColor()
/*  231:     */   {
/*  232: 388 */     return new CTHslColor();
/*  233:     */   }
/*  234:     */   
/*  235:     */   public CTRatio createCTRatio()
/*  236:     */   {
/*  237: 396 */     return new CTRatio();
/*  238:     */   }
/*  239:     */   
/*  240:     */   public CTGeomGuideList createCTGeomGuideList()
/*  241:     */   {
/*  242: 404 */     return new CTGeomGuideList();
/*  243:     */   }
/*  244:     */   
/*  245:     */   public CTTransform2D createCTTransform2D()
/*  246:     */   {
/*  247: 412 */     return new CTTransform2D();
/*  248:     */   }
/*  249:     */   
/*  250:     */   public CTGammaTransform createCTGammaTransform()
/*  251:     */   {
/*  252: 420 */     return new CTGammaTransform();
/*  253:     */   }
/*  254:     */   
/*  255:     */   public CTPath2DQuadBezierTo createCTPath2DQuadBezierTo()
/*  256:     */   {
/*  257: 428 */     return new CTPath2DQuadBezierTo();
/*  258:     */   }
/*  259:     */   
/*  260:     */   public CTAngle createCTAngle()
/*  261:     */   {
/*  262: 436 */     return new CTAngle();
/*  263:     */   }
/*  264:     */   
/*  265:     */   public CTConnectionSite createCTConnectionSite()
/*  266:     */   {
/*  267: 444 */     return new CTConnectionSite();
/*  268:     */   }
/*  269:     */   
/*  270:     */   public CTHyperlink createCTHyperlink()
/*  271:     */   {
/*  272: 452 */     return new CTHyperlink();
/*  273:     */   }
/*  274:     */   
/*  275:     */   public CTFixedPercentage createCTFixedPercentage()
/*  276:     */   {
/*  277: 460 */     return new CTFixedPercentage();
/*  278:     */   }
/*  279:     */   
/*  280:     */   public CTPath2DLineTo createCTPath2DLineTo()
/*  281:     */   {
/*  282: 468 */     return new CTPath2DLineTo();
/*  283:     */   }
/*  284:     */   
/*  285:     */   public CTColor createCTColor()
/*  286:     */   {
/*  287: 476 */     return new CTColor();
/*  288:     */   }
/*  289:     */   
/*  290:     */   public CTPresetColor createCTPresetColor()
/*  291:     */   {
/*  292: 484 */     return new CTPresetColor();
/*  293:     */   }
/*  294:     */   
/*  295:     */   public CTVector3D createCTVector3D()
/*  296:     */   {
/*  297: 492 */     return new CTVector3D();
/*  298:     */   }
/*  299:     */   
/*  300:     */   public CTOfficeArtExtensionList createCTOfficeArtExtensionList()
/*  301:     */   {
/*  302: 500 */     return new CTOfficeArtExtensionList();
/*  303:     */   }
/*  304:     */   
/*  305:     */   public CTConnection createCTConnection()
/*  306:     */   {
/*  307: 508 */     return new CTConnection();
/*  308:     */   }
/*  309:     */   
/*  310:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alpha", scope=CTSRgbColor.class)
/*  311:     */   public JAXBElement<CTPositiveFixedPercentage> createCTSRgbColorAlpha(CTPositiveFixedPercentage value)
/*  312:     */   {
/*  313: 517 */     return new JAXBElement(_CTSRgbColorAlpha_QNAME, CTPositiveFixedPercentage.class, CTSRgbColor.class, value);
/*  314:     */   }
/*  315:     */   
/*  316:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lum", scope=CTSRgbColor.class)
/*  317:     */   public JAXBElement<CTPercentage> createCTSRgbColorLum(CTPercentage value)
/*  318:     */   {
/*  319: 526 */     return new JAXBElement(_CTSRgbColorLum_QNAME, CTPercentage.class, CTSRgbColor.class, value);
/*  320:     */   }
/*  321:     */   
/*  322:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="gamma", scope=CTSRgbColor.class)
/*  323:     */   public JAXBElement<CTGammaTransform> createCTSRgbColorGamma(CTGammaTransform value)
/*  324:     */   {
/*  325: 535 */     return new JAXBElement(_CTSRgbColorGamma_QNAME, CTGammaTransform.class, CTSRgbColor.class, value);
/*  326:     */   }
/*  327:     */   
/*  328:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="invGamma", scope=CTSRgbColor.class)
/*  329:     */   public JAXBElement<CTInverseGammaTransform> createCTSRgbColorInvGamma(CTInverseGammaTransform value)
/*  330:     */   {
/*  331: 544 */     return new JAXBElement(_CTSRgbColorInvGamma_QNAME, CTInverseGammaTransform.class, CTSRgbColor.class, value);
/*  332:     */   }
/*  333:     */   
/*  334:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="redOff", scope=CTSRgbColor.class)
/*  335:     */   public JAXBElement<CTPercentage> createCTSRgbColorRedOff(CTPercentage value)
/*  336:     */   {
/*  337: 553 */     return new JAXBElement(_CTSRgbColorRedOff_QNAME, CTPercentage.class, CTSRgbColor.class, value);
/*  338:     */   }
/*  339:     */   
/*  340:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alphaMod", scope=CTSRgbColor.class)
/*  341:     */   public JAXBElement<CTPositivePercentage> createCTSRgbColorAlphaMod(CTPositivePercentage value)
/*  342:     */   {
/*  343: 562 */     return new JAXBElement(_CTSRgbColorAlphaMod_QNAME, CTPositivePercentage.class, CTSRgbColor.class, value);
/*  344:     */   }
/*  345:     */   
/*  346:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alphaOff", scope=CTSRgbColor.class)
/*  347:     */   public JAXBElement<CTFixedPercentage> createCTSRgbColorAlphaOff(CTFixedPercentage value)
/*  348:     */   {
/*  349: 571 */     return new JAXBElement(_CTSRgbColorAlphaOff_QNAME, CTFixedPercentage.class, CTSRgbColor.class, value);
/*  350:     */   }
/*  351:     */   
/*  352:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="greenOff", scope=CTSRgbColor.class)
/*  353:     */   public JAXBElement<CTPercentage> createCTSRgbColorGreenOff(CTPercentage value)
/*  354:     */   {
/*  355: 580 */     return new JAXBElement(_CTSRgbColorGreenOff_QNAME, CTPercentage.class, CTSRgbColor.class, value);
/*  356:     */   }
/*  357:     */   
/*  358:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="redMod", scope=CTSRgbColor.class)
/*  359:     */   public JAXBElement<CTPercentage> createCTSRgbColorRedMod(CTPercentage value)
/*  360:     */   {
/*  361: 589 */     return new JAXBElement(_CTSRgbColorRedMod_QNAME, CTPercentage.class, CTSRgbColor.class, value);
/*  362:     */   }
/*  363:     */   
/*  364:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hue", scope=CTSRgbColor.class)
/*  365:     */   public JAXBElement<CTPositiveFixedAngle> createCTSRgbColorHue(CTPositiveFixedAngle value)
/*  366:     */   {
/*  367: 598 */     return new JAXBElement(_CTSRgbColorHue_QNAME, CTPositiveFixedAngle.class, CTSRgbColor.class, value);
/*  368:     */   }
/*  369:     */   
/*  370:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="satOff", scope=CTSRgbColor.class)
/*  371:     */   public JAXBElement<CTPercentage> createCTSRgbColorSatOff(CTPercentage value)
/*  372:     */   {
/*  373: 607 */     return new JAXBElement(_CTSRgbColorSatOff_QNAME, CTPercentage.class, CTSRgbColor.class, value);
/*  374:     */   }
/*  375:     */   
/*  376:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="greenMod", scope=CTSRgbColor.class)
/*  377:     */   public JAXBElement<CTPercentage> createCTSRgbColorGreenMod(CTPercentage value)
/*  378:     */   {
/*  379: 616 */     return new JAXBElement(_CTSRgbColorGreenMod_QNAME, CTPercentage.class, CTSRgbColor.class, value);
/*  380:     */   }
/*  381:     */   
/*  382:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="sat", scope=CTSRgbColor.class)
/*  383:     */   public JAXBElement<CTPercentage> createCTSRgbColorSat(CTPercentage value)
/*  384:     */   {
/*  385: 625 */     return new JAXBElement(_CTSRgbColorSat_QNAME, CTPercentage.class, CTSRgbColor.class, value);
/*  386:     */   }
/*  387:     */   
/*  388:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blue", scope=CTSRgbColor.class)
/*  389:     */   public JAXBElement<CTPercentage> createCTSRgbColorBlue(CTPercentage value)
/*  390:     */   {
/*  391: 634 */     return new JAXBElement(_CTSRgbColorBlue_QNAME, CTPercentage.class, CTSRgbColor.class, value);
/*  392:     */   }
/*  393:     */   
/*  394:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="red", scope=CTSRgbColor.class)
/*  395:     */   public JAXBElement<CTPercentage> createCTSRgbColorRed(CTPercentage value)
/*  396:     */   {
/*  397: 643 */     return new JAXBElement(_CTSRgbColorRed_QNAME, CTPercentage.class, CTSRgbColor.class, value);
/*  398:     */   }
/*  399:     */   
/*  400:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="satMod", scope=CTSRgbColor.class)
/*  401:     */   public JAXBElement<CTPercentage> createCTSRgbColorSatMod(CTPercentage value)
/*  402:     */   {
/*  403: 652 */     return new JAXBElement(_CTSRgbColorSatMod_QNAME, CTPercentage.class, CTSRgbColor.class, value);
/*  404:     */   }
/*  405:     */   
/*  406:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hueOff", scope=CTSRgbColor.class)
/*  407:     */   public JAXBElement<CTAngle> createCTSRgbColorHueOff(CTAngle value)
/*  408:     */   {
/*  409: 661 */     return new JAXBElement(_CTSRgbColorHueOff_QNAME, CTAngle.class, CTSRgbColor.class, value);
/*  410:     */   }
/*  411:     */   
/*  412:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blueMod", scope=CTSRgbColor.class)
/*  413:     */   public JAXBElement<CTPercentage> createCTSRgbColorBlueMod(CTPercentage value)
/*  414:     */   {
/*  415: 670 */     return new JAXBElement(_CTSRgbColorBlueMod_QNAME, CTPercentage.class, CTSRgbColor.class, value);
/*  416:     */   }
/*  417:     */   
/*  418:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="shade", scope=CTSRgbColor.class)
/*  419:     */   public JAXBElement<CTPositiveFixedPercentage> createCTSRgbColorShade(CTPositiveFixedPercentage value)
/*  420:     */   {
/*  421: 679 */     return new JAXBElement(_CTSRgbColorShade_QNAME, CTPositiveFixedPercentage.class, CTSRgbColor.class, value);
/*  422:     */   }
/*  423:     */   
/*  424:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lumMod", scope=CTSRgbColor.class)
/*  425:     */   public JAXBElement<CTPercentage> createCTSRgbColorLumMod(CTPercentage value)
/*  426:     */   {
/*  427: 688 */     return new JAXBElement(_CTSRgbColorLumMod_QNAME, CTPercentage.class, CTSRgbColor.class, value);
/*  428:     */   }
/*  429:     */   
/*  430:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="inv", scope=CTSRgbColor.class)
/*  431:     */   public JAXBElement<CTInverseTransform> createCTSRgbColorInv(CTInverseTransform value)
/*  432:     */   {
/*  433: 697 */     return new JAXBElement(_CTSRgbColorInv_QNAME, CTInverseTransform.class, CTSRgbColor.class, value);
/*  434:     */   }
/*  435:     */   
/*  436:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lumOff", scope=CTSRgbColor.class)
/*  437:     */   public JAXBElement<CTPercentage> createCTSRgbColorLumOff(CTPercentage value)
/*  438:     */   {
/*  439: 706 */     return new JAXBElement(_CTSRgbColorLumOff_QNAME, CTPercentage.class, CTSRgbColor.class, value);
/*  440:     */   }
/*  441:     */   
/*  442:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="tint", scope=CTSRgbColor.class)
/*  443:     */   public JAXBElement<CTPositiveFixedPercentage> createCTSRgbColorTint(CTPositiveFixedPercentage value)
/*  444:     */   {
/*  445: 715 */     return new JAXBElement(_CTSRgbColorTint_QNAME, CTPositiveFixedPercentage.class, CTSRgbColor.class, value);
/*  446:     */   }
/*  447:     */   
/*  448:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="green", scope=CTSRgbColor.class)
/*  449:     */   public JAXBElement<CTPercentage> createCTSRgbColorGreen(CTPercentage value)
/*  450:     */   {
/*  451: 724 */     return new JAXBElement(_CTSRgbColorGreen_QNAME, CTPercentage.class, CTSRgbColor.class, value);
/*  452:     */   }
/*  453:     */   
/*  454:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="comp", scope=CTSRgbColor.class)
/*  455:     */   public JAXBElement<CTComplementTransform> createCTSRgbColorComp(CTComplementTransform value)
/*  456:     */   {
/*  457: 733 */     return new JAXBElement(_CTSRgbColorComp_QNAME, CTComplementTransform.class, CTSRgbColor.class, value);
/*  458:     */   }
/*  459:     */   
/*  460:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blueOff", scope=CTSRgbColor.class)
/*  461:     */   public JAXBElement<CTPercentage> createCTSRgbColorBlueOff(CTPercentage value)
/*  462:     */   {
/*  463: 742 */     return new JAXBElement(_CTSRgbColorBlueOff_QNAME, CTPercentage.class, CTSRgbColor.class, value);
/*  464:     */   }
/*  465:     */   
/*  466:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hueMod", scope=CTSRgbColor.class)
/*  467:     */   public JAXBElement<CTPositivePercentage> createCTSRgbColorHueMod(CTPositivePercentage value)
/*  468:     */   {
/*  469: 751 */     return new JAXBElement(_CTSRgbColorHueMod_QNAME, CTPositivePercentage.class, CTSRgbColor.class, value);
/*  470:     */   }
/*  471:     */   
/*  472:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="gray", scope=CTSRgbColor.class)
/*  473:     */   public JAXBElement<CTGrayscaleTransform> createCTSRgbColorGray(CTGrayscaleTransform value)
/*  474:     */   {
/*  475: 760 */     return new JAXBElement(_CTSRgbColorGray_QNAME, CTGrayscaleTransform.class, CTSRgbColor.class, value);
/*  476:     */   }
/*  477:     */   
/*  478:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lum", scope=CTSystemColor.class)
/*  479:     */   public JAXBElement<CTPercentage> createCTSystemColorLum(CTPercentage value)
/*  480:     */   {
/*  481: 769 */     return new JAXBElement(_CTSRgbColorLum_QNAME, CTPercentage.class, CTSystemColor.class, value);
/*  482:     */   }
/*  483:     */   
/*  484:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alpha", scope=CTSystemColor.class)
/*  485:     */   public JAXBElement<CTPositiveFixedPercentage> createCTSystemColorAlpha(CTPositiveFixedPercentage value)
/*  486:     */   {
/*  487: 778 */     return new JAXBElement(_CTSRgbColorAlpha_QNAME, CTPositiveFixedPercentage.class, CTSystemColor.class, value);
/*  488:     */   }
/*  489:     */   
/*  490:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="gamma", scope=CTSystemColor.class)
/*  491:     */   public JAXBElement<CTGammaTransform> createCTSystemColorGamma(CTGammaTransform value)
/*  492:     */   {
/*  493: 787 */     return new JAXBElement(_CTSRgbColorGamma_QNAME, CTGammaTransform.class, CTSystemColor.class, value);
/*  494:     */   }
/*  495:     */   
/*  496:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="invGamma", scope=CTSystemColor.class)
/*  497:     */   public JAXBElement<CTInverseGammaTransform> createCTSystemColorInvGamma(CTInverseGammaTransform value)
/*  498:     */   {
/*  499: 796 */     return new JAXBElement(_CTSRgbColorInvGamma_QNAME, CTInverseGammaTransform.class, CTSystemColor.class, value);
/*  500:     */   }
/*  501:     */   
/*  502:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alphaMod", scope=CTSystemColor.class)
/*  503:     */   public JAXBElement<CTPositivePercentage> createCTSystemColorAlphaMod(CTPositivePercentage value)
/*  504:     */   {
/*  505: 805 */     return new JAXBElement(_CTSRgbColorAlphaMod_QNAME, CTPositivePercentage.class, CTSystemColor.class, value);
/*  506:     */   }
/*  507:     */   
/*  508:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="redOff", scope=CTSystemColor.class)
/*  509:     */   public JAXBElement<CTPercentage> createCTSystemColorRedOff(CTPercentage value)
/*  510:     */   {
/*  511: 814 */     return new JAXBElement(_CTSRgbColorRedOff_QNAME, CTPercentage.class, CTSystemColor.class, value);
/*  512:     */   }
/*  513:     */   
/*  514:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alphaOff", scope=CTSystemColor.class)
/*  515:     */   public JAXBElement<CTFixedPercentage> createCTSystemColorAlphaOff(CTFixedPercentage value)
/*  516:     */   {
/*  517: 823 */     return new JAXBElement(_CTSRgbColorAlphaOff_QNAME, CTFixedPercentage.class, CTSystemColor.class, value);
/*  518:     */   }
/*  519:     */   
/*  520:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="greenOff", scope=CTSystemColor.class)
/*  521:     */   public JAXBElement<CTPercentage> createCTSystemColorGreenOff(CTPercentage value)
/*  522:     */   {
/*  523: 832 */     return new JAXBElement(_CTSRgbColorGreenOff_QNAME, CTPercentage.class, CTSystemColor.class, value);
/*  524:     */   }
/*  525:     */   
/*  526:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="redMod", scope=CTSystemColor.class)
/*  527:     */   public JAXBElement<CTPercentage> createCTSystemColorRedMod(CTPercentage value)
/*  528:     */   {
/*  529: 841 */     return new JAXBElement(_CTSRgbColorRedMod_QNAME, CTPercentage.class, CTSystemColor.class, value);
/*  530:     */   }
/*  531:     */   
/*  532:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hue", scope=CTSystemColor.class)
/*  533:     */   public JAXBElement<CTPositiveFixedAngle> createCTSystemColorHue(CTPositiveFixedAngle value)
/*  534:     */   {
/*  535: 850 */     return new JAXBElement(_CTSRgbColorHue_QNAME, CTPositiveFixedAngle.class, CTSystemColor.class, value);
/*  536:     */   }
/*  537:     */   
/*  538:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="satOff", scope=CTSystemColor.class)
/*  539:     */   public JAXBElement<CTPercentage> createCTSystemColorSatOff(CTPercentage value)
/*  540:     */   {
/*  541: 859 */     return new JAXBElement(_CTSRgbColorSatOff_QNAME, CTPercentage.class, CTSystemColor.class, value);
/*  542:     */   }
/*  543:     */   
/*  544:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="greenMod", scope=CTSystemColor.class)
/*  545:     */   public JAXBElement<CTPercentage> createCTSystemColorGreenMod(CTPercentage value)
/*  546:     */   {
/*  547: 868 */     return new JAXBElement(_CTSRgbColorGreenMod_QNAME, CTPercentage.class, CTSystemColor.class, value);
/*  548:     */   }
/*  549:     */   
/*  550:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blue", scope=CTSystemColor.class)
/*  551:     */   public JAXBElement<CTPercentage> createCTSystemColorBlue(CTPercentage value)
/*  552:     */   {
/*  553: 877 */     return new JAXBElement(_CTSRgbColorBlue_QNAME, CTPercentage.class, CTSystemColor.class, value);
/*  554:     */   }
/*  555:     */   
/*  556:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="sat", scope=CTSystemColor.class)
/*  557:     */   public JAXBElement<CTPercentage> createCTSystemColorSat(CTPercentage value)
/*  558:     */   {
/*  559: 886 */     return new JAXBElement(_CTSRgbColorSat_QNAME, CTPercentage.class, CTSystemColor.class, value);
/*  560:     */   }
/*  561:     */   
/*  562:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="red", scope=CTSystemColor.class)
/*  563:     */   public JAXBElement<CTPercentage> createCTSystemColorRed(CTPercentage value)
/*  564:     */   {
/*  565: 895 */     return new JAXBElement(_CTSRgbColorRed_QNAME, CTPercentage.class, CTSystemColor.class, value);
/*  566:     */   }
/*  567:     */   
/*  568:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="satMod", scope=CTSystemColor.class)
/*  569:     */   public JAXBElement<CTPercentage> createCTSystemColorSatMod(CTPercentage value)
/*  570:     */   {
/*  571: 904 */     return new JAXBElement(_CTSRgbColorSatMod_QNAME, CTPercentage.class, CTSystemColor.class, value);
/*  572:     */   }
/*  573:     */   
/*  574:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blueMod", scope=CTSystemColor.class)
/*  575:     */   public JAXBElement<CTPercentage> createCTSystemColorBlueMod(CTPercentage value)
/*  576:     */   {
/*  577: 913 */     return new JAXBElement(_CTSRgbColorBlueMod_QNAME, CTPercentage.class, CTSystemColor.class, value);
/*  578:     */   }
/*  579:     */   
/*  580:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hueOff", scope=CTSystemColor.class)
/*  581:     */   public JAXBElement<CTAngle> createCTSystemColorHueOff(CTAngle value)
/*  582:     */   {
/*  583: 922 */     return new JAXBElement(_CTSRgbColorHueOff_QNAME, CTAngle.class, CTSystemColor.class, value);
/*  584:     */   }
/*  585:     */   
/*  586:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="shade", scope=CTSystemColor.class)
/*  587:     */   public JAXBElement<CTPositiveFixedPercentage> createCTSystemColorShade(CTPositiveFixedPercentage value)
/*  588:     */   {
/*  589: 931 */     return new JAXBElement(_CTSRgbColorShade_QNAME, CTPositiveFixedPercentage.class, CTSystemColor.class, value);
/*  590:     */   }
/*  591:     */   
/*  592:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lumMod", scope=CTSystemColor.class)
/*  593:     */   public JAXBElement<CTPercentage> createCTSystemColorLumMod(CTPercentage value)
/*  594:     */   {
/*  595: 940 */     return new JAXBElement(_CTSRgbColorLumMod_QNAME, CTPercentage.class, CTSystemColor.class, value);
/*  596:     */   }
/*  597:     */   
/*  598:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="inv", scope=CTSystemColor.class)
/*  599:     */   public JAXBElement<CTInverseTransform> createCTSystemColorInv(CTInverseTransform value)
/*  600:     */   {
/*  601: 949 */     return new JAXBElement(_CTSRgbColorInv_QNAME, CTInverseTransform.class, CTSystemColor.class, value);
/*  602:     */   }
/*  603:     */   
/*  604:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lumOff", scope=CTSystemColor.class)
/*  605:     */   public JAXBElement<CTPercentage> createCTSystemColorLumOff(CTPercentage value)
/*  606:     */   {
/*  607: 958 */     return new JAXBElement(_CTSRgbColorLumOff_QNAME, CTPercentage.class, CTSystemColor.class, value);
/*  608:     */   }
/*  609:     */   
/*  610:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="tint", scope=CTSystemColor.class)
/*  611:     */   public JAXBElement<CTPositiveFixedPercentage> createCTSystemColorTint(CTPositiveFixedPercentage value)
/*  612:     */   {
/*  613: 967 */     return new JAXBElement(_CTSRgbColorTint_QNAME, CTPositiveFixedPercentage.class, CTSystemColor.class, value);
/*  614:     */   }
/*  615:     */   
/*  616:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="green", scope=CTSystemColor.class)
/*  617:     */   public JAXBElement<CTPercentage> createCTSystemColorGreen(CTPercentage value)
/*  618:     */   {
/*  619: 976 */     return new JAXBElement(_CTSRgbColorGreen_QNAME, CTPercentage.class, CTSystemColor.class, value);
/*  620:     */   }
/*  621:     */   
/*  622:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="comp", scope=CTSystemColor.class)
/*  623:     */   public JAXBElement<CTComplementTransform> createCTSystemColorComp(CTComplementTransform value)
/*  624:     */   {
/*  625: 985 */     return new JAXBElement(_CTSRgbColorComp_QNAME, CTComplementTransform.class, CTSystemColor.class, value);
/*  626:     */   }
/*  627:     */   
/*  628:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blueOff", scope=CTSystemColor.class)
/*  629:     */   public JAXBElement<CTPercentage> createCTSystemColorBlueOff(CTPercentage value)
/*  630:     */   {
/*  631: 994 */     return new JAXBElement(_CTSRgbColorBlueOff_QNAME, CTPercentage.class, CTSystemColor.class, value);
/*  632:     */   }
/*  633:     */   
/*  634:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hueMod", scope=CTSystemColor.class)
/*  635:     */   public JAXBElement<CTPositivePercentage> createCTSystemColorHueMod(CTPositivePercentage value)
/*  636:     */   {
/*  637:1003 */     return new JAXBElement(_CTSRgbColorHueMod_QNAME, CTPositivePercentage.class, CTSystemColor.class, value);
/*  638:     */   }
/*  639:     */   
/*  640:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="gray", scope=CTSystemColor.class)
/*  641:     */   public JAXBElement<CTGrayscaleTransform> createCTSystemColorGray(CTGrayscaleTransform value)
/*  642:     */   {
/*  643:1012 */     return new JAXBElement(_CTSRgbColorGray_QNAME, CTGrayscaleTransform.class, CTSystemColor.class, value);
/*  644:     */   }
/*  645:     */   
/*  646:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lum", scope=CTSchemeColor.class)
/*  647:     */   public JAXBElement<CTPercentage> createCTSchemeColorLum(CTPercentage value)
/*  648:     */   {
/*  649:1021 */     return new JAXBElement(_CTSRgbColorLum_QNAME, CTPercentage.class, CTSchemeColor.class, value);
/*  650:     */   }
/*  651:     */   
/*  652:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alpha", scope=CTSchemeColor.class)
/*  653:     */   public JAXBElement<CTPositiveFixedPercentage> createCTSchemeColorAlpha(CTPositiveFixedPercentage value)
/*  654:     */   {
/*  655:1030 */     return new JAXBElement(_CTSRgbColorAlpha_QNAME, CTPositiveFixedPercentage.class, CTSchemeColor.class, value);
/*  656:     */   }
/*  657:     */   
/*  658:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="gamma", scope=CTSchemeColor.class)
/*  659:     */   public JAXBElement<CTGammaTransform> createCTSchemeColorGamma(CTGammaTransform value)
/*  660:     */   {
/*  661:1039 */     return new JAXBElement(_CTSRgbColorGamma_QNAME, CTGammaTransform.class, CTSchemeColor.class, value);
/*  662:     */   }
/*  663:     */   
/*  664:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="invGamma", scope=CTSchemeColor.class)
/*  665:     */   public JAXBElement<CTInverseGammaTransform> createCTSchemeColorInvGamma(CTInverseGammaTransform value)
/*  666:     */   {
/*  667:1048 */     return new JAXBElement(_CTSRgbColorInvGamma_QNAME, CTInverseGammaTransform.class, CTSchemeColor.class, value);
/*  668:     */   }
/*  669:     */   
/*  670:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="redOff", scope=CTSchemeColor.class)
/*  671:     */   public JAXBElement<CTPercentage> createCTSchemeColorRedOff(CTPercentage value)
/*  672:     */   {
/*  673:1057 */     return new JAXBElement(_CTSRgbColorRedOff_QNAME, CTPercentage.class, CTSchemeColor.class, value);
/*  674:     */   }
/*  675:     */   
/*  676:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alphaMod", scope=CTSchemeColor.class)
/*  677:     */   public JAXBElement<CTPositivePercentage> createCTSchemeColorAlphaMod(CTPositivePercentage value)
/*  678:     */   {
/*  679:1066 */     return new JAXBElement(_CTSRgbColorAlphaMod_QNAME, CTPositivePercentage.class, CTSchemeColor.class, value);
/*  680:     */   }
/*  681:     */   
/*  682:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alphaOff", scope=CTSchemeColor.class)
/*  683:     */   public JAXBElement<CTFixedPercentage> createCTSchemeColorAlphaOff(CTFixedPercentage value)
/*  684:     */   {
/*  685:1075 */     return new JAXBElement(_CTSRgbColorAlphaOff_QNAME, CTFixedPercentage.class, CTSchemeColor.class, value);
/*  686:     */   }
/*  687:     */   
/*  688:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="greenOff", scope=CTSchemeColor.class)
/*  689:     */   public JAXBElement<CTPercentage> createCTSchemeColorGreenOff(CTPercentage value)
/*  690:     */   {
/*  691:1084 */     return new JAXBElement(_CTSRgbColorGreenOff_QNAME, CTPercentage.class, CTSchemeColor.class, value);
/*  692:     */   }
/*  693:     */   
/*  694:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hue", scope=CTSchemeColor.class)
/*  695:     */   public JAXBElement<CTPositiveFixedAngle> createCTSchemeColorHue(CTPositiveFixedAngle value)
/*  696:     */   {
/*  697:1093 */     return new JAXBElement(_CTSRgbColorHue_QNAME, CTPositiveFixedAngle.class, CTSchemeColor.class, value);
/*  698:     */   }
/*  699:     */   
/*  700:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="redMod", scope=CTSchemeColor.class)
/*  701:     */   public JAXBElement<CTPercentage> createCTSchemeColorRedMod(CTPercentage value)
/*  702:     */   {
/*  703:1102 */     return new JAXBElement(_CTSRgbColorRedMod_QNAME, CTPercentage.class, CTSchemeColor.class, value);
/*  704:     */   }
/*  705:     */   
/*  706:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="satOff", scope=CTSchemeColor.class)
/*  707:     */   public JAXBElement<CTPercentage> createCTSchemeColorSatOff(CTPercentage value)
/*  708:     */   {
/*  709:1111 */     return new JAXBElement(_CTSRgbColorSatOff_QNAME, CTPercentage.class, CTSchemeColor.class, value);
/*  710:     */   }
/*  711:     */   
/*  712:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="greenMod", scope=CTSchemeColor.class)
/*  713:     */   public JAXBElement<CTPercentage> createCTSchemeColorGreenMod(CTPercentage value)
/*  714:     */   {
/*  715:1120 */     return new JAXBElement(_CTSRgbColorGreenMod_QNAME, CTPercentage.class, CTSchemeColor.class, value);
/*  716:     */   }
/*  717:     */   
/*  718:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blue", scope=CTSchemeColor.class)
/*  719:     */   public JAXBElement<CTPercentage> createCTSchemeColorBlue(CTPercentage value)
/*  720:     */   {
/*  721:1129 */     return new JAXBElement(_CTSRgbColorBlue_QNAME, CTPercentage.class, CTSchemeColor.class, value);
/*  722:     */   }
/*  723:     */   
/*  724:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="sat", scope=CTSchemeColor.class)
/*  725:     */   public JAXBElement<CTPercentage> createCTSchemeColorSat(CTPercentage value)
/*  726:     */   {
/*  727:1138 */     return new JAXBElement(_CTSRgbColorSat_QNAME, CTPercentage.class, CTSchemeColor.class, value);
/*  728:     */   }
/*  729:     */   
/*  730:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="red", scope=CTSchemeColor.class)
/*  731:     */   public JAXBElement<CTPercentage> createCTSchemeColorRed(CTPercentage value)
/*  732:     */   {
/*  733:1147 */     return new JAXBElement(_CTSRgbColorRed_QNAME, CTPercentage.class, CTSchemeColor.class, value);
/*  734:     */   }
/*  735:     */   
/*  736:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="satMod", scope=CTSchemeColor.class)
/*  737:     */   public JAXBElement<CTPercentage> createCTSchemeColorSatMod(CTPercentage value)
/*  738:     */   {
/*  739:1156 */     return new JAXBElement(_CTSRgbColorSatMod_QNAME, CTPercentage.class, CTSchemeColor.class, value);
/*  740:     */   }
/*  741:     */   
/*  742:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hueOff", scope=CTSchemeColor.class)
/*  743:     */   public JAXBElement<CTAngle> createCTSchemeColorHueOff(CTAngle value)
/*  744:     */   {
/*  745:1165 */     return new JAXBElement(_CTSRgbColorHueOff_QNAME, CTAngle.class, CTSchemeColor.class, value);
/*  746:     */   }
/*  747:     */   
/*  748:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blueMod", scope=CTSchemeColor.class)
/*  749:     */   public JAXBElement<CTPercentage> createCTSchemeColorBlueMod(CTPercentage value)
/*  750:     */   {
/*  751:1174 */     return new JAXBElement(_CTSRgbColorBlueMod_QNAME, CTPercentage.class, CTSchemeColor.class, value);
/*  752:     */   }
/*  753:     */   
/*  754:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="shade", scope=CTSchemeColor.class)
/*  755:     */   public JAXBElement<CTPositiveFixedPercentage> createCTSchemeColorShade(CTPositiveFixedPercentage value)
/*  756:     */   {
/*  757:1183 */     return new JAXBElement(_CTSRgbColorShade_QNAME, CTPositiveFixedPercentage.class, CTSchemeColor.class, value);
/*  758:     */   }
/*  759:     */   
/*  760:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lumMod", scope=CTSchemeColor.class)
/*  761:     */   public JAXBElement<CTPercentage> createCTSchemeColorLumMod(CTPercentage value)
/*  762:     */   {
/*  763:1192 */     return new JAXBElement(_CTSRgbColorLumMod_QNAME, CTPercentage.class, CTSchemeColor.class, value);
/*  764:     */   }
/*  765:     */   
/*  766:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="inv", scope=CTSchemeColor.class)
/*  767:     */   public JAXBElement<CTInverseTransform> createCTSchemeColorInv(CTInverseTransform value)
/*  768:     */   {
/*  769:1201 */     return new JAXBElement(_CTSRgbColorInv_QNAME, CTInverseTransform.class, CTSchemeColor.class, value);
/*  770:     */   }
/*  771:     */   
/*  772:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lumOff", scope=CTSchemeColor.class)
/*  773:     */   public JAXBElement<CTPercentage> createCTSchemeColorLumOff(CTPercentage value)
/*  774:     */   {
/*  775:1210 */     return new JAXBElement(_CTSRgbColorLumOff_QNAME, CTPercentage.class, CTSchemeColor.class, value);
/*  776:     */   }
/*  777:     */   
/*  778:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="tint", scope=CTSchemeColor.class)
/*  779:     */   public JAXBElement<CTPositiveFixedPercentage> createCTSchemeColorTint(CTPositiveFixedPercentage value)
/*  780:     */   {
/*  781:1219 */     return new JAXBElement(_CTSRgbColorTint_QNAME, CTPositiveFixedPercentage.class, CTSchemeColor.class, value);
/*  782:     */   }
/*  783:     */   
/*  784:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="green", scope=CTSchemeColor.class)
/*  785:     */   public JAXBElement<CTPercentage> createCTSchemeColorGreen(CTPercentage value)
/*  786:     */   {
/*  787:1228 */     return new JAXBElement(_CTSRgbColorGreen_QNAME, CTPercentage.class, CTSchemeColor.class, value);
/*  788:     */   }
/*  789:     */   
/*  790:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="comp", scope=CTSchemeColor.class)
/*  791:     */   public JAXBElement<CTComplementTransform> createCTSchemeColorComp(CTComplementTransform value)
/*  792:     */   {
/*  793:1237 */     return new JAXBElement(_CTSRgbColorComp_QNAME, CTComplementTransform.class, CTSchemeColor.class, value);
/*  794:     */   }
/*  795:     */   
/*  796:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blueOff", scope=CTSchemeColor.class)
/*  797:     */   public JAXBElement<CTPercentage> createCTSchemeColorBlueOff(CTPercentage value)
/*  798:     */   {
/*  799:1246 */     return new JAXBElement(_CTSRgbColorBlueOff_QNAME, CTPercentage.class, CTSchemeColor.class, value);
/*  800:     */   }
/*  801:     */   
/*  802:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hueMod", scope=CTSchemeColor.class)
/*  803:     */   public JAXBElement<CTPositivePercentage> createCTSchemeColorHueMod(CTPositivePercentage value)
/*  804:     */   {
/*  805:1255 */     return new JAXBElement(_CTSRgbColorHueMod_QNAME, CTPositivePercentage.class, CTSchemeColor.class, value);
/*  806:     */   }
/*  807:     */   
/*  808:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="gray", scope=CTSchemeColor.class)
/*  809:     */   public JAXBElement<CTGrayscaleTransform> createCTSchemeColorGray(CTGrayscaleTransform value)
/*  810:     */   {
/*  811:1264 */     return new JAXBElement(_CTSRgbColorGray_QNAME, CTGrayscaleTransform.class, CTSchemeColor.class, value);
/*  812:     */   }
/*  813:     */   
/*  814:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lum", scope=CTScRgbColor.class)
/*  815:     */   public JAXBElement<CTPercentage> createCTScRgbColorLum(CTPercentage value)
/*  816:     */   {
/*  817:1273 */     return new JAXBElement(_CTSRgbColorLum_QNAME, CTPercentage.class, CTScRgbColor.class, value);
/*  818:     */   }
/*  819:     */   
/*  820:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alpha", scope=CTScRgbColor.class)
/*  821:     */   public JAXBElement<CTPositiveFixedPercentage> createCTScRgbColorAlpha(CTPositiveFixedPercentage value)
/*  822:     */   {
/*  823:1282 */     return new JAXBElement(_CTSRgbColorAlpha_QNAME, CTPositiveFixedPercentage.class, CTScRgbColor.class, value);
/*  824:     */   }
/*  825:     */   
/*  826:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="gamma", scope=CTScRgbColor.class)
/*  827:     */   public JAXBElement<CTGammaTransform> createCTScRgbColorGamma(CTGammaTransform value)
/*  828:     */   {
/*  829:1291 */     return new JAXBElement(_CTSRgbColorGamma_QNAME, CTGammaTransform.class, CTScRgbColor.class, value);
/*  830:     */   }
/*  831:     */   
/*  832:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="invGamma", scope=CTScRgbColor.class)
/*  833:     */   public JAXBElement<CTInverseGammaTransform> createCTScRgbColorInvGamma(CTInverseGammaTransform value)
/*  834:     */   {
/*  835:1300 */     return new JAXBElement(_CTSRgbColorInvGamma_QNAME, CTInverseGammaTransform.class, CTScRgbColor.class, value);
/*  836:     */   }
/*  837:     */   
/*  838:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="redOff", scope=CTScRgbColor.class)
/*  839:     */   public JAXBElement<CTPercentage> createCTScRgbColorRedOff(CTPercentage value)
/*  840:     */   {
/*  841:1309 */     return new JAXBElement(_CTSRgbColorRedOff_QNAME, CTPercentage.class, CTScRgbColor.class, value);
/*  842:     */   }
/*  843:     */   
/*  844:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alphaMod", scope=CTScRgbColor.class)
/*  845:     */   public JAXBElement<CTPositivePercentage> createCTScRgbColorAlphaMod(CTPositivePercentage value)
/*  846:     */   {
/*  847:1318 */     return new JAXBElement(_CTSRgbColorAlphaMod_QNAME, CTPositivePercentage.class, CTScRgbColor.class, value);
/*  848:     */   }
/*  849:     */   
/*  850:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alphaOff", scope=CTScRgbColor.class)
/*  851:     */   public JAXBElement<CTFixedPercentage> createCTScRgbColorAlphaOff(CTFixedPercentage value)
/*  852:     */   {
/*  853:1327 */     return new JAXBElement(_CTSRgbColorAlphaOff_QNAME, CTFixedPercentage.class, CTScRgbColor.class, value);
/*  854:     */   }
/*  855:     */   
/*  856:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="greenOff", scope=CTScRgbColor.class)
/*  857:     */   public JAXBElement<CTPercentage> createCTScRgbColorGreenOff(CTPercentage value)
/*  858:     */   {
/*  859:1336 */     return new JAXBElement(_CTSRgbColorGreenOff_QNAME, CTPercentage.class, CTScRgbColor.class, value);
/*  860:     */   }
/*  861:     */   
/*  862:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hue", scope=CTScRgbColor.class)
/*  863:     */   public JAXBElement<CTPositiveFixedAngle> createCTScRgbColorHue(CTPositiveFixedAngle value)
/*  864:     */   {
/*  865:1345 */     return new JAXBElement(_CTSRgbColorHue_QNAME, CTPositiveFixedAngle.class, CTScRgbColor.class, value);
/*  866:     */   }
/*  867:     */   
/*  868:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="redMod", scope=CTScRgbColor.class)
/*  869:     */   public JAXBElement<CTPercentage> createCTScRgbColorRedMod(CTPercentage value)
/*  870:     */   {
/*  871:1354 */     return new JAXBElement(_CTSRgbColorRedMod_QNAME, CTPercentage.class, CTScRgbColor.class, value);
/*  872:     */   }
/*  873:     */   
/*  874:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="satOff", scope=CTScRgbColor.class)
/*  875:     */   public JAXBElement<CTPercentage> createCTScRgbColorSatOff(CTPercentage value)
/*  876:     */   {
/*  877:1363 */     return new JAXBElement(_CTSRgbColorSatOff_QNAME, CTPercentage.class, CTScRgbColor.class, value);
/*  878:     */   }
/*  879:     */   
/*  880:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="greenMod", scope=CTScRgbColor.class)
/*  881:     */   public JAXBElement<CTPercentage> createCTScRgbColorGreenMod(CTPercentage value)
/*  882:     */   {
/*  883:1372 */     return new JAXBElement(_CTSRgbColorGreenMod_QNAME, CTPercentage.class, CTScRgbColor.class, value);
/*  884:     */   }
/*  885:     */   
/*  886:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="sat", scope=CTScRgbColor.class)
/*  887:     */   public JAXBElement<CTPercentage> createCTScRgbColorSat(CTPercentage value)
/*  888:     */   {
/*  889:1381 */     return new JAXBElement(_CTSRgbColorSat_QNAME, CTPercentage.class, CTScRgbColor.class, value);
/*  890:     */   }
/*  891:     */   
/*  892:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blue", scope=CTScRgbColor.class)
/*  893:     */   public JAXBElement<CTPercentage> createCTScRgbColorBlue(CTPercentage value)
/*  894:     */   {
/*  895:1390 */     return new JAXBElement(_CTSRgbColorBlue_QNAME, CTPercentage.class, CTScRgbColor.class, value);
/*  896:     */   }
/*  897:     */   
/*  898:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="red", scope=CTScRgbColor.class)
/*  899:     */   public JAXBElement<CTPercentage> createCTScRgbColorRed(CTPercentage value)
/*  900:     */   {
/*  901:1399 */     return new JAXBElement(_CTSRgbColorRed_QNAME, CTPercentage.class, CTScRgbColor.class, value);
/*  902:     */   }
/*  903:     */   
/*  904:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="satMod", scope=CTScRgbColor.class)
/*  905:     */   public JAXBElement<CTPercentage> createCTScRgbColorSatMod(CTPercentage value)
/*  906:     */   {
/*  907:1408 */     return new JAXBElement(_CTSRgbColorSatMod_QNAME, CTPercentage.class, CTScRgbColor.class, value);
/*  908:     */   }
/*  909:     */   
/*  910:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hueOff", scope=CTScRgbColor.class)
/*  911:     */   public JAXBElement<CTAngle> createCTScRgbColorHueOff(CTAngle value)
/*  912:     */   {
/*  913:1417 */     return new JAXBElement(_CTSRgbColorHueOff_QNAME, CTAngle.class, CTScRgbColor.class, value);
/*  914:     */   }
/*  915:     */   
/*  916:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blueMod", scope=CTScRgbColor.class)
/*  917:     */   public JAXBElement<CTPercentage> createCTScRgbColorBlueMod(CTPercentage value)
/*  918:     */   {
/*  919:1426 */     return new JAXBElement(_CTSRgbColorBlueMod_QNAME, CTPercentage.class, CTScRgbColor.class, value);
/*  920:     */   }
/*  921:     */   
/*  922:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="shade", scope=CTScRgbColor.class)
/*  923:     */   public JAXBElement<CTPositiveFixedPercentage> createCTScRgbColorShade(CTPositiveFixedPercentage value)
/*  924:     */   {
/*  925:1435 */     return new JAXBElement(_CTSRgbColorShade_QNAME, CTPositiveFixedPercentage.class, CTScRgbColor.class, value);
/*  926:     */   }
/*  927:     */   
/*  928:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lumMod", scope=CTScRgbColor.class)
/*  929:     */   public JAXBElement<CTPercentage> createCTScRgbColorLumMod(CTPercentage value)
/*  930:     */   {
/*  931:1444 */     return new JAXBElement(_CTSRgbColorLumMod_QNAME, CTPercentage.class, CTScRgbColor.class, value);
/*  932:     */   }
/*  933:     */   
/*  934:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="inv", scope=CTScRgbColor.class)
/*  935:     */   public JAXBElement<CTInverseTransform> createCTScRgbColorInv(CTInverseTransform value)
/*  936:     */   {
/*  937:1453 */     return new JAXBElement(_CTSRgbColorInv_QNAME, CTInverseTransform.class, CTScRgbColor.class, value);
/*  938:     */   }
/*  939:     */   
/*  940:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lumOff", scope=CTScRgbColor.class)
/*  941:     */   public JAXBElement<CTPercentage> createCTScRgbColorLumOff(CTPercentage value)
/*  942:     */   {
/*  943:1462 */     return new JAXBElement(_CTSRgbColorLumOff_QNAME, CTPercentage.class, CTScRgbColor.class, value);
/*  944:     */   }
/*  945:     */   
/*  946:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="tint", scope=CTScRgbColor.class)
/*  947:     */   public JAXBElement<CTPositiveFixedPercentage> createCTScRgbColorTint(CTPositiveFixedPercentage value)
/*  948:     */   {
/*  949:1471 */     return new JAXBElement(_CTSRgbColorTint_QNAME, CTPositiveFixedPercentage.class, CTScRgbColor.class, value);
/*  950:     */   }
/*  951:     */   
/*  952:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="green", scope=CTScRgbColor.class)
/*  953:     */   public JAXBElement<CTPercentage> createCTScRgbColorGreen(CTPercentage value)
/*  954:     */   {
/*  955:1480 */     return new JAXBElement(_CTSRgbColorGreen_QNAME, CTPercentage.class, CTScRgbColor.class, value);
/*  956:     */   }
/*  957:     */   
/*  958:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="comp", scope=CTScRgbColor.class)
/*  959:     */   public JAXBElement<CTComplementTransform> createCTScRgbColorComp(CTComplementTransform value)
/*  960:     */   {
/*  961:1489 */     return new JAXBElement(_CTSRgbColorComp_QNAME, CTComplementTransform.class, CTScRgbColor.class, value);
/*  962:     */   }
/*  963:     */   
/*  964:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blueOff", scope=CTScRgbColor.class)
/*  965:     */   public JAXBElement<CTPercentage> createCTScRgbColorBlueOff(CTPercentage value)
/*  966:     */   {
/*  967:1498 */     return new JAXBElement(_CTSRgbColorBlueOff_QNAME, CTPercentage.class, CTScRgbColor.class, value);
/*  968:     */   }
/*  969:     */   
/*  970:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hueMod", scope=CTScRgbColor.class)
/*  971:     */   public JAXBElement<CTPositivePercentage> createCTScRgbColorHueMod(CTPositivePercentage value)
/*  972:     */   {
/*  973:1507 */     return new JAXBElement(_CTSRgbColorHueMod_QNAME, CTPositivePercentage.class, CTScRgbColor.class, value);
/*  974:     */   }
/*  975:     */   
/*  976:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="gray", scope=CTScRgbColor.class)
/*  977:     */   public JAXBElement<CTGrayscaleTransform> createCTScRgbColorGray(CTGrayscaleTransform value)
/*  978:     */   {
/*  979:1516 */     return new JAXBElement(_CTSRgbColorGray_QNAME, CTGrayscaleTransform.class, CTScRgbColor.class, value);
/*  980:     */   }
/*  981:     */   
/*  982:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alpha", scope=CTHslColor.class)
/*  983:     */   public JAXBElement<CTPositiveFixedPercentage> createCTHslColorAlpha(CTPositiveFixedPercentage value)
/*  984:     */   {
/*  985:1525 */     return new JAXBElement(_CTSRgbColorAlpha_QNAME, CTPositiveFixedPercentage.class, CTHslColor.class, value);
/*  986:     */   }
/*  987:     */   
/*  988:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lum", scope=CTHslColor.class)
/*  989:     */   public JAXBElement<CTPercentage> createCTHslColorLum(CTPercentage value)
/*  990:     */   {
/*  991:1534 */     return new JAXBElement(_CTSRgbColorLum_QNAME, CTPercentage.class, CTHslColor.class, value);
/*  992:     */   }
/*  993:     */   
/*  994:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="gamma", scope=CTHslColor.class)
/*  995:     */   public JAXBElement<CTGammaTransform> createCTHslColorGamma(CTGammaTransform value)
/*  996:     */   {
/*  997:1543 */     return new JAXBElement(_CTSRgbColorGamma_QNAME, CTGammaTransform.class, CTHslColor.class, value);
/*  998:     */   }
/*  999:     */   
/* 1000:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="invGamma", scope=CTHslColor.class)
/* 1001:     */   public JAXBElement<CTInverseGammaTransform> createCTHslColorInvGamma(CTInverseGammaTransform value)
/* 1002:     */   {
/* 1003:1552 */     return new JAXBElement(_CTSRgbColorInvGamma_QNAME, CTInverseGammaTransform.class, CTHslColor.class, value);
/* 1004:     */   }
/* 1005:     */   
/* 1006:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alphaMod", scope=CTHslColor.class)
/* 1007:     */   public JAXBElement<CTPositivePercentage> createCTHslColorAlphaMod(CTPositivePercentage value)
/* 1008:     */   {
/* 1009:1561 */     return new JAXBElement(_CTSRgbColorAlphaMod_QNAME, CTPositivePercentage.class, CTHslColor.class, value);
/* 1010:     */   }
/* 1011:     */   
/* 1012:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="redOff", scope=CTHslColor.class)
/* 1013:     */   public JAXBElement<CTPercentage> createCTHslColorRedOff(CTPercentage value)
/* 1014:     */   {
/* 1015:1570 */     return new JAXBElement(_CTSRgbColorRedOff_QNAME, CTPercentage.class, CTHslColor.class, value);
/* 1016:     */   }
/* 1017:     */   
/* 1018:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alphaOff", scope=CTHslColor.class)
/* 1019:     */   public JAXBElement<CTFixedPercentage> createCTHslColorAlphaOff(CTFixedPercentage value)
/* 1020:     */   {
/* 1021:1579 */     return new JAXBElement(_CTSRgbColorAlphaOff_QNAME, CTFixedPercentage.class, CTHslColor.class, value);
/* 1022:     */   }
/* 1023:     */   
/* 1024:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="greenOff", scope=CTHslColor.class)
/* 1025:     */   public JAXBElement<CTPercentage> createCTHslColorGreenOff(CTPercentage value)
/* 1026:     */   {
/* 1027:1588 */     return new JAXBElement(_CTSRgbColorGreenOff_QNAME, CTPercentage.class, CTHslColor.class, value);
/* 1028:     */   }
/* 1029:     */   
/* 1030:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hue", scope=CTHslColor.class)
/* 1031:     */   public JAXBElement<CTPositiveFixedAngle> createCTHslColorHue(CTPositiveFixedAngle value)
/* 1032:     */   {
/* 1033:1597 */     return new JAXBElement(_CTSRgbColorHue_QNAME, CTPositiveFixedAngle.class, CTHslColor.class, value);
/* 1034:     */   }
/* 1035:     */   
/* 1036:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="redMod", scope=CTHslColor.class)
/* 1037:     */   public JAXBElement<CTPercentage> createCTHslColorRedMod(CTPercentage value)
/* 1038:     */   {
/* 1039:1606 */     return new JAXBElement(_CTSRgbColorRedMod_QNAME, CTPercentage.class, CTHslColor.class, value);
/* 1040:     */   }
/* 1041:     */   
/* 1042:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="satOff", scope=CTHslColor.class)
/* 1043:     */   public JAXBElement<CTPercentage> createCTHslColorSatOff(CTPercentage value)
/* 1044:     */   {
/* 1045:1615 */     return new JAXBElement(_CTSRgbColorSatOff_QNAME, CTPercentage.class, CTHslColor.class, value);
/* 1046:     */   }
/* 1047:     */   
/* 1048:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="greenMod", scope=CTHslColor.class)
/* 1049:     */   public JAXBElement<CTPercentage> createCTHslColorGreenMod(CTPercentage value)
/* 1050:     */   {
/* 1051:1624 */     return new JAXBElement(_CTSRgbColorGreenMod_QNAME, CTPercentage.class, CTHslColor.class, value);
/* 1052:     */   }
/* 1053:     */   
/* 1054:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blue", scope=CTHslColor.class)
/* 1055:     */   public JAXBElement<CTPercentage> createCTHslColorBlue(CTPercentage value)
/* 1056:     */   {
/* 1057:1633 */     return new JAXBElement(_CTSRgbColorBlue_QNAME, CTPercentage.class, CTHslColor.class, value);
/* 1058:     */   }
/* 1059:     */   
/* 1060:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="sat", scope=CTHslColor.class)
/* 1061:     */   public JAXBElement<CTPercentage> createCTHslColorSat(CTPercentage value)
/* 1062:     */   {
/* 1063:1642 */     return new JAXBElement(_CTSRgbColorSat_QNAME, CTPercentage.class, CTHslColor.class, value);
/* 1064:     */   }
/* 1065:     */   
/* 1066:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="red", scope=CTHslColor.class)
/* 1067:     */   public JAXBElement<CTPercentage> createCTHslColorRed(CTPercentage value)
/* 1068:     */   {
/* 1069:1651 */     return new JAXBElement(_CTSRgbColorRed_QNAME, CTPercentage.class, CTHslColor.class, value);
/* 1070:     */   }
/* 1071:     */   
/* 1072:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="satMod", scope=CTHslColor.class)
/* 1073:     */   public JAXBElement<CTPercentage> createCTHslColorSatMod(CTPercentage value)
/* 1074:     */   {
/* 1075:1660 */     return new JAXBElement(_CTSRgbColorSatMod_QNAME, CTPercentage.class, CTHslColor.class, value);
/* 1076:     */   }
/* 1077:     */   
/* 1078:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blueMod", scope=CTHslColor.class)
/* 1079:     */   public JAXBElement<CTPercentage> createCTHslColorBlueMod(CTPercentage value)
/* 1080:     */   {
/* 1081:1669 */     return new JAXBElement(_CTSRgbColorBlueMod_QNAME, CTPercentage.class, CTHslColor.class, value);
/* 1082:     */   }
/* 1083:     */   
/* 1084:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hueOff", scope=CTHslColor.class)
/* 1085:     */   public JAXBElement<CTAngle> createCTHslColorHueOff(CTAngle value)
/* 1086:     */   {
/* 1087:1678 */     return new JAXBElement(_CTSRgbColorHueOff_QNAME, CTAngle.class, CTHslColor.class, value);
/* 1088:     */   }
/* 1089:     */   
/* 1090:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="shade", scope=CTHslColor.class)
/* 1091:     */   public JAXBElement<CTPositiveFixedPercentage> createCTHslColorShade(CTPositiveFixedPercentage value)
/* 1092:     */   {
/* 1093:1687 */     return new JAXBElement(_CTSRgbColorShade_QNAME, CTPositiveFixedPercentage.class, CTHslColor.class, value);
/* 1094:     */   }
/* 1095:     */   
/* 1096:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lumMod", scope=CTHslColor.class)
/* 1097:     */   public JAXBElement<CTPercentage> createCTHslColorLumMod(CTPercentage value)
/* 1098:     */   {
/* 1099:1696 */     return new JAXBElement(_CTSRgbColorLumMod_QNAME, CTPercentage.class, CTHslColor.class, value);
/* 1100:     */   }
/* 1101:     */   
/* 1102:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="inv", scope=CTHslColor.class)
/* 1103:     */   public JAXBElement<CTInverseTransform> createCTHslColorInv(CTInverseTransform value)
/* 1104:     */   {
/* 1105:1705 */     return new JAXBElement(_CTSRgbColorInv_QNAME, CTInverseTransform.class, CTHslColor.class, value);
/* 1106:     */   }
/* 1107:     */   
/* 1108:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lumOff", scope=CTHslColor.class)
/* 1109:     */   public JAXBElement<CTPercentage> createCTHslColorLumOff(CTPercentage value)
/* 1110:     */   {
/* 1111:1714 */     return new JAXBElement(_CTSRgbColorLumOff_QNAME, CTPercentage.class, CTHslColor.class, value);
/* 1112:     */   }
/* 1113:     */   
/* 1114:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="tint", scope=CTHslColor.class)
/* 1115:     */   public JAXBElement<CTPositiveFixedPercentage> createCTHslColorTint(CTPositiveFixedPercentage value)
/* 1116:     */   {
/* 1117:1723 */     return new JAXBElement(_CTSRgbColorTint_QNAME, CTPositiveFixedPercentage.class, CTHslColor.class, value);
/* 1118:     */   }
/* 1119:     */   
/* 1120:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="green", scope=CTHslColor.class)
/* 1121:     */   public JAXBElement<CTPercentage> createCTHslColorGreen(CTPercentage value)
/* 1122:     */   {
/* 1123:1732 */     return new JAXBElement(_CTSRgbColorGreen_QNAME, CTPercentage.class, CTHslColor.class, value);
/* 1124:     */   }
/* 1125:     */   
/* 1126:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="comp", scope=CTHslColor.class)
/* 1127:     */   public JAXBElement<CTComplementTransform> createCTHslColorComp(CTComplementTransform value)
/* 1128:     */   {
/* 1129:1741 */     return new JAXBElement(_CTSRgbColorComp_QNAME, CTComplementTransform.class, CTHslColor.class, value);
/* 1130:     */   }
/* 1131:     */   
/* 1132:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blueOff", scope=CTHslColor.class)
/* 1133:     */   public JAXBElement<CTPercentage> createCTHslColorBlueOff(CTPercentage value)
/* 1134:     */   {
/* 1135:1750 */     return new JAXBElement(_CTSRgbColorBlueOff_QNAME, CTPercentage.class, CTHslColor.class, value);
/* 1136:     */   }
/* 1137:     */   
/* 1138:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hueMod", scope=CTHslColor.class)
/* 1139:     */   public JAXBElement<CTPositivePercentage> createCTHslColorHueMod(CTPositivePercentage value)
/* 1140:     */   {
/* 1141:1759 */     return new JAXBElement(_CTSRgbColorHueMod_QNAME, CTPositivePercentage.class, CTHslColor.class, value);
/* 1142:     */   }
/* 1143:     */   
/* 1144:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="gray", scope=CTHslColor.class)
/* 1145:     */   public JAXBElement<CTGrayscaleTransform> createCTHslColorGray(CTGrayscaleTransform value)
/* 1146:     */   {
/* 1147:1768 */     return new JAXBElement(_CTSRgbColorGray_QNAME, CTGrayscaleTransform.class, CTHslColor.class, value);
/* 1148:     */   }
/* 1149:     */   
/* 1150:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lum", scope=CTPresetColor.class)
/* 1151:     */   public JAXBElement<CTPercentage> createCTPresetColorLum(CTPercentage value)
/* 1152:     */   {
/* 1153:1777 */     return new JAXBElement(_CTSRgbColorLum_QNAME, CTPercentage.class, CTPresetColor.class, value);
/* 1154:     */   }
/* 1155:     */   
/* 1156:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alpha", scope=CTPresetColor.class)
/* 1157:     */   public JAXBElement<CTPositiveFixedPercentage> createCTPresetColorAlpha(CTPositiveFixedPercentage value)
/* 1158:     */   {
/* 1159:1786 */     return new JAXBElement(_CTSRgbColorAlpha_QNAME, CTPositiveFixedPercentage.class, CTPresetColor.class, value);
/* 1160:     */   }
/* 1161:     */   
/* 1162:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="gamma", scope=CTPresetColor.class)
/* 1163:     */   public JAXBElement<CTGammaTransform> createCTPresetColorGamma(CTGammaTransform value)
/* 1164:     */   {
/* 1165:1795 */     return new JAXBElement(_CTSRgbColorGamma_QNAME, CTGammaTransform.class, CTPresetColor.class, value);
/* 1166:     */   }
/* 1167:     */   
/* 1168:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="invGamma", scope=CTPresetColor.class)
/* 1169:     */   public JAXBElement<CTInverseGammaTransform> createCTPresetColorInvGamma(CTInverseGammaTransform value)
/* 1170:     */   {
/* 1171:1804 */     return new JAXBElement(_CTSRgbColorInvGamma_QNAME, CTInverseGammaTransform.class, CTPresetColor.class, value);
/* 1172:     */   }
/* 1173:     */   
/* 1174:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="redOff", scope=CTPresetColor.class)
/* 1175:     */   public JAXBElement<CTPercentage> createCTPresetColorRedOff(CTPercentage value)
/* 1176:     */   {
/* 1177:1813 */     return new JAXBElement(_CTSRgbColorRedOff_QNAME, CTPercentage.class, CTPresetColor.class, value);
/* 1178:     */   }
/* 1179:     */   
/* 1180:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alphaMod", scope=CTPresetColor.class)
/* 1181:     */   public JAXBElement<CTPositivePercentage> createCTPresetColorAlphaMod(CTPositivePercentage value)
/* 1182:     */   {
/* 1183:1822 */     return new JAXBElement(_CTSRgbColorAlphaMod_QNAME, CTPositivePercentage.class, CTPresetColor.class, value);
/* 1184:     */   }
/* 1185:     */   
/* 1186:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="alphaOff", scope=CTPresetColor.class)
/* 1187:     */   public JAXBElement<CTFixedPercentage> createCTPresetColorAlphaOff(CTFixedPercentage value)
/* 1188:     */   {
/* 1189:1831 */     return new JAXBElement(_CTSRgbColorAlphaOff_QNAME, CTFixedPercentage.class, CTPresetColor.class, value);
/* 1190:     */   }
/* 1191:     */   
/* 1192:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="greenOff", scope=CTPresetColor.class)
/* 1193:     */   public JAXBElement<CTPercentage> createCTPresetColorGreenOff(CTPercentage value)
/* 1194:     */   {
/* 1195:1840 */     return new JAXBElement(_CTSRgbColorGreenOff_QNAME, CTPercentage.class, CTPresetColor.class, value);
/* 1196:     */   }
/* 1197:     */   
/* 1198:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hue", scope=CTPresetColor.class)
/* 1199:     */   public JAXBElement<CTPositiveFixedAngle> createCTPresetColorHue(CTPositiveFixedAngle value)
/* 1200:     */   {
/* 1201:1849 */     return new JAXBElement(_CTSRgbColorHue_QNAME, CTPositiveFixedAngle.class, CTPresetColor.class, value);
/* 1202:     */   }
/* 1203:     */   
/* 1204:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="redMod", scope=CTPresetColor.class)
/* 1205:     */   public JAXBElement<CTPercentage> createCTPresetColorRedMod(CTPercentage value)
/* 1206:     */   {
/* 1207:1858 */     return new JAXBElement(_CTSRgbColorRedMod_QNAME, CTPercentage.class, CTPresetColor.class, value);
/* 1208:     */   }
/* 1209:     */   
/* 1210:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="satOff", scope=CTPresetColor.class)
/* 1211:     */   public JAXBElement<CTPercentage> createCTPresetColorSatOff(CTPercentage value)
/* 1212:     */   {
/* 1213:1867 */     return new JAXBElement(_CTSRgbColorSatOff_QNAME, CTPercentage.class, CTPresetColor.class, value);
/* 1214:     */   }
/* 1215:     */   
/* 1216:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="greenMod", scope=CTPresetColor.class)
/* 1217:     */   public JAXBElement<CTPercentage> createCTPresetColorGreenMod(CTPercentage value)
/* 1218:     */   {
/* 1219:1876 */     return new JAXBElement(_CTSRgbColorGreenMod_QNAME, CTPercentage.class, CTPresetColor.class, value);
/* 1220:     */   }
/* 1221:     */   
/* 1222:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blue", scope=CTPresetColor.class)
/* 1223:     */   public JAXBElement<CTPercentage> createCTPresetColorBlue(CTPercentage value)
/* 1224:     */   {
/* 1225:1885 */     return new JAXBElement(_CTSRgbColorBlue_QNAME, CTPercentage.class, CTPresetColor.class, value);
/* 1226:     */   }
/* 1227:     */   
/* 1228:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="sat", scope=CTPresetColor.class)
/* 1229:     */   public JAXBElement<CTPercentage> createCTPresetColorSat(CTPercentage value)
/* 1230:     */   {
/* 1231:1894 */     return new JAXBElement(_CTSRgbColorSat_QNAME, CTPercentage.class, CTPresetColor.class, value);
/* 1232:     */   }
/* 1233:     */   
/* 1234:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="red", scope=CTPresetColor.class)
/* 1235:     */   public JAXBElement<CTPercentage> createCTPresetColorRed(CTPercentage value)
/* 1236:     */   {
/* 1237:1903 */     return new JAXBElement(_CTSRgbColorRed_QNAME, CTPercentage.class, CTPresetColor.class, value);
/* 1238:     */   }
/* 1239:     */   
/* 1240:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="satMod", scope=CTPresetColor.class)
/* 1241:     */   public JAXBElement<CTPercentage> createCTPresetColorSatMod(CTPercentage value)
/* 1242:     */   {
/* 1243:1912 */     return new JAXBElement(_CTSRgbColorSatMod_QNAME, CTPercentage.class, CTPresetColor.class, value);
/* 1244:     */   }
/* 1245:     */   
/* 1246:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blueMod", scope=CTPresetColor.class)
/* 1247:     */   public JAXBElement<CTPercentage> createCTPresetColorBlueMod(CTPercentage value)
/* 1248:     */   {
/* 1249:1921 */     return new JAXBElement(_CTSRgbColorBlueMod_QNAME, CTPercentage.class, CTPresetColor.class, value);
/* 1250:     */   }
/* 1251:     */   
/* 1252:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hueOff", scope=CTPresetColor.class)
/* 1253:     */   public JAXBElement<CTAngle> createCTPresetColorHueOff(CTAngle value)
/* 1254:     */   {
/* 1255:1930 */     return new JAXBElement(_CTSRgbColorHueOff_QNAME, CTAngle.class, CTPresetColor.class, value);
/* 1256:     */   }
/* 1257:     */   
/* 1258:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="shade", scope=CTPresetColor.class)
/* 1259:     */   public JAXBElement<CTPositiveFixedPercentage> createCTPresetColorShade(CTPositiveFixedPercentage value)
/* 1260:     */   {
/* 1261:1939 */     return new JAXBElement(_CTSRgbColorShade_QNAME, CTPositiveFixedPercentage.class, CTPresetColor.class, value);
/* 1262:     */   }
/* 1263:     */   
/* 1264:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lumMod", scope=CTPresetColor.class)
/* 1265:     */   public JAXBElement<CTPercentage> createCTPresetColorLumMod(CTPercentage value)
/* 1266:     */   {
/* 1267:1948 */     return new JAXBElement(_CTSRgbColorLumMod_QNAME, CTPercentage.class, CTPresetColor.class, value);
/* 1268:     */   }
/* 1269:     */   
/* 1270:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="inv", scope=CTPresetColor.class)
/* 1271:     */   public JAXBElement<CTInverseTransform> createCTPresetColorInv(CTInverseTransform value)
/* 1272:     */   {
/* 1273:1957 */     return new JAXBElement(_CTSRgbColorInv_QNAME, CTInverseTransform.class, CTPresetColor.class, value);
/* 1274:     */   }
/* 1275:     */   
/* 1276:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="lumOff", scope=CTPresetColor.class)
/* 1277:     */   public JAXBElement<CTPercentage> createCTPresetColorLumOff(CTPercentage value)
/* 1278:     */   {
/* 1279:1966 */     return new JAXBElement(_CTSRgbColorLumOff_QNAME, CTPercentage.class, CTPresetColor.class, value);
/* 1280:     */   }
/* 1281:     */   
/* 1282:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="tint", scope=CTPresetColor.class)
/* 1283:     */   public JAXBElement<CTPositiveFixedPercentage> createCTPresetColorTint(CTPositiveFixedPercentage value)
/* 1284:     */   {
/* 1285:1975 */     return new JAXBElement(_CTSRgbColorTint_QNAME, CTPositiveFixedPercentage.class, CTPresetColor.class, value);
/* 1286:     */   }
/* 1287:     */   
/* 1288:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="green", scope=CTPresetColor.class)
/* 1289:     */   public JAXBElement<CTPercentage> createCTPresetColorGreen(CTPercentage value)
/* 1290:     */   {
/* 1291:1984 */     return new JAXBElement(_CTSRgbColorGreen_QNAME, CTPercentage.class, CTPresetColor.class, value);
/* 1292:     */   }
/* 1293:     */   
/* 1294:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="comp", scope=CTPresetColor.class)
/* 1295:     */   public JAXBElement<CTComplementTransform> createCTPresetColorComp(CTComplementTransform value)
/* 1296:     */   {
/* 1297:1993 */     return new JAXBElement(_CTSRgbColorComp_QNAME, CTComplementTransform.class, CTPresetColor.class, value);
/* 1298:     */   }
/* 1299:     */   
/* 1300:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="blueOff", scope=CTPresetColor.class)
/* 1301:     */   public JAXBElement<CTPercentage> createCTPresetColorBlueOff(CTPercentage value)
/* 1302:     */   {
/* 1303:2002 */     return new JAXBElement(_CTSRgbColorBlueOff_QNAME, CTPercentage.class, CTPresetColor.class, value);
/* 1304:     */   }
/* 1305:     */   
/* 1306:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="hueMod", scope=CTPresetColor.class)
/* 1307:     */   public JAXBElement<CTPositivePercentage> createCTPresetColorHueMod(CTPositivePercentage value)
/* 1308:     */   {
/* 1309:2011 */     return new JAXBElement(_CTSRgbColorHueMod_QNAME, CTPositivePercentage.class, CTPresetColor.class, value);
/* 1310:     */   }
/* 1311:     */   
/* 1312:     */   @XmlElementDecl(namespace="http://schemas.openxmlformats.org/drawingml/2006/main", name="gray", scope=CTPresetColor.class)
/* 1313:     */   public JAXBElement<CTGrayscaleTransform> createCTPresetColorGray(CTGrayscaleTransform value)
/* 1314:     */   {
/* 1315:2020 */     return new JAXBElement(_CTSRgbColorGray_QNAME, CTGrayscaleTransform.class, CTPresetColor.class, value);
/* 1316:     */   }
/* 1317:     */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.ObjectFactory
 * JD-Core Version:    0.7.0.1
 */