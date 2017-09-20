/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ final class FormulaCellCacheEntrySet
/*   4:    */ {
/*   5: 30 */   private static final FormulaCellCacheEntry[] EMPTY_ARRAY = new FormulaCellCacheEntry[0];
/*   6:    */   private int _size;
/*   7:    */   private FormulaCellCacheEntry[] _arr;
/*   8:    */   
/*   9:    */   public FormulaCellCacheEntrySet()
/*  10:    */   {
/*  11: 36 */     this._arr = EMPTY_ARRAY;
/*  12:    */   }
/*  13:    */   
/*  14:    */   public FormulaCellCacheEntry[] toArray()
/*  15:    */   {
/*  16: 40 */     int nItems = this._size;
/*  17: 41 */     if (nItems < 1) {
/*  18: 42 */       return EMPTY_ARRAY;
/*  19:    */     }
/*  20: 44 */     FormulaCellCacheEntry[] result = new FormulaCellCacheEntry[nItems];
/*  21: 45 */     int j = 0;
/*  22: 46 */     for (int i = 0; i < this._arr.length; i++)
/*  23:    */     {
/*  24: 47 */       FormulaCellCacheEntry cce = this._arr[i];
/*  25: 48 */       if (cce != null) {
/*  26: 49 */         result[(j++)] = cce;
/*  27:    */       }
/*  28:    */     }
/*  29: 52 */     if (j != nItems) {
/*  30: 53 */       throw new IllegalStateException("size mismatch");
/*  31:    */     }
/*  32: 55 */     return result;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void add(CellCacheEntry cce)
/*  36:    */   {
/*  37: 60 */     if (this._size * 3 >= this._arr.length * 2)
/*  38:    */     {
/*  39: 62 */       FormulaCellCacheEntry[] prevArr = this._arr;
/*  40: 63 */       FormulaCellCacheEntry[] newArr = new FormulaCellCacheEntry[4 + this._arr.length * 3 / 2];
/*  41: 64 */       for (int i = 0; i < prevArr.length; i++)
/*  42:    */       {
/*  43: 65 */         FormulaCellCacheEntry prevCce = this._arr[i];
/*  44: 66 */         if (prevCce != null) {
/*  45: 67 */           addInternal(newArr, prevCce);
/*  46:    */         }
/*  47:    */       }
/*  48: 70 */       this._arr = newArr;
/*  49:    */     }
/*  50: 72 */     if (addInternal(this._arr, cce)) {
/*  51: 73 */       this._size += 1;
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   private static boolean addInternal(CellCacheEntry[] arr, CellCacheEntry cce)
/*  56:    */   {
/*  57: 79 */     int startIx = Math.abs(cce.hashCode() % arr.length);
/*  58: 81 */     for (int i = startIx; i < arr.length; i++)
/*  59:    */     {
/*  60: 82 */       CellCacheEntry item = arr[i];
/*  61: 83 */       if (item == cce) {
/*  62: 85 */         return false;
/*  63:    */       }
/*  64: 87 */       if (item == null)
/*  65:    */       {
/*  66: 88 */         arr[i] = cce;
/*  67: 89 */         return true;
/*  68:    */       }
/*  69:    */     }
/*  70: 92 */     for (int i = 0; i < startIx; i++)
/*  71:    */     {
/*  72: 93 */       CellCacheEntry item = arr[i];
/*  73: 94 */       if (item == cce) {
/*  74: 96 */         return false;
/*  75:    */       }
/*  76: 98 */       if (item == null)
/*  77:    */       {
/*  78: 99 */         arr[i] = cce;
/*  79:100 */         return true;
/*  80:    */       }
/*  81:    */     }
/*  82:103 */     throw new IllegalStateException("No empty space found");
/*  83:    */   }
/*  84:    */   
/*  85:    */   public boolean remove(CellCacheEntry cce)
/*  86:    */   {
/*  87:107 */     FormulaCellCacheEntry[] arr = this._arr;
/*  88:109 */     if ((this._size * 3 < this._arr.length) && (this._arr.length > 8))
/*  89:    */     {
/*  90:111 */       boolean found = false;
/*  91:112 */       FormulaCellCacheEntry[] prevArr = this._arr;
/*  92:113 */       FormulaCellCacheEntry[] newArr = new FormulaCellCacheEntry[this._arr.length / 2];
/*  93:114 */       for (int i = 0; i < prevArr.length; i++)
/*  94:    */       {
/*  95:115 */         FormulaCellCacheEntry prevCce = this._arr[i];
/*  96:116 */         if (prevCce != null) {
/*  97:117 */           if (prevCce == cce)
/*  98:    */           {
/*  99:118 */             found = true;
/* 100:119 */             this._size -= 1;
/* 101:    */           }
/* 102:    */           else
/* 103:    */           {
/* 104:123 */             addInternal(newArr, prevCce);
/* 105:    */           }
/* 106:    */         }
/* 107:    */       }
/* 108:126 */       this._arr = newArr;
/* 109:127 */       return found;
/* 110:    */     }
/* 111:132 */     int startIx = Math.abs(cce.hashCode() % arr.length);
/* 112:135 */     for (int i = startIx; i < arr.length; i++)
/* 113:    */     {
/* 114:136 */       FormulaCellCacheEntry item = arr[i];
/* 115:137 */       if (item == cce)
/* 116:    */       {
/* 117:139 */         arr[i] = null;
/* 118:140 */         this._size -= 1;
/* 119:141 */         return true;
/* 120:    */       }
/* 121:    */     }
/* 122:144 */     for (int i = 0; i < startIx; i++)
/* 123:    */     {
/* 124:145 */       FormulaCellCacheEntry item = arr[i];
/* 125:146 */       if (item == cce)
/* 126:    */       {
/* 127:148 */         arr[i] = null;
/* 128:149 */         this._size -= 1;
/* 129:150 */         return true;
/* 130:    */       }
/* 131:    */     }
/* 132:153 */     return false;
/* 133:    */   }
/* 134:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.FormulaCellCacheEntrySet
 * JD-Core Version:    0.7.0.1
 */