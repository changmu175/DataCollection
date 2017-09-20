/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.FileNotFoundException;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Collection;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.HashSet;
/*  10:    */ import java.util.Iterator;
/*  11:    */ import java.util.List;
/*  12:    */ import java.util.Map;
/*  13:    */ import java.util.Set;
/*  14:    */ import org.apache.poi.hpsf.ClassID;
/*  15:    */ 
/*  16:    */ public class FilteringDirectoryNode
/*  17:    */   implements DirectoryEntry
/*  18:    */ {
/*  19:    */   private Set<String> excludes;
/*  20:    */   private Map<String, List<String>> childExcludes;
/*  21:    */   private DirectoryEntry directory;
/*  22:    */   
/*  23:    */   public FilteringDirectoryNode(DirectoryEntry directory, Collection<String> excludes)
/*  24:    */   {
/*  25: 64 */     this.directory = directory;
/*  26:    */     
/*  27:    */ 
/*  28: 67 */     this.excludes = new HashSet();
/*  29: 68 */     this.childExcludes = new HashMap();
/*  30: 69 */     for (String excl : excludes)
/*  31:    */     {
/*  32: 70 */       int splitAt = excl.indexOf('/');
/*  33: 71 */       if (splitAt == -1)
/*  34:    */       {
/*  35: 73 */         this.excludes.add(excl);
/*  36:    */       }
/*  37:    */       else
/*  38:    */       {
/*  39: 76 */         String child = excl.substring(0, splitAt);
/*  40: 77 */         String childExcl = excl.substring(splitAt + 1);
/*  41: 78 */         if (!this.childExcludes.containsKey(child)) {
/*  42: 79 */           this.childExcludes.put(child, new ArrayList());
/*  43:    */         }
/*  44: 81 */         ((List)this.childExcludes.get(child)).add(childExcl);
/*  45:    */       }
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public DirectoryEntry createDirectory(String name)
/*  50:    */     throws IOException
/*  51:    */   {
/*  52: 87 */     return this.directory.createDirectory(name);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public DocumentEntry createDocument(String name, InputStream stream)
/*  56:    */     throws IOException
/*  57:    */   {
/*  58: 92 */     return this.directory.createDocument(name, stream);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public DocumentEntry createDocument(String name, int size, POIFSWriterListener writer)
/*  62:    */     throws IOException
/*  63:    */   {
/*  64: 97 */     return this.directory.createDocument(name, size, writer);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public Iterator<Entry> getEntries()
/*  68:    */   {
/*  69:101 */     return new FilteringIterator(null);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public Iterator<Entry> iterator()
/*  73:    */   {
/*  74:105 */     return getEntries();
/*  75:    */   }
/*  76:    */   
/*  77:    */   public int getEntryCount()
/*  78:    */   {
/*  79:109 */     int size = this.directory.getEntryCount();
/*  80:110 */     for (String excl : this.excludes) {
/*  81:111 */       if (this.directory.hasEntry(excl)) {
/*  82:112 */         size--;
/*  83:    */       }
/*  84:    */     }
/*  85:115 */     return size;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public Set<String> getEntryNames()
/*  89:    */   {
/*  90:119 */     Set<String> names = new HashSet();
/*  91:120 */     for (String name : this.directory.getEntryNames()) {
/*  92:121 */       if (!this.excludes.contains(name)) {
/*  93:122 */         names.add(name);
/*  94:    */       }
/*  95:    */     }
/*  96:125 */     return names;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public boolean isEmpty()
/* 100:    */   {
/* 101:129 */     return getEntryCount() == 0;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public boolean hasEntry(String name)
/* 105:    */   {
/* 106:133 */     if (this.excludes.contains(name)) {
/* 107:134 */       return false;
/* 108:    */     }
/* 109:136 */     return this.directory.hasEntry(name);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public Entry getEntry(String name)
/* 113:    */     throws FileNotFoundException
/* 114:    */   {
/* 115:140 */     if (this.excludes.contains(name)) {
/* 116:141 */       throw new FileNotFoundException(name);
/* 117:    */     }
/* 118:144 */     Entry entry = this.directory.getEntry(name);
/* 119:145 */     return wrapEntry(entry);
/* 120:    */   }
/* 121:    */   
/* 122:    */   private Entry wrapEntry(Entry entry)
/* 123:    */   {
/* 124:148 */     String name = entry.getName();
/* 125:149 */     if ((this.childExcludes.containsKey(name)) && ((entry instanceof DirectoryEntry))) {
/* 126:150 */       return new FilteringDirectoryNode((DirectoryEntry)entry, (Collection)this.childExcludes.get(name));
/* 127:    */     }
/* 128:153 */     return entry;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public ClassID getStorageClsid()
/* 132:    */   {
/* 133:157 */     return this.directory.getStorageClsid();
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void setStorageClsid(ClassID clsidStorage)
/* 137:    */   {
/* 138:161 */     this.directory.setStorageClsid(clsidStorage);
/* 139:    */   }
/* 140:    */   
/* 141:    */   public boolean delete()
/* 142:    */   {
/* 143:165 */     return this.directory.delete();
/* 144:    */   }
/* 145:    */   
/* 146:    */   public boolean renameTo(String newName)
/* 147:    */   {
/* 148:169 */     return this.directory.renameTo(newName);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public String getName()
/* 152:    */   {
/* 153:173 */     return this.directory.getName();
/* 154:    */   }
/* 155:    */   
/* 156:    */   public DirectoryEntry getParent()
/* 157:    */   {
/* 158:177 */     return this.directory.getParent();
/* 159:    */   }
/* 160:    */   
/* 161:    */   public boolean isDirectoryEntry()
/* 162:    */   {
/* 163:181 */     return true;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public boolean isDocumentEntry()
/* 167:    */   {
/* 168:185 */     return false;
/* 169:    */   }
/* 170:    */   
/* 171:    */   private class FilteringIterator
/* 172:    */     implements Iterator<Entry>
/* 173:    */   {
/* 174:    */     private Iterator<Entry> parent;
/* 175:    */     private Entry next;
/* 176:    */     
/* 177:    */     private FilteringIterator()
/* 178:    */     {
/* 179:193 */       this.parent = FilteringDirectoryNode.this.directory.getEntries();
/* 180:194 */       locateNext();
/* 181:    */     }
/* 182:    */     
/* 183:    */     private void locateNext()
/* 184:    */     {
/* 185:197 */       this.next = null;
/* 186:199 */       while ((this.parent.hasNext()) && (this.next == null))
/* 187:    */       {
/* 188:200 */         Entry e = (Entry)this.parent.next();
/* 189:201 */         if (!FilteringDirectoryNode.this.excludes.contains(e.getName())) {
/* 190:202 */           this.next = FilteringDirectoryNode.this.wrapEntry(e);
/* 191:    */         }
/* 192:    */       }
/* 193:    */     }
/* 194:    */     
/* 195:    */     public boolean hasNext()
/* 196:    */     {
/* 197:208 */       return this.next != null;
/* 198:    */     }
/* 199:    */     
/* 200:    */     public Entry next()
/* 201:    */     {
/* 202:212 */       Entry e = this.next;
/* 203:213 */       locateNext();
/* 204:214 */       return e;
/* 205:    */     }
/* 206:    */     
/* 207:    */     public void remove()
/* 208:    */     {
/* 209:218 */       throw new UnsupportedOperationException("Remove not supported");
/* 210:    */     }
/* 211:    */   }
/* 212:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.FilteringDirectoryNode
 * JD-Core Version:    0.7.0.1
 */