/*   1:    */ package org.apache.poi.hssf.record.cf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.BitField;
/*   4:    */ import org.apache.poi.util.BitFieldFactory;
/*   5:    */ import org.apache.poi.util.LittleEndianInput;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ 
/*   8:    */ public final class PatternFormatting
/*   9:    */   implements Cloneable
/*  10:    */ {
/*  11:    */   public static final short NO_FILL = 0;
/*  12:    */   public static final short SOLID_FOREGROUND = 1;
/*  13:    */   public static final short FINE_DOTS = 2;
/*  14:    */   public static final short ALT_BARS = 3;
/*  15:    */   public static final short SPARSE_DOTS = 4;
/*  16:    */   public static final short THICK_HORZ_BANDS = 5;
/*  17:    */   public static final short THICK_VERT_BANDS = 6;
/*  18:    */   public static final short THICK_BACKWARD_DIAG = 7;
/*  19:    */   public static final short THICK_FORWARD_DIAG = 8;
/*  20:    */   public static final short BIG_SPOTS = 9;
/*  21:    */   public static final short BRICKS = 10;
/*  22:    */   public static final short THIN_HORZ_BANDS = 11;
/*  23:    */   public static final short THIN_VERT_BANDS = 12;
/*  24:    */   public static final short THIN_BACKWARD_DIAG = 13;
/*  25:    */   public static final short THIN_FORWARD_DIAG = 14;
/*  26:    */   public static final short SQUARES = 15;
/*  27:    */   public static final short DIAMONDS = 16;
/*  28:    */   public static final short LESS_DOTS = 17;
/*  29:    */   public static final short LEAST_DOTS = 18;
/*  30:    */   private int field_15_pattern_style;
/*  31: 72 */   private static final BitField fillPatternStyle = BitFieldFactory.getInstance(64512);
/*  32:    */   private int field_16_pattern_color_indexes;
/*  33: 75 */   private static final BitField patternColorIndex = BitFieldFactory.getInstance(127);
/*  34: 76 */   private static final BitField patternBackgroundColorIndex = BitFieldFactory.getInstance(16256);
/*  35:    */   
/*  36:    */   public PatternFormatting()
/*  37:    */   {
/*  38: 80 */     this.field_15_pattern_style = 0;
/*  39: 81 */     this.field_16_pattern_color_indexes = 0;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public PatternFormatting(LittleEndianInput in)
/*  43:    */   {
/*  44: 86 */     this.field_15_pattern_style = in.readUShort();
/*  45: 87 */     this.field_16_pattern_color_indexes = in.readUShort();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public int getDataLength()
/*  49:    */   {
/*  50: 91 */     return 4;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setFillPattern(int fp)
/*  54:    */   {
/*  55:118 */     this.field_15_pattern_style = fillPatternStyle.setValue(this.field_15_pattern_style, fp);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public int getFillPattern()
/*  59:    */   {
/*  60:125 */     return fillPatternStyle.getValue(this.field_15_pattern_style);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setFillBackgroundColor(int bg)
/*  64:    */   {
/*  65:132 */     this.field_16_pattern_color_indexes = patternBackgroundColorIndex.setValue(this.field_16_pattern_color_indexes, bg);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public int getFillBackgroundColor()
/*  69:    */   {
/*  70:140 */     return patternBackgroundColorIndex.getValue(this.field_16_pattern_color_indexes);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setFillForegroundColor(int fg)
/*  74:    */   {
/*  75:147 */     this.field_16_pattern_color_indexes = patternColorIndex.setValue(this.field_16_pattern_color_indexes, fg);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public int getFillForegroundColor()
/*  79:    */   {
/*  80:155 */     return patternColorIndex.getValue(this.field_16_pattern_color_indexes);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public String toString()
/*  84:    */   {
/*  85:159 */     StringBuffer buffer = new StringBuffer();
/*  86:160 */     buffer.append("    [Pattern Formatting]\n");
/*  87:161 */     buffer.append("          .fillpattern= ").append(Integer.toHexString(getFillPattern())).append("\n");
/*  88:162 */     buffer.append("          .fgcoloridx= ").append(Integer.toHexString(getFillForegroundColor())).append("\n");
/*  89:163 */     buffer.append("          .bgcoloridx= ").append(Integer.toHexString(getFillBackgroundColor())).append("\n");
/*  90:164 */     buffer.append("    [/Pattern Formatting]\n");
/*  91:165 */     return buffer.toString();
/*  92:    */   }
/*  93:    */   
/*  94:    */   public Object clone()
/*  95:    */   {
/*  96:169 */     PatternFormatting rec = new PatternFormatting();
/*  97:170 */     rec.field_15_pattern_style = this.field_15_pattern_style;
/*  98:171 */     rec.field_16_pattern_color_indexes = this.field_16_pattern_color_indexes;
/*  99:172 */     return rec;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void serialize(LittleEndianOutput out)
/* 103:    */   {
/* 104:176 */     out.writeShort(this.field_15_pattern_style);
/* 105:177 */     out.writeShort(this.field_16_pattern_color_indexes);
/* 106:    */   }
/* 107:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.cf.PatternFormatting
 * JD-Core Version:    0.7.0.1
 */