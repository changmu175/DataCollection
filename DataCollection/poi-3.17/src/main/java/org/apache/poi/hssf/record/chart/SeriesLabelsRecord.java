/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.HexDump;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class SeriesLabelsRecord
/*  11:    */   extends StandardRecord
/*  12:    */ {
/*  13:    */   public static final short sid = 4108;
/*  14: 35 */   private static final BitField showActual = BitFieldFactory.getInstance(1);
/*  15: 36 */   private static final BitField showPercent = BitFieldFactory.getInstance(2);
/*  16: 37 */   private static final BitField labelAsPercentage = BitFieldFactory.getInstance(4);
/*  17: 38 */   private static final BitField smoothedLine = BitFieldFactory.getInstance(8);
/*  18: 39 */   private static final BitField showLabel = BitFieldFactory.getInstance(16);
/*  19: 40 */   private static final BitField showBubbleSizes = BitFieldFactory.getInstance(32);
/*  20:    */   private short field_1_formatFlags;
/*  21:    */   
/*  22:    */   public SeriesLabelsRecord() {}
/*  23:    */   
/*  24:    */   public SeriesLabelsRecord(RecordInputStream in)
/*  25:    */   {
/*  26: 51 */     this.field_1_formatFlags = in.readShort();
/*  27:    */   }
/*  28:    */   
/*  29:    */   public String toString()
/*  30:    */   {
/*  31: 56 */     StringBuffer buffer = new StringBuffer();
/*  32:    */     
/*  33: 58 */     buffer.append("[ATTACHEDLABEL]\n");
/*  34: 59 */     buffer.append("    .formatFlags          = ").append("0x").append(HexDump.toHex(getFormatFlags())).append(" (").append(getFormatFlags()).append(" )");
/*  35:    */     
/*  36:    */ 
/*  37: 62 */     buffer.append(System.getProperty("line.separator"));
/*  38: 63 */     buffer.append("         .showActual               = ").append(isShowActual()).append('\n');
/*  39: 64 */     buffer.append("         .showPercent              = ").append(isShowPercent()).append('\n');
/*  40: 65 */     buffer.append("         .labelAsPercentage        = ").append(isLabelAsPercentage()).append('\n');
/*  41: 66 */     buffer.append("         .smoothedLine             = ").append(isSmoothedLine()).append('\n');
/*  42: 67 */     buffer.append("         .showLabel                = ").append(isShowLabel()).append('\n');
/*  43: 68 */     buffer.append("         .showBubbleSizes          = ").append(isShowBubbleSizes()).append('\n');
/*  44:    */     
/*  45: 70 */     buffer.append("[/ATTACHEDLABEL]\n");
/*  46: 71 */     return buffer.toString();
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void serialize(LittleEndianOutput out)
/*  50:    */   {
/*  51: 75 */     out.writeShort(this.field_1_formatFlags);
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected int getDataSize()
/*  55:    */   {
/*  56: 79 */     return 2;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public short getSid()
/*  60:    */   {
/*  61: 84 */     return 4108;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public Object clone()
/*  65:    */   {
/*  66: 88 */     SeriesLabelsRecord rec = new SeriesLabelsRecord();
/*  67:    */     
/*  68: 90 */     rec.field_1_formatFlags = this.field_1_formatFlags;
/*  69: 91 */     return rec;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public short getFormatFlags()
/*  73:    */   {
/*  74:102 */     return this.field_1_formatFlags;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setFormatFlags(short field_1_formatFlags)
/*  78:    */   {
/*  79:110 */     this.field_1_formatFlags = field_1_formatFlags;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void setShowActual(boolean value)
/*  83:    */   {
/*  84:119 */     this.field_1_formatFlags = showActual.setShortBoolean(this.field_1_formatFlags, value);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public boolean isShowActual()
/*  88:    */   {
/*  89:128 */     return showActual.isSet(this.field_1_formatFlags);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setShowPercent(boolean value)
/*  93:    */   {
/*  94:137 */     this.field_1_formatFlags = showPercent.setShortBoolean(this.field_1_formatFlags, value);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public boolean isShowPercent()
/*  98:    */   {
/*  99:146 */     return showPercent.isSet(this.field_1_formatFlags);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setLabelAsPercentage(boolean value)
/* 103:    */   {
/* 104:155 */     this.field_1_formatFlags = labelAsPercentage.setShortBoolean(this.field_1_formatFlags, value);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public boolean isLabelAsPercentage()
/* 108:    */   {
/* 109:164 */     return labelAsPercentage.isSet(this.field_1_formatFlags);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void setSmoothedLine(boolean value)
/* 113:    */   {
/* 114:173 */     this.field_1_formatFlags = smoothedLine.setShortBoolean(this.field_1_formatFlags, value);
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean isSmoothedLine()
/* 118:    */   {
/* 119:182 */     return smoothedLine.isSet(this.field_1_formatFlags);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setShowLabel(boolean value)
/* 123:    */   {
/* 124:191 */     this.field_1_formatFlags = showLabel.setShortBoolean(this.field_1_formatFlags, value);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public boolean isShowLabel()
/* 128:    */   {
/* 129:200 */     return showLabel.isSet(this.field_1_formatFlags);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void setShowBubbleSizes(boolean value)
/* 133:    */   {
/* 134:209 */     this.field_1_formatFlags = showBubbleSizes.setShortBoolean(this.field_1_formatFlags, value);
/* 135:    */   }
/* 136:    */   
/* 137:    */   public boolean isShowBubbleSizes()
/* 138:    */   {
/* 139:218 */     return showBubbleSizes.isSet(this.field_1_formatFlags);
/* 140:    */   }
/* 141:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.SeriesLabelsRecord
 * JD-Core Version:    0.7.0.1
 */