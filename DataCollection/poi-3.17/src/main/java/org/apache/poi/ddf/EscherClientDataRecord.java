/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndian;
/*   4:    */ 
/*   5:    */ public class EscherClientDataRecord
/*   6:    */   extends EscherRecord
/*   7:    */ {
/*   8:    */   public static final short RECORD_ID = -4079;
/*   9:    */   public static final String RECORD_DESCRIPTION = "MsofbtClientData";
/*  10:    */   private byte[] remainingData;
/*  11:    */   
/*  12:    */   public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory)
/*  13:    */   {
/*  14: 37 */     int bytesRemaining = readHeader(data, offset);
/*  15: 38 */     int pos = offset + 8;
/*  16: 39 */     this.remainingData = new byte[bytesRemaining];
/*  17: 40 */     System.arraycopy(data, pos, this.remainingData, 0, bytesRemaining);
/*  18: 41 */     return 8 + bytesRemaining;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  22:    */   {
/*  23: 46 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  24: 48 */     if (this.remainingData == null) {
/*  25: 49 */       this.remainingData = new byte[0];
/*  26:    */     }
/*  27: 51 */     LittleEndian.putShort(data, offset, getOptions());
/*  28: 52 */     LittleEndian.putShort(data, offset + 2, getRecordId());
/*  29: 53 */     LittleEndian.putInt(data, offset + 4, this.remainingData.length);
/*  30: 54 */     System.arraycopy(this.remainingData, 0, data, offset + 8, this.remainingData.length);
/*  31: 55 */     int pos = offset + 8 + this.remainingData.length;
/*  32:    */     
/*  33: 57 */     listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
/*  34: 58 */     return pos - offset;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public int getRecordSize()
/*  38:    */   {
/*  39: 64 */     return 8 + (this.remainingData == null ? 0 : this.remainingData.length);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public short getRecordId()
/*  43:    */   {
/*  44: 69 */     return -4079;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public String getRecordName()
/*  48:    */   {
/*  49: 74 */     return "ClientData";
/*  50:    */   }
/*  51:    */   
/*  52:    */   public byte[] getRemainingData()
/*  53:    */   {
/*  54: 84 */     return this.remainingData;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setRemainingData(byte[] remainingData)
/*  58:    */   {
/*  59: 93 */     this.remainingData = (remainingData == null ? new byte[0] : (byte[])remainingData.clone());
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected Object[][] getAttributeMap()
/*  63:    */   {
/*  64:100 */     return new Object[][] { { "Extra Data", getRemainingData() } };
/*  65:    */   }
/*  66:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherClientDataRecord
 * JD-Core Version:    0.7.0.1
 */