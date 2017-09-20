/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.ddf.DefaultEscherRecordFactory;
/*   6:    */ import org.apache.poi.ddf.EscherContainerRecord;
/*   7:    */ import org.apache.poi.ddf.EscherRecord;
/*   8:    */ import org.apache.poi.ddf.EscherRecordFactory;
/*   9:    */ import org.apache.poi.ddf.NullEscherSerializationListener;
/*  10:    */ import org.apache.poi.hssf.util.LazilyConcatenatedByteArray;
/*  11:    */ import org.apache.poi.util.LittleEndian;
/*  12:    */ 
/*  13:    */ public abstract class AbstractEscherHolderRecord
/*  14:    */   extends Record
/*  15:    */   implements Cloneable
/*  16:    */ {
/*  17:    */   private static boolean DESERIALISE;
/*  18:    */   private final List<EscherRecord> escherRecords;
/*  19:    */   
/*  20:    */   static
/*  21:    */   {
/*  22:    */     try
/*  23:    */     {
/*  24: 39 */       DESERIALISE = System.getProperty("poi.deserialize.escher") != null;
/*  25:    */     }
/*  26:    */     catch (SecurityException e)
/*  27:    */     {
/*  28: 41 */       DESERIALISE = false;
/*  29:    */     }
/*  30:    */   }
/*  31:    */   
/*  32: 46 */   private final LazilyConcatenatedByteArray rawDataContainer = new LazilyConcatenatedByteArray();
/*  33:    */   
/*  34:    */   public AbstractEscherHolderRecord()
/*  35:    */   {
/*  36: 50 */     this.escherRecords = new ArrayList();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public AbstractEscherHolderRecord(RecordInputStream in)
/*  40:    */   {
/*  41: 55 */     this.escherRecords = new ArrayList();
/*  42: 56 */     if (!DESERIALISE)
/*  43:    */     {
/*  44: 57 */       this.rawDataContainer.concatenate(in.readRemainder());
/*  45:    */     }
/*  46:    */     else
/*  47:    */     {
/*  48: 59 */       byte[] data = in.readAllContinuedRemainder();
/*  49: 60 */       convertToEscherRecords(0, data.length, data);
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected void convertRawBytesToEscherRecords()
/*  54:    */   {
/*  55: 65 */     if (!DESERIALISE)
/*  56:    */     {
/*  57: 66 */       byte[] rawData = getRawData();
/*  58: 67 */       convertToEscherRecords(0, rawData.length, rawData);
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   private void convertToEscherRecords(int offset, int size, byte[] data)
/*  63:    */   {
/*  64: 72 */     this.escherRecords.clear();
/*  65: 73 */     EscherRecordFactory recordFactory = new DefaultEscherRecordFactory();
/*  66: 74 */     int pos = offset;
/*  67: 75 */     while (pos < offset + size)
/*  68:    */     {
/*  69: 77 */       EscherRecord r = recordFactory.createRecord(data, pos);
/*  70: 78 */       int bytesRead = r.fillFields(data, pos, recordFactory);
/*  71: 79 */       this.escherRecords.add(r);
/*  72: 80 */       pos += bytesRead;
/*  73:    */     }
/*  74:    */   }
/*  75:    */   
/*  76:    */   public String toString()
/*  77:    */   {
/*  78: 87 */     StringBuffer buffer = new StringBuffer();
/*  79:    */     
/*  80: 89 */     String nl = System.getProperty("line.separator");
/*  81: 90 */     buffer.append('[' + getRecordName() + ']' + nl);
/*  82: 91 */     if (this.escherRecords.size() == 0) {
/*  83: 92 */       buffer.append("No Escher Records Decoded" + nl);
/*  84:    */     }
/*  85: 93 */     for (EscherRecord r : this.escherRecords) {
/*  86: 94 */       buffer.append(r);
/*  87:    */     }
/*  88: 96 */     buffer.append("[/" + getRecordName() + ']' + nl);
/*  89:    */     
/*  90: 98 */     return buffer.toString();
/*  91:    */   }
/*  92:    */   
/*  93:    */   public int serialize(int offset, byte[] data)
/*  94:    */   {
/*  95:106 */     LittleEndian.putShort(data, 0 + offset, getSid());
/*  96:107 */     LittleEndian.putShort(data, 2 + offset, (short)(getRecordSize() - 4));
/*  97:108 */     byte[] rawData = getRawData();
/*  98:109 */     if ((this.escherRecords.size() == 0) && (rawData != null))
/*  99:    */     {
/* 100:111 */       LittleEndian.putShort(data, 0 + offset, getSid());
/* 101:112 */       LittleEndian.putShort(data, 2 + offset, (short)(getRecordSize() - 4));
/* 102:113 */       System.arraycopy(rawData, 0, data, 4 + offset, rawData.length);
/* 103:114 */       return rawData.length + 4;
/* 104:    */     }
/* 105:116 */     LittleEndian.putShort(data, 0 + offset, getSid());
/* 106:117 */     LittleEndian.putShort(data, 2 + offset, (short)(getRecordSize() - 4));
/* 107:    */     
/* 108:119 */     int pos = offset + 4;
/* 109:120 */     for (EscherRecord r : this.escherRecords) {
/* 110:121 */       pos += r.serialize(pos, data, new NullEscherSerializationListener());
/* 111:    */     }
/* 112:123 */     return getRecordSize();
/* 113:    */   }
/* 114:    */   
/* 115:    */   public int getRecordSize()
/* 116:    */   {
/* 117:128 */     byte[] rawData = getRawData();
/* 118:129 */     if ((this.escherRecords.size() == 0) && (rawData != null)) {
/* 119:131 */       return rawData.length;
/* 120:    */     }
/* 121:133 */     int size = 0;
/* 122:134 */     for (EscherRecord r : this.escherRecords) {
/* 123:135 */       size += r.getRecordSize();
/* 124:    */     }
/* 125:137 */     return size;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public AbstractEscherHolderRecord clone()
/* 129:    */   {
/* 130:147 */     return (AbstractEscherHolderRecord)cloneViaReserialise();
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void addEscherRecord(int index, EscherRecord element)
/* 134:    */   {
/* 135:152 */     this.escherRecords.add(index, element);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public boolean addEscherRecord(EscherRecord element)
/* 139:    */   {
/* 140:157 */     return this.escherRecords.add(element);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public List<EscherRecord> getEscherRecords()
/* 144:    */   {
/* 145:162 */     return this.escherRecords;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public void clearEscherRecords()
/* 149:    */   {
/* 150:167 */     this.escherRecords.clear();
/* 151:    */   }
/* 152:    */   
/* 153:    */   public EscherContainerRecord getEscherContainer()
/* 154:    */   {
/* 155:178 */     for (EscherRecord er : this.escherRecords) {
/* 156:179 */       if ((er instanceof EscherContainerRecord)) {
/* 157:180 */         return (EscherContainerRecord)er;
/* 158:    */       }
/* 159:    */     }
/* 160:183 */     return null;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public EscherRecord findFirstWithId(short id)
/* 164:    */   {
/* 165:196 */     return findFirstWithId(id, getEscherRecords());
/* 166:    */   }
/* 167:    */   
/* 168:    */   private EscherRecord findFirstWithId(short id, List<EscherRecord> records)
/* 169:    */   {
/* 170:201 */     for (EscherRecord r : records) {
/* 171:202 */       if (r.getRecordId() == id) {
/* 172:203 */         return r;
/* 173:    */       }
/* 174:    */     }
/* 175:208 */     for (EscherRecord r : records) {
/* 176:209 */       if (r.isContainerRecord())
/* 177:    */       {
/* 178:210 */         EscherRecord found = findFirstWithId(id, r.getChildRecords());
/* 179:211 */         if (found != null) {
/* 180:212 */           return found;
/* 181:    */         }
/* 182:    */       }
/* 183:    */     }
/* 184:218 */     return null;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public EscherRecord getEscherRecord(int index)
/* 188:    */   {
/* 189:224 */     return (EscherRecord)this.escherRecords.get(index);
/* 190:    */   }
/* 191:    */   
/* 192:    */   public void join(AbstractEscherHolderRecord record)
/* 193:    */   {
/* 194:235 */     this.rawDataContainer.concatenate(record.getRawData());
/* 195:    */   }
/* 196:    */   
/* 197:    */   public void processContinueRecord(byte[] record)
/* 198:    */   {
/* 199:240 */     this.rawDataContainer.concatenate(record);
/* 200:    */   }
/* 201:    */   
/* 202:    */   public byte[] getRawData()
/* 203:    */   {
/* 204:245 */     return this.rawDataContainer.toArray();
/* 205:    */   }
/* 206:    */   
/* 207:    */   public void setRawData(byte[] rawData)
/* 208:    */   {
/* 209:250 */     this.rawDataContainer.clear();
/* 210:251 */     this.rawDataContainer.concatenate(rawData);
/* 211:    */   }
/* 212:    */   
/* 213:    */   public void decode()
/* 214:    */   {
/* 215:259 */     if ((null == this.escherRecords) || (0 == this.escherRecords.size()))
/* 216:    */     {
/* 217:260 */       byte[] rawData = getRawData();
/* 218:261 */       convertToEscherRecords(0, rawData.length, rawData);
/* 219:    */     }
/* 220:    */   }
/* 221:    */   
/* 222:    */   protected abstract String getRecordName();
/* 223:    */   
/* 224:    */   public abstract short getSid();
/* 225:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.AbstractEscherHolderRecord
 * JD-Core Version:    0.7.0.1
 */