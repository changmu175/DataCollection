/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileOutputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import org.apache.poi.poifs.dev.POIFSViewable;
/*   9:    */ import org.apache.poi.util.CloseIgnoringInputStream;
/*  10:    */ 
/*  11:    */ public class POIFSFileSystem
/*  12:    */   extends NPOIFSFileSystem
/*  13:    */   implements POIFSViewable
/*  14:    */ {
/*  15:    */   public static InputStream createNonClosingInputStream(InputStream is)
/*  16:    */   {
/*  17: 41 */     return new CloseIgnoringInputStream(is);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public POIFSFileSystem() {}
/*  21:    */   
/*  22:    */   public POIFSFileSystem(InputStream stream)
/*  23:    */     throws IOException
/*  24:    */   {
/*  25: 81 */     super(stream);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public POIFSFileSystem(File file, boolean readOnly)
/*  29:    */     throws IOException
/*  30:    */   {
/*  31: 98 */     super(file, readOnly);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public POIFSFileSystem(File file)
/*  35:    */     throws IOException
/*  36:    */   {
/*  37:114 */     super(file);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static POIFSFileSystem create(File file)
/*  41:    */     throws IOException
/*  42:    */   {
/*  43:128 */     POIFSFileSystem tmp = new POIFSFileSystem();
/*  44:    */     try
/*  45:    */     {
/*  46:130 */       OutputStream out = new FileOutputStream(file);
/*  47:    */       try
/*  48:    */       {
/*  49:132 */         tmp.writeFilesystem(out);
/*  50:    */       }
/*  51:    */       finally {}
/*  52:    */     }
/*  53:    */     finally
/*  54:    */     {
/*  55:137 */       tmp.close();
/*  56:    */     }
/*  57:141 */     return new POIFSFileSystem(file, false);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public static void main(String[] args)
/*  61:    */     throws IOException
/*  62:    */   {
/*  63:151 */     OPOIFSFileSystem.main(args);
/*  64:    */   }
/*  65:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.POIFSFileSystem
 * JD-Core Version:    0.7.0.1
 */