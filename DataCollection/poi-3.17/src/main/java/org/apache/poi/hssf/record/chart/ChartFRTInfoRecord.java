/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianInput;
/*   7:    */ import org.apache.poi.util.LittleEndianOutput;
/*   8:    */ 
/*   9:    */ public final class ChartFRTInfoRecord
/*  10:    */   extends StandardRecord
/*  11:    */ {
/*  12:    */   public static final short sid = 2128;
/*  13:    */   private short rt;
/*  14:    */   private short grbitFrt;
/*  15:    */   private byte verOriginator;
/*  16:    */   private byte verWriter;
/*  17:    */   private CFRTID[] rgCFRTID;
/*  18:    */   
/*  19:    */   private static final class CFRTID
/*  20:    */   {
/*  21:    */     public static final int ENCODED_SIZE = 4;
/*  22:    */     private int rtFirst;
/*  23:    */     private int rtLast;
/*  24:    */     
/*  25:    */     public CFRTID(LittleEndianInput in)
/*  26:    */     {
/*  27: 44 */       this.rtFirst = in.readShort();
/*  28: 45 */       this.rtLast = in.readShort();
/*  29:    */     }
/*  30:    */     
/*  31:    */     public void serialize(LittleEndianOutput out)
/*  32:    */     {
/*  33: 49 */       out.writeShort(this.rtFirst);
/*  34: 50 */       out.writeShort(this.rtLast);
/*  35:    */     }
/*  36:    */   }
/*  37:    */   
/*  38:    */   public ChartFRTInfoRecord(RecordInputStream in)
/*  39:    */   {
/*  40: 55 */     this.rt = in.readShort();
/*  41: 56 */     this.grbitFrt = in.readShort();
/*  42: 57 */     this.verOriginator = in.readByte();
/*  43: 58 */     this.verWriter = in.readByte();
/*  44: 59 */     int cCFRTID = in.readShort();
/*  45:    */     
/*  46: 61 */     this.rgCFRTID = new CFRTID[cCFRTID];
/*  47: 62 */     for (int i = 0; i < cCFRTID; i++) {
/*  48: 63 */       this.rgCFRTID[i] = new CFRTID(in);
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected int getDataSize()
/*  53:    */   {
/*  54: 69 */     return 8 + this.rgCFRTID.length * 4;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public short getSid()
/*  58:    */   {
/*  59: 74 */     return 2128;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void serialize(LittleEndianOutput out)
/*  63:    */   {
/*  64: 80 */     out.writeShort(this.rt);
/*  65: 81 */     out.writeShort(this.grbitFrt);
/*  66: 82 */     out.writeByte(this.verOriginator);
/*  67: 83 */     out.writeByte(this.verWriter);
/*  68: 84 */     int nCFRTIDs = this.rgCFRTID.length;
/*  69: 85 */     out.writeShort(nCFRTIDs);
/*  70: 87 */     for (int i = 0; i < nCFRTIDs; i++) {
/*  71: 88 */       this.rgCFRTID[i].serialize(out);
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   public String toString()
/*  76:    */   {
/*  77: 94 */     StringBuffer buffer = new StringBuffer();
/*  78:    */     
/*  79: 96 */     buffer.append("[CHARTFRTINFO]\n");
/*  80: 97 */     buffer.append("    .rt           =").append(HexDump.shortToHex(this.rt)).append('\n');
/*  81: 98 */     buffer.append("    .grbitFrt     =").append(HexDump.shortToHex(this.grbitFrt)).append('\n');
/*  82: 99 */     buffer.append("    .verOriginator=").append(HexDump.byteToHex(this.verOriginator)).append('\n');
/*  83:100 */     buffer.append("    .verWriter    =").append(HexDump.byteToHex(this.verOriginator)).append('\n');
/*  84:101 */     buffer.append("    .nCFRTIDs     =").append(HexDump.shortToHex(this.rgCFRTID.length)).append('\n');
/*  85:102 */     buffer.append("[/CHARTFRTINFO]\n");
/*  86:103 */     return buffer.toString();
/*  87:    */   }
/*  88:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.ChartFRTInfoRecord
 * JD-Core Version:    0.7.0.1
 */