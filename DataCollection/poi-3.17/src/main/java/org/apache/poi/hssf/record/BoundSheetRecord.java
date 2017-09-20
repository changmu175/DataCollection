/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Comparator;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.ss.util.WorkbookUtil;
/*   7:    */ import org.apache.poi.util.BitField;
/*   8:    */ import org.apache.poi.util.BitFieldFactory;
/*   9:    */ import org.apache.poi.util.HexDump;
/*  10:    */ import org.apache.poi.util.LittleEndian;
/*  11:    */ import org.apache.poi.util.LittleEndianOutput;
/*  12:    */ import org.apache.poi.util.StringUtil;
/*  13:    */ 
/*  14:    */ public final class BoundSheetRecord
/*  15:    */   extends StandardRecord
/*  16:    */ {
/*  17:    */   public static final short sid = 133;
/*  18: 43 */   private static final BitField hiddenFlag = BitFieldFactory.getInstance(1);
/*  19: 44 */   private static final BitField veryHiddenFlag = BitFieldFactory.getInstance(2);
/*  20:    */   private int field_1_position_of_BOF;
/*  21:    */   private int field_2_option_flags;
/*  22:    */   private int field_4_isMultibyteUnicode;
/*  23:    */   private String field_5_sheetname;
/*  24:    */   
/*  25:    */   public BoundSheetRecord(String sheetname)
/*  26:    */   {
/*  27: 51 */     this.field_2_option_flags = 0;
/*  28: 52 */     setSheetname(sheetname);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public BoundSheetRecord(RecordInputStream in)
/*  32:    */   {
/*  33: 65 */     byte[] buf = new byte[4];
/*  34: 66 */     in.readPlain(buf, 0, buf.length);
/*  35: 67 */     this.field_1_position_of_BOF = LittleEndian.getInt(buf);
/*  36: 68 */     this.field_2_option_flags = in.readUShort();
/*  37: 69 */     int field_3_sheetname_length = in.readUByte();
/*  38: 70 */     this.field_4_isMultibyteUnicode = in.readByte();
/*  39: 72 */     if (isMultibyte()) {
/*  40: 73 */       this.field_5_sheetname = in.readUnicodeLEString(field_3_sheetname_length);
/*  41:    */     } else {
/*  42: 75 */       this.field_5_sheetname = in.readCompressedUnicode(field_3_sheetname_length);
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setPositionOfBof(int pos)
/*  47:    */   {
/*  48: 86 */     this.field_1_position_of_BOF = pos;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setSheetname(String sheetName)
/*  52:    */   {
/*  53: 98 */     WorkbookUtil.validateSheetName(sheetName);
/*  54: 99 */     this.field_5_sheetname = sheetName;
/*  55:100 */     this.field_4_isMultibyteUnicode = (StringUtil.hasMultibyte(sheetName) ? 1 : 0);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public int getPositionOfBof()
/*  59:    */   {
/*  60:109 */     return this.field_1_position_of_BOF;
/*  61:    */   }
/*  62:    */   
/*  63:    */   private boolean isMultibyte()
/*  64:    */   {
/*  65:113 */     return (this.field_4_isMultibyteUnicode & 0x1) != 0;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public String getSheetname()
/*  69:    */   {
/*  70:121 */     return this.field_5_sheetname;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public String toString()
/*  74:    */   {
/*  75:125 */     StringBuffer buffer = new StringBuffer();
/*  76:    */     
/*  77:127 */     buffer.append("[BOUNDSHEET]\n");
/*  78:128 */     buffer.append("    .bof        = ").append(HexDump.intToHex(getPositionOfBof())).append("\n");
/*  79:129 */     buffer.append("    .options    = ").append(HexDump.shortToHex(this.field_2_option_flags)).append("\n");
/*  80:130 */     buffer.append("    .unicodeflag= ").append(HexDump.byteToHex(this.field_4_isMultibyteUnicode)).append("\n");
/*  81:131 */     buffer.append("    .sheetname  = ").append(this.field_5_sheetname).append("\n");
/*  82:132 */     buffer.append("[/BOUNDSHEET]\n");
/*  83:133 */     return buffer.toString();
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected int getDataSize()
/*  87:    */   {
/*  88:137 */     return 8 + this.field_5_sheetname.length() * (isMultibyte() ? 2 : 1);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void serialize(LittleEndianOutput out)
/*  92:    */   {
/*  93:141 */     out.writeInt(getPositionOfBof());
/*  94:142 */     out.writeShort(this.field_2_option_flags);
/*  95:    */     
/*  96:144 */     String name = this.field_5_sheetname;
/*  97:145 */     out.writeByte(name.length());
/*  98:146 */     out.writeByte(this.field_4_isMultibyteUnicode);
/*  99:148 */     if (isMultibyte()) {
/* 100:149 */       StringUtil.putUnicodeLE(name, out);
/* 101:    */     } else {
/* 102:151 */       StringUtil.putCompressedUnicode(name, out);
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   public short getSid()
/* 107:    */   {
/* 108:156 */     return 133;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public boolean isHidden()
/* 112:    */   {
/* 113:165 */     return hiddenFlag.isSet(this.field_2_option_flags);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public void setHidden(boolean hidden)
/* 117:    */   {
/* 118:174 */     this.field_2_option_flags = hiddenFlag.setBoolean(this.field_2_option_flags, hidden);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public boolean isVeryHidden()
/* 122:    */   {
/* 123:183 */     return veryHiddenFlag.isSet(this.field_2_option_flags);
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void setVeryHidden(boolean veryHidden)
/* 127:    */   {
/* 128:192 */     this.field_2_option_flags = veryHiddenFlag.setBoolean(this.field_2_option_flags, veryHidden);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public static BoundSheetRecord[] orderByBofPosition(List<BoundSheetRecord> boundSheetRecords)
/* 132:    */   {
/* 133:204 */     BoundSheetRecord[] bsrs = new BoundSheetRecord[boundSheetRecords.size()];
/* 134:205 */     boundSheetRecords.toArray(bsrs);
/* 135:206 */     Arrays.sort(bsrs, BOFComparator);
/* 136:207 */     return bsrs;
/* 137:    */   }
/* 138:    */   
/* 139:210 */   private static final Comparator<BoundSheetRecord> BOFComparator = new Comparator()
/* 140:    */   {
/* 141:    */     public int compare(BoundSheetRecord bsr1, BoundSheetRecord bsr2)
/* 142:    */     {
/* 143:213 */       return bsr1.getPositionOfBof() - bsr2.getPositionOfBof();
/* 144:    */     }
/* 145:    */   };
/* 146:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.BoundSheetRecord
 * JD-Core Version:    0.7.0.1
 */