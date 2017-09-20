/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import org.apache.poi.ss.formula.ptg.Area3DPtg;
/*   5:    */ import org.apache.poi.ss.formula.ptg.AreaPtg;
/*   6:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   7:    */ import org.apache.poi.ss.formula.ptg.Ref3DPtg;
/*   8:    */ import org.apache.poi.ss.formula.ptg.RefPtg;
/*   9:    */ import org.apache.poi.util.HexDump;
/*  10:    */ import org.apache.poi.util.LittleEndianInput;
/*  11:    */ import org.apache.poi.util.LittleEndianInputStream;
/*  12:    */ import org.apache.poi.util.LittleEndianOutput;
/*  13:    */ import org.apache.poi.util.POILogFactory;
/*  14:    */ import org.apache.poi.util.POILogger;
/*  15:    */ import org.apache.poi.util.RecordFormatException;
/*  16:    */ import org.apache.poi.util.StringUtil;
/*  17:    */ 
/*  18:    */ public final class EmbeddedObjectRefSubRecord
/*  19:    */   extends SubRecord
/*  20:    */   implements Cloneable
/*  21:    */ {
/*  22: 43 */   private static POILogger logger = POILogFactory.getLogger(EmbeddedObjectRefSubRecord.class);
/*  23:    */   public static final short sid = 9;
/*  24: 46 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*  25:    */   private int field_1_unknown_int;
/*  26:    */   private Ptg field_2_refPtg;
/*  27:    */   private byte[] field_2_unknownFormulaData;
/*  28:    */   private boolean field_3_unicode_flag;
/*  29:    */   private String field_4_ole_classname;
/*  30:    */   private Byte field_4_unknownByte;
/*  31:    */   private Integer field_5_stream_id;
/*  32:    */   private byte[] field_6_unknown;
/*  33:    */   
/*  34:    */   public EmbeddedObjectRefSubRecord()
/*  35:    */   {
/*  36: 67 */     this.field_2_unknownFormulaData = new byte[] { 2, 108, 106, 22, 1 };
/*  37: 68 */     this.field_6_unknown = EMPTY_BYTE_ARRAY;
/*  38: 69 */     this.field_4_ole_classname = null;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public short getSid()
/*  42:    */   {
/*  43: 73 */     return 9;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public EmbeddedObjectRefSubRecord(LittleEndianInput in, int size)
/*  47:    */   {
/*  48: 83 */     int streamIdOffset = in.readShort();
/*  49: 84 */     int remaining = size - 2;
/*  50:    */     
/*  51: 86 */     int dataLenAfterFormula = remaining - streamIdOffset;
/*  52: 87 */     int formulaSize = in.readUShort();
/*  53: 88 */     remaining -= 2;
/*  54: 89 */     this.field_1_unknown_int = in.readInt();
/*  55: 90 */     remaining -= 4;
/*  56: 91 */     byte[] formulaRawBytes = readRawData(in, formulaSize);
/*  57: 92 */     remaining -= formulaSize;
/*  58: 93 */     this.field_2_refPtg = readRefPtg(formulaRawBytes);
/*  59: 94 */     if (this.field_2_refPtg == null) {
/*  60: 98 */       this.field_2_unknownFormulaData = formulaRawBytes;
/*  61:    */     } else {
/*  62:100 */       this.field_2_unknownFormulaData = null;
/*  63:    */     }
/*  64:    */     int stringByteCount;
/*  65:104 */     if (remaining >= dataLenAfterFormula + 3)
/*  66:    */     {
/*  67:105 */       int tag = in.readByte();
/*  68:106 */       int stringByteCount = 1;
/*  69:107 */       if (tag != 3) {
/*  70:108 */         throw new RecordFormatException("Expected byte 0x03 here");
/*  71:    */       }
/*  72:110 */       int nChars = in.readUShort();
/*  73:111 */       stringByteCount += 2;
/*  74:112 */       if (nChars > 0)
/*  75:    */       {
/*  76:114 */         this.field_3_unicode_flag = ((in.readByte() & 0x1) != 0);
/*  77:115 */         stringByteCount++;
/*  78:116 */         if (this.field_3_unicode_flag)
/*  79:    */         {
/*  80:117 */           this.field_4_ole_classname = StringUtil.readUnicodeLE(in, nChars);
/*  81:118 */           stringByteCount += nChars * 2;
/*  82:    */         }
/*  83:    */         else
/*  84:    */         {
/*  85:120 */           this.field_4_ole_classname = StringUtil.readCompressedUnicode(in, nChars);
/*  86:121 */           stringByteCount += nChars;
/*  87:    */         }
/*  88:    */       }
/*  89:    */       else
/*  90:    */       {
/*  91:124 */         this.field_4_ole_classname = "";
/*  92:    */       }
/*  93:    */     }
/*  94:    */     else
/*  95:    */     {
/*  96:127 */       this.field_4_ole_classname = null;
/*  97:128 */       stringByteCount = 0;
/*  98:    */     }
/*  99:130 */     remaining -= stringByteCount;
/* 100:132 */     if ((stringByteCount + formulaSize) % 2 != 0)
/* 101:    */     {
/* 102:133 */       int b = in.readByte();
/* 103:134 */       remaining--;
/* 104:135 */       if ((this.field_2_refPtg != null) && (this.field_4_ole_classname == null)) {
/* 105:136 */         this.field_4_unknownByte = Byte.valueOf((byte)b);
/* 106:    */       }
/* 107:    */     }
/* 108:139 */     int nUnexpectedPadding = remaining - dataLenAfterFormula;
/* 109:141 */     if (nUnexpectedPadding > 0)
/* 110:    */     {
/* 111:142 */       logger.log(7, new Object[] { "Discarding " + nUnexpectedPadding + " unexpected padding bytes " });
/* 112:143 */       readRawData(in, nUnexpectedPadding);
/* 113:144 */       remaining -= nUnexpectedPadding;
/* 114:    */     }
/* 115:148 */     if (dataLenAfterFormula >= 4)
/* 116:    */     {
/* 117:149 */       this.field_5_stream_id = Integer.valueOf(in.readInt());
/* 118:150 */       remaining -= 4;
/* 119:    */     }
/* 120:    */     else
/* 121:    */     {
/* 122:152 */       this.field_5_stream_id = null;
/* 123:    */     }
/* 124:154 */     this.field_6_unknown = readRawData(in, remaining);
/* 125:    */   }
/* 126:    */   
/* 127:    */   private static Ptg readRefPtg(byte[] formulaRawBytes)
/* 128:    */   {
/* 129:158 */     LittleEndianInput in = new LittleEndianInputStream(new ByteArrayInputStream(formulaRawBytes));
/* 130:159 */     byte ptgSid = in.readByte();
/* 131:160 */     switch (ptgSid)
/* 132:    */     {
/* 133:    */     case 37: 
/* 134:161 */       return new AreaPtg(in);
/* 135:    */     case 59: 
/* 136:162 */       return new Area3DPtg(in);
/* 137:    */     case 36: 
/* 138:163 */       return new RefPtg(in);
/* 139:    */     case 58: 
/* 140:164 */       return new Ref3DPtg(in);
/* 141:    */     }
/* 142:166 */     return null;
/* 143:    */   }
/* 144:    */   
/* 145:    */   private static byte[] readRawData(LittleEndianInput in, int size)
/* 146:    */   {
/* 147:170 */     if (size < 0) {
/* 148:171 */       throw new IllegalArgumentException("Negative size (" + size + ")");
/* 149:    */     }
/* 150:173 */     if (size == 0) {
/* 151:174 */       return EMPTY_BYTE_ARRAY;
/* 152:    */     }
/* 153:176 */     byte[] result = new byte[size];
/* 154:177 */     in.readFully(result);
/* 155:178 */     return result;
/* 156:    */   }
/* 157:    */   
/* 158:    */   private int getStreamIDOffset(int formulaSize)
/* 159:    */   {
/* 160:182 */     int result = 6;
/* 161:183 */     result += formulaSize;
/* 162:    */     int stringLen;
/* 163:186 */     if (this.field_4_ole_classname == null)
/* 164:    */     {
/* 165:188 */       stringLen = 0;
/* 166:    */     }
/* 167:    */     else
/* 168:    */     {
/* 169:190 */       result += 3;
/* 170:191 */       int stringLen = this.field_4_ole_classname.length();
/* 171:192 */       if (stringLen > 0)
/* 172:    */       {
/* 173:193 */         result++;
/* 174:194 */         if (this.field_3_unicode_flag) {
/* 175:195 */           result += stringLen * 2;
/* 176:    */         } else {
/* 177:197 */           result += stringLen;
/* 178:    */         }
/* 179:    */       }
/* 180:    */     }
/* 181:202 */     if (result % 2 != 0) {
/* 182:203 */       result++;
/* 183:    */     }
/* 184:205 */     return result;
/* 185:    */   }
/* 186:    */   
/* 187:    */   private int getDataSize(int idOffset)
/* 188:    */   {
/* 189:210 */     int result = 2 + idOffset;
/* 190:211 */     if (this.field_5_stream_id != null) {
/* 191:212 */       result += 4;
/* 192:    */     }
/* 193:214 */     return result + this.field_6_unknown.length;
/* 194:    */   }
/* 195:    */   
/* 196:    */   protected int getDataSize()
/* 197:    */   {
/* 198:217 */     int formulaSize = this.field_2_refPtg == null ? this.field_2_unknownFormulaData.length : this.field_2_refPtg.getSize();
/* 199:218 */     int idOffset = getStreamIDOffset(formulaSize);
/* 200:219 */     return getDataSize(idOffset);
/* 201:    */   }
/* 202:    */   
/* 203:    */   public void serialize(LittleEndianOutput out)
/* 204:    */   {
/* 205:224 */     int formulaSize = this.field_2_refPtg == null ? this.field_2_unknownFormulaData.length : this.field_2_refPtg.getSize();
/* 206:225 */     int idOffset = getStreamIDOffset(formulaSize);
/* 207:226 */     int dataSize = getDataSize(idOffset);
/* 208:    */     
/* 209:    */ 
/* 210:229 */     out.writeShort(9);
/* 211:230 */     out.writeShort(dataSize);
/* 212:    */     
/* 213:232 */     out.writeShort(idOffset);
/* 214:233 */     out.writeShort(formulaSize);
/* 215:234 */     out.writeInt(this.field_1_unknown_int);
/* 216:    */     
/* 217:236 */     int pos = 12;
/* 218:238 */     if (this.field_2_refPtg == null) {
/* 219:239 */       out.write(this.field_2_unknownFormulaData);
/* 220:    */     } else {
/* 221:241 */       this.field_2_refPtg.write(out);
/* 222:    */     }
/* 223:243 */     pos += formulaSize;
/* 224:    */     int stringLen;
/* 225:246 */     if (this.field_4_ole_classname == null)
/* 226:    */     {
/* 227:248 */       stringLen = 0;
/* 228:    */     }
/* 229:    */     else
/* 230:    */     {
/* 231:250 */       out.writeByte(3);
/* 232:251 */       pos++;
/* 233:252 */       int stringLen = this.field_4_ole_classname.length();
/* 234:253 */       out.writeShort(stringLen);
/* 235:254 */       pos += 2;
/* 236:255 */       if (stringLen > 0)
/* 237:    */       {
/* 238:256 */         out.writeByte(this.field_3_unicode_flag ? 1 : 0);
/* 239:257 */         pos++;
/* 240:259 */         if (this.field_3_unicode_flag)
/* 241:    */         {
/* 242:260 */           StringUtil.putUnicodeLE(this.field_4_ole_classname, out);
/* 243:261 */           pos += stringLen * 2;
/* 244:    */         }
/* 245:    */         else
/* 246:    */         {
/* 247:263 */           StringUtil.putCompressedUnicode(this.field_4_ole_classname, out);
/* 248:264 */           pos += stringLen;
/* 249:    */         }
/* 250:    */       }
/* 251:    */     }
/* 252:270 */     switch (idOffset - (pos - 6))
/* 253:    */     {
/* 254:    */     case 1: 
/* 255:272 */       out.writeByte(this.field_4_unknownByte == null ? 0 : this.field_4_unknownByte.intValue());
/* 256:273 */       pos++;
/* 257:274 */       break;
/* 258:    */     case 0: 
/* 259:    */       break;
/* 260:    */     default: 
/* 261:278 */       throw new IllegalStateException("Bad padding calculation (" + idOffset + ", " + pos + ")");
/* 262:    */     }
/* 263:281 */     if (this.field_5_stream_id != null)
/* 264:    */     {
/* 265:282 */       out.writeInt(this.field_5_stream_id.intValue());
/* 266:283 */       pos += 4;
/* 267:    */     }
/* 268:285 */     out.write(this.field_6_unknown);
/* 269:    */   }
/* 270:    */   
/* 271:    */   public Integer getStreamId()
/* 272:    */   {
/* 273:297 */     return this.field_5_stream_id;
/* 274:    */   }
/* 275:    */   
/* 276:    */   public String getOLEClassName()
/* 277:    */   {
/* 278:301 */     return this.field_4_ole_classname;
/* 279:    */   }
/* 280:    */   
/* 281:    */   public byte[] getObjectData()
/* 282:    */   {
/* 283:305 */     return this.field_6_unknown;
/* 284:    */   }
/* 285:    */   
/* 286:    */   public EmbeddedObjectRefSubRecord clone()
/* 287:    */   {
/* 288:310 */     return this;
/* 289:    */   }
/* 290:    */   
/* 291:    */   public String toString()
/* 292:    */   {
/* 293:314 */     StringBuffer sb = new StringBuffer();
/* 294:315 */     sb.append("[ftPictFmla]\n");
/* 295:316 */     sb.append("    .f2unknown     = ").append(HexDump.intToHex(this.field_1_unknown_int)).append("\n");
/* 296:317 */     if (this.field_2_refPtg == null) {
/* 297:318 */       sb.append("    .f3unknown     = ").append(HexDump.toHex(this.field_2_unknownFormulaData)).append("\n");
/* 298:    */     } else {
/* 299:320 */       sb.append("    .formula       = ").append(this.field_2_refPtg).append("\n");
/* 300:    */     }
/* 301:322 */     if (this.field_4_ole_classname != null)
/* 302:    */     {
/* 303:323 */       sb.append("    .unicodeFlag   = ").append(this.field_3_unicode_flag).append("\n");
/* 304:324 */       sb.append("    .oleClassname  = ").append(this.field_4_ole_classname).append("\n");
/* 305:    */     }
/* 306:326 */     if (this.field_4_unknownByte != null) {
/* 307:327 */       sb.append("    .f4unknown   = ").append(HexDump.byteToHex(this.field_4_unknownByte.intValue())).append("\n");
/* 308:    */     }
/* 309:329 */     if (this.field_5_stream_id != null) {
/* 310:330 */       sb.append("    .streamId      = ").append(HexDump.intToHex(this.field_5_stream_id.intValue())).append("\n");
/* 311:    */     }
/* 312:332 */     if (this.field_6_unknown.length > 0) {
/* 313:333 */       sb.append("    .f7unknown     = ").append(HexDump.toHex(this.field_6_unknown)).append("\n");
/* 314:    */     }
/* 315:335 */     sb.append("[/ftPictFmla]");
/* 316:336 */     return sb.toString();
/* 317:    */   }
/* 318:    */   
/* 319:    */   public void setUnknownFormulaData(byte[] formularData)
/* 320:    */   {
/* 321:340 */     this.field_2_unknownFormulaData = formularData;
/* 322:    */   }
/* 323:    */   
/* 324:    */   public void setOleClassname(String oleClassname)
/* 325:    */   {
/* 326:344 */     this.field_4_ole_classname = oleClassname;
/* 327:    */   }
/* 328:    */   
/* 329:    */   public void setStorageId(int storageId)
/* 330:    */   {
/* 331:348 */     this.field_5_stream_id = Integer.valueOf(storageId);
/* 332:    */   }
/* 333:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.EmbeddedObjectRefSubRecord
 * JD-Core Version:    0.7.0.1
 */