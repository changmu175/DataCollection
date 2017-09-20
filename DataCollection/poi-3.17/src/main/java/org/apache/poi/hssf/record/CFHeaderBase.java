/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*   4:    */ import org.apache.poi.ss.util.CellRangeAddressList;
/*   5:    */ import org.apache.poi.ss.util.CellRangeUtil;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ 
/*   8:    */ public abstract class CFHeaderBase
/*   9:    */   extends StandardRecord
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12:    */   private int field_1_numcf;
/*  13:    */   private int field_2_need_recalculation_and_id;
/*  14:    */   private CellRangeAddress field_3_enclosing_cell_range;
/*  15:    */   private CellRangeAddressList field_4_cell_ranges;
/*  16:    */   
/*  17:    */   protected CFHeaderBase() {}
/*  18:    */   
/*  19:    */   protected CFHeaderBase(CellRangeAddress[] regions, int nRules)
/*  20:    */   {
/*  21: 39 */     CellRangeAddress[] unmergedRanges = regions;
/*  22: 40 */     CellRangeAddress[] mergeCellRanges = CellRangeUtil.mergeCellRanges(unmergedRanges);
/*  23: 41 */     setCellRanges(mergeCellRanges);
/*  24: 42 */     this.field_1_numcf = nRules;
/*  25:    */   }
/*  26:    */   
/*  27:    */   protected void createEmpty()
/*  28:    */   {
/*  29: 46 */     this.field_3_enclosing_cell_range = new CellRangeAddress(0, 0, 0, 0);
/*  30: 47 */     this.field_4_cell_ranges = new CellRangeAddressList();
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected void read(RecordInputStream in)
/*  34:    */   {
/*  35: 50 */     this.field_1_numcf = in.readShort();
/*  36: 51 */     this.field_2_need_recalculation_and_id = in.readShort();
/*  37: 52 */     this.field_3_enclosing_cell_range = new CellRangeAddress(in);
/*  38: 53 */     this.field_4_cell_ranges = new CellRangeAddressList(in);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public int getNumberOfConditionalFormats()
/*  42:    */   {
/*  43: 57 */     return this.field_1_numcf;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setNumberOfConditionalFormats(int n)
/*  47:    */   {
/*  48: 60 */     this.field_1_numcf = n;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public boolean getNeedRecalculation()
/*  52:    */   {
/*  53: 65 */     return (this.field_2_need_recalculation_and_id & 0x1) == 1;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setNeedRecalculation(boolean b)
/*  57:    */   {
/*  58: 69 */     if (b == getNeedRecalculation()) {
/*  59: 70 */       return;
/*  60:    */     }
/*  61: 71 */     if (b) {
/*  62: 72 */       this.field_2_need_recalculation_and_id += 1;
/*  63:    */     } else {
/*  64: 74 */       this.field_2_need_recalculation_and_id -= 1;
/*  65:    */     }
/*  66:    */   }
/*  67:    */   
/*  68:    */   public int getID()
/*  69:    */   {
/*  70: 80 */     return this.field_2_need_recalculation_and_id >> 1;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setID(int id)
/*  74:    */   {
/*  75: 84 */     boolean needsRecalc = getNeedRecalculation();
/*  76: 85 */     this.field_2_need_recalculation_and_id = (id << 1);
/*  77: 86 */     if (needsRecalc) {
/*  78: 87 */       this.field_2_need_recalculation_and_id += 1;
/*  79:    */     }
/*  80:    */   }
/*  81:    */   
/*  82:    */   public CellRangeAddress getEnclosingCellRange()
/*  83:    */   {
/*  84: 92 */     return this.field_3_enclosing_cell_range;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setEnclosingCellRange(CellRangeAddress cr)
/*  88:    */   {
/*  89: 95 */     this.field_3_enclosing_cell_range = cr;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setCellRanges(CellRangeAddress[] cellRanges)
/*  93:    */   {
/*  94:104 */     if (cellRanges == null) {
/*  95:105 */       throw new IllegalArgumentException("cellRanges must not be null");
/*  96:    */     }
/*  97:107 */     CellRangeAddressList cral = new CellRangeAddressList();
/*  98:108 */     CellRangeAddress enclosingRange = null;
/*  99:109 */     for (int i = 0; i < cellRanges.length; i++)
/* 100:    */     {
/* 101:110 */       CellRangeAddress cr = cellRanges[i];
/* 102:111 */       enclosingRange = CellRangeUtil.createEnclosingCellRange(cr, enclosingRange);
/* 103:112 */       cral.addCellRangeAddress(cr);
/* 104:    */     }
/* 105:114 */     this.field_3_enclosing_cell_range = enclosingRange;
/* 106:115 */     this.field_4_cell_ranges = cral;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public CellRangeAddress[] getCellRanges()
/* 110:    */   {
/* 111:119 */     return this.field_4_cell_ranges.getCellRangeAddresses();
/* 112:    */   }
/* 113:    */   
/* 114:    */   protected abstract String getRecordName();
/* 115:    */   
/* 116:    */   public String toString()
/* 117:    */   {
/* 118:124 */     StringBuffer buffer = new StringBuffer();
/* 119:    */     
/* 120:126 */     buffer.append("[").append(getRecordName()).append("]\n");
/* 121:127 */     buffer.append("\t.numCF             = ").append(getNumberOfConditionalFormats()).append("\n");
/* 122:128 */     buffer.append("\t.needRecalc        = ").append(getNeedRecalculation()).append("\n");
/* 123:129 */     buffer.append("\t.id                = ").append(getID()).append("\n");
/* 124:130 */     buffer.append("\t.enclosingCellRange= ").append(getEnclosingCellRange()).append("\n");
/* 125:131 */     buffer.append("\t.cfranges=[");
/* 126:132 */     for (int i = 0; i < this.field_4_cell_ranges.countRanges(); i++) {
/* 127:133 */       buffer.append(i == 0 ? "" : ",").append(this.field_4_cell_ranges.getCellRangeAddress(i));
/* 128:    */     }
/* 129:135 */     buffer.append("]\n");
/* 130:136 */     buffer.append("[/").append(getRecordName()).append("]\n");
/* 131:137 */     return buffer.toString();
/* 132:    */   }
/* 133:    */   
/* 134:    */   protected int getDataSize()
/* 135:    */   {
/* 136:141 */     return 12 + this.field_4_cell_ranges.getSize();
/* 137:    */   }
/* 138:    */   
/* 139:    */   public void serialize(LittleEndianOutput out)
/* 140:    */   {
/* 141:147 */     out.writeShort(this.field_1_numcf);
/* 142:148 */     out.writeShort(this.field_2_need_recalculation_and_id);
/* 143:149 */     this.field_3_enclosing_cell_range.serialize(out);
/* 144:150 */     this.field_4_cell_ranges.serialize(out);
/* 145:    */   }
/* 146:    */   
/* 147:    */   protected void copyTo(CFHeaderBase result)
/* 148:    */   {
/* 149:154 */     result.field_1_numcf = this.field_1_numcf;
/* 150:155 */     result.field_2_need_recalculation_and_id = this.field_2_need_recalculation_and_id;
/* 151:156 */     result.field_3_enclosing_cell_range = this.field_3_enclosing_cell_range.copy();
/* 152:157 */     result.field_4_cell_ranges = this.field_4_cell_ranges.copy();
/* 153:    */   }
/* 154:    */   
/* 155:    */   public abstract CFHeaderBase clone();
/* 156:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.CFHeaderBase
 * JD-Core Version:    0.7.0.1
 */