/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.util.HexDump;
/*   7:    */ import org.apache.poi.util.LittleEndian;
/*   8:    */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*   9:    */ import org.apache.poi.util.LittleEndianInputStream;
/*  10:    */ import org.apache.poi.util.RecordFormatException;
/*  11:    */ 
/*  12:    */ public final class ObjRecord
/*  13:    */   extends Record
/*  14:    */   implements Cloneable
/*  15:    */ {
/*  16:    */   public static final short sid = 93;
/*  17:    */   private static final int NORMAL_PAD_ALIGNMENT = 2;
/*  18: 39 */   private static int MAX_PAD_ALIGNMENT = 4;
/*  19:    */   private List<SubRecord> subrecords;
/*  20:    */   private final byte[] _uninterpretedData;
/*  21:    */   private boolean _isPaddedToQuadByteMultiple;
/*  22:    */   
/*  23:    */   public ObjRecord()
/*  24:    */   {
/*  25: 54 */     this.subrecords = new ArrayList(2);
/*  26:    */     
/*  27: 56 */     this._uninterpretedData = null;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public ObjRecord(RecordInputStream in)
/*  31:    */   {
/*  32: 69 */     byte[] subRecordData = in.readRemainder();
/*  33: 70 */     if (LittleEndian.getUShort(subRecordData, 0) != 21)
/*  34:    */     {
/*  35: 74 */       this._uninterpretedData = subRecordData;
/*  36: 75 */       this.subrecords = null;
/*  37: 76 */       return;
/*  38:    */     }
/*  39: 87 */     this.subrecords = new ArrayList();
/*  40: 88 */     ByteArrayInputStream bais = new ByteArrayInputStream(subRecordData);
/*  41: 89 */     LittleEndianInputStream subRecStream = new LittleEndianInputStream(bais);
/*  42: 90 */     CommonObjectDataSubRecord cmo = (CommonObjectDataSubRecord)SubRecord.createSubRecord(subRecStream, 0);
/*  43: 91 */     this.subrecords.add(cmo);
/*  44:    */     for (;;)
/*  45:    */     {
/*  46: 93 */       SubRecord subRecord = SubRecord.createSubRecord(subRecStream, cmo.getObjectType());
/*  47: 94 */       this.subrecords.add(subRecord);
/*  48: 95 */       if (subRecord.isTerminating()) {
/*  49:    */         break;
/*  50:    */       }
/*  51:    */     }
/*  52: 99 */     int nRemainingBytes = bais.available();
/*  53:100 */     if (nRemainingBytes > 0)
/*  54:    */     {
/*  55:102 */       this._isPaddedToQuadByteMultiple = (subRecordData.length % MAX_PAD_ALIGNMENT == 0);
/*  56:103 */       if (nRemainingBytes >= (this._isPaddedToQuadByteMultiple ? MAX_PAD_ALIGNMENT : 2))
/*  57:    */       {
/*  58:104 */         if (!canPaddingBeDiscarded(subRecordData, nRemainingBytes))
/*  59:    */         {
/*  60:105 */           String msg = "Leftover " + nRemainingBytes + " bytes in subrecord data " + HexDump.toHex(subRecordData);
/*  61:    */           
/*  62:107 */           throw new RecordFormatException(msg);
/*  63:    */         }
/*  64:109 */         this._isPaddedToQuadByteMultiple = false;
/*  65:    */       }
/*  66:    */     }
/*  67:    */     else
/*  68:    */     {
/*  69:113 */       this._isPaddedToQuadByteMultiple = false;
/*  70:    */     }
/*  71:115 */     this._uninterpretedData = null;
/*  72:    */   }
/*  73:    */   
/*  74:    */   private static boolean canPaddingBeDiscarded(byte[] data, int nRemainingBytes)
/*  75:    */   {
/*  76:129 */     for (int i = data.length - nRemainingBytes; i < data.length; i++) {
/*  77:130 */       if (data[i] != 0) {
/*  78:131 */         return false;
/*  79:    */       }
/*  80:    */     }
/*  81:134 */     return true;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public String toString()
/*  85:    */   {
/*  86:139 */     StringBuffer sb = new StringBuffer();
/*  87:    */     
/*  88:141 */     sb.append("[OBJ]\n");
/*  89:142 */     if (this.subrecords != null) {
/*  90:143 */       for (SubRecord record : this.subrecords) {
/*  91:144 */         sb.append("SUBRECORD: ").append(record);
/*  92:    */       }
/*  93:    */     }
/*  94:147 */     sb.append("[/OBJ]\n");
/*  95:148 */     return sb.toString();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public int getRecordSize()
/*  99:    */   {
/* 100:153 */     if (this._uninterpretedData != null) {
/* 101:154 */       return this._uninterpretedData.length + 4;
/* 102:    */     }
/* 103:156 */     int size = 0;
/* 104:157 */     for (SubRecord record : this.subrecords) {
/* 105:158 */       size += record.getDataSize() + 4;
/* 106:    */     }
/* 107:160 */     if (this._isPaddedToQuadByteMultiple) {
/* 108:161 */       while (size % MAX_PAD_ALIGNMENT != 0) {
/* 109:162 */         size++;
/* 110:    */       }
/* 111:    */     }
/* 112:165 */     while (size % 2 != 0) {
/* 113:166 */       size++;
/* 114:    */     }
/* 115:169 */     return size + 4;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public int serialize(int offset, byte[] data)
/* 119:    */   {
/* 120:174 */     int recSize = getRecordSize();
/* 121:175 */     int dataSize = recSize - 4;
/* 122:176 */     LittleEndianByteArrayOutputStream out = new LittleEndianByteArrayOutputStream(data, offset, recSize);
/* 123:    */     
/* 124:178 */     out.writeShort(93);
/* 125:179 */     out.writeShort(dataSize);
/* 126:181 */     if (this._uninterpretedData == null)
/* 127:    */     {
/* 128:183 */       for (int i = 0; i < this.subrecords.size(); i++)
/* 129:    */       {
/* 130:184 */         SubRecord record = (SubRecord)this.subrecords.get(i);
/* 131:185 */         record.serialize(out);
/* 132:    */       }
/* 133:187 */       int expectedEndIx = offset + dataSize;
/* 134:189 */       while (out.getWriteIndex() < expectedEndIx) {
/* 135:190 */         out.writeByte(0);
/* 136:    */       }
/* 137:    */     }
/* 138:    */     else
/* 139:    */     {
/* 140:193 */       out.write(this._uninterpretedData);
/* 141:    */     }
/* 142:195 */     return recSize;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public short getSid()
/* 146:    */   {
/* 147:200 */     return 93;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public List<SubRecord> getSubRecords()
/* 151:    */   {
/* 152:205 */     return this.subrecords;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void clearSubRecords()
/* 156:    */   {
/* 157:209 */     this.subrecords.clear();
/* 158:    */   }
/* 159:    */   
/* 160:    */   public void addSubRecord(int index, SubRecord element)
/* 161:    */   {
/* 162:213 */     this.subrecords.add(index, element);
/* 163:    */   }
/* 164:    */   
/* 165:    */   public boolean addSubRecord(SubRecord o)
/* 166:    */   {
/* 167:217 */     return this.subrecords.add(o);
/* 168:    */   }
/* 169:    */   
/* 170:    */   public ObjRecord clone()
/* 171:    */   {
/* 172:222 */     ObjRecord rec = new ObjRecord();
/* 173:224 */     for (SubRecord record : this.subrecords) {
/* 174:225 */       rec.addSubRecord(record.clone());
/* 175:    */     }
/* 176:227 */     return rec;
/* 177:    */   }
/* 178:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.ObjRecord
 * JD-Core Version:    0.7.0.1
 */