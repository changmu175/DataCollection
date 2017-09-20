/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.BitField;
/*  4:   */ import org.apache.poi.util.BitFieldFactory;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class DSFRecord
/*  9:   */   extends StandardRecord
/* 10:   */ {
/* 11:   */   public static final short sid = 353;
/* 12:34 */   private static final BitField biff5BookStreamFlag = BitFieldFactory.getInstance(1);
/* 13:   */   private int _options;
/* 14:   */   
/* 15:   */   private DSFRecord(int options)
/* 16:   */   {
/* 17:39 */     this._options = options;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public DSFRecord(boolean isBiff5BookStreamPresent)
/* 21:   */   {
/* 22:42 */     this(0);
/* 23:43 */     this._options = biff5BookStreamFlag.setBoolean(0, isBiff5BookStreamPresent);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public DSFRecord(RecordInputStream in)
/* 27:   */   {
/* 28:47 */     this(in.readShort());
/* 29:   */   }
/* 30:   */   
/* 31:   */   public boolean isBiff5BookStreamPresent()
/* 32:   */   {
/* 33:51 */     return biff5BookStreamFlag.isSet(this._options);
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String toString()
/* 37:   */   {
/* 38:55 */     StringBuffer buffer = new StringBuffer();
/* 39:   */     
/* 40:57 */     buffer.append("[DSF]\n");
/* 41:58 */     buffer.append("    .options = ").append(HexDump.shortToHex(this._options)).append("\n");
/* 42:59 */     buffer.append("[/DSF]\n");
/* 43:60 */     return buffer.toString();
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void serialize(LittleEndianOutput out)
/* 47:   */   {
/* 48:64 */     out.writeShort(this._options);
/* 49:   */   }
/* 50:   */   
/* 51:   */   protected int getDataSize()
/* 52:   */   {
/* 53:68 */     return 2;
/* 54:   */   }
/* 55:   */   
/* 56:   */   public short getSid()
/* 57:   */   {
/* 58:72 */     return 353;
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.DSFRecord
 * JD-Core Version:    0.7.0.1
 */