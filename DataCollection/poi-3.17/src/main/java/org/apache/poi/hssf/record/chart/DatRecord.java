/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.HexDump;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class DatRecord
/*  11:    */   extends StandardRecord
/*  12:    */   implements Cloneable
/*  13:    */ {
/*  14:    */   public static final short sid = 4195;
/*  15: 33 */   private static final BitField horizontalBorder = BitFieldFactory.getInstance(1);
/*  16: 34 */   private static final BitField verticalBorder = BitFieldFactory.getInstance(2);
/*  17: 35 */   private static final BitField border = BitFieldFactory.getInstance(4);
/*  18: 36 */   private static final BitField showSeriesKey = BitFieldFactory.getInstance(8);
/*  19:    */   private short field_1_options;
/*  20:    */   
/*  21:    */   public DatRecord() {}
/*  22:    */   
/*  23:    */   public DatRecord(RecordInputStream in)
/*  24:    */   {
/*  25: 48 */     this.field_1_options = in.readShort();
/*  26:    */   }
/*  27:    */   
/*  28:    */   public String toString()
/*  29:    */   {
/*  30: 53 */     StringBuffer buffer = new StringBuffer();
/*  31:    */     
/*  32: 55 */     buffer.append("[DAT]\n");
/*  33: 56 */     buffer.append("    .options              = ").append("0x").append(HexDump.toHex(getOptions())).append(" (").append(getOptions()).append(" )");
/*  34:    */     
/*  35:    */ 
/*  36: 59 */     buffer.append(System.getProperty("line.separator"));
/*  37: 60 */     buffer.append("         .horizontalBorder         = ").append(isHorizontalBorder()).append('\n');
/*  38: 61 */     buffer.append("         .verticalBorder           = ").append(isVerticalBorder()).append('\n');
/*  39: 62 */     buffer.append("         .border                   = ").append(isBorder()).append('\n');
/*  40: 63 */     buffer.append("         .showSeriesKey            = ").append(isShowSeriesKey()).append('\n');
/*  41:    */     
/*  42: 65 */     buffer.append("[/DAT]\n");
/*  43: 66 */     return buffer.toString();
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void serialize(LittleEndianOutput out)
/*  47:    */   {
/*  48: 70 */     out.writeShort(this.field_1_options);
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected int getDataSize()
/*  52:    */   {
/*  53: 74 */     return 2;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public short getSid()
/*  57:    */   {
/*  58: 79 */     return 4195;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public DatRecord clone()
/*  62:    */   {
/*  63: 84 */     DatRecord rec = new DatRecord();
/*  64: 85 */     rec.field_1_options = this.field_1_options;
/*  65: 86 */     return rec;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public short getOptions()
/*  69:    */   {
/*  70: 97 */     return this.field_1_options;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setOptions(short field_1_options)
/*  74:    */   {
/*  75:105 */     this.field_1_options = field_1_options;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setHorizontalBorder(boolean value)
/*  79:    */   {
/*  80:114 */     this.field_1_options = horizontalBorder.setShortBoolean(this.field_1_options, value);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public boolean isHorizontalBorder()
/*  84:    */   {
/*  85:123 */     return horizontalBorder.isSet(this.field_1_options);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void setVerticalBorder(boolean value)
/*  89:    */   {
/*  90:132 */     this.field_1_options = verticalBorder.setShortBoolean(this.field_1_options, value);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public boolean isVerticalBorder()
/*  94:    */   {
/*  95:141 */     return verticalBorder.isSet(this.field_1_options);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setBorder(boolean value)
/*  99:    */   {
/* 100:150 */     this.field_1_options = border.setShortBoolean(this.field_1_options, value);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public boolean isBorder()
/* 104:    */   {
/* 105:159 */     return border.isSet(this.field_1_options);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void setShowSeriesKey(boolean value)
/* 109:    */   {
/* 110:168 */     this.field_1_options = showSeriesKey.setShortBoolean(this.field_1_options, value);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public boolean isShowSeriesKey()
/* 114:    */   {
/* 115:177 */     return showSeriesKey.isSet(this.field_1_options);
/* 116:    */   }
/* 117:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.DatRecord
 * JD-Core Version:    0.7.0.1
 */