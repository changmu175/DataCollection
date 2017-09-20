/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.BitSet;
/*   6:    */ import java.util.Collections;
/*   7:    */ import java.util.Comparator;
/*   8:    */ import java.util.Iterator;
/*   9:    */ import java.util.List;
/*  10:    */ import org.apache.poi.util.LittleEndian;
/*  11:    */ import org.apache.poi.util.RecordFormatException;
/*  12:    */ 
/*  13:    */ public final class EscherDggRecord
/*  14:    */   extends EscherRecord
/*  15:    */ {
/*  16:    */   public static final short RECORD_ID = -4090;
/*  17:    */   public static final String RECORD_DESCRIPTION = "MsofbtDgg";
/*  18:    */   private int field_1_shapeIdMax;
/*  19:    */   private int field_3_numShapesSaved;
/*  20:    */   private int field_4_drawingsSaved;
/*  21:    */   private final List<FileIdCluster> field_5_fileIdClusters;
/*  22:    */   private int maxDgId;
/*  23:    */   
/*  24:    */   public EscherDggRecord()
/*  25:    */   {
/*  26: 42 */     this.field_5_fileIdClusters = new ArrayList();
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static class FileIdCluster
/*  30:    */   {
/*  31:    */     private int field_1_drawingGroupId;
/*  32:    */     private int field_2_numShapeIdsUsed;
/*  33:    */     
/*  34:    */     public FileIdCluster(int drawingGroupId, int numShapeIdsUsed)
/*  35:    */     {
/*  36: 50 */       this.field_1_drawingGroupId = drawingGroupId;
/*  37: 51 */       this.field_2_numShapeIdsUsed = numShapeIdsUsed;
/*  38:    */     }
/*  39:    */     
/*  40:    */     public int getDrawingGroupId()
/*  41:    */     {
/*  42: 55 */       return this.field_1_drawingGroupId;
/*  43:    */     }
/*  44:    */     
/*  45:    */     public int getNumShapeIdsUsed()
/*  46:    */     {
/*  47: 59 */       return this.field_2_numShapeIdsUsed;
/*  48:    */     }
/*  49:    */     
/*  50:    */     private void incrementUsedShapeId()
/*  51:    */     {
/*  52: 63 */       this.field_2_numShapeIdsUsed += 1;
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory)
/*  57:    */   {
/*  58: 69 */     int bytesRemaining = readHeader(data, offset);
/*  59: 70 */     int pos = offset + 8;
/*  60: 71 */     int size = 0;
/*  61: 72 */     this.field_1_shapeIdMax = LittleEndian.getInt(data, pos + size);size += 4;
/*  62:    */     
/*  63: 74 */     size += 4;
/*  64: 75 */     this.field_3_numShapesSaved = LittleEndian.getInt(data, pos + size);size += 4;
/*  65: 76 */     this.field_4_drawingsSaved = LittleEndian.getInt(data, pos + size);size += 4;
/*  66:    */     
/*  67: 78 */     this.field_5_fileIdClusters.clear();
/*  68:    */     
/*  69: 80 */     int numIdClusters = (bytesRemaining - size) / 8;
/*  70: 82 */     for (int i = 0; i < numIdClusters; i++)
/*  71:    */     {
/*  72: 83 */       int drawingGroupId = LittleEndian.getInt(data, pos + size);
/*  73: 84 */       int numShapeIdsUsed = LittleEndian.getInt(data, pos + size + 4);
/*  74: 85 */       FileIdCluster fic = new FileIdCluster(drawingGroupId, numShapeIdsUsed);
/*  75: 86 */       this.field_5_fileIdClusters.add(fic);
/*  76: 87 */       this.maxDgId = Math.max(this.maxDgId, drawingGroupId);
/*  77: 88 */       size += 8;
/*  78:    */     }
/*  79: 90 */     bytesRemaining -= size;
/*  80: 91 */     if (bytesRemaining != 0) {
/*  81: 92 */       throw new RecordFormatException("Expecting no remaining data but got " + bytesRemaining + " byte(s).");
/*  82:    */     }
/*  83: 94 */     return 8 + size;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  87:    */   {
/*  88: 99 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  89:    */     
/*  90:101 */     int pos = offset;
/*  91:102 */     LittleEndian.putShort(data, pos, getOptions());pos += 2;
/*  92:103 */     LittleEndian.putShort(data, pos, getRecordId());pos += 2;
/*  93:104 */     int remainingBytes = getRecordSize() - 8;
/*  94:105 */     LittleEndian.putInt(data, pos, remainingBytes);pos += 4;
/*  95:    */     
/*  96:107 */     LittleEndian.putInt(data, pos, this.field_1_shapeIdMax);pos += 4;
/*  97:108 */     LittleEndian.putInt(data, pos, getNumIdClusters());pos += 4;
/*  98:109 */     LittleEndian.putInt(data, pos, this.field_3_numShapesSaved);pos += 4;
/*  99:110 */     LittleEndian.putInt(data, pos, this.field_4_drawingsSaved);pos += 4;
/* 100:112 */     for (Iterator i$ = this.field_5_fileIdClusters.iterator(); i$.hasNext(); pos += 4)
/* 101:    */     {
/* 102:112 */       FileIdCluster fic = (FileIdCluster)i$.next();
/* 103:113 */       LittleEndian.putInt(data, pos, fic.getDrawingGroupId());pos += 4;
/* 104:114 */       LittleEndian.putInt(data, pos, fic.getNumShapeIdsUsed());
/* 105:    */     }
/* 106:117 */     listener.afterRecordSerialize(pos, getRecordId(), getRecordSize(), this);
/* 107:118 */     return getRecordSize();
/* 108:    */   }
/* 109:    */   
/* 110:    */   public int getRecordSize()
/* 111:    */   {
/* 112:123 */     return 24 + 8 * this.field_5_fileIdClusters.size();
/* 113:    */   }
/* 114:    */   
/* 115:    */   public short getRecordId()
/* 116:    */   {
/* 117:128 */     return -4090;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public String getRecordName()
/* 121:    */   {
/* 122:133 */     return "Dgg";
/* 123:    */   }
/* 124:    */   
/* 125:    */   public int getShapeIdMax()
/* 126:    */   {
/* 127:142 */     return this.field_1_shapeIdMax;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void setShapeIdMax(int shapeIdMax)
/* 131:    */   {
/* 132:151 */     this.field_1_shapeIdMax = shapeIdMax;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public int getNumIdClusters()
/* 136:    */   {
/* 137:160 */     return this.field_5_fileIdClusters.isEmpty() ? 0 : this.field_5_fileIdClusters.size() + 1;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public int getNumShapesSaved()
/* 141:    */   {
/* 142:169 */     return this.field_3_numShapesSaved;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void setNumShapesSaved(int numShapesSaved)
/* 146:    */   {
/* 147:178 */     this.field_3_numShapesSaved = numShapesSaved;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public int getDrawingsSaved()
/* 151:    */   {
/* 152:187 */     return this.field_4_drawingsSaved;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void setDrawingsSaved(int drawingsSaved)
/* 156:    */   {
/* 157:196 */     this.field_4_drawingsSaved = drawingsSaved;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public int getMaxDrawingGroupId()
/* 161:    */   {
/* 162:205 */     return this.maxDgId;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public FileIdCluster[] getFileIdClusters()
/* 166:    */   {
/* 167:212 */     return (FileIdCluster[])this.field_5_fileIdClusters.toArray(new FileIdCluster[this.field_5_fileIdClusters.size()]);
/* 168:    */   }
/* 169:    */   
/* 170:    */   public void setFileIdClusters(FileIdCluster[] fileIdClusters)
/* 171:    */   {
/* 172:221 */     this.field_5_fileIdClusters.clear();
/* 173:222 */     if (fileIdClusters != null) {
/* 174:223 */       this.field_5_fileIdClusters.addAll(Arrays.asList(fileIdClusters));
/* 175:    */     }
/* 176:    */   }
/* 177:    */   
/* 178:    */   public FileIdCluster addCluster(int dgId, int numShapedUsed)
/* 179:    */   {
/* 180:237 */     return addCluster(dgId, numShapedUsed, true);
/* 181:    */   }
/* 182:    */   
/* 183:    */   public FileIdCluster addCluster(int dgId, int numShapedUsed, boolean sort)
/* 184:    */   {
/* 185:251 */     FileIdCluster ficNew = new FileIdCluster(dgId, numShapedUsed);
/* 186:252 */     this.field_5_fileIdClusters.add(ficNew);
/* 187:253 */     this.maxDgId = Math.min(this.maxDgId, dgId);
/* 188:255 */     if (sort) {
/* 189:256 */       sortCluster();
/* 190:    */     }
/* 191:259 */     return ficNew;
/* 192:    */   }
/* 193:    */   
/* 194:    */   private void sortCluster()
/* 195:    */   {
/* 196:263 */     Collections.sort(this.field_5_fileIdClusters, new Comparator()
/* 197:    */     {
/* 198:    */       public int compare(FileIdCluster f1, FileIdCluster f2)
/* 199:    */       {
/* 200:266 */         int dgDif = f1.getDrawingGroupId() - f2.getDrawingGroupId();
/* 201:267 */         int cntDif = f2.getNumShapeIdsUsed() - f1.getNumShapeIdsUsed();
/* 202:268 */         return dgDif != 0 ? dgDif : cntDif;
/* 203:    */       }
/* 204:    */     });
/* 205:    */   }
/* 206:    */   
/* 207:    */   public short findNewDrawingGroupId()
/* 208:    */   {
/* 209:279 */     BitSet bs = new BitSet();
/* 210:280 */     bs.set(0);
/* 211:281 */     for (FileIdCluster fic : this.field_5_fileIdClusters) {
/* 212:282 */       bs.set(fic.getDrawingGroupId());
/* 213:    */     }
/* 214:284 */     return (short)bs.nextClearBit(0);
/* 215:    */   }
/* 216:    */   
/* 217:    */   public int allocateShapeId(EscherDgRecord dg, boolean sort)
/* 218:    */   {
/* 219:297 */     short drawingGroupId = dg.getDrawingGroupId();
/* 220:298 */     this.field_3_numShapesSaved += 1;
/* 221:    */     
/* 222:    */ 
/* 223:    */ 
/* 224:    */ 
/* 225:303 */     FileIdCluster ficAdd = null;
/* 226:304 */     int index = 1;
/* 227:305 */     for (FileIdCluster fic : this.field_5_fileIdClusters)
/* 228:    */     {
/* 229:306 */       if ((fic.getDrawingGroupId() == drawingGroupId) && (fic.getNumShapeIdsUsed() < 1024))
/* 230:    */       {
/* 231:308 */         ficAdd = fic;
/* 232:309 */         break;
/* 233:    */       }
/* 234:311 */       index++;
/* 235:    */     }
/* 236:314 */     if (ficAdd == null)
/* 237:    */     {
/* 238:315 */       ficAdd = addCluster(drawingGroupId, 0, sort);
/* 239:316 */       this.maxDgId = Math.max(this.maxDgId, drawingGroupId);
/* 240:    */     }
/* 241:319 */     int shapeId = index * 1024 + ficAdd.getNumShapeIdsUsed();
/* 242:320 */     ficAdd.incrementUsedShapeId();
/* 243:    */     
/* 244:322 */     dg.setNumShapes(dg.getNumShapes() + 1);
/* 245:323 */     dg.setLastMSOSPID(shapeId);
/* 246:324 */     this.field_1_shapeIdMax = Math.max(this.field_1_shapeIdMax, shapeId + 1);
/* 247:    */     
/* 248:326 */     return shapeId;
/* 249:    */   }
/* 250:    */   
/* 251:    */   protected Object[][] getAttributeMap()
/* 252:    */   {
/* 253:332 */     List<Object> fldIds = new ArrayList();
/* 254:333 */     fldIds.add("FileId Clusters");
/* 255:334 */     fldIds.add(Integer.valueOf(this.field_5_fileIdClusters.size()));
/* 256:335 */     for (FileIdCluster fic : this.field_5_fileIdClusters)
/* 257:    */     {
/* 258:336 */       fldIds.add("Group" + fic.field_1_drawingGroupId);
/* 259:337 */       fldIds.add(Integer.valueOf(fic.field_2_numShapeIdsUsed));
/* 260:    */     }
/* 261:340 */     return new Object[][] { { "ShapeIdMax", Integer.valueOf(this.field_1_shapeIdMax) }, { "NumIdClusters", Integer.valueOf(getNumIdClusters()) }, { "NumShapesSaved", Integer.valueOf(this.field_3_numShapesSaved) }, { "DrawingsSaved", Integer.valueOf(this.field_4_drawingsSaved) }, fldIds.toArray() };
/* 262:    */   }
/* 263:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ddf.EscherDggRecord

 * JD-Core Version:    0.7.0.1

 */