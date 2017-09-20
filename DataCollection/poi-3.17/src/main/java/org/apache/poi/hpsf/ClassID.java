/*   1:    */ package org.apache.poi.hpsf;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.poi.util.HexDump;
/*   5:    */ 
/*   6:    */ public class ClassID
/*   7:    */ {
/*   8: 33 */   public static final ClassID OLE10_PACKAGE = new ClassID("{0003000C-0000-0000-C000-000000000046}");
/*   9: 34 */   public static final ClassID PPT_SHOW = new ClassID("{64818D10-4F9B-11CF-86EA-00AA00B929E8}");
/*  10: 35 */   public static final ClassID XLS_WORKBOOK = new ClassID("{00020841-0000-0000-C000-000000000046}");
/*  11: 36 */   public static final ClassID TXT_ONLY = new ClassID("{5e941d80-bf96-11cd-b579-08002b30bfeb}");
/*  12: 39 */   public static final ClassID EXCEL_V3 = new ClassID("{00030000-0000-0000-C000-000000000046}");
/*  13: 40 */   public static final ClassID EXCEL_V3_CHART = new ClassID("{00030001-0000-0000-C000-000000000046}");
/*  14: 41 */   public static final ClassID EXCEL_V3_MACRO = new ClassID("{00030002-0000-0000-C000-000000000046}");
/*  15: 43 */   public static final ClassID EXCEL95 = new ClassID("{00020810-0000-0000-C000-000000000046}");
/*  16: 44 */   public static final ClassID EXCEL95_CHART = new ClassID("{00020811-0000-0000-C000-000000000046}");
/*  17: 46 */   public static final ClassID EXCEL97 = new ClassID("{00020820-0000-0000-C000-000000000046}");
/*  18: 47 */   public static final ClassID EXCEL97_CHART = new ClassID("{00020821-0000-0000-C000-000000000046}");
/*  19: 49 */   public static final ClassID EXCEL2003 = new ClassID("{00020812-0000-0000-C000-000000000046}");
/*  20: 51 */   public static final ClassID EXCEL2007 = new ClassID("{00020830-0000-0000-C000-000000000046}");
/*  21: 52 */   public static final ClassID EXCEL2007_MACRO = new ClassID("{00020832-0000-0000-C000-000000000046}");
/*  22: 53 */   public static final ClassID EXCEL2007_XLSB = new ClassID("{00020833-0000-0000-C000-000000000046}");
/*  23: 55 */   public static final ClassID EXCEL2010 = new ClassID("{00024500-0000-0000-C000-000000000046}");
/*  24: 56 */   public static final ClassID EXCEL2010_CHART = new ClassID("{00024505-0014-0000-C000-000000000046}");
/*  25: 57 */   public static final ClassID EXCEL2010_ODS = new ClassID("{EABCECDB-CC1C-4A6F-B4E3-7F888A5ADFC8}");
/*  26: 59 */   public static final ClassID WORD97 = new ClassID("{00020906-0000-0000-C000-000000000046}");
/*  27: 60 */   public static final ClassID WORD95 = new ClassID("{00020900-0000-0000-C000-000000000046}");
/*  28: 61 */   public static final ClassID WORD2007 = new ClassID("{F4754C9B-64F5-4B40-8AF4-679732AC0607}");
/*  29: 62 */   public static final ClassID WORD2007_MACRO = new ClassID("{18A06B6B-2F3F-4E2B-A611-52BE631B2D22}");
/*  30: 64 */   public static final ClassID POWERPOINT97 = new ClassID("{64818D10-4F9B-11CF-86EA-00AA00B929E8}");
/*  31: 65 */   public static final ClassID POWERPOINT95 = new ClassID("{EA7BAE70-FB3B-11CD-A903-00AA00510EA3}");
/*  32: 66 */   public static final ClassID POWERPOINT2007 = new ClassID("{CF4F55F4-8F87-4D47-80BB-5808164BB3F8}");
/*  33: 67 */   public static final ClassID POWERPOINT2007_MACRO = new ClassID("{DC020317-E6E2-4A62-B9FA-B3EFE16626F4}");
/*  34: 69 */   public static final ClassID EQUATION30 = new ClassID("{0002CE02-0000-0000-C000-000000000046}");
/*  35:    */   public static final int LENGTH = 16;
/*  36: 77 */   private final byte[] bytes = new byte[16];
/*  37:    */   
/*  38:    */   public ClassID(byte[] src, int offset)
/*  39:    */   {
/*  40: 86 */     read(src, offset);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public ClassID()
/*  44:    */   {
/*  45: 94 */     Arrays.fill(this.bytes, (byte)0);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public ClassID(String externalForm)
/*  49:    */   {
/*  50:105 */     String clsStr = externalForm.replaceAll("[{}-]", "");
/*  51:106 */     for (int i = 0; i < clsStr.length(); i += 2) {
/*  52:107 */       this.bytes[(i / 2)] = ((byte)Integer.parseInt(clsStr.substring(i, i + 2), 16));
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public int length()
/*  57:    */   {
/*  58:116 */     return 16;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public byte[] getBytes()
/*  62:    */   {
/*  63:127 */     return this.bytes;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void setBytes(byte[] bytes)
/*  67:    */   {
/*  68:139 */     System.arraycopy(bytes, 0, this.bytes, 0, 16);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public byte[] read(byte[] src, int offset)
/*  72:    */   {
/*  73:153 */     this.bytes[0] = src[(3 + offset)];
/*  74:154 */     this.bytes[1] = src[(2 + offset)];
/*  75:155 */     this.bytes[2] = src[(1 + offset)];
/*  76:156 */     this.bytes[3] = src[(0 + offset)];
/*  77:    */     
/*  78:    */ 
/*  79:159 */     this.bytes[4] = src[(5 + offset)];
/*  80:160 */     this.bytes[5] = src[(4 + offset)];
/*  81:    */     
/*  82:    */ 
/*  83:163 */     this.bytes[6] = src[(7 + offset)];
/*  84:164 */     this.bytes[7] = src[(6 + offset)];
/*  85:    */     
/*  86:    */ 
/*  87:167 */     System.arraycopy(src, 8 + offset, this.bytes, 8, 8);
/*  88:    */     
/*  89:169 */     return this.bytes;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void write(byte[] dst, int offset)
/*  93:    */     throws ArrayStoreException
/*  94:    */   {
/*  95:185 */     if (dst.length < 16) {
/*  96:186 */       throw new ArrayStoreException("Destination byte[] must have room for at least 16 bytes, but has a length of only " + dst.length + ".");
/*  97:    */     }
/*  98:192 */     dst[(0 + offset)] = this.bytes[3];
/*  99:193 */     dst[(1 + offset)] = this.bytes[2];
/* 100:194 */     dst[(2 + offset)] = this.bytes[1];
/* 101:195 */     dst[(3 + offset)] = this.bytes[0];
/* 102:    */     
/* 103:    */ 
/* 104:198 */     dst[(4 + offset)] = this.bytes[5];
/* 105:199 */     dst[(5 + offset)] = this.bytes[4];
/* 106:    */     
/* 107:    */ 
/* 108:202 */     dst[(6 + offset)] = this.bytes[7];
/* 109:203 */     dst[(7 + offset)] = this.bytes[6];
/* 110:    */     
/* 111:    */ 
/* 112:206 */     System.arraycopy(this.bytes, 8, dst, 8 + offset, 8);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public boolean equals(Object o)
/* 116:    */   {
/* 117:219 */     return ((o instanceof ClassID)) && (Arrays.equals(this.bytes, ((ClassID)o).bytes));
/* 118:    */   }
/* 119:    */   
/* 120:    */   public boolean equalsInverted(ClassID o)
/* 121:    */   {
/* 122:231 */     return (o.bytes[0] == this.bytes[3]) && (o.bytes[1] == this.bytes[2]) && (o.bytes[2] == this.bytes[1]) && (o.bytes[3] == this.bytes[0]) && (o.bytes[4] == this.bytes[5]) && (o.bytes[5] == this.bytes[4]) && (o.bytes[6] == this.bytes[7]) && (o.bytes[7] == this.bytes[6]) && (o.bytes[8] == this.bytes[8]) && (o.bytes[9] == this.bytes[9]) && (o.bytes[10] == this.bytes[10]) && (o.bytes[11] == this.bytes[11]) && (o.bytes[12] == this.bytes[12]) && (o.bytes[13] == this.bytes[13]) && (o.bytes[14] == this.bytes[14]) && (o.bytes[15] == this.bytes[15]);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public int hashCode()
/* 126:    */   {
/* 127:257 */     return toString().hashCode();
/* 128:    */   }
/* 129:    */   
/* 130:    */   public String toString()
/* 131:    */   {
/* 132:268 */     StringBuilder sbClassId = new StringBuilder(38);
/* 133:269 */     sbClassId.append('{');
/* 134:270 */     for (int i = 0; i < 16; i++)
/* 135:    */     {
/* 136:271 */       sbClassId.append(HexDump.toHex(this.bytes[i]));
/* 137:272 */       if ((i == 3) || (i == 5) || (i == 7) || (i == 9)) {
/* 138:273 */         sbClassId.append('-');
/* 139:    */       }
/* 140:    */     }
/* 141:276 */     sbClassId.append('}');
/* 142:277 */     return sbClassId.toString();
/* 143:    */   }
/* 144:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.ClassID
 * JD-Core Version:    0.7.0.1
 */