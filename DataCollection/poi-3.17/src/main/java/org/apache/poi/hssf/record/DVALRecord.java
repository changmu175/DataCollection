/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ 
/*   5:    */ public final class DVALRecord
/*   6:    */   extends StandardRecord
/*   7:    */   implements Cloneable
/*   8:    */ {
/*   9:    */   public static final short sid = 434;
/*  10:    */   private short field_1_options;
/*  11:    */   private int field_2_horiz_pos;
/*  12:    */   private int field_3_vert_pos;
/*  13:    */   private int field_cbo_id;
/*  14:    */   private int field_5_dv_no;
/*  15:    */   
/*  16:    */   public DVALRecord()
/*  17:    */   {
/*  18: 46 */     this.field_cbo_id = -1;
/*  19: 47 */     this.field_5_dv_no = 0;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public DVALRecord(RecordInputStream in)
/*  23:    */   {
/*  24: 51 */     this.field_1_options = in.readShort();
/*  25: 52 */     this.field_2_horiz_pos = in.readInt();
/*  26: 53 */     this.field_3_vert_pos = in.readInt();
/*  27: 54 */     this.field_cbo_id = in.readInt();
/*  28: 55 */     this.field_5_dv_no = in.readInt();
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setOptions(short options)
/*  32:    */   {
/*  33: 62 */     this.field_1_options = options;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setHorizontalPos(int horiz_pos)
/*  37:    */   {
/*  38: 69 */     this.field_2_horiz_pos = horiz_pos;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setVerticalPos(int vert_pos)
/*  42:    */   {
/*  43: 76 */     this.field_3_vert_pos = vert_pos;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setObjectID(int cboID)
/*  47:    */   {
/*  48: 84 */     this.field_cbo_id = cboID;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setDVRecNo(int dvNo)
/*  52:    */   {
/*  53: 92 */     this.field_5_dv_no = dvNo;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public short getOptions()
/*  57:    */   {
/*  58: 99 */     return this.field_1_options;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public int getHorizontalPos()
/*  62:    */   {
/*  63:106 */     return this.field_2_horiz_pos;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public int getVerticalPos()
/*  67:    */   {
/*  68:113 */     return this.field_3_vert_pos;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public int getObjectID()
/*  72:    */   {
/*  73:120 */     return this.field_cbo_id;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public int getDVRecNo()
/*  77:    */   {
/*  78:127 */     return this.field_5_dv_no;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public String toString()
/*  82:    */   {
/*  83:132 */     StringBuffer buffer = new StringBuffer();
/*  84:    */     
/*  85:134 */     buffer.append("[DVAL]\n");
/*  86:135 */     buffer.append("    .options      = ").append(getOptions()).append('\n');
/*  87:136 */     buffer.append("    .horizPos     = ").append(getHorizontalPos()).append('\n');
/*  88:137 */     buffer.append("    .vertPos      = ").append(getVerticalPos()).append('\n');
/*  89:138 */     buffer.append("    .comboObjectID   = ").append(Integer.toHexString(getObjectID())).append("\n");
/*  90:139 */     buffer.append("    .DVRecordsNumber = ").append(Integer.toHexString(getDVRecNo())).append("\n");
/*  91:140 */     buffer.append("[/DVAL]\n");
/*  92:141 */     return buffer.toString();
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void serialize(LittleEndianOutput out)
/*  96:    */   {
/*  97:146 */     out.writeShort(getOptions());
/*  98:147 */     out.writeInt(getHorizontalPos());
/*  99:148 */     out.writeInt(getVerticalPos());
/* 100:149 */     out.writeInt(getObjectID());
/* 101:150 */     out.writeInt(getDVRecNo());
/* 102:    */   }
/* 103:    */   
/* 104:    */   protected int getDataSize()
/* 105:    */   {
/* 106:154 */     return 18;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public short getSid()
/* 110:    */   {
/* 111:158 */     return 434;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public DVALRecord clone()
/* 115:    */   {
/* 116:163 */     DVALRecord rec = new DVALRecord();
/* 117:164 */     rec.field_1_options = this.field_1_options;
/* 118:165 */     rec.field_2_horiz_pos = this.field_2_horiz_pos;
/* 119:166 */     rec.field_3_vert_pos = this.field_3_vert_pos;
/* 120:167 */     rec.field_cbo_id = this.field_cbo_id;
/* 121:168 */     rec.field_5_dv_no = this.field_5_dv_no;
/* 122:169 */     return rec;
/* 123:    */   }
/* 124:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.DVALRecord
 * JD-Core Version:    0.7.0.1
 */