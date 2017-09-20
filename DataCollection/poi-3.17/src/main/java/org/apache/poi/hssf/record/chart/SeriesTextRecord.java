/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ import org.apache.poi.util.StringUtil;
/*   8:    */ 
/*   9:    */ public final class SeriesTextRecord
/*  10:    */   extends StandardRecord
/*  11:    */ {
/*  12:    */   public static final short sid = 4109;
/*  13:    */   private static final int MAX_LEN = 255;
/*  14:    */   private int field_1_id;
/*  15:    */   private boolean is16bit;
/*  16:    */   private String field_4_text;
/*  17:    */   
/*  18:    */   public SeriesTextRecord()
/*  19:    */   {
/*  20: 40 */     this.field_4_text = "";
/*  21: 41 */     this.is16bit = false;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public SeriesTextRecord(RecordInputStream in)
/*  25:    */   {
/*  26: 45 */     this.field_1_id = in.readUShort();
/*  27: 46 */     int field_2_textLength = in.readUByte();
/*  28: 47 */     this.is16bit = ((in.readUByte() & 0x1) != 0);
/*  29: 48 */     if (this.is16bit) {
/*  30: 49 */       this.field_4_text = in.readUnicodeLEString(field_2_textLength);
/*  31:    */     } else {
/*  32: 51 */       this.field_4_text = in.readCompressedUnicode(field_2_textLength);
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   public String toString()
/*  37:    */   {
/*  38: 56 */     StringBuffer sb = new StringBuffer();
/*  39:    */     
/*  40: 58 */     sb.append("[SERIESTEXT]\n");
/*  41: 59 */     sb.append("  .id     =").append(HexDump.shortToHex(getId())).append('\n');
/*  42: 60 */     sb.append("  .textLen=").append(this.field_4_text.length()).append('\n');
/*  43: 61 */     sb.append("  .is16bit=").append(this.is16bit).append('\n');
/*  44: 62 */     sb.append("  .text   =").append(" (").append(getText()).append(" )").append('\n');
/*  45: 63 */     sb.append("[/SERIESTEXT]\n");
/*  46: 64 */     return sb.toString();
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void serialize(LittleEndianOutput out)
/*  50:    */   {
/*  51: 69 */     out.writeShort(this.field_1_id);
/*  52: 70 */     out.writeByte(this.field_4_text.length());
/*  53: 71 */     if (this.is16bit)
/*  54:    */     {
/*  55: 73 */       out.writeByte(1);
/*  56: 74 */       StringUtil.putUnicodeLE(this.field_4_text, out);
/*  57:    */     }
/*  58:    */     else
/*  59:    */     {
/*  60: 77 */       out.writeByte(0);
/*  61: 78 */       StringUtil.putCompressedUnicode(this.field_4_text, out);
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected int getDataSize()
/*  66:    */   {
/*  67: 83 */     return 4 + this.field_4_text.length() * (this.is16bit ? 2 : 1);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public short getSid()
/*  71:    */   {
/*  72: 87 */     return 4109;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public Object clone()
/*  76:    */   {
/*  77: 91 */     SeriesTextRecord rec = new SeriesTextRecord();
/*  78:    */     
/*  79: 93 */     rec.field_1_id = this.field_1_id;
/*  80: 94 */     rec.is16bit = this.is16bit;
/*  81: 95 */     rec.field_4_text = this.field_4_text;
/*  82: 96 */     return rec;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public int getId()
/*  86:    */   {
/*  87:103 */     return this.field_1_id;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setId(int id)
/*  91:    */   {
/*  92:110 */     this.field_1_id = id;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public String getText()
/*  96:    */   {
/*  97:117 */     return this.field_4_text;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setText(String text)
/* 101:    */   {
/* 102:124 */     if (text.length() > 255) {
/* 103:125 */       throw new IllegalArgumentException("Text is too long (" + text.length() + ">" + 255 + ")");
/* 104:    */     }
/* 105:128 */     this.field_4_text = text;
/* 106:129 */     this.is16bit = StringUtil.hasMultibyte(text);
/* 107:    */   }
/* 108:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.SeriesTextRecord
 * JD-Core Version:    0.7.0.1
 */