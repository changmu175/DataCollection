/*  1:   */ package org.apache.poi.xdgf.extractor;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.PrintStream;
/*  5:   */ import org.apache.poi.POIXMLDocument;
/*  6:   */ import org.apache.poi.POIXMLTextExtractor;
/*  7:   */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  8:   */ import org.apache.poi.xdgf.usermodel.XDGFPage;
/*  9:   */ import org.apache.poi.xdgf.usermodel.XDGFPageContents;
/* 10:   */ import org.apache.poi.xdgf.usermodel.XmlVisioDocument;
/* 11:   */ import org.apache.poi.xdgf.usermodel.shape.ShapeTextVisitor;
/* 12:   */ 
/* 13:   */ public class XDGFVisioExtractor
/* 14:   */   extends POIXMLTextExtractor
/* 15:   */ {
/* 16:   */   protected final XmlVisioDocument document;
/* 17:   */   
/* 18:   */   public XDGFVisioExtractor(XmlVisioDocument document)
/* 19:   */   {
/* 20:36 */     super(document);
/* 21:37 */     this.document = document;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public XDGFVisioExtractor(OPCPackage openPackage)
/* 25:   */     throws IOException
/* 26:   */   {
/* 27:41 */     this(new XmlVisioDocument(openPackage));
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String getText()
/* 31:   */   {
/* 32:45 */     ShapeTextVisitor visitor = new ShapeTextVisitor();
/* 33:47 */     for (XDGFPage page : this.document.getPages()) {
/* 34:48 */       page.getContent().visitShapes(visitor);
/* 35:   */     }
/* 36:51 */     return visitor.getText().toString();
/* 37:   */   }
/* 38:   */   
/* 39:   */   public static void main(String[] args)
/* 40:   */     throws IOException
/* 41:   */   {
/* 42:55 */     if (args.length < 1)
/* 43:   */     {
/* 44:56 */       System.err.println("Use:");
/* 45:57 */       System.err.println("  XDGFVisioExtractor <filename.vsdx>");
/* 46:58 */       System.exit(1);
/* 47:   */     }
/* 48:60 */     POIXMLTextExtractor extractor = new XDGFVisioExtractor(POIXMLDocument.openPackage(args[0]));
/* 49:   */     
/* 50:   */ 
/* 51:   */ 
/* 52:64 */     System.out.println(extractor.getText());
/* 53:65 */     extractor.close();
/* 54:   */   }
/* 55:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.extractor.XDGFVisioExtractor
 * JD-Core Version:    0.7.0.1
 */