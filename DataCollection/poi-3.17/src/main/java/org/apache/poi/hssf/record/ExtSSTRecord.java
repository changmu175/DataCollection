/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import org.apache.poi.hssf.record.cont.ContinuableRecord;
/*   5:    */ import org.apache.poi.hssf.record.cont.ContinuableRecordOutput;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ 
/*   8:    */ public final class ExtSSTRecord
/*   9:    */   extends ContinuableRecord
/*  10:    */ {
/*  11:    */   public static final short sid = 255;
/*  12:    */   public static final int DEFAULT_BUCKET_SIZE = 8;
/*  13:    */   public static final int MAX_BUCKETS = 128;
/*  14:    */   private short _stringsPerBucket;
/*  15:    */   private InfoSubRecord[] _sstInfos;
/*  16:    */   
/*  17:    */   public static final class InfoSubRecord
/*  18:    */   {
/*  19:    */     public static final int ENCODED_SIZE = 8;
/*  20:    */     private int field_1_stream_pos;
/*  21:    */     private int field_2_bucket_sst_offset;
/*  22:    */     private short field_3_zero;
/*  23:    */     
/*  24:    */     public InfoSubRecord(int streamPos, int bucketSstOffset)
/*  25:    */     {
/*  26: 56 */       this.field_1_stream_pos = streamPos;
/*  27: 57 */       this.field_2_bucket_sst_offset = bucketSstOffset;
/*  28:    */     }
/*  29:    */     
/*  30:    */     public InfoSubRecord(RecordInputStream in)
/*  31:    */     {
/*  32: 62 */       this.field_1_stream_pos = in.readInt();
/*  33: 63 */       this.field_2_bucket_sst_offset = in.readShort();
/*  34: 64 */       this.field_3_zero = in.readShort();
/*  35:    */     }
/*  36:    */     
/*  37:    */     public int getStreamPos()
/*  38:    */     {
/*  39: 68 */       return this.field_1_stream_pos;
/*  40:    */     }
/*  41:    */     
/*  42:    */     public int getBucketSSTOffset()
/*  43:    */     {
/*  44: 72 */       return this.field_2_bucket_sst_offset;
/*  45:    */     }
/*  46:    */     
/*  47:    */     public void serialize(LittleEndianOutput out)
/*  48:    */     {
/*  49: 76 */       out.writeInt(this.field_1_stream_pos);
/*  50: 77 */       out.writeShort(this.field_2_bucket_sst_offset);
/*  51: 78 */       out.writeShort(this.field_3_zero);
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   public ExtSSTRecord()
/*  56:    */   {
/*  57: 88 */     this._stringsPerBucket = 8;
/*  58: 89 */     this._sstInfos = new InfoSubRecord[0];
/*  59:    */   }
/*  60:    */   
/*  61:    */   public ExtSSTRecord(RecordInputStream in)
/*  62:    */   {
/*  63: 93 */     this._stringsPerBucket = in.readShort();
/*  64:    */     
/*  65: 95 */     int nInfos = in.remaining() / 8;
/*  66: 96 */     ArrayList<InfoSubRecord> lst = new ArrayList(nInfos);
/*  67: 98 */     while (in.available() > 0)
/*  68:    */     {
/*  69: 99 */       InfoSubRecord info = new InfoSubRecord(in);
/*  70:100 */       lst.add(info);
/*  71:102 */       if ((in.available() == 0) && (in.hasNextRecord()) && (in.getNextSid() == 60)) {
/*  72:103 */         in.nextRecord();
/*  73:    */       }
/*  74:    */     }
/*  75:106 */     this._sstInfos = ((InfoSubRecord[])lst.toArray(new InfoSubRecord[lst.size()]));
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setNumStringsPerBucket(short numStrings)
/*  79:    */   {
/*  80:110 */     this._stringsPerBucket = numStrings;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public String toString()
/*  84:    */   {
/*  85:114 */     StringBuffer buffer = new StringBuffer();
/*  86:    */     
/*  87:116 */     buffer.append("[EXTSST]\n");
/*  88:117 */     buffer.append("    .dsst           = ").append(Integer.toHexString(this._stringsPerBucket)).append("\n");
/*  89:    */     
/*  90:    */ 
/*  91:120 */     buffer.append("    .numInfoRecords = ").append(this._sstInfos.length).append("\n");
/*  92:122 */     for (int k = 0; k < this._sstInfos.length; k++)
/*  93:    */     {
/*  94:124 */       buffer.append("    .inforecord     = ").append(k).append("\n");
/*  95:125 */       buffer.append("    .streampos      = ").append(Integer.toHexString(this._sstInfos[k].getStreamPos())).append("\n");
/*  96:    */       
/*  97:    */ 
/*  98:128 */       buffer.append("    .sstoffset      = ").append(Integer.toHexString(this._sstInfos[k].getBucketSSTOffset())).append("\n");
/*  99:    */     }
/* 100:133 */     buffer.append("[/EXTSST]\n");
/* 101:134 */     return buffer.toString();
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void serialize(ContinuableRecordOutput out)
/* 105:    */   {
/* 106:138 */     out.writeShort(this._stringsPerBucket);
/* 107:139 */     for (int k = 0; k < this._sstInfos.length; k++) {
/* 108:140 */       this._sstInfos[k].serialize(out);
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   protected int getDataSize()
/* 113:    */   {
/* 114:144 */     return 2 + 8 * this._sstInfos.length;
/* 115:    */   }
/* 116:    */   
/* 117:    */   protected InfoSubRecord[] getInfoSubRecords()
/* 118:    */   {
/* 119:148 */     return this._sstInfos;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public static final int getNumberOfInfoRecsForStrings(int numStrings)
/* 123:    */   {
/* 124:152 */     int infoRecs = numStrings / 8;
/* 125:153 */     if (numStrings % 8 != 0) {
/* 126:154 */       infoRecs++;
/* 127:    */     }
/* 128:157 */     if (infoRecs > 128) {
/* 129:158 */       infoRecs = 128;
/* 130:    */     }
/* 131:159 */     return infoRecs;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public static final int getRecordSizeForStrings(int numStrings)
/* 135:    */   {
/* 136:170 */     return 6 + getNumberOfInfoRecsForStrings(numStrings) * 8;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public short getSid()
/* 140:    */   {
/* 141:174 */     return 255;
/* 142:    */   }
/* 143:    */   
/* 144:    */   public void setBucketOffsets(int[] bucketAbsoluteOffsets, int[] bucketRelativeOffsets)
/* 145:    */   {
/* 146:179 */     this._sstInfos = new InfoSubRecord[bucketAbsoluteOffsets.length];
/* 147:180 */     for (int i = 0; i < bucketAbsoluteOffsets.length; i++) {
/* 148:181 */       this._sstInfos[i] = new InfoSubRecord(bucketAbsoluteOffsets[i], bucketRelativeOffsets[i]);
/* 149:    */     }
/* 150:    */   }
/* 151:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.ExtSSTRecord
 * JD-Core Version:    0.7.0.1
 */