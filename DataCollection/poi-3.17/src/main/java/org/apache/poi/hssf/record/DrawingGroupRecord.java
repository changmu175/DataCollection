/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.ddf.EscherRecord;
/*   6:    */ import org.apache.poi.ddf.NullEscherSerializationListener;
/*   7:    */ import org.apache.poi.util.LittleEndian;
/*   8:    */ 
/*   9:    */ public final class DrawingGroupRecord
/*  10:    */   extends AbstractEscherHolderRecord
/*  11:    */ {
/*  12:    */   public static final short sid = 235;
/*  13:    */   static final int MAX_RECORD_SIZE = 8228;
/*  14:    */   private static final int MAX_DATA_SIZE = 8224;
/*  15:    */   
/*  16:    */   public DrawingGroupRecord() {}
/*  17:    */   
/*  18:    */   public DrawingGroupRecord(RecordInputStream in)
/*  19:    */   {
/*  20: 40 */     super(in);
/*  21:    */   }
/*  22:    */   
/*  23:    */   protected String getRecordName()
/*  24:    */   {
/*  25: 45 */     return "MSODRAWINGGROUP";
/*  26:    */   }
/*  27:    */   
/*  28:    */   public short getSid()
/*  29:    */   {
/*  30: 50 */     return 235;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public int serialize(int offset, byte[] data)
/*  34:    */   {
/*  35: 55 */     byte[] rawData = getRawData();
/*  36: 56 */     if ((getEscherRecords().size() == 0) && (rawData != null)) {
/*  37: 58 */       return writeData(offset, data, rawData);
/*  38:    */     }
/*  39: 60 */     byte[] buffer = new byte[getRawDataSize()];
/*  40: 61 */     int pos = 0;
/*  41: 62 */     for (Iterator<EscherRecord> iterator = getEscherRecords().iterator(); iterator.hasNext();)
/*  42:    */     {
/*  43: 64 */       EscherRecord r = (EscherRecord)iterator.next();
/*  44: 65 */       pos += r.serialize(pos, buffer, new NullEscherSerializationListener());
/*  45:    */     }
/*  46: 68 */     return writeData(offset, data, buffer);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void processChildRecords()
/*  50:    */   {
/*  51: 78 */     convertRawBytesToEscherRecords();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public int getRecordSize()
/*  55:    */   {
/*  56: 83 */     return grossSizeFromDataSize(getRawDataSize());
/*  57:    */   }
/*  58:    */   
/*  59:    */   private int getRawDataSize()
/*  60:    */   {
/*  61: 87 */     List<EscherRecord> escherRecords = getEscherRecords();
/*  62: 88 */     byte[] rawData = getRawData();
/*  63: 89 */     if ((escherRecords.size() == 0) && (rawData != null)) {
/*  64: 91 */       return rawData.length;
/*  65:    */     }
/*  66: 93 */     int size = 0;
/*  67: 94 */     for (Iterator<EscherRecord> iterator = escherRecords.iterator(); iterator.hasNext();)
/*  68:    */     {
/*  69: 96 */       EscherRecord r = (EscherRecord)iterator.next();
/*  70: 97 */       size += r.getRecordSize();
/*  71:    */     }
/*  72: 99 */     return size;
/*  73:    */   }
/*  74:    */   
/*  75:    */   static int grossSizeFromDataSize(int dataSize)
/*  76:    */   {
/*  77:104 */     return dataSize + ((dataSize - 1) / 8224 + 1) * 4;
/*  78:    */   }
/*  79:    */   
/*  80:    */   private int writeData(int offset, byte[] data, byte[] rawData)
/*  81:    */   {
/*  82:109 */     int writtenActualData = 0;
/*  83:110 */     int writtenRawData = 0;
/*  84:111 */     while (writtenRawData < rawData.length)
/*  85:    */     {
/*  86:113 */       int segmentLength = Math.min(rawData.length - writtenRawData, 8224);
/*  87:114 */       if (writtenRawData / 8224 >= 2) {
/*  88:115 */         writeContinueHeader(data, offset, segmentLength);
/*  89:    */       } else {
/*  90:117 */         writeHeader(data, offset, segmentLength);
/*  91:    */       }
/*  92:118 */       writtenActualData += 4;
/*  93:119 */       offset += 4;
/*  94:120 */       System.arraycopy(rawData, writtenRawData, data, offset, segmentLength);
/*  95:121 */       offset += segmentLength;
/*  96:122 */       writtenRawData += segmentLength;
/*  97:123 */       writtenActualData += segmentLength;
/*  98:    */     }
/*  99:125 */     return writtenActualData;
/* 100:    */   }
/* 101:    */   
/* 102:    */   private void writeHeader(byte[] data, int offset, int sizeExcludingHeader)
/* 103:    */   {
/* 104:130 */     LittleEndian.putShort(data, 0 + offset, getSid());
/* 105:131 */     LittleEndian.putShort(data, 2 + offset, (short)sizeExcludingHeader);
/* 106:    */   }
/* 107:    */   
/* 108:    */   private void writeContinueHeader(byte[] data, int offset, int sizeExcludingHeader)
/* 109:    */   {
/* 110:136 */     LittleEndian.putShort(data, 0 + offset, (short)60);
/* 111:137 */     LittleEndian.putShort(data, 2 + offset, (short)sizeExcludingHeader);
/* 112:    */   }
/* 113:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.DrawingGroupRecord
 * JD-Core Version:    0.7.0.1
 */