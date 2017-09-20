/*   1:    */ package org.apache.poi.openxml4j.opc;
/*   2:    */ 
/*   3:    */ import java.math.BigInteger;
/*   4:    */ import java.net.URI;
/*   5:    */ import java.net.URISyntaxException;
/*   6:    */ import java.util.Locale;
/*   7:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*   8:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JRuntimeException;
/*   9:    */ 
/*  10:    */ public final class PackagePartName
/*  11:    */   implements Comparable<PackagePartName>
/*  12:    */ {
/*  13:    */   private URI partNameURI;
/*  14: 49 */   private static String[] RFC3986_PCHAR_SUB_DELIMS = { "!", "$", "&", "'", "(", ")", "*", "+", ",", ";", "=" };
/*  15: 55 */   private static String[] RFC3986_PCHAR_UNRESERVED_SUP = { "-", ".", "_", "~" };
/*  16: 60 */   private static String[] RFC3986_PCHAR_AUTHORIZED_SUP = { ":", "@" };
/*  17:    */   private boolean isRelationship;
/*  18:    */   
/*  19:    */   PackagePartName(URI uri, boolean checkConformance)
/*  20:    */     throws InvalidFormatException
/*  21:    */   {
/*  22: 84 */     if (checkConformance) {
/*  23: 85 */       throwExceptionIfInvalidPartUri(uri);
/*  24: 87 */     } else if (!PackagingURIHelper.PACKAGE_ROOT_URI.equals(uri)) {
/*  25: 88 */       throw new OpenXML4JRuntimeException("OCP conformance must be check for ALL part name except special cases : ['/']");
/*  26:    */     }
/*  27: 92 */     this.partNameURI = uri;
/*  28: 93 */     this.isRelationship = isRelationshipPartURI(this.partNameURI);
/*  29:    */   }
/*  30:    */   
/*  31:    */   PackagePartName(String partName, boolean checkConformance)
/*  32:    */     throws InvalidFormatException
/*  33:    */   {
/*  34:    */     URI partURI;
/*  35:    */     try
/*  36:    */     {
/*  37:114 */       partURI = new URI(partName);
/*  38:    */     }
/*  39:    */     catch (URISyntaxException e)
/*  40:    */     {
/*  41:116 */       throw new IllegalArgumentException("partName argmument is not a valid OPC part name !");
/*  42:    */     }
/*  43:120 */     if (checkConformance) {
/*  44:121 */       throwExceptionIfInvalidPartUri(partURI);
/*  45:123 */     } else if (!PackagingURIHelper.PACKAGE_ROOT_URI.equals(partURI)) {
/*  46:124 */       throw new OpenXML4JRuntimeException("OCP conformance must be check for ALL part name except special cases : ['/']");
/*  47:    */     }
/*  48:128 */     this.partNameURI = partURI;
/*  49:129 */     this.isRelationship = isRelationshipPartURI(this.partNameURI);
/*  50:    */   }
/*  51:    */   
/*  52:    */   private boolean isRelationshipPartURI(URI partUri)
/*  53:    */   {
/*  54:141 */     if (partUri == null) {
/*  55:142 */       throw new IllegalArgumentException("partUri");
/*  56:    */     }
/*  57:144 */     return partUri.getPath().matches("^.*/" + PackagingURIHelper.RELATIONSHIP_PART_SEGMENT_NAME + "/.*\\" + PackagingURIHelper.RELATIONSHIP_PART_EXTENSION_NAME + "$");
/*  58:    */   }
/*  59:    */   
/*  60:    */   public boolean isRelationshipPartURI()
/*  61:    */   {
/*  62:157 */     return this.isRelationship;
/*  63:    */   }
/*  64:    */   
/*  65:    */   private static void throwExceptionIfInvalidPartUri(URI partUri)
/*  66:    */     throws InvalidFormatException
/*  67:    */   {
/*  68:171 */     if (partUri == null) {
/*  69:172 */       throw new IllegalArgumentException("partUri");
/*  70:    */     }
/*  71:174 */     throwExceptionIfEmptyURI(partUri);
/*  72:    */     
/*  73:    */ 
/*  74:177 */     throwExceptionIfAbsoluteUri(partUri);
/*  75:    */     
/*  76:    */ 
/*  77:180 */     throwExceptionIfPartNameNotStartsWithForwardSlashChar(partUri);
/*  78:    */     
/*  79:    */ 
/*  80:183 */     throwExceptionIfPartNameEndsWithForwardSlashChar(partUri);
/*  81:    */     
/*  82:    */ 
/*  83:    */ 
/*  84:187 */     throwExceptionIfPartNameHaveInvalidSegments(partUri);
/*  85:    */   }
/*  86:    */   
/*  87:    */   private static void throwExceptionIfEmptyURI(URI partURI)
/*  88:    */     throws InvalidFormatException
/*  89:    */   {
/*  90:200 */     if (partURI == null) {
/*  91:201 */       throw new IllegalArgumentException("partURI");
/*  92:    */     }
/*  93:203 */     String uriPath = partURI.getPath();
/*  94:204 */     if ((uriPath.length() == 0) || ((uriPath.length() == 1) && (uriPath.charAt(0) == PackagingURIHelper.FORWARD_SLASH_CHAR))) {
/*  95:206 */       throw new InvalidFormatException("A part name shall not be empty [M1.1]: " + partURI.getPath());
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   private static void throwExceptionIfPartNameHaveInvalidSegments(URI partUri)
/* 100:    */     throws InvalidFormatException
/* 101:    */   {
/* 102:238 */     if (partUri == null) {
/* 103:239 */       throw new IllegalArgumentException("partUri");
/* 104:    */     }
/* 105:243 */     String[] segments = partUri.toASCIIString().split("/");
/* 106:244 */     if ((segments.length <= 1) || (!segments[0].equals(""))) {
/* 107:245 */       throw new InvalidFormatException("A part name shall not have empty segments [M1.3]: " + partUri.getPath());
/* 108:    */     }
/* 109:249 */     for (int i = 1; i < segments.length; i++)
/* 110:    */     {
/* 111:250 */       String seg = segments[i];
/* 112:251 */       if ((seg == null) || ("".equals(seg))) {
/* 113:252 */         throw new InvalidFormatException("A part name shall not have empty segments [M1.3]: " + partUri.getPath());
/* 114:    */       }
/* 115:257 */       if (seg.endsWith(".")) {
/* 116:258 */         throw new InvalidFormatException("A segment shall not end with a dot ('.') character [M1.9]: " + partUri.getPath());
/* 117:    */       }
/* 118:263 */       if ("".equals(seg.replaceAll("\\\\.", ""))) {
/* 119:266 */         throw new InvalidFormatException("A segment shall include at least one non-dot character. [M1.10]: " + partUri.getPath());
/* 120:    */       }
/* 121:272 */       checkPCharCompliance(seg);
/* 122:    */     }
/* 123:    */   }
/* 124:    */   
/* 125:    */   private static void checkPCharCompliance(String segment)
/* 126:    */     throws InvalidFormatException
/* 127:    */   {
/* 128:292 */     int length = segment.length();
/* 129:293 */     for (int i = 0; i < length; i++)
/* 130:    */     {
/* 131:294 */       char c = segment.charAt(i);
/* 132:295 */       boolean errorFlag = true;
/* 133:300 */       if (((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z')) || ((c >= '0') && (c <= '9')))
/* 134:    */       {
/* 135:302 */         errorFlag = false;
/* 136:    */       }
/* 137:    */       else
/* 138:    */       {
/* 139:305 */         for (int j = 0; j < RFC3986_PCHAR_UNRESERVED_SUP.length; j++) {
/* 140:306 */           if (c == RFC3986_PCHAR_UNRESERVED_SUP[j].charAt(0))
/* 141:    */           {
/* 142:307 */             errorFlag = false;
/* 143:308 */             break;
/* 144:    */           }
/* 145:    */         }
/* 146:313 */         for (int j = 0; (errorFlag) && (j < RFC3986_PCHAR_AUTHORIZED_SUP.length); j++) {
/* 147:315 */           if (c == RFC3986_PCHAR_AUTHORIZED_SUP[j].charAt(0)) {
/* 148:316 */             errorFlag = false;
/* 149:    */           }
/* 150:    */         }
/* 151:321 */         for (int j = 0; (errorFlag) && (j < RFC3986_PCHAR_SUB_DELIMS.length); j++) {
/* 152:323 */           if (c == RFC3986_PCHAR_SUB_DELIMS[j].charAt(0)) {
/* 153:324 */             errorFlag = false;
/* 154:    */           }
/* 155:    */         }
/* 156:    */       }
/* 157:329 */       if ((errorFlag) && (c == '%'))
/* 158:    */       {
/* 159:332 */         if (length - i < 2) {
/* 160:333 */           throw new InvalidFormatException("The segment " + segment + " contain invalid encoded character !");
/* 161:    */         }
/* 162:339 */         errorFlag = false;
/* 163:    */         
/* 164:    */ 
/* 165:342 */         char decodedChar = (char)Integer.parseInt(segment.substring(i + 1, i + 3), 16);
/* 166:    */         
/* 167:344 */         i += 2;
/* 168:347 */         if ((decodedChar == '/') || (decodedChar == '\\')) {
/* 169:348 */           throw new InvalidFormatException("A segment shall not contain percent-encoded forward slash ('/'), or backward slash ('') characters. [M1.7]");
/* 170:    */         }
/* 171:354 */         if (((decodedChar >= 'A') && (decodedChar <= 'Z')) || ((decodedChar >= 'a') && (decodedChar <= 'z')) || ((decodedChar >= '0') && (decodedChar <= '9'))) {
/* 172:357 */           errorFlag = true;
/* 173:    */         }
/* 174:360 */         for (int j = 0; (!errorFlag) && (j < RFC3986_PCHAR_UNRESERVED_SUP.length); j++) {
/* 175:362 */           if (c == RFC3986_PCHAR_UNRESERVED_SUP[j].charAt(0))
/* 176:    */           {
/* 177:363 */             errorFlag = true;
/* 178:364 */             break;
/* 179:    */           }
/* 180:    */         }
/* 181:367 */         if (errorFlag) {
/* 182:368 */           throw new InvalidFormatException("A segment shall not contain percent-encoded unreserved characters. [M1.8]");
/* 183:    */         }
/* 184:    */       }
/* 185:372 */       if (errorFlag) {
/* 186:373 */         throw new InvalidFormatException("A segment shall not hold any characters other than pchar characters. [M1.6]");
/* 187:    */       }
/* 188:    */     }
/* 189:    */   }
/* 190:    */   
/* 191:    */   private static void throwExceptionIfPartNameNotStartsWithForwardSlashChar(URI partUri)
/* 192:    */     throws InvalidFormatException
/* 193:    */   {
/* 194:390 */     String uriPath = partUri.getPath();
/* 195:391 */     if ((uriPath.length() > 0) && (uriPath.charAt(0) != PackagingURIHelper.FORWARD_SLASH_CHAR)) {
/* 196:393 */       throw new InvalidFormatException("A part name shall start with a forward slash ('/') character [M1.4]: " + partUri.getPath());
/* 197:    */     }
/* 198:    */   }
/* 199:    */   
/* 200:    */   private static void throwExceptionIfPartNameEndsWithForwardSlashChar(URI partUri)
/* 201:    */     throws InvalidFormatException
/* 202:    */   {
/* 203:410 */     String uriPath = partUri.getPath();
/* 204:411 */     if ((uriPath.length() > 0) && (uriPath.charAt(uriPath.length() - 1) == PackagingURIHelper.FORWARD_SLASH_CHAR)) {
/* 205:413 */       throw new InvalidFormatException("A part name shall not have a forward slash as the last character [M1.5]: " + partUri.getPath());
/* 206:    */     }
/* 207:    */   }
/* 208:    */   
/* 209:    */   private static void throwExceptionIfAbsoluteUri(URI partUri)
/* 210:    */     throws InvalidFormatException
/* 211:    */   {
/* 212:428 */     if (partUri.isAbsolute()) {
/* 213:429 */       throw new InvalidFormatException("Absolute URI forbidden: " + partUri);
/* 214:    */     }
/* 215:    */   }
/* 216:    */   
/* 217:    */   public int compareTo(PackagePartName other)
/* 218:    */   {
/* 219:445 */     return compare(this, other);
/* 220:    */   }
/* 221:    */   
/* 222:    */   public String getExtension()
/* 223:    */   {
/* 224:456 */     String fragment = this.partNameURI.getPath();
/* 225:457 */     if (fragment.length() > 0)
/* 226:    */     {
/* 227:458 */       int i = fragment.lastIndexOf(".");
/* 228:459 */       if (i > -1) {
/* 229:460 */         return fragment.substring(i + 1);
/* 230:    */       }
/* 231:    */     }
/* 232:462 */     return "";
/* 233:    */   }
/* 234:    */   
/* 235:    */   public String getName()
/* 236:    */   {
/* 237:471 */     return this.partNameURI.toASCIIString();
/* 238:    */   }
/* 239:    */   
/* 240:    */   public boolean equals(Object other)
/* 241:    */   {
/* 242:482 */     if ((other instanceof PackagePartName)) {
/* 243:484 */       return this.partNameURI.toASCIIString().toLowerCase(Locale.ROOT).equals(((PackagePartName)other).partNameURI.toASCIIString().toLowerCase(Locale.ROOT));
/* 244:    */     }
/* 245:489 */     return false;
/* 246:    */   }
/* 247:    */   
/* 248:    */   public int hashCode()
/* 249:    */   {
/* 250:495 */     return this.partNameURI.toASCIIString().toLowerCase(Locale.ROOT).hashCode();
/* 251:    */   }
/* 252:    */   
/* 253:    */   public String toString()
/* 254:    */   {
/* 255:500 */     return getName();
/* 256:    */   }
/* 257:    */   
/* 258:    */   public URI getURI()
/* 259:    */   {
/* 260:511 */     return this.partNameURI;
/* 261:    */   }
/* 262:    */   
/* 263:    */   public static int compare(PackagePartName obj1, PackagePartName obj2)
/* 264:    */   {
/* 265:535 */     if (obj1 == null) {
/* 266:538 */       return obj2 == null ? 0 : -1;
/* 267:    */     }
/* 268:540 */     if (obj2 == null) {
/* 269:543 */       return 1;
/* 270:    */     }
/* 271:546 */     return compare(obj1.getURI().toASCIIString().toLowerCase(Locale.ROOT), obj2.getURI().toASCIIString().toLowerCase(Locale.ROOT));
/* 272:    */   }
/* 273:    */   
/* 274:    */   public static int compare(String str1, String str2)
/* 275:    */   {
/* 276:565 */     if (str1 == null) {
/* 277:568 */       return str2 == null ? 0 : -1;
/* 278:    */     }
/* 279:570 */     if (str2 == null) {
/* 280:573 */       return 1;
/* 281:    */     }
/* 282:576 */     int len1 = str1.length();
/* 283:577 */     int len2 = str2.length();
/* 284:578 */     int idx1 = 0;
/* 285:578 */     for (int idx2 = 0; (idx1 < len1) && (idx2 < len2);)
/* 286:    */     {
/* 287:580 */       char c1 = str1.charAt(idx1++);
/* 288:581 */       char c2 = str2.charAt(idx2++);
/* 289:583 */       if ((Character.isDigit(c1)) && (Character.isDigit(c2)))
/* 290:    */       {
/* 291:585 */         int beg1 = idx1 - 1;
/* 292:586 */         while ((idx1 < len1) && (Character.isDigit(str1.charAt(idx1)))) {
/* 293:588 */           idx1++;
/* 294:    */         }
/* 295:591 */         int beg2 = idx2 - 1;
/* 296:592 */         while ((idx2 < len2) && (Character.isDigit(str2.charAt(idx2)))) {
/* 297:594 */           idx2++;
/* 298:    */         }
/* 299:598 */         int cmp = new BigInteger(str1.substring(beg1, idx1)).compareTo(new BigInteger(str2.substring(beg2, idx2)));
/* 300:602 */         if (cmp != 0) {
/* 301:602 */           return cmp;
/* 302:    */         }
/* 303:    */       }
/* 304:604 */       else if (c1 != c2)
/* 305:    */       {
/* 306:606 */         return c1 - c2;
/* 307:    */       }
/* 308:    */     }
/* 309:610 */     return len1 - len2;
/* 310:    */   }
/* 311:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.PackagePartName
 * JD-Core Version:    0.7.0.1
 */