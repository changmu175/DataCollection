/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.BitField;
/*  4:   */ import org.apache.poi.util.BitFieldFactory;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class UseSelFSRecord
/*  9:   */   extends StandardRecord
/* 10:   */ {
/* 11:   */   public static final short sid = 352;
/* 12:34 */   private static final BitField useNaturalLanguageFormulasFlag = BitFieldFactory.getInstance(1);
/* 13:   */   private int _options;
/* 14:   */   
/* 15:   */   private UseSelFSRecord(int options)
/* 16:   */   {
/* 17:39 */     this._options = options;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public UseSelFSRecord(RecordInputStream in)
/* 21:   */   {
/* 22:43 */     this(in.readUShort());
/* 23:   */   }
/* 24:   */   
/* 25:   */   public UseSelFSRecord(boolean b)
/* 26:   */   {
/* 27:47 */     this(0);
/* 28:48 */     this._options = useNaturalLanguageFormulasFlag.setBoolean(this._options, b);
/* 29:   */   }
/* 30:   */   
/* 31:   */   public String toString()
/* 32:   */   {
/* 33:52 */     StringBuffer buffer = new StringBuffer();
/* 34:   */     
/* 35:54 */     buffer.append("[USESELFS]\n");
/* 36:55 */     buffer.append("    .options = ").append(HexDump.shortToHex(this._options)).append("\n");
/* 37:56 */     buffer.append("[/USESELFS]\n");
/* 38:57 */     return buffer.toString();
/* 39:   */   }
/* 40:   */   
/* 41:   */   public void serialize(LittleEndianOutput out)
/* 42:   */   {
/* 43:61 */     out.writeShort(this._options);
/* 44:   */   }
/* 45:   */   
/* 46:   */   protected int getDataSize()
/* 47:   */   {
/* 48:65 */     return 2;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public short getSid()
/* 52:   */   {
/* 53:69 */     return 352;
/* 54:   */   }
/* 55:   */   
/* 56:   */   public Object clone()
/* 57:   */   {
/* 58:74 */     return new UseSelFSRecord(this._options);
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.UseSelFSRecord
 * JD-Core Version:    0.7.0.1
 */