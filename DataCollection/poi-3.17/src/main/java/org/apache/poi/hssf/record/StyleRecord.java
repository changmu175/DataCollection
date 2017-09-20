/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.BitField;
/*   4:    */ import org.apache.poi.util.BitFieldFactory;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ import org.apache.poi.util.RecordFormatException;
/*   8:    */ import org.apache.poi.util.StringUtil;
/*   9:    */ 
/*  10:    */ public final class StyleRecord
/*  11:    */   extends StandardRecord
/*  12:    */ {
/*  13:    */   public static final short sid = 659;
/*  14: 35 */   private static final BitField styleIndexMask = BitFieldFactory.getInstance(4095);
/*  15: 36 */   private static final BitField isBuiltinFlag = BitFieldFactory.getInstance(32768);
/*  16:    */   private int field_1_xf_index;
/*  17:    */   private int field_2_builtin_style;
/*  18:    */   private int field_3_outline_style_level;
/*  19:    */   private boolean field_3_stringHasMultibyte;
/*  20:    */   private String field_4_name;
/*  21:    */   
/*  22:    */   public StyleRecord()
/*  23:    */   {
/*  24: 53 */     this.field_1_xf_index = isBuiltinFlag.set(0);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public StyleRecord(RecordInputStream in)
/*  28:    */   {
/*  29: 57 */     this.field_1_xf_index = in.readShort();
/*  30: 58 */     if (isBuiltin())
/*  31:    */     {
/*  32: 59 */       this.field_2_builtin_style = in.readByte();
/*  33: 60 */       this.field_3_outline_style_level = in.readByte();
/*  34:    */     }
/*  35:    */     else
/*  36:    */     {
/*  37: 62 */       int field_2_name_length = in.readShort();
/*  38: 64 */       if (in.remaining() < 1)
/*  39:    */       {
/*  40: 67 */         if (field_2_name_length != 0) {
/*  41: 68 */           throw new RecordFormatException("Ran out of data reading style record");
/*  42:    */         }
/*  43: 71 */         this.field_4_name = "";
/*  44:    */       }
/*  45:    */       else
/*  46:    */       {
/*  47: 74 */         this.field_3_stringHasMultibyte = (in.readByte() != 0);
/*  48: 75 */         if (this.field_3_stringHasMultibyte) {
/*  49: 76 */           this.field_4_name = StringUtil.readUnicodeLE(in, field_2_name_length);
/*  50:    */         } else {
/*  51: 78 */           this.field_4_name = StringUtil.readCompressedUnicode(in, field_2_name_length);
/*  52:    */         }
/*  53:    */       }
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setXFIndex(int xfIndex)
/*  58:    */   {
/*  59: 89 */     this.field_1_xf_index = styleIndexMask.setValue(this.field_1_xf_index, xfIndex);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public int getXFIndex()
/*  63:    */   {
/*  64: 98 */     return styleIndexMask.getValue(this.field_1_xf_index);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setName(String name)
/*  68:    */   {
/*  69:106 */     this.field_4_name = name;
/*  70:107 */     this.field_3_stringHasMultibyte = StringUtil.hasMultibyte(name);
/*  71:108 */     this.field_1_xf_index = isBuiltinFlag.clear(this.field_1_xf_index);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setBuiltinStyle(int builtinStyleId)
/*  75:    */   {
/*  76:117 */     this.field_1_xf_index = isBuiltinFlag.set(this.field_1_xf_index);
/*  77:118 */     this.field_2_builtin_style = builtinStyleId;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setOutlineStyleLevel(int level)
/*  81:    */   {
/*  82:125 */     this.field_3_outline_style_level = (level & 0xFF);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public boolean isBuiltin()
/*  86:    */   {
/*  87:129 */     return isBuiltinFlag.isSet(this.field_1_xf_index);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public String getName()
/*  91:    */   {
/*  92:137 */     return this.field_4_name;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public String toString()
/*  96:    */   {
/*  97:142 */     StringBuilder sb = new StringBuilder();
/*  98:    */     
/*  99:144 */     sb.append("[STYLE]\n");
/* 100:145 */     sb.append("    .xf_index_raw =").append(HexDump.shortToHex(this.field_1_xf_index)).append("\n");
/* 101:146 */     sb.append("        .type     =").append(isBuiltin() ? "built-in" : "user-defined").append("\n");
/* 102:147 */     sb.append("        .xf_index =").append(HexDump.shortToHex(getXFIndex())).append("\n");
/* 103:148 */     if (isBuiltin())
/* 104:    */     {
/* 105:149 */       sb.append("    .builtin_style=").append(HexDump.byteToHex(this.field_2_builtin_style)).append("\n");
/* 106:150 */       sb.append("    .outline_level=").append(HexDump.byteToHex(this.field_3_outline_style_level)).append("\n");
/* 107:    */     }
/* 108:    */     else
/* 109:    */     {
/* 110:152 */       sb.append("    .name        =").append(getName()).append("\n");
/* 111:    */     }
/* 112:154 */     sb.append("[/STYLE]\n");
/* 113:155 */     return sb.toString();
/* 114:    */   }
/* 115:    */   
/* 116:    */   protected int getDataSize()
/* 117:    */   {
/* 118:161 */     if (isBuiltin()) {
/* 119:162 */       return 4;
/* 120:    */     }
/* 121:164 */     return 5 + this.field_4_name.length() * (this.field_3_stringHasMultibyte ? 2 : 1);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void serialize(LittleEndianOutput out)
/* 125:    */   {
/* 126:171 */     out.writeShort(this.field_1_xf_index);
/* 127:172 */     if (isBuiltin())
/* 128:    */     {
/* 129:173 */       out.writeByte(this.field_2_builtin_style);
/* 130:174 */       out.writeByte(this.field_3_outline_style_level);
/* 131:    */     }
/* 132:    */     else
/* 133:    */     {
/* 134:176 */       out.writeShort(this.field_4_name.length());
/* 135:177 */       out.writeByte(this.field_3_stringHasMultibyte ? 1 : 0);
/* 136:178 */       if (this.field_3_stringHasMultibyte) {
/* 137:179 */         StringUtil.putUnicodeLE(getName(), out);
/* 138:    */       } else {
/* 139:181 */         StringUtil.putCompressedUnicode(getName(), out);
/* 140:    */       }
/* 141:    */     }
/* 142:    */   }
/* 143:    */   
/* 144:    */   public short getSid()
/* 145:    */   {
/* 146:188 */     return 659;
/* 147:    */   }
/* 148:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.StyleRecord
 * JD-Core Version:    0.7.0.1
 */