/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import org.apache.poi.hssf.util.HSSFColor;
/*   5:    */ 
/*   6:    */ public class HSSFExtendedColor
/*   7:    */   extends org.apache.poi.ss.usermodel.ExtendedColor
/*   8:    */ {
/*   9:    */   private org.apache.poi.hssf.record.common.ExtendedColor color;
/*  10:    */   
/*  11:    */   public HSSFExtendedColor(org.apache.poi.hssf.record.common.ExtendedColor color)
/*  12:    */   {
/*  13: 38 */     this.color = color;
/*  14:    */   }
/*  15:    */   
/*  16:    */   protected org.apache.poi.hssf.record.common.ExtendedColor getExtendedColor()
/*  17:    */   {
/*  18: 42 */     return this.color;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public boolean isAuto()
/*  22:    */   {
/*  23: 46 */     return this.color.getType() == 0;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public boolean isIndexed()
/*  27:    */   {
/*  28: 49 */     return this.color.getType() == 1;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public boolean isRGB()
/*  32:    */   {
/*  33: 52 */     return this.color.getType() == 2;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public boolean isThemed()
/*  37:    */   {
/*  38: 55 */     return this.color.getType() == 3;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public short getIndex()
/*  42:    */   {
/*  43: 59 */     return (short)this.color.getColorIndex();
/*  44:    */   }
/*  45:    */   
/*  46:    */   public int getTheme()
/*  47:    */   {
/*  48: 62 */     return this.color.getThemeIndex();
/*  49:    */   }
/*  50:    */   
/*  51:    */   public byte[] getRGB()
/*  52:    */   {
/*  53: 67 */     byte[] rgb = new byte[3];
/*  54: 68 */     byte[] rgba = this.color.getRGBA();
/*  55: 69 */     if (rgba == null) {
/*  56: 69 */       return null;
/*  57:    */     }
/*  58: 70 */     System.arraycopy(rgba, 0, rgb, 0, 3);
/*  59: 71 */     return rgb;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public byte[] getARGB()
/*  63:    */   {
/*  64: 75 */     byte[] argb = new byte[4];
/*  65: 76 */     byte[] rgba = this.color.getRGBA();
/*  66: 77 */     if (rgba == null) {
/*  67: 77 */       return null;
/*  68:    */     }
/*  69: 78 */     System.arraycopy(rgba, 0, argb, 1, 3);
/*  70: 79 */     argb[0] = rgba[3];
/*  71: 80 */     return argb;
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected byte[] getStoredRBG()
/*  75:    */   {
/*  76: 84 */     return getARGB();
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setRGB(byte[] rgb)
/*  80:    */   {
/*  81: 88 */     if (rgb.length == 3)
/*  82:    */     {
/*  83: 89 */       byte[] rgba = new byte[4];
/*  84: 90 */       System.arraycopy(rgb, 0, rgba, 0, 3);
/*  85: 91 */       rgba[3] = -1;
/*  86:    */     }
/*  87:    */     else
/*  88:    */     {
/*  89: 94 */       byte a = rgb[0];
/*  90: 95 */       rgb[0] = rgb[1];
/*  91: 96 */       rgb[1] = rgb[2];
/*  92: 97 */       rgb[2] = rgb[3];
/*  93: 98 */       rgb[3] = a;
/*  94: 99 */       this.color.setRGBA(rgb);
/*  95:    */     }
/*  96:101 */     this.color.setType(2);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public double getTint()
/* 100:    */   {
/* 101:105 */     return this.color.getTint();
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setTint(double tint)
/* 105:    */   {
/* 106:108 */     this.color.setTint(tint);
/* 107:    */   }
/* 108:    */   
/* 109:    */   protected byte[] getIndexedRGB()
/* 110:    */   {
/* 111:112 */     if ((isIndexed()) && (getIndex() > 0))
/* 112:    */     {
/* 113:113 */       int indexNum = getIndex();
/* 114:114 */       HSSFColor indexed = (HSSFColor)HSSFColor.getIndexHash().get(Integer.valueOf(indexNum));
/* 115:115 */       if (indexed != null)
/* 116:    */       {
/* 117:116 */         byte[] rgb = new byte[3];
/* 118:117 */         rgb[0] = ((byte)indexed.getTriplet()[0]);
/* 119:118 */         rgb[1] = ((byte)indexed.getTriplet()[1]);
/* 120:119 */         rgb[2] = ((byte)indexed.getTriplet()[2]);
/* 121:120 */         return rgb;
/* 122:    */       }
/* 123:    */     }
/* 124:123 */     return null;
/* 125:    */   }
/* 126:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFExtendedColor
 * JD-Core Version:    0.7.0.1
 */