/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.poi.util.LittleEndian;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ import org.apache.poi.util.RecordFormatException;
/*   7:    */ import org.apache.poi.util.StringUtil;
/*   8:    */ 
/*   9:    */ public final class WriteAccessRecord
/*  10:    */   extends StandardRecord
/*  11:    */ {
/*  12:    */   public static final short sid = 92;
/*  13:    */   private static final byte PAD_CHAR = 32;
/*  14:    */   private static final int DATA_SIZE = 112;
/*  15:    */   private String field_1_username;
/*  16: 42 */   private static final byte[] PADDING = new byte[112];
/*  17:    */   
/*  18:    */   static
/*  19:    */   {
/*  20: 44 */     Arrays.fill(PADDING, (byte)32);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public WriteAccessRecord()
/*  24:    */   {
/*  25: 48 */     setUsername("");
/*  26:    */   }
/*  27:    */   
/*  28:    */   public WriteAccessRecord(RecordInputStream in)
/*  29:    */   {
/*  30: 52 */     if (in.remaining() > 112) {
/*  31: 53 */       throw new RecordFormatException("Expected data size (112) but got (" + in.remaining() + ")");
/*  32:    */     }
/*  33: 59 */     int nChars = in.readUShort();
/*  34: 60 */     int is16BitFlag = in.readUByte();
/*  35: 61 */     if ((nChars > 112) || ((is16BitFlag & 0xFE) != 0))
/*  36:    */     {
/*  37: 65 */       byte[] data = new byte[3 + in.remaining()];
/*  38: 66 */       LittleEndian.putUShort(data, 0, nChars);
/*  39: 67 */       LittleEndian.putByte(data, 2, is16BitFlag);
/*  40: 68 */       in.readFully(data, 3, data.length - 3);
/*  41: 69 */       String rawValue = new String(data, StringUtil.UTF8);
/*  42: 70 */       setUsername(rawValue.trim()); return;
/*  43:    */     }
/*  44:    */     String rawText;
/*  45:    */     String rawText;
/*  46: 75 */     if ((is16BitFlag & 0x1) == 0) {
/*  47: 76 */       rawText = StringUtil.readCompressedUnicode(in, nChars);
/*  48:    */     } else {
/*  49: 78 */       rawText = StringUtil.readUnicodeLE(in, nChars);
/*  50:    */     }
/*  51: 80 */     this.field_1_username = rawText.trim();
/*  52:    */     
/*  53:    */ 
/*  54: 83 */     int padSize = in.remaining();
/*  55: 84 */     while (padSize > 0)
/*  56:    */     {
/*  57: 86 */       in.readUByte();
/*  58: 87 */       padSize--;
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setUsername(String username)
/*  63:    */   {
/*  64: 98 */     boolean is16bit = StringUtil.hasMultibyte(username);
/*  65: 99 */     int encodedByteCount = 3 + username.length() * (is16bit ? 2 : 1);
/*  66:100 */     int paddingSize = 112 - encodedByteCount;
/*  67:101 */     if (paddingSize < 0) {
/*  68:102 */       throw new IllegalArgumentException("Name is too long: " + username);
/*  69:    */     }
/*  70:105 */     this.field_1_username = username;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public String getUsername()
/*  74:    */   {
/*  75:116 */     return this.field_1_username;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public String toString()
/*  79:    */   {
/*  80:120 */     StringBuffer buffer = new StringBuffer();
/*  81:    */     
/*  82:122 */     buffer.append("[WRITEACCESS]\n");
/*  83:123 */     buffer.append("    .name = ").append(this.field_1_username).append("\n");
/*  84:124 */     buffer.append("[/WRITEACCESS]\n");
/*  85:125 */     return buffer.toString();
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void serialize(LittleEndianOutput out)
/*  89:    */   {
/*  90:129 */     String username = getUsername();
/*  91:130 */     boolean is16bit = StringUtil.hasMultibyte(username);
/*  92:    */     
/*  93:132 */     out.writeShort(username.length());
/*  94:133 */     out.writeByte(is16bit ? 1 : 0);
/*  95:134 */     if (is16bit) {
/*  96:135 */       StringUtil.putUnicodeLE(username, out);
/*  97:    */     } else {
/*  98:137 */       StringUtil.putCompressedUnicode(username, out);
/*  99:    */     }
/* 100:139 */     int encodedByteCount = 3 + username.length() * (is16bit ? 2 : 1);
/* 101:140 */     int paddingSize = 112 - encodedByteCount;
/* 102:141 */     out.write(PADDING, 0, paddingSize);
/* 103:    */   }
/* 104:    */   
/* 105:    */   protected int getDataSize()
/* 106:    */   {
/* 107:145 */     return 112;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public short getSid()
/* 111:    */   {
/* 112:149 */     return 92;
/* 113:    */   }
/* 114:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.WriteAccessRecord
 * JD-Core Version:    0.7.0.1
 */