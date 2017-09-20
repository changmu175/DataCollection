/*   1:    */ package org.apache.poi.hssf.eventusermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.util.Set;
/*   6:    */ import org.apache.poi.hssf.model.InternalWorkbook;
/*   7:    */ import org.apache.poi.hssf.record.Record;
/*   8:    */ import org.apache.poi.hssf.record.RecordFactoryInputStream;
/*   9:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  10:    */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*  11:    */ 
/*  12:    */ public class HSSFEventFactory
/*  13:    */ {
/*  14:    */   public void processWorkbookEvents(HSSFRequest req, POIFSFileSystem fs)
/*  15:    */     throws IOException
/*  16:    */   {
/*  17: 54 */     processWorkbookEvents(req, fs.getRoot());
/*  18:    */   }
/*  19:    */   
/*  20:    */   public void processWorkbookEvents(HSSFRequest req, DirectoryNode dir)
/*  21:    */     throws IOException
/*  22:    */   {
/*  23: 67 */     String name = null;
/*  24: 68 */     Set<String> entryNames = dir.getEntryNames();
/*  25: 69 */     for (String potentialName : InternalWorkbook.WORKBOOK_DIR_ENTRY_NAMES) {
/*  26: 70 */       if (entryNames.contains(potentialName))
/*  27:    */       {
/*  28: 71 */         name = potentialName;
/*  29: 72 */         break;
/*  30:    */       }
/*  31:    */     }
/*  32: 76 */     if (name == null) {
/*  33: 77 */       name = InternalWorkbook.WORKBOOK_DIR_ENTRY_NAMES[0];
/*  34:    */     }
/*  35: 80 */     InputStream in = dir.createDocumentInputStream(name);
/*  36:    */     try
/*  37:    */     {
/*  38: 82 */       processEvents(req, in);
/*  39:    */     }
/*  40:    */     finally
/*  41:    */     {
/*  42: 84 */       in.close();
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public short abortableProcessWorkbookEvents(HSSFRequest req, POIFSFileSystem fs)
/*  47:    */     throws IOException, HSSFUserException
/*  48:    */   {
/*  49:100 */     return abortableProcessWorkbookEvents(req, fs.getRoot());
/*  50:    */   }
/*  51:    */   
/*  52:    */   public short abortableProcessWorkbookEvents(HSSFRequest req, DirectoryNode dir)
/*  53:    */     throws IOException, HSSFUserException
/*  54:    */   {
/*  55:115 */     InputStream in = dir.createDocumentInputStream("Workbook");
/*  56:    */     try
/*  57:    */     {
/*  58:117 */       return abortableProcessEvents(req, in);
/*  59:    */     }
/*  60:    */     finally
/*  61:    */     {
/*  62:119 */       in.close();
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void processEvents(HSSFRequest req, InputStream in)
/*  67:    */   {
/*  68:    */     try
/*  69:    */     {
/*  70:136 */       genericProcessEvents(req, in);
/*  71:    */     }
/*  72:    */     catch (HSSFUserException hue) {}
/*  73:    */   }
/*  74:    */   
/*  75:    */   public short abortableProcessEvents(HSSFRequest req, InputStream in)
/*  76:    */     throws HSSFUserException
/*  77:    */   {
/*  78:155 */     return genericProcessEvents(req, in);
/*  79:    */   }
/*  80:    */   
/*  81:    */   private short genericProcessEvents(HSSFRequest req, InputStream in)
/*  82:    */     throws HSSFUserException
/*  83:    */   {
/*  84:168 */     short userCode = 0;
/*  85:    */     
/*  86:    */ 
/*  87:171 */     RecordFactoryInputStream recordStream = new RecordFactoryInputStream(in, false);
/*  88:    */     for (;;)
/*  89:    */     {
/*  90:175 */       Record r = recordStream.nextRecord();
/*  91:176 */       if (r == null) {
/*  92:    */         break;
/*  93:    */       }
/*  94:179 */       userCode = req.processRecord(r);
/*  95:180 */       if (userCode != 0) {
/*  96:    */         break;
/*  97:    */       }
/*  98:    */     }
/*  99:186 */     return userCode;
/* 100:    */   }
/* 101:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.eventusermodel.HSSFEventFactory
 * JD-Core Version:    0.7.0.1
 */