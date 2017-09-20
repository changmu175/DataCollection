/*   1:    */ package org.apache.poi.ss.formula.ptg;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianInput;
/*   4:    */ import org.apache.poi.util.LittleEndianOutput;
/*   5:    */ import org.apache.poi.util.StringUtil;
/*   6:    */ 
/*   7:    */ public final class StringPtg
/*   8:    */   extends ScalarConstantPtg
/*   9:    */ {
/*  10:    */   public static final byte sid = 23;
/*  11:    */   private static final char FORMULA_DELIMITER = '"';
/*  12:    */   private final boolean _is16bitUnicode;
/*  13:    */   private final String field_3_string;
/*  14:    */   
/*  15:    */   public StringPtg(LittleEndianInput in)
/*  16:    */   {
/*  17: 46 */     int nChars = in.readUByte();
/*  18: 47 */     this._is16bitUnicode = ((in.readByte() & 0x1) != 0);
/*  19: 48 */     if (this._is16bitUnicode) {
/*  20: 49 */       this.field_3_string = StringUtil.readUnicodeLE(in, nChars);
/*  21:    */     } else {
/*  22: 51 */       this.field_3_string = StringUtil.readCompressedUnicode(in, nChars);
/*  23:    */     }
/*  24:    */   }
/*  25:    */   
/*  26:    */   public StringPtg(String value)
/*  27:    */   {
/*  28: 64 */     if (value.length() > 255) {
/*  29: 65 */       throw new IllegalArgumentException("String literals in formulas can't be bigger than 255 characters ASCII");
/*  30:    */     }
/*  31: 68 */     this._is16bitUnicode = StringUtil.hasMultibyte(value);
/*  32: 69 */     this.field_3_string = value;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public String getValue()
/*  36:    */   {
/*  37: 73 */     return this.field_3_string;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void write(LittleEndianOutput out)
/*  41:    */   {
/*  42: 77 */     out.writeByte(23 + getPtgClass());
/*  43: 78 */     out.writeByte(this.field_3_string.length());
/*  44: 79 */     out.writeByte(this._is16bitUnicode ? 1 : 0);
/*  45: 80 */     if (this._is16bitUnicode) {
/*  46: 81 */       StringUtil.putUnicodeLE(this.field_3_string, out);
/*  47:    */     } else {
/*  48: 83 */       StringUtil.putCompressedUnicode(this.field_3_string, out);
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int getSize()
/*  53:    */   {
/*  54: 88 */     return 3 + this.field_3_string.length() * (this._is16bitUnicode ? 2 : 1);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public String toFormulaString()
/*  58:    */   {
/*  59: 92 */     String value = this.field_3_string;
/*  60: 93 */     int len = value.length();
/*  61: 94 */     StringBuffer sb = new StringBuffer(len + 4);
/*  62: 95 */     sb.append('"');
/*  63: 97 */     for (int i = 0; i < len; i++)
/*  64:    */     {
/*  65: 98 */       char c = value.charAt(i);
/*  66: 99 */       if (c == '"') {
/*  67:100 */         sb.append('"');
/*  68:    */       }
/*  69:102 */       sb.append(c);
/*  70:    */     }
/*  71:105 */     sb.append('"');
/*  72:106 */     return sb.toString();
/*  73:    */   }
/*  74:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.StringPtg
 * JD-Core Version:    0.7.0.1
 */