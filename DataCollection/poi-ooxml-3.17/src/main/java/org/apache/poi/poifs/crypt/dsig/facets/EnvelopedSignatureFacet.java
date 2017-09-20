/*  1:   */ package org.apache.poi.poifs.crypt.dsig.facets;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import javax.xml.crypto.dsig.Reference;
/*  6:   */ import javax.xml.crypto.dsig.Transform;
/*  7:   */ import javax.xml.crypto.dsig.XMLObject;
/*  8:   */ import javax.xml.crypto.dsig.XMLSignatureException;
/*  9:   */ import org.w3c.dom.Document;
/* 10:   */ 
/* 11:   */ public class EnvelopedSignatureFacet
/* 12:   */   extends SignatureFacet
/* 13:   */ {
/* 14:   */   public void preSign(Document document, List<Reference> references, List<XMLObject> objects)
/* 15:   */     throws XMLSignatureException
/* 16:   */   {
/* 17:51 */     List<Transform> transforms = new ArrayList();
/* 18:52 */     Transform envelopedTransform = newTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature");
/* 19:53 */     transforms.add(envelopedTransform);
/* 20:54 */     Transform exclusiveTransform = newTransform("http://www.w3.org/2001/10/xml-exc-c14n#");
/* 21:55 */     transforms.add(exclusiveTransform);
/* 22:   */     
/* 23:57 */     Reference reference = newReference("", transforms, null, null, null);
/* 24:58 */     references.add(reference);
/* 25:   */   }
/* 26:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.facets.EnvelopedSignatureFacet
 * JD-Core Version:    0.7.0.1
 */