/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.poifs.dev.POIFSViewable;
/*   7:    */ import org.apache.poi.poifs.property.DocumentProperty;
/*   8:    */ import org.apache.poi.poifs.property.Property;
/*   9:    */ 
/*  10:    */ public class DocumentNode
/*  11:    */   extends EntryNode
/*  12:    */   implements DocumentEntry, POIFSViewable
/*  13:    */ {
/*  14:    */   private OPOIFSDocument _document;
/*  15:    */   
/*  16:    */   DocumentNode(DocumentProperty property, DirectoryNode parent)
/*  17:    */   {
/*  18: 50 */     super(property, parent);
/*  19: 51 */     this._document = property.getDocument();
/*  20:    */   }
/*  21:    */   
/*  22:    */   OPOIFSDocument getDocument()
/*  23:    */   {
/*  24: 61 */     return this._document;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public int getSize()
/*  28:    */   {
/*  29: 74 */     return getProperty().getSize();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public boolean isDocumentEntry()
/*  33:    */   {
/*  34: 89 */     return true;
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected boolean isDeleteOK()
/*  38:    */   {
/*  39:106 */     return true;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public Object[] getViewableArray()
/*  43:    */   {
/*  44:121 */     return new Object[0];
/*  45:    */   }
/*  46:    */   
/*  47:    */   public Iterator<Object> getViewableIterator()
/*  48:    */   {
/*  49:134 */     List<Object> components = new ArrayList();
/*  50:    */     
/*  51:136 */     components.add(getProperty());
/*  52:137 */     if (this._document != null) {
/*  53:138 */       components.add(this._document);
/*  54:    */     }
/*  55:140 */     return components.iterator();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public boolean preferArray()
/*  59:    */   {
/*  60:153 */     return false;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public String getShortDescription()
/*  64:    */   {
/*  65:165 */     return getName();
/*  66:    */   }
/*  67:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.DocumentNode
 * JD-Core Version:    0.7.0.1
 */