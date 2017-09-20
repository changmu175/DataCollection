/*   1:    */ package org.apache.poi.openxml4j.opc.internal;
/*   2:    */ 
/*   3:    */ import java.util.Collections;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Map.Entry;
/*   7:    */ import java.util.Set;
/*   8:    */ import java.util.regex.Matcher;
/*   9:    */ import java.util.regex.Pattern;
/*  10:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  11:    */ 
/*  12:    */ public final class ContentType
/*  13:    */ {
/*  14:    */   private final String type;
/*  15:    */   private final String subType;
/*  16:    */   private final Map<String, String> parameters;
/*  17:    */   private static final Pattern patternTypeSubType;
/*  18:    */   private static final Pattern patternTypeSubTypeParams;
/*  19:    */   private static final Pattern patternParams;
/*  20:    */   
/*  21:    */   static
/*  22:    */   {
/*  23: 95 */     String token = "[\\x21-\\x7E&&[^\\(\\)<>@,;:\\\\/\"\\[\\]\\?={}\\x20\\x09]]";
/*  24:    */     
/*  25:    */ 
/*  26:    */ 
/*  27:    */ 
/*  28:    */ 
/*  29:    */ 
/*  30:    */ 
/*  31:    */ 
/*  32:104 */     String parameter = "(" + token + "+)=(\"?" + token + "+\"?)";
/*  33:    */     
/*  34:    */ 
/*  35:    */ 
/*  36:    */ 
/*  37:    */ 
/*  38:    */ 
/*  39:    */ 
/*  40:    */ 
/*  41:    */ 
/*  42:    */ 
/*  43:    */ 
/*  44:    */ 
/*  45:    */ 
/*  46:    */ 
/*  47:    */ 
/*  48:    */ 
/*  49:    */ 
/*  50:    */ 
/*  51:    */ 
/*  52:    */ 
/*  53:    */ 
/*  54:    */ 
/*  55:    */ 
/*  56:    */ 
/*  57:    */ 
/*  58:    */ 
/*  59:131 */     patternTypeSubType = Pattern.compile("^(" + token + "+)/(" + token + "+)$");
/*  60:    */     
/*  61:133 */     patternTypeSubTypeParams = Pattern.compile("^(" + token + "+)/(" + token + "+)(;" + parameter + ")*$");
/*  62:    */     
/*  63:135 */     patternParams = Pattern.compile(";" + parameter);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public ContentType(String contentType)
/*  67:    */     throws InvalidFormatException
/*  68:    */   {
/*  69:147 */     Matcher mMediaType = patternTypeSubType.matcher(contentType);
/*  70:148 */     if (!mMediaType.matches()) {
/*  71:150 */       mMediaType = patternTypeSubTypeParams.matcher(contentType);
/*  72:    */     }
/*  73:152 */     if (!mMediaType.matches()) {
/*  74:153 */       throw new InvalidFormatException("The specified content type '" + contentType + "' is not compliant with RFC 2616: malformed content type.");
/*  75:    */     }
/*  76:160 */     if (mMediaType.groupCount() >= 2)
/*  77:    */     {
/*  78:161 */       this.type = mMediaType.group(1);
/*  79:162 */       this.subType = mMediaType.group(2);
/*  80:    */       
/*  81:    */ 
/*  82:165 */       this.parameters = new HashMap();
/*  83:168 */       if (mMediaType.groupCount() >= 5)
/*  84:    */       {
/*  85:169 */         Matcher mParams = patternParams.matcher(contentType.substring(mMediaType.end(2)));
/*  86:170 */         while (mParams.find()) {
/*  87:171 */           this.parameters.put(mParams.group(1), mParams.group(2));
/*  88:    */         }
/*  89:    */       }
/*  90:    */     }
/*  91:    */     else
/*  92:    */     {
/*  93:176 */       this.type = "";
/*  94:177 */       this.subType = "";
/*  95:178 */       this.parameters = Collections.emptyMap();
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   public final String toString()
/* 100:    */   {
/* 101:187 */     return toString(true);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public final String toString(boolean withParameters)
/* 105:    */   {
/* 106:191 */     StringBuffer retVal = new StringBuffer();
/* 107:192 */     retVal.append(getType());
/* 108:193 */     retVal.append("/");
/* 109:194 */     retVal.append(getSubType());
/* 110:196 */     if (withParameters) {
/* 111:197 */       for (Entry<String, String> me : this.parameters.entrySet())
/* 112:    */       {
/* 113:198 */         retVal.append(";");
/* 114:199 */         retVal.append((String)me.getKey());
/* 115:200 */         retVal.append("=");
/* 116:201 */         retVal.append((String)me.getValue());
/* 117:    */       }
/* 118:    */     }
/* 119:204 */     return retVal.toString();
/* 120:    */   }
/* 121:    */   
/* 122:    */   public boolean equals(Object obj)
/* 123:    */   {
/* 124:209 */     return (!(obj instanceof ContentType)) || (toString().equalsIgnoreCase(obj.toString()));
/* 125:    */   }
/* 126:    */   
/* 127:    */   public int hashCode()
/* 128:    */   {
/* 129:215 */     return toString().hashCode();
/* 130:    */   }
/* 131:    */   
/* 132:    */   public String getSubType()
/* 133:    */   {
/* 134:226 */     return this.subType;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public String getType()
/* 138:    */   {
/* 139:235 */     return this.type;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public boolean hasParameters()
/* 143:    */   {
/* 144:242 */     return (this.parameters != null) && (!this.parameters.isEmpty());
/* 145:    */   }
/* 146:    */   
/* 147:    */   public String[] getParameterKeys()
/* 148:    */   {
/* 149:249 */     if (this.parameters == null) {
/* 150:250 */       return new String[0];
/* 151:    */     }
/* 152:251 */     return (String[])this.parameters.keySet().toArray(new String[this.parameters.size()]);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public String getParameter(String key)
/* 156:    */   {
/* 157:262 */     return (String)this.parameters.get(key);
/* 158:    */   }
/* 159:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.openxml4j.opc.internal.ContentType

 * JD-Core Version:    0.7.0.1

 */