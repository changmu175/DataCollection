/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import org.apache.poi.util.POILogFactory;
/*   5:    */ import org.apache.poi.util.POILogger;
/*   6:    */ 
/*   7:    */ public class POIFSDocumentPath
/*   8:    */ {
/*   9: 36 */   private static final POILogger log = POILogFactory.getLogger(POIFSDocumentPath.class);
/*  10:    */   private final String[] components;
/*  11: 39 */   private int hashcode = 0;
/*  12:    */   
/*  13:    */   public POIFSDocumentPath(String[] components)
/*  14:    */     throws IllegalArgumentException
/*  15:    */   {
/*  16: 68 */     if (components == null)
/*  17:    */     {
/*  18: 70 */       this.components = new String[0];
/*  19:    */     }
/*  20:    */     else
/*  21:    */     {
/*  22: 74 */       this.components = new String[components.length];
/*  23: 75 */       for (int j = 0; j < components.length; j++)
/*  24:    */       {
/*  25: 77 */         if ((components[j] == null) || (components[j].length() == 0)) {
/*  26: 80 */           throw new IllegalArgumentException("components cannot contain null or empty strings");
/*  27:    */         }
/*  28: 83 */         this.components[j] = components[j];
/*  29:    */       }
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public POIFSDocumentPath()
/*  34:    */   {
/*  35: 97 */     this.components = new String[0];
/*  36:    */   }
/*  37:    */   
/*  38:    */   public POIFSDocumentPath(POIFSDocumentPath path, String[] components)
/*  39:    */     throws IllegalArgumentException
/*  40:    */   {
/*  41:116 */     if (components == null) {
/*  42:118 */       this.components = new String[path.components.length];
/*  43:    */     } else {
/*  44:122 */       this.components = new String[path.components.length + components.length];
/*  45:    */     }
/*  46:125 */     for (int j = 0; j < path.components.length; j++) {
/*  47:127 */       this.components[j] = path.components[j];
/*  48:    */     }
/*  49:129 */     if (components != null) {
/*  50:131 */       for (int j = 0; j < components.length; j++)
/*  51:    */       {
/*  52:133 */         if (components[j] == null) {
/*  53:135 */           throw new IllegalArgumentException("components cannot contain null");
/*  54:    */         }
/*  55:138 */         if (components[j].length() == 0) {
/*  56:140 */           log.log(5, new Object[] { "Directory under " + path + " has an empty name, " + "not all OLE2 readers will handle this file correctly!" });
/*  57:    */         }
/*  58:144 */         this.components[(j + path.components.length)] = components[j];
/*  59:    */       }
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   public boolean equals(Object o)
/*  64:    */   {
/*  65:162 */     boolean rval = false;
/*  66:164 */     if ((o != null) && (o.getClass() == getClass())) {
/*  67:166 */       if (this == o)
/*  68:    */       {
/*  69:168 */         rval = true;
/*  70:    */       }
/*  71:    */       else
/*  72:    */       {
/*  73:172 */         POIFSDocumentPath path = (POIFSDocumentPath)o;
/*  74:174 */         if (path.components.length == this.components.length)
/*  75:    */         {
/*  76:176 */           rval = true;
/*  77:177 */           for (int j = 0; j < this.components.length; j++) {
/*  78:179 */             if (!path.components[j].equals(this.components[j]))
/*  79:    */             {
/*  80:182 */               rval = false;
/*  81:183 */               break;
/*  82:    */             }
/*  83:    */           }
/*  84:    */         }
/*  85:    */       }
/*  86:    */     }
/*  87:189 */     return rval;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public int hashCode()
/*  91:    */   {
/*  92:200 */     if (this.hashcode == 0) {
/*  93:202 */       this.hashcode = computeHashCode();
/*  94:    */     }
/*  95:204 */     return this.hashcode;
/*  96:    */   }
/*  97:    */   
/*  98:    */   private int computeHashCode()
/*  99:    */   {
/* 100:208 */     int code = 0;
/* 101:209 */     for (int j = 0; j < this.components.length; j++) {
/* 102:211 */       code += this.components[j].hashCode();
/* 103:    */     }
/* 104:213 */     return code;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public int length()
/* 108:    */   {
/* 109:222 */     return this.components.length;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public String getComponent(int n)
/* 113:    */     throws ArrayIndexOutOfBoundsException
/* 114:    */   {
/* 115:238 */     return this.components[n];
/* 116:    */   }
/* 117:    */   
/* 118:    */   public POIFSDocumentPath getParent()
/* 119:    */   {
/* 120:251 */     int length = this.components.length - 1;
/* 121:253 */     if (length < 0) {
/* 122:255 */       return null;
/* 123:    */     }
/* 124:257 */     String[] parentComponents = new String[length];
/* 125:258 */     System.arraycopy(this.components, 0, parentComponents, 0, length);
/* 126:259 */     POIFSDocumentPath parent = new POIFSDocumentPath(parentComponents);
/* 127:    */     
/* 128:261 */     return parent;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public String getName()
/* 132:    */   {
/* 133:274 */     if (this.components.length == 0) {
/* 134:275 */       return "";
/* 135:    */     }
/* 136:277 */     return this.components[(this.components.length - 1)];
/* 137:    */   }
/* 138:    */   
/* 139:    */   public String toString()
/* 140:    */   {
/* 141:291 */     StringBuffer b = new StringBuffer();
/* 142:292 */     int l = length();
/* 143:    */     
/* 144:294 */     b.append(File.separatorChar);
/* 145:295 */     for (int i = 0; i < l; i++)
/* 146:    */     {
/* 147:297 */       b.append(getComponent(i));
/* 148:298 */       if (i < l - 1) {
/* 149:300 */         b.append(File.separatorChar);
/* 150:    */       }
/* 151:    */     }
/* 152:303 */     return b.toString();
/* 153:    */   }
/* 154:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.POIFSDocumentPath
 * JD-Core Version:    0.7.0.1
 */