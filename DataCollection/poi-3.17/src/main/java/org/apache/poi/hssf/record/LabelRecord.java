/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.POILogFactory;
/*   5:    */ import org.apache.poi.util.POILogger;
/*   6:    */ import org.apache.poi.util.RecordFormatException;
/*   7:    */ 
/*   8:    */ public final class LabelRecord
/*   9:    */   extends Record
/*  10:    */   implements CellValueRecordInterface, Cloneable
/*  11:    */ {
/*  12: 33 */   private static final POILogger logger = POILogFactory.getLogger(LabelRecord.class);
/*  13:    */   public static final short sid = 516;
/*  14:    */   private int field_1_row;
/*  15:    */   private short field_2_column;
/*  16:    */   private short field_3_xf_index;
/*  17:    */   private short field_4_string_len;
/*  18:    */   private byte field_5_unicode_flag;
/*  19:    */   private String field_6_value;
/*  20:    */   
/*  21:    */   public LabelRecord() {}
/*  22:    */   
/*  23:    */   public LabelRecord(RecordInputStream in)
/*  24:    */   {
/*  25: 54 */     this.field_1_row = in.readUShort();
/*  26: 55 */     this.field_2_column = in.readShort();
/*  27: 56 */     this.field_3_xf_index = in.readShort();
/*  28: 57 */     this.field_4_string_len = in.readShort();
/*  29: 58 */     this.field_5_unicode_flag = in.readByte();
/*  30: 59 */     if (this.field_4_string_len > 0)
/*  31:    */     {
/*  32: 60 */       if (isUnCompressedUnicode()) {
/*  33: 61 */         this.field_6_value = in.readUnicodeLEString(this.field_4_string_len);
/*  34:    */       } else {
/*  35: 63 */         this.field_6_value = in.readCompressedUnicode(this.field_4_string_len);
/*  36:    */       }
/*  37:    */     }
/*  38:    */     else {
/*  39: 66 */       this.field_6_value = "";
/*  40:    */     }
/*  41: 69 */     if (in.remaining() > 0) {
/*  42: 70 */       logger.log(3, new Object[] { "LabelRecord data remains: " + in.remaining() + " : " + HexDump.toHex(in.readRemainder()) });
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public int getRow()
/*  47:    */   {
/*  48: 83 */     return this.field_1_row;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public short getColumn()
/*  52:    */   {
/*  53: 89 */     return this.field_2_column;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public short getXFIndex()
/*  57:    */   {
/*  58: 95 */     return this.field_3_xf_index;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public short getStringLength()
/*  62:    */   {
/*  63:104 */     return this.field_4_string_len;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public boolean isUnCompressedUnicode()
/*  67:    */   {
/*  68:113 */     return (this.field_5_unicode_flag & 0x1) != 0;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public String getValue()
/*  72:    */   {
/*  73:124 */     return this.field_6_value;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public int serialize(int offset, byte[] data)
/*  77:    */   {
/*  78:132 */     throw new RecordFormatException("Label Records are supported READ ONLY...convert to LabelSST");
/*  79:    */   }
/*  80:    */   
/*  81:    */   public int getRecordSize()
/*  82:    */   {
/*  83:136 */     throw new RecordFormatException("Label Records are supported READ ONLY...convert to LabelSST");
/*  84:    */   }
/*  85:    */   
/*  86:    */   public short getSid()
/*  87:    */   {
/*  88:142 */     return 516;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public String toString()
/*  92:    */   {
/*  93:148 */     StringBuffer sb = new StringBuffer();
/*  94:149 */     sb.append("[LABEL]\n");
/*  95:150 */     sb.append("    .row       = ").append(HexDump.shortToHex(getRow())).append("\n");
/*  96:151 */     sb.append("    .column    = ").append(HexDump.shortToHex(getColumn())).append("\n");
/*  97:152 */     sb.append("    .xfindex   = ").append(HexDump.shortToHex(getXFIndex())).append("\n");
/*  98:153 */     sb.append("    .string_len= ").append(HexDump.shortToHex(this.field_4_string_len)).append("\n");
/*  99:154 */     sb.append("    .unicode_flag= ").append(HexDump.byteToHex(this.field_5_unicode_flag)).append("\n");
/* 100:155 */     sb.append("    .value       = ").append(getValue()).append("\n");
/* 101:156 */     sb.append("[/LABEL]\n");
/* 102:157 */     return sb.toString();
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setColumn(short col) {}
/* 106:    */   
/* 107:    */   public void setRow(int row) {}
/* 108:    */   
/* 109:    */   public void setXFIndex(short xf) {}
/* 110:    */   
/* 111:    */   public LabelRecord clone()
/* 112:    */   {
/* 113:186 */     LabelRecord rec = new LabelRecord();
/* 114:187 */     rec.field_1_row = this.field_1_row;
/* 115:188 */     rec.field_2_column = this.field_2_column;
/* 116:189 */     rec.field_3_xf_index = this.field_3_xf_index;
/* 117:190 */     rec.field_4_string_len = this.field_4_string_len;
/* 118:191 */     rec.field_5_unicode_flag = this.field_5_unicode_flag;
/* 119:192 */     rec.field_6_value = this.field_6_value;
/* 120:193 */     return rec;
/* 121:    */   }
/* 122:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.LabelRecord
 * JD-Core Version:    0.7.0.1
 */