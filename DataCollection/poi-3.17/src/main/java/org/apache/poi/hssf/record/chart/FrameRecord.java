/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.HexDump;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class FrameRecord
/*  11:    */   extends StandardRecord
/*  12:    */   implements Cloneable
/*  13:    */ {
/*  14:    */   public static final short sid = 4146;
/*  15: 33 */   private static final BitField autoSize = BitFieldFactory.getInstance(1);
/*  16: 34 */   private static final BitField autoPosition = BitFieldFactory.getInstance(2);
/*  17:    */   private short field_1_borderType;
/*  18:    */   public static final short BORDER_TYPE_REGULAR = 0;
/*  19:    */   public static final short BORDER_TYPE_SHADOW = 1;
/*  20:    */   private short field_2_options;
/*  21:    */   
/*  22:    */   public FrameRecord() {}
/*  23:    */   
/*  24:    */   public FrameRecord(RecordInputStream in)
/*  25:    */   {
/*  26: 49 */     this.field_1_borderType = in.readShort();
/*  27: 50 */     this.field_2_options = in.readShort();
/*  28:    */   }
/*  29:    */   
/*  30:    */   public String toString()
/*  31:    */   {
/*  32: 55 */     StringBuffer buffer = new StringBuffer();
/*  33:    */     
/*  34: 57 */     buffer.append("[FRAME]\n");
/*  35: 58 */     buffer.append("    .borderType           = ").append("0x").append(HexDump.toHex(getBorderType())).append(" (").append(getBorderType()).append(" )");
/*  36:    */     
/*  37:    */ 
/*  38: 61 */     buffer.append(System.getProperty("line.separator"));
/*  39: 62 */     buffer.append("    .options              = ").append("0x").append(HexDump.toHex(getOptions())).append(" (").append(getOptions()).append(" )");
/*  40:    */     
/*  41:    */ 
/*  42: 65 */     buffer.append(System.getProperty("line.separator"));
/*  43: 66 */     buffer.append("         .autoSize                 = ").append(isAutoSize()).append('\n');
/*  44: 67 */     buffer.append("         .autoPosition             = ").append(isAutoPosition()).append('\n');
/*  45:    */     
/*  46: 69 */     buffer.append("[/FRAME]\n");
/*  47: 70 */     return buffer.toString();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void serialize(LittleEndianOutput out)
/*  51:    */   {
/*  52: 74 */     out.writeShort(this.field_1_borderType);
/*  53: 75 */     out.writeShort(this.field_2_options);
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected int getDataSize()
/*  57:    */   {
/*  58: 79 */     return 4;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public short getSid()
/*  62:    */   {
/*  63: 84 */     return 4146;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public FrameRecord clone()
/*  67:    */   {
/*  68: 89 */     FrameRecord rec = new FrameRecord();
/*  69:    */     
/*  70: 91 */     rec.field_1_borderType = this.field_1_borderType;
/*  71: 92 */     rec.field_2_options = this.field_2_options;
/*  72: 93 */     return rec;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public short getBorderType()
/*  76:    */   {
/*  77:108 */     return this.field_1_borderType;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setBorderType(short field_1_borderType)
/*  81:    */   {
/*  82:121 */     this.field_1_borderType = field_1_borderType;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public short getOptions()
/*  86:    */   {
/*  87:129 */     return this.field_2_options;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setOptions(short field_2_options)
/*  91:    */   {
/*  92:137 */     this.field_2_options = field_2_options;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setAutoSize(boolean value)
/*  96:    */   {
/*  97:146 */     this.field_2_options = autoSize.setShortBoolean(this.field_2_options, value);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public boolean isAutoSize()
/* 101:    */   {
/* 102:155 */     return autoSize.isSet(this.field_2_options);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setAutoPosition(boolean value)
/* 106:    */   {
/* 107:164 */     this.field_2_options = autoPosition.setShortBoolean(this.field_2_options, value);
/* 108:    */   }
/* 109:    */   
/* 110:    */   public boolean isAutoPosition()
/* 111:    */   {
/* 112:173 */     return autoPosition.isSet(this.field_2_options);
/* 113:    */   }
/* 114:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.FrameRecord
 * JD-Core Version:    0.7.0.1
 */