/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ import org.apache.poi.util.POILogFactory;
/*   5:    */ import org.apache.poi.util.POILogger;
/*   6:    */ 
/*   7:    */ public final class DimensionsRecord
/*   8:    */   extends StandardRecord
/*   9:    */   implements Cloneable
/*  10:    */ {
/*  11: 38 */   private static final POILogger logger = POILogFactory.getLogger(DimensionsRecord.class);
/*  12:    */   public static final short sid = 512;
/*  13:    */   private int field_1_first_row;
/*  14:    */   private int field_2_last_row;
/*  15:    */   private short field_3_first_col;
/*  16:    */   private short field_4_last_col;
/*  17:    */   private short field_5_zero;
/*  18:    */   
/*  19:    */   public DimensionsRecord() {}
/*  20:    */   
/*  21:    */   public DimensionsRecord(RecordInputStream in)
/*  22:    */   {
/*  23: 53 */     this.field_1_first_row = in.readInt();
/*  24: 54 */     this.field_2_last_row = in.readInt();
/*  25: 55 */     this.field_3_first_col = in.readShort();
/*  26: 56 */     this.field_4_last_col = in.readShort();
/*  27: 57 */     this.field_5_zero = in.readShort();
/*  28: 59 */     if (in.available() == 2)
/*  29:    */     {
/*  30: 60 */       logger.log(3, new Object[] { "DimensionsRecord has extra 2 bytes." });
/*  31: 61 */       in.readShort();
/*  32:    */     }
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setFirstRow(int row)
/*  36:    */   {
/*  37: 72 */     this.field_1_first_row = row;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setLastRow(int row)
/*  41:    */   {
/*  42: 82 */     this.field_2_last_row = row;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setFirstCol(short col)
/*  46:    */   {
/*  47: 92 */     this.field_3_first_col = col;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setLastCol(short col)
/*  51:    */   {
/*  52:102 */     this.field_4_last_col = col;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public int getFirstRow()
/*  56:    */   {
/*  57:112 */     return this.field_1_first_row;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public int getLastRow()
/*  61:    */   {
/*  62:122 */     return this.field_2_last_row;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public short getFirstCol()
/*  66:    */   {
/*  67:132 */     return this.field_3_first_col;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public short getLastCol()
/*  71:    */   {
/*  72:142 */     return this.field_4_last_col;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public String toString()
/*  76:    */   {
/*  77:147 */     StringBuffer buffer = new StringBuffer();
/*  78:    */     
/*  79:149 */     buffer.append("[DIMENSIONS]\n");
/*  80:150 */     buffer.append("    .firstrow       = ").append(Integer.toHexString(getFirstRow())).append("\n");
/*  81:    */     
/*  82:152 */     buffer.append("    .lastrow        = ").append(Integer.toHexString(getLastRow())).append("\n");
/*  83:    */     
/*  84:154 */     buffer.append("    .firstcol       = ").append(Integer.toHexString(getFirstCol())).append("\n");
/*  85:    */     
/*  86:156 */     buffer.append("    .lastcol        = ").append(Integer.toHexString(getLastCol())).append("\n");
/*  87:    */     
/*  88:158 */     buffer.append("    .zero           = ").append(Integer.toHexString(this.field_5_zero)).append("\n");
/*  89:    */     
/*  90:160 */     buffer.append("[/DIMENSIONS]\n");
/*  91:161 */     return buffer.toString();
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void serialize(LittleEndianOutput out)
/*  95:    */   {
/*  96:165 */     out.writeInt(getFirstRow());
/*  97:166 */     out.writeInt(getLastRow());
/*  98:167 */     out.writeShort(getFirstCol());
/*  99:168 */     out.writeShort(getLastCol());
/* 100:169 */     out.writeShort(0);
/* 101:    */   }
/* 102:    */   
/* 103:    */   protected int getDataSize()
/* 104:    */   {
/* 105:173 */     return 14;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public short getSid()
/* 109:    */   {
/* 110:178 */     return 512;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public DimensionsRecord clone()
/* 114:    */   {
/* 115:183 */     DimensionsRecord rec = new DimensionsRecord();
/* 116:184 */     rec.field_1_first_row = this.field_1_first_row;
/* 117:185 */     rec.field_2_last_row = this.field_2_last_row;
/* 118:186 */     rec.field_3_first_col = this.field_3_first_col;
/* 119:187 */     rec.field_4_last_col = this.field_4_last_col;
/* 120:188 */     rec.field_5_zero = this.field_5_zero;
/* 121:189 */     return rec;
/* 122:    */   }
/* 123:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.DimensionsRecord
 * JD-Core Version:    0.7.0.1
 */