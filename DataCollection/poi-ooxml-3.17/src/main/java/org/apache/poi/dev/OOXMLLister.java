/*   1:    */ package org.apache.poi.dev;
/*   2:    */ 
/*   3:    */ import java.io.Closeable;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.PrintStream;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  10:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  11:    */ import org.apache.poi.openxml4j.opc.PackageAccess;
/*  12:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  13:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  14:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  15:    */ import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
/*  16:    */ 
/*  17:    */ public class OOXMLLister
/*  18:    */   implements Closeable
/*  19:    */ {
/*  20:    */   private final OPCPackage container;
/*  21:    */   private final PrintStream disp;
/*  22:    */   
/*  23:    */   public OOXMLLister(OPCPackage container)
/*  24:    */   {
/*  25: 39 */     this(container, System.out);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public OOXMLLister(OPCPackage container, PrintStream disp)
/*  29:    */   {
/*  30: 42 */     this.container = container;
/*  31: 43 */     this.disp = disp;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static long getSize(PackagePart part)
/*  35:    */     throws IOException
/*  36:    */   {
/*  37: 55 */     InputStream in = part.getInputStream();
/*  38:    */     try
/*  39:    */     {
/*  40: 57 */       byte[] b = new byte[8192];
/*  41: 58 */       long size = 0L;
/*  42: 59 */       int read = 0;
/*  43: 61 */       while (read > -1)
/*  44:    */       {
/*  45: 62 */         read = in.read(b);
/*  46: 63 */         if (read > 0) {
/*  47: 64 */           size += read;
/*  48:    */         }
/*  49:    */       }
/*  50: 68 */       return size;
/*  51:    */     }
/*  52:    */     finally
/*  53:    */     {
/*  54: 70 */       in.close();
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void displayParts()
/*  59:    */     throws InvalidFormatException, IOException
/*  60:    */   {
/*  61: 81 */     ArrayList<PackagePart> parts = this.container.getParts();
/*  62: 82 */     for (PackagePart part : parts)
/*  63:    */     {
/*  64: 83 */       this.disp.println(part.getPartName());
/*  65: 84 */       this.disp.println("\t" + part.getContentType());
/*  66: 86 */       if (!part.getPartName().toString().equals("/docProps/core.xml")) {
/*  67: 87 */         this.disp.println("\t" + getSize(part) + " bytes");
/*  68:    */       }
/*  69: 90 */       if (!part.isRelationshipPart())
/*  70:    */       {
/*  71: 91 */         this.disp.println("\t" + part.getRelationships().size() + " relations");
/*  72: 92 */         for (PackageRelationship rel : part.getRelationships()) {
/*  73: 93 */           displayRelation(rel, "\t  ");
/*  74:    */         }
/*  75:    */       }
/*  76:    */     }
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void displayRelations()
/*  80:    */   {
/*  81:104 */     PackageRelationshipCollection rels = this.container.getRelationships();
/*  82:106 */     for (PackageRelationship rel : rels) {
/*  83:107 */       displayRelation(rel, "");
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   private void displayRelation(PackageRelationship rel, String indent)
/*  88:    */   {
/*  89:112 */     this.disp.println(indent + "Relationship:");
/*  90:113 */     this.disp.println(indent + "\tFrom: " + rel.getSourceURI());
/*  91:114 */     this.disp.println(indent + "\tTo:   " + rel.getTargetURI());
/*  92:115 */     this.disp.println(indent + "\tID:   " + rel.getId());
/*  93:116 */     this.disp.println(indent + "\tMode: " + rel.getTargetMode());
/*  94:117 */     this.disp.println(indent + "\tType: " + rel.getRelationshipType());
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void close()
/*  98:    */     throws IOException
/*  99:    */   {
/* 100:122 */     this.container.close();
/* 101:    */   }
/* 102:    */   
/* 103:    */   public static void main(String[] args)
/* 104:    */     throws IOException, InvalidFormatException
/* 105:    */   {
/* 106:126 */     if (args.length == 0)
/* 107:    */     {
/* 108:127 */       System.err.println("Use:");
/* 109:128 */       System.err.println("\tjava OOXMLLister <filename>");
/* 110:129 */       System.exit(1);
/* 111:    */     }
/* 112:132 */     File f = new File(args[0]);
/* 113:133 */     if (!f.exists())
/* 114:    */     {
/* 115:134 */       System.err.println("Error, file not found!");
/* 116:135 */       System.err.println("\t" + f);
/* 117:136 */       System.exit(2);
/* 118:    */     }
/* 119:139 */     OOXMLLister lister = new OOXMLLister(OPCPackage.open(f.toString(), PackageAccess.READ));
/* 120:    */     try
/* 121:    */     {
/* 122:144 */       lister.disp.println(f + "\n");
/* 123:145 */       lister.displayParts();
/* 124:146 */       lister.disp.println();
/* 125:147 */       lister.displayRelations();
/* 126:    */     }
/* 127:    */     finally
/* 128:    */     {
/* 129:149 */       lister.close();
/* 130:    */     }
/* 131:    */   }
/* 132:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.dev.OOXMLLister
 * JD-Core Version:    0.7.0.1
 */