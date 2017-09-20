/*   1:    */ package org.apache.poi.sl.draw.geom;
/*   2:    */ 
/*   3:    */ import java.io.InputStream;
/*   4:    */ import java.util.LinkedHashMap;
/*   5:    */ import javax.xml.bind.JAXBContext;
/*   6:    */ import javax.xml.bind.JAXBElement;
/*   7:    */ import javax.xml.bind.JAXBException;
/*   8:    */ import javax.xml.bind.Unmarshaller;
/*   9:    */ import javax.xml.namespace.QName;
/*  10:    */ import javax.xml.stream.EventFilter;
/*  11:    */ import javax.xml.stream.XMLEventReader;
/*  12:    */ import javax.xml.stream.XMLInputFactory;
/*  13:    */ import javax.xml.stream.XMLStreamException;
/*  14:    */ import javax.xml.stream.XMLStreamReader;
/*  15:    */ import javax.xml.stream.events.StartElement;
/*  16:    */ import javax.xml.stream.events.XMLEvent;
/*  17:    */ import org.apache.poi.sl.draw.binding.CTCustomGeometry2D;
/*  18:    */ import org.apache.poi.util.POILogFactory;
/*  19:    */ import org.apache.poi.util.POILogger;
/*  20:    */ import org.apache.poi.util.StaxHelper;
/*  21:    */ 
/*  22:    */ public class PresetGeometries
/*  23:    */   extends LinkedHashMap<String, CustomGeometry>
/*  24:    */ {
/*  25: 46 */   private static final POILogger LOG = POILogFactory.getLogger(PresetGeometries.class);
/*  26:    */   protected static final String BINDING_PACKAGE = "org.apache.poi.sl.draw.binding";
/*  27:    */   protected static PresetGeometries _inst;
/*  28:    */   
/*  29:    */   public void init(InputStream is)
/*  30:    */     throws XMLStreamException, JAXBException
/*  31:    */   {
/*  32: 56 */     EventFilter startElementFilter = new EventFilter()
/*  33:    */     {
/*  34:    */       public boolean accept(XMLEvent event)
/*  35:    */       {
/*  36: 59 */         return event.isStartElement();
/*  37:    */       }
/*  38: 62 */     };
/*  39: 63 */     XMLInputFactory staxFactory = StaxHelper.newXMLInputFactory();
/*  40: 64 */     XMLEventReader staxReader = staxFactory.createXMLEventReader(is);
/*  41: 65 */     XMLEventReader staxFiltRd = staxFactory.createFilteredReader(staxReader, startElementFilter);
/*  42:    */     
/*  43: 67 */     staxFiltRd.nextEvent();
/*  44:    */     
/*  45: 69 */     JAXBContext jaxbContext = JAXBContext.newInstance("org.apache.poi.sl.draw.binding");
/*  46: 70 */     Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
/*  47:    */     
/*  48: 72 */     long cntElem = 0L;
/*  49: 73 */     while (staxFiltRd.peek() != null)
/*  50:    */     {
/*  51: 74 */       StartElement evRoot = (StartElement)staxFiltRd.peek();
/*  52: 75 */       String name = evRoot.getName().getLocalPart();
/*  53: 76 */       JAXBElement<CTCustomGeometry2D> el = unmarshaller.unmarshal(staxReader, CTCustomGeometry2D.class);
/*  54: 77 */       CTCustomGeometry2D cus = (CTCustomGeometry2D)el.getValue();
/*  55: 78 */       cntElem += 1L;
/*  56: 80 */       if (containsKey(name)) {
/*  57: 81 */         LOG.log(5, new Object[] { "Duplicate definition of " + name });
/*  58:    */       }
/*  59: 83 */       put(name, new CustomGeometry(cus));
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   public static CustomGeometry convertCustomGeometry(XMLStreamReader staxReader)
/*  64:    */   {
/*  65:    */     try
/*  66:    */     {
/*  67: 92 */       JAXBContext jaxbContext = JAXBContext.newInstance("org.apache.poi.sl.draw.binding");
/*  68: 93 */       Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
/*  69: 94 */       JAXBElement<CTCustomGeometry2D> el = unmarshaller.unmarshal(staxReader, CTCustomGeometry2D.class);
/*  70: 95 */       return new CustomGeometry((CTCustomGeometry2D)el.getValue());
/*  71:    */     }
/*  72:    */     catch (JAXBException e)
/*  73:    */     {
/*  74: 97 */       LOG.log(7, new Object[] { "Unable to parse single custom geometry", e });
/*  75:    */     }
/*  76: 98 */     return null;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public static synchronized PresetGeometries getInstance()
/*  80:    */   {
/*  81:103 */     if (_inst == null)
/*  82:    */     {
/*  83:106 */       PresetGeometries lInst = new PresetGeometries();
/*  84:    */       try
/*  85:    */       {
/*  86:108 */         InputStream is = PresetGeometries.class.getResourceAsStream("presetShapeDefinitions.xml");
/*  87:    */         try
/*  88:    */         {
/*  89:111 */           lInst.init(is);
/*  90:    */         }
/*  91:    */         finally
/*  92:    */         {
/*  93:113 */           is.close();
/*  94:    */         }
/*  95:    */       }
/*  96:    */       catch (Exception e)
/*  97:    */       {
/*  98:116 */         throw new RuntimeException(e);
/*  99:    */       }
/* 100:118 */       _inst = lInst;
/* 101:    */     }
/* 102:121 */     return _inst;
/* 103:    */   }
/* 104:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.geom.PresetGeometries
 * JD-Core Version:    0.7.0.1
 */