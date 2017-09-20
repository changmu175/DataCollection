/*  1:   */ package org.apache.poi.hssf.model;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import org.apache.poi.hssf.record.Record;
/*  5:   */ 
/*  6:   */ public final class RecordStream
/*  7:   */ {
/*  8:   */   private final List<Record> _list;
/*  9:   */   private int _nextIndex;
/* 10:   */   private int _countRead;
/* 11:   */   private final int _endIx;
/* 12:   */   
/* 13:   */   public RecordStream(List<Record> inputList, int startIndex, int endIx)
/* 14:   */   {
/* 15:41 */     this._list = inputList;
/* 16:42 */     this._nextIndex = startIndex;
/* 17:43 */     this._endIx = endIx;
/* 18:44 */     this._countRead = 0;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public RecordStream(List<Record> records, int startIx)
/* 22:   */   {
/* 23:48 */     this(records, startIx, records.size());
/* 24:   */   }
/* 25:   */   
/* 26:   */   public boolean hasNext()
/* 27:   */   {
/* 28:52 */     return this._nextIndex < this._endIx;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public Record getNext()
/* 32:   */   {
/* 33:56 */     if (!hasNext()) {
/* 34:57 */       throw new RuntimeException("Attempt to read past end of record stream");
/* 35:   */     }
/* 36:59 */     this._countRead += 1;
/* 37:60 */     return (Record)this._list.get(this._nextIndex++);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public Class<? extends Record> peekNextClass()
/* 41:   */   {
/* 42:67 */     if (!hasNext()) {
/* 43:68 */       return null;
/* 44:   */     }
/* 45:70 */     return ((Record)this._list.get(this._nextIndex)).getClass();
/* 46:   */   }
/* 47:   */   
/* 48:   */   public int peekNextSid()
/* 49:   */   {
/* 50:77 */     if (!hasNext()) {
/* 51:78 */       return -1;
/* 52:   */     }
/* 53:80 */     return ((Record)this._list.get(this._nextIndex)).getSid();
/* 54:   */   }
/* 55:   */   
/* 56:   */   public int getCountRead()
/* 57:   */   {
/* 58:84 */     return this._countRead;
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.model.RecordStream
 * JD-Core Version:    0.7.0.1
 */