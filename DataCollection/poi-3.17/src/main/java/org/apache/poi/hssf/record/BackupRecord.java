/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class BackupRecord
/*  6:   */   extends StandardRecord
/*  7:   */ {
/*  8:   */   public static final short sid = 64;
/*  9:   */   private short field_1_backup;
/* 10:   */   
/* 11:   */   public BackupRecord() {}
/* 12:   */   
/* 13:   */   public BackupRecord(RecordInputStream in)
/* 14:   */   {
/* 15:45 */     this.field_1_backup = in.readShort();
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void setBackup(short backup)
/* 19:   */   {
/* 20:56 */     this.field_1_backup = backup;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public short getBackup()
/* 24:   */   {
/* 25:67 */     return this.field_1_backup;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String toString()
/* 29:   */   {
/* 30:72 */     StringBuffer buffer = new StringBuffer();
/* 31:   */     
/* 32:74 */     buffer.append("[BACKUP]\n");
/* 33:75 */     buffer.append("    .backup          = ").append(Integer.toHexString(getBackup())).append("\n");
/* 34:   */     
/* 35:77 */     buffer.append("[/BACKUP]\n");
/* 36:78 */     return buffer.toString();
/* 37:   */   }
/* 38:   */   
/* 39:   */   public void serialize(LittleEndianOutput out)
/* 40:   */   {
/* 41:82 */     out.writeShort(getBackup());
/* 42:   */   }
/* 43:   */   
/* 44:   */   protected int getDataSize()
/* 45:   */   {
/* 46:86 */     return 2;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public short getSid()
/* 50:   */   {
/* 51:91 */     return 64;
/* 52:   */   }
/* 53:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.BackupRecord
 * JD-Core Version:    0.7.0.1
 */