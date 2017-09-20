/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.ptg.AbstractFunctionPtg;
/*   4:    */ import org.apache.poi.ss.formula.ptg.AttrPtg;
/*   5:    */ import org.apache.poi.ss.formula.ptg.ControlPtg;
/*   6:    */ import org.apache.poi.ss.formula.ptg.FuncVarPtg;
/*   7:    */ import org.apache.poi.ss.formula.ptg.IntersectionPtg;
/*   8:    */ import org.apache.poi.ss.formula.ptg.MemAreaPtg;
/*   9:    */ import org.apache.poi.ss.formula.ptg.MemFuncPtg;
/*  10:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  11:    */ import org.apache.poi.ss.formula.ptg.RangePtg;
/*  12:    */ import org.apache.poi.ss.formula.ptg.UnionPtg;
/*  13:    */ import org.apache.poi.ss.formula.ptg.ValueOperatorPtg;
/*  14:    */ 
/*  15:    */ final class OperandClassTransformer
/*  16:    */ {
/*  17:    */   private final FormulaType _formulaType;
/*  18:    */   
/*  19:    */   public OperandClassTransformer(FormulaType formulaType)
/*  20:    */   {
/*  21: 64 */     this._formulaType = formulaType;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void transformFormula(ParseNode rootNode)
/*  25:    */   {
/*  26:    */     byte rootNodeOperandClass;
/*  27: 73 */     switch (1.$SwitchMap$org$apache$poi$ss$formula$FormulaType[this._formulaType.ordinal()])
/*  28:    */     {
/*  29:    */     case 1: 
/*  30: 75 */       rootNodeOperandClass = 32;
/*  31: 76 */       break;
/*  32:    */     case 2: 
/*  33: 78 */       rootNodeOperandClass = 64;
/*  34: 79 */       break;
/*  35:    */     case 3: 
/*  36:    */     case 4: 
/*  37: 82 */       rootNodeOperandClass = 0;
/*  38: 83 */       break;
/*  39:    */     default: 
/*  40: 85 */       throw new RuntimeException("Incomplete code - formula type (" + this._formulaType + ") not supported yet");
/*  41:    */     }
/*  42: 89 */     transformNode(rootNode, rootNodeOperandClass, false);
/*  43:    */   }
/*  44:    */   
/*  45:    */   private void transformNode(ParseNode node, byte desiredOperandClass, boolean callerForceArrayFlag)
/*  46:    */   {
/*  47: 99 */     Ptg token = node.getToken();
/*  48:100 */     ParseNode[] children = node.getChildren();
/*  49:101 */     boolean isSimpleValueFunc = isSimpleValueFunction(token);
/*  50:103 */     if (isSimpleValueFunc)
/*  51:    */     {
/*  52:104 */       boolean localForceArray = desiredOperandClass == 64;
/*  53:105 */       for (int i = 0; i < children.length; i++) {
/*  54:106 */         transformNode(children[i], desiredOperandClass, localForceArray);
/*  55:    */       }
/*  56:108 */       setSimpleValueFuncClass((AbstractFunctionPtg)token, desiredOperandClass, callerForceArrayFlag);
/*  57:109 */       return;
/*  58:    */     }
/*  59:112 */     if (isSingleArgSum(token)) {
/*  60:115 */       token = FuncVarPtg.SUM;
/*  61:    */     }
/*  62:119 */     if (((token instanceof ValueOperatorPtg)) || ((token instanceof ControlPtg)) || ((token instanceof MemFuncPtg)) || ((token instanceof MemAreaPtg)) || ((token instanceof UnionPtg)) || ((token instanceof IntersectionPtg)))
/*  63:    */     {
/*  64:130 */       byte localDesiredOperandClass = desiredOperandClass == 0 ? 32 : desiredOperandClass;
/*  65:131 */       for (int i = 0; i < children.length; i++) {
/*  66:132 */         transformNode(children[i], localDesiredOperandClass, callerForceArrayFlag);
/*  67:    */       }
/*  68:134 */       return;
/*  69:    */     }
/*  70:136 */     if ((token instanceof AbstractFunctionPtg))
/*  71:    */     {
/*  72:137 */       transformFunctionNode((AbstractFunctionPtg)token, children, desiredOperandClass, callerForceArrayFlag);
/*  73:138 */       return;
/*  74:    */     }
/*  75:140 */     if (children.length > 0)
/*  76:    */     {
/*  77:141 */       if (token == RangePtg.instance) {
/*  78:143 */         return;
/*  79:    */       }
/*  80:145 */       throw new IllegalStateException("Node should not have any children");
/*  81:    */     }
/*  82:148 */     if (token.isBaseToken()) {
/*  83:150 */       return;
/*  84:    */     }
/*  85:152 */     token.setClass(transformClass(token.getPtgClass(), desiredOperandClass, callerForceArrayFlag));
/*  86:    */   }
/*  87:    */   
/*  88:    */   private static boolean isSingleArgSum(Ptg token)
/*  89:    */   {
/*  90:156 */     if ((token instanceof AttrPtg))
/*  91:    */     {
/*  92:157 */       AttrPtg attrPtg = (AttrPtg)token;
/*  93:158 */       return attrPtg.isSum();
/*  94:    */     }
/*  95:160 */     return false;
/*  96:    */   }
/*  97:    */   
/*  98:    */   private static boolean isSimpleValueFunction(Ptg token)
/*  99:    */   {
/* 100:164 */     if ((token instanceof AbstractFunctionPtg))
/* 101:    */     {
/* 102:165 */       AbstractFunctionPtg aptg = (AbstractFunctionPtg)token;
/* 103:166 */       if (aptg.getDefaultOperandClass() != 32) {
/* 104:167 */         return false;
/* 105:    */       }
/* 106:169 */       int numberOfOperands = aptg.getNumberOfOperands();
/* 107:170 */       for (int i = numberOfOperands - 1; i >= 0; i--) {
/* 108:171 */         if (aptg.getParameterClass(i) != 32) {
/* 109:172 */           return false;
/* 110:    */         }
/* 111:    */       }
/* 112:175 */       return true;
/* 113:    */     }
/* 114:177 */     return false;
/* 115:    */   }
/* 116:    */   
/* 117:    */   private byte transformClass(byte currentOperandClass, byte desiredOperandClass, boolean callerForceArrayFlag)
/* 118:    */   {
/* 119:182 */     switch (desiredOperandClass)
/* 120:    */     {
/* 121:    */     case 32: 
/* 122:184 */       if (!callerForceArrayFlag) {
/* 123:185 */         return 32;
/* 124:    */       }
/* 125:    */     case 64: 
/* 126:189 */       return 64;
/* 127:    */     case 0: 
/* 128:191 */       if (!callerForceArrayFlag) {
/* 129:192 */         return currentOperandClass;
/* 130:    */       }
/* 131:194 */       return 0;
/* 132:    */     }
/* 133:196 */     throw new IllegalStateException("Unexpected operand class (" + desiredOperandClass + ")");
/* 134:    */   }
/* 135:    */   
/* 136:    */   private void transformFunctionNode(AbstractFunctionPtg afp, ParseNode[] children, byte desiredOperandClass, boolean callerForceArrayFlag)
/* 137:    */   {
/* 138:203 */     byte defaultReturnOperandClass = afp.getDefaultOperandClass();
/* 139:    */     boolean localForceArrayFlag;
/* 140:    */     boolean localForceArrayFlag;
/* 141:205 */     if (callerForceArrayFlag)
/* 142:    */     {
/* 143:206 */       switch (defaultReturnOperandClass)
/* 144:    */       {
/* 145:    */       case 0: 
/* 146:208 */         if (desiredOperandClass == 0) {
/* 147:209 */           afp.setClass((byte)0);
/* 148:    */         } else {
/* 149:211 */           afp.setClass((byte)64);
/* 150:    */         }
/* 151:213 */         localForceArrayFlag = false;
/* 152:214 */         break;
/* 153:    */       case 64: 
/* 154:216 */         afp.setClass((byte)64);
/* 155:217 */         localForceArrayFlag = false;
/* 156:218 */         break;
/* 157:    */       case 32: 
/* 158:220 */         afp.setClass((byte)64);
/* 159:221 */         localForceArrayFlag = true;
/* 160:222 */         break;
/* 161:    */       default: 
/* 162:224 */         throw new IllegalStateException("Unexpected operand class (" + defaultReturnOperandClass + ")");
/* 163:    */       }
/* 164:    */     }
/* 165:228 */     else if (defaultReturnOperandClass == desiredOperandClass)
/* 166:    */     {
/* 167:229 */       localForceArrayFlag = false;
/* 168:    */       
/* 169:    */ 
/* 170:    */ 
/* 171:233 */       afp.setClass(defaultReturnOperandClass);
/* 172:    */     }
/* 173:    */     else
/* 174:    */     {
/* 175:235 */       switch (desiredOperandClass)
/* 176:    */       {
/* 177:    */       case 32: 
/* 178:238 */         afp.setClass((byte)32);
/* 179:239 */         localForceArrayFlag = false;
/* 180:240 */         break;
/* 181:    */       case 64: 
/* 182:242 */         switch (defaultReturnOperandClass)
/* 183:    */         {
/* 184:    */         case 0: 
/* 185:244 */           afp.setClass((byte)0);
/* 186:    */           
/* 187:246 */           break;
/* 188:    */         case 32: 
/* 189:248 */           afp.setClass((byte)64);
/* 190:249 */           break;
/* 191:    */         default: 
/* 192:251 */           throw new IllegalStateException("Unexpected operand class (" + defaultReturnOperandClass + ")");
/* 193:    */         }
/* 194:254 */         localForceArrayFlag = defaultReturnOperandClass == 32;
/* 195:255 */         break;
/* 196:    */       case 0: 
/* 197:257 */         switch (defaultReturnOperandClass)
/* 198:    */         {
/* 199:    */         case 64: 
/* 200:259 */           afp.setClass((byte)64);
/* 201:260 */           break;
/* 202:    */         case 32: 
/* 203:262 */           afp.setClass((byte)32);
/* 204:263 */           break;
/* 205:    */         default: 
/* 206:265 */           throw new IllegalStateException("Unexpected operand class (" + defaultReturnOperandClass + ")");
/* 207:    */         }
/* 208:268 */         localForceArrayFlag = false;
/* 209:269 */         break;
/* 210:    */       default: 
/* 211:271 */         throw new IllegalStateException("Unexpected operand class (" + desiredOperandClass + ")");
/* 212:    */       }
/* 213:    */     }
/* 214:278 */     for (int i = 0; i < children.length; i++)
/* 215:    */     {
/* 216:279 */       ParseNode child = children[i];
/* 217:280 */       byte paramOperandClass = afp.getParameterClass(i);
/* 218:281 */       transformNode(child, paramOperandClass, localForceArrayFlag);
/* 219:    */     }
/* 220:    */   }
/* 221:    */   
/* 222:    */   private void setSimpleValueFuncClass(AbstractFunctionPtg afp, byte desiredOperandClass, boolean callerForceArrayFlag)
/* 223:    */   {
/* 224:288 */     if ((callerForceArrayFlag) || (desiredOperandClass == 64)) {
/* 225:289 */       afp.setClass((byte)64);
/* 226:    */     } else {
/* 227:291 */       afp.setClass((byte)32);
/* 228:    */     }
/* 229:    */   }
/* 230:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.OperandClassTransformer
 * JD-Core Version:    0.7.0.1
 */