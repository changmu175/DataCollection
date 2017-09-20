/*  1:   */ package org.apache.poi.xdgf.util;
/*  2:   */ 
/*  3:   */ import java.awt.geom.AffineTransform;
/*  4:   */ import java.io.File;
/*  5:   */ import java.io.FileInputStream;
/*  6:   */ import java.io.FileNotFoundException;
/*  7:   */ import java.io.FileOutputStream;
/*  8:   */ import java.io.OutputStream;
/*  9:   */ import java.io.PrintStream;
/* 10:   */ import java.io.UnsupportedEncodingException;
/* 11:   */ import org.apache.poi.xdgf.usermodel.XDGFPage;
/* 12:   */ import org.apache.poi.xdgf.usermodel.XDGFPageContents;
/* 13:   */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/* 14:   */ import org.apache.poi.xdgf.usermodel.XmlVisioDocument;
/* 15:   */ import org.apache.poi.xdgf.usermodel.shape.ShapeVisitor;
/* 16:   */ 
/* 17:   */ public class HierarchyPrinter
/* 18:   */ {
/* 19:   */   public static void printHierarchy(XDGFPage page, File outDir)
/* 20:   */     throws FileNotFoundException, UnsupportedEncodingException
/* 21:   */   {
/* 22:43 */     File pageFile = new File(outDir, "page" + page.getPageNumber() + "-" + Util.sanitizeFilename(page.getName()) + ".txt");
/* 23:   */     
/* 24:   */ 
/* 25:46 */     OutputStream os = new FileOutputStream(pageFile);
/* 26:47 */     PrintStream pos = new PrintStream(os, false, "utf-8");
/* 27:   */     
/* 28:49 */     printHierarchy(page, pos);
/* 29:   */     
/* 30:51 */     pos.close();
/* 31:   */   }
/* 32:   */   
/* 33:   */   public static void printHierarchy(XDGFPage page, PrintStream os)
/* 34:   */   {
/* 35:56 */     page.getContent().visitShapes(new ShapeVisitor()
/* 36:   */     {
/* 37:   */       public void visit(XDGFShape shape, AffineTransform globalTransform, int level)
/* 38:   */       {
/* 39:61 */         for (int i = 0; i < level; i++) {
/* 40:62 */           this.val$os.append("  ");
/* 41:   */         }
/* 42:65 */         this.val$os.println(shape + " [" + shape.getShapeType() + ", " + shape.getSymbolName() + "] " + shape.getMasterShape() + " " + shape.getTextAsString().trim());
/* 43:   */       }
/* 44:   */     });
/* 45:   */   }
/* 46:   */   
/* 47:   */   public static void printHierarchy(XmlVisioDocument document, String outDirname)
/* 48:   */     throws FileNotFoundException, UnsupportedEncodingException
/* 49:   */   {
/* 50:76 */     File outDir = new File(outDirname);
/* 51:78 */     for (XDGFPage page : document.getPages()) {
/* 52:79 */       printHierarchy(page, outDir);
/* 53:   */     }
/* 54:   */   }
/* 55:   */   
/* 56:   */   public static void main(String[] args)
/* 57:   */     throws Exception
/* 58:   */   {
/* 59:84 */     if (args.length != 2)
/* 60:   */     {
/* 61:85 */       System.err.println("Usage: in.vsdx outdir");
/* 62:86 */       System.exit(1);
/* 63:   */     }
/* 64:89 */     String inFilename = args[0];
/* 65:90 */     String outDir = args[1];
/* 66:   */     
/* 67:92 */     XmlVisioDocument doc = new XmlVisioDocument(new FileInputStream(inFilename));
/* 68:   */     
/* 69:94 */     printHierarchy(doc, outDir);
/* 70:   */   }
/* 71:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.util.HierarchyPrinter
 * JD-Core Version:    0.7.0.1
 */