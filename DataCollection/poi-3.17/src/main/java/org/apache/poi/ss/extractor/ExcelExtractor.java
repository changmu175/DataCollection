package org.apache.poi.ss.extractor;

public abstract interface ExcelExtractor
{
  public abstract void setIncludeSheetNames(boolean paramBoolean);
  
  public abstract void setFormulasNotResults(boolean paramBoolean);
  
  public abstract void setIncludeHeadersFooters(boolean paramBoolean);
  
  public abstract void setIncludeCellComments(boolean paramBoolean);
  
  public abstract String getText();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.extractor.ExcelExtractor
 * JD-Core Version:    0.7.0.1
 */