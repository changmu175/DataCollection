/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.LittleEndianOutput;
/*   5:    */ 
/*   6:    */ public final class PaneRecord
/*   7:    */   extends StandardRecord
/*   8:    */ {
/*   9:    */   public static final short sid = 65;
/*  10:    */   private short field_1_x;
/*  11:    */   private short field_2_y;
/*  12:    */   private short field_3_topRow;
/*  13:    */   private short field_4_leftColumn;
/*  14:    */   private short field_5_activePane;
/*  15:    */   public static final short ACTIVE_PANE_LOWER_RIGHT = 0;
/*  16:    */   public static final short ACTIVE_PANE_UPPER_RIGHT = 1;
/*  17:    */   public static final short ACTIVE_PANE_LOWER_LEFT = 2;
/*  18:    */   public static final short ACTIVE_PANE_UPPER_LEFT = 3;
/*  19:    */   
/*  20:    */   public PaneRecord() {}
/*  21:    */   
/*  22:    */   public PaneRecord(RecordInputStream in)
/*  23:    */   {
/*  24: 47 */     this.field_1_x = in.readShort();
/*  25: 48 */     this.field_2_y = in.readShort();
/*  26: 49 */     this.field_3_topRow = in.readShort();
/*  27: 50 */     this.field_4_leftColumn = in.readShort();
/*  28: 51 */     this.field_5_activePane = in.readShort();
/*  29:    */   }
/*  30:    */   
/*  31:    */   public String toString()
/*  32:    */   {
/*  33: 57 */     StringBuffer buffer = new StringBuffer();
/*  34:    */     
/*  35: 59 */     buffer.append("[PANE]\n");
/*  36: 60 */     buffer.append("    .x                    = ").append("0x").append(HexDump.toHex(getX())).append(" (").append(getX()).append(" )");
/*  37:    */     
/*  38:    */ 
/*  39: 63 */     buffer.append(System.getProperty("line.separator"));
/*  40: 64 */     buffer.append("    .y                    = ").append("0x").append(HexDump.toHex(getY())).append(" (").append(getY()).append(" )");
/*  41:    */     
/*  42:    */ 
/*  43: 67 */     buffer.append(System.getProperty("line.separator"));
/*  44: 68 */     buffer.append("    .topRow               = ").append("0x").append(HexDump.toHex(getTopRow())).append(" (").append(getTopRow()).append(" )");
/*  45:    */     
/*  46:    */ 
/*  47: 71 */     buffer.append(System.getProperty("line.separator"));
/*  48: 72 */     buffer.append("    .leftColumn           = ").append("0x").append(HexDump.toHex(getLeftColumn())).append(" (").append(getLeftColumn()).append(" )");
/*  49:    */     
/*  50:    */ 
/*  51: 75 */     buffer.append(System.getProperty("line.separator"));
/*  52: 76 */     buffer.append("    .activePane           = ").append("0x").append(HexDump.toHex(getActivePane())).append(" (").append(getActivePane()).append(" )");
/*  53:    */     
/*  54:    */ 
/*  55: 79 */     buffer.append(System.getProperty("line.separator"));
/*  56:    */     
/*  57: 81 */     buffer.append("[/PANE]\n");
/*  58: 82 */     return buffer.toString();
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void serialize(LittleEndianOutput out)
/*  62:    */   {
/*  63: 87 */     out.writeShort(this.field_1_x);
/*  64: 88 */     out.writeShort(this.field_2_y);
/*  65: 89 */     out.writeShort(this.field_3_topRow);
/*  66: 90 */     out.writeShort(this.field_4_leftColumn);
/*  67: 91 */     out.writeShort(this.field_5_activePane);
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected int getDataSize()
/*  71:    */   {
/*  72: 96 */     return 10;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public short getSid()
/*  76:    */   {
/*  77:102 */     return 65;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public Object clone()
/*  81:    */   {
/*  82:107 */     PaneRecord rec = new PaneRecord();
/*  83:    */     
/*  84:109 */     rec.field_1_x = this.field_1_x;
/*  85:110 */     rec.field_2_y = this.field_2_y;
/*  86:111 */     rec.field_3_topRow = this.field_3_topRow;
/*  87:112 */     rec.field_4_leftColumn = this.field_4_leftColumn;
/*  88:113 */     rec.field_5_activePane = this.field_5_activePane;
/*  89:114 */     return rec;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public short getX()
/*  93:    */   {
/*  94:124 */     return this.field_1_x;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void setX(short field_1_x)
/*  98:    */   {
/*  99:134 */     this.field_1_x = field_1_x;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public short getY()
/* 103:    */   {
/* 104:144 */     return this.field_2_y;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void setY(short field_2_y)
/* 108:    */   {
/* 109:154 */     this.field_2_y = field_2_y;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public short getTopRow()
/* 113:    */   {
/* 114:164 */     return this.field_3_topRow;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void setTopRow(short field_3_topRow)
/* 118:    */   {
/* 119:174 */     this.field_3_topRow = field_3_topRow;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public short getLeftColumn()
/* 123:    */   {
/* 124:184 */     return this.field_4_leftColumn;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public void setLeftColumn(short field_4_leftColumn)
/* 128:    */   {
/* 129:194 */     this.field_4_leftColumn = field_4_leftColumn;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public short getActivePane()
/* 133:    */   {
/* 134:208 */     return this.field_5_activePane;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public void setActivePane(short field_5_activePane)
/* 138:    */   {
/* 139:223 */     this.field_5_activePane = field_5_activePane;
/* 140:    */   }
/* 141:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.PaneRecord
 * JD-Core Version:    0.7.0.1
 */