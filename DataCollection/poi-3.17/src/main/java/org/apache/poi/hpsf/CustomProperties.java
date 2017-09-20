/*   1:    */ package org.apache.poi.hpsf;
/*   2:    */ 
/*   3:    */ import java.io.UnsupportedEncodingException;
/*   4:    */ import java.math.BigInteger;
/*   5:    */ import java.nio.charset.Charset;
/*   6:    */ import java.nio.charset.CharsetEncoder;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.Collection;
/*   9:    */ import java.util.Collections;
/*  10:    */ import java.util.Date;
/*  11:    */ import java.util.HashMap;
/*  12:    */ import java.util.LinkedHashMap;
/*  13:    */ import java.util.List;
/*  14:    */ import java.util.Map;
/*  15:    */ import java.util.Map.Entry;
/*  16:    */ import java.util.Set;
/*  17:    */ import org.apache.commons.collections4.bidimap.TreeBidiMap;
/*  18:    */ import org.apache.poi.util.CodePageUtil;
/*  19:    */ import org.apache.poi.util.POILogFactory;
/*  20:    */ import org.apache.poi.util.POILogger;
/*  21:    */ 
/*  22:    */ public class CustomProperties
/*  23:    */   implements Map<String, Object>
/*  24:    */ {
/*  25: 68 */   private static final POILogger LOG = POILogFactory.getLogger(CustomProperties.class);
/*  26: 73 */   private final HashMap<Long, CustomProperty> props = new HashMap();
/*  27: 78 */   private final TreeBidiMap<Long, String> dictionary = new TreeBidiMap();
/*  28: 83 */   private boolean isPure = true;
/*  29: 85 */   private int codepage = -1;
/*  30:    */   
/*  31:    */   public CustomProperty put(String name, CustomProperty cp)
/*  32:    */   {
/*  33: 98 */     if (name == null)
/*  34:    */     {
/*  35:100 */       this.isPure = false;
/*  36:101 */       return null;
/*  37:    */     }
/*  38:104 */     if (!name.equals(cp.getName())) {
/*  39:105 */       throw new IllegalArgumentException("Parameter \"name\" (" + name + ") and custom property's name (" + cp.getName() + ") do not match.");
/*  40:    */     }
/*  41:110 */     checkCodePage(name);
/*  42:    */     
/*  43:    */ 
/*  44:113 */     this.props.remove(this.dictionary.getKey(name));
/*  45:114 */     this.dictionary.put(Long.valueOf(cp.getID()), name);
/*  46:    */     
/*  47:    */ 
/*  48:117 */     return (CustomProperty)this.props.put(Long.valueOf(cp.getID()), cp);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Object put(String key, Object value)
/*  52:    */   {
/*  53:    */     int variantType;
/*  54:131 */     if ((value instanceof String))
/*  55:    */     {
/*  56:132 */       variantType = 30;
/*  57:    */     }
/*  58:    */     else
/*  59:    */     {
/*  60:    */       int variantType;
/*  61:133 */       if ((value instanceof Short))
/*  62:    */       {
/*  63:134 */         variantType = 2;
/*  64:    */       }
/*  65:    */       else
/*  66:    */       {
/*  67:    */         int variantType;
/*  68:135 */         if ((value instanceof Integer))
/*  69:    */         {
/*  70:136 */           variantType = 3;
/*  71:    */         }
/*  72:    */         else
/*  73:    */         {
/*  74:    */           int variantType;
/*  75:137 */           if ((value instanceof Long))
/*  76:    */           {
/*  77:138 */             variantType = 20;
/*  78:    */           }
/*  79:    */           else
/*  80:    */           {
/*  81:    */             int variantType;
/*  82:139 */             if ((value instanceof Float))
/*  83:    */             {
/*  84:140 */               variantType = 4;
/*  85:    */             }
/*  86:    */             else
/*  87:    */             {
/*  88:    */               int variantType;
/*  89:141 */               if ((value instanceof Double))
/*  90:    */               {
/*  91:142 */                 variantType = 5;
/*  92:    */               }
/*  93:    */               else
/*  94:    */               {
/*  95:    */                 int variantType;
/*  96:143 */                 if ((value instanceof Boolean))
/*  97:    */                 {
/*  98:144 */                   variantType = 11;
/*  99:    */                 }
/* 100:    */                 else
/* 101:    */                 {
/* 102:    */                   int variantType;
/* 103:145 */                   if (((value instanceof BigInteger)) && (((BigInteger)value).bitLength() <= 64) && (((BigInteger)value).compareTo(BigInteger.ZERO) >= 0))
/* 104:    */                   {
/* 105:148 */                     variantType = 21;
/* 106:    */                   }
/* 107:    */                   else
/* 108:    */                   {
/* 109:    */                     int variantType;
/* 110:149 */                     if ((value instanceof Date)) {
/* 111:150 */                       variantType = 64;
/* 112:    */                     } else {
/* 113:152 */                       throw new IllegalStateException("unsupported datatype - currently String,Short,Integer,Long,Float,Double,Boolean,BigInteger(unsigned long),Date can be processed.");
/* 114:    */                     }
/* 115:    */                   }
/* 116:    */                 }
/* 117:    */               }
/* 118:    */             }
/* 119:    */           }
/* 120:    */         }
/* 121:    */       }
/* 122:    */     }
/* 123:    */     int variantType;
/* 124:154 */     Property p = new MutableProperty(-1L, variantType, value);
/* 125:155 */     return put(new CustomProperty(p, key));
/* 126:    */   }
/* 127:    */   
/* 128:    */   public Object get(Object key)
/* 129:    */   {
/* 130:167 */     Long id = (Long)this.dictionary.getKey(key);
/* 131:168 */     CustomProperty cp = (CustomProperty)this.props.get(id);
/* 132:169 */     return cp != null ? cp.getValue() : null;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public CustomProperty remove(Object key)
/* 136:    */   {
/* 137:179 */     Long id = (Long)this.dictionary.removeValue(key);
/* 138:180 */     return (CustomProperty)this.props.remove(id);
/* 139:    */   }
/* 140:    */   
/* 141:    */   public int size()
/* 142:    */   {
/* 143:185 */     return this.props.size();
/* 144:    */   }
/* 145:    */   
/* 146:    */   public boolean isEmpty()
/* 147:    */   {
/* 148:190 */     return this.props.isEmpty();
/* 149:    */   }
/* 150:    */   
/* 151:    */   public void clear()
/* 152:    */   {
/* 153:195 */     this.props.clear();
/* 154:    */   }
/* 155:    */   
/* 156:    */   public int hashCode()
/* 157:    */   {
/* 158:200 */     return this.props.hashCode();
/* 159:    */   }
/* 160:    */   
/* 161:    */   public boolean equals(Object obj)
/* 162:    */   {
/* 163:205 */     if (!(obj instanceof CustomProperties)) {
/* 164:206 */       return false;
/* 165:    */     }
/* 166:208 */     return this.props.equals(((CustomProperties)obj).props);
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void putAll(Map<? extends String, ? extends Object> m)
/* 170:    */   {
/* 171:213 */     for (Entry<? extends String, ? extends Object> me : m.entrySet()) {
/* 172:214 */       put((String)me.getKey(), me.getValue());
/* 173:    */     }
/* 174:    */   }
/* 175:    */   
/* 176:    */   public List<CustomProperty> properties()
/* 177:    */   {
/* 178:222 */     List<CustomProperty> list = new ArrayList(this.props.size());
/* 179:223 */     for (Long l : this.dictionary.keySet()) {
/* 180:224 */       list.add(this.props.get(l));
/* 181:    */     }
/* 182:226 */     return Collections.unmodifiableList(list);
/* 183:    */   }
/* 184:    */   
/* 185:    */   public Collection<Object> values()
/* 186:    */   {
/* 187:234 */     List<Object> list = new ArrayList(this.props.size());
/* 188:235 */     for (Long l : this.dictionary.keySet()) {
/* 189:236 */       list.add(((CustomProperty)this.props.get(l)).getValue());
/* 190:    */     }
/* 191:238 */     return Collections.unmodifiableCollection(list);
/* 192:    */   }
/* 193:    */   
/* 194:    */   public Set<Entry<String, Object>> entrySet()
/* 195:    */   {
/* 196:243 */     Map<String, Object> set = new LinkedHashMap(this.props.size());
/* 197:244 */     for (Entry<Long, String> se : this.dictionary.entrySet()) {
/* 198:245 */       set.put(se.getValue(), ((CustomProperty)this.props.get(se.getKey())).getValue());
/* 199:    */     }
/* 200:247 */     return Collections.unmodifiableSet(set.entrySet());
/* 201:    */   }
/* 202:    */   
/* 203:    */   public Set keySet()
/* 204:    */   {
/* 205:259 */     return Collections.unmodifiableSet(this.dictionary.values());
/* 206:    */   }
/* 207:    */   
/* 208:    */   public Set<String> nameSet()
/* 209:    */   {
/* 210:268 */     return Collections.unmodifiableSet(this.dictionary.values());
/* 211:    */   }
/* 212:    */   
/* 213:    */   public Set<Long> idSet()
/* 214:    */   {
/* 215:277 */     return Collections.unmodifiableSet(this.dictionary.keySet());
/* 216:    */   }
/* 217:    */   
/* 218:    */   public void setCodepage(int codepage)
/* 219:    */   {
/* 220:287 */     this.codepage = codepage;
/* 221:    */   }
/* 222:    */   
/* 223:    */   public int getCodepage()
/* 224:    */   {
/* 225:296 */     return this.codepage;
/* 226:    */   }
/* 227:    */   
/* 228:    */   Map<Long, String> getDictionary()
/* 229:    */   {
/* 230:306 */     return this.dictionary;
/* 231:    */   }
/* 232:    */   
/* 233:    */   public boolean containsKey(Object key)
/* 234:    */   {
/* 235:315 */     return (((key instanceof Long)) && (this.dictionary.containsKey(key))) || (this.dictionary.containsValue(key));
/* 236:    */   }
/* 237:    */   
/* 238:    */   public boolean containsValue(Object value)
/* 239:    */   {
/* 240:323 */     if ((value instanceof CustomProperty)) {
/* 241:324 */       return this.props.containsValue(value);
/* 242:    */     }
/* 243:327 */     for (CustomProperty cp : this.props.values()) {
/* 244:328 */       if (cp.getValue() == value) {
/* 245:329 */         return true;
/* 246:    */       }
/* 247:    */     }
/* 248:333 */     return false;
/* 249:    */   }
/* 250:    */   
/* 251:    */   public boolean isPure()
/* 252:    */   {
/* 253:345 */     return this.isPure;
/* 254:    */   }
/* 255:    */   
/* 256:    */   public void setPure(boolean isPure)
/* 257:    */   {
/* 258:354 */     this.isPure = isPure;
/* 259:    */   }
/* 260:    */   
/* 261:    */   private Object put(CustomProperty customProperty)
/* 262:    */     throws ClassCastException
/* 263:    */   {
/* 264:373 */     String name = customProperty.getName();
/* 265:    */     
/* 266:    */ 
/* 267:376 */     Long oldId = name == null ? null : (Long)this.dictionary.getKey(name);
/* 268:377 */     if (oldId != null)
/* 269:    */     {
/* 270:378 */       customProperty.setID(oldId.longValue());
/* 271:    */     }
/* 272:    */     else
/* 273:    */     {
/* 274:380 */       long lastKey = this.dictionary.isEmpty() ? 0L : ((Long)this.dictionary.lastKey()).longValue();
/* 275:381 */       long nextKey = Math.max(lastKey, 31L) + 1L;
/* 276:382 */       customProperty.setID(nextKey);
/* 277:    */     }
/* 278:384 */     return put(name, customProperty);
/* 279:    */   }
/* 280:    */   
/* 281:    */   private void checkCodePage(String value)
/* 282:    */   {
/* 283:388 */     int cp = getCodepage();
/* 284:389 */     if (cp == -1) {
/* 285:390 */       cp = 1252;
/* 286:    */     }
/* 287:392 */     if (cp == 1200) {
/* 288:393 */       return;
/* 289:    */     }
/* 290:395 */     String cps = "";
/* 291:    */     try
/* 292:    */     {
/* 293:397 */       cps = CodePageUtil.codepageToEncoding(cp, false);
/* 294:    */     }
/* 295:    */     catch (UnsupportedEncodingException e)
/* 296:    */     {
/* 297:399 */       LOG.log(7, new Object[] { "Codepage '" + cp + "' can't be found." });
/* 298:    */     }
/* 299:401 */     if ((!cps.isEmpty()) && (Charset.forName(cps).newEncoder().canEncode(value))) {
/* 300:402 */       return;
/* 301:    */     }
/* 302:404 */     LOG.log(1, new Object[] { "Charset '" + cps + "' can't encode '" + value + "' - switching to unicode." });
/* 303:405 */     setCodepage(1200);
/* 304:    */   }
/* 305:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hpsf.CustomProperties

 * JD-Core Version:    0.7.0.1

 */