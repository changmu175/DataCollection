/*  1:   */ package org.apache.poi.util;
/*  2:   */ 
/*  3:   */ import java.awt.Font;
/*  4:   */ import java.awt.FontMetrics;
/*  5:   */ import java.awt.GraphicsEnvironment;
/*  6:   */ import java.awt.Toolkit;
/*  7:   */ import java.io.FileOutputStream;
/*  8:   */ import java.io.IOException;
/*  9:   */ import java.io.OutputStream;
/* 10:   */ import java.util.Properties;
/* 11:   */ 
/* 12:   */ public class FontMetricsDumper
/* 13:   */ {
/* 14:   */   @SuppressForbidden("command line tool")
/* 15:   */   public static void main(String[] args)
/* 16:   */     throws IOException
/* 17:   */   {
/* 18:34 */     Properties props = new Properties();
/* 19:   */     
/* 20:36 */     Font[] allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
/* 21:37 */     for (Font allFont : allFonts)
/* 22:   */     {
/* 23:38 */       String fontName = allFont.getFontName();
/* 24:   */       
/* 25:40 */       Font font = new Font(fontName, 1, 10);
/* 26:41 */       FontMetrics fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
/* 27:42 */       int fontHeight = fontMetrics.getHeight();
/* 28:   */       
/* 29:44 */       props.setProperty("font." + fontName + ".height", fontHeight + "");
/* 30:45 */       StringBuilder characters = new StringBuilder();
/* 31:46 */       for (char c = 'a'; c <= 'z'; c = (char)(c + '\001')) {
/* 32:47 */         characters.append(c).append(", ");
/* 33:   */       }
/* 34:49 */       for (char c = 'A'; c <= 'Z'; c = (char)(c + '\001')) {
/* 35:50 */         characters.append(c).append(", ");
/* 36:   */       }
/* 37:52 */       for (char c = '0'; c <= '9'; c = (char)(c + '\001')) {
/* 38:53 */         characters.append(c).append(", ");
/* 39:   */       }
/* 40:55 */       StringBuilder widths = new StringBuilder();
/* 41:56 */       for (char c = 'a'; c <= 'z'; c = (char)(c + '\001')) {
/* 42:57 */         widths.append(fontMetrics.getWidths()[c]).append(", ");
/* 43:   */       }
/* 44:59 */       for (char c = 'A'; c <= 'Z'; c = (char)(c + '\001')) {
/* 45:60 */         widths.append(fontMetrics.getWidths()[c]).append(", ");
/* 46:   */       }
/* 47:62 */       for (char c = '0'; c <= '9'; c = (char)(c + '\001')) {
/* 48:63 */         widths.append(fontMetrics.getWidths()[c]).append(", ");
/* 49:   */       }
/* 50:65 */       props.setProperty("font." + fontName + ".characters", characters.toString());
/* 51:66 */       props.setProperty("font." + fontName + ".widths", widths.toString());
/* 52:   */     }
/* 53:69 */     OutputStream fileOut = new FileOutputStream("font_metrics.properties");
/* 54:   */     try
/* 55:   */     {
/* 56:71 */       props.store(fileOut, "Font Metrics");
/* 57:   */     }
/* 58:   */     finally
/* 59:   */     {
/* 60:73 */       fileOut.close();
/* 61:   */     }
/* 62:   */   }
/* 63:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.FontMetricsDumper
 * JD-Core Version:    0.7.0.1
 */