/*   1:    */ package org.apache.poi.xssf.usermodel.helpers;
/*   2:    */ 
/*   3:    */ import java.security.SecureRandom;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Locale;
/*   6:    */ import javax.xml.bind.DatatypeConverter;
/*   7:    */ import javax.xml.namespace.QName;
/*   8:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*   9:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  10:    */ import org.apache.poi.util.Internal;
/*  11:    */ import org.apache.xmlbeans.XmlCursor;
/*  12:    */ import org.apache.xmlbeans.XmlObject;
/*  13:    */ 
/*  14:    */ @Internal(since="3.15 beta 3")
/*  15:    */ public final class XSSFPasswordHelper
/*  16:    */ {
/*  17:    */   public static void setPassword(XmlObject xobj, String password, HashAlgorithm hashAlgo, String prefix)
/*  18:    */   {
/*  19: 50 */     XmlCursor cur = xobj.newCursor();
/*  20: 52 */     if (password == null)
/*  21:    */     {
/*  22: 53 */       cur.removeAttribute(getAttrName(prefix, "password"));
/*  23: 54 */       cur.removeAttribute(getAttrName(prefix, "algorithmName"));
/*  24: 55 */       cur.removeAttribute(getAttrName(prefix, "hashValue"));
/*  25: 56 */       cur.removeAttribute(getAttrName(prefix, "saltValue"));
/*  26: 57 */       cur.removeAttribute(getAttrName(prefix, "spinCount"));
/*  27: 58 */       return;
/*  28:    */     }
/*  29: 61 */     cur.toFirstContentToken();
/*  30: 62 */     if (hashAlgo == null)
/*  31:    */     {
/*  32: 63 */       int hash = CryptoFunctions.createXorVerifier1(password);
/*  33: 64 */       cur.insertAttributeWithValue(getAttrName(prefix, "password"), String.format(Locale.ROOT, "%04X", new Object[] { Integer.valueOf(hash) }).toUpperCase(Locale.ROOT));
/*  34:    */     }
/*  35:    */     else
/*  36:    */     {
/*  37: 67 */       SecureRandom random = new SecureRandom();
/*  38: 68 */       byte[] salt = random.generateSeed(16);
/*  39:    */       
/*  40:    */ 
/*  41:    */ 
/*  42: 72 */       int spinCount = 100000;
/*  43:    */       
/*  44:    */ 
/*  45:    */ 
/*  46:    */ 
/*  47: 77 */       byte[] hash = CryptoFunctions.hashPassword(password, hashAlgo, salt, spinCount, false);
/*  48:    */       
/*  49: 79 */       cur.insertAttributeWithValue(getAttrName(prefix, "algorithmName"), hashAlgo.jceId);
/*  50: 80 */       cur.insertAttributeWithValue(getAttrName(prefix, "hashValue"), DatatypeConverter.printBase64Binary(hash));
/*  51: 81 */       cur.insertAttributeWithValue(getAttrName(prefix, "saltValue"), DatatypeConverter.printBase64Binary(salt));
/*  52: 82 */       cur.insertAttributeWithValue(getAttrName(prefix, "spinCount"), "" + spinCount);
/*  53:    */     }
/*  54: 84 */     cur.dispose();
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static boolean validatePassword(XmlObject xobj, String password, String prefix)
/*  58:    */   {
/*  59:100 */     if (password == null) {
/*  60:100 */       return false;
/*  61:    */     }
/*  62:102 */     XmlCursor cur = xobj.newCursor();
/*  63:103 */     String xorHashVal = cur.getAttributeText(getAttrName(prefix, "password"));
/*  64:104 */     String algoName = cur.getAttributeText(getAttrName(prefix, "algorithmName"));
/*  65:105 */     String hashVal = cur.getAttributeText(getAttrName(prefix, "hashValue"));
/*  66:106 */     String saltVal = cur.getAttributeText(getAttrName(prefix, "saltValue"));
/*  67:107 */     String spinCount = cur.getAttributeText(getAttrName(prefix, "spinCount"));
/*  68:108 */     cur.dispose();
/*  69:110 */     if (xorHashVal != null)
/*  70:    */     {
/*  71:111 */       int hash1 = Integer.parseInt(xorHashVal, 16);
/*  72:112 */       int hash2 = CryptoFunctions.createXorVerifier1(password);
/*  73:113 */       return hash1 == hash2;
/*  74:    */     }
/*  75:115 */     if ((hashVal == null) || (algoName == null) || (saltVal == null) || (spinCount == null)) {
/*  76:116 */       return false;
/*  77:    */     }
/*  78:119 */     byte[] hash1 = DatatypeConverter.parseBase64Binary(hashVal);
/*  79:120 */     HashAlgorithm hashAlgo = HashAlgorithm.fromString(algoName);
/*  80:121 */     byte[] salt = DatatypeConverter.parseBase64Binary(saltVal);
/*  81:122 */     int spinCnt = Integer.parseInt(spinCount);
/*  82:123 */     byte[] hash2 = CryptoFunctions.hashPassword(password, hashAlgo, salt, spinCnt, false);
/*  83:124 */     return Arrays.equals(hash1, hash2);
/*  84:    */   }
/*  85:    */   
/*  86:    */   private static QName getAttrName(String prefix, String name)
/*  87:    */   {
/*  88:130 */     if ((prefix == null) || ("".equals(prefix))) {
/*  89:131 */       return new QName(name);
/*  90:    */     }
/*  91:133 */     return new QName(prefix + Character.toUpperCase(name.charAt(0)) + name.substring(1));
/*  92:    */   }
/*  93:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.helpers.XSSFPasswordHelper
 * JD-Core Version:    0.7.0.1
 */