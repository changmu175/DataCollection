/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ 
/*   8:    */ public final class SeriesRecord
/*   9:    */   extends StandardRecord
/*  10:    */ {
/*  11:    */   public static final short sid = 4099;
/*  12:    */   private short field_1_categoryDataType;
/*  13:    */   public static final short CATEGORY_DATA_TYPE_DATES = 0;
/*  14:    */   public static final short CATEGORY_DATA_TYPE_NUMERIC = 1;
/*  15:    */   public static final short CATEGORY_DATA_TYPE_SEQUENCE = 2;
/*  16:    */   public static final short CATEGORY_DATA_TYPE_TEXT = 3;
/*  17:    */   private short field_2_valuesDataType;
/*  18:    */   public static final short VALUES_DATA_TYPE_DATES = 0;
/*  19:    */   public static final short VALUES_DATA_TYPE_NUMERIC = 1;
/*  20:    */   public static final short VALUES_DATA_TYPE_SEQUENCE = 2;
/*  21:    */   public static final short VALUES_DATA_TYPE_TEXT = 3;
/*  22:    */   private short field_3_numCategories;
/*  23:    */   private short field_4_numValues;
/*  24:    */   private short field_5_bubbleSeriesType;
/*  25:    */   public static final short BUBBLE_SERIES_TYPE_DATES = 0;
/*  26:    */   public static final short BUBBLE_SERIES_TYPE_NUMERIC = 1;
/*  27:    */   public static final short BUBBLE_SERIES_TYPE_SEQUENCE = 2;
/*  28:    */   public static final short BUBBLE_SERIES_TYPE_TEXT = 3;
/*  29:    */   private short field_6_numBubbleValues;
/*  30:    */   
/*  31:    */   public SeriesRecord() {}
/*  32:    */   
/*  33:    */   public SeriesRecord(RecordInputStream in)
/*  34:    */   {
/*  35: 57 */     this.field_1_categoryDataType = in.readShort();
/*  36: 58 */     this.field_2_valuesDataType = in.readShort();
/*  37: 59 */     this.field_3_numCategories = in.readShort();
/*  38: 60 */     this.field_4_numValues = in.readShort();
/*  39: 61 */     this.field_5_bubbleSeriesType = in.readShort();
/*  40: 62 */     this.field_6_numBubbleValues = in.readShort();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public String toString()
/*  44:    */   {
/*  45: 68 */     StringBuffer buffer = new StringBuffer();
/*  46:    */     
/*  47: 70 */     buffer.append("[SERIES]\n");
/*  48: 71 */     buffer.append("    .categoryDataType     = ").append("0x").append(HexDump.toHex(getCategoryDataType())).append(" (").append(getCategoryDataType()).append(" )");
/*  49:    */     
/*  50:    */ 
/*  51: 74 */     buffer.append(System.getProperty("line.separator"));
/*  52: 75 */     buffer.append("    .valuesDataType       = ").append("0x").append(HexDump.toHex(getValuesDataType())).append(" (").append(getValuesDataType()).append(" )");
/*  53:    */     
/*  54:    */ 
/*  55: 78 */     buffer.append(System.getProperty("line.separator"));
/*  56: 79 */     buffer.append("    .numCategories        = ").append("0x").append(HexDump.toHex(getNumCategories())).append(" (").append(getNumCategories()).append(" )");
/*  57:    */     
/*  58:    */ 
/*  59: 82 */     buffer.append(System.getProperty("line.separator"));
/*  60: 83 */     buffer.append("    .numValues            = ").append("0x").append(HexDump.toHex(getNumValues())).append(" (").append(getNumValues()).append(" )");
/*  61:    */     
/*  62:    */ 
/*  63: 86 */     buffer.append(System.getProperty("line.separator"));
/*  64: 87 */     buffer.append("    .bubbleSeriesType     = ").append("0x").append(HexDump.toHex(getBubbleSeriesType())).append(" (").append(getBubbleSeriesType()).append(" )");
/*  65:    */     
/*  66:    */ 
/*  67: 90 */     buffer.append(System.getProperty("line.separator"));
/*  68: 91 */     buffer.append("    .numBubbleValues      = ").append("0x").append(HexDump.toHex(getNumBubbleValues())).append(" (").append(getNumBubbleValues()).append(" )");
/*  69:    */     
/*  70:    */ 
/*  71: 94 */     buffer.append(System.getProperty("line.separator"));
/*  72:    */     
/*  73: 96 */     buffer.append("[/SERIES]\n");
/*  74: 97 */     return buffer.toString();
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void serialize(LittleEndianOutput out)
/*  78:    */   {
/*  79:101 */     out.writeShort(this.field_1_categoryDataType);
/*  80:102 */     out.writeShort(this.field_2_valuesDataType);
/*  81:103 */     out.writeShort(this.field_3_numCategories);
/*  82:104 */     out.writeShort(this.field_4_numValues);
/*  83:105 */     out.writeShort(this.field_5_bubbleSeriesType);
/*  84:106 */     out.writeShort(this.field_6_numBubbleValues);
/*  85:    */   }
/*  86:    */   
/*  87:    */   protected int getDataSize()
/*  88:    */   {
/*  89:110 */     return 12;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public short getSid()
/*  93:    */   {
/*  94:115 */     return 4099;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public Object clone()
/*  98:    */   {
/*  99:119 */     SeriesRecord rec = new SeriesRecord();
/* 100:    */     
/* 101:121 */     rec.field_1_categoryDataType = this.field_1_categoryDataType;
/* 102:122 */     rec.field_2_valuesDataType = this.field_2_valuesDataType;
/* 103:123 */     rec.field_3_numCategories = this.field_3_numCategories;
/* 104:124 */     rec.field_4_numValues = this.field_4_numValues;
/* 105:125 */     rec.field_5_bubbleSeriesType = this.field_5_bubbleSeriesType;
/* 106:126 */     rec.field_6_numBubbleValues = this.field_6_numBubbleValues;
/* 107:127 */     return rec;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public short getCategoryDataType()
/* 111:    */   {
/* 112:144 */     return this.field_1_categoryDataType;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void setCategoryDataType(short field_1_categoryDataType)
/* 116:    */   {
/* 117:159 */     this.field_1_categoryDataType = field_1_categoryDataType;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public short getValuesDataType()
/* 121:    */   {
/* 122:173 */     return this.field_2_valuesDataType;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void setValuesDataType(short field_2_valuesDataType)
/* 126:    */   {
/* 127:188 */     this.field_2_valuesDataType = field_2_valuesDataType;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public short getNumCategories()
/* 131:    */   {
/* 132:196 */     return this.field_3_numCategories;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void setNumCategories(short field_3_numCategories)
/* 136:    */   {
/* 137:204 */     this.field_3_numCategories = field_3_numCategories;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public short getNumValues()
/* 141:    */   {
/* 142:212 */     return this.field_4_numValues;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void setNumValues(short field_4_numValues)
/* 146:    */   {
/* 147:220 */     this.field_4_numValues = field_4_numValues;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public short getBubbleSeriesType()
/* 151:    */   {
/* 152:234 */     return this.field_5_bubbleSeriesType;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void setBubbleSeriesType(short field_5_bubbleSeriesType)
/* 156:    */   {
/* 157:249 */     this.field_5_bubbleSeriesType = field_5_bubbleSeriesType;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public short getNumBubbleValues()
/* 161:    */   {
/* 162:257 */     return this.field_6_numBubbleValues;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public void setNumBubbleValues(short field_6_numBubbleValues)
/* 166:    */   {
/* 167:265 */     this.field_6_numBubbleValues = field_6_numBubbleValues;
/* 168:    */   }
/* 169:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.SeriesRecord
 * JD-Core Version:    0.7.0.1
 */