/*   1:    */ package org.apache.poi.xdgf.usermodel.section;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*   4:    */ import com.microsoft.schemas.office.visio.x2012.main.SectionType;
/*   5:    */ import java.awt.geom.Path2D.Double;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.Map.Entry;
/*   8:    */ import java.util.SortedMap;
/*   9:    */ import java.util.TreeMap;
/*  10:    */ import org.apache.poi.POIXMLException;
/*  11:    */ import org.apache.poi.xdgf.geom.SplineCollector;
/*  12:    */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/*  13:    */ import org.apache.poi.xdgf.usermodel.XDGFShape;
/*  14:    */ import org.apache.poi.xdgf.usermodel.XDGFSheet;
/*  15:    */ import org.apache.poi.xdgf.usermodel.section.geometry.Ellipse;
/*  16:    */ import org.apache.poi.xdgf.usermodel.section.geometry.GeometryRow;
/*  17:    */ import org.apache.poi.xdgf.usermodel.section.geometry.GeometryRowFactory;
/*  18:    */ import org.apache.poi.xdgf.usermodel.section.geometry.InfiniteLine;
/*  19:    */ import org.apache.poi.xdgf.usermodel.section.geometry.SplineKnot;
/*  20:    */ import org.apache.poi.xdgf.usermodel.section.geometry.SplineStart;
/*  21:    */ 
/*  22:    */ public class GeometrySection
/*  23:    */   extends XDGFSection
/*  24:    */ {
/*  25: 43 */   GeometrySection _master = null;
/*  26: 46 */   SortedMap<Long, GeometryRow> _rows = new TreeMap();
/*  27:    */   
/*  28:    */   public GeometrySection(SectionType section, XDGFSheet containingSheet)
/*  29:    */   {
/*  30: 49 */     super(section, containingSheet);
/*  31: 51 */     for (RowType row : section.getRowArray())
/*  32:    */     {
/*  33: 52 */       if (this._rows.containsKey(Long.valueOf(row.getIX()))) {
/*  34: 53 */         throw new POIXMLException("Index element '" + row.getIX() + "' already exists");
/*  35:    */       }
/*  36: 55 */       this._rows.put(Long.valueOf(row.getIX()), GeometryRowFactory.load(row));
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setupMaster(XDGFSection master)
/*  41:    */   {
/*  42: 62 */     this._master = ((GeometrySection)master);
/*  43: 64 */     for (Map.Entry<Long, GeometryRow> entry : this._rows.entrySet())
/*  44:    */     {
/*  45: 65 */       GeometryRow masterRow = (GeometryRow)this._master._rows.get(entry.getKey());
/*  46: 66 */       if (masterRow != null) {
/*  47:    */         try
/*  48:    */         {
/*  49: 68 */           ((GeometryRow)entry.getValue()).setupMaster(masterRow);
/*  50:    */         }
/*  51:    */         catch (ClassCastException e) {}
/*  52:    */       }
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public Boolean getNoShow()
/*  57:    */   {
/*  58: 80 */     Boolean noShow = XDGFCell.maybeGetBoolean(this._cells, "NoShow");
/*  59: 81 */     if (noShow == null)
/*  60:    */     {
/*  61: 82 */       if (this._master != null) {
/*  62: 83 */         return this._master.getNoShow();
/*  63:    */       }
/*  64: 85 */       return Boolean.valueOf(false);
/*  65:    */     }
/*  66: 88 */     return noShow;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public Iterable<GeometryRow> getCombinedRows()
/*  70:    */   {
/*  71: 92 */     return new CombinedIterable(this._rows, this._master == null ? null : this._master._rows);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public Path2D.Double getPath(XDGFShape parent)
/*  75:    */   {
/*  76: 98 */     Iterator<GeometryRow> rows = getCombinedRows().iterator();
/*  77:    */     
/*  78:    */ 
/*  79:101 */     GeometryRow first = (GeometryRow)rows.next();
/*  80:103 */     if ((first instanceof Ellipse)) {
/*  81:104 */       return ((Ellipse)first).getPath();
/*  82:    */     }
/*  83:105 */     if ((first instanceof InfiniteLine)) {
/*  84:106 */       return ((InfiniteLine)first).getPath();
/*  85:    */     }
/*  86:107 */     if ((first instanceof SplineStart)) {
/*  87:108 */       throw new POIXMLException("SplineStart must be preceded by another type");
/*  88:    */     }
/*  89:112 */     Path2D.Double path = new Path2D.Double();
/*  90:    */     
/*  91:    */ 
/*  92:115 */     SplineCollector renderer = null;
/*  93:    */     for (;;)
/*  94:    */     {
/*  95:    */       GeometryRow row;
/*  96:120 */       if (first != null)
/*  97:    */       {
/*  98:121 */         GeometryRow row = first;
/*  99:122 */         first = null;
/* 100:    */       }
/* 101:    */       else
/* 102:    */       {
/* 103:124 */         if (!rows.hasNext()) {
/* 104:    */           break;
/* 105:    */         }
/* 106:126 */         row = (GeometryRow)rows.next();
/* 107:    */       }
/* 108:129 */       if ((row instanceof SplineStart))
/* 109:    */       {
/* 110:130 */         if (renderer != null) {
/* 111:131 */           throw new POIXMLException("SplineStart found multiple times!");
/* 112:    */         }
/* 113:132 */         renderer = new SplineCollector((SplineStart)row);
/* 114:    */       }
/* 115:133 */       else if ((row instanceof SplineKnot))
/* 116:    */       {
/* 117:134 */         if (renderer == null) {
/* 118:135 */           throw new POIXMLException("SplineKnot found without SplineStart!");
/* 119:    */         }
/* 120:136 */         renderer.addKnot((SplineKnot)row);
/* 121:    */       }
/* 122:    */       else
/* 123:    */       {
/* 124:138 */         if (renderer != null)
/* 125:    */         {
/* 126:139 */           renderer.addToPath(path, parent);
/* 127:140 */           renderer = null;
/* 128:    */         }
/* 129:143 */         row.addToPath(path, parent);
/* 130:    */       }
/* 131:    */     }
/* 132:148 */     if (renderer != null) {
/* 133:149 */       renderer.addToPath(path, parent);
/* 134:    */     }
/* 135:151 */     return path;
/* 136:    */   }
/* 137:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.GeometrySection
 * JD-Core Version:    0.7.0.1
 */