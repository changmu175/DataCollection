/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ 
/*   7:    */ public final class PaletteRecord
/*   8:    */   extends StandardRecord
/*   9:    */ {
/*  10:    */   public static final short sid = 146;
/*  11:    */   public static final byte STANDARD_PALETTE_SIZE = 56;
/*  12:    */   public static final short FIRST_COLOR_INDEX = 8;
/*  13:    */   private final List<PColor> _colors;
/*  14:    */   
/*  15:    */   public PaletteRecord()
/*  16:    */   {
/*  17: 41 */     PColor[] defaultPalette = createDefaultPalette();
/*  18: 42 */     this._colors = new ArrayList(defaultPalette.length);
/*  19: 43 */     for (PColor element : defaultPalette) {
/*  20: 44 */       this._colors.add(element);
/*  21:    */     }
/*  22:    */   }
/*  23:    */   
/*  24:    */   public PaletteRecord(RecordInputStream in)
/*  25:    */   {
/*  26: 49 */     int field_1_numcolors = in.readShort();
/*  27: 50 */     this._colors = new ArrayList(field_1_numcolors);
/*  28: 51 */     for (int k = 0; k < field_1_numcolors; k++) {
/*  29: 52 */       this._colors.add(new PColor(in));
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String toString()
/*  34:    */   {
/*  35: 58 */     StringBuffer buffer = new StringBuffer();
/*  36:    */     
/*  37: 60 */     buffer.append("[PALETTE]\n");
/*  38: 61 */     buffer.append("  numcolors     = ").append(this._colors.size()).append('\n');
/*  39: 62 */     for (int i = 0; i < this._colors.size(); i++)
/*  40:    */     {
/*  41: 63 */       PColor c = (PColor)this._colors.get(i);
/*  42: 64 */       buffer.append("* colornum      = ").append(i).append('\n');
/*  43: 65 */       buffer.append(c);
/*  44: 66 */       buffer.append("/*colornum      = ").append(i).append('\n');
/*  45:    */     }
/*  46: 68 */     buffer.append("[/PALETTE]\n");
/*  47: 69 */     return buffer.toString();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void serialize(LittleEndianOutput out)
/*  51:    */   {
/*  52: 74 */     out.writeShort(this._colors.size());
/*  53: 75 */     for (int i = 0; i < this._colors.size(); i++) {
/*  54: 76 */       ((PColor)this._colors.get(i)).serialize(out);
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected int getDataSize()
/*  59:    */   {
/*  60: 82 */     return 2 + this._colors.size() * 4;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public short getSid()
/*  64:    */   {
/*  65: 87 */     return 146;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public byte[] getColor(int byteIndex)
/*  69:    */   {
/*  70: 99 */     int i = byteIndex - 8;
/*  71:100 */     if ((i < 0) || (i >= this._colors.size())) {
/*  72:101 */       return null;
/*  73:    */     }
/*  74:103 */     return ((PColor)this._colors.get(i)).getTriplet();
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setColor(short byteIndex, byte red, byte green, byte blue)
/*  78:    */   {
/*  79:120 */     int i = byteIndex - 8;
/*  80:121 */     if ((i < 0) || (i >= 56)) {
/*  81:123 */       return;
/*  82:    */     }
/*  83:126 */     while (this._colors.size() <= i) {
/*  84:127 */       this._colors.add(new PColor(0, 0, 0));
/*  85:    */     }
/*  86:129 */     PColor custColor = new PColor(red, green, blue);
/*  87:130 */     this._colors.set(i, custColor);
/*  88:    */   }
/*  89:    */   
/*  90:    */   private static PColor[] createDefaultPalette()
/*  91:    */   {
/*  92:138 */     return new PColor[] { pc(0, 0, 0), pc(255, 255, 255), pc(255, 0, 0), pc(0, 255, 0), pc(0, 0, 255), pc(255, 255, 0), pc(255, 0, 255), pc(0, 255, 255), pc(128, 0, 0), pc(0, 128, 0), pc(0, 0, 128), pc(128, 128, 0), pc(128, 0, 128), pc(0, 128, 128), pc(192, 192, 192), pc(128, 128, 128), pc(153, 153, 255), pc(153, 51, 102), pc(255, 255, 204), pc(204, 255, 255), pc(102, 0, 102), pc(255, 128, 128), pc(0, 102, 204), pc(204, 204, 255), pc(0, 0, 128), pc(255, 0, 255), pc(255, 255, 0), pc(0, 255, 255), pc(128, 0, 128), pc(128, 0, 0), pc(0, 128, 128), pc(0, 0, 255), pc(0, 204, 255), pc(204, 255, 255), pc(204, 255, 204), pc(255, 255, 153), pc(153, 204, 255), pc(255, 153, 204), pc(204, 153, 255), pc(255, 204, 153), pc(51, 102, 255), pc(51, 204, 204), pc(153, 204, 0), pc(255, 204, 0), pc(255, 153, 0), pc(255, 102, 0), pc(102, 102, 153), pc(150, 150, 150), pc(0, 51, 102), pc(51, 153, 102), pc(0, 51, 0), pc(51, 51, 0), pc(153, 51, 0), pc(153, 51, 102), pc(51, 51, 153), pc(51, 51, 51) };
/*  93:    */   }
/*  94:    */   
/*  95:    */   private static PColor pc(int r, int g, int b)
/*  96:    */   {
/*  97:199 */     return new PColor(r, g, b);
/*  98:    */   }
/*  99:    */   
/* 100:    */   private static final class PColor
/* 101:    */   {
/* 102:    */     public static final short ENCODED_SIZE = 4;
/* 103:    */     private final int _red;
/* 104:    */     private final int _green;
/* 105:    */     private final int _blue;
/* 106:    */     
/* 107:    */     public PColor(int red, int green, int blue)
/* 108:    */     {
/* 109:212 */       this._red = red;
/* 110:213 */       this._green = green;
/* 111:214 */       this._blue = blue;
/* 112:    */     }
/* 113:    */     
/* 114:    */     public byte[] getTriplet()
/* 115:    */     {
/* 116:218 */       return new byte[] { (byte)this._red, (byte)this._green, (byte)this._blue };
/* 117:    */     }
/* 118:    */     
/* 119:    */     public PColor(RecordInputStream in)
/* 120:    */     {
/* 121:222 */       this._red = in.readByte();
/* 122:223 */       this._green = in.readByte();
/* 123:224 */       this._blue = in.readByte();
/* 124:225 */       in.readByte();
/* 125:    */     }
/* 126:    */     
/* 127:    */     public void serialize(LittleEndianOutput out)
/* 128:    */     {
/* 129:229 */       out.writeByte(this._red);
/* 130:230 */       out.writeByte(this._green);
/* 131:231 */       out.writeByte(this._blue);
/* 132:232 */       out.writeByte(0);
/* 133:    */     }
/* 134:    */     
/* 135:    */     public String toString()
/* 136:    */     {
/* 137:237 */       StringBuffer buffer = new StringBuffer();
/* 138:238 */       buffer.append("  red   = ").append(this._red & 0xFF).append('\n');
/* 139:239 */       buffer.append("  green = ").append(this._green & 0xFF).append('\n');
/* 140:240 */       buffer.append("  blue  = ").append(this._blue & 0xFF).append('\n');
/* 141:241 */       return buffer.toString();
/* 142:    */     }
/* 143:    */   }
/* 144:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.PaletteRecord
 * JD-Core Version:    0.7.0.1
 */