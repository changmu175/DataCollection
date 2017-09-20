/*   1:    */ package org.apache.poi.ss.formula.ptg;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.util.CellReference;
/*   4:    */ import org.apache.poi.util.BitField;
/*   5:    */ import org.apache.poi.util.BitFieldFactory;
/*   6:    */ import org.apache.poi.util.LittleEndianInput;
/*   7:    */ import org.apache.poi.util.LittleEndianOutput;
/*   8:    */ 
/*   9:    */ public abstract class RefPtgBase
/*  10:    */   extends OperandPtg
/*  11:    */ {
/*  12:    */   private int field_1_row;
/*  13:    */   private int field_2_col;
/*  14: 41 */   private static final BitField rowRelative = BitFieldFactory.getInstance(32768);
/*  15: 42 */   private static final BitField colRelative = BitFieldFactory.getInstance(16384);
/*  16: 50 */   private static final BitField column = BitFieldFactory.getInstance(16383);
/*  17:    */   
/*  18:    */   protected RefPtgBase() {}
/*  19:    */   
/*  20:    */   protected RefPtgBase(CellReference c)
/*  21:    */   {
/*  22: 57 */     setRow(c.getRow());
/*  23: 58 */     setColumn(c.getCol());
/*  24: 59 */     setColRelative(!c.isColAbsolute());
/*  25: 60 */     setRowRelative(!c.isRowAbsolute());
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected final void readCoordinates(LittleEndianInput in)
/*  29:    */   {
/*  30: 64 */     this.field_1_row = in.readUShort();
/*  31: 65 */     this.field_2_col = in.readUShort();
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected final void writeCoordinates(LittleEndianOutput out)
/*  35:    */   {
/*  36: 69 */     out.writeShort(this.field_1_row);
/*  37: 70 */     out.writeShort(this.field_2_col);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public final void setRow(int rowIndex)
/*  41:    */   {
/*  42: 74 */     this.field_1_row = rowIndex;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public final int getRow()
/*  46:    */   {
/*  47: 81 */     return this.field_1_row;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public final boolean isRowRelative()
/*  51:    */   {
/*  52: 85 */     return rowRelative.isSet(this.field_2_col);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public final void setRowRelative(boolean rel)
/*  56:    */   {
/*  57: 89 */     this.field_2_col = rowRelative.setBoolean(this.field_2_col, rel);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public final boolean isColRelative()
/*  61:    */   {
/*  62: 93 */     return colRelative.isSet(this.field_2_col);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public final void setColRelative(boolean rel)
/*  66:    */   {
/*  67: 97 */     this.field_2_col = colRelative.setBoolean(this.field_2_col, rel);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public final void setColumn(int col)
/*  71:    */   {
/*  72:101 */     this.field_2_col = column.setValue(this.field_2_col, col);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public final int getColumn()
/*  76:    */   {
/*  77:105 */     return column.getValue(this.field_2_col);
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected final String formatReferenceAsString()
/*  81:    */   {
/*  82:110 */     CellReference cr = new CellReference(getRow(), getColumn(), !isRowRelative(), !isColRelative());
/*  83:111 */     return cr.formatAsString();
/*  84:    */   }
/*  85:    */   
/*  86:    */   public final byte getDefaultOperandClass()
/*  87:    */   {
/*  88:116 */     return 0;
/*  89:    */   }
/*  90:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.RefPtgBase
 * JD-Core Version:    0.7.0.1
 */