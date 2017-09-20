/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.HashSet;
/*   7:    */ import java.util.IdentityHashMap;
/*   8:    */ import java.util.Iterator;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Map.Entry;
/*  11:    */ import java.util.Set;
/*  12:    */ import org.apache.poi.ss.usermodel.FormulaEvaluator;
/*  13:    */ import org.apache.poi.util.Internal;
/*  14:    */ 
/*  15:    */ @Internal
/*  16:    */ public final class CollaboratingWorkbooksEnvironment
/*  17:    */ {
/*  18:    */   public static final class WorkbookNotFoundException
/*  19:    */     extends Exception
/*  20:    */   {
/*  21:    */     private static final long serialVersionUID = 8787784539811167941L;
/*  22:    */     
/*  23:    */     WorkbookNotFoundException(String msg)
/*  24:    */     {
/*  25: 43 */       super();
/*  26:    */     }
/*  27:    */   }
/*  28:    */   
/*  29: 47 */   public static final CollaboratingWorkbooksEnvironment EMPTY = new CollaboratingWorkbooksEnvironment();
/*  30:    */   private final Map<String, WorkbookEvaluator> _evaluatorsByName;
/*  31:    */   private final WorkbookEvaluator[] _evaluators;
/*  32:    */   private boolean _unhooked;
/*  33:    */   
/*  34:    */   private CollaboratingWorkbooksEnvironment()
/*  35:    */   {
/*  36: 54 */     this._evaluatorsByName = Collections.emptyMap();
/*  37: 55 */     this._evaluators = new WorkbookEvaluator[0];
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static void setup(String[] workbookNames, WorkbookEvaluator[] evaluators)
/*  41:    */   {
/*  42: 59 */     int nItems = workbookNames.length;
/*  43: 60 */     if (evaluators.length != nItems) {
/*  44: 61 */       throw new IllegalArgumentException("Number of workbook names is " + nItems + " but number of evaluators is " + evaluators.length);
/*  45:    */     }
/*  46: 64 */     if (nItems < 1) {
/*  47: 65 */       throw new IllegalArgumentException("Must provide at least one collaborating worbook");
/*  48:    */     }
/*  49: 67 */     new CollaboratingWorkbooksEnvironment(workbookNames, evaluators, nItems);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static void setup(Map<String, WorkbookEvaluator> evaluatorsByName)
/*  53:    */   {
/*  54: 70 */     if (evaluatorsByName.size() < 1) {
/*  55: 71 */       throw new IllegalArgumentException("Must provide at least one collaborating worbook");
/*  56:    */     }
/*  57: 73 */     WorkbookEvaluator[] evaluators = (WorkbookEvaluator[])evaluatorsByName.values().toArray(new WorkbookEvaluator[evaluatorsByName.size()]);
/*  58:    */     
/*  59: 75 */     new CollaboratingWorkbooksEnvironment(evaluatorsByName, evaluators);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public static void setupFormulaEvaluator(Map<String, FormulaEvaluator> evaluators)
/*  63:    */   {
/*  64: 78 */     Map<String, WorkbookEvaluator> evaluatorsByName = new HashMap(evaluators.size());
/*  65: 79 */     for (Entry<String, FormulaEvaluator> swb : evaluators.entrySet())
/*  66:    */     {
/*  67: 80 */       String wbName = (String)swb.getKey();
/*  68: 81 */       FormulaEvaluator eval = (FormulaEvaluator)swb.getValue();
/*  69: 82 */       if ((eval instanceof WorkbookEvaluatorProvider)) {
/*  70: 83 */         evaluatorsByName.put(wbName, ((WorkbookEvaluatorProvider)eval)._getWorkbookEvaluator());
/*  71:    */       } else {
/*  72: 85 */         throw new IllegalArgumentException("Formula Evaluator " + eval + " provides no WorkbookEvaluator access");
/*  73:    */       }
/*  74:    */     }
/*  75: 89 */     setup(evaluatorsByName);
/*  76:    */   }
/*  77:    */   
/*  78:    */   private CollaboratingWorkbooksEnvironment(String[] workbookNames, WorkbookEvaluator[] evaluators, int nItems)
/*  79:    */   {
/*  80: 93 */     this(toUniqueMap(workbookNames, evaluators, nItems), evaluators);
/*  81:    */   }
/*  82:    */   
/*  83:    */   private static Map<String, WorkbookEvaluator> toUniqueMap(String[] workbookNames, WorkbookEvaluator[] evaluators, int nItems)
/*  84:    */   {
/*  85: 96 */     Map<String, WorkbookEvaluator> evaluatorsByName = new HashMap(nItems * 3 / 2);
/*  86: 97 */     for (int i = 0; i < nItems; i++)
/*  87:    */     {
/*  88: 98 */       String wbName = workbookNames[i];
/*  89: 99 */       WorkbookEvaluator wbEval = evaluators[i];
/*  90:100 */       if (evaluatorsByName.containsKey(wbName)) {
/*  91:101 */         throw new IllegalArgumentException("Duplicate workbook name '" + wbName + "'");
/*  92:    */       }
/*  93:103 */       evaluatorsByName.put(wbName, wbEval);
/*  94:    */     }
/*  95:105 */     return evaluatorsByName;
/*  96:    */   }
/*  97:    */   
/*  98:    */   private CollaboratingWorkbooksEnvironment(Map<String, WorkbookEvaluator> evaluatorsByName, WorkbookEvaluator[] evaluators)
/*  99:    */   {
/* 100:108 */     IdentityHashMap<WorkbookEvaluator, String> uniqueEvals = new IdentityHashMap(evaluators.length);
/* 101:109 */     for (Entry<String, WorkbookEvaluator> me : evaluatorsByName.entrySet())
/* 102:    */     {
/* 103:110 */       String uniEval = (String)uniqueEvals.put(me.getValue(), me.getKey());
/* 104:111 */       if (uniEval != null)
/* 105:    */       {
/* 106:112 */         String msg = "Attempted to register same workbook under names '" + uniEval + "' and '" + (String)me.getKey() + "'";
/* 107:    */         
/* 108:114 */         throw new IllegalArgumentException(msg);
/* 109:    */       }
/* 110:    */     }
/* 111:117 */     unhookOldEnvironments(evaluators);
/* 112:118 */     hookNewEnvironment(evaluators, this);
/* 113:119 */     this._unhooked = false;
/* 114:120 */     this._evaluators = ((WorkbookEvaluator[])evaluators.clone());
/* 115:121 */     this._evaluatorsByName = evaluatorsByName;
/* 116:    */   }
/* 117:    */   
/* 118:    */   private static void hookNewEnvironment(WorkbookEvaluator[] evaluators, CollaboratingWorkbooksEnvironment env)
/* 119:    */   {
/* 120:127 */     int nItems = evaluators.length;
/* 121:128 */     IEvaluationListener evalListener = evaluators[0].getEvaluationListener();
/* 122:130 */     for (int i = 0; i < nItems; i++) {
/* 123:131 */       if (evalListener != evaluators[i].getEvaluationListener()) {
/* 124:133 */         throw new RuntimeException("Workbook evaluators must all have the same evaluation listener");
/* 125:    */       }
/* 126:    */     }
/* 127:136 */     EvaluationCache cache = new EvaluationCache(evalListener);
/* 128:138 */     for (int i = 0; i < nItems; i++) {
/* 129:139 */       evaluators[i].attachToEnvironment(env, cache, i);
/* 130:    */     }
/* 131:    */   }
/* 132:    */   
/* 133:    */   private void unhookOldEnvironments(WorkbookEvaluator[] evaluators)
/* 134:    */   {
/* 135:147 */     Set<CollaboratingWorkbooksEnvironment> oldEnvs = new HashSet();
/* 136:148 */     for (int i = 0; i < evaluators.length; i++) {
/* 137:149 */       oldEnvs.add(evaluators[i].getEnvironment());
/* 138:    */     }
/* 139:151 */     CollaboratingWorkbooksEnvironment[] oldCWEs = new CollaboratingWorkbooksEnvironment[oldEnvs.size()];
/* 140:152 */     oldEnvs.toArray(oldCWEs);
/* 141:153 */     for (int i = 0; i < oldCWEs.length; i++) {
/* 142:154 */       oldCWEs[i].unhook();
/* 143:    */     }
/* 144:    */   }
/* 145:    */   
/* 146:    */   private void unhook()
/* 147:    */   {
/* 148:162 */     if (this._evaluators.length < 1) {
/* 149:164 */       return;
/* 150:    */     }
/* 151:166 */     for (int i = 0; i < this._evaluators.length; i++) {
/* 152:167 */       this._evaluators[i].detachFromEnvironment();
/* 153:    */     }
/* 154:169 */     this._unhooked = true;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public WorkbookEvaluator getWorkbookEvaluator(String workbookName)
/* 158:    */     throws WorkbookNotFoundException
/* 159:    */   {
/* 160:173 */     if (this._unhooked) {
/* 161:174 */       throw new IllegalStateException("This environment has been unhooked");
/* 162:    */     }
/* 163:176 */     WorkbookEvaluator result = (WorkbookEvaluator)this._evaluatorsByName.get(workbookName);
/* 164:177 */     if (result == null)
/* 165:    */     {
/* 166:178 */       StringBuffer sb = new StringBuffer(256);
/* 167:179 */       sb.append("Could not resolve external workbook name '").append(workbookName).append("'.");
/* 168:180 */       if (this._evaluators.length < 1)
/* 169:    */       {
/* 170:181 */         sb.append(" Workbook environment has not been set up.");
/* 171:    */       }
/* 172:    */       else
/* 173:    */       {
/* 174:183 */         sb.append(" The following workbook names are valid: (");
/* 175:184 */         Iterator<String> i = this._evaluatorsByName.keySet().iterator();
/* 176:185 */         int count = 0;
/* 177:186 */         while (i.hasNext())
/* 178:    */         {
/* 179:187 */           if (count++ > 0) {
/* 180:188 */             sb.append(", ");
/* 181:    */           }
/* 182:190 */           sb.append("'").append((String)i.next()).append("'");
/* 183:    */         }
/* 184:192 */         sb.append(")");
/* 185:    */       }
/* 186:194 */       throw new WorkbookNotFoundException(sb.toString());
/* 187:    */     }
/* 188:196 */     return result;
/* 189:    */   }
/* 190:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.formula.CollaboratingWorkbooksEnvironment

 * JD-Core Version:    0.7.0.1

 */