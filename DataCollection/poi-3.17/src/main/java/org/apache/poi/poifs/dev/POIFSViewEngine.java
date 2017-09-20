/*   1:    */ package org.apache.poi.poifs.dev;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.LineNumberReader;
/*   5:    */ import java.io.StringReader;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.List;
/*   9:    */ 
/*  10:    */ public class POIFSViewEngine
/*  11:    */ {
/*  12: 37 */   private static final String _EOL = System.getProperty("line.separator");
/*  13:    */   
/*  14:    */   public static List<String> inspectViewable(Object viewable, boolean drilldown, int indentLevel, String indentString)
/*  15:    */   {
/*  16: 58 */     List<String> objects = new ArrayList();
/*  17: 60 */     if ((viewable instanceof POIFSViewable))
/*  18:    */     {
/*  19: 62 */       POIFSViewable inspected = (POIFSViewable)viewable;
/*  20:    */       
/*  21: 64 */       objects.add(indent(indentLevel, indentString, inspected.getShortDescription()));
/*  22: 66 */       if (drilldown) {
/*  23: 68 */         if (inspected.preferArray())
/*  24:    */         {
/*  25: 70 */           Object[] data = inspected.getViewableArray();
/*  26: 72 */           for (int j = 0; j < data.length; j++) {
/*  27: 74 */             objects.addAll(inspectViewable(data[j], drilldown, indentLevel + 1, indentString));
/*  28:    */           }
/*  29:    */         }
/*  30:    */         else
/*  31:    */         {
/*  32: 81 */           Iterator<Object> iter = inspected.getViewableIterator();
/*  33: 83 */           while (iter.hasNext()) {
/*  34: 85 */             objects.addAll(inspectViewable(iter.next(), drilldown, indentLevel + 1, indentString));
/*  35:    */           }
/*  36:    */         }
/*  37:    */       }
/*  38:    */     }
/*  39:    */     else
/*  40:    */     {
/*  41: 95 */       objects.add(indent(indentLevel, indentString, viewable.toString()));
/*  42:    */     }
/*  43: 98 */     return objects;
/*  44:    */   }
/*  45:    */   
/*  46:    */   private static String indent(int indentLevel, String indentString, String data)
/*  47:    */   {
/*  48:104 */     StringBuffer finalBuffer = new StringBuffer();
/*  49:105 */     StringBuffer indentPrefix = new StringBuffer();
/*  50:107 */     for (int j = 0; j < indentLevel; j++) {
/*  51:109 */       indentPrefix.append(indentString);
/*  52:    */     }
/*  53:111 */     LineNumberReader reader = new LineNumberReader(new StringReader(data));
/*  54:    */     try
/*  55:    */     {
/*  56:116 */       String line = reader.readLine();
/*  57:118 */       while (line != null)
/*  58:    */       {
/*  59:120 */         finalBuffer.append(indentPrefix).append(line).append(_EOL);
/*  60:121 */         line = reader.readLine();
/*  61:    */       }
/*  62:    */     }
/*  63:    */     catch (IOException e)
/*  64:    */     {
/*  65:126 */       finalBuffer.append(indentPrefix).append(e.getMessage()).append(_EOL);
/*  66:    */     }
/*  67:129 */     return finalBuffer.toString();
/*  68:    */   }
/*  69:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.dev.POIFSViewEngine
 * JD-Core Version:    0.7.0.1
 */