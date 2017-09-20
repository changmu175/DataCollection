/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.LittleEndianInput;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ import org.apache.poi.util.StringUtil;
/*   7:    */ 
/*   8:    */ public final class NameCommentRecord
/*   9:    */   extends StandardRecord
/*  10:    */ {
/*  11:    */   public static final short sid = 2196;
/*  12:    */   private final short field_1_record_type;
/*  13:    */   private final short field_2_frt_cell_ref_flag;
/*  14:    */   private final long field_3_reserved;
/*  15:    */   private String field_6_name_text;
/*  16:    */   private String field_7_comment_text;
/*  17:    */   
/*  18:    */   public NameCommentRecord(String name, String comment)
/*  19:    */   {
/*  20: 42 */     this.field_1_record_type = 0;
/*  21: 43 */     this.field_2_frt_cell_ref_flag = 0;
/*  22: 44 */     this.field_3_reserved = 0L;
/*  23: 45 */     this.field_6_name_text = name;
/*  24: 46 */     this.field_7_comment_text = comment;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void serialize(LittleEndianOutput out)
/*  28:    */   {
/*  29: 51 */     int field_4_name_length = this.field_6_name_text.length();
/*  30: 52 */     int field_5_comment_length = this.field_7_comment_text.length();
/*  31:    */     
/*  32: 54 */     out.writeShort(this.field_1_record_type);
/*  33: 55 */     out.writeShort(this.field_2_frt_cell_ref_flag);
/*  34: 56 */     out.writeLong(this.field_3_reserved);
/*  35: 57 */     out.writeShort(field_4_name_length);
/*  36: 58 */     out.writeShort(field_5_comment_length);
/*  37:    */     
/*  38: 60 */     boolean isNameMultiByte = StringUtil.hasMultibyte(this.field_6_name_text);
/*  39: 61 */     out.writeByte(isNameMultiByte ? 1 : 0);
/*  40: 62 */     if (isNameMultiByte) {
/*  41: 63 */       StringUtil.putUnicodeLE(this.field_6_name_text, out);
/*  42:    */     } else {
/*  43: 65 */       StringUtil.putCompressedUnicode(this.field_6_name_text, out);
/*  44:    */     }
/*  45: 67 */     boolean isCommentMultiByte = StringUtil.hasMultibyte(this.field_7_comment_text);
/*  46: 68 */     out.writeByte(isCommentMultiByte ? 1 : 0);
/*  47: 69 */     if (isCommentMultiByte) {
/*  48: 70 */       StringUtil.putUnicodeLE(this.field_7_comment_text, out);
/*  49:    */     } else {
/*  50: 72 */       StringUtil.putCompressedUnicode(this.field_7_comment_text, out);
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected int getDataSize()
/*  55:    */   {
/*  56: 78 */     return 18 + (StringUtil.hasMultibyte(this.field_6_name_text) ? this.field_6_name_text.length() * 2 : this.field_6_name_text.length()) + (StringUtil.hasMultibyte(this.field_7_comment_text) ? this.field_7_comment_text.length() * 2 : this.field_7_comment_text.length());
/*  57:    */   }
/*  58:    */   
/*  59:    */   public NameCommentRecord(RecordInputStream ris)
/*  60:    */   {
/*  61: 87 */     LittleEndianInput in = ris;
/*  62: 88 */     this.field_1_record_type = in.readShort();
/*  63: 89 */     this.field_2_frt_cell_ref_flag = in.readShort();
/*  64: 90 */     this.field_3_reserved = in.readLong();
/*  65: 91 */     int field_4_name_length = in.readShort();
/*  66: 92 */     int field_5_comment_length = in.readShort();
/*  67: 94 */     if (in.readByte() == 0) {
/*  68: 95 */       this.field_6_name_text = StringUtil.readCompressedUnicode(in, field_4_name_length);
/*  69:    */     } else {
/*  70: 97 */       this.field_6_name_text = StringUtil.readUnicodeLE(in, field_4_name_length);
/*  71:    */     }
/*  72: 99 */     if (in.readByte() == 0) {
/*  73:100 */       this.field_7_comment_text = StringUtil.readCompressedUnicode(in, field_5_comment_length);
/*  74:    */     } else {
/*  75:102 */       this.field_7_comment_text = StringUtil.readUnicodeLE(in, field_5_comment_length);
/*  76:    */     }
/*  77:    */   }
/*  78:    */   
/*  79:    */   public short getSid()
/*  80:    */   {
/*  81:111 */     return 2196;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public String toString()
/*  85:    */   {
/*  86:116 */     StringBuffer sb = new StringBuffer();
/*  87:    */     
/*  88:118 */     sb.append("[NAMECMT]\n");
/*  89:119 */     sb.append("    .record type            = ").append(HexDump.shortToHex(this.field_1_record_type)).append("\n");
/*  90:120 */     sb.append("    .frt cell ref flag      = ").append(HexDump.byteToHex(this.field_2_frt_cell_ref_flag)).append("\n");
/*  91:121 */     sb.append("    .reserved               = ").append(this.field_3_reserved).append("\n");
/*  92:122 */     sb.append("    .name length            = ").append(this.field_6_name_text.length()).append("\n");
/*  93:123 */     sb.append("    .comment length         = ").append(this.field_7_comment_text.length()).append("\n");
/*  94:124 */     sb.append("    .name                   = ").append(this.field_6_name_text).append("\n");
/*  95:125 */     sb.append("    .comment                = ").append(this.field_7_comment_text).append("\n");
/*  96:126 */     sb.append("[/NAMECMT]\n");
/*  97:    */     
/*  98:128 */     return sb.toString();
/*  99:    */   }
/* 100:    */   
/* 101:    */   public String getNameText()
/* 102:    */   {
/* 103:135 */     return this.field_6_name_text;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setNameText(String newName)
/* 107:    */   {
/* 108:145 */     this.field_6_name_text = newName;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public String getCommentText()
/* 112:    */   {
/* 113:152 */     return this.field_7_comment_text;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public void setCommentText(String comment)
/* 117:    */   {
/* 118:156 */     this.field_7_comment_text = comment;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public short getRecordType()
/* 122:    */   {
/* 123:160 */     return this.field_1_record_type;
/* 124:    */   }
/* 125:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.NameCommentRecord
 * JD-Core Version:    0.7.0.1
 */