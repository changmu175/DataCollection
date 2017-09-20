/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.util.CellRangeAddress8Bit;
/*   4:    */ import org.apache.poi.ss.formula.Formula;
/*   5:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   6:    */ import org.apache.poi.util.HexDump;
/*   7:    */ import org.apache.poi.util.LittleEndianOutput;
/*   8:    */ 
/*   9:    */ public final class ArrayRecord
/*  10:    */   extends SharedValueRecordBase
/*  11:    */   implements Cloneable
/*  12:    */ {
/*  13:    */   public static final short sid = 545;
/*  14:    */   private static final int OPT_ALWAYS_RECALCULATE = 1;
/*  15:    */   private static final int OPT_CALCULATE_ON_OPEN = 2;
/*  16:    */   private int _options;
/*  17:    */   private int _field3notUsed;
/*  18:    */   private Formula _formula;
/*  19:    */   
/*  20:    */   public ArrayRecord(RecordInputStream in)
/*  21:    */   {
/*  22: 42 */     super(in);
/*  23: 43 */     this._options = in.readUShort();
/*  24: 44 */     this._field3notUsed = in.readInt();
/*  25: 45 */     int formulaTokenLen = in.readUShort();
/*  26: 46 */     int totalFormulaLen = in.available();
/*  27: 47 */     this._formula = Formula.read(formulaTokenLen, in, totalFormulaLen);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public ArrayRecord(Formula formula, CellRangeAddress8Bit range)
/*  31:    */   {
/*  32: 51 */     super(range);
/*  33: 52 */     this._options = 0;
/*  34: 53 */     this._field3notUsed = 0;
/*  35: 54 */     this._formula = formula;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public boolean isAlwaysRecalculate()
/*  39:    */   {
/*  40: 58 */     return (this._options & 0x1) != 0;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public boolean isCalculateOnOpen()
/*  44:    */   {
/*  45: 61 */     return (this._options & 0x2) != 0;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public Ptg[] getFormulaTokens()
/*  49:    */   {
/*  50: 65 */     return this._formula.getTokens();
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected int getExtraDataSize()
/*  54:    */   {
/*  55: 69 */     return 6 + this._formula.getEncodedSize();
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected void serializeExtraData(LittleEndianOutput out)
/*  59:    */   {
/*  60: 72 */     out.writeShort(this._options);
/*  61: 73 */     out.writeInt(this._field3notUsed);
/*  62: 74 */     this._formula.serialize(out);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public short getSid()
/*  66:    */   {
/*  67: 78 */     return 545;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public String toString()
/*  71:    */   {
/*  72: 82 */     StringBuffer sb = new StringBuffer();
/*  73: 83 */     sb.append(getClass().getName()).append(" [ARRAY]\n");
/*  74: 84 */     sb.append(" range=").append(getRange()).append("\n");
/*  75: 85 */     sb.append(" options=").append(HexDump.shortToHex(this._options)).append("\n");
/*  76: 86 */     sb.append(" notUsed=").append(HexDump.intToHex(this._field3notUsed)).append("\n");
/*  77: 87 */     sb.append(" formula:").append("\n");
/*  78: 88 */     Ptg[] ptgs = this._formula.getTokens();
/*  79: 89 */     for (int i = 0; i < ptgs.length; i++)
/*  80:    */     {
/*  81: 90 */       Ptg ptg = ptgs[i];
/*  82: 91 */       sb.append(ptg).append(ptg.getRVAType()).append("\n");
/*  83:    */     }
/*  84: 93 */     sb.append("]");
/*  85: 94 */     return sb.toString();
/*  86:    */   }
/*  87:    */   
/*  88:    */   public ArrayRecord clone()
/*  89:    */   {
/*  90: 99 */     ArrayRecord rec = new ArrayRecord(this._formula.copy(), getRange());
/*  91:    */     
/*  92:    */ 
/*  93:102 */     rec._options = this._options;
/*  94:103 */     rec._field3notUsed = this._field3notUsed;
/*  95:    */     
/*  96:105 */     return rec;
/*  97:    */   }
/*  98:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.ArrayRecord
 * JD-Core Version:    0.7.0.1
 */