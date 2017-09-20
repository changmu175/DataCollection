/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.LittleEndianOutput;
/*   5:    */ 
/*   6:    */ public final class BlankRecord
/*   7:    */   extends StandardRecord
/*   8:    */   implements CellValueRecordInterface, Cloneable
/*   9:    */ {
/*  10:    */   public static final short sid = 513;
/*  11:    */   private int field_1_row;
/*  12:    */   private short field_2_col;
/*  13:    */   private short field_3_xf;
/*  14:    */   
/*  15:    */   public BlankRecord() {}
/*  16:    */   
/*  17:    */   public BlankRecord(RecordInputStream in)
/*  18:    */   {
/*  19: 44 */     this.field_1_row = in.readUShort();
/*  20: 45 */     this.field_2_col = in.readShort();
/*  21: 46 */     this.field_3_xf = in.readShort();
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setRow(int row)
/*  25:    */   {
/*  26: 55 */     this.field_1_row = row;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public int getRow()
/*  30:    */   {
/*  31: 65 */     return this.field_1_row;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public short getColumn()
/*  35:    */   {
/*  36: 75 */     return this.field_2_col;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setXFIndex(short xf)
/*  40:    */   {
/*  41: 86 */     this.field_3_xf = xf;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public short getXFIndex()
/*  45:    */   {
/*  46: 96 */     return this.field_3_xf;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setColumn(short col)
/*  50:    */   {
/*  51:107 */     this.field_2_col = col;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public short getSid()
/*  55:    */   {
/*  56:115 */     return 513;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public String toString()
/*  60:    */   {
/*  61:120 */     StringBuffer sb = new StringBuffer();
/*  62:    */     
/*  63:122 */     sb.append("[BLANK]\n");
/*  64:123 */     sb.append("    row= ").append(HexDump.shortToHex(getRow())).append("\n");
/*  65:124 */     sb.append("    col= ").append(HexDump.shortToHex(getColumn())).append("\n");
/*  66:125 */     sb.append("    xf = ").append(HexDump.shortToHex(getXFIndex())).append("\n");
/*  67:126 */     sb.append("[/BLANK]\n");
/*  68:127 */     return sb.toString();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void serialize(LittleEndianOutput out)
/*  72:    */   {
/*  73:131 */     out.writeShort(getRow());
/*  74:132 */     out.writeShort(getColumn());
/*  75:133 */     out.writeShort(getXFIndex());
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected int getDataSize()
/*  79:    */   {
/*  80:137 */     return 6;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public BlankRecord clone()
/*  84:    */   {
/*  85:142 */     BlankRecord rec = new BlankRecord();
/*  86:143 */     rec.field_1_row = this.field_1_row;
/*  87:144 */     rec.field_2_col = this.field_2_col;
/*  88:145 */     rec.field_3_xf = this.field_3_xf;
/*  89:146 */     return rec;
/*  90:    */   }
/*  91:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.BlankRecord
 * JD-Core Version:    0.7.0.1
 */