/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.HexDump;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class DataFormatRecord
/*  11:    */   extends StandardRecord
/*  12:    */   implements Cloneable
/*  13:    */ {
/*  14:    */   public static final short sid = 4102;
/*  15: 33 */   private static final BitField useExcel4Colors = BitFieldFactory.getInstance(1);
/*  16:    */   private short field_1_pointNumber;
/*  17:    */   private short field_2_seriesIndex;
/*  18:    */   private short field_3_seriesNumber;
/*  19:    */   private short field_4_formatFlags;
/*  20:    */   
/*  21:    */   public DataFormatRecord() {}
/*  22:    */   
/*  23:    */   public DataFormatRecord(RecordInputStream in)
/*  24:    */   {
/*  25: 48 */     this.field_1_pointNumber = in.readShort();
/*  26: 49 */     this.field_2_seriesIndex = in.readShort();
/*  27: 50 */     this.field_3_seriesNumber = in.readShort();
/*  28: 51 */     this.field_4_formatFlags = in.readShort();
/*  29:    */   }
/*  30:    */   
/*  31:    */   public String toString()
/*  32:    */   {
/*  33: 56 */     StringBuffer buffer = new StringBuffer();
/*  34:    */     
/*  35: 58 */     buffer.append("[DATAFORMAT]\n");
/*  36: 59 */     buffer.append("    .pointNumber          = ").append("0x").append(HexDump.toHex(getPointNumber())).append(" (").append(getPointNumber()).append(" )");
/*  37:    */     
/*  38:    */ 
/*  39: 62 */     buffer.append(System.getProperty("line.separator"));
/*  40: 63 */     buffer.append("    .seriesIndex          = ").append("0x").append(HexDump.toHex(getSeriesIndex())).append(" (").append(getSeriesIndex()).append(" )");
/*  41:    */     
/*  42:    */ 
/*  43: 66 */     buffer.append(System.getProperty("line.separator"));
/*  44: 67 */     buffer.append("    .seriesNumber         = ").append("0x").append(HexDump.toHex(getSeriesNumber())).append(" (").append(getSeriesNumber()).append(" )");
/*  45:    */     
/*  46:    */ 
/*  47: 70 */     buffer.append(System.getProperty("line.separator"));
/*  48: 71 */     buffer.append("    .formatFlags          = ").append("0x").append(HexDump.toHex(getFormatFlags())).append(" (").append(getFormatFlags()).append(" )");
/*  49:    */     
/*  50:    */ 
/*  51: 74 */     buffer.append(System.getProperty("line.separator"));
/*  52: 75 */     buffer.append("         .useExcel4Colors          = ").append(isUseExcel4Colors()).append('\n');
/*  53:    */     
/*  54: 77 */     buffer.append("[/DATAFORMAT]\n");
/*  55: 78 */     return buffer.toString();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void serialize(LittleEndianOutput out)
/*  59:    */   {
/*  60: 82 */     out.writeShort(this.field_1_pointNumber);
/*  61: 83 */     out.writeShort(this.field_2_seriesIndex);
/*  62: 84 */     out.writeShort(this.field_3_seriesNumber);
/*  63: 85 */     out.writeShort(this.field_4_formatFlags);
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected int getDataSize()
/*  67:    */   {
/*  68: 89 */     return 8;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public short getSid()
/*  72:    */   {
/*  73: 94 */     return 4102;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public DataFormatRecord clone()
/*  77:    */   {
/*  78: 99 */     DataFormatRecord rec = new DataFormatRecord();
/*  79:    */     
/*  80:101 */     rec.field_1_pointNumber = this.field_1_pointNumber;
/*  81:102 */     rec.field_2_seriesIndex = this.field_2_seriesIndex;
/*  82:103 */     rec.field_3_seriesNumber = this.field_3_seriesNumber;
/*  83:104 */     rec.field_4_formatFlags = this.field_4_formatFlags;
/*  84:105 */     return rec;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public short getPointNumber()
/*  88:    */   {
/*  89:116 */     return this.field_1_pointNumber;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setPointNumber(short field_1_pointNumber)
/*  93:    */   {
/*  94:124 */     this.field_1_pointNumber = field_1_pointNumber;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public short getSeriesIndex()
/*  98:    */   {
/*  99:132 */     return this.field_2_seriesIndex;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setSeriesIndex(short field_2_seriesIndex)
/* 103:    */   {
/* 104:140 */     this.field_2_seriesIndex = field_2_seriesIndex;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public short getSeriesNumber()
/* 108:    */   {
/* 109:148 */     return this.field_3_seriesNumber;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void setSeriesNumber(short field_3_seriesNumber)
/* 113:    */   {
/* 114:156 */     this.field_3_seriesNumber = field_3_seriesNumber;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public short getFormatFlags()
/* 118:    */   {
/* 119:164 */     return this.field_4_formatFlags;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setFormatFlags(short field_4_formatFlags)
/* 123:    */   {
/* 124:172 */     this.field_4_formatFlags = field_4_formatFlags;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public void setUseExcel4Colors(boolean value)
/* 128:    */   {
/* 129:181 */     this.field_4_formatFlags = useExcel4Colors.setShortBoolean(this.field_4_formatFlags, value);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public boolean isUseExcel4Colors()
/* 133:    */   {
/* 134:190 */     return useExcel4Colors.isSet(this.field_4_formatFlags);
/* 135:    */   }
/* 136:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.DataFormatRecord
 * JD-Core Version:    0.7.0.1
 */