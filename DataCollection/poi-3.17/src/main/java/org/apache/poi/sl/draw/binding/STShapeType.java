/*    1:     */ package org.apache.poi.sl.draw.binding;
/*    2:     */ 
/*    3:     */ import javax.xml.bind.annotation.XmlEnum;
/*    4:     */ import javax.xml.bind.annotation.XmlEnumValue;
/*    5:     */ import javax.xml.bind.annotation.XmlType;
/*    6:     */ 
/*    7:     */ @XmlType(name="ST_ShapeType", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*    8:     */ @XmlEnum
/*    9:     */ public enum STShapeType
/*   10:     */ {
/*   11: 234 */   LINE("line"),  LINE_INV("lineInv"),  TRIANGLE("triangle"),  RT_TRIANGLE("rtTriangle"),  RECT("rect"),  DIAMOND("diamond"),  PARALLELOGRAM("parallelogram"),  TRAPEZOID("trapezoid"),  NON_ISOSCELES_TRAPEZOID("nonIsoscelesTrapezoid"),  PENTAGON("pentagon"),  HEXAGON("hexagon"),  HEPTAGON("heptagon"),  OCTAGON("octagon"),  DECAGON("decagon"),  DODECAGON("dodecagon"),  STAR_4("star4"),  STAR_5("star5"),  STAR_6("star6"),  STAR_7("star7"),  STAR_8("star8"),  STAR_10("star10"),  STAR_12("star12"),  STAR_16("star16"),  STAR_24("star24"),  STAR_32("star32"),  ROUND_RECT("roundRect"),  ROUND_1_RECT("round1Rect"),  ROUND_2_SAME_RECT("round2SameRect"),  ROUND_2_DIAG_RECT("round2DiagRect"),  SNIP_ROUND_RECT("snipRoundRect"),  SNIP_1_RECT("snip1Rect"),  SNIP_2_SAME_RECT("snip2SameRect"),  SNIP_2_DIAG_RECT("snip2DiagRect"),  PLAQUE("plaque"),  ELLIPSE("ellipse"),  TEARDROP("teardrop"),  HOME_PLATE("homePlate"),  CHEVRON("chevron"),  PIE_WEDGE("pieWedge"),  PIE("pie"),  BLOCK_ARC("blockArc"),  DONUT("donut"),  NO_SMOKING("noSmoking"),  RIGHT_ARROW("rightArrow"),  LEFT_ARROW("leftArrow"),  UP_ARROW("upArrow"),  DOWN_ARROW("downArrow"),  STRIPED_RIGHT_ARROW("stripedRightArrow"),  NOTCHED_RIGHT_ARROW("notchedRightArrow"),  BENT_UP_ARROW("bentUpArrow"),  LEFT_RIGHT_ARROW("leftRightArrow"),  UP_DOWN_ARROW("upDownArrow"),  LEFT_UP_ARROW("leftUpArrow"),  LEFT_RIGHT_UP_ARROW("leftRightUpArrow"),  QUAD_ARROW("quadArrow"),  LEFT_ARROW_CALLOUT("leftArrowCallout"),  RIGHT_ARROW_CALLOUT("rightArrowCallout"),  UP_ARROW_CALLOUT("upArrowCallout"),  DOWN_ARROW_CALLOUT("downArrowCallout"),  LEFT_RIGHT_ARROW_CALLOUT("leftRightArrowCallout"),  UP_DOWN_ARROW_CALLOUT("upDownArrowCallout"),  QUAD_ARROW_CALLOUT("quadArrowCallout"),  BENT_ARROW("bentArrow"),  UTURN_ARROW("uturnArrow"),  CIRCULAR_ARROW("circularArrow"),  LEFT_CIRCULAR_ARROW("leftCircularArrow"),  LEFT_RIGHT_CIRCULAR_ARROW("leftRightCircularArrow"),  CURVED_RIGHT_ARROW("curvedRightArrow"),  CURVED_LEFT_ARROW("curvedLeftArrow"),  CURVED_UP_ARROW("curvedUpArrow"),  CURVED_DOWN_ARROW("curvedDownArrow"),  SWOOSH_ARROW("swooshArrow"),  CUBE("cube"),  CAN("can"),  LIGHTNING_BOLT("lightningBolt"),  HEART("heart"),  SUN("sun"),  MOON("moon"),  SMILEY_FACE("smileyFace"),  IRREGULAR_SEAL_1("irregularSeal1"),  IRREGULAR_SEAL_2("irregularSeal2"),  FOLDED_CORNER("foldedCorner"),  BEVEL("bevel"),  FRAME("frame"),  HALF_FRAME("halfFrame"),  CORNER("corner"),  DIAG_STRIPE("diagStripe"),  CHORD("chord"),  ARC("arc"),  LEFT_BRACKET("leftBracket"),  RIGHT_BRACKET("rightBracket"),  LEFT_BRACE("leftBrace"),  RIGHT_BRACE("rightBrace"),  BRACKET_PAIR("bracketPair"),  BRACE_PAIR("bracePair"),  STRAIGHT_CONNECTOR_1("straightConnector1"),  BENT_CONNECTOR_2("bentConnector2"),  BENT_CONNECTOR_3("bentConnector3"),  BENT_CONNECTOR_4("bentConnector4"),  BENT_CONNECTOR_5("bentConnector5"),  CURVED_CONNECTOR_2("curvedConnector2"),  CURVED_CONNECTOR_3("curvedConnector3"),  CURVED_CONNECTOR_4("curvedConnector4"),  CURVED_CONNECTOR_5("curvedConnector5"),  CALLOUT_1("callout1"),  CALLOUT_2("callout2"),  CALLOUT_3("callout3"),  ACCENT_CALLOUT_1("accentCallout1"),  ACCENT_CALLOUT_2("accentCallout2"),  ACCENT_CALLOUT_3("accentCallout3"),  BORDER_CALLOUT_1("borderCallout1"),  BORDER_CALLOUT_2("borderCallout2"),  BORDER_CALLOUT_3("borderCallout3"),  ACCENT_BORDER_CALLOUT_1("accentBorderCallout1"),  ACCENT_BORDER_CALLOUT_2("accentBorderCallout2"),  ACCENT_BORDER_CALLOUT_3("accentBorderCallout3"),  WEDGE_RECT_CALLOUT("wedgeRectCallout"),  WEDGE_ROUND_RECT_CALLOUT("wedgeRoundRectCallout"),  WEDGE_ELLIPSE_CALLOUT("wedgeEllipseCallout"),  CLOUD_CALLOUT("cloudCallout"),  CLOUD("cloud"),  RIBBON("ribbon"),  RIBBON_2("ribbon2"),  ELLIPSE_RIBBON("ellipseRibbon"),  ELLIPSE_RIBBON_2("ellipseRibbon2"),  LEFT_RIGHT_RIBBON("leftRightRibbon"),  VERTICAL_SCROLL("verticalScroll"),  HORIZONTAL_SCROLL("horizontalScroll"),  WAVE("wave"),  DOUBLE_WAVE("doubleWave"),  PLUS("plus"),  FLOW_CHART_PROCESS("flowChartProcess"),  FLOW_CHART_DECISION("flowChartDecision"),  FLOW_CHART_INPUT_OUTPUT("flowChartInputOutput"),  FLOW_CHART_PREDEFINED_PROCESS("flowChartPredefinedProcess"),  FLOW_CHART_INTERNAL_STORAGE("flowChartInternalStorage"),  FLOW_CHART_DOCUMENT("flowChartDocument"),  FLOW_CHART_MULTIDOCUMENT("flowChartMultidocument"),  FLOW_CHART_TERMINATOR("flowChartTerminator"),  FLOW_CHART_PREPARATION("flowChartPreparation"),  FLOW_CHART_MANUAL_INPUT("flowChartManualInput"),  FLOW_CHART_MANUAL_OPERATION("flowChartManualOperation"),  FLOW_CHART_CONNECTOR("flowChartConnector"),  FLOW_CHART_PUNCHED_CARD("flowChartPunchedCard"),  FLOW_CHART_PUNCHED_TAPE("flowChartPunchedTape"),  FLOW_CHART_SUMMING_JUNCTION("flowChartSummingJunction"),  FLOW_CHART_OR("flowChartOr"),  FLOW_CHART_COLLATE("flowChartCollate"),  FLOW_CHART_SORT("flowChartSort"),  FLOW_CHART_EXTRACT("flowChartExtract"),  FLOW_CHART_MERGE("flowChartMerge"),  FLOW_CHART_OFFLINE_STORAGE("flowChartOfflineStorage"),  FLOW_CHART_ONLINE_STORAGE("flowChartOnlineStorage"),  FLOW_CHART_MAGNETIC_TAPE("flowChartMagneticTape"),  FLOW_CHART_MAGNETIC_DISK("flowChartMagneticDisk"),  FLOW_CHART_MAGNETIC_DRUM("flowChartMagneticDrum"),  FLOW_CHART_DISPLAY("flowChartDisplay"),  FLOW_CHART_DELAY("flowChartDelay"),  FLOW_CHART_ALTERNATE_PROCESS("flowChartAlternateProcess"),  FLOW_CHART_OFFPAGE_CONNECTOR("flowChartOffpageConnector"),  ACTION_BUTTON_BLANK("actionButtonBlank"),  ACTION_BUTTON_HOME("actionButtonHome"),  ACTION_BUTTON_HELP("actionButtonHelp"),  ACTION_BUTTON_INFORMATION("actionButtonInformation"),  ACTION_BUTTON_FORWARD_NEXT("actionButtonForwardNext"),  ACTION_BUTTON_BACK_PREVIOUS("actionButtonBackPrevious"),  ACTION_BUTTON_END("actionButtonEnd"),  ACTION_BUTTON_BEGINNING("actionButtonBeginning"),  ACTION_BUTTON_RETURN("actionButtonReturn"),  ACTION_BUTTON_DOCUMENT("actionButtonDocument"),  ACTION_BUTTON_SOUND("actionButtonSound"),  ACTION_BUTTON_MOVIE("actionButtonMovie"),  GEAR_6("gear6"),  GEAR_9("gear9"),  FUNNEL("funnel"),  MATH_PLUS("mathPlus"),  MATH_MINUS("mathMinus"),  MATH_MULTIPLY("mathMultiply"),  MATH_DIVIDE("mathDivide"),  MATH_EQUAL("mathEqual"),  MATH_NOT_EQUAL("mathNotEqual"),  CORNER_TABS("cornerTabs"),  SQUARE_TABS("squareTabs"),  PLAQUE_TABS("plaqueTabs"),  CHART_X("chartX"),  CHART_STAR("chartStar"),  CHART_PLUS("chartPlus");
/*   12:     */   
/*   13:     */   private final String value;
/*   14:     */   
/*   15:     */   private STShapeType(String v)
/*   16:     */   {
/*   17:1541 */     this.value = v;
/*   18:     */   }
/*   19:     */   
/*   20:     */   public String value()
/*   21:     */   {
/*   22:1545 */     return this.value;
/*   23:     */   }
/*   24:     */   
/*   25:     */   public static STShapeType fromValue(String v)
/*   26:     */   {
/*   27:1549 */     for (STShapeType c : ) {
/*   28:1550 */       if (c.value.equals(v)) {
/*   29:1551 */         return c;
/*   30:     */       }
/*   31:     */     }
/*   32:1554 */     throw new IllegalArgumentException(v);
/*   33:     */   }
/*   34:     */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.STShapeType
 * JD-Core Version:    0.7.0.1
 */