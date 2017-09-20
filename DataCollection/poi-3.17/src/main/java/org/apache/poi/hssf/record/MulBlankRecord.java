/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ 
/*   5:    */ public final class MulBlankRecord
/*   6:    */   extends StandardRecord
/*   7:    */ {
/*   8:    */   public static final short sid = 190;
/*   9:    */   private final int _row;
/*  10:    */   private final int _firstCol;
/*  11:    */   private final short[] _xfs;
/*  12:    */   private final int _lastCol;
/*  13:    */   
/*  14:    */   public MulBlankRecord(int row, int firstCol, short[] xfs)
/*  15:    */   {
/*  16: 39 */     this._row = row;
/*  17: 40 */     this._firstCol = firstCol;
/*  18: 41 */     this._xfs = xfs;
/*  19: 42 */     this._lastCol = (firstCol + xfs.length - 1);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public int getRow()
/*  23:    */   {
/*  24: 49 */     return this._row;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public int getFirstColumn()
/*  28:    */   {
/*  29: 56 */     return this._firstCol;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int getLastColumn()
/*  33:    */   {
/*  34: 63 */     return this._lastCol;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public int getNumColumns()
/*  38:    */   {
/*  39: 71 */     return this._lastCol - this._firstCol + 1;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public short getXFAt(int coffset)
/*  43:    */   {
/*  44: 80 */     return this._xfs[coffset];
/*  45:    */   }
/*  46:    */   
/*  47:    */   public MulBlankRecord(RecordInputStream in)
/*  48:    */   {
/*  49: 87 */     this._row = in.readUShort();
/*  50: 88 */     this._firstCol = in.readShort();
/*  51: 89 */     this._xfs = parseXFs(in);
/*  52: 90 */     this._lastCol = in.readShort();
/*  53:    */   }
/*  54:    */   
/*  55:    */   private static short[] parseXFs(RecordInputStream in)
/*  56:    */   {
/*  57: 94 */     short[] retval = new short[(in.remaining() - 2) / 2];
/*  58: 96 */     for (int idx = 0; idx < retval.length; idx++) {
/*  59: 97 */       retval[idx] = in.readShort();
/*  60:    */     }
/*  61: 99 */     return retval;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String toString()
/*  65:    */   {
/*  66:103 */     StringBuffer buffer = new StringBuffer();
/*  67:    */     
/*  68:105 */     buffer.append("[MULBLANK]\n");
/*  69:106 */     buffer.append("row  = ").append(Integer.toHexString(getRow())).append("\n");
/*  70:107 */     buffer.append("firstcol  = ").append(Integer.toHexString(getFirstColumn())).append("\n");
/*  71:108 */     buffer.append(" lastcol  = ").append(Integer.toHexString(this._lastCol)).append("\n");
/*  72:109 */     for (int k = 0; k < getNumColumns(); k++) {
/*  73:110 */       buffer.append("xf").append(k).append("\t\t= ").append(Integer.toHexString(getXFAt(k))).append("\n");
/*  74:    */     }
/*  75:113 */     buffer.append("[/MULBLANK]\n");
/*  76:114 */     return buffer.toString();
/*  77:    */   }
/*  78:    */   
/*  79:    */   public short getSid()
/*  80:    */   {
/*  81:118 */     return 190;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void serialize(LittleEndianOutput out)
/*  85:    */   {
/*  86:122 */     out.writeShort(this._row);
/*  87:123 */     out.writeShort(this._firstCol);
/*  88:124 */     int nItems = this._xfs.length;
/*  89:125 */     for (int i = 0; i < nItems; i++) {
/*  90:126 */       out.writeShort(this._xfs[i]);
/*  91:    */     }
/*  92:128 */     out.writeShort(this._lastCol);
/*  93:    */   }
/*  94:    */   
/*  95:    */   protected int getDataSize()
/*  96:    */   {
/*  97:133 */     return 6 + this._xfs.length * 2;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public MulBlankRecord clone()
/* 101:    */   {
/* 102:139 */     return this;
/* 103:    */   }
/* 104:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.MulBlankRecord
 * JD-Core Version:    0.7.0.1
 */