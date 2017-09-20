/*  1:   */ package org.apache.poi.poifs.property;
/*  2:   */ 
/*  3:   */ import org.apache.poi.poifs.storage.SmallDocumentBlock;
/*  4:   */ 
/*  5:   */ public final class RootProperty
/*  6:   */   extends DirectoryProperty
/*  7:   */ {
/*  8:   */   private static final String NAME = "Root Entry";
/*  9:   */   
/* 10:   */   RootProperty()
/* 11:   */   {
/* 12:31 */     super("Root Entry");
/* 13:   */     
/* 14:   */ 
/* 15:34 */     setNodeColor((byte)1);
/* 16:35 */     setPropertyType((byte)5);
/* 17:36 */     setStartBlock(-2);
/* 18:   */   }
/* 19:   */   
/* 20:   */   protected RootProperty(int index, byte[] array, int offset)
/* 21:   */   {
/* 22:49 */     super(index, array, offset);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void setSize(int size)
/* 26:   */   {
/* 27:59 */     super.setSize(SmallDocumentBlock.calcSize(size));
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String getName()
/* 31:   */   {
/* 32:68 */     return "Root Entry";
/* 33:   */   }
/* 34:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.property.RootProperty
 * JD-Core Version:    0.7.0.1
 */