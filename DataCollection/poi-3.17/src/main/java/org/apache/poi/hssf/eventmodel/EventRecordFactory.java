/*   1:    */ package org.apache.poi.hssf.eventmodel;
/*   2:    */ 
/*   3:    */ import java.io.InputStream;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import org.apache.poi.hssf.record.Record;
/*   6:    */ import org.apache.poi.hssf.record.RecordFactory;
/*   7:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   8:    */ import org.apache.poi.util.RecordFormatException;
/*   9:    */ 
/*  10:    */ public final class EventRecordFactory
/*  11:    */ {
/*  12:    */   private final ERFListener _listener;
/*  13:    */   private final short[] _sids;
/*  14:    */   
/*  15:    */   public EventRecordFactory(ERFListener listener, short[] sids)
/*  16:    */   {
/*  17: 49 */     this._listener = listener;
/*  18: 50 */     if (sids == null)
/*  19:    */     {
/*  20: 51 */       this._sids = null;
/*  21:    */     }
/*  22:    */     else
/*  23:    */     {
/*  24: 53 */       this._sids = ((short[])sids.clone());
/*  25: 54 */       Arrays.sort(this._sids);
/*  26:    */     }
/*  27:    */   }
/*  28:    */   
/*  29:    */   private boolean isSidIncluded(short sid)
/*  30:    */   {
/*  31: 58 */     if (this._sids == null) {
/*  32: 59 */       return true;
/*  33:    */     }
/*  34: 61 */     return Arrays.binarySearch(this._sids, sid) >= 0;
/*  35:    */   }
/*  36:    */   
/*  37:    */   private boolean processRecord(Record record)
/*  38:    */   {
/*  39: 72 */     if (!isSidIncluded(record.getSid())) {
/*  40: 73 */       return true;
/*  41:    */     }
/*  42: 75 */     return this._listener.processRecord(record);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void processRecords(InputStream in)
/*  46:    */     throws RecordFormatException
/*  47:    */   {
/*  48: 88 */     Record last_record = null;
/*  49:    */     
/*  50: 90 */     RecordInputStream recStream = new RecordInputStream(in);
/*  51: 92 */     while (recStream.hasNextRecord())
/*  52:    */     {
/*  53: 93 */       recStream.nextRecord();
/*  54: 94 */       Record[] recs = RecordFactory.createRecord(recStream);
/*  55: 95 */       if (recs.length > 1)
/*  56:    */       {
/*  57: 96 */         for (Record rec : recs)
/*  58:    */         {
/*  59: 97 */           if ((last_record != null) && 
/*  60: 98 */             (!processRecord(last_record))) {
/*  61: 99 */             return;
/*  62:    */           }
/*  63:102 */           last_record = rec;
/*  64:    */         }
/*  65:    */       }
/*  66:    */       else
/*  67:    */       {
/*  68:105 */         Record record = recs[0];
/*  69:107 */         if (record != null)
/*  70:    */         {
/*  71:108 */           if ((last_record != null) && 
/*  72:109 */             (!processRecord(last_record))) {
/*  73:110 */             return;
/*  74:    */           }
/*  75:113 */           last_record = record;
/*  76:    */         }
/*  77:    */       }
/*  78:    */     }
/*  79:118 */     if (last_record != null) {
/*  80:119 */       processRecord(last_record);
/*  81:    */     }
/*  82:    */   }
/*  83:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.eventmodel.EventRecordFactory
 * JD-Core Version:    0.7.0.1
 */