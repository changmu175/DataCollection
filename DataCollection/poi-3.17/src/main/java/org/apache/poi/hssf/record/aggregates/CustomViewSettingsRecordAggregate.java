/*  1:   */ package org.apache.poi.hssf.record.aggregates;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.apache.poi.hssf.model.RecordStream;
/*  6:   */ import org.apache.poi.hssf.record.HeaderFooterRecord;
/*  7:   */ import org.apache.poi.hssf.record.Record;
/*  8:   */ import org.apache.poi.hssf.record.RecordBase;
/*  9:   */ 
/* 10:   */ public final class CustomViewSettingsRecordAggregate
/* 11:   */   extends RecordAggregate
/* 12:   */ {
/* 13:   */   private final Record _begin;
/* 14:   */   private final Record _end;
/* 15:   */   private final List<RecordBase> _recs;
/* 16:   */   private PageSettingsBlock _psBlock;
/* 17:   */   
/* 18:   */   public CustomViewSettingsRecordAggregate(RecordStream rs)
/* 19:   */   {
/* 20:41 */     this._begin = rs.getNext();
/* 21:42 */     if (this._begin.getSid() != 426) {
/* 22:43 */       throw new IllegalStateException("Bad begin record");
/* 23:   */     }
/* 24:45 */     List<RecordBase> temp = new ArrayList();
/* 25:46 */     while (rs.peekNextSid() != 427) {
/* 26:47 */       if (PageSettingsBlock.isComponentRecord(rs.peekNextSid()))
/* 27:   */       {
/* 28:48 */         if (this._psBlock != null)
/* 29:   */         {
/* 30:49 */           if (rs.peekNextSid() == 2204) {
/* 31:51 */             this._psBlock.addLateHeaderFooter((HeaderFooterRecord)rs.getNext());
/* 32:   */           } else {
/* 33:54 */             throw new IllegalStateException("Found more than one PageSettingsBlock in chart sub-stream, had sid: " + rs.peekNextSid());
/* 34:   */           }
/* 35:   */         }
/* 36:   */         else
/* 37:   */         {
/* 38:57 */           this._psBlock = new PageSettingsBlock(rs);
/* 39:58 */           temp.add(this._psBlock);
/* 40:   */         }
/* 41:   */       }
/* 42:   */       else {
/* 43:61 */         temp.add(rs.getNext());
/* 44:   */       }
/* 45:   */     }
/* 46:63 */     this._recs = temp;
/* 47:64 */     this._end = rs.getNext();
/* 48:65 */     if (this._end.getSid() != 427) {
/* 49:66 */       throw new IllegalStateException("Bad custom view settings end record");
/* 50:   */     }
/* 51:   */   }
/* 52:   */   
/* 53:   */   public void visitContainedRecords(RecordVisitor rv)
/* 54:   */   {
/* 55:71 */     if (this._recs.isEmpty()) {
/* 56:72 */       return;
/* 57:   */     }
/* 58:74 */     rv.visitRecord(this._begin);
/* 59:75 */     for (int i = 0; i < this._recs.size(); i++)
/* 60:   */     {
/* 61:76 */       RecordBase rb = (RecordBase)this._recs.get(i);
/* 62:77 */       if ((rb instanceof RecordAggregate)) {
/* 63:78 */         ((RecordAggregate)rb).visitContainedRecords(rv);
/* 64:   */       } else {
/* 65:80 */         rv.visitRecord((Record)rb);
/* 66:   */       }
/* 67:   */     }
/* 68:83 */     rv.visitRecord(this._end);
/* 69:   */   }
/* 70:   */   
/* 71:   */   public static boolean isBeginRecord(int sid)
/* 72:   */   {
/* 73:87 */     return sid == 426;
/* 74:   */   }
/* 75:   */   
/* 76:   */   public void append(RecordBase r)
/* 77:   */   {
/* 78:91 */     this._recs.add(r);
/* 79:   */   }
/* 80:   */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.aggregates.CustomViewSettingsRecordAggregate

 * JD-Core Version:    0.7.0.1

 */