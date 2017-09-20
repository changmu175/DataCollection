/*   1:    */ package org.apache.poi.xssf.model;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Collections;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Map;
/*  11:    */ import org.apache.poi.POIXMLDocumentPart;
/*  12:    */ import org.apache.poi.POIXMLTypeLoader;
/*  13:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  14:    */ import org.apache.xmlbeans.XmlException;
/*  15:    */ import org.apache.xmlbeans.XmlOptions;
/*  16:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst;
/*  17:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSst;
/*  18:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.SstDocument;
/*  19:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.SstDocument.Factory;
/*  20:    */ 
/*  21:    */ public class SharedStringsTable
/*  22:    */   extends POIXMLDocumentPart
/*  23:    */ {
/*  24: 66 */   private final List<CTRst> strings = new ArrayList();
/*  25: 71 */   private final Map<String, Integer> stmap = new HashMap();
/*  26:    */   private int count;
/*  27:    */   private int uniqueCount;
/*  28:    */   private SstDocument _sstDoc;
/*  29: 88 */   private static final XmlOptions options = new XmlOptions();
/*  30:    */   
/*  31:    */   static
/*  32:    */   {
/*  33: 90 */     options.put("SAVE_INNER");
/*  34: 91 */     options.put("SAVE_AGGRESSIVE_NAMESPACES");
/*  35: 92 */     options.put("SAVE_USE_DEFAULT_NAMESPACE");
/*  36: 93 */     options.setSaveImplicitNamespaces(Collections.singletonMap("", "http://schemas.openxmlformats.org/spreadsheetml/2006/main"));
/*  37:    */   }
/*  38:    */   
/*  39:    */   public SharedStringsTable()
/*  40:    */   {
/*  41: 98 */     this._sstDoc = SstDocument.Factory.newInstance();
/*  42: 99 */     this._sstDoc.addNewSst();
/*  43:    */   }
/*  44:    */   
/*  45:    */   public SharedStringsTable(PackagePart part)
/*  46:    */     throws IOException
/*  47:    */   {
/*  48:106 */     super(part);
/*  49:107 */     readFrom(part.getInputStream());
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void readFrom(InputStream is)
/*  53:    */     throws IOException
/*  54:    */   {
/*  55:    */     try
/*  56:    */     {
/*  57:118 */       int cnt = 0;
/*  58:119 */       this._sstDoc = SstDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  59:120 */       CTSst sst = this._sstDoc.getSst();
/*  60:121 */       this.count = ((int)sst.getCount());
/*  61:122 */       this.uniqueCount = ((int)sst.getUniqueCount());
/*  62:124 */       for (CTRst st : sst.getSiArray())
/*  63:    */       {
/*  64:125 */         this.stmap.put(getKey(st), Integer.valueOf(cnt));
/*  65:126 */         this.strings.add(st);
/*  66:127 */         cnt++;
/*  67:    */       }
/*  68:    */     }
/*  69:    */     catch (XmlException e)
/*  70:    */     {
/*  71:130 */       throw new IOException("unable to parse shared strings table", e);
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   private String getKey(CTRst st)
/*  76:    */   {
/*  77:135 */     return st.xmlText(options);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public CTRst getEntryAt(int idx)
/*  81:    */   {
/*  82:145 */     return (CTRst)this.strings.get(idx);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public int getCount()
/*  86:    */   {
/*  87:155 */     return this.count;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public int getUniqueCount()
/*  91:    */   {
/*  92:166 */     return this.uniqueCount;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public int addEntry(CTRst st)
/*  96:    */   {
/*  97:181 */     String s = getKey(st);
/*  98:182 */     this.count += 1;
/*  99:183 */     if (this.stmap.containsKey(s)) {
/* 100:184 */       return ((Integer)this.stmap.get(s)).intValue();
/* 101:    */     }
/* 102:187 */     this.uniqueCount += 1;
/* 103:    */     
/* 104:189 */     CTRst newSt = this._sstDoc.getSst().addNewSi();
/* 105:190 */     newSt.set(st);
/* 106:191 */     int idx = this.strings.size();
/* 107:192 */     this.stmap.put(s, Integer.valueOf(idx));
/* 108:193 */     this.strings.add(newSt);
/* 109:194 */     return idx;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public List<CTRst> getItems()
/* 113:    */   {
/* 114:202 */     return Collections.unmodifiableList(this.strings);
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void writeTo(OutputStream out)
/* 118:    */     throws IOException
/* 119:    */   {
/* 120:212 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 121:    */     
/* 122:    */ 
/* 123:215 */     xmlOptions.setSaveCDataLengthThreshold(1000000);
/* 124:216 */     xmlOptions.setSaveCDataEntityCountThreshold(-1);
/* 125:    */     
/* 126:    */ 
/* 127:219 */     CTSst sst = this._sstDoc.getSst();
/* 128:220 */     sst.setCount(this.count);
/* 129:221 */     sst.setUniqueCount(this.uniqueCount);
/* 130:    */     
/* 131:223 */     this._sstDoc.save(out, xmlOptions);
/* 132:    */   }
/* 133:    */   
/* 134:    */   protected void commit()
/* 135:    */     throws IOException
/* 136:    */   {
/* 137:228 */     PackagePart part = getPackagePart();
/* 138:229 */     OutputStream out = part.getOutputStream();
/* 139:230 */     writeTo(out);
/* 140:231 */     out.close();
/* 141:    */   }
/* 142:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.model.SharedStringsTable
 * JD-Core Version:    0.7.0.1
 */