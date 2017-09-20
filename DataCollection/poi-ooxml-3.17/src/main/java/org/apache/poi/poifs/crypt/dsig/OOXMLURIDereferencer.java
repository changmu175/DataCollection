/*   1:    */ package org.apache.poi.poifs.crypt.dsig;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.ByteArrayOutputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.net.URI;
/*   8:    */ import java.net.URISyntaxException;
/*   9:    */ import javax.xml.crypto.Data;
/*  10:    */ import javax.xml.crypto.OctetStreamData;
/*  11:    */ import javax.xml.crypto.URIDereferencer;
/*  12:    */ import javax.xml.crypto.URIReference;
/*  13:    */ import javax.xml.crypto.URIReferenceException;
/*  14:    */ import javax.xml.crypto.XMLCryptoContext;
/*  15:    */ import javax.xml.crypto.dsig.XMLSignatureFactory;
/*  16:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  17:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  18:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  19:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  20:    */ import org.apache.poi.openxml4j.opc.PackagingURIHelper;
/*  21:    */ import org.apache.poi.util.POILogFactory;
/*  22:    */ import org.apache.poi.util.POILogger;
/*  23:    */ 
/*  24:    */ public class OOXMLURIDereferencer
/*  25:    */   implements URIDereferencer, SignatureConfig.SignatureConfigurable
/*  26:    */ {
/*  27: 54 */   private static final POILogger LOG = POILogFactory.getLogger(OOXMLURIDereferencer.class);
/*  28:    */   private SignatureConfig signatureConfig;
/*  29:    */   private URIDereferencer baseUriDereferencer;
/*  30:    */   
/*  31:    */   public void setSignatureConfig(SignatureConfig signatureConfig)
/*  32:    */   {
/*  33: 60 */     this.signatureConfig = signatureConfig;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public Data dereference(URIReference uriReference, XMLCryptoContext context)
/*  37:    */     throws URIReferenceException
/*  38:    */   {
/*  39: 64 */     if (this.baseUriDereferencer == null) {
/*  40: 65 */       this.baseUriDereferencer = this.signatureConfig.getSignatureFactory().getURIDereferencer();
/*  41:    */     }
/*  42: 68 */     if (null == uriReference) {
/*  43: 69 */       throw new NullPointerException("URIReference cannot be null");
/*  44:    */     }
/*  45: 71 */     if (null == context) {
/*  46: 72 */       throw new NullPointerException("XMLCrytoContext cannot be null");
/*  47:    */     }
/*  48:    */     URI uri;
/*  49:    */     try
/*  50:    */     {
/*  51: 77 */       uri = new URI(uriReference.getURI());
/*  52:    */     }
/*  53:    */     catch (URISyntaxException e)
/*  54:    */     {
/*  55: 79 */       throw new URIReferenceException("could not URL decode the uri: " + uriReference.getURI(), e);
/*  56:    */     }
/*  57: 82 */     PackagePart part = findPart(uri);
/*  58: 83 */     if (part == null)
/*  59:    */     {
/*  60: 84 */       LOG.log(1, new Object[] { "cannot resolve, delegating to base DOM URI dereferencer", uri });
/*  61: 85 */       return this.baseUriDereferencer.dereference(uriReference, context);
/*  62:    */     }
/*  63:    */     InputStream dataStream;
/*  64:    */     try
/*  65:    */     {
/*  66: 90 */       dataStream = part.getInputStream();
/*  67: 93 */       if (part.getPartName().toString().endsWith(".rels"))
/*  68:    */       {
/*  69: 97 */         ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  70:    */         int ch;
/*  71: 98 */         while ((ch = dataStream.read()) != -1) {
/*  72: 99 */           if ((ch != 10) && (ch != 13)) {
/*  73:100 */             bos.write(ch);
/*  74:    */           }
/*  75:    */         }
/*  76:102 */         dataStream = new ByteArrayInputStream(bos.toByteArray());
/*  77:    */       }
/*  78:    */     }
/*  79:    */     catch (IOException e)
/*  80:    */     {
/*  81:105 */       throw new URIReferenceException("I/O error: " + e.getMessage(), e);
/*  82:    */     }
/*  83:108 */     return new OctetStreamData(dataStream, uri.toString(), null);
/*  84:    */   }
/*  85:    */   
/*  86:    */   private PackagePart findPart(URI uri)
/*  87:    */   {
/*  88:112 */     LOG.log(1, new Object[] { "dereference", uri });
/*  89:    */     
/*  90:114 */     String path = uri.getPath();
/*  91:115 */     if ((path == null) || ("".equals(path)))
/*  92:    */     {
/*  93:116 */       LOG.log(1, new Object[] { "illegal part name (expected)", uri });
/*  94:117 */       return null;
/*  95:    */     }
/*  96:    */     PackagePartName ppn;
/*  97:    */     try
/*  98:    */     {
/*  99:122 */       ppn = PackagingURIHelper.createPartName(path);
/* 100:    */     }
/* 101:    */     catch (InvalidFormatException e)
/* 102:    */     {
/* 103:124 */       LOG.log(5, new Object[] { "illegal part name (not expected)", uri });
/* 104:125 */       return null;
/* 105:    */     }
/* 106:128 */     return this.signatureConfig.getOpcPackage().getPart(ppn);
/* 107:    */   }
/* 108:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.OOXMLURIDereferencer
 * JD-Core Version:    0.7.0.1
 */