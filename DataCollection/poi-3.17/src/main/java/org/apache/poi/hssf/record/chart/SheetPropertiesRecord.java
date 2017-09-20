/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.HexDump;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class SheetPropertiesRecord
/*  11:    */   extends StandardRecord
/*  12:    */ {
/*  13:    */   public static final short sid = 4164;
/*  14: 36 */   private static final BitField chartTypeManuallyFormatted = BitFieldFactory.getInstance(1);
/*  15: 37 */   private static final BitField plotVisibleOnly = BitFieldFactory.getInstance(2);
/*  16: 38 */   private static final BitField doNotSizeWithWindow = BitFieldFactory.getInstance(4);
/*  17: 39 */   private static final BitField defaultPlotDimensions = BitFieldFactory.getInstance(8);
/*  18: 40 */   private static final BitField autoPlotArea = BitFieldFactory.getInstance(16);
/*  19:    */   private int field_1_flags;
/*  20:    */   private int field_2_empty;
/*  21:    */   public static final byte EMPTY_NOT_PLOTTED = 0;
/*  22:    */   public static final byte EMPTY_ZERO = 1;
/*  23:    */   public static final byte EMPTY_INTERPOLATED = 2;
/*  24:    */   
/*  25:    */   public SheetPropertiesRecord() {}
/*  26:    */   
/*  27:    */   public SheetPropertiesRecord(RecordInputStream in)
/*  28:    */   {
/*  29: 54 */     this.field_1_flags = in.readUShort();
/*  30: 55 */     this.field_2_empty = in.readUShort();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String toString()
/*  34:    */   {
/*  35: 59 */     StringBuffer buffer = new StringBuffer();
/*  36:    */     
/*  37: 61 */     buffer.append("[SHTPROPS]\n");
/*  38: 62 */     buffer.append("    .flags                = ").append(HexDump.shortToHex(this.field_1_flags)).append('\n');
/*  39: 63 */     buffer.append("         .chartTypeManuallyFormatted= ").append(isChartTypeManuallyFormatted()).append('\n');
/*  40: 64 */     buffer.append("         .plotVisibleOnly           = ").append(isPlotVisibleOnly()).append('\n');
/*  41: 65 */     buffer.append("         .doNotSizeWithWindow       = ").append(isDoNotSizeWithWindow()).append('\n');
/*  42: 66 */     buffer.append("         .defaultPlotDimensions     = ").append(isDefaultPlotDimensions()).append('\n');
/*  43: 67 */     buffer.append("         .autoPlotArea              = ").append(isAutoPlotArea()).append('\n');
/*  44: 68 */     buffer.append("    .empty                = ").append(HexDump.shortToHex(this.field_2_empty)).append('\n');
/*  45:    */     
/*  46: 70 */     buffer.append("[/SHTPROPS]\n");
/*  47: 71 */     return buffer.toString();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void serialize(LittleEndianOutput out)
/*  51:    */   {
/*  52: 75 */     out.writeShort(this.field_1_flags);
/*  53: 76 */     out.writeShort(this.field_2_empty);
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected int getDataSize()
/*  57:    */   {
/*  58: 80 */     return 4;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public short getSid()
/*  62:    */   {
/*  63: 84 */     return 4164;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public Object clone()
/*  67:    */   {
/*  68: 88 */     SheetPropertiesRecord rec = new SheetPropertiesRecord();
/*  69:    */     
/*  70: 90 */     rec.field_1_flags = this.field_1_flags;
/*  71: 91 */     rec.field_2_empty = this.field_2_empty;
/*  72: 92 */     return rec;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public int getFlags()
/*  76:    */   {
/*  77: 99 */     return this.field_1_flags;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public int getEmpty()
/*  81:    */   {
/*  82:111 */     return this.field_2_empty;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setEmpty(byte empty)
/*  86:    */   {
/*  87:124 */     this.field_2_empty = empty;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setChartTypeManuallyFormatted(boolean value)
/*  91:    */   {
/*  92:132 */     this.field_1_flags = chartTypeManuallyFormatted.setBoolean(this.field_1_flags, value);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public boolean isChartTypeManuallyFormatted()
/*  96:    */   {
/*  97:140 */     return chartTypeManuallyFormatted.isSet(this.field_1_flags);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setPlotVisibleOnly(boolean value)
/* 101:    */   {
/* 102:148 */     this.field_1_flags = plotVisibleOnly.setBoolean(this.field_1_flags, value);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public boolean isPlotVisibleOnly()
/* 106:    */   {
/* 107:156 */     return plotVisibleOnly.isSet(this.field_1_flags);
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setDoNotSizeWithWindow(boolean value)
/* 111:    */   {
/* 112:164 */     this.field_1_flags = doNotSizeWithWindow.setBoolean(this.field_1_flags, value);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public boolean isDoNotSizeWithWindow()
/* 116:    */   {
/* 117:172 */     return doNotSizeWithWindow.isSet(this.field_1_flags);
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void setDefaultPlotDimensions(boolean value)
/* 121:    */   {
/* 122:180 */     this.field_1_flags = defaultPlotDimensions.setBoolean(this.field_1_flags, value);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public boolean isDefaultPlotDimensions()
/* 126:    */   {
/* 127:188 */     return defaultPlotDimensions.isSet(this.field_1_flags);
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void setAutoPlotArea(boolean value)
/* 131:    */   {
/* 132:196 */     this.field_1_flags = autoPlotArea.setBoolean(this.field_1_flags, value);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public boolean isAutoPlotArea()
/* 136:    */   {
/* 137:204 */     return autoPlotArea.isSet(this.field_1_flags);
/* 138:    */   }
/* 139:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.SheetPropertiesRecord
 * JD-Core Version:    0.7.0.1
 */