/*   1:    */ package org.apache.poi.poifs.crypt.temp;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileOutputStream;
/*   5:    */ import java.io.FilterOutputStream;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStream;
/*   8:    */ import java.io.OutputStream;
/*   9:    */ import java.security.GeneralSecurityException;
/*  10:    */ import java.security.SecureRandom;
/*  11:    */ import java.util.Enumeration;
/*  12:    */ import java.util.zip.ZipEntry;
/*  13:    */ import java.util.zip.ZipException;
/*  14:    */ import java.util.zip.ZipFile;
/*  15:    */ import java.util.zip.ZipInputStream;
/*  16:    */ import java.util.zip.ZipOutputStream;
/*  17:    */ import javax.crypto.Cipher;
/*  18:    */ import javax.crypto.CipherInputStream;
/*  19:    */ import javax.crypto.CipherOutputStream;
/*  20:    */ import javax.crypto.spec.SecretKeySpec;
/*  21:    */ import org.apache.poi.openxml4j.util.ZipEntrySource;
/*  22:    */ import org.apache.poi.poifs.crypt.ChainingMode;
/*  23:    */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  24:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*  25:    */ import org.apache.poi.util.IOUtils;
/*  26:    */ import org.apache.poi.util.POILogFactory;
/*  27:    */ import org.apache.poi.util.POILogger;
/*  28:    */ import org.apache.poi.util.TempFile;
/*  29:    */ 
/*  30:    */ public class AesZipFileZipEntrySource
/*  31:    */   implements ZipEntrySource
/*  32:    */ {
/*  33: 57 */   private static final POILogger LOG = POILogFactory.getLogger(AesZipFileZipEntrySource.class);
/*  34:    */   private final File tmpFile;
/*  35:    */   private final ZipFile zipFile;
/*  36:    */   private final Cipher ci;
/*  37:    */   private boolean closed;
/*  38:    */   
/*  39:    */   public AesZipFileZipEntrySource(File tmpFile, Cipher ci)
/*  40:    */     throws IOException
/*  41:    */   {
/*  42: 65 */     this.tmpFile = tmpFile;
/*  43: 66 */     this.zipFile = new ZipFile(tmpFile);
/*  44: 67 */     this.ci = ci;
/*  45: 68 */     this.closed = false;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public Enumeration<? extends ZipEntry> getEntries()
/*  49:    */   {
/*  50: 77 */     return this.zipFile.entries();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public InputStream getInputStream(ZipEntry entry)
/*  54:    */     throws IOException
/*  55:    */   {
/*  56: 82 */     InputStream is = this.zipFile.getInputStream(entry);
/*  57: 83 */     return new CipherInputStream(is, this.ci);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void close()
/*  61:    */     throws IOException
/*  62:    */   {
/*  63: 88 */     if (!this.closed)
/*  64:    */     {
/*  65: 89 */       this.zipFile.close();
/*  66: 90 */       if (!this.tmpFile.delete()) {
/*  67: 91 */         LOG.log(5, new Object[] { this.tmpFile.getAbsolutePath() + " can't be removed (or was already removed." });
/*  68:    */       }
/*  69:    */     }
/*  70: 94 */     this.closed = true;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public boolean isClosed()
/*  74:    */   {
/*  75: 99 */     return this.closed;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public static AesZipFileZipEntrySource createZipEntrySource(InputStream is)
/*  79:    */     throws IOException, GeneralSecurityException
/*  80:    */   {
/*  81:104 */     SecureRandom sr = new SecureRandom();
/*  82:105 */     byte[] ivBytes = new byte[16];byte[] keyBytes = new byte[16];
/*  83:106 */     sr.nextBytes(ivBytes);
/*  84:107 */     sr.nextBytes(keyBytes);
/*  85:108 */     File tmpFile = TempFile.createTempFile("protectedXlsx", ".zip");
/*  86:109 */     copyToFile(is, tmpFile, CipherAlgorithm.aes128, keyBytes, ivBytes);
/*  87:110 */     IOUtils.closeQuietly(is);
/*  88:111 */     return fileToSource(tmpFile, CipherAlgorithm.aes128, keyBytes, ivBytes);
/*  89:    */   }
/*  90:    */   
/*  91:    */   private static void copyToFile(InputStream is, File tmpFile, CipherAlgorithm cipherAlgorithm, byte[] keyBytes, byte[] ivBytes)
/*  92:    */     throws IOException, GeneralSecurityException
/*  93:    */   {
/*  94:115 */     SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, cipherAlgorithm.jceId);
/*  95:116 */     Cipher ciEnc = CryptoFunctions.getCipher(skeySpec, cipherAlgorithm, ChainingMode.cbc, ivBytes, 1, "PKCS5Padding");
/*  96:    */     
/*  97:118 */     ZipInputStream zis = new ZipInputStream(is);
/*  98:119 */     FileOutputStream fos = new FileOutputStream(tmpFile);
/*  99:120 */     ZipOutputStream zos = new ZipOutputStream(fos);
/* 100:    */     ZipEntry ze;
/* 101:123 */     while ((ze = zis.getNextEntry()) != null)
/* 102:    */     {
/* 103:126 */       ZipEntry zeNew = new ZipEntry(ze.getName());
/* 104:127 */       zeNew.setComment(ze.getComment());
/* 105:128 */       zeNew.setExtra(ze.getExtra());
/* 106:129 */       zeNew.setTime(ze.getTime());
/* 107:    */       
/* 108:131 */       zos.putNextEntry(zeNew);
/* 109:132 */       FilterOutputStream fos2 = new FilterOutputStream(zos)
/* 110:    */       {
/* 111:    */         public void close() {}
/* 112:136 */       };
/* 113:137 */       CipherOutputStream cos = new CipherOutputStream(fos2, ciEnc);
/* 114:138 */       IOUtils.copy(zis, cos);
/* 115:139 */       cos.close();
/* 116:140 */       fos2.close();
/* 117:141 */       zos.closeEntry();
/* 118:142 */       zis.closeEntry();
/* 119:    */     }
/* 120:144 */     zos.close();
/* 121:145 */     fos.close();
/* 122:146 */     zis.close();
/* 123:    */   }
/* 124:    */   
/* 125:    */   private static AesZipFileZipEntrySource fileToSource(File tmpFile, CipherAlgorithm cipherAlgorithm, byte[] keyBytes, byte[] ivBytes)
/* 126:    */     throws ZipException, IOException
/* 127:    */   {
/* 128:150 */     SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, cipherAlgorithm.jceId);
/* 129:151 */     Cipher ciDec = CryptoFunctions.getCipher(skeySpec, cipherAlgorithm, ChainingMode.cbc, ivBytes, 2, "PKCS5Padding");
/* 130:152 */     return new AesZipFileZipEntrySource(tmpFile, ciDec);
/* 131:    */   }
/* 132:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.temp.AesZipFileZipEntrySource
 * JD-Core Version:    0.7.0.1
 */