/*  1:   */ package org.apache.poi.hssf.record.aggregates;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.apache.poi.hssf.model.RecordStream;
/*  6:   */ import org.apache.poi.hssf.record.BOFRecord;
/*  7:   */ import org.apache.poi.hssf.record.EOFRecord;
/*  8:   */ import org.apache.poi.hssf.record.HeaderFooterRecord;
/*  9:   */ import org.apache.poi.hssf.record.Record;
/* 10:   */ import org.apache.poi.hssf.record.RecordBase;
/* 11:   */ 
/* 12:   */ public final class ChartSubstreamRecordAggregate
/* 13:   */   extends RecordAggregate
/* 14:   */ {
/* 15:   */   private final BOFRecord _bofRec;
/* 16:   */   private final List<RecordBase> _recs;
/* 17:   */   private PageSettingsBlock _psBlock;
/* 18:   */   
/* 19:   */   public ChartSubstreamRecordAggregate(RecordStream rs)
/* 20:   */   {
/* 21:40 */     this._bofRec = ((BOFRecord)rs.getNext());
/* 22:41 */     List<RecordBase> temp = new ArrayList();
/* 23:42 */     while (rs.peekNextClass() != EOFRecord.class) {
/* 24:43 */       if (PageSettingsBlock.isComponentRecord(rs.peekNextSid()))
/* 25:   */       {
/* 26:44 */         if (this._psBlock != null)
/* 27:   */         {
/* 28:45 */           if (rs.peekNextSid() == 2204) {
/* 29:47 */             this._psBlock.addLateHeaderFooter((HeaderFooterRecord)rs.getNext());
/* 30:   */           } else {
/* 31:50 */             throw new IllegalStateException("Found more than one PageSettingsBlock in chart sub-stream, had sid: " + rs.peekNextSid());
/* 32:   */           }
/* 33:   */         }
/* 34:   */         else
/* 35:   */         {
/* 36:53 */           this._psBlock = new PageSettingsBlock(rs);
/* 37:54 */           temp.add(this._psBlock);
/* 38:   */         }
/* 39:   */       }
/* 40:   */       else {
/* 41:57 */         temp.add(rs.getNext());
/* 42:   */       }
/* 43:   */     }
/* 44:59 */     this._recs = temp;
/* 45:60 */     Record eof = rs.getNext();
/* 46:61 */     if (!(eof instanceof EOFRecord)) {
/* 47:62 */       throw new IllegalStateException("Bad chart EOF");
/* 48:   */     }
/* 49:   */   }
/* 50:   */   
/* 51:   */   public void visitContainedRecords(RecordVisitor rv)
/* 52:   */   {
/* 53:67 */     if (this._recs.isEmpty()) {
/* 54:68 */       return;
/* 55:   */     }
/* 56:70 */     rv.visitRecord(this._bofRec);
/* 57:71 */     for (int i = 0; i < this._recs.size(); i++)
/* 58:   */     {
/* 59:72 */       RecordBase rb = (RecordBase)this._recs.get(i);
/* 60:73 */       if ((rb instanceof RecordAggregate)) {
/* 61:74 */         ((RecordAggregate)rb).visitContainedRecords(rv);
/* 62:   */       } else {
/* 63:76 */         rv.visitRecord((Record)rb);
/* 64:   */       }
/* 65:   */     }
/* 66:79 */     rv.visitRecord(EOFRecord.instance);
/* 67:   */   }
/* 68:   */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.aggregates.ChartSubstreamRecordAggregate

 * JD-Core Version:    0.7.0.1

 */