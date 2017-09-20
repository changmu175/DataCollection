/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ddf.EscherBlipRecord;
/*   4:    */ import org.apache.poi.ss.usermodel.PictureData;
/*   5:    */ import org.apache.poi.util.PngUtils;
/*   6:    */ 
/*   7:    */ public class HSSFPictureData
/*   8:    */   implements PictureData
/*   9:    */ {
/*  10:    */   public static final short MSOBI_WMF = 8544;
/*  11:    */   public static final short MSOBI_EMF = 15680;
/*  12:    */   public static final short MSOBI_PICT = 21536;
/*  13:    */   public static final short MSOBI_PNG = 28160;
/*  14:    */   public static final short MSOBI_JPEG = 18080;
/*  15:    */   public static final short MSOBI_DIB = 31360;
/*  16:    */   public static final short FORMAT_MASK = -16;
/*  17:    */   private EscherBlipRecord blip;
/*  18:    */   
/*  19:    */   public HSSFPictureData(EscherBlipRecord blip)
/*  20:    */   {
/*  21: 55 */     this.blip = blip;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public byte[] getData()
/*  25:    */   {
/*  26: 63 */     byte[] pictureData = this.blip.getPicturedata();
/*  27: 67 */     if (PngUtils.matchesPngHeader(pictureData, 16))
/*  28:    */     {
/*  29: 69 */       byte[] png = new byte[pictureData.length - 16];
/*  30: 70 */       System.arraycopy(pictureData, 16, png, 0, png.length);
/*  31: 71 */       pictureData = png;
/*  32:    */     }
/*  33: 74 */     return pictureData;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public int getFormat()
/*  37:    */   {
/*  38: 88 */     return this.blip.getRecordId() - -4072;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public String suggestFileExtension()
/*  42:    */   {
/*  43: 96 */     switch (this.blip.getRecordId())
/*  44:    */     {
/*  45:    */     case -4069: 
/*  46: 98 */       return "wmf";
/*  47:    */     case -4070: 
/*  48:100 */       return "emf";
/*  49:    */     case -4068: 
/*  50:102 */       return "pict";
/*  51:    */     case -4066: 
/*  52:104 */       return "png";
/*  53:    */     case -4067: 
/*  54:106 */       return "jpeg";
/*  55:    */     case -4065: 
/*  56:108 */       return "dib";
/*  57:    */     }
/*  58:110 */     return "";
/*  59:    */   }
/*  60:    */   
/*  61:    */   public String getMimeType()
/*  62:    */   {
/*  63:118 */     switch (this.blip.getRecordId())
/*  64:    */     {
/*  65:    */     case -4069: 
/*  66:120 */       return "image/x-wmf";
/*  67:    */     case -4070: 
/*  68:122 */       return "image/x-emf";
/*  69:    */     case -4068: 
/*  70:124 */       return "image/x-pict";
/*  71:    */     case -4066: 
/*  72:126 */       return "image/png";
/*  73:    */     case -4067: 
/*  74:128 */       return "image/jpeg";
/*  75:    */     case -4065: 
/*  76:130 */       return "image/bmp";
/*  77:    */     }
/*  78:132 */     return "image/unknown";
/*  79:    */   }
/*  80:    */   
/*  81:    */   public int getPictureType()
/*  82:    */   {
/*  83:147 */     switch (this.blip.getRecordId())
/*  84:    */     {
/*  85:    */     case -4069: 
/*  86:149 */       return 3;
/*  87:    */     case -4070: 
/*  88:151 */       return 2;
/*  89:    */     case -4068: 
/*  90:153 */       return 4;
/*  91:    */     case -4066: 
/*  92:155 */       return 6;
/*  93:    */     case -4067: 
/*  94:157 */       return 5;
/*  95:    */     case -4065: 
/*  96:159 */       return 7;
/*  97:    */     }
/*  98:161 */     return -1;
/*  99:    */   }
/* 100:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFPictureData
 * JD-Core Version:    0.7.0.1
 */