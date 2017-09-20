/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ 
/*   7:    */ public final class ChartRecord
/*   8:    */   extends StandardRecord
/*   9:    */   implements Cloneable
/*  10:    */ {
/*  11:    */   public static final short sid = 4098;
/*  12:    */   private int field_1_x;
/*  13:    */   private int field_2_y;
/*  14:    */   private int field_3_width;
/*  15:    */   private int field_4_height;
/*  16:    */   
/*  17:    */   public ChartRecord() {}
/*  18:    */   
/*  19:    */   public ChartRecord(RecordInputStream in)
/*  20:    */   {
/*  21: 54 */     this.field_1_x = in.readInt();
/*  22: 55 */     this.field_2_y = in.readInt();
/*  23: 56 */     this.field_3_width = in.readInt();
/*  24: 57 */     this.field_4_height = in.readInt();
/*  25:    */   }
/*  26:    */   
/*  27:    */   public String toString()
/*  28:    */   {
/*  29: 61 */     StringBuffer sb = new StringBuffer();
/*  30:    */     
/*  31: 63 */     sb.append("[CHART]\n");
/*  32: 64 */     sb.append("    .x     = ").append(getX()).append('\n');
/*  33: 65 */     sb.append("    .y     = ").append(getY()).append('\n');
/*  34: 66 */     sb.append("    .width = ").append(getWidth()).append('\n');
/*  35: 67 */     sb.append("    .height= ").append(getHeight()).append('\n');
/*  36: 68 */     sb.append("[/CHART]\n");
/*  37: 69 */     return sb.toString();
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void serialize(LittleEndianOutput out)
/*  41:    */   {
/*  42: 73 */     out.writeInt(this.field_1_x);
/*  43: 74 */     out.writeInt(this.field_2_y);
/*  44: 75 */     out.writeInt(this.field_3_width);
/*  45: 76 */     out.writeInt(this.field_4_height);
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected int getDataSize()
/*  49:    */   {
/*  50: 80 */     return 16;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public short getSid()
/*  54:    */   {
/*  55: 85 */     return 4098;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public ChartRecord clone()
/*  59:    */   {
/*  60: 90 */     ChartRecord rec = new ChartRecord();
/*  61:    */     
/*  62: 92 */     rec.field_1_x = this.field_1_x;
/*  63: 93 */     rec.field_2_y = this.field_2_y;
/*  64: 94 */     rec.field_3_width = this.field_3_width;
/*  65: 95 */     rec.field_4_height = this.field_4_height;
/*  66: 96 */     return rec;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public int getX()
/*  70:    */   {
/*  71:106 */     return this.field_1_x;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setX(int x)
/*  75:    */   {
/*  76:113 */     this.field_1_x = x;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public int getY()
/*  80:    */   {
/*  81:120 */     return this.field_2_y;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void setY(int y)
/*  85:    */   {
/*  86:127 */     this.field_2_y = y;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public int getWidth()
/*  90:    */   {
/*  91:134 */     return this.field_3_width;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void setWidth(int width)
/*  95:    */   {
/*  96:141 */     this.field_3_width = width;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public int getHeight()
/* 100:    */   {
/* 101:148 */     return this.field_4_height;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setHeight(int height)
/* 105:    */   {
/* 106:155 */     this.field_4_height = height;
/* 107:    */   }
/* 108:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.ChartRecord
 * JD-Core Version:    0.7.0.1
 */