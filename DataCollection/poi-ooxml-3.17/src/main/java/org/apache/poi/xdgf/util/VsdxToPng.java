/*   1:    */ package org.apache.poi.xdgf.util;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.Graphics2D;
/*   5:    */ import java.awt.RenderingHints;
/*   6:    */ import java.awt.image.BufferedImage;
/*   7:    */ import java.io.File;
/*   8:    */ import java.io.FileInputStream;
/*   9:    */ import java.io.FileOutputStream;
/*  10:    */ import java.io.IOException;
/*  11:    */ import java.io.OutputStream;
/*  12:    */ import java.io.PrintStream;
/*  13:    */ import javax.imageio.ImageIO;
/*  14:    */ import org.apache.poi.xdgf.geom.Dimension2dDouble;
/*  15:    */ import org.apache.poi.xdgf.usermodel.XDGFPage;
/*  16:    */ import org.apache.poi.xdgf.usermodel.XDGFPageContents;
/*  17:    */ import org.apache.poi.xdgf.usermodel.XmlVisioDocument;
/*  18:    */ import org.apache.poi.xdgf.usermodel.shape.ShapeDebuggerRenderer;
/*  19:    */ import org.apache.poi.xdgf.usermodel.shape.ShapeRenderer;
/*  20:    */ 
/*  21:    */ public class VsdxToPng
/*  22:    */ {
/*  23:    */   public static void renderToPng(XDGFPage page, String outFilename, double scale, ShapeRenderer renderer)
/*  24:    */     throws IOException
/*  25:    */   {
/*  26: 44 */     renderToPng(page, new File(outFilename), scale, renderer);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static void renderToPngDir(XDGFPage page, File outDir, double scale, ShapeRenderer renderer)
/*  30:    */     throws IOException
/*  31:    */   {
/*  32: 50 */     File pageFile = new File(outDir, "page" + page.getPageNumber() + "-" + Util.sanitizeFilename(page.getName()) + ".png");
/*  33:    */     
/*  34: 52 */     System.out.println("** Writing image to " + pageFile);
/*  35:    */     
/*  36: 54 */     renderToPng(page, pageFile, scale, renderer);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static void renderToPng(XDGFPage page, File outFile, double scale, ShapeRenderer renderer)
/*  40:    */     throws IOException
/*  41:    */   {
/*  42: 61 */     Dimension2dDouble sz = page.getPageSize();
/*  43:    */     
/*  44: 63 */     int width = (int)(scale * sz.getWidth());
/*  45: 64 */     int height = (int)(scale * sz.getHeight());
/*  46:    */     
/*  47: 66 */     BufferedImage img = new BufferedImage(width, height, 1);
/*  48:    */     
/*  49: 68 */     Graphics2D graphics = img.createGraphics();
/*  50:    */     
/*  51:    */ 
/*  52: 71 */     graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*  53:    */     
/*  54: 73 */     graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
/*  55:    */     
/*  56: 75 */     graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
/*  57:    */     
/*  58: 77 */     graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
/*  59:    */     
/*  60:    */ 
/*  61: 80 */     graphics.setColor(Color.black);
/*  62: 81 */     graphics.setBackground(Color.white);
/*  63: 82 */     graphics.clearRect(0, 0, width, height);
/*  64:    */     
/*  65:    */ 
/*  66: 85 */     graphics.translate(0, img.getHeight());
/*  67: 86 */     graphics.scale(scale, -scale);
/*  68:    */     
/*  69:    */ 
/*  70: 89 */     renderer.setGraphics(graphics);
/*  71: 90 */     page.getContent().visitShapes(renderer);
/*  72:    */     
/*  73: 92 */     graphics.dispose();
/*  74:    */     
/*  75: 94 */     OutputStream out = new FileOutputStream(outFile);
/*  76:    */     try
/*  77:    */     {
/*  78: 96 */       ImageIO.write(img, "png", out);
/*  79:    */     }
/*  80:    */     finally
/*  81:    */     {
/*  82: 98 */       out.close();
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static void renderToPng(XmlVisioDocument document, String outDirname, double scale, ShapeRenderer renderer)
/*  87:    */     throws IOException
/*  88:    */   {
/*  89:106 */     File outDir = new File(outDirname);
/*  90:108 */     for (XDGFPage page : document.getPages()) {
/*  91:109 */       renderToPngDir(page, outDir, scale, renderer);
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static void main(String[] args)
/*  96:    */     throws Exception
/*  97:    */   {
/*  98:114 */     if (args.length > 2)
/*  99:    */     {
/* 100:115 */       System.err.println("Usage: [--debug] in.vsdx outdir");
/* 101:116 */       System.exit(1);
/* 102:    */     }
/* 103:119 */     ShapeRenderer renderer = new ShapeRenderer();
/* 104:    */     
/* 105:121 */     String inFilename = args[0];
/* 106:122 */     String pngDir = args[1];
/* 107:124 */     if (args[0].equals("--debug"))
/* 108:    */     {
/* 109:125 */       inFilename = args[1];
/* 110:126 */       pngDir = args[2];
/* 111:127 */       renderer = new ShapeDebuggerRenderer();
/* 112:    */     }
/* 113:130 */     XmlVisioDocument doc = new XmlVisioDocument(new FileInputStream(inFilename));
/* 114:    */     
/* 115:132 */     renderToPng(doc, pngDir, 181.81818181818181D, renderer);
/* 116:    */   }
/* 117:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.util.VsdxToPng
 * JD-Core Version:    0.7.0.1
 */