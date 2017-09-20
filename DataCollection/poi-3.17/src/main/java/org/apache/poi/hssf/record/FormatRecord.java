/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.LittleEndianOutput;
/*   5:    */ import org.apache.poi.util.POILogFactory;
/*   6:    */ import org.apache.poi.util.POILogger;
/*   7:    */ import org.apache.poi.util.StringUtil;
/*   8:    */ 
/*   9:    */ public final class FormatRecord
/*  10:    */   extends StandardRecord
/*  11:    */   implements Cloneable
/*  12:    */ {
/*  13: 35 */   private static final POILogger logger = POILogFactory.getLogger(FormatRecord.class);
/*  14:    */   public static final short sid = 1054;
/*  15:    */   private final int field_1_index_code;
/*  16:    */   private final boolean field_3_hasMultibyte;
/*  17:    */   private final String field_4_formatstring;
/*  18:    */   
/*  19:    */   private FormatRecord(FormatRecord other)
/*  20:    */   {
/*  21: 44 */     this.field_1_index_code = other.field_1_index_code;
/*  22: 45 */     this.field_3_hasMultibyte = other.field_3_hasMultibyte;
/*  23: 46 */     this.field_4_formatstring = other.field_4_formatstring;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public FormatRecord(int indexCode, String fs)
/*  27:    */   {
/*  28: 50 */     this.field_1_index_code = indexCode;
/*  29: 51 */     this.field_4_formatstring = fs;
/*  30: 52 */     this.field_3_hasMultibyte = StringUtil.hasMultibyte(fs);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public FormatRecord(RecordInputStream in)
/*  34:    */   {
/*  35: 56 */     this.field_1_index_code = in.readShort();
/*  36: 57 */     int field_3_unicode_len = in.readUShort();
/*  37: 58 */     this.field_3_hasMultibyte = ((in.readByte() & 0x1) != 0);
/*  38: 60 */     if (this.field_3_hasMultibyte) {
/*  39: 61 */       this.field_4_formatstring = readStringCommon(in, field_3_unicode_len, false);
/*  40:    */     } else {
/*  41: 63 */       this.field_4_formatstring = readStringCommon(in, field_3_unicode_len, true);
/*  42:    */     }
/*  43:    */   }
/*  44:    */   
/*  45:    */   public int getIndexCode()
/*  46:    */   {
/*  47: 74 */     return this.field_1_index_code;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public String getFormatString()
/*  51:    */   {
/*  52: 83 */     return this.field_4_formatstring;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public String toString()
/*  56:    */   {
/*  57: 87 */     StringBuffer buffer = new StringBuffer();
/*  58:    */     
/*  59: 89 */     buffer.append("[FORMAT]\n");
/*  60: 90 */     buffer.append("    .indexcode       = ").append(HexDump.shortToHex(getIndexCode())).append("\n");
/*  61: 91 */     buffer.append("    .isUnicode       = ").append(this.field_3_hasMultibyte).append("\n");
/*  62: 92 */     buffer.append("    .formatstring    = ").append(getFormatString()).append("\n");
/*  63: 93 */     buffer.append("[/FORMAT]\n");
/*  64: 94 */     return buffer.toString();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void serialize(LittleEndianOutput out)
/*  68:    */   {
/*  69: 98 */     String formatString = getFormatString();
/*  70: 99 */     out.writeShort(getIndexCode());
/*  71:100 */     out.writeShort(formatString.length());
/*  72:101 */     out.writeByte(this.field_3_hasMultibyte ? 1 : 0);
/*  73:103 */     if (this.field_3_hasMultibyte) {
/*  74:104 */       StringUtil.putUnicodeLE(formatString, out);
/*  75:    */     } else {
/*  76:106 */       StringUtil.putCompressedUnicode(formatString, out);
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected int getDataSize()
/*  81:    */   {
/*  82:110 */     return 5 + getFormatString().length() * (this.field_3_hasMultibyte ? 2 : 1);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public short getSid()
/*  86:    */   {
/*  87:115 */     return 1054;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public FormatRecord clone()
/*  91:    */   {
/*  92:120 */     return new FormatRecord(this);
/*  93:    */   }
/*  94:    */   
/*  95:    */   private static String readStringCommon(RecordInputStream ris, int requestedLength, boolean pIsCompressedEncoding)
/*  96:    */   {
/*  97:127 */     if ((requestedLength < 0) || (requestedLength > 1048576)) {
/*  98:128 */       throw new IllegalArgumentException("Bad requested string length (" + requestedLength + ")");
/*  99:    */     }
/* 100:130 */     char[] buf = null;
/* 101:131 */     boolean isCompressedEncoding = pIsCompressedEncoding;
/* 102:132 */     int availableChars = isCompressedEncoding ? ris.remaining() : ris.remaining() / 2;
/* 103:    */     
/* 104:134 */     int remaining = ris.remaining();
/* 105:135 */     if (requestedLength == availableChars) {
/* 106:136 */       buf = new char[requestedLength];
/* 107:    */     } else {
/* 108:141 */       buf = new char[availableChars];
/* 109:    */     }
/* 110:143 */     for (int i = 0; i < buf.length; i++)
/* 111:    */     {
/* 112:    */       char ch;
/* 113:    */       char ch;
/* 114:145 */       if (isCompressedEncoding) {
/* 115:146 */         ch = (char)ris.readUByte();
/* 116:    */       } else {
/* 117:148 */         ch = (char)ris.readShort();
/* 118:    */       }
/* 119:150 */       buf[i] = ch;
/* 120:    */     }
/* 121:156 */     if (ris.available() == 1)
/* 122:    */     {
/* 123:157 */       char[] tmp = new char[buf.length + 1];
/* 124:158 */       System.arraycopy(buf, 0, tmp, 0, buf.length);
/* 125:159 */       tmp[buf.length] = ((char)ris.readUByte());
/* 126:160 */       buf = tmp;
/* 127:    */     }
/* 128:163 */     if (ris.available() > 0)
/* 129:    */     {
/* 130:164 */       logger.log(3, new Object[] { "FormatRecord has " + ris.available() + " unexplained bytes. Silently skipping" });
/* 131:166 */       while (ris.available() > 0) {
/* 132:167 */         ris.readByte();
/* 133:    */       }
/* 134:    */     }
/* 135:170 */     return new String(buf);
/* 136:    */   }
/* 137:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.FormatRecord
 * JD-Core Version:    0.7.0.1
 */