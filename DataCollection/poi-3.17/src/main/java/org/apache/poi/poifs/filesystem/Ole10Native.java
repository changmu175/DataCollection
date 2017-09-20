/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import org.apache.poi.util.LittleEndian;
/*   7:    */ import org.apache.poi.util.LittleEndianOutputStream;
/*   8:    */ import org.apache.poi.util.StringUtil;
/*   9:    */ 
/*  10:    */ public class Ole10Native
/*  11:    */ {
/*  12:    */   public static final String OLE10_NATIVE = "\001Ole10Native";
/*  13:    */   protected static final String ISO1 = "ISO-8859-1";
/*  14:    */   private int totalSize;
/*  15: 42 */   private short flags1 = 2;
/*  16:    */   private String label;
/*  17:    */   private String fileName;
/*  18: 45 */   private short flags2 = 0;
/*  19: 46 */   private short unknown1 = 3;
/*  20:    */   private String command;
/*  21:    */   private byte[] dataBuffer;
/*  22: 49 */   private short flags3 = 0;
/*  23:    */   private EncodingMode mode;
/*  24:    */   
/*  25:    */   private static enum EncodingMode
/*  26:    */   {
/*  27: 58 */     parsed,  unparsed,  compact;
/*  28:    */     
/*  29:    */     private EncodingMode() {}
/*  30:    */   }
/*  31:    */   
/*  32:    */   public static Ole10Native createFromEmbeddedOleObject(POIFSFileSystem poifs)
/*  33:    */     throws IOException, Ole10NativeException
/*  34:    */   {
/*  35: 84 */     return createFromEmbeddedOleObject(poifs.getRoot());
/*  36:    */   }
/*  37:    */   
/*  38:    */   public static Ole10Native createFromEmbeddedOleObject(DirectoryNode directory)
/*  39:    */     throws IOException, Ole10NativeException
/*  40:    */   {
/*  41: 98 */     DocumentEntry nativeEntry = (DocumentEntry)directory.getEntry("\001Ole10Native");
/*  42:    */     
/*  43:100 */     byte[] data = new byte[nativeEntry.getSize()];
/*  44:101 */     int readBytes = directory.createDocumentInputStream(nativeEntry).read(data);
/*  45:102 */     assert (readBytes == data.length);
/*  46:    */     
/*  47:104 */     return new Ole10Native(data, 0);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public Ole10Native(String label, String filename, String command, byte[] data)
/*  51:    */   {
/*  52:111 */     setLabel(label);
/*  53:112 */     setFileName(filename);
/*  54:113 */     setCommand(command);
/*  55:114 */     setDataBuffer(data);
/*  56:115 */     this.mode = EncodingMode.parsed;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public Ole10Native(byte[] data, int offset)
/*  60:    */     throws Ole10NativeException
/*  61:    */   {
/*  62:126 */     int ofs = offset;
/*  63:128 */     if (data.length < offset + 2) {
/*  64:129 */       throw new Ole10NativeException("data is too small");
/*  65:    */     }
/*  66:132 */     this.totalSize = LittleEndian.getInt(data, ofs);
/*  67:133 */     ofs += 4;
/*  68:    */     
/*  69:135 */     this.mode = EncodingMode.unparsed;
/*  70:136 */     if (LittleEndian.getShort(data, ofs) == 2) {
/*  71:139 */       if (Character.isISOControl(data[(ofs + 2)])) {
/*  72:140 */         this.mode = EncodingMode.compact;
/*  73:    */       } else {
/*  74:142 */         this.mode = EncodingMode.parsed;
/*  75:    */       }
/*  76:    */     }
/*  77:    */     int dataSize;
/*  78:147 */     switch (1.$SwitchMap$org$apache$poi$poifs$filesystem$Ole10Native$EncodingMode[this.mode.ordinal()])
/*  79:    */     {
/*  80:    */     case 1: 
/*  81:149 */       this.flags1 = LittleEndian.getShort(data, ofs);
/*  82:    */       
/*  83:    */ 
/*  84:152 */       ofs += 2;
/*  85:    */       
/*  86:154 */       int len = getStringLength(data, ofs);
/*  87:155 */       this.label = StringUtil.getFromCompressedUnicode(data, ofs, len - 1);
/*  88:156 */       ofs += len;
/*  89:    */       
/*  90:158 */       len = getStringLength(data, ofs);
/*  91:159 */       this.fileName = StringUtil.getFromCompressedUnicode(data, ofs, len - 1);
/*  92:160 */       ofs += len;
/*  93:    */       
/*  94:162 */       this.flags2 = LittleEndian.getShort(data, ofs);
/*  95:163 */       ofs += 2;
/*  96:    */       
/*  97:165 */       this.unknown1 = LittleEndian.getShort(data, ofs);
/*  98:166 */       ofs += 2;
/*  99:    */       
/* 100:168 */       len = LittleEndian.getInt(data, ofs);
/* 101:169 */       ofs += 4;
/* 102:170 */       this.command = StringUtil.getFromCompressedUnicode(data, ofs, len - 1);
/* 103:171 */       ofs += len;
/* 104:173 */       if (this.totalSize < ofs) {
/* 105:174 */         throw new Ole10NativeException("Invalid Ole10Native");
/* 106:    */       }
/* 107:177 */       dataSize = LittleEndian.getInt(data, ofs);
/* 108:178 */       ofs += 4;
/* 109:180 */       if ((dataSize < 0) || (this.totalSize - (ofs - 4) < dataSize)) {
/* 110:181 */         throw new Ole10NativeException("Invalid Ole10Native");
/* 111:    */       }
/* 112:    */       break;
/* 113:    */     case 2: 
/* 114:186 */       this.flags1 = LittleEndian.getShort(data, ofs);
/* 115:187 */       ofs += 2;
/* 116:188 */       dataSize = this.totalSize - 2;
/* 117:189 */       break;
/* 118:    */     case 3: 
/* 119:    */     default: 
/* 120:192 */       dataSize = this.totalSize;
/* 121:    */     }
/* 122:196 */     if (dataSize + ofs > data.length) {
/* 123:197 */       throw new Ole10NativeException("Invalid Ole10Native: declared data length > available data");
/* 124:    */     }
/* 125:199 */     this.dataBuffer = new byte[dataSize];
/* 126:200 */     System.arraycopy(data, ofs, this.dataBuffer, 0, dataSize);
/* 127:201 */     ofs += dataSize;
/* 128:    */   }
/* 129:    */   
/* 130:    */   private static int getStringLength(byte[] data, int ofs)
/* 131:    */   {
/* 132:208 */     int len = 0;
/* 133:209 */     while ((len + ofs < data.length) && (data[(ofs + len)] != 0)) {
/* 134:210 */       len++;
/* 135:    */     }
/* 136:212 */     len++;
/* 137:213 */     return len;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public int getTotalSize()
/* 141:    */   {
/* 142:223 */     return this.totalSize;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public short getFlags1()
/* 146:    */   {
/* 147:232 */     return this.flags1;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public String getLabel()
/* 151:    */   {
/* 152:243 */     return this.label;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public String getFileName()
/* 156:    */   {
/* 157:253 */     return this.fileName;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public short getFlags2()
/* 161:    */   {
/* 162:262 */     return this.flags2;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public short getUnknown1()
/* 166:    */   {
/* 167:271 */     return this.unknown1;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public String getCommand()
/* 171:    */   {
/* 172:282 */     return this.command;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public int getDataSize()
/* 176:    */   {
/* 177:293 */     return this.dataBuffer.length;
/* 178:    */   }
/* 179:    */   
/* 180:    */   public byte[] getDataBuffer()
/* 181:    */   {
/* 182:306 */     return this.dataBuffer;
/* 183:    */   }
/* 184:    */   
/* 185:    */   public short getFlags3()
/* 186:    */   {
/* 187:315 */     return this.flags3;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public void writeOut(OutputStream out)
/* 191:    */     throws IOException
/* 192:    */   {
/* 193:329 */     LittleEndianOutputStream leosOut = new LittleEndianOutputStream(out);
/* 194:331 */     switch (1.$SwitchMap$org$apache$poi$poifs$filesystem$Ole10Native$EncodingMode[this.mode.ordinal()])
/* 195:    */     {
/* 196:    */     case 1: 
/* 197:333 */       ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 198:334 */       LittleEndianOutputStream leos = new LittleEndianOutputStream(bos);
/* 199:    */       
/* 200:    */ 
/* 201:337 */       leos.writeShort(getFlags1());
/* 202:338 */       leos.write(getLabel().getBytes("ISO-8859-1"));
/* 203:339 */       leos.write(0);
/* 204:340 */       leos.write(getFileName().getBytes("ISO-8859-1"));
/* 205:341 */       leos.write(0);
/* 206:342 */       leos.writeShort(getFlags2());
/* 207:343 */       leos.writeShort(getUnknown1());
/* 208:344 */       leos.writeInt(getCommand().length() + 1);
/* 209:345 */       leos.write(getCommand().getBytes("ISO-8859-1"));
/* 210:346 */       leos.write(0);
/* 211:347 */       leos.writeInt(getDataSize());
/* 212:348 */       leos.write(getDataBuffer());
/* 213:349 */       leos.writeShort(getFlags3());
/* 214:350 */       leos.close();
/* 215:    */       
/* 216:352 */       leosOut.writeInt(bos.size());
/* 217:353 */       bos.writeTo(out);
/* 218:354 */       break;
/* 219:    */     case 2: 
/* 220:357 */       leosOut.writeInt(getDataSize() + 2);
/* 221:358 */       leosOut.writeShort(getFlags1());
/* 222:359 */       out.write(getDataBuffer());
/* 223:360 */       break;
/* 224:    */     case 3: 
/* 225:    */     default: 
/* 226:363 */       leosOut.writeInt(getDataSize());
/* 227:364 */       out.write(getDataBuffer());
/* 228:    */     }
/* 229:    */   }
/* 230:    */   
/* 231:    */   public void setFlags1(short flags1)
/* 232:    */   {
/* 233:371 */     this.flags1 = flags1;
/* 234:    */   }
/* 235:    */   
/* 236:    */   public void setFlags2(short flags2)
/* 237:    */   {
/* 238:375 */     this.flags2 = flags2;
/* 239:    */   }
/* 240:    */   
/* 241:    */   public void setFlags3(short flags3)
/* 242:    */   {
/* 243:379 */     this.flags3 = flags3;
/* 244:    */   }
/* 245:    */   
/* 246:    */   public void setLabel(String label)
/* 247:    */   {
/* 248:383 */     this.label = label;
/* 249:    */   }
/* 250:    */   
/* 251:    */   public void setFileName(String fileName)
/* 252:    */   {
/* 253:387 */     this.fileName = fileName;
/* 254:    */   }
/* 255:    */   
/* 256:    */   public void setCommand(String command)
/* 257:    */   {
/* 258:391 */     this.command = command;
/* 259:    */   }
/* 260:    */   
/* 261:    */   public void setUnknown1(short unknown1)
/* 262:    */   {
/* 263:395 */     this.unknown1 = unknown1;
/* 264:    */   }
/* 265:    */   
/* 266:    */   public void setDataBuffer(byte[] dataBuffer)
/* 267:    */   {
/* 268:399 */     this.dataBuffer = ((byte[])dataBuffer.clone());
/* 269:    */   }
/* 270:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.Ole10Native
 * JD-Core Version:    0.7.0.1
 */