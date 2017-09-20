/*   1:    */ package org.apache.poi.hssf.record.pivottable;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ import org.apache.poi.util.StringUtil;
/*   8:    */ 
/*   9:    */ public final class ViewDefinitionRecord
/*  10:    */   extends StandardRecord
/*  11:    */ {
/*  12:    */   public static final short sid = 176;
/*  13:    */   private int rwFirst;
/*  14:    */   private int rwLast;
/*  15:    */   private int colFirst;
/*  16:    */   private int colLast;
/*  17:    */   private int rwFirstHead;
/*  18:    */   private int rwFirstData;
/*  19:    */   private int colFirstData;
/*  20:    */   private int iCache;
/*  21:    */   private int reserved;
/*  22:    */   private int sxaxis4Data;
/*  23:    */   private int ipos4Data;
/*  24:    */   private int cDim;
/*  25:    */   private int cDimRw;
/*  26:    */   private int cDimCol;
/*  27:    */   private int cDimPg;
/*  28:    */   private int cDimData;
/*  29:    */   private int cRw;
/*  30:    */   private int cCol;
/*  31:    */   private int grbit;
/*  32:    */   private int itblAutoFmt;
/*  33:    */   private String dataField;
/*  34:    */   private String name;
/*  35:    */   
/*  36:    */   public ViewDefinitionRecord(RecordInputStream in)
/*  37:    */   {
/*  38: 62 */     this.rwFirst = in.readUShort();
/*  39: 63 */     this.rwLast = in.readUShort();
/*  40: 64 */     this.colFirst = in.readUShort();
/*  41: 65 */     this.colLast = in.readUShort();
/*  42: 66 */     this.rwFirstHead = in.readUShort();
/*  43: 67 */     this.rwFirstData = in.readUShort();
/*  44: 68 */     this.colFirstData = in.readUShort();
/*  45: 69 */     this.iCache = in.readUShort();
/*  46: 70 */     this.reserved = in.readUShort();
/*  47: 71 */     this.sxaxis4Data = in.readUShort();
/*  48: 72 */     this.ipos4Data = in.readUShort();
/*  49: 73 */     this.cDim = in.readUShort();
/*  50: 74 */     this.cDimRw = in.readUShort();
/*  51: 75 */     this.cDimCol = in.readUShort();
/*  52: 76 */     this.cDimPg = in.readUShort();
/*  53: 77 */     this.cDimData = in.readUShort();
/*  54: 78 */     this.cRw = in.readUShort();
/*  55: 79 */     this.cCol = in.readUShort();
/*  56: 80 */     this.grbit = in.readUShort();
/*  57: 81 */     this.itblAutoFmt = in.readUShort();
/*  58: 82 */     int cchName = in.readUShort();
/*  59: 83 */     int cchData = in.readUShort();
/*  60:    */     
/*  61: 85 */     this.name = StringUtil.readUnicodeString(in, cchName);
/*  62: 86 */     this.dataField = StringUtil.readUnicodeString(in, cchData);
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected void serialize(LittleEndianOutput out)
/*  66:    */   {
/*  67: 91 */     out.writeShort(this.rwFirst);
/*  68: 92 */     out.writeShort(this.rwLast);
/*  69: 93 */     out.writeShort(this.colFirst);
/*  70: 94 */     out.writeShort(this.colLast);
/*  71: 95 */     out.writeShort(this.rwFirstHead);
/*  72: 96 */     out.writeShort(this.rwFirstData);
/*  73: 97 */     out.writeShort(this.colFirstData);
/*  74: 98 */     out.writeShort(this.iCache);
/*  75: 99 */     out.writeShort(this.reserved);
/*  76:100 */     out.writeShort(this.sxaxis4Data);
/*  77:101 */     out.writeShort(this.ipos4Data);
/*  78:102 */     out.writeShort(this.cDim);
/*  79:103 */     out.writeShort(this.cDimRw);
/*  80:104 */     out.writeShort(this.cDimCol);
/*  81:105 */     out.writeShort(this.cDimPg);
/*  82:106 */     out.writeShort(this.cDimData);
/*  83:107 */     out.writeShort(this.cRw);
/*  84:108 */     out.writeShort(this.cCol);
/*  85:109 */     out.writeShort(this.grbit);
/*  86:110 */     out.writeShort(this.itblAutoFmt);
/*  87:111 */     out.writeShort(this.name.length());
/*  88:112 */     out.writeShort(this.dataField.length());
/*  89:    */     
/*  90:114 */     StringUtil.writeUnicodeStringFlagAndData(out, this.name);
/*  91:115 */     StringUtil.writeUnicodeStringFlagAndData(out, this.dataField);
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected int getDataSize()
/*  95:    */   {
/*  96:120 */     return 40 + StringUtil.getEncodedSize(this.name) + StringUtil.getEncodedSize(this.dataField);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public short getSid()
/* 100:    */   {
/* 101:126 */     return 176;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public String toString()
/* 105:    */   {
/* 106:131 */     StringBuffer buffer = new StringBuffer();
/* 107:    */     
/* 108:133 */     buffer.append("[SXVIEW]\n");
/* 109:134 */     buffer.append("    .rwFirst      =").append(HexDump.shortToHex(this.rwFirst)).append('\n');
/* 110:135 */     buffer.append("    .rwLast       =").append(HexDump.shortToHex(this.rwLast)).append('\n');
/* 111:136 */     buffer.append("    .colFirst     =").append(HexDump.shortToHex(this.colFirst)).append('\n');
/* 112:137 */     buffer.append("    .colLast      =").append(HexDump.shortToHex(this.colLast)).append('\n');
/* 113:138 */     buffer.append("    .rwFirstHead  =").append(HexDump.shortToHex(this.rwFirstHead)).append('\n');
/* 114:139 */     buffer.append("    .rwFirstData  =").append(HexDump.shortToHex(this.rwFirstData)).append('\n');
/* 115:140 */     buffer.append("    .colFirstData =").append(HexDump.shortToHex(this.colFirstData)).append('\n');
/* 116:141 */     buffer.append("    .iCache       =").append(HexDump.shortToHex(this.iCache)).append('\n');
/* 117:142 */     buffer.append("    .reserved     =").append(HexDump.shortToHex(this.reserved)).append('\n');
/* 118:143 */     buffer.append("    .sxaxis4Data  =").append(HexDump.shortToHex(this.sxaxis4Data)).append('\n');
/* 119:144 */     buffer.append("    .ipos4Data    =").append(HexDump.shortToHex(this.ipos4Data)).append('\n');
/* 120:145 */     buffer.append("    .cDim         =").append(HexDump.shortToHex(this.cDim)).append('\n');
/* 121:146 */     buffer.append("    .cDimRw       =").append(HexDump.shortToHex(this.cDimRw)).append('\n');
/* 122:147 */     buffer.append("    .cDimCol      =").append(HexDump.shortToHex(this.cDimCol)).append('\n');
/* 123:148 */     buffer.append("    .cDimPg       =").append(HexDump.shortToHex(this.cDimPg)).append('\n');
/* 124:149 */     buffer.append("    .cDimData     =").append(HexDump.shortToHex(this.cDimData)).append('\n');
/* 125:150 */     buffer.append("    .cRw          =").append(HexDump.shortToHex(this.cRw)).append('\n');
/* 126:151 */     buffer.append("    .cCol         =").append(HexDump.shortToHex(this.cCol)).append('\n');
/* 127:152 */     buffer.append("    .grbit        =").append(HexDump.shortToHex(this.grbit)).append('\n');
/* 128:153 */     buffer.append("    .itblAutoFmt  =").append(HexDump.shortToHex(this.itblAutoFmt)).append('\n');
/* 129:154 */     buffer.append("    .name         =").append(this.name).append('\n');
/* 130:155 */     buffer.append("    .dataField    =").append(this.dataField).append('\n');
/* 131:    */     
/* 132:157 */     buffer.append("[/SXVIEW]\n");
/* 133:158 */     return buffer.toString();
/* 134:    */   }
/* 135:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.pivottable.ViewDefinitionRecord
 * JD-Core Version:    0.7.0.1
 */