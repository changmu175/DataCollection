/*   1:    */ package org.apache.poi.poifs.crypt.dsig.services;
/*   2:    */ 
/*   3:    */ import java.security.cert.CRLException;
/*   4:    */ import java.security.cert.X509CRL;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.List;
/*   7:    */ 
/*   8:    */ public class RevocationData
/*   9:    */ {
/*  10:    */   private final List<byte[]> crls;
/*  11:    */   private final List<byte[]> ocsps;
/*  12:    */   
/*  13:    */   public RevocationData()
/*  14:    */   {
/*  15: 48 */     this.crls = new ArrayList();
/*  16: 49 */     this.ocsps = new ArrayList();
/*  17:    */   }
/*  18:    */   
/*  19:    */   public void addCRL(byte[] encodedCrl)
/*  20:    */   {
/*  21: 58 */     this.crls.add(encodedCrl);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void addCRL(X509CRL crl)
/*  25:    */   {
/*  26:    */     byte[] encodedCrl;
/*  27:    */     try
/*  28:    */     {
/*  29: 69 */       encodedCrl = crl.getEncoded();
/*  30:    */     }
/*  31:    */     catch (CRLException e)
/*  32:    */     {
/*  33: 71 */       throw new IllegalArgumentException("CRL coding error: " + e.getMessage(), e);
/*  34:    */     }
/*  35: 74 */     addCRL(encodedCrl);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void addOCSP(byte[] encodedOcsp)
/*  39:    */   {
/*  40: 83 */     this.ocsps.add(encodedOcsp);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public List<byte[]> getCRLs()
/*  44:    */   {
/*  45: 92 */     return this.crls;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public List<byte[]> getOCSPs()
/*  49:    */   {
/*  50:101 */     return this.ocsps;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public boolean hasOCSPs()
/*  54:    */   {
/*  55:112 */     return false == this.ocsps.isEmpty();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public boolean hasCRLs()
/*  59:    */   {
/*  60:121 */     return false == this.crls.isEmpty();
/*  61:    */   }
/*  62:    */   
/*  63:    */   public boolean hasRevocationDataEntries()
/*  64:    */   {
/*  65:130 */     return (hasOCSPs()) || (hasCRLs());
/*  66:    */   }
/*  67:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.services.RevocationData
 * JD-Core Version:    0.7.0.1
 */