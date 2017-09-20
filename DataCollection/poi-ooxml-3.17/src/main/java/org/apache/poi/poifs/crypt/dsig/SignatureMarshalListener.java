/*  1:   */ package org.apache.poi.poifs.crypt.dsig;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import org.w3c.dom.Element;
/*  5:   */ import org.w3c.dom.Node;
/*  6:   */ import org.w3c.dom.NodeList;
/*  7:   */ import org.w3c.dom.events.Event;
/*  8:   */ import org.w3c.dom.events.EventListener;
/*  9:   */ import org.w3c.dom.events.EventTarget;
/* 10:   */ import org.w3c.dom.events.MutationEvent;
/* 11:   */ 
/* 12:   */ public class SignatureMarshalListener
/* 13:   */   implements EventListener, SignatureConfig.SignatureConfigurable
/* 14:   */ {
/* 15:37 */   ThreadLocal<EventTarget> target = new ThreadLocal();
/* 16:   */   SignatureConfig signatureConfig;
/* 17:   */   
/* 18:   */   public void setEventTarget(EventTarget target)
/* 19:   */   {
/* 20:40 */     this.target.set(target);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void handleEvent(Event e)
/* 24:   */   {
/* 25:45 */     if (!(e instanceof MutationEvent)) {
/* 26:46 */       return;
/* 27:   */     }
/* 28:48 */     MutationEvent mutEvt = (MutationEvent)e;
/* 29:49 */     EventTarget et = mutEvt.getTarget();
/* 30:50 */     if (!(et instanceof Element)) {
/* 31:51 */       return;
/* 32:   */     }
/* 33:53 */     handleElement((Element)et);
/* 34:   */   }
/* 35:   */   
/* 36:   */   public void handleElement(Element el)
/* 37:   */   {
/* 38:57 */     EventTarget target = (EventTarget)this.target.get();
/* 39:59 */     if (el.hasAttribute("Id")) {
/* 40:60 */       el.setIdAttribute("Id", true);
/* 41:   */     }
/* 42:63 */     setListener(target, this, false);
/* 43:64 */     if ("http://schemas.openxmlformats.org/package/2006/digital-signature".equals(el.getNamespaceURI()))
/* 44:   */     {
/* 45:65 */       String parentNS = el.getParentNode().getNamespaceURI();
/* 46:66 */       if ((!"http://schemas.openxmlformats.org/package/2006/digital-signature".equals(parentNS)) && (!el.hasAttributeNS("http://www.w3.org/2000/xmlns/", "mdssi"))) {
/* 47:67 */         el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:mdssi", "http://schemas.openxmlformats.org/package/2006/digital-signature");
/* 48:   */       }
/* 49:   */     }
/* 50:70 */     setPrefix(el);
/* 51:71 */     setListener(target, this, true);
/* 52:   */   }
/* 53:   */   
/* 54:   */   public static void setListener(EventTarget target, EventListener listener, boolean enabled)
/* 55:   */   {
/* 56:76 */     String type = "DOMSubtreeModified";
/* 57:77 */     boolean useCapture = false;
/* 58:78 */     if (enabled) {
/* 59:79 */       target.addEventListener(type, listener, useCapture);
/* 60:   */     } else {
/* 61:81 */       target.removeEventListener(type, listener, useCapture);
/* 62:   */     }
/* 63:   */   }
/* 64:   */   
/* 65:   */   protected void setPrefix(Node el)
/* 66:   */   {
/* 67:86 */     String prefix = (String)this.signatureConfig.getNamespacePrefixes().get(el.getNamespaceURI());
/* 68:87 */     if ((prefix != null) && (el.getPrefix() == null)) {
/* 69:88 */       el.setPrefix(prefix);
/* 70:   */     }
/* 71:91 */     NodeList nl = el.getChildNodes();
/* 72:92 */     for (int i = 0; i < nl.getLength(); i++) {
/* 73:93 */       setPrefix(nl.item(i));
/* 74:   */     }
/* 75:   */   }
/* 76:   */   
/* 77:   */   public void setSignatureConfig(SignatureConfig signatureConfig)
/* 78:   */   {
/* 79:99 */     this.signatureConfig = signatureConfig;
/* 80:   */   }
/* 81:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.SignatureMarshalListener
 * JD-Core Version:    0.7.0.1
 */