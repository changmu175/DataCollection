/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.BitField;
/*   4:    */ import org.apache.poi.util.BitFieldFactory;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ 
/*   7:    */ public final class WSBoolRecord
/*   8:    */   extends StandardRecord
/*   9:    */ {
/*  10:    */   public static final short sid = 129;
/*  11:    */   private byte field_1_wsbool;
/*  12:    */   private byte field_2_wsbool;
/*  13: 36 */   private static final BitField autobreaks = BitFieldFactory.getInstance(1);
/*  14: 39 */   private static final BitField dialog = BitFieldFactory.getInstance(16);
/*  15: 40 */   private static final BitField applystyles = BitFieldFactory.getInstance(32);
/*  16: 41 */   private static final BitField rowsumsbelow = BitFieldFactory.getInstance(64);
/*  17: 42 */   private static final BitField rowsumsright = BitFieldFactory.getInstance(128);
/*  18: 43 */   private static final BitField fittopage = BitFieldFactory.getInstance(1);
/*  19: 46 */   private static final BitField displayguts = BitFieldFactory.getInstance(6);
/*  20: 49 */   private static final BitField alternateexpression = BitFieldFactory.getInstance(64);
/*  21: 50 */   private static final BitField alternateformula = BitFieldFactory.getInstance(128);
/*  22:    */   
/*  23:    */   public WSBoolRecord() {}
/*  24:    */   
/*  25:    */   public WSBoolRecord(RecordInputStream in)
/*  26:    */   {
/*  27: 58 */     byte[] data = in.readRemainder();
/*  28: 59 */     this.field_1_wsbool = data[1];
/*  29:    */     
/*  30: 61 */     this.field_2_wsbool = data[0];
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setWSBool1(byte bool1)
/*  34:    */   {
/*  35: 77 */     this.field_1_wsbool = bool1;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setAutobreaks(boolean ab)
/*  39:    */   {
/*  40: 89 */     this.field_1_wsbool = autobreaks.setByteBoolean(this.field_1_wsbool, ab);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setDialog(boolean isDialog)
/*  44:    */   {
/*  45: 99 */     this.field_1_wsbool = dialog.setByteBoolean(this.field_1_wsbool, isDialog);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setRowSumsBelow(boolean below)
/*  49:    */   {
/*  50:109 */     this.field_1_wsbool = rowsumsbelow.setByteBoolean(this.field_1_wsbool, below);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setRowSumsRight(boolean right)
/*  54:    */   {
/*  55:119 */     this.field_1_wsbool = rowsumsright.setByteBoolean(this.field_1_wsbool, right);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setWSBool2(byte bool2)
/*  59:    */   {
/*  60:130 */     this.field_2_wsbool = bool2;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setFitToPage(boolean fit2page)
/*  64:    */   {
/*  65:142 */     this.field_2_wsbool = fittopage.setByteBoolean(this.field_2_wsbool, fit2page);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setDisplayGuts(boolean guts)
/*  69:    */   {
/*  70:153 */     this.field_2_wsbool = displayguts.setByteBoolean(this.field_2_wsbool, guts);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setAlternateExpression(boolean altexp)
/*  74:    */   {
/*  75:163 */     this.field_2_wsbool = alternateexpression.setByteBoolean(this.field_2_wsbool, altexp);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setAlternateFormula(boolean formula)
/*  79:    */   {
/*  80:174 */     this.field_2_wsbool = alternateformula.setByteBoolean(this.field_2_wsbool, formula);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public byte getWSBool1()
/*  84:    */   {
/*  85:186 */     return this.field_1_wsbool;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public boolean getAutobreaks()
/*  89:    */   {
/*  90:198 */     return autobreaks.isSet(this.field_1_wsbool);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public boolean getDialog()
/*  94:    */   {
/*  95:208 */     return dialog.isSet(this.field_1_wsbool);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public boolean getRowSumsBelow()
/*  99:    */   {
/* 100:218 */     return rowsumsbelow.isSet(this.field_1_wsbool);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public boolean getRowSumsRight()
/* 104:    */   {
/* 105:228 */     return rowsumsright.isSet(this.field_1_wsbool);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public byte getWSBool2()
/* 109:    */   {
/* 110:239 */     return this.field_2_wsbool;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public boolean getFitToPage()
/* 114:    */   {
/* 115:251 */     return fittopage.isSet(this.field_2_wsbool);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public boolean getDisplayGuts()
/* 119:    */   {
/* 120:262 */     return displayguts.isSet(this.field_2_wsbool);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public boolean getAlternateExpression()
/* 124:    */   {
/* 125:272 */     return alternateexpression.isSet(this.field_2_wsbool);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public boolean getAlternateFormula()
/* 129:    */   {
/* 130:282 */     return alternateformula.isSet(this.field_2_wsbool);
/* 131:    */   }
/* 132:    */   
/* 133:    */   public String toString()
/* 134:    */   {
/* 135:287 */     return "[WSBOOL]\n    .wsbool1        = " + Integer.toHexString(getWSBool1()) + "\n" + "        .autobreaks = " + getAutobreaks() + "\n" + "        .dialog     = " + getDialog() + "\n" + "        .rowsumsbelw= " + getRowSumsBelow() + "\n" + "        .rowsumsrigt= " + getRowSumsRight() + "\n" + "    .wsbool2        = " + Integer.toHexString(getWSBool2()) + "\n" + "        .fittopage  = " + getFitToPage() + "\n" + "        .displayguts= " + getDisplayGuts() + "\n" + "        .alternateex= " + getAlternateExpression() + "\n" + "        .alternatefo= " + getAlternateFormula() + "\n" + "[/WSBOOL]\n";
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void serialize(LittleEndianOutput out)
/* 139:    */   {
/* 140:302 */     out.writeByte(getWSBool2());
/* 141:303 */     out.writeByte(getWSBool1());
/* 142:    */   }
/* 143:    */   
/* 144:    */   protected int getDataSize()
/* 145:    */   {
/* 146:307 */     return 2;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public short getSid()
/* 150:    */   {
/* 151:312 */     return 129;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public Object clone()
/* 155:    */   {
/* 156:316 */     WSBoolRecord rec = new WSBoolRecord();
/* 157:317 */     rec.field_1_wsbool = this.field_1_wsbool;
/* 158:318 */     rec.field_2_wsbool = this.field_2_wsbool;
/* 159:319 */     return rec;
/* 160:    */   }
/* 161:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.WSBoolRecord
 * JD-Core Version:    0.7.0.1
 */