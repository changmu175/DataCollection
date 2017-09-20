/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.poi.hssf.usermodel.HSSFSheet;
/*   5:    */ import org.apache.poi.ss.formula.Formula;
/*   6:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   7:    */ import org.apache.poi.util.BitField;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class CFRuleRecord
/*  11:    */   extends CFRuleBase
/*  12:    */   implements Cloneable
/*  13:    */ {
/*  14:    */   public static final short sid = 433;
/*  15:    */   
/*  16:    */   private CFRuleRecord(byte conditionType, byte comparisonOperation)
/*  17:    */   {
/*  18: 39 */     super(conditionType, comparisonOperation);
/*  19: 40 */     setDefaults();
/*  20:    */   }
/*  21:    */   
/*  22:    */   private CFRuleRecord(byte conditionType, byte comparisonOperation, Ptg[] formula1, Ptg[] formula2)
/*  23:    */   {
/*  24: 44 */     super(conditionType, comparisonOperation, formula1, formula2);
/*  25: 45 */     setDefaults();
/*  26:    */   }
/*  27:    */   
/*  28:    */   private void setDefaults()
/*  29:    */   {
/*  30: 49 */     this.formatting_options = modificationBits.setValue(this.formatting_options, -1);
/*  31:    */     
/*  32: 51 */     this.formatting_options = fmtBlockBits.setValue(this.formatting_options, 0);
/*  33: 52 */     this.formatting_options = undocumented.clear(this.formatting_options);
/*  34:    */     
/*  35: 54 */     this.formatting_not_used = -32766;
/*  36: 55 */     this._fontFormatting = null;
/*  37: 56 */     this._borderFormatting = null;
/*  38: 57 */     this._patternFormatting = null;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public static CFRuleRecord create(HSSFSheet sheet, String formulaText)
/*  42:    */   {
/*  43: 69 */     Ptg[] formula1 = parseFormula(formulaText, sheet);
/*  44: 70 */     return new CFRuleRecord((byte)2, (byte)0, formula1, null);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static CFRuleRecord create(HSSFSheet sheet, byte comparisonOperation, String formulaText1, String formulaText2)
/*  48:    */   {
/*  49: 85 */     Ptg[] formula1 = parseFormula(formulaText1, sheet);
/*  50: 86 */     Ptg[] formula2 = parseFormula(formulaText2, sheet);
/*  51: 87 */     return new CFRuleRecord((byte)1, comparisonOperation, formula1, formula2);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public CFRuleRecord(RecordInputStream in)
/*  55:    */   {
/*  56: 91 */     setConditionType(in.readByte());
/*  57: 92 */     setComparisonOperation(in.readByte());
/*  58: 93 */     int field_3_formula1_len = in.readUShort();
/*  59: 94 */     int field_4_formula2_len = in.readUShort();
/*  60: 95 */     readFormatOptions(in);
/*  61:    */     
/*  62:    */ 
/*  63: 98 */     setFormula1(Formula.read(field_3_formula1_len, in));
/*  64: 99 */     setFormula2(Formula.read(field_4_formula2_len, in));
/*  65:    */   }
/*  66:    */   
/*  67:    */   public short getSid()
/*  68:    */   {
/*  69:104 */     return 433;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void serialize(LittleEndianOutput out)
/*  73:    */   {
/*  74:116 */     int formula1Len = getFormulaSize(getFormula1());
/*  75:117 */     int formula2Len = getFormulaSize(getFormula2());
/*  76:    */     
/*  77:119 */     out.writeByte(getConditionType());
/*  78:120 */     out.writeByte(getComparisonOperation());
/*  79:121 */     out.writeShort(formula1Len);
/*  80:122 */     out.writeShort(formula2Len);
/*  81:    */     
/*  82:124 */     serializeFormattingBlock(out);
/*  83:    */     
/*  84:126 */     getFormula1().serializeTokens(out);
/*  85:127 */     getFormula2().serializeTokens(out);
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected int getDataSize()
/*  89:    */   {
/*  90:132 */     return 6 + getFormattingBlockSize() + getFormulaSize(getFormula1()) + getFormulaSize(getFormula2());
/*  91:    */   }
/*  92:    */   
/*  93:    */   public String toString()
/*  94:    */   {
/*  95:139 */     StringBuffer buffer = new StringBuffer();
/*  96:140 */     buffer.append("[CFRULE]\n");
/*  97:141 */     buffer.append("    .condition_type   =").append(getConditionType()).append("\n");
/*  98:142 */     buffer.append("    OPTION FLAGS=0x").append(Integer.toHexString(getOptions())).append("\n");
/*  99:143 */     if (containsFontFormattingBlock()) {
/* 100:144 */       buffer.append(this._fontFormatting).append("\n");
/* 101:    */     }
/* 102:146 */     if (containsBorderFormattingBlock()) {
/* 103:147 */       buffer.append(this._borderFormatting).append("\n");
/* 104:    */     }
/* 105:149 */     if (containsPatternFormattingBlock()) {
/* 106:150 */       buffer.append(this._patternFormatting).append("\n");
/* 107:    */     }
/* 108:152 */     buffer.append("    Formula 1 =").append(Arrays.toString(getFormula1().getTokens())).append("\n");
/* 109:153 */     buffer.append("    Formula 2 =").append(Arrays.toString(getFormula2().getTokens())).append("\n");
/* 110:154 */     buffer.append("[/CFRULE]\n");
/* 111:155 */     return buffer.toString();
/* 112:    */   }
/* 113:    */   
/* 114:    */   public CFRuleRecord clone()
/* 115:    */   {
/* 116:160 */     CFRuleRecord rec = new CFRuleRecord(getConditionType(), getComparisonOperation());
/* 117:161 */     super.copyTo(rec);
/* 118:162 */     return rec;
/* 119:    */   }
/* 120:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.CFRuleRecord
 * JD-Core Version:    0.7.0.1
 */