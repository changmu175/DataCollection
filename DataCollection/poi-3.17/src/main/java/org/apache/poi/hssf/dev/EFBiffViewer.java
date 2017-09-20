/*  1:   */ package org.apache.poi.hssf.dev;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.InputStream;
/*  6:   */ import java.io.PrintStream;
/*  7:   */ import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
/*  8:   */ import org.apache.poi.hssf.eventusermodel.HSSFListener;
/*  9:   */ import org.apache.poi.hssf.eventusermodel.HSSFRequest;
/* 10:   */ import org.apache.poi.hssf.record.Record;
/* 11:   */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/* 12:   */ 
/* 13:   */ public class EFBiffViewer
/* 14:   */ {
/* 15:   */   String file;
/* 16:   */   
/* 17:   */   public void run()
/* 18:   */     throws IOException
/* 19:   */   {
/* 20:46 */     NPOIFSFileSystem fs = new NPOIFSFileSystem(new File(this.file), true);
/* 21:   */     try
/* 22:   */     {
/* 23:48 */       InputStream din = BiffViewer.getPOIFSInputStream(fs);
/* 24:   */       try
/* 25:   */       {
/* 26:50 */         HSSFRequest req = new HSSFRequest();
/* 27:   */         
/* 28:52 */         req.addListenerForAllRecords(new HSSFListener()
/* 29:   */         {
/* 30:   */           public void processRecord(Record rec)
/* 31:   */           {
/* 32:56 */             System.out.println(rec);
/* 33:   */           }
/* 34:58 */         });
/* 35:59 */         HSSFEventFactory factory = new HSSFEventFactory();
/* 36:   */         
/* 37:61 */         factory.processEvents(req, din);
/* 38:   */       }
/* 39:   */       finally {}
/* 40:   */     }
/* 41:   */     finally
/* 42:   */     {
/* 43:66 */       fs.close();
/* 44:   */     }
/* 45:   */   }
/* 46:   */   
/* 47:   */   public void setFile(String file)
/* 48:   */   {
/* 49:72 */     this.file = file;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public static void main(String[] args)
/* 53:   */     throws IOException
/* 54:   */   {
/* 55:77 */     if ((args.length == 1) && (!args[0].equals("--help")))
/* 56:   */     {
/* 57:79 */       EFBiffViewer viewer = new EFBiffViewer();
/* 58:   */       
/* 59:81 */       viewer.setFile(args[0]);
/* 60:82 */       viewer.run();
/* 61:   */     }
/* 62:   */     else
/* 63:   */     {
/* 64:86 */       System.out.println("EFBiffViewer");
/* 65:87 */       System.out.println("Outputs biffview of records based on HSSFEventFactory");
/* 66:   */       
/* 67:89 */       System.out.println("usage: java org.apache.poi.hssf.dev.EBBiffViewer filename");
/* 68:   */     }
/* 69:   */   }
/* 70:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.dev.EFBiffViewer
 * JD-Core Version:    0.7.0.1
 */