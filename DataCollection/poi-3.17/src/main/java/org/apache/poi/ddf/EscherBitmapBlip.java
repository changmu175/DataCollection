/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndian;
/*   4:    */ 
/*   5:    */ public class EscherBitmapBlip
/*   6:    */   extends EscherBlipRecord
/*   7:    */ {
/*   8:    */   public static final short RECORD_ID_JPEG = -4067;
/*   9:    */   public static final short RECORD_ID_PNG = -4066;
/*  10:    */   public static final short RECORD_ID_DIB = -4065;
/*  11:    */   private static final int HEADER_SIZE = 8;
/*  12: 29 */   private final byte[] field_1_UID = new byte[16];
/*  13: 30 */   private byte field_2_marker = -1;
/*  14:    */   
/*  15:    */   public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory)
/*  16:    */   {
/*  17: 34 */     int bytesAfterHeader = readHeader(data, offset);
/*  18: 35 */     int pos = offset + 8;
/*  19:    */     
/*  20: 37 */     System.arraycopy(data, pos, this.field_1_UID, 0, 16);pos += 16;
/*  21: 38 */     this.field_2_marker = data[pos];pos++;
/*  22:    */     
/*  23: 40 */     setPictureData(data, pos, bytesAfterHeader - 17);
/*  24:    */     
/*  25: 42 */     return bytesAfterHeader + 8;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  29:    */   {
/*  30: 47 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  31:    */     
/*  32: 49 */     LittleEndian.putShort(data, offset, getOptions());
/*  33: 50 */     LittleEndian.putShort(data, offset + 2, getRecordId());
/*  34: 51 */     LittleEndian.putInt(data, offset + 4, getRecordSize() - 8);
/*  35: 52 */     int pos = offset + 8;
/*  36:    */     
/*  37: 54 */     System.arraycopy(this.field_1_UID, 0, data, pos, 16);
/*  38: 55 */     data[(pos + 16)] = this.field_2_marker;
/*  39: 56 */     byte[] pd = getPicturedata();
/*  40: 57 */     System.arraycopy(pd, 0, data, pos + 17, pd.length);
/*  41:    */     
/*  42: 59 */     listener.afterRecordSerialize(offset + getRecordSize(), getRecordId(), getRecordSize(), this);
/*  43: 60 */     return 25 + pd.length;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public int getRecordSize()
/*  47:    */   {
/*  48: 65 */     return 25 + getPicturedata().length;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public byte[] getUID()
/*  52:    */   {
/*  53: 75 */     return this.field_1_UID;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setUID(byte[] field_1_UID)
/*  57:    */   {
/*  58: 85 */     if ((field_1_UID == null) || (field_1_UID.length != 16)) {
/*  59: 86 */       throw new IllegalArgumentException("field_1_UID must be byte[16]");
/*  60:    */     }
/*  61: 88 */     System.arraycopy(field_1_UID, 0, this.field_1_UID, 0, 16);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public byte getMarker()
/*  65:    */   {
/*  66: 98 */     return this.field_2_marker;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setMarker(byte field_2_marker)
/*  70:    */   {
/*  71:108 */     this.field_2_marker = field_2_marker;
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected Object[][] getAttributeMap()
/*  75:    */   {
/*  76:113 */     return new Object[][] { { "Marker", Byte.valueOf(this.field_2_marker) }, { "Extra Data", getPicturedata() } };
/*  77:    */   }
/*  78:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherBitmapBlip
 * JD-Core Version:    0.7.0.1
 */