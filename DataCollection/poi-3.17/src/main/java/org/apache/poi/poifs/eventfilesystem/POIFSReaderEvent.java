/*  1:   */ package org.apache.poi.poifs.eventfilesystem;
/*  2:   */ 
/*  3:   */ import org.apache.poi.poifs.filesystem.DocumentInputStream;
/*  4:   */ import org.apache.poi.poifs.filesystem.POIFSDocumentPath;
/*  5:   */ 
/*  6:   */ public class POIFSReaderEvent
/*  7:   */ {
/*  8:   */   private final DocumentInputStream stream;
/*  9:   */   private final POIFSDocumentPath path;
/* 10:   */   private final String documentName;
/* 11:   */   
/* 12:   */   POIFSReaderEvent(DocumentInputStream stream, POIFSDocumentPath path, String documentName)
/* 13:   */   {
/* 14:49 */     this.stream = stream;
/* 15:50 */     this.path = path;
/* 16:51 */     this.documentName = documentName;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public DocumentInputStream getStream()
/* 20:   */   {
/* 21:60 */     return this.stream;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public POIFSDocumentPath getPath()
/* 25:   */   {
/* 26:69 */     return this.path;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String getName()
/* 30:   */   {
/* 31:78 */     return this.documentName;
/* 32:   */   }
/* 33:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent
 * JD-Core Version:    0.7.0.1
 */