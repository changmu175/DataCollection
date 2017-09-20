/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Font;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.FileInputStream;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStream;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Properties;
/*  11:    */ import org.apache.poi.util.POILogFactory;
/*  12:    */ import org.apache.poi.util.POILogger;
/*  13:    */ 
/*  14:    */ final class StaticFontMetrics
/*  15:    */ {
/*  16: 40 */   private static final POILogger LOGGER = POILogFactory.getLogger(StaticFontMetrics.class);
/*  17:    */   private static Properties fontMetricsProps;
/*  18: 44 */   private static final Map<String, FontDetails> fontDetailsMap = new HashMap();
/*  19:    */   
/*  20:    */   public static synchronized FontDetails getFontDetails(Font font)
/*  21:    */   {
/*  22: 58 */     if (fontMetricsProps == null) {
/*  23:    */       try
/*  24:    */       {
/*  25: 60 */         fontMetricsProps = loadMetrics();
/*  26:    */       }
/*  27:    */       catch (IOException e)
/*  28:    */       {
/*  29: 62 */         throw new RuntimeException("Could not load font metrics", e);
/*  30:    */       }
/*  31:    */     }
/*  32: 67 */     String fontName = font.getName();
/*  33:    */     
/*  34:    */ 
/*  35:    */ 
/*  36:    */ 
/*  37: 72 */     String fontStyle = "";
/*  38: 73 */     if (font.isPlain()) {
/*  39: 74 */       fontStyle = fontStyle + "plain";
/*  40:    */     }
/*  41: 76 */     if (font.isBold()) {
/*  42: 77 */       fontStyle = fontStyle + "bold";
/*  43:    */     }
/*  44: 79 */     if (font.isItalic()) {
/*  45: 80 */       fontStyle = fontStyle + "italic";
/*  46:    */     }
/*  47: 85 */     String fontHeight = FontDetails.buildFontHeightProperty(fontName);
/*  48: 86 */     String styleHeight = FontDetails.buildFontHeightProperty(fontName + "." + fontStyle);
/*  49: 88 */     if ((fontMetricsProps.get(fontHeight) == null) && (fontMetricsProps.get(styleHeight) != null)) {
/*  50: 91 */       fontName = fontName + "." + fontStyle;
/*  51:    */     }
/*  52: 95 */     FontDetails fontDetails = (FontDetails)fontDetailsMap.get(fontName);
/*  53: 96 */     if (fontDetails == null)
/*  54:    */     {
/*  55: 97 */       fontDetails = FontDetails.create(fontName, fontMetricsProps);
/*  56: 98 */       fontDetailsMap.put(fontName, fontDetails);
/*  57:    */     }
/*  58:100 */     return fontDetails;
/*  59:    */   }
/*  60:    */   
/*  61:    */   private static Properties loadMetrics()
/*  62:    */     throws IOException
/*  63:    */   {
/*  64:106 */     File propFile = null;
/*  65:    */     try
/*  66:    */     {
/*  67:108 */       String propFileName = System.getProperty("font.metrics.filename");
/*  68:109 */       if (propFileName != null)
/*  69:    */       {
/*  70:110 */         propFile = new File(propFileName);
/*  71:111 */         if (!propFile.exists())
/*  72:    */         {
/*  73:112 */           LOGGER.log(5, new Object[] { "font_metrics.properties not found at path " + propFile.getAbsolutePath() });
/*  74:113 */           propFile = null;
/*  75:    */         }
/*  76:    */       }
/*  77:    */     }
/*  78:    */     catch (SecurityException e)
/*  79:    */     {
/*  80:117 */       LOGGER.log(5, new Object[] { "Can't access font.metrics.filename system property", e });
/*  81:    */     }
/*  82:120 */     InputStream metricsIn = null;
/*  83:    */     try
/*  84:    */     {
/*  85:122 */       if (propFile != null)
/*  86:    */       {
/*  87:123 */         metricsIn = new FileInputStream(propFile);
/*  88:    */       }
/*  89:    */       else
/*  90:    */       {
/*  91:126 */         metricsIn = FontDetails.class.getResourceAsStream("/font_metrics.properties");
/*  92:127 */         if (metricsIn == null)
/*  93:    */         {
/*  94:128 */           String err = "font_metrics.properties not found in classpath";
/*  95:129 */           throw new IOException(err);
/*  96:    */         }
/*  97:    */       }
/*  98:133 */       Properties props = new Properties();
/*  99:134 */       props.load(metricsIn);
/* 100:135 */       return props;
/* 101:    */     }
/* 102:    */     finally
/* 103:    */     {
/* 104:137 */       if (metricsIn != null) {
/* 105:138 */         metricsIn.close();
/* 106:    */       }
/* 107:    */     }
/* 108:    */   }
/* 109:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.StaticFontMetrics
 * JD-Core Version:    0.7.0.1
 */