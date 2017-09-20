/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import java.io.PrintWriter;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.List;
/*   8:    */ import org.apache.poi.util.HexDump;
/*   9:    */ import org.apache.poi.util.LittleEndian;
/*  10:    */ import org.apache.poi.util.POILogFactory;
/*  11:    */ import org.apache.poi.util.POILogger;
/*  12:    */ import org.apache.poi.util.Removal;
/*  13:    */ 
/*  14:    */ public final class EscherContainerRecord
/*  15:    */   extends EscherRecord
/*  16:    */   implements Iterable<EscherRecord>
/*  17:    */ {
/*  18:    */   public static final short DGG_CONTAINER = -4096;
/*  19:    */   public static final short BSTORE_CONTAINER = -4095;
/*  20:    */   public static final short DG_CONTAINER = -4094;
/*  21:    */   public static final short SPGR_CONTAINER = -4093;
/*  22:    */   public static final short SP_CONTAINER = -4092;
/*  23:    */   public static final short SOLVER_CONTAINER = -4091;
/*  24: 46 */   private static final POILogger log = POILogFactory.getLogger(EscherContainerRecord.class);
/*  25:    */   private int _remainingLength;
/*  26: 72 */   private final List<EscherRecord> _childRecords = new ArrayList();
/*  27:    */   
/*  28:    */   public int fillFields(byte[] data, int pOffset, EscherRecordFactory recordFactory)
/*  29:    */   {
/*  30: 76 */     int bytesRemaining = readHeader(data, pOffset);
/*  31: 77 */     int bytesWritten = 8;
/*  32: 78 */     int offset = pOffset + 8;
/*  33: 79 */     while ((bytesRemaining > 0) && (offset < data.length))
/*  34:    */     {
/*  35: 80 */       EscherRecord child = recordFactory.createRecord(data, offset);
/*  36: 81 */       int childBytesWritten = child.fillFields(data, offset, recordFactory);
/*  37: 82 */       bytesWritten += childBytesWritten;
/*  38: 83 */       offset += childBytesWritten;
/*  39: 84 */       bytesRemaining -= childBytesWritten;
/*  40: 85 */       addChildRecord(child);
/*  41: 86 */       if ((offset >= data.length) && (bytesRemaining > 0))
/*  42:    */       {
/*  43: 87 */         this._remainingLength = bytesRemaining;
/*  44: 88 */         if (log.check(5)) {
/*  45: 89 */           log.log(5, new Object[] { "Not enough Escher data: " + bytesRemaining + " bytes remaining but no space left" });
/*  46:    */         }
/*  47:    */       }
/*  48:    */     }
/*  49: 93 */     return bytesWritten;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  53:    */   {
/*  54: 99 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  55:    */     
/*  56:101 */     LittleEndian.putShort(data, offset, getOptions());
/*  57:102 */     LittleEndian.putShort(data, offset + 2, getRecordId());
/*  58:103 */     int remainingBytes = 0;
/*  59:104 */     for (EscherRecord r : this) {
/*  60:105 */       remainingBytes += r.getRecordSize();
/*  61:    */     }
/*  62:107 */     remainingBytes += this._remainingLength;
/*  63:108 */     LittleEndian.putInt(data, offset + 4, remainingBytes);
/*  64:109 */     int pos = offset + 8;
/*  65:110 */     for (EscherRecord r : this) {
/*  66:111 */       pos += r.serialize(pos, data, listener);
/*  67:    */     }
/*  68:114 */     listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
/*  69:115 */     return pos - offset;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public int getRecordSize()
/*  73:    */   {
/*  74:120 */     int childRecordsSize = 0;
/*  75:121 */     for (EscherRecord r : this) {
/*  76:122 */       childRecordsSize += r.getRecordSize();
/*  77:    */     }
/*  78:124 */     return 8 + childRecordsSize;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public boolean hasChildOfType(short recordId)
/*  82:    */   {
/*  83:135 */     for (EscherRecord r : this) {
/*  84:136 */       if (r.getRecordId() == recordId) {
/*  85:137 */         return true;
/*  86:    */       }
/*  87:    */     }
/*  88:140 */     return false;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public EscherRecord getChild(int index)
/*  92:    */   {
/*  93:144 */     return (EscherRecord)this._childRecords.get(index);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public List<EscherRecord> getChildRecords()
/*  97:    */   {
/*  98:152 */     return new ArrayList(this._childRecords);
/*  99:    */   }
/* 100:    */   
/* 101:    */   @Removal(version="3.18")
/* 102:    */   @Deprecated
/* 103:    */   public Iterator<EscherRecord> getChildIterator()
/* 104:    */   {
/* 105:163 */     return iterator();
/* 106:    */   }
/* 107:    */   
/* 108:    */   public Iterator<EscherRecord> iterator()
/* 109:    */   {
/* 110:171 */     return Collections.unmodifiableList(this._childRecords).iterator();
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setChildRecords(List<EscherRecord> childRecords)
/* 114:    */   {
/* 115:179 */     if (childRecords == this._childRecords) {
/* 116:180 */       throw new IllegalStateException("Child records private data member has escaped");
/* 117:    */     }
/* 118:182 */     this._childRecords.clear();
/* 119:183 */     this._childRecords.addAll(childRecords);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public boolean removeChildRecord(EscherRecord toBeRemoved)
/* 123:    */   {
/* 124:193 */     return this._childRecords.remove(toBeRemoved);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public List<EscherContainerRecord> getChildContainers()
/* 128:    */   {
/* 129:205 */     List<EscherContainerRecord> containers = new ArrayList();
/* 130:206 */     for (EscherRecord r : this) {
/* 131:207 */       if ((r instanceof EscherContainerRecord)) {
/* 132:208 */         containers.add((EscherContainerRecord)r);
/* 133:    */       }
/* 134:    */     }
/* 135:211 */     return containers;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public String getRecordName()
/* 139:    */   {
/* 140:216 */     switch (getRecordId())
/* 141:    */     {
/* 142:    */     case -4096: 
/* 143:218 */       return "DggContainer";
/* 144:    */     case -4095: 
/* 145:220 */       return "BStoreContainer";
/* 146:    */     case -4094: 
/* 147:222 */       return "DgContainer";
/* 148:    */     case -4093: 
/* 149:224 */       return "SpgrContainer";
/* 150:    */     case -4092: 
/* 151:226 */       return "SpContainer";
/* 152:    */     case -4091: 
/* 153:228 */       return "SolverContainer";
/* 154:    */     }
/* 155:230 */     return "Container 0x" + HexDump.toHex(getRecordId());
/* 156:    */   }
/* 157:    */   
/* 158:    */   public void display(PrintWriter w, int indent)
/* 159:    */   {
/* 160:236 */     super.display(w, indent);
/* 161:237 */     for (EscherRecord escherRecord : this) {
/* 162:238 */       escherRecord.display(w, indent + 1);
/* 163:    */     }
/* 164:    */   }
/* 165:    */   
/* 166:    */   public void addChildRecord(EscherRecord record)
/* 167:    */   {
/* 168:248 */     this._childRecords.add(record);
/* 169:    */   }
/* 170:    */   
/* 171:    */   public void addChildBefore(EscherRecord record, int insertBeforeRecordId)
/* 172:    */   {
/* 173:258 */     int idx = 0;
/* 174:259 */     for (EscherRecord rec : this)
/* 175:    */     {
/* 176:260 */       if (rec.getRecordId() == (short)insertBeforeRecordId) {
/* 177:    */         break;
/* 178:    */       }
/* 179:264 */       idx++;
/* 180:    */     }
/* 181:266 */     this._childRecords.add(idx, record);
/* 182:    */   }
/* 183:    */   
/* 184:    */   public <T extends EscherRecord> T getChildById(short recordId)
/* 185:    */   {
/* 186:270 */     for (EscherRecord childRecord : this) {
/* 187:271 */       if (childRecord.getRecordId() == recordId)
/* 188:    */       {
/* 189:273 */         T result = childRecord;
/* 190:274 */         return result;
/* 191:    */       }
/* 192:    */     }
/* 193:277 */     return null;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public void getRecordsById(short recordId, List<EscherRecord> out)
/* 197:    */   {
/* 198:287 */     for (EscherRecord r : this) {
/* 199:288 */       if ((r instanceof EscherContainerRecord))
/* 200:    */       {
/* 201:289 */         EscherContainerRecord c = (EscherContainerRecord)r;
/* 202:290 */         c.getRecordsById(recordId, out);
/* 203:    */       }
/* 204:291 */       else if (r.getRecordId() == recordId)
/* 205:    */       {
/* 206:292 */         out.add(r);
/* 207:    */       }
/* 208:    */     }
/* 209:    */   }
/* 210:    */   
/* 211:    */   protected Object[][] getAttributeMap()
/* 212:    */   {
/* 213:299 */     List<Object> chList = new ArrayList(this._childRecords.size() * 2 + 2);
/* 214:300 */     chList.add("children");
/* 215:301 */     chList.add(Integer.valueOf(this._childRecords.size()));
/* 216:302 */     int count = 0;
/* 217:303 */     for (EscherRecord record : this)
/* 218:    */     {
/* 219:304 */       chList.add("Child " + count);
/* 220:305 */       chList.add(record);
/* 221:306 */       count++;
/* 222:    */     }
/* 223:308 */     return new Object[][] { { "isContainer", Boolean.valueOf(isContainerRecord()) }, chList.toArray() };
/* 224:    */   }
/* 225:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherContainerRecord
 * JD-Core Version:    0.7.0.1
 */