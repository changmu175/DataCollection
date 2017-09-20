/*   1:    */ package org.apache.poi.hssf.record.cf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.common.ExtendedColor;
/*   4:    */ import org.apache.poi.util.BitField;
/*   5:    */ import org.apache.poi.util.BitFieldFactory;
/*   6:    */ import org.apache.poi.util.LittleEndianInput;
/*   7:    */ import org.apache.poi.util.LittleEndianOutput;
/*   8:    */ import org.apache.poi.util.POILogFactory;
/*   9:    */ import org.apache.poi.util.POILogger;
/*  10:    */ 
/*  11:    */ public final class ColorGradientFormatting
/*  12:    */   implements Cloneable
/*  13:    */ {
/*  14: 34 */   private static POILogger log = POILogFactory.getLogger(ColorGradientFormatting.class);
/*  15: 36 */   private byte options = 0;
/*  16:    */   private ColorGradientThreshold[] thresholds;
/*  17:    */   private ExtendedColor[] colors;
/*  18: 40 */   private static BitField clamp = BitFieldFactory.getInstance(1);
/*  19: 41 */   private static BitField background = BitFieldFactory.getInstance(2);
/*  20:    */   
/*  21:    */   public ColorGradientFormatting()
/*  22:    */   {
/*  23: 44 */     this.options = 3;
/*  24: 45 */     this.thresholds = new ColorGradientThreshold[3];
/*  25: 46 */     this.colors = new ExtendedColor[3];
/*  26:    */   }
/*  27:    */   
/*  28:    */   public ColorGradientFormatting(LittleEndianInput in)
/*  29:    */   {
/*  30: 49 */     in.readShort();
/*  31: 50 */     in.readByte();
/*  32: 51 */     int numI = in.readByte();
/*  33: 52 */     int numG = in.readByte();
/*  34: 53 */     if (numI != numG) {
/*  35: 54 */       log.log(5, new Object[] { "Inconsistent Color Gradient defintion, found " + numI + " vs " + numG + " entries" });
/*  36:    */     }
/*  37: 56 */     this.options = in.readByte();
/*  38:    */     
/*  39: 58 */     this.thresholds = new ColorGradientThreshold[numI];
/*  40: 59 */     for (int i = 0; i < this.thresholds.length; i++) {
/*  41: 60 */       this.thresholds[i] = new ColorGradientThreshold(in);
/*  42:    */     }
/*  43: 62 */     this.colors = new ExtendedColor[numG];
/*  44: 63 */     for (int i = 0; i < this.colors.length; i++)
/*  45:    */     {
/*  46: 64 */       in.readDouble();
/*  47: 65 */       this.colors[i] = new ExtendedColor(in);
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   public int getNumControlPoints()
/*  52:    */   {
/*  53: 70 */     return this.thresholds.length;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setNumControlPoints(int num)
/*  57:    */   {
/*  58: 73 */     if (num != this.thresholds.length)
/*  59:    */     {
/*  60: 74 */       ColorGradientThreshold[] nt = new ColorGradientThreshold[num];
/*  61: 75 */       ExtendedColor[] nc = new ExtendedColor[num];
/*  62:    */       
/*  63: 77 */       int copy = Math.min(this.thresholds.length, num);
/*  64: 78 */       System.arraycopy(this.thresholds, 0, nt, 0, copy);
/*  65: 79 */       System.arraycopy(this.colors, 0, nc, 0, copy);
/*  66:    */       
/*  67: 81 */       this.thresholds = nt;
/*  68: 82 */       this.colors = nc;
/*  69:    */       
/*  70: 84 */       updateThresholdPositions();
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   public ColorGradientThreshold[] getThresholds()
/*  75:    */   {
/*  76: 89 */     return this.thresholds;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setThresholds(ColorGradientThreshold[] thresholds)
/*  80:    */   {
/*  81: 92 */     this.thresholds = (thresholds == null ? null : (ColorGradientThreshold[])thresholds.clone());
/*  82: 93 */     updateThresholdPositions();
/*  83:    */   }
/*  84:    */   
/*  85:    */   public ExtendedColor[] getColors()
/*  86:    */   {
/*  87: 97 */     return this.colors;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setColors(ExtendedColor[] colors)
/*  91:    */   {
/*  92:100 */     this.colors = (colors == null ? null : (ExtendedColor[])colors.clone());
/*  93:    */   }
/*  94:    */   
/*  95:    */   public boolean isClampToCurve()
/*  96:    */   {
/*  97:104 */     return getOptionFlag(clamp);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public boolean isAppliesToBackground()
/* 101:    */   {
/* 102:107 */     return getOptionFlag(background);
/* 103:    */   }
/* 104:    */   
/* 105:    */   private boolean getOptionFlag(BitField field)
/* 106:    */   {
/* 107:110 */     int value = field.getValue(this.options);
/* 108:111 */     return value != 0;
/* 109:    */   }
/* 110:    */   
/* 111:    */   private void updateThresholdPositions()
/* 112:    */   {
/* 113:115 */     double step = 1.0D / (this.thresholds.length - 1);
/* 114:116 */     for (int i = 0; i < this.thresholds.length; i++) {
/* 115:117 */       this.thresholds[i].setPosition(step * i);
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   public String toString()
/* 120:    */   {
/* 121:122 */     StringBuffer buffer = new StringBuffer();
/* 122:123 */     buffer.append("    [Color Gradient Formatting]\n");
/* 123:124 */     buffer.append("          .clamp     = ").append(isClampToCurve()).append("\n");
/* 124:125 */     buffer.append("          .background= ").append(isAppliesToBackground()).append("\n");
/* 125:126 */     for (Threshold t : this.thresholds) {
/* 126:127 */       buffer.append(t);
/* 127:    */     }
/* 128:129 */     for (ExtendedColor c : this.colors) {
/* 129:130 */       buffer.append(c);
/* 130:    */     }
/* 131:132 */     buffer.append("    [/Color Gradient Formatting]\n");
/* 132:133 */     return buffer.toString();
/* 133:    */   }
/* 134:    */   
/* 135:    */   public Object clone()
/* 136:    */   {
/* 137:137 */     ColorGradientFormatting rec = new ColorGradientFormatting();
/* 138:138 */     rec.options = this.options;
/* 139:139 */     rec.thresholds = new ColorGradientThreshold[this.thresholds.length];
/* 140:140 */     rec.colors = new ExtendedColor[this.colors.length];
/* 141:141 */     System.arraycopy(this.thresholds, 0, rec.thresholds, 0, this.thresholds.length);
/* 142:142 */     System.arraycopy(this.colors, 0, rec.colors, 0, this.colors.length);
/* 143:143 */     return rec;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public int getDataLength()
/* 147:    */   {
/* 148:147 */     int len = 6;
/* 149:148 */     for (Threshold t : this.thresholds) {
/* 150:149 */       len += t.getDataLength();
/* 151:    */     }
/* 152:151 */     for (ExtendedColor c : this.colors)
/* 153:    */     {
/* 154:152 */       len += c.getDataLength();
/* 155:153 */       len += 8;
/* 156:    */     }
/* 157:155 */     return len;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public void serialize(LittleEndianOutput out)
/* 161:    */   {
/* 162:159 */     out.writeShort(0);
/* 163:160 */     out.writeByte(0);
/* 164:161 */     out.writeByte(this.thresholds.length);
/* 165:162 */     out.writeByte(this.thresholds.length);
/* 166:163 */     out.writeByte(this.options);
/* 167:165 */     for (ColorGradientThreshold t : this.thresholds) {
/* 168:166 */       t.serialize(out);
/* 169:    */     }
/* 170:169 */     double step = 1.0D / (this.colors.length - 1);
/* 171:170 */     for (int i = 0; i < this.colors.length; i++)
/* 172:    */     {
/* 173:171 */       out.writeDouble(i * step);
/* 174:    */       
/* 175:173 */       ExtendedColor c = this.colors[i];
/* 176:174 */       c.serialize(out);
/* 177:    */     }
/* 178:    */   }
/* 179:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.cf.ColorGradientFormatting
 * JD-Core Version:    0.7.0.1
 */