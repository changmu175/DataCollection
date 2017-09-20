/*   1:    */ package org.apache.poi.poifs.crypt.dsig;
/*   2:    */ 
/*   3:    */ import java.security.Key;
/*   4:    */ import java.security.cert.X509Certificate;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.List;
/*   7:    */ import javax.xml.crypto.AlgorithmMethod;
/*   8:    */ import javax.xml.crypto.KeySelector;
/*   9:    */ import javax.xml.crypto.KeySelector.Purpose;
/*  10:    */ import javax.xml.crypto.KeySelectorException;
/*  11:    */ import javax.xml.crypto.KeySelectorResult;
/*  12:    */ import javax.xml.crypto.XMLCryptoContext;
/*  13:    */ import javax.xml.crypto.XMLStructure;
/*  14:    */ import javax.xml.crypto.dsig.keyinfo.KeyInfo;
/*  15:    */ import javax.xml.crypto.dsig.keyinfo.X509Data;
/*  16:    */ import org.apache.poi.util.POILogFactory;
/*  17:    */ import org.apache.poi.util.POILogger;
/*  18:    */ 
/*  19:    */ public class KeyInfoKeySelector
/*  20:    */   extends KeySelector
/*  21:    */   implements KeySelectorResult
/*  22:    */ {
/*  23: 50 */   private static final POILogger LOG = POILogFactory.getLogger(KeyInfoKeySelector.class);
/*  24: 52 */   private List<X509Certificate> certChain = new ArrayList();
/*  25:    */   
/*  26:    */   public KeySelectorResult select(KeyInfo keyInfo, Purpose purpose, AlgorithmMethod method, XMLCryptoContext context)
/*  27:    */     throws KeySelectorException
/*  28:    */   {
/*  29: 57 */     LOG.log(1, new Object[] { "select key" });
/*  30: 58 */     if (null == keyInfo) {
/*  31: 59 */       throw new KeySelectorException("no ds:KeyInfo present");
/*  32:    */     }
/*  33: 61 */     List<XMLStructure> keyInfoContent = keyInfo.getContent();
/*  34: 62 */     this.certChain.clear();
/*  35: 63 */     for (XMLStructure keyInfoStructure : keyInfoContent) {
/*  36: 64 */       if ((keyInfoStructure instanceof X509Data))
/*  37:    */       {
/*  38: 67 */         X509Data x509Data = (X509Data)keyInfoStructure;
/*  39: 68 */         List<?> x509DataList = x509Data.getContent();
/*  40: 69 */         for (Object x509DataObject : x509DataList) {
/*  41: 70 */           if ((x509DataObject instanceof X509Certificate))
/*  42:    */           {
/*  43: 73 */             X509Certificate certificate = (X509Certificate)x509DataObject;
/*  44: 74 */             LOG.log(1, new Object[] { "certificate", certificate.getSubjectX500Principal() });
/*  45: 75 */             this.certChain.add(certificate);
/*  46:    */           }
/*  47:    */         }
/*  48:    */       }
/*  49:    */     }
/*  50: 78 */     if (this.certChain.isEmpty()) {
/*  51: 79 */       throw new KeySelectorException("No key found!");
/*  52:    */     }
/*  53: 81 */     return this;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public Key getKey()
/*  57:    */   {
/*  58: 86 */     return this.certChain.isEmpty() ? null : ((X509Certificate)this.certChain.get(0)).getPublicKey();
/*  59:    */   }
/*  60:    */   
/*  61:    */   public X509Certificate getSigner()
/*  62:    */   {
/*  63: 97 */     return this.certChain.isEmpty() ? null : (X509Certificate)this.certChain.get(0);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public List<X509Certificate> getCertChain()
/*  67:    */   {
/*  68:101 */     return this.certChain;
/*  69:    */   }
/*  70:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.KeyInfoKeySelector

 * JD-Core Version:    0.7.0.1

 */