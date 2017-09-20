/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndian;
/*   4:    */ import org.apache.poi.util.RecordFormatException;
/*   5:    */ 
/*   6:    */ public class EscherSplitMenuColorsRecord
/*   7:    */   extends EscherRecord
/*   8:    */ {
/*   9:    */   public static final short RECORD_ID = -3810;
/*  10:    */   public static final String RECORD_DESCRIPTION = "MsofbtSplitMenuColors";
/*  11:    */   private int field_1_color1;
/*  12:    */   private int field_2_color2;
/*  13:    */   private int field_3_color3;
/*  14:    */   private int field_4_color4;
/*  15:    */   
/*  16:    */   public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory)
/*  17:    */   {
/*  18: 40 */     int bytesRemaining = readHeader(data, offset);
/*  19: 41 */     int pos = offset + 8;
/*  20: 42 */     int size = 0;
/*  21: 43 */     this.field_1_color1 = LittleEndian.getInt(data, pos + size);size += 4;
/*  22: 44 */     this.field_2_color2 = LittleEndian.getInt(data, pos + size);size += 4;
/*  23: 45 */     this.field_3_color3 = LittleEndian.getInt(data, pos + size);size += 4;
/*  24: 46 */     this.field_4_color4 = LittleEndian.getInt(data, pos + size);size += 4;
/*  25: 47 */     bytesRemaining -= size;
/*  26: 48 */     if (bytesRemaining != 0) {
/*  27: 49 */       throw new RecordFormatException("Expecting no remaining data but got " + bytesRemaining + " byte(s).");
/*  28:    */     }
/*  29: 51 */     return 8 + size + bytesRemaining;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  33:    */   {
/*  34: 57 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  35:    */     
/*  36: 59 */     int pos = offset;
/*  37: 60 */     LittleEndian.putShort(data, pos, getOptions());pos += 2;
/*  38: 61 */     LittleEndian.putShort(data, pos, getRecordId());pos += 2;
/*  39: 62 */     int remainingBytes = getRecordSize() - 8;
/*  40:    */     
/*  41: 64 */     LittleEndian.putInt(data, pos, remainingBytes);pos += 4;
/*  42: 65 */     LittleEndian.putInt(data, pos, this.field_1_color1);pos += 4;
/*  43: 66 */     LittleEndian.putInt(data, pos, this.field_2_color2);pos += 4;
/*  44: 67 */     LittleEndian.putInt(data, pos, this.field_3_color3);pos += 4;
/*  45: 68 */     LittleEndian.putInt(data, pos, this.field_4_color4);pos += 4;
/*  46: 69 */     listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
/*  47: 70 */     return getRecordSize();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public int getRecordSize()
/*  51:    */   {
/*  52: 75 */     return 24;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public short getRecordId()
/*  56:    */   {
/*  57: 80 */     return -3810;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public String getRecordName()
/*  61:    */   {
/*  62: 85 */     return "SplitMenuColors";
/*  63:    */   }
/*  64:    */   
/*  65:    */   public int getColor1()
/*  66:    */   {
/*  67: 94 */     return this.field_1_color1;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setColor1(int field_1_color1)
/*  71:    */   {
/*  72:103 */     this.field_1_color1 = field_1_color1;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public int getColor2()
/*  76:    */   {
/*  77:112 */     return this.field_2_color2;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setColor2(int field_2_color2)
/*  81:    */   {
/*  82:121 */     this.field_2_color2 = field_2_color2;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public int getColor3()
/*  86:    */   {
/*  87:130 */     return this.field_3_color3;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setColor3(int field_3_color3)
/*  91:    */   {
/*  92:139 */     this.field_3_color3 = field_3_color3;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public int getColor4()
/*  96:    */   {
/*  97:148 */     return this.field_4_color4;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setColor4(int field_4_color4)
/* 101:    */   {
/* 102:157 */     this.field_4_color4 = field_4_color4;
/* 103:    */   }
/* 104:    */   
/* 105:    */   protected Object[][] getAttributeMap()
/* 106:    */   {
/* 107:162 */     return new Object[][] { { "Color1", Integer.valueOf(this.field_1_color1) }, { "Color2", Integer.valueOf(this.field_2_color2) }, { "Color3", Integer.valueOf(this.field_3_color3) }, { "Color4", Integer.valueOf(this.field_4_color4) } };
/* 108:    */   }
/* 109:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherSplitMenuColorsRecord
 * JD-Core Version:    0.7.0.1
 */