/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import org.apache.poi.util.HexDump;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ 
/*   7:    */ public final class UnknownRecord
/*   8:    */   extends StandardRecord
/*   9:    */ {
/*  10:    */   public static final int PRINTSIZE_0033 = 51;
/*  11:    */   public static final int PLS_004D = 77;
/*  12:    */   public static final int SHEETPR_0081 = 129;
/*  13:    */   public static final int SORT_0090 = 144;
/*  14:    */   public static final int STANDARDWIDTH_0099 = 153;
/*  15:    */   public static final int SCL_00A0 = 160;
/*  16:    */   public static final int BITMAP_00E9 = 233;
/*  17:    */   public static final int PHONETICPR_00EF = 239;
/*  18:    */   public static final int LABELRANGES_015F = 351;
/*  19:    */   public static final int QUICKTIP_0800 = 2048;
/*  20:    */   public static final int SHEETEXT_0862 = 2146;
/*  21:    */   public static final int SHEETPROTECTION_0867 = 2151;
/*  22:    */   public static final int HEADER_FOOTER_089C = 2204;
/*  23:    */   public static final int CODENAME_1BA = 442;
/*  24:    */   public static final int PLV_MAC = 2248;
/*  25:    */   private int _sid;
/*  26:    */   private byte[] _rawData;
/*  27:    */   
/*  28:    */   public UnknownRecord(int id, byte[] data)
/*  29:    */   {
/*  30: 70 */     this._sid = (id & 0xFFFF);
/*  31: 71 */     this._rawData = data;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public UnknownRecord(RecordInputStream in)
/*  35:    */   {
/*  36: 81 */     this._sid = in.getSid();
/*  37: 82 */     this._rawData = in.readRemainder();
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void serialize(LittleEndianOutput out)
/*  41:    */   {
/*  42: 97 */     out.write(this._rawData);
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected int getDataSize()
/*  46:    */   {
/*  47:102 */     return this._rawData.length;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public String toString()
/*  51:    */   {
/*  52:110 */     String biffName = getBiffName(this._sid);
/*  53:111 */     if (biffName == null) {
/*  54:112 */       biffName = "UNKNOWNRECORD";
/*  55:    */     }
/*  56:114 */     StringBuffer sb = new StringBuffer();
/*  57:    */     
/*  58:116 */     sb.append("[").append(biffName).append("] (0x");
/*  59:117 */     sb.append(Integer.toHexString(this._sid).toUpperCase(Locale.ROOT) + ")\n");
/*  60:118 */     if (this._rawData.length > 0) {
/*  61:119 */       sb.append("  rawData=").append(HexDump.toHex(this._rawData)).append("\n");
/*  62:    */     }
/*  63:121 */     sb.append("[/").append(biffName).append("]\n");
/*  64:122 */     return sb.toString();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public short getSid()
/*  68:    */   {
/*  69:127 */     return (short)this._sid;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static String getBiffName(int sid)
/*  73:    */   {
/*  74:139 */     switch (sid)
/*  75:    */     {
/*  76:    */     case 51: 
/*  77:140 */       return "PRINTSIZE";
/*  78:    */     case 77: 
/*  79:141 */       return "PLS";
/*  80:    */     case 80: 
/*  81:142 */       return "DCON";
/*  82:    */     case 127: 
/*  83:143 */       return "IMDATA";
/*  84:    */     case 129: 
/*  85:144 */       return "SHEETPR";
/*  86:    */     case 144: 
/*  87:145 */       return "SORT";
/*  88:    */     case 148: 
/*  89:146 */       return "LHRECORD";
/*  90:    */     case 153: 
/*  91:147 */       return "STANDARDWIDTH";
/*  92:    */     case 160: 
/*  93:148 */       return "SCL";
/*  94:    */     case 174: 
/*  95:149 */       return "SCENMAN";
/*  96:    */     case 178: 
/*  97:151 */       return "SXVI";
/*  98:    */     case 180: 
/*  99:152 */       return "SXIVD";
/* 100:    */     case 181: 
/* 101:153 */       return "SXLI";
/* 102:    */     case 211: 
/* 103:155 */       return "OBPROJ";
/* 104:    */     case 220: 
/* 105:156 */       return "PARAMQRY";
/* 106:    */     case 222: 
/* 107:157 */       return "OLESIZE";
/* 108:    */     case 233: 
/* 109:158 */       return "BITMAP";
/* 110:    */     case 239: 
/* 111:159 */       return "PHONETICPR";
/* 112:    */     case 241: 
/* 113:160 */       return "SXEX";
/* 114:    */     case 351: 
/* 115:162 */       return "LABELRANGES";
/* 116:    */     case 442: 
/* 117:163 */       return "CODENAME";
/* 118:    */     case 425: 
/* 119:164 */       return "USERBVIEW";
/* 120:    */     case 429: 
/* 121:165 */       return "QSI";
/* 122:    */     case 448: 
/* 123:167 */       return "EXCEL9FILE";
/* 124:    */     case 2050: 
/* 125:169 */       return "QSISXTAG";
/* 126:    */     case 2051: 
/* 127:170 */       return "DBQUERYEXT";
/* 128:    */     case 2053: 
/* 129:171 */       return "TXTQUERY";
/* 130:    */     case 2064: 
/* 131:172 */       return "SXVIEWEX9";
/* 132:    */     case 2066: 
/* 133:174 */       return "CONTINUEFRT";
/* 134:    */     case 2048: 
/* 135:175 */       return "QUICKTIP";
/* 136:    */     case 2146: 
/* 137:176 */       return "SHEETEXT";
/* 138:    */     case 2147: 
/* 139:177 */       return "BOOKEXT";
/* 140:    */     case 2148: 
/* 141:178 */       return "SXADDL";
/* 142:    */     case 2151: 
/* 143:179 */       return "SHEETPROTECTION";
/* 144:    */     case 2155: 
/* 145:180 */       return "DATALABEXTCONTENTS";
/* 146:    */     case 2156: 
/* 147:181 */       return "CELLWATCH";
/* 148:    */     case 2162: 
/* 149:182 */       return "SHARED FEATURE v11";
/* 150:    */     case 2164: 
/* 151:183 */       return "DROPDOWNOBJIDS";
/* 152:    */     case 2166: 
/* 153:184 */       return "DCONN";
/* 154:    */     case 2168: 
/* 155:185 */       return "SHARED FEATURE v12";
/* 156:    */     case 2171: 
/* 157:186 */       return "CFEX";
/* 158:    */     case 2172: 
/* 159:187 */       return "XFCRC";
/* 160:    */     case 2173: 
/* 161:188 */       return "XFEXT";
/* 162:    */     case 2175: 
/* 163:189 */       return "CONTINUEFRT12";
/* 164:    */     case 2187: 
/* 165:190 */       return "PLV";
/* 166:    */     case 2188: 
/* 167:191 */       return "COMPAT12";
/* 168:    */     case 2189: 
/* 169:192 */       return "DXF";
/* 170:    */     case 2194: 
/* 171:193 */       return "STYLEEXT";
/* 172:    */     case 2198: 
/* 173:194 */       return "THEME";
/* 174:    */     case 2199: 
/* 175:195 */       return "GUIDTYPELIB";
/* 176:    */     case 2202: 
/* 177:196 */       return "MTRSETTINGS";
/* 178:    */     case 2203: 
/* 179:197 */       return "COMPRESSPICTURES";
/* 180:    */     case 2204: 
/* 181:198 */       return "HEADERFOOTER";
/* 182:    */     case 2205: 
/* 183:199 */       return "CRTLAYOUT12";
/* 184:    */     case 2206: 
/* 185:200 */       return "CRTMLFRT";
/* 186:    */     case 2207: 
/* 187:201 */       return "CRTMLFRTCONTINUE";
/* 188:    */     case 2209: 
/* 189:202 */       return "SHAPEPROPSSTREAM";
/* 190:    */     case 2211: 
/* 191:203 */       return "FORCEFULLCALCULATION";
/* 192:    */     case 2212: 
/* 193:204 */       return "SHAPEPROPSSTREAM";
/* 194:    */     case 2213: 
/* 195:205 */       return "TEXTPROPSSTREAM";
/* 196:    */     case 2214: 
/* 197:206 */       return "RICHTEXTSTREAM";
/* 198:    */     case 2215: 
/* 199:207 */       return "CRTLAYOUT12A";
/* 200:    */     case 2248: 
/* 201:209 */       return "PLV{Mac Excel}";
/* 202:    */     case 4097: 
/* 203:211 */       return "UNITS";
/* 204:    */     case 4102: 
/* 205:212 */       return "CHARTDATAFORMAT";
/* 206:    */     case 4103: 
/* 207:213 */       return "CHARTLINEFORMAT";
/* 208:    */     }
/* 209:215 */     if (isObservedButUnknown(sid)) {
/* 210:216 */       return "UNKNOWN-" + Integer.toHexString(sid).toUpperCase(Locale.ROOT);
/* 211:    */     }
/* 212:219 */     return null;
/* 213:    */   }
/* 214:    */   
/* 215:    */   private static boolean isObservedButUnknown(int sid)
/* 216:    */   {
/* 217:227 */     switch (sid)
/* 218:    */     {
/* 219:    */     case 51: 
/* 220:    */     case 52: 
/* 221:    */     case 445: 
/* 222:    */     case 450: 
/* 223:    */     case 4105: 
/* 224:    */     case 4106: 
/* 225:    */     case 4107: 
/* 226:    */     case 4108: 
/* 227:    */     case 4116: 
/* 228:    */     case 4119: 
/* 229:    */     case 4120: 
/* 230:    */     case 4121: 
/* 231:    */     case 4122: 
/* 232:    */     case 4123: 
/* 233:    */     case 4125: 
/* 234:    */     case 4126: 
/* 235:    */     case 4127: 
/* 236:    */     case 4128: 
/* 237:    */     case 4129: 
/* 238:    */     case 4130: 
/* 239:    */     case 4132: 
/* 240:    */     case 4133: 
/* 241:    */     case 4134: 
/* 242:    */     case 4135: 
/* 243:    */     case 4146: 
/* 244:    */     case 4147: 
/* 245:    */     case 4148: 
/* 246:    */     case 4149: 
/* 247:    */     case 4154: 
/* 248:    */     case 4161: 
/* 249:    */     case 4163: 
/* 250:    */     case 4164: 
/* 251:    */     case 4165: 
/* 252:    */     case 4166: 
/* 253:    */     case 4170: 
/* 254:    */     case 4171: 
/* 255:    */     case 4174: 
/* 256:    */     case 4175: 
/* 257:    */     case 4177: 
/* 258:    */     case 4188: 
/* 259:    */     case 4189: 
/* 260:    */     case 4191: 
/* 261:    */     case 4192: 
/* 262:    */     case 4194: 
/* 263:    */     case 4195: 
/* 264:    */     case 4196: 
/* 265:    */     case 4197: 
/* 266:    */     case 4198: 
/* 267:284 */       return true;
/* 268:    */     }
/* 269:286 */     return false;
/* 270:    */   }
/* 271:    */   
/* 272:    */   public Object clone()
/* 273:    */   {
/* 274:292 */     return this;
/* 275:    */   }
/* 276:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.UnknownRecord
 * JD-Core Version:    0.7.0.1
 */