/*  1:   */ package org.apache.poi.hssf.record.aggregates;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.apache.poi.hssf.model.RecordStream;
/*  6:   */ import org.apache.poi.hssf.record.CFHeader12Record;
/*  7:   */ import org.apache.poi.hssf.record.CFHeaderBase;
/*  8:   */ import org.apache.poi.hssf.record.CFHeaderRecord;
/*  9:   */ import org.apache.poi.ss.formula.FormulaShifter;
/* 10:   */ 
/* 11:   */ public final class ConditionalFormattingTable
/* 12:   */   extends RecordAggregate
/* 13:   */ {
/* 14:   */   private final List<CFRecordsAggregate> _cfHeaders;
/* 15:   */   
/* 16:   */   public ConditionalFormattingTable()
/* 17:   */   {
/* 18:40 */     this._cfHeaders = new ArrayList();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public ConditionalFormattingTable(RecordStream rs)
/* 22:   */   {
/* 23:45 */     List<CFRecordsAggregate> temp = new ArrayList();
/* 24:47 */     while ((rs.peekNextClass() == CFHeaderRecord.class) || (rs.peekNextClass() == CFHeader12Record.class)) {
/* 25:48 */       temp.add(CFRecordsAggregate.createCFAggregate(rs));
/* 26:   */     }
/* 27:50 */     this._cfHeaders = temp;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void visitContainedRecords(RecordVisitor rv)
/* 31:   */   {
/* 32:54 */     for (CFRecordsAggregate subAgg : this._cfHeaders) {
/* 33:55 */       subAgg.visitContainedRecords(rv);
/* 34:   */     }
/* 35:   */   }
/* 36:   */   
/* 37:   */   public int add(CFRecordsAggregate cfAggregate)
/* 38:   */   {
/* 39:63 */     cfAggregate.getHeader().setID(this._cfHeaders.size());
/* 40:64 */     this._cfHeaders.add(cfAggregate);
/* 41:65 */     return this._cfHeaders.size() - 1;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public int size()
/* 45:   */   {
/* 46:69 */     return this._cfHeaders.size();
/* 47:   */   }
/* 48:   */   
/* 49:   */   public CFRecordsAggregate get(int index)
/* 50:   */   {
/* 51:73 */     checkIndex(index);
/* 52:74 */     return (CFRecordsAggregate)this._cfHeaders.get(index);
/* 53:   */   }
/* 54:   */   
/* 55:   */   public void remove(int index)
/* 56:   */   {
/* 57:78 */     checkIndex(index);
/* 58:79 */     this._cfHeaders.remove(index);
/* 59:   */   }
/* 60:   */   
/* 61:   */   private void checkIndex(int index)
/* 62:   */   {
/* 63:83 */     if ((index < 0) || (index >= this._cfHeaders.size())) {
/* 64:84 */       throw new IllegalArgumentException("Specified CF index " + index + " is outside the allowable range (0.." + (this._cfHeaders.size() - 1) + ")");
/* 65:   */     }
/* 66:   */   }
/* 67:   */   
/* 68:   */   public void updateFormulasAfterCellShift(FormulaShifter shifter, int externSheetIndex)
/* 69:   */   {
/* 70:90 */     for (int i = 0; i < this._cfHeaders.size(); i++)
/* 71:   */     {
/* 72:91 */       CFRecordsAggregate subAgg = (CFRecordsAggregate)this._cfHeaders.get(i);
/* 73:92 */       boolean shouldKeep = subAgg.updateFormulasAfterCellShift(shifter, externSheetIndex);
/* 74:93 */       if (!shouldKeep)
/* 75:   */       {
/* 76:94 */         this._cfHeaders.remove(i);
/* 77:95 */         i--;
/* 78:   */       }
/* 79:   */     }
/* 80:   */   }
/* 81:   */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.aggregates.ConditionalFormattingTable

 * JD-Core Version:    0.7.0.1

 */