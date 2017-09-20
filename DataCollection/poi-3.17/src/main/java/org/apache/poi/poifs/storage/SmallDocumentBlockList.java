/*  1:   */ package org.apache.poi.poifs.storage;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ 
/*  5:   */ public class SmallDocumentBlockList
/*  6:   */   extends BlockListImpl
/*  7:   */ {
/*  8:   */   public SmallDocumentBlockList(List<SmallDocumentBlock> blocks)
/*  9:   */   {
/* 10:38 */     setBlocks((ListManagedBlock[])blocks.toArray(new SmallDocumentBlock[blocks.size()]));
/* 11:   */   }
/* 12:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.SmallDocumentBlockList
 * JD-Core Version:    0.7.0.1
 */