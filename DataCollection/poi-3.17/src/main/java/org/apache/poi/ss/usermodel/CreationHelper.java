package org.apache.poi.ss.usermodel;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;

public abstract interface CreationHelper
{
  public abstract RichTextString createRichTextString(String paramString);
  
  public abstract DataFormat createDataFormat();
  
  public abstract Hyperlink createHyperlink(HyperlinkType paramHyperlinkType);
  
  public abstract FormulaEvaluator createFormulaEvaluator();
  
  public abstract ExtendedColor createExtendedColor();
  
  public abstract ClientAnchor createClientAnchor();
  
  public abstract AreaReference createAreaReference(String paramString);
  
  public abstract AreaReference createAreaReference(CellReference paramCellReference1, CellReference paramCellReference2);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.CreationHelper
 * JD-Core Version:    0.7.0.1
 */