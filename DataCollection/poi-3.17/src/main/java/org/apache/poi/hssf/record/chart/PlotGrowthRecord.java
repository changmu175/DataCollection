/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ 
/*   8:    */ public final class PlotGrowthRecord
/*   9:    */   extends StandardRecord
/*  10:    */ {
/*  11:    */   public static final short sid = 4196;
/*  12:    */   private int field_1_horizontalScale;
/*  13:    */   private int field_2_verticalScale;
/*  14:    */   
/*  15:    */   public PlotGrowthRecord() {}
/*  16:    */   
/*  17:    */   public PlotGrowthRecord(RecordInputStream in)
/*  18:    */   {
/*  19: 41 */     this.field_1_horizontalScale = in.readInt();
/*  20: 42 */     this.field_2_verticalScale = in.readInt();
/*  21:    */   }
/*  22:    */   
/*  23:    */   public String toString()
/*  24:    */   {
/*  25: 48 */     StringBuffer buffer = new StringBuffer();
/*  26:    */     
/*  27: 50 */     buffer.append("[PLOTGROWTH]\n");
/*  28: 51 */     buffer.append("    .horizontalScale      = ").append("0x").append(HexDump.toHex(getHorizontalScale())).append(" (").append(getHorizontalScale()).append(" )");
/*  29:    */     
/*  30:    */ 
/*  31: 54 */     buffer.append(System.getProperty("line.separator"));
/*  32: 55 */     buffer.append("    .verticalScale        = ").append("0x").append(HexDump.toHex(getVerticalScale())).append(" (").append(getVerticalScale()).append(" )");
/*  33:    */     
/*  34:    */ 
/*  35: 58 */     buffer.append(System.getProperty("line.separator"));
/*  36:    */     
/*  37: 60 */     buffer.append("[/PLOTGROWTH]\n");
/*  38: 61 */     return buffer.toString();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void serialize(LittleEndianOutput out)
/*  42:    */   {
/*  43: 65 */     out.writeInt(this.field_1_horizontalScale);
/*  44: 66 */     out.writeInt(this.field_2_verticalScale);
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected int getDataSize()
/*  48:    */   {
/*  49: 70 */     return 8;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public short getSid()
/*  53:    */   {
/*  54: 75 */     return 4196;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public Object clone()
/*  58:    */   {
/*  59: 79 */     PlotGrowthRecord rec = new PlotGrowthRecord();
/*  60:    */     
/*  61: 81 */     rec.field_1_horizontalScale = this.field_1_horizontalScale;
/*  62: 82 */     rec.field_2_verticalScale = this.field_2_verticalScale;
/*  63: 83 */     return rec;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public int getHorizontalScale()
/*  67:    */   {
/*  68: 94 */     return this.field_1_horizontalScale;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setHorizontalScale(int field_1_horizontalScale)
/*  72:    */   {
/*  73:102 */     this.field_1_horizontalScale = field_1_horizontalScale;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public int getVerticalScale()
/*  77:    */   {
/*  78:110 */     return this.field_2_verticalScale;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setVerticalScale(int field_2_verticalScale)
/*  82:    */   {
/*  83:118 */     this.field_2_verticalScale = field_2_verticalScale;
/*  84:    */   }
/*  85:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.PlotGrowthRecord
 * JD-Core Version:    0.7.0.1
 */