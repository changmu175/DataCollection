/*   1:    */ package org.apache.poi.ss.extractor;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.Shape;
/*   4:    */ 
/*   5:    */ public class EmbeddedData
/*   6:    */ {
/*   7:    */   private String filename;
/*   8:    */   private byte[] embeddedData;
/*   9:    */   private Shape shape;
/*  10: 29 */   private String contentType = "binary/octet-stream";
/*  11:    */   
/*  12:    */   public EmbeddedData(String filename, byte[] embeddedData, String contentType)
/*  13:    */   {
/*  14: 32 */     setFilename(filename);
/*  15: 33 */     setEmbeddedData(embeddedData);
/*  16: 34 */     setContentType(contentType);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public String getFilename()
/*  20:    */   {
/*  21: 41 */     return this.filename;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setFilename(String filename)
/*  25:    */   {
/*  26: 50 */     if (filename == null) {
/*  27: 51 */       this.filename = "unknown.bin";
/*  28:    */     } else {
/*  29: 53 */       this.filename = filename.replaceAll("[^/\\\\]*[/\\\\]", "").trim();
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public byte[] getEmbeddedData()
/*  34:    */   {
/*  35: 61 */     return this.embeddedData;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setEmbeddedData(byte[] embeddedData)
/*  39:    */   {
/*  40: 70 */     this.embeddedData = (embeddedData == null ? null : (byte[])embeddedData.clone());
/*  41:    */   }
/*  42:    */   
/*  43:    */   public Shape getShape()
/*  44:    */   {
/*  45: 77 */     return this.shape;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setShape(Shape shape)
/*  49:    */   {
/*  50: 86 */     this.shape = shape;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public String getContentType()
/*  54:    */   {
/*  55: 93 */     return this.contentType;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setContentType(String contentType)
/*  59:    */   {
/*  60:102 */     this.contentType = contentType;
/*  61:    */   }
/*  62:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.ss.extractor.EmbeddedData
 * JD-Core Version:    0.7.0.1
 */