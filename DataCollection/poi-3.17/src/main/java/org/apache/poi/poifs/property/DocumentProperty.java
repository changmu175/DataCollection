/*   1:    */ package org.apache.poi.poifs.property;
/*   2:    */ 
/*   3:    */ import org.apache.poi.poifs.filesystem.OPOIFSDocument;
/*   4:    */ 
/*   5:    */ public class DocumentProperty
/*   6:    */   extends Property
/*   7:    */ {
/*   8:    */   private OPOIFSDocument _document;
/*   9:    */   
/*  10:    */   public DocumentProperty(String name, int size)
/*  11:    */   {
/*  12: 41 */     this._document = null;
/*  13: 42 */     setName(name);
/*  14: 43 */     setSize(size);
/*  15: 44 */     setNodeColor((byte)1);
/*  16: 45 */     setPropertyType((byte)2);
/*  17:    */   }
/*  18:    */   
/*  19:    */   protected DocumentProperty(int index, byte[] array, int offset)
/*  20:    */   {
/*  21: 58 */     super(index, array, offset);
/*  22: 59 */     this._document = null;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setDocument(OPOIFSDocument doc)
/*  26:    */   {
/*  27: 69 */     this._document = doc;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public OPOIFSDocument getDocument()
/*  31:    */   {
/*  32: 79 */     return this._document;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public boolean shouldUseSmallBlocks()
/*  36:    */   {
/*  37: 91 */     return super.shouldUseSmallBlocks();
/*  38:    */   }
/*  39:    */   
/*  40:    */   public boolean isDirectory()
/*  41:    */   {
/*  42: 99 */     return false;
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected void preWrite() {}
/*  46:    */   
/*  47:    */   public void updateSize(int size)
/*  48:    */   {
/*  49:117 */     setSize(size);
/*  50:    */   }
/*  51:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.property.DocumentProperty
 * JD-Core Version:    0.7.0.1
 */