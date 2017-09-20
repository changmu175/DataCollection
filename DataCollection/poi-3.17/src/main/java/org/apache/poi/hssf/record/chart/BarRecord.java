/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.HexDump;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class BarRecord
/*  11:    */   extends StandardRecord
/*  12:    */   implements Cloneable
/*  13:    */ {
/*  14:    */   public static final short sid = 4119;
/*  15: 35 */   private static final BitField horizontal = BitFieldFactory.getInstance(1);
/*  16: 36 */   private static final BitField stacked = BitFieldFactory.getInstance(2);
/*  17: 37 */   private static final BitField displayAsPercentage = BitFieldFactory.getInstance(4);
/*  18: 38 */   private static final BitField shadow = BitFieldFactory.getInstance(8);
/*  19:    */   private short field_1_barSpace;
/*  20:    */   private short field_2_categorySpace;
/*  21:    */   private short field_3_formatFlags;
/*  22:    */   
/*  23:    */   public BarRecord() {}
/*  24:    */   
/*  25:    */   public BarRecord(RecordInputStream in)
/*  26:    */   {
/*  27: 52 */     this.field_1_barSpace = in.readShort();
/*  28: 53 */     this.field_2_categorySpace = in.readShort();
/*  29: 54 */     this.field_3_formatFlags = in.readShort();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String toString()
/*  33:    */   {
/*  34: 59 */     StringBuffer buffer = new StringBuffer();
/*  35:    */     
/*  36: 61 */     buffer.append("[BAR]\n");
/*  37: 62 */     buffer.append("    .barSpace             = ").append("0x").append(HexDump.toHex(getBarSpace())).append(" (").append(getBarSpace()).append(" )");
/*  38:    */     
/*  39:    */ 
/*  40: 65 */     buffer.append(System.getProperty("line.separator"));
/*  41: 66 */     buffer.append("    .categorySpace        = ").append("0x").append(HexDump.toHex(getCategorySpace())).append(" (").append(getCategorySpace()).append(" )");
/*  42:    */     
/*  43:    */ 
/*  44: 69 */     buffer.append(System.getProperty("line.separator"));
/*  45: 70 */     buffer.append("    .formatFlags          = ").append("0x").append(HexDump.toHex(getFormatFlags())).append(" (").append(getFormatFlags()).append(" )");
/*  46:    */     
/*  47:    */ 
/*  48: 73 */     buffer.append(System.getProperty("line.separator"));
/*  49: 74 */     buffer.append("         .horizontal               = ").append(isHorizontal()).append('\n');
/*  50: 75 */     buffer.append("         .stacked                  = ").append(isStacked()).append('\n');
/*  51: 76 */     buffer.append("         .displayAsPercentage      = ").append(isDisplayAsPercentage()).append('\n');
/*  52: 77 */     buffer.append("         .shadow                   = ").append(isShadow()).append('\n');
/*  53:    */     
/*  54: 79 */     buffer.append("[/BAR]\n");
/*  55: 80 */     return buffer.toString();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void serialize(LittleEndianOutput out)
/*  59:    */   {
/*  60: 84 */     out.writeShort(this.field_1_barSpace);
/*  61: 85 */     out.writeShort(this.field_2_categorySpace);
/*  62: 86 */     out.writeShort(this.field_3_formatFlags);
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected int getDataSize()
/*  66:    */   {
/*  67: 90 */     return 6;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public short getSid()
/*  71:    */   {
/*  72: 95 */     return 4119;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public BarRecord clone()
/*  76:    */   {
/*  77:100 */     BarRecord rec = new BarRecord();
/*  78:    */     
/*  79:102 */     rec.field_1_barSpace = this.field_1_barSpace;
/*  80:103 */     rec.field_2_categorySpace = this.field_2_categorySpace;
/*  81:104 */     rec.field_3_formatFlags = this.field_3_formatFlags;
/*  82:105 */     return rec;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public short getBarSpace()
/*  86:    */   {
/*  87:116 */     return this.field_1_barSpace;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setBarSpace(short field_1_barSpace)
/*  91:    */   {
/*  92:124 */     this.field_1_barSpace = field_1_barSpace;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public short getCategorySpace()
/*  96:    */   {
/*  97:132 */     return this.field_2_categorySpace;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setCategorySpace(short field_2_categorySpace)
/* 101:    */   {
/* 102:140 */     this.field_2_categorySpace = field_2_categorySpace;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public short getFormatFlags()
/* 106:    */   {
/* 107:148 */     return this.field_3_formatFlags;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setFormatFlags(short field_3_formatFlags)
/* 111:    */   {
/* 112:156 */     this.field_3_formatFlags = field_3_formatFlags;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void setHorizontal(boolean value)
/* 116:    */   {
/* 117:165 */     this.field_3_formatFlags = horizontal.setShortBoolean(this.field_3_formatFlags, value);
/* 118:    */   }
/* 119:    */   
/* 120:    */   public boolean isHorizontal()
/* 121:    */   {
/* 122:174 */     return horizontal.isSet(this.field_3_formatFlags);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void setStacked(boolean value)
/* 126:    */   {
/* 127:183 */     this.field_3_formatFlags = stacked.setShortBoolean(this.field_3_formatFlags, value);
/* 128:    */   }
/* 129:    */   
/* 130:    */   public boolean isStacked()
/* 131:    */   {
/* 132:192 */     return stacked.isSet(this.field_3_formatFlags);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void setDisplayAsPercentage(boolean value)
/* 136:    */   {
/* 137:201 */     this.field_3_formatFlags = displayAsPercentage.setShortBoolean(this.field_3_formatFlags, value);
/* 138:    */   }
/* 139:    */   
/* 140:    */   public boolean isDisplayAsPercentage()
/* 141:    */   {
/* 142:210 */     return displayAsPercentage.isSet(this.field_3_formatFlags);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void setShadow(boolean value)
/* 146:    */   {
/* 147:219 */     this.field_3_formatFlags = shadow.setShortBoolean(this.field_3_formatFlags, value);
/* 148:    */   }
/* 149:    */   
/* 150:    */   public boolean isShadow()
/* 151:    */   {
/* 152:228 */     return shadow.isSet(this.field_3_formatFlags);
/* 153:    */   }
/* 154:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.BarRecord
 * JD-Core Version:    0.7.0.1
 */