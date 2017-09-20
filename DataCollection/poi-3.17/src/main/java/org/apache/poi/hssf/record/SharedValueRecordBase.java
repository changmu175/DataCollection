/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.util.CellRangeAddress8Bit;
/*   4:    */ import org.apache.poi.util.LittleEndianInput;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ 
/*   7:    */ public abstract class SharedValueRecordBase
/*   8:    */   extends StandardRecord
/*   9:    */ {
/*  10:    */   private CellRangeAddress8Bit _range;
/*  11:    */   
/*  12:    */   protected SharedValueRecordBase(CellRangeAddress8Bit range)
/*  13:    */   {
/*  14: 33 */     if (range == null) {
/*  15: 34 */       throw new IllegalArgumentException("range must be supplied.");
/*  16:    */     }
/*  17: 36 */     this._range = range;
/*  18:    */   }
/*  19:    */   
/*  20:    */   protected SharedValueRecordBase()
/*  21:    */   {
/*  22: 40 */     this(new CellRangeAddress8Bit(0, 0, 0, 0));
/*  23:    */   }
/*  24:    */   
/*  25:    */   public SharedValueRecordBase(LittleEndianInput in)
/*  26:    */   {
/*  27: 47 */     this._range = new CellRangeAddress8Bit(in);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public final CellRangeAddress8Bit getRange()
/*  31:    */   {
/*  32: 54 */     return this._range;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public final int getFirstRow()
/*  36:    */   {
/*  37: 58 */     return this._range.getFirstRow();
/*  38:    */   }
/*  39:    */   
/*  40:    */   public final int getLastRow()
/*  41:    */   {
/*  42: 62 */     return this._range.getLastRow();
/*  43:    */   }
/*  44:    */   
/*  45:    */   public final int getFirstColumn()
/*  46:    */   {
/*  47: 66 */     return (short)this._range.getFirstColumn();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public final int getLastColumn()
/*  51:    */   {
/*  52: 70 */     return (short)this._range.getLastColumn();
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected int getDataSize()
/*  56:    */   {
/*  57: 74 */     return 6 + getExtraDataSize();
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected abstract int getExtraDataSize();
/*  61:    */   
/*  62:    */   protected abstract void serializeExtraData(LittleEndianOutput paramLittleEndianOutput);
/*  63:    */   
/*  64:    */   public void serialize(LittleEndianOutput out)
/*  65:    */   {
/*  66: 82 */     this._range.serialize(out);
/*  67: 83 */     serializeExtraData(out);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public final boolean isInRange(int rowIx, int colIx)
/*  71:    */   {
/*  72: 95 */     CellRangeAddress8Bit r = this._range;
/*  73: 96 */     return (r.getFirstRow() <= rowIx) && (r.getLastRow() >= rowIx) && (r.getFirstColumn() <= colIx) && (r.getLastColumn() >= colIx);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public final boolean isFirstCell(int rowIx, int colIx)
/*  77:    */   {
/*  78:113 */     CellRangeAddress8Bit r = getRange();
/*  79:114 */     return (r.getFirstRow() == rowIx) && (r.getFirstColumn() == colIx);
/*  80:    */   }
/*  81:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.SharedValueRecordBase
 * JD-Core Version:    0.7.0.1
 */