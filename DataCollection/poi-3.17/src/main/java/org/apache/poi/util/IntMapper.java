/*  1:   */ package org.apache.poi.util;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.HashMap;
/*  5:   */ import java.util.Iterator;
/*  6:   */ import java.util.List;
/*  7:   */ import java.util.Map;
/*  8:   */ 
/*  9:   */ public class IntMapper<T>
/* 10:   */ {
/* 11:   */   private List<T> elements;
/* 12:   */   private Map<T, Integer> valueKeyMap;
/* 13:   */   private static final int _default_size = 10;
/* 14:   */   
/* 15:   */   public IntMapper()
/* 16:   */   {
/* 17:50 */     this(10);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public IntMapper(int initialCapacity)
/* 21:   */   {
/* 22:55 */     this.elements = new ArrayList(initialCapacity);
/* 23:56 */     this.valueKeyMap = new HashMap(initialCapacity);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public boolean add(T value)
/* 27:   */   {
/* 28:69 */     int index = this.elements.size();
/* 29:70 */     this.elements.add(value);
/* 30:71 */     this.valueKeyMap.put(value, Integer.valueOf(index));
/* 31:72 */     return true;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public int size()
/* 35:   */   {
/* 36:76 */     return this.elements.size();
/* 37:   */   }
/* 38:   */   
/* 39:   */   public T get(int index)
/* 40:   */   {
/* 41:80 */     return this.elements.get(index);
/* 42:   */   }
/* 43:   */   
/* 44:   */   public int getIndex(T o)
/* 45:   */   {
/* 46:84 */     Integer i = (Integer)this.valueKeyMap.get(o);
/* 47:85 */     if (i == null) {
/* 48:86 */       return -1;
/* 49:   */     }
/* 50:87 */     return i.intValue();
/* 51:   */   }
/* 52:   */   
/* 53:   */   public Iterator<T> iterator()
/* 54:   */   {
/* 55:91 */     return this.elements.iterator();
/* 56:   */   }
/* 57:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.IntMapper
 * JD-Core Version:    0.7.0.1
 */