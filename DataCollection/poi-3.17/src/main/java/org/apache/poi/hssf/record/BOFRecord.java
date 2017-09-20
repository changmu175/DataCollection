/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.LittleEndianOutput;
/*   5:    */ 
/*   6:    */ public final class BOFRecord
/*   7:    */   extends StandardRecord
/*   8:    */   implements Cloneable
/*   9:    */ {
/*  10:    */   public static final short sid = 2057;
/*  11:    */   public static final short biff2_sid = 9;
/*  12:    */   public static final short biff3_sid = 521;
/*  13:    */   public static final short biff4_sid = 1033;
/*  14:    */   public static final short biff5_sid = 2057;
/*  15:    */   public static final int VERSION = 1536;
/*  16:    */   public static final int BUILD = 4307;
/*  17:    */   public static final int BUILD_YEAR = 1996;
/*  18:    */   public static final int HISTORY_MASK = 65;
/*  19:    */   public static final int TYPE_WORKBOOK = 5;
/*  20:    */   public static final int TYPE_VB_MODULE = 6;
/*  21:    */   public static final int TYPE_WORKSHEET = 16;
/*  22:    */   public static final int TYPE_CHART = 32;
/*  23:    */   public static final int TYPE_EXCEL_4_MACRO = 64;
/*  24:    */   public static final int TYPE_WORKSPACE_FILE = 256;
/*  25:    */   private int field_1_version;
/*  26:    */   private int field_2_type;
/*  27:    */   private int field_3_build;
/*  28:    */   private int field_4_year;
/*  29:    */   private int field_5_history;
/*  30:    */   private int field_6_rversion;
/*  31:    */   
/*  32:    */   public BOFRecord() {}
/*  33:    */   
/*  34:    */   private BOFRecord(int type)
/*  35:    */   {
/*  36: 75 */     this.field_1_version = 1536;
/*  37: 76 */     this.field_2_type = type;
/*  38: 77 */     this.field_3_build = 4307;
/*  39: 78 */     this.field_4_year = 1996;
/*  40: 79 */     this.field_5_history = 1;
/*  41: 80 */     this.field_6_rversion = 1536;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public static BOFRecord createSheetBOF()
/*  45:    */   {
/*  46: 84 */     return new BOFRecord(16);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public BOFRecord(RecordInputStream in)
/*  50:    */   {
/*  51: 88 */     this.field_1_version = in.readShort();
/*  52: 89 */     this.field_2_type = in.readShort();
/*  53: 93 */     if (in.remaining() >= 2) {
/*  54: 94 */       this.field_3_build = in.readShort();
/*  55:    */     }
/*  56: 96 */     if (in.remaining() >= 2) {
/*  57: 97 */       this.field_4_year = in.readShort();
/*  58:    */     }
/*  59: 99 */     if (in.remaining() >= 4) {
/*  60:100 */       this.field_5_history = in.readInt();
/*  61:    */     }
/*  62:102 */     if (in.remaining() >= 4) {
/*  63:103 */       this.field_6_rversion = in.readInt();
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setVersion(int version)
/*  68:    */   {
/*  69:113 */     this.field_1_version = version;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setType(int type)
/*  73:    */   {
/*  74:127 */     this.field_2_type = type;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setBuild(int build)
/*  78:    */   {
/*  79:136 */     this.field_3_build = build;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void setBuildYear(int year)
/*  83:    */   {
/*  84:145 */     this.field_4_year = year;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setHistoryBitMask(int bitmask)
/*  88:    */   {
/*  89:154 */     this.field_5_history = bitmask;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setRequiredVersion(int version)
/*  93:    */   {
/*  94:164 */     this.field_6_rversion = version;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public int getVersion()
/*  98:    */   {
/*  99:173 */     return this.field_1_version;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public int getType()
/* 103:    */   {
/* 104:187 */     return this.field_2_type;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public int getBuild()
/* 108:    */   {
/* 109:196 */     return this.field_3_build;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public int getBuildYear()
/* 113:    */   {
/* 114:205 */     return this.field_4_year;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public int getHistoryBitMask()
/* 118:    */   {
/* 119:214 */     return this.field_5_history;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public int getRequiredVersion()
/* 123:    */   {
/* 124:224 */     return this.field_6_rversion;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public String toString()
/* 128:    */   {
/* 129:228 */     StringBuffer buffer = new StringBuffer();
/* 130:    */     
/* 131:230 */     buffer.append("[BOF RECORD]\n");
/* 132:231 */     buffer.append("    .version  = ").append(HexDump.shortToHex(getVersion())).append("\n");
/* 133:232 */     buffer.append("    .type     = ").append(HexDump.shortToHex(getType()));
/* 134:233 */     buffer.append(" (").append(getTypeName()).append(")").append("\n");
/* 135:234 */     buffer.append("    .build    = ").append(HexDump.shortToHex(getBuild())).append("\n");
/* 136:235 */     buffer.append("    .buildyear= ").append(getBuildYear()).append("\n");
/* 137:236 */     buffer.append("    .history  = ").append(HexDump.intToHex(getHistoryBitMask())).append("\n");
/* 138:237 */     buffer.append("    .reqver   = ").append(HexDump.intToHex(getRequiredVersion())).append("\n");
/* 139:238 */     buffer.append("[/BOF RECORD]\n");
/* 140:239 */     return buffer.toString();
/* 141:    */   }
/* 142:    */   
/* 143:    */   private String getTypeName()
/* 144:    */   {
/* 145:243 */     switch (this.field_2_type)
/* 146:    */     {
/* 147:    */     case 32: 
/* 148:244 */       return "chart";
/* 149:    */     case 64: 
/* 150:245 */       return "excel 4 macro";
/* 151:    */     case 6: 
/* 152:246 */       return "vb module";
/* 153:    */     case 5: 
/* 154:247 */       return "workbook";
/* 155:    */     case 16: 
/* 156:248 */       return "worksheet";
/* 157:    */     case 256: 
/* 158:249 */       return "workspace file";
/* 159:    */     }
/* 160:251 */     return "#error unknown type#";
/* 161:    */   }
/* 162:    */   
/* 163:    */   public void serialize(LittleEndianOutput out)
/* 164:    */   {
/* 165:255 */     out.writeShort(getVersion());
/* 166:256 */     out.writeShort(getType());
/* 167:257 */     out.writeShort(getBuild());
/* 168:258 */     out.writeShort(getBuildYear());
/* 169:259 */     out.writeInt(getHistoryBitMask());
/* 170:260 */     out.writeInt(getRequiredVersion());
/* 171:    */   }
/* 172:    */   
/* 173:    */   protected int getDataSize()
/* 174:    */   {
/* 175:264 */     return 16;
/* 176:    */   }
/* 177:    */   
/* 178:    */   public short getSid()
/* 179:    */   {
/* 180:268 */     return 2057;
/* 181:    */   }
/* 182:    */   
/* 183:    */   public BOFRecord clone()
/* 184:    */   {
/* 185:273 */     BOFRecord rec = new BOFRecord();
/* 186:274 */     rec.field_1_version = this.field_1_version;
/* 187:275 */     rec.field_2_type = this.field_2_type;
/* 188:276 */     rec.field_3_build = this.field_3_build;
/* 189:277 */     rec.field_4_year = this.field_4_year;
/* 190:278 */     rec.field_5_history = this.field_5_history;
/* 191:279 */     rec.field_6_rversion = this.field_6_rversion;
/* 192:280 */     return rec;
/* 193:    */   }
/* 194:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.BOFRecord
 * JD-Core Version:    0.7.0.1
 */