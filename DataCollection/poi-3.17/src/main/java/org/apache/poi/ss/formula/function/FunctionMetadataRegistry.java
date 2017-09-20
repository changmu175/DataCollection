/*  1:   */ package org.apache.poi.ss.formula.function;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import java.util.Set;
/*  5:   */ 
/*  6:   */ public final class FunctionMetadataRegistry
/*  7:   */ {
/*  8:   */   public static final String FUNCTION_NAME_IF = "IF";
/*  9:   */   public static final int FUNCTION_INDEX_IF = 1;
/* 10:   */   public static final short FUNCTION_INDEX_SUM = 4;
/* 11:   */   public static final int FUNCTION_INDEX_CHOOSE = 100;
/* 12:   */   public static final short FUNCTION_INDEX_INDIRECT = 148;
/* 13:   */   public static final short FUNCTION_INDEX_EXTERNAL = 255;
/* 14:   */   private static FunctionMetadataRegistry _instance;
/* 15:   */   private final FunctionMetadata[] _functionDataByIndex;
/* 16:   */   private final Map<String, FunctionMetadata> _functionDataByName;
/* 17:   */   
/* 18:   */   private static FunctionMetadataRegistry getInstance()
/* 19:   */   {
/* 20:45 */     if (_instance == null) {
/* 21:46 */       _instance = FunctionMetadataReader.createRegistry();
/* 22:   */     }
/* 23:48 */     return _instance;
/* 24:   */   }
/* 25:   */   
/* 26:   */   FunctionMetadataRegistry(FunctionMetadata[] functionDataByIndex, Map<String, FunctionMetadata> functionDataByName)
/* 27:   */   {
/* 28:52 */     this._functionDataByIndex = (functionDataByIndex == null ? null : (FunctionMetadata[])functionDataByIndex.clone());
/* 29:53 */     this._functionDataByName = functionDataByName;
/* 30:   */   }
/* 31:   */   
/* 32:   */   Set<String> getAllFunctionNames()
/* 33:   */   {
/* 34:57 */     return this._functionDataByName.keySet();
/* 35:   */   }
/* 36:   */   
/* 37:   */   public static FunctionMetadata getFunctionByIndex(int index)
/* 38:   */   {
/* 39:62 */     return getInstance().getFunctionByIndexInternal(index);
/* 40:   */   }
/* 41:   */   
/* 42:   */   private FunctionMetadata getFunctionByIndexInternal(int index)
/* 43:   */   {
/* 44:66 */     return this._functionDataByIndex[index];
/* 45:   */   }
/* 46:   */   
/* 47:   */   public static short lookupIndexByName(String name)
/* 48:   */   {
/* 49:75 */     FunctionMetadata fd = getInstance().getFunctionByNameInternal(name);
/* 50:76 */     if (fd == null) {
/* 51:77 */       return -1;
/* 52:   */     }
/* 53:79 */     return (short)fd.getIndex();
/* 54:   */   }
/* 55:   */   
/* 56:   */   private FunctionMetadata getFunctionByNameInternal(String name)
/* 57:   */   {
/* 58:83 */     return (FunctionMetadata)this._functionDataByName.get(name);
/* 59:   */   }
/* 60:   */   
/* 61:   */   public static FunctionMetadata getFunctionByName(String name)
/* 62:   */   {
/* 63:88 */     return getInstance().getFunctionByNameInternal(name);
/* 64:   */   }
/* 65:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.function.FunctionMetadataRegistry
 * JD-Core Version:    0.7.0.1
 */