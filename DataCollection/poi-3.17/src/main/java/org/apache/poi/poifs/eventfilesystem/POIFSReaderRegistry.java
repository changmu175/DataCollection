/*   1:    */ package org.apache.poi.poifs.eventfilesystem;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Set;
/*   8:    */ import org.apache.poi.poifs.filesystem.DocumentDescriptor;
/*   9:    */ import org.apache.poi.poifs.filesystem.POIFSDocumentPath;
/*  10:    */ 
/*  11:    */ class POIFSReaderRegistry
/*  12:    */ {
/*  13:    */   private Set<POIFSReaderListener> omnivorousListeners;
/*  14:    */   private Map<POIFSReaderListener, Set<DocumentDescriptor>> selectiveListeners;
/*  15:    */   private Map<DocumentDescriptor, Set<POIFSReaderListener>> chosenDocumentDescriptors;
/*  16:    */   
/*  17:    */   POIFSReaderRegistry()
/*  18:    */   {
/*  19: 60 */     this.omnivorousListeners = new HashSet();
/*  20: 61 */     this.selectiveListeners = new HashMap();
/*  21: 62 */     this.chosenDocumentDescriptors = new HashMap();
/*  22:    */   }
/*  23:    */   
/*  24:    */   void registerListener(POIFSReaderListener listener, POIFSDocumentPath path, String documentName)
/*  25:    */   {
/*  26: 77 */     if (!this.omnivorousListeners.contains(listener))
/*  27:    */     {
/*  28: 82 */       Set<DocumentDescriptor> descriptors = (Set)this.selectiveListeners.get(listener);
/*  29: 84 */       if (descriptors == null)
/*  30:    */       {
/*  31: 88 */         descriptors = new HashSet();
/*  32: 89 */         this.selectiveListeners.put(listener, descriptors);
/*  33:    */       }
/*  34: 91 */       DocumentDescriptor descriptor = new DocumentDescriptor(path, documentName);
/*  35: 94 */       if (descriptors.add(descriptor))
/*  36:    */       {
/*  37:100 */         Set<POIFSReaderListener> listeners = (Set)this.chosenDocumentDescriptors.get(descriptor);
/*  38:103 */         if (listeners == null)
/*  39:    */         {
/*  40:107 */           listeners = new HashSet();
/*  41:108 */           this.chosenDocumentDescriptors.put(descriptor, listeners);
/*  42:    */         }
/*  43:110 */         listeners.add(listener);
/*  44:    */       }
/*  45:    */     }
/*  46:    */   }
/*  47:    */   
/*  48:    */   void registerListener(POIFSReaderListener listener)
/*  49:    */   {
/*  50:123 */     if (!this.omnivorousListeners.contains(listener))
/*  51:    */     {
/*  52:130 */       removeSelectiveListener(listener);
/*  53:131 */       this.omnivorousListeners.add(listener);
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   Iterator<POIFSReaderListener> getListeners(POIFSDocumentPath path, String name)
/*  58:    */   {
/*  59:146 */     Set<POIFSReaderListener> rval = new HashSet(this.omnivorousListeners);
/*  60:147 */     Set<POIFSReaderListener> selectiveListenersInner = (Set)this.chosenDocumentDescriptors.get(new DocumentDescriptor(path, name));
/*  61:150 */     if (selectiveListenersInner != null) {
/*  62:152 */       rval.addAll(selectiveListenersInner);
/*  63:    */     }
/*  64:154 */     return rval.iterator();
/*  65:    */   }
/*  66:    */   
/*  67:    */   private void removeSelectiveListener(POIFSReaderListener listener)
/*  68:    */   {
/*  69:159 */     Set<DocumentDescriptor> selectedDescriptors = (Set)this.selectiveListeners.remove(listener);
/*  70:161 */     if (selectedDescriptors != null)
/*  71:    */     {
/*  72:163 */       Iterator<DocumentDescriptor> iter = selectedDescriptors.iterator();
/*  73:165 */       while (iter.hasNext()) {
/*  74:167 */         dropDocument(listener, (DocumentDescriptor)iter.next());
/*  75:    */       }
/*  76:    */     }
/*  77:    */   }
/*  78:    */   
/*  79:    */   private void dropDocument(POIFSReaderListener listener, DocumentDescriptor descriptor)
/*  80:    */   {
/*  81:175 */     Set<POIFSReaderListener> listeners = (Set)this.chosenDocumentDescriptors.get(descriptor);
/*  82:    */     
/*  83:177 */     listeners.remove(listener);
/*  84:178 */     if (listeners.size() == 0) {
/*  85:180 */       this.chosenDocumentDescriptors.remove(descriptor);
/*  86:    */     }
/*  87:    */   }
/*  88:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.eventfilesystem.POIFSReaderRegistry
 * JD-Core Version:    0.7.0.1
 */