/*   1:    */ package org.apache.poi.xssf.binary;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.Serializable;
/*   6:    */ import java.net.URI;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.BitSet;
/*   9:    */ import java.util.Comparator;
/*  10:    */ import java.util.HashMap;
/*  11:    */ import java.util.List;
/*  12:    */ import java.util.Map;
/*  13:    */ import java.util.TreeMap;
/*  14:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  15:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  16:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  17:    */ import org.apache.poi.ss.util.CellAddress;
/*  18:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*  19:    */ import org.apache.poi.ss.util.CellRangeUtil;
/*  20:    */ import org.apache.poi.util.Internal;
/*  21:    */ import org.apache.poi.xssf.usermodel.XSSFRelation;
/*  22:    */ 
/*  23:    */ @Internal
/*  24:    */ public class XSSFBHyperlinksTable
/*  25:    */ {
/*  26: 46 */   private static final BitSet RECORDS = new BitSet();
/*  27:    */   
/*  28:    */   static
/*  29:    */   {
/*  30: 50 */     RECORDS.set(XSSFBRecordType.BrtHLink.getId());
/*  31:    */   }
/*  32:    */   
/*  33: 54 */   private final List<XSSFHyperlinkRecord> hyperlinkRecords = new ArrayList();
/*  34: 57 */   private Map<String, String> relIdToHyperlink = new HashMap();
/*  35:    */   
/*  36:    */   public XSSFBHyperlinksTable(PackagePart sheetPart)
/*  37:    */     throws IOException
/*  38:    */   {
/*  39: 61 */     loadUrlsFromSheetRels(sheetPart);
/*  40:    */     
/*  41: 63 */     HyperlinkSheetScraper scraper = new HyperlinkSheetScraper(sheetPart.getInputStream());
/*  42: 64 */     scraper.parse();
/*  43:    */   }
/*  44:    */   
/*  45:    */   public Map<CellAddress, List<XSSFHyperlinkRecord>> getHyperLinks()
/*  46:    */   {
/*  47: 72 */     Map<CellAddress, List<XSSFHyperlinkRecord>> hyperlinkMap = new TreeMap(new TopLeftCellAddressComparator(null));
/*  48: 74 */     for (XSSFHyperlinkRecord hyperlinkRecord : this.hyperlinkRecords)
/*  49:    */     {
/*  50: 75 */       CellAddress cellAddress = new CellAddress(hyperlinkRecord.getCellRangeAddress().getFirstRow(), hyperlinkRecord.getCellRangeAddress().getFirstColumn());
/*  51:    */       
/*  52: 77 */       List<XSSFHyperlinkRecord> list = (List)hyperlinkMap.get(cellAddress);
/*  53: 78 */       if (list == null) {
/*  54: 79 */         list = new ArrayList();
/*  55:    */       }
/*  56: 81 */       list.add(hyperlinkRecord);
/*  57: 82 */       hyperlinkMap.put(cellAddress, list);
/*  58:    */     }
/*  59: 84 */     return hyperlinkMap;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public List<XSSFHyperlinkRecord> findHyperlinkRecord(CellAddress cellAddress)
/*  63:    */   {
/*  64: 94 */     List<XSSFHyperlinkRecord> overlapping = null;
/*  65: 95 */     CellRangeAddress targetCellRangeAddress = new CellRangeAddress(cellAddress.getRow(), cellAddress.getRow(), cellAddress.getColumn(), cellAddress.getColumn());
/*  66: 99 */     for (XSSFHyperlinkRecord record : this.hyperlinkRecords) {
/*  67:100 */       if (CellRangeUtil.intersect(targetCellRangeAddress, record.getCellRangeAddress()) != 1)
/*  68:    */       {
/*  69:101 */         if (overlapping == null) {
/*  70:102 */           overlapping = new ArrayList();
/*  71:    */         }
/*  72:104 */         overlapping.add(record);
/*  73:    */       }
/*  74:    */     }
/*  75:107 */     return overlapping;
/*  76:    */   }
/*  77:    */   
/*  78:    */   private void loadUrlsFromSheetRels(PackagePart sheetPart)
/*  79:    */   {
/*  80:    */     try
/*  81:    */     {
/*  82:112 */       for (PackageRelationship rel : sheetPart.getRelationshipsByType(XSSFRelation.SHEET_HYPERLINKS.getRelation())) {
/*  83:113 */         this.relIdToHyperlink.put(rel.getId(), rel.getTargetURI().toString());
/*  84:    */       }
/*  85:    */     }
/*  86:    */     catch (InvalidFormatException e) {}
/*  87:    */   }
/*  88:    */   
/*  89:    */   private class HyperlinkSheetScraper
/*  90:    */     extends XSSFBParser
/*  91:    */   {
/*  92:122 */     private XSSFBCellRange hyperlinkCellRange = new XSSFBCellRange();
/*  93:123 */     private final StringBuilder xlWideStringBuffer = new StringBuilder();
/*  94:    */     
/*  95:    */     HyperlinkSheetScraper(InputStream is)
/*  96:    */     {
/*  97:126 */       super(XSSFBHyperlinksTable.RECORDS);
/*  98:    */     }
/*  99:    */     
/* 100:    */     public void handleRecord(int recordType, byte[] data)
/* 101:    */       throws XSSFBParseException
/* 102:    */     {
/* 103:131 */       if (recordType != XSSFBRecordType.BrtHLink.getId()) {
/* 104:132 */         return;
/* 105:    */       }
/* 106:134 */       int offset = 0;
/* 107:135 */       String relId = "";
/* 108:136 */       String location = "";
/* 109:137 */       String toolTip = "";
/* 110:138 */       String display = "";
/* 111:    */       
/* 112:140 */       this.hyperlinkCellRange = XSSFBCellRange.parse(data, offset, this.hyperlinkCellRange);
/* 113:141 */       offset += 16;
/* 114:142 */       this.xlWideStringBuffer.setLength(0);
/* 115:143 */       offset += XSSFBUtils.readXLNullableWideString(data, offset, this.xlWideStringBuffer);
/* 116:144 */       relId = this.xlWideStringBuffer.toString();
/* 117:145 */       this.xlWideStringBuffer.setLength(0);
/* 118:146 */       offset += XSSFBUtils.readXLWideString(data, offset, this.xlWideStringBuffer);
/* 119:147 */       location = this.xlWideStringBuffer.toString();
/* 120:148 */       this.xlWideStringBuffer.setLength(0);
/* 121:149 */       offset += XSSFBUtils.readXLWideString(data, offset, this.xlWideStringBuffer);
/* 122:150 */       toolTip = this.xlWideStringBuffer.toString();
/* 123:151 */       this.xlWideStringBuffer.setLength(0);
/* 124:152 */       offset += XSSFBUtils.readXLWideString(data, offset, this.xlWideStringBuffer);
/* 125:153 */       display = this.xlWideStringBuffer.toString();
/* 126:154 */       CellRangeAddress cellRangeAddress = new CellRangeAddress(this.hyperlinkCellRange.firstRow, this.hyperlinkCellRange.lastRow, this.hyperlinkCellRange.firstCol, this.hyperlinkCellRange.lastCol);
/* 127:    */       
/* 128:156 */       String url = (String)XSSFBHyperlinksTable.this.relIdToHyperlink.get(relId);
/* 129:157 */       if ((location == null) || (location.length() == 0)) {
/* 130:158 */         location = url;
/* 131:    */       }
/* 132:161 */       XSSFBHyperlinksTable.this.hyperlinkRecords.add(new XSSFHyperlinkRecord(cellRangeAddress, relId, location, toolTip, display));
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   private static class TopLeftCellAddressComparator
/* 137:    */     implements Comparator<CellAddress>, Serializable
/* 138:    */   {
/* 139:    */     private static final long serialVersionUID = 1L;
/* 140:    */     
/* 141:    */     public int compare(CellAddress o1, CellAddress o2)
/* 142:    */     {
/* 143:172 */       if (o1.getRow() < o2.getRow()) {
/* 144:173 */         return -1;
/* 145:    */       }
/* 146:174 */       if (o1.getRow() > o2.getRow()) {
/* 147:175 */         return 1;
/* 148:    */       }
/* 149:177 */       if (o1.getColumn() < o2.getColumn()) {
/* 150:178 */         return -1;
/* 151:    */       }
/* 152:179 */       if (o1.getColumn() > o2.getColumn()) {
/* 153:180 */         return 1;
/* 154:    */       }
/* 155:182 */       return 0;
/* 156:    */     }
/* 157:    */   }
/* 158:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFBHyperlinksTable
 * JD-Core Version:    0.7.0.1
 */