/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.util.CellRangeAddress8Bit;
/*   4:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   5:    */ import org.apache.poi.ss.formula.Formula;
/*   6:    */ import org.apache.poi.ss.formula.SharedFormula;
/*   7:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   8:    */ import org.apache.poi.util.HexDump;
/*   9:    */ import org.apache.poi.util.LittleEndianOutput;
/*  10:    */ 
/*  11:    */ public final class SharedFormulaRecord
/*  12:    */   extends SharedValueRecordBase
/*  13:    */ {
/*  14:    */   public static final short sid = 1212;
/*  15:    */   private int field_5_reserved;
/*  16:    */   private Formula field_7_parsed_expr;
/*  17:    */   
/*  18:    */   public SharedFormulaRecord()
/*  19:    */   {
/*  20: 47 */     this(new CellRangeAddress8Bit(0, 0, 0, 0));
/*  21:    */   }
/*  22:    */   
/*  23:    */   private SharedFormulaRecord(CellRangeAddress8Bit range)
/*  24:    */   {
/*  25: 50 */     super(range);
/*  26: 51 */     this.field_7_parsed_expr = Formula.create(Ptg.EMPTY_PTG_ARRAY);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public SharedFormulaRecord(RecordInputStream in)
/*  30:    */   {
/*  31: 58 */     super(in);
/*  32: 59 */     this.field_5_reserved = in.readShort();
/*  33: 60 */     int field_6_expression_len = in.readShort();
/*  34: 61 */     int nAvailableBytes = in.available();
/*  35: 62 */     this.field_7_parsed_expr = Formula.read(field_6_expression_len, in, nAvailableBytes);
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected void serializeExtraData(LittleEndianOutput out)
/*  39:    */   {
/*  40: 66 */     out.writeShort(this.field_5_reserved);
/*  41: 67 */     this.field_7_parsed_expr.serialize(out);
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected int getExtraDataSize()
/*  45:    */   {
/*  46: 71 */     return 2 + this.field_7_parsed_expr.getEncodedSize();
/*  47:    */   }
/*  48:    */   
/*  49:    */   public String toString()
/*  50:    */   {
/*  51: 80 */     StringBuffer buffer = new StringBuffer();
/*  52:    */     
/*  53: 82 */     buffer.append("[SHARED FORMULA (").append(HexDump.intToHex(1212)).append("]\n");
/*  54: 83 */     buffer.append("    .range      = ").append(getRange()).append("\n");
/*  55: 84 */     buffer.append("    .reserved    = ").append(HexDump.shortToHex(this.field_5_reserved)).append("\n");
/*  56:    */     
/*  57: 86 */     Ptg[] ptgs = this.field_7_parsed_expr.getTokens();
/*  58: 87 */     for (int k = 0; k < ptgs.length; k++)
/*  59:    */     {
/*  60: 88 */       buffer.append("Formula[").append(k).append("]");
/*  61: 89 */       Ptg ptg = ptgs[k];
/*  62: 90 */       buffer.append(ptg).append(ptg.getRVAType()).append("\n");
/*  63:    */     }
/*  64: 93 */     buffer.append("[/SHARED FORMULA]\n");
/*  65: 94 */     return buffer.toString();
/*  66:    */   }
/*  67:    */   
/*  68:    */   public short getSid()
/*  69:    */   {
/*  70: 98 */     return 1212;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public Ptg[] getFormulaTokens(FormulaRecord formula)
/*  74:    */   {
/*  75:105 */     int formulaRow = formula.getRow();
/*  76:106 */     int formulaColumn = formula.getColumn();
/*  77:108 */     if (!isInRange(formulaRow, formulaColumn)) {
/*  78:109 */       throw new RuntimeException("Shared Formula Conversion: Coding Error");
/*  79:    */     }
/*  80:112 */     SharedFormula sf = new SharedFormula(SpreadsheetVersion.EXCEL97);
/*  81:113 */     return sf.convertSharedFormulas(this.field_7_parsed_expr.getTokens(), formulaRow, formulaColumn);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public Object clone()
/*  85:    */   {
/*  86:117 */     SharedFormulaRecord result = new SharedFormulaRecord(getRange());
/*  87:118 */     result.field_5_reserved = this.field_5_reserved;
/*  88:119 */     result.field_7_parsed_expr = this.field_7_parsed_expr.copy();
/*  89:120 */     return result;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public boolean isFormulaSame(SharedFormulaRecord other)
/*  93:    */   {
/*  94:123 */     return this.field_7_parsed_expr.isSame(other.field_7_parsed_expr);
/*  95:    */   }
/*  96:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.SharedFormulaRecord
 * JD-Core Version:    0.7.0.1
 */