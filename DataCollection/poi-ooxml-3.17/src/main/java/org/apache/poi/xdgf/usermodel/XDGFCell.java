/*   1:    */ package org.apache.poi.xdgf.usermodel;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.apache.poi.POIXMLException;
/*   6:    */ import org.apache.poi.util.Internal;
/*   7:    */ 
/*   8:    */ public class XDGFCell
/*   9:    */ {
/*  10:    */   CellType _cell;
/*  11:    */   
/*  12:    */   public static Boolean maybeGetBoolean(Map<String, XDGFCell> cells, String name)
/*  13:    */   {
/*  14: 43 */     XDGFCell cell = (XDGFCell)cells.get(name);
/*  15: 44 */     if (cell == null) {
/*  16: 45 */       return null;
/*  17:    */     }
/*  18: 47 */     if (cell.getValue().equals("0")) {
/*  19: 48 */       return Boolean.valueOf(false);
/*  20:    */     }
/*  21: 49 */     if (cell.getValue().equals("1")) {
/*  22: 50 */       return Boolean.valueOf(true);
/*  23:    */     }
/*  24: 52 */     throw new POIXMLException("Invalid boolean value for '" + cell.getName() + "'");
/*  25:    */   }
/*  26:    */   
/*  27:    */   public static Double maybeGetDouble(Map<String, XDGFCell> cells, String name)
/*  28:    */   {
/*  29: 57 */     XDGFCell cell = (XDGFCell)cells.get(name);
/*  30: 58 */     if (cell != null) {
/*  31: 59 */       return parseDoubleValue(cell._cell);
/*  32:    */     }
/*  33: 60 */     return null;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static Integer maybeGetInteger(Map<String, XDGFCell> cells, String name)
/*  37:    */   {
/*  38: 65 */     XDGFCell cell = (XDGFCell)cells.get(name);
/*  39: 66 */     if (cell != null) {
/*  40: 67 */       return parseIntegerValue(cell._cell);
/*  41:    */     }
/*  42: 68 */     return null;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public static String maybeGetString(Map<String, XDGFCell> cells, String name)
/*  46:    */   {
/*  47: 72 */     XDGFCell cell = (XDGFCell)cells.get(name);
/*  48: 73 */     if (cell != null)
/*  49:    */     {
/*  50: 74 */       String v = cell._cell.getV();
/*  51: 75 */       if (v.equals("Themed")) {
/*  52: 76 */         return null;
/*  53:    */       }
/*  54: 77 */       return v;
/*  55:    */     }
/*  56: 79 */     return null;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public static Double parseDoubleValue(CellType cell)
/*  60:    */   {
/*  61:    */     try
/*  62:    */     {
/*  63: 84 */       return Double.valueOf(Double.parseDouble(cell.getV()));
/*  64:    */     }
/*  65:    */     catch (NumberFormatException e)
/*  66:    */     {
/*  67: 86 */       if (cell.getV().equals("Themed")) {
/*  68: 87 */         return null;
/*  69:    */       }
/*  70: 88 */       throw new POIXMLException("Invalid float value for '" + cell.getN() + "': " + e);
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   public static Integer parseIntegerValue(CellType cell)
/*  75:    */   {
/*  76:    */     try
/*  77:    */     {
/*  78: 95 */       return Integer.valueOf(Integer.parseInt(cell.getV()));
/*  79:    */     }
/*  80:    */     catch (NumberFormatException e)
/*  81:    */     {
/*  82: 97 */       if (cell.getV().equals("Themed")) {
/*  83: 98 */         return null;
/*  84:    */       }
/*  85: 99 */       throw new POIXMLException("Invalid integer value for '" + cell.getN() + "': " + e);
/*  86:    */     }
/*  87:    */   }
/*  88:    */   
/*  89:    */   public static Double parseVLength(CellType cell)
/*  90:    */   {
/*  91:    */     try
/*  92:    */     {
/*  93:110 */       return Double.valueOf(Double.parseDouble(cell.getV()));
/*  94:    */     }
/*  95:    */     catch (NumberFormatException e)
/*  96:    */     {
/*  97:112 */       if (cell.getV().equals("Themed")) {
/*  98:113 */         return null;
/*  99:    */       }
/* 100:114 */       throw new POIXMLException("Invalid float value for '" + cell.getN() + "': " + e);
/* 101:    */     }
/* 102:    */   }
/* 103:    */   
/* 104:    */   public XDGFCell(CellType cell)
/* 105:    */   {
/* 106:122 */     this._cell = cell;
/* 107:    */   }
/* 108:    */   
/* 109:    */   @Internal
/* 110:    */   protected CellType getXmlObject()
/* 111:    */   {
/* 112:127 */     return this._cell;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public String getName()
/* 116:    */   {
/* 117:134 */     return this._cell.getN();
/* 118:    */   }
/* 119:    */   
/* 120:    */   public String getValue()
/* 121:    */   {
/* 122:141 */     return this._cell.getV();
/* 123:    */   }
/* 124:    */   
/* 125:    */   public String getFormula()
/* 126:    */   {
/* 127:151 */     return this._cell.getF();
/* 128:    */   }
/* 129:    */   
/* 130:    */   public String getError()
/* 131:    */   {
/* 132:160 */     return this._cell.getE();
/* 133:    */   }
/* 134:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFCell
 * JD-Core Version:    0.7.0.1
 */