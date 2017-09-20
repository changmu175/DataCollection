/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ 
/*   7:    */ public class ChartTitleFormatRecord
/*   8:    */   extends StandardRecord
/*   9:    */ {
/*  10:    */   public static final short sid = 4176;
/*  11:    */   private CTFormat[] _formats;
/*  12:    */   
/*  13:    */   private static final class CTFormat
/*  14:    */   {
/*  15:    */     public static final int ENCODED_SIZE = 4;
/*  16:    */     private int _offset;
/*  17:    */     private int _fontIndex;
/*  18:    */     
/*  19:    */     public CTFormat(RecordInputStream in)
/*  20:    */     {
/*  21: 42 */       this._offset = in.readShort();
/*  22: 43 */       this._fontIndex = in.readShort();
/*  23:    */     }
/*  24:    */     
/*  25:    */     public int getOffset()
/*  26:    */     {
/*  27: 47 */       return this._offset;
/*  28:    */     }
/*  29:    */     
/*  30:    */     public void setOffset(int newOff)
/*  31:    */     {
/*  32: 50 */       this._offset = newOff;
/*  33:    */     }
/*  34:    */     
/*  35:    */     public int getFontIndex()
/*  36:    */     {
/*  37: 53 */       return this._fontIndex;
/*  38:    */     }
/*  39:    */     
/*  40:    */     public void serialize(LittleEndianOutput out)
/*  41:    */     {
/*  42: 57 */       out.writeShort(this._offset);
/*  43: 58 */       out.writeShort(this._fontIndex);
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   public ChartTitleFormatRecord(RecordInputStream in)
/*  48:    */   {
/*  49: 64 */     int nRecs = in.readUShort();
/*  50: 65 */     this._formats = new CTFormat[nRecs];
/*  51: 67 */     for (int i = 0; i < nRecs; i++) {
/*  52: 68 */       this._formats[i] = new CTFormat(in);
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void serialize(LittleEndianOutput out)
/*  57:    */   {
/*  58: 73 */     out.writeShort(this._formats.length);
/*  59: 74 */     for (int i = 0; i < this._formats.length; i++) {
/*  60: 75 */       this._formats[i].serialize(out);
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected int getDataSize()
/*  65:    */   {
/*  66: 80 */     return 2 + 4 * this._formats.length;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public short getSid()
/*  70:    */   {
/*  71: 84 */     return 4176;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public int getFormatCount()
/*  75:    */   {
/*  76: 88 */     return this._formats.length;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void modifyFormatRun(short oldPos, short newLen)
/*  80:    */   {
/*  81: 92 */     int shift = 0;
/*  82: 93 */     for (int i = 0; i < this._formats.length; i++)
/*  83:    */     {
/*  84: 94 */       CTFormat ctf = this._formats[i];
/*  85: 95 */       if (shift != 0)
/*  86:    */       {
/*  87: 96 */         ctf.setOffset(ctf.getOffset() + shift);
/*  88:    */       }
/*  89: 97 */       else if ((oldPos == ctf.getOffset()) && (i < this._formats.length - 1))
/*  90:    */       {
/*  91: 98 */         CTFormat nextCTF = this._formats[(i + 1)];
/*  92: 99 */         shift = newLen - (nextCTF.getOffset() - ctf.getOffset());
/*  93:    */       }
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   public String toString()
/*  98:    */   {
/*  99:105 */     StringBuffer buffer = new StringBuffer();
/* 100:    */     
/* 101:107 */     buffer.append("[CHARTTITLEFORMAT]\n");
/* 102:108 */     buffer.append("    .format_runs       = ").append(this._formats.length).append("\n");
/* 103:109 */     for (int i = 0; i < this._formats.length; i++)
/* 104:    */     {
/* 105:110 */       CTFormat ctf = this._formats[i];
/* 106:111 */       buffer.append("       .char_offset= ").append(ctf.getOffset());
/* 107:112 */       buffer.append(",.fontidx= ").append(ctf.getFontIndex());
/* 108:113 */       buffer.append("\n");
/* 109:    */     }
/* 110:115 */     buffer.append("[/CHARTTITLEFORMAT]\n");
/* 111:116 */     return buffer.toString();
/* 112:    */   }
/* 113:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.ChartTitleFormatRecord
 * JD-Core Version:    0.7.0.1
 */