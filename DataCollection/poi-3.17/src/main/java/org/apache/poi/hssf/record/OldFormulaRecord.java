/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.Formula;
/*   4:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   5:    */ import org.apache.poi.ss.usermodel.CellType;
/*   6:    */ 
/*   7:    */ public final class OldFormulaRecord
/*   8:    */   extends OldCellRecord
/*   9:    */ {
/*  10:    */   public static final short biff2_sid = 6;
/*  11:    */   public static final short biff3_sid = 518;
/*  12:    */   public static final short biff4_sid = 1030;
/*  13:    */   public static final short biff5_sid = 6;
/*  14:    */   private FormulaRecord.SpecialCachedValue specialCachedValue;
/*  15:    */   private double field_4_value;
/*  16:    */   private short field_5_options;
/*  17:    */   private Formula field_6_parsed_expr;
/*  18:    */   
/*  19:    */   public OldFormulaRecord(RecordInputStream ris)
/*  20:    */   {
/*  21: 41 */     super(ris, ris.getSid() == 6);
/*  22: 43 */     if (isBiff2())
/*  23:    */     {
/*  24: 44 */       this.field_4_value = ris.readDouble();
/*  25:    */     }
/*  26:    */     else
/*  27:    */     {
/*  28: 46 */       long valueLongBits = ris.readLong();
/*  29: 47 */       this.specialCachedValue = FormulaRecord.SpecialCachedValue.create(valueLongBits);
/*  30: 48 */       if (this.specialCachedValue == null) {
/*  31: 49 */         this.field_4_value = Double.longBitsToDouble(valueLongBits);
/*  32:    */       }
/*  33:    */     }
/*  34: 53 */     if (isBiff2()) {
/*  35: 54 */       this.field_5_options = ((short)ris.readUByte());
/*  36:    */     } else {
/*  37: 56 */       this.field_5_options = ris.readShort();
/*  38:    */     }
/*  39: 59 */     int expression_len = ris.readShort();
/*  40: 60 */     int nBytesAvailable = ris.available();
/*  41: 61 */     this.field_6_parsed_expr = Formula.read(expression_len, ris, nBytesAvailable);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public int getCachedResultType()
/*  45:    */   {
/*  46: 65 */     if (this.specialCachedValue == null) {
/*  47: 66 */       return CellType.NUMERIC.getCode();
/*  48:    */     }
/*  49: 68 */     return this.specialCachedValue.getValueType();
/*  50:    */   }
/*  51:    */   
/*  52:    */   public boolean getCachedBooleanValue()
/*  53:    */   {
/*  54: 72 */     return this.specialCachedValue.getBooleanValue();
/*  55:    */   }
/*  56:    */   
/*  57:    */   public int getCachedErrorValue()
/*  58:    */   {
/*  59: 75 */     return this.specialCachedValue.getErrorValue();
/*  60:    */   }
/*  61:    */   
/*  62:    */   public double getValue()
/*  63:    */   {
/*  64: 84 */     return this.field_4_value;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public short getOptions()
/*  68:    */   {
/*  69: 93 */     return this.field_5_options;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public Ptg[] getParsedExpression()
/*  73:    */   {
/*  74:100 */     return this.field_6_parsed_expr.getTokens();
/*  75:    */   }
/*  76:    */   
/*  77:    */   public Formula getFormula()
/*  78:    */   {
/*  79:104 */     return this.field_6_parsed_expr;
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected void appendValueText(StringBuilder sb)
/*  83:    */   {
/*  84:108 */     sb.append("    .value       = ").append(getValue()).append("\n");
/*  85:    */   }
/*  86:    */   
/*  87:    */   protected String getRecordName()
/*  88:    */   {
/*  89:111 */     return "Old Formula";
/*  90:    */   }
/*  91:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.OldFormulaRecord
 * JD-Core Version:    0.7.0.1
 */