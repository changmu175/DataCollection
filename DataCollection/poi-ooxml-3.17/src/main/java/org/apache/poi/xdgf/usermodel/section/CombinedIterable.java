/*  1:   */ package org.apache.poi.xdgf.usermodel.section;
/*  2:   */ 
/*  3:   */ import java.util.Collections;
/*  4:   */ import java.util.Iterator;
/*  5:   */ import java.util.Map.Entry;
/*  6:   */ import java.util.NoSuchElementException;
/*  7:   */ import java.util.Set;
/*  8:   */ import java.util.SortedMap;
/*  9:   */ 
/* 10:   */ public class CombinedIterable<T>
/* 11:   */   implements Iterable<T>
/* 12:   */ {
/* 13:   */   final SortedMap<Long, T> _baseItems;
/* 14:   */   final SortedMap<Long, T> _masterItems;
/* 15:   */   
/* 16:   */   public CombinedIterable(SortedMap<Long, T> baseItems, SortedMap<Long, T> masterItems)
/* 17:   */   {
/* 18:39 */     this._baseItems = baseItems;
/* 19:40 */     this._masterItems = masterItems;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Iterator<T> iterator()
/* 23:   */   {
/* 24:   */     Iterator<Map.Entry<Long, T>> vmasterI;
/* 25:   */     final Iterator<Map.Entry<Long, T>> vmasterI;
/* 26:48 */     if (this._masterItems != null)
/* 27:   */     {
/* 28:49 */       vmasterI = this._masterItems.entrySet().iterator();
/* 29:   */     }
/* 30:   */     else
/* 31:   */     {
/* 32:51 */       Set<Map.Entry<Long, T>> empty = Collections.emptySet();
/* 33:52 */       vmasterI = empty.iterator();
/* 34:   */     }
/* 35:55 */     new Iterator()
/* 36:   */     {
/* 37:57 */       Long lastI = Long.valueOf(-9223372036854775808L);
/* 38:59 */       Map.Entry<Long, T> currentBase = null;
/* 39:60 */       Map.Entry<Long, T> currentMaster = null;
/* 40:63 */       Iterator<Map.Entry<Long, T>> baseI = CombinedIterable.this._baseItems.entrySet().iterator();
/* 41:64 */       Iterator<Map.Entry<Long, T>> masterI = vmasterI;
/* 42:   */       
/* 43:   */       public boolean hasNext()
/* 44:   */       {
/* 45:68 */         return (this.currentBase != null) || (this.currentMaster != null) || (this.baseI.hasNext()) || (this.masterI.hasNext());
/* 46:   */       }
/* 47:   */       
/* 48:   */       public T next()
/* 49:   */       {
/* 50:77 */         long baseIdx = 9223372036854775807L;
/* 51:78 */         long masterIdx = 9223372036854775807L;
/* 52:80 */         if (this.currentBase == null)
/* 53:   */         {
/* 54:   */           do
/* 55:   */           {
/* 56:81 */             if (!this.baseI.hasNext()) {
/* 57:   */               break;
/* 58:   */             }
/* 59:82 */             this.currentBase = ((Map.Entry)this.baseI.next());
/* 60:83 */           } while (((Long)this.currentBase.getKey()).longValue() <= this.lastI.longValue());
/* 61:84 */           baseIdx = ((Long)this.currentBase.getKey()).longValue();
/* 62:   */         }
/* 63:   */         else
/* 64:   */         {
/* 65:89 */           baseIdx = ((Long)this.currentBase.getKey()).longValue();
/* 66:   */         }
/* 67:92 */         if (this.currentMaster == null)
/* 68:   */         {
/* 69:   */           do
/* 70:   */           {
/* 71:93 */             if (!this.masterI.hasNext()) {
/* 72:   */               break;
/* 73:   */             }
/* 74:94 */             this.currentMaster = ((Map.Entry)this.masterI.next());
/* 75:95 */           } while (((Long)this.currentMaster.getKey()).longValue() <= this.lastI.longValue());
/* 76:96 */           masterIdx = ((Long)this.currentMaster.getKey()).longValue();
/* 77:   */         }
/* 78:   */         else
/* 79:   */         {
/* 80::1 */           masterIdx = ((Long)this.currentMaster.getKey()).longValue();
/* 81:   */         }
/* 82::6 */         if (this.currentBase != null)
/* 83:   */         {
/* 84::8 */           if (baseIdx <= masterIdx)
/* 85:   */           {
/* 86::9 */             this.lastI = Long.valueOf(baseIdx);
/* 87:;0 */             T val = this.currentBase.getValue();
/* 88:;3 */             if (masterIdx == baseIdx) {
/* 89:;4 */               this.currentMaster = null;
/* 90:   */             }
/* 91:;7 */             this.currentBase = null;
/* 92:   */           }
/* 93:   */           else
/* 94:   */           {
/* 95:<0 */             this.lastI = Long.valueOf(masterIdx);
/* 96:<1 */             T val = this.currentMaster != null ? this.currentMaster.getValue() : null;
/* 97:<2 */             this.currentMaster = null;
/* 98:   */           }
/* 99:   */         }
/* :0:<5 */         else if (this.currentMaster != null)
/* :1:   */         {
/* :2:<6 */           this.lastI = ((Long)this.currentMaster.getKey());
/* :3:<7 */           T val = this.currentMaster.getValue();
/* :4:   */           
/* :5:<9 */           this.currentMaster = null;
/* :6:   */         }
/* :7:   */         else
/* :8:   */         {
/* :9:=1 */           throw new NoSuchElementException();
/* ;0:   */         }
/* ;1:   */         T val;
/* ;2:=4 */         return val;
/* ;3:   */       }
/* ;4:   */       
/* ;5:   */       public void remove()
/* ;6:   */       {
/* ;7:=9 */         throw new UnsupportedOperationException();
/* ;8:   */       }
/* ;9:   */     };
/* <0:   */   }
/* <1:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.CombinedIterable
 * JD-Core Version:    0.7.0.1
 */