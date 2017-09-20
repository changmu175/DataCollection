/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import org.apache.poi.util.HexDump;
/*  5:   */ import org.apache.poi.util.LittleEndianOutput;
/*  6:   */ 
/*  7:   */ public final class UserSViewBegin
/*  8:   */   extends StandardRecord
/*  9:   */ {
/* 10:   */   public static final short sid = 426;
/* 11:   */   private byte[] _rawData;
/* 12:   */   
/* 13:   */   public UserSViewBegin(byte[] data)
/* 14:   */   {
/* 15:39 */     this._rawData = data;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public UserSViewBegin(RecordInputStream in)
/* 19:   */   {
/* 20:48 */     this._rawData = in.readRemainder();
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void serialize(LittleEndianOutput out)
/* 24:   */   {
/* 25:55 */     out.write(this._rawData);
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected int getDataSize()
/* 29:   */   {
/* 30:59 */     return this._rawData.length;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public short getSid()
/* 34:   */   {
/* 35:64 */     return 426;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public byte[] getGuid()
/* 39:   */   {
/* 40:71 */     byte[] guid = new byte[16];
/* 41:72 */     System.arraycopy(this._rawData, 0, guid, 0, guid.length);
/* 42:73 */     return guid;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public String toString()
/* 46:   */   {
/* 47:77 */     StringBuffer sb = new StringBuffer();
/* 48:   */     
/* 49:79 */     sb.append("[").append("USERSVIEWBEGIN").append("] (0x");
/* 50:80 */     sb.append(Integer.toHexString(426).toUpperCase(Locale.ROOT) + ")\n");
/* 51:81 */     sb.append("  rawData=").append(HexDump.toHex(this._rawData)).append("\n");
/* 52:82 */     sb.append("[/").append("USERSVIEWBEGIN").append("]\n");
/* 53:83 */     return sb.toString();
/* 54:   */   }
/* 55:   */   
/* 56:   */   public Object clone()
/* 57:   */   {
/* 58:88 */     return cloneViaReserialise();
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.UserSViewBegin
 * JD-Core Version:    0.7.0.1
 */