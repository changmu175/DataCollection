/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.common.FtrHeader;
/*   4:    */ import org.apache.poi.util.LittleEndianOutput;
/*   5:    */ 
/*   6:    */ public final class FeatHdrRecord
/*   7:    */   extends StandardRecord
/*   8:    */   implements Cloneable
/*   9:    */ {
/*  10:    */   public static final int SHAREDFEATURES_ISFPROTECTION = 2;
/*  11:    */   public static final int SHAREDFEATURES_ISFFEC2 = 3;
/*  12:    */   public static final int SHAREDFEATURES_ISFFACTOID = 4;
/*  13:    */   public static final int SHAREDFEATURES_ISFLIST = 5;
/*  14:    */   public static final short sid = 2151;
/*  15:    */   private FtrHeader futureHeader;
/*  16:    */   private int isf_sharedFeatureType;
/*  17:    */   private byte reserved;
/*  18:    */   private long cbHdrData;
/*  19:    */   private byte[] rgbHdrData;
/*  20:    */   
/*  21:    */   public FeatHdrRecord()
/*  22:    */   {
/*  23: 68 */     this.futureHeader = new FtrHeader();
/*  24: 69 */     this.futureHeader.setRecordType((short)2151);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public short getSid()
/*  28:    */   {
/*  29: 73 */     return 2151;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public FeatHdrRecord(RecordInputStream in)
/*  33:    */   {
/*  34: 77 */     this.futureHeader = new FtrHeader(in);
/*  35:    */     
/*  36: 79 */     this.isf_sharedFeatureType = in.readShort();
/*  37: 80 */     this.reserved = in.readByte();
/*  38: 81 */     this.cbHdrData = in.readInt();
/*  39:    */     
/*  40: 83 */     this.rgbHdrData = in.readRemainder();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public String toString()
/*  44:    */   {
/*  45: 87 */     StringBuffer buffer = new StringBuffer();
/*  46: 88 */     buffer.append("[FEATURE HEADER]\n");
/*  47:    */     
/*  48:    */ 
/*  49:    */ 
/*  50: 92 */     buffer.append("[/FEATURE HEADER]\n");
/*  51: 93 */     return buffer.toString();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void serialize(LittleEndianOutput out)
/*  55:    */   {
/*  56: 97 */     this.futureHeader.serialize(out);
/*  57:    */     
/*  58: 99 */     out.writeShort(this.isf_sharedFeatureType);
/*  59:100 */     out.writeByte(this.reserved);
/*  60:101 */     out.writeInt((int)this.cbHdrData);
/*  61:102 */     out.write(this.rgbHdrData);
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected int getDataSize()
/*  65:    */   {
/*  66:106 */     return 19 + this.rgbHdrData.length;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public FeatHdrRecord clone()
/*  70:    */   {
/*  71:112 */     return (FeatHdrRecord)cloneViaReserialise();
/*  72:    */   }
/*  73:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.FeatHdrRecord
 * JD-Core Version:    0.7.0.1
 */