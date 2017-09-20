/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndian;
/*   4:    */ import org.apache.poi.util.RecordFormatException;
/*   5:    */ 
/*   6:    */ public class EscherSpgrRecord
/*   7:    */   extends EscherRecord
/*   8:    */ {
/*   9:    */   public static final short RECORD_ID = -4087;
/*  10:    */   public static final String RECORD_DESCRIPTION = "MsofbtSpgr";
/*  11:    */   private int field_1_rectX1;
/*  12:    */   private int field_2_rectY1;
/*  13:    */   private int field_3_rectX2;
/*  14:    */   private int field_4_rectY2;
/*  15:    */   
/*  16:    */   public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory)
/*  17:    */   {
/*  18: 40 */     int bytesRemaining = readHeader(data, offset);
/*  19: 41 */     int pos = offset + 8;
/*  20: 42 */     int size = 0;
/*  21: 43 */     this.field_1_rectX1 = LittleEndian.getInt(data, pos + size);size += 4;
/*  22: 44 */     this.field_2_rectY1 = LittleEndian.getInt(data, pos + size);size += 4;
/*  23: 45 */     this.field_3_rectX2 = LittleEndian.getInt(data, pos + size);size += 4;
/*  24: 46 */     this.field_4_rectY2 = LittleEndian.getInt(data, pos + size);size += 4;
/*  25: 47 */     bytesRemaining -= size;
/*  26: 48 */     if (bytesRemaining != 0) {
/*  27: 49 */       throw new RecordFormatException("Expected no remaining bytes but got " + bytesRemaining);
/*  28:    */     }
/*  29: 53 */     return 8 + size + bytesRemaining;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  33:    */   {
/*  34: 59 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  35: 60 */     LittleEndian.putShort(data, offset, getOptions());
/*  36: 61 */     LittleEndian.putShort(data, offset + 2, getRecordId());
/*  37: 62 */     int remainingBytes = 16;
/*  38: 63 */     LittleEndian.putInt(data, offset + 4, remainingBytes);
/*  39: 64 */     LittleEndian.putInt(data, offset + 8, this.field_1_rectX1);
/*  40: 65 */     LittleEndian.putInt(data, offset + 12, this.field_2_rectY1);
/*  41: 66 */     LittleEndian.putInt(data, offset + 16, this.field_3_rectX2);
/*  42: 67 */     LittleEndian.putInt(data, offset + 20, this.field_4_rectY2);
/*  43:    */     
/*  44:    */ 
/*  45: 70 */     listener.afterRecordSerialize(offset + getRecordSize(), getRecordId(), offset + getRecordSize(), this);
/*  46: 71 */     return 24;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public int getRecordSize()
/*  50:    */   {
/*  51: 77 */     return 24;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public short getRecordId()
/*  55:    */   {
/*  56: 82 */     return -4087;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public String getRecordName()
/*  60:    */   {
/*  61: 87 */     return "Spgr";
/*  62:    */   }
/*  63:    */   
/*  64:    */   public int getRectX1()
/*  65:    */   {
/*  66: 97 */     return this.field_1_rectX1;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setRectX1(int x1)
/*  70:    */   {
/*  71:107 */     this.field_1_rectX1 = x1;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public int getRectY1()
/*  75:    */   {
/*  76:117 */     return this.field_2_rectY1;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setRectY1(int y1)
/*  80:    */   {
/*  81:127 */     this.field_2_rectY1 = y1;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public int getRectX2()
/*  85:    */   {
/*  86:137 */     return this.field_3_rectX2;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setRectX2(int x2)
/*  90:    */   {
/*  91:147 */     this.field_3_rectX2 = x2;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public int getRectY2()
/*  95:    */   {
/*  96:157 */     return this.field_4_rectY2;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void setRectY2(int rectY2)
/* 100:    */   {
/* 101:166 */     this.field_4_rectY2 = rectY2;
/* 102:    */   }
/* 103:    */   
/* 104:    */   protected Object[][] getAttributeMap()
/* 105:    */   {
/* 106:171 */     return new Object[][] { { "RectX", Integer.valueOf(this.field_1_rectX1) }, { "RectY", Integer.valueOf(this.field_2_rectY1) }, { "RectWidth", Integer.valueOf(this.field_3_rectX2) }, { "RectHeight", Integer.valueOf(this.field_4_rectY2) } };
/* 107:    */   }
/* 108:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherSpgrRecord
 * JD-Core Version:    0.7.0.1
 */