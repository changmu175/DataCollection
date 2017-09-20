/*   1:    */ package org.apache.poi.openxml4j.opc;
/*   2:    */ 
/*   3:    */ import java.io.FilterOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ import javax.xml.transform.Result;
/*   8:    */ import javax.xml.transform.Source;
/*   9:    */ import javax.xml.transform.Transformer;
/*  10:    */ import javax.xml.transform.TransformerException;
/*  11:    */ import javax.xml.transform.TransformerFactory;
/*  12:    */ import javax.xml.transform.dom.DOMSource;
/*  13:    */ import javax.xml.transform.stream.StreamResult;
/*  14:    */ import org.w3c.dom.Document;
/*  15:    */ 
/*  16:    */ public final class StreamHelper
/*  17:    */ {
/*  18: 42 */   private static final TransformerFactory transformerFactory = ;
/*  19:    */   
/*  20:    */   private static synchronized Transformer getIdentityTransformer()
/*  21:    */     throws TransformerException
/*  22:    */   {
/*  23: 45 */     return transformerFactory.newTransformer();
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static boolean saveXmlInStream(Document xmlContent, OutputStream outStream)
/*  27:    */   {
/*  28:    */     try
/*  29:    */     {
/*  30: 61 */       Transformer trans = getIdentityTransformer();
/*  31: 62 */       Source xmlSource = new DOMSource(xmlContent);
/*  32:    */       
/*  33: 64 */       Result outputTarget = new StreamResult(new FilterOutputStream(outStream)
/*  34:    */       {
/*  35:    */         public void write(byte[] b, int off, int len)
/*  36:    */           throws IOException
/*  37:    */         {
/*  38: 69 */           this.out.write(b, off, len);
/*  39:    */         }
/*  40:    */         
/*  41:    */         public void close()
/*  42:    */           throws IOException
/*  43:    */         {
/*  44: 74 */           this.out.flush();
/*  45:    */         }
/*  46: 77 */       });
/*  47: 78 */       trans.setOutputProperty("encoding", "UTF-8");
/*  48:    */       
/*  49:    */ 
/*  50:    */ 
/*  51: 82 */       trans.setOutputProperty("indent", "no");
/*  52: 83 */       trans.setOutputProperty("standalone", "yes");
/*  53: 84 */       trans.transform(xmlSource, outputTarget);
/*  54:    */     }
/*  55:    */     catch (TransformerException e)
/*  56:    */     {
/*  57: 86 */       return false;
/*  58:    */     }
/*  59: 88 */     return true;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public static boolean copyStream(InputStream inStream, OutputStream outStream)
/*  63:    */   {
/*  64:    */     try
/*  65:    */     {
/*  66:102 */       byte[] buffer = new byte[1024];
/*  67:    */       int bytesRead;
/*  68:104 */       while ((bytesRead = inStream.read(buffer)) >= 0) {
/*  69:105 */         outStream.write(buffer, 0, bytesRead);
/*  70:    */       }
/*  71:    */     }
/*  72:    */     catch (Exception e)
/*  73:    */     {
/*  74:108 */       return false;
/*  75:    */     }
/*  76:110 */     return true;
/*  77:    */   }
/*  78:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.StreamHelper
 * JD-Core Version:    0.7.0.1
 */