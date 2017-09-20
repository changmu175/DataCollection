/*   1:    */ package org.apache.poi.xdgf.usermodel;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.MasterType;
/*   4:    */ import com.microsoft.schemas.office.visio.x2012.main.MastersDocument;
/*   5:    */ import com.microsoft.schemas.office.visio.x2012.main.MastersDocument.Factory;
/*   6:    */ import com.microsoft.schemas.office.visio.x2012.main.MastersType;
/*   7:    */ import com.microsoft.schemas.office.visio.x2012.main.RelType;
/*   8:    */ import java.io.IOException;
/*   9:    */ import java.util.Collection;
/*  10:    */ import java.util.Collections;
/*  11:    */ import java.util.HashMap;
/*  12:    */ import java.util.Map;
/*  13:    */ import org.apache.poi.POIXMLDocumentPart;
/*  14:    */ import org.apache.poi.POIXMLDocumentPart.RelationPart;
/*  15:    */ import org.apache.poi.POIXMLException;
/*  16:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  17:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  18:    */ import org.apache.poi.util.Internal;
/*  19:    */ import org.apache.poi.xdgf.exceptions.XDGFException;
/*  20:    */ import org.apache.poi.xdgf.xml.XDGFXMLDocumentPart;
/*  21:    */ import org.apache.xmlbeans.XmlException;
/*  22:    */ 
/*  23:    */ public class XDGFMasters
/*  24:    */   extends XDGFXMLDocumentPart
/*  25:    */ {
/*  26:    */   MastersType _mastersObject;
/*  27: 46 */   protected Map<Long, XDGFMaster> _masters = new HashMap();
/*  28:    */   
/*  29:    */   public XDGFMasters(PackagePart part, XDGFDocument document)
/*  30:    */   {
/*  31: 52 */     super(part, document);
/*  32:    */   }
/*  33:    */   
/*  34:    */   @Internal
/*  35:    */   protected MastersType getXmlObject()
/*  36:    */   {
/*  37: 57 */     return this._mastersObject;
/*  38:    */   }
/*  39:    */   
/*  40:    */   protected void onDocumentRead()
/*  41:    */   {
/*  42:    */     try
/*  43:    */     {
/*  44:    */       try
/*  45:    */       {
/*  46: 64 */         this._mastersObject = MastersDocument.Factory.parse(getPackagePart().getInputStream()).getMasters();
/*  47:    */       }
/*  48:    */       catch (XmlException e)
/*  49:    */       {
/*  50: 66 */         throw new POIXMLException(e);
/*  51:    */       }
/*  52:    */       catch (IOException e)
/*  53:    */       {
/*  54: 68 */         throw new POIXMLException(e);
/*  55:    */       }
/*  56: 71 */       masterSettings = new HashMap();
/*  57: 72 */       for (MasterType master : this._mastersObject.getMasterArray()) {
/*  58: 73 */         masterSettings.put(master.getRel().getId(), master);
/*  59:    */       }
/*  60: 77 */       for (POIXMLDocumentPart.RelationPart rp : getRelationParts())
/*  61:    */       {
/*  62: 78 */         POIXMLDocumentPart part = rp.getDocumentPart();
/*  63:    */         
/*  64: 80 */         String relId = rp.getRelationship().getId();
/*  65: 81 */         MasterType settings = (MasterType)masterSettings.get(relId);
/*  66: 83 */         if (settings == null) {
/*  67: 84 */           throw new POIXMLException("Master relationship for " + relId + " not found");
/*  68:    */         }
/*  69: 87 */         if (!(part instanceof XDGFMasterContents)) {
/*  70: 88 */           throw new POIXMLException("Unexpected masters relationship for " + relId + ": " + part);
/*  71:    */         }
/*  72: 91 */         XDGFMasterContents contents = (XDGFMasterContents)part;
/*  73: 92 */         contents.onDocumentRead();
/*  74:    */         
/*  75: 94 */         XDGFMaster master = new XDGFMaster(settings, contents, this._document);
/*  76: 95 */         this._masters.put(Long.valueOf(master.getID()), master);
/*  77:    */       }
/*  78:    */     }
/*  79:    */     catch (POIXMLException e)
/*  80:    */     {
/*  81:    */       Map<String, MasterType> masterSettings;
/*  82: 98 */       throw XDGFException.wrap(this, e);
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public Collection<XDGFMaster> getMastersList()
/*  87:    */   {
/*  88:103 */     return Collections.unmodifiableCollection(this._masters.values());
/*  89:    */   }
/*  90:    */   
/*  91:    */   public XDGFMaster getMasterById(long masterId)
/*  92:    */   {
/*  93:107 */     return (XDGFMaster)this._masters.get(Long.valueOf(masterId));
/*  94:    */   }
/*  95:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFMasters
 * JD-Core Version:    0.7.0.1
 */