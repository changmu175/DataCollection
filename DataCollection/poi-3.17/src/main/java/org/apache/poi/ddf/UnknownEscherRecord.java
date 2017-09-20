/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndian;
/*   7:    */ 
/*   8:    */ public final class UnknownEscherRecord
/*   9:    */   extends EscherRecord
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12: 31 */   private static final byte[] NO_BYTES = new byte[0];
/*  13: 34 */   private byte[] thedata = NO_BYTES;
/*  14:    */   private List<EscherRecord> _childRecords;
/*  15:    */   
/*  16:    */   public UnknownEscherRecord()
/*  17:    */   {
/*  18: 38 */     this._childRecords = new ArrayList();
/*  19:    */   }
/*  20:    */   
/*  21:    */   public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory)
/*  22:    */   {
/*  23: 43 */     int bytesRemaining = readHeader(data, offset);
/*  24:    */     
/*  25:    */ 
/*  26:    */ 
/*  27:    */ 
/*  28: 48 */     int available = data.length - (offset + 8);
/*  29: 49 */     if (bytesRemaining > available) {
/*  30: 50 */       bytesRemaining = available;
/*  31:    */     }
/*  32: 53 */     if (isContainerRecord())
/*  33:    */     {
/*  34: 54 */       int bytesWritten = 0;
/*  35: 55 */       this.thedata = new byte[0];
/*  36: 56 */       offset += 8;
/*  37: 57 */       bytesWritten += 8;
/*  38: 58 */       while (bytesRemaining > 0)
/*  39:    */       {
/*  40: 59 */         EscherRecord child = recordFactory.createRecord(data, offset);
/*  41: 60 */         int childBytesWritten = child.fillFields(data, offset, recordFactory);
/*  42: 61 */         bytesWritten += childBytesWritten;
/*  43: 62 */         offset += childBytesWritten;
/*  44: 63 */         bytesRemaining -= childBytesWritten;
/*  45: 64 */         getChildRecords().add(child);
/*  46:    */       }
/*  47: 66 */       return bytesWritten;
/*  48:    */     }
/*  49: 69 */     if (bytesRemaining < 0) {
/*  50: 70 */       bytesRemaining = 0;
/*  51:    */     }
/*  52: 73 */     this.thedata = new byte[bytesRemaining];
/*  53: 74 */     System.arraycopy(data, offset + 8, this.thedata, 0, bytesRemaining);
/*  54: 75 */     return bytesRemaining + 8;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  58:    */   {
/*  59: 80 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  60:    */     
/*  61: 82 */     LittleEndian.putShort(data, offset, getOptions());
/*  62: 83 */     LittleEndian.putShort(data, offset + 2, getRecordId());
/*  63: 84 */     int remainingBytes = this.thedata.length;
/*  64: 85 */     for (EscherRecord r : this._childRecords) {
/*  65: 86 */       remainingBytes += r.getRecordSize();
/*  66:    */     }
/*  67: 88 */     LittleEndian.putInt(data, offset + 4, remainingBytes);
/*  68: 89 */     System.arraycopy(this.thedata, 0, data, offset + 8, this.thedata.length);
/*  69: 90 */     int pos = offset + 8 + this.thedata.length;
/*  70: 91 */     for (EscherRecord r : this._childRecords) {
/*  71: 92 */       pos += r.serialize(pos, data, listener);
/*  72:    */     }
/*  73: 95 */     listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
/*  74: 96 */     return pos - offset;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public byte[] getData()
/*  78:    */   {
/*  79:103 */     return this.thedata;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public int getRecordSize()
/*  83:    */   {
/*  84:108 */     return 8 + this.thedata.length;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public List<EscherRecord> getChildRecords()
/*  88:    */   {
/*  89:113 */     return this._childRecords;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setChildRecords(List<EscherRecord> childRecords)
/*  93:    */   {
/*  94:118 */     this._childRecords = childRecords;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public UnknownEscherRecord clone()
/*  98:    */   {
/*  99:123 */     UnknownEscherRecord uer = new UnknownEscherRecord();
/* 100:124 */     uer.thedata = ((byte[])this.thedata.clone());
/* 101:125 */     uer.setOptions(getOptions());
/* 102:126 */     uer.setRecordId(getRecordId());
/* 103:127 */     return uer;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public String getRecordName()
/* 107:    */   {
/* 108:132 */     return "Unknown 0x" + HexDump.toHex(getRecordId());
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void addChildRecord(EscherRecord childRecord)
/* 112:    */   {
/* 113:136 */     getChildRecords().add(childRecord);
/* 114:    */   }
/* 115:    */   
/* 116:    */   protected Object[][] getAttributeMap()
/* 117:    */   {
/* 118:141 */     int numCh = getChildRecords().size();
/* 119:142 */     List<Object> chLst = new ArrayList(numCh * 2 + 2);
/* 120:143 */     chLst.add("children");
/* 121:144 */     chLst.add(Integer.valueOf(numCh));
/* 122:145 */     for (EscherRecord er : this._childRecords)
/* 123:    */     {
/* 124:146 */       chLst.add(er.getRecordName());
/* 125:147 */       chLst.add(er);
/* 126:    */     }
/* 127:150 */     return new Object[][] { { "isContainer", Boolean.valueOf(isContainerRecord()) }, chLst.toArray(), { "Extra Data", this.thedata } };
/* 128:    */   }
/* 129:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.UnknownEscherRecord
 * JD-Core Version:    0.7.0.1
 */