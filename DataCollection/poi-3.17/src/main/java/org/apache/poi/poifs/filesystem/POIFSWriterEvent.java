/*  1:   */ package org.apache.poi.poifs.filesystem;
/*  2:   */ 
/*  3:   */ public class POIFSWriterEvent
/*  4:   */ {
/*  5:   */   private DocumentOutputStream stream;
/*  6:   */   private POIFSDocumentPath path;
/*  7:   */   private String documentName;
/*  8:   */   private int limit;
/*  9:   */   
/* 10:   */   POIFSWriterEvent(DocumentOutputStream stream, POIFSDocumentPath path, String documentName, int limit)
/* 11:   */   {
/* 12:50 */     this.stream = stream;
/* 13:51 */     this.path = path;
/* 14:52 */     this.documentName = documentName;
/* 15:53 */     this.limit = limit;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public DocumentOutputStream getStream()
/* 19:   */   {
/* 20:62 */     return this.stream;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public POIFSDocumentPath getPath()
/* 24:   */   {
/* 25:71 */     return this.path;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String getName()
/* 29:   */   {
/* 30:80 */     return this.documentName;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public int getLimit()
/* 34:   */   {
/* 35:89 */     return this.limit;
/* 36:   */   }
/* 37:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.POIFSWriterEvent
 * JD-Core Version:    0.7.0.1
 */