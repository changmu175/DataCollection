/*   1:    */ package org.apache.poi.poifs.eventfilesystem;
/*   2:    */ 
/*   3:    */ import java.io.FileInputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import org.apache.poi.poifs.filesystem.DocumentInputStream;
/*   9:    */ import org.apache.poi.poifs.filesystem.OPOIFSDocument;
/*  10:    */ import org.apache.poi.poifs.filesystem.POIFSDocumentPath;
/*  11:    */ import org.apache.poi.poifs.property.DirectoryProperty;
/*  12:    */ import org.apache.poi.poifs.property.Property;
/*  13:    */ import org.apache.poi.poifs.property.PropertyTable;
/*  14:    */ import org.apache.poi.poifs.property.RootProperty;
/*  15:    */ import org.apache.poi.poifs.storage.BlockAllocationTableReader;
/*  16:    */ import org.apache.poi.poifs.storage.BlockList;
/*  17:    */ import org.apache.poi.poifs.storage.HeaderBlock;
/*  18:    */ import org.apache.poi.poifs.storage.RawDataBlockList;
/*  19:    */ import org.apache.poi.poifs.storage.SmallBlockTableReader;
/*  20:    */ import org.apache.poi.util.IOUtils;
/*  21:    */ 
/*  22:    */ public class POIFSReader
/*  23:    */ {
/*  24:    */   private final POIFSReaderRegistry registry;
/*  25:    */   private boolean registryClosed;
/*  26: 54 */   private boolean notifyEmptyDirectories = false;
/*  27:    */   
/*  28:    */   public POIFSReader()
/*  29:    */   {
/*  30: 62 */     this.registry = new POIFSReaderRegistry();
/*  31: 63 */     this.registryClosed = false;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void read(InputStream stream)
/*  35:    */     throws IOException
/*  36:    */   {
/*  37: 77 */     this.registryClosed = true;
/*  38:    */     
/*  39:    */ 
/*  40: 80 */     HeaderBlock header_block = new HeaderBlock(stream);
/*  41:    */     
/*  42:    */ 
/*  43: 83 */     RawDataBlockList data_blocks = new RawDataBlockList(stream, header_block.getBigBlockSize());
/*  44:    */     
/*  45:    */ 
/*  46:    */ 
/*  47: 87 */     new BlockAllocationTableReader(header_block.getBigBlockSize(), header_block.getBATCount(), header_block.getBATArray(), header_block.getXBATCount(), header_block.getXBATIndex(), data_blocks);
/*  48:    */     
/*  49:    */ 
/*  50:    */ 
/*  51:    */ 
/*  52:    */ 
/*  53:    */ 
/*  54:    */ 
/*  55: 95 */     PropertyTable properties = new PropertyTable(header_block, data_blocks);
/*  56:    */     
/*  57:    */ 
/*  58:    */ 
/*  59: 99 */     RootProperty root = properties.getRoot();
/*  60:100 */     processProperties(SmallBlockTableReader.getSmallDocumentBlocks(header_block.getBigBlockSize(), data_blocks, root, header_block.getSBATStart()), data_blocks, root.getChildren(), new POIFSDocumentPath());
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void registerListener(POIFSReaderListener listener)
/*  64:    */   {
/*  65:122 */     if (listener == null) {
/*  66:124 */       throw new NullPointerException();
/*  67:    */     }
/*  68:126 */     if (this.registryClosed) {
/*  69:128 */       throw new IllegalStateException();
/*  70:    */     }
/*  71:130 */     this.registry.registerListener(listener);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void registerListener(POIFSReaderListener listener, String name)
/*  75:    */   {
/*  76:149 */     registerListener(listener, null, name);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void registerListener(POIFSReaderListener listener, POIFSDocumentPath path, String name)
/*  80:    */   {
/*  81:171 */     if ((listener == null) || (name == null) || (name.length() == 0)) {
/*  82:173 */       throw new NullPointerException();
/*  83:    */     }
/*  84:175 */     if (this.registryClosed) {
/*  85:177 */       throw new IllegalStateException();
/*  86:    */     }
/*  87:179 */     this.registry.registerListener(listener, path == null ? new POIFSDocumentPath() : path, name);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setNotifyEmptyDirectories(boolean notifyEmptyDirectories)
/*  91:    */   {
/*  92:192 */     this.notifyEmptyDirectories = notifyEmptyDirectories;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static void main(String[] args)
/*  96:    */     throws IOException
/*  97:    */   {
/*  98:207 */     if (args.length == 0)
/*  99:    */     {
/* 100:209 */       System.err.println("at least one argument required: input filename(s)");
/* 101:210 */       System.exit(1);
/* 102:    */     }
/* 103:214 */     for (String arg : args)
/* 104:    */     {
/* 105:216 */       POIFSReader reader = new POIFSReader();
/* 106:217 */       POIFSReaderListener listener = new SampleListener();
/* 107:    */       
/* 108:219 */       reader.registerListener(listener);
/* 109:220 */       System.out.println("reading " + arg);
/* 110:221 */       FileInputStream istream = new FileInputStream(arg);
/* 111:    */       
/* 112:223 */       reader.read(istream);
/* 113:224 */       istream.close();
/* 114:    */     }
/* 115:    */   }
/* 116:    */   
/* 117:    */   private void processProperties(BlockList small_blocks, BlockList big_blocks, Iterator<Property> properties, POIFSDocumentPath path)
/* 118:    */     throws IOException
/* 119:    */   {
/* 120:233 */     if ((!properties.hasNext()) && (this.notifyEmptyDirectories))
/* 121:    */     {
/* 122:234 */       Iterator<POIFSReaderListener> listeners = this.registry.getListeners(path, ".");
/* 123:235 */       while (listeners.hasNext())
/* 124:    */       {
/* 125:236 */         POIFSReaderListener pl = (POIFSReaderListener)listeners.next();
/* 126:237 */         POIFSReaderEvent pe = new POIFSReaderEvent(null, path, null);
/* 127:238 */         pl.processPOIFSReaderEvent(pe);
/* 128:    */       }
/* 129:240 */       return;
/* 130:    */     }
/* 131:243 */     while (properties.hasNext())
/* 132:    */     {
/* 133:245 */       Property property = (Property)properties.next();
/* 134:246 */       String name = property.getName();
/* 135:248 */       if (property.isDirectory())
/* 136:    */       {
/* 137:249 */         POIFSDocumentPath new_path = new POIFSDocumentPath(path, new String[] { name });
/* 138:250 */         DirectoryProperty dp = (DirectoryProperty)property;
/* 139:251 */         processProperties(small_blocks, big_blocks, dp.getChildren(), new_path);
/* 140:    */       }
/* 141:    */       else
/* 142:    */       {
/* 143:253 */         int startBlock = property.getStartBlock();
/* 144:254 */         Iterator<POIFSReaderListener> listeners = this.registry.getListeners(path, name);
/* 145:256 */         if (listeners.hasNext())
/* 146:    */         {
/* 147:258 */           int size = property.getSize();
/* 148:259 */           OPOIFSDocument document = null;
/* 149:261 */           if (property.shouldUseSmallBlocks()) {
/* 150:263 */             document = new OPOIFSDocument(name, small_blocks.fetchBlocks(startBlock, -1), size);
/* 151:    */           } else {
/* 152:269 */             document = new OPOIFSDocument(name, big_blocks.fetchBlocks(startBlock, -1), size);
/* 153:    */           }
/* 154:273 */           while (listeners.hasNext())
/* 155:    */           {
/* 156:275 */             POIFSReaderListener listener = (POIFSReaderListener)listeners.next();
/* 157:    */             
/* 158:277 */             listener.processPOIFSReaderEvent(new POIFSReaderEvent(new DocumentInputStream(document), path, name));
/* 159:    */           }
/* 160:    */         }
/* 161:287 */         else if (property.shouldUseSmallBlocks())
/* 162:    */         {
/* 163:289 */           small_blocks.fetchBlocks(startBlock, -1);
/* 164:    */         }
/* 165:    */         else
/* 166:    */         {
/* 167:293 */           big_blocks.fetchBlocks(startBlock, -1);
/* 168:    */         }
/* 169:    */       }
/* 170:    */     }
/* 171:    */   }
/* 172:    */   
/* 173:    */   private static class SampleListener
/* 174:    */     implements POIFSReaderListener
/* 175:    */   {
/* 176:    */     public void processPOIFSReaderEvent(POIFSReaderEvent event)
/* 177:    */     {
/* 178:320 */       DocumentInputStream istream = event.getStream();
/* 179:321 */       POIFSDocumentPath path = event.getPath();
/* 180:322 */       String name = event.getName();
/* 181:    */       try
/* 182:    */       {
/* 183:325 */         byte[] data = IOUtils.toByteArray(istream);
/* 184:326 */         int pathLength = path.length();
/* 185:328 */         for (int k = 0; k < pathLength; k++) {
/* 186:329 */           System.out.print("/" + path.getComponent(k));
/* 187:    */         }
/* 188:331 */         System.out.println("/" + name + ": " + data.length + " bytes read");
/* 189:    */       }
/* 190:    */       catch (IOException ignored) {}finally
/* 191:    */       {
/* 192:334 */         IOUtils.closeQuietly(istream);
/* 193:    */       }
/* 194:    */     }
/* 195:    */   }
/* 196:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.eventfilesystem.POIFSReader
 * JD-Core Version:    0.7.0.1
 */