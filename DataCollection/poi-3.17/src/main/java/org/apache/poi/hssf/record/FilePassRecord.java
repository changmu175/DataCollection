/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import org.apache.poi.EncryptedDocumentException;
/*   6:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*   7:    */ import org.apache.poi.poifs.crypt.EncryptionMode;
/*   8:    */ import org.apache.poi.poifs.crypt.EncryptionVerifier;
/*   9:    */ import org.apache.poi.poifs.crypt.binaryrc4.BinaryRC4EncryptionHeader;
/*  10:    */ import org.apache.poi.poifs.crypt.binaryrc4.BinaryRC4EncryptionVerifier;
/*  11:    */ import org.apache.poi.poifs.crypt.cryptoapi.CryptoAPIEncryptionHeader;
/*  12:    */ import org.apache.poi.poifs.crypt.cryptoapi.CryptoAPIEncryptionVerifier;
/*  13:    */ import org.apache.poi.poifs.crypt.xor.XOREncryptionHeader;
/*  14:    */ import org.apache.poi.poifs.crypt.xor.XOREncryptionVerifier;
/*  15:    */ import org.apache.poi.util.HexDump;
/*  16:    */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*  17:    */ import org.apache.poi.util.LittleEndianOutput;
/*  18:    */ import org.apache.poi.util.LittleEndianOutputStream;
/*  19:    */ 
/*  20:    */ public final class FilePassRecord
/*  21:    */   extends StandardRecord
/*  22:    */   implements Cloneable
/*  23:    */ {
/*  24:    */   public static final short sid = 47;
/*  25:    */   private static final int ENCRYPTION_XOR = 0;
/*  26:    */   private static final int ENCRYPTION_OTHER = 1;
/*  27:    */   private final int encryptionType;
/*  28:    */   private EncryptionInfo encryptionInfo;
/*  29:    */   
/*  30:    */   private FilePassRecord(FilePassRecord other)
/*  31:    */   {
/*  32: 51 */     this.encryptionType = other.encryptionType;
/*  33:    */     try
/*  34:    */     {
/*  35: 53 */       this.encryptionInfo = other.encryptionInfo.clone();
/*  36:    */     }
/*  37:    */     catch (CloneNotSupportedException e)
/*  38:    */     {
/*  39: 55 */       throw new EncryptedDocumentException(e);
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   public FilePassRecord(EncryptionMode encryptionMode)
/*  44:    */   {
/*  45: 60 */     this.encryptionType = (encryptionMode == EncryptionMode.xor ? 0 : 1);
/*  46: 61 */     this.encryptionInfo = new EncryptionInfo(encryptionMode);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public FilePassRecord(RecordInputStream in)
/*  50:    */   {
/*  51: 65 */     this.encryptionType = in.readUShort();
/*  52:    */     EncryptionMode preferredMode;
/*  53: 68 */     switch (this.encryptionType)
/*  54:    */     {
/*  55:    */     case 0: 
/*  56: 70 */       preferredMode = EncryptionMode.xor;
/*  57: 71 */       break;
/*  58:    */     case 1: 
/*  59: 73 */       preferredMode = EncryptionMode.cryptoAPI;
/*  60: 74 */       break;
/*  61:    */     default: 
/*  62: 76 */       throw new EncryptedDocumentException("invalid encryption type");
/*  63:    */     }
/*  64:    */     try
/*  65:    */     {
/*  66: 80 */       this.encryptionInfo = new EncryptionInfo(in, preferredMode);
/*  67:    */     }
/*  68:    */     catch (IOException e)
/*  69:    */     {
/*  70: 82 */       throw new EncryptedDocumentException(e);
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void serialize(LittleEndianOutput out)
/*  75:    */   {
/*  76: 89 */     out.writeShort(this.encryptionType);
/*  77:    */     
/*  78: 91 */     byte[] data = new byte[1024];
/*  79: 92 */     LittleEndianByteArrayOutputStream bos = new LittleEndianByteArrayOutputStream(data, 0);
/*  80: 94 */     switch (1.$SwitchMap$org$apache$poi$poifs$crypt$EncryptionMode[this.encryptionInfo.getEncryptionMode().ordinal()])
/*  81:    */     {
/*  82:    */     case 1: 
/*  83: 96 */       ((XOREncryptionHeader)this.encryptionInfo.getHeader()).write(bos);
/*  84: 97 */       ((XOREncryptionVerifier)this.encryptionInfo.getVerifier()).write(bos);
/*  85: 98 */       break;
/*  86:    */     case 2: 
/*  87:100 */       out.writeShort(this.encryptionInfo.getVersionMajor());
/*  88:101 */       out.writeShort(this.encryptionInfo.getVersionMinor());
/*  89:102 */       ((BinaryRC4EncryptionHeader)this.encryptionInfo.getHeader()).write(bos);
/*  90:103 */       ((BinaryRC4EncryptionVerifier)this.encryptionInfo.getVerifier()).write(bos);
/*  91:104 */       break;
/*  92:    */     case 3: 
/*  93:106 */       out.writeShort(this.encryptionInfo.getVersionMajor());
/*  94:107 */       out.writeShort(this.encryptionInfo.getVersionMinor());
/*  95:108 */       out.writeInt(this.encryptionInfo.getEncryptionFlags());
/*  96:109 */       ((CryptoAPIEncryptionHeader)this.encryptionInfo.getHeader()).write(bos);
/*  97:110 */       ((CryptoAPIEncryptionVerifier)this.encryptionInfo.getVerifier()).write(bos);
/*  98:111 */       break;
/*  99:    */     default: 
/* 100:113 */       throw new EncryptedDocumentException("not supported");
/* 101:    */     }
/* 102:116 */     out.write(data, 0, bos.getWriteIndex());
/* 103:    */   }
/* 104:    */   
/* 105:    */   protected int getDataSize()
/* 106:    */   {
/* 107:121 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 108:122 */     LittleEndianOutputStream leos = new LittleEndianOutputStream(bos);
/* 109:123 */     serialize(leos);
/* 110:124 */     return bos.size();
/* 111:    */   }
/* 112:    */   
/* 113:    */   public EncryptionInfo getEncryptionInfo()
/* 114:    */   {
/* 115:128 */     return this.encryptionInfo;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public short getSid()
/* 119:    */   {
/* 120:133 */     return 47;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public FilePassRecord clone()
/* 124:    */   {
/* 125:138 */     return new FilePassRecord(this);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public String toString()
/* 129:    */   {
/* 130:143 */     StringBuilder buffer = new StringBuilder();
/* 131:    */     
/* 132:145 */     buffer.append("[FILEPASS]\n");
/* 133:146 */     buffer.append("    .type = ").append(HexDump.shortToHex(this.encryptionType)).append('\n');
/* 134:147 */     String prefix = "     ." + this.encryptionInfo.getEncryptionMode();
/* 135:148 */     buffer.append(prefix + ".info = ").append(HexDump.shortToHex(this.encryptionInfo.getVersionMajor())).append('\n');
/* 136:149 */     buffer.append(prefix + ".ver  = ").append(HexDump.shortToHex(this.encryptionInfo.getVersionMinor())).append('\n');
/* 137:150 */     buffer.append(prefix + ".salt = ").append(HexDump.toHex(this.encryptionInfo.getVerifier().getSalt())).append('\n');
/* 138:151 */     buffer.append(prefix + ".verifier = ").append(HexDump.toHex(this.encryptionInfo.getVerifier().getEncryptedVerifier())).append('\n');
/* 139:152 */     buffer.append(prefix + ".verifierHash = ").append(HexDump.toHex(this.encryptionInfo.getVerifier().getEncryptedVerifierHash())).append('\n');
/* 140:153 */     buffer.append("[/FILEPASS]\n");
/* 141:154 */     return buffer.toString();
/* 142:    */   }
/* 143:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.FilePassRecord
 * JD-Core Version:    0.7.0.1
 */