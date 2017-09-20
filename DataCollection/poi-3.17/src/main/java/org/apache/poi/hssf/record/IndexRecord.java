/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.IntList;
/*   4:    */ import org.apache.poi.util.LittleEndianOutput;
/*   5:    */ import org.apache.poi.util.RecordFormatException;
/*   6:    */ 
/*   7:    */ public final class IndexRecord
/*   8:    */   extends StandardRecord
/*   9:    */   implements Cloneable
/*  10:    */ {
/*  11:    */   public static final short sid = 523;
/*  12:    */   private int field_2_first_row;
/*  13:    */   private int field_3_last_row_add1;
/*  14:    */   private int field_4_zero;
/*  15:    */   private IntList field_5_dbcells;
/*  16:    */   
/*  17:    */   public IndexRecord() {}
/*  18:    */   
/*  19:    */   public IndexRecord(RecordInputStream in)
/*  20:    */   {
/*  21: 44 */     int field_1_zero = in.readInt();
/*  22: 45 */     if (field_1_zero != 0) {
/*  23: 46 */       throw new RecordFormatException("Expected zero for field 1 but got " + field_1_zero);
/*  24:    */     }
/*  25: 48 */     this.field_2_first_row = in.readInt();
/*  26: 49 */     this.field_3_last_row_add1 = in.readInt();
/*  27: 50 */     this.field_4_zero = in.readInt();
/*  28:    */     
/*  29: 52 */     int nCells = in.remaining() / 4;
/*  30: 53 */     this.field_5_dbcells = new IntList(nCells);
/*  31: 54 */     for (int i = 0; i < nCells; i++) {
/*  32: 55 */       this.field_5_dbcells.add(in.readInt());
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setFirstRow(int row)
/*  37:    */   {
/*  38: 61 */     this.field_2_first_row = row;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setLastRowAdd1(int row)
/*  42:    */   {
/*  43: 66 */     this.field_3_last_row_add1 = row;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void addDbcell(int cell)
/*  47:    */   {
/*  48: 71 */     if (this.field_5_dbcells == null) {
/*  49: 73 */       this.field_5_dbcells = new IntList();
/*  50:    */     }
/*  51: 75 */     this.field_5_dbcells.add(cell);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setDbcell(int cell, int value)
/*  55:    */   {
/*  56: 80 */     this.field_5_dbcells.set(cell, value);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public int getFirstRow()
/*  60:    */   {
/*  61: 85 */     return this.field_2_first_row;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public int getLastRowAdd1()
/*  65:    */   {
/*  66: 90 */     return this.field_3_last_row_add1;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public int getNumDbcells()
/*  70:    */   {
/*  71: 95 */     if (this.field_5_dbcells == null) {
/*  72: 97 */       return 0;
/*  73:    */     }
/*  74: 99 */     return this.field_5_dbcells.size();
/*  75:    */   }
/*  76:    */   
/*  77:    */   public int getDbcellAt(int cellnum)
/*  78:    */   {
/*  79:104 */     return this.field_5_dbcells.get(cellnum);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public String toString()
/*  83:    */   {
/*  84:110 */     StringBuffer buffer = new StringBuffer();
/*  85:    */     
/*  86:112 */     buffer.append("[INDEX]\n");
/*  87:113 */     buffer.append("    .firstrow       = ").append(Integer.toHexString(getFirstRow())).append("\n");
/*  88:    */     
/*  89:115 */     buffer.append("    .lastrowadd1    = ").append(Integer.toHexString(getLastRowAdd1())).append("\n");
/*  90:117 */     for (int k = 0; k < getNumDbcells(); k++) {
/*  91:118 */       buffer.append("    .dbcell_").append(k).append(" = ").append(Integer.toHexString(getDbcellAt(k))).append("\n");
/*  92:    */     }
/*  93:121 */     buffer.append("[/INDEX]\n");
/*  94:122 */     return buffer.toString();
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void serialize(LittleEndianOutput out)
/*  98:    */   {
/*  99:128 */     out.writeInt(0);
/* 100:129 */     out.writeInt(getFirstRow());
/* 101:130 */     out.writeInt(getLastRowAdd1());
/* 102:131 */     out.writeInt(this.field_4_zero);
/* 103:132 */     for (int k = 0; k < getNumDbcells(); k++) {
/* 104:133 */       out.writeInt(getDbcellAt(k));
/* 105:    */     }
/* 106:    */   }
/* 107:    */   
/* 108:    */   protected int getDataSize()
/* 109:    */   {
/* 110:139 */     return 16 + getNumDbcells() * 4;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public static int getRecordSizeForBlockCount(int blockCount)
/* 114:    */   {
/* 115:148 */     return 20 + 4 * blockCount;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public short getSid()
/* 119:    */   {
/* 120:153 */     return 523;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public IndexRecord clone()
/* 124:    */   {
/* 125:158 */     IndexRecord rec = new IndexRecord();
/* 126:159 */     rec.field_2_first_row = this.field_2_first_row;
/* 127:160 */     rec.field_3_last_row_add1 = this.field_3_last_row_add1;
/* 128:161 */     rec.field_4_zero = this.field_4_zero;
/* 129:162 */     rec.field_5_dbcells = new IntList();
/* 130:163 */     rec.field_5_dbcells.addAll(this.field_5_dbcells);
/* 131:164 */     return rec;
/* 132:    */   }
/* 133:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.IndexRecord
 * JD-Core Version:    0.7.0.1
 */