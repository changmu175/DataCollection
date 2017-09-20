/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ public class Units
/*   4:    */ {
/*   5:    */   public static final int EMU_PER_PIXEL = 9525;
/*   6:    */   public static final int EMU_PER_POINT = 12700;
/*   7:    */   public static final int EMU_PER_CENTIMETER = 360000;
/*   8:    */   public static final int MASTER_DPI = 576;
/*   9:    */   public static final int PIXEL_DPI = 96;
/*  10:    */   public static final int POINT_DPI = 72;
/*  11:    */   public static final float DEFAULT_CHARACTER_WIDTH = 7.0017F;
/*  12:    */   public static final int EMU_PER_CHARACTER = 66691;
/*  13:    */   
/*  14:    */   public static int toEMU(double points)
/*  15:    */   {
/*  16: 73 */     return (int)Math.rint(12700.0D * points);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public static int pixelToEMU(int pixels)
/*  20:    */   {
/*  21: 82 */     return pixels * 9525;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public static double toPoints(long emu)
/*  25:    */   {
/*  26: 91 */     return emu / 12700.0D;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static double fixedPointToDouble(int fixedPoint)
/*  30:    */   {
/*  31:103 */     int i = fixedPoint >> 16;
/*  32:104 */     int f = fixedPoint & 0xFFFF;
/*  33:105 */     return i + f / 65536.0D;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static int doubleToFixedPoint(double floatPoint)
/*  37:    */   {
/*  38:117 */     double fractionalPart = floatPoint % 1.0D;
/*  39:118 */     double integralPart = floatPoint - fractionalPart;
/*  40:119 */     int i = (int)Math.floor(integralPart);
/*  41:120 */     int f = (int)Math.rint(fractionalPart * 65536.0D);
/*  42:121 */     return i << 16 | f & 0xFFFF;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public static double masterToPoints(int masterDPI)
/*  46:    */   {
/*  47:125 */     double points = masterDPI;
/*  48:126 */     points *= 72.0D;
/*  49:127 */     points /= 576.0D;
/*  50:128 */     return points;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public static int pointsToMaster(double points)
/*  54:    */   {
/*  55:132 */     points *= 576.0D;
/*  56:133 */     points /= 72.0D;
/*  57:134 */     return (int)Math.rint(points);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public static int pointsToPixel(double points)
/*  61:    */   {
/*  62:138 */     points *= 96.0D;
/*  63:139 */     points /= 72.0D;
/*  64:140 */     return (int)Math.rint(points);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public static double pixelToPoints(int pixel)
/*  68:    */   {
/*  69:144 */     double points = pixel;
/*  70:145 */     points *= 72.0D;
/*  71:146 */     points /= 96.0D;
/*  72:147 */     return points;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static int charactersToEMU(double characters)
/*  76:    */   {
/*  77:151 */     return (int)characters * 66691;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static int columnWidthToEMU(int columnWidth)
/*  81:    */   {
/*  82:159 */     return charactersToEMU(columnWidth / 256.0D);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public static int TwipsToEMU(short twips)
/*  86:    */   {
/*  87:167 */     return (int)(twips / 20.0D * 12700.0D);
/*  88:    */   }
/*  89:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.Units
 * JD-Core Version:    0.7.0.1
 */