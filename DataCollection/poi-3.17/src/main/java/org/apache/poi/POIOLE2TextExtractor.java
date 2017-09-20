/*   1:    */ package org.apache.poi;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hpsf.DocumentSummaryInformation;
/*   4:    */ import org.apache.poi.hpsf.SummaryInformation;
/*   5:    */ import org.apache.poi.hpsf.extractor.HPSFPropertiesExtractor;
/*   6:    */ import org.apache.poi.poifs.filesystem.DirectoryEntry;
/*   7:    */ 
/*   8:    */ public abstract class POIOLE2TextExtractor
/*   9:    */   extends POITextExtractor
/*  10:    */ {
/*  11:    */   protected POIDocument document;
/*  12:    */   
/*  13:    */   public POIOLE2TextExtractor(POIDocument document)
/*  14:    */   {
/*  15: 46 */     this.document = document;
/*  16:    */     
/*  17:    */ 
/*  18:    */ 
/*  19: 50 */     setFilesystem(document);
/*  20:    */   }
/*  21:    */   
/*  22:    */   protected POIOLE2TextExtractor(POIOLE2TextExtractor otherExtractor)
/*  23:    */   {
/*  24: 61 */     this.document = otherExtractor.document;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public DocumentSummaryInformation getDocSummaryInformation()
/*  28:    */   {
/*  29: 71 */     return this.document.getDocumentSummaryInformation();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public SummaryInformation getSummaryInformation()
/*  33:    */   {
/*  34: 80 */     return this.document.getSummaryInformation();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public POITextExtractor getMetadataTextExtractor()
/*  38:    */   {
/*  39: 91 */     return new HPSFPropertiesExtractor(this);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public DirectoryEntry getRoot()
/*  43:    */   {
/*  44:100 */     return this.document.getDirectory();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public POIDocument getDocument()
/*  48:    */   {
/*  49:109 */     return this.document;
/*  50:    */   }
/*  51:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.POIOLE2TextExtractor
 * JD-Core Version:    0.7.0.1
 */