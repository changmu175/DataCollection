/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.util.LittleEndian;
/*   6:    */ import org.apache.poi.util.RecordFormatException;
/*   7:    */ 
/*   8:    */ public final class EscherTextboxRecord
/*   9:    */   extends EscherRecord
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12:    */   public static final short RECORD_ID = -4083;
/*  13:    */   public static final String RECORD_DESCRIPTION = "msofbtClientTextbox";
/*  14: 36 */   private static final byte[] NO_BYTES = new byte[0];
/*  15: 39 */   private byte[] thedata = NO_BYTES;
/*  16:    */   
/*  17:    */   public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory)
/*  18:    */   {
/*  19: 47 */     int bytesRemaining = readHeader(data, offset);
/*  20:    */     
/*  21:    */ 
/*  22:    */ 
/*  23: 51 */     this.thedata = new byte[bytesRemaining];
/*  24: 52 */     System.arraycopy(data, offset + 8, this.thedata, 0, bytesRemaining);
/*  25: 53 */     return bytesRemaining + 8;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  29:    */   {
/*  30: 59 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  31:    */     
/*  32: 61 */     LittleEndian.putShort(data, offset, getOptions());
/*  33: 62 */     LittleEndian.putShort(data, offset + 2, getRecordId());
/*  34: 63 */     int remainingBytes = this.thedata.length;
/*  35: 64 */     LittleEndian.putInt(data, offset + 4, remainingBytes);
/*  36: 65 */     System.arraycopy(this.thedata, 0, data, offset + 8, this.thedata.length);
/*  37: 66 */     int pos = offset + 8 + this.thedata.length;
/*  38:    */     
/*  39: 68 */     listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
/*  40: 69 */     int size = pos - offset;
/*  41: 70 */     if (size != getRecordSize()) {
/*  42: 71 */       throw new RecordFormatException(size + " bytes written but getRecordSize() reports " + getRecordSize());
/*  43:    */     }
/*  44: 73 */     return size;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public byte[] getData()
/*  48:    */   {
/*  49: 86 */     return this.thedata;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setData(byte[] b, int start, int length)
/*  53:    */   {
/*  54:100 */     this.thedata = new byte[length];
/*  55:101 */     System.arraycopy(b, start, this.thedata, 0, length);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setData(byte[] b)
/*  59:    */   {
/*  60:112 */     setData(b, 0, b.length);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public int getRecordSize()
/*  64:    */   {
/*  65:118 */     return 8 + this.thedata.length;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public EscherTextboxRecord clone()
/*  69:    */   {
/*  70:123 */     EscherTextboxRecord etr = new EscherTextboxRecord();
/*  71:124 */     etr.setOptions(getOptions());
/*  72:125 */     etr.setRecordId(getRecordId());
/*  73:126 */     etr.thedata = ((byte[])this.thedata.clone());
/*  74:127 */     return etr;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String getRecordName()
/*  78:    */   {
/*  79:132 */     return "ClientTextbox";
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected Object[][] getAttributeMap()
/*  83:    */   {
/*  84:137 */     int numCh = getChildRecords().size();
/*  85:138 */     List<Object> chLst = new ArrayList(numCh * 2 + 2);
/*  86:139 */     chLst.add("children");
/*  87:140 */     chLst.add(Integer.valueOf(numCh));
/*  88:141 */     for (EscherRecord er : getChildRecords())
/*  89:    */     {
/*  90:142 */       chLst.add(er.getRecordName());
/*  91:143 */       chLst.add(er);
/*  92:    */     }
/*  93:146 */     return new Object[][] { { "isContainer", Boolean.valueOf(isContainerRecord()) }, chLst.toArray(), { "Extra Data", this.thedata } };
/*  94:    */   }
/*  95:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherTextboxRecord
 * JD-Core Version:    0.7.0.1
 */