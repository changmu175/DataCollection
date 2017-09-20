/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.HexDump;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class AreaRecord
/*  11:    */   extends StandardRecord
/*  12:    */   implements Cloneable
/*  13:    */ {
/*  14:    */   public static final short sid = 4122;
/*  15:    */   private short field_1_formatFlags;
/*  16: 35 */   private static final BitField stacked = BitFieldFactory.getInstance(1);
/*  17: 36 */   private static final BitField displayAsPercentage = BitFieldFactory.getInstance(2);
/*  18: 37 */   private static final BitField shadow = BitFieldFactory.getInstance(4);
/*  19:    */   
/*  20:    */   public AreaRecord() {}
/*  21:    */   
/*  22:    */   public AreaRecord(RecordInputStream in)
/*  23:    */   {
/*  24: 48 */     this.field_1_formatFlags = in.readShort();
/*  25:    */   }
/*  26:    */   
/*  27:    */   public String toString()
/*  28:    */   {
/*  29: 53 */     StringBuffer buffer = new StringBuffer();
/*  30:    */     
/*  31: 55 */     buffer.append("[AREA]\n");
/*  32: 56 */     buffer.append("    .formatFlags          = ").append("0x").append(HexDump.toHex(getFormatFlags())).append(" (").append(getFormatFlags()).append(" )");
/*  33:    */     
/*  34:    */ 
/*  35: 59 */     buffer.append(System.getProperty("line.separator"));
/*  36: 60 */     buffer.append("         .stacked                  = ").append(isStacked()).append('\n');
/*  37: 61 */     buffer.append("         .displayAsPercentage      = ").append(isDisplayAsPercentage()).append('\n');
/*  38: 62 */     buffer.append("         .shadow                   = ").append(isShadow()).append('\n');
/*  39:    */     
/*  40: 64 */     buffer.append("[/AREA]\n");
/*  41: 65 */     return buffer.toString();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void serialize(LittleEndianOutput out)
/*  45:    */   {
/*  46: 69 */     out.writeShort(this.field_1_formatFlags);
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected int getDataSize()
/*  50:    */   {
/*  51: 73 */     return 2;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public short getSid()
/*  55:    */   {
/*  56: 78 */     return 4122;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public AreaRecord clone()
/*  60:    */   {
/*  61: 83 */     AreaRecord rec = new AreaRecord();
/*  62:    */     
/*  63: 85 */     rec.field_1_formatFlags = this.field_1_formatFlags;
/*  64: 86 */     return rec;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public short getFormatFlags()
/*  68:    */   {
/*  69: 97 */     return this.field_1_formatFlags;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setFormatFlags(short field_1_formatFlags)
/*  73:    */   {
/*  74:105 */     this.field_1_formatFlags = field_1_formatFlags;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setStacked(boolean value)
/*  78:    */   {
/*  79:114 */     this.field_1_formatFlags = stacked.setShortBoolean(this.field_1_formatFlags, value);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public boolean isStacked()
/*  83:    */   {
/*  84:123 */     return stacked.isSet(this.field_1_formatFlags);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setDisplayAsPercentage(boolean value)
/*  88:    */   {
/*  89:132 */     this.field_1_formatFlags = displayAsPercentage.setShortBoolean(this.field_1_formatFlags, value);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public boolean isDisplayAsPercentage()
/*  93:    */   {
/*  94:141 */     return displayAsPercentage.isSet(this.field_1_formatFlags);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void setShadow(boolean value)
/*  98:    */   {
/*  99:150 */     this.field_1_formatFlags = shadow.setShortBoolean(this.field_1_formatFlags, value);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public boolean isShadow()
/* 103:    */   {
/* 104:159 */     return shadow.isSet(this.field_1_formatFlags);
/* 105:    */   }
/* 106:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.AreaRecord
 * JD-Core Version:    0.7.0.1
 */