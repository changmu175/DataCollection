/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ 
/*   5:    */ public final class GutsRecord
/*   6:    */   extends StandardRecord
/*   7:    */   implements Cloneable
/*   8:    */ {
/*   9:    */   public static final short sid = 128;
/*  10:    */   private short field_1_left_row_gutter;
/*  11:    */   private short field_2_top_col_gutter;
/*  12:    */   private short field_3_row_level_max;
/*  13:    */   private short field_4_col_level_max;
/*  14:    */   
/*  15:    */   public GutsRecord() {}
/*  16:    */   
/*  17:    */   public GutsRecord(RecordInputStream in)
/*  18:    */   {
/*  19: 46 */     this.field_1_left_row_gutter = in.readShort();
/*  20: 47 */     this.field_2_top_col_gutter = in.readShort();
/*  21: 48 */     this.field_3_row_level_max = in.readShort();
/*  22: 49 */     this.field_4_col_level_max = in.readShort();
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setLeftRowGutter(short gut)
/*  26:    */   {
/*  27: 60 */     this.field_1_left_row_gutter = gut;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setTopColGutter(short gut)
/*  31:    */   {
/*  32: 71 */     this.field_2_top_col_gutter = gut;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setRowLevelMax(short max)
/*  36:    */   {
/*  37: 82 */     this.field_3_row_level_max = max;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setColLevelMax(short max)
/*  41:    */   {
/*  42: 93 */     this.field_4_col_level_max = max;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public short getLeftRowGutter()
/*  46:    */   {
/*  47:104 */     return this.field_1_left_row_gutter;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public short getTopColGutter()
/*  51:    */   {
/*  52:115 */     return this.field_2_top_col_gutter;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public short getRowLevelMax()
/*  56:    */   {
/*  57:126 */     return this.field_3_row_level_max;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public short getColLevelMax()
/*  61:    */   {
/*  62:137 */     return this.field_4_col_level_max;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public String toString()
/*  66:    */   {
/*  67:142 */     StringBuffer buffer = new StringBuffer();
/*  68:    */     
/*  69:144 */     buffer.append("[GUTS]\n");
/*  70:145 */     buffer.append("    .leftgutter     = ").append(Integer.toHexString(getLeftRowGutter())).append("\n");
/*  71:    */     
/*  72:147 */     buffer.append("    .topgutter      = ").append(Integer.toHexString(getTopColGutter())).append("\n");
/*  73:    */     
/*  74:149 */     buffer.append("    .rowlevelmax    = ").append(Integer.toHexString(getRowLevelMax())).append("\n");
/*  75:    */     
/*  76:151 */     buffer.append("    .collevelmax    = ").append(Integer.toHexString(getColLevelMax())).append("\n");
/*  77:    */     
/*  78:153 */     buffer.append("[/GUTS]\n");
/*  79:154 */     return buffer.toString();
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void serialize(LittleEndianOutput out)
/*  83:    */   {
/*  84:158 */     out.writeShort(getLeftRowGutter());
/*  85:159 */     out.writeShort(getTopColGutter());
/*  86:160 */     out.writeShort(getRowLevelMax());
/*  87:161 */     out.writeShort(getColLevelMax());
/*  88:    */   }
/*  89:    */   
/*  90:    */   protected int getDataSize()
/*  91:    */   {
/*  92:165 */     return 8;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public short getSid()
/*  96:    */   {
/*  97:170 */     return 128;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public GutsRecord clone()
/* 101:    */   {
/* 102:175 */     GutsRecord rec = new GutsRecord();
/* 103:176 */     rec.field_1_left_row_gutter = this.field_1_left_row_gutter;
/* 104:177 */     rec.field_2_top_col_gutter = this.field_2_top_col_gutter;
/* 105:178 */     rec.field_3_row_level_max = this.field_3_row_level_max;
/* 106:179 */     rec.field_4_col_level_max = this.field_4_col_level_max;
/* 107:180 */     return rec;
/* 108:    */   }
/* 109:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.GutsRecord
 * JD-Core Version:    0.7.0.1
 */