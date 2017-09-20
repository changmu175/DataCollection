/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ import org.apache.poi.util.POILogFactory;
/*   5:    */ import org.apache.poi.util.POILogger;
/*   6:    */ import org.apache.poi.util.StringUtil;
/*   7:    */ 
/*   8:    */ public final class SupBookRecord
/*   9:    */   extends StandardRecord
/*  10:    */ {
/*  11: 33 */   private static final POILogger logger = POILogFactory.getLogger(SupBookRecord.class);
/*  12:    */   public static final short sid = 430;
/*  13:    */   private static final short SMALL_RECORD_SIZE = 4;
/*  14:    */   private static final short TAG_INTERNAL_REFERENCES = 1025;
/*  15:    */   private static final short TAG_ADD_IN_FUNCTIONS = 14849;
/*  16:    */   private short field_1_number_of_sheets;
/*  17:    */   private String field_2_encoded_url;
/*  18:    */   private String[] field_3_sheet_names;
/*  19:    */   private boolean _isAddInFunctions;
/*  20:    */   protected static final char CH_VOLUME = '\001';
/*  21:    */   protected static final char CH_SAME_VOLUME = '\002';
/*  22:    */   protected static final char CH_DOWN_DIR = '\003';
/*  23:    */   protected static final char CH_UP_DIR = '\004';
/*  24:    */   protected static final char CH_LONG_VOLUME = '\005';
/*  25:    */   protected static final char CH_STARTUP_DIR = '\006';
/*  26:    */   protected static final char CH_ALT_STARTUP_DIR = '\007';
/*  27:    */   protected static final char CH_LIB_DIR = '\b';
/*  28: 54 */   protected static final String PATH_SEPERATOR = System.getProperty("file.separator");
/*  29:    */   
/*  30:    */   public static SupBookRecord createInternalReferences(short numberOfSheets)
/*  31:    */   {
/*  32: 57 */     return new SupBookRecord(false, numberOfSheets);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static SupBookRecord createAddInFunctions()
/*  36:    */   {
/*  37: 60 */     return new SupBookRecord(true, (short)1);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static SupBookRecord createExternalReferences(String url, String[] sheetNames)
/*  41:    */   {
/*  42: 63 */     return new SupBookRecord(url, sheetNames);
/*  43:    */   }
/*  44:    */   
/*  45:    */   private SupBookRecord(boolean isAddInFuncs, short numberOfSheets)
/*  46:    */   {
/*  47: 67 */     this.field_1_number_of_sheets = numberOfSheets;
/*  48: 68 */     this.field_2_encoded_url = null;
/*  49: 69 */     this.field_3_sheet_names = null;
/*  50: 70 */     this._isAddInFunctions = isAddInFuncs;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public SupBookRecord(String url, String[] sheetNames)
/*  54:    */   {
/*  55: 73 */     this.field_1_number_of_sheets = ((short)sheetNames.length);
/*  56: 74 */     this.field_2_encoded_url = url;
/*  57: 75 */     this.field_3_sheet_names = sheetNames;
/*  58: 76 */     this._isAddInFunctions = false;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public boolean isExternalReferences()
/*  62:    */   {
/*  63: 80 */     return this.field_3_sheet_names != null;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public boolean isInternalReferences()
/*  67:    */   {
/*  68: 83 */     return (this.field_3_sheet_names == null) && (!this._isAddInFunctions);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public boolean isAddInFunctions()
/*  72:    */   {
/*  73: 86 */     return (this.field_3_sheet_names == null) && (this._isAddInFunctions);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public SupBookRecord(RecordInputStream in)
/*  77:    */   {
/*  78: 95 */     int recLen = in.remaining();
/*  79:    */     
/*  80: 97 */     this.field_1_number_of_sheets = in.readShort();
/*  81: 99 */     if (recLen > 4)
/*  82:    */     {
/*  83:101 */       this._isAddInFunctions = false;
/*  84:    */       
/*  85:103 */       this.field_2_encoded_url = in.readString();
/*  86:104 */       String[] sheetNames = new String[this.field_1_number_of_sheets];
/*  87:105 */       for (int i = 0; i < sheetNames.length; i++) {
/*  88:106 */         sheetNames[i] = in.readString();
/*  89:    */       }
/*  90:108 */       this.field_3_sheet_names = sheetNames;
/*  91:109 */       return;
/*  92:    */     }
/*  93:112 */     this.field_2_encoded_url = null;
/*  94:113 */     this.field_3_sheet_names = null;
/*  95:    */     
/*  96:115 */     short nextShort = in.readShort();
/*  97:116 */     if (nextShort == 1025)
/*  98:    */     {
/*  99:118 */       this._isAddInFunctions = false;
/* 100:    */     }
/* 101:119 */     else if (nextShort == 14849)
/* 102:    */     {
/* 103:121 */       this._isAddInFunctions = true;
/* 104:122 */       if (this.field_1_number_of_sheets != 1) {
/* 105:123 */         throw new RuntimeException("Expected 0x0001 for number of sheets field in 'Add-In Functions' but got (" + this.field_1_number_of_sheets + ")");
/* 106:    */       }
/* 107:    */     }
/* 108:    */     else
/* 109:    */     {
/* 110:127 */       throw new RuntimeException("invalid EXTERNALBOOK code (" + Integer.toHexString(nextShort) + ")");
/* 111:    */     }
/* 112:    */   }
/* 113:    */   
/* 114:    */   public String toString()
/* 115:    */   {
/* 116:133 */     StringBuffer sb = new StringBuffer();
/* 117:134 */     sb.append("[SUPBOOK ");
/* 118:136 */     if (isExternalReferences())
/* 119:    */     {
/* 120:137 */       sb.append("External References]\n");
/* 121:138 */       sb.append(" .url     = ").append(this.field_2_encoded_url).append("\n");
/* 122:139 */       sb.append(" .nSheets = ").append(this.field_1_number_of_sheets).append("\n");
/* 123:140 */       for (String sheetname : this.field_3_sheet_names) {
/* 124:141 */         sb.append("    .name = ").append(sheetname).append("\n");
/* 125:    */       }
/* 126:143 */       sb.append("[/SUPBOOK");
/* 127:    */     }
/* 128:144 */     else if (this._isAddInFunctions)
/* 129:    */     {
/* 130:145 */       sb.append("Add-In Functions");
/* 131:    */     }
/* 132:    */     else
/* 133:    */     {
/* 134:147 */       sb.append("Internal References");
/* 135:148 */       sb.append(" nSheets=").append(this.field_1_number_of_sheets);
/* 136:    */     }
/* 137:150 */     sb.append("]");
/* 138:151 */     return sb.toString();
/* 139:    */   }
/* 140:    */   
/* 141:    */   protected int getDataSize()
/* 142:    */   {
/* 143:154 */     if (!isExternalReferences()) {
/* 144:155 */       return 4;
/* 145:    */     }
/* 146:157 */     int sum = 2;
/* 147:    */     
/* 148:159 */     sum += StringUtil.getEncodedSize(this.field_2_encoded_url);
/* 149:161 */     for (int i = 0; i < this.field_3_sheet_names.length; i++) {
/* 150:162 */       sum += StringUtil.getEncodedSize(this.field_3_sheet_names[i]);
/* 151:    */     }
/* 152:164 */     return sum;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void serialize(LittleEndianOutput out)
/* 156:    */   {
/* 157:168 */     out.writeShort(this.field_1_number_of_sheets);
/* 158:170 */     if (isExternalReferences())
/* 159:    */     {
/* 160:171 */       StringUtil.writeUnicodeString(out, this.field_2_encoded_url);
/* 161:173 */       for (int i = 0; i < this.field_3_sheet_names.length; i++) {
/* 162:174 */         StringUtil.writeUnicodeString(out, this.field_3_sheet_names[i]);
/* 163:    */       }
/* 164:    */     }
/* 165:    */     else
/* 166:    */     {
/* 167:177 */       int field2val = this._isAddInFunctions ? 14849 : 1025;
/* 168:    */       
/* 169:179 */       out.writeShort(field2val);
/* 170:    */     }
/* 171:    */   }
/* 172:    */   
/* 173:    */   public void setNumberOfSheets(short number)
/* 174:    */   {
/* 175:184 */     this.field_1_number_of_sheets = number;
/* 176:    */   }
/* 177:    */   
/* 178:    */   public short getNumberOfSheets()
/* 179:    */   {
/* 180:188 */     return this.field_1_number_of_sheets;
/* 181:    */   }
/* 182:    */   
/* 183:    */   public short getSid()
/* 184:    */   {
/* 185:193 */     return 430;
/* 186:    */   }
/* 187:    */   
/* 188:    */   public String getURL()
/* 189:    */   {
/* 190:196 */     String encodedUrl = this.field_2_encoded_url;
/* 191:197 */     switch (encodedUrl.charAt(0))
/* 192:    */     {
/* 193:    */     case '\000': 
/* 194:199 */       return encodedUrl.substring(1);
/* 195:    */     case '\001': 
/* 196:201 */       return decodeFileName(encodedUrl);
/* 197:    */     case '\002': 
/* 198:203 */       return encodedUrl.substring(1);
/* 199:    */     }
/* 200:206 */     return encodedUrl;
/* 201:    */   }
/* 202:    */   
/* 203:    */   private static String decodeFileName(String encodedUrl)
/* 204:    */   {
/* 205:210 */     StringBuilder sb = new StringBuilder();
/* 206:211 */     for (int i = 1; i < encodedUrl.length(); i++)
/* 207:    */     {
/* 208:212 */       char c = encodedUrl.charAt(i);
/* 209:213 */       switch (c)
/* 210:    */       {
/* 211:    */       case '\001': 
/* 212:215 */         char driveLetter = encodedUrl.charAt(++i);
/* 213:216 */         if (driveLetter == '@') {
/* 214:217 */           sb.append("\\\\");
/* 215:    */         } else {
/* 216:220 */           sb.append(driveLetter).append(":");
/* 217:    */         }
/* 218:222 */         break;
/* 219:    */       case '\002': 
/* 220:224 */         sb.append(PATH_SEPERATOR);
/* 221:225 */         break;
/* 222:    */       case '\003': 
/* 223:227 */         sb.append(PATH_SEPERATOR);
/* 224:228 */         break;
/* 225:    */       case '\004': 
/* 226:230 */         sb.append("..").append(PATH_SEPERATOR);
/* 227:231 */         break;
/* 228:    */       case '\005': 
/* 229:234 */         logger.log(5, new Object[] { "Found unexpected key: ChLongVolume - IGNORING" });
/* 230:235 */         break;
/* 231:    */       case '\006': 
/* 232:    */       case '\007': 
/* 233:    */       case '\b': 
/* 234:239 */         logger.log(5, new Object[] { "EXCEL.EXE path unkown - using this directoy instead: ." });
/* 235:240 */         sb.append(".").append(PATH_SEPERATOR);
/* 236:241 */         break;
/* 237:    */       default: 
/* 238:243 */         sb.append(c);
/* 239:    */       }
/* 240:    */     }
/* 241:246 */     return sb.toString();
/* 242:    */   }
/* 243:    */   
/* 244:    */   public String[] getSheetNames()
/* 245:    */   {
/* 246:249 */     return (String[])this.field_3_sheet_names.clone();
/* 247:    */   }
/* 248:    */   
/* 249:    */   public void setURL(String pUrl)
/* 250:    */   {
/* 251:254 */     this.field_2_encoded_url = (this.field_2_encoded_url.substring(0, 1) + pUrl);
/* 252:    */   }
/* 253:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.SupBookRecord
 * JD-Core Version:    0.7.0.1
 */