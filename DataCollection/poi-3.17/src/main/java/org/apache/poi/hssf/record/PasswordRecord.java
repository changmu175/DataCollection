/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.HexDump;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class PasswordRecord
/*  7:   */   extends StandardRecord
/*  8:   */ {
/*  9:   */   public static final short sid = 19;
/* 10:   */   private int field_1_password;
/* 11:   */   
/* 12:   */   public PasswordRecord(int password)
/* 13:   */   {
/* 14:33 */     this.field_1_password = password;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public PasswordRecord(RecordInputStream in)
/* 18:   */   {
/* 19:37 */     this.field_1_password = in.readShort();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setPassword(int password)
/* 23:   */   {
/* 24:47 */     this.field_1_password = password;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public int getPassword()
/* 28:   */   {
/* 29:56 */     return this.field_1_password;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public String toString()
/* 33:   */   {
/* 34:60 */     StringBuffer buffer = new StringBuffer();
/* 35:   */     
/* 36:62 */     buffer.append("[PASSWORD]\n");
/* 37:63 */     buffer.append("    .password = ").append(HexDump.shortToHex(this.field_1_password)).append("\n");
/* 38:64 */     buffer.append("[/PASSWORD]\n");
/* 39:65 */     return buffer.toString();
/* 40:   */   }
/* 41:   */   
/* 42:   */   public void serialize(LittleEndianOutput out)
/* 43:   */   {
/* 44:69 */     out.writeShort(this.field_1_password);
/* 45:   */   }
/* 46:   */   
/* 47:   */   protected int getDataSize()
/* 48:   */   {
/* 49:73 */     return 2;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public short getSid()
/* 53:   */   {
/* 54:77 */     return 19;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public Object clone()
/* 58:   */   {
/* 59:84 */     return new PasswordRecord(this.field_1_password);
/* 60:   */   }
/* 61:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.PasswordRecord
 * JD-Core Version:    0.7.0.1
 */