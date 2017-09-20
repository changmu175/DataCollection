/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.HexDump;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class PasswordRev4Record
/*  7:   */   extends StandardRecord
/*  8:   */ {
/*  9:   */   public static final short sid = 444;
/* 10:   */   private int field_1_password;
/* 11:   */   
/* 12:   */   public PasswordRev4Record(int pw)
/* 13:   */   {
/* 14:33 */     this.field_1_password = pw;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public PasswordRev4Record(RecordInputStream in)
/* 18:   */   {
/* 19:37 */     this.field_1_password = in.readShort();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setPassword(short pw)
/* 23:   */   {
/* 24:46 */     this.field_1_password = pw;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public String toString()
/* 28:   */   {
/* 29:50 */     StringBuffer buffer = new StringBuffer();
/* 30:   */     
/* 31:52 */     buffer.append("[PROT4REVPASSWORD]\n");
/* 32:53 */     buffer.append("    .password = ").append(HexDump.shortToHex(this.field_1_password)).append("\n");
/* 33:54 */     buffer.append("[/PROT4REVPASSWORD]\n");
/* 34:55 */     return buffer.toString();
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void serialize(LittleEndianOutput out)
/* 38:   */   {
/* 39:59 */     out.writeShort(this.field_1_password);
/* 40:   */   }
/* 41:   */   
/* 42:   */   protected int getDataSize()
/* 43:   */   {
/* 44:63 */     return 2;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public short getSid()
/* 48:   */   {
/* 49:67 */     return 444;
/* 50:   */   }
/* 51:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.PasswordRev4Record
 * JD-Core Version:    0.7.0.1
 */