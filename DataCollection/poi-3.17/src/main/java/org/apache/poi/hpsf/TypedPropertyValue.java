/*   1:    */ package org.apache.poi.hpsf;
/*   2:    */ 
/*   3:    */ import java.math.BigInteger;
/*   4:    */ import org.apache.poi.util.Internal;
/*   5:    */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*   6:    */ import org.apache.poi.util.POILogFactory;
/*   7:    */ import org.apache.poi.util.POILogger;
/*   8:    */ 
/*   9:    */ @Internal
/*  10:    */ class TypedPropertyValue
/*  11:    */ {
/*  12: 31 */   private static final POILogger LOG = POILogFactory.getLogger(TypedPropertyValue.class);
/*  13:    */   private int _type;
/*  14:    */   private Object _value;
/*  15:    */   
/*  16:    */   TypedPropertyValue(int type, Object value)
/*  17:    */   {
/*  18: 37 */     this._type = type;
/*  19: 38 */     this._value = value;
/*  20:    */   }
/*  21:    */   
/*  22:    */   Object getValue()
/*  23:    */   {
/*  24: 42 */     return this._value;
/*  25:    */   }
/*  26:    */   
/*  27:    */   void read(LittleEndianByteArrayInputStream lei)
/*  28:    */   {
/*  29: 46 */     this._type = lei.readShort();
/*  30: 47 */     short padding = lei.readShort();
/*  31: 48 */     if (padding != 0) {
/*  32: 49 */       LOG.log(5, new Object[] { "TypedPropertyValue padding at offset " + lei.getReadIndex() + " MUST be 0, but it's value is " + padding });
/*  33:    */     }
/*  34: 52 */     readValue(lei);
/*  35:    */   }
/*  36:    */   
/*  37:    */   void readValue(LittleEndianByteArrayInputStream lei)
/*  38:    */   {
/*  39: 56 */     switch (this._type)
/*  40:    */     {
/*  41:    */     case 0: 
/*  42:    */     case 1: 
/*  43: 59 */       this._value = null;
/*  44: 60 */       break;
/*  45:    */     case 16: 
/*  46: 63 */       this._value = Byte.valueOf(lei.readByte());
/*  47: 64 */       break;
/*  48:    */     case 17: 
/*  49: 67 */       this._value = Integer.valueOf(lei.readUByte());
/*  50: 68 */       break;
/*  51:    */     case 2: 
/*  52: 71 */       this._value = Short.valueOf(lei.readShort());
/*  53: 72 */       break;
/*  54:    */     case 18: 
/*  55: 75 */       this._value = Integer.valueOf(lei.readUShort());
/*  56: 76 */       break;
/*  57:    */     case 3: 
/*  58:    */     case 22: 
/*  59: 80 */       this._value = Integer.valueOf(lei.readInt());
/*  60: 81 */       break;
/*  61:    */     case 10: 
/*  62:    */     case 19: 
/*  63:    */     case 23: 
/*  64: 86 */       this._value = Long.valueOf(lei.readUInt());
/*  65: 87 */       break;
/*  66:    */     case 20: 
/*  67: 90 */       this._value = Long.valueOf(lei.readLong());
/*  68: 91 */       break;
/*  69:    */     case 21: 
/*  70: 94 */       byte[] biBytesLE = new byte[8];
/*  71: 95 */       lei.readFully(biBytesLE);
/*  72:    */       
/*  73:    */ 
/*  74: 98 */       byte[] biBytesBE = new byte[9];
/*  75: 99 */       int i = biBytesLE.length;
/*  76:100 */       for (byte b : biBytesLE)
/*  77:    */       {
/*  78:101 */         if (i <= 8) {
/*  79:102 */           biBytesBE[i] = b;
/*  80:    */         }
/*  81:104 */         i--;
/*  82:    */       }
/*  83:106 */       this._value = new BigInteger(biBytesBE);
/*  84:107 */       break;
/*  85:    */     case 4: 
/*  86:112 */       this._value = Float.valueOf(Float.intBitsToFloat(lei.readInt()));
/*  87:113 */       break;
/*  88:    */     case 5: 
/*  89:116 */       this._value = Double.valueOf(lei.readDouble());
/*  90:117 */       break;
/*  91:    */     case 6: 
/*  92:120 */       Currency cur = new Currency();
/*  93:121 */       cur.read(lei);
/*  94:122 */       this._value = cur;
/*  95:123 */       break;
/*  96:    */     case 7: 
/*  97:127 */       Date date = new Date();
/*  98:128 */       date.read(lei);
/*  99:129 */       this._value = date;
/* 100:130 */       break;
/* 101:    */     case 8: 
/* 102:    */     case 30: 
/* 103:134 */       CodePageString cps = new CodePageString();
/* 104:135 */       cps.read(lei);
/* 105:136 */       this._value = cps;
/* 106:137 */       break;
/* 107:    */     case 11: 
/* 108:140 */       VariantBool vb = new VariantBool();
/* 109:141 */       vb.read(lei);
/* 110:142 */       this._value = vb;
/* 111:143 */       break;
/* 112:    */     case 14: 
/* 113:146 */       Decimal dec = new Decimal();
/* 114:147 */       dec.read(lei);
/* 115:148 */       this._value = dec;
/* 116:149 */       break;
/* 117:    */     case 31: 
/* 118:152 */       UnicodeString us = new UnicodeString();
/* 119:153 */       us.read(lei);
/* 120:154 */       this._value = us;
/* 121:155 */       break;
/* 122:    */     case 64: 
/* 123:158 */       Filetime ft = new Filetime();
/* 124:159 */       ft.read(lei);
/* 125:160 */       this._value = ft;
/* 126:161 */       break;
/* 127:    */     case 65: 
/* 128:    */     case 70: 
/* 129:165 */       Blob blob = new Blob();
/* 130:166 */       blob.read(lei);
/* 131:167 */       this._value = blob;
/* 132:168 */       break;
/* 133:    */     case 66: 
/* 134:    */     case 67: 
/* 135:    */     case 68: 
/* 136:    */     case 69: 
/* 137:174 */       IndirectPropertyName ipn = new IndirectPropertyName();
/* 138:175 */       ipn.read(lei);
/* 139:176 */       this._value = ipn;
/* 140:177 */       break;
/* 141:    */     case 71: 
/* 142:180 */       ClipboardData cd = new ClipboardData();
/* 143:181 */       cd.read(lei);
/* 144:182 */       this._value = cd;
/* 145:183 */       break;
/* 146:    */     case 72: 
/* 147:186 */       GUID guid = new GUID();
/* 148:187 */       guid.read(lei);
/* 149:188 */       this._value = lei;
/* 150:189 */       break;
/* 151:    */     case 73: 
/* 152:192 */       VersionedStream vs = new VersionedStream();
/* 153:193 */       vs.read(lei);
/* 154:194 */       this._value = vs;
/* 155:195 */       break;
/* 156:    */     case 4098: 
/* 157:    */     case 4099: 
/* 158:    */     case 4100: 
/* 159:    */     case 4101: 
/* 160:    */     case 4102: 
/* 161:    */     case 4103: 
/* 162:    */     case 4104: 
/* 163:    */     case 4106: 
/* 164:    */     case 4107: 
/* 165:    */     case 4108: 
/* 166:    */     case 4112: 
/* 167:    */     case 4113: 
/* 168:    */     case 4114: 
/* 169:    */     case 4115: 
/* 170:    */     case 4116: 
/* 171:    */     case 4117: 
/* 172:    */     case 4126: 
/* 173:    */     case 4127: 
/* 174:    */     case 4160: 
/* 175:    */     case 4167: 
/* 176:    */     case 4168: 
/* 177:218 */       Vector vec = new Vector((short)(this._type & 0xFFF));
/* 178:219 */       vec.read(lei);
/* 179:220 */       this._value = vec;
/* 180:221 */       break;
/* 181:    */     case 8194: 
/* 182:    */     case 8195: 
/* 183:    */     case 8196: 
/* 184:    */     case 8197: 
/* 185:    */     case 8198: 
/* 186:    */     case 8199: 
/* 187:    */     case 8200: 
/* 188:    */     case 8202: 
/* 189:    */     case 8203: 
/* 190:    */     case 8204: 
/* 191:    */     case 8206: 
/* 192:    */     case 8208: 
/* 193:    */     case 8209: 
/* 194:    */     case 8210: 
/* 195:    */     case 8211: 
/* 196:    */     case 8214: 
/* 197:    */     case 8215: 
/* 198:240 */       Array arr = new Array();
/* 199:241 */       arr.read(lei);
/* 200:242 */       this._value = arr;
/* 201:243 */       break;
/* 202:    */     default: 
/* 203:246 */       String msg = "Unknown (possibly, incorrect) TypedPropertyValue type: " + this._type;
/* 204:247 */       throw new UnsupportedOperationException(msg);
/* 205:    */     }
/* 206:    */   }
/* 207:    */   
/* 208:    */   static void skipPadding(LittleEndianByteArrayInputStream lei)
/* 209:    */   {
/* 210:252 */     int offset = lei.getReadIndex();
/* 211:253 */     int skipBytes = 4 - (offset & 0x3) & 0x3;
/* 212:254 */     for (int i = 0; i < skipBytes; i++)
/* 213:    */     {
/* 214:255 */       lei.mark(1);
/* 215:256 */       int b = lei.read();
/* 216:257 */       if ((b == -1) || (b != 0))
/* 217:    */       {
/* 218:258 */         lei.reset();
/* 219:259 */         break;
/* 220:    */       }
/* 221:    */     }
/* 222:    */   }
/* 223:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.TypedPropertyValue
 * JD-Core Version:    0.7.0.1
 */