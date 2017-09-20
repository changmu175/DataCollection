/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ import org.apache.poi.util.StringUtil;
/*   5:    */ 
/*   6:    */ public final class NoteRecord
/*   7:    */   extends StandardRecord
/*   8:    */   implements Cloneable
/*   9:    */ {
/*  10:    */   public static final short sid = 28;
/*  11: 29 */   public static final NoteRecord[] EMPTY_ARRAY = new NoteRecord[0];
/*  12:    */   public static final short NOTE_HIDDEN = 0;
/*  13:    */   public static final short NOTE_VISIBLE = 2;
/*  14: 41 */   private static final Byte DEFAULT_PADDING = Byte.valueOf((byte)0);
/*  15:    */   private int field_1_row;
/*  16:    */   private int field_2_col;
/*  17:    */   private short field_3_flags;
/*  18:    */   private int field_4_shapeid;
/*  19:    */   private boolean field_5_hasMultibyte;
/*  20:    */   private String field_6_author;
/*  21:    */   private Byte field_7_padding;
/*  22:    */   
/*  23:    */   public NoteRecord()
/*  24:    */   {
/*  25: 62 */     this.field_6_author = "";
/*  26: 63 */     this.field_3_flags = 0;
/*  27: 64 */     this.field_7_padding = DEFAULT_PADDING;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public short getSid()
/*  31:    */   {
/*  32: 71 */     return 28;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public NoteRecord(RecordInputStream in)
/*  36:    */   {
/*  37: 80 */     this.field_1_row = in.readUShort();
/*  38: 81 */     this.field_2_col = in.readShort();
/*  39: 82 */     this.field_3_flags = in.readShort();
/*  40: 83 */     this.field_4_shapeid = in.readUShort();
/*  41: 84 */     int length = in.readShort();
/*  42: 85 */     this.field_5_hasMultibyte = (in.readByte() != 0);
/*  43: 86 */     if (this.field_5_hasMultibyte) {
/*  44: 87 */       this.field_6_author = StringUtil.readUnicodeLE(in, length);
/*  45:    */     } else {
/*  46: 89 */       this.field_6_author = StringUtil.readCompressedUnicode(in, length);
/*  47:    */     }
/*  48: 91 */     if (in.available() == 1)
/*  49:    */     {
/*  50: 92 */       this.field_7_padding = Byte.valueOf(in.readByte());
/*  51:    */     }
/*  52: 93 */     else if ((in.available() == 2) && (length == 0))
/*  53:    */     {
/*  54: 95 */       this.field_7_padding = Byte.valueOf(in.readByte());
/*  55: 96 */       in.readByte();
/*  56:    */     }
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void serialize(LittleEndianOutput out)
/*  60:    */   {
/*  61:101 */     out.writeShort(this.field_1_row);
/*  62:102 */     out.writeShort(this.field_2_col);
/*  63:103 */     out.writeShort(this.field_3_flags);
/*  64:104 */     out.writeShort(this.field_4_shapeid);
/*  65:105 */     out.writeShort(this.field_6_author.length());
/*  66:106 */     out.writeByte(this.field_5_hasMultibyte ? 1 : 0);
/*  67:107 */     if (this.field_5_hasMultibyte) {
/*  68:108 */       StringUtil.putUnicodeLE(this.field_6_author, out);
/*  69:    */     } else {
/*  70:110 */       StringUtil.putCompressedUnicode(this.field_6_author, out);
/*  71:    */     }
/*  72:112 */     if (this.field_7_padding != null) {
/*  73:113 */       out.writeByte(this.field_7_padding.intValue());
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected int getDataSize()
/*  78:    */   {
/*  79:118 */     return 11 + this.field_6_author.length() * (this.field_5_hasMultibyte ? 2 : 1) + (this.field_7_padding == null ? 0 : 1);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public String toString()
/*  83:    */   {
/*  84:128 */     StringBuffer buffer = new StringBuffer();
/*  85:    */     
/*  86:130 */     buffer.append("[NOTE]\n");
/*  87:131 */     buffer.append("    .row    = ").append(this.field_1_row).append("\n");
/*  88:132 */     buffer.append("    .col    = ").append(this.field_2_col).append("\n");
/*  89:133 */     buffer.append("    .flags  = ").append(this.field_3_flags).append("\n");
/*  90:134 */     buffer.append("    .shapeid= ").append(this.field_4_shapeid).append("\n");
/*  91:135 */     buffer.append("    .author = ").append(this.field_6_author).append("\n");
/*  92:136 */     buffer.append("[/NOTE]\n");
/*  93:137 */     return buffer.toString();
/*  94:    */   }
/*  95:    */   
/*  96:    */   public int getRow()
/*  97:    */   {
/*  98:146 */     return this.field_1_row;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void setRow(int row)
/* 102:    */   {
/* 103:155 */     this.field_1_row = row;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public int getColumn()
/* 107:    */   {
/* 108:164 */     return this.field_2_col;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void setColumn(int col)
/* 112:    */   {
/* 113:173 */     this.field_2_col = col;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public short getFlags()
/* 117:    */   {
/* 118:184 */     return this.field_3_flags;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public void setFlags(short flags)
/* 122:    */   {
/* 123:195 */     this.field_3_flags = flags;
/* 124:    */   }
/* 125:    */   
/* 126:    */   protected boolean authorIsMultibyte()
/* 127:    */   {
/* 128:204 */     return this.field_5_hasMultibyte;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public int getShapeId()
/* 132:    */   {
/* 133:213 */     return this.field_4_shapeid;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void setShapeId(int id)
/* 137:    */   {
/* 138:222 */     this.field_4_shapeid = id;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public String getAuthor()
/* 142:    */   {
/* 143:231 */     return this.field_6_author;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void setAuthor(String author)
/* 147:    */   {
/* 148:240 */     this.field_6_author = author;
/* 149:241 */     this.field_5_hasMultibyte = StringUtil.hasMultibyte(author);
/* 150:    */   }
/* 151:    */   
/* 152:    */   public NoteRecord clone()
/* 153:    */   {
/* 154:246 */     NoteRecord rec = new NoteRecord();
/* 155:247 */     rec.field_1_row = this.field_1_row;
/* 156:248 */     rec.field_2_col = this.field_2_col;
/* 157:249 */     rec.field_3_flags = this.field_3_flags;
/* 158:250 */     rec.field_4_shapeid = this.field_4_shapeid;
/* 159:251 */     rec.field_6_author = this.field_6_author;
/* 160:252 */     return rec;
/* 161:    */   }
/* 162:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.NoteRecord
 * JD-Core Version:    0.7.0.1
 */