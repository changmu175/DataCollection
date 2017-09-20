/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.FileNotFoundException;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import org.apache.poi.util.Internal;
/*  10:    */ 
/*  11:    */ @Internal
/*  12:    */ public class EntryUtils
/*  13:    */ {
/*  14:    */   @Internal
/*  15:    */   public static void copyNodeRecursively(Entry entry, DirectoryEntry target)
/*  16:    */     throws IOException
/*  17:    */   {
/*  18: 41 */     DirectoryEntry newTarget = null;
/*  19: 42 */     if (entry.isDirectoryEntry())
/*  20:    */     {
/*  21: 44 */       DirectoryEntry dirEntry = (DirectoryEntry)entry;
/*  22: 45 */       newTarget = target.createDirectory(entry.getName());
/*  23: 46 */       newTarget.setStorageClsid(dirEntry.getStorageClsid());
/*  24: 47 */       Iterator<Entry> entries = dirEntry.getEntries();
/*  25: 49 */       while (entries.hasNext()) {
/*  26: 51 */         copyNodeRecursively((Entry)entries.next(), newTarget);
/*  27:    */       }
/*  28:    */     }
/*  29:    */     else
/*  30:    */     {
/*  31: 56 */       DocumentEntry dentry = (DocumentEntry)entry;
/*  32: 57 */       DocumentInputStream dstream = new DocumentInputStream(dentry);
/*  33: 58 */       target.createDocument(dentry.getName(), dstream);
/*  34: 59 */       dstream.close();
/*  35:    */     }
/*  36:    */   }
/*  37:    */   
/*  38:    */   public static void copyNodes(DirectoryEntry sourceRoot, DirectoryEntry targetRoot)
/*  39:    */     throws IOException
/*  40:    */   {
/*  41: 74 */     for (Entry entry : sourceRoot) {
/*  42: 75 */       copyNodeRecursively(entry, targetRoot);
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static void copyNodes(FilteringDirectoryNode filteredSource, FilteringDirectoryNode filteredTarget)
/*  47:    */     throws IOException
/*  48:    */   {
/*  49: 90 */     copyNodes(filteredSource, filteredTarget);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static void copyNodes(OPOIFSFileSystem source, OPOIFSFileSystem target)
/*  53:    */     throws IOException
/*  54:    */   {
/*  55:104 */     copyNodes(source.getRoot(), target.getRoot());
/*  56:    */   }
/*  57:    */   
/*  58:    */   public static void copyNodes(NPOIFSFileSystem source, NPOIFSFileSystem target)
/*  59:    */     throws IOException
/*  60:    */   {
/*  61:117 */     copyNodes(source.getRoot(), target.getRoot());
/*  62:    */   }
/*  63:    */   
/*  64:    */   public static void copyNodes(OPOIFSFileSystem source, OPOIFSFileSystem target, List<String> excepts)
/*  65:    */     throws IOException
/*  66:    */   {
/*  67:133 */     copyNodes(new FilteringDirectoryNode(source.getRoot(), excepts), new FilteringDirectoryNode(target.getRoot(), excepts));
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static void copyNodes(NPOIFSFileSystem source, NPOIFSFileSystem target, List<String> excepts)
/*  71:    */     throws IOException
/*  72:    */   {
/*  73:151 */     copyNodes(new FilteringDirectoryNode(source.getRoot(), excepts), new FilteringDirectoryNode(target.getRoot(), excepts));
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static boolean areDirectoriesIdentical(DirectoryEntry dirA, DirectoryEntry dirB)
/*  77:    */   {
/*  78:167 */     if (!dirA.getName().equals(dirB.getName())) {
/*  79:168 */       return false;
/*  80:    */     }
/*  81:172 */     if (dirA.getEntryCount() != dirB.getEntryCount()) {
/*  82:173 */       return false;
/*  83:    */     }
/*  84:177 */     Map<String, Integer> aSizes = new HashMap();
/*  85:178 */     int isDirectory = -12345;
/*  86:179 */     for (Entry a : dirA)
/*  87:    */     {
/*  88:180 */       String aName = a.getName();
/*  89:181 */       if (a.isDirectoryEntry()) {
/*  90:182 */         aSizes.put(aName, Integer.valueOf(-12345));
/*  91:    */       } else {
/*  92:184 */         aSizes.put(aName, Integer.valueOf(((DocumentNode)a).getSize()));
/*  93:    */       }
/*  94:    */     }
/*  95:187 */     for (Entry b : dirB)
/*  96:    */     {
/*  97:188 */       String bName = b.getName();
/*  98:189 */       if (!aSizes.containsKey(bName)) {
/*  99:191 */         return false;
/* 100:    */       }
/* 101:    */       int size;
/* 102:    */       int size;
/* 103:195 */       if (b.isDirectoryEntry()) {
/* 104:196 */         size = -12345;
/* 105:    */       } else {
/* 106:198 */         size = ((DocumentNode)b).getSize();
/* 107:    */       }
/* 108:200 */       if (size != ((Integer)aSizes.get(bName)).intValue()) {
/* 109:202 */         return false;
/* 110:    */       }
/* 111:206 */       aSizes.remove(bName);
/* 112:    */     }
/* 113:208 */     if (!aSizes.isEmpty()) {
/* 114:210 */       return false;
/* 115:    */     }
/* 116:214 */     for (Entry a : dirA) {
/* 117:    */       try
/* 118:    */       {
/* 119:216 */         Entry b = dirB.getEntry(a.getName());
/* 120:    */         boolean match;
/* 121:    */         boolean match;
/* 122:218 */         if (a.isDirectoryEntry()) {
/* 123:219 */           match = areDirectoriesIdentical((DirectoryEntry)a, (DirectoryEntry)b);
/* 124:    */         } else {
/* 125:222 */           match = areDocumentsIdentical((DocumentEntry)a, (DocumentEntry)b);
/* 126:    */         }
/* 127:225 */         if (!match) {
/* 128:225 */           return false;
/* 129:    */         }
/* 130:    */       }
/* 131:    */       catch (FileNotFoundException e)
/* 132:    */       {
/* 133:228 */         return false;
/* 134:    */       }
/* 135:    */       catch (IOException e)
/* 136:    */       {
/* 137:231 */         return false;
/* 138:    */       }
/* 139:    */     }
/* 140:236 */     return true;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public static boolean areDocumentsIdentical(DocumentEntry docA, DocumentEntry docB)
/* 144:    */     throws IOException
/* 145:    */   {
/* 146:245 */     if (!docA.getName().equals(docB.getName())) {
/* 147:247 */       return false;
/* 148:    */     }
/* 149:249 */     if (docA.getSize() != docB.getSize()) {
/* 150:251 */       return false;
/* 151:    */     }
/* 152:254 */     boolean matches = true;
/* 153:255 */     DocumentInputStream inpA = null;DocumentInputStream inpB = null;
/* 154:    */     try
/* 155:    */     {
/* 156:257 */       inpA = new DocumentInputStream(docA);
/* 157:258 */       inpB = new DocumentInputStream(docB);
/* 158:    */       int readB;
/* 159:    */       do
/* 160:    */       {
/* 161:262 */         int readA = inpA.read();
/* 162:263 */         readB = inpB.read();
/* 163:264 */         if (readA != readB)
/* 164:    */         {
/* 165:265 */           matches = false;
/* 166:266 */           break;
/* 167:    */         }
/* 168:268 */         if (readA == -1) {
/* 169:    */           break;
/* 170:    */         }
/* 171:268 */       } while (readB != -1);
/* 172:    */     }
/* 173:    */     finally
/* 174:    */     {
/* 175:270 */       if (inpA != null) {
/* 176:270 */         inpA.close();
/* 177:    */       }
/* 178:271 */       if (inpB != null) {
/* 179:271 */         inpB.close();
/* 180:    */       }
/* 181:    */     }
/* 182:274 */     return matches;
/* 183:    */   }
/* 184:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.EntryUtils
 * JD-Core Version:    0.7.0.1
 */