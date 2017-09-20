/*   1:    */ package org.apache.poi.ss.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ 
/*   5:    */ public abstract class ExtendedColor
/*   6:    */   implements Color
/*   7:    */ {
/*   8:    */   protected void setColor(java.awt.Color clr)
/*   9:    */   {
/*  10: 33 */     setRGB(new byte[] { (byte)clr.getRed(), (byte)clr.getGreen(), (byte)clr.getBlue() });
/*  11:    */   }
/*  12:    */   
/*  13:    */   public abstract boolean isAuto();
/*  14:    */   
/*  15:    */   public abstract boolean isIndexed();
/*  16:    */   
/*  17:    */   public abstract boolean isRGB();
/*  18:    */   
/*  19:    */   public abstract boolean isThemed();
/*  20:    */   
/*  21:    */   public abstract short getIndex();
/*  22:    */   
/*  23:    */   public abstract int getTheme();
/*  24:    */   
/*  25:    */   public abstract byte[] getRGB();
/*  26:    */   
/*  27:    */   public abstract byte[] getARGB();
/*  28:    */   
/*  29:    */   protected abstract byte[] getStoredRBG();
/*  30:    */   
/*  31:    */   public abstract void setRGB(byte[] paramArrayOfByte);
/*  32:    */   
/*  33:    */   protected byte[] getRGBOrARGB()
/*  34:    */   {
/*  35: 92 */     if ((isIndexed()) && (getIndex() > 0))
/*  36:    */     {
/*  37: 93 */       byte[] rgb = getIndexedRGB();
/*  38: 94 */       if (rgb != null) {
/*  39: 95 */         return rgb;
/*  40:    */       }
/*  41:    */     }
/*  42:100 */     return getStoredRBG();
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected abstract byte[] getIndexedRGB();
/*  46:    */   
/*  47:    */   public byte[] getRGBWithTint()
/*  48:    */   {
/*  49:113 */     byte[] rgb = getStoredRBG();
/*  50:114 */     if (rgb != null)
/*  51:    */     {
/*  52:115 */       if (rgb.length == 4)
/*  53:    */       {
/*  54:116 */         byte[] tmp = new byte[3];
/*  55:117 */         System.arraycopy(rgb, 1, tmp, 0, 3);
/*  56:118 */         rgb = tmp;
/*  57:    */       }
/*  58:120 */       double tint = getTint();
/*  59:121 */       for (int i = 0; i < rgb.length; i++) {
/*  60:122 */         rgb[i] = applyTint(rgb[i] & 0xFF, tint);
/*  61:    */       }
/*  62:    */     }
/*  63:125 */     return rgb;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public String getARGBHex()
/*  67:    */   {
/*  68:133 */     byte[] rgb = getARGB();
/*  69:134 */     if (rgb == null) {
/*  70:135 */       return null;
/*  71:    */     }
/*  72:138 */     StringBuilder sb = new StringBuilder();
/*  73:139 */     for (byte c : rgb)
/*  74:    */     {
/*  75:140 */       int i = c & 0xFF;
/*  76:141 */       String cs = Integer.toHexString(i);
/*  77:142 */       if (cs.length() == 1) {
/*  78:143 */         sb.append('0');
/*  79:    */       }
/*  80:145 */       sb.append(cs);
/*  81:    */     }
/*  82:147 */     return sb.toString().toUpperCase(Locale.ROOT);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setARGBHex(String argb)
/*  86:    */   {
/*  87:156 */     if ((argb.length() == 6) || (argb.length() == 8))
/*  88:    */     {
/*  89:157 */       byte[] rgb = new byte[argb.length() / 2];
/*  90:158 */       for (int i = 0; i < rgb.length; i++)
/*  91:    */       {
/*  92:159 */         String part = argb.substring(i * 2, (i + 1) * 2);
/*  93:160 */         rgb[i] = ((byte)Integer.parseInt(part, 16));
/*  94:    */       }
/*  95:162 */       setRGB(rgb);
/*  96:    */     }
/*  97:    */     else
/*  98:    */     {
/*  99:164 */       throw new IllegalArgumentException("Must be of the form 112233 or FFEEDDCC");
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   private static byte applyTint(int lum, double tint)
/* 104:    */   {
/* 105:169 */     if (tint > 0.0D) {
/* 106:170 */       return (byte)(int)(lum * (1.0D - tint) + (255.0D - 255.0D * (1.0D - tint)));
/* 107:    */     }
/* 108:171 */     if (tint < 0.0D) {
/* 109:172 */       return (byte)(int)(lum * (1.0D + tint));
/* 110:    */     }
/* 111:174 */     return (byte)lum;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public abstract double getTint();
/* 115:    */   
/* 116:    */   public abstract void setTint(double paramDouble);
/* 117:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.ExtendedColor
 * JD-Core Version:    0.7.0.1
 */