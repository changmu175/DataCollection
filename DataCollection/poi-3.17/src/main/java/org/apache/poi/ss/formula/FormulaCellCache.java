/*  1:   */ package org.apache.poi.ss.formula;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import java.util.HashMap;
/*  5:   */ import java.util.Iterator;
/*  6:   */ import java.util.Map;
/*  7:   */ 
/*  8:   */ final class FormulaCellCache
/*  9:   */ {
/* 10:   */   private final Map<Object, FormulaCellCacheEntry> _formulaEntriesByCell;
/* 11:   */   
/* 12:   */   public FormulaCellCache()
/* 13:   */   {
/* 14:38 */     this._formulaEntriesByCell = new HashMap();
/* 15:   */   }
/* 16:   */   
/* 17:   */   public CellCacheEntry[] getCacheEntries()
/* 18:   */   {
/* 19:43 */     FormulaCellCacheEntry[] result = new FormulaCellCacheEntry[this._formulaEntriesByCell.size()];
/* 20:44 */     this._formulaEntriesByCell.values().toArray(result);
/* 21:45 */     return result;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void clear()
/* 25:   */   {
/* 26:49 */     this._formulaEntriesByCell.clear();
/* 27:   */   }
/* 28:   */   
/* 29:   */   public FormulaCellCacheEntry get(EvaluationCell cell)
/* 30:   */   {
/* 31:56 */     return (FormulaCellCacheEntry)this._formulaEntriesByCell.get(cell.getIdentityKey());
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void put(EvaluationCell cell, FormulaCellCacheEntry entry)
/* 35:   */   {
/* 36:60 */     this._formulaEntriesByCell.put(cell.getIdentityKey(), entry);
/* 37:   */   }
/* 38:   */   
/* 39:   */   public FormulaCellCacheEntry remove(EvaluationCell cell)
/* 40:   */   {
/* 41:64 */     return (FormulaCellCacheEntry)this._formulaEntriesByCell.remove(cell.getIdentityKey());
/* 42:   */   }
/* 43:   */   
/* 44:   */   public void applyOperation(IEntryOperation operation)
/* 45:   */   {
/* 46:68 */     Iterator<FormulaCellCacheEntry> i = this._formulaEntriesByCell.values().iterator();
/* 47:69 */     while (i.hasNext()) {
/* 48:70 */       operation.processEntry((FormulaCellCacheEntry)i.next());
/* 49:   */     }
/* 50:   */   }
/* 51:   */   
/* 52:   */   static abstract interface IEntryOperation
/* 53:   */   {
/* 54:   */     public abstract void processEntry(FormulaCellCacheEntry paramFormulaCellCacheEntry);
/* 55:   */   }
/* 56:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.FormulaCellCache
 * JD-Core Version:    0.7.0.1
 */