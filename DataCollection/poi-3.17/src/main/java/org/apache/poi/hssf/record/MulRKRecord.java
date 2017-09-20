/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.util.RKUtil;
/*   4:    */ import org.apache.poi.util.HexDump;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ import org.apache.poi.util.RecordFormatException;
/*   7:    */ 
/*   8:    */ public final class MulRKRecord
/*   9:    */   extends StandardRecord
/*  10:    */ {
/*  11:    */   public static final short sid = 189;
/*  12:    */   private final int field_1_row;
/*  13:    */   private final short field_2_first_col;
/*  14:    */   private final RkRec[] field_3_rks;
/*  15:    */   private final short field_4_last_col;
/*  16:    */   
/*  17:    */   public int getRow()
/*  18:    */   {
/*  19: 43 */     return this.field_1_row;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public short getFirstColumn()
/*  23:    */   {
/*  24: 51 */     return this.field_2_first_col;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public short getLastColumn()
/*  28:    */   {
/*  29: 59 */     return this.field_4_last_col;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int getNumColumns()
/*  33:    */   {
/*  34: 67 */     return this.field_4_last_col - this.field_2_first_col + 1;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public short getXFAt(int coffset)
/*  38:    */   {
/*  39: 78 */     return this.field_3_rks[coffset].xf;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public double getRKNumberAt(int coffset)
/*  43:    */   {
/*  44: 89 */     return RKUtil.decodeNumber(this.field_3_rks[coffset].rk);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public MulRKRecord(RecordInputStream in)
/*  48:    */   {
/*  49: 96 */     this.field_1_row = in.readUShort();
/*  50: 97 */     this.field_2_first_col = in.readShort();
/*  51: 98 */     this.field_3_rks = RkRec.parseRKs(in);
/*  52: 99 */     this.field_4_last_col = in.readShort();
/*  53:    */   }
/*  54:    */   
/*  55:    */   public String toString()
/*  56:    */   {
/*  57:105 */     StringBuffer buffer = new StringBuffer();
/*  58:    */     
/*  59:107 */     buffer.append("[MULRK]\n");
/*  60:108 */     buffer.append("\t.row\t = ").append(HexDump.shortToHex(getRow())).append("\n");
/*  61:109 */     buffer.append("\t.firstcol= ").append(HexDump.shortToHex(getFirstColumn())).append("\n");
/*  62:110 */     buffer.append("\t.lastcol = ").append(HexDump.shortToHex(getLastColumn())).append("\n");
/*  63:112 */     for (int k = 0; k < getNumColumns(); k++)
/*  64:    */     {
/*  65:113 */       buffer.append("\txf[").append(k).append("] = ").append(HexDump.shortToHex(getXFAt(k))).append("\n");
/*  66:114 */       buffer.append("\trk[").append(k).append("] = ").append(getRKNumberAt(k)).append("\n");
/*  67:    */     }
/*  68:116 */     buffer.append("[/MULRK]\n");
/*  69:117 */     return buffer.toString();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public short getSid()
/*  73:    */   {
/*  74:123 */     return 189;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void serialize(LittleEndianOutput out)
/*  78:    */   {
/*  79:128 */     throw new RecordFormatException("Sorry, you can't serialize MulRK in this release");
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected int getDataSize()
/*  83:    */   {
/*  84:132 */     throw new RecordFormatException("Sorry, you can't serialize MulRK in this release");
/*  85:    */   }
/*  86:    */   
/*  87:    */   private static final class RkRec
/*  88:    */   {
/*  89:    */     public static final int ENCODED_SIZE = 6;
/*  90:    */     public final short xf;
/*  91:    */     public final int rk;
/*  92:    */     
/*  93:    */     private RkRec(RecordInputStream in)
/*  94:    */     {
/*  95:141 */       this.xf = in.readShort();
/*  96:142 */       this.rk = in.readInt();
/*  97:    */     }
/*  98:    */     
/*  99:    */     public static RkRec[] parseRKs(RecordInputStream in)
/* 100:    */     {
/* 101:146 */       int nItems = (in.remaining() - 2) / 6;
/* 102:147 */       RkRec[] retval = new RkRec[nItems];
/* 103:148 */       for (int i = 0; i < nItems; i++) {
/* 104:149 */         retval[i] = new RkRec(in);
/* 105:    */       }
/* 106:151 */       return retval;
/* 107:    */     }
/* 108:    */   }
/* 109:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.MulRKRecord
 * JD-Core Version:    0.7.0.1
 */