/*   1:    */ package org.apache.poi.poifs.storage;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.nio.ByteBuffer;
/*   6:    */ import java.util.Arrays;
/*   7:    */ import java.util.List;
/*   8:    */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*   9:    */ import org.apache.poi.util.LittleEndian;
/*  10:    */ 
/*  11:    */ public final class BATBlock
/*  12:    */   extends BigBlock
/*  13:    */ {
/*  14:    */   private int[] _values;
/*  15:    */   private boolean _has_free_sectors;
/*  16:    */   private int ourBlockIndex;
/*  17:    */   
/*  18:    */   private BATBlock(POIFSBigBlockSize bigBlockSize)
/*  19:    */   {
/*  20: 58 */     super(bigBlockSize);
/*  21:    */     
/*  22: 60 */     int _entries_per_block = bigBlockSize.getBATEntriesPerBlock();
/*  23: 61 */     this._values = new int[_entries_per_block];
/*  24: 62 */     this._has_free_sectors = true;
/*  25:    */     
/*  26: 64 */     Arrays.fill(this._values, -1);
/*  27:    */   }
/*  28:    */   
/*  29:    */   private BATBlock(POIFSBigBlockSize bigBlockSize, int[] entries, int start_index, int end_index)
/*  30:    */   {
/*  31: 81 */     this(bigBlockSize);
/*  32: 82 */     for (int k = start_index; k < end_index; k++) {
/*  33: 83 */       this._values[(k - start_index)] = entries[k];
/*  34:    */     }
/*  35: 87 */     if (end_index - start_index == this._values.length) {
/*  36: 88 */       recomputeFree();
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   private void recomputeFree()
/*  41:    */   {
/*  42: 93 */     boolean hasFree = false;
/*  43: 94 */     for (int k = 0; k < this._values.length; k++) {
/*  44: 95 */       if (this._values[k] == -1)
/*  45:    */       {
/*  46: 96 */         hasFree = true;
/*  47: 97 */         break;
/*  48:    */       }
/*  49:    */     }
/*  50:100 */     this._has_free_sectors = hasFree;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public static BATBlock createBATBlock(POIFSBigBlockSize bigBlockSize, ByteBuffer data)
/*  54:    */   {
/*  55:110 */     BATBlock block = new BATBlock(bigBlockSize);
/*  56:    */     
/*  57:    */ 
/*  58:113 */     byte[] buffer = new byte[4];
/*  59:114 */     for (int i = 0; i < block._values.length; i++)
/*  60:    */     {
/*  61:115 */       data.get(buffer);
/*  62:116 */       block._values[i] = LittleEndian.getInt(buffer);
/*  63:    */     }
/*  64:118 */     block.recomputeFree();
/*  65:    */     
/*  66:    */ 
/*  67:121 */     return block;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static BATBlock createEmptyBATBlock(POIFSBigBlockSize bigBlockSize, boolean isXBAT)
/*  71:    */   {
/*  72:128 */     BATBlock block = new BATBlock(bigBlockSize);
/*  73:129 */     if (isXBAT) {
/*  74:130 */       block.setXBATChain(bigBlockSize, -2);
/*  75:    */     }
/*  76:132 */     return block;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public static BATBlock[] createBATBlocks(POIFSBigBlockSize bigBlockSize, int[] entries)
/*  80:    */   {
/*  81:145 */     int block_count = calculateStorageRequirements(bigBlockSize, entries.length);
/*  82:146 */     BATBlock[] blocks = new BATBlock[block_count];
/*  83:147 */     int index = 0;
/*  84:148 */     int remaining = entries.length;
/*  85:    */     
/*  86:150 */     int _entries_per_block = bigBlockSize.getBATEntriesPerBlock();
/*  87:151 */     for (int j = 0; j < entries.length; j += _entries_per_block)
/*  88:    */     {
/*  89:153 */       blocks[(index++)] = new BATBlock(bigBlockSize, entries, j, remaining > _entries_per_block ? j + _entries_per_block : entries.length);
/*  90:    */       
/*  91:    */ 
/*  92:    */ 
/*  93:157 */       remaining -= _entries_per_block;
/*  94:    */     }
/*  95:159 */     return blocks;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static BATBlock[] createXBATBlocks(POIFSBigBlockSize bigBlockSize, int[] entries, int startBlock)
/*  99:    */   {
/* 100:176 */     int block_count = calculateXBATStorageRequirements(bigBlockSize, entries.length);
/* 101:    */     
/* 102:178 */     BATBlock[] blocks = new BATBlock[block_count];
/* 103:179 */     int index = 0;
/* 104:180 */     int remaining = entries.length;
/* 105:    */     
/* 106:182 */     int _entries_per_xbat_block = bigBlockSize.getXBATEntriesPerBlock();
/* 107:183 */     if (block_count != 0)
/* 108:    */     {
/* 109:185 */       for (int j = 0; j < entries.length; j += _entries_per_xbat_block)
/* 110:    */       {
/* 111:187 */         blocks[(index++)] = new BATBlock(bigBlockSize, entries, j, remaining > _entries_per_xbat_block ? j + _entries_per_xbat_block : entries.length);
/* 112:    */         
/* 113:    */ 
/* 114:    */ 
/* 115:    */ 
/* 116:192 */         remaining -= _entries_per_xbat_block;
/* 117:    */       }
/* 118:194 */       for (index = 0; index < blocks.length - 1; index++) {
/* 119:196 */         blocks[index].setXBATChain(bigBlockSize, startBlock + index + 1);
/* 120:    */       }
/* 121:198 */       blocks[index].setXBATChain(bigBlockSize, -2);
/* 122:    */     }
/* 123:200 */     return blocks;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public static int calculateStorageRequirements(POIFSBigBlockSize bigBlockSize, int entryCount)
/* 127:    */   {
/* 128:213 */     int _entries_per_block = bigBlockSize.getBATEntriesPerBlock();
/* 129:214 */     return (entryCount + _entries_per_block - 1) / _entries_per_block;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public static int calculateXBATStorageRequirements(POIFSBigBlockSize bigBlockSize, int entryCount)
/* 133:    */   {
/* 134:227 */     int _entries_per_xbat_block = bigBlockSize.getXBATEntriesPerBlock();
/* 135:228 */     return (entryCount + _entries_per_xbat_block - 1) / _entries_per_xbat_block;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public static long calculateMaximumSize(POIFSBigBlockSize bigBlockSize, int numBATs)
/* 139:    */   {
/* 140:246 */     long size = 1L;
/* 141:    */     
/* 142:    */ 
/* 143:    */ 
/* 144:    */ 
/* 145:251 */     size += numBATs * bigBlockSize.getBATEntriesPerBlock();
/* 146:    */     
/* 147:    */ 
/* 148:254 */     return size * bigBlockSize.getBigBlockSize();
/* 149:    */   }
/* 150:    */   
/* 151:    */   public static long calculateMaximumSize(HeaderBlock header)
/* 152:    */   {
/* 153:258 */     return calculateMaximumSize(header.getBigBlockSize(), header.getBATCount());
/* 154:    */   }
/* 155:    */   
/* 156:    */   public static BATBlockAndIndex getBATBlockAndIndex(int offset, HeaderBlock header, List<BATBlock> bats)
/* 157:    */   {
/* 158:268 */     POIFSBigBlockSize bigBlockSize = header.getBigBlockSize();
/* 159:269 */     int entriesPerBlock = bigBlockSize.getBATEntriesPerBlock();
/* 160:    */     
/* 161:271 */     int whichBAT = offset / entriesPerBlock;
/* 162:272 */     int index = offset % entriesPerBlock;
/* 163:273 */     return new BATBlockAndIndex(index, (BATBlock)bats.get(whichBAT), null);
/* 164:    */   }
/* 165:    */   
/* 166:    */   public static BATBlockAndIndex getSBATBlockAndIndex(int offset, HeaderBlock header, List<BATBlock> sbats)
/* 167:    */   {
/* 168:283 */     POIFSBigBlockSize bigBlockSize = header.getBigBlockSize();
/* 169:284 */     int entriesPerBlock = bigBlockSize.getBATEntriesPerBlock();
/* 170:    */     
/* 171:    */ 
/* 172:287 */     int whichSBAT = offset / entriesPerBlock;
/* 173:288 */     int index = offset % entriesPerBlock;
/* 174:289 */     return new BATBlockAndIndex(index, (BATBlock)sbats.get(whichSBAT), null);
/* 175:    */   }
/* 176:    */   
/* 177:    */   private void setXBATChain(POIFSBigBlockSize bigBlockSize, int chainIndex)
/* 178:    */   {
/* 179:294 */     int _entries_per_xbat_block = bigBlockSize.getXBATEntriesPerBlock();
/* 180:295 */     this._values[_entries_per_xbat_block] = chainIndex;
/* 181:    */   }
/* 182:    */   
/* 183:    */   public boolean hasFreeSectors()
/* 184:    */   {
/* 185:303 */     return this._has_free_sectors;
/* 186:    */   }
/* 187:    */   
/* 188:    */   public int getUsedSectors(boolean isAnXBAT)
/* 189:    */   {
/* 190:310 */     int usedSectors = 0;
/* 191:311 */     int toCheck = this._values.length;
/* 192:312 */     if (isAnXBAT) {
/* 193:312 */       toCheck--;
/* 194:    */     }
/* 195:313 */     for (int k = 0; k < toCheck; k++) {
/* 196:314 */       if (this._values[k] != -1) {
/* 197:315 */         usedSectors++;
/* 198:    */       }
/* 199:    */     }
/* 200:318 */     return usedSectors;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public int getValueAt(int relativeOffset)
/* 204:    */   {
/* 205:322 */     if (relativeOffset >= this._values.length) {
/* 206:323 */       throw new ArrayIndexOutOfBoundsException("Unable to fetch offset " + relativeOffset + " as the " + "BAT only contains " + this._values.length + " entries");
/* 207:    */     }
/* 208:328 */     return this._values[relativeOffset];
/* 209:    */   }
/* 210:    */   
/* 211:    */   public void setValueAt(int relativeOffset, int value)
/* 212:    */   {
/* 213:331 */     int oldValue = this._values[relativeOffset];
/* 214:332 */     this._values[relativeOffset] = value;
/* 215:335 */     if (value == -1)
/* 216:    */     {
/* 217:336 */       this._has_free_sectors = true;
/* 218:337 */       return;
/* 219:    */     }
/* 220:339 */     if (oldValue == -1) {
/* 221:340 */       recomputeFree();
/* 222:    */     }
/* 223:    */   }
/* 224:    */   
/* 225:    */   public void setOurBlockIndex(int index)
/* 226:    */   {
/* 227:348 */     this.ourBlockIndex = index;
/* 228:    */   }
/* 229:    */   
/* 230:    */   public int getOurBlockIndex()
/* 231:    */   {
/* 232:354 */     return this.ourBlockIndex;
/* 233:    */   }
/* 234:    */   
/* 235:    */   void writeData(OutputStream stream)
/* 236:    */     throws IOException
/* 237:    */   {
/* 238:373 */     stream.write(serialize());
/* 239:    */   }
/* 240:    */   
/* 241:    */   void writeData(ByteBuffer block)
/* 242:    */     throws IOException
/* 243:    */   {
/* 244:380 */     block.put(serialize());
/* 245:    */   }
/* 246:    */   
/* 247:    */   private byte[] serialize()
/* 248:    */   {
/* 249:385 */     byte[] data = new byte[this.bigBlockSize.getBigBlockSize()];
/* 250:    */     
/* 251:    */ 
/* 252:388 */     int offset = 0;
/* 253:389 */     for (int i = 0; i < this._values.length; i++)
/* 254:    */     {
/* 255:390 */       LittleEndian.putInt(data, offset, this._values[i]);
/* 256:391 */       offset += 4;
/* 257:    */     }
/* 258:395 */     return data;
/* 259:    */   }
/* 260:    */   
/* 261:    */   public static class BATBlockAndIndex
/* 262:    */   {
/* 263:    */     private final int index;
/* 264:    */     private final BATBlock block;
/* 265:    */     
/* 266:    */     private BATBlockAndIndex(int index, BATBlock block)
/* 267:    */     {
/* 268:405 */       this.index = index;
/* 269:406 */       this.block = block;
/* 270:    */     }
/* 271:    */     
/* 272:    */     public int getIndex()
/* 273:    */     {
/* 274:409 */       return this.index;
/* 275:    */     }
/* 276:    */     
/* 277:    */     public BATBlock getBlock()
/* 278:    */     {
/* 279:412 */       return this.block;
/* 280:    */     }
/* 281:    */   }
/* 282:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.BATBlock
 * JD-Core Version:    0.7.0.1
 */