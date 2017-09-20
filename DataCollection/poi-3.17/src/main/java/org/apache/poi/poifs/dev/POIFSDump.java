/*   1:    */ package org.apache.poi.poifs.dev;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.io.FileOutputStream;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.nio.ByteBuffer;
/*  10:    */ import java.util.Iterator;
/*  11:    */ import org.apache.poi.poifs.filesystem.DirectoryEntry;
/*  12:    */ import org.apache.poi.poifs.filesystem.DocumentInputStream;
/*  13:    */ import org.apache.poi.poifs.filesystem.DocumentNode;
/*  14:    */ import org.apache.poi.poifs.filesystem.Entry;
/*  15:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  16:    */ import org.apache.poi.poifs.filesystem.NPOIFSStream;
/*  17:    */ import org.apache.poi.poifs.property.NPropertyTable;
/*  18:    */ import org.apache.poi.poifs.property.RootProperty;
/*  19:    */ import org.apache.poi.poifs.storage.HeaderBlock;
/*  20:    */ import org.apache.poi.util.IOUtils;
/*  21:    */ 
/*  22:    */ public class POIFSDump
/*  23:    */ {
/*  24:    */   public static void main(String[] args)
/*  25:    */     throws IOException
/*  26:    */   {
/*  27: 43 */     if (args.length == 0)
/*  28:    */     {
/*  29: 44 */       System.err.println("Must specify at least one file to dump");
/*  30: 45 */       System.exit(1);
/*  31:    */     }
/*  32: 48 */     boolean dumpProps = false;boolean dumpMini = false;
/*  33: 49 */     for (String filename : args) {
/*  34: 50 */       if ((filename.equalsIgnoreCase("-dumprops")) || (filename.equalsIgnoreCase("-dump-props")) || (filename.equalsIgnoreCase("-dump-properties")))
/*  35:    */       {
/*  36: 53 */         dumpProps = true;
/*  37:    */       }
/*  38: 56 */       else if ((filename.equalsIgnoreCase("-dumpmini")) || (filename.equalsIgnoreCase("-dump-mini")) || (filename.equalsIgnoreCase("-dump-ministream")) || (filename.equalsIgnoreCase("-dump-mini-stream")))
/*  39:    */       {
/*  40: 60 */         dumpMini = true;
/*  41:    */       }
/*  42:    */       else
/*  43:    */       {
/*  44: 64 */         System.out.println("Dumping " + filename);
/*  45: 65 */         FileInputStream is = new FileInputStream(filename);
/*  46:    */         NPOIFSFileSystem fs;
/*  47:    */         try
/*  48:    */         {
/*  49: 68 */           fs = new NPOIFSFileSystem(is);
/*  50:    */         }
/*  51:    */         finally
/*  52:    */         {
/*  53: 70 */           is.close();
/*  54:    */         }
/*  55:    */         try
/*  56:    */         {
/*  57: 73 */           Object root = fs.getRoot();
/*  58: 74 */           String filenameWithoutPath = new File(filename).getName();
/*  59: 75 */           File dumpDir = new File(filenameWithoutPath + "_dump");
/*  60: 76 */           File file = new File(dumpDir, ((DirectoryEntry)root).getName());
/*  61: 77 */           if ((!file.exists()) && (!file.mkdirs())) {
/*  62: 78 */             throw new IOException("Could not create directory " + file);
/*  63:    */           }
/*  64: 81 */           dump((DirectoryEntry)root, file);
/*  65: 83 */           if (dumpProps)
/*  66:    */           {
/*  67: 84 */             HeaderBlock header = fs.getHeaderBlock();
/*  68: 85 */             dump(fs, header.getPropertyStart(), "properties", file);
/*  69:    */           }
/*  70: 87 */           if (dumpMini)
/*  71:    */           {
/*  72: 88 */             NPropertyTable props = fs.getPropertyTable();
/*  73: 89 */             int startBlock = props.getRoot().getStartBlock();
/*  74: 90 */             if (startBlock == -2) {
/*  75: 91 */               System.err.println("No Mini Stream in file");
/*  76:    */             } else {
/*  77: 93 */               dump(fs, startBlock, "mini-stream", file);
/*  78:    */             }
/*  79:    */           }
/*  80:    */         }
/*  81:    */         finally
/*  82:    */         {
/*  83: 97 */           fs.close();
/*  84:    */         }
/*  85:    */       }
/*  86:    */     }
/*  87:    */   }
/*  88:    */   
/*  89:    */   public static void dump(DirectoryEntry root, File parent)
/*  90:    */     throws IOException
/*  91:    */   {
/*  92:103 */     for (Iterator<Entry> it = root.getEntries(); it.hasNext();)
/*  93:    */     {
/*  94:104 */       Entry entry = (Entry)it.next();
/*  95:105 */       if ((entry instanceof DocumentNode))
/*  96:    */       {
/*  97:106 */         DocumentNode node = (DocumentNode)entry;
/*  98:107 */         DocumentInputStream is = new DocumentInputStream(node);
/*  99:108 */         byte[] bytes = IOUtils.toByteArray(is);
/* 100:109 */         is.close();
/* 101:    */         
/* 102:111 */         OutputStream out = new FileOutputStream(new File(parent, node.getName().trim()));
/* 103:    */         try
/* 104:    */         {
/* 105:113 */           out.write(bytes);
/* 106:    */         }
/* 107:    */         finally
/* 108:    */         {
/* 109:115 */           out.close();
/* 110:    */         }
/* 111:    */       }
/* 112:117 */       else if ((entry instanceof DirectoryEntry))
/* 113:    */       {
/* 114:118 */         DirectoryEntry dir = (DirectoryEntry)entry;
/* 115:119 */         File file = new File(parent, entry.getName());
/* 116:120 */         if ((!file.exists()) && (!file.mkdirs())) {
/* 117:121 */           throw new IOException("Could not create directory " + file);
/* 118:    */         }
/* 119:123 */         dump(dir, file);
/* 120:    */       }
/* 121:    */       else
/* 122:    */       {
/* 123:125 */         System.err.println("Skipping unsupported POIFS entry: " + entry);
/* 124:    */       }
/* 125:    */     }
/* 126:    */   }
/* 127:    */   
/* 128:    */   public static void dump(NPOIFSFileSystem fs, int startBlock, String name, File parent)
/* 129:    */     throws IOException
/* 130:    */   {
/* 131:130 */     File file = new File(parent, name);
/* 132:131 */     FileOutputStream out = new FileOutputStream(file);
/* 133:    */     try
/* 134:    */     {
/* 135:133 */       NPOIFSStream stream = new NPOIFSStream(fs, startBlock);
/* 136:    */       
/* 137:135 */       b = new byte[fs.getBigBlockSize()];
/* 138:136 */       for (ByteBuffer bb : stream)
/* 139:    */       {
/* 140:137 */         int len = bb.remaining();
/* 141:138 */         bb.get(b);
/* 142:139 */         out.write(b, 0, len);
/* 143:    */       }
/* 144:    */     }
/* 145:    */     finally
/* 146:    */     {
/* 147:    */       byte[] b;
/* 148:142 */       out.close();
/* 149:    */     }
/* 150:    */   }
/* 151:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.dev.POIFSDump
 * JD-Core Version:    0.7.0.1
 */