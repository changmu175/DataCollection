/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import java.io.PrintWriter;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.util.BitField;
/*   7:    */ import org.apache.poi.util.BitFieldFactory;
/*   8:    */ import org.apache.poi.util.HexDump;
/*   9:    */ import org.apache.poi.util.Internal;
/*  10:    */ import org.apache.poi.util.LittleEndian;
/*  11:    */ 
/*  12:    */ public abstract class EscherRecord
/*  13:    */   implements Cloneable
/*  14:    */ {
/*  15: 36 */   private static final BitField fInstance = BitFieldFactory.getInstance(65520);
/*  16: 37 */   private static final BitField fVersion = BitFieldFactory.getInstance(15);
/*  17:    */   private short _options;
/*  18:    */   private short _recordId;
/*  19:    */   
/*  20:    */   protected int fillFields(byte[] data, EscherRecordFactory f)
/*  21:    */   {
/*  22: 60 */     return fillFields(data, 0, f);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public abstract int fillFields(byte[] paramArrayOfByte, int paramInt, EscherRecordFactory paramEscherRecordFactory);
/*  26:    */   
/*  27:    */   protected int readHeader(byte[] data, int offset)
/*  28:    */   {
/*  29: 85 */     this._options = LittleEndian.getShort(data, offset);
/*  30: 86 */     this._recordId = LittleEndian.getShort(data, offset + 2);
/*  31: 87 */     return LittleEndian.getInt(data, offset + 4);
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected static short readInstance(byte[] data, int offset)
/*  35:    */   {
/*  36: 97 */     short options = LittleEndian.getShort(data, offset);
/*  37: 98 */     return fInstance.getShortValue(options);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public boolean isContainerRecord()
/*  41:    */   {
/*  42:107 */     return getVersion() == 15;
/*  43:    */   }
/*  44:    */   
/*  45:    */   @Internal
/*  46:    */   public short getOptions()
/*  47:    */   {
/*  48:119 */     return this._options;
/*  49:    */   }
/*  50:    */   
/*  51:    */   @Internal
/*  52:    */   public void setOptions(short options)
/*  53:    */   {
/*  54:134 */     setVersion(fVersion.getShortValue(options));
/*  55:135 */     setInstance(fInstance.getShortValue(options));
/*  56:136 */     this._options = options;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public byte[] serialize()
/*  60:    */   {
/*  61:148 */     byte[] retval = new byte[getRecordSize()];
/*  62:    */     
/*  63:150 */     serialize(0, retval);
/*  64:151 */     return retval;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public int serialize(int offset, byte[] data)
/*  68:    */   {
/*  69:166 */     return serialize(offset, data, new NullEscherSerializationListener());
/*  70:    */   }
/*  71:    */   
/*  72:    */   public abstract int serialize(int paramInt, byte[] paramArrayOfByte, EscherSerializationListener paramEscherSerializationListener);
/*  73:    */   
/*  74:    */   public abstract int getRecordSize();
/*  75:    */   
/*  76:    */   public short getRecordId()
/*  77:    */   {
/*  78:196 */     return this._recordId;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setRecordId(short recordId)
/*  82:    */   {
/*  83:205 */     this._recordId = recordId;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public List<EscherRecord> getChildRecords()
/*  87:    */   {
/*  88:215 */     return Collections.emptyList();
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setChildRecords(List<EscherRecord> childRecords)
/*  92:    */   {
/*  93:224 */     throw new UnsupportedOperationException("This record does not support child records.");
/*  94:    */   }
/*  95:    */   
/*  96:    */   public EscherRecord clone()
/*  97:    */     throws CloneNotSupportedException
/*  98:    */   {
/*  99:236 */     return (EscherRecord)super.clone();
/* 100:    */   }
/* 101:    */   
/* 102:    */   public EscherRecord getChild(int index)
/* 103:    */   {
/* 104:246 */     return (EscherRecord)getChildRecords().get(index);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void display(PrintWriter w, int indent)
/* 108:    */   {
/* 109:258 */     for (int i = 0; i < indent * 4; i++) {
/* 110:259 */       w.print(' ');
/* 111:    */     }
/* 112:261 */     w.println(getRecordName());
/* 113:    */   }
/* 114:    */   
/* 115:    */   public abstract String getRecordName();
/* 116:    */   
/* 117:    */   public short getInstance()
/* 118:    */   {
/* 119:278 */     return fInstance.getShortValue(this._options);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setInstance(short value)
/* 123:    */   {
/* 124:288 */     this._options = fInstance.setShortValue(this._options, value);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public short getVersion()
/* 128:    */   {
/* 129:298 */     return fVersion.getShortValue(this._options);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void setVersion(short value)
/* 133:    */   {
/* 134:308 */     this._options = fVersion.setShortValue(this._options, value);
/* 135:    */   }
/* 136:    */   
/* 137:    */   public String toXml()
/* 138:    */   {
/* 139:312 */     return toXml("");
/* 140:    */   }
/* 141:    */   
/* 142:    */   public final String toXml(String tab)
/* 143:    */   {
/* 144:320 */     String nl = System.getProperty("line.separator");
/* 145:321 */     String clsNm = getClass().getSimpleName();
/* 146:322 */     StringBuilder sb = new StringBuilder(1000);
/* 147:323 */     sb.append(tab).append("<").append(clsNm).append(" recordId=\"0x").append(HexDump.toHex(getRecordId())).append("\" version=\"0x").append(HexDump.toHex(getVersion())).append("\" instance=\"0x").append(HexDump.toHex(getInstance())).append("\" options=\"0x").append(HexDump.toHex(getOptions())).append("\" recordSize=\"").append(getRecordSize());
/* 148:    */     
/* 149:    */ 
/* 150:    */ 
/* 151:    */ 
/* 152:    */ 
/* 153:329 */     Object[][] attrList = getAttributeMap();
/* 154:330 */     if ((attrList == null) || (attrList.length == 0))
/* 155:    */     {
/* 156:331 */       sb.append("\" />").append(nl);
/* 157:    */     }
/* 158:    */     else
/* 159:    */     {
/* 160:333 */       sb.append("\">").append(nl);
/* 161:334 */       String childTab = tab + "   ";
/* 162:335 */       for (Object[] attrs : attrList)
/* 163:    */       {
/* 164:336 */         String tagName = capitalizeAndTrim((String)attrs[0]);
/* 165:337 */         boolean hasValue = false;
/* 166:338 */         boolean lastChildComplex = false;
/* 167:339 */         for (int i = 0; i < attrs.length; i += 2)
/* 168:    */         {
/* 169:340 */           Object value = attrs[(i + 1)];
/* 170:341 */           if (value != null)
/* 171:    */           {
/* 172:345 */             if (!hasValue) {
/* 173:347 */               sb.append(childTab).append("<").append(tagName).append(">");
/* 174:    */             }
/* 175:350 */             String optName = capitalizeAndTrim((String)attrs[(i + 0)]);
/* 176:351 */             if (i > 0) {
/* 177:352 */               sb.append(nl).append(childTab).append("  <").append(optName).append(">");
/* 178:    */             }
/* 179:354 */             lastChildComplex = appendValue(sb, value, true, childTab);
/* 180:355 */             if (i > 0) {
/* 181:356 */               sb.append(nl).append(childTab).append("  </").append(optName).append(">");
/* 182:    */             }
/* 183:358 */             hasValue = true;
/* 184:    */           }
/* 185:    */         }
/* 186:360 */         if (hasValue)
/* 187:    */         {
/* 188:361 */           if (lastChildComplex) {
/* 189:362 */             sb.append(nl).append(childTab);
/* 190:    */           }
/* 191:364 */           sb.append("</").append(tagName).append(">").append(nl);
/* 192:    */         }
/* 193:    */       }
/* 194:367 */       sb.append(tab).append("</").append(clsNm).append(">");
/* 195:    */     }
/* 196:369 */     return sb.toString();
/* 197:    */   }
/* 198:    */   
/* 199:    */   public final String toString()
/* 200:    */   {
/* 201:374 */     String nl = System.getProperty("line.separator");
/* 202:375 */     StringBuilder sb = new StringBuilder(1000);
/* 203:376 */     sb.append(getClass().getName()).append(" (").append(getRecordName()).append("):").append(nl).append("  RecordId: 0x").append(HexDump.toHex(getRecordId())).append(nl).append("  Version: 0x").append(HexDump.toHex(getVersion())).append(nl).append("  Instance: 0x").append(HexDump.toHex(getInstance())).append(nl).append("  Options: 0x").append(HexDump.toHex(getOptions())).append(nl).append("  Record Size: ").append(getRecordSize());
/* 204:    */     
/* 205:    */ 
/* 206:    */ 
/* 207:    */ 
/* 208:    */ 
/* 209:    */ 
/* 210:383 */     Object[][] attrList = getAttributeMap();
/* 211:384 */     if ((attrList != null) && (attrList.length > 0))
/* 212:    */     {
/* 213:385 */       String childTab = "  ";
/* 214:386 */       for (Object[] attrs : attrList) {
/* 215:387 */         for (int i = 0; i < attrs.length; i += 2)
/* 216:    */         {
/* 217:388 */           Object value = attrs[(i + 1)];
/* 218:389 */           if (value != null)
/* 219:    */           {
/* 220:393 */             String name = (String)attrs[(i + 0)];
/* 221:394 */             sb.append(nl).append(childTab).append(name).append(": ");
/* 222:395 */             appendValue(sb, value, false, childTab);
/* 223:    */           }
/* 224:    */         }
/* 225:    */       }
/* 226:    */     }
/* 227:400 */     return sb.toString();
/* 228:    */   }
/* 229:    */   
/* 230:    */   private static boolean appendValue(StringBuilder sb, Object value, boolean toXML, String childTab)
/* 231:    */   {
/* 232:407 */     String nl = System.getProperty("line.separator");
/* 233:408 */     boolean isComplex = false;
/* 234:409 */     if ((value instanceof String))
/* 235:    */     {
/* 236:410 */       if (toXML) {
/* 237:411 */         escapeXML((String)value, sb);
/* 238:    */       } else {
/* 239:413 */         sb.append((String)value);
/* 240:    */       }
/* 241:    */     }
/* 242:415 */     else if ((value instanceof Byte))
/* 243:    */     {
/* 244:416 */       sb.append("0x").append(HexDump.toHex(((Byte)value).byteValue()));
/* 245:    */     }
/* 246:417 */     else if ((value instanceof Short))
/* 247:    */     {
/* 248:418 */       sb.append("0x").append(HexDump.toHex(((Short)value).shortValue()));
/* 249:    */     }
/* 250:419 */     else if ((value instanceof Integer))
/* 251:    */     {
/* 252:420 */       sb.append("0x").append(HexDump.toHex(((Integer)value).intValue()));
/* 253:    */     }
/* 254:421 */     else if ((value instanceof byte[]))
/* 255:    */     {
/* 256:422 */       sb.append(nl).append(HexDump.toHex((byte[])value, 32).replaceAll("(?m)^", childTab + "   "));
/* 257:    */     }
/* 258:423 */     else if ((value instanceof Boolean))
/* 259:    */     {
/* 260:424 */       sb.append(((Boolean)value).booleanValue());
/* 261:    */     }
/* 262:425 */     else if ((value instanceof EscherRecord))
/* 263:    */     {
/* 264:426 */       EscherRecord er = (EscherRecord)value;
/* 265:427 */       if (toXML) {
/* 266:428 */         sb.append(nl).append(er.toXml(childTab + "    "));
/* 267:    */       } else {
/* 268:430 */         sb.append(er.toString().replaceAll("(?m)^", childTab));
/* 269:    */       }
/* 270:432 */       isComplex = true;
/* 271:    */     }
/* 272:433 */     else if ((value instanceof EscherProperty))
/* 273:    */     {
/* 274:434 */       EscherProperty ep = (EscherProperty)value;
/* 275:435 */       if (toXML) {
/* 276:436 */         sb.append(nl).append(ep.toXml(childTab + "  "));
/* 277:    */       } else {
/* 278:438 */         sb.append(ep.toString().replaceAll("(?m)^", childTab));
/* 279:    */       }
/* 280:440 */       isComplex = true;
/* 281:    */     }
/* 282:    */     else
/* 283:    */     {
/* 284:442 */       throw new IllegalArgumentException("unknown attribute type " + value.getClass().getSimpleName());
/* 285:    */     }
/* 286:444 */     return isComplex;
/* 287:    */   }
/* 288:    */   
/* 289:    */   @Internal
/* 290:    */   protected abstract Object[][] getAttributeMap();
/* 291:    */   
/* 292:    */   private static String capitalizeAndTrim(String str)
/* 293:    */   {
/* 294:463 */     if ((str == null) || (str.length() == 0)) {
/* 295:464 */       return str;
/* 296:    */     }
/* 297:467 */     StringBuilder sb = new StringBuilder(str.length());
/* 298:468 */     boolean capitalizeNext = true;
/* 299:469 */     for (char ch : str.toCharArray()) {
/* 300:470 */       if (!Character.isLetterOrDigit(ch))
/* 301:    */       {
/* 302:471 */         capitalizeNext = true;
/* 303:    */       }
/* 304:    */       else
/* 305:    */       {
/* 306:475 */         if (capitalizeNext)
/* 307:    */         {
/* 308:476 */           if (!Character.isLetter(ch)) {
/* 309:477 */             sb.append('_');
/* 310:    */           } else {
/* 311:479 */             ch = Character.toTitleCase(ch);
/* 312:    */           }
/* 313:481 */           capitalizeNext = false;
/* 314:    */         }
/* 315:483 */         sb.append(ch);
/* 316:    */       }
/* 317:    */     }
/* 318:486 */     return sb.toString();
/* 319:    */   }
/* 320:    */   
/* 321:    */   private static void escapeXML(String s, StringBuilder out)
/* 322:    */   {
/* 323:490 */     if ((s == null) || (s.isEmpty())) {
/* 324:491 */       return;
/* 325:    */     }
/* 326:493 */     for (char c : s.toCharArray()) {
/* 327:494 */       if ((c > '') || (c == '"') || (c == '<') || (c == '>') || (c == '&'))
/* 328:    */       {
/* 329:495 */         out.append("&#");
/* 330:496 */         out.append(c);
/* 331:497 */         out.append(';');
/* 332:    */       }
/* 333:    */       else
/* 334:    */       {
/* 335:499 */         out.append(c);
/* 336:    */       }
/* 337:    */     }
/* 338:    */   }
/* 339:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherRecord
 * JD-Core Version:    0.7.0.1
 */