/*   1:    */ package org.apache.poi.poifs.macros;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileNotFoundException;
/*   5:    */ import java.io.FileOutputStream;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.OutputStreamWriter;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Map.Entry;
/*  11:    */ import org.apache.poi.util.StringUtil;
/*  12:    */ 
/*  13:    */ public class VBAMacroExtractor
/*  14:    */ {
/*  15:    */   public static void main(String[] args)
/*  16:    */     throws IOException
/*  17:    */   {
/*  18: 38 */     if (args.length == 0)
/*  19:    */     {
/*  20: 39 */       System.err.println("Use:");
/*  21: 40 */       System.err.println("   VBAMacroExtractor <office.doc> [output]");
/*  22: 41 */       System.err.println("");
/*  23: 42 */       System.err.println("If an output directory is given, macros are written there");
/*  24: 43 */       System.err.println("Otherwise they are output to the screen");
/*  25: 44 */       System.exit(1);
/*  26:    */     }
/*  27: 47 */     File input = new File(args[0]);
/*  28: 48 */     File output = null;
/*  29: 49 */     if (args.length > 1) {
/*  30: 50 */       output = new File(args[1]);
/*  31:    */     }
/*  32: 53 */     VBAMacroExtractor extractor = new VBAMacroExtractor();
/*  33: 54 */     extractor.extract(input, output);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void extract(File input, File outputDir, String extension)
/*  37:    */     throws IOException
/*  38:    */   {
/*  39: 71 */     if (!input.exists()) {
/*  40: 71 */       throw new FileNotFoundException(input.toString());
/*  41:    */     }
/*  42: 72 */     System.err.print("Extracting VBA Macros from " + input + " to ");
/*  43: 73 */     if (outputDir != null)
/*  44:    */     {
/*  45: 74 */       if ((!outputDir.exists()) && (!outputDir.mkdirs())) {
/*  46: 75 */         throw new IOException("Output directory " + outputDir + " could not be created");
/*  47:    */       }
/*  48: 77 */       System.err.println(outputDir);
/*  49:    */     }
/*  50:    */     else
/*  51:    */     {
/*  52: 79 */       System.err.println("STDOUT");
/*  53:    */     }
/*  54: 82 */     VBAMacroReader reader = new VBAMacroReader(input);
/*  55: 83 */     Map<String, String> macros = reader.readMacros();
/*  56: 84 */     reader.close();
/*  57:    */     
/*  58: 86 */     String divider = "---------------------------------------";
/*  59: 87 */     for (Entry<String, String> entry : macros.entrySet())
/*  60:    */     {
/*  61: 88 */       String moduleName = (String)entry.getKey();
/*  62: 89 */       String moduleCode = (String)entry.getValue();
/*  63: 90 */       if (outputDir == null)
/*  64:    */       {
/*  65: 91 */         System.out.println("---------------------------------------");
/*  66: 92 */         System.out.println(moduleName);
/*  67: 93 */         System.out.println("");
/*  68: 94 */         System.out.println(moduleCode);
/*  69:    */       }
/*  70:    */       else
/*  71:    */       {
/*  72: 96 */         File out = new File(outputDir, moduleName + extension);
/*  73: 97 */         FileOutputStream fout = new FileOutputStream(out);
/*  74: 98 */         OutputStreamWriter fwriter = new OutputStreamWriter(fout, StringUtil.UTF8);
/*  75: 99 */         fwriter.write(moduleCode);
/*  76:100 */         fwriter.close();
/*  77:101 */         fout.close();
/*  78:102 */         System.out.println("Extracted " + out);
/*  79:    */       }
/*  80:    */     }
/*  81:105 */     if (outputDir == null) {
/*  82:106 */       System.out.println("---------------------------------------");
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void extract(File input, File outputDir)
/*  87:    */     throws IOException
/*  88:    */   {
/*  89:123 */     extract(input, outputDir, ".vba");
/*  90:    */   }
/*  91:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.poifs.macros.VBAMacroExtractor

 * JD-Core Version:    0.7.0.1

 */