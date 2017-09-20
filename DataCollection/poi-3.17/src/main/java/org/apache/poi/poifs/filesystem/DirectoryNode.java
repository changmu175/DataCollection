/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.FileNotFoundException;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.Iterator;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Map;
/*  11:    */ import java.util.Set;
/*  12:    */ import org.apache.poi.hpsf.ClassID;
/*  13:    */ import org.apache.poi.poifs.dev.POIFSViewable;
/*  14:    */ import org.apache.poi.poifs.property.DirectoryProperty;
/*  15:    */ import org.apache.poi.poifs.property.DocumentProperty;
/*  16:    */ import org.apache.poi.poifs.property.Property;
/*  17:    */ 
/*  18:    */ public class DirectoryNode
/*  19:    */   extends EntryNode
/*  20:    */   implements DirectoryEntry, POIFSViewable, Iterable<Entry>
/*  21:    */ {
/*  22:    */   private Map<String, Entry> _byname;
/*  23:    */   private ArrayList<Entry> _entries;
/*  24:    */   private OPOIFSFileSystem _ofilesystem;
/*  25:    */   private NPOIFSFileSystem _nfilesystem;
/*  26:    */   private POIFSDocumentPath _path;
/*  27:    */   
/*  28:    */   DirectoryNode(DirectoryProperty property, OPOIFSFileSystem filesystem, DirectoryNode parent)
/*  29:    */   {
/*  30: 72 */     this(property, parent, filesystem, (NPOIFSFileSystem)null);
/*  31:    */   }
/*  32:    */   
/*  33:    */   DirectoryNode(DirectoryProperty property, NPOIFSFileSystem nfilesystem, DirectoryNode parent)
/*  34:    */   {
/*  35: 87 */     this(property, parent, (OPOIFSFileSystem)null, nfilesystem);
/*  36:    */   }
/*  37:    */   
/*  38:    */   private DirectoryNode(DirectoryProperty property, DirectoryNode parent, OPOIFSFileSystem ofilesystem, NPOIFSFileSystem nfilesystem)
/*  39:    */   {
/*  40: 95 */     super(property, parent);
/*  41: 96 */     this._ofilesystem = ofilesystem;
/*  42: 97 */     this._nfilesystem = nfilesystem;
/*  43: 99 */     if (parent == null) {
/*  44:101 */       this._path = new POIFSDocumentPath();
/*  45:    */     } else {
/*  46:105 */       this._path = new POIFSDocumentPath(parent._path, new String[] { property.getName() });
/*  47:    */     }
/*  48:110 */     this._byname = new HashMap();
/*  49:111 */     this._entries = new ArrayList();
/*  50:112 */     Iterator<Property> iter = property.getChildren();
/*  51:114 */     while (iter.hasNext())
/*  52:    */     {
/*  53:116 */       Property child = (Property)iter.next();
/*  54:117 */       Entry childNode = null;
/*  55:119 */       if (child.isDirectory())
/*  56:    */       {
/*  57:121 */         DirectoryProperty childDir = (DirectoryProperty)child;
/*  58:122 */         if (this._ofilesystem != null) {
/*  59:123 */           childNode = new DirectoryNode(childDir, this._ofilesystem, this);
/*  60:    */         } else {
/*  61:125 */           childNode = new DirectoryNode(childDir, this._nfilesystem, this);
/*  62:    */         }
/*  63:    */       }
/*  64:    */       else
/*  65:    */       {
/*  66:130 */         childNode = new DocumentNode((DocumentProperty)child, this);
/*  67:    */       }
/*  68:132 */       this._entries.add(childNode);
/*  69:133 */       this._byname.put(childNode.getName(), childNode);
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   public POIFSDocumentPath getPath()
/*  74:    */   {
/*  75:143 */     return this._path;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public NPOIFSFileSystem getFileSystem()
/*  79:    */   {
/*  80:151 */     return this._nfilesystem;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public OPOIFSFileSystem getOFileSystem()
/*  84:    */   {
/*  85:161 */     return this._ofilesystem;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public NPOIFSFileSystem getNFileSystem()
/*  89:    */   {
/*  90:171 */     return this._nfilesystem;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public DocumentInputStream createDocumentInputStream(String documentName)
/*  94:    */     throws IOException
/*  95:    */   {
/*  96:188 */     return createDocumentInputStream(getEntry(documentName));
/*  97:    */   }
/*  98:    */   
/*  99:    */   public DocumentInputStream createDocumentInputStream(Entry document)
/* 100:    */     throws IOException
/* 101:    */   {
/* 102:205 */     if (!document.isDocumentEntry()) {
/* 103:206 */       throw new IOException("Entry '" + document.getName() + "' is not a DocumentEntry");
/* 104:    */     }
/* 105:210 */     DocumentEntry entry = (DocumentEntry)document;
/* 106:211 */     return new DocumentInputStream(entry);
/* 107:    */   }
/* 108:    */   
/* 109:    */   DocumentEntry createDocument(OPOIFSDocument document)
/* 110:    */     throws IOException
/* 111:    */   {
/* 112:226 */     DocumentProperty property = document.getDocumentProperty();
/* 113:227 */     DocumentNode rval = new DocumentNode(property, this);
/* 114:    */     
/* 115:229 */     ((DirectoryProperty)getProperty()).addChild(property);
/* 116:230 */     this._ofilesystem.addDocument(document);
/* 117:    */     
/* 118:232 */     this._entries.add(rval);
/* 119:233 */     this._byname.put(property.getName(), rval);
/* 120:234 */     return rval;
/* 121:    */   }
/* 122:    */   
/* 123:    */   DocumentEntry createDocument(NPOIFSDocument document)
/* 124:    */     throws IOException
/* 125:    */   {
/* 126:249 */     DocumentProperty property = document.getDocumentProperty();
/* 127:250 */     DocumentNode rval = new DocumentNode(property, this);
/* 128:    */     
/* 129:252 */     ((DirectoryProperty)getProperty()).addChild(property);
/* 130:253 */     this._nfilesystem.addDocument(document);
/* 131:    */     
/* 132:255 */     this._entries.add(rval);
/* 133:256 */     this._byname.put(property.getName(), rval);
/* 134:257 */     return rval;
/* 135:    */   }
/* 136:    */   
/* 137:    */   boolean changeName(String oldName, String newName)
/* 138:    */   {
/* 139:270 */     boolean rval = false;
/* 140:271 */     EntryNode child = (EntryNode)this._byname.get(oldName);
/* 141:273 */     if (child != null)
/* 142:    */     {
/* 143:275 */       rval = ((DirectoryProperty)getProperty()).changeName(child.getProperty(), newName);
/* 144:277 */       if (rval)
/* 145:    */       {
/* 146:279 */         this._byname.remove(oldName);
/* 147:280 */         this._byname.put(child.getProperty().getName(), child);
/* 148:    */       }
/* 149:    */     }
/* 150:283 */     return rval;
/* 151:    */   }
/* 152:    */   
/* 153:    */   boolean deleteEntry(EntryNode entry)
/* 154:    */   {
/* 155:296 */     boolean rval = ((DirectoryProperty)getProperty()).deleteChild(entry.getProperty());
/* 156:300 */     if (rval)
/* 157:    */     {
/* 158:302 */       this._entries.remove(entry);
/* 159:303 */       this._byname.remove(entry.getName());
/* 160:305 */       if (this._ofilesystem != null) {
/* 161:306 */         this._ofilesystem.remove(entry);
/* 162:    */       } else {
/* 163:    */         try
/* 164:    */         {
/* 165:309 */           this._nfilesystem.remove(entry);
/* 166:    */         }
/* 167:    */         catch (IOException e) {}
/* 168:    */       }
/* 169:    */     }
/* 170:315 */     return rval;
/* 171:    */   }
/* 172:    */   
/* 173:    */   public Iterator<Entry> getEntries()
/* 174:    */   {
/* 175:333 */     return this._entries.iterator();
/* 176:    */   }
/* 177:    */   
/* 178:    */   public Set<String> getEntryNames()
/* 179:    */   {
/* 180:347 */     return this._byname.keySet();
/* 181:    */   }
/* 182:    */   
/* 183:    */   public boolean isEmpty()
/* 184:    */   {
/* 185:358 */     return this._entries.isEmpty();
/* 186:    */   }
/* 187:    */   
/* 188:    */   public int getEntryCount()
/* 189:    */   {
/* 190:371 */     return this._entries.size();
/* 191:    */   }
/* 192:    */   
/* 193:    */   public boolean hasEntry(String name)
/* 194:    */   {
/* 195:376 */     return (name != null) && (this._byname.containsKey(name));
/* 196:    */   }
/* 197:    */   
/* 198:    */   public Entry getEntry(String name)
/* 199:    */     throws FileNotFoundException
/* 200:    */   {
/* 201:392 */     Entry rval = null;
/* 202:394 */     if (name != null) {
/* 203:395 */       rval = (Entry)this._byname.get(name);
/* 204:    */     }
/* 205:397 */     if (rval == null) {
/* 206:399 */       throw new FileNotFoundException("no such entry: \"" + name + "\", had: " + this._byname.keySet());
/* 207:    */     }
/* 208:402 */     return rval;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public DocumentEntry createDocument(String name, InputStream stream)
/* 212:    */     throws IOException
/* 213:    */   {
/* 214:421 */     if (this._nfilesystem != null) {
/* 215:422 */       return createDocument(new NPOIFSDocument(name, this._nfilesystem, stream));
/* 216:    */     }
/* 217:424 */     return createDocument(new OPOIFSDocument(name, stream));
/* 218:    */   }
/* 219:    */   
/* 220:    */   public DocumentEntry createDocument(String name, int size, POIFSWriterListener writer)
/* 221:    */     throws IOException
/* 222:    */   {
/* 223:444 */     if (this._nfilesystem != null) {
/* 224:445 */       return createDocument(new NPOIFSDocument(name, size, this._nfilesystem, writer));
/* 225:    */     }
/* 226:447 */     return createDocument(new OPOIFSDocument(name, size, this._path, writer));
/* 227:    */   }
/* 228:    */   
/* 229:    */   public DirectoryEntry createDirectory(String name)
/* 230:    */     throws IOException
/* 231:    */   {
/* 232:465 */     DirectoryProperty property = new DirectoryProperty(name);
/* 233:    */     DirectoryNode rval;
/* 234:467 */     if (this._ofilesystem != null)
/* 235:    */     {
/* 236:468 */       DirectoryNode rval = new DirectoryNode(property, this._ofilesystem, this);
/* 237:469 */       this._ofilesystem.addDirectory(property);
/* 238:    */     }
/* 239:    */     else
/* 240:    */     {
/* 241:471 */       rval = new DirectoryNode(property, this._nfilesystem, this);
/* 242:472 */       this._nfilesystem.addDirectory(property);
/* 243:    */     }
/* 244:475 */     ((DirectoryProperty)getProperty()).addChild(property);
/* 245:476 */     this._entries.add(rval);
/* 246:477 */     this._byname.put(name, rval);
/* 247:478 */     return rval;
/* 248:    */   }
/* 249:    */   
/* 250:    */   public DocumentEntry createOrUpdateDocument(String name, InputStream stream)
/* 251:    */     throws IOException
/* 252:    */   {
/* 253:497 */     if (!hasEntry(name)) {
/* 254:498 */       return createDocument(name, stream);
/* 255:    */     }
/* 256:500 */     DocumentNode existing = (DocumentNode)getEntry(name);
/* 257:501 */     if (this._nfilesystem != null)
/* 258:    */     {
/* 259:502 */       NPOIFSDocument nDoc = new NPOIFSDocument(existing);
/* 260:503 */       nDoc.replaceContents(stream);
/* 261:504 */       return existing;
/* 262:    */     }
/* 263:507 */     deleteEntry(existing);
/* 264:508 */     return createDocument(name, stream);
/* 265:    */   }
/* 266:    */   
/* 267:    */   public ClassID getStorageClsid()
/* 268:    */   {
/* 269:520 */     return getProperty().getStorageClsid();
/* 270:    */   }
/* 271:    */   
/* 272:    */   public void setStorageClsid(ClassID clsidStorage)
/* 273:    */   {
/* 274:530 */     getProperty().setStorageClsid(clsidStorage);
/* 275:    */   }
/* 276:    */   
/* 277:    */   public boolean isDirectoryEntry()
/* 278:    */   {
/* 279:545 */     return true;
/* 280:    */   }
/* 281:    */   
/* 282:    */   protected boolean isDeleteOK()
/* 283:    */   {
/* 284:564 */     return isEmpty();
/* 285:    */   }
/* 286:    */   
/* 287:    */   public Object[] getViewableArray()
/* 288:    */   {
/* 289:579 */     return new Object[0];
/* 290:    */   }
/* 291:    */   
/* 292:    */   public Iterator<Object> getViewableIterator()
/* 293:    */   {
/* 294:591 */     List<Object> components = new ArrayList();
/* 295:    */     
/* 296:593 */     components.add(getProperty());
/* 297:594 */     Iterator<Entry> iter = this._entries.iterator();
/* 298:595 */     while (iter.hasNext()) {
/* 299:597 */       components.add(iter.next());
/* 300:    */     }
/* 301:599 */     return components.iterator();
/* 302:    */   }
/* 303:    */   
/* 304:    */   public boolean preferArray()
/* 305:    */   {
/* 306:612 */     return false;
/* 307:    */   }
/* 308:    */   
/* 309:    */   public String getShortDescription()
/* 310:    */   {
/* 311:624 */     return getName();
/* 312:    */   }
/* 313:    */   
/* 314:    */   public Iterator<Entry> iterator()
/* 315:    */   {
/* 316:631 */     return getEntries();
/* 317:    */   }
/* 318:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.DirectoryNode
 * JD-Core Version:    0.7.0.1
 */