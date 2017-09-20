package org.apache.poi.xwpf.usermodel;

import java.util.List;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;

public abstract interface IBody
{
  public abstract POIXMLDocumentPart getPart();
  
  public abstract BodyType getPartType();
  
  public abstract List<IBodyElement> getBodyElements();
  
  public abstract List<XWPFParagraph> getParagraphs();
  
  public abstract List<XWPFTable> getTables();
  
  public abstract XWPFParagraph getParagraph(CTP paramCTP);
  
  public abstract XWPFTable getTable(CTTbl paramCTTbl);
  
  public abstract XWPFParagraph getParagraphArray(int paramInt);
  
  public abstract XWPFTable getTableArray(int paramInt);
  
  public abstract XWPFParagraph insertNewParagraph(XmlCursor paramXmlCursor);
  
  public abstract XWPFTable insertNewTbl(XmlCursor paramXmlCursor);
  
  public abstract void insertTable(int paramInt, XWPFTable paramXWPFTable);
  
  public abstract XWPFTableCell getTableCell(CTTc paramCTTc);
  
  public abstract XWPFDocument getXWPFDocument();
}


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.IBody
 * JD-Core Version:    0.7.0.1
 */