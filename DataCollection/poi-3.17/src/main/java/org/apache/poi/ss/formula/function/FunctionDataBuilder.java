/*  1:   */ package org.apache.poi.ss.formula.function;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import java.util.HashMap;
/*  5:   */ import java.util.HashSet;
/*  6:   */ import java.util.Map;
/*  7:   */ import java.util.Set;
/*  8:   */ 
/*  9:   */ final class FunctionDataBuilder
/* 10:   */ {
/* 11:   */   private int _maxFunctionIndex;
/* 12:   */   private final Map<String, FunctionMetadata> _functionDataByName;
/* 13:   */   private final Map<Integer, FunctionMetadata> _functionDataByIndex;
/* 14:   */   private final Set<Integer> _mutatingFunctionIndexes;
/* 15:   */   
/* 16:   */   public FunctionDataBuilder(int sizeEstimate)
/* 17:   */   {
/* 18:39 */     this._maxFunctionIndex = -1;
/* 19:40 */     this._functionDataByName = new HashMap(sizeEstimate * 3 / 2);
/* 20:41 */     this._functionDataByIndex = new HashMap(sizeEstimate * 3 / 2);
/* 21:42 */     this._mutatingFunctionIndexes = new HashSet();
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void add(int functionIndex, String functionName, int minParams, int maxParams, byte returnClassCode, byte[] parameterClassCodes, boolean hasFootnote)
/* 25:   */   {
/* 26:47 */     FunctionMetadata fm = new FunctionMetadata(functionIndex, functionName, minParams, maxParams, returnClassCode, parameterClassCodes);
/* 27:   */     
/* 28:   */ 
/* 29:50 */     Integer indexKey = Integer.valueOf(functionIndex);
/* 30:53 */     if (functionIndex > this._maxFunctionIndex) {
/* 31:54 */       this._maxFunctionIndex = functionIndex;
/* 32:   */     }
/* 33:58 */     FunctionMetadata prevFM = (FunctionMetadata)this._functionDataByName.get(functionName);
/* 34:59 */     if (prevFM != null)
/* 35:   */     {
/* 36:60 */       if ((!hasFootnote) || (!this._mutatingFunctionIndexes.contains(indexKey))) {
/* 37:61 */         throw new RuntimeException("Multiple entries for function name '" + functionName + "'");
/* 38:   */       }
/* 39:63 */       this._functionDataByIndex.remove(Integer.valueOf(prevFM.getIndex()));
/* 40:   */     }
/* 41:65 */     prevFM = (FunctionMetadata)this._functionDataByIndex.get(indexKey);
/* 42:66 */     if (prevFM != null)
/* 43:   */     {
/* 44:67 */       if ((!hasFootnote) || (!this._mutatingFunctionIndexes.contains(indexKey))) {
/* 45:68 */         throw new RuntimeException("Multiple entries for function index (" + functionIndex + ")");
/* 46:   */       }
/* 47:70 */       this._functionDataByName.remove(prevFM.getName());
/* 48:   */     }
/* 49:72 */     if (hasFootnote) {
/* 50:73 */       this._mutatingFunctionIndexes.add(indexKey);
/* 51:   */     }
/* 52:75 */     this._functionDataByIndex.put(indexKey, fm);
/* 53:76 */     this._functionDataByName.put(functionName, fm);
/* 54:   */   }
/* 55:   */   
/* 56:   */   public FunctionMetadataRegistry build()
/* 57:   */   {
/* 58:81 */     FunctionMetadata[] jumbledArray = new FunctionMetadata[this._functionDataByName.size()];
/* 59:82 */     this._functionDataByName.values().toArray(jumbledArray);
/* 60:83 */     FunctionMetadata[] fdIndexArray = new FunctionMetadata[this._maxFunctionIndex + 1];
/* 61:84 */     for (int i = 0; i < jumbledArray.length; i++)
/* 62:   */     {
/* 63:85 */       FunctionMetadata fd = jumbledArray[i];
/* 64:86 */       fdIndexArray[fd.getIndex()] = fd;
/* 65:   */     }
/* 66:89 */     return new FunctionMetadataRegistry(fdIndexArray, this._functionDataByName);
/* 67:   */   }
/* 68:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.function.FunctionDataBuilder
 * JD-Core Version:    0.7.0.1
 */