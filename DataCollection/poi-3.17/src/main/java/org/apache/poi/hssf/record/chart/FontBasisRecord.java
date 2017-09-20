/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ 
/*   8:    */ public final class FontBasisRecord
/*   9:    */   extends StandardRecord
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12:    */   public static final short sid = 4192;
/*  13:    */   private short field_1_xBasis;
/*  14:    */   private short field_2_yBasis;
/*  15:    */   private short field_3_heightBasis;
/*  16:    */   private short field_4_scale;
/*  17:    */   private short field_5_indexToFontTable;
/*  18:    */   
/*  19:    */   public FontBasisRecord() {}
/*  20:    */   
/*  21:    */   public FontBasisRecord(RecordInputStream in)
/*  22:    */   {
/*  23: 44 */     this.field_1_xBasis = in.readShort();
/*  24: 45 */     this.field_2_yBasis = in.readShort();
/*  25: 46 */     this.field_3_heightBasis = in.readShort();
/*  26: 47 */     this.field_4_scale = in.readShort();
/*  27: 48 */     this.field_5_indexToFontTable = in.readShort();
/*  28:    */   }
/*  29:    */   
/*  30:    */   public String toString()
/*  31:    */   {
/*  32: 53 */     StringBuffer buffer = new StringBuffer();
/*  33:    */     
/*  34: 55 */     buffer.append("[FBI]\n");
/*  35: 56 */     buffer.append("    .xBasis               = ").append("0x").append(HexDump.toHex(getXBasis())).append(" (").append(getXBasis()).append(" )");
/*  36:    */     
/*  37:    */ 
/*  38: 59 */     buffer.append(System.getProperty("line.separator"));
/*  39: 60 */     buffer.append("    .yBasis               = ").append("0x").append(HexDump.toHex(getYBasis())).append(" (").append(getYBasis()).append(" )");
/*  40:    */     
/*  41:    */ 
/*  42: 63 */     buffer.append(System.getProperty("line.separator"));
/*  43: 64 */     buffer.append("    .heightBasis          = ").append("0x").append(HexDump.toHex(getHeightBasis())).append(" (").append(getHeightBasis()).append(" )");
/*  44:    */     
/*  45:    */ 
/*  46: 67 */     buffer.append(System.getProperty("line.separator"));
/*  47: 68 */     buffer.append("    .scale                = ").append("0x").append(HexDump.toHex(getScale())).append(" (").append(getScale()).append(" )");
/*  48:    */     
/*  49:    */ 
/*  50: 71 */     buffer.append(System.getProperty("line.separator"));
/*  51: 72 */     buffer.append("    .indexToFontTable     = ").append("0x").append(HexDump.toHex(getIndexToFontTable())).append(" (").append(getIndexToFontTable()).append(" )");
/*  52:    */     
/*  53:    */ 
/*  54: 75 */     buffer.append(System.getProperty("line.separator"));
/*  55:    */     
/*  56: 77 */     buffer.append("[/FBI]\n");
/*  57: 78 */     return buffer.toString();
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void serialize(LittleEndianOutput out)
/*  61:    */   {
/*  62: 82 */     out.writeShort(this.field_1_xBasis);
/*  63: 83 */     out.writeShort(this.field_2_yBasis);
/*  64: 84 */     out.writeShort(this.field_3_heightBasis);
/*  65: 85 */     out.writeShort(this.field_4_scale);
/*  66: 86 */     out.writeShort(this.field_5_indexToFontTable);
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected int getDataSize()
/*  70:    */   {
/*  71: 90 */     return 10;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public short getSid()
/*  75:    */   {
/*  76: 95 */     return 4192;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public FontBasisRecord clone()
/*  80:    */   {
/*  81:100 */     FontBasisRecord rec = new FontBasisRecord();
/*  82:    */     
/*  83:102 */     rec.field_1_xBasis = this.field_1_xBasis;
/*  84:103 */     rec.field_2_yBasis = this.field_2_yBasis;
/*  85:104 */     rec.field_3_heightBasis = this.field_3_heightBasis;
/*  86:105 */     rec.field_4_scale = this.field_4_scale;
/*  87:106 */     rec.field_5_indexToFontTable = this.field_5_indexToFontTable;
/*  88:107 */     return rec;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public short getXBasis()
/*  92:    */   {
/*  93:118 */     return this.field_1_xBasis;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void setXBasis(short field_1_xBasis)
/*  97:    */   {
/*  98:126 */     this.field_1_xBasis = field_1_xBasis;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public short getYBasis()
/* 102:    */   {
/* 103:134 */     return this.field_2_yBasis;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setYBasis(short field_2_yBasis)
/* 107:    */   {
/* 108:142 */     this.field_2_yBasis = field_2_yBasis;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public short getHeightBasis()
/* 112:    */   {
/* 113:150 */     return this.field_3_heightBasis;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public void setHeightBasis(short field_3_heightBasis)
/* 117:    */   {
/* 118:158 */     this.field_3_heightBasis = field_3_heightBasis;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public short getScale()
/* 122:    */   {
/* 123:166 */     return this.field_4_scale;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void setScale(short field_4_scale)
/* 127:    */   {
/* 128:174 */     this.field_4_scale = field_4_scale;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public short getIndexToFontTable()
/* 132:    */   {
/* 133:182 */     return this.field_5_indexToFontTable;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void setIndexToFontTable(short field_5_indexToFontTable)
/* 137:    */   {
/* 138:190 */     this.field_5_indexToFontTable = field_5_indexToFontTable;
/* 139:    */   }
/* 140:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.FontBasisRecord
 * JD-Core Version:    0.7.0.1
 */