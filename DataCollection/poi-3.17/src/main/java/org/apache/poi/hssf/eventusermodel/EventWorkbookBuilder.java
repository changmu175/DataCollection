/*   1:    */ package org.apache.poi.hssf.eventusermodel;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.hssf.model.InternalWorkbook;
/*   6:    */ import org.apache.poi.hssf.record.BoundSheetRecord;
/*   7:    */ import org.apache.poi.hssf.record.EOFRecord;
/*   8:    */ import org.apache.poi.hssf.record.ExternSheetRecord;
/*   9:    */ import org.apache.poi.hssf.record.Record;
/*  10:    */ import org.apache.poi.hssf.record.SSTRecord;
/*  11:    */ import org.apache.poi.hssf.record.SupBookRecord;
/*  12:    */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*  13:    */ 
/*  14:    */ public class EventWorkbookBuilder
/*  15:    */ {
/*  16:    */   public static InternalWorkbook createStubWorkbook(ExternSheetRecord[] externs, BoundSheetRecord[] bounds, SSTRecord sst)
/*  17:    */   {
/*  18: 68 */     List<Record> wbRecords = new ArrayList();
/*  19: 71 */     if (bounds != null) {
/*  20: 72 */       for (BoundSheetRecord bound : bounds) {
/*  21: 73 */         wbRecords.add(bound);
/*  22:    */       }
/*  23:    */     }
/*  24: 76 */     if (sst != null) {
/*  25: 77 */       wbRecords.add(sst);
/*  26:    */     }
/*  27: 82 */     if (externs != null)
/*  28:    */     {
/*  29: 83 */       wbRecords.add(SupBookRecord.createInternalReferences((short)externs.length));
/*  30: 85 */       for (ExternSheetRecord extern : externs) {
/*  31: 86 */         wbRecords.add(extern);
/*  32:    */       }
/*  33:    */     }
/*  34: 91 */     wbRecords.add(EOFRecord.instance);
/*  35:    */     
/*  36: 93 */     return InternalWorkbook.createWorkbook(wbRecords);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static InternalWorkbook createStubWorkbook(ExternSheetRecord[] externs, BoundSheetRecord[] bounds)
/*  40:    */   {
/*  41:105 */     return createStubWorkbook(externs, bounds, null);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public static class SheetRecordCollectingListener
/*  45:    */     implements HSSFListener
/*  46:    */   {
/*  47:    */     private final HSSFListener childListener;
/*  48:117 */     private final List<BoundSheetRecord> boundSheetRecords = new ArrayList();
/*  49:118 */     private final List<ExternSheetRecord> externSheetRecords = new ArrayList();
/*  50:119 */     private SSTRecord sstRecord = null;
/*  51:    */     
/*  52:    */     public SheetRecordCollectingListener(HSSFListener childListener)
/*  53:    */     {
/*  54:122 */       this.childListener = childListener;
/*  55:    */     }
/*  56:    */     
/*  57:    */     public BoundSheetRecord[] getBoundSheetRecords()
/*  58:    */     {
/*  59:127 */       return (BoundSheetRecord[])this.boundSheetRecords.toArray(new BoundSheetRecord[this.boundSheetRecords.size()]);
/*  60:    */     }
/*  61:    */     
/*  62:    */     public ExternSheetRecord[] getExternSheetRecords()
/*  63:    */     {
/*  64:132 */       return (ExternSheetRecord[])this.externSheetRecords.toArray(new ExternSheetRecord[this.externSheetRecords.size()]);
/*  65:    */     }
/*  66:    */     
/*  67:    */     public SSTRecord getSSTRecord()
/*  68:    */     {
/*  69:137 */       return this.sstRecord;
/*  70:    */     }
/*  71:    */     
/*  72:    */     public HSSFWorkbook getStubHSSFWorkbook()
/*  73:    */     {
/*  74:142 */       HSSFWorkbook wb = HSSFWorkbook.create(getStubWorkbook());
/*  75:144 */       for (BoundSheetRecord bsr : this.boundSheetRecords) {
/*  76:145 */         wb.createSheet(bsr.getSheetname());
/*  77:    */       }
/*  78:148 */       return wb;
/*  79:    */     }
/*  80:    */     
/*  81:    */     public InternalWorkbook getStubWorkbook()
/*  82:    */     {
/*  83:151 */       return EventWorkbookBuilder.createStubWorkbook(getExternSheetRecords(), getBoundSheetRecords(), getSSTRecord());
/*  84:    */     }
/*  85:    */     
/*  86:    */     public void processRecord(Record record)
/*  87:    */     {
/*  88:165 */       processRecordInternally(record);
/*  89:    */       
/*  90:    */ 
/*  91:168 */       this.childListener.processRecord(record);
/*  92:    */     }
/*  93:    */     
/*  94:    */     public void processRecordInternally(Record record)
/*  95:    */     {
/*  96:178 */       if ((record instanceof BoundSheetRecord)) {
/*  97:179 */         this.boundSheetRecords.add((BoundSheetRecord)record);
/*  98:181 */       } else if ((record instanceof ExternSheetRecord)) {
/*  99:182 */         this.externSheetRecords.add((ExternSheetRecord)record);
/* 100:184 */       } else if ((record instanceof SSTRecord)) {
/* 101:185 */         this.sstRecord = ((SSTRecord)record);
/* 102:    */       }
/* 103:    */     }
/* 104:    */   }
/* 105:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder
 * JD-Core Version:    0.7.0.1
 */