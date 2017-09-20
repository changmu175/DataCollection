/*    1:     */ package org.apache.poi.hssf.record;
/*    2:     */ 
/*    3:     */ import org.apache.poi.util.BitField;
/*    4:     */ import org.apache.poi.util.BitFieldFactory;
/*    5:     */ import org.apache.poi.util.LittleEndianOutput;
/*    6:     */ 
/*    7:     */ public final class ExtendedFormatRecord
/*    8:     */   extends StandardRecord
/*    9:     */ {
/*   10:     */   public static final short sid = 224;
/*   11:     */   public static final short NULL = -16;
/*   12:     */   public static final short XF_STYLE = 1;
/*   13:     */   public static final short XF_CELL = 0;
/*   14:     */   public static final short NONE = 0;
/*   15:     */   public static final short THIN = 1;
/*   16:     */   public static final short MEDIUM = 2;
/*   17:     */   public static final short DASHED = 3;
/*   18:     */   public static final short DOTTED = 4;
/*   19:     */   public static final short THICK = 5;
/*   20:     */   public static final short DOUBLE = 6;
/*   21:     */   public static final short HAIR = 7;
/*   22:     */   public static final short MEDIUM_DASHED = 8;
/*   23:     */   public static final short DASH_DOT = 9;
/*   24:     */   public static final short MEDIUM_DASH_DOT = 10;
/*   25:     */   public static final short DASH_DOT_DOT = 11;
/*   26:     */   public static final short MEDIUM_DASH_DOT_DOT = 12;
/*   27:     */   public static final short SLANTED_DASH_DOT = 13;
/*   28:     */   public static final short GENERAL = 0;
/*   29:     */   public static final short LEFT = 1;
/*   30:     */   public static final short CENTER = 2;
/*   31:     */   public static final short RIGHT = 3;
/*   32:     */   public static final short FILL = 4;
/*   33:     */   public static final short JUSTIFY = 5;
/*   34:     */   public static final short CENTER_SELECTION = 6;
/*   35:     */   public static final short VERTICAL_TOP = 0;
/*   36:     */   public static final short VERTICAL_CENTER = 1;
/*   37:     */   public static final short VERTICAL_BOTTOM = 2;
/*   38:     */   public static final short VERTICAL_JUSTIFY = 3;
/*   39:     */   public static final short NO_FILL = 0;
/*   40:     */   public static final short SOLID_FILL = 1;
/*   41:     */   public static final short FINE_DOTS = 2;
/*   42:     */   public static final short ALT_BARS = 3;
/*   43:     */   public static final short SPARSE_DOTS = 4;
/*   44:     */   public static final short THICK_HORZ_BANDS = 5;
/*   45:     */   public static final short THICK_VERT_BANDS = 6;
/*   46:     */   public static final short THICK_BACKWARD_DIAG = 7;
/*   47:     */   public static final short THICK_FORWARD_DIAG = 8;
/*   48:     */   public static final short BIG_SPOTS = 9;
/*   49:     */   public static final short BRICKS = 10;
/*   50:     */   public static final short THIN_HORZ_BANDS = 11;
/*   51:     */   public static final short THIN_VERT_BANDS = 12;
/*   52:     */   public static final short THIN_BACKWARD_DIAG = 13;
/*   53:     */   public static final short THIN_FORWARD_DIAG = 14;
/*   54:     */   public static final short SQUARES = 15;
/*   55:     */   public static final short DIAMONDS = 16;
/*   56:     */   private short field_1_font_index;
/*   57:     */   private short field_2_format_index;
/*   58: 108 */   private static final BitField _locked = BitFieldFactory.getInstance(1);
/*   59: 109 */   private static final BitField _hidden = BitFieldFactory.getInstance(2);
/*   60: 110 */   private static final BitField _xf_type = BitFieldFactory.getInstance(4);
/*   61: 111 */   private static final BitField _123_prefix = BitFieldFactory.getInstance(8);
/*   62: 112 */   private static final BitField _parent_index = BitFieldFactory.getInstance(65520);
/*   63:     */   private short field_3_cell_options;
/*   64: 116 */   private static final BitField _alignment = BitFieldFactory.getInstance(7);
/*   65: 117 */   private static final BitField _wrap_text = BitFieldFactory.getInstance(8);
/*   66: 118 */   private static final BitField _vertical_alignment = BitFieldFactory.getInstance(112);
/*   67: 119 */   private static final BitField _justify_last = BitFieldFactory.getInstance(128);
/*   68: 120 */   private static final BitField _rotation = BitFieldFactory.getInstance(65280);
/*   69:     */   private short field_4_alignment_options;
/*   70: 124 */   private static final BitField _indent = BitFieldFactory.getInstance(15);
/*   71: 126 */   private static final BitField _shrink_to_fit = BitFieldFactory.getInstance(16);
/*   72: 128 */   private static final BitField _merge_cells = BitFieldFactory.getInstance(32);
/*   73: 130 */   private static final BitField _reading_order = BitFieldFactory.getInstance(192);
/*   74: 134 */   private static final BitField _indent_not_parent_format = BitFieldFactory.getInstance(1024);
/*   75: 136 */   private static final BitField _indent_not_parent_font = BitFieldFactory.getInstance(2048);
/*   76: 138 */   private static final BitField _indent_not_parent_alignment = BitFieldFactory.getInstance(4096);
/*   77: 140 */   private static final BitField _indent_not_parent_border = BitFieldFactory.getInstance(8192);
/*   78: 142 */   private static final BitField _indent_not_parent_pattern = BitFieldFactory.getInstance(16384);
/*   79: 144 */   private static final BitField _indent_not_parent_cell_options = BitFieldFactory.getInstance(32768);
/*   80:     */   private short field_5_indention_options;
/*   81: 149 */   private static final BitField _border_left = BitFieldFactory.getInstance(15);
/*   82: 150 */   private static final BitField _border_right = BitFieldFactory.getInstance(240);
/*   83: 151 */   private static final BitField _border_top = BitFieldFactory.getInstance(3840);
/*   84: 152 */   private static final BitField _border_bottom = BitFieldFactory.getInstance(61440);
/*   85:     */   private short field_6_border_options;
/*   86: 157 */   private static final BitField _left_border_palette_idx = BitFieldFactory.getInstance(127);
/*   87: 159 */   private static final BitField _right_border_palette_idx = BitFieldFactory.getInstance(16256);
/*   88: 161 */   private static final BitField _diag = BitFieldFactory.getInstance(49152);
/*   89:     */   private short field_7_palette_options;
/*   90: 166 */   private static final BitField _top_border_palette_idx = BitFieldFactory.getInstance(127);
/*   91: 168 */   private static final BitField _bottom_border_palette_idx = BitFieldFactory.getInstance(16256);
/*   92: 170 */   private static final BitField _adtl_diag = BitFieldFactory.getInstance(2080768);
/*   93: 172 */   private static final BitField _adtl_diag_line_style = BitFieldFactory.getInstance(31457280);
/*   94: 176 */   private static final BitField _adtl_fill_pattern = BitFieldFactory.getInstance(-67108864);
/*   95:     */   private int field_8_adtl_palette_options;
/*   96: 181 */   private static final BitField _fill_foreground = BitFieldFactory.getInstance(127);
/*   97: 182 */   private static final BitField _fill_background = BitFieldFactory.getInstance(16256);
/*   98:     */   private short field_9_fill_palette_options;
/*   99:     */   
/*  100:     */   public ExtendedFormatRecord() {}
/*  101:     */   
/*  102:     */   public ExtendedFormatRecord(RecordInputStream in)
/*  103:     */   {
/*  104: 199 */     this.field_1_font_index = in.readShort();
/*  105: 200 */     this.field_2_format_index = in.readShort();
/*  106: 201 */     this.field_3_cell_options = in.readShort();
/*  107: 202 */     this.field_4_alignment_options = in.readShort();
/*  108: 203 */     this.field_5_indention_options = in.readShort();
/*  109: 204 */     this.field_6_border_options = in.readShort();
/*  110: 205 */     this.field_7_palette_options = in.readShort();
/*  111: 206 */     this.field_8_adtl_palette_options = in.readInt();
/*  112: 207 */     this.field_9_fill_palette_options = in.readShort();
/*  113:     */   }
/*  114:     */   
/*  115:     */   public void setFontIndex(short index)
/*  116:     */   {
/*  117: 220 */     this.field_1_font_index = index;
/*  118:     */   }
/*  119:     */   
/*  120:     */   public void setFormatIndex(short index)
/*  121:     */   {
/*  122: 233 */     this.field_2_format_index = index;
/*  123:     */   }
/*  124:     */   
/*  125:     */   public void setCellOptions(short options)
/*  126:     */   {
/*  127: 247 */     this.field_3_cell_options = options;
/*  128:     */   }
/*  129:     */   
/*  130:     */   public void setLocked(boolean locked)
/*  131:     */   {
/*  132: 262 */     this.field_3_cell_options = _locked.setShortBoolean(this.field_3_cell_options, locked);
/*  133:     */   }
/*  134:     */   
/*  135:     */   public void setHidden(boolean hidden)
/*  136:     */   {
/*  137: 276 */     this.field_3_cell_options = _hidden.setShortBoolean(this.field_3_cell_options, hidden);
/*  138:     */   }
/*  139:     */   
/*  140:     */   public void setXFType(short type)
/*  141:     */   {
/*  142: 292 */     this.field_3_cell_options = _xf_type.setShortValue(this.field_3_cell_options, type);
/*  143:     */   }
/*  144:     */   
/*  145:     */   public void set123Prefix(boolean prefix)
/*  146:     */   {
/*  147: 306 */     this.field_3_cell_options = _123_prefix.setShortBoolean(this.field_3_cell_options, prefix);
/*  148:     */   }
/*  149:     */   
/*  150:     */   public void setParentIndex(short parent)
/*  151:     */   {
/*  152: 323 */     this.field_3_cell_options = _parent_index.setShortValue(this.field_3_cell_options, parent);
/*  153:     */   }
/*  154:     */   
/*  155:     */   public void setAlignmentOptions(short options)
/*  156:     */   {
/*  157: 339 */     this.field_4_alignment_options = options;
/*  158:     */   }
/*  159:     */   
/*  160:     */   public void setAlignment(short align)
/*  161:     */   {
/*  162: 359 */     this.field_4_alignment_options = _alignment.setShortValue(this.field_4_alignment_options, align);
/*  163:     */   }
/*  164:     */   
/*  165:     */   public void setWrapText(boolean wrapped)
/*  166:     */   {
/*  167: 373 */     this.field_4_alignment_options = _wrap_text.setShortBoolean(this.field_4_alignment_options, wrapped);
/*  168:     */   }
/*  169:     */   
/*  170:     */   public void setVerticalAlignment(short align)
/*  171:     */   {
/*  172: 392 */     this.field_4_alignment_options = _vertical_alignment.setShortValue(this.field_4_alignment_options, align);
/*  173:     */   }
/*  174:     */   
/*  175:     */   public void setJustifyLast(short justify)
/*  176:     */   {
/*  177: 407 */     this.field_4_alignment_options = _justify_last.setShortValue(this.field_4_alignment_options, justify);
/*  178:     */   }
/*  179:     */   
/*  180:     */   public void setRotation(short rotation)
/*  181:     */   {
/*  182: 421 */     this.field_4_alignment_options = _rotation.setShortValue(this.field_4_alignment_options, rotation);
/*  183:     */   }
/*  184:     */   
/*  185:     */   public void setIndentionOptions(short options)
/*  186:     */   {
/*  187: 436 */     this.field_5_indention_options = options;
/*  188:     */   }
/*  189:     */   
/*  190:     */   public void setIndent(short indent)
/*  191:     */   {
/*  192: 450 */     this.field_5_indention_options = _indent.setShortValue(this.field_5_indention_options, indent);
/*  193:     */   }
/*  194:     */   
/*  195:     */   public void setShrinkToFit(boolean shrink)
/*  196:     */   {
/*  197: 464 */     this.field_5_indention_options = _shrink_to_fit.setShortBoolean(this.field_5_indention_options, shrink);
/*  198:     */   }
/*  199:     */   
/*  200:     */   public void setMergeCells(boolean merge)
/*  201:     */   {
/*  202: 478 */     this.field_5_indention_options = _merge_cells.setShortBoolean(this.field_5_indention_options, merge);
/*  203:     */   }
/*  204:     */   
/*  205:     */   public void setReadingOrder(short order)
/*  206:     */   {
/*  207: 492 */     this.field_5_indention_options = _reading_order.setShortValue(this.field_5_indention_options, order);
/*  208:     */   }
/*  209:     */   
/*  210:     */   public void setIndentNotParentFormat(boolean parent)
/*  211:     */   {
/*  212: 507 */     this.field_5_indention_options = _indent_not_parent_format.setShortBoolean(this.field_5_indention_options, parent);
/*  213:     */   }
/*  214:     */   
/*  215:     */   public void setIndentNotParentFont(boolean font)
/*  216:     */   {
/*  217: 523 */     this.field_5_indention_options = _indent_not_parent_font.setShortBoolean(this.field_5_indention_options, font);
/*  218:     */   }
/*  219:     */   
/*  220:     */   public void setIndentNotParentAlignment(boolean alignment)
/*  221:     */   {
/*  222: 539 */     this.field_5_indention_options = _indent_not_parent_alignment.setShortBoolean(this.field_5_indention_options, alignment);
/*  223:     */   }
/*  224:     */   
/*  225:     */   public void setIndentNotParentBorder(boolean border)
/*  226:     */   {
/*  227: 555 */     this.field_5_indention_options = _indent_not_parent_border.setShortBoolean(this.field_5_indention_options, border);
/*  228:     */   }
/*  229:     */   
/*  230:     */   public void setIndentNotParentPattern(boolean pattern)
/*  231:     */   {
/*  232: 571 */     this.field_5_indention_options = _indent_not_parent_pattern.setShortBoolean(this.field_5_indention_options, pattern);
/*  233:     */   }
/*  234:     */   
/*  235:     */   public void setIndentNotParentCellOptions(boolean options)
/*  236:     */   {
/*  237: 587 */     this.field_5_indention_options = _indent_not_parent_cell_options.setShortBoolean(this.field_5_indention_options, options);
/*  238:     */   }
/*  239:     */   
/*  240:     */   public void setBorderOptions(short options)
/*  241:     */   {
/*  242: 604 */     this.field_6_border_options = options;
/*  243:     */   }
/*  244:     */   
/*  245:     */   public void setBorderLeft(short border)
/*  246:     */   {
/*  247: 633 */     this.field_6_border_options = _border_left.setShortValue(this.field_6_border_options, border);
/*  248:     */   }
/*  249:     */   
/*  250:     */   public void setBorderRight(short border)
/*  251:     */   {
/*  252: 661 */     this.field_6_border_options = _border_right.setShortValue(this.field_6_border_options, border);
/*  253:     */   }
/*  254:     */   
/*  255:     */   public void setBorderTop(short border)
/*  256:     */   {
/*  257: 689 */     this.field_6_border_options = _border_top.setShortValue(this.field_6_border_options, border);
/*  258:     */   }
/*  259:     */   
/*  260:     */   public void setBorderBottom(short border)
/*  261:     */   {
/*  262: 717 */     this.field_6_border_options = _border_bottom.setShortValue(this.field_6_border_options, border);
/*  263:     */   }
/*  264:     */   
/*  265:     */   public void setPaletteOptions(short options)
/*  266:     */   {
/*  267: 734 */     this.field_7_palette_options = options;
/*  268:     */   }
/*  269:     */   
/*  270:     */   public void setLeftBorderPaletteIdx(short border)
/*  271:     */   {
/*  272: 749 */     this.field_7_palette_options = _left_border_palette_idx.setShortValue(this.field_7_palette_options, border);
/*  273:     */   }
/*  274:     */   
/*  275:     */   public void setRightBorderPaletteIdx(short border)
/*  276:     */   {
/*  277: 764 */     this.field_7_palette_options = _right_border_palette_idx.setShortValue(this.field_7_palette_options, border);
/*  278:     */   }
/*  279:     */   
/*  280:     */   public void setDiag(short diag)
/*  281:     */   {
/*  282: 782 */     this.field_7_palette_options = _diag.setShortValue(this.field_7_palette_options, diag);
/*  283:     */   }
/*  284:     */   
/*  285:     */   public void setAdtlPaletteOptions(short options)
/*  286:     */   {
/*  287: 799 */     this.field_8_adtl_palette_options = options;
/*  288:     */   }
/*  289:     */   
/*  290:     */   public void setTopBorderPaletteIdx(short border)
/*  291:     */   {
/*  292: 814 */     this.field_8_adtl_palette_options = _top_border_palette_idx.setValue(this.field_8_adtl_palette_options, border);
/*  293:     */   }
/*  294:     */   
/*  295:     */   public void setBottomBorderPaletteIdx(short border)
/*  296:     */   {
/*  297: 829 */     this.field_8_adtl_palette_options = _bottom_border_palette_idx.setValue(this.field_8_adtl_palette_options, border);
/*  298:     */   }
/*  299:     */   
/*  300:     */   public void setAdtlDiag(short diag)
/*  301:     */   {
/*  302: 845 */     this.field_8_adtl_palette_options = _adtl_diag.setValue(this.field_8_adtl_palette_options, diag);
/*  303:     */   }
/*  304:     */   
/*  305:     */   public void setAdtlDiagLineStyle(short diag)
/*  306:     */   {
/*  307: 873 */     this.field_8_adtl_palette_options = _adtl_diag_line_style.setValue(this.field_8_adtl_palette_options, diag);
/*  308:     */   }
/*  309:     */   
/*  310:     */   public void setAdtlFillPattern(short fill)
/*  311:     */   {
/*  312: 905 */     this.field_8_adtl_palette_options = _adtl_fill_pattern.setValue(this.field_8_adtl_palette_options, fill);
/*  313:     */   }
/*  314:     */   
/*  315:     */   public void setFillPaletteOptions(short options)
/*  316:     */   {
/*  317: 917 */     this.field_9_fill_palette_options = options;
/*  318:     */   }
/*  319:     */   
/*  320:     */   public void setFillForeground(short color)
/*  321:     */   {
/*  322: 930 */     this.field_9_fill_palette_options = _fill_foreground.setShortValue(this.field_9_fill_palette_options, color);
/*  323:     */   }
/*  324:     */   
/*  325:     */   public void setFillBackground(short color)
/*  326:     */   {
/*  327: 945 */     this.field_9_fill_palette_options = _fill_background.setShortValue(this.field_9_fill_palette_options, color);
/*  328:     */   }
/*  329:     */   
/*  330:     */   public short getFontIndex()
/*  331:     */   {
/*  332: 960 */     return this.field_1_font_index;
/*  333:     */   }
/*  334:     */   
/*  335:     */   public short getFormatIndex()
/*  336:     */   {
/*  337: 973 */     return this.field_2_format_index;
/*  338:     */   }
/*  339:     */   
/*  340:     */   public short getCellOptions()
/*  341:     */   {
/*  342: 987 */     return this.field_3_cell_options;
/*  343:     */   }
/*  344:     */   
/*  345:     */   public boolean isLocked()
/*  346:     */   {
/*  347:1002 */     return _locked.isSet(this.field_3_cell_options);
/*  348:     */   }
/*  349:     */   
/*  350:     */   public boolean isHidden()
/*  351:     */   {
/*  352:1015 */     return _hidden.isSet(this.field_3_cell_options);
/*  353:     */   }
/*  354:     */   
/*  355:     */   public short getXFType()
/*  356:     */   {
/*  357:1030 */     return _xf_type.getShortValue(this.field_3_cell_options);
/*  358:     */   }
/*  359:     */   
/*  360:     */   public boolean get123Prefix()
/*  361:     */   {
/*  362:1043 */     return _123_prefix.isSet(this.field_3_cell_options);
/*  363:     */   }
/*  364:     */   
/*  365:     */   public short getParentIndex()
/*  366:     */   {
/*  367:1057 */     return _parent_index.getShortValue(this.field_3_cell_options);
/*  368:     */   }
/*  369:     */   
/*  370:     */   public short getAlignmentOptions()
/*  371:     */   {
/*  372:1072 */     return this.field_4_alignment_options;
/*  373:     */   }
/*  374:     */   
/*  375:     */   public short getAlignment()
/*  376:     */   {
/*  377:1094 */     return _alignment.getShortValue(this.field_4_alignment_options);
/*  378:     */   }
/*  379:     */   
/*  380:     */   public boolean getWrapText()
/*  381:     */   {
/*  382:1107 */     return _wrap_text.isSet(this.field_4_alignment_options);
/*  383:     */   }
/*  384:     */   
/*  385:     */   public short getVerticalAlignment()
/*  386:     */   {
/*  387:1125 */     return _vertical_alignment.getShortValue(this.field_4_alignment_options);
/*  388:     */   }
/*  389:     */   
/*  390:     */   public short getJustifyLast()
/*  391:     */   {
/*  392:1139 */     return _justify_last.getShortValue(this.field_4_alignment_options);
/*  393:     */   }
/*  394:     */   
/*  395:     */   public short getRotation()
/*  396:     */   {
/*  397:1152 */     return _rotation.getShortValue(this.field_4_alignment_options);
/*  398:     */   }
/*  399:     */   
/*  400:     */   public short getIndentionOptions()
/*  401:     */   {
/*  402:1168 */     return this.field_5_indention_options;
/*  403:     */   }
/*  404:     */   
/*  405:     */   public short getIndent()
/*  406:     */   {
/*  407:1182 */     return _indent.getShortValue(this.field_5_indention_options);
/*  408:     */   }
/*  409:     */   
/*  410:     */   public boolean getShrinkToFit()
/*  411:     */   {
/*  412:1195 */     return _shrink_to_fit.isSet(this.field_5_indention_options);
/*  413:     */   }
/*  414:     */   
/*  415:     */   public boolean getMergeCells()
/*  416:     */   {
/*  417:1208 */     return _merge_cells.isSet(this.field_5_indention_options);
/*  418:     */   }
/*  419:     */   
/*  420:     */   public short getReadingOrder()
/*  421:     */   {
/*  422:1221 */     return _reading_order.getShortValue(this.field_5_indention_options);
/*  423:     */   }
/*  424:     */   
/*  425:     */   public boolean isIndentNotParentFormat()
/*  426:     */   {
/*  427:1235 */     return _indent_not_parent_format.isSet(this.field_5_indention_options);
/*  428:     */   }
/*  429:     */   
/*  430:     */   public boolean isIndentNotParentFont()
/*  431:     */   {
/*  432:1249 */     return _indent_not_parent_font.isSet(this.field_5_indention_options);
/*  433:     */   }
/*  434:     */   
/*  435:     */   public boolean isIndentNotParentAlignment()
/*  436:     */   {
/*  437:1263 */     return _indent_not_parent_alignment.isSet(this.field_5_indention_options);
/*  438:     */   }
/*  439:     */   
/*  440:     */   public boolean isIndentNotParentBorder()
/*  441:     */   {
/*  442:1277 */     return _indent_not_parent_border.isSet(this.field_5_indention_options);
/*  443:     */   }
/*  444:     */   
/*  445:     */   public boolean isIndentNotParentPattern()
/*  446:     */   {
/*  447:1291 */     return _indent_not_parent_pattern.isSet(this.field_5_indention_options);
/*  448:     */   }
/*  449:     */   
/*  450:     */   public boolean isIndentNotParentCellOptions()
/*  451:     */   {
/*  452:1305 */     return _indent_not_parent_cell_options.isSet(this.field_5_indention_options);
/*  453:     */   }
/*  454:     */   
/*  455:     */   public short getBorderOptions()
/*  456:     */   {
/*  457:1322 */     return this.field_6_border_options;
/*  458:     */   }
/*  459:     */   
/*  460:     */   public short getBorderLeft()
/*  461:     */   {
/*  462:1351 */     return _border_left.getShortValue(this.field_6_border_options);
/*  463:     */   }
/*  464:     */   
/*  465:     */   public short getBorderRight()
/*  466:     */   {
/*  467:1378 */     return _border_right.getShortValue(this.field_6_border_options);
/*  468:     */   }
/*  469:     */   
/*  470:     */   public short getBorderTop()
/*  471:     */   {
/*  472:1405 */     return _border_top.getShortValue(this.field_6_border_options);
/*  473:     */   }
/*  474:     */   
/*  475:     */   public short getBorderBottom()
/*  476:     */   {
/*  477:1432 */     return _border_bottom.getShortValue(this.field_6_border_options);
/*  478:     */   }
/*  479:     */   
/*  480:     */   public short getPaletteOptions()
/*  481:     */   {
/*  482:1448 */     return this.field_7_palette_options;
/*  483:     */   }
/*  484:     */   
/*  485:     */   public short getLeftBorderPaletteIdx()
/*  486:     */   {
/*  487:1463 */     return _left_border_palette_idx.getShortValue(this.field_7_palette_options);
/*  488:     */   }
/*  489:     */   
/*  490:     */   public short getRightBorderPaletteIdx()
/*  491:     */   {
/*  492:1477 */     return _right_border_palette_idx.getShortValue(this.field_7_palette_options);
/*  493:     */   }
/*  494:     */   
/*  495:     */   public short getDiag()
/*  496:     */   {
/*  497:1494 */     return _diag.getShortValue(this.field_7_palette_options);
/*  498:     */   }
/*  499:     */   
/*  500:     */   public int getAdtlPaletteOptions()
/*  501:     */   {
/*  502:1511 */     return this.field_8_adtl_palette_options;
/*  503:     */   }
/*  504:     */   
/*  505:     */   public short getTopBorderPaletteIdx()
/*  506:     */   {
/*  507:1526 */     return (short)_top_border_palette_idx.getValue(this.field_8_adtl_palette_options);
/*  508:     */   }
/*  509:     */   
/*  510:     */   public short getBottomBorderPaletteIdx()
/*  511:     */   {
/*  512:1540 */     return (short)_bottom_border_palette_idx.getValue(this.field_8_adtl_palette_options);
/*  513:     */   }
/*  514:     */   
/*  515:     */   public short getAdtlDiag()
/*  516:     */   {
/*  517:1555 */     return (short)_adtl_diag.getValue(this.field_8_adtl_palette_options);
/*  518:     */   }
/*  519:     */   
/*  520:     */   public short getAdtlDiagLineStyle()
/*  521:     */   {
/*  522:1582 */     return (short)_adtl_diag_line_style.getValue(this.field_8_adtl_palette_options);
/*  523:     */   }
/*  524:     */   
/*  525:     */   public short getAdtlFillPattern()
/*  526:     */   {
/*  527:1613 */     return (short)_adtl_fill_pattern.getValue(this.field_8_adtl_palette_options);
/*  528:     */   }
/*  529:     */   
/*  530:     */   public short getFillPaletteOptions()
/*  531:     */   {
/*  532:1630 */     return this.field_9_fill_palette_options;
/*  533:     */   }
/*  534:     */   
/*  535:     */   public short getFillForeground()
/*  536:     */   {
/*  537:1645 */     return _fill_foreground.getShortValue(this.field_9_fill_palette_options);
/*  538:     */   }
/*  539:     */   
/*  540:     */   public short getFillBackground()
/*  541:     */   {
/*  542:1657 */     return _fill_background.getShortValue(this.field_9_fill_palette_options);
/*  543:     */   }
/*  544:     */   
/*  545:     */   public String toString()
/*  546:     */   {
/*  547:1663 */     StringBuffer buffer = new StringBuffer();
/*  548:     */     
/*  549:1665 */     buffer.append("[EXTENDEDFORMAT]\n");
/*  550:1666 */     if (getXFType() == 1) {
/*  551:1668 */       buffer.append(" STYLE_RECORD_TYPE\n");
/*  552:1670 */     } else if (getXFType() == 0) {
/*  553:1672 */       buffer.append(" CELL_RECORD_TYPE\n");
/*  554:     */     }
/*  555:1674 */     buffer.append("    .fontindex       = ").append(Integer.toHexString(getFontIndex())).append("\n");
/*  556:     */     
/*  557:1676 */     buffer.append("    .formatindex     = ").append(Integer.toHexString(getFormatIndex())).append("\n");
/*  558:     */     
/*  559:1678 */     buffer.append("    .celloptions     = ").append(Integer.toHexString(getCellOptions())).append("\n");
/*  560:     */     
/*  561:1680 */     buffer.append("          .islocked  = ").append(isLocked()).append("\n");
/*  562:     */     
/*  563:1682 */     buffer.append("          .ishidden  = ").append(isHidden()).append("\n");
/*  564:     */     
/*  565:1684 */     buffer.append("          .recordtype= ").append(Integer.toHexString(getXFType())).append("\n");
/*  566:     */     
/*  567:1686 */     buffer.append("          .parentidx = ").append(Integer.toHexString(getParentIndex())).append("\n");
/*  568:     */     
/*  569:1688 */     buffer.append("    .alignmentoptions= ").append(Integer.toHexString(getAlignmentOptions())).append("\n");
/*  570:     */     
/*  571:1690 */     buffer.append("          .alignment = ").append(getAlignment()).append("\n");
/*  572:     */     
/*  573:1692 */     buffer.append("          .wraptext  = ").append(getWrapText()).append("\n");
/*  574:     */     
/*  575:1694 */     buffer.append("          .valignment= ").append(Integer.toHexString(getVerticalAlignment())).append("\n");
/*  576:     */     
/*  577:1696 */     buffer.append("          .justlast  = ").append(Integer.toHexString(getJustifyLast())).append("\n");
/*  578:     */     
/*  579:1698 */     buffer.append("          .rotation  = ").append(Integer.toHexString(getRotation())).append("\n");
/*  580:     */     
/*  581:1700 */     buffer.append("    .indentionoptions= ").append(Integer.toHexString(getIndentionOptions())).append("\n");
/*  582:     */     
/*  583:1702 */     buffer.append("          .indent    = ").append(Integer.toHexString(getIndent())).append("\n");
/*  584:     */     
/*  585:1704 */     buffer.append("          .shrinktoft= ").append(getShrinkToFit()).append("\n");
/*  586:     */     
/*  587:1706 */     buffer.append("          .mergecells= ").append(getMergeCells()).append("\n");
/*  588:     */     
/*  589:1708 */     buffer.append("          .readngordr= ").append(Integer.toHexString(getReadingOrder())).append("\n");
/*  590:     */     
/*  591:1710 */     buffer.append("          .formatflag= ").append(isIndentNotParentFormat()).append("\n");
/*  592:     */     
/*  593:1712 */     buffer.append("          .fontflag  = ").append(isIndentNotParentFont()).append("\n");
/*  594:     */     
/*  595:1714 */     buffer.append("          .prntalgnmt= ").append(isIndentNotParentAlignment()).append("\n");
/*  596:     */     
/*  597:1716 */     buffer.append("          .borderflag= ").append(isIndentNotParentBorder()).append("\n");
/*  598:     */     
/*  599:1718 */     buffer.append("          .paternflag= ").append(isIndentNotParentPattern()).append("\n");
/*  600:     */     
/*  601:1720 */     buffer.append("          .celloption= ").append(isIndentNotParentCellOptions()).append("\n");
/*  602:     */     
/*  603:1722 */     buffer.append("    .borderoptns     = ").append(Integer.toHexString(getBorderOptions())).append("\n");
/*  604:     */     
/*  605:1724 */     buffer.append("          .lftln     = ").append(Integer.toHexString(getBorderLeft())).append("\n");
/*  606:     */     
/*  607:1726 */     buffer.append("          .rgtln     = ").append(Integer.toHexString(getBorderRight())).append("\n");
/*  608:     */     
/*  609:1728 */     buffer.append("          .topln     = ").append(Integer.toHexString(getBorderTop())).append("\n");
/*  610:     */     
/*  611:1730 */     buffer.append("          .btmln     = ").append(Integer.toHexString(getBorderBottom())).append("\n");
/*  612:     */     
/*  613:1732 */     buffer.append("    .paleteoptns     = ").append(Integer.toHexString(getPaletteOptions())).append("\n");
/*  614:     */     
/*  615:1734 */     buffer.append("          .leftborder= ").append(Integer.toHexString(getLeftBorderPaletteIdx())).append("\n");
/*  616:     */     
/*  617:     */ 
/*  618:1737 */     buffer.append("          .rghtborder= ").append(Integer.toHexString(getRightBorderPaletteIdx())).append("\n");
/*  619:     */     
/*  620:     */ 
/*  621:1740 */     buffer.append("          .diag      = ").append(Integer.toHexString(getDiag())).append("\n");
/*  622:     */     
/*  623:1742 */     buffer.append("    .paleteoptn2     = ").append(Integer.toHexString(getAdtlPaletteOptions())).append("\n");
/*  624:     */     
/*  625:     */ 
/*  626:1745 */     buffer.append("          .topborder = ").append(Integer.toHexString(getTopBorderPaletteIdx())).append("\n");
/*  627:     */     
/*  628:     */ 
/*  629:1748 */     buffer.append("          .botmborder= ").append(Integer.toHexString(getBottomBorderPaletteIdx())).append("\n");
/*  630:     */     
/*  631:     */ 
/*  632:1751 */     buffer.append("          .adtldiag  = ").append(Integer.toHexString(getAdtlDiag())).append("\n");
/*  633:     */     
/*  634:1753 */     buffer.append("          .diaglnstyl= ").append(Integer.toHexString(getAdtlDiagLineStyle())).append("\n");
/*  635:     */     
/*  636:1755 */     buffer.append("          .fillpattrn= ").append(Integer.toHexString(getAdtlFillPattern())).append("\n");
/*  637:     */     
/*  638:1757 */     buffer.append("    .fillpaloptn     = ").append(Integer.toHexString(getFillPaletteOptions())).append("\n");
/*  639:     */     
/*  640:     */ 
/*  641:1760 */     buffer.append("          .foreground= ").append(Integer.toHexString(getFillForeground())).append("\n");
/*  642:     */     
/*  643:1762 */     buffer.append("          .background= ").append(Integer.toHexString(getFillBackground())).append("\n");
/*  644:     */     
/*  645:1764 */     buffer.append("[/EXTENDEDFORMAT]\n");
/*  646:1765 */     return buffer.toString();
/*  647:     */   }
/*  648:     */   
/*  649:     */   public void serialize(LittleEndianOutput out)
/*  650:     */   {
/*  651:1770 */     out.writeShort(getFontIndex());
/*  652:1771 */     out.writeShort(getFormatIndex());
/*  653:1772 */     out.writeShort(getCellOptions());
/*  654:1773 */     out.writeShort(getAlignmentOptions());
/*  655:1774 */     out.writeShort(getIndentionOptions());
/*  656:1775 */     out.writeShort(getBorderOptions());
/*  657:1776 */     out.writeShort(getPaletteOptions());
/*  658:1777 */     out.writeInt(getAdtlPaletteOptions());
/*  659:1778 */     out.writeShort(getFillPaletteOptions());
/*  660:     */   }
/*  661:     */   
/*  662:     */   protected int getDataSize()
/*  663:     */   {
/*  664:1783 */     return 20;
/*  665:     */   }
/*  666:     */   
/*  667:     */   public short getSid()
/*  668:     */   {
/*  669:1789 */     return 224;
/*  670:     */   }
/*  671:     */   
/*  672:     */   public void cloneStyleFrom(ExtendedFormatRecord source)
/*  673:     */   {
/*  674:1804 */     this.field_1_font_index = source.field_1_font_index;
/*  675:1805 */     this.field_2_format_index = source.field_2_format_index;
/*  676:1806 */     this.field_3_cell_options = source.field_3_cell_options;
/*  677:1807 */     this.field_4_alignment_options = source.field_4_alignment_options;
/*  678:1808 */     this.field_5_indention_options = source.field_5_indention_options;
/*  679:1809 */     this.field_6_border_options = source.field_6_border_options;
/*  680:1810 */     this.field_7_palette_options = source.field_7_palette_options;
/*  681:1811 */     this.field_8_adtl_palette_options = source.field_8_adtl_palette_options;
/*  682:1812 */     this.field_9_fill_palette_options = source.field_9_fill_palette_options;
/*  683:     */   }
/*  684:     */   
/*  685:     */   public int hashCode()
/*  686:     */   {
/*  687:1817 */     int prime = 31;
/*  688:1818 */     int result = 1;
/*  689:1819 */     result = 31 * result + this.field_1_font_index;
/*  690:1820 */     result = 31 * result + this.field_2_format_index;
/*  691:1821 */     result = 31 * result + this.field_3_cell_options;
/*  692:1822 */     result = 31 * result + this.field_4_alignment_options;
/*  693:1823 */     result = 31 * result + this.field_5_indention_options;
/*  694:1824 */     result = 31 * result + this.field_6_border_options;
/*  695:1825 */     result = 31 * result + this.field_7_palette_options;
/*  696:1826 */     result = 31 * result + this.field_8_adtl_palette_options;
/*  697:1827 */     result = 31 * result + this.field_9_fill_palette_options;
/*  698:1828 */     return result;
/*  699:     */   }
/*  700:     */   
/*  701:     */   public boolean equals(Object obj)
/*  702:     */   {
/*  703:1838 */     if (this == obj) {
/*  704:1839 */       return true;
/*  705:     */     }
/*  706:1840 */     if (obj == null) {
/*  707:1841 */       return false;
/*  708:     */     }
/*  709:1842 */     if ((obj instanceof ExtendedFormatRecord))
/*  710:     */     {
/*  711:1843 */       ExtendedFormatRecord other = (ExtendedFormatRecord)obj;
/*  712:1844 */       if (this.field_1_font_index != other.field_1_font_index) {
/*  713:1845 */         return false;
/*  714:     */       }
/*  715:1846 */       if (this.field_2_format_index != other.field_2_format_index) {
/*  716:1847 */         return false;
/*  717:     */       }
/*  718:1848 */       if (this.field_3_cell_options != other.field_3_cell_options) {
/*  719:1849 */         return false;
/*  720:     */       }
/*  721:1850 */       if (this.field_4_alignment_options != other.field_4_alignment_options) {
/*  722:1851 */         return false;
/*  723:     */       }
/*  724:1852 */       if (this.field_5_indention_options != other.field_5_indention_options) {
/*  725:1853 */         return false;
/*  726:     */       }
/*  727:1854 */       if (this.field_6_border_options != other.field_6_border_options) {
/*  728:1855 */         return false;
/*  729:     */       }
/*  730:1856 */       if (this.field_7_palette_options != other.field_7_palette_options) {
/*  731:1857 */         return false;
/*  732:     */       }
/*  733:1858 */       if (this.field_8_adtl_palette_options != other.field_8_adtl_palette_options) {
/*  734:1859 */         return false;
/*  735:     */       }
/*  736:1860 */       if (this.field_9_fill_palette_options != other.field_9_fill_palette_options) {
/*  737:1861 */         return false;
/*  738:     */       }
/*  739:1862 */       return true;
/*  740:     */     }
/*  741:1864 */     return false;
/*  742:     */   }
/*  743:     */   
/*  744:     */   public int[] stateSummary()
/*  745:     */   {
/*  746:1868 */     return new int[] { this.field_1_font_index, this.field_2_format_index, this.field_3_cell_options, this.field_4_alignment_options, this.field_5_indention_options, this.field_6_border_options, this.field_7_palette_options, this.field_8_adtl_palette_options, this.field_9_fill_palette_options };
/*  747:     */   }
/*  748:     */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.ExtendedFormatRecord
 * JD-Core Version:    0.7.0.1
 */