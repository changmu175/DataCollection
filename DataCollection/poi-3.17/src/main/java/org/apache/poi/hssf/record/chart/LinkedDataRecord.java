/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.ss.formula.Formula;
/*   6:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   7:    */ import org.apache.poi.util.BitField;
/*   8:    */ import org.apache.poi.util.BitFieldFactory;
/*   9:    */ import org.apache.poi.util.HexDump;
/*  10:    */ import org.apache.poi.util.LittleEndianOutput;
/*  11:    */ 
/*  12:    */ public final class LinkedDataRecord
/*  13:    */   extends StandardRecord
/*  14:    */   implements Cloneable
/*  15:    */ {
/*  16:    */   public static final short sid = 4177;
/*  17: 35 */   private static final BitField customNumberFormat = BitFieldFactory.getInstance(1);
/*  18:    */   private byte field_1_linkType;
/*  19:    */   public static final byte LINK_TYPE_TITLE_OR_TEXT = 0;
/*  20:    */   public static final byte LINK_TYPE_VALUES = 1;
/*  21:    */   public static final byte LINK_TYPE_CATEGORIES = 2;
/*  22:    */   public static final byte LINK_TYPE_SECONDARY_CATEGORIES = 3;
/*  23:    */   private byte field_2_referenceType;
/*  24:    */   public static final byte REFERENCE_TYPE_DEFAULT_CATEGORIES = 0;
/*  25:    */   public static final byte REFERENCE_TYPE_DIRECT = 1;
/*  26:    */   public static final byte REFERENCE_TYPE_WORKSHEET = 2;
/*  27:    */   public static final byte REFERENCE_TYPE_NOT_USED = 3;
/*  28:    */   public static final byte REFERENCE_TYPE_ERROR_REPORTED = 4;
/*  29:    */   private short field_3_options;
/*  30:    */   private short field_4_indexNumberFmtRecord;
/*  31:    */   private Formula field_5_formulaOfLink;
/*  32:    */   
/*  33:    */   public LinkedDataRecord() {}
/*  34:    */   
/*  35:    */   public LinkedDataRecord(RecordInputStream in)
/*  36:    */   {
/*  37: 60 */     this.field_1_linkType = in.readByte();
/*  38: 61 */     this.field_2_referenceType = in.readByte();
/*  39: 62 */     this.field_3_options = in.readShort();
/*  40: 63 */     this.field_4_indexNumberFmtRecord = in.readShort();
/*  41: 64 */     int encodedTokenLen = in.readUShort();
/*  42: 65 */     this.field_5_formulaOfLink = Formula.read(encodedTokenLen, in);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public String toString()
/*  46:    */   {
/*  47: 69 */     StringBuffer buffer = new StringBuffer();
/*  48:    */     
/*  49: 71 */     buffer.append("[AI]\n");
/*  50: 72 */     buffer.append("    .linkType             = ").append(HexDump.byteToHex(getLinkType())).append('\n');
/*  51: 73 */     buffer.append("    .referenceType        = ").append(HexDump.byteToHex(getReferenceType())).append('\n');
/*  52: 74 */     buffer.append("    .options              = ").append(HexDump.shortToHex(getOptions())).append('\n');
/*  53: 75 */     buffer.append("    .customNumberFormat   = ").append(isCustomNumberFormat()).append('\n');
/*  54: 76 */     buffer.append("    .indexNumberFmtRecord = ").append(HexDump.shortToHex(getIndexNumberFmtRecord())).append('\n');
/*  55: 77 */     buffer.append("    .formulaOfLink        = ").append('\n');
/*  56: 78 */     Ptg[] ptgs = this.field_5_formulaOfLink.getTokens();
/*  57: 79 */     for (int i = 0; i < ptgs.length; i++)
/*  58:    */     {
/*  59: 80 */       Ptg ptg = ptgs[i];
/*  60: 81 */       buffer.append(ptg).append(ptg.getRVAType()).append('\n');
/*  61:    */     }
/*  62: 84 */     buffer.append("[/AI]\n");
/*  63: 85 */     return buffer.toString();
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void serialize(LittleEndianOutput out)
/*  67:    */   {
/*  68: 89 */     out.writeByte(this.field_1_linkType);
/*  69: 90 */     out.writeByte(this.field_2_referenceType);
/*  70: 91 */     out.writeShort(this.field_3_options);
/*  71: 92 */     out.writeShort(this.field_4_indexNumberFmtRecord);
/*  72: 93 */     this.field_5_formulaOfLink.serialize(out);
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected int getDataSize()
/*  76:    */   {
/*  77: 97 */     return 6 + this.field_5_formulaOfLink.getEncodedSize();
/*  78:    */   }
/*  79:    */   
/*  80:    */   public short getSid()
/*  81:    */   {
/*  82:101 */     return 4177;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public LinkedDataRecord clone()
/*  86:    */   {
/*  87:106 */     LinkedDataRecord rec = new LinkedDataRecord();
/*  88:    */     
/*  89:108 */     rec.field_1_linkType = this.field_1_linkType;
/*  90:109 */     rec.field_2_referenceType = this.field_2_referenceType;
/*  91:110 */     rec.field_3_options = this.field_3_options;
/*  92:111 */     rec.field_4_indexNumberFmtRecord = this.field_4_indexNumberFmtRecord;
/*  93:112 */     rec.field_5_formulaOfLink = this.field_5_formulaOfLink.copy();
/*  94:113 */     return rec;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public byte getLinkType()
/*  98:    */   {
/*  99:130 */     return this.field_1_linkType;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setLinkType(byte field_1_linkType)
/* 103:    */   {
/* 104:145 */     this.field_1_linkType = field_1_linkType;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public byte getReferenceType()
/* 108:    */   {
/* 109:160 */     return this.field_2_referenceType;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void setReferenceType(byte field_2_referenceType)
/* 113:    */   {
/* 114:176 */     this.field_2_referenceType = field_2_referenceType;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public short getOptions()
/* 118:    */   {
/* 119:184 */     return this.field_3_options;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setOptions(short field_3_options)
/* 123:    */   {
/* 124:192 */     this.field_3_options = field_3_options;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public short getIndexNumberFmtRecord()
/* 128:    */   {
/* 129:200 */     return this.field_4_indexNumberFmtRecord;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void setIndexNumberFmtRecord(short field_4_indexNumberFmtRecord)
/* 133:    */   {
/* 134:208 */     this.field_4_indexNumberFmtRecord = field_4_indexNumberFmtRecord;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public Ptg[] getFormulaOfLink()
/* 138:    */   {
/* 139:215 */     return this.field_5_formulaOfLink.getTokens();
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void setFormulaOfLink(Ptg[] ptgs)
/* 143:    */   {
/* 144:223 */     this.field_5_formulaOfLink = Formula.create(ptgs);
/* 145:    */   }
/* 146:    */   
/* 147:    */   public void setCustomNumberFormat(boolean value)
/* 148:    */   {
/* 149:232 */     this.field_3_options = customNumberFormat.setShortBoolean(this.field_3_options, value);
/* 150:    */   }
/* 151:    */   
/* 152:    */   public boolean isCustomNumberFormat()
/* 153:    */   {
/* 154:241 */     return customNumberFormat.isSet(this.field_3_options);
/* 155:    */   }
/* 156:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.LinkedDataRecord
 * JD-Core Version:    0.7.0.1
 */