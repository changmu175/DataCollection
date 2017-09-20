/*    1:     */ package org.apache.poi.xslf.usermodel;
/*    2:     */ 
/*    3:     */ import javax.xml.namespace.QName;
/*    4:     */ import org.apache.poi.util.Internal;
/*    5:     */ import org.apache.poi.util.POILogFactory;
/*    6:     */ import org.apache.poi.util.POILogger;
/*    7:     */ import org.apache.xmlbeans.XmlCursor;
/*    8:     */ import org.apache.xmlbeans.XmlObject;
/*    9:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties;
/*   10:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTCustomGeometry2D;
/*   11:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer;
/*   12:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList;
/*   13:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTFillProperties;
/*   14:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties;
/*   15:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties;
/*   16:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties;
/*   17:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties;
/*   18:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties;
/*   19:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D;
/*   20:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*   21:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
/*   22:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference;
/*   23:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellProperties;
/*   24:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
/*   25:     */ import org.openxmlformats.schemas.presentationml.x2006.main.CTBackgroundProperties;
/*   26:     */ 
/*   27:     */ @Internal
/*   28:     */ class XSLFPropertiesDelegate
/*   29:     */ {
/*   30:  53 */   private static final POILogger LOG = POILogFactory.getLogger(XSLFPropertiesDelegate.class);
/*   31:     */   
/*   32:     */   public static XSLFFillProperties getFillDelegate(XmlObject props)
/*   33:     */   {
/*   34:  57 */     return (XSLFFillProperties)getDelegate(XSLFFillProperties.class, props);
/*   35:     */   }
/*   36:     */   
/*   37:     */   public static XSLFGeometryProperties getGeometryDelegate(XmlObject props)
/*   38:     */   {
/*   39:  61 */     return (XSLFGeometryProperties)getDelegate(XSLFGeometryProperties.class, props);
/*   40:     */   }
/*   41:     */   
/*   42:     */   public static XSLFEffectProperties getEffectDelegate(XmlObject props)
/*   43:     */   {
/*   44:  65 */     return (XSLFEffectProperties)getDelegate(XSLFEffectProperties.class, props);
/*   45:     */   }
/*   46:     */   
/*   47:     */   public static abstract interface XSLFFillProperties
/*   48:     */   {
/*   49:     */     public abstract CTNoFillProperties getNoFill();
/*   50:     */     
/*   51:     */     public abstract boolean isSetNoFill();
/*   52:     */     
/*   53:     */     public abstract void setNoFill(CTNoFillProperties paramCTNoFillProperties);
/*   54:     */     
/*   55:     */     public abstract CTNoFillProperties addNewNoFill();
/*   56:     */     
/*   57:     */     public abstract void unsetNoFill();
/*   58:     */     
/*   59:     */     public abstract CTSolidColorFillProperties getSolidFill();
/*   60:     */     
/*   61:     */     public abstract boolean isSetSolidFill();
/*   62:     */     
/*   63:     */     public abstract void setSolidFill(CTSolidColorFillProperties paramCTSolidColorFillProperties);
/*   64:     */     
/*   65:     */     public abstract CTSolidColorFillProperties addNewSolidFill();
/*   66:     */     
/*   67:     */     public abstract void unsetSolidFill();
/*   68:     */     
/*   69:     */     public abstract CTGradientFillProperties getGradFill();
/*   70:     */     
/*   71:     */     public abstract boolean isSetGradFill();
/*   72:     */     
/*   73:     */     public abstract void setGradFill(CTGradientFillProperties paramCTGradientFillProperties);
/*   74:     */     
/*   75:     */     public abstract CTGradientFillProperties addNewGradFill();
/*   76:     */     
/*   77:     */     public abstract void unsetGradFill();
/*   78:     */     
/*   79:     */     public abstract CTBlipFillProperties getBlipFill();
/*   80:     */     
/*   81:     */     public abstract boolean isSetBlipFill();
/*   82:     */     
/*   83:     */     public abstract void setBlipFill(CTBlipFillProperties paramCTBlipFillProperties);
/*   84:     */     
/*   85:     */     public abstract CTBlipFillProperties addNewBlipFill();
/*   86:     */     
/*   87:     */     public abstract void unsetBlipFill();
/*   88:     */     
/*   89:     */     public abstract CTPatternFillProperties getPattFill();
/*   90:     */     
/*   91:     */     public abstract boolean isSetPattFill();
/*   92:     */     
/*   93:     */     public abstract void setPattFill(CTPatternFillProperties paramCTPatternFillProperties);
/*   94:     */     
/*   95:     */     public abstract CTPatternFillProperties addNewPattFill();
/*   96:     */     
/*   97:     */     public abstract void unsetPattFill();
/*   98:     */     
/*   99:     */     public abstract CTGroupFillProperties getGrpFill();
/*  100:     */     
/*  101:     */     public abstract boolean isSetGrpFill();
/*  102:     */     
/*  103:     */     public abstract void setGrpFill(CTGroupFillProperties paramCTGroupFillProperties);
/*  104:     */     
/*  105:     */     public abstract CTGroupFillProperties addNewGrpFill();
/*  106:     */     
/*  107:     */     public abstract void unsetGrpFill();
/*  108:     */     
/*  109:     */     public abstract boolean isSetMatrixStyle();
/*  110:     */     
/*  111:     */     public abstract CTStyleMatrixReference getMatrixStyle();
/*  112:     */     
/*  113:     */     public abstract boolean isLineStyle();
/*  114:     */   }
/*  115:     */   
/*  116:     */   public static abstract interface XSLFGeometryProperties
/*  117:     */   {
/*  118:     */     public abstract CTCustomGeometry2D getCustGeom();
/*  119:     */     
/*  120:     */     public abstract boolean isSetCustGeom();
/*  121:     */     
/*  122:     */     public abstract void setCustGeom(CTCustomGeometry2D paramCTCustomGeometry2D);
/*  123:     */     
/*  124:     */     public abstract CTCustomGeometry2D addNewCustGeom();
/*  125:     */     
/*  126:     */     public abstract void unsetCustGeom();
/*  127:     */     
/*  128:     */     public abstract CTPresetGeometry2D getPrstGeom();
/*  129:     */     
/*  130:     */     public abstract boolean isSetPrstGeom();
/*  131:     */     
/*  132:     */     public abstract void setPrstGeom(CTPresetGeometry2D paramCTPresetGeometry2D);
/*  133:     */     
/*  134:     */     public abstract CTPresetGeometry2D addNewPrstGeom();
/*  135:     */     
/*  136:     */     public abstract void unsetPrstGeom();
/*  137:     */   }
/*  138:     */   
/*  139:     */   public static abstract interface XSLFEffectProperties
/*  140:     */   {
/*  141:     */     public abstract CTEffectList getEffectLst();
/*  142:     */     
/*  143:     */     public abstract boolean isSetEffectLst();
/*  144:     */     
/*  145:     */     public abstract void setEffectLst(CTEffectList paramCTEffectList);
/*  146:     */     
/*  147:     */     public abstract CTEffectList addNewEffectLst();
/*  148:     */     
/*  149:     */     public abstract void unsetEffectLst();
/*  150:     */     
/*  151:     */     public abstract CTEffectContainer getEffectDag();
/*  152:     */     
/*  153:     */     public abstract boolean isSetEffectDag();
/*  154:     */     
/*  155:     */     public abstract void setEffectDag(CTEffectContainer paramCTEffectContainer);
/*  156:     */     
/*  157:     */     public abstract CTEffectContainer addNewEffectDag();
/*  158:     */     
/*  159:     */     public abstract void unsetEffectDag();
/*  160:     */   }
/*  161:     */   
/*  162:     */   private static class ShapeDelegate
/*  163:     */     implements XSLFFillProperties, XSLFGeometryProperties, XSLFEffectProperties
/*  164:     */   {
/*  165:     */     final CTShapeProperties props;
/*  166:     */     
/*  167:     */     ShapeDelegate(CTShapeProperties props)
/*  168:     */     {
/*  169: 346 */       this.props = props;
/*  170:     */     }
/*  171:     */     
/*  172:     */     public CTNoFillProperties getNoFill()
/*  173:     */     {
/*  174: 351 */       return this.props.getNoFill();
/*  175:     */     }
/*  176:     */     
/*  177:     */     public boolean isSetNoFill()
/*  178:     */     {
/*  179: 356 */       return this.props.isSetNoFill();
/*  180:     */     }
/*  181:     */     
/*  182:     */     public void setNoFill(CTNoFillProperties noFill)
/*  183:     */     {
/*  184: 361 */       this.props.setNoFill(noFill);
/*  185:     */     }
/*  186:     */     
/*  187:     */     public CTNoFillProperties addNewNoFill()
/*  188:     */     {
/*  189: 366 */       return this.props.addNewNoFill();
/*  190:     */     }
/*  191:     */     
/*  192:     */     public void unsetNoFill()
/*  193:     */     {
/*  194: 371 */       this.props.unsetNoFill();
/*  195:     */     }
/*  196:     */     
/*  197:     */     public CTSolidColorFillProperties getSolidFill()
/*  198:     */     {
/*  199: 376 */       return this.props.getSolidFill();
/*  200:     */     }
/*  201:     */     
/*  202:     */     public boolean isSetSolidFill()
/*  203:     */     {
/*  204: 381 */       return this.props.isSetSolidFill();
/*  205:     */     }
/*  206:     */     
/*  207:     */     public void setSolidFill(CTSolidColorFillProperties solidFill)
/*  208:     */     {
/*  209: 386 */       this.props.setSolidFill(solidFill);
/*  210:     */     }
/*  211:     */     
/*  212:     */     public CTSolidColorFillProperties addNewSolidFill()
/*  213:     */     {
/*  214: 391 */       return this.props.addNewSolidFill();
/*  215:     */     }
/*  216:     */     
/*  217:     */     public void unsetSolidFill()
/*  218:     */     {
/*  219: 396 */       this.props.unsetSolidFill();
/*  220:     */     }
/*  221:     */     
/*  222:     */     public CTGradientFillProperties getGradFill()
/*  223:     */     {
/*  224: 401 */       return this.props.getGradFill();
/*  225:     */     }
/*  226:     */     
/*  227:     */     public boolean isSetGradFill()
/*  228:     */     {
/*  229: 406 */       return this.props.isSetGradFill();
/*  230:     */     }
/*  231:     */     
/*  232:     */     public void setGradFill(CTGradientFillProperties gradFill)
/*  233:     */     {
/*  234: 411 */       this.props.setGradFill(gradFill);
/*  235:     */     }
/*  236:     */     
/*  237:     */     public CTGradientFillProperties addNewGradFill()
/*  238:     */     {
/*  239: 416 */       return this.props.addNewGradFill();
/*  240:     */     }
/*  241:     */     
/*  242:     */     public void unsetGradFill()
/*  243:     */     {
/*  244: 421 */       this.props.unsetGradFill();
/*  245:     */     }
/*  246:     */     
/*  247:     */     public CTBlipFillProperties getBlipFill()
/*  248:     */     {
/*  249: 426 */       return this.props.getBlipFill();
/*  250:     */     }
/*  251:     */     
/*  252:     */     public boolean isSetBlipFill()
/*  253:     */     {
/*  254: 431 */       return this.props.isSetBlipFill();
/*  255:     */     }
/*  256:     */     
/*  257:     */     public void setBlipFill(CTBlipFillProperties blipFill)
/*  258:     */     {
/*  259: 436 */       this.props.setBlipFill(blipFill);
/*  260:     */     }
/*  261:     */     
/*  262:     */     public CTBlipFillProperties addNewBlipFill()
/*  263:     */     {
/*  264: 441 */       return this.props.addNewBlipFill();
/*  265:     */     }
/*  266:     */     
/*  267:     */     public void unsetBlipFill()
/*  268:     */     {
/*  269: 446 */       this.props.unsetBlipFill();
/*  270:     */     }
/*  271:     */     
/*  272:     */     public CTPatternFillProperties getPattFill()
/*  273:     */     {
/*  274: 451 */       return this.props.getPattFill();
/*  275:     */     }
/*  276:     */     
/*  277:     */     public boolean isSetPattFill()
/*  278:     */     {
/*  279: 456 */       return this.props.isSetPattFill();
/*  280:     */     }
/*  281:     */     
/*  282:     */     public void setPattFill(CTPatternFillProperties pattFill)
/*  283:     */     {
/*  284: 461 */       this.props.setPattFill(pattFill);
/*  285:     */     }
/*  286:     */     
/*  287:     */     public CTPatternFillProperties addNewPattFill()
/*  288:     */     {
/*  289: 466 */       return this.props.addNewPattFill();
/*  290:     */     }
/*  291:     */     
/*  292:     */     public void unsetPattFill()
/*  293:     */     {
/*  294: 471 */       this.props.unsetPattFill();
/*  295:     */     }
/*  296:     */     
/*  297:     */     public CTGroupFillProperties getGrpFill()
/*  298:     */     {
/*  299: 476 */       return this.props.getGrpFill();
/*  300:     */     }
/*  301:     */     
/*  302:     */     public boolean isSetGrpFill()
/*  303:     */     {
/*  304: 481 */       return this.props.isSetGrpFill();
/*  305:     */     }
/*  306:     */     
/*  307:     */     public void setGrpFill(CTGroupFillProperties grpFill)
/*  308:     */     {
/*  309: 486 */       this.props.setGrpFill(grpFill);
/*  310:     */     }
/*  311:     */     
/*  312:     */     public CTGroupFillProperties addNewGrpFill()
/*  313:     */     {
/*  314: 491 */       return this.props.addNewGrpFill();
/*  315:     */     }
/*  316:     */     
/*  317:     */     public void unsetGrpFill()
/*  318:     */     {
/*  319: 496 */       this.props.unsetGrpFill();
/*  320:     */     }
/*  321:     */     
/*  322:     */     public CTCustomGeometry2D getCustGeom()
/*  323:     */     {
/*  324: 501 */       return this.props.getCustGeom();
/*  325:     */     }
/*  326:     */     
/*  327:     */     public boolean isSetCustGeom()
/*  328:     */     {
/*  329: 506 */       return this.props.isSetCustGeom();
/*  330:     */     }
/*  331:     */     
/*  332:     */     public void setCustGeom(CTCustomGeometry2D custGeom)
/*  333:     */     {
/*  334: 511 */       this.props.setCustGeom(custGeom);
/*  335:     */     }
/*  336:     */     
/*  337:     */     public CTCustomGeometry2D addNewCustGeom()
/*  338:     */     {
/*  339: 516 */       return this.props.addNewCustGeom();
/*  340:     */     }
/*  341:     */     
/*  342:     */     public void unsetCustGeom()
/*  343:     */     {
/*  344: 521 */       this.props.unsetCustGeom();
/*  345:     */     }
/*  346:     */     
/*  347:     */     public CTPresetGeometry2D getPrstGeom()
/*  348:     */     {
/*  349: 526 */       return this.props.getPrstGeom();
/*  350:     */     }
/*  351:     */     
/*  352:     */     public boolean isSetPrstGeom()
/*  353:     */     {
/*  354: 531 */       return this.props.isSetPrstGeom();
/*  355:     */     }
/*  356:     */     
/*  357:     */     public void setPrstGeom(CTPresetGeometry2D prstGeom)
/*  358:     */     {
/*  359: 536 */       this.props.setPrstGeom(prstGeom);
/*  360:     */     }
/*  361:     */     
/*  362:     */     public CTPresetGeometry2D addNewPrstGeom()
/*  363:     */     {
/*  364: 541 */       return this.props.addNewPrstGeom();
/*  365:     */     }
/*  366:     */     
/*  367:     */     public void unsetPrstGeom()
/*  368:     */     {
/*  369: 546 */       this.props.unsetPrstGeom();
/*  370:     */     }
/*  371:     */     
/*  372:     */     public CTEffectList getEffectLst()
/*  373:     */     {
/*  374: 551 */       return this.props.getEffectLst();
/*  375:     */     }
/*  376:     */     
/*  377:     */     public boolean isSetEffectLst()
/*  378:     */     {
/*  379: 556 */       return this.props.isSetEffectLst();
/*  380:     */     }
/*  381:     */     
/*  382:     */     public void setEffectLst(CTEffectList effectLst)
/*  383:     */     {
/*  384: 561 */       this.props.setEffectLst(effectLst);
/*  385:     */     }
/*  386:     */     
/*  387:     */     public CTEffectList addNewEffectLst()
/*  388:     */     {
/*  389: 566 */       return this.props.addNewEffectLst();
/*  390:     */     }
/*  391:     */     
/*  392:     */     public void unsetEffectLst()
/*  393:     */     {
/*  394: 571 */       this.props.unsetEffectLst();
/*  395:     */     }
/*  396:     */     
/*  397:     */     public CTEffectContainer getEffectDag()
/*  398:     */     {
/*  399: 576 */       return this.props.getEffectDag();
/*  400:     */     }
/*  401:     */     
/*  402:     */     public boolean isSetEffectDag()
/*  403:     */     {
/*  404: 581 */       return this.props.isSetEffectDag();
/*  405:     */     }
/*  406:     */     
/*  407:     */     public void setEffectDag(CTEffectContainer effectDag)
/*  408:     */     {
/*  409: 586 */       this.props.setEffectDag(effectDag);
/*  410:     */     }
/*  411:     */     
/*  412:     */     public CTEffectContainer addNewEffectDag()
/*  413:     */     {
/*  414: 591 */       return this.props.addNewEffectDag();
/*  415:     */     }
/*  416:     */     
/*  417:     */     public void unsetEffectDag()
/*  418:     */     {
/*  419: 596 */       this.props.unsetEffectDag();
/*  420:     */     }
/*  421:     */     
/*  422:     */     public boolean isSetMatrixStyle()
/*  423:     */     {
/*  424: 601 */       return false;
/*  425:     */     }
/*  426:     */     
/*  427:     */     public CTStyleMatrixReference getMatrixStyle()
/*  428:     */     {
/*  429: 606 */       return null;
/*  430:     */     }
/*  431:     */     
/*  432:     */     public boolean isLineStyle()
/*  433:     */     {
/*  434: 611 */       return false;
/*  435:     */     }
/*  436:     */   }
/*  437:     */   
/*  438:     */   private static class BackgroundDelegate
/*  439:     */     implements XSLFFillProperties, XSLFEffectProperties
/*  440:     */   {
/*  441:     */     final CTBackgroundProperties props;
/*  442:     */     
/*  443:     */     BackgroundDelegate(CTBackgroundProperties props)
/*  444:     */     {
/*  445: 619 */       this.props = props;
/*  446:     */     }
/*  447:     */     
/*  448:     */     public CTNoFillProperties getNoFill()
/*  449:     */     {
/*  450: 624 */       return this.props.getNoFill();
/*  451:     */     }
/*  452:     */     
/*  453:     */     public boolean isSetNoFill()
/*  454:     */     {
/*  455: 629 */       return this.props.isSetNoFill();
/*  456:     */     }
/*  457:     */     
/*  458:     */     public void setNoFill(CTNoFillProperties noFill)
/*  459:     */     {
/*  460: 634 */       this.props.setNoFill(noFill);
/*  461:     */     }
/*  462:     */     
/*  463:     */     public CTNoFillProperties addNewNoFill()
/*  464:     */     {
/*  465: 639 */       return this.props.addNewNoFill();
/*  466:     */     }
/*  467:     */     
/*  468:     */     public void unsetNoFill()
/*  469:     */     {
/*  470: 644 */       this.props.unsetNoFill();
/*  471:     */     }
/*  472:     */     
/*  473:     */     public CTSolidColorFillProperties getSolidFill()
/*  474:     */     {
/*  475: 649 */       return this.props.getSolidFill();
/*  476:     */     }
/*  477:     */     
/*  478:     */     public boolean isSetSolidFill()
/*  479:     */     {
/*  480: 654 */       return this.props.isSetSolidFill();
/*  481:     */     }
/*  482:     */     
/*  483:     */     public void setSolidFill(CTSolidColorFillProperties solidFill)
/*  484:     */     {
/*  485: 659 */       this.props.setSolidFill(solidFill);
/*  486:     */     }
/*  487:     */     
/*  488:     */     public CTSolidColorFillProperties addNewSolidFill()
/*  489:     */     {
/*  490: 664 */       return this.props.addNewSolidFill();
/*  491:     */     }
/*  492:     */     
/*  493:     */     public void unsetSolidFill()
/*  494:     */     {
/*  495: 669 */       this.props.unsetSolidFill();
/*  496:     */     }
/*  497:     */     
/*  498:     */     public CTGradientFillProperties getGradFill()
/*  499:     */     {
/*  500: 674 */       return this.props.getGradFill();
/*  501:     */     }
/*  502:     */     
/*  503:     */     public boolean isSetGradFill()
/*  504:     */     {
/*  505: 679 */       return this.props.isSetGradFill();
/*  506:     */     }
/*  507:     */     
/*  508:     */     public void setGradFill(CTGradientFillProperties gradFill)
/*  509:     */     {
/*  510: 684 */       this.props.setGradFill(gradFill);
/*  511:     */     }
/*  512:     */     
/*  513:     */     public CTGradientFillProperties addNewGradFill()
/*  514:     */     {
/*  515: 689 */       return this.props.addNewGradFill();
/*  516:     */     }
/*  517:     */     
/*  518:     */     public void unsetGradFill()
/*  519:     */     {
/*  520: 694 */       this.props.unsetGradFill();
/*  521:     */     }
/*  522:     */     
/*  523:     */     public CTBlipFillProperties getBlipFill()
/*  524:     */     {
/*  525: 699 */       return this.props.getBlipFill();
/*  526:     */     }
/*  527:     */     
/*  528:     */     public boolean isSetBlipFill()
/*  529:     */     {
/*  530: 704 */       return this.props.isSetBlipFill();
/*  531:     */     }
/*  532:     */     
/*  533:     */     public void setBlipFill(CTBlipFillProperties blipFill)
/*  534:     */     {
/*  535: 709 */       this.props.setBlipFill(blipFill);
/*  536:     */     }
/*  537:     */     
/*  538:     */     public CTBlipFillProperties addNewBlipFill()
/*  539:     */     {
/*  540: 714 */       return this.props.addNewBlipFill();
/*  541:     */     }
/*  542:     */     
/*  543:     */     public void unsetBlipFill()
/*  544:     */     {
/*  545: 719 */       this.props.unsetBlipFill();
/*  546:     */     }
/*  547:     */     
/*  548:     */     public CTPatternFillProperties getPattFill()
/*  549:     */     {
/*  550: 724 */       return this.props.getPattFill();
/*  551:     */     }
/*  552:     */     
/*  553:     */     public boolean isSetPattFill()
/*  554:     */     {
/*  555: 729 */       return this.props.isSetPattFill();
/*  556:     */     }
/*  557:     */     
/*  558:     */     public void setPattFill(CTPatternFillProperties pattFill)
/*  559:     */     {
/*  560: 734 */       this.props.setPattFill(pattFill);
/*  561:     */     }
/*  562:     */     
/*  563:     */     public CTPatternFillProperties addNewPattFill()
/*  564:     */     {
/*  565: 739 */       return this.props.addNewPattFill();
/*  566:     */     }
/*  567:     */     
/*  568:     */     public void unsetPattFill()
/*  569:     */     {
/*  570: 744 */       this.props.unsetPattFill();
/*  571:     */     }
/*  572:     */     
/*  573:     */     public CTGroupFillProperties getGrpFill()
/*  574:     */     {
/*  575: 749 */       return this.props.getGrpFill();
/*  576:     */     }
/*  577:     */     
/*  578:     */     public boolean isSetGrpFill()
/*  579:     */     {
/*  580: 754 */       return this.props.isSetGrpFill();
/*  581:     */     }
/*  582:     */     
/*  583:     */     public void setGrpFill(CTGroupFillProperties grpFill)
/*  584:     */     {
/*  585: 759 */       this.props.setGrpFill(grpFill);
/*  586:     */     }
/*  587:     */     
/*  588:     */     public CTGroupFillProperties addNewGrpFill()
/*  589:     */     {
/*  590: 764 */       return this.props.addNewGrpFill();
/*  591:     */     }
/*  592:     */     
/*  593:     */     public void unsetGrpFill()
/*  594:     */     {
/*  595: 769 */       this.props.unsetGrpFill();
/*  596:     */     }
/*  597:     */     
/*  598:     */     public CTEffectList getEffectLst()
/*  599:     */     {
/*  600: 774 */       return this.props.getEffectLst();
/*  601:     */     }
/*  602:     */     
/*  603:     */     public boolean isSetEffectLst()
/*  604:     */     {
/*  605: 779 */       return this.props.isSetEffectLst();
/*  606:     */     }
/*  607:     */     
/*  608:     */     public void setEffectLst(CTEffectList effectLst)
/*  609:     */     {
/*  610: 784 */       this.props.setEffectLst(effectLst);
/*  611:     */     }
/*  612:     */     
/*  613:     */     public CTEffectList addNewEffectLst()
/*  614:     */     {
/*  615: 789 */       return this.props.addNewEffectLst();
/*  616:     */     }
/*  617:     */     
/*  618:     */     public void unsetEffectLst()
/*  619:     */     {
/*  620: 794 */       this.props.unsetEffectLst();
/*  621:     */     }
/*  622:     */     
/*  623:     */     public CTEffectContainer getEffectDag()
/*  624:     */     {
/*  625: 799 */       return this.props.getEffectDag();
/*  626:     */     }
/*  627:     */     
/*  628:     */     public boolean isSetEffectDag()
/*  629:     */     {
/*  630: 804 */       return this.props.isSetEffectDag();
/*  631:     */     }
/*  632:     */     
/*  633:     */     public void setEffectDag(CTEffectContainer effectDag)
/*  634:     */     {
/*  635: 809 */       this.props.setEffectDag(effectDag);
/*  636:     */     }
/*  637:     */     
/*  638:     */     public CTEffectContainer addNewEffectDag()
/*  639:     */     {
/*  640: 814 */       return this.props.addNewEffectDag();
/*  641:     */     }
/*  642:     */     
/*  643:     */     public void unsetEffectDag()
/*  644:     */     {
/*  645: 819 */       this.props.unsetEffectDag();
/*  646:     */     }
/*  647:     */     
/*  648:     */     public boolean isSetMatrixStyle()
/*  649:     */     {
/*  650: 824 */       return false;
/*  651:     */     }
/*  652:     */     
/*  653:     */     public CTStyleMatrixReference getMatrixStyle()
/*  654:     */     {
/*  655: 829 */       return null;
/*  656:     */     }
/*  657:     */     
/*  658:     */     public boolean isLineStyle()
/*  659:     */     {
/*  660: 834 */       return false;
/*  661:     */     }
/*  662:     */   }
/*  663:     */   
/*  664:     */   private static class TableCellDelegate
/*  665:     */     implements XSLFFillProperties
/*  666:     */   {
/*  667:     */     final CTTableCellProperties props;
/*  668:     */     
/*  669:     */     TableCellDelegate(CTTableCellProperties props)
/*  670:     */     {
/*  671: 842 */       this.props = props;
/*  672:     */     }
/*  673:     */     
/*  674:     */     public CTNoFillProperties getNoFill()
/*  675:     */     {
/*  676: 847 */       return this.props.getNoFill();
/*  677:     */     }
/*  678:     */     
/*  679:     */     public boolean isSetNoFill()
/*  680:     */     {
/*  681: 852 */       return this.props.isSetNoFill();
/*  682:     */     }
/*  683:     */     
/*  684:     */     public void setNoFill(CTNoFillProperties noFill)
/*  685:     */     {
/*  686: 857 */       this.props.setNoFill(noFill);
/*  687:     */     }
/*  688:     */     
/*  689:     */     public CTNoFillProperties addNewNoFill()
/*  690:     */     {
/*  691: 862 */       return this.props.addNewNoFill();
/*  692:     */     }
/*  693:     */     
/*  694:     */     public void unsetNoFill()
/*  695:     */     {
/*  696: 867 */       this.props.unsetNoFill();
/*  697:     */     }
/*  698:     */     
/*  699:     */     public CTSolidColorFillProperties getSolidFill()
/*  700:     */     {
/*  701: 872 */       return this.props.getSolidFill();
/*  702:     */     }
/*  703:     */     
/*  704:     */     public boolean isSetSolidFill()
/*  705:     */     {
/*  706: 877 */       return this.props.isSetSolidFill();
/*  707:     */     }
/*  708:     */     
/*  709:     */     public void setSolidFill(CTSolidColorFillProperties solidFill)
/*  710:     */     {
/*  711: 882 */       this.props.setSolidFill(solidFill);
/*  712:     */     }
/*  713:     */     
/*  714:     */     public CTSolidColorFillProperties addNewSolidFill()
/*  715:     */     {
/*  716: 887 */       return this.props.addNewSolidFill();
/*  717:     */     }
/*  718:     */     
/*  719:     */     public void unsetSolidFill()
/*  720:     */     {
/*  721: 892 */       this.props.unsetSolidFill();
/*  722:     */     }
/*  723:     */     
/*  724:     */     public CTGradientFillProperties getGradFill()
/*  725:     */     {
/*  726: 897 */       return this.props.getGradFill();
/*  727:     */     }
/*  728:     */     
/*  729:     */     public boolean isSetGradFill()
/*  730:     */     {
/*  731: 902 */       return this.props.isSetGradFill();
/*  732:     */     }
/*  733:     */     
/*  734:     */     public void setGradFill(CTGradientFillProperties gradFill)
/*  735:     */     {
/*  736: 907 */       this.props.setGradFill(gradFill);
/*  737:     */     }
/*  738:     */     
/*  739:     */     public CTGradientFillProperties addNewGradFill()
/*  740:     */     {
/*  741: 912 */       return this.props.addNewGradFill();
/*  742:     */     }
/*  743:     */     
/*  744:     */     public void unsetGradFill()
/*  745:     */     {
/*  746: 917 */       this.props.unsetGradFill();
/*  747:     */     }
/*  748:     */     
/*  749:     */     public CTBlipFillProperties getBlipFill()
/*  750:     */     {
/*  751: 922 */       return this.props.getBlipFill();
/*  752:     */     }
/*  753:     */     
/*  754:     */     public boolean isSetBlipFill()
/*  755:     */     {
/*  756: 927 */       return this.props.isSetBlipFill();
/*  757:     */     }
/*  758:     */     
/*  759:     */     public void setBlipFill(CTBlipFillProperties blipFill)
/*  760:     */     {
/*  761: 932 */       this.props.setBlipFill(blipFill);
/*  762:     */     }
/*  763:     */     
/*  764:     */     public CTBlipFillProperties addNewBlipFill()
/*  765:     */     {
/*  766: 937 */       return this.props.addNewBlipFill();
/*  767:     */     }
/*  768:     */     
/*  769:     */     public void unsetBlipFill()
/*  770:     */     {
/*  771: 942 */       this.props.unsetBlipFill();
/*  772:     */     }
/*  773:     */     
/*  774:     */     public CTPatternFillProperties getPattFill()
/*  775:     */     {
/*  776: 947 */       return this.props.getPattFill();
/*  777:     */     }
/*  778:     */     
/*  779:     */     public boolean isSetPattFill()
/*  780:     */     {
/*  781: 952 */       return this.props.isSetPattFill();
/*  782:     */     }
/*  783:     */     
/*  784:     */     public void setPattFill(CTPatternFillProperties pattFill)
/*  785:     */     {
/*  786: 957 */       this.props.setPattFill(pattFill);
/*  787:     */     }
/*  788:     */     
/*  789:     */     public CTPatternFillProperties addNewPattFill()
/*  790:     */     {
/*  791: 962 */       return this.props.addNewPattFill();
/*  792:     */     }
/*  793:     */     
/*  794:     */     public void unsetPattFill()
/*  795:     */     {
/*  796: 967 */       this.props.unsetPattFill();
/*  797:     */     }
/*  798:     */     
/*  799:     */     public CTGroupFillProperties getGrpFill()
/*  800:     */     {
/*  801: 972 */       return this.props.getGrpFill();
/*  802:     */     }
/*  803:     */     
/*  804:     */     public boolean isSetGrpFill()
/*  805:     */     {
/*  806: 977 */       return this.props.isSetGrpFill();
/*  807:     */     }
/*  808:     */     
/*  809:     */     public void setGrpFill(CTGroupFillProperties grpFill)
/*  810:     */     {
/*  811: 982 */       this.props.setGrpFill(grpFill);
/*  812:     */     }
/*  813:     */     
/*  814:     */     public CTGroupFillProperties addNewGrpFill()
/*  815:     */     {
/*  816: 987 */       return this.props.addNewGrpFill();
/*  817:     */     }
/*  818:     */     
/*  819:     */     public void unsetGrpFill()
/*  820:     */     {
/*  821: 992 */       this.props.unsetGrpFill();
/*  822:     */     }
/*  823:     */     
/*  824:     */     public boolean isSetMatrixStyle()
/*  825:     */     {
/*  826: 997 */       return false;
/*  827:     */     }
/*  828:     */     
/*  829:     */     public CTStyleMatrixReference getMatrixStyle()
/*  830:     */     {
/*  831:1002 */       return null;
/*  832:     */     }
/*  833:     */     
/*  834:     */     public boolean isLineStyle()
/*  835:     */     {
/*  836:1007 */       return false;
/*  837:     */     }
/*  838:     */   }
/*  839:     */   
/*  840:     */   private static class StyleMatrixDelegate
/*  841:     */     implements XSLFFillProperties
/*  842:     */   {
/*  843:     */     final CTStyleMatrixReference props;
/*  844:     */     
/*  845:     */     StyleMatrixDelegate(CTStyleMatrixReference props)
/*  846:     */     {
/*  847:1015 */       this.props = props;
/*  848:     */     }
/*  849:     */     
/*  850:     */     public CTNoFillProperties getNoFill()
/*  851:     */     {
/*  852:1020 */       return null;
/*  853:     */     }
/*  854:     */     
/*  855:     */     public boolean isSetNoFill()
/*  856:     */     {
/*  857:1025 */       return false;
/*  858:     */     }
/*  859:     */     
/*  860:     */     public void setNoFill(CTNoFillProperties noFill) {}
/*  861:     */     
/*  862:     */     public CTNoFillProperties addNewNoFill()
/*  863:     */     {
/*  864:1033 */       return null;
/*  865:     */     }
/*  866:     */     
/*  867:     */     public void unsetNoFill() {}
/*  868:     */     
/*  869:     */     public CTSolidColorFillProperties getSolidFill()
/*  870:     */     {
/*  871:1041 */       return null;
/*  872:     */     }
/*  873:     */     
/*  874:     */     public boolean isSetSolidFill()
/*  875:     */     {
/*  876:1046 */       return false;
/*  877:     */     }
/*  878:     */     
/*  879:     */     public void setSolidFill(CTSolidColorFillProperties solidFill) {}
/*  880:     */     
/*  881:     */     public CTSolidColorFillProperties addNewSolidFill()
/*  882:     */     {
/*  883:1054 */       return null;
/*  884:     */     }
/*  885:     */     
/*  886:     */     public void unsetSolidFill() {}
/*  887:     */     
/*  888:     */     public CTGradientFillProperties getGradFill()
/*  889:     */     {
/*  890:1062 */       return null;
/*  891:     */     }
/*  892:     */     
/*  893:     */     public boolean isSetGradFill()
/*  894:     */     {
/*  895:1067 */       return false;
/*  896:     */     }
/*  897:     */     
/*  898:     */     public void setGradFill(CTGradientFillProperties gradFill) {}
/*  899:     */     
/*  900:     */     public CTGradientFillProperties addNewGradFill()
/*  901:     */     {
/*  902:1075 */       return null;
/*  903:     */     }
/*  904:     */     
/*  905:     */     public void unsetGradFill() {}
/*  906:     */     
/*  907:     */     public CTBlipFillProperties getBlipFill()
/*  908:     */     {
/*  909:1083 */       return null;
/*  910:     */     }
/*  911:     */     
/*  912:     */     public boolean isSetBlipFill()
/*  913:     */     {
/*  914:1088 */       return false;
/*  915:     */     }
/*  916:     */     
/*  917:     */     public void setBlipFill(CTBlipFillProperties blipFill) {}
/*  918:     */     
/*  919:     */     public CTBlipFillProperties addNewBlipFill()
/*  920:     */     {
/*  921:1096 */       return null;
/*  922:     */     }
/*  923:     */     
/*  924:     */     public void unsetBlipFill() {}
/*  925:     */     
/*  926:     */     public CTPatternFillProperties getPattFill()
/*  927:     */     {
/*  928:1104 */       return null;
/*  929:     */     }
/*  930:     */     
/*  931:     */     public boolean isSetPattFill()
/*  932:     */     {
/*  933:1109 */       return false;
/*  934:     */     }
/*  935:     */     
/*  936:     */     public void setPattFill(CTPatternFillProperties pattFill) {}
/*  937:     */     
/*  938:     */     public CTPatternFillProperties addNewPattFill()
/*  939:     */     {
/*  940:1117 */       return null;
/*  941:     */     }
/*  942:     */     
/*  943:     */     public void unsetPattFill() {}
/*  944:     */     
/*  945:     */     public CTGroupFillProperties getGrpFill()
/*  946:     */     {
/*  947:1125 */       return null;
/*  948:     */     }
/*  949:     */     
/*  950:     */     public boolean isSetGrpFill()
/*  951:     */     {
/*  952:1130 */       return false;
/*  953:     */     }
/*  954:     */     
/*  955:     */     public void setGrpFill(CTGroupFillProperties grpFill) {}
/*  956:     */     
/*  957:     */     public CTGroupFillProperties addNewGrpFill()
/*  958:     */     {
/*  959:1138 */       return null;
/*  960:     */     }
/*  961:     */     
/*  962:     */     public void unsetGrpFill() {}
/*  963:     */     
/*  964:     */     public boolean isSetMatrixStyle()
/*  965:     */     {
/*  966:1147 */       return true;
/*  967:     */     }
/*  968:     */     
/*  969:     */     public CTStyleMatrixReference getMatrixStyle()
/*  970:     */     {
/*  971:1152 */       return this.props;
/*  972:     */     }
/*  973:     */     
/*  974:     */     public boolean isLineStyle()
/*  975:     */     {
/*  976:1157 */       XmlCursor cur = this.props.newCursor();
/*  977:1158 */       String name = cur.getName().getLocalPart();
/*  978:1159 */       cur.dispose();
/*  979:1160 */       return "lnRef".equals(name);
/*  980:     */     }
/*  981:     */   }
/*  982:     */   
/*  983:     */   private static class FillDelegate
/*  984:     */     implements XSLFFillProperties
/*  985:     */   {
/*  986:     */     final CTFillProperties props;
/*  987:     */     
/*  988:     */     FillDelegate(CTFillProperties props)
/*  989:     */     {
/*  990:1168 */       this.props = props;
/*  991:     */     }
/*  992:     */     
/*  993:     */     public CTNoFillProperties getNoFill()
/*  994:     */     {
/*  995:1173 */       return this.props.getNoFill();
/*  996:     */     }
/*  997:     */     
/*  998:     */     public boolean isSetNoFill()
/*  999:     */     {
/* 1000:1178 */       return this.props.isSetNoFill();
/* 1001:     */     }
/* 1002:     */     
/* 1003:     */     public void setNoFill(CTNoFillProperties noFill)
/* 1004:     */     {
/* 1005:1183 */       this.props.setNoFill(noFill);
/* 1006:     */     }
/* 1007:     */     
/* 1008:     */     public CTNoFillProperties addNewNoFill()
/* 1009:     */     {
/* 1010:1188 */       return this.props.addNewNoFill();
/* 1011:     */     }
/* 1012:     */     
/* 1013:     */     public void unsetNoFill()
/* 1014:     */     {
/* 1015:1193 */       this.props.unsetNoFill();
/* 1016:     */     }
/* 1017:     */     
/* 1018:     */     public CTSolidColorFillProperties getSolidFill()
/* 1019:     */     {
/* 1020:1198 */       return this.props.getSolidFill();
/* 1021:     */     }
/* 1022:     */     
/* 1023:     */     public boolean isSetSolidFill()
/* 1024:     */     {
/* 1025:1203 */       return this.props.isSetSolidFill();
/* 1026:     */     }
/* 1027:     */     
/* 1028:     */     public void setSolidFill(CTSolidColorFillProperties solidFill)
/* 1029:     */     {
/* 1030:1208 */       this.props.setSolidFill(solidFill);
/* 1031:     */     }
/* 1032:     */     
/* 1033:     */     public CTSolidColorFillProperties addNewSolidFill()
/* 1034:     */     {
/* 1035:1213 */       return this.props.addNewSolidFill();
/* 1036:     */     }
/* 1037:     */     
/* 1038:     */     public void unsetSolidFill()
/* 1039:     */     {
/* 1040:1218 */       this.props.unsetSolidFill();
/* 1041:     */     }
/* 1042:     */     
/* 1043:     */     public CTGradientFillProperties getGradFill()
/* 1044:     */     {
/* 1045:1223 */       return this.props.getGradFill();
/* 1046:     */     }
/* 1047:     */     
/* 1048:     */     public boolean isSetGradFill()
/* 1049:     */     {
/* 1050:1228 */       return this.props.isSetGradFill();
/* 1051:     */     }
/* 1052:     */     
/* 1053:     */     public void setGradFill(CTGradientFillProperties gradFill)
/* 1054:     */     {
/* 1055:1233 */       this.props.setGradFill(gradFill);
/* 1056:     */     }
/* 1057:     */     
/* 1058:     */     public CTGradientFillProperties addNewGradFill()
/* 1059:     */     {
/* 1060:1238 */       return this.props.addNewGradFill();
/* 1061:     */     }
/* 1062:     */     
/* 1063:     */     public void unsetGradFill()
/* 1064:     */     {
/* 1065:1243 */       this.props.unsetGradFill();
/* 1066:     */     }
/* 1067:     */     
/* 1068:     */     public CTBlipFillProperties getBlipFill()
/* 1069:     */     {
/* 1070:1248 */       return this.props.getBlipFill();
/* 1071:     */     }
/* 1072:     */     
/* 1073:     */     public boolean isSetBlipFill()
/* 1074:     */     {
/* 1075:1253 */       return this.props.isSetBlipFill();
/* 1076:     */     }
/* 1077:     */     
/* 1078:     */     public void setBlipFill(CTBlipFillProperties blipFill)
/* 1079:     */     {
/* 1080:1258 */       this.props.setBlipFill(blipFill);
/* 1081:     */     }
/* 1082:     */     
/* 1083:     */     public CTBlipFillProperties addNewBlipFill()
/* 1084:     */     {
/* 1085:1263 */       return this.props.addNewBlipFill();
/* 1086:     */     }
/* 1087:     */     
/* 1088:     */     public void unsetBlipFill()
/* 1089:     */     {
/* 1090:1268 */       this.props.unsetBlipFill();
/* 1091:     */     }
/* 1092:     */     
/* 1093:     */     public CTPatternFillProperties getPattFill()
/* 1094:     */     {
/* 1095:1273 */       return this.props.getPattFill();
/* 1096:     */     }
/* 1097:     */     
/* 1098:     */     public boolean isSetPattFill()
/* 1099:     */     {
/* 1100:1278 */       return this.props.isSetPattFill();
/* 1101:     */     }
/* 1102:     */     
/* 1103:     */     public void setPattFill(CTPatternFillProperties pattFill)
/* 1104:     */     {
/* 1105:1283 */       this.props.setPattFill(pattFill);
/* 1106:     */     }
/* 1107:     */     
/* 1108:     */     public CTPatternFillProperties addNewPattFill()
/* 1109:     */     {
/* 1110:1288 */       return this.props.addNewPattFill();
/* 1111:     */     }
/* 1112:     */     
/* 1113:     */     public void unsetPattFill()
/* 1114:     */     {
/* 1115:1293 */       this.props.unsetPattFill();
/* 1116:     */     }
/* 1117:     */     
/* 1118:     */     public CTGroupFillProperties getGrpFill()
/* 1119:     */     {
/* 1120:1298 */       return this.props.getGrpFill();
/* 1121:     */     }
/* 1122:     */     
/* 1123:     */     public boolean isSetGrpFill()
/* 1124:     */     {
/* 1125:1303 */       return this.props.isSetGrpFill();
/* 1126:     */     }
/* 1127:     */     
/* 1128:     */     public void setGrpFill(CTGroupFillProperties grpFill)
/* 1129:     */     {
/* 1130:1308 */       this.props.setGrpFill(grpFill);
/* 1131:     */     }
/* 1132:     */     
/* 1133:     */     public CTGroupFillProperties addNewGrpFill()
/* 1134:     */     {
/* 1135:1313 */       return this.props.addNewGrpFill();
/* 1136:     */     }
/* 1137:     */     
/* 1138:     */     public void unsetGrpFill()
/* 1139:     */     {
/* 1140:1318 */       this.props.unsetGrpFill();
/* 1141:     */     }
/* 1142:     */     
/* 1143:     */     public boolean isSetMatrixStyle()
/* 1144:     */     {
/* 1145:1323 */       return false;
/* 1146:     */     }
/* 1147:     */     
/* 1148:     */     public CTStyleMatrixReference getMatrixStyle()
/* 1149:     */     {
/* 1150:1328 */       return null;
/* 1151:     */     }
/* 1152:     */     
/* 1153:     */     public boolean isLineStyle()
/* 1154:     */     {
/* 1155:1333 */       return false;
/* 1156:     */     }
/* 1157:     */   }
/* 1158:     */   
/* 1159:     */   private static class FillPartDelegate
/* 1160:     */     implements XSLFFillProperties
/* 1161:     */   {
/* 1162:     */     final XmlObject props;
/* 1163:     */     
/* 1164:     */     FillPartDelegate(XmlObject props)
/* 1165:     */     {
/* 1166:1341 */       this.props = props;
/* 1167:     */     }
/* 1168:     */     
/* 1169:     */     public CTNoFillProperties getNoFill()
/* 1170:     */     {
/* 1171:1346 */       return isSetNoFill() ? (CTNoFillProperties)this.props : null;
/* 1172:     */     }
/* 1173:     */     
/* 1174:     */     public boolean isSetNoFill()
/* 1175:     */     {
/* 1176:1351 */       return this.props instanceof CTNoFillProperties;
/* 1177:     */     }
/* 1178:     */     
/* 1179:     */     public void setNoFill(CTNoFillProperties noFill) {}
/* 1180:     */     
/* 1181:     */     public CTNoFillProperties addNewNoFill()
/* 1182:     */     {
/* 1183:1359 */       return null;
/* 1184:     */     }
/* 1185:     */     
/* 1186:     */     public void unsetNoFill() {}
/* 1187:     */     
/* 1188:     */     public CTSolidColorFillProperties getSolidFill()
/* 1189:     */     {
/* 1190:1367 */       return isSetSolidFill() ? (CTSolidColorFillProperties)this.props : null;
/* 1191:     */     }
/* 1192:     */     
/* 1193:     */     public boolean isSetSolidFill()
/* 1194:     */     {
/* 1195:1372 */       return this.props instanceof CTSolidColorFillProperties;
/* 1196:     */     }
/* 1197:     */     
/* 1198:     */     public void setSolidFill(CTSolidColorFillProperties solidFill) {}
/* 1199:     */     
/* 1200:     */     public CTSolidColorFillProperties addNewSolidFill()
/* 1201:     */     {
/* 1202:1380 */       return null;
/* 1203:     */     }
/* 1204:     */     
/* 1205:     */     public void unsetSolidFill() {}
/* 1206:     */     
/* 1207:     */     public CTGradientFillProperties getGradFill()
/* 1208:     */     {
/* 1209:1388 */       return isSetGradFill() ? (CTGradientFillProperties)this.props : null;
/* 1210:     */     }
/* 1211:     */     
/* 1212:     */     public boolean isSetGradFill()
/* 1213:     */     {
/* 1214:1393 */       return this.props instanceof CTGradientFillProperties;
/* 1215:     */     }
/* 1216:     */     
/* 1217:     */     public void setGradFill(CTGradientFillProperties gradFill) {}
/* 1218:     */     
/* 1219:     */     public CTGradientFillProperties addNewGradFill()
/* 1220:     */     {
/* 1221:1401 */       return null;
/* 1222:     */     }
/* 1223:     */     
/* 1224:     */     public void unsetGradFill() {}
/* 1225:     */     
/* 1226:     */     public CTBlipFillProperties getBlipFill()
/* 1227:     */     {
/* 1228:1409 */       return isSetBlipFill() ? (CTBlipFillProperties)this.props : null;
/* 1229:     */     }
/* 1230:     */     
/* 1231:     */     public boolean isSetBlipFill()
/* 1232:     */     {
/* 1233:1414 */       return this.props instanceof CTBlipFillProperties;
/* 1234:     */     }
/* 1235:     */     
/* 1236:     */     public void setBlipFill(CTBlipFillProperties blipFill) {}
/* 1237:     */     
/* 1238:     */     public CTBlipFillProperties addNewBlipFill()
/* 1239:     */     {
/* 1240:1422 */       return null;
/* 1241:     */     }
/* 1242:     */     
/* 1243:     */     public void unsetBlipFill() {}
/* 1244:     */     
/* 1245:     */     public CTPatternFillProperties getPattFill()
/* 1246:     */     {
/* 1247:1430 */       return isSetPattFill() ? (CTPatternFillProperties)this.props : null;
/* 1248:     */     }
/* 1249:     */     
/* 1250:     */     public boolean isSetPattFill()
/* 1251:     */     {
/* 1252:1435 */       return this.props instanceof CTPatternFillProperties;
/* 1253:     */     }
/* 1254:     */     
/* 1255:     */     public void setPattFill(CTPatternFillProperties pattFill) {}
/* 1256:     */     
/* 1257:     */     public CTPatternFillProperties addNewPattFill()
/* 1258:     */     {
/* 1259:1443 */       return null;
/* 1260:     */     }
/* 1261:     */     
/* 1262:     */     public void unsetPattFill() {}
/* 1263:     */     
/* 1264:     */     public CTGroupFillProperties getGrpFill()
/* 1265:     */     {
/* 1266:1451 */       return isSetGrpFill() ? (CTGroupFillProperties)this.props : null;
/* 1267:     */     }
/* 1268:     */     
/* 1269:     */     public boolean isSetGrpFill()
/* 1270:     */     {
/* 1271:1456 */       return this.props instanceof CTGroupFillProperties;
/* 1272:     */     }
/* 1273:     */     
/* 1274:     */     public void setGrpFill(CTGroupFillProperties grpFill) {}
/* 1275:     */     
/* 1276:     */     public CTGroupFillProperties addNewGrpFill()
/* 1277:     */     {
/* 1278:1464 */       return null;
/* 1279:     */     }
/* 1280:     */     
/* 1281:     */     public void unsetGrpFill() {}
/* 1282:     */     
/* 1283:     */     public boolean isSetMatrixStyle()
/* 1284:     */     {
/* 1285:1472 */       return false;
/* 1286:     */     }
/* 1287:     */     
/* 1288:     */     public CTStyleMatrixReference getMatrixStyle()
/* 1289:     */     {
/* 1290:1477 */       return null;
/* 1291:     */     }
/* 1292:     */     
/* 1293:     */     public boolean isLineStyle()
/* 1294:     */     {
/* 1295:1482 */       return false;
/* 1296:     */     }
/* 1297:     */   }
/* 1298:     */   
/* 1299:     */   private static class LineStyleDelegate
/* 1300:     */     implements XSLFFillProperties
/* 1301:     */   {
/* 1302:     */     final CTLineProperties props;
/* 1303:     */     
/* 1304:     */     LineStyleDelegate(CTLineProperties props)
/* 1305:     */     {
/* 1306:1490 */       this.props = props;
/* 1307:     */     }
/* 1308:     */     
/* 1309:     */     public CTNoFillProperties getNoFill()
/* 1310:     */     {
/* 1311:1495 */       return this.props.getNoFill();
/* 1312:     */     }
/* 1313:     */     
/* 1314:     */     public boolean isSetNoFill()
/* 1315:     */     {
/* 1316:1500 */       return this.props.isSetNoFill();
/* 1317:     */     }
/* 1318:     */     
/* 1319:     */     public void setNoFill(CTNoFillProperties noFill)
/* 1320:     */     {
/* 1321:1505 */       this.props.setNoFill(noFill);
/* 1322:     */     }
/* 1323:     */     
/* 1324:     */     public CTNoFillProperties addNewNoFill()
/* 1325:     */     {
/* 1326:1510 */       return this.props.addNewNoFill();
/* 1327:     */     }
/* 1328:     */     
/* 1329:     */     public void unsetNoFill()
/* 1330:     */     {
/* 1331:1515 */       this.props.unsetNoFill();
/* 1332:     */     }
/* 1333:     */     
/* 1334:     */     public CTSolidColorFillProperties getSolidFill()
/* 1335:     */     {
/* 1336:1520 */       return this.props.getSolidFill();
/* 1337:     */     }
/* 1338:     */     
/* 1339:     */     public boolean isSetSolidFill()
/* 1340:     */     {
/* 1341:1525 */       return this.props.isSetSolidFill();
/* 1342:     */     }
/* 1343:     */     
/* 1344:     */     public void setSolidFill(CTSolidColorFillProperties solidFill)
/* 1345:     */     {
/* 1346:1530 */       this.props.setSolidFill(solidFill);
/* 1347:     */     }
/* 1348:     */     
/* 1349:     */     public CTSolidColorFillProperties addNewSolidFill()
/* 1350:     */     {
/* 1351:1535 */       return this.props.addNewSolidFill();
/* 1352:     */     }
/* 1353:     */     
/* 1354:     */     public void unsetSolidFill()
/* 1355:     */     {
/* 1356:1540 */       this.props.unsetSolidFill();
/* 1357:     */     }
/* 1358:     */     
/* 1359:     */     public CTGradientFillProperties getGradFill()
/* 1360:     */     {
/* 1361:1545 */       return this.props.getGradFill();
/* 1362:     */     }
/* 1363:     */     
/* 1364:     */     public boolean isSetGradFill()
/* 1365:     */     {
/* 1366:1550 */       return this.props.isSetGradFill();
/* 1367:     */     }
/* 1368:     */     
/* 1369:     */     public void setGradFill(CTGradientFillProperties gradFill)
/* 1370:     */     {
/* 1371:1555 */       this.props.setGradFill(gradFill);
/* 1372:     */     }
/* 1373:     */     
/* 1374:     */     public CTGradientFillProperties addNewGradFill()
/* 1375:     */     {
/* 1376:1560 */       return this.props.addNewGradFill();
/* 1377:     */     }
/* 1378:     */     
/* 1379:     */     public void unsetGradFill()
/* 1380:     */     {
/* 1381:1565 */       this.props.unsetGradFill();
/* 1382:     */     }
/* 1383:     */     
/* 1384:     */     public CTBlipFillProperties getBlipFill()
/* 1385:     */     {
/* 1386:1570 */       return null;
/* 1387:     */     }
/* 1388:     */     
/* 1389:     */     public boolean isSetBlipFill()
/* 1390:     */     {
/* 1391:1575 */       return false;
/* 1392:     */     }
/* 1393:     */     
/* 1394:     */     public void setBlipFill(CTBlipFillProperties blipFill) {}
/* 1395:     */     
/* 1396:     */     public CTBlipFillProperties addNewBlipFill()
/* 1397:     */     {
/* 1398:1583 */       return null;
/* 1399:     */     }
/* 1400:     */     
/* 1401:     */     public void unsetBlipFill() {}
/* 1402:     */     
/* 1403:     */     public CTPatternFillProperties getPattFill()
/* 1404:     */     {
/* 1405:1591 */       return this.props.getPattFill();
/* 1406:     */     }
/* 1407:     */     
/* 1408:     */     public boolean isSetPattFill()
/* 1409:     */     {
/* 1410:1596 */       return this.props.isSetPattFill();
/* 1411:     */     }
/* 1412:     */     
/* 1413:     */     public void setPattFill(CTPatternFillProperties pattFill)
/* 1414:     */     {
/* 1415:1601 */       this.props.setPattFill(pattFill);
/* 1416:     */     }
/* 1417:     */     
/* 1418:     */     public CTPatternFillProperties addNewPattFill()
/* 1419:     */     {
/* 1420:1606 */       return this.props.addNewPattFill();
/* 1421:     */     }
/* 1422:     */     
/* 1423:     */     public void unsetPattFill()
/* 1424:     */     {
/* 1425:1611 */       this.props.unsetPattFill();
/* 1426:     */     }
/* 1427:     */     
/* 1428:     */     public CTGroupFillProperties getGrpFill()
/* 1429:     */     {
/* 1430:1616 */       return null;
/* 1431:     */     }
/* 1432:     */     
/* 1433:     */     public boolean isSetGrpFill()
/* 1434:     */     {
/* 1435:1621 */       return false;
/* 1436:     */     }
/* 1437:     */     
/* 1438:     */     public void setGrpFill(CTGroupFillProperties grpFill) {}
/* 1439:     */     
/* 1440:     */     public CTGroupFillProperties addNewGrpFill()
/* 1441:     */     {
/* 1442:1629 */       return null;
/* 1443:     */     }
/* 1444:     */     
/* 1445:     */     public void unsetGrpFill() {}
/* 1446:     */     
/* 1447:     */     public boolean isSetMatrixStyle()
/* 1448:     */     {
/* 1449:1637 */       return false;
/* 1450:     */     }
/* 1451:     */     
/* 1452:     */     public CTStyleMatrixReference getMatrixStyle()
/* 1453:     */     {
/* 1454:1642 */       return null;
/* 1455:     */     }
/* 1456:     */     
/* 1457:     */     public boolean isLineStyle()
/* 1458:     */     {
/* 1459:1647 */       return true;
/* 1460:     */     }
/* 1461:     */   }
/* 1462:     */   
/* 1463:     */   private static class TextCharDelegate
/* 1464:     */     implements XSLFFillProperties
/* 1465:     */   {
/* 1466:     */     final CTTextCharacterProperties props;
/* 1467:     */     
/* 1468:     */     TextCharDelegate(CTTextCharacterProperties props)
/* 1469:     */     {
/* 1470:1655 */       this.props = props;
/* 1471:     */     }
/* 1472:     */     
/* 1473:     */     public CTNoFillProperties getNoFill()
/* 1474:     */     {
/* 1475:1660 */       return this.props.getNoFill();
/* 1476:     */     }
/* 1477:     */     
/* 1478:     */     public boolean isSetNoFill()
/* 1479:     */     {
/* 1480:1665 */       return this.props.isSetNoFill();
/* 1481:     */     }
/* 1482:     */     
/* 1483:     */     public void setNoFill(CTNoFillProperties noFill)
/* 1484:     */     {
/* 1485:1670 */       this.props.setNoFill(noFill);
/* 1486:     */     }
/* 1487:     */     
/* 1488:     */     public CTNoFillProperties addNewNoFill()
/* 1489:     */     {
/* 1490:1675 */       return this.props.addNewNoFill();
/* 1491:     */     }
/* 1492:     */     
/* 1493:     */     public void unsetNoFill()
/* 1494:     */     {
/* 1495:1680 */       this.props.unsetNoFill();
/* 1496:     */     }
/* 1497:     */     
/* 1498:     */     public CTSolidColorFillProperties getSolidFill()
/* 1499:     */     {
/* 1500:1685 */       return this.props.getSolidFill();
/* 1501:     */     }
/* 1502:     */     
/* 1503:     */     public boolean isSetSolidFill()
/* 1504:     */     {
/* 1505:1690 */       return this.props.isSetSolidFill();
/* 1506:     */     }
/* 1507:     */     
/* 1508:     */     public void setSolidFill(CTSolidColorFillProperties solidFill)
/* 1509:     */     {
/* 1510:1695 */       this.props.setSolidFill(solidFill);
/* 1511:     */     }
/* 1512:     */     
/* 1513:     */     public CTSolidColorFillProperties addNewSolidFill()
/* 1514:     */     {
/* 1515:1700 */       return this.props.addNewSolidFill();
/* 1516:     */     }
/* 1517:     */     
/* 1518:     */     public void unsetSolidFill()
/* 1519:     */     {
/* 1520:1705 */       this.props.unsetSolidFill();
/* 1521:     */     }
/* 1522:     */     
/* 1523:     */     public CTGradientFillProperties getGradFill()
/* 1524:     */     {
/* 1525:1710 */       return this.props.getGradFill();
/* 1526:     */     }
/* 1527:     */     
/* 1528:     */     public boolean isSetGradFill()
/* 1529:     */     {
/* 1530:1715 */       return this.props.isSetGradFill();
/* 1531:     */     }
/* 1532:     */     
/* 1533:     */     public void setGradFill(CTGradientFillProperties gradFill)
/* 1534:     */     {
/* 1535:1720 */       this.props.setGradFill(gradFill);
/* 1536:     */     }
/* 1537:     */     
/* 1538:     */     public CTGradientFillProperties addNewGradFill()
/* 1539:     */     {
/* 1540:1725 */       return this.props.addNewGradFill();
/* 1541:     */     }
/* 1542:     */     
/* 1543:     */     public void unsetGradFill()
/* 1544:     */     {
/* 1545:1730 */       this.props.unsetGradFill();
/* 1546:     */     }
/* 1547:     */     
/* 1548:     */     public CTBlipFillProperties getBlipFill()
/* 1549:     */     {
/* 1550:1735 */       return this.props.getBlipFill();
/* 1551:     */     }
/* 1552:     */     
/* 1553:     */     public boolean isSetBlipFill()
/* 1554:     */     {
/* 1555:1740 */       return this.props.isSetBlipFill();
/* 1556:     */     }
/* 1557:     */     
/* 1558:     */     public void setBlipFill(CTBlipFillProperties blipFill)
/* 1559:     */     {
/* 1560:1745 */       this.props.setBlipFill(blipFill);
/* 1561:     */     }
/* 1562:     */     
/* 1563:     */     public CTBlipFillProperties addNewBlipFill()
/* 1564:     */     {
/* 1565:1750 */       return this.props.addNewBlipFill();
/* 1566:     */     }
/* 1567:     */     
/* 1568:     */     public void unsetBlipFill()
/* 1569:     */     {
/* 1570:1755 */       this.props.unsetBlipFill();
/* 1571:     */     }
/* 1572:     */     
/* 1573:     */     public CTPatternFillProperties getPattFill()
/* 1574:     */     {
/* 1575:1760 */       return this.props.getPattFill();
/* 1576:     */     }
/* 1577:     */     
/* 1578:     */     public boolean isSetPattFill()
/* 1579:     */     {
/* 1580:1765 */       return this.props.isSetPattFill();
/* 1581:     */     }
/* 1582:     */     
/* 1583:     */     public void setPattFill(CTPatternFillProperties pattFill)
/* 1584:     */     {
/* 1585:1770 */       this.props.setPattFill(pattFill);
/* 1586:     */     }
/* 1587:     */     
/* 1588:     */     public CTPatternFillProperties addNewPattFill()
/* 1589:     */     {
/* 1590:1775 */       return this.props.addNewPattFill();
/* 1591:     */     }
/* 1592:     */     
/* 1593:     */     public void unsetPattFill()
/* 1594:     */     {
/* 1595:1780 */       this.props.unsetPattFill();
/* 1596:     */     }
/* 1597:     */     
/* 1598:     */     public CTGroupFillProperties getGrpFill()
/* 1599:     */     {
/* 1600:1785 */       return this.props.getGrpFill();
/* 1601:     */     }
/* 1602:     */     
/* 1603:     */     public boolean isSetGrpFill()
/* 1604:     */     {
/* 1605:1790 */       return this.props.isSetGrpFill();
/* 1606:     */     }
/* 1607:     */     
/* 1608:     */     public void setGrpFill(CTGroupFillProperties grpFill)
/* 1609:     */     {
/* 1610:1795 */       this.props.setGrpFill(grpFill);
/* 1611:     */     }
/* 1612:     */     
/* 1613:     */     public CTGroupFillProperties addNewGrpFill()
/* 1614:     */     {
/* 1615:1800 */       return this.props.addNewGrpFill();
/* 1616:     */     }
/* 1617:     */     
/* 1618:     */     public void unsetGrpFill()
/* 1619:     */     {
/* 1620:1805 */       this.props.unsetGrpFill();
/* 1621:     */     }
/* 1622:     */     
/* 1623:     */     public boolean isSetMatrixStyle()
/* 1624:     */     {
/* 1625:1810 */       return false;
/* 1626:     */     }
/* 1627:     */     
/* 1628:     */     public CTStyleMatrixReference getMatrixStyle()
/* 1629:     */     {
/* 1630:1815 */       return null;
/* 1631:     */     }
/* 1632:     */     
/* 1633:     */     public boolean isLineStyle()
/* 1634:     */     {
/* 1635:1820 */       return false;
/* 1636:     */     }
/* 1637:     */   }
/* 1638:     */   
/* 1639:     */   private static <T> T getDelegate(Class<T> clazz, XmlObject props)
/* 1640:     */   {
/* 1641:1826 */     Object obj = null;
/* 1642:1827 */     if (props == null) {
/* 1643:1828 */       return null;
/* 1644:     */     }
/* 1645:1829 */     if ((props instanceof CTShapeProperties))
/* 1646:     */     {
/* 1647:1830 */       obj = new ShapeDelegate((CTShapeProperties)props);
/* 1648:     */     }
/* 1649:1831 */     else if ((props instanceof CTBackgroundProperties))
/* 1650:     */     {
/* 1651:1832 */       obj = new BackgroundDelegate((CTBackgroundProperties)props);
/* 1652:     */     }
/* 1653:1833 */     else if ((props instanceof CTStyleMatrixReference))
/* 1654:     */     {
/* 1655:1834 */       obj = new StyleMatrixDelegate((CTStyleMatrixReference)props);
/* 1656:     */     }
/* 1657:1835 */     else if ((props instanceof CTTableCellProperties))
/* 1658:     */     {
/* 1659:1836 */       obj = new TableCellDelegate((CTTableCellProperties)props);
/* 1660:     */     }
/* 1661:1837 */     else if (((props instanceof CTNoFillProperties)) || ((props instanceof CTSolidColorFillProperties)) || ((props instanceof CTGradientFillProperties)) || ((props instanceof CTBlipFillProperties)) || ((props instanceof CTPatternFillProperties)) || ((props instanceof CTGroupFillProperties)))
/* 1662:     */     {
/* 1663:1843 */       obj = new FillPartDelegate(props);
/* 1664:     */     }
/* 1665:1844 */     else if ((props instanceof CTFillProperties))
/* 1666:     */     {
/* 1667:1845 */       obj = new FillDelegate((CTFillProperties)props);
/* 1668:     */     }
/* 1669:1846 */     else if ((props instanceof CTLineProperties))
/* 1670:     */     {
/* 1671:1847 */       obj = new LineStyleDelegate((CTLineProperties)props);
/* 1672:     */     }
/* 1673:1848 */     else if ((props instanceof CTTextCharacterProperties))
/* 1674:     */     {
/* 1675:1849 */       obj = new TextCharDelegate((CTTextCharacterProperties)props);
/* 1676:     */     }
/* 1677:     */     else
/* 1678:     */     {
/* 1679:1851 */       LOG.log(7, new Object[] { props.getClass() + " is an unknown properties type" });
/* 1680:1852 */       return null;
/* 1681:     */     }
/* 1682:1855 */     if (clazz.isInstance(obj)) {
/* 1683:1856 */       return obj;
/* 1684:     */     }
/* 1685:1859 */     LOG.log(5, new Object[] { obj.getClass() + " doesn't implement " + clazz });
/* 1686:1860 */     return null;
/* 1687:     */   }
/* 1688:     */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFPropertiesDelegate

 * JD-Core Version:    0.7.0.1

 */