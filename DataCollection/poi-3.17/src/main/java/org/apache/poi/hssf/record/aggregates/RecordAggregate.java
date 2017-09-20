/*   1:    */ package org.apache.poi.hssf.record.aggregates;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.Record;
/*   4:    */ import org.apache.poi.hssf.record.RecordBase;
/*   5:    */ 
/*   6:    */ public abstract class RecordAggregate
/*   7:    */   extends RecordBase
/*   8:    */ {
/*   9:    */   public abstract void visitContainedRecords(RecordVisitor paramRecordVisitor);
/*  10:    */   
/*  11:    */   public final int serialize(int offset, byte[] data)
/*  12:    */   {
/*  13: 41 */     SerializingRecordVisitor srv = new SerializingRecordVisitor(data, offset);
/*  14: 42 */     visitContainedRecords(srv);
/*  15: 43 */     return srv.countBytesWritten();
/*  16:    */   }
/*  17:    */   
/*  18:    */   public int getRecordSize()
/*  19:    */   {
/*  20: 46 */     RecordSizingVisitor rsv = new RecordSizingVisitor();
/*  21: 47 */     visitContainedRecords(rsv);
/*  22: 48 */     return rsv.getTotalSize();
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static abstract interface RecordVisitor
/*  26:    */   {
/*  27:    */     public abstract void visitRecord(Record paramRecord);
/*  28:    */   }
/*  29:    */   
/*  30:    */   private static final class SerializingRecordVisitor
/*  31:    */     implements RecordVisitor
/*  32:    */   {
/*  33:    */     private final byte[] _data;
/*  34:    */     private final int _startOffset;
/*  35:    */     private int _countBytesWritten;
/*  36:    */     
/*  37:    */     public SerializingRecordVisitor(byte[] data, int startOffset)
/*  38:    */     {
/*  39: 66 */       this._data = data;
/*  40: 67 */       this._startOffset = startOffset;
/*  41: 68 */       this._countBytesWritten = 0;
/*  42:    */     }
/*  43:    */     
/*  44:    */     public int countBytesWritten()
/*  45:    */     {
/*  46: 71 */       return this._countBytesWritten;
/*  47:    */     }
/*  48:    */     
/*  49:    */     public void visitRecord(Record r)
/*  50:    */     {
/*  51: 74 */       int currentOffset = this._startOffset + this._countBytesWritten;
/*  52: 75 */       this._countBytesWritten += r.serialize(currentOffset, this._data);
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   private static final class RecordSizingVisitor
/*  57:    */     implements RecordVisitor
/*  58:    */   {
/*  59:    */     private int _totalSize;
/*  60:    */     
/*  61:    */     public RecordSizingVisitor()
/*  62:    */     {
/*  63: 83 */       this._totalSize = 0;
/*  64:    */     }
/*  65:    */     
/*  66:    */     public int getTotalSize()
/*  67:    */     {
/*  68: 86 */       return this._totalSize;
/*  69:    */     }
/*  70:    */     
/*  71:    */     public void visitRecord(Record r)
/*  72:    */     {
/*  73: 89 */       this._totalSize += r.getRecordSize();
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static final class PositionTrackingVisitor
/*  78:    */     implements RecordVisitor
/*  79:    */   {
/*  80:    */     private final RecordVisitor _rv;
/*  81:    */     private int _position;
/*  82:    */     
/*  83:    */     public PositionTrackingVisitor(RecordVisitor rv, int initialPosition)
/*  84:    */     {
/*  85:101 */       this._rv = rv;
/*  86:102 */       this._position = initialPosition;
/*  87:    */     }
/*  88:    */     
/*  89:    */     public void visitRecord(Record r)
/*  90:    */     {
/*  91:105 */       this._position += r.getRecordSize();
/*  92:106 */       this._rv.visitRecord(r);
/*  93:    */     }
/*  94:    */     
/*  95:    */     public void setPosition(int position)
/*  96:    */     {
/*  97:109 */       this._position = position;
/*  98:    */     }
/*  99:    */     
/* 100:    */     public int getPosition()
/* 101:    */     {
/* 102:112 */       return this._position;
/* 103:    */     }
/* 104:    */   }
/* 105:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.aggregates.RecordAggregate

 * JD-Core Version:    0.7.0.1

 */