/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ 
/*   8:    */ public final class ObjectLinkRecord
/*   9:    */   extends StandardRecord
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12:    */   public static final short sid = 4135;
/*  13:    */   private short field_1_anchorId;
/*  14:    */   public static final short ANCHOR_ID_CHART_TITLE = 1;
/*  15:    */   public static final short ANCHOR_ID_Y_AXIS = 2;
/*  16:    */   public static final short ANCHOR_ID_X_AXIS = 3;
/*  17:    */   public static final short ANCHOR_ID_SERIES_OR_POINT = 4;
/*  18:    */   public static final short ANCHOR_ID_Z_AXIS = 7;
/*  19:    */   private short field_2_link1;
/*  20:    */   private short field_3_link2;
/*  21:    */   
/*  22:    */   public ObjectLinkRecord() {}
/*  23:    */   
/*  24:    */   public ObjectLinkRecord(RecordInputStream in)
/*  25:    */   {
/*  26: 47 */     this.field_1_anchorId = in.readShort();
/*  27: 48 */     this.field_2_link1 = in.readShort();
/*  28: 49 */     this.field_3_link2 = in.readShort();
/*  29:    */   }
/*  30:    */   
/*  31:    */   public String toString()
/*  32:    */   {
/*  33: 55 */     StringBuffer buffer = new StringBuffer();
/*  34:    */     
/*  35: 57 */     buffer.append("[OBJECTLINK]\n");
/*  36: 58 */     buffer.append("    .anchorId             = ").append("0x").append(HexDump.toHex(getAnchorId())).append(" (").append(getAnchorId()).append(" )");
/*  37:    */     
/*  38:    */ 
/*  39: 61 */     buffer.append(System.getProperty("line.separator"));
/*  40: 62 */     buffer.append("    .link1                = ").append("0x").append(HexDump.toHex(getLink1())).append(" (").append(getLink1()).append(" )");
/*  41:    */     
/*  42:    */ 
/*  43: 65 */     buffer.append(System.getProperty("line.separator"));
/*  44: 66 */     buffer.append("    .link2                = ").append("0x").append(HexDump.toHex(getLink2())).append(" (").append(getLink2()).append(" )");
/*  45:    */     
/*  46:    */ 
/*  47: 69 */     buffer.append(System.getProperty("line.separator"));
/*  48:    */     
/*  49: 71 */     buffer.append("[/OBJECTLINK]\n");
/*  50: 72 */     return buffer.toString();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void serialize(LittleEndianOutput out)
/*  54:    */   {
/*  55: 76 */     out.writeShort(this.field_1_anchorId);
/*  56: 77 */     out.writeShort(this.field_2_link1);
/*  57: 78 */     out.writeShort(this.field_3_link2);
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected int getDataSize()
/*  61:    */   {
/*  62: 82 */     return 6;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public short getSid()
/*  66:    */   {
/*  67: 87 */     return 4135;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public ObjectLinkRecord clone()
/*  71:    */   {
/*  72: 92 */     ObjectLinkRecord rec = new ObjectLinkRecord();
/*  73:    */     
/*  74: 94 */     rec.field_1_anchorId = this.field_1_anchorId;
/*  75: 95 */     rec.field_2_link1 = this.field_2_link1;
/*  76: 96 */     rec.field_3_link2 = this.field_3_link2;
/*  77: 97 */     return rec;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public short getAnchorId()
/*  81:    */   {
/*  82:115 */     return this.field_1_anchorId;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setAnchorId(short field_1_anchorId)
/*  86:    */   {
/*  87:131 */     this.field_1_anchorId = field_1_anchorId;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public short getLink1()
/*  91:    */   {
/*  92:139 */     return this.field_2_link1;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setLink1(short field_2_link1)
/*  96:    */   {
/*  97:147 */     this.field_2_link1 = field_2_link1;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public short getLink2()
/* 101:    */   {
/* 102:155 */     return this.field_3_link2;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setLink2(short field_3_link2)
/* 106:    */   {
/* 107:163 */     this.field_3_link2 = field_3_link2;
/* 108:    */   }
/* 109:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.ObjectLinkRecord
 * JD-Core Version:    0.7.0.1
 */