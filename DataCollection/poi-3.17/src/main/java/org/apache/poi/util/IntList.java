/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ public class IntList
/*   4:    */ {
/*   5:    */   private int[] _array;
/*   6:    */   private int _limit;
/*   7: 50 */   private int fillval = 0;
/*   8:    */   private static final int _default_size = 128;
/*   9:    */   
/*  10:    */   public IntList()
/*  11:    */   {
/*  12: 59 */     this(128);
/*  13:    */   }
/*  14:    */   
/*  15:    */   public IntList(int initialCapacity)
/*  16:    */   {
/*  17: 64 */     this(initialCapacity, 0);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public IntList(IntList list)
/*  21:    */   {
/*  22: 76 */     this(list._array.length);
/*  23: 77 */     System.arraycopy(list._array, 0, this._array, 0, this._array.length);
/*  24: 78 */     this._limit = list._limit;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public IntList(int initialCapacity, int fillvalue)
/*  28:    */   {
/*  29: 89 */     this._array = new int[initialCapacity];
/*  30: 90 */     if (this.fillval != 0)
/*  31:    */     {
/*  32: 91 */       this.fillval = fillvalue;
/*  33: 92 */       fillArray(this.fillval, this._array, 0);
/*  34:    */     }
/*  35: 94 */     this._limit = 0;
/*  36:    */   }
/*  37:    */   
/*  38:    */   private void fillArray(int val, int[] array, int index)
/*  39:    */   {
/*  40: 98 */     for (int k = index; k < array.length; k++) {
/*  41: 99 */       array[k] = val;
/*  42:    */     }
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void add(int index, int value)
/*  46:    */   {
/*  47:115 */     if (index > this._limit) {
/*  48:117 */       throw new IndexOutOfBoundsException();
/*  49:    */     }
/*  50:119 */     if (index == this._limit)
/*  51:    */     {
/*  52:121 */       add(value);
/*  53:    */     }
/*  54:    */     else
/*  55:    */     {
/*  56:127 */       if (this._limit == this._array.length) {
/*  57:129 */         growArray(this._limit * 2);
/*  58:    */       }
/*  59:131 */       System.arraycopy(this._array, index, this._array, index + 1, this._limit - index);
/*  60:    */       
/*  61:133 */       this._array[index] = value;
/*  62:134 */       this._limit += 1;
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   public boolean add(int value)
/*  67:    */   {
/*  68:149 */     if (this._limit == this._array.length) {
/*  69:151 */       growArray(this._limit * 2);
/*  70:    */     }
/*  71:153 */     this._array[(this._limit++)] = value;
/*  72:154 */     return true;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public boolean addAll(IntList c)
/*  76:    */   {
/*  77:174 */     if (c._limit != 0)
/*  78:    */     {
/*  79:176 */       if (this._limit + c._limit > this._array.length) {
/*  80:178 */         growArray(this._limit + c._limit);
/*  81:    */       }
/*  82:180 */       System.arraycopy(c._array, 0, this._array, this._limit, c._limit);
/*  83:181 */       this._limit += c._limit;
/*  84:    */     }
/*  85:183 */     return true;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public boolean addAll(int index, IntList c)
/*  89:    */   {
/*  90:210 */     if (index > this._limit) {
/*  91:212 */       throw new IndexOutOfBoundsException();
/*  92:    */     }
/*  93:214 */     if (c._limit != 0)
/*  94:    */     {
/*  95:216 */       if (this._limit + c._limit > this._array.length) {
/*  96:218 */         growArray(this._limit + c._limit);
/*  97:    */       }
/*  98:222 */       System.arraycopy(this._array, index, this._array, index + c._limit, this._limit - index);
/*  99:    */       
/* 100:    */ 
/* 101:    */ 
/* 102:226 */       System.arraycopy(c._array, 0, this._array, index, c._limit);
/* 103:227 */       this._limit += c._limit;
/* 104:    */     }
/* 105:229 */     return true;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void clear()
/* 109:    */   {
/* 110:239 */     this._limit = 0;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public boolean contains(int o)
/* 114:    */   {
/* 115:254 */     boolean rval = false;
/* 116:256 */     for (int j = 0; (!rval) && (j < this._limit); j++) {
/* 117:258 */       if (this._array[j] == o) {
/* 118:260 */         rval = true;
/* 119:    */       }
/* 120:    */     }
/* 121:263 */     return rval;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public boolean containsAll(IntList c)
/* 125:    */   {
/* 126:278 */     boolean rval = true;
/* 127:280 */     if (this != c) {
/* 128:282 */       for (int j = 0; (rval) && (j < c._limit); j++) {
/* 129:284 */         if (!contains(c._array[j])) {
/* 130:286 */           rval = false;
/* 131:    */         }
/* 132:    */       }
/* 133:    */     }
/* 134:290 */     return rval;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public boolean equals(Object o)
/* 138:    */   {
/* 139:311 */     boolean rval = this == o;
/* 140:313 */     if ((!rval) && (o != null) && (o.getClass() == getClass()))
/* 141:    */     {
/* 142:315 */       IntList other = (IntList)o;
/* 143:317 */       if (other._limit == this._limit)
/* 144:    */       {
/* 145:321 */         rval = true;
/* 146:322 */         for (int j = 0; (rval) && (j < this._limit); j++) {
/* 147:324 */           rval = this._array[j] == other._array[j];
/* 148:    */         }
/* 149:    */       }
/* 150:    */     }
/* 151:328 */     return rval;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public int get(int index)
/* 155:    */   {
/* 156:344 */     if (index >= this._limit) {
/* 157:346 */       throw new IndexOutOfBoundsException(index + " not accessible in a list of length " + this._limit);
/* 158:    */     }
/* 159:350 */     return this._array[index];
/* 160:    */   }
/* 161:    */   
/* 162:    */   public int hashCode()
/* 163:    */   {
/* 164:375 */     int hash = 0;
/* 165:377 */     for (int j = 0; j < this._limit; j++) {
/* 166:379 */       hash = 31 * hash + this._array[j];
/* 167:    */     }
/* 168:381 */     return hash;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public int indexOf(int o)
/* 172:    */   {
/* 173:399 */     for (int rval = 0; rval < this._limit; rval++) {
/* 174:403 */       if (o == this._array[rval]) {
/* 175:    */         break;
/* 176:    */       }
/* 177:    */     }
/* 178:408 */     if (rval == this._limit) {
/* 179:410 */       rval = -1;
/* 180:    */     }
/* 181:412 */     return rval;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public boolean isEmpty()
/* 185:    */   {
/* 186:423 */     return this._limit == 0;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public int lastIndexOf(int o)
/* 190:    */   {
/* 191:441 */     for (int rval = this._limit - 1; rval >= 0; rval--) {
/* 192:445 */       if (o == this._array[rval]) {
/* 193:    */         break;
/* 194:    */       }
/* 195:    */     }
/* 196:450 */     return rval;
/* 197:    */   }
/* 198:    */   
/* 199:    */   public int remove(int index)
/* 200:    */   {
/* 201:469 */     if (index >= this._limit) {
/* 202:471 */       throw new IndexOutOfBoundsException();
/* 203:    */     }
/* 204:473 */     int rval = this._array[index];
/* 205:    */     
/* 206:475 */     System.arraycopy(this._array, index + 1, this._array, index, this._limit - index);
/* 207:476 */     this._limit -= 1;
/* 208:477 */     return rval;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public boolean removeValue(int o)
/* 212:    */   {
/* 213:494 */     boolean rval = false;
/* 214:496 */     for (int j = 0; (!rval) && (j < this._limit); j++) {
/* 215:498 */       if (o == this._array[j])
/* 216:    */       {
/* 217:500 */         if (j + 1 < this._limit) {
/* 218:501 */           System.arraycopy(this._array, j + 1, this._array, j, this._limit - j);
/* 219:    */         }
/* 220:503 */         this._limit -= 1;
/* 221:504 */         rval = true;
/* 222:    */       }
/* 223:    */     }
/* 224:507 */     return rval;
/* 225:    */   }
/* 226:    */   
/* 227:    */   public boolean removeAll(IntList c)
/* 228:    */   {
/* 229:522 */     boolean rval = false;
/* 230:524 */     for (int j = 0; j < c._limit; j++) {
/* 231:526 */       if (removeValue(c._array[j])) {
/* 232:528 */         rval = true;
/* 233:    */       }
/* 234:    */     }
/* 235:531 */     return rval;
/* 236:    */   }
/* 237:    */   
/* 238:    */   public boolean retainAll(IntList c)
/* 239:    */   {
/* 240:548 */     boolean rval = false;
/* 241:550 */     for (int j = 0; j < this._limit;) {
/* 242:552 */       if (!c.contains(this._array[j]))
/* 243:    */       {
/* 244:554 */         remove(j);
/* 245:555 */         rval = true;
/* 246:    */       }
/* 247:    */       else
/* 248:    */       {
/* 249:559 */         j++;
/* 250:    */       }
/* 251:    */     }
/* 252:562 */     return rval;
/* 253:    */   }
/* 254:    */   
/* 255:    */   public int set(int index, int element)
/* 256:    */   {
/* 257:580 */     if (index >= this._limit) {
/* 258:582 */       throw new IndexOutOfBoundsException();
/* 259:    */     }
/* 260:584 */     int rval = this._array[index];
/* 261:    */     
/* 262:586 */     this._array[index] = element;
/* 263:587 */     return rval;
/* 264:    */   }
/* 265:    */   
/* 266:    */   public int size()
/* 267:    */   {
/* 268:600 */     return this._limit;
/* 269:    */   }
/* 270:    */   
/* 271:    */   public int[] toArray()
/* 272:    */   {
/* 273:614 */     int[] rval = new int[this._limit];
/* 274:    */     
/* 275:616 */     System.arraycopy(this._array, 0, rval, 0, this._limit);
/* 276:617 */     return rval;
/* 277:    */   }
/* 278:    */   
/* 279:    */   public int[] toArray(int[] a)
/* 280:    */   {
/* 281:    */     int[] rval;
/* 282:    */     int[] rval;
/* 283:636 */     if (a.length == this._limit)
/* 284:    */     {
/* 285:638 */       System.arraycopy(this._array, 0, a, 0, this._limit);
/* 286:639 */       rval = a;
/* 287:    */     }
/* 288:    */     else
/* 289:    */     {
/* 290:643 */       rval = toArray();
/* 291:    */     }
/* 292:645 */     return rval;
/* 293:    */   }
/* 294:    */   
/* 295:    */   private void growArray(int new_size)
/* 296:    */   {
/* 297:650 */     int size = new_size == this._array.length ? new_size + 1 : new_size;
/* 298:    */     
/* 299:652 */     int[] new_array = new int[size];
/* 300:654 */     if (this.fillval != 0) {
/* 301:655 */       fillArray(this.fillval, new_array, this._array.length);
/* 302:    */     }
/* 303:658 */     System.arraycopy(this._array, 0, new_array, 0, this._limit);
/* 304:659 */     this._array = new_array;
/* 305:    */   }
/* 306:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.IntList
 * JD-Core Version:    0.7.0.1
 */