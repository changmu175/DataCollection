/*   1:    */ package org.apache.poi.poifs.crypt;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.security.GeneralSecurityException;
/*   6:    */ import javax.crypto.SecretKey;
/*   7:    */ import javax.crypto.spec.SecretKeySpec;
/*   8:    */ import org.apache.poi.EncryptedDocumentException;
/*   9:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  10:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  11:    */ import org.apache.poi.poifs.filesystem.OPOIFSFileSystem;
/*  12:    */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*  13:    */ 
/*  14:    */ public abstract class Encryptor
/*  15:    */   implements Cloneable
/*  16:    */ {
/*  17:    */   protected static final String DEFAULT_POIFS_ENTRY = "EncryptedPackage";
/*  18:    */   private EncryptionInfo encryptionInfo;
/*  19:    */   private SecretKey secretKey;
/*  20:    */   
/*  21:    */   public abstract OutputStream getDataStream(DirectoryNode paramDirectoryNode)
/*  22:    */     throws IOException, GeneralSecurityException;
/*  23:    */   
/*  24:    */   public abstract void confirmPassword(String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, byte[] paramArrayOfByte5);
/*  25:    */   
/*  26:    */   public abstract void confirmPassword(String paramString);
/*  27:    */   
/*  28:    */   public static Encryptor getInstance(EncryptionInfo info)
/*  29:    */   {
/*  30: 52 */     return info.getEncryptor();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public OutputStream getDataStream(NPOIFSFileSystem fs)
/*  34:    */     throws IOException, GeneralSecurityException
/*  35:    */   {
/*  36: 56 */     return getDataStream(fs.getRoot());
/*  37:    */   }
/*  38:    */   
/*  39:    */   public OutputStream getDataStream(OPOIFSFileSystem fs)
/*  40:    */     throws IOException, GeneralSecurityException
/*  41:    */   {
/*  42: 59 */     return getDataStream(fs.getRoot());
/*  43:    */   }
/*  44:    */   
/*  45:    */   public OutputStream getDataStream(POIFSFileSystem fs)
/*  46:    */     throws IOException, GeneralSecurityException
/*  47:    */   {
/*  48: 62 */     return getDataStream(fs.getRoot());
/*  49:    */   }
/*  50:    */   
/*  51:    */   public ChunkedCipherOutputStream getDataStream(OutputStream stream, int initialOffset)
/*  52:    */     throws IOException, GeneralSecurityException
/*  53:    */   {
/*  54: 67 */     throw new EncryptedDocumentException("this decryptor doesn't support writing directly to a stream");
/*  55:    */   }
/*  56:    */   
/*  57:    */   public SecretKey getSecretKey()
/*  58:    */   {
/*  59: 71 */     return this.secretKey;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setSecretKey(SecretKey secretKey)
/*  63:    */   {
/*  64: 75 */     this.secretKey = secretKey;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public EncryptionInfo getEncryptionInfo()
/*  68:    */   {
/*  69: 79 */     return this.encryptionInfo;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setEncryptionInfo(EncryptionInfo encryptionInfo)
/*  73:    */   {
/*  74: 83 */     this.encryptionInfo = encryptionInfo;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setChunkSize(int chunkSize)
/*  78:    */   {
/*  79: 94 */     throw new EncryptedDocumentException("this decryptor doesn't support changing the chunk size");
/*  80:    */   }
/*  81:    */   
/*  82:    */   public Encryptor clone()
/*  83:    */     throws CloneNotSupportedException
/*  84:    */   {
/*  85: 99 */     Encryptor other = (Encryptor)super.clone();
/*  86:100 */     other.secretKey = new SecretKeySpec(this.secretKey.getEncoded(), this.secretKey.getAlgorithm());
/*  87:    */     
/*  88:102 */     return other;
/*  89:    */   }
/*  90:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.Encryptor
 * JD-Core Version:    0.7.0.1
 */