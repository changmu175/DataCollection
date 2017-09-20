/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.apache.poi.POIXMLDocumentPart;
/*   6:    */ import org.apache.poi.POIXMLRelation;
/*   7:    */ import org.apache.poi.sl.usermodel.PictureData.PictureType;
/*   8:    */ 
/*   9:    */ public class XSLFRelation
/*  10:    */   extends POIXMLRelation
/*  11:    */ {
/*  12: 33 */   private static final Map<String, XSLFRelation> _table = new HashMap();
/*  13: 35 */   public static final XSLFRelation MAIN = new XSLFRelation("application/vnd.openxmlformats-officedocument.presentationml.presentation.main+xml", null, null, null);
/*  14: 40 */   public static final XSLFRelation MACRO = new XSLFRelation("application/vnd.ms-powerpoint.slideshow.macroEnabled.main+xml", null, null, null);
/*  15: 45 */   public static final XSLFRelation MACRO_TEMPLATE = new XSLFRelation("application/vnd.ms-powerpoint.template.macroEnabled.main+xml", null, null, null);
/*  16: 50 */   public static final XSLFRelation PRESENTATIONML = new XSLFRelation("application/vnd.openxmlformats-officedocument.presentationml.slideshow.main+xml", null, null, null);
/*  17: 55 */   public static final XSLFRelation PRESENTATIONML_TEMPLATE = new XSLFRelation("application/vnd.openxmlformats-officedocument.presentationml.template.main+xml", null, null, null);
/*  18: 60 */   public static final XSLFRelation PRESENTATION_MACRO = new XSLFRelation("application/vnd.ms-powerpoint.presentation.macroEnabled.main+xml", null, null, null);
/*  19: 65 */   public static final XSLFRelation THEME_MANAGER = new XSLFRelation("application/vnd.openxmlformats-officedocument.themeManager+xml", null, null, null);
/*  20: 70 */   public static final XSLFRelation NOTES = new XSLFRelation("application/vnd.openxmlformats-officedocument.presentationml.notesSlide+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/notesSlide", "/ppt/notesSlides/notesSlide#.xml", XSLFNotes.class);
/*  21: 77 */   public static final XSLFRelation SLIDE = new XSLFRelation("application/vnd.openxmlformats-officedocument.presentationml.slide+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/slide", "/ppt/slides/slide#.xml", XSLFSlide.class);
/*  22: 84 */   public static final XSLFRelation SLIDE_LAYOUT = new XSLFRelation("application/vnd.openxmlformats-officedocument.presentationml.slideLayout+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/slideLayout", "/ppt/slideLayouts/slideLayout#.xml", XSLFSlideLayout.class);
/*  23: 91 */   public static final XSLFRelation SLIDE_MASTER = new XSLFRelation("application/vnd.openxmlformats-officedocument.presentationml.slideMaster+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/slideMaster", "/ppt/slideMasters/slideMaster#.xml", XSLFSlideMaster.class);
/*  24: 98 */   public static final XSLFRelation NOTES_MASTER = new XSLFRelation("application/vnd.openxmlformats-officedocument.presentationml.notesMaster+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/notesMaster", "/ppt/notesMasters/notesMaster#.xml", XSLFNotesMaster.class);
/*  25:105 */   public static final XSLFRelation COMMENTS = new XSLFRelation("application/vnd.openxmlformats-officedocument.presentationml.comments+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/comments", "/ppt/comments/comment#.xml", XSLFComments.class);
/*  26:112 */   public static final XSLFRelation COMMENT_AUTHORS = new XSLFRelation("application/vnd.openxmlformats-officedocument.presentationml.commentAuthors+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/commentAuthors", "/ppt/commentAuthors.xml", XSLFCommentAuthors.class);
/*  27:119 */   public static final XSLFRelation HYPERLINK = new XSLFRelation(null, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink", null, null);
/*  28:126 */   public static final XSLFRelation THEME = new XSLFRelation("application/vnd.openxmlformats-officedocument.theme+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme", "/ppt/theme/theme#.xml", XSLFTheme.class);
/*  29:133 */   public static final XSLFRelation VML_DRAWING = new XSLFRelation("application/vnd.openxmlformats-officedocument.vmlDrawing", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/vmlDrawing", "/ppt/drawings/vmlDrawing#.vml", null);
/*  30:140 */   public static final XSLFRelation CHART = new XSLFRelation("application/vnd.openxmlformats-officedocument.drawingml.chart+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/chart", "/ppt/charts/chart#.xml", XSLFChart.class);
/*  31:147 */   public static final XSLFRelation IMAGE_EMF = new XSLFRelation(PictureData.PictureType.EMF.contentType, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/ppt/media/image#.emf", XSLFPictureData.class);
/*  32:153 */   public static final XSLFRelation IMAGE_WMF = new XSLFRelation(PictureData.PictureType.WMF.contentType, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/ppt/media/image#.wmf", XSLFPictureData.class);
/*  33:159 */   public static final XSLFRelation IMAGE_PICT = new XSLFRelation(PictureData.PictureType.PICT.contentType, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/ppt/media/image#.pict", XSLFPictureData.class);
/*  34:165 */   public static final XSLFRelation IMAGE_JPEG = new XSLFRelation(PictureData.PictureType.JPEG.contentType, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/ppt/media/image#.jpeg", XSLFPictureData.class);
/*  35:171 */   public static final XSLFRelation IMAGE_PNG = new XSLFRelation(PictureData.PictureType.PNG.contentType, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/ppt/media/image#.png", XSLFPictureData.class);
/*  36:177 */   public static final XSLFRelation IMAGE_DIB = new XSLFRelation(PictureData.PictureType.DIB.contentType, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/ppt/media/image#.dib", XSLFPictureData.class);
/*  37:183 */   public static final XSLFRelation IMAGE_GIF = new XSLFRelation(PictureData.PictureType.GIF.contentType, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/ppt/media/image#.gif", XSLFPictureData.class);
/*  38:189 */   public static final XSLFRelation IMAGE_TIFF = new XSLFRelation(PictureData.PictureType.TIFF.contentType, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/ppt/media/image#.tiff", XSLFPictureData.class);
/*  39:195 */   public static final XSLFRelation IMAGE_EPS = new XSLFRelation(PictureData.PictureType.EPS.contentType, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/ppt/media/image#.eps", XSLFPictureData.class);
/*  40:201 */   public static final XSLFRelation IMAGE_BMP = new XSLFRelation(PictureData.PictureType.BMP.contentType, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/ppt/media/image#.bmp", XSLFPictureData.class);
/*  41:207 */   public static final XSLFRelation IMAGE_WPG = new XSLFRelation(PictureData.PictureType.WPG.contentType, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/ppt/media/image#.wpg", XSLFPictureData.class);
/*  42:213 */   public static final XSLFRelation IMAGE_WDP = new XSLFRelation(PictureData.PictureType.WDP.contentType, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", "/ppt/media/image#.wdp", XSLFPictureData.class);
/*  43:220 */   public static final XSLFRelation IMAGES = new XSLFRelation(null, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", null, XSLFPictureData.class);
/*  44:227 */   public static final XSLFRelation TABLE_STYLES = new XSLFRelation("application/vnd.openxmlformats-officedocument.presentationml.tableStyles+xml", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/tableStyles", "/ppt/tableStyles.xml", XSLFTableStyles.class);
/*  45:    */   
/*  46:    */   private XSLFRelation(String type, String rel, String defaultName, Class<? extends POIXMLDocumentPart> cls)
/*  47:    */   {
/*  48:235 */     super(type, rel, defaultName, cls);
/*  49:236 */     _table.put(rel, this);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static XSLFRelation getInstance(String rel)
/*  53:    */   {
/*  54:247 */     return (XSLFRelation)_table.get(rel);
/*  55:    */   }
/*  56:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFRelation
 * JD-Core Version:    0.7.0.1
 */