/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class HideObjRecord
/*  6:   */   extends StandardRecord
/*  7:   */ {
/*  8:   */   public static final short sid = 141;
/*  9:   */   public static final short HIDE_ALL = 2;
/* 10:   */   public static final short SHOW_PLACEHOLDERS = 1;
/* 11:   */   public static final short SHOW_ALL = 0;
/* 12:   */   private short field_1_hide_obj;
/* 13:   */   
/* 14:   */   public HideObjRecord() {}
/* 15:   */   
/* 16:   */   public HideObjRecord(RecordInputStream in)
/* 17:   */   {
/* 18:47 */     this.field_1_hide_obj = in.readShort();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void setHideObj(short hide)
/* 22:   */   {
/* 23:61 */     this.field_1_hide_obj = hide;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public short getHideObj()
/* 27:   */   {
/* 28:75 */     return this.field_1_hide_obj;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public String toString()
/* 32:   */   {
/* 33:80 */     StringBuffer buffer = new StringBuffer();
/* 34:   */     
/* 35:82 */     buffer.append("[HIDEOBJ]\n");
/* 36:83 */     buffer.append("    .hideobj         = ").append(Integer.toHexString(getHideObj())).append("\n");
/* 37:   */     
/* 38:85 */     buffer.append("[/HIDEOBJ]\n");
/* 39:86 */     return buffer.toString();
/* 40:   */   }
/* 41:   */   
/* 42:   */   public void serialize(LittleEndianOutput out)
/* 43:   */   {
/* 44:90 */     out.writeShort(getHideObj());
/* 45:   */   }
/* 46:   */   
/* 47:   */   protected int getDataSize()
/* 48:   */   {
/* 49:94 */     return 2;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public short getSid()
/* 53:   */   {
/* 54:99 */     return 141;
/* 55:   */   }
/* 56:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.HideObjRecord
 * JD-Core Version:    0.7.0.1
 */