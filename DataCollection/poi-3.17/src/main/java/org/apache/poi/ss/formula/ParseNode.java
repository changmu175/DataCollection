/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.ptg.ArrayPtg;
/*   4:    */ import org.apache.poi.ss.formula.ptg.AttrPtg;
/*   5:    */ import org.apache.poi.ss.formula.ptg.FuncVarPtg;
/*   6:    */ import org.apache.poi.ss.formula.ptg.MemAreaPtg;
/*   7:    */ import org.apache.poi.ss.formula.ptg.MemFuncPtg;
/*   8:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   9:    */ 
/*  10:    */ final class ParseNode
/*  11:    */ {
/*  12: 34 */   public static final ParseNode[] EMPTY_ARRAY = new ParseNode[0];
/*  13:    */   private final Ptg _token;
/*  14:    */   private final ParseNode[] _children;
/*  15:    */   private boolean _isIf;
/*  16:    */   private final int _tokenCount;
/*  17:    */   
/*  18:    */   public ParseNode(Ptg token, ParseNode[] children)
/*  19:    */   {
/*  20: 41 */     if (token == null) {
/*  21: 42 */       throw new IllegalArgumentException("token must not be null");
/*  22:    */     }
/*  23: 44 */     this._token = token;
/*  24: 45 */     this._children = ((ParseNode[])children.clone());
/*  25: 46 */     this._isIf = isIf(token);
/*  26: 47 */     int tokenCount = 1;
/*  27: 48 */     for (int i = 0; i < children.length; i++) {
/*  28: 49 */       tokenCount += children[i].getTokenCount();
/*  29:    */     }
/*  30: 51 */     if (this._isIf) {
/*  31: 53 */       tokenCount += children.length;
/*  32:    */     }
/*  33: 55 */     this._tokenCount = tokenCount;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public ParseNode(Ptg token)
/*  37:    */   {
/*  38: 58 */     this(token, EMPTY_ARRAY);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public ParseNode(Ptg token, ParseNode child0)
/*  42:    */   {
/*  43: 61 */     this(token, new ParseNode[] { child0 });
/*  44:    */   }
/*  45:    */   
/*  46:    */   public ParseNode(Ptg token, ParseNode child0, ParseNode child1)
/*  47:    */   {
/*  48: 64 */     this(token, new ParseNode[] { child0, child1 });
/*  49:    */   }
/*  50:    */   
/*  51:    */   private int getTokenCount()
/*  52:    */   {
/*  53: 67 */     return this._tokenCount;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public int getEncodedSize()
/*  57:    */   {
/*  58: 70 */     int result = (this._token instanceof ArrayPtg) ? 8 : this._token.getSize();
/*  59: 71 */     for (int i = 0; i < this._children.length; i++) {
/*  60: 72 */       result += this._children[i].getEncodedSize();
/*  61:    */     }
/*  62: 74 */     return result;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public static Ptg[] toTokenArray(ParseNode rootNode)
/*  66:    */   {
/*  67: 81 */     TokenCollector temp = new TokenCollector(rootNode.getTokenCount());
/*  68: 82 */     rootNode.collectPtgs(temp);
/*  69: 83 */     return temp.getResult();
/*  70:    */   }
/*  71:    */   
/*  72:    */   private void collectPtgs(TokenCollector temp)
/*  73:    */   {
/*  74: 86 */     if (isIf(this._token))
/*  75:    */     {
/*  76: 87 */       collectIfPtgs(temp);
/*  77: 88 */       return;
/*  78:    */     }
/*  79: 90 */     boolean isPreFixOperator = ((this._token instanceof MemFuncPtg)) || ((this._token instanceof MemAreaPtg));
/*  80: 91 */     if (isPreFixOperator) {
/*  81: 92 */       temp.add(this._token);
/*  82:    */     }
/*  83: 94 */     for (int i = 0; i < getChildren().length; i++) {
/*  84: 95 */       getChildren()[i].collectPtgs(temp);
/*  85:    */     }
/*  86: 97 */     if (!isPreFixOperator) {
/*  87: 98 */       temp.add(this._token);
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   private void collectIfPtgs(TokenCollector temp)
/*  92:    */   {
/*  93:110 */     getChildren()[0].collectPtgs(temp);
/*  94:    */     
/*  95:    */ 
/*  96:113 */     int ifAttrIndex = temp.createPlaceholder();
/*  97:    */     
/*  98:    */ 
/*  99:116 */     getChildren()[1].collectPtgs(temp);
/* 100:    */     
/* 101:    */ 
/* 102:119 */     int skipAfterTrueParamIndex = temp.createPlaceholder();
/* 103:120 */     int trueParamSize = temp.sumTokenSizes(ifAttrIndex + 1, skipAfterTrueParamIndex);
/* 104:    */     
/* 105:122 */     AttrPtg attrIf = AttrPtg.createIf(trueParamSize + 4);
/* 106:124 */     if (getChildren().length > 2)
/* 107:    */     {
/* 108:128 */       getChildren()[2].collectPtgs(temp);
/* 109:    */       
/* 110:130 */       int skipAfterFalseParamIndex = temp.createPlaceholder();
/* 111:    */       
/* 112:132 */       int falseParamSize = temp.sumTokenSizes(skipAfterTrueParamIndex + 1, skipAfterFalseParamIndex);
/* 113:    */       
/* 114:134 */       AttrPtg attrSkipAfterTrue = AttrPtg.createSkip(falseParamSize + 4 + 4 - 1);
/* 115:135 */       AttrPtg attrSkipAfterFalse = AttrPtg.createSkip(3);
/* 116:    */       
/* 117:137 */       temp.setPlaceholder(ifAttrIndex, attrIf);
/* 118:138 */       temp.setPlaceholder(skipAfterTrueParamIndex, attrSkipAfterTrue);
/* 119:139 */       temp.setPlaceholder(skipAfterFalseParamIndex, attrSkipAfterFalse);
/* 120:    */     }
/* 121:    */     else
/* 122:    */     {
/* 123:142 */       AttrPtg attrSkipAfterTrue = AttrPtg.createSkip(3);
/* 124:    */       
/* 125:144 */       temp.setPlaceholder(ifAttrIndex, attrIf);
/* 126:145 */       temp.setPlaceholder(skipAfterTrueParamIndex, attrSkipAfterTrue);
/* 127:    */     }
/* 128:147 */     temp.add(this._token);
/* 129:    */   }
/* 130:    */   
/* 131:    */   private static boolean isIf(Ptg token)
/* 132:    */   {
/* 133:151 */     if ((token instanceof FuncVarPtg))
/* 134:    */     {
/* 135:152 */       FuncVarPtg func = (FuncVarPtg)token;
/* 136:153 */       if ("IF".equals(func.getName())) {
/* 137:154 */         return true;
/* 138:    */       }
/* 139:    */     }
/* 140:157 */     return false;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public Ptg getToken()
/* 144:    */   {
/* 145:161 */     return this._token;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public ParseNode[] getChildren()
/* 149:    */   {
/* 150:165 */     return this._children;
/* 151:    */   }
/* 152:    */   
/* 153:    */   private static final class TokenCollector
/* 154:    */   {
/* 155:    */     private final Ptg[] _ptgs;
/* 156:    */     private int _offset;
/* 157:    */     
/* 158:    */     public TokenCollector(int tokenCount)
/* 159:    */     {
/* 160:174 */       this._ptgs = new Ptg[tokenCount];
/* 161:175 */       this._offset = 0;
/* 162:    */     }
/* 163:    */     
/* 164:    */     public int sumTokenSizes(int fromIx, int toIx)
/* 165:    */     {
/* 166:179 */       int result = 0;
/* 167:180 */       for (int i = fromIx; i < toIx; i++) {
/* 168:181 */         result += this._ptgs[i].getSize();
/* 169:    */       }
/* 170:183 */       return result;
/* 171:    */     }
/* 172:    */     
/* 173:    */     public int createPlaceholder()
/* 174:    */     {
/* 175:187 */       return this._offset++;
/* 176:    */     }
/* 177:    */     
/* 178:    */     public void add(Ptg token)
/* 179:    */     {
/* 180:191 */       if (token == null) {
/* 181:192 */         throw new IllegalArgumentException("token must not be null");
/* 182:    */       }
/* 183:194 */       this._ptgs[this._offset] = token;
/* 184:195 */       this._offset += 1;
/* 185:    */     }
/* 186:    */     
/* 187:    */     public void setPlaceholder(int index, Ptg token)
/* 188:    */     {
/* 189:199 */       if (this._ptgs[index] != null) {
/* 190:200 */         throw new IllegalStateException("Invalid placeholder index (" + index + ")");
/* 191:    */       }
/* 192:202 */       this._ptgs[index] = token;
/* 193:    */     }
/* 194:    */     
/* 195:    */     public Ptg[] getResult()
/* 196:    */     {
/* 197:206 */       return this._ptgs;
/* 198:    */     }
/* 199:    */   }
/* 200:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ParseNode
 * JD-Core Version:    0.7.0.1
 */