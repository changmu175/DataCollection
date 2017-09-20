/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class TabIdRecord
/*  6:   */   extends StandardRecord
/*  7:   */ {
/*  8:   */   public static final short sid = 317;
/*  9:30 */   private static final short[] EMPTY_SHORT_ARRAY = new short[0];
/* 10:   */   public short[] _tabids;
/* 11:   */   
/* 12:   */   public TabIdRecord()
/* 13:   */   {
/* 14:35 */     this._tabids = EMPTY_SHORT_ARRAY;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public TabIdRecord(RecordInputStream in)
/* 18:   */   {
/* 19:39 */     int nTabs = in.remaining() / 2;
/* 20:40 */     this._tabids = new short[nTabs];
/* 21:41 */     for (int i = 0; i < this._tabids.length; i++) {
/* 22:42 */       this._tabids[i] = in.readShort();
/* 23:   */     }
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void setTabIdArray(short[] array)
/* 27:   */   {
/* 28:51 */     this._tabids = ((short[])array.clone());
/* 29:   */   }
/* 30:   */   
/* 31:   */   public String toString()
/* 32:   */   {
/* 33:55 */     StringBuffer buffer = new StringBuffer();
/* 34:   */     
/* 35:57 */     buffer.append("[TABID]\n");
/* 36:58 */     buffer.append("    .elements        = ").append(this._tabids.length).append("\n");
/* 37:59 */     for (int i = 0; i < this._tabids.length; i++) {
/* 38:60 */       buffer.append("    .element_").append(i).append(" = ").append(this._tabids[i]).append("\n");
/* 39:   */     }
/* 40:62 */     buffer.append("[/TABID]\n");
/* 41:63 */     return buffer.toString();
/* 42:   */   }
/* 43:   */   
/* 44:   */   public void serialize(LittleEndianOutput out)
/* 45:   */   {
/* 46:67 */     short[] tabids = this._tabids;
/* 47:69 */     for (int i = 0; i < tabids.length; i++) {
/* 48:70 */       out.writeShort(tabids[i]);
/* 49:   */     }
/* 50:   */   }
/* 51:   */   
/* 52:   */   protected int getDataSize()
/* 53:   */   {
/* 54:75 */     return this._tabids.length * 2;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public short getSid()
/* 58:   */   {
/* 59:79 */     return 317;
/* 60:   */   }
/* 61:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.TabIdRecord
 * JD-Core Version:    0.7.0.1
 */