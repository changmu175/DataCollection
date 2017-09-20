/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.LittleEndian;
/*   5:    */ 
/*   6:    */ public class EscherSpRecord
/*   7:    */   extends EscherRecord
/*   8:    */ {
/*   9:    */   public static final short RECORD_ID = -4086;
/*  10:    */   public static final String RECORD_DESCRIPTION = "MsofbtSp";
/*  11:    */   public static final int FLAG_GROUP = 1;
/*  12:    */   public static final int FLAG_CHILD = 2;
/*  13:    */   public static final int FLAG_PATRIARCH = 4;
/*  14:    */   public static final int FLAG_DELETED = 8;
/*  15:    */   public static final int FLAG_OLESHAPE = 16;
/*  16:    */   public static final int FLAG_HAVEMASTER = 32;
/*  17:    */   public static final int FLAG_FLIPHORIZ = 64;
/*  18:    */   public static final int FLAG_FLIPVERT = 128;
/*  19:    */   public static final int FLAG_CONNECTOR = 256;
/*  20:    */   public static final int FLAG_HAVEANCHOR = 512;
/*  21:    */   public static final int FLAG_BACKGROUND = 1024;
/*  22:    */   public static final int FLAG_HASSHAPETYPE = 2048;
/*  23:    */   private int field_1_shapeId;
/*  24:    */   private int field_2_flags;
/*  25:    */   
/*  26:    */   public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory)
/*  27:    */   {
/*  28: 51 */     readHeader(data, offset);
/*  29: 52 */     int pos = offset + 8;
/*  30: 53 */     int size = 0;
/*  31: 54 */     this.field_1_shapeId = LittleEndian.getInt(data, pos + size);size += 4;
/*  32: 55 */     this.field_2_flags = LittleEndian.getInt(data, pos + size);size += 4;
/*  33:    */     
/*  34:    */ 
/*  35:    */ 
/*  36: 59 */     return getRecordSize();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  40:    */   {
/*  41: 75 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  42: 76 */     LittleEndian.putShort(data, offset, getOptions());
/*  43: 77 */     LittleEndian.putShort(data, offset + 2, getRecordId());
/*  44: 78 */     int remainingBytes = 8;
/*  45: 79 */     LittleEndian.putInt(data, offset + 4, remainingBytes);
/*  46: 80 */     LittleEndian.putInt(data, offset + 8, this.field_1_shapeId);
/*  47: 81 */     LittleEndian.putInt(data, offset + 12, this.field_2_flags);
/*  48:    */     
/*  49:    */ 
/*  50: 84 */     listener.afterRecordSerialize(offset + getRecordSize(), getRecordId(), getRecordSize(), this);
/*  51: 85 */     return 16;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public int getRecordSize()
/*  55:    */   {
/*  56: 91 */     return 16;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public short getRecordId()
/*  60:    */   {
/*  61: 96 */     return -4086;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String getRecordName()
/*  65:    */   {
/*  66:101 */     return "Sp";
/*  67:    */   }
/*  68:    */   
/*  69:    */   private String decodeFlags(int flags)
/*  70:    */   {
/*  71:109 */     StringBuffer result = new StringBuffer();
/*  72:110 */     result.append((flags & 0x1) != 0 ? "|GROUP" : "");
/*  73:111 */     result.append((flags & 0x2) != 0 ? "|CHILD" : "");
/*  74:112 */     result.append((flags & 0x4) != 0 ? "|PATRIARCH" : "");
/*  75:113 */     result.append((flags & 0x8) != 0 ? "|DELETED" : "");
/*  76:114 */     result.append((flags & 0x10) != 0 ? "|OLESHAPE" : "");
/*  77:115 */     result.append((flags & 0x20) != 0 ? "|HAVEMASTER" : "");
/*  78:116 */     result.append((flags & 0x40) != 0 ? "|FLIPHORIZ" : "");
/*  79:117 */     result.append((flags & 0x80) != 0 ? "|FLIPVERT" : "");
/*  80:118 */     result.append((flags & 0x100) != 0 ? "|CONNECTOR" : "");
/*  81:119 */     result.append((flags & 0x200) != 0 ? "|HAVEANCHOR" : "");
/*  82:120 */     result.append((flags & 0x400) != 0 ? "|BACKGROUND" : "");
/*  83:121 */     result.append((flags & 0x800) != 0 ? "|HASSHAPETYPE" : "");
/*  84:124 */     if (result.length() > 0) {
/*  85:125 */       result.deleteCharAt(0);
/*  86:    */     }
/*  87:127 */     return result.toString();
/*  88:    */   }
/*  89:    */   
/*  90:    */   public int getShapeId()
/*  91:    */   {
/*  92:135 */     return this.field_1_shapeId;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setShapeId(int field_1_shapeId)
/*  96:    */   {
/*  97:145 */     this.field_1_shapeId = field_1_shapeId;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public int getFlags()
/* 101:    */   {
/* 102:168 */     return this.field_2_flags;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setFlags(int field_2_flags)
/* 106:    */   {
/* 107:191 */     this.field_2_flags = field_2_flags;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public short getShapeType()
/* 111:    */   {
/* 112:202 */     return getInstance();
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void setShapeType(short value)
/* 116:    */   {
/* 117:214 */     setInstance(value);
/* 118:    */   }
/* 119:    */   
/* 120:    */   protected Object[][] getAttributeMap()
/* 121:    */   {
/* 122:219 */     return new Object[][] { { "ShapeType", Short.valueOf(getShapeType()) }, { "ShapeId", Integer.valueOf(this.field_1_shapeId) }, { "Flags", decodeFlags(this.field_2_flags) + " (0x" + HexDump.toHex(this.field_2_flags) + ")" } };
/* 123:    */   }
/* 124:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherSpRecord
 * JD-Core Version:    0.7.0.1
 */