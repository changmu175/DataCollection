/*   1:    */ package org.apache.poi.hssf.model;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.hssf.record.ArrayRecord;
/*   6:    */ import org.apache.poi.hssf.record.FormulaRecord;
/*   7:    */ import org.apache.poi.hssf.record.MergeCellsRecord;
/*   8:    */ import org.apache.poi.hssf.record.Record;
/*   9:    */ import org.apache.poi.hssf.record.SharedFormulaRecord;
/*  10:    */ import org.apache.poi.hssf.record.TableRecord;
/*  11:    */ import org.apache.poi.hssf.record.aggregates.SharedValueManager;
/*  12:    */ import org.apache.poi.ss.util.CellReference;
/*  13:    */ 
/*  14:    */ public final class RowBlocksReader
/*  15:    */ {
/*  16:    */   private final List<Record> _plainRecords;
/*  17:    */   private final SharedValueManager _sfm;
/*  18:    */   private final MergeCellsRecord[] _mergedCellsRecords;
/*  19:    */   
/*  20:    */   public RowBlocksReader(RecordStream rs)
/*  21:    */   {
/*  22: 50 */     List<Record> plainRecords = new ArrayList();
/*  23: 51 */     List<Record> shFrmRecords = new ArrayList();
/*  24: 52 */     List<CellReference> firstCellRefs = new ArrayList();
/*  25: 53 */     List<Record> arrayRecords = new ArrayList();
/*  26: 54 */     List<Record> tableRecords = new ArrayList();
/*  27: 55 */     List<Record> mergeCellRecords = new ArrayList();
/*  28:    */     
/*  29: 57 */     Record prevRec = null;
/*  30: 58 */     while (!RecordOrderer.isEndOfRowBlock(rs.peekNextSid()))
/*  31:    */     {
/*  32: 63 */       if (!rs.hasNext()) {
/*  33: 64 */         throw new RuntimeException("Failed to find end of row/cell records");
/*  34:    */       }
/*  35: 67 */       Record rec = rs.getNext();
/*  36:    */       List<Record> dest;
/*  37: 69 */       switch (rec.getSid())
/*  38:    */       {
/*  39:    */       case 229: 
/*  40: 70 */         dest = mergeCellRecords; break;
/*  41:    */       case 1212: 
/*  42: 71 */         dest = shFrmRecords;
/*  43: 72 */         if (!(prevRec instanceof FormulaRecord)) {
/*  44: 73 */           throw new RuntimeException("Shared formula record should follow a FormulaRecord");
/*  45:    */         }
/*  46: 75 */         FormulaRecord fr = (FormulaRecord)prevRec;
/*  47: 76 */         firstCellRefs.add(new CellReference(fr.getRow(), fr.getColumn()));
/*  48: 77 */         break;
/*  49:    */       case 545: 
/*  50: 78 */         dest = arrayRecords; break;
/*  51:    */       case 566: 
/*  52: 79 */         dest = tableRecords; break;
/*  53:    */       default: 
/*  54: 80 */         dest = plainRecords;
/*  55:    */       }
/*  56: 82 */       dest.add(rec);
/*  57: 83 */       prevRec = rec;
/*  58:    */     }
/*  59: 85 */     SharedFormulaRecord[] sharedFormulaRecs = new SharedFormulaRecord[shFrmRecords.size()];
/*  60: 86 */     CellReference[] firstCells = new CellReference[firstCellRefs.size()];
/*  61: 87 */     ArrayRecord[] arrayRecs = new ArrayRecord[arrayRecords.size()];
/*  62: 88 */     TableRecord[] tableRecs = new TableRecord[tableRecords.size()];
/*  63: 89 */     shFrmRecords.toArray(sharedFormulaRecs);
/*  64: 90 */     firstCellRefs.toArray(firstCells);
/*  65: 91 */     arrayRecords.toArray(arrayRecs);
/*  66: 92 */     tableRecords.toArray(tableRecs);
/*  67:    */     
/*  68: 94 */     this._plainRecords = plainRecords;
/*  69: 95 */     this._sfm = SharedValueManager.create(sharedFormulaRecs, firstCells, arrayRecs, tableRecs);
/*  70: 96 */     this._mergedCellsRecords = new MergeCellsRecord[mergeCellRecords.size()];
/*  71: 97 */     mergeCellRecords.toArray(this._mergedCellsRecords);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public MergeCellsRecord[] getLooseMergedCells()
/*  75:    */   {
/*  76:106 */     return this._mergedCellsRecords;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public SharedValueManager getSharedFormulaManager()
/*  80:    */   {
/*  81:110 */     return this._sfm;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public RecordStream getPlainRecordStream()
/*  85:    */   {
/*  86:117 */     return new RecordStream(this._plainRecords, 0);
/*  87:    */   }
/*  88:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.model.RowBlocksReader
 * JD-Core Version:    0.7.0.1
 */