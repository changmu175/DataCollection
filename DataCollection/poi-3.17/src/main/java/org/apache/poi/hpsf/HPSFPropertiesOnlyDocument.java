/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.OutputStream;
/*  6:   */ import java.util.ArrayList;
/*  7:   */ import java.util.List;
/*  8:   */ import org.apache.poi.POIDocument;
/*  9:   */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/* 10:   */ import org.apache.poi.poifs.filesystem.EntryUtils;
/* 11:   */ import org.apache.poi.poifs.filesystem.FilteringDirectoryNode;
/* 12:   */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/* 13:   */ import org.apache.poi.poifs.filesystem.OPOIFSFileSystem;
/* 14:   */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/* 15:   */ 
/* 16:   */ public class HPSFPropertiesOnlyDocument
/* 17:   */   extends POIDocument
/* 18:   */ {
/* 19:   */   public HPSFPropertiesOnlyDocument(NPOIFSFileSystem fs)
/* 20:   */   {
/* 21:40 */     super(fs.getRoot());
/* 22:   */   }
/* 23:   */   
/* 24:   */   public HPSFPropertiesOnlyDocument(OPOIFSFileSystem fs)
/* 25:   */   {
/* 26:43 */     super(fs);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public HPSFPropertiesOnlyDocument(POIFSFileSystem fs)
/* 30:   */   {
/* 31:46 */     super(fs);
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void write()
/* 35:   */     throws IOException
/* 36:   */   {
/* 37:53 */     NPOIFSFileSystem fs = getDirectory().getFileSystem();
/* 38:   */     
/* 39:55 */     validateInPlaceWritePossible();
/* 40:56 */     writeProperties(fs, null);
/* 41:57 */     fs.writeFilesystem();
/* 42:   */   }
/* 43:   */   
/* 44:   */   public void write(File newFile)
/* 45:   */     throws IOException
/* 46:   */   {
/* 47:63 */     POIFSFileSystem fs = POIFSFileSystem.create(newFile);
/* 48:   */     try
/* 49:   */     {
/* 50:65 */       write(fs);
/* 51:66 */       fs.writeFilesystem();
/* 52:   */     }
/* 53:   */     finally
/* 54:   */     {
/* 55:68 */       fs.close();
/* 56:   */     }
/* 57:   */   }
/* 58:   */   
/* 59:   */   public void write(OutputStream out)
/* 60:   */     throws IOException
/* 61:   */   {
/* 62:75 */     NPOIFSFileSystem fs = new NPOIFSFileSystem();
/* 63:   */     try
/* 64:   */     {
/* 65:77 */       write(fs);
/* 66:78 */       fs.writeFilesystem(out);
/* 67:   */     }
/* 68:   */     finally
/* 69:   */     {
/* 70:80 */       fs.close();
/* 71:   */     }
/* 72:   */   }
/* 73:   */   
/* 74:   */   private void write(NPOIFSFileSystem fs)
/* 75:   */     throws IOException
/* 76:   */   {
/* 77:86 */     List<String> excepts = new ArrayList(2);
/* 78:   */     
/* 79:   */ 
/* 80:89 */     writeProperties(fs, excepts);
/* 81:   */     
/* 82:   */ 
/* 83:92 */     FilteringDirectoryNode src = new FilteringDirectoryNode(getDirectory(), excepts);
/* 84:93 */     FilteringDirectoryNode dest = new FilteringDirectoryNode(fs.getRoot(), excepts);
/* 85:94 */     EntryUtils.copyNodes(src, dest);
/* 86:   */   }
/* 87:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.HPSFPropertiesOnlyDocument
 * JD-Core Version:    0.7.0.1
 */