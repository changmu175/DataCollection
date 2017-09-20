/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ public class DocumentDescriptor
/*   4:    */ {
/*   5:    */   private POIFSDocumentPath path;
/*   6:    */   private String name;
/*   7: 33 */   private int hashcode = 0;
/*   8:    */   
/*   9:    */   public DocumentDescriptor(POIFSDocumentPath path, String name)
/*  10:    */   {
/*  11: 44 */     if (path == null) {
/*  12: 46 */       throw new NullPointerException("path must not be null");
/*  13:    */     }
/*  14: 48 */     if (name == null) {
/*  15: 50 */       throw new NullPointerException("name must not be null");
/*  16:    */     }
/*  17: 52 */     if (name.length() == 0) {
/*  18: 54 */       throw new IllegalArgumentException("name cannot be empty");
/*  19:    */     }
/*  20: 56 */     this.path = path;
/*  21: 57 */     this.name = name;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public boolean equals(Object o)
/*  25:    */   {
/*  26: 71 */     boolean rval = false;
/*  27: 73 */     if ((o != null) && (o.getClass() == getClass())) {
/*  28: 75 */       if (this == o)
/*  29:    */       {
/*  30: 77 */         rval = true;
/*  31:    */       }
/*  32:    */       else
/*  33:    */       {
/*  34: 81 */         DocumentDescriptor descriptor = (DocumentDescriptor)o;
/*  35:    */         
/*  36: 83 */         rval = (this.path.equals(descriptor.path)) && (this.name.equals(descriptor.name));
/*  37:    */       }
/*  38:    */     }
/*  39: 87 */     return rval;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int hashCode()
/*  43:    */   {
/*  44: 98 */     if (this.hashcode == 0) {
/*  45:100 */       this.hashcode = (this.path.hashCode() ^ this.name.hashCode());
/*  46:    */     }
/*  47:102 */     return this.hashcode;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public String toString()
/*  51:    */   {
/*  52:107 */     StringBuffer buffer = new StringBuffer(40 * (this.path.length() + 1));
/*  53:109 */     for (int j = 0; j < this.path.length(); j++) {
/*  54:111 */       buffer.append(this.path.getComponent(j)).append("/");
/*  55:    */     }
/*  56:113 */     buffer.append(this.name);
/*  57:114 */     return buffer.toString();
/*  58:    */   }
/*  59:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.DocumentDescriptor
 * JD-Core Version:    0.7.0.1
 */