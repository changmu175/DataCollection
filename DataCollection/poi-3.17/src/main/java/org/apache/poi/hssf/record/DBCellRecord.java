/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.LittleEndianOutput;
/*   5:    */ 
/*   6:    */ public final class DBCellRecord
/*   7:    */   extends StandardRecord
/*   8:    */   implements Cloneable
/*   9:    */ {
/*  10:    */   public static final short sid = 215;
/*  11:    */   public static final int BLOCK_SIZE = 32;
/*  12:    */   private final int field_1_row_offset;
/*  13:    */   private final short[] field_2_cell_offsets;
/*  14:    */   
/*  15:    */   public static final class Builder
/*  16:    */   {
/*  17:    */     private short[] _cellOffsets;
/*  18:    */     private int _nCellOffsets;
/*  19:    */     
/*  20:    */     public Builder()
/*  21:    */     {
/*  22: 36 */       this._cellOffsets = new short[4];
/*  23:    */     }
/*  24:    */     
/*  25:    */     public void addCellOffset(int cellRefOffset)
/*  26:    */     {
/*  27: 40 */       if (this._cellOffsets.length <= this._nCellOffsets)
/*  28:    */       {
/*  29: 41 */         short[] temp = new short[this._nCellOffsets * 2];
/*  30: 42 */         System.arraycopy(this._cellOffsets, 0, temp, 0, this._nCellOffsets);
/*  31: 43 */         this._cellOffsets = temp;
/*  32:    */       }
/*  33: 45 */       this._cellOffsets[this._nCellOffsets] = ((short)cellRefOffset);
/*  34: 46 */       this._nCellOffsets += 1;
/*  35:    */     }
/*  36:    */     
/*  37:    */     public DBCellRecord build(int rowOffset)
/*  38:    */     {
/*  39: 50 */       short[] cellOffsets = new short[this._nCellOffsets];
/*  40: 51 */       System.arraycopy(this._cellOffsets, 0, cellOffsets, 0, this._nCellOffsets);
/*  41: 52 */       return new DBCellRecord(rowOffset, cellOffsets);
/*  42:    */     }
/*  43:    */   }
/*  44:    */   
/*  45:    */   DBCellRecord(int rowOffset, short[] cellOffsets)
/*  46:    */   {
/*  47: 63 */     this.field_1_row_offset = rowOffset;
/*  48: 64 */     this.field_2_cell_offsets = cellOffsets;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public DBCellRecord(RecordInputStream in)
/*  52:    */   {
/*  53: 68 */     this.field_1_row_offset = in.readUShort();
/*  54: 69 */     int size = in.remaining();
/*  55: 70 */     this.field_2_cell_offsets = new short[size / 2];
/*  56: 72 */     for (int i = 0; i < this.field_2_cell_offsets.length; i++) {
/*  57: 74 */       this.field_2_cell_offsets[i] = in.readShort();
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   public String toString()
/*  62:    */   {
/*  63: 80 */     StringBuffer buffer = new StringBuffer();
/*  64:    */     
/*  65: 82 */     buffer.append("[DBCELL]\n");
/*  66: 83 */     buffer.append("    .rowoffset = ").append(HexDump.intToHex(this.field_1_row_offset)).append("\n");
/*  67: 84 */     for (int k = 0; k < this.field_2_cell_offsets.length; k++) {
/*  68: 85 */       buffer.append("    .cell_").append(k).append(" = ").append(HexDump.shortToHex(this.field_2_cell_offsets[k])).append("\n");
/*  69:    */     }
/*  70: 88 */     buffer.append("[/DBCELL]\n");
/*  71: 89 */     return buffer.toString();
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void serialize(LittleEndianOutput out)
/*  75:    */   {
/*  76: 93 */     out.writeInt(this.field_1_row_offset);
/*  77: 94 */     for (int k = 0; k < this.field_2_cell_offsets.length; k++) {
/*  78: 95 */       out.writeShort(this.field_2_cell_offsets[k]);
/*  79:    */     }
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected int getDataSize()
/*  83:    */   {
/*  84: 99 */     return 4 + this.field_2_cell_offsets.length * 2;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public short getSid()
/*  88:    */   {
/*  89:103 */     return 215;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public DBCellRecord clone()
/*  93:    */   {
/*  94:109 */     return this;
/*  95:    */   }
/*  96:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.DBCellRecord
 * JD-Core Version:    0.7.0.1
 */