/*   1:    */ package org.apache.poi.hssf.record.cf;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.poi.ss.formula.Formula;
/*   5:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   6:    */ import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType;
/*   7:    */ import org.apache.poi.util.LittleEndianInput;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public abstract class Threshold
/*  11:    */ {
/*  12:    */   private byte type;
/*  13:    */   private Formula formula;
/*  14:    */   private Double value;
/*  15:    */   
/*  16:    */   protected Threshold()
/*  17:    */   {
/*  18: 37 */     this.type = ((byte)ConditionalFormattingThreshold.RangeType.NUMBER.id);
/*  19: 38 */     this.formula = Formula.create(null);
/*  20: 39 */     this.value = Double.valueOf(0.0D);
/*  21:    */   }
/*  22:    */   
/*  23:    */   protected Threshold(LittleEndianInput in)
/*  24:    */   {
/*  25: 44 */     this.type = in.readByte();
/*  26: 45 */     short formulaLen = in.readShort();
/*  27: 46 */     if (formulaLen > 0) {
/*  28: 47 */       this.formula = Formula.read(formulaLen, in);
/*  29:    */     } else {
/*  30: 49 */       this.formula = Formula.create(null);
/*  31:    */     }
/*  32: 52 */     if ((formulaLen == 0) && (this.type != ConditionalFormattingThreshold.RangeType.MIN.id) && (this.type != ConditionalFormattingThreshold.RangeType.MAX.id)) {
/*  33: 54 */       this.value = Double.valueOf(in.readDouble());
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   public byte getType()
/*  38:    */   {
/*  39: 59 */     return this.type;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setType(byte type)
/*  43:    */   {
/*  44: 62 */     this.type = type;
/*  45: 65 */     if ((type == ConditionalFormattingThreshold.RangeType.MIN.id) || (type == ConditionalFormattingThreshold.RangeType.MAX.id) || (type == ConditionalFormattingThreshold.RangeType.FORMULA.id)) {
/*  46: 67 */       this.value = null;
/*  47: 68 */     } else if (this.value == null) {
/*  48: 69 */       this.value = Double.valueOf(0.0D);
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setType(int type)
/*  53:    */   {
/*  54: 73 */     this.type = ((byte)type);
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected Formula getFormula()
/*  58:    */   {
/*  59: 77 */     return this.formula;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public Ptg[] getParsedExpression()
/*  63:    */   {
/*  64: 80 */     return this.formula.getTokens();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setParsedExpression(Ptg[] ptgs)
/*  68:    */   {
/*  69: 83 */     this.formula = Formula.create(ptgs);
/*  70: 84 */     if (ptgs.length > 0) {
/*  71: 85 */       this.value = null;
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   public Double getValue()
/*  76:    */   {
/*  77: 90 */     return this.value;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setValue(Double value)
/*  81:    */   {
/*  82: 93 */     this.value = value;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public int getDataLength()
/*  86:    */   {
/*  87: 97 */     int len = 1 + this.formula.getEncodedSize();
/*  88: 98 */     if (this.value != null) {
/*  89: 99 */       len += 8;
/*  90:    */     }
/*  91:101 */     return len;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public String toString()
/*  95:    */   {
/*  96:105 */     StringBuffer buffer = new StringBuffer();
/*  97:106 */     buffer.append("    [CF Threshold]\n");
/*  98:107 */     buffer.append("          .type    = ").append(Integer.toHexString(this.type)).append("\n");
/*  99:108 */     buffer.append("          .formula = ").append(Arrays.toString(this.formula.getTokens())).append("\n");
/* 100:109 */     buffer.append("          .value   = ").append(this.value).append("\n");
/* 101:110 */     buffer.append("    [/CF Threshold]\n");
/* 102:111 */     return buffer.toString();
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void copyTo(Threshold rec)
/* 106:    */   {
/* 107:115 */     rec.type = this.type;
/* 108:116 */     rec.formula = this.formula;
/* 109:117 */     rec.value = this.value;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void serialize(LittleEndianOutput out)
/* 113:    */   {
/* 114:121 */     out.writeByte(this.type);
/* 115:122 */     if (this.formula.getTokens().length == 0) {
/* 116:123 */       out.writeShort(0);
/* 117:    */     } else {
/* 118:125 */       this.formula.serialize(out);
/* 119:    */     }
/* 120:127 */     if (this.value != null) {
/* 121:128 */       out.writeDouble(this.value.doubleValue());
/* 122:    */     }
/* 123:    */   }
/* 124:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.cf.Threshold
 * JD-Core Version:    0.7.0.1
 */