/*   1:    */ package org.apache.poi.xdgf.usermodel;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.ConnectType;
/*   4:    */ import com.microsoft.schemas.office.visio.x2012.main.ConnectsType;
/*   5:    */ import com.microsoft.schemas.office.visio.x2012.main.PageContentsType;
/*   6:    */ import com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType;
/*   7:    */ import com.microsoft.schemas.office.visio.x2012.main.ShapesType;
/*   8:    */ import java.awt.Graphics2D;
/*   9:    */ import java.awt.geom.AffineTransform;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import java.util.Collection;
/*  12:    */ import java.util.Collections;
/*  13:    */ import java.util.HashMap;
/*  14:    */ import java.util.List;
/*  15:    */ import java.util.Map;
/*  16:    */ import org.apache.poi.POIXMLException;
/*  17:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  18:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  19:    */ import org.apache.poi.util.Internal;
/*  20:    */ import org.apache.poi.xdgf.exceptions.XDGFException;
/*  21:    */ import org.apache.poi.xdgf.usermodel.shape.ShapeRenderer;
/*  22:    */ import org.apache.poi.xdgf.usermodel.shape.ShapeVisitor;
/*  23:    */ import org.apache.poi.xdgf.usermodel.shape.exceptions.StopVisiting;
/*  24:    */ import org.apache.poi.xdgf.xml.XDGFXMLDocumentPart;
/*  25:    */ 
/*  26:    */ public class XDGFBaseContents
/*  27:    */   extends XDGFXMLDocumentPart
/*  28:    */ {
/*  29:    */   protected PageContentsType _pageContents;
/*  30: 52 */   protected List<XDGFShape> _toplevelShapes = new ArrayList();
/*  31: 53 */   protected Map<Long, XDGFShape> _shapes = new HashMap();
/*  32: 54 */   protected List<XDGFConnection> _connections = new ArrayList();
/*  33:    */   
/*  34:    */   public XDGFBaseContents(PackagePart part, XDGFDocument document)
/*  35:    */   {
/*  36: 60 */     super(part, document);
/*  37:    */   }
/*  38:    */   
/*  39:    */   @Internal
/*  40:    */   public PageContentsType getXmlObject()
/*  41:    */   {
/*  42: 65 */     return this._pageContents;
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected void onDocumentRead()
/*  46:    */   {
/*  47: 72 */     if (this._pageContents.isSetShapes()) {
/*  48: 73 */       for (ShapeSheetType shapeSheet : this._pageContents.getShapes().getShapeArray())
/*  49:    */       {
/*  50: 74 */         XDGFShape shape = new XDGFShape(shapeSheet, this, this._document);
/*  51: 75 */         this._toplevelShapes.add(shape);
/*  52: 76 */         addToShapeIndex(shape);
/*  53:    */       }
/*  54:    */     }
/*  55: 80 */     if (this._pageContents.isSetConnects()) {
/*  56: 81 */       for (ConnectType connect : this._pageContents.getConnects().getConnectArray())
/*  57:    */       {
/*  58: 83 */         XDGFShape from = (XDGFShape)this._shapes.get(Long.valueOf(connect.getFromSheet()));
/*  59: 84 */         XDGFShape to = (XDGFShape)this._shapes.get(Long.valueOf(connect.getToSheet()));
/*  60: 86 */         if (from == null) {
/*  61: 87 */           throw new POIXMLException(this + "; Connect; Invalid from id: " + connect.getFromSheet());
/*  62:    */         }
/*  63: 89 */         if (to == null) {
/*  64: 90 */           throw new POIXMLException(this + "; Connect; Invalid to id: " + connect.getToSheet());
/*  65:    */         }
/*  66: 92 */         this._connections.add(new XDGFConnection(connect, from, to));
/*  67:    */       }
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected void addToShapeIndex(XDGFShape shape)
/*  72:    */   {
/*  73: 98 */     this._shapes.put(Long.valueOf(shape.getID()), shape);
/*  74:    */     
/*  75:100 */     List<XDGFShape> shapes = shape.getShapes();
/*  76:101 */     if (shapes == null) {
/*  77:102 */       return;
/*  78:    */     }
/*  79:104 */     for (XDGFShape subshape : shapes) {
/*  80:105 */       addToShapeIndex(subshape);
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void draw(Graphics2D graphics)
/*  85:    */   {
/*  86:118 */     visitShapes(new ShapeRenderer(graphics));
/*  87:    */   }
/*  88:    */   
/*  89:    */   public XDGFShape getShapeById(long id)
/*  90:    */   {
/*  91:123 */     return (XDGFShape)this._shapes.get(Long.valueOf(id));
/*  92:    */   }
/*  93:    */   
/*  94:    */   public Map<Long, XDGFShape> getShapesMap()
/*  95:    */   {
/*  96:127 */     return Collections.unmodifiableMap(this._shapes);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public Collection<XDGFShape> getShapes()
/* 100:    */   {
/* 101:131 */     return this._shapes.values();
/* 102:    */   }
/* 103:    */   
/* 104:    */   public List<XDGFShape> getTopLevelShapes()
/* 105:    */   {
/* 106:135 */     return Collections.unmodifiableList(this._toplevelShapes);
/* 107:    */   }
/* 108:    */   
/* 109:    */   public List<XDGFConnection> getConnections()
/* 110:    */   {
/* 111:139 */     return Collections.unmodifiableList(this._connections);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public String toString()
/* 115:    */   {
/* 116:144 */     return getPackagePart().getPartName().toString();
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void visitShapes(ShapeVisitor visitor)
/* 120:    */   {
/* 121:    */     try
/* 122:    */     {
/* 123:154 */       for (XDGFShape shape : this._toplevelShapes) {
/* 124:155 */         shape.visitShapes(visitor, new AffineTransform(), 0);
/* 125:    */       }
/* 126:    */     }
/* 127:    */     catch (StopVisiting e) {}catch (POIXMLException e)
/* 128:    */     {
/* 129:160 */       throw XDGFException.wrap(this, e);
/* 130:    */     }
/* 131:    */   }
/* 132:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFBaseContents
 * JD-Core Version:    0.7.0.1
 */