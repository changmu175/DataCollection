/*   1:    */ package org.apache.poi.openxml4j.util;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.ByteArrayOutputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.Enumeration;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.zip.ZipEntry;
/*  11:    */ 
/*  12:    */ public class ZipInputStreamZipEntrySource
/*  13:    */   implements ZipEntrySource
/*  14:    */ {
/*  15:    */   private ArrayList<FakeZipEntry> zipEntries;
/*  16:    */   
/*  17:    */   public ZipInputStreamZipEntrySource(ZipSecureFile.ThresholdInputStream inp)
/*  18:    */     throws IOException
/*  19:    */   {
/*  20: 48 */     this.zipEntries = new ArrayList();
/*  21:    */     
/*  22: 50 */     boolean going = true;
/*  23: 51 */     while (going)
/*  24:    */     {
/*  25: 52 */       ZipEntry zipEntry = inp.getNextEntry();
/*  26: 53 */       if (zipEntry == null)
/*  27:    */       {
/*  28: 54 */         going = false;
/*  29:    */       }
/*  30:    */       else
/*  31:    */       {
/*  32: 56 */         FakeZipEntry entry = new FakeZipEntry(zipEntry, inp);
/*  33: 57 */         inp.closeEntry();
/*  34:    */         
/*  35: 59 */         this.zipEntries.add(entry);
/*  36:    */       }
/*  37:    */     }
/*  38: 62 */     inp.close();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public Enumeration<? extends ZipEntry> getEntries()
/*  42:    */   {
/*  43: 66 */     return new EntryEnumerator(null);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public InputStream getInputStream(ZipEntry zipEntry)
/*  47:    */   {
/*  48: 70 */     assert ((zipEntry instanceof FakeZipEntry));
/*  49: 71 */     FakeZipEntry entry = (FakeZipEntry)zipEntry;
/*  50: 72 */     return entry.getInputStream();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void close()
/*  54:    */   {
/*  55: 77 */     this.zipEntries = null;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public boolean isClosed()
/*  59:    */   {
/*  60: 80 */     return this.zipEntries == null;
/*  61:    */   }
/*  62:    */   
/*  63:    */   private class EntryEnumerator
/*  64:    */     implements Enumeration<ZipEntry>
/*  65:    */   {
/*  66:    */     private Iterator<? extends ZipEntry> iterator;
/*  67:    */     
/*  68:    */     private EntryEnumerator()
/*  69:    */     {
/*  70: 91 */       this.iterator = ZipInputStreamZipEntrySource.this.zipEntries.iterator();
/*  71:    */     }
/*  72:    */     
/*  73:    */     public boolean hasMoreElements()
/*  74:    */     {
/*  75: 95 */       return this.iterator.hasNext();
/*  76:    */     }
/*  77:    */     
/*  78:    */     public ZipEntry nextElement()
/*  79:    */     {
/*  80: 99 */       return (ZipEntry)this.iterator.next();
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static class FakeZipEntry
/*  85:    */     extends ZipEntry
/*  86:    */   {
/*  87:    */     private byte[] data;
/*  88:    */     
/*  89:    */     public FakeZipEntry(ZipEntry entry, InputStream inp)
/*  90:    */       throws IOException
/*  91:    */     {
/*  92:113 */       super();
/*  93:    */       
/*  94:    */ 
/*  95:    */ 
/*  96:    */ 
/*  97:118 */       long entrySize = entry.getSize();
/*  98:    */       ByteArrayOutputStream baos;
/*  99:    */       ByteArrayOutputStream baos;
/* 100:120 */       if (entrySize != -1L)
/* 101:    */       {
/* 102:121 */         if (entrySize >= 2147483647L) {
/* 103:122 */           throw new IOException("ZIP entry size is too large");
/* 104:    */         }
/* 105:125 */         baos = new ByteArrayOutputStream((int)entrySize);
/* 106:    */       }
/* 107:    */       else
/* 108:    */       {
/* 109:127 */         baos = new ByteArrayOutputStream();
/* 110:    */       }
/* 111:130 */       byte[] buffer = new byte[4096];
/* 112:131 */       int read = 0;
/* 113:132 */       while ((read = inp.read(buffer)) != -1) {
/* 114:133 */         baos.write(buffer, 0, read);
/* 115:    */       }
/* 116:136 */       this.data = baos.toByteArray();
/* 117:    */     }
/* 118:    */     
/* 119:    */     public InputStream getInputStream()
/* 120:    */     {
/* 121:140 */       return new ByteArrayInputStream(this.data);
/* 122:    */     }
/* 123:    */   }
/* 124:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.util.ZipInputStreamZipEntrySource
 * JD-Core Version:    0.7.0.1
 */