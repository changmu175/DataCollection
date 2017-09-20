/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.BitField;
/*   4:    */ import org.apache.poi.util.LittleEndian;
/*   5:    */ 
/*   6:    */ public class EscherColorRef
/*   7:    */ {
/*   8: 27 */   private int opid = -1;
/*   9: 29 */   private int colorRef = 0;
/*  10:    */   
/*  11:    */   public static enum SysIndexSource
/*  12:    */   {
/*  13: 33 */     FILL_COLOR(240),  LINE_OR_FILL_COLOR(241),  LINE_COLOR(242),  SHADOW_COLOR(243),  CURRENT_OR_LAST_COLOR(244),  FILL_BACKGROUND_COLOR(245),  LINE_BACKGROUND_COLOR(246),  FILL_OR_LINE_COLOR(247);
/*  14:    */     
/*  15:    */     private int value;
/*  16:    */     
/*  17:    */     private SysIndexSource(int value)
/*  18:    */     {
/*  19: 50 */       this.value = value;
/*  20:    */     }
/*  21:    */   }
/*  22:    */   
/*  23:    */   public static enum SysIndexProcedure
/*  24:    */   {
/*  25: 65 */     DARKEN_COLOR(1),  LIGHTEN_COLOR(2),  ADD_GRAY_LEVEL(3),  SUB_GRAY_LEVEL(4),  REVERSE_GRAY_LEVEL(5),  THRESHOLD(6),  INVERT_AFTER(32),  INVERT_HIGHBIT_AFTER(64);
/*  26:    */     
/*  27:    */     private BitField mask;
/*  28:    */     
/*  29:    */     private SysIndexProcedure(int mask)
/*  30:    */     {
/*  31:108 */       this.mask = new BitField(mask);
/*  32:    */     }
/*  33:    */   }
/*  34:    */   
/*  35:117 */   private static final BitField FLAG_SYS_INDEX = new BitField(268435456);
/*  36:124 */   private static final BitField FLAG_SCHEME_INDEX = new BitField(134217728);
/*  37:131 */   private static final BitField FLAG_SYSTEM_RGB = new BitField(67108864);
/*  38:138 */   private static final BitField FLAG_PALETTE_RGB = new BitField(33554432);
/*  39:145 */   private static final BitField FLAG_PALETTE_INDEX = new BitField(16777216);
/*  40:151 */   private static final BitField FLAG_BLUE = new BitField(16711680);
/*  41:157 */   private static final BitField FLAG_GREEN = new BitField(65280);
/*  42:163 */   private static final BitField FLAG_RED = new BitField(255);
/*  43:    */   
/*  44:    */   public EscherColorRef(int colorRef)
/*  45:    */   {
/*  46:166 */     this.colorRef = colorRef;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public EscherColorRef(byte[] source, int start, int len)
/*  50:    */   {
/*  51:170 */     assert ((len == 4) || (len == 6));
/*  52:    */     
/*  53:172 */     int offset = start;
/*  54:173 */     if (len == 6)
/*  55:    */     {
/*  56:174 */       this.opid = LittleEndian.getUShort(source, offset);
/*  57:175 */       offset += 2;
/*  58:    */     }
/*  59:177 */     this.colorRef = LittleEndian.getInt(source, offset);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public boolean hasSysIndexFlag()
/*  63:    */   {
/*  64:181 */     return FLAG_SYS_INDEX.isSet(this.colorRef);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setSysIndexFlag(boolean flag)
/*  68:    */   {
/*  69:185 */     this.colorRef = FLAG_SYS_INDEX.setBoolean(this.colorRef, flag);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public boolean hasSchemeIndexFlag()
/*  73:    */   {
/*  74:189 */     return FLAG_SCHEME_INDEX.isSet(this.colorRef);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setSchemeIndexFlag(boolean flag)
/*  78:    */   {
/*  79:193 */     this.colorRef = FLAG_SCHEME_INDEX.setBoolean(this.colorRef, flag);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public boolean hasSystemRGBFlag()
/*  83:    */   {
/*  84:197 */     return FLAG_SYSTEM_RGB.isSet(this.colorRef);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setSystemRGBFlag(boolean flag)
/*  88:    */   {
/*  89:201 */     this.colorRef = FLAG_SYSTEM_RGB.setBoolean(this.colorRef, flag);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public boolean hasPaletteRGBFlag()
/*  93:    */   {
/*  94:205 */     return FLAG_PALETTE_RGB.isSet(this.colorRef);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void setPaletteRGBFlag(boolean flag)
/*  98:    */   {
/*  99:209 */     this.colorRef = FLAG_PALETTE_RGB.setBoolean(this.colorRef, flag);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public boolean hasPaletteIndexFlag()
/* 103:    */   {
/* 104:213 */     return FLAG_PALETTE_INDEX.isSet(this.colorRef);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void setPaletteIndexFlag(boolean flag)
/* 108:    */   {
/* 109:217 */     this.colorRef = FLAG_PALETTE_INDEX.setBoolean(this.colorRef, flag);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public int[] getRGB()
/* 113:    */   {
/* 114:221 */     int[] rgb = { FLAG_RED.getValue(this.colorRef), FLAG_GREEN.getValue(this.colorRef), FLAG_BLUE.getValue(this.colorRef) };
/* 115:    */     
/* 116:    */ 
/* 117:    */ 
/* 118:    */ 
/* 119:226 */     return rgb;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public SysIndexSource getSysIndexSource()
/* 123:    */   {
/* 124:233 */     if (!hasSysIndexFlag()) {
/* 125:234 */       return null;
/* 126:    */     }
/* 127:236 */     int val = FLAG_RED.getValue(this.colorRef);
/* 128:237 */     for (SysIndexSource sis : SysIndexSource.values()) {
/* 129:238 */       if (sis.value == val) {
/* 130:239 */         return sis;
/* 131:    */       }
/* 132:    */     }
/* 133:242 */     return null;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public SysIndexProcedure getSysIndexProcedure()
/* 137:    */   {
/* 138:250 */     if (!hasSysIndexFlag()) {
/* 139:251 */       return null;
/* 140:    */     }
/* 141:253 */     int val = FLAG_GREEN.getValue(this.colorRef);
/* 142:254 */     for (SysIndexProcedure sip : SysIndexProcedure.values()) {
/* 143:255 */       if ((sip != SysIndexProcedure.INVERT_AFTER) && (sip != SysIndexProcedure.INVERT_HIGHBIT_AFTER)) {
/* 144:258 */         if (sip.mask.isSet(val)) {
/* 145:259 */           return sip;
/* 146:    */         }
/* 147:    */       }
/* 148:    */     }
/* 149:262 */     return null;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public int getSysIndexInvert()
/* 153:    */   {
/* 154:270 */     if (!hasSysIndexFlag()) {
/* 155:271 */       return 0;
/* 156:    */     }
/* 157:273 */     int val = FLAG_GREEN.getValue(this.colorRef);
/* 158:274 */     if (SysIndexProcedure.INVERT_AFTER.mask.isSet(val)) {
/* 159:275 */       return 1;
/* 160:    */     }
/* 161:277 */     if (SysIndexProcedure.INVERT_HIGHBIT_AFTER.mask.isSet(val)) {
/* 162:278 */       return 2;
/* 163:    */     }
/* 164:280 */     return 0;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public int getSchemeIndex()
/* 168:    */   {
/* 169:289 */     if (!hasSchemeIndexFlag()) {
/* 170:290 */       return -1;
/* 171:    */     }
/* 172:292 */     return FLAG_RED.getValue(this.colorRef);
/* 173:    */   }
/* 174:    */   
/* 175:    */   public int getPaletteIndex()
/* 176:    */   {
/* 177:299 */     return hasPaletteIndexFlag() ? getIndex() : -1;
/* 178:    */   }
/* 179:    */   
/* 180:    */   public int getSysIndex()
/* 181:    */   {
/* 182:308 */     return hasSysIndexFlag() ? getIndex() : -1;
/* 183:    */   }
/* 184:    */   
/* 185:    */   private int getIndex()
/* 186:    */   {
/* 187:312 */     return FLAG_GREEN.getValue(this.colorRef) << 8 | FLAG_RED.getValue(this.colorRef);
/* 188:    */   }
/* 189:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherColorRef
 * JD-Core Version:    0.7.0.1
 */