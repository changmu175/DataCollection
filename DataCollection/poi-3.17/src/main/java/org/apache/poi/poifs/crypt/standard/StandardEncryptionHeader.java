/*   1:    */ package org.apache.poi.poifs.crypt.standard;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   6:    */ import org.apache.poi.poifs.crypt.ChainingMode;
/*   7:    */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*   8:    */ import org.apache.poi.poifs.crypt.CipherProvider;
/*   9:    */ import org.apache.poi.poifs.crypt.EncryptionHeader;
/*  10:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  11:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  12:    */ import org.apache.poi.util.BitField;
/*  13:    */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*  14:    */ import org.apache.poi.util.LittleEndianInput;
/*  15:    */ import org.apache.poi.util.LittleEndianOutput;
/*  16:    */ import org.apache.poi.util.StringUtil;
/*  17:    */ 
/*  18:    */ public class StandardEncryptionHeader
/*  19:    */   extends EncryptionHeader
/*  20:    */   implements EncryptionRecord, Cloneable
/*  21:    */ {
/*  22:    */   protected StandardEncryptionHeader(LittleEndianInput is)
/*  23:    */     throws IOException
/*  24:    */   {
/*  25: 40 */     setFlags(is.readInt());
/*  26: 41 */     setSizeExtra(is.readInt());
/*  27: 42 */     setCipherAlgorithm(CipherAlgorithm.fromEcmaId(is.readInt()));
/*  28: 43 */     setHashAlgorithm(HashAlgorithm.fromEcmaId(is.readInt()));
/*  29: 44 */     int keySize = is.readInt();
/*  30: 45 */     if (keySize == 0) {
/*  31: 49 */       keySize = 40;
/*  32:    */     }
/*  33: 51 */     setKeySize(keySize);
/*  34: 52 */     setBlockSize(getKeySize());
/*  35: 53 */     setCipherProvider(CipherProvider.fromEcmaId(is.readInt()));
/*  36:    */     
/*  37: 55 */     is.readLong();
/*  38: 59 */     if ((is instanceof RecordInputStream)) {
/*  39: 60 */       ((RecordInputStream)is).mark(5);
/*  40:    */     } else {
/*  41: 62 */       ((InputStream)is).mark(5);
/*  42:    */     }
/*  43: 64 */     int checkForSalt = is.readInt();
/*  44: 65 */     if ((is instanceof RecordInputStream)) {
/*  45: 66 */       ((RecordInputStream)is).reset();
/*  46:    */     } else {
/*  47: 68 */       ((InputStream)is).reset();
/*  48:    */     }
/*  49: 71 */     if (checkForSalt == 16)
/*  50:    */     {
/*  51: 72 */       setCspName("");
/*  52:    */     }
/*  53:    */     else
/*  54:    */     {
/*  55: 74 */       StringBuilder builder = new StringBuilder();
/*  56:    */       for (;;)
/*  57:    */       {
/*  58: 76 */         char c = (char)is.readShort();
/*  59: 77 */         if (c == 0) {
/*  60:    */           break;
/*  61:    */         }
/*  62: 80 */         builder.append(c);
/*  63:    */       }
/*  64: 82 */       setCspName(builder.toString());
/*  65:    */     }
/*  66: 85 */     setChainingMode(ChainingMode.ecb);
/*  67: 86 */     setKeySalt(null);
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected StandardEncryptionHeader(CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode)
/*  71:    */   {
/*  72: 90 */     setCipherAlgorithm(cipherAlgorithm);
/*  73: 91 */     setHashAlgorithm(hashAlgorithm);
/*  74: 92 */     setKeySize(keyBits);
/*  75: 93 */     setBlockSize(blockSize);
/*  76: 94 */     setCipherProvider(cipherAlgorithm.provider);
/*  77: 95 */     setFlags(EncryptionInfo.flagCryptoAPI.setBoolean(0, true) | EncryptionInfo.flagAES.setBoolean(0, cipherAlgorithm.provider == CipherProvider.aes));
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void write(LittleEndianByteArrayOutputStream bos)
/*  81:    */   {
/*  82:106 */     int startIdx = bos.getWriteIndex();
/*  83:107 */     LittleEndianOutput sizeOutput = bos.createDelayedOutput(4);
/*  84:108 */     bos.writeInt(getFlags());
/*  85:109 */     bos.writeInt(0);
/*  86:110 */     bos.writeInt(getCipherAlgorithm().ecmaId);
/*  87:111 */     bos.writeInt(getHashAlgorithm().ecmaId);
/*  88:112 */     bos.writeInt(getKeySize());
/*  89:113 */     bos.writeInt(getCipherProvider().ecmaId);
/*  90:114 */     bos.writeInt(0);
/*  91:115 */     bos.writeInt(0);
/*  92:116 */     String cspName = getCspName();
/*  93:117 */     if (cspName == null) {
/*  94:118 */       cspName = getCipherProvider().cipherProviderName;
/*  95:    */     }
/*  96:120 */     bos.write(StringUtil.getToUnicodeLE(cspName));
/*  97:121 */     bos.writeShort(0);
/*  98:122 */     int headerSize = bos.getWriteIndex() - startIdx - 4;
/*  99:123 */     sizeOutput.writeInt(headerSize);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public StandardEncryptionHeader clone()
/* 103:    */     throws CloneNotSupportedException
/* 104:    */   {
/* 105:128 */     return (StandardEncryptionHeader)super.clone();
/* 106:    */   }
/* 107:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.standard.StandardEncryptionHeader
 * JD-Core Version:    0.7.0.1
 */