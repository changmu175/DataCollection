/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ import org.apache.poi.util.StringUtil;
/*   5:    */ 
/*   6:    */ public final class FileSharingRecord
/*   7:    */   extends StandardRecord
/*   8:    */   implements Cloneable
/*   9:    */ {
/*  10:    */   public static final short sid = 91;
/*  11:    */   private short field_1_readonly;
/*  12:    */   private short field_2_password;
/*  13:    */   private byte field_3_username_unicode_options;
/*  14:    */   private String field_3_username_value;
/*  15:    */   
/*  16:    */   public FileSharingRecord() {}
/*  17:    */   
/*  18:    */   public FileSharingRecord(RecordInputStream in)
/*  19:    */   {
/*  20: 40 */     this.field_1_readonly = in.readShort();
/*  21: 41 */     this.field_2_password = in.readShort();
/*  22:    */     
/*  23: 43 */     int nameLen = in.readShort();
/*  24: 45 */     if (nameLen > 0)
/*  25:    */     {
/*  26: 47 */       this.field_3_username_unicode_options = in.readByte();
/*  27: 48 */       this.field_3_username_value = in.readCompressedUnicode(nameLen);
/*  28:    */     }
/*  29:    */     else
/*  30:    */     {
/*  31: 50 */       this.field_3_username_value = "";
/*  32:    */     }
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setReadOnly(short readonly)
/*  36:    */   {
/*  37: 60 */     this.field_1_readonly = readonly;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public short getReadOnly()
/*  41:    */   {
/*  42: 69 */     return this.field_1_readonly;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setPassword(short password)
/*  46:    */   {
/*  47: 76 */     this.field_2_password = password;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public short getPassword()
/*  51:    */   {
/*  52: 83 */     return this.field_2_password;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public String getUsername()
/*  56:    */   {
/*  57: 91 */     return this.field_3_username_value;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setUsername(String username)
/*  61:    */   {
/*  62: 98 */     this.field_3_username_value = username;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public String toString()
/*  66:    */   {
/*  67:103 */     StringBuffer buffer = new StringBuffer();
/*  68:    */     
/*  69:105 */     buffer.append("[FILESHARING]\n");
/*  70:106 */     buffer.append("    .readonly       = ").append(getReadOnly() == 1 ? "true" : "false").append("\n");
/*  71:    */     
/*  72:108 */     buffer.append("    .password       = ").append(Integer.toHexString(getPassword())).append("\n");
/*  73:    */     
/*  74:110 */     buffer.append("    .username       = ").append(getUsername()).append("\n");
/*  75:    */     
/*  76:112 */     buffer.append("[/FILESHARING]\n");
/*  77:113 */     return buffer.toString();
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void serialize(LittleEndianOutput out)
/*  81:    */   {
/*  82:118 */     out.writeShort(getReadOnly());
/*  83:119 */     out.writeShort(getPassword());
/*  84:120 */     out.writeShort(this.field_3_username_value.length());
/*  85:121 */     if (this.field_3_username_value.length() > 0)
/*  86:    */     {
/*  87:122 */       out.writeByte(this.field_3_username_unicode_options);
/*  88:123 */       StringUtil.putCompressedUnicode(getUsername(), out);
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   protected int getDataSize()
/*  93:    */   {
/*  94:128 */     int nameLen = this.field_3_username_value.length();
/*  95:129 */     if (nameLen < 1) {
/*  96:130 */       return 6;
/*  97:    */     }
/*  98:132 */     return 7 + nameLen;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public short getSid()
/* 102:    */   {
/* 103:136 */     return 91;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public FileSharingRecord clone()
/* 107:    */   {
/* 108:141 */     FileSharingRecord clone = new FileSharingRecord();
/* 109:142 */     clone.setReadOnly(this.field_1_readonly);
/* 110:143 */     clone.setPassword(this.field_2_password);
/* 111:144 */     clone.setUsername(this.field_3_username_value);
/* 112:145 */     return clone;
/* 113:    */   }
/* 114:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.FileSharingRecord
 * JD-Core Version:    0.7.0.1
 */