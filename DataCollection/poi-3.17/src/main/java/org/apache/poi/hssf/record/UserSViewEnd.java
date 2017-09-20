/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import org.apache.poi.util.HexDump;
/*  5:   */ import org.apache.poi.util.LittleEndianOutput;
/*  6:   */ 
/*  7:   */ public final class UserSViewEnd
/*  8:   */   extends StandardRecord
/*  9:   */ {
/* 10:   */   public static final short sid = 427;
/* 11:   */   private byte[] _rawData;
/* 12:   */   
/* 13:   */   public UserSViewEnd(byte[] data)
/* 14:   */   {
/* 15:34 */     this._rawData = data;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public UserSViewEnd(RecordInputStream in)
/* 19:   */   {
/* 20:43 */     this._rawData = in.readRemainder();
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void serialize(LittleEndianOutput out)
/* 24:   */   {
/* 25:50 */     out.write(this._rawData);
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected int getDataSize()
/* 29:   */   {
/* 30:54 */     return this._rawData.length;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public short getSid()
/* 34:   */   {
/* 35:59 */     return 427;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public String toString()
/* 39:   */   {
/* 40:63 */     StringBuffer sb = new StringBuffer();
/* 41:   */     
/* 42:65 */     sb.append("[").append("USERSVIEWEND").append("] (0x");
/* 43:66 */     sb.append(Integer.toHexString(427).toUpperCase(Locale.ROOT) + ")\n");
/* 44:67 */     sb.append("  rawData=").append(HexDump.toHex(this._rawData)).append("\n");
/* 45:68 */     sb.append("[/").append("USERSVIEWEND").append("]\n");
/* 46:69 */     return sb.toString();
/* 47:   */   }
/* 48:   */   
/* 49:   */   public Object clone()
/* 50:   */   {
/* 51:74 */     return cloneViaReserialise();
/* 52:   */   }
/* 53:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.UserSViewEnd
 * JD-Core Version:    0.7.0.1
 */