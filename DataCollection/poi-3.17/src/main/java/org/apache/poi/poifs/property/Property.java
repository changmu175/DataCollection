/*   1:    */ package org.apache.poi.poifs.property;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.util.Arrays;
/*   6:    */ import java.util.Collections;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.List;
/*   9:    */ import org.apache.poi.hpsf.ClassID;
/*  10:    */ import org.apache.poi.poifs.dev.POIFSViewable;
/*  11:    */ import org.apache.poi.util.ByteField;
/*  12:    */ import org.apache.poi.util.IntegerField;
/*  13:    */ import org.apache.poi.util.ShortField;
/*  14:    */ 
/*  15:    */ public abstract class Property
/*  16:    */   implements Child, POIFSViewable
/*  17:    */ {
/*  18:    */   private static final byte _default_fill = 0;
/*  19:    */   private static final int _name_size_offset = 64;
/*  20:    */   private static final int _max_name_length = 31;
/*  21:    */   protected static final int _NO_INDEX = -1;
/*  22:    */   private static final int _node_color_offset = 67;
/*  23:    */   private static final int _previous_property_offset = 68;
/*  24:    */   private static final int _next_property_offset = 72;
/*  25:    */   private static final int _child_property_offset = 76;
/*  26:    */   private static final int _storage_clsid_offset = 80;
/*  27:    */   private static final int _user_flags_offset = 96;
/*  28:    */   private static final int _seconds_1_offset = 100;
/*  29:    */   private static final int _days_1_offset = 104;
/*  30:    */   private static final int _seconds_2_offset = 108;
/*  31:    */   private static final int _days_2_offset = 112;
/*  32:    */   private static final int _start_block_offset = 116;
/*  33:    */   private static final int _size_offset = 120;
/*  34:    */   protected static final byte _NODE_BLACK = 1;
/*  35:    */   protected static final byte _NODE_RED = 0;
/*  36:    */   private static final int _big_block_minimum_bytes = 4096;
/*  37:    */   private String _name;
/*  38:    */   private ShortField _name_size;
/*  39:    */   private ByteField _property_type;
/*  40:    */   private ByteField _node_color;
/*  41:    */   private IntegerField _previous_property;
/*  42:    */   private IntegerField _next_property;
/*  43:    */   private IntegerField _child_property;
/*  44:    */   private ClassID _storage_clsid;
/*  45:    */   private IntegerField _user_flags;
/*  46:    */   private IntegerField _seconds_1;
/*  47:    */   private IntegerField _days_1;
/*  48:    */   private IntegerField _seconds_2;
/*  49:    */   private IntegerField _days_2;
/*  50:    */   private IntegerField _start_block;
/*  51:    */   private IntegerField _size;
/*  52:    */   private byte[] _raw_data;
/*  53:    */   private int _index;
/*  54:    */   private Child _next_child;
/*  55:    */   private Child _previous_child;
/*  56:    */   
/*  57:    */   protected Property()
/*  58:    */   {
/*  59: 90 */     this._raw_data = new byte[''];
/*  60: 91 */     Arrays.fill(this._raw_data, (byte)0);
/*  61: 92 */     this._name_size = new ShortField(64);
/*  62: 93 */     this._property_type = new ByteField(66);
/*  63:    */     
/*  64: 95 */     this._node_color = new ByteField(67);
/*  65: 96 */     this._previous_property = new IntegerField(68, -1, this._raw_data);
/*  66:    */     
/*  67: 98 */     this._next_property = new IntegerField(72, -1, this._raw_data);
/*  68:    */     
/*  69:100 */     this._child_property = new IntegerField(76, -1, this._raw_data);
/*  70:    */     
/*  71:102 */     this._storage_clsid = new ClassID(this._raw_data, 80);
/*  72:103 */     this._user_flags = new IntegerField(96, 0, this._raw_data);
/*  73:104 */     this._seconds_1 = new IntegerField(100, 0, this._raw_data);
/*  74:    */     
/*  75:106 */     this._days_1 = new IntegerField(104, 0, this._raw_data);
/*  76:107 */     this._seconds_2 = new IntegerField(108, 0, this._raw_data);
/*  77:    */     
/*  78:109 */     this._days_2 = new IntegerField(112, 0, this._raw_data);
/*  79:110 */     this._start_block = new IntegerField(116);
/*  80:111 */     this._size = new IntegerField(120, 0, this._raw_data);
/*  81:112 */     this._index = -1;
/*  82:113 */     setName("");
/*  83:114 */     setNextChild(null);
/*  84:115 */     setPreviousChild(null);
/*  85:    */   }
/*  86:    */   
/*  87:    */   protected Property(int index, byte[] array, int offset)
/*  88:    */   {
/*  89:127 */     this._raw_data = new byte[''];
/*  90:128 */     System.arraycopy(array, offset, this._raw_data, 0, 128);
/*  91:    */     
/*  92:130 */     this._name_size = new ShortField(64, this._raw_data);
/*  93:131 */     this._property_type = new ByteField(66, this._raw_data);
/*  94:    */     
/*  95:133 */     this._node_color = new ByteField(67, this._raw_data);
/*  96:134 */     this._previous_property = new IntegerField(68, this._raw_data);
/*  97:    */     
/*  98:136 */     this._next_property = new IntegerField(72, this._raw_data);
/*  99:    */     
/* 100:138 */     this._child_property = new IntegerField(76, this._raw_data);
/* 101:    */     
/* 102:140 */     this._storage_clsid = new ClassID(this._raw_data, 80);
/* 103:141 */     this._user_flags = new IntegerField(96, 0, this._raw_data);
/* 104:142 */     this._seconds_1 = new IntegerField(100, this._raw_data);
/* 105:143 */     this._days_1 = new IntegerField(104, this._raw_data);
/* 106:144 */     this._seconds_2 = new IntegerField(108, this._raw_data);
/* 107:145 */     this._days_2 = new IntegerField(112, this._raw_data);
/* 108:146 */     this._start_block = new IntegerField(116, this._raw_data);
/* 109:147 */     this._size = new IntegerField(120, this._raw_data);
/* 110:148 */     this._index = index;
/* 111:149 */     int name_length = this._name_size.get() / 2 - 1;
/* 112:152 */     if (name_length < 1)
/* 113:    */     {
/* 114:154 */       this._name = "";
/* 115:    */     }
/* 116:    */     else
/* 117:    */     {
/* 118:158 */       char[] char_array = new char[name_length];
/* 119:159 */       int name_offset = 0;
/* 120:161 */       for (int j = 0; j < name_length; j++)
/* 121:    */       {
/* 122:163 */         char_array[j] = ((char)new ShortField(name_offset, this._raw_data).get());
/* 123:    */         
/* 124:165 */         name_offset += 2;
/* 125:    */       }
/* 126:167 */       this._name = new String(char_array, 0, name_length);
/* 127:    */     }
/* 128:169 */     this._next_child = null;
/* 129:170 */     this._previous_child = null;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void writeData(OutputStream stream)
/* 133:    */     throws IOException
/* 134:    */   {
/* 135:185 */     stream.write(this._raw_data);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void setStartBlock(int startBlock)
/* 139:    */   {
/* 140:196 */     this._start_block.set(startBlock, this._raw_data);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public int getStartBlock()
/* 144:    */   {
/* 145:204 */     return this._start_block.get();
/* 146:    */   }
/* 147:    */   
/* 148:    */   public int getSize()
/* 149:    */   {
/* 150:214 */     return this._size.get();
/* 151:    */   }
/* 152:    */   
/* 153:    */   public boolean shouldUseSmallBlocks()
/* 154:    */   {
/* 155:225 */     return isSmall(this._size.get());
/* 156:    */   }
/* 157:    */   
/* 158:    */   public static boolean isSmall(int length)
/* 159:    */   {
/* 160:238 */     return length < 4096;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public String getName()
/* 164:    */   {
/* 165:248 */     return this._name;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public abstract boolean isDirectory();
/* 169:    */   
/* 170:    */   public ClassID getStorageClsid()
/* 171:    */   {
/* 172:263 */     return this._storage_clsid;
/* 173:    */   }
/* 174:    */   
/* 175:    */   protected void setName(String name)
/* 176:    */   {
/* 177:273 */     char[] char_array = name.toCharArray();
/* 178:274 */     int limit = Math.min(char_array.length, 31);
/* 179:    */     
/* 180:276 */     this._name = new String(char_array, 0, limit);
/* 181:277 */     short offset = 0;
/* 182:278 */     for (int j = 0; j < limit; j++)
/* 183:    */     {
/* 184:282 */       new ShortField(offset, (short)char_array[j], this._raw_data);
/* 185:283 */       offset = (short)(offset + 2);
/* 186:    */     }
/* 187:285 */     for (; j < 32; j++)
/* 188:    */     {
/* 189:287 */       new ShortField(offset, (short)0, this._raw_data);
/* 190:288 */       offset = (short)(offset + 2);
/* 191:    */     }
/* 192:292 */     this._name_size.set((short)((limit + 1) * 2), this._raw_data);
/* 193:    */   }
/* 194:    */   
/* 195:    */   public void setStorageClsid(ClassID clsidStorage)
/* 196:    */   {
/* 197:304 */     this._storage_clsid = clsidStorage;
/* 198:305 */     if (clsidStorage == null) {
/* 199:306 */       Arrays.fill(this._raw_data, 80, 96, (byte)0);
/* 200:    */     } else {
/* 201:308 */       clsidStorage.write(this._raw_data, 80);
/* 202:    */     }
/* 203:    */   }
/* 204:    */   
/* 205:    */   protected void setPropertyType(byte propertyType)
/* 206:    */   {
/* 207:318 */     this._property_type.set(propertyType, this._raw_data);
/* 208:    */   }
/* 209:    */   
/* 210:    */   protected void setNodeColor(byte nodeColor)
/* 211:    */   {
/* 212:328 */     this._node_color.set(nodeColor, this._raw_data);
/* 213:    */   }
/* 214:    */   
/* 215:    */   protected void setChildProperty(int child)
/* 216:    */   {
/* 217:338 */     this._child_property.set(child, this._raw_data);
/* 218:    */   }
/* 219:    */   
/* 220:    */   protected int getChildIndex()
/* 221:    */   {
/* 222:348 */     return this._child_property.get();
/* 223:    */   }
/* 224:    */   
/* 225:    */   protected void setSize(int size)
/* 226:    */   {
/* 227:358 */     this._size.set(size, this._raw_data);
/* 228:    */   }
/* 229:    */   
/* 230:    */   protected void setIndex(int index)
/* 231:    */   {
/* 232:369 */     this._index = index;
/* 233:    */   }
/* 234:    */   
/* 235:    */   protected int getIndex()
/* 236:    */   {
/* 237:379 */     return this._index;
/* 238:    */   }
/* 239:    */   
/* 240:    */   protected abstract void preWrite();
/* 241:    */   
/* 242:    */   int getNextChildIndex()
/* 243:    */   {
/* 244:395 */     return this._next_property.get();
/* 245:    */   }
/* 246:    */   
/* 247:    */   int getPreviousChildIndex()
/* 248:    */   {
/* 249:405 */     return this._previous_property.get();
/* 250:    */   }
/* 251:    */   
/* 252:    */   static boolean isValidIndex(int index)
/* 253:    */   {
/* 254:417 */     return index != -1;
/* 255:    */   }
/* 256:    */   
/* 257:    */   public Child getNextChild()
/* 258:    */   {
/* 259:427 */     return this._next_child;
/* 260:    */   }
/* 261:    */   
/* 262:    */   public Child getPreviousChild()
/* 263:    */   {
/* 264:437 */     return this._previous_child;
/* 265:    */   }
/* 266:    */   
/* 267:    */   public void setNextChild(Child child)
/* 268:    */   {
/* 269:448 */     this._next_child = child;
/* 270:449 */     this._next_property.set(child == null ? -1 : ((Property)child).getIndex(), this._raw_data);
/* 271:    */   }
/* 272:    */   
/* 273:    */   public void setPreviousChild(Child child)
/* 274:    */   {
/* 275:462 */     this._previous_child = child;
/* 276:463 */     this._previous_property.set(child == null ? -1 : ((Property)child).getIndex(), this._raw_data);
/* 277:    */   }
/* 278:    */   
/* 279:    */   public Object[] getViewableArray()
/* 280:    */   {
/* 281:476 */     Object[] results = new Object[6];
/* 282:    */     
/* 283:478 */     results[0] = ("Name          = \"" + getName() + "\"");
/* 284:479 */     results[1] = ("Property Type = " + this._property_type.get());
/* 285:480 */     results[2] = ("Node Color    = " + this._node_color.get());
/* 286:481 */     long time = this._days_1.get();
/* 287:    */     
/* 288:483 */     time <<= 32;
/* 289:484 */     time += (this._seconds_1.get() & 0xFFFF);
/* 290:485 */     results[3] = ("Time 1        = " + time);
/* 291:486 */     time = this._days_2.get();
/* 292:487 */     time <<= 32;
/* 293:488 */     time += (this._seconds_2.get() & 0xFFFF);
/* 294:489 */     results[4] = ("Time 2        = " + time);
/* 295:490 */     results[5] = ("Size          = " + getSize());
/* 296:491 */     return results;
/* 297:    */   }
/* 298:    */   
/* 299:    */   public Iterator<Object> getViewableIterator()
/* 300:    */   {
/* 301:503 */     return Collections.emptyList().iterator();
/* 302:    */   }
/* 303:    */   
/* 304:    */   public boolean preferArray()
/* 305:    */   {
/* 306:515 */     return true;
/* 307:    */   }
/* 308:    */   
/* 309:    */   public String getShortDescription()
/* 310:    */   {
/* 311:525 */     return "Property: \"" + getName() + "\"";
/* 312:    */   }
/* 313:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.property.Property
 * JD-Core Version:    0.7.0.1
 */