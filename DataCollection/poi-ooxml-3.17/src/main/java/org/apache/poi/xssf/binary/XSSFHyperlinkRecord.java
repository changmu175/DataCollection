/*   1:    */ package org.apache.poi.xssf.binary;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*   4:    */ import org.apache.poi.util.Internal;
/*   5:    */ 
/*   6:    */ @Internal
/*   7:    */ public class XSSFHyperlinkRecord
/*   8:    */ {
/*   9:    */   private final CellRangeAddress cellRangeAddress;
/*  10:    */   private final String relId;
/*  11:    */   private String location;
/*  12:    */   private String toolTip;
/*  13:    */   private String display;
/*  14:    */   
/*  15:    */   XSSFHyperlinkRecord(CellRangeAddress cellRangeAddress, String relId, String location, String toolTip, String display)
/*  16:    */   {
/*  17: 44 */     this.cellRangeAddress = cellRangeAddress;
/*  18: 45 */     this.relId = relId;
/*  19: 46 */     this.location = location;
/*  20: 47 */     this.toolTip = toolTip;
/*  21: 48 */     this.display = display;
/*  22:    */   }
/*  23:    */   
/*  24:    */   void setLocation(String location)
/*  25:    */   {
/*  26: 52 */     this.location = location;
/*  27:    */   }
/*  28:    */   
/*  29:    */   void setToolTip(String toolTip)
/*  30:    */   {
/*  31: 56 */     this.toolTip = toolTip;
/*  32:    */   }
/*  33:    */   
/*  34:    */   void setDisplay(String display)
/*  35:    */   {
/*  36: 60 */     this.display = display;
/*  37:    */   }
/*  38:    */   
/*  39:    */   CellRangeAddress getCellRangeAddress()
/*  40:    */   {
/*  41: 64 */     return this.cellRangeAddress;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public String getRelId()
/*  45:    */   {
/*  46: 68 */     return this.relId;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public String getLocation()
/*  50:    */   {
/*  51: 72 */     return this.location;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public String getToolTip()
/*  55:    */   {
/*  56: 76 */     return this.toolTip;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public String getDisplay()
/*  60:    */   {
/*  61: 80 */     return this.display;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public boolean equals(Object o)
/*  65:    */   {
/*  66: 85 */     if (this == o) {
/*  67: 85 */       return true;
/*  68:    */     }
/*  69: 86 */     if ((o == null) || (getClass() != o.getClass())) {
/*  70: 86 */       return false;
/*  71:    */     }
/*  72: 88 */     XSSFHyperlinkRecord that = (XSSFHyperlinkRecord)o;
/*  73: 90 */     if (this.cellRangeAddress != null ? !this.cellRangeAddress.equals(that.cellRangeAddress) : that.cellRangeAddress != null) {
/*  74: 91 */       return false;
/*  75:    */     }
/*  76: 92 */     if (this.relId != null ? !this.relId.equals(that.relId) : that.relId != null) {
/*  77: 92 */       return false;
/*  78:    */     }
/*  79: 93 */     if (this.location != null ? !this.location.equals(that.location) : that.location != null) {
/*  80: 93 */       return false;
/*  81:    */     }
/*  82: 94 */     if (this.toolTip != null ? !this.toolTip.equals(that.toolTip) : that.toolTip != null) {
/*  83: 94 */       return false;
/*  84:    */     }
/*  85: 95 */     return that.display == null ? true : this.display != null ? this.display.equals(that.display) : false;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public int hashCode()
/*  89:    */   {
/*  90:100 */     int result = this.cellRangeAddress != null ? this.cellRangeAddress.hashCode() : 0;
/*  91:101 */     result = 31 * result + (this.relId != null ? this.relId.hashCode() : 0);
/*  92:102 */     result = 31 * result + (this.location != null ? this.location.hashCode() : 0);
/*  93:103 */     result = 31 * result + (this.toolTip != null ? this.toolTip.hashCode() : 0);
/*  94:104 */     result = 31 * result + (this.display != null ? this.display.hashCode() : 0);
/*  95:105 */     return result;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public String toString()
/*  99:    */   {
/* 100:110 */     return "XSSFHyperlinkRecord{cellRangeAddress=" + this.cellRangeAddress + ", relId='" + this.relId + '\'' + ", location='" + this.location + '\'' + ", toolTip='" + this.toolTip + '\'' + ", display='" + this.display + '\'' + '}';
/* 101:    */   }
/* 102:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFHyperlinkRecord
 * JD-Core Version:    0.7.0.1
 */