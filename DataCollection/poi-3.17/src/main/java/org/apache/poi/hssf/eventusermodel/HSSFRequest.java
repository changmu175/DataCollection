/*   1:    */ package org.apache.poi.hssf.eventusermodel;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import org.apache.poi.hssf.record.Record;
/*   8:    */ import org.apache.poi.hssf.record.RecordFactory;
/*   9:    */ 
/*  10:    */ public class HSSFRequest
/*  11:    */ {
/*  12:    */   private final Map<Short, List<HSSFListener>> _records;
/*  13:    */   
/*  14:    */   public HSSFRequest()
/*  15:    */   {
/*  16: 41 */     this._records = new HashMap(50);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public void addListener(HSSFListener lsnr, short sid)
/*  20:    */   {
/*  21: 58 */     List<HSSFListener> list = (List)this._records.get(Short.valueOf(sid));
/*  22: 60 */     if (list == null)
/*  23:    */     {
/*  24: 61 */       list = new ArrayList(1);
/*  25: 62 */       this._records.put(Short.valueOf(sid), list);
/*  26:    */     }
/*  27: 64 */     list.add(lsnr);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void addListenerForAllRecords(HSSFListener lsnr)
/*  31:    */   {
/*  32: 78 */     short[] rectypes = RecordFactory.getAllKnownRecordSIDs();
/*  33: 80 */     for (short rectype : rectypes) {
/*  34: 81 */       addListener(lsnr, rectype);
/*  35:    */     }
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected short processRecord(Record rec)
/*  39:    */     throws HSSFUserException
/*  40:    */   {
/*  41: 95 */     List<HSSFListener> listeners = (List)this._records.get(Short.valueOf(rec.getSid()));
/*  42: 96 */     short userCode = 0;
/*  43: 98 */     if (listeners != null) {
/*  44:100 */       for (int k = 0; k < listeners.size(); k++)
/*  45:    */       {
/*  46:101 */         Object listenObj = listeners.get(k);
/*  47:102 */         if ((listenObj instanceof AbortableHSSFListener))
/*  48:    */         {
/*  49:103 */           AbortableHSSFListener listener = (AbortableHSSFListener)listenObj;
/*  50:104 */           userCode = listener.abortableProcessRecord(rec);
/*  51:105 */           if (userCode != 0) {
/*  52:    */             break;
/*  53:    */           }
/*  54:    */         }
/*  55:    */         else
/*  56:    */         {
/*  57:108 */           HSSFListener listener = (HSSFListener)listenObj;
/*  58:109 */           listener.processRecord(rec);
/*  59:    */         }
/*  60:    */       }
/*  61:    */     }
/*  62:113 */     return userCode;
/*  63:    */   }
/*  64:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.eventusermodel.HSSFRequest
 * JD-Core Version:    0.7.0.1
 */