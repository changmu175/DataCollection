/*   1:    */ package org.apache.poi.hssf.eventusermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
/*   4:    */ import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
/*   5:    */ import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingRowDummyRecord;
/*   6:    */ import org.apache.poi.hssf.record.BOFRecord;
/*   7:    */ import org.apache.poi.hssf.record.CellValueRecordInterface;
/*   8:    */ import org.apache.poi.hssf.record.MulBlankRecord;
/*   9:    */ import org.apache.poi.hssf.record.MulRKRecord;
/*  10:    */ import org.apache.poi.hssf.record.NoteRecord;
/*  11:    */ import org.apache.poi.hssf.record.Record;
/*  12:    */ import org.apache.poi.hssf.record.RecordFactory;
/*  13:    */ import org.apache.poi.hssf.record.RowRecord;
/*  14:    */ import org.apache.poi.hssf.record.StringRecord;
/*  15:    */ 
/*  16:    */ public final class MissingRecordAwareHSSFListener
/*  17:    */   implements HSSFListener
/*  18:    */ {
/*  19:    */   private HSSFListener childListener;
/*  20:    */   private int lastRowRow;
/*  21:    */   private int lastCellRow;
/*  22:    */   private int lastCellColumn;
/*  23:    */   
/*  24:    */   public MissingRecordAwareHSSFListener(HSSFListener listener)
/*  25:    */   {
/*  26: 62 */     resetCounts();
/*  27: 63 */     this.childListener = listener;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void processRecord(Record record)
/*  31:    */   {
/*  32: 69 */     CellValueRecordInterface[] expandedRecords = null;
/*  33:    */     int thisColumn;
/*  34:    */     int thisRow;
/*  35:    */     int thisColumn;
/*  36: 71 */     if ((record instanceof CellValueRecordInterface))
/*  37:    */     {
/*  38: 72 */       CellValueRecordInterface valueRec = (CellValueRecordInterface)record;
/*  39: 73 */       int thisRow = valueRec.getRow();
/*  40: 74 */       thisColumn = valueRec.getColumn();
/*  41:    */     }
/*  42:    */     else
/*  43:    */     {
/*  44: 76 */       if ((record instanceof StringRecord))
/*  45:    */       {
/*  46: 78 */         this.childListener.processRecord(record);
/*  47: 79 */         return;
/*  48:    */       }
/*  49: 81 */       thisRow = -1;
/*  50: 82 */       thisColumn = -1;
/*  51: 84 */       switch (record.getSid())
/*  52:    */       {
/*  53:    */       case 2057: 
/*  54: 88 */         BOFRecord bof = (BOFRecord)record;
/*  55: 89 */         if ((bof.getType() == 5) || (bof.getType() == 16)) {
/*  56: 92 */           resetCounts();
/*  57:    */         }
/*  58:    */         break;
/*  59:    */       case 520: 
/*  60: 96 */         RowRecord rowrec = (RowRecord)record;
/*  61:101 */         if (this.lastRowRow + 1 < rowrec.getRowNumber()) {
/*  62:102 */           for (int i = this.lastRowRow + 1; i < rowrec.getRowNumber(); i++)
/*  63:    */           {
/*  64:103 */             MissingRowDummyRecord dr = new MissingRowDummyRecord(i);
/*  65:104 */             this.childListener.processRecord(dr);
/*  66:    */           }
/*  67:    */         }
/*  68:109 */         this.lastRowRow = rowrec.getRowNumber();
/*  69:110 */         this.lastCellColumn = -1;
/*  70:111 */         break;
/*  71:    */       case 1212: 
/*  72:117 */         this.childListener.processRecord(record);
/*  73:118 */         return;
/*  74:    */       case 190: 
/*  75:123 */         MulBlankRecord mbr = (MulBlankRecord)record;
/*  76:124 */         expandedRecords = RecordFactory.convertBlankRecords(mbr);
/*  77:125 */         break;
/*  78:    */       case 189: 
/*  79:129 */         MulRKRecord mrk = (MulRKRecord)record;
/*  80:130 */         expandedRecords = RecordFactory.convertRKRecords(mrk);
/*  81:131 */         break;
/*  82:    */       case 28: 
/*  83:133 */         NoteRecord nrec = (NoteRecord)record;
/*  84:134 */         thisRow = nrec.getRow();
/*  85:135 */         thisColumn = nrec.getColumn();
/*  86:136 */         break;
/*  87:    */       }
/*  88:    */     }
/*  89:143 */     if ((expandedRecords != null) && (expandedRecords.length > 0))
/*  90:    */     {
/*  91:144 */       thisRow = expandedRecords[0].getRow();
/*  92:145 */       thisColumn = expandedRecords[0].getColumn();
/*  93:    */     }
/*  94:151 */     if ((thisRow != this.lastCellRow) && (thisRow > 0))
/*  95:    */     {
/*  96:152 */       if (this.lastCellRow == -1) {
/*  97:152 */         this.lastCellRow = 0;
/*  98:    */       }
/*  99:153 */       for (int i = this.lastCellRow; i < thisRow; i++)
/* 100:    */       {
/* 101:154 */         int cols = -1;
/* 102:155 */         if (i == this.lastCellRow) {
/* 103:156 */           cols = this.lastCellColumn;
/* 104:    */         }
/* 105:158 */         this.childListener.processRecord(new LastCellOfRowDummyRecord(i, cols));
/* 106:    */       }
/* 107:    */     }
/* 108:164 */     if ((this.lastCellRow != -1) && (this.lastCellColumn != -1) && (thisRow == -1))
/* 109:    */     {
/* 110:165 */       this.childListener.processRecord(new LastCellOfRowDummyRecord(this.lastCellRow, this.lastCellColumn));
/* 111:    */       
/* 112:167 */       this.lastCellRow = -1;
/* 113:168 */       this.lastCellColumn = -1;
/* 114:    */     }
/* 115:173 */     if (thisRow != this.lastCellRow) {
/* 116:174 */       this.lastCellColumn = -1;
/* 117:    */     }
/* 118:179 */     if (this.lastCellColumn != thisColumn - 1) {
/* 119:180 */       for (int i = this.lastCellColumn + 1; i < thisColumn; i++) {
/* 120:181 */         this.childListener.processRecord(new MissingCellDummyRecord(thisRow, i));
/* 121:    */       }
/* 122:    */     }
/* 123:186 */     if ((expandedRecords != null) && (expandedRecords.length > 0)) {
/* 124:187 */       thisColumn = expandedRecords[(expandedRecords.length - 1)].getColumn();
/* 125:    */     }
/* 126:192 */     if (thisColumn != -1)
/* 127:    */     {
/* 128:193 */       this.lastCellColumn = thisColumn;
/* 129:194 */       this.lastCellRow = thisRow;
/* 130:    */     }
/* 131:198 */     if ((expandedRecords != null) && (expandedRecords.length > 0)) {
/* 132:199 */       for (CellValueRecordInterface r : expandedRecords) {
/* 133:200 */         this.childListener.processRecord((Record)r);
/* 134:    */       }
/* 135:    */     } else {
/* 136:203 */       this.childListener.processRecord(record);
/* 137:    */     }
/* 138:    */   }
/* 139:    */   
/* 140:    */   private void resetCounts()
/* 141:    */   {
/* 142:208 */     this.lastRowRow = -1;
/* 143:209 */     this.lastCellRow = -1;
/* 144:210 */     this.lastCellColumn = -1;
/* 145:    */   }
/* 146:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener
 * JD-Core Version:    0.7.0.1
 */