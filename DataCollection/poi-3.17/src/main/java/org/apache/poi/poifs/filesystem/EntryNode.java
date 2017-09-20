/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import org.apache.poi.poifs.property.Property;
/*   4:    */ 
/*   5:    */ public abstract class EntryNode
/*   6:    */   implements Entry
/*   7:    */ {
/*   8:    */   private Property _property;
/*   9:    */   private DirectoryNode _parent;
/*  10:    */   
/*  11:    */   protected EntryNode(Property property, DirectoryNode parent)
/*  12:    */   {
/*  13: 55 */     this._property = property;
/*  14: 56 */     this._parent = parent;
/*  15:    */   }
/*  16:    */   
/*  17:    */   protected Property getProperty()
/*  18:    */   {
/*  19: 67 */     return this._property;
/*  20:    */   }
/*  21:    */   
/*  22:    */   protected boolean isRoot()
/*  23:    */   {
/*  24: 80 */     return this._parent == null;
/*  25:    */   }
/*  26:    */   
/*  27:    */   protected abstract boolean isDeleteOK();
/*  28:    */   
/*  29:    */   public String getName()
/*  30:    */   {
/*  31:103 */     return this._property.getName();
/*  32:    */   }
/*  33:    */   
/*  34:    */   public boolean isDirectoryEntry()
/*  35:    */   {
/*  36:114 */     return false;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public boolean isDocumentEntry()
/*  40:    */   {
/*  41:125 */     return false;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public DirectoryEntry getParent()
/*  45:    */   {
/*  46:137 */     return this._parent;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean delete()
/*  50:    */   {
/*  51:155 */     boolean rval = false;
/*  52:157 */     if ((!isRoot()) && (isDeleteOK())) {
/*  53:159 */       rval = this._parent.deleteEntry(this);
/*  54:    */     }
/*  55:161 */     return rval;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public boolean renameTo(String newName)
/*  59:    */   {
/*  60:180 */     boolean rval = false;
/*  61:182 */     if (!isRoot()) {
/*  62:184 */       rval = this._parent.changeName(getName(), newName);
/*  63:    */     }
/*  64:186 */     return rval;
/*  65:    */   }
/*  66:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.EntryNode
 * JD-Core Version:    0.7.0.1
 */