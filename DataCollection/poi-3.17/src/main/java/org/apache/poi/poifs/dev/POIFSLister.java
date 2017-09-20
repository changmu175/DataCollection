/*   1:    */ package org.apache.poi.poifs.dev;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*   9:    */ import org.apache.poi.poifs.filesystem.DocumentNode;
/*  10:    */ import org.apache.poi.poifs.filesystem.Entry;
/*  11:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  12:    */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*  13:    */ 
/*  14:    */ public class POIFSLister
/*  15:    */ {
/*  16:    */   public static void main(String[] args)
/*  17:    */     throws IOException
/*  18:    */   {
/*  19: 43 */     if (args.length == 0)
/*  20:    */     {
/*  21: 44 */       System.err.println("Must specify at least one file to view");
/*  22: 45 */       System.exit(1);
/*  23:    */     }
/*  24: 48 */     boolean withSizes = false;
/*  25: 49 */     boolean newPOIFS = true;
/*  26: 50 */     for (int j = 0; j < args.length; j++) {
/*  27: 51 */       if ((args[j].equalsIgnoreCase("-size")) || (args[j].equalsIgnoreCase("-sizes"))) {
/*  28: 52 */         withSizes = true;
/*  29: 53 */       } else if ((args[j].equalsIgnoreCase("-old")) || (args[j].equalsIgnoreCase("-old-poifs"))) {
/*  30: 54 */         newPOIFS = false;
/*  31: 56 */       } else if (newPOIFS) {
/*  32: 57 */         viewFile(args[j], withSizes);
/*  33:    */       } else {
/*  34: 59 */         viewFileOld(args[j], withSizes);
/*  35:    */       }
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static void viewFile(String filename, boolean withSizes)
/*  40:    */     throws IOException
/*  41:    */   {
/*  42: 66 */     NPOIFSFileSystem fs = new NPOIFSFileSystem(new File(filename));
/*  43: 67 */     displayDirectory(fs.getRoot(), "", withSizes);
/*  44: 68 */     fs.close();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static void viewFileOld(String filename, boolean withSizes)
/*  48:    */     throws IOException
/*  49:    */   {
/*  50: 72 */     POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filename));
/*  51: 73 */     displayDirectory(fs.getRoot(), "", withSizes);
/*  52: 74 */     fs.close();
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static void displayDirectory(DirectoryNode dir, String indent, boolean withSizes)
/*  56:    */   {
/*  57: 78 */     System.out.println(indent + dir.getName() + " -");
/*  58: 79 */     String newIndent = indent + "  ";
/*  59:    */     
/*  60: 81 */     boolean hadChildren = false;
/*  61: 82 */     for (Iterator<Entry> it = dir.getEntries(); it.hasNext();)
/*  62:    */     {
/*  63: 83 */       hadChildren = true;
/*  64: 84 */       Entry entry = (Entry)it.next();
/*  65: 85 */       if ((entry instanceof DirectoryNode))
/*  66:    */       {
/*  67: 86 */         displayDirectory((DirectoryNode)entry, newIndent, withSizes);
/*  68:    */       }
/*  69:    */       else
/*  70:    */       {
/*  71: 88 */         DocumentNode doc = (DocumentNode)entry;
/*  72: 89 */         String name = doc.getName();
/*  73: 90 */         String size = "";
/*  74: 91 */         if (name.charAt(0) < '\n')
/*  75:    */         {
/*  76: 92 */           String altname = "(0x0" + name.charAt(0) + ")" + name.substring(1);
/*  77: 93 */           name = name.substring(1) + " <" + altname + ">";
/*  78:    */         }
/*  79: 95 */         if (withSizes) {
/*  80: 96 */           size = " [" + doc.getSize() + " / 0x" + Integer.toHexString(doc.getSize()) + "]";
/*  81:    */         }
/*  82: 99 */         System.out.println(newIndent + name + size);
/*  83:    */       }
/*  84:    */     }
/*  85:102 */     if (!hadChildren) {
/*  86:103 */       System.out.println(newIndent + "(no children)");
/*  87:    */     }
/*  88:    */   }
/*  89:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.dev.POIFSLister
 * JD-Core Version:    0.7.0.1
 */