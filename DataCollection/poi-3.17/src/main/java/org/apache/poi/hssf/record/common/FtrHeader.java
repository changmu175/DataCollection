/*  1:   */ package org.apache.poi.hssf.record.common;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.ss.util.CellRangeAddress;
/*  5:   */ import org.apache.poi.util.LittleEndianOutput;
/*  6:   */ 
/*  7:   */ public final class FtrHeader
/*  8:   */   implements Cloneable
/*  9:   */ {
/* 10:   */   private short recordType;
/* 11:   */   private short grbitFrt;
/* 12:   */   private CellRangeAddress associatedRange;
/* 13:   */   
/* 14:   */   public FtrHeader()
/* 15:   */   {
/* 16:40 */     this.associatedRange = new CellRangeAddress(0, 0, 0, 0);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public FtrHeader(RecordInputStream in)
/* 20:   */   {
/* 21:44 */     this.recordType = in.readShort();
/* 22:45 */     this.grbitFrt = in.readShort();
/* 23:   */     
/* 24:47 */     this.associatedRange = new CellRangeAddress(in);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public String toString()
/* 28:   */   {
/* 29:51 */     StringBuffer buffer = new StringBuffer();
/* 30:52 */     buffer.append(" [FUTURE HEADER]\n");
/* 31:53 */     buffer.append("   Type " + this.recordType);
/* 32:54 */     buffer.append("   Flags " + this.grbitFrt);
/* 33:55 */     buffer.append(" [/FUTURE HEADER]\n");
/* 34:56 */     return buffer.toString();
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void serialize(LittleEndianOutput out)
/* 38:   */   {
/* 39:60 */     out.writeShort(this.recordType);
/* 40:61 */     out.writeShort(this.grbitFrt);
/* 41:62 */     this.associatedRange.serialize(out);
/* 42:   */   }
/* 43:   */   
/* 44:   */   public static int getDataSize()
/* 45:   */   {
/* 46:66 */     return 12;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public short getRecordType()
/* 50:   */   {
/* 51:70 */     return this.recordType;
/* 52:   */   }
/* 53:   */   
/* 54:   */   public void setRecordType(short recordType)
/* 55:   */   {
/* 56:73 */     this.recordType = recordType;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public short getGrbitFrt()
/* 60:   */   {
/* 61:77 */     return this.grbitFrt;
/* 62:   */   }
/* 63:   */   
/* 64:   */   public void setGrbitFrt(short grbitFrt)
/* 65:   */   {
/* 66:80 */     this.grbitFrt = grbitFrt;
/* 67:   */   }
/* 68:   */   
/* 69:   */   public CellRangeAddress getAssociatedRange()
/* 70:   */   {
/* 71:84 */     return this.associatedRange;
/* 72:   */   }
/* 73:   */   
/* 74:   */   public void setAssociatedRange(CellRangeAddress associatedRange)
/* 75:   */   {
/* 76:87 */     this.associatedRange = associatedRange;
/* 77:   */   }
/* 78:   */   
/* 79:   */   public Object clone()
/* 80:   */   {
/* 81:91 */     FtrHeader result = new FtrHeader();
/* 82:92 */     result.recordType = this.recordType;
/* 83:93 */     result.grbitFrt = this.grbitFrt;
/* 84:94 */     result.associatedRange = this.associatedRange.copy();
/* 85:95 */     return result;
/* 86:   */   }
/* 87:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.common.FtrHeader
 * JD-Core Version:    0.7.0.1
 */