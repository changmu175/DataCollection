/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Locale;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ 
/*   8:    */ public final class HeaderFooterRecord
/*   9:    */   extends StandardRecord
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12: 33 */   private static final byte[] BLANK_GUID = new byte[16];
/*  13:    */   public static final short sid = 2204;
/*  14:    */   private byte[] _rawData;
/*  15:    */   
/*  16:    */   public HeaderFooterRecord(byte[] data)
/*  17:    */   {
/*  18: 39 */     this._rawData = data;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public HeaderFooterRecord(RecordInputStream in)
/*  22:    */   {
/*  23: 48 */     this._rawData = in.readRemainder();
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void serialize(LittleEndianOutput out)
/*  27:    */   {
/*  28: 55 */     out.write(this._rawData);
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected int getDataSize()
/*  32:    */   {
/*  33: 59 */     return this._rawData.length;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public short getSid()
/*  37:    */   {
/*  38: 64 */     return 2204;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public byte[] getGuid()
/*  42:    */   {
/*  43: 76 */     byte[] guid = new byte[16];
/*  44: 77 */     System.arraycopy(this._rawData, 12, guid, 0, guid.length);
/*  45: 78 */     return guid;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean isCurrentSheet()
/*  49:    */   {
/*  50: 85 */     return Arrays.equals(getGuid(), BLANK_GUID);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public String toString()
/*  54:    */   {
/*  55: 89 */     StringBuffer sb = new StringBuffer();
/*  56:    */     
/*  57: 91 */     sb.append("[").append("HEADERFOOTER").append("] (0x");
/*  58: 92 */     sb.append(Integer.toHexString(2204).toUpperCase(Locale.ROOT) + ")\n");
/*  59: 93 */     sb.append("  rawData=").append(HexDump.toHex(this._rawData)).append("\n");
/*  60: 94 */     sb.append("[/").append("HEADERFOOTER").append("]\n");
/*  61: 95 */     return sb.toString();
/*  62:    */   }
/*  63:    */   
/*  64:    */   public HeaderFooterRecord clone()
/*  65:    */   {
/*  66:101 */     return (HeaderFooterRecord)cloneViaReserialise();
/*  67:    */   }
/*  68:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.HeaderFooterRecord
 * JD-Core Version:    0.7.0.1
 */