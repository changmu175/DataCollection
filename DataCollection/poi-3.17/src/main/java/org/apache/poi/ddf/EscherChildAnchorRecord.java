/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndian;
/*   4:    */ 
/*   5:    */ public class EscherChildAnchorRecord
/*   6:    */   extends EscherRecord
/*   7:    */ {
/*   8:    */   public static final short RECORD_ID = -4081;
/*   9:    */   public static final String RECORD_DESCRIPTION = "MsofbtChildAnchor";
/*  10:    */   private int field_1_dx1;
/*  11:    */   private int field_2_dy1;
/*  12:    */   private int field_3_dx2;
/*  13:    */   private int field_4_dy2;
/*  14:    */   
/*  15:    */   public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory)
/*  16:    */   {
/*  17: 42 */     int bytesRemaining = readHeader(data, offset);
/*  18: 43 */     int pos = offset + 8;
/*  19: 44 */     int size = 0;
/*  20: 45 */     switch (bytesRemaining)
/*  21:    */     {
/*  22:    */     case 16: 
/*  23: 47 */       this.field_1_dx1 = LittleEndian.getInt(data, pos + size);size += 4;
/*  24: 48 */       this.field_2_dy1 = LittleEndian.getInt(data, pos + size);size += 4;
/*  25: 49 */       this.field_3_dx2 = LittleEndian.getInt(data, pos + size);size += 4;
/*  26: 50 */       this.field_4_dy2 = LittleEndian.getInt(data, pos + size);size += 4;
/*  27: 51 */       break;
/*  28:    */     case 8: 
/*  29: 53 */       this.field_1_dx1 = LittleEndian.getShort(data, pos + size);size += 2;
/*  30: 54 */       this.field_2_dy1 = LittleEndian.getShort(data, pos + size);size += 2;
/*  31: 55 */       this.field_3_dx2 = LittleEndian.getShort(data, pos + size);size += 2;
/*  32: 56 */       this.field_4_dy2 = LittleEndian.getShort(data, pos + size);size += 2;
/*  33: 57 */       break;
/*  34:    */     default: 
/*  35: 59 */       throw new RuntimeException("Invalid EscherChildAnchorRecord - neither 8 nor 16 bytes.");
/*  36:    */     }
/*  37: 62 */     return 8 + size;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  41:    */   {
/*  42: 67 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  43: 68 */     int pos = offset;
/*  44: 69 */     LittleEndian.putShort(data, pos, getOptions());pos += 2;
/*  45: 70 */     LittleEndian.putShort(data, pos, getRecordId());pos += 2;
/*  46: 71 */     LittleEndian.putInt(data, pos, getRecordSize() - 8);pos += 4;
/*  47: 72 */     LittleEndian.putInt(data, pos, this.field_1_dx1);pos += 4;
/*  48: 73 */     LittleEndian.putInt(data, pos, this.field_2_dy1);pos += 4;
/*  49: 74 */     LittleEndian.putInt(data, pos, this.field_3_dx2);pos += 4;
/*  50: 75 */     LittleEndian.putInt(data, pos, this.field_4_dy2);pos += 4;
/*  51:    */     
/*  52: 77 */     listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
/*  53: 78 */     return pos - offset;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public int getRecordSize()
/*  57:    */   {
/*  58: 84 */     return 24;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public short getRecordId()
/*  62:    */   {
/*  63: 89 */     return -4081;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public String getRecordName()
/*  67:    */   {
/*  68: 94 */     return "ChildAnchor";
/*  69:    */   }
/*  70:    */   
/*  71:    */   public int getDx1()
/*  72:    */   {
/*  73:105 */     return this.field_1_dx1;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void setDx1(int field_1_dx1)
/*  77:    */   {
/*  78:115 */     this.field_1_dx1 = field_1_dx1;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public int getDy1()
/*  82:    */   {
/*  83:125 */     return this.field_2_dy1;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setDy1(int field_2_dy1)
/*  87:    */   {
/*  88:135 */     this.field_2_dy1 = field_2_dy1;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public int getDx2()
/*  92:    */   {
/*  93:145 */     return this.field_3_dx2;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void setDx2(int field_3_dx2)
/*  97:    */   {
/*  98:155 */     this.field_3_dx2 = field_3_dx2;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public int getDy2()
/* 102:    */   {
/* 103:165 */     return this.field_4_dy2;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setDy2(int field_4_dy2)
/* 107:    */   {
/* 108:175 */     this.field_4_dy2 = field_4_dy2;
/* 109:    */   }
/* 110:    */   
/* 111:    */   protected Object[][] getAttributeMap()
/* 112:    */   {
/* 113:180 */     return new Object[][] { { "X1", Integer.valueOf(this.field_1_dx1) }, { "Y1", Integer.valueOf(this.field_2_dy1) }, { "X2", Integer.valueOf(this.field_3_dx2) }, { "Y2", Integer.valueOf(this.field_4_dy2) } };
/* 114:    */   }
/* 115:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherChildAnchorRecord
 * JD-Core Version:    0.7.0.1
 */