/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndian;
/*   4:    */ 
/*   5:    */ public class EscherBlipRecord
/*   6:    */   extends EscherRecord
/*   7:    */ {
/*   8:    */   public static final short RECORD_ID_START = -4072;
/*   9:    */   public static final short RECORD_ID_END = -3817;
/*  10:    */   public static final String RECORD_DESCRIPTION = "msofbtBlip";
/*  11:    */   private static final int HEADER_SIZE = 8;
/*  12:    */   private byte[] field_pictureData;
/*  13:    */   
/*  14:    */   public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory)
/*  15:    */   {
/*  16: 36 */     int bytesAfterHeader = readHeader(data, offset);
/*  17: 37 */     int pos = offset + 8;
/*  18:    */     
/*  19: 39 */     this.field_pictureData = new byte[bytesAfterHeader];
/*  20: 40 */     System.arraycopy(data, pos, this.field_pictureData, 0, bytesAfterHeader);
/*  21:    */     
/*  22: 42 */     return bytesAfterHeader + 8;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  26:    */   {
/*  27: 47 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  28:    */     
/*  29: 49 */     LittleEndian.putShort(data, offset, getOptions());
/*  30: 50 */     LittleEndian.putShort(data, offset + 2, getRecordId());
/*  31:    */     
/*  32: 52 */     System.arraycopy(this.field_pictureData, 0, data, offset + 4, this.field_pictureData.length);
/*  33:    */     
/*  34: 54 */     listener.afterRecordSerialize(offset + 4 + this.field_pictureData.length, getRecordId(), this.field_pictureData.length + 4, this);
/*  35: 55 */     return this.field_pictureData.length + 4;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public int getRecordSize()
/*  39:    */   {
/*  40: 60 */     return this.field_pictureData.length + 8;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public String getRecordName()
/*  44:    */   {
/*  45: 65 */     return "Blip";
/*  46:    */   }
/*  47:    */   
/*  48:    */   public byte[] getPicturedata()
/*  49:    */   {
/*  50: 74 */     return this.field_pictureData;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setPictureData(byte[] pictureData)
/*  54:    */   {
/*  55: 83 */     setPictureData(pictureData, 0, pictureData == null ? 0 : pictureData.length);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setPictureData(byte[] pictureData, int offset, int length)
/*  59:    */   {
/*  60: 94 */     if ((pictureData == null) || (offset < 0) || (length < 0) || (pictureData.length < offset + length)) {
/*  61: 95 */       throw new IllegalArgumentException("picture data can't be null");
/*  62:    */     }
/*  63: 97 */     this.field_pictureData = new byte[length];
/*  64: 98 */     System.arraycopy(pictureData, offset, this.field_pictureData, 0, length);
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected Object[][] getAttributeMap()
/*  68:    */   {
/*  69:103 */     return new Object[][] { { "Extra Data", getPicturedata() } };
/*  70:    */   }
/*  71:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherBlipRecord
 * JD-Core Version:    0.7.0.1
 */