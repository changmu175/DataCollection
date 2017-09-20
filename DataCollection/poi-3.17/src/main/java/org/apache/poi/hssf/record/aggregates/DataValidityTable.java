/*  1:   */ package org.apache.poi.hssf.record.aggregates;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.apache.poi.hssf.model.RecordStream;
/*  6:   */ import org.apache.poi.hssf.record.DVALRecord;
/*  7:   */ import org.apache.poi.hssf.record.DVRecord;
/*  8:   */ import org.apache.poi.hssf.record.Record;
/*  9:   */ 
/* 10:   */ public final class DataValidityTable
/* 11:   */   extends RecordAggregate
/* 12:   */ {
/* 13:   */   private final DVALRecord _headerRec;
/* 14:   */   private final List<DVRecord> _validationList;
/* 15:   */   
/* 16:   */   public DataValidityTable(RecordStream rs)
/* 17:   */   {
/* 18:41 */     this._headerRec = ((DVALRecord)rs.getNext());
/* 19:42 */     List<DVRecord> temp = new ArrayList();
/* 20:43 */     while (rs.peekNextClass() == DVRecord.class) {
/* 21:44 */       temp.add((DVRecord)rs.getNext());
/* 22:   */     }
/* 23:46 */     this._validationList = temp;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public DataValidityTable()
/* 27:   */   {
/* 28:50 */     this._headerRec = new DVALRecord();
/* 29:51 */     this._validationList = new ArrayList();
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void visitContainedRecords(RecordVisitor rv)
/* 33:   */   {
/* 34:55 */     if (this._validationList.isEmpty()) {
/* 35:56 */       return;
/* 36:   */     }
/* 37:58 */     rv.visitRecord(this._headerRec);
/* 38:59 */     for (int i = 0; i < this._validationList.size(); i++) {
/* 39:60 */       rv.visitRecord((Record)this._validationList.get(i));
/* 40:   */     }
/* 41:   */   }
/* 42:   */   
/* 43:   */   public void addDataValidation(DVRecord dvRecord)
/* 44:   */   {
/* 45:65 */     this._validationList.add(dvRecord);
/* 46:66 */     this._headerRec.setDVRecNo(this._validationList.size());
/* 47:   */   }
/* 48:   */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.aggregates.DataValidityTable

 * JD-Core Version:    0.7.0.1

 */