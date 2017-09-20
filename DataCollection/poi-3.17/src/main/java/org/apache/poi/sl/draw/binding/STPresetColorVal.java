/*    1:     */ package org.apache.poi.sl.draw.binding;
/*    2:     */ 
/*    3:     */ import javax.xml.bind.annotation.XmlEnum;
/*    4:     */ import javax.xml.bind.annotation.XmlEnumValue;
/*    5:     */ import javax.xml.bind.annotation.XmlType;
/*    6:     */ 
/*    7:     */ @XmlType(name="ST_PresetColorVal", namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*    8:     */ @XmlEnum
/*    9:     */ public enum STPresetColorVal
/*   10:     */ {
/*   11: 187 */   ALICE_BLUE("aliceBlue"),  ANTIQUE_WHITE("antiqueWhite"),  AQUA("aqua"),  AQUAMARINE("aquamarine"),  AZURE("azure"),  BEIGE("beige"),  BISQUE("bisque"),  BLACK("black"),  BLANCHED_ALMOND("blanchedAlmond"),  BLUE("blue"),  BLUE_VIOLET("blueViolet"),  BROWN("brown"),  BURLY_WOOD("burlyWood"),  CADET_BLUE("cadetBlue"),  CHARTREUSE("chartreuse"),  CHOCOLATE("chocolate"),  CORAL("coral"),  CORNFLOWER_BLUE("cornflowerBlue"),  CORNSILK("cornsilk"),  CRIMSON("crimson"),  CYAN("cyan"),  DK_BLUE("dkBlue"),  DK_CYAN("dkCyan"),  DK_GOLDENROD("dkGoldenrod"),  DK_GRAY("dkGray"),  DK_GREEN("dkGreen"),  DK_KHAKI("dkKhaki"),  DK_MAGENTA("dkMagenta"),  DK_OLIVE_GREEN("dkOliveGreen"),  DK_ORANGE("dkOrange"),  DK_ORCHID("dkOrchid"),  DK_RED("dkRed"),  DK_SALMON("dkSalmon"),  DK_SEA_GREEN("dkSeaGreen"),  DK_SLATE_BLUE("dkSlateBlue"),  DK_SLATE_GRAY("dkSlateGray"),  DK_TURQUOISE("dkTurquoise"),  DK_VIOLET("dkViolet"),  DEEP_PINK("deepPink"),  DEEP_SKY_BLUE("deepSkyBlue"),  DIM_GRAY("dimGray"),  DODGER_BLUE("dodgerBlue"),  FIREBRICK("firebrick"),  FLORAL_WHITE("floralWhite"),  FOREST_GREEN("forestGreen"),  FUCHSIA("fuchsia"),  GAINSBORO("gainsboro"),  GHOST_WHITE("ghostWhite"),  GOLD("gold"),  GOLDENROD("goldenrod"),  GRAY("gray"),  GREEN("green"),  GREEN_YELLOW("greenYellow"),  HONEYDEW("honeydew"),  HOT_PINK("hotPink"),  INDIAN_RED("indianRed"),  INDIGO("indigo"),  IVORY("ivory"),  KHAKI("khaki"),  LAVENDER("lavender"),  LAVENDER_BLUSH("lavenderBlush"),  LAWN_GREEN("lawnGreen"),  LEMON_CHIFFON("lemonChiffon"),  LT_BLUE("ltBlue"),  LT_CORAL("ltCoral"),  LT_CYAN("ltCyan"),  LT_GOLDENROD_YELLOW("ltGoldenrodYellow"),  LT_GRAY("ltGray"),  LT_GREEN("ltGreen"),  LT_PINK("ltPink"),  LT_SALMON("ltSalmon"),  LT_SEA_GREEN("ltSeaGreen"),  LT_SKY_BLUE("ltSkyBlue"),  LT_SLATE_GRAY("ltSlateGray"),  LT_STEEL_BLUE("ltSteelBlue"),  LT_YELLOW("ltYellow"),  LIME("lime"),  LIME_GREEN("limeGreen"),  LINEN("linen"),  MAGENTA("magenta"),  MAROON("maroon"),  MED_AQUAMARINE("medAquamarine"),  MED_BLUE("medBlue"),  MED_ORCHID("medOrchid"),  MED_PURPLE("medPurple"),  MED_SEA_GREEN("medSeaGreen"),  MED_SLATE_BLUE("medSlateBlue"),  MED_SPRING_GREEN("medSpringGreen"),  MED_TURQUOISE("medTurquoise"),  MED_VIOLET_RED("medVioletRed"),  MIDNIGHT_BLUE("midnightBlue"),  MINT_CREAM("mintCream"),  MISTY_ROSE("mistyRose"),  MOCCASIN("moccasin"),  NAVAJO_WHITE("navajoWhite"),  NAVY("navy"),  OLD_LACE("oldLace"),  OLIVE("olive"),  OLIVE_DRAB("oliveDrab"),  ORANGE("orange"),  ORANGE_RED("orangeRed"),  ORCHID("orchid"),  PALE_GOLDENROD("paleGoldenrod"),  PALE_GREEN("paleGreen"),  PALE_TURQUOISE("paleTurquoise"),  PALE_VIOLET_RED("paleVioletRed"),  PAPAYA_WHIP("papayaWhip"),  PEACH_PUFF("peachPuff"),  PERU("peru"),  PINK("pink"),  PLUM("plum"),  POWDER_BLUE("powderBlue"),  PURPLE("purple"),  RED("red"),  ROSY_BROWN("rosyBrown"),  ROYAL_BLUE("royalBlue"),  SADDLE_BROWN("saddleBrown"),  SALMON("salmon"),  SANDY_BROWN("sandyBrown"),  SEA_GREEN("seaGreen"),  SEA_SHELL("seaShell"),  SIENNA("sienna"),  SILVER("silver"),  SKY_BLUE("skyBlue"),  SLATE_BLUE("slateBlue"),  SLATE_GRAY("slateGray"),  SNOW("snow"),  SPRING_GREEN("springGreen"),  STEEL_BLUE("steelBlue"),  TAN("tan"),  TEAL("teal"),  THISTLE("thistle"),  TOMATO("tomato"),  TURQUOISE("turquoise"),  VIOLET("violet"),  WHEAT("wheat"),  WHITE("white"),  WHITE_SMOKE("whiteSmoke"),  YELLOW("yellow"),  YELLOW_GREEN("yellowGreen");
/*   12:     */   
/*   13:     */   private final String value;
/*   14:     */   
/*   15:     */   private STPresetColorVal(String v)
/*   16:     */   {
/*   17:1165 */     this.value = v;
/*   18:     */   }
/*   19:     */   
/*   20:     */   public String value()
/*   21:     */   {
/*   22:1169 */     return this.value;
/*   23:     */   }
/*   24:     */   
/*   25:     */   public static STPresetColorVal fromValue(String v)
/*   26:     */   {
/*   27:1173 */     for (STPresetColorVal c : ) {
/*   28:1174 */       if (c.value.equals(v)) {
/*   29:1175 */         return c;
/*   30:     */       }
/*   31:     */     }
/*   32:1178 */     throw new IllegalArgumentException(v);
/*   33:     */   }
/*   34:     */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.STPresetColorVal
 * JD-Core Version:    0.7.0.1
 */